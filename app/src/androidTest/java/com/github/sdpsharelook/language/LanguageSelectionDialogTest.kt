package com.github.sdpsharelook.language

import android.app.Activity
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.action.ViewActions.click
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.github.sdpsharelook.MainActivity
import com.github.sdpsharelook.camera.CameraActivity
import com.github.sdpsharelook.language.Matchers.Companion.isEquals
import com.github.sdpsharelook.language.Matchers.Companion.withTag
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LanguageSelectionDialogTest {
    /*@get:Rule
    var backgroundActivity = ActivityScenarioRule(MainActivity::class.java)
    private fun getActivity(): AppCompatActivity {
        var activity: AppCompatActivity? = null
        getInstrumentation().waitForIdleSync()
        backgroundActivity.scenario.onActivity { activity = it }
        return activity!!
    }

    @Test
    @ExperimentalCoroutinesApi
    fun languageSelectionDialogLaunches() = runTest{
        Looper.prepare()

        val selected=LanguageSelectionDialog.selectLanguage(
            getActivity(),
            setOf(Language.auto)
        )
        onData(isEquals(Language.auto)).perform(click())
    }*/

}