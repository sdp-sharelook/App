package com.github.sdpsharelook

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.sdpsharelook.Section.*
import com.github.sdpsharelook.databinding.CardSectionBinding
import com.github.sdpsharelook.databinding.FragmentSectionBinding
import com.github.sdpsharelook.databinding.PopupBinding

var edit = false
var editPosition = 0

/**
 * A simple [Fragment] subclass.
 */
class SectionFragment : Fragment(), SectionClickListener {

    private lateinit var binding: FragmentSectionBinding
    private lateinit var popupBinding: PopupBinding
    private lateinit var cardBinding: CardSectionBinding

    private lateinit var dialog: Dialog
    var mainCountryList = initList()

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_open) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onCreate(savedInstanceState)
        popupBinding = PopupBinding.inflate(layoutInflater)
        cardBinding = CardSectionBinding.inflate(layoutInflater)

        //init list of possible languages for the spinner
        initList()

        dialog = Dialog(requireContext())
        dialog.setContentView(popupBinding.root)

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


        popupBinding.popupAddBtn.setOnClickListener{
            val sectionName = popupBinding.editSectionName.text.toString()
            val countryIndex = popupBinding.spinnerCountries.selectedItemPosition

            if (edit){
                cardAdapter.editItem(Section(sectionName, mainCountryList.get(countryIndex).flag))
            } else {
                addSection(sectionName, mainCountryList.get(countryIndex).flag)
            }
            Toast.makeText(requireContext(), "Section: " + sectionName + " saved", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }


    }

    private fun initList(): List<CountryItem> {
        val list = mutableListOf<CountryItem>()
        list.add(CountryItem(R.drawable.spain))
        list.add(CountryItem(R.drawable.us))
        return list
    }

    private fun addSection(title: String, flag: Int) {
        val section = Section(title, flag)
        sectionList.add(section)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun removeSection(index: Int) {
        sectionList.removeAt(index)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun editSectionName(index: Int, title: String, flag: Int) {
        sectionList.set(0, Section(title, flag))
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onClick(section: Section) {
        val intent = Intent(requireContext(), SectionDetail::class.java)
        intent.putExtra(SECTION_ID, section.id)
        startActivity(intent)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSectionBinding.inflate(layoutInflater)
        return binding.root
    }
}