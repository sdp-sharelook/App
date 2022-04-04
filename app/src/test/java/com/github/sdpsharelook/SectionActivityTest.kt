package com.github.sdpsharelook


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.github.sdpsharelook.Section.SectionActivity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class SectionActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityScenarioRule(SectionActivity::class.java)

    @Test
    fun sectionActivityTest() {

        val floatingActionButton = onView(
            allOf(
                withId(R.id.addingBtn),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        floatingActionButton.perform(click())

        val appCompatEditText = onView(
            allOf(
                withId(R.id.edit_section_name), withText("Section name"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatEditText.perform(replaceText("cuisine"))

        val appCompatEditText2 = onView(
            allOf(
                withId(R.id.edit_section_name), withText("cuisine"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatEditText2.perform(closeSoftKeyboard())

//        val appCompatSpinner = onView(
//            allOf(
//                withId(R.id.spinner_countries), withContentDescription("ShareLook"),
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
//        appCompatSpinner.perform(click())
//
//        val relativeLayout = onData(anything())
//            .inAdapterView(
//                childAtPosition(
//                    withClassName(`is`("android.widget.PopupWindow$PopupBackgroundView")),
//                    0
//                )
//            )
//            .atPosition(1)
//        relativeLayout.perform(click())

        val materialButton2 = onView(
            allOf(
                withId(R.id.popup_add_btn), withText("Save"),
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

        val textView = onView(
            allOf(
                withId(R.id.sectionTitle), withText("cuisine"),
                withParent(withParent(withId(R.id.cardView))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("cuisine")))

        val imageView = onView(
            allOf(
                withId(R.id.sectionFlag),
                withParent(withParent(withId(R.id.cardView))),
                isDisplayed()
            )
        )
        imageView.check(matches(isDisplayed()))

        val imageButton = onView(
            allOf(
                withId(R.id.editButton),
                withParent(withParent(withId(R.id.cardView))),
                isDisplayed()
            )
        )
        imageButton.check(matches(isDisplayed()))

        val imageButton2 = onView(
            allOf(
                withId(R.id.deleteButton),
                withParent(withParent(withId(R.id.cardView))),
                isDisplayed()
            )
        )
        imageButton2.check(matches(isDisplayed()))

        val imageButton3 = onView(
            allOf(
                withId(R.id.addingBtn),
                withParent(withParent(withId(android.R.id.content))),
                isDisplayed()
            )
        )
        imageButton3.check(matches(isDisplayed()))

        val appCompatImageButton = onView(
            allOf(
                withId(R.id.editButton),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.cardView),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatImageButton.perform(click())

        val appCompatEditText3 = onView(
            allOf(
                withId(R.id.edit_section_name), withText("cuisine"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatEditText3.perform(click())

        val appCompatEditText4 = onView(
            allOf(
                withId(R.id.edit_section_name), withText("cuisine"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatEditText4.perform(replaceText("salon"))

        val appCompatEditText5 = onView(
            allOf(
                withId(R.id.edit_section_name), withText("salon"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatEditText5.perform(closeSoftKeyboard())

//        val appCompatSpinner2 = onView(
//            allOf(
//                withId(R.id.spinner_countries), withContentDescription("ShareLook"),
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
//        appCompatSpinner2.perform(click())
//
//        val relativeLayout2 = onData(anything())
//            .inAdapterView(
//                childAtPosition(
//                    withClassName(`is`("android.widget.PopupWindow$PopupBackgroundView")),
//                    0
//                )
//            )
//            .atPosition(0)
//        relativeLayout2.perform(click())

        val materialButton3 = onView(
            allOf(
                withId(R.id.popup_add_btn), withText("Save"),
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
        materialButton3.perform(click())

        val textView2 = onView(
            allOf(
                withId(R.id.sectionTitle), withText("salon"),
                withParent(withParent(withId(R.id.cardView))),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("salon")))
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
