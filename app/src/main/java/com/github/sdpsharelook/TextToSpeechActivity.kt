package com.github.sdpsharelook

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.github.sdpsharelook.textToSpeech.TextToSpeech


class TextToSpeechActivity : AppCompatActivity() {
    private lateinit var tts: TextToSpeech
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_to_speech)
        // Create text-to-speech object
        tts = TextToSpeech(this)
        tts.setupSpinnerLanguages(findViewById(R.id.spinner_languages))
        tts.bindSeekBars(
            findViewById(R.id.seek_bar_pitch),
            findViewById(R.id.seek_bar_speech_rate)
        )
    }

    fun speak(@Suppress("UNUSED_PARAMETER")view: View) {
        val editText = findViewById<EditText>(R.id.edit_text_input_tts)
        val message = editText.text.toString()
        tts.speak(message)
    }
}