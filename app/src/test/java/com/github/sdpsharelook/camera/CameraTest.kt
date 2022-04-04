package com.github.sdpsharelook.camera

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.runner.AndroidJUnit4
import com.github.sdpsharelook.R
import com.github.sdpsharelook.authorization.SignUpActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CameraTest {
    @get:Rule
    var mActivityTestRule = ActivityScenarioRule(CameraActivity::class.java)

    @Test
    fun checkDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.cameraImageView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.buttonTakePic))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}