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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.sdpsharelook.R
import com.github.sdpsharelook.databinding.CardSectionBinding
import com.github.sdpsharelook.databinding.FragmentSectionBinding
import com.github.sdpsharelook.databinding.PopupBinding
import com.github.sdpsharelook.storage.IRepository
import com.github.sdpsharelook.storage.RTDBWordListRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

var edit = false

@AndroidEntryPoint
class SectionFragment : Fragment(), SectionClickListener {

    /**
     * This property is only valid between onCreateView and onDestroyView.
     */
    private val binding get() = _binding!!
    private var _binding: FragmentSectionBinding? = null
    private lateinit var popupBinding: PopupBinding
    private lateinit var cardBinding: CardSectionBinding
    @Inject
    lateinit var databaseWordList: IRepository<List<String>>

    private lateinit var dialog: Dialog
    var mainCountryList = initList()

    private var sectionWord: SectionWord? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: SectionFragmentArgs by navArgs()
        sectionWord = args.sectionWord
        popupBinding = PopupBinding.inflate(layoutInflater)
        cardBinding = CardSectionBinding.inflate(layoutInflater)

        //init list of possible languages for the spinner
        initList()

        // set up the popup when cliking on add button
        dialog = Dialog(requireContext())
        dialog.setContentView(popupBinding.root)
        dialog.setOnDismissListener {
            popupBinding.editSectionName.setText("Section name")
        }

        // set up the spinner
        popupBinding.spinnerCountries.adapter = CountryAdapter(requireContext(), mainCountryList)

        // set up the recyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val cardAdapter = CardAdapter(sectionList, this, dialog)
        binding.recyclerView.adapter = cardAdapter


        binding.addingBtn.setOnClickListener {
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialog.show()
        }

        popupBinding.popupAddBtn.setOnClickListener {
            val sectionName = popupBinding.editSectionName.text.toString()
            val countryIndex = popupBinding.spinnerCountries.selectedItemPosition
            // Popu do 2 different things if it is editing a section or creating one
            if (edit) {
                cardAdapter.editItem(sectionName, mainCountryList.get(countryIndex).flag)
            } else {
                addSection(
                    Section(
                        sectionName,
                        mainCountryList[countryIndex].flag,
                        databaseWordList,
                        sectionName + countryIndex
                    )
                )
            }

            Toast.makeText(requireContext(), "Section: $sectionName saved", Toast.LENGTH_SHORT)
                .show()
            dialog.dismiss()
        }
    }

    private fun initList(): List<CountryItem> {
        val list = mutableListOf<CountryItem>()
        list.add(CountryItem(R.drawable.spain))
        list.add(CountryItem(R.drawable.us))
        return list
    }

    private fun addSection(section: Section) {
        sectionList.add(section)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onClick(section: Section) {
        val action = SectionFragmentDirections.actionMenuSectionsLinkToSectionDetailFragment(
            section.id, sectionWord
        )
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