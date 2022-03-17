package com.github.sdpsharelook

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.sdpsharelook.Utils.Companion.toast
import java.util.*


class SpeechRecognitionActivity : AppCompatActivity() {

    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var textViewResult: TextView
    private val speechRecognizerIntent = createSRIntent()
    private val recognitionListener: RecognitionListener = object : RecognitionListener {
        //https://medium.com/voice-tech-podcast/android-speech-to-text-tutorial-8f6fa71606ac
        private val console: ArrayList<String> = ArrayList()
        private fun print(message: String) {
            console.add(message)
            val textViewContent = console.takeLast(10).joinToString("\n")
            textViewResult.setText(textViewContent)
        }

        override fun onReadyForSpeech(p0: Bundle?) {
            print("ready for speech")
        }

        override fun onBeginningOfSpeech() {
            print("begining of speech")
        }

        override fun onRmsChanged(p0: Float) {
            // print("rms changed by $p0")
        }

        override fun onBufferReceived(results: ByteArray?) {
            print("buffer received : ${results.toString()}")
        }

        override fun onEndOfSpeech() {
            print("end of speech")
        }

        override fun onError(p0: Int) {
            print("error $p0")

        }

        override fun onResults(results: Bundle?) {
            val data =
                results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.joinToString("")
                    ?: "null"
            print("result : \n\t$data")

        }

        override fun onPartialResults(partialResults: Bundle?) {
            print("partial results")
        }

        override fun onEvent(p0: Int, p1: Bundle?) {
            print("event $p0")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voice_recognition)
        askPermissions()
        textViewResult = findViewById<TextView>(R.id.text_view_speech_recognition_result)
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        speechRecognizer.setRecognitionListener(recognitionListener)
    }

    private fun askPermissions() {
        // https://developer.android.com/training/permissions/requesting#kotlin
        fun errorPermission() {
            toast("Please give us the audio permission to use this feature", this)
            finish()
        }

        val requestPermissionLauncher =
            registerForActivityResult(
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
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
            }
            shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO) -> {
                // In an educational UI, explain to the user why your app requires this
                // permission for a specific feature to behave as expected. In this UI,
                // include a "cancel" or "no thanks" button that allows the user to
                // continue using your app without granting the permission.

                requestPermissionLauncher.launch(
                    Manifest.permission.RECORD_AUDIO
                )
            }
            else -> {
                errorPermission()
            }
        }
    }

    private fun createSRIntent(): Intent {
        // https://medium.com/voice-tech-podcast/android-speech-to-text-tutorial-8f6fa71606ac
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        );
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        return intent
    }

    fun startRecognition(view: View) {
        // https://medium.com/voice-tech-podcast/android-speech-to-text-tutorial-8f6fa71606ac
        speechRecognizer.startListening(speechRecognizerIntent)
    }
}