package com.github.sdpsharelook.storage

import android.util.Log
import com.github.sdpsharelook.Word
import com.github.sdpsharelook.authorization.AuthProvider
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.gson.Gson
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RTDBWordListRepository @Inject constructor(
    firebaseDatabase: FirebaseDatabase,
    auth: AuthProvider
) : RTRepo<@JvmSuppressWildcards List<Word>>(firebaseDatabase, auth) {
    /**
     * Gets an asynchronous data stream any updated [List] of [String]s
     *
     * @param name format: "path/name"
     * @return [Flow] of changes in the database at [name]
     */
    override fun flow(name: String): Flow<Result<List<Word>?>> =
        callbackFlow {

            val fireListener = object : ChildEventListener {
                val wordList = mutableListOf<Word>()
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val builder = Gson()

                    val word = builder.fromJson(snapshot.value.toString(), Word::class.java)

                    wordList.add(wordList.size, word)
                    trySendBlocking(Result.success(wordList))
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    val word = Gson().fromJson(snapshot.value.toString(), Word::class.java)

                    val i = wordList.indexOfFirst { it.uid == word.uid }
                    wordList[i] = word
                    trySendBlocking(Result.success(wordList))
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    val word = Gson().fromJson(snapshot.value.toString(), Word::class.java)
                    wordList.remove(word)
                    trySendBlocking(Result.success(wordList))
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}


                override fun onCancelled(error: DatabaseError) {
                    val result = Result.failure<List<Word>>(error.toException())
                    trySendBlocking(result)
                }

            }
            getUserFolderReference(name).addChildEventListener(fireListener)
            awaitClose {
                getUserFolderReference(name).removeEventListener(fireListener)
            }
        }

    /**
     * Create permanent repository entry
     *
     * @param name identifier of entity
     * @param entity Entity List of words
     */

    override suspend fun insert(name: String, entity: List<Word>) {
        for (word in entity) {
            getUserFolderReference(name).child(word.uid).setValue(Gson().toJson(word))
        }
    }

    /**
     * Delete repository entry at [name].
     *
     * @param name identifier of entity
     */
    override suspend fun delete(name: String, words: List<Word>) =
        words.forEach {
            getUserFolderReference(name).child(it.uid).removeValue().await()
        }

    /**
     * Update data entry at [name].
     *
     * Note: will not create entry, for that use [insert]
     *
     * @param name Caution: wrong [name] can overwrite data.
     * @param entity Entity
     */
    override suspend fun update(name: String, entity: List<Word>) {
        TODO("Not yet implemented")
    }
}
