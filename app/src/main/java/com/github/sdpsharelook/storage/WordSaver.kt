package com.github.sdpsharelook.storage

import com.github.sdpsharelook.Word
import kotlinx.coroutines.runBlocking

class WordSaver(private val wordSaverDatabase: WordSaverDatabase) : HashMap<String, Word>() {
    init {
        val self = this
        runBlocking {
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