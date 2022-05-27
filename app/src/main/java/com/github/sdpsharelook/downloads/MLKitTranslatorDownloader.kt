package com.github.sdpsharelook.downloads

import com.github.sdpsharelook.language.Language
import com.github.sdpsharelook.translate.MLKitTranslator
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateRemoteModel
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object MLKitTranslatorDownloader : TranslatorDownloader {
    private val modelManager = RemoteModelManager.getInstance()

    override suspend fun downloadedLanguages(): List<Language>? = suspendCoroutine { continuation ->
        modelManager.getDownloadedModels(TranslateRemoteModel::class.java)
            .addOnSuccessListener { models -> continuation.resume(models.map { Language(it.language) }.sortedBy { it.displayName }) }
            .addOnFailureListener { continuation.resume(null) }
    }

    override suspend fun deleteLanguage(language: Language): Boolean =
        if (language == Language("en")) false
        else if (language !in (downloadedLanguages() ?: listOf())) false
        else suspendCoroutine { continuation ->
            val model = TranslateRemoteModel.Builder(language.tag).build()
            modelManager.deleteDownloadedModel(model)
                .addOnSuccessListener {
                    continuation.resume(true)
                }
                .addOnFailureListener {
                    continuation.resume(false)
                }
        }

    override suspend fun downloadLanguage(language: Language, requireWifi: Boolean): Boolean =
        downloadedLanguages()?.let { downloadedLanguages ->
            if (language !in MLKitTranslator.availableLanguages) false
            else if (language in downloadedLanguages) true
            else {
                val model = TranslateRemoteModel.Builder(language.tag).build()
                val conditionsBuilder = DownloadConditions.Builder()
                if (requireWifi) conditionsBuilder.requireWifi()
                return suspendCoroutine { continuation ->
                    modelManager.download(model, conditionsBuilder.build())
                        .addOnSuccessListener {
                            continuation.resume(true)
                        }
                        .addOnFailureListener {
                            continuation.resume(false)
                        }
                }
            }
        } ?: false


}