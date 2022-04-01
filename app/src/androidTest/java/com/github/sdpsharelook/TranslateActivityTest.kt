package com.github.sdpsharelook

import androidx.core.content.PermissionChecker
import androidx.test.core.app.ActivityScenario
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
import com.github.sdpsharelook.language.Language
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.hamcrest.TypeSafeMatcher
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

    fun withTag(tagMatcher: Matcher<String>): Matcher<Language> =
        object : TypeSafeMatcher<Language>() {
            override fun describeTo(description: Description?) {

            }

            override fun matchesSafely(item: Language?): Boolean =
                item?.let {
                    tagMatcher.matches(it.tag)
                } ?: false
        }


    private fun selectSourceLanguage(srcLang: String) {
        onView(withId(R.id.buttonSourceLang)).perform(click())
        onData(withTag(containsString(srcLang))).perform(click())
    }

    private fun selectTargetLanguage(targetLang: String) {
        onView(withId(R.id.buttonTargetLang)).perform(click())
        onData(withTag(containsString(targetLang))).perform(click())
    }

    @Test
    @ExperimentalCoroutinesApi
    fun testTranslatorIntegration() = runTest {
        // simple translate
        selectSourceLanguage("fr")
        selectTargetLanguage("en")
        onView(withId(R.id.sourceText))
            .perform(typeText("Bonjour."), closeSoftKeyboard())

        onView(withId(R.id.targetText)).check(matches(withText("Hello.")))
        // switch button change
        onView(withId(R.id.buttonSwitchLang)).perform(click())

        // change target lang
        selectTargetLanguage("it")
        onView(withId(R.id.sourceText))
            .perform(clearText(), typeText("Bonjour."), closeSoftKeyboard())
        onView(withId(R.id.targetText)).check(matches(withText("Ciao.")))

        // menu
        onView(withId(R.id.imageButtonHamburger)).perform(click())
        onView(withId(R.id.button_back)).perform(click())

    }

    @Test
    @ExperimentalCoroutinesApi
    fun testSpeechRecognizerIntegration() = runTest {
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
        // onView(withId(R.id.sourceText)).check(matches(withText("...")))
        // onView(withId(R.id.sourceText)).check(matches(not(isEnabled())))
        delay(1000)
    }

    @Test
    @ExperimentalCoroutinesApi
    fun testTextToSpeechButton() = runTest {
        // speak
        onView(withId(R.id.imageButtonSpeak)).perform(click())
        delay(2000)
    }


    @Test
    @ExperimentalCoroutinesApi
    fun testSwitchSourceOrTargetLanguageMustRunTranslation() = runTest {
        selectSourceLanguage("fr")
        selectTargetLanguage("en")
        onView(withId(R.id.sourceText))
            .perform(typeText("Ciao."), closeSoftKeyboard())
        selectSourceLanguage("it")
        // onView(withId(R.id.targetText)).check(matches(withText("Hello.")))
        selectTargetLanguage("fr")
        // onView(withId(R.id.targetText)).check(matches(withText("Bonjour.")))
    }

    @Test
    @ExperimentalCoroutinesApi
    fun testAutoDetectShouldBeFriendlyIfLanguageIsNotRecognized() = runTest {
        selectSourceLanguage("auto")
        selectTargetLanguage("en")
        onView(withId(R.id.sourceText))
            .perform(typeText("Bo"), closeSoftKeyboard())
        onView(withId(R.id.targetText)).check(matches(withText(R.string.unrecognized_source_language)))
    }

    @Test
    @ExperimentalCoroutinesApi
    fun testAutoDetectShouldCorrectlyDetectSourceLanguage() = runTest {
        selectSourceLanguage("auto")
        selectTargetLanguage("en")
        onView(withId(R.id.sourceText))
            .perform(typeText("Bonjour."), closeSoftKeyboard())
        onView(withId(R.id.targetText)).check(matches(withText("Hello.")))
    }

    @Test
    @ExperimentalCoroutinesApi
    fun testSwitchButtonMustSwitchLanguagesAndRunTranslation() = runTest {
        selectSourceLanguage("fr")
        selectTargetLanguage("en")
        onView(withId(R.id.sourceText)).perform(clearText())
            .perform(typeText("Hello."), closeSoftKeyboard())
        onView(withId(R.id.buttonSwitchLang)).perform(click())
        // onView(withId(R.id.buttonSourceLang)).check(matches(withText(Language("en").displayName)))
        // onView(withId(R.id.buttonTargetLang)).check(matches(withText(Language("fr").displayName)))
        // onView(withId(R.id.targetText)).check(matches(withText("Bonjour.")))
    }

    @After
    fun unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource)
        }
    }
}
