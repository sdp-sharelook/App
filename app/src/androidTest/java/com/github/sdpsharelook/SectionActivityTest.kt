package com.github.sdpsharelook

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class SectionActivityTest {

    @Before
    fun init() {
        launchFragmentInContainer<SectionFragment>(Bundle(), R.style.Theme_Sherlook)
    }

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
