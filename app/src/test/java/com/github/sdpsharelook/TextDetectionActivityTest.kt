package com.github.sdpsharelook


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.github.sdpsharelook.textDetection.TextDetectionActivity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class TextDetectionActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityScenarioRule(TextDetectionActivity::class.java)

    @Test
    fun textDetectionActivityTest() {

        val detectButton = onView(
            allOf(
                withId(R.id.detectButton), isDisplayed()
            )
        )
        detectButton.check(matches(isNotEnabled()))

        val materialButton2 = onView(
            allOf(
                withId(R.id.captureButton), withText("Capture"),
                childAtPosition(
                    allOf(
                        withId(R.id.linearHolder),
                        childAtPosition(
                            withClassName(`is`("android.widget.RelativeLayout")),
                            2
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialButton2.perform(click())

        detectButton.check(matches(isEnabled()))

        val textView = onView(
            allOf(
                withId(R.id.text_data), withText("Detect text on the image"),
                withParent(withParent(withId(android.R.id.content))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Detect text on the image")))

        val button = onView(
            allOf(
                withId(R.id.detectButton), withText("DETECT"),
                withParent(
                    allOf(
                        withId(R.id.linearHolder),
                        withParent(IsInstanceOf.instanceOf(android.widget.RelativeLayout::class.java))
                    )
                ),
                isDisplayed()
            )
        )

        val materialButton3 = onView(
            allOf(
                withId(R.id.detectButton), withText("Detect"),
                childAtPosition(
                    allOf(
                        withId(R.id.linearHolder),
                        childAtPosition(
                            withClassName(`is`("android.widget.RelativeLayout")),
                            2
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        materialButton3.perform(click())

        val textView2 = onView(
            allOf(
                withId(R.id.text_data),
                withParent(withParent(withId(android.R.id.content))),
                isDisplayed()
            )
        )
        textView2.check(matches(isDisplayed()))
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
