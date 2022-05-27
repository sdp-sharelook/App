package com.github.sdpsharelook.translate

import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdpsharelook.R
import com.github.sdpsharelook.language.Language
import com.github.sdpsharelook.language.MatchersTest
import com.github.sdpsharelook.utils.FragmentScenarioRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class TranslateFragmentTest {
    private var mIdlingResource: IdlingResource? = null

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val fragmentScenarioRule = FragmentScenarioRule.launch(TranslateFragment::class)

    @Before
    fun init() {
        hiltRule.inject()
        fragmentScenarioRule.scenario.onFragment {
            mIdlingResource = getIdlingResource()
            // To prove that the test fails, omit this call:
            IdlingRegistry.getInstance().register(mIdlingResource)
        }
    }

    private fun selectSourceLanguage(language: Language) {
        onView(withId(R.id.spinner_source_lang)).perform(click())
        onData(MatchersTest.isEquals(language)).perform(click())
    }

    private fun selectTargetLanguage(language: Language) {
        onView(withId(R.id.spinner_target_lang)).perform(click())
        onData(MatchersTest.isEquals(language)).perform(click())
    }

    @Test
    fun `test translate hello from auto to english`() = runTest {
        selectSourceLanguage(Language.auto)
        selectTargetLanguage(Language("en"))
        onView(withId(R.id.sourceText)).perform(replaceText("Hello"))
        onView(withId(R.id.targetText)).check(matches(withText("Hello")))
    }

    @Test
    fun `translate too short string from auto`() = runTest {
        selectSourceLanguage(Language.auto)
        selectTargetLanguage(Language("en"))
        onView(withId(R.id.sourceText)).perform(replaceText("a"))
        onView(withId(R.id.targetText)).check(matches(withText("Source language unrecognized.")))
    }

    @Test
    fun `translate meaningless string from auto`() = runTest {
        selectSourceLanguage(Language.auto)
        selectTargetLanguage(Language("en"))
        onView(withId(R.id.sourceText)).perform(replaceText("didnndoeld"))
        onView(withId(R.id.targetText)).check(matches(withText("Source language unrecognized.")))
    }

    @Test
    fun `translate string in missing language from auto`() = runTest {
        selectSourceLanguage(Language.auto)
        selectTargetLanguage(Language("en"))
        onView(withId(R.id.sourceText)).perform(replaceText("Mama mia !"))
        onView(withId(R.id.targetText)).check(matches(withText("You need to download this language : it")))
    }

    @Test
    fun `test switch button from auto`() = runTest {
        selectSourceLanguage(Language.auto)
        selectTargetLanguage(Language("en"))
        onView(withId(R.id.sourceText)).perform(replaceText("Mama mia !"))
        onView(withId(R.id.targetText)).check(matches(withText("hello")))
        onView(withId(R.id.buttonSwitchLang)).perform(click())
        // assert toast ?
    }

    @Test
    fun `test switch button from not auto`() = runTest {
        selectSourceLanguage(Language("en"))
        selectTargetLanguage(Language("en"))
        onView(withId(R.id.sourceText)).perform(replaceText("Hello"))
        onView(withId(R.id.buttonSwitchLang)).perform(click())
        // assert not toast ?
    }

    @Test
    fun `test text to speech`() = runTest {
        onView(withId(R.id.imageButtonTTS)).perform(click())
        // nothing to assert
    }

    @Test
    fun `test speech recognition`() = runTest {
        onView(withId(R.id.imageButtonSR)).perform(click())
        // permission feature was disabled for now in SpeechRecognizer due to a crash
        /* PermissionChecker.checkCallingOrSelfPermission(
            context,
            android.Manifest.permission.RECORD_AUDIO
        )
        PermissionRequester().apply {
            addPermissions(android.Manifest.permission.RECORD_AUDIO)
            requestPermissions()
        } */
    }
}