package com.github.sdpsharelook.revision

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdpsharelook.Word
import com.github.sdpsharelook.storage.IRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RevisionQuizViewModel @Inject constructor(
    private val repo: IRepository<List<Word>>,
    app: Application
) : AndroidViewModel(app) {
    private var orphanRevisions: MutableMap<String, RevisionWord>
    private val quizPairs: MutableMap<String, Pair<RevisionWord, Word>> = mutableMapOf()
    private var quizIterator: Iterator<Pair<RevisionWord, Word>>? = null
    private var launched: Boolean = false
    private var quizLength = -1
    private var _current: Pair<RevisionWord, Word>? = null
        set(value) {
            if (value != null) {
                current = value.second
            }
            field = value
        }
    private val _uiEvent = Channel<UiEvent>()

    val uiEvent = _uiEvent.receiveAsFlow()
    var current: Word? = null
    val size: Int
        get() = if (launched) quizLength else orphanRevisions.size

    init {
        orphanRevisions =
            SRAlgo.loadRevWordsFromLocal(app.applicationContext)
                .associateBy { it.wordId }
                .toMutableMap()
        sendUiEvent(UiEvent.UpdateBadge)
        viewModelScope.launch { collectWordsFromFlow(getWordFlow()) }
    }

    private fun getWordFlow(): Flow<List<Word>?> {
        repo.flowSection().onEach { TODO() }
        val flow = repo.flow()
        return flow.map { it.getOrNull() }
    }

    private suspend fun collectWordsFromFlow(wordFlow: Flow<List<Word>?>) =
        withContext(Dispatchers.Default) {
            wordFlow.collect { list ->
                list?.onEach {
                    val revisionWord = orphanRevisions.remove(it.uid)
                    if (revisionWord != null) {
                        quizPairs[it.uid] = Pair(revisionWord, it)
                    } else {
                        quizPairs[it.uid] =
                            Pair(RevisionWord(it.uid, System.currentTimeMillis()), it)
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
            is QuizEvent.ClickEffortButton -> {
                SRAlgo.calcNextReviewTime(_current!!.first, event.quality)
                if (nextWord()) {
                    sendUiEvent(UiEvent.NewWord)
                } else {
                    sendUiEvent(UiEvent.Navigate(Routes.QUIZ_RESULTS))
                    sendUiEvent(UiEvent.ShowSnackbar("Congratulations"))
                }
            }
            is QuizEvent.StartQuiz -> {
                if (event.length > quizPairs.size) {
                    sendUiEvent(
                        UiEvent.ShowSnackbar(
                            "Not enough words (${orphanRevisions.size})"
                        )
                    )
                    return
                }
                launched = true
                quizLength = event.length
                quizIterator = quizPairs.values.take(quizLength).iterator()
                nextWord()
                sendUiEvent(UiEvent.Navigate(Routes.QUIZ))
            }
            QuizEvent.Ping -> sendUiEvent(UiEvent.UpdateBadge)
        }
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
            quizIterator?.let {
                val hadNext = it.hasNext()
                _current = quizIterator?.next()
                hadNext
            } ?: false
}