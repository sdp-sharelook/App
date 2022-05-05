package com.github.sdpsharelook

import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Resources
import android.content.res.loader.ResourcesLoader
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.github.sdpsharelook.Section.SectionActivity
import com.github.sdpsharelook.authorization.LoginActivity

import com.github.sdpsharelook.textDetection.TextDetectionActivity
import com.github.sdpsharelook.storage.DatabaseViewActivity
import com.google.android.material.navigation.NavigationView
import com.github.sdpsharelook.camera.CameraActivity
import java.io.Serializable


const val EXTRA_MESSAGE = "com.github.sdpsharelook.NAME"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setDrawerListener()

    }

    private fun setDrawerListener(){
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        findViewById<ImageView>(R.id.menu_hamburger).setOnClickListener{
            drawerLayout.openDrawer(GravityCompat.START)
        }
        findViewById<NavigationView>(R.id.navView)
        applicationContext.theme


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

    fun cameraActivity(@Suppress("UNUSED_PARAMETER") view: View) =
        startActivity(Intent(this, CameraActivity::class.java))
        
    @Suppress("unused")
    fun textDetectionActivity(@Suppress("UNUSED_PARAMETER") view: View) =
        startActivity(Intent(this, TextDetectionActivity::class.java))

    fun databaseActivity(@Suppress("UNUSED_PARAMETER")view: View) {
        startActivity(Intent(this, DatabaseViewActivity::class.java))
    }

    fun signUpActivity(@Suppress("UNUSED_PARAMETER") view: View) =
        startActivity(Intent(this, LoginActivity::class.java))

    fun mapActivity(@Suppress("UNUSED_PARAMETER") view: View) {
        startActivity(Intent(this, MapsActivity::class.java))
    }
}
