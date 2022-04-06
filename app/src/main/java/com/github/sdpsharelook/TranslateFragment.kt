package com.github.sdpsharelook

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.VisibleForTesting
import androidx.core.widget.addTextChangedListener
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.idling.CountingIdlingResource
import com.github.sdpsharelook.speechRecognition.RecognitionListener
import com.github.sdpsharelook.speechRecognition.SpeechRecognizer
import com.github.sdpsharelook.textToSpeech.TextToSpeech
import com.github.sdpsharelook.translate.Translator
import com.google.mlkit.nl.translate.TranslateLanguage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [TranslateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TranslateFragment : Fragment() {
    private val allLanguages = TranslateLanguage.getAllLanguages().toMutableList()

    private lateinit var targetText: TextView
    private lateinit var sourceLangSelector: Spinner
    private lateinit var targetLangSelector: Spinner
    private lateinit var sourceText: TextView
    private lateinit var buttonSwitchLang: ImageButton
    private lateinit var imageButtonListen: ImageButton
    private lateinit var imageButtonSpeak: ImageButton

    private lateinit var tts: TextToSpeech

    private var mIdlingResource: CountingIdlingResource? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        targetText = view.findViewById(R.id.targetText)
        sourceLangSelector = view.findViewById(R.id.sourceLangSelector)
        targetLangSelector = view.findViewById(R.id.targetLangSelector)
        sourceText = view.findViewById(R.id.sourceText)
        buttonSwitchLang = view.findViewById(R.id.buttonSwitchLang)
        imageButtonListen = view.findViewById(R.id.imageButtonListen)
        imageButtonSpeak = view.findViewById(R.id.imageButtonSpeak)

        fillAndInitializeSpinners()

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

        //initializeSpeechRecognition()
    }

    /** Filling spinners with available languages and initializing them. */
    private fun fillAndInitializeSpinners() {
        allLanguages.sort()
        allLanguages.add(0, autoDetectName)
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item, allLanguages
        )
        sourceLangSelector.adapter = adapter
        targetLangSelector.adapter = adapter

        // On create, we set the source language to FR, and the target to EN
        sourceLangSelector.setSelection(allLanguages.indexOf(autoDetectName))
        targetLangSelector.setSelection(allLanguages.indexOf(TranslateLanguage.ENGLISH))

        // Dynamically update the translation on language source or target changed
        val spinnerOnItemSelected = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                //tts.setLanguage(Locale.forLanguageTag(allLanguages[targetLangSelector.selectedItemPosition]))
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

    private fun initializeSpeechRecognition() {
        val sr = SpeechRecognizer(requireActivity())
        val ctx = requireActivity().applicationContext

        imageButtonListen.setOnClickListener {
            sr.cancel()
            sr.recognizeSpeech(object : RecognitionListener {
                override fun onResults(s: String) {
                    if (s.trim().isEmpty())
                        sourceText.text = "..."

                    sourceText.text = s
                }

                override fun onReady() {
                    sourceText.isEnabled = false
                    sourceText.text = "..."
                }

                override fun onBegin() {
                    sourceText.isEnabled = false
                }

                override fun onEnd() {
                    sourceText.isEnabled = true
                }

                override fun onError() {
                    Toast.makeText(ctx, "Error recognition", Toast.LENGTH_SHORT).show()
                    sourceText.text = ""
                    sourceText.isEnabled = true
                }
            })
        }

        tts = TextToSpeech(ctx)

        imageButtonSpeak.setOnClickListener {
            tts.speak(targetText.text.toString())
        }
    }

    /** Call to update the text to translate and translate it.
     * @param textToTranslate [String] | The text to translate.
     */
    private suspend fun updateTranslation(textToTranslate: String) {
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_translate, container, false)
    }

    companion object {
        const val autoDetectName = "auto"
        /**
         * Use this factory method to create a new instance of
         * this fragment.
         *
         * @return A new instance of fragment TranslateFragment.
         */
        @JvmStatic
        fun newInstance() =
            TranslateFragment()
    }
}