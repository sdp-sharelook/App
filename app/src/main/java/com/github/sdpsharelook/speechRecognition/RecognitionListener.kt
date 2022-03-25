package com.github.sdpsharelook.speechRecognition

interface RecognitionListener {
    fun onResults(s: String)
    fun onReady()
    fun onBegin()
    fun onEnd()
    fun onError()
}
