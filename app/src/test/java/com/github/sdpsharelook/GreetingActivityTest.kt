package com.github.sdpsharelook

import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GreetingActivityTest {
    @Test
    fun testMainActivity() {
        val intent: Intent =
            Intent(
                ApplicationProvider.getApplicationContext(),
                GreetingActivity::class.java
            ).apply {
                putExtra(EXTRA_MESSAGE, "World")
            }
        val scenario = launchActivity<GreetingActivity>(intent)
        scenario.use {
            onView(withId(R.id.greetingMessage))
                .check(matches(withText("Hello World!")))
        }
    }
}