package com.github.sdpsharelook.section

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.github.sdpsharelook.Word
import com.github.sdpsharelook.databinding.FragmentSectionDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SectionDetailFragment : Fragment() {


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
        val sectionWord = args.sectionWord

        /**set the section detail**/
        if (section != null) {
            binding.sectionTitle.text = section.title
            binding.sectionFlag.setImageResource(section.flag)
        }

        binding.wordList.adapter = SectionWordAdapter(requireContext(), wordList)


        lifecycleScope.launch {
            collectListFlow(section!!)
        }

        /**Check if we are adding a word from the translator Activity**/
        if (sectionWord != null) {
            lifecycleScope.launch {
                section?.databaseRepo?.insert(section.sectionRepo, listOf(sectionWord))
            }
            addSectionWord(sectionWord)
        }

    }

    private fun collectListFlow(section: Section) {
        //Todo

    }

    private fun addSectionWord(sw: Word) {
        wordList.add(sw)
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
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSectionDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}