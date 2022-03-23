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


    fun cancel() {
        speechRecognizer.cancel()
    }

    private fun errorPermission() {
        Utils.toast("Please give us the audio permission to use this feature", activity)
        // activity.finish()
    }

    private fun checkPermissions() {
        // https://developer.android.com/training/permissions/requesting#kotlin
        when {
            ContextCompat.checkSelfPermission(
                activity,
                android.Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
            }
            else -> {
                requestPermissionLauncher.launch(
                    android.Manifest.permission.RECORD_AUDIO
                )
            }
        }
    }

    private fun createIntent(language: String? = null): Intent {
        // https://medium.com/voice-tech-podcast/android-speech-to-text-tutorial-8f6fa71606ac
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        // set language; free form if null :
        // https://stackoverflow.com/questions/10538791/how-to-set-the-language-in-speech-recognition-on-android
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

    private fun createGoogleRecognitionListener(listener: RecognitionListener): GoogleRecognitionListener {
        return object : android.speech.RecognitionListener {
            override fun onReadyForSpeech(p0: Bundle?) = listener.onReady()

            override fun onBeginningOfSpeech() = listener.onBegin()


            override fun onRmsChanged(p0: Float) {}

            override fun onBufferReceived(results: ByteArray?) {}

            override fun onEndOfSpeech() = listener.onEnd()

            override fun onError(code: Int) = listener.onError()


            override fun onResults(results: Bundle?) {
                results?.getStringArrayList(GoogleSpeechRecognizer.RESULTS_RECOGNITION)?.let {
                    return listener.onResults(it.joinToString(""))
                }
                listener.onError()
            }

            override fun onPartialResults(results: Bundle?) {
                results?.getStringArrayList(GoogleSpeechRecognizer.RESULTS_RECOGNITION)?.let {
                    return listener.onResults(it.joinToString(""))
                }
                listener.onError()
            }

            override fun onEvent(p0: Int, p1: Bundle?) {}
        }
    }

    fun recognizeSpeech(listener: RecognitionListener, language: String? = null) {
        val intent = createIntent(language)
        val googleListener = createGoogleRecognitionListener(listener)
        checkPermissions()
        speechRecognizer.setRecognitionListener(googleListener)
        speechRecognizer.startListening(intent)
    }

    suspend fun recognizeSpeechCoroutine(language: String? = null): String =
        suspendCoroutine { cont ->
            checkPermissions()
            val listener = object : android.speech.RecognitionListener {
                override fun onReadyForSpeech(p0: Bundle?) {}

                override fun onBeginningOfSpeech() {}

                override fun onRmsChanged(p0: Float) {}

                override fun onBufferReceived(results: ByteArray?) {}

                override fun onEndOfSpeech() {}

                override fun onError(code: Int) {
                    cont.resumeWithException(Exception())
                }

                override fun onResults(results: Bundle?) {
                    results?.getStringArrayList(GoogleSpeechRecognizer.RESULTS_RECOGNITION)?.let {
                        cont.resume(it.joinToString(""))
                    }
                    cont.resumeWithException(Exception())
                }

                override fun onPartialResults(partialResults: Bundle?) {}

                override fun onEvent(p0: Int, p1: Bundle?) {}
            }
            speechRecognizer.setRecognitionListener(listener)
            speechRecognizer.startListening(createIntent(language))
        }
}