package com.github.sdpsharelook.download

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdpsharelook.R
import com.github.sdpsharelook.di.MLKitModule
import com.github.sdpsharelook.di.TranslationModule
import com.github.sdpsharelook.downloads.DownloadLanguagesFragment
import com.github.sdpsharelook.downloads.MLKitTranslatorDownloader
import com.github.sdpsharelook.downloads.TranslatorDownloader
import com.github.sdpsharelook.language.Language
import com.github.sdpsharelook.utils.FragmentScenarioRule
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock

@ExperimentalCoroutinesApi
@HiltAndroidTest
@UninstallModules(MLKitModule::class)
@RunWith(AndroidJUnit4::class)
class DownloadLanguagesFragmentTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val fragmentScenarioRule = FragmentScenarioRule.launch(DownloadLanguagesFragment::class)

    @BindValue
    val downloader: TranslatorDownloader = object : TranslatorDownloader {
        override suspend fun downloadedLanguages(): List<Language> {
            return listOf()
        }

        override suspend fun deleteLanguage(language: Language): Boolean {
            return false
        }

        override suspend fun downloadLanguage(language: Language, requireWifi: Boolean): Boolean {
            return false
        }
    }


    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun `test download a language`() = runTest {
        advanceUntilIdle()
        onView(withId(R.id.image_button_download)).perform(click())
        onView(withId(R.id.progress_bar_downloading)).check(matches(isDisplayed()))
        // i hope the coroutine block before the next line
        onView(withId(R.id.image_view_downloaded)).check(matches(isDisplayed()))
        onView(withId(R.id.image_button_delete)).check(matches(isDisplayed()))
    }

}