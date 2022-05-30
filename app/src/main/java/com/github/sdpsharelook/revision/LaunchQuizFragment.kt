package com.github.sdpsharelook.revision

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.github.sdpsharelook.R
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class LaunchQuizFragment : LaunchQuizFragmentLift()

open class LaunchQuizFragmentLift : Fragment() {
    private val viewModel: RevisionQuizViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_launch_quiz, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.startQuizButton).setOnClickListener {
            try{
                val len =
                    view.findViewById<EditText>(R.id.quizLengthPicker).text.toString().toInt()
                viewModel.onEvent(QuizEvent.StartQuiz(len))
            } catch (e: NumberFormatException) {
                Snackbar.make(view,"Must be a number",Snackbar.LENGTH_SHORT).show()
            }
        }
        lifecycleScope.launch {
            collectViewModelEvent(view)
        }
    }

    private suspend fun collectViewModelEvent(view: View) {
        viewModel.uiEvent.collect {
            when (it) {
                is UiEvent.Navigate -> when (it.route) {
                    Routes.QUIZ -> {
                        val action =
                            LaunchQuizFragmentDirections.actionLaunchQuizFragmentToRevisionQuizFragment()
                        withContext(Dispatchers.Main) { findNavController().navigate(action) }
                    }
                }
                is UiEvent.ShowSnackbar ->
                    Snackbar.make(view, it.message, Snackbar.LENGTH_SHORT).show()
                else -> Unit
            }
        }
    }


}