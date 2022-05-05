package com.github.sdpsharelook.storage

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdpsharelook.Word
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WordSaverDatabaseTestRobo {
    val uid = "test"
    private val key = "Test"
    private val repository = object : IRepository<List<String>> {
        lateinit var stored: Set<String>

        override fun flow(name: String): Flow<Result<List<String>?>> {
            assertEquals(uid, name)
            return flowOf(Result.success(stored.toList()))
        }

        override suspend fun insert(name: String, entity: List<String>) {
            assertEquals(uid, name)
            stored = stored + entity
        }

        override suspend fun read(name: String): List<String> {
            assertEquals(uid, name)
            return stored.toList()
        }

        override suspend fun update(name: String, entity: List<String>) {
            assertEquals(uid, name)
            stored = entity.toSet()
        }

        override suspend fun delete(name: String) {
            assertEquals(uid, name)
            stored = emptySet()
        }
    }

    private val testWord = Word(
        key,
        "A procedure intended to establish the quality, performance, or reliability of something, especially before it is taken into widespread use.",
        true
    )

    @Before
    fun setUp() {
        repository.stored = emptySet()
    }

    @Test
    @ExperimentalCoroutinesApi
    fun `words saved in realtime database can be retrieved`() = runTest {
        val wordSaverDatabase = RealTimeWordSaverDatabase(uid, repository)
        val words = WordSaver(wordSaverDatabase)
        words[key] = testWord
        words.save()
        words.clear()
        assert(words.isEmpty())
        words.fill()
        assert(words.containsKey(key))
        assertEquals(words[key]!!.name, key)
        assert(words[key]!!.isFavourite)
    }

    @Test
    @ExperimentalCoroutinesApi
    fun `words saved in internal storage can be retrieved`() = runTest {
        val wordSaverDatabase =
            InternalFileWordSaverDatabase(ApplicationProvider.getApplicationContext())
        val words = WordSaver(wordSaverDatabase)
        words[key] = testWord
        words.save()
        words.clear()
        assert(words.isEmpty())
        words.fill()
        assert(words.containsKey(key))
        assertEquals(key, words[key]!!.name)
        assert(words[key]!!.isFavourite)
    }
}