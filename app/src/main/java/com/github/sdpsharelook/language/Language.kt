package com.github.sdpsharelook.language

import android.content.Context
import com.github.sdpsharelook.speechRecognition.SpeechRecognizer
import com.github.sdpsharelook.textToSpeech.TextToSpeech
import com.google.mlkit.nl.translate.TranslateLanguage
import java.util.*

data class Language(val locale: Locale) {
    val displayName: String = locale.displayName
    val languageTag: String = locale.toLanguageTag()

    /**@param ctx [Context] : the context of the app
     * return [Int] : the id of the flag or 0 if it doesn't exists
     */
    fun flagId(ctx: Context) =
        ctx.resources.getIdentifier(
            languageTag,
            "raw",
            ctx.getPackageName()
        )


    companion object {
        fun forLanguageTag(tag: String) = Language(Locale.forLanguageTag(tag))
        val translatorAvailableLanguages: Set<Language> =
            TranslateLanguage.getAllLanguages().map {
                Language(Locale.forLanguageTag(it))
            }.toSet()


        fun ttsAvailableLanguages(tts: TextToSpeech): Set<Language> =
            tts.availableLanguages

        suspend fun srAvailableLanguages(sr: SpeechRecognizer) =
            sr.availableLanguages()

        val default by lazy { Language(Locale.getDefault()) }
    }

    val isAvailableForTranslator by lazy { translatorAvailableLanguages.contains(this) }
    fun isAvailableForTTS(tts: TextToSpeech) = tts.isLanguageAvailable(this)
    suspend fun isAvailableForSR(sr: SpeechRecognizer) = sr.availableLanguages().contains(this)
}