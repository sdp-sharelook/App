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
import com.github.sdpsharelook.Section.SectionActivity
import com.github.sdpsharelook.speechRecognition.RecognitionListener
import com.github.sdpsharelook.speechRecognition.SpeechRecognizer
import com.github.sdpsharelook.textToSpeech.TextToSpeech
import com.github.sdpsharelook.translate.Translator
import com.google.mlkit.nl.translate.TranslateLanguage
import kotlinx.coroutines.*


class TranslateActivity : AppCompatActivity() {
    private val allLanguages = TranslateLanguage.getAllLanguages().toMutableList()
    private lateinit var targetText: TextView

    @Nullable
    private var mIdlingResource: CountingIdlingResource? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_translate)
        targetText = findViewById(R.id.targetText)
        // original translator activity
        val sourceLangSelector = findViewById<Spinner>(R.id.sourceLangSelector)
        val targetLangSelector = findViewById<Spinner>(R.id.targetLangSelector)
        val sourceText = findViewById<TextView>(R.id.sourceText)
        val buttonSwitchLang = findViewById<ImageButton>(R.id.buttonSwitchLang)
        fillAndInitializeSpinners(sourceLangSelector, targetLangSelector)
        sourceText.addTextChangedListener { afterTextChanged ->
            mIdlingResource?.increment()
            val scope = CoroutineScope(Dispatchers.IO)
            val downloadingLanguagesView = findViewById<View>(R.id.view_downloading_languages)
            scope.launch {
                // FIXME hide and show downloadingLanguagesView doesn't work
                // downloadingLanguagesView.visibility = View.VISIBLE
                updateTranslation(afterTextChanged.toString())
                // downloadingLanguagesView.visibility = View.GONE
            }
        }
        buttonSwitchLang.setOnClickListener {
            val temp = sourceLangSelector.selectedItemPosition
            sourceLangSelector.setSelection(targetLangSelector.selectedItemPosition)
            targetLangSelector.setSelection(temp)
        }
        // speech recognition
        val sr = SpeechRecognizer(this)
        val ctx = this
        findViewById<ImageButton>(R.id.imageButtonListen).setOnClickListener {
            sr.cancel()
            sr.recognizeSpeech(object : RecognitionListener {
                override fun onResults(s: String) {
                    if (s.isEmpty())
                        sourceText.setText("...")

                    sourceText.setText(s)
                }

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
                    sourceText.setText("...")
                }
            })

        }
        // text to speech
        val tts = TextToSpeech(this)

        findViewById<ImageButton>(R.id.imageButtonSpeak).setOnClickListener {
            // fixme replace "Bonjour" with sourceText.text.toString()
            tts.speak(sourceText.text.toString())
        }
        // hamburger menu
        findViewById<ImageButton>(R.id.imageButtonHamburger).setOnClickListener {
            val intent = Intent(this, NavigationMenuActivity::class.java)
            startActivity(intent)
        }

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

    /** Call to update the text to translate and translate it.
     * @param textToTranslate [String] | The text to translate.
     */
    private suspend fun updateTranslation(textToTranslate: String) {
        val sourceLangSelector = findViewById<Spinner>(R.id.sourceLangSelector)
        val targetLangSelector = findViewById<Spinner>(R.id.targetLangSelector)

        val t = Translator(
            allLanguages[sourceLangSelector.selectedItemPosition],
            allLanguages[targetLangSelector.selectedItemPosition]
        )

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
