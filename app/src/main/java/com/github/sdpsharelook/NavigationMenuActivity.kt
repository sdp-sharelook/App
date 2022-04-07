package com.github.sdpsharelook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class NavigationMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation_menu)
    }

    fun back(button: View) {
        assert(button.id == R.id.button_back)
        finish()
    }
}