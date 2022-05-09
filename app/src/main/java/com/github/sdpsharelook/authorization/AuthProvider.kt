package com.github.sdpsharelook.authorization

import android.util.Log
import com.github.sdpsharelook.authorization.TestUserConstants.TEST_USER_EMAIL
import com.github.sdpsharelook.authorization.TestUserConstants.TEST_USER_PASS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

data class User(
    val email: String,
    val displayName: String = "anonymous",
    val isAnonymous: Boolean = true
)



interface AuthProvider {
    suspend fun createUserWithEmailAndPassword(email: String, password: String): Result<User>
    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<User>
    fun signOut()
    var currentUser: User?
}

object TestUserConstants {
    const val TEST_USER_EMAIL = "testuser@gmail.com"
    const val TEST_USER_PASS = "123456"
    const val TEST_USER_PASS2 = "Abcdef1!"
}

open class TestAuth @Inject constructor() : AuthProvider {

    override fun signOut() {
        currentUser = null
    }

    override suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String
    ): Result<User> {
        if (email == TEST_USER_EMAIL) return Result.failure(exception = IllegalArgumentException("email already in use"))

        Log.d("REGISTER","registering user")
        currentUser = User(email)
        return Result.success(User(email))
    }

    override suspend fun signInWithEmailAndPassword(email: String, password: String): Result<User> {
        if (email == TEST_USER_EMAIL && password == TEST_USER_PASS) {
            currentUser = User(email)
            return Result.success(User(email))
        }
        return Result.failure( exception =  IllegalArgumentException("could not login, no such login "))

    }

    override var currentUser: User? = null


}

class FireAuth @Inject constructor(
    private val auth: FirebaseAuth
) : AuthProvider {
    override fun signOut() {
        auth.signOut()
    }

    private fun firebaseToAppUser(fbUser: FirebaseUser): User {
        return User(fbUser.email.toString(), fbUser.displayName.toString(), fbUser.isAnonymous)
    }

    override suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String
    ): Result<User> {
        return try {
            val res = auth.createUserWithEmailAndPassword(email, password).await()
            val user = res.user
            Result.success(firebaseToAppUser(user!!))
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    override suspend fun signInWithEmailAndPassword(email: String, password: String): Result<User> {
        return try {
            val res = auth.signInWithEmailAndPassword(email, password).await()
            val user = res.user
            Result.success(firebaseToAppUser(user!!))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override var currentUser: User? = null
        get() = auth.currentUser?.let { firebaseToAppUser(it) }
}