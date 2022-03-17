package com.github.sdpsharelook.authorization

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.github.sdpsharelook.GreetingActivity
import com.github.sdpsharelook.R
import com.google.firebase.auth.FirebaseAuth
import java.lang.IllegalArgumentException

const val GREET_NAME_EXTRA = "com.github.sdpsharelook.NAME"
lateinit var auth: FirebaseAuth

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
    }


    fun logIn(view: View) {
        //TODO: add e-mail and password verification before login button is pressed
        val email = findViewById<EditText>(R.id.email).text.toString()
        val password = findViewById<EditText>(R.id.password).text.toString()

        if (auth.currentUser != null) greet(auth.currentUser?.displayName)
        try {
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener { task ->
                Toast.makeText(applicationContext, "Logged in !", Toast.LENGTH_SHORT).show()
                greet(auth.currentUser?.displayName);
            }.addOnFailureListener { exc ->
                Toast.makeText(this, exc.message, Toast.LENGTH_LONG).show()
            }
        }catch (e:IllegalArgumentException){
            Toast.makeText(this,e.message,Toast.LENGTH_LONG).show()
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