package com.github.sdpsharelook.translate

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.mlkit.nl.translate.TranslateLanguage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class TranslatorTest {
    @Test
    fun empty() {

    }
//    @Test
//    @ExperimentalCoroutinesApi
//    fun translatorTestWithCoroutines() = runTest {
//        val t = Translator(TranslateLanguage.FRENCH, TranslateLanguage.ENGLISH)
//        val translatedText = t.translate("Bonjour.")
//        assertEquals("Hello.", translatedText)
//    }
}