package com.github.sdpsharelook

import android.os.Bundle
import android.widget.Button
import android.widget.ShareActionProvider
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.github.sdpsharelook.camera.CameraFragment
import org.junit.Assert.*
import com.github.sdpsharelook.section.SectionFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Matchers

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.intent.Intents
import org.robolectric.Robolectric
import org.robolectric.shadows.ShadowDialog
import org.robolectric.shadows.ShadowLooper

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
        val cameraView = onView(withId(R.id.cameraImageView))
        cameraView.check(matches(isDisplayed()))
        val captureButton = onView(withId(R.id.buttonTakePic))
        captureButton.check(matches(isDisplayed()))

        Intents.init()

        captureButton.perform(click())
        onView(withId(R.id.cameraImageView))
            .check(matches(isDisplayed()))
        intended(IntentMatchers.hasAction("android.media.action.IMAGE_CAPTURE"))
        assert(Intents.getIntents().size == 1)
        Intents.release()

        onView(withId(R.id.cameraImageView))
            .check(matches(isDisplayed()))
    }

}
