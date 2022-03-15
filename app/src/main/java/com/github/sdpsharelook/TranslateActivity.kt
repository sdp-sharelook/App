package com.github.sdpsharelook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import com.github.sdpsharelook.translate.TranslateListener
import com.github.sdpsharelook.translate.Translator
import com.google.mlkit.nl.translate.TranslateLanguage

class TranslateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_translate)
        val t = Translator(TranslateLanguage.FRENCH, TranslateLanguage.ENGLISH);

        val sourceText = findViewById<TextView>(R.id.sourceText)
        val targetText = findViewById<TextView>(R.id.targetText)

        sourceText.addTextChangedListener { afterTextChanged ->
            t.translate(afterTextChanged.toString(), object : TranslateListener {
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
}
