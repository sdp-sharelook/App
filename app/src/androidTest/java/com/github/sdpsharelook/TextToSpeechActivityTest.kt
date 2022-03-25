package com.github.sdpsharelook

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TextToSpeechActivityTest {
    @get:Rule
    var testRule = ActivityScenarioRule(TextToSpeechActivity::class.java)

    @Test
    fun testTextToSpeechActivity() {
        /* Just perform some actions to detect crashes */
        Espresso.onView(ViewMatchers.withId(R.id.seek_bar_pitch))
            .perform(ViewActions.swipeLeft())
        Espresso.onView(ViewMatchers.withId(R.id.seek_bar_speech_rate))
            .perform(ViewActions.swipeLeft())
        Espresso.onView(ViewMatchers.withId(R.id.edit_text_input_tts))
            .perform(ViewActions.typeText("TextToSpeech is a cool feature !"))
        Espresso.closeSoftKeyboard()
        Espresso.onView(ViewMatchers.withId(R.id.button_speak))
            .perform(ViewActions.click())

    }

}