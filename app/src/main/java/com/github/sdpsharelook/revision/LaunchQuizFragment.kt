package com.github.sdpsharelook.revision

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.sdpsharelook.R
import com.github.sdpsharelook.revision.SnackbarShowers.LAUNCH_QUIZ
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@FlowPreview
@AndroidEntryPoint
class LaunchQuizFragment : LaunchQuizFragmentLift()

@FlowPreview
open class LaunchQuizFragmentLift : Fragment() {
    private val viewModel: RevisionQuizViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_launch_quiz, container, false)
    }

    private val sectionSelects: MutableList<SectionSelect> = mutableListOf()
    private lateinit var sectionSelectAdapter: SectionSelectAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sectionSelectAdapter = SectionSelectAdapter(sectionSelects)
        bindView(view)
        observeModel(sectionSelectAdapter)
        viewModel.onEvent(QuizEvent.RequestSections)
        handleViewModelEvents(view)
    }

    private fun bindView(view: View) {
        view.findViewById<Button>(R.id.start10QuizButton).setOnClickListener {
            viewModel.onEvent(QuizEvent.StartQuiz(10))
        }
        view.findViewById<Button>(R.id.startAllQuizButton).setOnClickListener {
            viewModel.onEvent(QuizEvent.StartQuiz(viewModel.size))
        }
        view.findViewById<Button>(R.id.startQuizButton).setOnClickListener {
            numberButton(view)
        }
        view.findViewById<RecyclerView>(R.id.sectionPicker).apply {
            adapter = sectionSelectAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setOnClickListener {
                view.findViewById<TextView>(R.id.maxWordsToQuiz).text = viewModel.size.toString()
            }
        }
    }

    private fun observeModel(sectionSelectAdapter: SectionSelectAdapter) {
        viewModel.checkboxes.observe(viewLifecycleOwner) { (sectionSelect, change) ->
            when (change) {
                "remove" -> {
                    val i = sectionSelects.indexOf(sectionSelect)
                    sectionSelects.remove(sectionSelect)
                    sectionSelectAdapter.notifyItemRemoved(i)
                }
                "add" -> {
                    sectionSelects.add(sectionSelect)
                    sectionSelectAdapter.notifyItemInserted(sectionSelects.lastIndex)
                }
            }
        }
    }

    private fun numberButton(view: View) {
        try {
            val len =
                view.findViewById<EditText>(R.id.quizLengthPicker).text.toString().toInt()
            viewModel.onEvent(QuizEvent.StartQuiz(len))
        } catch (e: NumberFormatException) {
            Snackbar.make(view, "Must be a number", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun handleViewModelEvents(view: View) {
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is UiEvent.Navigate -> when (event.route) {
                        Routes.QUIZ -> {
                            val action =
                                LaunchQuizFragmentDirections.actionLaunchQuizFragmentToRevisionQuizFragment()
                            withContext(Dispatchers.Main) { findNavController().navigate(action) }
                        }
                    }
                    is UiEvent.ShowSnackbar -> if (event.who == LAUNCH_QUIZ)
                        Snackbar.make(view, event.message, Snackbar.LENGTH_SHORT).show()
                    else -> Unit
                }
            }
        }
    }
}