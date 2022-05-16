package com.github.sdpsharelook

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdpsharelook.storage.RTDBWordListRepository
import com.github.sdpsharelook.utils.FragmentScenarioRule
import dagger.hilt.android.testing.BindValue
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
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MapsFragmentTest {
    @BindValue
    val repo: RTDBWordListRepository = mock {
        val flow: Flow<Result<List<Word>?>> = mock()
        on { flow() } doReturn flow
    }
    private lateinit var testFlow: Flow<Result<List<Word>?>>

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val fragmentScenarioRule = FragmentScenarioRule.launch(MapsFragment::class)

    @Before
    fun setUp() {
        hiltRule.inject()
        testFlow = repo.flow()
    }

    @Test
    fun `test maps fragment displays`() = runTest {
        advanceUntilIdle()
        verify(repo).flow()
    }
}