package com.github.sdpsharelook.revision

import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json.Default.decodeFromString
import kotlinx.serialization.json.Json.Default.encodeToString
import java.io.File
import java.io.IOException
import kotlin.math.max
import kotlin.math.min

const val SRDATAFILE = "srdatafile.csv"

@Serializable
data class RevisionWord(
    val wordId: String,
    //unix time last review
    var lastReview: Long = 0L,
    //next review in days
    var nextReview: Double = 0.0,
    //number of revisions already done
    var n: Int = 0,
    //easiness factor used to calculate when next review should be
    var EF: Double = SRAlgo.MAX_EF,
) {

    fun saveToStorage(context: Context, filePath: String = SRDATAFILE) {

        val file = File(context.filesDir, filePath)
        file.createNewFile()
        val newF = file.readLines().toMutableList()
        var found = false
        newF.forEachIndexed { index, str ->
            val word = decodeFromString(serializer(), str)
            if (word.wordId == wordId) {
                found = true
                newF[index] = encodeToString(serializer(),this)
                return@forEachIndexed
            }
        }
        file.bufferedWriter().use { printer ->
            newF.forEach { str ->
                printer.write(str + "\n")
            }
            if (!found) {
                printer.append(encodeToString(serializer(),this)+"\n")
            }
        }


    }

    override fun toString(): String {
        return "${this.wordId},${this.lastReview},${this.nextReview},${this.n},${this.EF}"
    }
}

class SRAlgo {


    companion object {

        const val MIN_EF = 1.3
        const val MAX_EF = 2.5

        @JvmStatic
        fun loadRevWordsFromLocal(
            context: Context,
            filePath: String = SRDATAFILE
        ): List<RevisionWord> {
            val f = File(context.filesDir, filePath)
            if (f.createNewFile()) {
                return emptyList()
            }
            if (!f.isFile || !f.canRead()) {
                throw IOException("Could not read file to get revision words")
            }
            f.useLines { lines ->
                return lines.map {
                    decodeFromString(RevisionWord.serializer(),it)
                }.toList()
            }
        }

        @JvmStatic
        fun calcNextReviewTime(word: RevisionWord, q: Int) {

            if (q < 3) {
                //if got it wrong or can't remember
                //start repetitions anew without changing the E-Facto
                word.n = 0
            } else {
                //else calc new EF
                val newEF = word.EF
                val newF = newEF + (0.1 - (5 - q) * (0.08 + (5 - q) * 0.02))
                word.EF = min(max(newF, MIN_EF), MAX_EF)
            }

            word.nextReview = when (word.n) {
                0 -> 0.0
                1 -> 1.0
                2 -> 6.0
                else -> word.EF * word.nextReview
            }
        }
    }


}