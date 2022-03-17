package com.github.sdpsharelook

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class TranslateActivityTest {
    private var mIdlingResource: IdlingResource? = null;

    @Before
    fun registerIdlingResource() {
        ActivityScenario.launch(TranslateActivity::class.java).onActivity { activity ->
            mIdlingResource = activity.getIdlingResource()
            // To prove that the test fails, omit this call:
            IdlingRegistry.getInstance().register(mIdlingResource)
        }
    }

    @Test
    @ExperimentalCoroutinesApi
    fun testTranslateActivity() = runTest {
        onView(withId(R.id.sourceText))
            .perform(typeText("Bonjour."), closeSoftKeyboard())
        onView(withId(R.id.targetText)).check(matches(withText("Hello.")))

        onView(withId(R.id.sourceText)).perform(clearText())
            .perform(typeText("Hello."), closeSoftKeyboard())
        onView(withId(R.id.buttonSwitchLang)).perform(click())
        onView(withId(R.id.targetText)).check(matches(withText("Bonjour.")))

        onView(withId(R.id.targetLangSelector)).perform(click())
        onView(withText("it")).perform(click())
        onView(withId(R.id.targetText)).check(matches(withText("Ciao.")))
    }

    @After
    fun unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource)
        }
    }
}
