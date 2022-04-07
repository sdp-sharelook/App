package com.github.sdpsharelook.authorization


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdpsharelook.R
import com.github.sdpsharelook.authorization.TestUserConstants.TEST_USER_PASS2
import com.github.sdpsharelook.authorization.UserConstants.TEST_USER_EMAIL
import com.github.sdpsharelook.authorization.UserConstants.TEST_USER_PASS
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class SignUpTest {

    @get:Rule
    var mActivityTestRule = ActivityScenarioRule(SignUpActivity::class.java)
    val NEW_USER_EMAIL = "testuser123@gmail.com"
    val NEW_USER_PASS = "123456"


    @Before
    fun setAuth() {
        auth = TestAuth()
    }

    @After
    fun cleanUp() {
        auth.signOut()
    }

    @Test
    fun testSignUpTest() {
        onView(withId(R.id.layout_signup)).check(matches(isDisplayed()))
        onView(withId(R.id.email)).check(matches(isDisplayed()))
        onView(withId(R.id.emailBox)).check(matches(isDisplayed()))
        onView(withId(R.id.firstName)).check(matches(isDisplayed()))
        onView(withId(R.id.firstNameBox)).check(matches(isDisplayed()))
        onView(withId(R.id.lastName)).check(matches(isDisplayed()))
        onView(withId(R.id.lastNameBox)).check(matches(isDisplayed()))
        onView(withId(R.id.password)).check(matches(isDisplayed()))
        onView(withId(R.id.passwordBox)).check(matches(isDisplayed()))
        onView(withId(R.id.preliminary_password)).check(matches(isDisplayed()))
        onView(withId(R.id.prelimPasswordBox)).check(matches(isDisplayed()))
        onView(withId(R.id.phoneNumber)).check(matches(isDisplayed()))
        onView(withId(R.id.phoneNumberBox)).check(matches(isDisplayed()))
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()))
    }

//    @Test
//    fun testSignUp() {
//        val appCompatEditText = onView(
//            allOf(
//                withId(R.id.email),
//                childAtPosition(
//                    childAtPosition(
//                        withId(android.R.id.content),
//                        0
//                    ),
//                    0
//                ),
//                isDisplayed()
//            )
//        )
//        appCompatEditText.perform(replaceText(TEST_USER_EMAIL), closeSoftKeyboard())
//
//        val appCompatEditText2 = onView(
//            allOf(
//                withId(R.id.password),
//                childAtPosition(
//                    childAtPosition(
//                        withId(android.R.id.content),
//                        0
//                    ),
//                    1
//                ),
//                isDisplayed()
//            )
//        )
//        appCompatEditText2.perform(replaceText(TEST_USER_PASS), closeSoftKeyboard())
//
//        val materialButton2 = onView(
//            allOf(
//                withId(R.id.loginButton), withText("Sign Up !"),
//                childAtPosition(
//                    childAtPosition(
//                        withId(android.R.id.content),
//                        0
//                    ),
//                    2
//                ),
//                isDisplayed()
//            )
//        )
//        materialButton2.perform(click())
//
//        val appCompatEditText3 = onView(
//            allOf(
//                withId(R.id.email), withText(TEST_USER_EMAIL),
//                childAtPosition(
//                    childAtPosition(
//                        withId(android.R.id.content),
//                        0
//                    ),
//                    0
//                ),
//                isDisplayed()
//            )
//        )
//        appCompatEditText3.perform(replaceText("testuser123@gmail.com"))
//
//        val appCompatEditText4 = onView(
//            allOf(
//                withId(R.id.email), withText("testuser123@gmail.com"),
//                childAtPosition(
//                    childAtPosition(
//                        withId(android.R.id.content),
//                        0
//                    ),
//                    0
//                ),
//                isDisplayed()
//            )
//        )
//        appCompatEditText4.perform(closeSoftKeyboard())
//2
//        val materialButton3 = onView(
//            allOf(
//                withId(R.id.loginButton), withText("Sign Up !"),
//                childAtPosition(
//                    childAtPosition(
//                        withId(android.R.id.content),
//                        0
//                    ),
//                    2
//                ),
//                isDisplayed()
//            )
//        )
//        materialButton3.perform(click())
//        assert(auth.currentUser!!.email == NEW_USER_EMAIL)
//    }
//
//    @Test
//    fun testSignUpNoEmail() {
//        val appCompatEditText = onView(
//            allOf(
//                withId(R.id.email),
//                childAtPosition(
//                    childAtPosition(
//                        withId(android.R.id.content),
//                        0
//                    ),
//                    0
//                ),
//                isDisplayed()
//            )
//        )
//        appCompatEditText.perform(replaceText(" "), closeSoftKeyboard())
//
//        val appCompatEditText2 = onView(
//            allOf(
//                withId(R.id.password),
//                childAtPosition(
//                    childAtPosition(
//                        withId(android.R.id.content),
//                        0
//                    ),
//                    1
//                ),
//                isDisplayed()
//            )
//        )
//        appCompatEditText2.perform(replaceText(TEST_USER_PASS), closeSoftKeyboard())
//
//        val materialButton2 = onView(
//            allOf(
//                withId(R.id.loginButton), withText("Sign Up !"),
//                childAtPosition(
//                    childAtPosition(
//                        withId(android.R.id.content),
//                        0
//                    ),
//                    2
//                ),
//                isDisplayed()
//            )
//        )
//        materialButton2.perform(click())
//
//
//        val materialButton3 = onView(
//            allOf(
//                withId(R.id.loginButton), withText("Sign Up !"),
//                childAtPosition(
//                    childAtPosition(
//                        withId(android.R.id.content),
//                        0
//                    ),
//                    2
//                ),
//                isDisplayed()
//            )
//        )
//        materialButton3.perform(click())
//        assert(auth.currentUser == null)
//    }
//
//    private fun childAtPosition(
//        parentMatcher: Matcher<View>, position: Int
//    ): Matcher<View> {
//
//        return object : TypeSafeMatcher<View>() {
//            override fun describeTo(description: Description) {
//                description.appendText("Child at position $position in parent ")
//                parentMatcher.describeTo(description)
//            }
//
//            public override fun matchesSafely(view: View): Boolean {
//                val parent = view.parent
//                return parent is ViewGroup && parentMatcher.matches(parent)
//                        && view == parent.getChildAt(position)
//            }
//        }
//    }
}
