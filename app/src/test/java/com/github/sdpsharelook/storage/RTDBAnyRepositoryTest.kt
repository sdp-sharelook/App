package com.github.sdpsharelook.storage

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdpsharelook.di.StorageModule
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Answers
import org.mockito.kotlin.*
import javax.inject.Inject

@UninstallModules(StorageModule::class)
@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class RTDBAnyRepositoryTest {
    @BindValue
    val db: FirebaseDatabase = mock(defaultAnswer = { ref })
    private val ref: DatabaseReference = mock(defaultAnswer = Answers.RETURNS_SELF) {
        on { addValueEventListener(any()) } doAnswer {
            lastFlowListener = it.arguments[0] as ValueEventListener
            lastFlowListener
        }
    }
    var lastFlowListener: ValueEventListener? = null
    private val testString = "test"

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repo: RTDBAnyRepository

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun flow() = runTest {
        val failure: DatabaseException = mock()
        val testFlow = repo.flow()
        val job = testFlow
            .onEach { result ->
                result
                    .onSuccess {
                        assertEquals(testString, it!!)
                    }.onFailure {
                        assertSame(failure, it)
                    }
            }.launchIn(this)
        advanceUntilIdle()
        lastFlowListener!!.onDataChange(mock(defaultAnswer = { testString }))
        advanceUntilIdle()
        lastFlowListener!!.onCancelled(mock {
            on {message} doReturn testString
            on {toException()} doReturn failure
        })
        advanceUntilIdle()
        job.cancel()
        advanceUntilIdle()
        verify(ref).removeEventListener(any<ValueEventListener>())
    }

    @Test
    fun `test functionalities not implemented`() = runTest {
        runCatching { repo.insert(entity = "test") }
            .onSuccess { fail() }
            .onFailure { assertEquals(NotImplementedError::class.java, it.javaClass) }
        runCatching { repo.update(entity = "test") }
            .onSuccess { fail() }
            .onFailure { assertEquals(NotImplementedError::class.java, it.javaClass) }
        runCatching { repo.read() }
            .onSuccess { fail() }
            .onFailure { assertEquals(NotImplementedError::class.java, it.javaClass) }
        runCatching { repo.delete() }
            .onSuccess { fail() }
            .onFailure { assertEquals(NotImplementedError::class.java, it.javaClass) }
    }
}