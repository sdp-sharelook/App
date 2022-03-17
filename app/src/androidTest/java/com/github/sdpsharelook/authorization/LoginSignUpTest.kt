package com.github.sdpsharelook.authorization


import android.content.Intent
import android.service.autofill.Validators.not
import android.util.Log
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.BundleMatchers.hasEntry
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdpsharelook.R
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.IllegalArgumentException
import kotlin.coroutines.coroutineContext

class MockFirebaseAuth(p0: FirebaseApp) : FirebaseAuth(p0) {
    override fun signInWithEmailAndPassword(email: String, password: String): Task<AuthResult> {
        return super.signInWithEmailAndPassword(email, password)
    }

}

@RunWith(AndroidJUnit4::class)
class LoginSignUpTest {

    @get:Rule var loginRule = ActivityScenarioRule(LoginActivity::class.java)
    val TEST_USER_EMAIL = "testuser@gmail.com"
    val TEST_USER_PASS = "123456"

    @Before
    fun logOut() {
        auth.signOut()
    }

    @Test
    fun testLoginWithTestUser() {
        auth.createUserWithEmailAndPassword(TEST_USER_EMAIL, TEST_USER_PASS).addOnCompleteListener {
            onView(withId(R.id.email)).perform(typeText(TEST_USER_EMAIL))
            onView(withId(R.id.password)).perform(typeText(TEST_USER_PASS))
            onView(withId(R.id.loginButton)).perform(click())
            auth.pendingAuthResult?.addOnCompleteListener {
                assert(auth.currentUser!!.email == TEST_USER_EMAIL);
            }

        }
    }


    @Test
    fun testLoginWithNoEmail() {

        onView(withId(R.id.loginButton)).perform(swipeLeft())
        onView(withId(R.id.email)).perform(typeText(""))
        onView(withId(R.id.password)).perform(typeText(TEST_USER_PASS))
        onView(withId(R.id.loginButton)).perform(click()).also {
            assert(auth.currentUser == null);
        }

    }
}