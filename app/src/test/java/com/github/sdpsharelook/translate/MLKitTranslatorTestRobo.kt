package com.github.sdpsharelook.translate

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class MLKitTranslatorTestRobo {
    @Test
    @ExperimentalCoroutinesApi
    fun translatorTestWithCoroutines() = runTest {
        //according to google, mlkit can only be tested on androidTest
//        val t = MLKitTranslator(TranslateLanguage.FRENCH, TranslateLanguage.ENGLISH)
//        val translatedText = t.translate("Bonjour.")
//        assertEquals("Hello.", translatedText)
    }
}