package com.github.sdpsharelook

import android.content.Intent
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdpsharelook.textDetection.TextDetectionFragment
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GreetingFragmentTest {

//    @Test
//    fun testMainActivity() {
//        launchFragmentInContainer<GreetingFragment>(bundleOf("name" to "World"), R.style.Theme_Sherlook)
//        onView(withId(R.id.greetingMessage)).check(matches(withText("Hello World!")))
//    }
}