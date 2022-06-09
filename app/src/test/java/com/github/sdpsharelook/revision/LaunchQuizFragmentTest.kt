package com.github.sdpsharelook.revision

import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdpsharelook.R
import com.github.sdpsharelook.utils.FragmentScenarioRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.Matchers.containsString
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class LaunchQuizFragmentTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val fragmentScenarioRule = FragmentScenarioRule.launch(LaunchQuizFragment::class)

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun `test bad number input`() = runTest {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        fragmentScenarioRule.scenario.onFragment {
            navController.setGraph(R.navigation.main)
            navController.setCurrentDestination(R.id.launchQuizFragment)
            Navigation.setViewNavController(requireView(), navController)
        }
        onView(withId(R.id.startQuizButton)).perform(click())
        advanceUntilIdle()
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(containsString("number"))))
            .perform(click())
        assertEquals(R.id.launchQuizFragment, navController.currentDestination!!.id)
    }

    @Test
    fun `test too many asked`() = runTest {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        fragmentScenarioRule.scenario.onFragment {
            navController.setGraph(R.navigation.main)
            navController.setCurrentDestination(R.id.launchQuizFragment)
            Navigation.setViewNavController(requireView(), navController)
        }
        onView(withId(R.id.start10QuizButton)).perform(click())
        advanceUntilIdle()
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(containsString("enough"))))
            .perform(click())
        assertEquals(R.id.launchQuizFragment, navController.currentDestination!!.id)
    }

    @Test
    fun `check navigation`() = runTest {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        fragmentScenarioRule.scenario.onFragment {
            navController.setGraph(R.navigation.main)
            navController.setCurrentDestination(R.id.launchQuizFragment)
            Navigation.setViewNavController(requireView(), navController)
        }
        assertEquals(R.id.launchQuizFragment, navController.currentDestination!!.id)
        onView(withId(R.id.startAllQuizButton)).perform(click())
        advanceUntilIdle()
        assertEquals(R.id.revisionQuizFragment, navController.currentDestination!!.id)
    }
}