package com.github.sdpsharelook.storage

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdpsharelook.di.StorageModule
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.*
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Answers
import org.mockito.kotlin.*


@UninstallModules(StorageModule::class)
@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class RTDBStringListRepositoryTest {
    @BindValue
    var db: FirebaseDatabase = mock(defaultAnswer = { reference })
    private val reference: DatabaseReference = mock(defaultAnswer = Answers.RETURNS_SELF) {
        on { setValue(any()) } doReturn Tasks.forResult(null)
        on { addChildEventListener(any()) } doAnswer {
            lastFlowListener = it.arguments[0] as ChildEventListener
            lastFlowListener
        }
    }
    var lastFlowListener: ChildEventListener? = null
    private val snapWord: DataSnapshot = mock(defaultAnswer = { testString })


    private val testString = "test"
    private lateinit var repo: RTDBStringListRepository
    private lateinit var testFlow: Flow<Result<List<String>?>>

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        hiltRule.inject()
        repo = RTDBStringListRepository(db)
        testFlow = repo.flow()
    }

    @Test
    fun `test flow`() = runTest(dispatchTimeoutMs = 5000) {
        val job = testFlow
            .onEach { result ->
                result
                    .onSuccess {
                        assertEquals(testString, it!!.first())
                    }.onFailure {
                        assertEquals(testString,it.message)
                    }
            }.launchIn(this)
        advanceUntilIdle()
        lastFlowListener!!.onChildAdded(snapWord, null)
        advanceUntilIdle()
        lastFlowListener!!.onChildChanged(snapWord, null)
        advanceUntilIdle()
        lastFlowListener!!.onChildRemoved(snapWord)
        advanceUntilIdle()
        lastFlowListener!!.onChildMoved(snapWord, null)
        advanceUntilIdle()
        lastFlowListener!!.onCancelled(mock {
            val databaseExceptionMock = mock<DatabaseException> {
                on { message } doReturn testString
            }
            on { toException() } doReturn databaseExceptionMock
        })
        advanceUntilIdle()
        job.cancel()
        advanceUntilIdle()
        verify(reference).removeEventListener(any<ChildEventListener>())
    }

    @Test
    fun `insert three strings`() = runTest {
        repo.insert(entity = listOf(testString, testString, testString))
        verify(reference, times(3)).setValue(any())
    }
}