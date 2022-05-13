package com.github.sdpsharelook.authorization

import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdpsharelook.R
import com.github.sdpsharelook.authorization.TestUserConstants.TEST_USER_EMAIL
import com.github.sdpsharelook.authorization.TestUserConstants.TEST_USER_PASS
import com.github.sdpsharelook.utils.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class LoginTestRobo {

    @Inject
    lateinit var auth: AuthProvider

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun init() {
        hiltRule.inject()
        launchFragmentInHiltContainer<LoginFragment>(Bundle(), R.style.Theme_Sherlook)
    }

    @After
    fun cleanUp() {
        auth.signOut()
    }

    @Test
    fun `test login with test user`() = runTest {
        onView(withId(R.id.email)).perform(typeText(TEST_USER_EMAIL))
        onView(withId(R.id.password)).perform(typeText(TEST_USER_PASS))
        onView(withId(R.id.loginButton)).perform(click())
        assert(auth.currentUser!!.email == TEST_USER_EMAIL)
    }

    @Test
    fun `test login with no email`() = runTest {
        onView(withId(R.id.loginButton)).perform(swipeLeft())
        onView(withId(R.id.email)).perform(typeText(""))
        onView(withId(R.id.password)).perform(typeText(TEST_USER_PASS))
        onView(withId(R.id.loginButton)).perform(click()).also {
            assert(auth.currentUser == null)
        }
    }
}