package com.github.sdpsharelook.revision

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.github.sdpsharelook.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RevisionQuizFragment : RevisionQuizFragmentLift()

open class RevisionQuizFragmentLift : Fragment() {
    private val viewModel: RevisionQuizViewModel by viewModels()
    private var showHelp = false
    private val buttonIds = mapOf(
        R.id.answerQualityButton0 to R.string.quality0,
        R.id.answerQualityButton1 to R.string.quality1,
        R.id.answerQualityButton2 to R.string.quality2,
        R.id.answerQualityButton3 to R.string.quality3,
        R.id.answerQualityButton4 to R.string.quality4,
        R.id.answerQualityButton5 to R.string.quality5
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<FloatingActionButton>(R.id.helpToggleButton)
            .setOnClickListener {
                handleHelpToggle(view)
            }
        view.findViewById<ConstraintLayout>(R.id.quizLayout)
            .setOnClickListener {
                view.isClickable = false
                view.isFocusable = false
                viewModel.onEvent(QuizEvent.Continue)
            }
        lifecycleScope.launch {
            viewModel.uiEvent.apply { TODO() }
        }
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