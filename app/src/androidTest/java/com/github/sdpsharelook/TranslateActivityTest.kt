package com.github.sdpsharelook

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TranslateActivityTest {
    @get:Rule
    var testRule = ActivityScenarioRule(TranslateActivity::class.java)

    @Test
    fun testTranslateActivity() {
        Espresso.onView(ViewMatchers.withId(R.id.sourceText))
            .perform(ViewActions.typeText("Bonjour."))
        Thread.sleep(10000);
        Espresso.onView(ViewMatchers.withId(R.id.targetText))
            .check(ViewAssertions.matches(ViewMatchers.withText("Hello.")))
    }
}
