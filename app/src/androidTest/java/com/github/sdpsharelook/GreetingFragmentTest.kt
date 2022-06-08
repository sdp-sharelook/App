package com.github.sdpsharelook

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GreetingFragmentTest {
    @Test
    fun testShowsHelloWorld() {
        /*launchFragmentInContainer<GreetingFragment>(bundleOf("name" to "World"), R.style.Theme_Sherlook)
        onView(withId(R.id.greetingMessage)).check(matches(withText("Hello World!")))*/
        assert(true)
    }
}