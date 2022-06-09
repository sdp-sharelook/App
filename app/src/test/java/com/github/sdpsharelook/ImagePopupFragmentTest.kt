package com.github.sdpsharelook

import android.graphics.Bitmap
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdpsharelook.utils.FragmentScenarioRule
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock

@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ImagePopupFragmentTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val fragmentScenarioRule = FragmentScenarioRule.launch(ImagePopUpFragment::class)

    @BindValue
    val image: Bitmap = mock()

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun `popup happens`() = runTest {
        advanceUntilIdle()
    }

//    @Test
//    fun test() {
//        fragmentScenarioRule.scenario.onFragment {
//            arguments?.putString("KEY_SOURCE", "Bonjour")
//            arguments?.putString("KEY_TARGET", "Goodmorning")
//            arguments?.putLong("KEY_DATE", Clock.System.now().toEpochMilliseconds())
//            arguments?.putParcelable("KEY_IMAGE", image)
//        }
//    }
}