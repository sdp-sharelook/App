package com.github.sdpsharelook.download

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdpsharelook.R
import com.github.sdpsharelook.SelectPictureFragment
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
class SelectPictureFragmentTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val fragmentScenarioRule = FragmentScenarioRule.launch(SelectPictureFragment::class)

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun `test take picture`() = runTest {
        val cameraButton = onView(withId(R.id.button_camera))
        cameraButton.check(matches(isDisplayed()))
        Intents.init()
        cameraButton.perform(click())
        Intents.intended(IntentMatchers.hasAction("android.media.action.IMAGE_CAPTURE"))
        assert(Intents.getIntents().size == 1)
        Intents.release()
    }

    @Test
    fun `test launch OnlinePictureFragment`() {

    }

}