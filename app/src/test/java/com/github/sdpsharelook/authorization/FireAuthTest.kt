package com.github.sdpsharelook.authorization

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdpsharelook.di.AuthModule
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.*
import javax.inject.Inject

@UninstallModules(AuthModule::class)
@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class FireAuthTest {
    private var succeed = true

    @BindValue
    val mockAuth: FirebaseAuth = mock {
        on { currentUser } doAnswer { mockFBUser.let { println(it);it } }
        val authResult: AuthResult = mock {
            on { user } doAnswer {
                println(succeed)
                if (succeed) mockFBUser
                else null
            }
        }
        on { signInWithEmailAndPassword(any(), any()) } doReturn Tasks.forResult(authResult)
        on { createUserWithEmailAndPassword(any(), any()) } doReturn Tasks.forResult(authResult)

    }
    private val mockFBUser: FirebaseUser = mock {
        on { uid } doReturn "test"
        on { email } doReturn "test@email.lol"
        on { displayName } doReturn "test"
        on { isAnonymous } doReturn false
    }

    @Inject
    lateinit var auth: FireAuth

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        hiltRule.inject()
        succeed = true
    }

    @After
    fun tearDown() {
        auth.signOut()
    }

    @Test
    fun `test fire signup`() = runTest {
        val result = auth.createUserWithEmailAndPassword("test", "test")
        val user = auth.currentUser
        advanceUntilIdle()
        assertTrue(result.isSuccess)
        assertNotNull(user)
        assertEquals(result.getOrThrow(), user)
    }

    @Test
    fun `test fire login`() = runTest {
        val result = auth.signInWithEmailAndPassword("test", "test")
        val user = auth.currentUser
        advanceUntilIdle()
        assertTrue(result.isSuccess)
        assertNotNull(user)
        assertNotNull(auth.currentUser)
    }

    @Test
    fun `test fire fails`() = runTest {
        succeed = false
        val result = auth.signInWithEmailAndPassword("test", "test")
        assertTrue(result.isFailure)
    }

    @Test
    fun `test sign out`() {
        auth.signOut()
        verify(mockAuth).signOut()
    }
}