package com.github.sdpsharelook.download

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.test.espresso.Espresso.onData
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
import com.github.sdpsharelook.SelectPictureFragmentLift
import com.github.sdpsharelook.onlinePictures.OnlinePictureFragment
import com.github.sdpsharelook.onlinePictures.OnlinePictureFragmentLift
import com.github.sdpsharelook.utils.FragmentScenarioRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.core.IsAnything.anything
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import java.io.Serializable

@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class OnlinePictureFragmentTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val fragmentScenarioRule = FragmentScenarioRule.launch(
        OnlinePictureFragment::class,
        null,
        Bundle().apply {
            putString(OnlinePictureFragmentLift.WORD_PARAMETER, "banane")
            putString(OnlinePictureFragmentLift.LANGUAGE_PARAMETER, "fr")
            putSerializable(OnlinePictureFragmentLift.CALLBACK_FUNCTION_PARAMETER,
                { _: String? -> } as Serializable)
        })

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun `launch OnlinePictureFragment`() = runTest {
        Robolectric.flushForegroundThreadScheduler()
    }

}