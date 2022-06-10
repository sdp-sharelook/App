package com.github.sdpsharelook

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdpsharelook.di.StorageModule
import com.github.sdpsharelook.section.Section
import com.github.sdpsharelook.storage.IRepository
import com.github.sdpsharelook.utils.FragmentScenarioRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@UninstallModules(StorageModule::class)
@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MapsFragmentTest {
    @Inject
    lateinit var repo: IRepository<List<Section>>
    private lateinit var testFlow: Flow<Result<List<Section>?>>

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val fragmentScenarioRule = FragmentScenarioRule.launch(MapsFragment::class)

    @Before
    fun setUp() {
        hiltRule.inject()
        testFlow = repo.flow()
    }

    @Test
    fun `test maps fragment displays`() = runTest {
        advanceUntilIdle()
        onView(withId(R.id.map)).check(matches(isDisplayed()))
        onView(withId(R.id.map)).perform(click())
    }

    @Test
    fun `map shows`() {
        //onView(withId(R.id.map)).check(matches(isDisplayed()))
        //onView(withId(R.id.map)).perform(click())
    }
}