package com.github.sdpsharelook

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.Matchers.*
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
        fun selectLanguage(lang: String, spinnerId: Int) {
            onView(withId(spinnerId)).perform(click())
            onData(allOf(`is`(instanceOf(String::class.java)), `is`(lang))).perform(click())
        }

        selectLanguage("fr", R.id.sourceLangSelector)
        selectLanguage("en", R.id.targetLangSelector)
        onView(withId(R.id.sourceText))
            .perform(typeText("Bonjour."), closeSoftKeyboard())

        onView(withId(R.id.targetText)).check(matches(withText("Hello.")))

        onView(withId(R.id.buttonSwitchLang)).perform(click())
        onView(withId(R.id.targetText)).check(matches(withText("Bonjour.")))

        selectLanguage("it", R.id.targetLangSelector)
        onView(withId(R.id.targetText)).check(matches(withText("Ciao.")))
        onView(withId(R.id.imageButtonHamburger)).perform(click())
        onView(withId(R.id.button_back)).perform(click())
    }
    @After
    fun unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource)
        }
    }
}
