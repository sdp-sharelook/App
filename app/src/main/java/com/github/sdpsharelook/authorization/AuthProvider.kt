package com.github.sdpsharelook.authorization

interface AuthProvider {
    suspend fun createUserWithEmailAndPassword(email: String, password: String): Result<User>
    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<User>
    fun signOut()
    var currentUser: User?
}
