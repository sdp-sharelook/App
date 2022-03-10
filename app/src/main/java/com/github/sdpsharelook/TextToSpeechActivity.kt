package com.github.sdpsharelook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.SeekBar
import android.widget.Spinner
import java.util.*
import com.github.sdpsharelook.Utils.Companion.toast

class TextToSpeechActivity : AppCompatActivity() {
    private var tts: TextToSpeech? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_to_speech)
        tts = TextToSpeech(this) {
            when (it) {
                TextToSpeech.SUCCESS -> {
                    tts?.setPitch(0.2f)
                    tts?.setSpeechRate(1.4f)
                    tts?.setLanguage(Locale.UK)
                    putLanguagesInSpinner(tts?.availableLanguages ?: setOf())
                    bindSeekBars()
                }
                else -> {
                    toast("An error happened while creating the TextToSpeech object", this)
                }
            }
        }
    }

    private fun putLanguagesInSpinner(languages: Set<Locale>?) {
        val spinner: Spinner? = findViewById(R.id.spinner_languages)
        val adapter: ArrayAdapter<String> = ArrayAdapter(this, R.layout.activity_text_to_speech,
            languages?.toList()?.map { it.displayCountry } ?: listOf("No language available"))
        spinner?.setAdapter(adapter)
        // crash
        // spinner?.setAdapter(adapter)
    }

    private fun bindSeekBars() {
        // spinner?.adapter = adapter
        val seekBarPitch: SeekBar? = findViewById(R.id.seek_bar_pitch)
        val seekBarSpeechRate: SeekBar? = findViewById(R.id.seek_bar_speech_rate)

        val seekBarsListener = object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar?, i: Int, b: Boolean) {
                if (seekBar === seekBarPitch)
                    tts?.setPitch(i.toFloat() / 5)
                else if (seekBar == seekBarSpeechRate)
                    tts?.setSpeechRate(i.toFloat() / 5)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        }

        seekBarPitch?.setOnSeekBarChangeListener(seekBarsListener)
        seekBarSpeechRate?.setOnSeekBarChangeListener(seekBarsListener)
    }


    fun speak(view: View) {
        val editText = findViewById<EditText>(R.id.edit_text_input_tts)
        val name = editText.text.toString()
        tts?.speak(name, TextToSpeech.QUEUE_FLUSH, null, null)
            ?: toast("The TextToSpeech object is null", this)
    }
}