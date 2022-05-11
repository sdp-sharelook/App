package com.github.sdpsharelook.storage

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdpsharelook.Word
import com.github.sdpsharelook.authorization.AuthProvider
import com.github.sdpsharelook.di.StorageModule
import com.github.sdpsharelook.language.Language
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Answers
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import javax.inject.Inject


@UninstallModules(StorageModule::class)
@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class RTDBWordListRepositoryTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    lateinit var lastFlowListener: ChildEventListener
    private val reference: DatabaseReference = mock(defaultAnswer = Answers.RETURNS_SELF) {
        on { setValue(any()) } doReturn Tasks.forResult(null)
        on { addChildEventListener(any()) } doAnswer {
            lastFlowListener = it.arguments[0] as ChildEventListener
            lastFlowListener
        }
    }
    private val word1 = Word(
        "test",
        "Guten Tag",
        Language("German"),
        "Bonjour",
        Language("Francais"),
        null,
        null,
        null,
        true
    )
    private val snapWord: DataSnapshot = mock(defaultAnswer = {
        Gson().toJson(word1).toString()
    })

    @BindValue
    @JvmField
    var db: FirebaseDatabase = mock {
        on { reference } doReturn reference
        on { getReference(any()) } doReturn reference
    }

    @Inject
    lateinit var auth: AuthProvider
    private lateinit var repo: RTDBWordListRepository
    private lateinit var testFlow: Flow<Result<List<Word>?>>

    @Before
    fun setUp() {
        hiltRule.inject()
        repo = RTDBWordListRepository(db, auth)
        testFlow = repo.flow()
    }

    @Test
    fun `test flow`() = runTest(dispatchTimeoutMs = 5000) {
        testFlow.launchIn(this)
        advanceUntilIdle()
        lastFlowListener.onChildAdded(snapWord, null)
        assertEquals(1, testFlow.count())
    }

    @Test
    fun `insert three words`() = runTest {
        val word = Word("test")
        repo.insert(entity = listOf(word, word, word))
    }
}