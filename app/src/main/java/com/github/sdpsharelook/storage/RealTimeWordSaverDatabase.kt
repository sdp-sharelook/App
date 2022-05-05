package com.github.sdpsharelook.storage

import com.github.sdpsharelook.Word

/**
 * Real time database implementation of [WordSaverDatabase]
 *
 * @constructor Create [RealTimeWordSaverDatabase]
 *
 * @param uid unique identifier of word list
 * @param repository database root injection
 */
class RealTimeWordSaverDatabase(
    private val uid: String,
    private val repository: IRepository<List<String>>
) : WordSaverDatabase {

    override suspend fun saveFavourites(wordSaver: WordSaver) {
        val value = wordSaver.filter { it.value?.isFavourite == true }.keys.toList()
        repository.insert(uid, value)
    }

    override suspend fun fillFavourites(wordSaver: WordSaver) {
        val read = repository.read(uid)
        read?.forEach {
            if (!wordSaver.containsKey(it))
                wordSaver[it] = Word(it)
        }
    }
}