package com.github.sdpsharelook

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.github.sdpsharelook.camera.CameraActivity
import com.github.sdpsharelook.storage.DatabaseViewActivity
import com.github.sdpsharelook.textDetection.TextDetectionActivity
import com.google.android.material.navigation.NavigationView

const val EXTRA_MESSAGE = "com.github.sdpsharelook.NAME"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeDrawer()
    }

    private fun initializeDrawer() {
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        findViewById<ImageView>(R.id.menu_hamburger).setOnClickListener{
            drawerLayout.openDrawer(GravityCompat.START);
        }
        val navView = findViewById<NavigationView>(R.id.navView)
        val navController = Navigation.findNavController(this, R.id.navHostFragment)
        NavigationUI.setupWithNavController(navView, navController)
    }

    fun textToSpeech(@Suppress("UNUSED_PARAMETER")view: View) {}
    //startActivity(Intent(this, TextToSpeechActivity::class.java))

    fun voiceRecognition(@Suppress("UNUSED_PARAMETER")view: View) {}
    // startActivity(Intent(this, SpeechRecognitionActivity::class.java))

    fun cameraActivity(view: View) =
        startActivity(Intent(this, CameraActivity::class.java))

    fun textDetectionActivity(view: View) =
        startActivity(Intent(this, TextDetectionActivity::class.java))

    fun databaseActivity(@Suppress("UNUSED_PARAMETER")view: View) {
        startActivity(Intent(this, DatabaseViewActivity::class.java))
    }

}
