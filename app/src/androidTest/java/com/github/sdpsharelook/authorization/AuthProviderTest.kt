package com.github.sdpsharelook.authorization

import com.github.sdpsharelook.authorization.TestUserConstants.TEST_USER_EMAIL
import com.github.sdpsharelook.authorization.TestUserConstants.TEST_USER_PASS
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AuthProviderTest {
    @Before
    fun resetAuthProvider(){
        auth= TestAuth()
    }
    @Test
    fun testLogin(){

        runTest{
            val user = auth.signInWithEmailAndPassword(TEST_USER_EMAIL, TEST_USER_PASS)
            assert(auth.currentUser!!.email==TEST_USER_EMAIL)
            auth.signOut();
            assert(auth.currentUser==null)
        }
    }
    @Test
    fun testInvalidLogin(){
        runTest {
            val result = auth.signInWithEmailAndPassword(TEST_USER_EMAIL, "")
            assert(result.isFailure)
        }
    }
    @Test
    fun signUpAlreadyUsedEmail(){
        runTest{
            val result = auth.createUserWithEmailAndPassword(TEST_USER_EMAIL, TEST_USER_PASS)
            assert(result.isFailure)
        }
    }

}