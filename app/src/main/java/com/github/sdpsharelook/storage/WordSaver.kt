package com.github.sdpsharelook.storage

import com.github.sdpsharelook.Word
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WordSaver(private val wordSaverDatabase: WordSaverDatabase) : HashMap<String, Word>() {
    init {
        val self = this
        CoroutineScope(Dispatchers.IO).launch {
            wordSaverDatabase.fillFavourites(self)
        }
    }

    suspend fun save() {
        wordSaverDatabase.saveFavourites(this)
    }

    suspend fun fill() {
        wordSaverDatabase.fillFavourites(this)
    }
}