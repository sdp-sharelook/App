package com.github.sdpsharelook.language

import android.content.Context
import com.github.sdpsharelook.R
import com.github.sdpsharelook.speechRecognition.SpeechRecognizer
import com.github.sdpsharelook.textToSpeech.TextToSpeech
import com.google.mlkit.nl.translate.TranslateLanguage
import java.util.*

data class Language(
    val tag: String
) {
    val locale: Locale? =
        when (tag) {
            AUTO_TAG -> null
            else -> Locale.forLanguageTag(tag)
        }
    val displayName: String = locale?.displayName ?: AUTO_TAG

    /**@param ctx [Context] : the context of the app
     * return [Int] : the id of the flag or 0 if it doesn't exists
     */
    fun flagId(ctx: Context): Int? {
        val flagId = when (tag) {
            AUTO_TAG -> R.drawable.ic_auto
            else -> {
                ctx.resources.getIdentifier(
                    tag,
                    "raw",
                    ctx.getPackageName()
                )
            }
        }
        return when (flagId) {
            0 -> null
            else -> flagId
        }
    }
    companion object {
        val AUTO_TAG: String = "auto"
        val default by lazy { Language(Locale.getDefault().toLanguageTag()) }
        val auto = Language(AUTO_TAG)
    }
}