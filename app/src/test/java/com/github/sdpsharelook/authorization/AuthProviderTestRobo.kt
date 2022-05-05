package com.github.sdpsharelook.authorization

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdpsharelook.authorization.TestUserConstants.TEST_USER_EMAIL
import com.github.sdpsharelook.authorization.TestUserConstants.TEST_USER_PASS
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class AuthProviderTestRobo {

    @Inject
    lateinit var auth: AuthProvider

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun resetAuthProvider(){
        hiltRule.inject()
    }

    @Test
    fun testLogin() = runTest {
        auth.signInWithEmailAndPassword(TEST_USER_EMAIL, TEST_USER_PASS)
        assert(auth.currentUser!!.email==TEST_USER_EMAIL)
        auth.signOut()
        assert(auth.currentUser==null)
    }

    @Test
    fun testInvalidLogin() = runTest {
        val result = auth.signInWithEmailAndPassword(TEST_USER_EMAIL, "")
        assert(result.isFailure)
    }

    @Test
    fun signUpAlreadyUsedEmail() = runTest{
        val result = auth.createUserWithEmailAndPassword(TEST_USER_EMAIL, TEST_USER_PASS)
        assert(result.isFailure)
    }
}