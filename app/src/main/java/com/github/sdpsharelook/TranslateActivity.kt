package com.github.sdpsharelook

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.Nullable
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.idling.CountingIdlingResource
import com.github.sdpsharelook.translate.Translator
import com.google.mlkit.nl.translate.TranslateLanguage
import kotlinx.coroutines.*


class TranslateActivity : AppCompatActivity() {
    private val allLanguages = TranslateLanguage.getAllLanguages().toMutableList()
    @Nullable
    private var mIdlingResource: CountingIdlingResource? = null

    /** Filling spinners with available languages and initializing them.
     * @param sourceLangSelector [Spinner] | The source language spinner.
     * @param targetLangSelector [Spinner] | The target language spinner.
     */
    private fun fillAndInitializeSpinners(sourceLangSelector : Spinner, targetLangSelector : Spinner) {
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

        val sourceText = findViewById<TextView>(R.id.sourceText)
        // Dynamically update the translation on language source or target changed
        val spinnerOnItemSelected = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                mIdlingResource?.increment()
                val scope = CoroutineScope(Dispatchers.IO)
                scope.launch {
                    if (sourceText.text.isNotEmpty())
                        updateTranslation(sourceText.text.toString())
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        sourceLangSelector.onItemSelectedListener = spinnerOnItemSelected
        targetLangSelector.onItemSelectedListener = spinnerOnItemSelected
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_translate)

        val sourceLangSelector = findViewById<Spinner>(R.id.sourceLangSelector)
        val targetLangSelector = findViewById<Spinner>(R.id.targetLangSelector)
        val sourceText = findViewById<TextView>(R.id.sourceText)
        val buttonSwitchLang = findViewById<Button>(R.id.buttonSwitchLang)

        fillAndInitializeSpinners(sourceLangSelector, targetLangSelector)

        sourceText.addTextChangedListener { afterTextChanged ->
            mIdlingResource?.increment()
            val scope = CoroutineScope(Dispatchers.IO)
            scope.launch {
                updateTranslation(afterTextChanged.toString())
            }
        }

        buttonSwitchLang.setOnClickListener {
            val temp = sourceLangSelector.selectedItemPosition
            sourceLangSelector.setSelection(targetLangSelector.selectedItemPosition)
            targetLangSelector.setSelection(temp)
        }
    }

    /** Call to update the text to translate and translate it.
     * @param textToTranslate [String] | The text to translate.
     */
    private suspend fun updateTranslation(textToTranslate : String) {
        val sourceLangSelector = findViewById<Spinner>(R.id.sourceLangSelector)
        val targetLangSelector = findViewById<Spinner>(R.id.targetLangSelector)
        val targetText = findViewById<TextView>(R.id.targetText)

        val t = Translator( allLanguages[sourceLangSelector.selectedItemPosition],
                            allLanguages[targetLangSelector.selectedItemPosition])

        targetText.text = getString(R.string.translation_running)
        targetText.text = t.translate(textToTranslate)
        mIdlingResource?.decrement()
    }

    /**
     * Only called from test, creates and returns a new [CountingIdlingResource].
     */
    @VisibleForTesting
    fun getIdlingResource(): IdlingResource {
        if (mIdlingResource == null) {
            mIdlingResource = CountingIdlingResource("Translation")
        }
        return mIdlingResource!!
    }
}
