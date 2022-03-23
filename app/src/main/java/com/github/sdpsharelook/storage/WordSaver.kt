package com.github.sdpsharelook.storage

import com.github.sdpsharelook.Word

class WordSaver(private val database: Database) : HashMap<String, Word>() {
    init {
        database.fillFavourites(this)
    }

    fun save() {
        database.saveFavourites(this)
    }

    fun fill() {
        database.fillFavourites(this)
    }
}