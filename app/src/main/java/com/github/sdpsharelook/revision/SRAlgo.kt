package com.github.sdpsharelook.revision

import android.content.Context
import java.io.File
import java.io.IOException
import kotlin.math.max
import kotlin.math.min

const val SRDATAFILE = "srdatafile.csv"

data class revisionWord(
    val wordId: String,
    //unix time last review
    var lastReview: Long = 0L,
    //next review in days
    var nextReview: Double = 0.0,
    //number of revisions already done
    var n: Int = 1,
    //easiness factor used to calculate when next review should be
    var EF: Double = 1.3,
) {

    fun saveToStorage(context: Context, filePath: String = SRDATAFILE) {

        val file = File(context.filesDir, filePath)
        file.createNewFile()
        val newF = file.readLines().toMutableList()
        var found = false
        newF.forEach { str ->
            if (str.startsWith(this.wordId)) {
                found = true
                newF[newF.indexOf(str)] = this.toString()
            }
        }
        file.bufferedWriter().use { printer ->
            newF.forEach { str ->
                printer.write(str + "\n")
            }
            if (!found) {
                printer.write(this.toString() + "\n")
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
        ): List<revisionWord> {
            val f = File(context.filesDir, filePath)

            if (!f.isFile || !f.canRead()) {
                throw IOException("Could not read file to get revision words")
            }
            f.useLines { lines ->
                return lines.map {
                    val elements = it.split(",")
                    revisionWord(
                        elements[0],
                        elements[1].toLong(),
                        elements[2].toDouble(),
                        elements[3].toInt(),
                        elements[4].toDouble()
                    )
                }.toList()
            }

        }

        @JvmStatic
        fun calcNextReviewTime(word: revisionWord, q: Int) {

            //if got it wrong and can't remember review as soon as possible
            if (q < 3) {
                word.nextReview = 0.0
                word.n = 1
                return
            }

            //else calc new EF
            val EF = word.EF
            val newF = EF + (0.1 - (5 - q) * (0.08 + (5 - q) * 0.02))
            word.EF = min(max(newF, MIN_EF), MAX_EF)
            word.nextReview = when (word.n) {
                0 -> 0.0
                1 -> 1.0
                2 -> 6.0
                else -> word.EF * word.nextReview

            }
        }
    }


}