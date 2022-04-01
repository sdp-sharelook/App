package com.github.sdpsharelook

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.sdpsharelook.Section.SectionActivity
import com.github.sdpsharelook.storage.DatabaseViewActivity
import com.github.sdpsharelook.authorization.LoginActivity
import com.github.sdpsharelook.language.Language
import com.github.sdpsharelook.language.LanguageSelectionDialog
import com.github.sdpsharelook.textDetection.TextDetectionActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



const val EXTRA_MESSAGE = "com.github.sdpsharelook.NAME"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    fun greet(@Suppress("UNUSED_PARAMETER")view: View) {
        val editText = findViewById<EditText>(R.id.edit_text_name)
        val name = editText?.text.toString()
        val intent = Intent(this, GreetingActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, name)
        }
        startActivity(intent)
    }

    fun textToSpeech(@Suppress("UNUSED_PARAMETER")view: View) {}
    //startActivity(Intent(this, TextToSpeechActivity::class.java))



    fun voiceRecognition(@Suppress("UNUSED_PARAMETER")view: View) {}
    // startActivity(Intent(this, SpeechRecognitionActivity::class.java))


    fun sectionActivity(@Suppress("UNUSED_PARAMETER")view: View) =
        startActivity(Intent(this, SectionActivity::class.java))

    fun translatorActivity(@Suppress("UNUSED_PARAMETER")view: View) =
        startActivity(Intent(this, TranslateActivity::class.java))

    fun textDetectionActivity(view: View) =
        startActivity(Intent(this, TextDetectionActivity::class.java))

    fun databaseActivity(@Suppress("UNUSED_PARAMETER")view: View) {
        startActivity(Intent(this, DatabaseViewActivity::class.java))
    }

}
