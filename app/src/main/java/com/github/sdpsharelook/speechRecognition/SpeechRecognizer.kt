package com.github.sdpsharelook.speechRecognition

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener as GoogleRecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer as GoogleSpeechRecognizer
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.sdpsharelook.Utils
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class SpeechRecognizer(val activity: AppCompatActivity) {
    private var hasPermissions = false
    private val speechRecognizer = GoogleSpeechRecognizer.createSpeechRecognizer(activity)

    private val requestPermissionLauncher =
        activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                hasPermissions = true
            } else {
                errorPermission()
            }
        }


    fun cancel() = speechRecognizer.cancel()

    private fun errorPermission() =
        Utils.toast("Please give us the audio permission to use this feature", activity)

    /** start the permission asking procedure if needed
     */
    fun checkPermissions() = when {
        ContextCompat.checkSelfPermission(
            activity,
            android.Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED -> {

        }
        else -> {
            requestPermissionLauncher.launch(
                android.Manifest.permission.RECORD_AUDIO
            )
        }
    }

    /**
     * Create the intent to start the speech recognizer
     * @param language: the language to put in the intent (free form by default)
     */
    private fun createIntent(language: String? = null): Intent {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        language?.let {
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                it
            )
        } ?: intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)

        return intent
    }

    /** Create the recognition listener from the library
     */
    private fun createGoogleRecognitionListener(listener: RecognitionListener): GoogleRecognitionListener {
        return object : android.speech.RecognitionListener {
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

    /** Start the speech recognition
     * @param listener: the listener for callback when the result is ready
     * @param language: to eventually specify the language (free form by default)
     */
    fun recognizeSpeech(listener: RecognitionListener, language: String? = null) {
        val intent = createIntent(language)
        val googleListener = createGoogleRecognitionListener(listener)
        checkPermissions()
        speechRecognizer.setRecognitionListener(googleListener)
        speechRecognizer.startListening(intent)
    }
}