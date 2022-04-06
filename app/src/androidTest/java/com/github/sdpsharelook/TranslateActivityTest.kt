package com.github.sdpsharelook

import android.os.Bundle
import androidx.core.content.PermissionChecker
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.runner.permission.PermissionRequester
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
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
        launchFragmentInContainer<TranslateFragment>(Bundle(), R.style.Theme_Sherlook).onFragment {
            mIdlingResource = it.getIdlingResource()
            // To prove that the test fails, omit this call:
            IdlingRegistry.getInstance().register(mIdlingResource)
        }
    }

    private fun selectSourceLanguage(srcLang: String) {
        onView(withId(R.id.sourceLangSelector)).perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)), `is`(srcLang)))
            .perform(click())
    }

    private fun selectTargetLanguage(targetLang: String) {
        onView(withId(R.id.targetLangSelector)).perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)), `is`(targetLang)))
            .perform(click())
    }

    @Test
    @ExperimentalCoroutinesApi
    fun testTranslateActivity() = runTest {
        fun selectLanguage(lang: String, spinnerId: Int) {
            onView(withId(spinnerId)).perform(click())
            onData(allOf(`is`(instanceOf(String::class.java)), `is`(lang))).perform(click())
        }

        // simple translate
        selectLanguage("fr", R.id.sourceLangSelector)
        selectLanguage("en", R.id.targetLangSelector)
        onView(withId(R.id.sourceText))
            .perform(typeText("Bonjour."), closeSoftKeyboard())

        onView(withId(R.id.targetText)).check(matches(withText("Hello.")))
        // switch button change
        onView(withId(R.id.buttonSwitchLang)).perform(click())
   
        // change target lang
        selectLanguage("it", R.id.targetLangSelector)
        onView(withId(R.id.targetText)).check(matches(withText("Ciao.")))

        // speak
        onView(withId(R.id.imageButtonSpeak)).perform(click())
        delay(2000)

        // listen
        onView(withId(R.id.imageButtonListen)).perform(click())
        getInstrumentation().waitForIdleSync()
        delay(2000)

        val context = getInstrumentation().getTargetContext()
        PermissionChecker.checkCallingOrSelfPermission(
            context,
            android.Manifest.permission.RECORD_AUDIO
        )
        PermissionRequester().apply {
            addPermissions(android.Manifest.permission.RECORD_AUDIO)
            requestPermissions()
        }
        delay(1000)
    }

        
    @Test
    fun testSwitchSourceOrTargetLanguageMustRunTranslation() {
        selectSourceLanguage("fr")
        selectTargetLanguage("en")
        onView(withId(R.id.sourceText))
            .perform(typeText("Ciao."), closeSoftKeyboard())
        selectSourceLanguage("it")
        onView(withId(R.id.targetText)).check(matches(withText("Hello.")))
        selectTargetLanguage("fr")
        onView(withId(R.id.targetText)).check(matches(withText("Bonjour.")))
    }

    @Test
    fun testAutoDetectShouldBeFriendlyIfLanguageIsNotRecognized() {
        selectSourceLanguage("auto")
        selectTargetLanguage("en")
        onView(withId(R.id.sourceText))
            .perform(typeText("Bo"), closeSoftKeyboard())
        onView(withId(R.id.targetText)).check(matches(withText(R.string.unrecognized_source_language)))
    }

    @Test
    fun testAutoDetectShouldCorrectlyDetectSourceLanguage() {
        selectSourceLanguage("auto")
        selectTargetLanguage("en")
        onView(withId(R.id.sourceText))
            .perform(typeText("Bonjour."), closeSoftKeyboard())
        onView(withId(R.id.targetText)).check(matches(withText("Hello.")))
    }   
    @Test
    fun testSwitchButtonMustSwitchLanguagesAndRunTranslation() {
        selectSourceLanguage("fr")
        selectTargetLanguage("en")
        onView(withId(R.id.sourceText)).perform(clearText())
            .perform(typeText("Hello."), closeSoftKeyboard())
        onView(withId(R.id.buttonSwitchLang)).perform(click())
        onView(withId(R.id.sourceLangSelector)).check(matches(withSpinnerText("en")))
        onView(withId(R.id.targetLangSelector)).check(matches(withSpinnerText("fr")))
        onView(withId(R.id.targetText)).check(matches(withText("Bonjour.")))
    }

    @After
    fun unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource)
        }
    }
}
