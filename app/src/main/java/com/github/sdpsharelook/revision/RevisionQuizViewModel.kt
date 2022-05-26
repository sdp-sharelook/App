package com.github.sdpsharelook.revision

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdpsharelook.Word
import com.github.sdpsharelook.storage.IRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RevisionQuizViewModel @Inject constructor(
    private val repo: IRepository<List<Word>>,
    private val app: Application
) : AndroidViewModel(app) {
    init {
        viewModelScope.launch {
            repo.flow().collect { result ->
                result.onSuccess { list ->
                    list?.onEach {
                        wordsToQuiz += RevisionWord(it.uid, System.currentTimeMillis())
                    }
                }
            }
        }
    }

    private var wordsToQuiz: List<RevisionWord> = RevisionWord.read(app.applicationContext)
    private var indexIntoQuiz = -1
    private var quizLength = -1
    var current: RevisionWord = wordsToQuiz.first()
    private var launched: Boolean = false
    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: QuizEvent) {
        when (event) {
            is QuizEvent.Continue -> {
                assert(launched)
                sendUiEvent(UiEvent.ShowAnswer)
            }
            is QuizEvent.ClickEffortButton -> {
                SRAlgo.calcNextReviewTime(current, event.quality)
                nextWord()
                sendUiEvent(UiEvent.NewWord(TODO()))
            }
            is QuizEvent.StartQuiz -> {
                launched = true
                quizLength = event.length
                indexIntoQuiz = 0
                sendUiEvent(UiEvent.Navigate(Routes.QUIZ))
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

    private fun nextWord() {
        current.saveToStorage(app.applicationContext)
        current = wordsToQuiz[++indexIntoQuiz]
    }
}