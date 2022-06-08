package com.github.sdpsharelook.storage

import com.github.sdpsharelook.authorization.AuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

abstract class RTRepo<T> constructor(
    private val firebaseDatabase: FirebaseDatabase,
    private val auth: AuthProvider
) : IRepository<T> {
    protected val user get() = auth.currentUser
    protected val reference: DatabaseReference by lazy { firebaseDatabase.getReference("users/" + user!!.uid) }
    protected fun getUserFolderReference(uid: String): DatabaseReference =
        user?.let {
            firebaseDatabase.getReference("users/" + it.uid).child(uid)
        } ?: firebaseDatabase.getReference("users/guest/$uid")
}