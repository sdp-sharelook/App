package com.github.sdpsharelook.Section

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.sdpsharelook.R
import com.github.sdpsharelook.databinding.ActivitySectionBinding
import com.github.sdpsharelook.databinding.CardSectionBinding
import com.github.sdpsharelook.databinding.PopupBinding
import com.github.sdpsharelook.storage.RTDBWordListRepository


var edit = false

const val TRANSLATOR_WORD = "translatorExtra"


class SectionActivity : AppCompatActivity(), SectionClickListener {

    private lateinit var binding: ActivitySectionBinding
    private lateinit var popupBinding: PopupBinding
    private lateinit var cardBinding: CardSectionBinding
    private var databaseWordList = RTDBWordListRepository()

    private lateinit var dialog: Dialog
    var mainCountryList = initList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySectionBinding.inflate(layoutInflater)
        popupBinding = PopupBinding.inflate(layoutInflater)
        cardBinding = CardSectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sectionActivity = this

        //init list of possible languages for the spinner
        initList()

        // set up the popup when clicking on add button
        dialog = Dialog(sectionActivity)
        dialog.setContentView(popupBinding.root)
        dialog.setOnDismissListener {
            popupBinding.editSectionName.setText(getString(R.string.section_name_hint))
        }

        // set up the spinner
        popupBinding.spinnerCountries.adapter = CountryAdapter(sectionActivity, mainCountryList)

        // set up the recyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        val cardAdapter = CardAdapter(sectionList, sectionActivity, dialog)
        binding.recyclerView.adapter = cardAdapter


        binding.addingBtn.setOnClickListener {
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialog.show()
        }

        popupBinding.popupAddBtn.setOnClickListener {
            val sectionName = popupBinding.editSectionName.text.toString()
            val countryIndex = popupBinding.spinnerCountries.selectedItemPosition
            // Popu do 2 different things if it is editing a section or creating one
            if (edit){
                cardAdapter.editItem(sectionName, mainCountryList.get(countryIndex).flag)
            } else{
                addSection(Section(
                    sectionName,
                    mainCountryList[countryIndex].flag,
                    databaseWordList,
                    sectionName + countryIndex
                ))
            }

            Toast.makeText(this, "Section: " + sectionName + " saved", Toast.LENGTH_SHORT).show()
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
        binding.recyclerView.adapter?.notifyItemInserted(sectionList.lastIndex)
    }

    override fun onClick(section: Section) {
        val newIntent = Intent(applicationContext, SectionDetail::class.java)

        if (addWordToSection) {
            val sectionWord = intent.getSerializableExtra(TRANSLATOR_WORD) as SectionWord
            newIntent.putExtra(SECTION_ID, section.id).putExtra(TRANSLATOR_WORD, sectionWord)
        }else newIntent.putExtra(SECTION_ID, section.id)
        startActivity(newIntent)
    }

}