package com.github.sdpsharelook.authorization

import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdpsharelook.R
import com.github.sdpsharelook.authorization.TestUserConstants.TEST_USER_EMAIL
import com.github.sdpsharelook.authorization.TestUserConstants.TEST_USER_PASS
import com.github.sdpsharelook.utils.FragmentScenarioRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
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

    @get:Rule(order = 1)
    val fragmentScenarioRule = FragmentScenarioRule.launch(LoginFragment::class)

    @Before
    fun init() {
        hiltRule.inject()
    }

    @After
    fun cleanUp() {
        auth.signOut()
    }

    @Test
    fun `test login with test user`() = runTest {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        fragmentScenarioRule.scenario.onFragment {
            navController.setGraph(R.navigation.main)
            navController.setCurrentDestination(R.id.menuLoginLink)
            Navigation.setViewNavController(requireView(), navController)
        }
        onView(withId(R.id.email)).perform(typeText(TEST_USER_EMAIL))
        onView(withId(R.id.password)).perform(typeText(TEST_USER_PASS))
        onView(withId(R.id.loginButton)).perform(click())
        assertEquals(R.id.profileInformation, navController.currentDestination!!.id)
        assert(auth.currentUser!!.email == TEST_USER_EMAIL)
    }

    @Test
    fun `test login with no email`() = runTest {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        fragmentScenarioRule.scenario.onFragment {
            navController.setGraph(R.navigation.main)
            navController.setCurrentDestination(R.id.menuLoginLink)
            Navigation.setViewNavController(requireView(), navController)
        }
        onView(withId(R.id.loginButton)).perform(swipeLeft())
        onView(withId(R.id.email)).perform(typeText(""))
        onView(withId(R.id.password)).perform(typeText(TEST_USER_PASS))
        assertEquals(R.id.menuLoginLink, navController.currentDestination!!.id)
        onView(withId(R.id.loginButton)).perform(click()).also {
            assert(auth.currentUser == null)
        }
    }

    @Test
    fun `test nab to signup`() = runTest {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        fragmentScenarioRule.scenario.onFragment {
            navController.setGraph(R.navigation.main)
            navController.setCurrentDestination(R.id.menuLoginLink)
            Navigation.setViewNavController(requireView(), navController)
        }
        onView(withId(R.id.signUpButton)).perform(click())
        assertEquals(R.id.signUpFragment, navController.currentDestination!!.id)
    }
}