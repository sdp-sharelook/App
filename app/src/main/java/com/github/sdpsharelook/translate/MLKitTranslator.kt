package com.github.sdpsharelook.translate

import com.github.sdpsharelook.language.Language
import com.google.android.gms.tasks.Task
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.tasks.await

/**
 * @param src : String defined in TranslateLanguage class | source language
 * @param dst : String defined in TranslateLanguage class | destination language
 */
class MLKitTranslator(src: String, dst: String) {

    private var translator: Translator

    init {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(src)
            .setTargetLanguage(dst)
            .build()
        translator = Translation.getClient(options)
    }

    private fun downloadModelIfNeeded(): Task<Void> {
        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()
        return translator.downloadModelIfNeeded(conditions)
    }

    /** Translate the text from src language to dst language using coroutines
     * @param text : String | Text in src language to translate in dst language
     * @return translationResult : String
     */
    suspend fun translate(text: String): String {
        downloadModelIfNeeded().await()
        return translator.translate(text).await()
    }

    companion object {
        suspend fun detectLanguage(text: String) : String =
            LanguageIdentification.getClient().identifyLanguage(text).await()

        val availableLanguages: Set<Language> =
            TranslateLanguage.getAllLanguages().map {
                Language(it)
            }.toSet()
    }
}