package com.github.sdpsharelook.storage

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RealTimeDatabase : Database {
    private val instance = Firebase.database

    override fun saveFavourites(wordSaver: WordSaver) {
        TODO("Not yet implemented")
    }

    override fun fillFavourites(wordSaver: WordSaver) {
        TODO("Not yet implemented")
    }
}