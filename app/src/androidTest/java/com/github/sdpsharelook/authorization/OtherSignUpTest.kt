@file:Suppress("UNUSED_VARIABLE")

package com.github.sdpsharelook.authorization


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.github.sdpsharelook.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class OtherSignUpTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun finalSignUpTest() {

        val materialButton2 = onView(
            allOf(
                withId(R.id.signUpButton), withText("Don't have an account? Sign up here !"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        materialButton2.perform(click())

        val materialButton3 = onView(
            allOf(
                withId(R.id.loginButton), withText("Sign Up !"),
                childAtPosition(
                    allOf(
                        withId(R.id.layout_signup),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        materialButton3.perform(click())

        val textInputEditText = onView(
            allOf(
                withId(R.id.firstName),
                isDisplayed()
            )
        )
        textInputEditText.perform(replaceText("first"), closeSoftKeyboard())

        val textInputEditText2 = onView(
            allOf(
                withId(R.id.firstName), withText("first"),
                isDisplayed()
            )
        )
        textInputEditText2.perform(pressImeActionButton())

        val textInputEditText3 = onView(
            allOf(
                withId(R.id.lastName),
                isDisplayed()
            )
        )
        textInputEditText3.perform(replaceText("last"), closeSoftKeyboard())

        //pressBack()

        val materialButton4 = onView(
            allOf(
                withId(R.id.loginButton), withText("Sign Up !"),
                childAtPosition(
                    allOf(
                        withId(R.id.layout_signup),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        materialButton4.perform(click())

        val textInputEditText4 = onView(
            allOf(
                withId(R.id.email),
                isDisplayed()
            )
        )
        textInputEditText4.perform(replaceText("firstlast"), closeSoftKeyboard())

        val textInputEditText5 = onView(
            allOf(
                withId(R.id.email), withText("firstlast"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.emailBox),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        //textInputEditText5.perform(pressImeActionButton())

        val textInputEditText6 = onView(
            allOf(
                withId(R.id.email), withText("firstlast"),
                isDisplayed()
            )
        )
        textInputEditText6.perform(replaceText("firstlast@mail"))

        val textInputEditText7 = onView(
            allOf(
                withId(R.id.email), withText("firstlast@mail"),
                isDisplayed()
            )
        )
        textInputEditText7.perform(closeSoftKeyboard())

        val textInputEditText8 = onView(
            allOf(
                withId(R.id.email), withText("firstlast@mail"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.emailBox),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
//        textInputEditText8.perform(pressImeActionButton())
//
//        pressBack()

        val materialButton5 = onView(
            allOf(
                withId(R.id.loginButton), withText("Sign Up !"),
                childAtPosition(
                    allOf(
                        withId(R.id.layout_signup),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        materialButton5.perform(click())

        val textInputEditText9 = onView(
            allOf(
                withId(R.id.email), withText("firstlast@mail"),
                isDisplayed()
            )
        )
        textInputEditText9.perform(replaceText("firstlast@mail.ch"))

        val textInputEditText10 = onView(
            allOf(
                withId(R.id.email), withText("firstlast@mail.ch"),
                isDisplayed()
            )
        )
        textInputEditText10.perform(closeSoftKeyboard())

        val textInputEditText11 = onView(
            allOf(
                withId(R.id.email), withText("firstlast@mail.ch"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.emailBox),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
//        textInputEditText11.perform(pressImeActionButton())
//
//        pressBack()

        val materialButton6 = onView(
            allOf(
                withId(R.id.loginButton), withText("Sign Up !"),
                childAtPosition(
                    allOf(
                        withId(R.id.layout_signup),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        materialButton6.perform(click())

        val textInputEditText12 = onView(
            allOf(
                withId(R.id.phoneNumber),
                isDisplayed()
            )
        )
        textInputEditText12.perform(click())

        val textInputEditText13 = onView(
            allOf(
                withId(R.id.phoneNumber),
                isDisplayed()
            )
        )
        textInputEditText13.perform(replaceText("9658235863"), closeSoftKeyboard())

        val textInputEditText14 = onView(
            allOf(
                withId(R.id.phoneNumber), withText("9658235863"),
                isDisplayed()
            )
        )
//        textInputEditText14.perform(pressImeActionButton())
//
//        pressBack()

        val materialButton7 = onView(
            allOf(
                withId(R.id.loginButton), withText("Sign Up !"),
                childAtPosition(
                    allOf(
                        withId(R.id.layout_signup),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        materialButton7.perform(click())

        val textInputEditText15 = onView(
            allOf(
                withId(R.id.prelimpassword),
                isDisplayed()
            )
        )
        textInputEditText15.perform(click())

        val textInputEditText16 = onView(
            allOf(
                withId(R.id.prelimpassword),
                isDisplayed()
            )
        )
        textInputEditText16.perform(replaceText("a"), closeSoftKeyboard())

        val textInputEditText17 = onView(
            allOf(
                withId(R.id.prelimpassword), withText("a"),
                isDisplayed()
            )
        )
        textInputEditText17.perform(pressImeActionButton())

        val textInputEditText18 = onView(
            allOf(
                withId(R.id.prelimpassword), withText("a"),
                isDisplayed()
            )
        )
        textInputEditText18.perform(replaceText("A"))

        val textInputEditText19 = onView(
            allOf(
                withId(R.id.prelimpassword), withText("A"),
                isDisplayed()
            )
        )
        textInputEditText19.perform(closeSoftKeyboard())

        val textInputEditText20 = onView(
            allOf(
                withId(R.id.prelimpassword), withText("A"),
                isDisplayed()
            )
        )
        textInputEditText20.perform(pressImeActionButton())

        val textInputEditText21 = onView(
            allOf(
                withId(R.id.prelimpassword), withText("A"),
                isDisplayed()
            )
        )
        textInputEditText21.perform(replaceText("Aa"))

        val textInputEditText22 = onView(
            allOf(
                withId(R.id.prelimpassword), withText("Aa"),
                isDisplayed()
            )
        )
        textInputEditText22.perform(closeSoftKeyboard())

        val textInputEditText23 = onView(
            allOf(
                withId(R.id.prelimpassword), withText("Aa"),
                isDisplayed()
            )
        )
        textInputEditText23.perform(pressImeActionButton())

        val textInputEditText24 = onView(
            allOf(
                withId(R.id.prelimpassword), withText("Aa"),
                isDisplayed()
            )
        )
        textInputEditText24.perform(replaceText("Aa0"))

        val textInputEditText25 = onView(
            allOf(
                withId(R.id.prelimpassword), withText("Aa0"),
                isDisplayed()
            )
        )
        textInputEditText25.perform(closeSoftKeyboard())

        val textInputEditText26 = onView(
            allOf(
                withId(R.id.prelimpassword), withText("Aa0"),
                isDisplayed()
            )
        )
        textInputEditText26.perform(pressImeActionButton())

        val textInputEditText27 = onView(
            allOf(
                withId(R.id.prelimpassword), withText("Aa0"),
                isDisplayed()
            )
        )
        textInputEditText27.perform(replaceText("Aa0!"))

        val textInputEditText28 = onView(
            allOf(
                withId(R.id.prelimpassword), withText("Aa0!"),
                isDisplayed()
            )
        )
        textInputEditText28.perform(closeSoftKeyboard())

        val textInputEditText29 = onView(
            allOf(
                withId(R.id.prelimpassword), withText("Aa0!"),
                isDisplayed()
            )
        )
        textInputEditText29.perform(pressImeActionButton())
//
//        pressBack()

        val materialButton8 = onView(
            allOf(
                withId(R.id.loginButton), withText("Sign Up !"),
                childAtPosition(
                    allOf(
                        withId(R.id.layout_signup),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        materialButton8.perform(click())

        val textInputEditText30 = onView(
            allOf(
                withId(R.id.prelimpassword), withText("Aa0!"),
                isDisplayed()
            )
        )
        textInputEditText30.perform(replaceText("Aa0!aaaa"))

        val textInputEditText31 = onView(
            allOf(
                withId(R.id.prelimpassword), withText("Aa0!aaaa"),
                isDisplayed()
            )
        )
        textInputEditText31.perform(closeSoftKeyboard())

        val textInputEditText32 = onView(
            allOf(
                withId(R.id.prelimpassword), withText("Aa0!aaaa"),
                isDisplayed()
            )
        )
        textInputEditText32.perform(pressImeActionButton())
//
//        pressBack()

        val materialButton9 = onView(
            allOf(
                withId(R.id.loginButton), withText("Sign Up !"),
                childAtPosition(
                    allOf(
                        withId(R.id.layout_signup),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        materialButton9.perform(click())

        val textInputEditText33 = onView(
            allOf(
                withId(R.id.password),
                isDisplayed()
            )
        )
        textInputEditText33.perform(click())

        val textInputEditText34 = onView(
            allOf(
                withId(R.id.password),
                isDisplayed()
            )
        )
        textInputEditText34.perform(replaceText("Aa0!aaaa"), closeSoftKeyboard())

        val textInputEditText35 = onView(
            allOf(
                withId(R.id.password), withText("Aa0!aaaa"),
                isDisplayed()
            )
        )
        textInputEditText35.perform(pressImeActionButton())

        textInputEditText32.perform(click())
        textInputEditText32.perform(pressImeActionButton())
        textInputEditText35.perform(pressImeActionButton())

        val materialButton10 = onView(
            allOf(
                withId(R.id.loginButton), withText("Sign Up !"),
                childAtPosition(
                    allOf(
                        withId(R.id.layout_signup),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        materialButton10.perform(click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
