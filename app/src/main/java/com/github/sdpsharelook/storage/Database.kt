package com.github.sdpsharelook.storage

interface Database {
    suspend fun saveFavourites(wordSaver: WordSaver)
    suspend fun fillFavourites(wordSaver: WordSaver)
}
