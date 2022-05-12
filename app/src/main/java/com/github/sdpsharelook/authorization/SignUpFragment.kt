package com.github.sdpsharelook.authorization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.github.sdpsharelook.databinding.FragmentSignUpBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    /**
     * This property is only valid between onCreateView and onDestroyView.
     */
    private val binding get() = _binding!!
    private var _binding: FragmentSignUpBinding? = null

    @Inject
    lateinit var auth: AuthProvider

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firstNameListener()
        lastNameListener()
        emailListener()
        prelimPasswordListener()
        passwordListener()
        binding.loginButton.setOnClickListener { checkBeforeSignUp() }
    }

    private fun firstNameListener() {
        binding.firstName.setOnFocusChangeListener { _, focus ->
            if (!focus) {
                binding.firstName.error = isFirstNameValid()
            }
        }
    }

    private fun isFirstNameValid(): String? {
        return if (binding.firstName.text.toString() == "")
            "Required"
        else
            null
    }

    private fun lastNameListener() {
        binding.lastName.setOnFocusChangeListener { _, focus ->
            if (!focus) {
                binding.lastName.error = isLastNameValid()
            }
        }
    }

    private fun isLastNameValid(): String? {
        return if (binding.lastName.text.toString() != "")
            null
        else
            "Required"
    }

    private fun emailListener() {
        binding.email.setOnFocusChangeListener { _, focus ->
            if (!focus) {
                binding.email.error = isEmailValid()
            }
        }

    }

    private fun isEmailValid(): String? {
        val regex =
            "(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))".toRegex()
        return if (binding.email.text.toString() == "")
            "Required"
        else if (!binding.email.text.toString().matches(regex))
            "Invalid Email Address"
        else
            null
    }

    private fun prelimPasswordListener() {
        binding.password.setOnFocusChangeListener { _, focus ->
            if (!focus) {
                binding.password.error = isPrelimPassword()
                binding.prelimPasswordBox.helperText = isPrelimPassword()
            }
        }
    }

    private fun isPrelimPassword(): String? {
        val upperCase = "(.*?[A-Z].*)".toRegex()
        val lowerCase = "(.*?[a-z].*)".toRegex()
        val oneDigit = "(.*?[0-9].*)".toRegex()
        val specialChar = "(.*?[#?!()@\$ %^&*-].*)".toRegex()
        val minLength8 = ".{8,}".toRegex()
        return if (binding.password.text.toString() == "")
            "Required"
        else if (!binding.password.text.toString().matches(upperCase))
            "Must contain an uppercase letter"
        else if (!binding.password.text.toString().matches(lowerCase))
            "Must contain a lowercase letter"
        else if (!binding.password.text.toString().matches(oneDigit))
            "Must contain at least one digit"
        else if (!binding.password.text.toString().matches(specialChar))
            "Must contain at least one special character"
        else if (!binding.password.text.toString().matches(minLength8))
            "Must contain at least 8 characters"
        else
            null
    }

    private fun passwordListener() {
        binding.password.setOnFocusChangeListener { _, focus ->
            if (!focus) {
                binding.passwordBox.error = isPasswordValid()
                binding.passwordBox.helperText = isPasswordValid()
            }
        }
    }

    private fun isPasswordValid(): String? = when {
        binding.password.text.toString() != binding.confirmPassword.text.toString() -> "Passwords do not match"
        else -> null
    }

    private fun checkBeforeSignUp() {
        updateErrors()
        val firstNameValid = binding.firstName.error == null
        val lastNameValid = binding.lastName.error == null
        val emailValid = binding.email.error == null
        val prelimPassValid = binding.prelimPasswordBox.helperText == null
        val passValid = binding.password.error == null && binding.passwordBox.helperText == null
        if (firstNameValid && lastNameValid && emailValid && prelimPassValid && passValid) {
            signUp()
        } else {
            Toast.makeText(requireContext(), "Form is filled incorrectly", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateErrors() {
        binding.firstName.error = isFirstNameValid()
        binding.lastName.error = isLastNameValid()
        binding.email.error = isEmailValid()
        binding.password.error = isPrelimPassword()
        binding.prelimPasswordBox.helperText = isPrelimPassword()
        binding.passwordBox.error = isPasswordValid()
        binding.passwordBox.helperText = isPasswordValid()
    }

    fun signUp() {
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()

        lifecycleScope.launch {
            val user = auth.createUserWithEmailAndPassword(email, password)
            when {
                user.isSuccess -> {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Signed Up !", Toast.LENGTH_LONG).show()
                    }
                    greet(user.getOrNull()!!.displayName)
                }
                user.isFailure -> {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireContext(),
                            user.exceptionOrNull()!!.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun greet(name: String?) {
        val tName =
            if (name.isNullOrBlank() || auth.currentUser!!.isAnonymous) "anonymous" else name
        val action = SignUpFragmentDirections.actionSignUpFragmentToGreetingFragment(tName)
        findNavController().navigate(action)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}