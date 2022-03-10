package com.github.sdpsharelook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        auth = FirebaseAuth.getInstance()
    }


    fun signUp(view:View){
        //TODO: add e-mail and password verification before login button is pressed
        val email = findViewById<EditText>(R.id.email).text.toString()
        val password = findViewById<EditText>(R.id.password).text.toString()

        if(email.isNullOrBlank()){
            Toast.makeText(this,"Email cannot be left blank!",Toast.LENGTH_LONG ).show()
            return
        }
        if(password.isNullOrBlank()){
            Toast.makeText(this,"Password cannot be left blank!",Toast.LENGTH_LONG ).show()
            return
        }

        auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener { res ->
            Toast.makeText(this,"Signed Up !",Toast.LENGTH_LONG ).show()
            greet(auth.currentUser?.displayName)
        }.addOnFailureListener{exc->
            Toast.makeText(this,exc.message,Toast.LENGTH_LONG ).show()
        }


    }
    fun greet(name: String?){
        val tName = if(name.isNullOrBlank() || auth.currentUser!!.isAnonymous) "anonymous" else name
        val intent = Intent(this,GreetingActivity::class.java).apply {
            putExtra(GREET_NAME_EXTRA, tName)
        }
        startActivity(intent)
    }
}