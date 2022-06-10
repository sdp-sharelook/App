package com.github.sdpsharelook.history

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdpsharelook.Word
import com.github.sdpsharelook.storage.IRepository
import com.github.sdpsharelook.utils.FragmentScenarioRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class HistoryFragmentTest {
    @Inject
    lateinit var repo: IRepository<List<Word>>
    private lateinit var testFlow: Flow<Result<List<Word>?>>

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val fragmentScenarioRule = FragmentScenarioRule.launch(HistoryFragment::class)

    @Before
    fun setUp() {
        hiltRule.inject()
        testFlow = repo.flow()
    }

    @Test
    fun `test maps fragment displays`() = runTest {
        advanceUntilIdle()
    }
}