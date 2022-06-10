package com.github.sdpsharelook.storage

import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RTDBStringListRepository @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : IRepository<List<@JvmSuppressWildcards String>> {

    private val reference: DatabaseReference by lazy { firebaseDatabase.getReference("wordlists") }

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
        databaseReference(name).addChildEventListener(fireListener)
        awaitClose {
            databaseReference(name).removeEventListener(fireListener)
        }
    }

    /**
     * Create permanent repository entry
     *
     * @param name identifier of entity
     * @param entity Entity
     */
    override suspend fun insert(name: String, entity: List<String>) {
        entity.forEach { databaseReference(name).push().setValue(it).await() }
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
        databaseReference(name).setValue(entity).await()
    }

    /**
     * Delete repository entry at [name].
     *
     * @param name identifier of entity
     */
    override suspend fun delete(name: String, entity: List<@JvmSuppressWildcards String>) {
        databaseReference(name).removeValue().await()
    }

    private fun databaseReference(name: String) = reference.child(name)

}