package com.github.sdpsharelook.translate

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.mlkit.nl.translate.TranslateLanguage
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class TranslatorTestRobo {
    @Test
    @ExperimentalCoroutinesApi
    fun translatorTestWithCoroutines() = runTest {
        val t = Translator(TranslateLanguage.FRENCH, TranslateLanguage.ENGLISH)
        val translatedText = t.translate("Bonjour.")
        assertEquals("Hello.", translatedText)
    }
}