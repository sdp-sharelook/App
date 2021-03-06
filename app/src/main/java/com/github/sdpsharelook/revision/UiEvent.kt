package com.github.sdpsharelook.revision

sealed class UiEvent {
    object PopBackStack : UiEvent()
    data class Navigate(val route: String) : UiEvent()
    object NewWord : UiEvent()
    data class ShowSnackbar(
        val who: String,
        val message: String,
        val action: String? = null
    ): UiEvent()
    object ShowAnswer : UiEvent()
    object UpdateBadge : UiEvent()
}

object Routes {
    const val QUIZ_LAUNCHER = "quizLauncher"
    const val QUIZ = "quizFragment"
    const val QUIZ_RESULTS = "quizResults"
}

object SnackbarShowers{
    const val MAIN = "activityMain"
    const val LAUNCH_QUIZ = "quizLauncher"
    const val QUIZ = "quizFragment"
}