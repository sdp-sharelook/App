package com.github.sdpsharelook.storage

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdpsharelook.Word
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InternalFileWordSaverDatabaseTest {
    @Test
    fun empty() {

    }

//    @Test
//    @ExperimentalCoroutinesApi
//    fun testSaving() = runTest {
//        val words = WordSaver(InternalFileWordSaverDatabase(ApplicationProvider.getApplicationContext()))
//        val key = "Test"
//        val testWord = Word(
//            key,
//            "A procedure intended to establish the quality, performance, or reliability of something, especially before it is taken into widespread use.",
//            true
//        )
//        words[key] = testWord
//        words.save()
//        words.clear()
//        assert(words.isEmpty())
//        words.fill()
//        assert(words.containsKey(key))
//        assertEquals(key, words[key]!!.name)
//        assert(words[key]!!.isFavourite)
//    }
}