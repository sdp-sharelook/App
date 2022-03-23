package com.github.sdpsharelook.authorization

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.github.sdpsharelook.GreetingActivity
import com.github.sdpsharelook.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        auth = FireAuth()
    }


    fun signUp(@Suppress("UNUSED_PARAMETER")view: View) {
        //TODO: add e-mail and password verification before login button is pressed
        val email = findViewById<EditText>(R.id.email).text.toString()
        val password = findViewById<EditText>(R.id.password).text.toString()

        if (email.isNullOrBlank()) {
            Toast.makeText(this, "Email cannot be left blank!", Toast.LENGTH_LONG).show()
            return
        }
        if (password.isNullOrBlank()) {
            Toast.makeText(this, "Password cannot be left blank!", Toast.LENGTH_LONG).show()
            return
        }



        GlobalScope.launch {
            val user = auth.createUserWithEmailAndPassword(email, password)
            when (user) {
                is Result.Success<User> -> {
                    runOnUiThread {
                        Toast.makeText(applicationContext, "Signed Up !", Toast.LENGTH_LONG).show()
                    }
                    greet(user.data.displayName)
                }
                is Result.Error -> {
                    runOnUiThread {
                        Toast.makeText(
                            applicationContext,
                            user.exception.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
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
}