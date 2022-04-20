package com.github.sdpsharelook


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import com.github.sdpsharelook.camera.CameraActivity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class IncompleteCameraTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityScenarioRule(CameraActivity::class.java)

    @Rule
    @JvmField
    var mGrantPermissionRule =
        GrantPermissionRule.grant(
            "android.permission.CAMERA"
        )

    @Test
    fun incompleteCameraTest() {
        val materialButton3 = onView(
            allOf(
                withId(R.id.buttonTakePic), withText("Capture"),
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
        materialButton3.perform(click())

//        val materialButton3 = onView(
//            allOf(
//                withId(R.id.buttonTakePic), withText("Take Picture"),
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
//        materialButton3.perform(click())
//
//        pressBack()
//
//        val materialButton4 = onView(
//            allOf(
//                withId(R.id.cameraButton), withText("camera"),
//                childAtPosition(
//                    allOf(
//                        withId(R.id.linearLayout),
//                        childAtPosition(
//                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
//                            1
//                        )
//                    ),
//                    12
//                ),
//                isDisplayed()
//            )
//        )
//        materialButton4.perform(click())
//
//        val materialButton5 = onView(
//            allOf(
//                withId(R.id.buttonTakePic), withText("Take Picture"),
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
//        materialButton5.perform(click())
//
//        val materialButton6 = onView(
//            allOf(
//                withId(android.R.id.button1), withText("Button"),
//                childAtPosition(
//                    childAtPosition(
//                        withClassName(`is`("android.widget.ScrollView")),
//                        0
//                    ),
//                    3
//                )
//            )
//        )
//        materialButton6.perform(scrollTo(), click())
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
