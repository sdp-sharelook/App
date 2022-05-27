package com.github.sdpsharelook.authorization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.github.sdpsharelook.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : LoginFragmentLift()

open class LoginFragmentLift : Fragment() {

    /**
     * This property is only valid between onCreateView and onDestroyView.
     */
    private val binding get() = _binding!!
    private var _binding: FragmentLoginBinding? = null

    @Inject
    lateinit var auth: AuthProvider

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginButton.setOnClickListener { logIn() }
        binding.signUpButton.setOnClickListener { goToSignUp() }
    }

    private fun logIn() {
        //TODO: add e-mail and password verification before login button is pressed
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()

        if (auth.currentUser != null) moveToProfileFragment()

        lifecycleScope.launch {
            val user = auth.signInWithEmailAndPassword(email, password)
            if (user.isSuccess) {
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), "Logged in !", Toast.LENGTH_SHORT).show()
                }
                moveToProfileFragment()
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

    private fun moveToProfileFragment() {
        //val tName =
        //    if (name.isNullOrBlank() || auth.currentUser!!.isAnonymous) "anonymous" else name
        val action = LoginFragmentDirections.actionMenuLoginLinkToProfileInformationFragment()
        findNavController().navigate(action)
    }

    private fun goToSignUp() {
        val action = LoginFragmentDirections.actionMenuLoginLinkToSignUpFragment()
        findNavController().navigate(action)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}