package com.github.sdpsharelook.revision

import androidx.lifecycle.ViewModel
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
    private val repo: IRepository<List<Word>>
) : ViewModel() {
    init {
        val flow = repo.flow()
        viewModelScope.launch {
            flow.collect {
                it.onSuccess { list ->
                    wordsToQuiz = list?: emptyList()
                }
            }
        }
    }

    private lateinit var wordsToQuiz: List<Word>
    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: QuizEvent) {
        when (event) {
            is QuizEvent.Continue -> {

            }
            is QuizEvent.ClickEffortButton -> {

            }
        }
    }
}