package com.github.sdpsharelook

import android.content.Context
import java.io.File

class InternalFileDatabase(private val context: Context) : Database {
    override fun saveFavourites(wordSaver: WordSaver) {
        val file = File(context.filesDir, "favouriteWords.txt")
        file.createNewFile()
        file.printWriter().use { out ->
            wordSaver.filter { it.value.isFavourite }.keys.forEach { out.println(it) }
        }
    }

    override fun fillFavourites(wordSaver: WordSaver) {
        val file = File(context.filesDir, "favouriteWords.txt")
        if (file.isFile and file.canRead())
            file.forEachLine { wordSaver[it] = Word(it,"TODO",true) }
    }
}