package com.github.sdpsharelook.section

import androidx.core.os.bundleOf
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdpsharelook.R
import com.github.sdpsharelook.launchFragmentInHiltContainer
import com.github.sdpsharelook.storage.IRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
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
        val section = Section("Hello World!", R.drawable.spain, repo, "testHelloWorld")
        sectionList.add(0, section)
        val sectionWord = SectionWord("Hola", "Bonjour", null)
        val fragmentArgs = bundleOf(
            "sectionID" to 0,
            "sectionWord" to sectionWord
        )
        launchFragmentInHiltContainer<SectionDetailFragment>(fragmentArgs, R.style.Theme_Sherlook)
    }

    @Test
    fun `argument word is displayed`() = runTest {
        onView(withText("Hola")).check(matches(isDisplayed()))
        onView(withText("Bonjour")).check(matches(isDisplayed()))
        onView(withText("Hello World!")).check(matches(isDisplayed()))
        onView(withId(R.id.sectionFlag)).check(matches(isDisplayed()))
    }
}