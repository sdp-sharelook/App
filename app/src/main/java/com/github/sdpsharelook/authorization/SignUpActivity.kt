package com.github.sdpsharelook.authorization

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.github.sdpsharelook.GreetingActivity
import com.github.sdpsharelook.R
import com.github.sdpsharelook.databinding.ActivitySignUpBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firstNameListener()
        lastNameListener()
        emailListener()
        phoneListener()
        prelimPasswordListener()
        passwordListener()
        auth = FireAuth()
    }

    fun firstNameListener() {
        binding.firstName.setOnFocusChangeListener{ _, focus ->
            if (!focus) {
                if(binding.firstName.text.toString() != "") {
                    binding.firstNameBox.helperText = null;
                } else {
                    binding.firstNameBox.helperText = "Required"
                }
            }
        }
    }

    fun lastNameListener() {
        binding.lastName.setOnFocusChangeListener{ _, focus ->
            if (!focus) {
                if(binding.lastName.text.toString() != "") {
                    binding.lastNameBox.helperText = null;
                } else {
                    binding.lastNameBox.helperText = "Required"
                }
            }
        }
    }

    fun emailListener() {
        var regex = "/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$/i".toRegex()
        binding.email.setOnFocusChangeListener{_, focus ->
            if(!focus) {
                if (binding.lastName.text.toString() == "") {
                    binding.emailBox.helperText = "Required"
                } else
                if(!binding.email.text.toString().matches(regex)) {
                    binding.emailBox.helperText = "Invalid Email Address"
                } else {
                    binding.emailBox.helperText = null
                }
            }
        }

    }

    fun phoneListener() {
    }

    fun prelimPasswordListener() {
        var upperCase = "(?=.*?[A-Z])".toRegex()
        var lowerCase = "(?=.*?[a-z])".toRegex()
        var oneDigit = "(?=.*?[0-9])".toRegex()
        var specialChar = "(?=.*?[#?!@\$ %^&*-])".toRegex()
        var minLength8 = ".{8,}".toRegex()
        binding.phoneNumber.setOnFocusChangeListener{ _, focus ->
            if (!focus) {
                var string = binding.phoneNumber.text.toString()
                if(string == "") {
                    binding.lastNameBox.helperText = "Required";
                } else
                if(string.matches)

                else {
                    binding.lastNameBox.helperText = null
                }
            }
        }
    }

    fun passwordListener() {

    }

    fun signUp(view: View) {
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