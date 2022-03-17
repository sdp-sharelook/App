package com.github.sdpsharelook


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
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
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun sectionActivityTest() {
        val materialButton = onView(
            allOf(
                withId(R.id.mainGoButton), withText("Button"),
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
        materialButton.perform(click())

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

        val editText = onView(
            allOf(
                withId(R.id.edit_section_name), withText("Section name"),
                withParent(withParent(withId(android.R.id.content))),
                isDisplayed()
            )
        )
        editText.check(matches(withText("Section name")))

        val spinner = onView(
            allOf(
                withId(R.id.spinner_countries), withContentDescription("ShareLook"),
                withParent(withParent(withId(android.R.id.content))),
                isDisplayed()
            )
        )
        spinner.check(matches(isDisplayed()))

        val button = onView(
            allOf(
                withId(R.id.popup_add_btn), withText("ADD"),
                withParent(withParent(withId(android.R.id.content))),
                isDisplayed()
            )
        )
        button.check(matches(isDisplayed()))

        val materialButton2 = onView(
            allOf(
                withId(R.id.popup_add_btn), withText("Add"),
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
                withId(R.id.sectionTitle), withText("kitchen"),
                withParent(withParent(withId(R.id.cardView))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("kitchen")))

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

        val cardView = onView(
            allOf(
                withId(R.id.cardView),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.recyclerView),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        cardView.perform(click())

        val textView2 = onView(
            allOf(
                withId(R.id.sectionTitle), withText("kitchen"),
                withParent(withParent(withId(android.R.id.content))),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("kitchen")))

        val imageView2 = onView(
            allOf(
                withId(R.id.sectionFlag),
                withParent(withParent(withId(android.R.id.content))),
                isDisplayed()
            )
        )
        imageView2.check(matches(isDisplayed()))
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
