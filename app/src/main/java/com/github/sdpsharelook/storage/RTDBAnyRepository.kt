package com.github.sdpsharelook.storage

import com.github.sdpsharelook.Word
import com.google.firebase.database.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

/**
 * Firebase Realtime Database repository implementation for any value
 *
 * @constructor Create [RTDBAnyRepository]
 */
class RTDBAnyRepository @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : IRepository<Any> {

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

    override suspend fun deleteWord(name: String, entity: Word) {
        TODO("Not yet implemented")
    }

}