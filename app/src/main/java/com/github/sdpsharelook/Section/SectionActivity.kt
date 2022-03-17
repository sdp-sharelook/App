package com.github.sdpsharelook.Section

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.sdpsharelook.R
import com.github.sdpsharelook.databinding.ActivitySectionBinding
import com.github.sdpsharelook.databinding.PopupBinding

class SectionActivity : AppCompatActivity(), SectionClickListener {

    private lateinit var binding: ActivitySectionBinding
    private lateinit var popupBinding: PopupBinding
    private lateinit var dialog: Dialog

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_open) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySectionBinding.inflate(layoutInflater)
        popupBinding = PopupBinding.inflate(layoutInflater)

        setContentView(binding.root)
        val sectionActivity = this

        //init list of traduction for the spinner
        initList()

        //Set up the spinner


        popupBinding.spinnerCountries.adapter = CountryAdapter(sectionActivity, mainCountryList)

        binding.addingBtn.setOnClickListener {

            dialog = Dialog(sectionActivity)

            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            Toast.makeText(this, "New Section added", Toast.LENGTH_SHORT).show()
//            addSection("kitchen", R.drawable.spain)
            binding.addingBtn.startAnimation(rotateOpen)
            dialog.setContentView(popupBinding.root)
            dialog.show()
        }

        popupBinding.popupAddBtn.setOnClickListener{
           // TODO recupe section name et flage et add section
            addSection("kitchen", R.drawable.spain)
            dialog.dismiss()
        }


        binding.recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        binding.recyclerView.apply {
            adapter = CardAdapter(sectionList, sectionActivity)
        }

    }

    private fun initList() {
        mainCountryList.add(CountryItem(R.drawable.spain))
        mainCountryList.add(CountryItem(R.drawable.us))
    }

    private fun addSection(title: String, flag: Int) {
        val section = Section(title, flag)
        sectionList.add(section)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onClick(section: Section) {
        val intent = Intent(applicationContext, SectionDetail::class.java)
        intent.putExtra(SECTION_ID, section.id)
        startActivity(intent)
    }

}