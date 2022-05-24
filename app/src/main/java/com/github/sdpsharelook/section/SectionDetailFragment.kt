package com.github.sdpsharelook.section

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.github.sdpsharelook.Word
import com.github.sdpsharelook.databinding.FragmentSectionDetailBinding
import com.github.sdpsharelook.storage.RTDBWordListRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@AndroidEntryPoint
class SectionDetailFragment : Fragment() {

    @Inject
    lateinit var wordRTDB : RTDBWordListRepository

    /**
     * This property is only valid between onCreateView and onDestroyView.
     */
    private val binding get() = _binding!!
    private var _binding: FragmentSectionDetailBinding? = null

    private var wordList = mutableListOf<Word>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: SectionDetailFragmentArgs by navArgs()
        var section:Section? = null
        if(args.section!= null){

            section = Json.decodeFromString<Section>(args.section!!)
        }

        /**set the section detail**/
        Log.e("SECTION", section.toString())
        if (section != null) {
            binding.sectionTitle.text = section.title
            binding.sectionFlag.setImageResource(section.flag)
        }




        lifecycleScope.launch{
            if (section != null) {
                collectListFlow(section)
            }
        }

        /**Check if we are adding a word from the translator Fragment**/


    }

    private suspend fun collectListFlow(section: Section) {
        Log.e("", "PSODHSÖLIHGÖOL")
        wordRTDB.flow(section.id).collect{
            when {
                it.isSuccess -> {
                    wordList = it.getOrDefault(emptyList()) as MutableList<Word>

                    binding.wordList.adapter = SectionWordAdapter(requireContext(), wordList)
                    Log.e("WORD ",wordList.toString())
                }
                it.isFailure -> {
                    Log.e("WORD ",wordList.toString())
                    it.exceptionOrNull()?.printStackTrace()
                }
            }
            (binding.wordList.adapter as BaseAdapter).notifyDataSetChanged()
        }
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