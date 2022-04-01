package com.github.sdpsharelook.storage

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdpsharelook.Word
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealTimeWordSaverDatabaseTest {

    var storedValue : Any? = null

    @Test
    @ExperimentalCoroutinesApi
    fun testSaving() = runTest {
        val uid = "1"
        val words = WordSaver(RealTimeWordSaverDatabase(uid, object : RTTRoot {
            override val reference: RTTRef
                get() = object : RTTRef {
                    override fun child(pathString: String): RTTRef {
                        assert(pathString == "users" || pathString == uid || pathString == "wordlists" || pathString == "favourites")
                        return this
                    }

                    override fun setValue(value: Any?): Task<Void?> {
                        storedValue = value
                        return Tasks.forResult(null)
                    }

                    override fun get(): Task<SnapshotProvider> {
                        return Tasks.forResult(object : SnapshotProvider {
                            override fun getValue(): Any? = storedValue
                        })
                    }

                }
        }))
        val key = "Test"
        val testWord = Word(
            key,
            "A procedure intended to establish the quality, performance, or reliability of something, especially before it is taken into widespread use.",
            true
        )
        words[key] = testWord
        words.save()
        words.clear()
        assert(words.isEmpty())
        words.fill()
        assert(words.containsKey(key))
        assertEquals(words[key]!!.name, key)
        assert(words[key]!!.isFavourite)
    }
}