package com.github.sdpsharelook.speechRecognition

interface RecognitionListener {
    fun onSuccess(s: String)
    fun onReady()
    fun onBegin()
    fun onEnd()
    fun onError()
}
