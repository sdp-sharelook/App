package com.github.sdpsharelook

import com.github.sdpsharelook.di.MLKitModule
import com.github.sdpsharelook.di.TranslationModule
import com.github.sdpsharelook.downloads.TranslatorDownloader
import com.github.sdpsharelook.language.Language
import com.github.sdpsharelook.translate.TranslationProvider
import com.google.android.gms.tasks.Tasks
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.languageid.LanguageIdentifier
import com.google.mlkit.nl.translate.TranslateRemoteModel
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.delay
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [TranslationModule::class, MLKitModule::class]
)
object FakeTranslationModule {
    @Provides
    @Singleton
    fun providesTranslationProvider() = object : TranslationProvider {
        override val availableLanguages: Set<Language>
            get() = setOf(Language("en"), Language("fr"))

        override suspend fun detectLanguage(text: String): String =
            "en"

        override suspend fun translate(text: String, src: String, dst: String): String =
            when (Triple(text, src, dst)) {
                Triple("bonjour", "fr", "en") -> "hello"
                else -> "unmatched pattern"
            }
    }

    @Provides
    @Singleton
    fun providesTranslationDownloader() = object : TranslatorDownloader {
        val available = setOf(Language("en"), Language("fr"))
        val downloaded = mutableSetOf(Language("en"))
        override suspend fun downloadedLanguages(): List<Language> =
            listOf(Language("en"))

        override suspend fun deleteLanguage(language: Language): Boolean {
            delay(100)
            return language in available && language != Language("en") && downloaded.remove(language)
        }

        override suspend fun downloadLanguage(language: Language, requireWifi: Boolean): Boolean {
            delay(100)
            return language in available && downloaded.add(language)
        }
    }

    @Provides
    @Singleton
    fun providesLanguageIdentifier(): LanguageIdentifier = mock()

    @Singleton
    @Provides
    fun provideFakeModelManager(): RemoteModelManager = mock {
        val translateRemoteModel = mock<TranslateRemoteModel> {
            on { language } doReturn "en"
        }
        on { getDownloadedModels(TranslateRemoteModel::class.java) } doReturn
                Tasks.forResult(setOf(
            translateRemoteModel))

        on { deleteDownloadedModel(translateRemoteModel) } doReturn Tasks.forResult(null)

        /*on { download(translateRemoteModel, TODO conditions) } doAnswer {
            Tasks.forResult(null)
        }*/
    }
}