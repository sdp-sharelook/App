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
import java.util.*
import javax.inject.Inject

class RTDBWordListRepository @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
    private val auth: AuthProvider
) : IRepository<@JvmSuppressWildcards List<Word>> {
    private val user = auth.currentUser
    private val reference: DatabaseReference by lazy { firebaseDatabase.getReference("users/" + user!!.uid) }


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
                    val list = listOfNotNull(snapshot.getValue<Word>())
                    trySendBlocking(Result.success(list))
                }

                override fun onChildRemoved(snapshot: DataSnapshot){
                    val list: List<Word> = listOfNotNull(snapshot.getValue<Word>())
                    trySendBlocking(Result.success(list))
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}


                override fun onCancelled(error: DatabaseError) {
                    val result = Result.failure<List<Word>>(error.toException())
                    trySendBlocking(result)
                }

            }
            getSectionReference(name).addChildEventListener(fireListener)
            awaitClose {
                getSectionReference(name).removeEventListener(fireListener)
            }
        }

    private fun getSectionReference(uid: String): DatabaseReference {
        if (user != null) {
            return firebaseDatabase.getReference("users/" + user.uid ).child(uid)
        }
        return firebaseDatabase.getReference("users/guest/$uid")
    }


    /**
     * Create permanent repository entry
     *
     * @param name identifier of entity
     * @param entity Entity List of words
     */
    override suspend fun insertList(name: String, entity: List<Word>) {
        for (word in entity) {
           getSectionReference(name).child(word.uid).setValue(Gson().toJson(word))
        }
    }


    /**
     * Create permanent repository entry
     *
     * @param name identifier of entity
     * @param entity Entity Section
     */
    override suspend fun insertSection(entity: Section) {
        getSectionReference("SectionList").child(entity.id).setValue(Gson().toJson(entity).toString())
    }

    override fun flowSection(): Flow<Result<List<Section>?>> =
        callbackFlow {

            val fireListener = object : ChildEventListener {
                val sectionList = mutableListOf<Section>()
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    var section = Gson().fromJson(snapshot.value.toString(), Section::class.java)
                    sectionList.add(section)
                    trySendBlocking(Result.success(sectionList))
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    var section = Gson().fromJson(snapshot.value.toString(), Section::class.java)
                    val oldSection= sectionList.find {
                        it.id == section.id
                    }
                    val oldIndex = sectionList.indexOf(oldSection)
                    sectionList.removeAt(oldIndex)
                    sectionList.add(oldIndex, section)
                    trySendBlocking(Result.success(sectionList))
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    var section = Gson().fromJson(snapshot.value.toString(), Section::class.java)
                    sectionList.remove(section)
                    trySendBlocking(Result.success(sectionList))
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
    override suspend fun read(name: String): List<Word> {
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

    /**
     * Delete repository entry at [name].
     *
     * @param name identifier of entity
     */
    override suspend fun delete(name: String) {
        getSectionReference(name).removeValue().await()
    }

    override suspend fun deleteSection(entity: Section) {
        getSectionReference("SectionList").child(entity.id).removeValue().await()
    }

    /**
     * Insert repository entity into existing list
     *
     * @param name identifier of entity
     * @param entity Entity
     */
    override suspend fun insert(name: String, entity: List<Word>) {
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
    override suspend fun update(name: String, entity: List<Word>) {
        TODO("Not yet implemented")
    }

}
