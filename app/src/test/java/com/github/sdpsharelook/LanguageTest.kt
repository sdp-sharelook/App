package com.github.sdpsharelook


import com.github.sdpsharelook.language.Language
import org.junit.Assert
import org.junit.Test
import java.util.*


class LanguageTest {
    @Test
    fun testLanguage() {
        val locale=Locale.ENGLISH
        val tag = locale.toLanguageTag()
        val l = Language(tag)
        Assert.assertEquals(l.tag, tag)
        Assert.assertEquals(l.locale, locale)
        Assert.assertEquals(l.displayName, locale.displayName)
    }
}