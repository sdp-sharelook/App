package com.github.sdpsharelook.translate

import android.os.Bundle
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
import com.github.sdpsharelook.databinding.FragmentTranslateBinding
import com.github.sdpsharelook.downloads.MLKitTranslatorDownloader
import com.github.sdpsharelook.language.Language
import com.github.sdpsharelook.language.LanguageAdapter
import com.github.sdpsharelook.section.SectionWord
import com.github.sdpsharelook.speechRecognition.RecognitionListener
import com.github.sdpsharelook.speechRecognition.SpeechRecognizer
import com.github.sdpsharelook.textToSpeech.TextToSpeech
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class TranslateFragment : TranslateFragmentLift()

open class TranslateFragmentLift : Fragment() {

    /**
     * This property is only valid between onCreateView and onDestroyView.
     */
    private val binding get() = _binding!!
    private var _binding: FragmentTranslateBinding? = null
    private lateinit var textToSpeech: TextToSpeech
    private val sourceLanguage: Language
        get() = binding.spinnerSourceLang.selectedItem as Language? ?: Language.auto
    private val targetLanguage: Language
        get() = binding.spinnerTargetLang.selectedItem as Language? ?: Language("en")

    private lateinit var speechRecognizer: SpeechRecognizer

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
        initTranslator()
        initTextToSpeech()


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
            // speechRecognizer.language = availableLanguages[position]
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
            textToSpeech.language = availableLanguages[position]
        }

        override fun onNothingSelected(p0: AdapterView<*>?) { /* do nothing */
        }
    }

    override fun onResume() {
        super.onResume()
        initSpeechRecognizer()
    }

    private fun putLanguagesInSpinners() {
        CoroutineScope(Dispatchers.IO).launch {
            availableLanguages =
                MLKitTranslatorDownloader().downloadedLanguages() ?: listOf(Language("en"))
            withContext(Dispatchers.Main) {
                binding.apply {
                    spinnerSourceLang.adapter =
                        LanguageAdapter(requireContext(),
                            (listOf(Language.auto) + availableLanguages))
                    spinnerTargetLang.adapter =
                        LanguageAdapter(requireContext(), availableLanguages)
                    spinnerSourceLang.setOnItemSelectedListener(onSourceLanguageSelected)
                    spinnerTargetLang.setOnItemSelectedListener(onTargetLanguageSelected)
                }
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
                    val srcSelection = binding.spinnerSourceLang.selectedItemPosition
                    val dstSelection = binding.spinnerTargetLang.selectedItemPosition
                    // -1 and +1 are to realign with language.auto
                    binding.spinnerTargetLang.setSelection(srcSelection - 1)
                    binding.spinnerSourceLang.setSelection(dstSelection + 1)
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
        speechRecognizer = SpeechRecognizer(this, requireContext(), recognitionListener)
        binding.imageButtonSR.setOnClickListener {
            speechRecognizer.cancel()
            speechRecognizer.recognizeSpeech()
        }
    }

    /** Call to update the text to translate and translate it.
     * @param textToTranslate [String] | The text to translate.
     */
    private fun updateTranslation(textToTranslate: String) {
        mIdlingResource?.increment()
        CoroutineScope(Dispatchers.IO).launch {
            val detectedLanguage =
                if (sourceLanguage == Language.auto && sourceText.length > 2)
                    Language(MLKitTranslator.detectLanguage(textToTranslate))
                else sourceLanguage

            if (detectedLanguage !in MLKitTranslator.availableLanguages) {
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