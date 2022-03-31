package com.github.sdpsharelook.storage

import com.github.sdpsharelook.Word
import kotlinx.coroutines.tasks.await

/**
 * Real time database implementation of [WordSaverDatabase]
 *
 * @constructor Create [RealTimeWordSaverDatabase]
 *
 * @param uid unique identifier of user in firebase auth
 * @param root possible injection of database root
 */
class RealTimeWordSaverDatabase(
    uid: String,
    root: RTTRoot = FireDataBase()
) : WordSaverDatabase {
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