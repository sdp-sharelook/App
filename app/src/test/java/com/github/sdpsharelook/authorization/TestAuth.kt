package com.github.sdpsharelook.authorization

import android.util.Log
import javax.inject.Inject

open class TestAuth @Inject constructor() : AuthProvider {

    override fun signOut() {
        currentUser = null
    }

    override suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String
    ): Result<User> {
        if (email == TestUserConstants.TEST_USER_EMAIL) return Result.failure(
            exception = IllegalArgumentException(
                "email already in use"
            )
        )

        Log.d("REGISTER", "registering user")
        currentUser = User(TestUserConstants.TEST_USER_UID, email)
        return Result.success(User(TestUserConstants.TEST_USER_UID, email))
    }

    override suspend fun signInWithEmailAndPassword(email: String, password: String): Result<User> {
        if (email == TestUserConstants.TEST_USER_EMAIL && password == TestUserConstants.TEST_USER_PASS) {
            currentUser = User(uid = TestUserConstants.TEST_USER_UID, email)
            return Result.success(User(TestUserConstants.TEST_USER_UID, email))
        }
        return Result.failure(exception = IllegalArgumentException("could not login, no such login "))

    }

    override var currentUser: User? = null


}