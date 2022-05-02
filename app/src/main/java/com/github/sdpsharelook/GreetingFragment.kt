package com.github.sdpsharelook

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs

class GreetingFragment : Fragment(R.layout.fragment_greeting) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args : GreetingFragmentArgs by navArgs()
        val name = args.name
        val message = "Hello $name!"
        view.findViewById<TextView>(R.id.greetingMessage).apply { text = message }
    }
}