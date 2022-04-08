package com.github.sdpsharelook.language

import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import java.util.*

class Matchers {
    companion object {
        fun withTag(tagMatcher: Matcher<String>): Matcher<Language> =
            object : TypeSafeMatcher<Language>() {
                override fun describeTo(description: Description?) {

                }

                override fun matchesSafely(item: Language?): Boolean =
                    item?.let {
                        tagMatcher.matches(it.tag)
                    } ?: false
            }

        fun withDisplayName(nameMatcher: Matcher<String>): Matcher<Language> =
            object : TypeSafeMatcher<Language>() {
                override fun describeTo(description: Description?) {}

                override fun matchesSafely(item: Language?): Boolean =
                    item?.let {
                        nameMatcher.matches(it.displayName)
                    } ?: false
            }

        fun withLocale(localeMatcher: Matcher<Locale>): Matcher<Language> =
            object : TypeSafeMatcher<Language>() {
                override fun describeTo(description: Description?) {}

                override fun matchesSafely(item: Language?): Boolean =
                    item?.let {
                        localeMatcher.matches(it.locale)
                    } ?: false
            }

        fun isEquals(language: Language) =
            object : TypeSafeMatcher<Language>() {
                override fun describeTo(description: Description?) {}

                override fun matchesSafely(item: Language?): Boolean =
                    item?.let {
                        it == language
                    } ?: false
            }
    }
}