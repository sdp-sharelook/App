package com.github.sdpsharelook.section

import android.widget.ImageButton
import androidx.core.os.bundleOf
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.longClick
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdpsharelook.storage.IRepository
import com.github.sdpsharelook.utils.FragmentScenario
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.*
import org.junit.Assert.assertTrue
import org.junit.runner.RunWith
import org.robolectric.shadows.ShadowDialog
import com.github.sdpsharelook.R
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class SectionDetailFragmentTest {
    @Inject
    lateinit var repo: IRepository<List<String>>

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        hiltRule.inject()
        val sec = Section("Barcelone", 0, "test")
        val fragmentArgs = bundleOf( "section" to Json.encodeToString(sec))
        FragmentScenario.launch(SectionDetailFragment::class, null, fragmentArgs)
    }
    @After
    fun end() {
        sectionList.clear()
    }

    @Test
    fun sectionDetailFragmentTest() = runTest {
        onView(withText("Hola")).check(matches(isDisplayed()))
        onView(withText("Bonjour")).check(matches(isDisplayed()))
        onView(withText("Barcelone")).check(matches(isDisplayed()))

        val word = onView(withText("Hola"))
        word.check(matches(isDisplayed()))

        word.perform(click())

        var dialog = ShadowDialog.getLatestDialog()
        assertTrue(dialog.isShowing)

        val clearDialog = dialog.findViewById<ImageButton>(R.id.button_clear_picture)
        clearDialog.performClick()
        word.perform(longClick())
        onView(withText("Hola")).check(ViewAssertions.doesNotExist())
    }
}