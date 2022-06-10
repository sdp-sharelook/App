package com.github.sdpsharelook.storage

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdpsharelook.Word
import com.github.sdpsharelook.authorization.AuthProvider
import com.github.sdpsharelook.di.StorageModule
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.*
import com.google.gson.Gson
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
import org.junit.Assert
import org.junit.Assert.assertEquals
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
class RTDBWordListRepositoryTest {
    @BindValue
    @JvmField
    var db: FirebaseDatabase = mock(defaultAnswer = { reference })
    private val reference: DatabaseReference = mock(defaultAnswer = Answers.RETURNS_SELF) {
        on { setValue(any()) } doReturn Tasks.forResult(null)
        on { removeValue() } doReturn Tasks.forResult(null)
        on { addChildEventListener(any()) } doAnswer {
            lastFlowListener = it.arguments[0] as ChildEventListener
            lastFlowListener
        }
    }
    var lastFlowListener: ChildEventListener? = null
    private val snap: DataSnapshot = mock(defaultAnswer = {
        Gson().toJson(testVal).toString()
    })


    @Inject
    lateinit var auth: AuthProvider
    private val testString = "test"
    private val testVal = Word.testWord
    private lateinit var repo: RTDBWordListRepository
    private lateinit var testFlow: Flow<Result<List<Word>?>>

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        hiltRule.inject()
        repo = RTDBWordListRepository(db, auth)
        testFlow = repo.flow()
    }

    @Test
    fun `test flow`() = runTest(dispatchTimeoutMs = 5000) {
        val changed = testVal.copy(source = "test2")
        val changedSnap = mock<DataSnapshot>(defaultAnswer = {
            Gson().toJson(changed).toString()
        })
        val databaseExceptionMock = mock<DatabaseException> {
            on { message } doReturn testString
        }
        val databaseError = mock<DatabaseError> {
            on { toException() } doReturn databaseExceptionMock
        }
        var i = 0
        val job = testFlow
            .onEach { result ->
                result
                    .onSuccess { list ->
                        list!!.let {
                            when (i++) {
                                0 -> assertEquals(listOf(testVal), it)
//                                1 -> assertEquals(listOf(changed), it)
                                2 -> assertEquals(listOf(testVal), it)
                                3 -> assertEquals(listOf<Word>(), it)
                            }
                        }
                    }.onFailure {
                        assertEquals(testString, it.message) // 4
                    }
            }.launchIn(this)
        advanceUntilIdle()
        lastFlowListener!!.onChildAdded(snap, null) // 0
        advanceUntilIdle()
        lastFlowListener!!.onChildChanged(changedSnap, null) // 1
        advanceUntilIdle()
        lastFlowListener!!.onChildChanged(snap, null) // 2
        advanceUntilIdle()
        lastFlowListener!!.onChildMoved(snap, null) // Nothing
        advanceUntilIdle()
        lastFlowListener!!.onChildRemoved(snap) // 3
        advanceUntilIdle()
        lastFlowListener!!.onCancelled(databaseError) // 4
        advanceUntilIdle()
        job.cancel()
        advanceUntilIdle()
        verify(reference).removeEventListener(any<ChildEventListener>())
    }

    @Test
    fun `insert three words`() = runTest {
        val word = Word("test")
        repo.insert("test", listOf(word, word, word))
        verify(reference, times(3)).setValue(any())
    }

    @Test
    fun `test other functions`() = runTest {
        runCatching { repo.update(entity = listOf(testVal)) }
            .onSuccess { Assert.fail() }
            .onFailure { assertEquals(NotImplementedError::class.java, it.javaClass) }
        repo.insert(entity = listOf(testVal))
        verify(reference).setValue(any())
        repo.delete(words = listOf(testVal))
        verify(reference).removeValue()
    }
}