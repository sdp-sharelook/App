package com.github.sdpsharelook

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdpsharelook.di.TextDetectionModule
import com.github.sdpsharelook.textDetection.TextDetectionFragment
import com.github.sdpsharelook.utils.FragmentScenarioRule
import com.google.android.gms.tasks.Tasks
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognizer
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock

@ExperimentalCoroutinesApi
@UninstallModules(TextDetectionModule::class)
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class TextDetectionFragmentTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val fragmentScenarioRule = FragmentScenarioRule.launch(TextDetectionFragment::class)

    @BindValue
    val textReco: TextRecognizer = mock(defaultAnswer = {Tasks.forResult(txtMock)})
    private val txtMock: Text = mock(defaultAnswer = {"test"})

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun textDetectionActivityTest() {
        //TODO recognizer is not injected exception
        val textView = onView(allOf(withId(R.id.text_data), withText("Detect the text")))
        textView.check(matches(isDisplayed()))
        val detectButton = onView(withId(R.id.detectButton))
        detectButton.check(matches(isDisplayed()))

//        detectButton.perform(click())
//        ShadowLooper.runUiThreadTasks()
//        verify(textReco).process(any<InputImage>())
//        onView(withText("test")).check(matches(isDisplayed()))
    }

    @Test
    fun `test process image`() {
        fragmentScenarioRule.scenario.onFragment {
            recognizer = textReco
            recognizer
            inputImage = mock()
            inputImage
        }
        onView(withId(R.id.detectButton)).perform(click())
        onView(withId(R.id.text_data)).check(matches(withText("test")))
    }

    @Test
    fun `test process null image`() {
        fragmentScenarioRule.scenario.onFragment {
            recognizer = textReco
            @Suppress("SelfAssignment")
            recognizer = recognizer
            inputImage = null
        }
        onView(withId(R.id.detectButton)).perform(click())
    }

    @Test
    fun `test detection failed`() {
        fragmentScenarioRule.scenario.onFragment {
            val txtMock1: Text = mock(defaultAnswer = {""})
            recognizer = mock(defaultAnswer = {Tasks.forResult(txtMock1)})
            inputImage = mock()
        }
        onView(withId(R.id.detectButton)).perform(click())
        onView(withId(R.id.text_data)).check(matches(withText(R.string.no_text_detected)))
    }
}
