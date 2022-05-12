package com.github.sdpsharelook.section

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.github.sdpsharelook.Word
import com.github.sdpsharelook.databinding.FragmentSectionDetailBinding
import dagger.hilt.android.AndroidEntryPoint
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




        /**Check if we are adding a word from the translator Activity**/
        if (sectionWord != null) {
            lifecycleScope.launch {
                section?.databaseRepo?.insert(section.id, listOf(sectionWord))
            }
        }

        lifecycleScope.launch {
            collectListFlow(section!!)
        }

    }

    private suspend fun collectListFlow(section: Section) {
        section.databaseRepo.flow(section.id).collect{
            when {
                it.isSuccess -> {
                    val words = it.getOrNull()
                    words!!.forEach { word -> addSectionWord(word) }
                }
                it.isFailure -> {
                    it.exceptionOrNull()?.printStackTrace()
                }
            }
            (binding.wordList.adapter as BaseAdapter).notifyDataSetChanged()
        }
    }

    private fun addSectionWord(sw: Word) {
        wordList.add(sw)
    }

    private fun sectionFromId(sectionID: Int): Section? {
        for (section in sectionList) {
            if (sectionID == section.sectionSize) {
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