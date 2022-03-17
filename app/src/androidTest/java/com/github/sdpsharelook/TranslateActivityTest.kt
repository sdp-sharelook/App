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
        Espresso.closeSoftKeyboard()
        Thread.sleep(1000)
        Espresso.onView(ViewMatchers.withId(R.id.targetText))
            .check(ViewAssertions.matches(ViewMatchers.withText("Hello.")))

        Espresso.onView(ViewMatchers.withId(R.id.sourceText))
            .perform(ViewActions.clearText())
            .perform(ViewActions.typeText("Hello."))
        Espresso.closeSoftKeyboard()
        Espresso.onView(ViewMatchers.withId(R.id.buttonSwitchLang))
            .perform(ViewActions.click())
        Thread.sleep(1000)
        Espresso.onView(ViewMatchers.withId(R.id.targetText))
            .check(ViewAssertions.matches(ViewMatchers.withText("Bonjour.")))

        Espresso.onView(ViewMatchers.withId(R.id.targetLangSelector))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withText("it")).perform(ViewActions.click())
        Thread.sleep(1000)
        Espresso.onView(ViewMatchers.withId(R.id.targetText))
            .check(ViewAssertions.matches(ViewMatchers.withText("Ciao.")))
    }
}
