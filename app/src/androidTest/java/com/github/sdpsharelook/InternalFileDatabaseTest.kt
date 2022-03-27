package com.github.sdpsharelook

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InternalFileDatabaseTest {

    @Test
    fun testSaving() {
        val words = WordSaver(InternalFileDatabase(ApplicationProvider.getApplicationContext()))
        val testWord = Word(
            "Test",
            "A procedure intended to establish the quality, performance, or reliability of something, especially before it is taken into widespread use.",
            true
        )
        words["Test"] = testWord
        words.save()
        words.clear()
        assert(words.isEmpty())
        words.fill()
        assert(words.containsKey("Test"))
        assert(words["Test"]!!.name.equals("Test"))
    }
}