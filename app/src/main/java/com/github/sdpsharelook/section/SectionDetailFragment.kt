package com.github.sdpsharelook.section

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.github.sdpsharelook.SelectPictureFragment
import com.github.sdpsharelook.Word
import com.github.sdpsharelook.databinding.FragmentSectionDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SectionDetailFragment : SectionDetailFragmentLift()

open class SectionDetailFragmentLift : Fragment() {


    /**
     * This property is only valid between onCreateView and onDestroyView.
     */
    private val binding get() = _binding!!
    private var _binding: FragmentSectionDetailBinding? = null

    private val wordList = mutableListOf<Word>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: SectionDetailFragmentArgs by navArgs()
        val section = sectionFromId(args.sectionID)
        val word = args.word

//        CoroutineScope(Dispatchers.IO).launch {
//            section!!.databaseRepo.flow().collect {
//                when {
//                    it.isSuccess -> {
//                        val message = it.getOrNull().toString()
//                        withContext(Dispatchers.Main) {
//                            findViewById<TextView>(R.id.database_contents).apply {
//                                text = message
//                            }
//                        }
//                    }
//                    it.isFailure -> {
//                        it.exceptionOrNull()?.printStackTrace()
//                    }
//                }
//            }
//        }


        // If we are adding a word from the translator Activity
        if (word != null) {
            lifecycleScope.launch {
                section?.databaseRepo?.insert(section.sectionRepo, word.toList())
            }
            addSectionWord(word)
        }

        if (section != null) {
            binding.sectionTitle.text = section.title
            binding.sectionFlag.setImageResource(section.flag)
        }

        binding.wordList.adapter = WordAdapter(requireContext(), wordList)
        binding.wordList.setOnItemClickListener { _, _, _, _ ->
            SelectPictureFragment(word!!) {
                Toast.makeText(requireContext(), it ?: "picture deleted", Toast.LENGTH_SHORT).show()
            }.show(parentFragmentManager, null)
        }
    }

    private fun addSectionWord(word: Word) {
        wordList.add(word)
    }

    private fun sectionFromId(sectionID: Int): Section? {
        for (section in sectionList) {
            if (sectionID == section.id) {
                return section
            }
        }
        return null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSectionDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}