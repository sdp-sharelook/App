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
import java.util.*
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

    private var wordsToQuiz: Queue<RevisionWord> = LinkedList(RevisionWord.read(app.applicationContext))
    var current: RevisionWord = wordsToQuiz.remove()
    private var launched: Boolean = false
    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: QuizEvent) {
        when (event) {
            is QuizEvent.Continue -> {
                TODO()
            }
            is QuizEvent.ClickEffortButton -> {
                SRAlgo.calcNextReviewTime(current, event.quality)
                nextWord()
                sendUiEvent(UiEvent.Navigate(Routes.QUIZ))
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

    private fun nextWord() {
        current.saveToStorage(app.applicationContext)
        current = wordsToQuiz.remove()
    }
}