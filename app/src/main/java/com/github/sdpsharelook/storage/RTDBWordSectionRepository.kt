package com.github.sdpsharelook.storage

import com.github.sdpsharelook.section.SectionWord
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.lang.UnsupportedOperationException

class RTDBWordSectionRepository : IRepository<SectionWord> {

    private val firebaseDatabase: FirebaseDatabase by lazy { FirebaseDatabase.getInstance("https://billinguee-default-rtdb.europe-west1.firebasedatabase.app/") }
    private val reference: DatabaseReference by lazy { firebaseDatabase.reference.child("wordlists") }

    /**
     * Gets an asynchronous data stream any updated [List] of [SectionWord]s
     *
     * @param name format: "path/name"
     * @return [Flow] of changes in the database at [name]
     */
    override fun flow(name: String): Flow<Result<SectionWord?>> = callbackFlow {
        val fireListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                var word = snapshot.getValue<SectionWord>()
                trySendBlocking(Result.success(word))
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                var word = snapshot.getValue<SectionWord>()
                trySendBlocking(Result.success(word))
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                var word = snapshot.getValue<SectionWord>()
                trySendBlocking(Result.success(word))
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {
                trySendBlocking(Result.failure(error.toException()))
            }

        }
        reference.child(name).addChildEventListener(fireListener)
        awaitClose {
            reference.child(name).removeEventListener(fireListener)
        }
    }

    /**
     * Create permanent repository entry
     *
     * @param name identifier of entity
     * @param entity Entity
     */
    override suspend fun insert(name: String, entity: SectionWord) {
        val databaseReference = databaseReference(name)
        databaseReference.push().setValue(entity)
    }

    /**
     * Don't use
     */
    override suspend fun read(name: String): SectionWord {
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
    override suspend fun update(name: String, entity: SectionWord) {
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