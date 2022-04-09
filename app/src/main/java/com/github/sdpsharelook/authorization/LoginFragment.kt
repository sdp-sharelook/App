package com.github.sdpsharelook.authorization

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.github.sdpsharelook.databinding.FragmentLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

lateinit var auth: AuthProvider

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FireAuth()
        binding.loginButton.setOnClickListener { logIn() }
        binding.signUpButton.setOnClickListener { goToSignUp() }
    }

    private fun logIn() {
        //TODO: add e-mail and password verification before login button is pressed
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()

        if (auth.currentUser != null) greet(auth.currentUser?.displayName)

        CoroutineScope(Dispatchers.IO).launch {
            val user = auth.signInWithEmailAndPassword(email, password)
            if (user.isSuccess) {
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), "Logged in !", Toast.LENGTH_SHORT).show()
                }
                greet(user.getOrThrow().displayName)
            } else {
                requireActivity().runOnUiThread {
                    Toast.makeText(
                        requireContext(),
                        user.exceptionOrNull()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun greet(name: String?) {
        // TODO
//        val tName =
//            if (name.isNullOrBlank() || auth.currentUser!!.isAnonymous) "anonymous" else name
//        val intent = Intent(this, GreetingActivity::class.java).apply {
//            putExtra(GREET_NAME_EXTRA, tName)
//        }
//        startActivity(intent)
    }

    private fun goToSignUp() {
        val action = LoginFragmentDirections.actionMenuLoginLinkToSignUpFragment()
        findNavController().navigate(action)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }
}