package com.github.sdpsharelook.storage

import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RTDBWordListRepository @Inject constructor() : IRepository<List<String>> {

    @Inject
    lateinit var firebaseDatabase: FirebaseDatabase
    private val reference: DatabaseReference by lazy { firebaseDatabase.reference.child("wordlists") }

    /**
     * Gets an asynchronous data stream any updated [List] of [String]s
     *
     * @param name format: "path/name"
     * @return [Flow] of changes in the database at [name]
     */
    override fun flow(name: String): Flow<Result<List<String>?>> = callbackFlow {
        val fireListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val list = listOfNotNull(snapshot.getValue<String>())
                trySendBlocking(Result.success(list))
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val list = listOfNotNull(snapshot.getValue<String>(), "changed")
                trySendBlocking(Result.success(list))
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val list = listOfNotNull(snapshot.getValue<String>(), "")
                trySendBlocking(Result.success(list))
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {
                trySendBlocking(Result.failure(error.toException()))
            }

        }
        firebaseDatabase.getReference(name).addChildEventListener(fireListener)
        awaitClose {
            firebaseDatabase.getReference(name).removeEventListener(fireListener)
        }
    }

    /**
     * Create permanent repository entry
     *
     * @param name identifier of entity
     * @param entity Entity
     */
    override suspend fun insert(name: String, entity: List<String>) {
        val databaseReference =
            databaseReference(name)
        entity.forEach { databaseReference.push().setValue(it).await() }
    }

    /**
     * Don't use
     */
    override suspend fun read(name: String): List<String>? {
        throw UnsupportedOperationException("Use flow function for lists")
    }


    /**
     * Update data entry at [name].
     *
     * Note: will not create entry, for that use [insert]
     *
     * @param name Caution: wrong [name] can overwrite data.
     * @param entity Entity
     */
    override suspend fun update(name: String, entity: List<String>) {
        val databaseReference =
            databaseReference(name)
        databaseReference.setValue(entity).await()
    }

    /**
     * Delete repository entry at [name].
     *
     * @param name identifier of entity
     */
    override suspend fun delete(name: String) {
        val databaseReference =
            databaseReference(name)
        databaseReference.removeValue().await()
    }

    private fun databaseReference(name: String): DatabaseReference {
        return if (name == "test") firebaseDatabase.getReference(name) else reference.child(name)
    }

}