package com.github.sdpsharelook.language


import org.junit.Assert
import org.junit.Test
import java.util.*


class LanguageTest {
    @Test
    fun testLanguage() {
        val locale=Locale.ENGLISH
        val tag = locale.toLanguageTag()
        val l = Language(tag)
        Assert.assertEquals(tag, l.tag)
        Assert.assertEquals(locale, l.locale)
        Assert.assertEquals(locale.displayName, l.displayName)
    }
}