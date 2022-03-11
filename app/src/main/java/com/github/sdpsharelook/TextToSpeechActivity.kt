package com.github.sdpsharelook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.*
import java.util.*

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
                    val mapToTag = putLanguagesInSpinner(tts?.availableLanguages ?: setOf())
                    bindSpinnerLanguages(mapToTag)
                    bindSeekBars()
                }
                else -> {
                    Toast.makeText(this, "An error happened while creating the TextToSpeech object", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun putLanguagesInSpinner(languages: Set<Locale>?): Map<String, String>? {
        val spinner: Spinner? = findViewById(R.id.spinner_languages)
        val nameToTag = languages?.map { it.displayLanguage to it.toLanguageTag() }?.toMap()
        val adapter: ArrayAdapter<String> =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,
                nameToTag?.keys?.toList()?.filter { it.trim() != "" }
                    ?: listOf("No language available"))
        spinner?.setAdapter(adapter)
        return nameToTag
    }

    private fun bindSpinnerLanguages(nameToTag: Map<String, String>?) {
        val spinner: Spinner? = findViewById(R.id.spinner_languages)
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val name = parent.getItemAtPosition(position).toString()
                nameToTag?.get(name)?.let {
                    val selectedLanguage = Locale.forLanguageTag(it)
                    tts?.setLanguage(selectedLanguage)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
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
            ?: Toast.makeText(this, "The TextToSpeech object is null", Toast.LENGTH_SHORT).show()
    }
}