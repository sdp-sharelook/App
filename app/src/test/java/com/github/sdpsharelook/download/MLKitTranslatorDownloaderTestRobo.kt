package com.github.sdpsharelook.download

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdpsharelook.downloads.MLKitTranslatorDownloader
import com.github.sdpsharelook.language.Language
import com.github.sdpsharelook.translate.MLKitTranslator
import com.google.mlkit.common.model.RemoteModelManager
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MLKitTranslatorDownloaderTestRobo {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)


    private val NULL_LIST_MESSAGE =
        "MLKitTranslatorDownloader.downloadedLanguages() returned null"


    @Inject
    lateinit var translator: MLKitTranslator

    @Inject
    lateinit var modelManager: RemoteModelManager
    lateinit var translatorDownloader: MLKitTranslatorDownloader


    @Before
    fun setUp() {
        hiltRule.inject()
        translatorDownloader = MLKitTranslatorDownloader(translator, modelManager)
    }

    @Test
    @ExperimentalCoroutinesApi
    fun `test english always in downloaded languages`() = runTest {
        advanceUntilIdle()
        val english = Language("en")
        translatorDownloader.downloadedLanguages()?.let {
            assert(english in it) { "$english language should always be in downloaded languages" }
        } ?: assert(false) { NULL_LIST_MESSAGE }
    }

    @Test
    @ExperimentalCoroutinesApi
    fun `test downloading a language`() = runTest {
        advanceUntilIdle()
        val language = Language("it")
        translatorDownloader.downloadLanguage(language)
        translatorDownloader.downloadedLanguages()?.let {
            assert(language in it) { "$language should be in downloaded languages after downloading it" }
        } ?: assert(false) { NULL_LIST_MESSAGE }
    }

    @Test
    @ExperimentalCoroutinesApi
    fun `test downloading a non-existent language`() = runTest {
        advanceUntilIdle()
        assert(!translatorDownloader.downloadLanguage(Language("hello"))) {
            "downloading a non-existent language should return false"
        }
    }

    @Test
    @ExperimentalCoroutinesApi
    fun `test downloading then deleting a language`() = runTest {
        advanceUntilIdle()
        val language = Language("it")
        assert(translatorDownloader.downloadLanguage(language)) { "downloading $language should work" }
        translatorDownloader.downloadedLanguages()?.let {
            assert(language in it) { "$language should be in downloaded languages after downloading it" }
        } ?: assert(false) { NULL_LIST_MESSAGE }
        assert(translatorDownloader.deleteLanguage(language)) { "deleting $language should work after downloading it" }
        translatorDownloader.downloadedLanguages()?.let {
            assert(language !in it) { "$language should not be in downloaded languages after removing it" }
        } ?: assert(false) { NULL_LIST_MESSAGE }
    }

    @Test
    @ExperimentalCoroutinesApi
    fun `test deleting a not downloaded language`() = runTest {
        advanceUntilIdle()
        val language = Language("hr")
        assert(!translatorDownloader.deleteLanguage(language)) {
            "deleting $language should work after downloading it"
        }
    }
}