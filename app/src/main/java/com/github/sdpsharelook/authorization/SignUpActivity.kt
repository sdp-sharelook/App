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
        prelimPasswordListener()
        passwordListener()
        auth = FireAuth()
    }

    private fun firstNameListener() {
        binding.firstName.setOnFocusChangeListener{ _, focus ->
            if (!focus) {
                if(binding.firstName.text.toString() != "") {
                    binding.firstNameBox.helperText = null
                } else {
                    binding.firstNameBox.helperText = "Required"
                }
            }
        }
    }

    private fun lastNameListener() {
        binding.lastName.setOnFocusChangeListener{ _, focus ->
            if (!focus) {
                if(binding.lastName.text.toString() != "") {
                    binding.lastNameBox.helperText = null
                } else {
                    binding.lastNameBox.helperText = "Required"
                }
            }
        }
    }

    private fun emailListener() {
        val regex = "(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))".toRegex()
        binding.email.setOnFocusChangeListener{_, focus ->
            if(!focus) {
                if (binding.email.text.toString() == "") {
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

    private fun prelimPasswordListener() {
        val upperCase = "(.*?[A-Z].*)".toRegex()
        val lowerCase = "(.*?[a-z].*)".toRegex()
        val oneDigit = "(.*?[0-9].*)".toRegex()
        val specialChar = "(.*?[#?!@\$ %^&*-].*)".toRegex()
        val minLength8 = ".{8,}".toRegex()
        binding.prelimpassword.setOnFocusChangeListener{ _, focus ->
            if (!focus) {
                binding.prelimpassword.text.toString()
                if(binding.prelimpassword.text.toString() == "") {
                    binding.prelimPasswordBox.helperText = "Required"
                } else
                if(!binding.prelimpassword.text.toString().matches(upperCase)) {
                    binding.prelimPasswordBox.helperText = "Must contain an uppercase letter"
                } else
                if(!binding.prelimpassword.text.toString().matches(lowerCase)) {
                    binding.prelimPasswordBox.helperText = "Must contain a lowercase letter"
                } else
                if(!binding.prelimpassword.text.toString().matches(oneDigit)) {
                    binding.prelimPasswordBox.helperText = "Must contain at least one digit"
                } else
                if(!binding.prelimpassword.text.toString().matches(specialChar)) {
                    binding.prelimPasswordBox.helperText = "Must contain at least one special character"
                } else
                if(!binding.prelimpassword.text.toString().matches(minLength8)) {
                    binding.prelimPasswordBox.helperText = "Must contain at least 8 characters"
                }
                else {
                    binding.prelimPasswordBox.helperText = null
                }
            }
        }
    }

    private fun passwordListener() {
        binding.password.setOnFocusChangeListener{_, focus ->
            if(!focus) {
                when {
                    binding.password.text.toString() == "" -> {
                        binding.passwordBox.helperText = "Required"
                    }
                    binding.password.text.toString() != binding.prelimpassword.text.toString() -> {
                        binding.passwordBox.helperText = "Passwords do not match"
                    }
                    else -> {
                        binding.passwordBox.helperText = null
                    }
                }
            }
        }
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