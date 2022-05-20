package com.github.sdpsharelook.authorization

data class User(
    val uid: String,
    val email: String,
    val displayName: String = "anonymous",
    val isAnonymous: Boolean = true
)

