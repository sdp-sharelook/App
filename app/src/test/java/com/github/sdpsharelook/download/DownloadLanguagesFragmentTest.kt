package com.github.sdpsharelook.download

import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdpsharelook.R
import com.github.sdpsharelook.downloads.DownloadLanguagesFragment
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
    fun `test download a language` () = runTest {
        onView(withId(R.id.image_button_download)).perform(click())
        onView(withId(R.id.progress_bar_downloading)).check(matches(isDisplayed()))
        // i hope the coroutine block before the next line
        onView(withId(R.id.image_view_downloaded)).check(matches(isDisplayed()))
        onView(withId(R.id.image_button_delete)).check(matches(isDisplayed()))
    }

}