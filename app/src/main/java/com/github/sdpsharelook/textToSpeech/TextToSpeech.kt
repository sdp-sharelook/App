package com.github.sdpsharelook.textToSpeech

import android.content.Context
import android.widget.*
import com.github.sdpsharelook.language.Language
import android.speech.tts.TextToSpeech as GoogleTextToSpeech

class TextToSpeech(private val ctx: Context) {
    private var tts: GoogleTextToSpeech? = null
    private var initialized = false

    init {
        tts = GoogleTextToSpeech(ctx) {
            when (it) {
                GoogleTextToSpeech.SUCCESS -> {
                    initialized = true
                    tts?.setPitch(pitch)
                    tts?.setSpeechRate(speechRate)
                    setTTSLanguage(language)
                }
                else -> {
                    Toast.makeText(
                        ctx,
                        "An error happened while creating the TextToSpeech object",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun setTTSLanguage(language: Language) =
        language.locale?.let {
            if (isLanguageAvailable(language))
                tts?.language = it
        }
            ?: Toast.makeText(
                ctx,
                "Language not available for text-to-speech ${language.displayName}(${language.tag})",
                Toast.LENGTH_SHORT
            ).show()


    private var _language = Language.default
    var language
        get() = _language
        set(value) {
            _language = value
            if (initialized) tts?.language = value.locale
        }
    private var _speechRate = .5f
    var speechRate
        get() = _speechRate
        set(value) {
            _speechRate = value
            if (initialized) tts?.setSpeechRate(value)
        }
    private var _pitch = .5f
    var pitch
        get() = _pitch
        set(value) {
            _pitch = value
            if (initialized) tts?.setPitch(value)
        }

    fun speak(message: String) =
        tts?.speak(message, GoogleTextToSpeech.QUEUE_FLUSH, null, null)
            ?: Toast.makeText(ctx, "The TextToSpeech object is null", Toast.LENGTH_SHORT).show()

    fun isLanguageAvailable(language: Language): Boolean =
        language.locale?.let { initialized && tts?.isLanguageAvailable(it) == GoogleTextToSpeech.LANG_AVAILABLE }
            ?: false
}