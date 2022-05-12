package com.github.sdpsharelook.authorization

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireAuth @Inject constructor(
    private val auth: FirebaseAuth
) : AuthProvider {
    override fun signOut() {
        auth.signOut()
    }

    private fun firebaseToAppUser(fbUser: FirebaseUser): User {
        return User(
            fbUser.uid,
            fbUser.email.toString(),
            fbUser.displayName.toString(),
            fbUser.isAnonymous
        )
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