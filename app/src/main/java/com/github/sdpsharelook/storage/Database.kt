package com.github.sdpsharelook.storage

interface Database {
    fun saveFavourites(wordSaver: WordSaver)
    fun fillFavourites(wordSaver: WordSaver)
}
