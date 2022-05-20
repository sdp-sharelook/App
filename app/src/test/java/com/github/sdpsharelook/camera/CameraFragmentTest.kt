package com.github.sdpsharelook.camera

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.github.sdpsharelook.R
import com.github.sdpsharelook.di.TextDetectionModule
import com.github.sdpsharelook.utils.FragmentScenarioRule
import com.google.android.gms.tasks.Tasks
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognizer
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

@ExperimentalCoroutinesApi
@UninstallModules(TextDetectionModule::class)
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class CameraFragmentTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val fragmentScenarioRule = FragmentScenarioRule.launch(CameraFragment::class)

    @BindValue
    val textReco: TextRecognizer = mock {
        on { process(any<InputImage>()) } doReturn Tasks.forResult(txtMock)
    }
    private val txtMock: Text = mock {
        on { text } doReturn "test"
    }

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Rule
    @JvmField
    var mGrantPermissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(
            "android.permission.CAMERA"
        )

    @Test
    fun testCamera() {
        val cameraView = Espresso.onView(ViewMatchers.withId(R.id.cameraImageView))
        cameraView.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        val captureButton = Espresso.onView(ViewMatchers.withId(R.id.buttonTakePic))
        captureButton.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Intents.init()

        captureButton.perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.cameraImageView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Intents.intended(IntentMatchers.hasAction("android.media.action.IMAGE_CAPTURE"))
        assert(Intents.getIntents().size == 1)
        Intents.release()

        Espresso.onView(ViewMatchers.withId(R.id.cameraImageView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

}