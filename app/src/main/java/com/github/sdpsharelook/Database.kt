package com.github.sdpsharelook

interface Database {
    fun saveFavourites(wordSaver: WordSaver)
    fun fillFavourites(wordSaver: WordSaver)
}
