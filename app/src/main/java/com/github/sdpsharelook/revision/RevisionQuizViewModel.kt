package com.github.sdpsharelook.revision

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.sdpsharelook.Word
import com.github.sdpsharelook.revision.SnackbarShowers.LAUNCH_QUIZ
import com.github.sdpsharelook.revision.UiEvent.Navigate
import com.github.sdpsharelook.section.Section
import com.github.sdpsharelook.storage.IRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import kotlin.collections.*

@FlowPreview
@HiltViewModel
class RevisionQuizViewModel @Inject constructor(
    private val wordRepo: IRepository<List<Word>>,
    private val sectionRepo: IRepository<List<Section>>,
    app: Application
) : AndroidViewModel(app) {
    private var orphanRevisions: MutableMap<String, RevisionWord> =
        SRAlgo.loadRevWordsFromLocal(getApplication())
            .associateBy { it.wordId }
            .toMutableMap()
    private val quizPairsBySection: MutableMap<Section, MutableList<Pair<RevisionWord, Word>>> =
        mutableMapOf()
    private val wordFlowsBySection: MutableMap<Section, Pair<Job, Flow<List<Word>>>> =
        mutableMapOf()
    private var quizIterator: Iterator<Pair<RevisionWord, Word>>? = null
    private var launched: Boolean = false
    private var quizLength = -1
    private var _current: Pair<RevisionWord, Word>? = null
    private val _uiEvent = Channel<UiEvent>()

    // general public shared values
    val uiEvent = _uiEvent.receiveAsFlow().shareIn(viewModelScope, SharingStarted.Lazily)
    val size: Int
        get() = if (launched) quizLength else quizPairsBySection.size

    // only for [RevisionQuizFragment]
    val current: Word get() = _current?.second ?: Word()

    // only for [LaunchQuizFragment]
    private val sectionSelects: MutableList<SectionSelect> = mutableListOf()
    private val _checkboxes: MutableLiveData<List<SectionSelect>> =
        MutableLiveData()
    val checkboxes: LiveData<List<SectionSelect>> get() = _checkboxes

    init {
        sendUiEvent(UiEvent.UpdateBadge)
        viewModelScope.launch {
            getSectionFlow().collectLatest { list ->
                val deletedSections = quizPairsBySection.keys.filterNot { it in list }
                handleDeletedSections(deletedSections)
                val newSections = list.filterNot { it in quizPairsBySection }
                handleNewSections(newSections)
            }
        }
    }

    private fun handleDeletedSections(deletedSections: List<Section>) {
        deletedSections.forEach { section ->
            sectionSelects.removeAll { it.section == section }
            _checkboxes.postValue(sectionSelects)

            quizPairsBySection.remove(section)!!.forEach { (rev, _) ->
                rev.saveToStorage(getApplication())
            }

            wordFlowsBySection.remove(section)!!.let { (job, _) ->
                job.cancel()
            }
        }
    }

    private fun handleNewSections(newSections: List<Section>) {
        newSections.forEach { section ->
            sectionSelects.add(SectionSelect(section))
            _checkboxes.postValue(sectionSelects)

            assert(section !in quizPairsBySection)
            quizPairsBySection[section] = mutableListOf()

            assert(section !in wordFlowsBySection)
            val flow = getWordFlow(section)
            val job = viewModelScope.launch {
                collectWordsFromFlow(flow, section)
            }
            wordFlowsBySection[section] = job to flow
        }
    }

    private fun getSectionFlow() =
        sectionRepo
            .flow()
            .filter { it.isSuccess }
            .map { it.getOrThrow() }
            .filterNotNull()

    private fun getWordFlow(section: Section): Flow<List<Word>> =
        wordRepo
            .flow(section.id)
            .filter { it.isSuccess }
            .map { it.getOrThrow() }
            .filterNotNull()


    private suspend fun collectWordsFromFlow(wordFlow: Flow<List<Word>>, section: Section) =
        withContext(Dispatchers.IO) {
            wordFlow.collectLatest { list: List<Word> ->
                val sectionPairs: MutableList<Pair<RevisionWord, Word>> =
                    quizPairsBySection[section]!!
                val currentSectionWords = sectionPairs.map { (_, word) -> word }
                val sectionSelect = sectionSelects.find { it.section == section }!!

                val deletedWords = currentSectionWords.filterNot { it in list }
                handleDeletedWords(deletedWords, sectionPairs, sectionSelect)

                val newWords = list.filterNot { it in currentSectionWords }
                handleNewWords(newWords, sectionPairs, sectionSelect)

                sendUiEvent(UiEvent.UpdateBadge)
            }
        }

    private fun handleNewWords(
        newWords: List<Word>,
        sectionPairs: MutableList<Pair<RevisionWord, Word>>,
        sectionSelect: SectionSelect
    ) {
        newWords.forEach {
            var revisionWord = orphanRevisions.remove(it.uid)
            if (revisionWord != null) {
                sectionPairs.add(revisionWord to it)
            } else { // Generate a new one
                revisionWord = RevisionWord(it.uid, System.currentTimeMillis())
                sectionPairs.add(revisionWord to it)
            }
            if (revisionWord.isTime())
                sectionSelect.wordsToReview += 1
        }
    }

    private fun handleDeletedWords(
        deletedWords: List<Word>,
        sectionPairs: MutableList<Pair<RevisionWord, Word>>,
        sectionSelect: SectionSelect
    ) {
        deletedWords.forEach {
            val deletedPair = sectionPairs
                .find { (_, word) -> word == it }
            deletedPair
                ?.let { (rev, _) ->
                    if (rev.isTime())
                        sectionSelect.wordsToReview -= 1
                    rev.saveToStorage(getApplication())
                    val remove = sectionPairs.remove(deletedPair)
                    assert(remove)
                }
        }
    }

    fun onEvent(event: QuizEvent) {
        when (event) {
            is QuizEvent.Continue -> {
                assert(launched)
                sendUiEvent(UiEvent.ShowAnswer)
            }
            is QuizEvent.ClickEffortButton -> handleEffortButton(event.quality)
            is QuizEvent.StartQuiz -> startQuiz(event.length)
            QuizEvent.Ping -> sendUiEvent(UiEvent.UpdateBadge)
            is QuizEvent.Started -> if (!launched) {
                // handle empty quiz launch
                if (quizPairsBySection.isEmpty()) {
                    val section = Section()
                    quizPairsBySection[section] = mutableListOf(RevisionWord("") to Word())
                    sectionSelects.add(SectionSelect(section, 1, true))
                }
                startQuiz(1)
            }
        }
    }

    private fun handleEffortButton(quality: Int) {
        _current?.let {
            it.first.n += 1
            SRAlgo.calcNextReviewTime(it.first, quality)
            it.first.saveToStorage(getApplication())
        }
        if (nextWord()) {
            sendUiEvent(UiEvent.NewWord)
        } else {
            sendUiEvent(Navigate(Routes.QUIZ_RESULTS))
            sendUiEvent(UiEvent.ShowSnackbar(LAUNCH_QUIZ, "Congratulations"))
        }
    }

    private fun startQuiz(length: Int) {
        if (lengthFails(length)) return
        launched = true
        quizLength = length
        quizIterator = quizPairsBySection
            .filterKeys { section ->
                _checkboxes.value.orEmpty().any { it.section == section && it.isChecked }
            } // select only checked-section words
            .flatMap { it.value } // get all the words
            .shuffled().take(quizLength) // make a random quiz of length quizlength
            .iterator()
        nextWord()
        sendUiEvent(Navigate(Routes.QUIZ))
    }

    private fun lengthFails(length: Int): Boolean {
        if (length > quizPairsBySection.size) {
            sendUiEvent(
                UiEvent.ShowSnackbar(
                    LAUNCH_QUIZ,
                    "Not enough words (${quizPairsBySection.size})"
                )
            )
            return true
        }
        if (length == 0) {
            sendUiEvent(
                UiEvent.ShowSnackbar(
                    LAUNCH_QUIZ,
                    "Can't start empty quiz (${quizPairsBySection.size} words available)"
                )
            )
            return true
        }
        return false
    }


    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

    /**
     * Iterates to next word and update full state of model
     *
     * @return success (false if state inconsistent)
     */
    private fun nextWord(): Boolean = launched && quizIterator != null &&
            quizIterator!!.let {
                if (!it.hasNext()) return@let false
                _current = quizIterator!!.next()
                quizLength -= 1
                true
            }
}