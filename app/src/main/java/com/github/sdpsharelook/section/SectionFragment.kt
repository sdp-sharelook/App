package com.github.sdpsharelook.section

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.sdpsharelook.Word
import com.github.sdpsharelook.databinding.CardSectionBinding
import com.github.sdpsharelook.databinding.FragmentSectionBinding
import com.github.sdpsharelook.databinding.PopupBinding
import com.github.sdpsharelook.downloads.MLKitTranslatorDownloader
import com.github.sdpsharelook.downloads.TranslatorDownloader
import com.github.sdpsharelook.language.Language
import com.github.sdpsharelook.storage.IRepository
import com.github.sdpsharelook.translate.MLKitTranslator
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.languageid.LanguageIdentification
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*
import javax.inject.Inject

var edit = false

var sectionList: MutableList<Section> = mutableListOf()

@AndroidEntryPoint
class SectionFragment : SectionFragmentLift()
open class SectionFragmentLift : Fragment(), SectionClickListener {

    //    @Inject
//    lateinit var
    val translatorDownloader: TranslatorDownloader =
        MLKitTranslatorDownloader(
            MLKitTranslator(LanguageIdentification.getClient()),
            RemoteModelManager.getInstance()
        )

    /**
     * This property is only valid between onCreateView and onDestroyView.
     */
    private val binding get() = _binding!!
    private var _binding: FragmentSectionBinding? = null
    private lateinit var popupBinding: PopupBinding
    private lateinit var cardBinding: CardSectionBinding
    private lateinit var availableLanguages: List<Language>

    @Inject
    lateinit var databaseWordList: IRepository<List<Word>>

    private lateinit var dialog: Dialog

    private var sectionWord: Word? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: SectionFragmentArgs by navArgs()
        if (args.sectionWord != null) {
            sectionWord = args.sectionWord?.let { Json.decodeFromString<Word>(it) }
        }
        popupBinding = PopupBinding.inflate(layoutInflater)
        cardBinding = CardSectionBinding.inflate(layoutInflater)

        //init list of possible languages for the spinner

        // set up the popup when cliking on add button
        dialog = Dialog(requireContext())
        dialog.setContentView(popupBinding.root)
        dialog.setOnDismissListener { popupBinding.editSectionName.text.clear() }

        // set up the spinner
        putLanguagesInSpinners()

        // set up the recyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val cardAdapter = CardAdapter(this, dialog, databaseWordList)
        binding.recyclerView.adapter = cardAdapter

        lifecycleScope.launch(Dispatchers.IO) {
            collectSectionFlow()
        }

        binding.addingBtn.setOnClickListener {
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialog.show()
        }

        popupBinding.popupAddBtn.setOnClickListener {

            val sectionName = popupBinding.editSectionName.text.toString()
            val countryIndex = popupBinding.spinnerCountries.selectedItemPosition
            val flag =
                (popupBinding.spinnerCountries.adapter as SpinnerAdapter).getItemFlag(countryIndex)
            val newSection = Section(
                sectionName,
                flag,
                UUID.randomUUID().toString()
            )

            // Popu do 2 different things if it is editing a section or creating one
            if (edit) {
                cardAdapter.editItem(sectionName, flag)
            } else {
                addSection(newSection)
                Toast.makeText(requireContext(), "Section: $sectionName saved", Toast.LENGTH_SHORT)
                    .show()
            }
            dialog.dismiss()
        }
    }

    private suspend fun collectSectionFlow() {
        databaseWordList.flowSection().collect {
            when {
                it.isSuccess -> {
                    sectionList = it.getOrDefault(emptyList())?.toMutableList() ?: mutableListOf()
                }
                it.isFailure -> {
                    it.exceptionOrNull()?.printStackTrace()
                }
            }
            activity?.runOnUiThread(Runnable() {
                run() {
                    updateData()
                }

            })
        }
    }

    private fun updateData() {
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun putLanguagesInSpinners() {
        CoroutineScope(Dispatchers.IO).launch {
            availableLanguages =
                translatorDownloader.downloadedLanguages() ?: listOf(Language("en"))
            withContext(Dispatchers.Main) {
                popupBinding.spinnerCountries.adapter = SpinnerAdapter(
                    requireContext(), availableLanguages
                )
            }
        }
    }

    private fun addSection(section: Section) {
        // if the section already exist do not add it
        lifecycleScope.launch {
            databaseWordList.insertSection(section)
        }
        true
    }

    override fun onClick(section: Section) {
        val action = SectionFragmentDirections.actionMenuSectionsLinkToSectionDetailFragment(
            Json.encodeToString(section)
        )



        lifecycleScope.launch(Dispatchers.IO) {
            sectionWord?.let {
                databaseWordList.insertList(section.id, listOf(it))
            }
            sectionWord = null
        }

        findNavController().navigate(action)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSectionBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}