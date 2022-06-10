package com.github.sdpsharelook.download

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdpsharelook.R
import com.github.sdpsharelook.downloads.DownloadLanguagesFragment
import com.github.sdpsharelook.utils.FragmentScenarioRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric

@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class DownloadLanguagesFragmentTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val fragmentScenarioRule = FragmentScenarioRule.launch(DownloadLanguagesFragment::class)

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun `one language is always already downloaded`() = runTest {
        advanceUntilIdle()
        onView(allOf(withId(R.id.image_view_downloaded), isDisplayed()))
            .check(matches(isDisplayed()))
        onView(allOf(withId(R.id.image_button_delete), isDisplayed())).check(matches(isDisplayed()))
    }

    @Test
    fun `test progressbar is visible`() {
        onView(allOf(withId(R.id.image_button_download), isDisplayed())).perform(click())
        onView(allOf(withId(R.id.progress_bar_downloading), isDisplayed()))
            .check(matches(isDisplayed()))
        Robolectric.flushForegroundThreadScheduler()
        onView(allOf(withId(R.id.image_view_downloaded), isDisplayed()))
            .check(matches(isDisplayed()))
    }

    @Test
    fun `test delete language`() {
        onView(allOf(withId(R.id.image_button_delete), isDisplayed())).perform(click())
        Robolectric.flushForegroundThreadScheduler()
    }


    @Test
    fun `try unexistant dowload`() {
        val card = onView(withId(R.id.image_button_download))
        onView(allOf(withId(R.id.image_button_download), isDisplayed())).perform(click())
        onView(allOf(withId(R.id.progress_bar_downloading), isDisplayed()))
            .check(matches(isDisplayed()))
        Robolectric.flushForegroundThreadScheduler()
        onView(allOf(withId(R.id.image_button_delete), isDisplayed())).perform(click())
        Robolectric.flushForegroundThreadScheduler()
    }

}