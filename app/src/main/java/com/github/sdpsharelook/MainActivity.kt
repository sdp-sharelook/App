package com.github.sdpsharelook

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.github.sdpsharelook.Section.SectionActivity
import com.github.sdpsharelook.storage.DatabaseViewActivity


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

    fun textToSpeech(@Suppress("UNUSED_PARAMETER")view: View) {
        startActivity(Intent(this, TextToSpeechActivity::class.java))
    }

    fun voiceRecognition(@Suppress("UNUSED_PARAMETER")view: View) {
        startActivity(Intent(this, SpeechRecognitionActivity::class.java))
    }

    fun sectionAcitivity(@Suppress("UNUSED_PARAMETER")view: View) {
        val intent = Intent(this, SectionActivity::class.java)
        startActivity(intent)
    }

    fun databaseActivity(@Suppress("UNUSED_PARAMETER")view: View) {
        startActivity(Intent(this, DatabaseViewActivity::class.java))
    }
}
