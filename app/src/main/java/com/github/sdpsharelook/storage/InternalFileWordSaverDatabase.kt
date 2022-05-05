package com.github.sdpsharelook.storage

import android.content.Context
import com.github.sdpsharelook.Word
import com.github.sdpsharelook.language.Language
import java.io.File

class InternalFileWordSaverDatabase(private val context: Context) : WordSaverDatabase {
    override suspend fun saveFavourites(wordSaver: WordSaver) {
        val file = File(context.filesDir, "favouriteWords.txt")
        @Suppress("BlockingMethodInNonBlockingContext") // there is a bug where kotlin thinks this is blocking because it throws IOException
        file.createNewFile()
        file.printWriter().use { out ->
            wordSaver.filter { it.value?.isFavourite == true }.keys.forEach { out.println(it) }
        }
    }

    override suspend fun fillFavourites(wordSaver: WordSaver) {
        val file = File(context.filesDir, "favouriteWords.txt")
        if (file.isFile and file.canRead())
            //TODO: actually convert line of text to Word
            file.forEachLine { wordSaver[it] = Word(it) }
    }
}