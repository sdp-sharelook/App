package com.github.sdpsharelook.translate

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.mlkit.nl.translate.TranslateLanguage
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
    fun translatorTest() {
        val t = Translator(TranslateLanguage.FRENCH, TranslateLanguage.ENGLISH);

        t.translate("Bonjour.", object : TranslateListener {
            override fun onError(e: Exception) {
                throw e
            }

            override fun onTranslated(translatedText: String) {
                assertEquals("Hello.", translatedText);
            }
        })

        Thread.sleep(10000); // Wait for the callback to be executed.
    }
}