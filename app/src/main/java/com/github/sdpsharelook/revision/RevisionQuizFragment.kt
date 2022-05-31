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
import com.github.sdpsharelook.R
import com.github.sdpsharelook.revision.QuizEvent.Continue
import com.github.sdpsharelook.revision.UiEvent.ShowAnswer
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RevisionQuizFragment : RevisionQuizFragmentLift()

open class RevisionQuizFragmentLift : Fragment() {
    private val viewModel: RevisionQuizViewModel by activityViewModels()
    private var showHelp = false
    private val buttonIds = mapOf(
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
    private lateinit var buttons: Map<Button, Int>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttons = buttonIds.mapKeys { view.findViewById(it.key) }
        buttons.forEach { it.key.visibility = INVISIBLE }
        helpToggle = view.findViewById(R.id.helpToggleButton)
        layout = view.findViewById(R.id.quizLayout)
        wordView = view.findViewById(R.id.quizWord)
        answerView = view.findViewById(R.id.quizAnswer)

        hide()
        viewModel.current.observe(viewLifecycleOwner) {
            wordView.text = it.source
            answerView.text = it.target
        }
        helpToggle.setOnClickListener { handleHelpToggle(view) }
        setClickableView(layout, true)
        layout.setOnClickListener {
            show()
            setClickableView(it, false)
            viewModel.onEvent(Continue)
        }
        lifecycleScope.launch {
            collectViewModelEvents(view)
        }
    }

    private suspend fun collectViewModelEvents(view: View) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is ShowAnswer -> buttonIds.keys.forEach {
                    view.findViewById<Button>(it).visibility = VISIBLE
                }
                else -> {}
            }
        }
    }

    private fun show() {
        setVisibilitiesAndActOnHelp(VISIBLE) { show() }
    }


    private fun hide() {
        setVisibilitiesAndActOnHelp(INVISIBLE) { hide() }
    }

    private fun setVisibilitiesAndActOnHelp(
        visibility: Int,
        action: FloatingActionButton.() -> Unit
    ) {
        answerView.visibility = visibility
        helpToggle.action()
        buttons.keys.forEach { it.visibility = visibility }
    }

    private fun setClickableView(
        view: View,
        clickable: Boolean
    ) {
        view.isClickable = clickable
        view.isFocusable = clickable
    }

    private fun handleHelpToggle(view: View) {
        if (showHelp) {
            showHelp = false
            buttonIds.forEach { (id, txt) ->
                view.findViewById<Button>(id).setText(txt)
            }
        } else {
            showHelp = true
            buttonIds.forEach { (id, _) ->
                view.findViewById<Button>(id).text = ""
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_revision_quiz, container, false)
    }
}