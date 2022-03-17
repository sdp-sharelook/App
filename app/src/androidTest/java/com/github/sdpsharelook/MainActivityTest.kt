package com.github.sdpsharelook

import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @get:Rule
    var testRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testMainActivity() {
        Intents.init()
        val result = Intent()
        Espresso.onView(ViewMatchers.withId(R.id.mainGoButton))
            .perform(ViewActions.click())
        Intents.intended(IntentMatchers.toPackage("com.github.sdpsharelook"))
        Intents.release()
    }
}