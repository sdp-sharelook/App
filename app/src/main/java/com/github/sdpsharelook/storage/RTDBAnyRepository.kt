package com.github.sdpsharelook.storage

import com.google.firebase.database.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Firebase Realtime Database repository implementation for any value
 *
 * @constructor Create [RTDBAnyRepository]
 */
class RTDBAnyRepository : IRepository<Any> {

    private val firebaseDatabase: FirebaseDatabase by lazy { FirebaseDatabase.getInstance("https://billinguee-default-rtdb.europe-west1.firebasedatabase.app/") }

    /**
     * Gets an asynchronous data stream of [Any]
     *
     * @param name a path in the realtime database
     * @return [Flow] of changes in the database at [name]
     */
    override fun flow(name: String): Flow<Result<Any?>> = callbackFlow {
        val fireListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                trySendBlocking(Result.failure(error.toException()))
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                trySendBlocking(Result.success(snapshot.value))
            }
        }
        firebaseDatabase.getReference(name).addValueEventListener(fireListener)
        awaitClose {
            firebaseDatabase.getReference(name).removeEventListener(fireListener)
        }
    }

    /**
     * Create permanent repository entry
     *
     * @param name format: "path/name"
     * @param entity Entity
     */
    override suspend fun insert(name: String, entity: Any) {
        TODO("Not yet implemented")
    }

    /**
     * Read data at [name] once asynchronously.
     *
     * @param name identifier of entity
     * @return [Any] or null
     */
    override suspend fun read(name: String): Any? {
        TODO("Not yet implemented")
    }

    /**
     * Update data entry at [name].
     *
     * Note: will not create entry, for that use [insert]
     *
     * @param name Caution: wrong [name] can overwrite data.
     * @param entity Entity
     */
    override suspend fun update(name: String, entity: Any) {
        TODO("Not yet implemented")
    }

    /**
     * Delete repository entry at [name].
     *
     * @param name identifier of entity
     */
    override suspend fun delete(name: String) {
        TODO("Not yet implemented")
    }

    /**
     * Create permanent repository entry
     *
     * @param name identifier of entity
     */
    override suspend fun create(name: String): String {
        TODO("Not yet implemented")
    }
}