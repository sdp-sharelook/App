package com.github.sdpsharelook

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.github.sdpsharelook.authorization.AuthProvider
import com.github.sdpsharelook.revision.QuizEvent
import com.github.sdpsharelook.revision.RevisionQuizViewModel
import com.github.sdpsharelook.revision.SnackbarShowers.MAIN
import com.github.sdpsharelook.revision.UiEvent
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import javax.inject.Inject

const val EXTRA_MESSAGE = "com.github.sdpsharelook.NAME"

@FlowPreview
@AndroidEntryPoint
class MainActivity : MainActivityLift()

@FlowPreview
open class MainActivityLift : AppCompatActivity() {

    @Inject
    lateinit var auth: AuthProvider
    private val quizViewModel: RevisionQuizViewModel by viewModels()

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
        val navController =
            supportFragmentManager.findFragmentById(R.id.navHostFragment)!!.findNavController()
        NavigationUI.setupWithNavController(navView, navController)
        NavigationUI.setupWithNavController(bottomView, navController)
        navView.getHeaderView(0).setOnClickListener {
            if (auth.currentUser == null || auth.currentUser!!.isAnonymous)
                navController.navigate(R.id.moveToLogin)
            else
                navController.navigate(R.id.moveToProfile)
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        bottomView.setOnClickListener { quizViewModel.onEvent(QuizEvent.Ping) }
        val badge = bottomView.getOrCreateBadge(R.id.launchQuizFragment)
        lifecycleScope.launch {
            collectQuizViewModelEvents(badge)
        }
    }

    private suspend fun collectQuizViewModelEvents(badge: BadgeDrawable) {
        quizViewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> if (event.who == MAIN)
                    supportFragmentManager.findFragmentById(R.id.navHostFragment)?.view?.let { view ->
                        Snackbar.make(
                            view, event.message, Snackbar.LENGTH_SHORT
                        ).show()
                    }
                is UiEvent.UpdateBadge ->
                    badge.apply {
                        if (quizViewModel.size > 0) {
                            isVisible = true
                            number = quizViewModel.size
                        } else {
                            isVisible = false
                            number = 0
                        }
                    }
                else -> {}
            }
        }
    }
}
