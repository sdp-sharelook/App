package com.github.sdpsharelook.revision

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.sdpsharelook.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RevisionQuizFragment : RevisionQuizFragmentLift()

open class RevisionQuizFragmentLift : Fragment() {

    companion object {
        fun newInstance() = RevisionQuizFragment()
    }

    private lateinit var viewModel: RevisionQuizViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_revision_quiz, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[RevisionQuizViewModel::class.java]
        // TODO: Use the ViewModel
    }
}