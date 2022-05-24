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
class MLKitTranslatorTest {

    @Test
    @ExperimentalCoroutinesApi
    fun translatorTestWithCoroutines() = runTest {
        //according to google, mlkit can only be tested on androidTest
        val translatedText = MLKitTranslator.translate("Bonjour.",TranslateLanguage.FRENCH, TranslateLanguage.ENGLISH)
        assertEquals("Hello.", translatedText)
    }
}