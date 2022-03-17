package com.github.sdpsharelook.translate

import com.google.android.gms.tasks.Task
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.tasks.await

/**
 * @param src : String defined in TranslateLanguage class | source language
 * @param dst : String defined in TranslateLanguage class | destination language
 */
class Translator(src: String, dst: String) {

    private var translator: Translator

    init {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(src)
            .setTargetLanguage(dst)
            .build()
        translator = Translation.getClient(options)
    }

    private fun downloadModelIfNeeded(l: TranslateListener): Task<Void> {
        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()

        return translator.downloadModelIfNeeded(conditions)
            .addOnFailureListener { exception ->
                l.onError(exception)
            }
    }

    private fun downloadModelIfNeeded(): Task<Void> {
        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()
        return translator.downloadModelIfNeeded(conditions)
    }

    /** Translate the text from src language to dst language.
     * @param text : String | Text in src language to translate in dst language
     * @param l : TranslateListener |
     */
    fun translate(text: String, l: TranslateListener) {
        downloadModelIfNeeded(l)
            .addOnSuccessListener {
                translator.translate(text)
                    .addOnSuccessListener { translatedText ->
                        l.onTranslated(translatedText)
                    }
                    .addOnFailureListener { exception ->
                        l.onError(exception)
                    }
            }
    }

    /** Translate the text from src language to dst language using coroutines
     * @param text : String | Text in src language to translate in dst language
     * @return translationResult : String
     */
    suspend fun translate(text: String): String {
        downloadModelIfNeeded()
        return translator.translate(text).await()
    }
}