package com.github.sdpsharelook.storage

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdpsharelook.authorization.AuthProvider
import com.github.sdpsharelook.di.StorageModule
import com.github.sdpsharelook.section.Section
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
class RTSectionRepoTest {
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
    private val testVal = Section("test")
    private val testString = "test"

    @Inject
    lateinit var auth: AuthProvider

    @Inject
    lateinit var repo: RTSectionRepo
    private lateinit var testFlow: Flow<Result<List<Section>?>>

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        hiltRule.inject()
        testFlow = repo.flow()
    }

    @Test
    fun `test flow`() = runTest {
        val changed = testVal.copy(title = testString)
        val changedSnap = mock<DataSnapshot>(defaultAnswer = {
            Gson().toJson(changed).toString()
        })
        val databaseError = mock<DatabaseError> {
            val databaseExceptionMock = mock<DatabaseException> {
                on { message } doReturn testString
            }
            on { toException() } doReturn databaseExceptionMock
        }
        var i = 0
        val job = testFlow
            .onEach { result ->
                result
                    .onSuccess {
                        when (i++) {
                            0 -> assertEquals(listOf(testVal), it!!)
                            1 -> assertEquals(listOf(changed), it!!)
                            2 -> assertEquals(listOf(testVal), it!!)
                            3 -> assertEquals(listOf<Section>(), it!!)
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
        lastFlowListener!!.onChildMoved(snap, null) // nothing
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
    fun `test other functions`() = runTest {
        runCatching { repo.update(entity = listOf(testVal)) }
            .onSuccess { Assert.fail() }
            .onFailure { assertEquals(NotImplementedError::class.java, it.javaClass) }
        repo.insert(entity = listOf(testVal))
        verify(reference).setValue(any())
        repo.delete(entity = listOf(testVal))
        verify(reference).removeValue()
    }
}