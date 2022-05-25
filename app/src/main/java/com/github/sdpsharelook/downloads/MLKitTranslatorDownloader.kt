package com.github.sdpsharelook.downloads

import com.github.sdpsharelook.language.Language
import com.github.sdpsharelook.translate.MLKitTranslator
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateRemoteModel
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MLKitTranslatorDownloader(
    var requireWifi: Boolean = true,
) {
    private val modelManager = RemoteModelManager.getInstance()

    suspend fun downloadedLanguages(): List<Language>? = suspendCoroutine { continuation ->
        modelManager.getDownloadedModels(TranslateRemoteModel::class.java)
            .addOnSuccessListener { models -> continuation.resume(models.map { Language(it.language) }) }
            .addOnFailureListener { continuation.resume(null) }
    }

    suspend fun deleteLanguage(language: Language): Boolean =
        if (language !in (downloadedLanguages() ?: listOf())) false
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

    suspend fun downloadLanguage(language: Language): Boolean =
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