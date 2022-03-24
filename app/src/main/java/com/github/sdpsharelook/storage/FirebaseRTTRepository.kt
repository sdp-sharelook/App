package com.github.sdpsharelook.storage

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Firebase Realtime Database implementation using the repository design pattern,
 * to be used like this:
 * ```
 *  viewModelScope.launch {
 *      firebaseRTTDirectory.fetchValues().collect {
 *          when {
 *              it.isSuccess -> {
 *                  val value = it.getOrNull()
 *              }
 *              it.isFailure -> {
 *                  it.exceptionOrNull()?.printStackTrace()
 *              }
 *          }
 *      }
 *  }
 * ```
 *
 * @property firebaseDatabase
 * @property path
 * @constructor Create [FirebaseRTTRepository]
 */
class FirebaseRTTRepository(
    private val firebaseDatabase: FirebaseDatabase,
    private val path: String
) : RealTimeDatabaseRepository {

    /**
     * @return a producer of whatever changes in the database at [path]
     */
    override fun fetchValues(): Flow<Result<Any?>> = callbackFlow {
        val fireListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(Result.failure(error.toException()))
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                this@callbackFlow.trySendBlocking(Result.success(snapshot.value))
            }
        }
        firebaseDatabase.getReference(path).addValueEventListener(fireListener)
        awaitClose {
            firebaseDatabase.getReference(path).removeEventListener(fireListener)
        }
    }
}