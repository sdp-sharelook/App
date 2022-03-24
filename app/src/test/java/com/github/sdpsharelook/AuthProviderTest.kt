package com.github.sdpsharelook

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdpsharelook.authorization.TestAuth
import com.github.sdpsharelook.authorization.TestUserConstants.TEST_USER_EMAIL
import com.github.sdpsharelook.authorization.TestUserConstants.TEST_USER_PASS
import com.github.sdpsharelook.authorization.User
import com.github.sdpsharelook.authorization.auth
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AuthProviderTest {
    @Before
    fun resetAuthProvider(){
        auth= TestAuth()
    }
    @Test
    fun testLogin(){

        runBlocking{
            auth.signInWithEmailAndPassword(TEST_USER_EMAIL, TEST_USER_PASS)
        }
        assert(auth.currentUser!!.email==TEST_USER_EMAIL)
        auth.signOut();
        assert(auth.currentUser==null)
    }
    @Test
    fun testInvalidLogin(){
        runBlocking {
            val result = auth.signInWithEmailAndPassword(TEST_USER_EMAIL, "")
            assert(result.isFailure)
        }
    }

}