package com.github.sdpsharelook.translate

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdpsharelook.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class TranslateFragmentTest {
    //    private var mIdlingResource: IdlingResource? = null
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)
    @Test
    fun empty() {
        launchFragmentInHiltContainer<TranslateFragment>()
    }
//
//    @Before
//    fun init() {
//        hiltRule.inject()
//        launchFragmentInHiltContainer<TranslateFragment>(fragmentArgs = Bundle.EMPTY)
////            .onFragment {
////            mIdlingResource = it.getIdlingResource()
////            // To prove that the test fails, omit this call:
////            IdlingRegistry.getInstance().register(mIdlingResource)
////        }
//    }
//
//    private fun selectSourceLanguage(srcLang: String) {
//        onView(withId(R.id.buttonSourceLang)).perform(click())
//        val dialog = ShadowDialog.getLatestDialog()
//        Assert.assertTrue(dialog.isShowing)
//        // Don't work, have to find how to select the lang
//
//
//        val et = dialog.findViewById<EditText>(R.id.edit_text_search_language)
//        et.setText(srcLang)
//        val lv = dialog.findViewById<ListView>(R.id.list_view_languages)
//        Assert.assertTrue(lv.count != 0)
//
//        //val lv = inDialogView<ListView>(R.id.list_view_languages)
//        //elementAt(0).performClick()
//
//
//        ShadowLooper.runUiThreadTasks()
//        Assert.assertFalse(dialog.isShowing)
//    }
//
//    private fun selectTargetLanguage(targetLang: String) {
//        onView(withId(R.id.buttonTargetLang)).perform(click())
//        val dialog = ShadowDialog.getLatestDialog()
//        Assert.assertTrue(dialog.isShowing)
//
//        // Don't work, have to find how to select the lang
//        //onData(withTag(containsString(targetLang))).perform(click())
//    }
//
//    @Test
//    fun example() {
//        selectSourceLanguage("français")
//    }
//
//    @Test
//    @ExperimentalCoroutinesApi
//    fun testTranslateActivity() = runTest {
//        // simple translate
//        selectSourceLanguage("fr")
//        selectTargetLanguage("en")
//        onView(withId(R.id.sourceText))
//            .perform(typeText("Bonjour."), closeSoftKeyboard())
//
//        onView(withId(R.id.targetText)).check(matches(withText("Hello.")))
//        // switch button change
//        onView(withId(R.id.buttonSwitchLang)).perform(click())
//
//        // change target lang
//        selectTargetLanguage("it")
//        onView(withId(R.id.targetText)).check(matches(withText("Ciao.")))
//    }
//
//    @Test
//    @ExperimentalCoroutinesApi
//    fun testTextToSpeechButton() = runTest {
//        // speak
//        onView(withId(R.id.imageButtonTTS)).perform(click())
//        delay(2000)
//    }
//
////    @Test
////    @ExperimentalCoroutinesApi
////    fun testSpeechRecognitionButton() = runTest {
////        // listen
////        onView(withId(R.id.imageButtonSR)).perform(click())
////        getInstrumentation().waitForIdleSync()
////        delay(2000)
////
////        val context = getInstrumentation().context
////        PermissionChecker.checkCallingOrSelfPermission(
////            context,
////            android.Manifest.permission.RECORD_AUDIO
////        )
////        PermissionRequester().apply {
////            addPermissions(android.Manifest.permission.RECORD_AUDIO)
////            requestPermissions()
////        }
////        // onView(withId(R.id.sourceText)).check(matches(withText("...")))
////        // onView(withId(R.id.sourceText)).check(matches(not(isEnabled())))
////        delay(1000)
////    }
//
//    @Test
//    fun testSwitchSourceOrTargetLanguageMustRunTranslation() {
//        selectSourceLanguage("fr")
//        selectTargetLanguage("en")
//        onView(withId(R.id.sourceText))
//            .perform(typeText("Ciao."), closeSoftKeyboard())
//        selectSourceLanguage("it")
//        onView(withId(R.id.targetText)).check(matches(withText("Hello.")))
//        selectTargetLanguage("fr")
//        onView(withId(R.id.targetText)).check(matches(withText("Bonjour.")))
//    }
//
//    @Test
//    fun testAutoDetectShouldCorrectlyDetectSourceLanguage() {
//        selectSourceLanguage("auto")
//        selectTargetLanguage("en")
//        onView(withId(R.id.sourceText))
//            .perform(typeText("Bonjour."), closeSoftKeyboard())
//        onView(withId(R.id.targetText)).check(matches(withText("Hello.")))
//    }
//
//    @Test
//    fun testSwitchButtonMustSwitchLanguagesAndRunTranslation() {
//        selectSourceLanguage("fr")
//        selectTargetLanguage("en")
//        onView(withId(R.id.sourceText)).perform(clearText())
//            .perform(typeText("Hello."), closeSoftKeyboard())
//        onView(withId(R.id.buttonSwitchLang)).perform(click())
//        onView(withId(R.id.buttonSourceLang)).check(matches(withText(Language("en").displayName)))
//        onView(withId(R.id.buttonTargetLang)).check(matches(withText(Language("fr").displayName)))
//        onView(withId(R.id.targetText)).check(matches(withText("Bonjour.")))
//    }
//
//    @After
//    fun unregisterIdlingResource() {
//        if (mIdlingResource != null) {
//            IdlingRegistry.getInstance().unregister(mIdlingResource)
//        }
//    }
//
////    companion object {
////        @JvmStatic
////        @BeforeClass
////        fun dismissANRSystemDialog() {
////            val device = UiDevice.getInstance(getInstrumentation())
////            val waitButton = device.findObject(UiSelector().textContains("wait"))
////            if (waitButton.exists()) {
////                waitButton.click()
////            }
////        }
////    }
}