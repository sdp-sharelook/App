package com.github.sdpsharelook

import com.github.sdpsharelook.revision.SRAlgo
import com.github.sdpsharelook.revision.revisionWord
import org.junit.Before
import org.junit.Test
import java.io.File

class SRAlgoTest {


    val TEST_SR_FILE = "test_sr_file.csv"
    val TEST_WORDS = listOf<revisionWord>(
        revisionWord("id0"),
        revisionWord("id1", 12312L, 6.0, 3), revisionWord("id2")
    )

    @Before
    fun clearTest() {
        File(TEST_SR_FILE).delete()
        TEST_WORDS.forEach {
            it.saveToStorage(TEST_SR_FILE)
        }
    }

    @Test
    fun getWords() {
        val revWords = SRAlgo.loadRevWordsFromLocal(TEST_SR_FILE)
        assert(revWords == TEST_WORDS)
    }
    @Test
    fun checkNextReviewTime() {
        val revW = TEST_WORDS[0]
        for(i in 1..5){
            revW.n=i
            SRAlgo.calcNextReviewTime(revW, 5)
        }
        assert(revW.EF <= SRAlgo.MAX_EF && revW.EF>= SRAlgo.MIN_EF)
    }
}