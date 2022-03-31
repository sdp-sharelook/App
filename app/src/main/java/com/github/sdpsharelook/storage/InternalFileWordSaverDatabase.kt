package com.github.sdpsharelook.storage

import android.content.Context
import com.github.sdpsharelook.Word
import java.io.File

class InternalFileWordSaverDatabase(private val context: Context) : WordSaverDatabase {
    override suspend fun saveFavourites(wordSaver: WordSaver) {
        val file = File(context.filesDir, "favouriteWords.txt")
        @Suppress("BlockingMethodInNonBlockingContext") // there is a bug where kotlin thinks this is blocking because it throws IOException
        file.createNewFile()
        file.printWriter().use { out ->
            wordSaver.filter { it.value.isFavourite }.keys.forEach { out.println(it) }
        }
    }

    override suspend fun fillFavourites(wordSaver: WordSaver) {
        val file = File(context.filesDir, "favouriteWords.txt")
        if (file.isFile and file.canRead())
            file.forEachLine { wordSaver[it] = Word(it,"TODO",true) }
    }
}