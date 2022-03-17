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
class SpeechRecognitionActivityTest {
    @get:Rule
    var testRule = ActivityScenarioRule(SpeechRecognitionActivity::class.java)

    @Test
    fun testPermissionsAsking() {
        /* Just perform some actions to detect crashes */
        Espresso.onView(ViewMatchers.withId(R.id.button_start_speech_recognition)).perform(ViewActions.click())

    }

}