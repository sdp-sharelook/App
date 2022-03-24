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


class SpeechRecognizer(val activity: AppCompatActivity) {
    private val speechRecognizer = GoogleSpeechRecognizer.createSpeechRecognizer(activity)
    private val requestPermissionLauncher =
        activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your
                // app.
            } else {
                // Explain to the user that the feature is unavailable because the
                // features requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.
                errorPermission()
            }
        }


    init {
        askPermissions()
    }

    private fun errorPermission() {
        Utils.toast("Please give us the audio permission to use this feature", activity)
        activity.finish()
    }

    private fun askPermissions() {
        // https://developer.android.com/training/permissions/requesting#kotlin
        when {
            ContextCompat.checkSelfPermission(
                activity,
                android.Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
            }
            activity.shouldShowRequestPermissionRationale(android.Manifest.permission.RECORD_AUDIO) -> {
                // In an educational UI, explain to the user why your app requires this
                // permission for a specific feature to behave as expected. In this UI,
                // include a "cancel" or "no thanks" button that allows the user to
                // continue using your app without granting the permission.

                requestPermissionLauncher.launch(
                    android.Manifest.permission.RECORD_AUDIO
                )
            }
            else -> {
                errorPermission()
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
        return intent
    }

    private fun createGoogleRecognitionListener(listener: RecognitionListener): GoogleRecognitionListener {
        return object : android.speech.RecognitionListener {
            override fun onReadyForSpeech(p0: Bundle?) { listener.onReady() }

            override fun onBeginningOfSpeech() {listener.onBegin() }

            override fun onRmsChanged(p0: Float) {}

            override fun onBufferReceived(results: ByteArray?) {}

            override fun onEndOfSpeech() {listener.onEnd() }

            override fun onError(code: Int) { listener.onError() }
            override fun onResults(results: Bundle?) {
                results?.getStringArrayList(GoogleSpeechRecognizer.RESULTS_RECOGNITION)?.let {
                    return listener.onSuccess(it.joinToString(""))
                }
                return listener.onError()
            }
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(p0: Int, p1: Bundle?) {}
        }
    }

    fun recognizeSpeech(listener: RecognitionListener, language: String? = null) {
        val intent = createIntent(language)
        val googleListener = createGoogleRecognitionListener(listener)
        speechRecognizer.setRecognitionListener(googleListener)
        speechRecognizer.startListening(intent)
    }
}