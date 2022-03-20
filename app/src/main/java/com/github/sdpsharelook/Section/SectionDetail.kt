package com.github.sdpsharelook.Section

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.sdpsharelook.databinding.ActivitySectionDetailBinding

class SectionDetail : AppCompatActivity() {

    private lateinit var binding: ActivitySectionDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySectionDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionID = intent.getIntExtra(SECTION_ID, -1)
        val section = sectionFromId(sectionID)

        if (section != null){
            binding.sectionTitle.text = section.title
            binding.sectionFlag.setImageResource(section.flag)
        }
    }

    private fun sectionFromId(sectionID: Int): Section? {
        for (section in sectionList){
            if (sectionID == section.id){
                return section
            }
        }
        return null
    }
}