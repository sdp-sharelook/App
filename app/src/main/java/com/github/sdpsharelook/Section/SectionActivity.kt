package com.github.sdpsharelook.Section

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.sdpsharelook.R
import com.github.sdpsharelook.databinding.ActivitySectionBinding
import com.github.sdpsharelook.databinding.CardSectionBinding
import com.github.sdpsharelook.databinding.PopupBinding
import com.github.sdpsharelook.storage.RTDBWordListRepository
import com.github.sdpsharelook.storage.RTDBWordSectionRepository


var edit = false

val TRANSLATOR_WORD = "translatorExtra"


class SectionActivity : AppCompatActivity(), SectionClickListener {

    private lateinit var binding: ActivitySectionBinding
    private lateinit var popupBinding: PopupBinding
    private lateinit var cardBinding: CardSectionBinding
    private var databaseWordList = RTDBWordSectionRepository()

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

        // set up the popup when cliking on add button
        dialog = Dialog(sectionActivity)
        dialog.setContentView(popupBinding.root)
        dialog.setOnDismissListener{
            popupBinding.editSectionName.setText("Section name")
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

        popupBinding.popupAddBtn.setOnClickListener{
            var sectionName = popupBinding.editSectionName.text.toString()
            var countryIndex = popupBinding.spinnerCountries.selectedItemPosition
            var newSection = Section(
                sectionName,
                mainCountryList[countryIndex].flag,
                databaseWordList,
                sectionName + countryIndex.toString()
            )

            // Popu do 2 different things if it is editing a section or creating one
            if (edit){
                cardAdapter.editItem(sectionName, mainCountryList.get(countryIndex).flag)
                Toast.makeText(this, "Section: " + sectionName + " edited", Toast.LENGTH_SHORT).show()
            } else if(addSection(newSection)){
                Toast.makeText(this, "Section: " + sectionName + " saved", Toast.LENGTH_SHORT).show()
            }else{
                // if the section already exist
                Toast.makeText(this, "This " + sectionName + " already exist", Toast.LENGTH_SHORT).show()
            }

            dialog.dismiss()
        }

    }

    private fun initList(): List<CountryItem> {
        var list = mutableListOf<CountryItem>()
        list.add(CountryItem(R.drawable.spain))
        list.add(CountryItem(R.drawable.us))
        return list
    }

    private fun addSection(section: Section): Boolean {
        // if the section already exist do not add it
        return if(sectionList.contains(section)){
            false
        }else{
            sectionList.add(section)
            binding.recyclerView.adapter?.notifyDataSetChanged()
            true
        }
    }

    override fun onClick(section: Section) {
        val newIntent = Intent(applicationContext, SectionDetail::class.java)

        if(addWordToSection){
            var sectionWord = intent.getSerializableExtra(TRANSLATOR_WORD) as SectionWord
            newIntent.putExtra(SECTION_ID, section.id).putExtra(TRANSLATOR_WORD, sectionWord)
        }else newIntent.putExtra(SECTION_ID, section.id)
        startActivity(newIntent)
    }

}