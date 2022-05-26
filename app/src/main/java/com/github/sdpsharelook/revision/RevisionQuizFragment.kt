package com.github.sdpsharelook.revision

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.sdpsharelook.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RevisionQuizFragment : RevisionQuizFragmentLift()

open class RevisionQuizFragmentLift : Fragment() {
    private val viewModel: RevisionQuizViewModel by viewModels()
    private var showHelp = false

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

    }

    private fun handleHelpToggle(view: View) {
        if (showHelp) {
            showHelp = false
            view.findViewById<Button>(R.id.answerQualityButton0).setText(R.string.quality0)
            view.findViewById<Button>(R.id.answerQualityButton1).setText(R.string.quality2)
            view.findViewById<Button>(R.id.answerQualityButton2).setText(R.string.quality1)
            view.findViewById<Button>(R.id.answerQualityButton3).setText(R.string.quality3)
            view.findViewById<Button>(R.id.answerQualityButton4).setText(R.string.quality4)
            view.findViewById<Button>(R.id.answerQualityButton5).setText(R.string.quality5)
        } else {
            showHelp = true
            view.findViewById<Button>(R.id.answerQualityButton0).text = ""
            view.findViewById<Button>(R.id.answerQualityButton1).text = ""
            view.findViewById<Button>(R.id.answerQualityButton2).text = ""
            view.findViewById<Button>(R.id.answerQualityButton3).text = ""
            view.findViewById<Button>(R.id.answerQualityButton4).text = ""
            view.findViewById<Button>(R.id.answerQualityButton5).text = ""
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_revision_quiz, container, false)
    }
}