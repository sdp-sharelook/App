package com.github.sdpsharelook

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.GrantPermissionRule
import com.github.sdpsharelook.camera.CameraFragment
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class OtherCameraTest {

    @Before
    fun init() {
        launchFragmentInContainer<CameraFragment>(Bundle(), R.style.Theme_Sherlook)
    }

    @Rule
    @JvmField
    var mGrantPermissionRule =
        GrantPermissionRule.grant(
            "android.permission.CAMERA"
        )

    @Test
    fun mainActivityTest3() {

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
        assert(Intents.getIntents().size == 1)
        Intents.release()
        assert(1+1 == 2)

        //After we inject the image we see whether it's correctly handled
        //and put in the imageView
    }

//    @Test
//    fun activityResultTest {
//        // Create an expected result Bitmap
//        val expectedResult = Bitmap.createBitmap(1, 1, Bitmap.Config.RGBA_F16)
//
//        // Create the test ActivityResultRegistry
//        val testRegistry = object : ActivityResultRegistry() {
//            override fun <Uri, Boolean> onLaunch(
//                requestCode: Int,
//                contract: ActivityResultContract<Uri, Boolean>,
//                input: Uri,
//                options: ActivityOptionsCompat?
//            ) {
//                dispatchResult(requestCode, expectedResult)
//            }
//        }
//
//    }



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
            val imageView = view as ImageView
            return if (resourceId < 0) imageView.drawable == null else imageView.drawable != null
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
