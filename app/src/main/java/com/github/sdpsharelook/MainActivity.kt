package com.github.sdpsharelook

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.github.sdpsharelook.Section.SectionActivity


const val EXTRA_MESSAGE = "com.github.sdpsharelook.NAME"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }


    fun greet(view: View) {
        val editText = findViewById<EditText>(R.id.edit_text_name)
        val name = editText?.text.toString()
        val intent = Intent(this, GreetingActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, name)
        }
        startActivity(intent)
    }

    fun textToSpeech(view: View) {
        startActivity(Intent(this, TextToSpeechActivity::class.java))
    }

    fun voiceRecognition(view: View) {
        startActivity(Intent(this, SpeechRecognitionActivity::class.java))
    }

    fun sectionAcitivity(view: View) {
        val intent = Intent(this, SectionActivity::class.java)
        startActivity(intent)
    }
}
