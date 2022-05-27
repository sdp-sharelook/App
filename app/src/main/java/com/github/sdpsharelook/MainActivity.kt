package com.github.sdpsharelook

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.github.sdpsharelook.authorization.AuthProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

const val EXTRA_MESSAGE = "com.github.sdpsharelook.NAME"

@AndroidEntryPoint
class MainActivity : MainActivityLift()

open class MainActivityLift : AppCompatActivity() {

    @Inject
    lateinit var auth: AuthProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth.signOut()
        setDrawerListener()
    }

    private fun setDrawerListener() {
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        findViewById<ImageView>(R.id.menu_hamburger).setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        val navView = findViewById<NavigationView>(R.id.navView)
        val bottomView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = supportFragmentManager.findFragmentById(R.id.navHostFragment)!!.findNavController()
        NavigationUI.setupWithNavController(navView, navController)
        NavigationUI.setupWithNavController(bottomView, navController)
        navView.getHeaderView(0).setOnClickListener {
            if (auth.currentUser == null || auth.currentUser!!.isAnonymous)
                navController.navigate(R.id.moveToLogin)
            else
                navController.navigate(R.id.moveToProfile)

            drawerLayout.closeDrawer(GravityCompat.START)
        }
    }
}
