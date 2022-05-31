package com.github.sdpsharelook.revision

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdpsharelook.Word
import com.github.sdpsharelook.revision.SnackbarShowers.LAUNCH_QUIZ
import com.github.sdpsharelook.revision.UiEvent.Navigate
import com.github.sdpsharelook.section.Section
import com.github.sdpsharelook.storage.IRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@FlowPreview
@HiltViewModel
class RevisionQuizViewModel @Inject constructor(
    private val repo: IRepository<List<Word>>,
    private val app: Application
) : AndroidViewModel(app) {
    private var orphanRevisions: MutableMap<String, RevisionWord> =
        SRAlgo.loadRevWordsFromLocal(app.applicationContext)
            .associateBy { it.wordId }
            .toMutableMap()
    private val quizPairs: MutableMap<String, Pair<RevisionWord, Word>> = mutableMapOf()
    private var quizIterator: Iterator<Pair<RevisionWord, Word>>? = null
    private var launched: Boolean = false
    private var quizLength = -1
    private var _current: Pair<RevisionWord, Word>? = null
    private val _uiEvent = Channel<UiEvent>()
    private val sectionIds: MutableSet<String> = mutableSetOf()

    val uiEvent = _uiEvent.receiveAsFlow().shareIn(viewModelScope, SharingStarted.Lazily)
    val current: Word get() = _current?.second ?: Word()
    val size: Int
        get() = if (launched) quizLength else quizPairs.size

    init {
        sendUiEvent(UiEvent.UpdateBadge)
        viewModelScope.launch { collectWordsFromFlow(getWordFlow()) }
    }

    private fun getWordFlow(): Flow<List<Word>?> = repo.flowSection()
        .filter { it.isSuccess }
        .map { it.getOrThrow() }
        .filterNotNull()
        .flatMapMerge { list: List<Section> ->
            list.filter { it.id !in sectionIds }
                .onEach { sectionIds.add(it.id) }
                .map { repo.flow(it.id) }.merge()
        }
        .filter { it.isSuccess }
        .map { it.getOrThrow() }

    private suspend fun collectWordsFromFlow(wordFlow: Flow<List<Word>?>) =
        withContext(Dispatchers.Default) {
            wordFlow
                .filterNotNull()
                .collect { list: List<Word> ->
                    list.filter { it.uid !in quizPairs }
                        .onEach {
                            val revisionWord = orphanRevisions.remove(it.uid)
                            if (revisionWord != null) {
                                quizPairs[it.uid] = revisionWord to it
                            } else {
                                quizPairs[it.uid] =
                                    RevisionWord(it.uid, System.currentTimeMillis()) to it
                            }
                            sendUiEvent(UiEvent.UpdateBadge)
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
                if (quizPairs.isEmpty())
                    quizPairs[""] = RevisionWord("") to Word()
                startQuiz(1)
            }
        }
    }

    private fun handleEffortButton(quality: Int) {
        _current?.let {
            it.first.n += 1
            SRAlgo.calcNextReviewTime(it.first, quality)
            it.first.saveToStorage(app.applicationContext)
        }
        if (nextWord()) {
            sendUiEvent(UiEvent.NewWord)
        } else {
            sendUiEvent(Navigate(Routes.QUIZ_RESULTS))
            sendUiEvent(UiEvent.ShowSnackbar(LAUNCH_QUIZ, "Congratulations"))
        }
    }

    private fun startQuiz(length: Int) {
        if (length > quizPairs.size) {
            sendUiEvent(
                UiEvent.ShowSnackbar(
                    LAUNCH_QUIZ,
                    "Not enough words (${quizPairs.size})"
                )
            )
            return
        }
        if (length == 0) {
            sendUiEvent(
                UiEvent.ShowSnackbar(
                    LAUNCH_QUIZ,
                    "Can't start empty quiz (${quizPairs.size} words available)"
                )
            )
            return
        }
        launched = true
        quizLength = length
        quizIterator = quizPairs.values.take(quizLength).iterator()
        nextWord()
        sendUiEvent(Navigate(Routes.QUIZ))
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