package com.github.sdpsharelook.SRAlgo

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdpsharelook.revision.SRAlgo
import com.github.sdpsharelook.revision.RevisionWord
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class SRAlgoTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    val TEST_SR_FILE = "test_sr_file.csv"
    val TEST_WORDS = listOf<RevisionWord>(
        RevisionWord("id0"),
        RevisionWord("id1", 12312L, 6.0, 3), RevisionWord("id2")
    )

    @Before
    fun init() {
        hiltRule.inject()
        File(ApplicationProvider.getApplicationContext<Context?>().filesDir,TEST_SR_FILE).delete()
        TEST_WORDS.forEach {
            it.saveToStorage(ApplicationProvider.getApplicationContext(),TEST_SR_FILE)
        }
    }


    @Test
    fun getWords() {
        val revWords = SRAlgo.loadRevWordsFromLocal(ApplicationProvider.getApplicationContext(),TEST_SR_FILE)
        assert(revWords == TEST_WORDS)
    }

    @Test
    fun checkNextReviewTime() {
        val revW = TEST_WORDS[0]
        for (i in 1..5) {
            revW.n = i
            SRAlgo.calcNextReviewTime(revW, 5)
        }
        assert(revW.EF <= SRAlgo.MAX_EF && revW.EF >= SRAlgo.MIN_EF)
    }
}