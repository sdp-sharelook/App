package com.github.sdpsharelook.speechRecognition

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import androidx.activity.result.ActivityResultCaller
import com.github.sdpsharelook.language.Language
import com.github.sdpsharelook.permissions.PermissionManager
import java.util.*
import android.speech.RecognitionListener as GoogleRecognitionListener
import android.speech.SpeechRecognizer as GoogleSpeechRecognizer


class SpeechRecognizer(
    caller: ActivityResultCaller,
    private val context: Context,
    private val listener: RecognitionListener,
) {
    private val permissionManager = PermissionManager(Manifest.permission.RECORD_AUDIO, caller)

    /** Start the speech recognition
     * @param listener: the listener for callback when the result is ready
     */
    fun recognizeSpeech() {
        val intent = createIntent()
        val googleListener = createGoogleRecognitionListener(listener)
        permissionManager.grantPermission(context) {
            speechRecognizer.setRecognitionListener(googleListener)
            speechRecognizer.startListening(intent)
        }
    }

    /** Cancel the speech recognition (must call it before starting a new one)
     */
    fun cancel() = speechRecognizer.cancel()

    private var _language: Language = Language.auto
    var language: Language
        get() = _language
        set(value) {
            _language = value
        }

    private val speechRecognizer = GoogleSpeechRecognizer.createSpeechRecognizer(context)

    /**
     * Create the intent to start the speech recognizer
     */
    private fun createIntent(): Intent {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        _language.locale?.let {
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                it.toLanguageTag()
            )
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, it.toLanguageTag())
        } ?: run {
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
        }
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        return intent
    }

    /** Create the recognition listener from the library
     */
    private fun createGoogleRecognitionListener(listener: RecognitionListener): GoogleRecognitionListener =
        object : android.speech.RecognitionListener {
            override fun onReadyForSpeech(p0: Bundle?) = listener.onReady()

            override fun onBeginningOfSpeech() = listener.onBegin()

            override fun onRmsChanged(p0: Float) {}

            override fun onBufferReceived(results: ByteArray?) {}

            override fun onEndOfSpeech() = listener.onEnd()

            override fun onError(code: Int) = listener.onError()

            fun catchResults(results: Bundle?) =
                results?.getStringArrayList(GoogleSpeechRecognizer.RESULTS_RECOGNITION)?.let {
                    listener.onResults(it.joinToString(""))
                } ?: listener.onError()

            override fun onResults(results: Bundle?) = catchResults(results)

            override fun onPartialResults(results: Bundle?) = catchResults(results)

            override fun onEvent(p0: Int, p1: Bundle?) {}
        }


}