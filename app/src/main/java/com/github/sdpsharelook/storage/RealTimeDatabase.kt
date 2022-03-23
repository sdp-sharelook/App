package com.github.sdpsharelook.storage

import com.github.sdpsharelook.Word
import kotlinx.coroutines.tasks.await

class RealTimeDatabase(
    uid: String,
    root: RTTRoot = FireDataBase()
) : Database {
    private val favReference = root.reference.child("users").child(uid).child("wordlists").child("favourites")

    override suspend fun saveFavourites(wordSaver: WordSaver) {
        favReference.setValue(wordSaver.filter { it.value.isFavourite }.keys.toList()).await()
    }

    override suspend fun fillFavourites(wordSaver: WordSaver) {
        val dataSnapshot = favReference.get().await()
        val wordlist = dataSnapshot.getValue() as? List<*>
        wordlist?.forEach {
            if (it is String && !wordSaver.containsKey(it))
                wordSaver[it] = Word(it, "TODO", true)
        }
    }
}