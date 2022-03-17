package com.github.sdpsharelook

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.github.sdpsharelook.translate.TranslateListener
import com.github.sdpsharelook.translate.Translator
import com.google.mlkit.nl.translate.TranslateLanguage


class TranslateActivity : AppCompatActivity() {
    // They array of all languages available took from TranslateLanguage.allLanguages()
    private val allLanguages = arrayOf("af", "sq", "ar", "be", "bg", "bn", "ca", "zh", "hr", "cs",
        "da", "nl", "en", "eo", "et", "fi", "fr", "gl", "ka", "de", "el", "gu", "ht", "he", "hi",
        "hu", "is", "id", "ga", "it", "ja", "kn", "ko", "lt", "lv", "mk", "mr", "ms", "mt", "no",
        "fa", "pl", "pt", "ro", "ru", "sk", "sl", "es", "sv", "sw", "tl", "ta", "te", "th","tr",
        "uk", "ur", "vi", "cy"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_translate)

        val sourceLangSelector = findViewById<Spinner>(R.id.sourceLangSelector)
        val targetLangSelector = findViewById<Spinner>(R.id.targetLangSelector)
        val sourceText = findViewById<TextView>(R.id.sourceText)
        val buttonSwitchLang = findViewById<Button>(R.id.buttonSwitchLang)

        // Filling spinners with available languages
        allLanguages.sort()
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item, allLanguages
        )
        sourceLangSelector.adapter = adapter
        targetLangSelector.adapter = adapter

        // On create, we set the source language to FR, and the target to EN
        sourceLangSelector.setSelection(allLanguages.indexOf(TranslateLanguage.FRENCH))
        targetLangSelector.setSelection(allLanguages.indexOf(TranslateLanguage.ENGLISH))

        sourceText.addTextChangedListener { afterTextChanged ->
            updateTranslation(afterTextChanged.toString())
        }

        // Dynamically update the translation on language source or target changed
        val spinnerOnItemSelected = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                if (sourceText.text.isNotEmpty())
                    updateTranslation(sourceText.text.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        sourceLangSelector.onItemSelectedListener = spinnerOnItemSelected
        targetLangSelector.onItemSelectedListener = spinnerOnItemSelected

        buttonSwitchLang.setOnClickListener {
            val temp = sourceLangSelector.selectedItemPosition
            sourceLangSelector.setSelection(targetLangSelector.selectedItemPosition)
            targetLangSelector.setSelection(temp)
        }
    }

    /** Call to update the text to translate and translate it.
     * @param textToTranslate : String | The text to translate.
     */
    private fun updateTranslation(textToTranslate : String) {
        val sourceLangSelector = findViewById<Spinner>(R.id.sourceLangSelector)
        val targetLangSelector = findViewById<Spinner>(R.id.targetLangSelector)
        val targetText = findViewById<TextView>(R.id.targetText)

        val t = Translator(allLanguages[sourceLangSelector.selectedItemPosition], allLanguages[targetLangSelector.selectedItemPosition]);

        t.translate(textToTranslate, object : TranslateListener {
            override fun onError(e: Exception) {
                throw e
            }

            override fun onTranslated(translatedText: String) {
                targetText.text = translatedText
            }
        })

        if (targetText.text.isEmpty())
            targetText.text = getString(R.string.translation_running)
    }
}
