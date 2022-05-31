package com.github.sdpsharelook.revision

import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.matcher.ViewMatchers.Visibility.INVISIBLE
import androidx.test.espresso.matcher.ViewMatchers.Visibility.VISIBLE
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdpsharelook.R
import com.github.sdpsharelook.Word
import com.github.sdpsharelook.storage.IRepository
import com.github.sdpsharelook.utils.FragmentScenarioRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.not
import org.junit.Assert.assertEquals
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
    fun `test visibilities`() = runTest {
        val answerButton = onView(withId(R.id.answerQualityButton0))
        val layout = onView(withId(R.id.quizLayout))
        answerButton.check(matches(withEffectiveVisibility(INVISIBLE)))
        layout.perform(click())
        answerButton.check(matches(withEffectiveVisibility(VISIBLE)))

        answerButton.check(matches(withText("")))
        onView(withId(R.id.helpToggleButton)).perform(click())
        answerButton.check(matches(withText(not(""))))
        onView(withId(R.id.helpToggleButton)).perform(click())
        answerButton.check(matches(withText("")))
    }

    @Test
    fun `test navigation`() = runTest {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        fragmentScenarioRule.scenario.onFragment {
            navController.setGraph(R.navigation.main)
            navController.setCurrentDestination(R.id.revisionQuizFragment)
            Navigation.setViewNavController(requireView(), navController)
        }
        val layout = onView(withId(R.id.quizLayout))
        val answerButton = onView(withId(R.id.answerQualityButton0))

        assertEquals(R.id.revisionQuizFragment, navController.currentDestination!!.id)
        answerButton.check(matches(withEffectiveVisibility(INVISIBLE)))
        layout.perform(click())
        answerButton.check(matches(withEffectiveVisibility(VISIBLE)))
        answerButton.perform(click())
//        answerButton.check(matches(withEffectiveVisibility(INVISIBLE)))
//        layout.perform(click())
//        answerButton.check(matches(withEffectiveVisibility(VISIBLE)))
//        answerButton.perform(click())
        assertEquals(R.id.launchQuizFragment, navController.currentDestination!!.id)
    }
}