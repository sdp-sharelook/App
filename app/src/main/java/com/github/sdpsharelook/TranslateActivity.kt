package com.github.sdpsharelook

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.Nullable
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.idling.CountingIdlingResource
import com.github.sdpsharelook.language.Language
import com.github.sdpsharelook.speechRecognition.RecognitionListener
import com.github.sdpsharelook.speechRecognition.SpeechRecognizer
import com.github.sdpsharelook.textToSpeech.TextToSpeech
import com.github.sdpsharelook.translate.Translator
import com.google.mlkit.nl.translate.TranslateLanguage
import kotlinx.coroutines.*


class TranslateActivity : AppCompatActivity() {
    private val allLanguages = TranslateLanguage.getAllLanguages().toMutableList()
    private lateinit var targetText: TextView
    private lateinit var tts: TextToSpeech

    @Nullable
    private var mIdlingResource: CountingIdlingResource? = null
    private lateinit var sourceText: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_translate)
        targetText = findViewById(R.id.targetText)
        sourceText = findViewById(R.id.sourceText)
        initTranslator()
        initTextToSpeech()
        initSpeechRecognizer()
    }

    private fun initTranslator() {
        val sourceLangSelector = findViewById<Spinner>(R.id.sourceLangSelector)
        val targetLangSelector = findViewById<Spinner>(R.id.targetLangSelector)
        val buttonSwitchLang = findViewById<ImageButton>(R.id.buttonSwitchLang)
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
            val tempText = sourceText.text
            sourceText.text = targetText.text
            targetText.text = tempText
        }
    }

    private fun initTextToSpeech() {
        val ctx = this
        tts = TextToSpeech(this)

        findViewById<ImageButton>(R.id.imageButtonSpeak).setOnClickListener {
            tts.speak(targetText.text.toString())
        }
        // hamburger menu
        findViewById<ImageButton>(R.id.imageButtonHamburger).setOnClickListener {
            val intent = Intent(ctx, NavigationMenuActivity::class.java)
            ctx.startActivity(intent)
        }
    }

    private fun initSpeechRecognizer() {
        val ctx = this
        val sr = SpeechRecognizer(this)
        findViewById<ImageButton>(R.id.imageButtonListen).setOnClickListener {
            sr.cancel()
            sr.recognizeSpeech(object : RecognitionListener {
                override fun onResults(s: String) =
                    if (s.trim().isEmpty()) sourceText.setText("...")
                    else sourceText.setText(s)


                override fun onReady() {
                    sourceText.isEnabled = false
                    sourceText.setText("...")
                }

                override fun onBegin() {
                    sourceText.isEnabled = false
                }

                override fun onEnd() {
                    sourceText.isEnabled = true
                }

                override fun onError() {
                    Toast.makeText(ctx, "Error recognition", Toast.LENGTH_SHORT).show()
                    sourceText.setText("")
                    sourceText.isEnabled = true
                }
            })

        }
    }


    companion object {
        const val autoDetectName = "auto"
    }


    /** Filling spinners with available languages and initializing them.
     * @param sourceLangSelector [Spinner] | The source language spinner.
     * @param targetLangSelector [Spinner] | The target language spinner.
     */
    private fun fillAndInitializeSpinners(
        sourceLangSelector: Spinner,
        targetLangSelector: Spinner
    ) {
        allLanguages.sort()
        allLanguages.add(0, autoDetectName)
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item, allLanguages
        )
        sourceLangSelector.adapter = adapter
        targetLangSelector.adapter = adapter

        // On create, we set the source language to FR, and the target to EN
        sourceLangSelector.setSelection(allLanguages.indexOf(autoDetectName))
        targetLangSelector.setSelection(allLanguages.indexOf(TranslateLanguage.ENGLISH))

        val sourceText = findViewById<TextView>(R.id.sourceText)
        // Dynamically update the translation on language source or target changed
        val spinnerOnItemSelected = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                tts.language =
                    Language.forLanguageTag(allLanguages[targetLangSelector.selectedItemPosition])
                mIdlingResource?.increment()
                val scope = CoroutineScope(Dispatchers.IO)
                scope.launch {
                    if (sourceText.text.isNotEmpty())
                        updateTranslation(sourceText.text.toString())
                    else
                        mIdlingResource?.decrement()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        sourceLangSelector.onItemSelectedListener = spinnerOnItemSelected
        targetLangSelector.onItemSelectedListener = spinnerOnItemSelected
    }

    /** Call to update the text to translate and translate it.
     * @param textToTranslate [String] | The text to translate.
     */
    private suspend fun updateTranslation(textToTranslate: String) {
        val sourceLangSelector = findViewById<Spinner>(R.id.sourceLangSelector)
        val targetLangSelector = findViewById<Spinner>(R.id.targetLangSelector)
        val targetText = findViewById<TextView>(R.id.targetText)
        var sourceLang = allLanguages[sourceLangSelector.selectedItemPosition]
        val destLang = allLanguages[targetLangSelector.selectedItemPosition]

        if (sourceLang == autoDetectName) {
            sourceLang = Translator.detectLanguage(textToTranslate)

            if (!allLanguages.contains(sourceLang)) {
                targetText.text = getString(R.string.unrecognized_source_language)
                mIdlingResource?.decrement()
                return
            }
        }

        val t = Translator(sourceLang, destLang)

        targetText.text = getString(R.string.translation_running)
        targetText.setText(t.translate(textToTranslate))
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
