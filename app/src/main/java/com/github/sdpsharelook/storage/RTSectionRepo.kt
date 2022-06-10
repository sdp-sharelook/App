package com.github.sdpsharelook.storage

import com.github.sdpsharelook.authorization.AuthProvider
import com.github.sdpsharelook.section.Section
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RTSectionRepo @Inject constructor(
    firebaseDatabase: FirebaseDatabase,
    auth: AuthProvider
) : RTRepo<List<@JvmSuppressWildcards Section>>(firebaseDatabase, auth) {
    override fun flow(name: String): Flow<Result<List<Section>?>> =
        callbackFlow {
            val fireListener = object : ChildEventListener {
                val sectionList = mutableListOf<Section>()
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val section = Gson().fromJson(snapshot.value.toString(), Section::class.java)
                    sectionList.add(section)
                    trySendBlocking(Result.success(sectionList.toList()))
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    val section = Gson().fromJson(snapshot.value.toString(), Section::class.java)
                    val oldSection = sectionList.find {
                        it.id == section.id
                    }
                    val oldIndex = sectionList.indexOf(oldSection)
                    sectionList.removeAt(oldIndex)
                    sectionList.add(oldIndex, section)
                    trySendBlocking(Result.success(sectionList.toList()))
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    val section = Gson().fromJson(snapshot.value.toString(), Section::class.java)
                    sectionList.remove(section)
                    trySendBlocking(Result.success(sectionList.toList()))
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

                override fun onCancelled(error: DatabaseError) {
                    trySendBlocking(Result.failure(error.toException()))
                }

            }
            getUserFolderReference("SectionList").addChildEventListener(fireListener)
            awaitClose {
                getUserFolderReference("SectionList").removeEventListener(fireListener)
            }
        }

    override suspend fun insert(name: String, entity: List<Section>) =
        entity.forEach {
            getUserFolderReference(name).child(it.id)
                .setValue(Gson().toJson(it).toString()).await()
        }

    override suspend fun update(name: String, entity: List<Section>) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(name: String, entity: List<Section>) =
        entity.forEach {
            getUserFolderReference(name).child(it.id).removeValue().await()
        }
}