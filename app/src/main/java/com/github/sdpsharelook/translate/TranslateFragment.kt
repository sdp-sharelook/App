package com.github.sdpsharelook.translate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.VisibleForTesting
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.idling.CountingIdlingResource
import com.github.sdpsharelook.R
import com.github.sdpsharelook.section.SectionWord
import com.github.sdpsharelook.databinding.FragmentTranslateBinding
import com.github.sdpsharelook.downloads.MLKitTranslatorDownloader
import com.github.sdpsharelook.language.Language
import com.github.sdpsharelook.language.LanguageSelectionDialog
import com.github.sdpsharelook.speechRecognition.RecognitionListener
import com.github.sdpsharelook.textToSpeech.TextToSpeech
import com.google.mlkit.nl.translate.TranslateLanguage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TranslateFragment : TranslateFragmentLift()

open class TranslateFragmentLift : Fragment() {

    /**
     * This property is only valid between onCreateView and onDestroyView.
     */
    private val binding get() = _binding!!
    private var _binding: FragmentTranslateBinding? = null
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var sourceLanguage: Language
    private lateinit var targetLanguage: Language
    private lateinit var detectedLanguage: Language

    //private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var languageDownloader: MLKitTranslatorDownloader
    private val sourceText
        get() = binding.sourceText.text.toString()

    private var targetText: String
        get() = binding.targetText.text.toString()
        set(value) {
            binding.targetText.text = value
        }

    private var mIdlingResource: CountingIdlingResource? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTranslator()
        initTextToSpeech()
        initSpeechRecognizer()
        setSource(Language.auto)

        setTarget(Language("en"), true)
        binding.buttonSourceLang.apply {
            setOnClickListener { selectLanguage(this) }
        }
        binding.buttonTargetLang.apply {
            setOnClickListener { selectLanguage(this) }
        }

        binding.captureImageButton.setOnClickListener {
            captureImage()
        }

        binding.addWordToSectionButton.setOnClickListener { addWordToSection() }

        languageDownloader = MLKitTranslatorDownloader()
        val args: TranslateFragmentArgs by navArgs()
        binding.sourceText.setText(args.textDetected)
    }

    private fun setSource(language: Language) {
        sourceLanguage = language
        binding.buttonSourceLang.text = language.displayName
        //speechRecognizer.language = language
    }

    private fun setTarget(language: Language, forceEnableTTS: Boolean = false) {
        targetLanguage = language
        binding.buttonTargetLang.text = language.displayName
        textToSpeech.language = language
        binding.imageButtonTTS.isEnabled =
            forceEnableTTS || textToSpeech.isLanguageAvailable(language)
    }

    private fun selectLanguage(button: Button) {
        val translatorLanguages = when (button) {
            binding.buttonSourceLang -> MLKitTranslator.availableLanguages.union(setOf(Language.auto))
            binding.buttonTargetLang -> MLKitTranslator.availableLanguages
            else -> setOf()
        }
        CoroutineScope(Dispatchers.Main).launch {
            launchLanguageDialog(
                button,
                translatorLanguages,
                binding.buttonSourceLang,
                binding.buttonTargetLang
            )
            if (binding.sourceText.text!!.isNotEmpty())
                updateTranslation(binding.sourceText.text.toString())
        }
    }

    private suspend fun launchLanguageDialog(
        button: Button,
        translatorLanguages: Set<Language>,
        buttonSource: Button,
        buttonTarget: Button,
    ) {
        val ttsLanguages =
            translatorLanguages.filter { textToSpeech.isLanguageAvailable(it) }.toSet()
        LanguageSelectionDialog.selectLanguage(
            requireActivity(),
            translatorLanguages,
            translatorLanguages,
            ttsLanguages,
            translatorLanguages
        )?.let {
            when (button) {
                buttonSource -> setSource(it)
                buttonTarget -> setTarget(it)
            }
        }
    }

    private fun initTranslator() {
        binding.sourceText.addTextChangedListener { afterTextChanged ->
            updateTranslation(afterTextChanged.toString())
        }

        binding.buttonSwitchLang.setOnClickListener {
            when (sourceLanguage) {
                Language.auto ->
                    Toast.makeText(
                        requireContext(),
                        "Cannot switch language in auto mode",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                else -> {
                    val tempSource = binding.sourceText.text.toString()
                    binding.sourceText.setText(targetText ?: "")
                    binding.targetText.text = tempSource
                    val tempLanguage = sourceLanguage
                    setSource(targetLanguage)
                    setTarget(tempLanguage)
                    if (binding.sourceText.text!!.isNotEmpty())
                        updateTranslation(binding.sourceText.text.toString())
                }
            }
        }
    }

    private fun initTextToSpeech() {
        textToSpeech = TextToSpeech(requireContext())
        binding.imageButtonTTS.setOnClickListener {
            textToSpeech.speak(targetText)
        }
    }

    private val recognitionListener = object : RecognitionListener {
        override fun onResults(s: String) =
            if (s.trim().isEmpty()) binding.sourceText.setText("...")
            else binding.sourceText.setText(s)

        override fun onReady() {
            binding.sourceText.isEnabled = false
            binding.imageButtonSR.isEnabled = false
            binding.sourceText.setText("...")
        }

        override fun onBegin() {}

        override fun onEnd() {
            binding.sourceText.isEnabled = true
            binding.imageButtonSR.isEnabled = true
        }

        override fun onError() {
            Toast.makeText(requireContext(), "Error recognition", Toast.LENGTH_SHORT).show()
            binding.sourceText.setText("")
            onEnd()
        }
    }

    private fun initSpeechRecognizer() {
        binding.imageButtonSR.setOnClickListener {
            // speechRecognizer.cancel()
            // speechRecognizer.recognizeSpeech(recognitionListener)
        }
    }

    private var translatorLanguagesTag = TranslateLanguage.getAllLanguages().toSet()

    /** Call to update the text to translate and translate it.
     * @param textToTranslate [String] | The text to translate.
     */
    private fun updateTranslation(textToTranslate: String) {
        mIdlingResource?.increment()
        CoroutineScope(Dispatchers.IO).launch {
            detectedLanguage =
                if (sourceLanguage == Language.auto)
                    Language(MLKitTranslator.detectLanguage(textToTranslate))
                else sourceLanguage
            if (detectedLanguage !in MLKitTranslator.availableLanguages ||
                !languageDownloader.downloadLanguage(detectedLanguage)
            ) return@launch
            // println("source language recognized ${sourceLang.tag}")
            targetText = getString(R.string.translation_running)
            targetText = MLKitTranslator.translate(textToTranslate, detectedLanguage.tag,
                targetLanguage.tag)
            mIdlingResource?.decrement()
        }
    }

    private fun addWordToSection() {

        val action = TranslateFragmentDirections.actionMenuTranslateLinkToMenuSectionsLink(
            SectionWord(sourceText, targetText ?: "error", null)
        )
        findNavController().navigate(action)

    }

    private fun captureImage() {
        val action = TranslateFragmentDirections.actionMenuTranslateLinkToMenuSectionsLink()
        findNavController().navigate(action)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTranslateBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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