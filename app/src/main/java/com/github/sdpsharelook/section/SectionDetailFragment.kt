package com.github.sdpsharelook.section

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.github.sdpsharelook.SelectPictureFragment
import com.github.sdpsharelook.Word
import com.github.sdpsharelook.databinding.FragmentSectionDetailBinding
import com.github.sdpsharelook.storage.IRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@AndroidEntryPoint
class SectionDetailFragment : SectionDetailFragmentLift()
open class SectionDetailFragmentLift : Fragment() {

    @Inject
    lateinit var wordRTDB: IRepository<List<Word>>

    /**
     * This property is only valid between onCreateView and onDestroyView.
     */
    private val binding get() = _binding!!
    private var _binding: FragmentSectionDetailBinding? = null

    private var wordList = mutableListOf<Word>()

    private lateinit var section: Section

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: SectionDetailFragmentArgs by navArgs()

        if (args.section != null) {
            section = Json.decodeFromString<Section>(args.section!!)
        }

        /**set the section detail**/
        if (section != null) {
            binding.sectionTitle.text = section.title
            binding.sectionFlag.setImageResource(section.flag)
        }
        binding.wordList.isLongClickable = true

        binding.wordList.setOnItemLongClickListener { _, _, pos, _ ->
            lifecycleScope.launch {
                removeWord(pos, section!!)
            }
            true
        }

        binding.wordList.setOnItemClickListener { _, _, index, _ ->
            val word = wordList[index].source
            val language = wordList[index].sourceLanguage?.tag
            SelectPictureFragment().apply {
                arguments = Bundle().apply {
                    putString(SelectPictureFragment.LANGUAGE_PARAMETER, language)
                    putString(SelectPictureFragment.WORD_PARAMETER, word)
                }
                show(this@SectionDetailFragmentLift.parentFragmentManager, null)
                setFragmentResultListener(SelectPictureFragment.RESULT_PARAMETER) {_, bundle ->
                    val s = bundle.getString(SelectPictureFragment.RESULT_PARAMETER)
                    Toast.makeText(this@SectionDetailFragmentLift.requireContext(), s ?: "picture deleted", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        binding.wordList.adapter = WordAdapter(requireContext(), wordList)


        lifecycleScope.launch {
            section?.let { collectListFlow(it) }
        }

    }

    private suspend fun collectListFlow(section: Section) {
        wordRTDB.flow(section.id).collect {
            when {
                it.isSuccess -> {
                    wordList.clear()
                    wordList.addAll(it.getOrDefault(emptyList()) as MutableList<Word>)

                }
                it.isFailure -> {
                    it.exceptionOrNull()?.printStackTrace()
                }
            }
            (binding.wordList.adapter as BaseAdapter).notifyDataSetChanged()
        }
    }

    private suspend fun removeWord(pos: Int, section: Section) {
        wordRTDB.delete(section.id, listOf(wordList[pos]))
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