package com.github.sdpsharelook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.View
import com.github.sdpsharelook.Utils.Companion.toast
import java.util.*

class SpeechRecognitionActivity : AppCompatActivity() {
    lateinit var speechRecognizer: SpeechRecognizer
    private val ctx = this
    private val recognitionListener: RecognitionListener = object : RecognitionListener {
        override fun onReadyForSpeech(p0: Bundle?) {
            TODO("Not implemented yet")
        }

        override fun onBeginningOfSpeech() {
            TODO("Not implemented yet")
        }

        override fun onRmsChanged(p0: Float) {
            TODO("Not implemented yet")
        }

        override fun onBufferReceived(p0: ByteArray?) {
            TODO("Not implemented yet")
        }

        override fun onEndOfSpeech() {
            TODO("Not implemented yet")
        }

        override fun onError(p0: Int) {
            TODO("Not implemented yet")
        }

        override fun onResults(p0: Bundle?) {
            toast("onResults called !", ctx)
        }

        override fun onPartialResults(p0: Bundle?) {
            TODO("Not implemented yet")
        }

        override fun onEvent(p0: Int, p1: Bundle?) {
            TODO("Not implemented yet")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voice_recognition)
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        speechRecognizer.setRecognitionListener(recognitionListener)
    }

    fun startRecognition(view: View) {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);

        speechRecognizer.startListening(intent)

        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, 10)
        } else {
            toast("Unfortunately device not supported", this)
        }
    }
}