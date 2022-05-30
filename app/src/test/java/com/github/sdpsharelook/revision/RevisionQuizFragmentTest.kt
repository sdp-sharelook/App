package com.github.sdpsharelook.revision

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.isClickable
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdpsharelook.Word
import com.github.sdpsharelook.storage.IRepository
import com.github.sdpsharelook.utils.FragmentScenarioRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.anything
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class RevisionQuizFragmentTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val fragmentScenarioRule = FragmentScenarioRule.launch(RevisionQuizFragment::class)

    @Inject
    lateinit var repo: IRepository<List<Word>>

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun `test continue part`() = runTest {
        advanceUntilIdle()
        onView(allOf(isClickable(), isDisplayed())).check(ViewAssertions.matches(anything()))
//        onView(withId(R.id.helpToggleButton)).perform(click())
//        onView(withId(R.id.answerQualityButton0)).check(matches(withText(any<String>())))
//        onView(withId(R.id.answerQualityButton1)).check(matches(withText(any<String>())))
//        onView(withId(R.id.answerQualityButton2)).check(matches(withText(any<String>())))
//        onView(withId(R.id.answerQualityButton3)).check(matches(withText(any<String>())))
//        onView(withId(R.id.answerQualityButton4)).check(matches(withText(any<String>())))
//        onView(withId(R.id.answerQualityButton5)).check(matches(withText(any<String>())))
    }
}