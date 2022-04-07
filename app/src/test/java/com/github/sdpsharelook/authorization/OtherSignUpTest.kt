package com.github.sdpsharelook.authorization


import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.intent.Intents
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
class PL {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun pL() {
        Intents.init()
        val materialButton = onView(
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
        materialButton.perform(click())

        val materialButton2 = onView(
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
        materialButton2.perform(click())

        //assert(!Intents.getIntents().isEmpty())
        Log.d("assert", Intents.getIntents().toString())
        //assert(Intents.getIntents().toString() == "cmp=com.github.sdpsharelook/.authorization.SignUpActivity")

        val textInputEditText = onView(
            allOf(
                withId(R.id.firstName),
                isDisplayed()
            )
        )
        textInputEditText.perform(click())

        val textInputEditText2 = onView(
            allOf(
                withId(R.id.firstName),
                isDisplayed()
            )
        )
        textInputEditText2.perform(replaceText("kjhgkjh"), closeSoftKeyboard())

        val textInputEditText3 = onView(
            allOf(
                withId(R.id.firstName), withText("kjhgkjh"),
                isDisplayed()
            )
        )
        textInputEditText3.perform(pressImeActionButton())

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

//        assert(Intents.getIntents() == null)


        val textInputEditText4 = onView(
            allOf(
                withId(R.id.lastName),
                isDisplayed()
            )
        )
        textInputEditText4.perform(click())

        val textInputEditText5 = onView(
            allOf(
                withId(R.id.lastName),
                isDisplayed()
            )
        )
        textInputEditText5.perform(replaceText("kjbkjbh"), closeSoftKeyboard())

        val textInputEditText6 = onView(
            allOf(
                withId(R.id.lastName), withText("kjbkjbh"),
                isDisplayed()
            )
        )
        textInputEditText6.perform(pressImeActionButton())

        val textInputEditText7 = onView(
            allOf(
                withId(R.id.email),
                isDisplayed()
            )
        )
        textInputEditText7.perform(replaceText("kjhgkjh"), closeSoftKeyboard())

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

//        assert(Intents.getIntents() == null)


        val textInputEditText8 = onView(
            allOf(
                withId(R.id.email), withText("kjhgkjh"),
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
        textInputEditText8.perform(click())

        val textInputEditText9 = onView(
            allOf(
                withId(R.id.email), withText("kjhgkjh"),
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
        textInputEditText9.perform(replaceText("kjhgkjh@kjghkjh.kfjg"))

        val textInputEditText10 = onView(
            allOf(
                withId(R.id.email), withText("kjhgkjh@kjghkjh.kfjg"),
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
        textInputEditText10.perform(closeSoftKeyboard())

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

//        assert(Intents.getIntents() == null)

        val textInputEditText11 = onView(
            allOf(
                withId(R.id.phoneNumber),
                isDisplayed()
            )
        )
        textInputEditText11.perform(replaceText("5544554455"), closeSoftKeyboard())

        val textInputEditText12 = onView(
            allOf(
                withId(R.id.phoneNumber), withText("5544554455"),
                isDisplayed()
            )
        )
        textInputEditText12.perform(click())

        val textInputEditText13 = onView(
            allOf(
                withId(R.id.phoneNumber), withText("5544554455"),
                isDisplayed()
            )
        )
        textInputEditText13.perform(pressImeActionButton())

        val textInputEditText14 = onView(
            allOf(
                withId(R.id.preliminary_password),
                isDisplayed()
            )
        )
        textInputEditText14.perform(replaceText("Aaaaaaaaa"), closeSoftKeyboard())

        val textInputEditText15 = onView(
            allOf(
                withId(R.id.preliminary_password), withText("Aaaaaaaaa"),
                isDisplayed()
            )
        )
        textInputEditText15.perform(pressImeActionButton())

        val textInputEditText16 = onView(
            allOf(
                withId(R.id.password),
                isDisplayed()
            )
        )
        textInputEditText16.perform(replaceText("Aaaaaaaaa"), closeSoftKeyboard())

        val textInputEditText17 = onView(
            allOf(
                withId(R.id.password), withText("Aaaaaaaaa"),
                isDisplayed()
            )
        )
        textInputEditText17.perform(pressImeActionButton())

        val materialButton6 = onView(
            allOf(
                withId(R.id.loginButton), withText("Sign Up !"),
                isDisplayed()
            )
        )
        materialButton6.perform(click())

//        assert(Intents.getIntents() == null)


        val textInputEditText18 = onView(
            allOf(
                withId(R.id.preliminary_password), withText("Aaaaaaaaa"),
                isDisplayed()
            )
        )
        textInputEditText18.perform(replaceText("Aaaaaaaaa9"))

        val textInputEditText19 = onView(
            allOf(
                withId(R.id.preliminary_password), withText("Aaaaaaaaa9"),
                isDisplayed()
            )
        )
        textInputEditText19.perform(closeSoftKeyboard())

        val textInputEditText20 = onView(
            allOf(
                withId(R.id.preliminary_password), withText("Aaaaaaaaa9"),
                isDisplayed()
            )
        )
        textInputEditText20.perform(pressImeActionButton())

        val textInputEditText21 = onView(
            allOf(
                withId(R.id.password), withText("Aaaaaaaaa"),
                isDisplayed()
            )
        )
        textInputEditText21.perform(pressImeActionButton())

        val materialButton7 = onView(
            allOf(
                withId(R.id.loginButton), withText("Sign Up !"),
                isDisplayed()
            )
        )
        materialButton7.perform(click())

//        assert(Intents.getIntents() == null)

        val textInputEditText22 = onView(
            allOf(
                withId(R.id.password), withText("Aaaaaaaaa"),
                isDisplayed()
            )
        )
        textInputEditText22.perform(replaceText("Aaaaaaaaa9"))

        val textInputEditText23 = onView(
            allOf(
                withId(R.id.password), withText("Aaaaaaaaa9"),
                isDisplayed()
            )
        )
        textInputEditText23.perform(closeSoftKeyboard())

        val textInputEditText24 = onView(
            allOf(
                withId(R.id.password), withText("Aaaaaaaaa9"),
                isDisplayed()
            )
        )
        textInputEditText24.perform(pressImeActionButton())

        val materialButton8 = onView(
            allOf(
                withId(R.id.loginButton), withText("Sign Up !"),
                isDisplayed()
            )
        )
        materialButton8.perform(click())

//        assert(Intents.getIntents() == null)

        val textInputEditText25 = onView(
            allOf(
                withId(R.id.preliminary_password), withText("Aaaaaaaaa9"),
                isDisplayed()
            )
        )
        textInputEditText25.perform(replaceText("Aaaaaaaaa9!"))

        val textInputEditText26 = onView(
            allOf(
                withId(R.id.preliminary_password), withText("Aaaaaaaaa9!"),
                isDisplayed()
            )
        )
        textInputEditText26.perform(closeSoftKeyboard())

        val textInputEditText27 = onView(
            allOf(
                withId(R.id.preliminary_password), withText("Aaaaaaaaa9!"),
                isDisplayed()
            )
        )
        textInputEditText27.perform(pressImeActionButton())

        val textInputEditText28 = onView(
            allOf(
                withId(R.id.password), withText("Aaaaaaaaa9"),
                isDisplayed()
            )
        )
        textInputEditText28.perform(replaceText("Aaaaaaaaa9!"))

        val textInputEditText29 = onView(
            allOf(
                withId(R.id.password), withText("Aaaaaaaaa9!"),
                isDisplayed()
            )
        )
        textInputEditText29.perform(closeSoftKeyboard())

        val textInputEditText30 = onView(
            allOf(
                withId(R.id.password), withText("Aaaaaaaaa9!"),
                isDisplayed()
            )
        )
        textInputEditText30.perform(pressImeActionButton())

        val materialButton9 = onView(
            allOf(
                withId(R.id.loginButton), withText("Sign Up !"),
                isDisplayed()
            )
        )
        materialButton9.perform(click())
//        assert(Intents.getIntents() == null)

        Intents.release()

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
