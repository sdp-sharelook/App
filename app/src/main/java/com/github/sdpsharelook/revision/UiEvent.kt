package com.github.sdpsharelook.revision

import com.github.sdpsharelook.Word

sealed class UiEvent {
    object PopBackStack : UiEvent()
    data class Navigate(val route: String) : UiEvent()
    data class NewWord(val word: Word) : UiEvent()
    data class ShowSnackbar(
        val message: String,
        val action: String? = null
    ): UiEvent()
}

object Routes {
    const val QUIZ = "quizFragment"
}