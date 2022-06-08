package com.github.sdpsharelook.translate

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.annotation.VisibleForTesting
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.idling.CountingIdlingResource
import com.github.sdpsharelook.R
import com.github.sdpsharelook.Word
import com.github.sdpsharelook.databinding.FragmentTranslateBinding
import com.github.sdpsharelook.downloads.TranslatorDownloader
import com.github.sdpsharelook.language.Language
import com.github.sdpsharelook.language.LanguageAdapter
import com.github.sdpsharelook.speechRecognition.RecognitionListener
import com.github.sdpsharelook.speechRecognition.SpeechRecognizer
import com.github.sdpsharelook.textToSpeech.TextToSpeech
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class TranslateFragment : TranslateFragmentLift()

open class TranslateFragmentLift : Fragment() {

    @Inject
    lateinit var translator: TranslationProvider

    @Inject
    lateinit var translatorDownloader: TranslatorDownloader

    /**
     * This property is only valid between onCreateView and onDestroyView.
     */
    private var _binding: FragmentTranslateBinding? = null
    private val binding get() = _binding!!
    private lateinit var textToSpeech: TextToSpeech
    private val sourceLanguage: Language
        get() = binding.spinnerSourceLang.selectedItem as Language? ?: Language.auto
    private val targetLanguage: Language
        get() = binding.spinnerTargetLang.selectedItem as Language? ?: Language("en")

    private var speechRecognizer: SpeechRecognizer? = null

    private lateinit var availableLanguages: List<Language>
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

        putLanguagesInSpinners()

        initTextToSpeech()
        initSpeechRecognizer()
        binding.captureImageButton.setOnClickListener {
            captureImage()
        }

        binding.addWordToSectionButton.setOnClickListener { addWordToSection() }
        val args: TranslateFragmentArgs by navArgs()
        binding.sourceText.setText(args.textDetected)
    }

    private val onSourceLanguageSelected = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
            parent: AdapterView<*>,
            view: View,
            position: Int,
            id: Long,
        ) {
            speechRecognizer?.language = sourceLanguage
            updateTranslation()
        }

        override fun onNothingSelected(p0: AdapterView<*>?) { /* do nothing */
        }
    }
    private val onTargetLanguageSelected = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
            parent: AdapterView<*>,
            view: View,
            position: Int,
            id: Long,
        ) {
            updateTranslation()
            textToSpeech.language = availableLanguages[position]
        }

        override fun onNothingSelected(p0: AdapterView<*>?) { /* do nothing */
        }
    }


    private fun putLanguagesInSpinners() {
        CoroutineScope(Dispatchers.IO).launch {
            availableLanguages =
                translatorDownloader.downloadedLanguages() ?: listOf(Language("en"))
            withContext(Dispatchers.Main) {
                initTranslator()
                binding.apply {
                    spinnerSourceLang.adapter =
                        LanguageAdapter(
                            requireContext(),
                            (listOf(Language.auto) + availableLanguages)
                        )
                    spinnerTargetLang.adapter =
                        LanguageAdapter(requireContext(), availableLanguages)
                    spinnerSourceLang.onItemSelectedListener = onSourceLanguageSelected
                    spinnerTargetLang.onItemSelectedListener = onTargetLanguageSelected
                }
            }

        }
    }


    private fun initTranslator() {
        binding.sourceText.addTextChangedListener { updateTranslation() }

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
                    binding.sourceText.setText(targetText)
                    binding.targetText.text = tempSource
                    val srcSelection = binding.spinnerSourceLang.selectedItemPosition
                    val dstSelection = binding.spinnerTargetLang.selectedItemPosition
                    // -1 and +1 are to realign with language.auto
                    binding.spinnerTargetLang.setSelection(srcSelection - 1)
                    binding.spinnerSourceLang.setSelection(dstSelection + 1)
                    if (binding.sourceText.text!!.isNotEmpty())
                        updateTranslation()
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
        speechRecognizer = SpeechRecognizer(this, requireContext(), recognitionListener)
        binding.imageButtonSR.setOnClickListener {
            speechRecognizer?.cancel()
            speechRecognizer?.recognizeSpeech()
        }
    }

    /** Call to update the text to translate and translate it.
     */
    private fun updateTranslation() {
        mIdlingResource?.increment()
        CoroutineScope(Dispatchers.IO).launch {
            val detectedLanguage =
                if (sourceLanguage == Language.auto && sourceText.length > 2)
                    Language(translator.detectLanguage(sourceText))
                else sourceLanguage

            if (detectedLanguage !in translator.availableLanguages) {
                targetText = getString(R.string.unrecognized_source_language)
                return@launch
            }
            if (!::availableLanguages.isInitialized ||
                detectedLanguage !in availableLanguages
            ) {
                targetText =
                    getString(R.string.need_to_download_source_language).format(detectedLanguage.tag)
                return@launch
            }
            targetText = getString(R.string.translation_running)
            targetText = translator.translate(
                sourceText, detectedLanguage.tag,
                targetLanguage.tag
            )
            mIdlingResource?.decrement()
        }
    }

    private fun addWordToSection() {

        if (sourceText.isNullOrEmpty() || targetText.isNullOrEmpty()) {
            Log.e("TRANSLATE", "TRYING TO TRANSLATE WORD WITHOUT SOURCE AND/OR TARGET TEXT")
            return
        }
        val action = TranslateFragmentDirections.actionMenuTranslateLinkToMenuSectionsLink(
            Json.encodeToString(
                Word(
                    uid = UUID.randomUUID().toString(),
                    source = sourceText.toString(),
                    target = targetText.toString()
                )
            )
        )
        findNavController().navigate(action)

    }

    private fun captureImage() {
        val action = TranslateFragmentDirections.actionMenuTranslateLinkToMenuCameraLink()
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