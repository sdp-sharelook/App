package com.github.sdpsharelook

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.sdpsharelook.speechRecognition.RecognitionListener
import com.github.sdpsharelook.speechRecognition.SpeechRecognizer


class SpeechRecognitionActivity : AppCompatActivity() {

    private lateinit var textViewResult: TextView
    private lateinit var speechRecognizer: SpeechRecognizer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speech_recognition)
        textViewResult = findViewById<TextView>(R.id.text_view_speech_recognition_result)
        speechRecognizer = SpeechRecognizer(this)
    }


    fun startRecognition(view: View) {
        // https://medium.com/voice-tech-podcast/android-speech-to-text-tutorial-8f6fa71606ac
        speechRecognizer.recognizeSpeech(object : RecognitionListener {
            override fun onSuccess(s: String) {
                textViewResult.setText(s)
            }

            override fun onError() {
                textViewResult.setText("error")
            }
        })
    }
}