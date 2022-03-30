package com.github.sdpsharelook.textToSpeech

import android.content.Context
import com.github.sdpsharelook.language.Language
import android.speech.tts.TextToSpeech as GoogleTextToSpeech

class TextToSpeech(ctx: Context) {
    var ttsCreated: Boolean = false

    private val tts: GoogleTextToSpeech = GoogleTextToSpeech(ctx) {
        when (it) {
            GoogleTextToSpeech.SUCCESS -> ttsCreated = true
            else -> ttsCreated = false
        }
    }

    val availableLanguages
        get() =
            if (ttsCreated) tts.availableLanguages.map { Language(it.toLanguageTag()) }.toSet()
            else setOf()

    fun isLanguageAvailable(language: Language) =
        tts.isLanguageAvailable(language.locale) == GoogleTextToSpeech.LANG_AVAILABLE

    private var _language: Language? = null
    var language
        get() = _language
        set(language) {
            _language = language
            language?.let {
                if (isLanguageAvailable(it) && ttsCreated)
                    tts.setLanguage(it.locale)
                else _language = null
            }
        }

    private var _speechRate: Float = 0.5f
    var speechRate
        get() = _speechRate
        set(speechRate) {
            _speechRate = speechRate
            if (ttsCreated)
                tts.setSpeechRate(speechRate)
        }

    private var _pitch: Float = 0.5f
    var pitch
        get() = _pitch
        set(pitch) {
            _pitch = pitch
            if (ttsCreated)
                tts.setPitch(pitch)
        }

    fun speak(message: String) {
        if (ttsCreated)
            tts.speak(message, GoogleTextToSpeech.QUEUE_FLUSH, null, null)
    }
}