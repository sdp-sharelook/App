package com.github.sdpsharelook.storage

interface WordSaverDatabase {
    /**
     * Save favourite words from [wordSaver], they can be retrieved later with [fillFavourites]
     *
     * @param wordSaver a set of words to save
     */
    suspend fun saveFavourites(wordSaver: WordSaver)

    /**
     * Fill [wordSaver] with previously saved words
     *
     * @param wordSaver a set of words to fill
     */
    suspend fun fillFavourites(wordSaver: WordSaver)
}
