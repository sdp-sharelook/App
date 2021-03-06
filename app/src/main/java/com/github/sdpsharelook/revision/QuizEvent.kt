package com.github.sdpsharelook.revision

sealed class QuizEvent {
    object Ping: QuizEvent()
    object Continue: QuizEvent()
    object Started : QuizEvent()
    object RequestSections : QuizEvent()
    data class ClickEffortButton(val quality: Int): QuizEvent()
    data class StartQuiz(val length: Int) : QuizEvent()
}
