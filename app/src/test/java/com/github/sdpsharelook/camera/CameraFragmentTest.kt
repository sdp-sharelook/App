package com.github.sdpsharelook.camera

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.github.sdpsharelook.R
import com.github.sdpsharelook.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class CameraFragmentTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Rule
    @JvmField
    var mGrantPermissionRule =
        GrantPermissionRule.grant(
            "android.permission.CAMERA"
        )

    @Test
    fun testReceivesAndPrintsHelloWorld() {
        launchFragmentInHiltContainer<CameraFragment>()
        val cameraView = Espresso.onView(ViewMatchers.withId(R.id.cameraImageView))
        cameraView.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        val captureButton = Espresso.onView(ViewMatchers.withId(R.id.buttonTakePic))
        captureButton.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Intents.init()

        captureButton.perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.cameraImageView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Intents.intended(IntentMatchers.hasAction("android.media.action.IMAGE_CAPTURE"))
        assert(Intents.getIntents().size == 1)
        Intents.release()

        Espresso.onView(ViewMatchers.withId(R.id.cameraImageView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

}