package com.github.sdpsharelook.textToSpeech

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.sdpsharelook.language.Language
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TextToSpeechTest {
    @Test
    fun testTTs() {
        TextToSpeech(InstrumentationRegistry.getInstrumentation().targetContext).apply {
            isLanguageAvailable(Language.default)
            language = language
            speechRate = speechRate
            pitch = pitch
            speak("test")
        }
    }
}