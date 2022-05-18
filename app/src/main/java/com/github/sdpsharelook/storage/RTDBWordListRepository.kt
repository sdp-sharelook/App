package com.github.sdpsharelook.storage

import com.github.sdpsharelook.Word
import com.github.sdpsharelook.authorization.AuthProvider
import com.github.sdpsharelook.section.Section
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
    private val firebaseDatabase: FirebaseDatabase,
    private val auth: AuthProvider
) : IRepository<@JvmSuppressWildcards Word> {
    private val user = auth.currentUser
    private val reference: DatabaseReference by lazy { firebaseDatabase.getReference("users/" + user!!.uid) }


    /**
     * Gets an asynchronous data stream any updated [List] of [String]s
     *
     * @param name format: "path/name"
     * @return [Flow] of changes in the database at [name]
     */
    override fun flow(name: String): Flow<Result<Word?>> =
        callbackFlow {
            val fireListener = object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    var word = Gson().fromJson(snapshot.value.toString(), Word::class.java)
                    trySendBlocking(Result.success(word))
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    var word = snapshot.getValue<Word>()
                    trySendBlocking(Result.success(word))
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    var word = snapshot.getValue<Word>()
                    trySendBlocking(Result.success(word))
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

                override fun onCancelled(error: DatabaseError) {
                    trySendBlocking(Result.failure(error.toException()))
                }

            }
            getSectionReference(name).addChildEventListener(fireListener)
            awaitClose {
                getSectionReference(name).removeEventListener(fireListener)
            }
        }

    private fun getSectionReference(uid: String): DatabaseReference {
        if (user != null) {
            return firebaseDatabase.getReference("users/" + user.uid + "/"+ uid)
        }
        return firebaseDatabase.getReference("users/guest/$uid")
    }

    /**
     * Create permanent repository entry
     *
     * @param name identifier of entity
     * @param entity Entity
     */
    override suspend fun insert(name: String, entity: Word) {
        getSectionReference(name).child(entity.uid).setValue(Gson().toJson(entity).toString())
    }

    /**
     * Create permanent repository entry
     *
     * @param name identifier of entity
     * @param entity Entity List of words
     */
    suspend fun insertList(name: String, entity: List<Word>) {
        entity.forEach { insert(name, it) }
    }

    /**
     * Create permanent repository entry
     *
     * @param name identifier of entity
     * @param entity Entity Section
     */
    suspend fun insertSection(entity: Section) {
        getSectionReference(entity.id).setValue(Gson().toJson(entity).toString())
    }

    fun flowSection(): Flow<Result<Section?>> =
        callbackFlow {
            val fireListener = object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    var section = Gson().fromJson(snapshot.value.toString(), Section::class.java)
                    trySendBlocking(Result.success(section))
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    var section = snapshot.getValue<Section>()
                    trySendBlocking(Result.success(section))
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    var section = snapshot.getValue<Section>()
                    trySendBlocking(Result.success(section))
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

                override fun onCancelled(error: DatabaseError) {
                    trySendBlocking(Result.failure(error.toException()))
                }

            }
            getSectionReference("SectionList").addChildEventListener(fireListener)
            awaitClose {
                getSectionReference("SectionList").removeEventListener(fireListener)
            }
        }

    /**
     * Don't use
     */
    override suspend fun read(name: String): Word {
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
    override suspend fun update(name: String, entity: Word) {
//        val databaseReference =
//            databaseReference(name)
//        databaseReference.setValue(entity).await()
    }

    /**
     * Delete repository entry at [name].
     *
     * @param name identifier of entity
     */
    override suspend fun delete(name: String) {
        getSectionReference(name).removeValue().await()
    }

}