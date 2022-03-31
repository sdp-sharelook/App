package com.github.sdpsharelook

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.annotation.Nullable
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.idling.CountingIdlingResource
import com.github.sdpsharelook.language.Language
import com.github.sdpsharelook.language.LanguageSelectionDialog
import com.github.sdpsharelook.speechRecognition.RecognitionListener
import com.github.sdpsharelook.speechRecognition.SpeechRecognizer
import com.github.sdpsharelook.textToSpeech.TextToSpeech
import com.github.sdpsharelook.translate.Translator
import kotlinx.coroutines.*


class TranslateActivity : AppCompatActivity() {
    private lateinit var targetTextView: TextView
    private lateinit var tts: TextToSpeech
    private lateinit var sourceLanguage: Language
    private lateinit var targetLanguage: Language
    private lateinit var speechRecognizer: SpeechRecognizer
    private var targetTextString: String? = null

    @Nullable
    private var mIdlingResource: CountingIdlingResource? = null
    private lateinit var sourceText: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_translate)
        targetTextView = findViewById(R.id.targetText)
        sourceText = findViewById(R.id.sourceText)
        setSource(Language.auto)
        setTarget(Language("en"))
        initTranslator()
        initTextToSpeech()
        initSpeechRecognizer()
        findViewById<Button>(R.id.buttonSourceLang).apply {
            setOnClickListener { selectLanguage(this) }
        }
        findViewById<Button>(R.id.buttonTargetLang).apply {
            setOnClickListener { selectLanguage(this) }
        }
        val ctx = this
        findViewById<ImageButton>(R.id.imageButtonHamburger).setOnClickListener {
            val intent = Intent(ctx, NavigationMenuActivity::class.java)
            ctx.startActivity(intent)
        }
    }

    private fun setSource(language: Language) {
        sourceLanguage = language
        val buttonSource = findViewById<Button>(R.id.buttonSourceLang)
        buttonSource.setText(language.displayName)
        // speechRecognizer.language = language
    }

    private fun setTarget(language: Language) {
        targetLanguage = language
        val buttonTarget = findViewById<Button>(R.id.buttonTargetLang)
        buttonTarget.setText(language.displayName)
        // speechRecognizer.language = language
    }


    private fun selectLanguage(button: Button) {
        val activity = this
        val buttonSource = findViewById<Button>(R.id.buttonSourceLang)
        val buttonTarget = findViewById<Button>(R.id.buttonTargetLang)
        CoroutineScope(Dispatchers.Main).launch {
            when (button) {
                buttonSource -> {
                    val availableLanguages =
                        Language.translatorAvailableLanguages.union(setOf(Language.auto))

                    LanguageSelectionDialog.selectLanguage(activity, availableLanguages)
                        ?.let { setSource(it) }

                }
                buttonTarget -> {
                    val availableLanguages = Language.translatorAvailableLanguages
                    LanguageSelectionDialog.selectLanguage(activity, availableLanguages)
                        ?.let { setTarget(it) }
                }
            }
            mIdlingResource?.increment()
            val scope = CoroutineScope(Dispatchers.IO)
            scope.launch {
                if (sourceText.text.isNotEmpty())
                    updateTranslation(sourceText.text.toString())
                else
                    mIdlingResource?.decrement()
            }
        }
    }

    private fun initTranslator() {
        val buttonSwitchLang = findViewById<ImageButton>(R.id.buttonSwitchLang)
        sourceText.addTextChangedListener { afterTextChanged ->
            mIdlingResource?.increment()
            val scope = CoroutineScope(Dispatchers.IO)
            scope.launch {
                updateTranslation(afterTextChanged.toString())
            }
        }
        buttonSwitchLang.setOnClickListener {
            when (sourceLanguage) {
                Language.auto ->
                    Toast.makeText(this, "Cannot switch language in auto mode", Toast.LENGTH_SHORT)
                        .show()
                else -> {
                    val sourceEditText = findViewById<EditText>(R.id.sourceText)
                    val tempsource = sourceEditText.text.toString()
                    sourceEditText.setText(targetTextString ?: "")
                    targetTextView.setText(tempsource)
                    val templanguage = sourceLanguage
                    setSource(targetLanguage)
                    setTarget(templanguage)
                    val scope = CoroutineScope(Dispatchers.IO)
                    scope.launch {
                        if (sourceText.text.isNotEmpty())
                            updateTranslation(sourceText.text.toString())
                        else
                            mIdlingResource?.decrement()
                    }
                }
            }
        }
    }

    private fun initTextToSpeech() {
        val ctx = this
        tts = TextToSpeech(this)

        findViewById<ImageButton>(R.id.imageButtonSpeak).setOnClickListener {
            tts.speak(targetTextView.text.toString())
        }
    }

    private fun initSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer(this)
        val ctx = this
        findViewById<ImageButton>(R.id.imageButtonListen).setOnClickListener {
            speechRecognizer.cancel()
            speechRecognizer.recognizeSpeech(object : RecognitionListener {
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

    /** Call to update the text to translate and translate it.
     * @param textToTranslate [String] | The text to translate.
     */
    private suspend fun updateTranslation(textToTranslate: String) {
        var sourceLang = sourceLanguage
        val destLang = targetLanguage
        if (sourceLang == Language.auto) {
            sourceLang = Language(Translator.detectLanguage(textToTranslate))
            if (!Language.translatorAvailableLanguages.contains(sourceLang)) {
                targetTextString = null
                targetTextView.text = getString(R.string.unrecognized_source_language)
                mIdlingResource?.decrement()
                return
            }
        }

        val t = Translator(sourceLang.tag, destLang.tag)
        targetTextString = null

        targetTextView.text = getString(R.string.translation_running)
        targetTextString = t.translate(textToTranslate)
        this.targetTextView.text = targetTextString
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
