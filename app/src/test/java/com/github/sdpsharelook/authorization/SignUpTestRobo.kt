package com.github.sdpsharelook.authorization

import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdpsharelook.R
import com.github.sdpsharelook.authorization.TestUserConstants.NEW_USER_EMAIL
import com.github.sdpsharelook.authorization.TestUserConstants.NEW_USER_PASS
import com.github.sdpsharelook.authorization.TestUserConstants.TEST_USER_EMAIL
import com.github.sdpsharelook.authorization.TestUserConstants.TEST_USER_PASS
import com.github.sdpsharelook.utils.FragmentScenarioRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class SignUpTestRobo {

    @Inject
    lateinit var auth: AuthProvider

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val fragmentScenarioRule = FragmentScenarioRule.launch(SignUpFragment::class)

    @Before
    fun init() {
        hiltRule.inject()
    }

    @After
    fun cleanUp() {
        auth.signOut()
    }

    @Test
    fun `test sign up is displayed`() = runTest {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        fragmentScenarioRule.scenario.onFragment {
            navController.setGraph(R.navigation.main)
            navController.setCurrentDestination(R.id.signUpFragment)
            Navigation.setViewNavController(requireView(), navController)
        }
        onView(withId(R.id.layout_signup)).check(matches(isDisplayed()))
        onView(withId(R.id.email)).check(matches(isDisplayed()))
        onView(withId(R.id.emailBox)).check(matches(isDisplayed()))
        onView(withId(R.id.firstName)).check(matches(isDisplayed()))
        onView(withId(R.id.firstNameBox)).check(matches(isDisplayed()))
        onView(withId(R.id.lastName)).check(matches(isDisplayed()))
        onView(withId(R.id.lastNameBox)).check(matches(isDisplayed()))
        onView(withId(R.id.confirmPassword)).check(matches(isDisplayed()))
        onView(withId(R.id.passwordBox)).check(matches(isDisplayed()))
        onView(withId(R.id.password)).check(matches(isDisplayed()))
        onView(withId(R.id.prelimPasswordBox)).check(matches(isDisplayed()))
        onView(withId(R.id.phoneNumber)).check(matches(isDisplayed()))
        onView(withId(R.id.phoneNumberBox)).check(matches(isDisplayed()))
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()))
    }

    @Test
    fun `test sign up`() = runTest {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        fragmentScenarioRule.scenario.onFragment {
            navController.setGraph(R.navigation.main)
            navController.setCurrentDestination(R.id.signUpFragment)
            Navigation.setViewNavController(requireView(), navController)
        }
        onView(withId(R.id.firstName)).perform(replaceText("Jean"), closeSoftKeyboard())
        onView(withId(R.id.lastName)).perform(replaceText("Paul"), closeSoftKeyboard())
//        onView(withId(R.id.phoneNumber)).perform(replaceText("000 000 00 00"), closeSoftKeyboard())
        onView(withId(R.id.email)).perform(replaceText(TEST_USER_EMAIL), closeSoftKeyboard())
        onView(withId(R.id.password)).perform(replaceText(TEST_USER_PASS), closeSoftKeyboard())
        onView(withId(R.id.confirmPassword)).perform(
            replaceText(TEST_USER_PASS),
            closeSoftKeyboard()
        )
        onView(allOf(withId(R.id.loginButton), withText("Sign Up !"))).perform(click())
        onView(withId(R.id.email)).perform(replaceText(NEW_USER_EMAIL), closeSoftKeyboard())
        onView(withId(R.id.password)).perform(replaceText(NEW_USER_PASS), closeSoftKeyboard())
        onView(withId(R.id.confirmPassword)).perform(
            replaceText(NEW_USER_PASS),
            closeSoftKeyboard()
        )
        onView(allOf(withId(R.id.loginButton), withText("Sign Up !"))).perform(click())
        assertEquals(R.id.greetingFragment, navController.currentDestination!!.id)
        assertNotNull(auth.currentUser)
        assertEquals(NEW_USER_EMAIL, auth.currentUser!!.email)
    }

    @Test
    fun `test sign up no email`() = runTest {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        fragmentScenarioRule.scenario.onFragment {
            navController.setGraph(R.navigation.main)
            navController.setCurrentDestination(R.id.signUpFragment)
            Navigation.setViewNavController(requireView(), navController)
        }
        onView(withId(R.id.email)).perform(replaceText(" "), closeSoftKeyboard())
        onView(withId(R.id.confirmPassword)).perform(
            replaceText(TEST_USER_PASS),
            closeSoftKeyboard()
        )
        onView(allOf(withId(R.id.loginButton), withText("Sign Up !"))).perform(click())
        onView(allOf(withId(R.id.loginButton), withText("Sign Up !"))).perform(click())
        assertEquals(R.id.signUpFragment, navController.currentDestination!!.id)
        assertNull(auth.currentUser)
    }

}
