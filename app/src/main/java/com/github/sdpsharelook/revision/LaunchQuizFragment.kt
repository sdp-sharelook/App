package com.github.sdpsharelook.revision

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.sdpsharelook.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LaunchQuizFragment : LaunchQuizFragmentLift()

open class LaunchQuizFragmentLift : Fragment() {
    private val viewModel: RevisionQuizViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_launch_quiz, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.startQuizButton).setOnClickListener {
            val action =
                LaunchQuizFragmentDirections.actionLaunchQuizFragmentToRevisionQuizFragment()
            findNavController().navigate(action)
        }
    }


}