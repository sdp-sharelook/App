package com.github.sdpsharelook.section

import androidx.core.os.bundleOf
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdpsharelook.R
import com.github.sdpsharelook.Word
import com.github.sdpsharelook.storage.IRepository
import com.github.sdpsharelook.utils.FragmentScenario
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
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
        word.perform(ViewActions.longClick())
        onView(withText("Hola")).check(ViewAssertions.doesNotExist())

    }
}