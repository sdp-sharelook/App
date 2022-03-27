package com.github.sdpsharelook.language

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.widget.ListView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.github.sdpsharelook.R
import com.github.sdpsharelook.speechRecognition.SpeechRecognizer
import com.github.sdpsharelook.textToSpeech.TextToSpeech

class LanguageSelectionDialog private constructor(
    private val activity: AppCompatActivity,
    private val sr : SpeechRecognizer?=null
) : Dialog(activity) {
    private val tts: TextToSpeech= TextToSpeech(activity)

    private fun search(text: String): LanguageAdapter =
        LanguageAdapter(
            activity,
            Language.translatorAvailableLanguages.filter {
                it.displayName.lowercase().contains(text.lowercase())
            }.toSet(),
            sr,
            tts
        )

    private fun buildDialog() {
        getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.dialog_language_selection)
        val dialog = this

        val listView = findViewById<ListView>(R.id.list_view_languages).apply {
            adapter = search("")
            setOnItemClickListener { adapterView, view, i, l ->
                dialog.dismiss()
                val language = adapter.getItem(i) as Language
                Toast.makeText(activity, language.toString(), Toast.LENGTH_SHORT).show()
            }
        }
        findViewById<EditText>(R.id.edit_text_search_language).let {
            it.addTextChangedListener { afterTextChanged ->
                listView.adapter = search(it.text.toString())
            }
        }
    }

    private fun selectLanguage() {
        buildDialog()
        show()
    }

    companion object {
        fun selectLanguage(
            activity: AppCompatActivity,
            sr : SpeechRecognizer?=null
        ) = LanguageSelectionDialog(activity, sr).selectLanguage()
    }
}