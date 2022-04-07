package com.github.sdpsharelook.section

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.sdpsharelook.R
import com.github.sdpsharelook.databinding.ActivitySectionBinding
import com.github.sdpsharelook.databinding.CardSectionBinding
import com.github.sdpsharelook.databinding.PopupBinding

var edit = false
var editPosition = 0


class SectionActivity : AppCompatActivity(), SectionClickListener {

    private lateinit var binding: ActivitySectionBinding
    private lateinit var popupBinding: PopupBinding
    private lateinit var cardBinding: CardSectionBinding

    private lateinit var dialog: Dialog
    var mainCountryList = initList()


    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_open
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySectionBinding.inflate(layoutInflater)
        popupBinding = PopupBinding.inflate(layoutInflater)
        cardBinding = CardSectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionActivity = this

        //init list of possible languages for the spinner
        initList()

        dialog = Dialog(sectionActivity)
        dialog.setContentView(popupBinding.root)

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


        popupBinding.popupAddBtn.setOnClickListener { popupListener(cardAdapter) }
    }

    private fun popupListener(cardAdapter: CardAdapter) {
        val sectionName = popupBinding.editSectionName.text.toString()
        val countryIndex = popupBinding.spinnerCountries.selectedItemPosition

        if (edit) {
            cardAdapter.editItem(Section(sectionName, mainCountryList.get(countryIndex).flag))
        } else {
            addSection(sectionName, mainCountryList[countryIndex].flag)
        }
        Toast.makeText(this, "Section: $sectionName saved", Toast.LENGTH_SHORT).show()
        dialog.dismiss()
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
        binding.recyclerView.adapter?.notifyItemInserted(sectionList.lastIndex)
    }

    private fun removeSection(index: Int) {
        sectionList.removeAt(index)
        binding.recyclerView.adapter?.notifyItemRemoved(index)
    }

    private fun editSectionName(index: Int, title: String, flag: Int) {
        assert(index == 0 && sectionList.isNotEmpty())
        sectionList[0] = Section(title, flag)
        binding.recyclerView.adapter?.notifyItemChanged(0)
    }

    override fun onClick(section: Section) {
        val intent = Intent(applicationContext, SectionDetail::class.java)
        intent.putExtra(SECTION_ID, section.id)
        startActivity(intent)
    }

}