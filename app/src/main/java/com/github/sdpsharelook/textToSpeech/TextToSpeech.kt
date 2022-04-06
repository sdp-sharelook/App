package com.github.sdpsharelook.textToSpeech

import android.content.Context
import android.widget.*
import java.util.*
import android.speech.tts.TextToSpeech as GoogleTextToSpeech

class TextToSpeech(val ctx: Context) {
    private var tts: GoogleTextToSpeech? = null

    init {
        tts = GoogleTextToSpeech(ctx) {
            when (it) {
                GoogleTextToSpeech.SUCCESS -> {
                    tts?.setPitch(0.2f)
                    tts?.setSpeechRate(1.4f)
                    tts?.setLanguage(Locale.UK)
                }
                else -> {
                    Toast.makeText(ctx, "An error happened while creating the TextToSpeech object", Toast.LENGTH_SHORT)
                }
            }
        }
    }

    /*private fun putLanguagesInSpinner(spinner: Spinner?): Map<String, String> {
        val languages = tts?.availableLanguages ?: setOf()
        val nameToTag = languages.map { it.displayLanguage to it.toLanguageTag() }.toMap()
        val adapter: ArrayAdapter<String> =
            ArrayAdapter(ctx, android.R.layout.simple_spinner_dropdown_item,
                nameToTag.keys.toList().filter { it.trim() != "" }
                    ?: listOf("No language available"))
        spinner?.setAdapter(adapter)
        return nameToTag
    }*/

    /*private fun bindSpinnerLanguages(nameToTag: Map<String, String>?, spinner: Spinner?) {
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
    }*/

    /*    fun setupSpinnerLanguages(spinner: Spinner?) {
            bindSpinnerLanguages(putLanguagesInSpinner(spinner), spinner)
        }*/

    /*fun bindSeekBars(seekBarPitch: SeekBar?, seekBarSpeechRate: SeekBar?) {
        // spinner?.adapter = adapter
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
    }*/

    fun setLanguage(loc: Locale) = tts?.setLanguage(loc)


    fun speak(message: String) =
        tts?.speak(message, GoogleTextToSpeech.QUEUE_FLUSH, null, null)
            ?: Toast.makeText ( ctx,"The TextToSpeech object is null", Toast.LENGTH_SHORT).show()

}