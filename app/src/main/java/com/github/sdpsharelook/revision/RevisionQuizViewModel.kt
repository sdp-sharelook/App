package com.github.sdpsharelook.revision

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdpsharelook.Word
import com.github.sdpsharelook.revision.UiEvent.Navigate
import com.github.sdpsharelook.revision.UiEvent.NewWord
import com.github.sdpsharelook.storage.IRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RevisionQuizViewModel @Inject constructor(
    private val repo: IRepository<List<Word>>,
    private val app: Application
) : AndroidViewModel(app) {
    init {
        viewModelScope.launch(Dispatchers.IO) {
            repo.flow().collect { result ->
                result.onSuccess { list ->
                    list?.onEach {
                        wordsToQuiz += RevisionWord(it.uid, System.currentTimeMillis())
                    }
                }
                sendUiEvent(UiEvent.UpdateBadge)
            }
        }
    }

    private var wordsToQuiz: MutableList<RevisionWord> =
        SRAlgo.loadRevWordsFromLocal(app.applicationContext)
            .plus(RevisionWord("1"))
            .plus(RevisionWord("2"))
            .toMutableList()
    private var indexIntoQuiz = -1
    private var quizLength = -1
    private var _currentRevision: RevisionWord = wordsToQuiz.firstOrNull() ?: RevisionWord("")
        set(value) {
            _current = getWordFromRevision(value)
            field = value
        }
    private lateinit var _current: Word
    val current: Word get() = _current
    val size
        get() = wordsToQuiz.size

    private fun getWordFromRevision(revisionWord: RevisionWord): Word {
        // TODO: get words
        return Word(revisionWord.wordId)
    }

    private var launched: Boolean = false
    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow().shareIn(viewModelScope, SharingStarted.Lazily)

    fun onEvent(event: QuizEvent) {
        when (event) {
            is QuizEvent.Continue -> {
                assert(launched)
                sendUiEvent(UiEvent.ShowAnswer)
            }
            is QuizEvent.ClickEffortButton -> {
                _currentRevision.n += 1
                SRAlgo.calcNextReviewTime(_currentRevision, event.quality)
                sendUiEvent(nextWord())
            }
            is QuizEvent.StartQuiz -> startQuiz(event)
            QuizEvent.Ping -> sendUiEvent(UiEvent.UpdateBadge)
            is QuizEvent.Started -> launched = true
        }
    }

    private fun startQuiz(event: QuizEvent.StartQuiz) {
        if (event.length > wordsToQuiz.size) {
            sendUiEvent(
                UiEvent.ShowSnackbar(
                    "Not enough words (${wordsToQuiz.size})"
                )
            )
            return
        }
        quizLength = event.length
        indexIntoQuiz = 0
        _currentRevision = wordsToQuiz[indexIntoQuiz]
        sendUiEvent(Navigate(Routes.QUIZ))
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

    private fun nextWord(): UiEvent {
        _currentRevision.saveToStorage(app.applicationContext)
        indexIntoQuiz += 1
        return if (indexIntoQuiz < size) {
            _currentRevision = wordsToQuiz[indexIntoQuiz]
            NewWord
        } else {
            // quiz is finished
            Navigate(Routes.QUIZ_LAUNCH)
        }
    }
}