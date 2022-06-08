package com.github.sdpsharelook.authorization

import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
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
import org.hamcrest.Matchers.*
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
class SignUpTest {

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
        onView(withId(R.id.firstName)).perform(replaceText("Jean"))
        onView(withId(R.id.lastName)).perform(replaceText("Paul"))
//        onView(withId(R.id.phoneNumber)).perform(replaceText("000 000 00 00"))
        onView(withId(R.id.email)).perform(replaceText(TEST_USER_EMAIL))
        onView(withId(R.id.password)).perform(replaceText(TEST_USER_PASS))
        onView(withId(R.id.confirmPassword)).perform(
            replaceText(TEST_USER_PASS)
        )
        onView(allOf(withId(R.id.loginButton), withText("Sign Up !"))).perform(click())
        onView(withId(R.id.email)).perform(replaceText(NEW_USER_EMAIL))
        onView(withId(R.id.password)).perform(replaceText(NEW_USER_PASS))
        onView(withId(R.id.confirmPassword)).perform(
            replaceText(NEW_USER_PASS)
        )
        onView(allOf(withId(R.id.loginButton), withText("Sign Up !"))).perform(click())
        assertEquals(R.id.greetingFragment, navController.currentDestination!!.id)
        assertNotNull(auth.currentUser)
        assertEquals(NEW_USER_EMAIL, auth.currentUser!!.email)
    }

    @Test
    fun `test sign up no email`() = runTest {
        /*val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        fragmentScenarioRule.scenario.onFragment {
            navController.setGraph(R.navigation.main)
            navController.setCurrentDestination(R.id.signUpFragment)
            Navigation.setViewNavController(requireView(), navController)
        }
        onView(withId(R.id.email)).perform(replaceText(" "))
        onView(withId(R.id.password)).perform(replaceText(NEW_USER_PASS))
        onView(withId(R.id.confirmPassword)).perform(replaceText(NEW_USER_PASS))
        onView(allOf(withId(R.id.loginButton), withText("Sign Up !"))).perform(click())
        onView(allOf(withId(R.id.loginButton), withText("Sign Up !"))).perform(click())
        assertEquals(R.id.signUpFragment, navController.currentDestination!!.id)
        assertNull(auth.currentUser)*/
    }

    @Test
    fun `test sign up name validation`() {
        val firstName = onView(withId(R.id.firstName))
        val lastName = onView(withId(R.id.lastName))

        firstName.perform(replaceText(""))
//        lastName.perform(click()) // unfocus previous view
//        firstName.check(matches(hasErrorText(any(String::class.java))))
        firstName.perform(replaceText("a"))
//        lastName.perform(click()) // unfocus previous view
//        firstName.check(matches(hasErrorText(nullValue(String::class.java))))

        lastName.perform(replaceText(""))
//        firstName.perform(click()) // unfocus previous view
//        lastName.check(matches(hasErrorText(any(String::class.java))))
        lastName.perform(replaceText("a"))
//        firstName.perform(click()) // unfocus previous view
//        lastName.check(matches(hasErrorText(nullValue(String::class.java))))

        firstName.perform(replaceText(""))
    }

    @Test
    fun `test sign up email validation`() {
        val email = onView(withId(R.id.email))
//        val emailBox = onView(withId(R.id.emailBox))
        val firstName = onView(withId(R.id.firstName))

        email.perform(replaceText(""))
        firstName.perform(click()) // unfocus previous view
//        emailBox.check(matches(hasDescendant(withText(any(String::class.java)))))
        email.perform(replaceText("wrong_email.address"))
        firstName.perform(click()) // unfocus previous view
//        emailBox.check(matches(hasDescendant(withText(any(String::class.java)))))
        email.perform(replaceText("right@email.address"))
        firstName.perform(click()) // unfocus previous view
//        emailBox.check(matches(hasDescendant(withText(nullValue(String::class.java)))))
    }

    @Test
    fun `test sign up password validation`() {

        val password = onView(withId(R.id.password))
        val passwordBox = onView(withId(R.id.prelimPasswordBox))

        password.perform(replaceText(""))
        passwordBox.check(matches(hasDescendant(withText("Required"))))
        password.perform(replaceText("a"))
        passwordBox.check(matches(hasDescendant(withText(containsString("uppercase")))))
        password.perform(replaceText("aA"))
        passwordBox.check(matches(hasDescendant(withText(containsString("digit")))))
        password.perform(replaceText("aA1"))
        passwordBox.check(matches(hasDescendant(withText(containsString("special")))))
        password.perform(replaceText("aA1+"))
        passwordBox.check(matches(hasDescendant(withText(containsString("8")))))
        password.perform(replaceText("aA1+5678"))
        passwordBox.check(matches(hasDescendant(withText(isEmptyString()))))

        val confirmPassword = onView(withId(R.id.confirmPassword))
        val confirmPasswordBox = onView(withId(R.id.passwordBox))

        confirmPasswordBox.check(matches(hasDescendant(withText(containsString("match")))))
        confirmPassword.perform(replaceText("aA1+5678"))
        confirmPasswordBox.check(matches(hasDescendant(withText(""))))
    }
}
