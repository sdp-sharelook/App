package com.github.sdpsharelook.storage

import com.github.sdpsharelook.Word
import kotlinx.coroutines.runBlocking

class WordSaver(private val database: Database) : HashMap<String, Word>() {
    init {
        val self = this
        runBlocking {
            database.fillFavourites(self)
        }
    }

    suspend fun save() {
        database.saveFavourites(this)
    }

    suspend fun fill() {
        database.fillFavourites(this)
    }
}