package com.github.sdpsharelook.section

import android.widget.Button
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressBack
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdpsharelook.R
import com.github.sdpsharelook.utils.FragmentScenarioRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.allOf
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.shadows.ShadowDialog
import org.robolectric.shadows.ShadowLooper

@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class SectionFragmentTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val fragmentScenarioRule = FragmentScenarioRule.launch(SectionFragment::class)

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun sectionFragmentTest() = runTest {
        val floatingActionButton = onView(withId(R.id.addingBtn))
        floatingActionButton.perform(click())

        // wait for the dialogue to popup
        var dialog = ShadowDialog.getLatestDialog()
        assertTrue(dialog.isShowing)
        var title = dialog.findViewById<TextView>(R.id.edit_section_name)
        title.text = "Cuisine"
        dialog.findViewById<Button>(R.id.popup_add_btn).performClick()
        ShadowLooper.runUiThreadTasks()
        assertFalse(dialog.isShowing)

        //wait the recyclerView to be updated
        Robolectric.flushForegroundThreadScheduler()

        onView(withText("Cuisine")).check(matches(isDisplayed()))

        // test editing the section
        val editButton = onView(withId(R.id.editButton))
        editButton.perform(click())

        dialog = ShadowDialog.getLatestDialog()
        assertTrue(dialog.isShowing)
        title = dialog.findViewById<TextView>(R.id.edit_section_name)
        title.text = "Chambre"
        dialog.findViewById<Button>(R.id.popup_add_btn).performClick()
        ShadowLooper.runUiThreadTasks()
        assertFalse(dialog.isShowing)

        //wait the recyclerView to be updated
        Robolectric.flushForegroundThreadScheduler()
        onView(withText("Chambre")).check(matches(isDisplayed()))

        // test deleting the section

        val deleteButton = onView(withId(R.id.deleteButton))
        deleteButton.perform(click())
        Robolectric.flushForegroundThreadScheduler()
        onView(withText("Chambre")).check(doesNotExist())

        //TODO sectionCard.perform(click())
    }
}
