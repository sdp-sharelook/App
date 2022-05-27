package com.github.sdpsharelook.download

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.testing.WorkManagerTestInitHelper
import com.github.sdpsharelook.downloads.MLKitTranslatorDownloader
import com.github.sdpsharelook.language.Language
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith

private var initialized = false

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class MLKitTranslatorDownloaderTestRobo {
    private val NULL_LIST_MESSAGE =
        "MLKitTranslatorDownloader.downloadedLanguages() returned null"

    init {
        if (!initialized) {
            val ctx: Context = ApplicationProvider.getApplicationContext()
            WorkManagerTestInitHelper.initializeTestWorkManager(ctx)
            com.google.mlkit.common.MlKit.initialize(ctx)
            initialized = true
        }
    }

    @Test
    @ExperimentalCoroutinesApi
    fun `test english always in downloaded languages`() = runTest {
        advanceUntilIdle()
        val english = Language("en")
        MLKitTranslatorDownloader.downloadedLanguages()?.let {
            assert(english in it) { "$english language should always be in downloaded languages" }
        } ?: assert(false) { NULL_LIST_MESSAGE }
    }

    @Test
    @ExperimentalCoroutinesApi
    fun `test downloading a language`() = runTest {
        advanceUntilIdle()
        val language = Language("it")
        MLKitTranslatorDownloader.downloadLanguage(language)
        MLKitTranslatorDownloader.downloadedLanguages()?.let {
            assert(language in it) { "$language should be in downloaded languages after downloading it" }
        } ?: assert(false) { NULL_LIST_MESSAGE }
    }

    @Test
    @ExperimentalCoroutinesApi
    fun `test downloading a non-existent language`() = runTest {
        advanceUntilIdle()
        assert(!MLKitTranslatorDownloader.downloadLanguage(Language("hello"))) {
            "downloading a non-existent language should return false"
        }
    }

    @Test
    @ExperimentalCoroutinesApi
    fun `test downloading then deleting a language`() = runTest {
        advanceUntilIdle()
        val language = Language("it")
        assert(MLKitTranslatorDownloader.downloadLanguage(language)) { "downloading $language should work" }
        MLKitTranslatorDownloader.downloadedLanguages()?.let {
            assert(language in it) { "$language should be in downloaded languages after downloading it" }
        } ?: assert(false) { NULL_LIST_MESSAGE }
        assert(MLKitTranslatorDownloader.deleteLanguage(language)) { "deleting $language should work after downloading it" }
        MLKitTranslatorDownloader.downloadedLanguages()?.let {
            assert(language !in it) { "$language should not be in downloaded languages after removing it" }
        } ?: assert(false) { NULL_LIST_MESSAGE }
    }

    @Test
    @ExperimentalCoroutinesApi
    fun `test deleting a not downloaded language`() = runTest {
        advanceUntilIdle()
        val language = Language("hr")
        assert(!MLKitTranslatorDownloader.deleteLanguage(language)) {
            "deleting $language should work after downloading it"
        }
    }
}