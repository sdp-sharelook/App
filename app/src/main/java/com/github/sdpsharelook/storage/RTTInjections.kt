package com.github.sdpsharelook.storage

import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

interface RTTRef {
    fun child(pathString: String): RTTRef
    fun setValue(value: Any?): Task<Void?>
    fun get(): Task<SnapshotProvider>
}

interface RTTRoot {
    val reference: RTTRef
}

interface SnapshotProvider {
    fun getValue(): Any?
}


class FireRef(private val reference: DatabaseReference) : RTTRef {
    override fun child(pathString: String): RTTRef = FireRef(reference.child(pathString))
    override fun setValue(value: Any?): Task<Void?> = reference.setValue(value)
    override fun get(): Task<SnapshotProvider> = Tasks.forResult(FireSnapshot(Tasks.await(reference.get())))
}

class FireDataBase : RTTRoot {
    private val database = Firebase.database("https://billinguee-default-rtdb.europe-west1.firebasedatabase.app/")
    override val reference: RTTRef
        get() = FireRef(database.reference)
}

class FireSnapshot(private val snapshot: DataSnapshot) : SnapshotProvider {
    override fun getValue(): Any? = snapshot.value
}