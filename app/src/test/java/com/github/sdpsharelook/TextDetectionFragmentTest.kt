package com.github.sdpsharelook

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.github.sdpsharelook.section.SectionFragment
import com.github.sdpsharelook.textDetection.TextDetectionFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.shadows.ShadowLooper

@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class TextDetectionFragmentTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun init() {
        hiltRule.inject()
        launchFragmentInHiltContainer<TextDetectionFragment>()
    }
    @Test
    fun textDetectionActivityTest() {
        val textView = onView(allOf(withId(R.id.text_data), withText("Detect the text")))
        textView.check(matches(isDisplayed()))
        val detectButton = onView(withId(R.id.detectButton))
        detectButton.perform(click())

        ShadowLooper.runUiThreadTasks()
        onView(withText("Detect the text")).check(matches(isDisplayed()))
    }

}
