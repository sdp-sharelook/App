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
import com.github.sdpsharelook.databinding.ActivityTranslateBinding
import com.github.sdpsharelook.speechRecognition.RecognitionListener
import com.github.sdpsharelook.speechRecognition.SpeechRecognizer
import com.github.sdpsharelook.textToSpeech.TextToSpeech
import com.github.sdpsharelook.translate.Translator
import com.google.mlkit.nl.translate.TranslateLanguage
import kotlinx.coroutines.*
import java.util.*


class TranslateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTranslateBinding
    private val allLanguages = TranslateLanguage.getAllLanguages().toMutableList()
    private lateinit var tts: TextToSpeech

    @Nullable
    private var mIdlingResource: CountingIdlingResource? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTranslateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fillAndInitializeSpinners()

        binding.sourceText.addTextChangedListener { afterTextChanged ->
            mIdlingResource?.increment()
            val scope = CoroutineScope(Dispatchers.IO)
            scope.launch {
                updateTranslation(afterTextChanged.toString())
            }
        }

        binding.buttonSwitchLang.setOnClickListener {
            val temp = binding.sourceLangSelector.selectedItemPosition
            binding.sourceLangSelector.setSelection(binding.targetLangSelector.selectedItemPosition)
            binding.targetLangSelector.setSelection(temp)
            val tempText = binding.sourceText.text
            binding.sourceText.setText(binding.targetText.text)
            binding.targetText.text = tempText
        }
        // speech recognition
        val sr = SpeechRecognizer(this)
        val ctx = this
        findViewById<ImageButton>(R.id.imageButtonListen).setOnClickListener {
            sr.cancel()
            sr.recognizeSpeech(object : RecognitionListener {
                override fun onResults(s: String) {
                    if (s.trim().isEmpty())
                        binding.sourceText.setText("...")

                    binding.sourceText.setText(s)
                }

                override fun onReady() {
                    binding.sourceText.isEnabled = false
                    binding.sourceText.setText("...")
                }

                override fun onBegin() {
                    binding.sourceText.isEnabled = false
                }

                override fun onEnd() {
                    binding.sourceText.isEnabled = true
                }

                override fun onError() {
                    Toast.makeText(ctx, "Error recognition", Toast.LENGTH_SHORT).show()
                    binding.sourceText.setText("")
                    binding.sourceText.isEnabled = true
                }
            })

        }

        // text to speech
        tts = TextToSpeech(ctx)

        findViewById<ImageButton>(R.id.imageButtonSpeak).setOnClickListener {
            // fixme replace "Bonjour" with sourceText.text.toString()
            tts.speak(binding.targetText.text.toString())
        }

        // hamburger menu
        findViewById<ImageButton>(R.id.imageButtonHamburger).setOnClickListener {
            val intent = Intent(ctx, NavigationMenuActivity::class.java)
            ctx.startActivity(intent)
        }

    }


    companion object {
        const val autoDetectName = "auto"
    }


    /** Filling spinners with available languages and initializing them. */
    private fun fillAndInitializeSpinners() {
        allLanguages.sort()
        allLanguages.add(0, autoDetectName)
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item, allLanguages
        )
        binding.sourceLangSelector.adapter = adapter
        binding.targetLangSelector.adapter = adapter

        // On create, we set the source language to FR, and the target to EN
        binding.sourceLangSelector.setSelection(allLanguages.indexOf(autoDetectName))
        binding.targetLangSelector.setSelection(allLanguages.indexOf(TranslateLanguage.ENGLISH))

        val sourceText = findViewById<TextView>(R.id.sourceText)
        // Dynamically update the translation on language source or target changed
        val spinnerOnItemSelected = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                tts.setLanguage(Locale.forLanguageTag(
                    allLanguages[binding.targetLangSelector.selectedItemPosition]))

                findViewById<ImageButton>(R.id.buttonSwitchLang).isEnabled =
                    allLanguages[binding.sourceLangSelector.selectedItemPosition] != autoDetectName

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

        binding.sourceLangSelector.onItemSelectedListener = spinnerOnItemSelected
        binding.targetLangSelector.onItemSelectedListener = spinnerOnItemSelected
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
