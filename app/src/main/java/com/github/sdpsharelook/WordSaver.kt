package com.github.sdpsharelook

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

    fun contextualize(word: Word): Map<String, Word> {
        return filter { it.value.isRelatedTo(word) > 0 }
    }
}