package com.github.sdpsharelook

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splash = installSplashScreen();
        super.onCreate(savedInstanceState)

        splash.setKeepOnScreenCondition{true}
        val intent = Intent(applicationContext,MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}