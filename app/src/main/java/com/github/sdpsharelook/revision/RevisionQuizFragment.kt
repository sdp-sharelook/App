package com.github.sdpsharelook.revision

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.github.sdpsharelook.R
import com.github.sdpsharelook.revision.QuizEvent.ClickEffortButton
import com.github.sdpsharelook.revision.QuizEvent.Continue
import com.github.sdpsharelook.revision.UiEvent.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class RevisionQuizFragment : RevisionQuizFragmentLift()

open class RevisionQuizFragmentLift : Fragment() {
    private val viewModel: RevisionQuizViewModel by activityViewModels()
    private var showingHelp = false
    private val buttonIds = listOf(
        R.id.answerQualityButton0 to R.string.quality0,
        R.id.answerQualityButton1 to R.string.quality1,
        R.id.answerQualityButton2 to R.string.quality2,
        R.id.answerQualityButton3 to R.string.quality3,
        R.id.answerQualityButton4 to R.string.quality4,
        R.id.answerQualityButton5 to R.string.quality5
    )

    private lateinit var helpToggle: FloatingActionButton
    private lateinit var layout: ConstraintLayout
    private lateinit var wordView: TextView
    private lateinit var answerView: TextView
    private lateinit var buttons: List<Button>
    private lateinit var buttonStrings: List<Int>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonStrings = buttonIds.map { it.second }
        buttons = buttonIds.map { view.findViewById(it.first) }
        buttons.forEachIndexed { i, it ->
            it.visibility = INVISIBLE
            it.setOnClickListener {
                viewModel.onEvent(ClickEffortButton(i))
            }
        }
        helpToggle = view.findViewById(R.id.helpToggleButton)
        layout = view.findViewById(R.id.quizLayout)
        wordView = view.findViewById(R.id.quizWord)
        answerView = view.findViewById(R.id.quizAnswer)

        hide()
        viewModel.onEvent(QuizEvent.Started)
        viewModel.current.apply {
            wordView.text = source
            answerView.text = target
        }
        helpToggle.setOnClickListener { handleHelpToggle() }
        setClickableView(layout, true)
        layout.setOnClickListener { viewModel.onEvent(Continue) }
        lifecycleScope.launch { collectViewModelEvents(view) }
    }

    private suspend fun collectViewModelEvents(view: View) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is ShowAnswer -> revealAnswer()
                is NewWord -> hideAnswer()
                is Navigate -> when (event.route) {
                    Routes.QUIZ_LAUNCH -> {
                        val action =
                            RevisionQuizFragmentDirections.actionRevisionQuizFragmentToLaunchQuizFragment()
                        withContext(Dispatchers.Main) { findNavController().navigate(action) }
                    }
                }
                else -> {}
            }
        }
    }

    private fun revealAnswer() {
        show()
        setClickableView(layout, false)
    }

    private fun hideAnswer() {
        hide()
        setClickableView(layout, true)
    }

    private fun show() {
        setVisibilities(VISIBLE) { show() }
    }


    private fun hide() {
        setVisibilities(INVISIBLE) { hide() }
    }

    private fun setVisibilities(
        visibility: Int,
        action: FloatingActionButton.() -> Unit
    ) {
        answerView.visibility = visibility
        helpToggle.action()
        buttons.forEach { it.visibility = visibility }
    }

    private fun setClickableView(
        view: View,
        clickable: Boolean
    ) {
        view.isClickable = clickable
        view.isFocusable = clickable
    }

    private fun handleHelpToggle() {
        if (!showingHelp) {
            showingHelp = true
            buttons.zip(buttonStrings).forEach { (b, txt) -> b.setText(txt) }
        } else {
            showingHelp = false
            buttons.zip(buttonStrings).forEach { (b, _) -> b.text = "" }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_revision_quiz, container, false)
    }
}