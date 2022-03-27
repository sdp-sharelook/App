package com.github.sdpsharelook.authorization

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.sdpsharelook.GreetingActivity
import com.github.sdpsharelook.R
import kotlinx.coroutines.*

const val GREET_NAME_EXTRA = "com.github.sdpsharelook.NAME"
lateinit var auth: AuthProvider

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FireAuth()
    }


    fun logIn(view: View) {
        //TODO: add e-mail and password verification before login button is pressed
        val email = findViewById<EditText>(R.id.email).text.toString()
        val password = findViewById<EditText>(R.id.password).text.toString()

        if (auth.currentUser != null) greet(auth.currentUser?.displayName)

        GlobalScope.launch {
            val user = auth.signInWithEmailAndPassword(email, password)
            if (user.isSuccess) {


                runOnUiThread {
                    Toast.makeText(applicationContext, "Logged in !", Toast.LENGTH_SHORT).show()
                }
                greet(user.getOrThrow().displayName)
            } else {
                runOnUiThread {
                    Toast.makeText(
                        applicationContext,
                        user.exceptionOrNull()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    fun greet(name: String?) {
        val tName =
            if (name.isNullOrBlank() || auth.currentUser!!.isAnonymous) "anonymous" else name
        val intent = Intent(this, GreetingActivity::class.java).apply {
            putExtra(GREET_NAME_EXTRA, tName)
        }
        startActivity(intent)
    }

    fun goToSignUp(view: View) {
        startActivity(Intent(this, SignUpActivity::class.java).apply { })
    }
}