package com.github.sdpsharelook.speechRecognition

interface RecognitionListener {
    fun onSuccess(s: String)
    fun onError()
}
