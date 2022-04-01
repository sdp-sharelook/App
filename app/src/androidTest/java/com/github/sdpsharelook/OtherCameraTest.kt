package com.github.sdpsharelook


import android.graphics.Bitmap
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.reflect.Type

@LargeTest
@RunWith(AndroidJUnit4::class)
class OtherCameraTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Rule
    @JvmField
    var mGrantPermissionRule =
        GrantPermissionRule.grant(
            "android.permission.CAMERA"
        )

    @Test
    fun mainActivityTest3() {
        val materialButton = onView(
            allOf(
                withId(R.id.button), withText("camera"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                        0
                    ),
                    10
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())
        Intents.init()
        val materialButton2 = onView(
            allOf(
                withId(R.id.buttonTakePic), withText("Take Picture"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialButton2.perform(click())
        intended(IntentMatchers.hasAction("android.media.action.IMAGE_CAPTURE"))
        Intents.release()
        //pressBack()


        //After we inject the image we see whether it's correctly handled
        //and put in the imageView
    }

    @Test
    fun activityResultTest {
        val expectedResult = Bitmap.createBitmap(1, 1, Bitmap.Config.RGBA_F16)

        val testRegistry = object : ActivityResultRegistry() {
            override fun <Uri, Boolean> onLaunch(
                requestCode: Int,
                contract: ActivityResultContract<Uri, Boolean>,
                uri: Uri
            ) {
                dispatchResult(requestCode, expectedResult)
            }
        }

    }



    private fun withDrawable(resourceId: Int) : TypeSafeMatcher<View> {
        return drawable(resourceId)
    }

    private fun noDrawable() : TypeSafeMatcher<View> {
        return drawable(-1)
    }

    private fun drawable(resourceId: Int) = object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description) {
            description.appendText("Check there is something in imageView, not content though")
        }

        override fun matchesSafely(view: View): Boolean {
            val context = view.context
            var ppp = true
            val imageView = view as ImageView
            ppp = if (resourceId < 0) {
                imageView.drawable == null
            } else {
                imageView.drawable != null
            }
            return view is ImageView && ppp
        }
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
