package com.github.sdpsharelook.revision

sealed class UiEvent {
    object PopBackStack : UiEvent()
    data class Navigate(val route: String) : UiEvent()
    object NewWord : UiEvent()
    data class ShowSnackbar(
        val message: String,
        val action: String? = null
    ): UiEvent()
    object ShowAnswer : UiEvent()
    object UpdateBadge : UiEvent()
}

object Routes {
    const val QUIZ = "quizFragment"
}