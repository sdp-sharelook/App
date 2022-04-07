package com.github.sdpsharelook

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.github.sdpsharelook.camera.CameraActivity
import com.github.sdpsharelook.section.SectionActivity
import com.github.sdpsharelook.storage.DatabaseViewActivity
import com.github.sdpsharelook.textDetection.TextDetectionActivity
import com.google.android.material.navigation.NavigationView


const val EXTRA_MESSAGE = "com.github.sdpsharelook.NAME"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setDrawerListener()
    }

    private fun setDrawerListener() {
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        findViewById<ImageView>(R.id.menu_hamburger).setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        val navView = findViewById<NavigationView>(R.id.navView)
        val theme = applicationContext.theme
    }

    fun greet(button: View) {
        assert(button.id == R.id.button_greet)
        val editText = findViewById<EditText>(R.id.edit_text_name)
        val name = editText?.text.toString()
        val intent = Intent(this, GreetingActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, name)
        }
        startActivity(intent)
    }

    fun textToSpeech(button: View) {
        assert(button.id == R.id.button_text_to_speech)
        //startActivity(Intent(this, TextToSpeechActivity::class.java))
    }


    fun voiceRecognition(button: View) {
//        assert(button.id == R.id.button_voice_recognition)
        // startActivity(Intent(this, SpeechRecognitionActivity::class.java))
    }


    fun sectionActivity(button: View) {
//        assert(button.id == R.id.button_section)
        startActivity(Intent(this, SectionActivity::class.java))
    }

    fun translatorActivity(button: View) {
//        assert(button.id == R.id.button_translation)
        startActivity(Intent(this, TranslateActivity::class.java))
    }

    fun cameraActivity(button: View) {
//        assert(button.id == R.id.button_camera)
        startActivity(Intent(this, CameraActivity::class.java))
    }

    fun textDetectionActivity(button: View) {
//        assert(button.id == R.id.button_text_detection)
        startActivity(Intent(this, TextDetectionActivity::class.java))
    }

    fun databaseActivity(button: View) {
        assert(button.id == R.id.databaseButton)
        startActivity(Intent(this, DatabaseViewActivity::class.java))
    }

}
