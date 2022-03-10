package com.github.sdpsharelook.Section

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.sdpsharelook.R
import com.github.sdpsharelook.databinding.ActivitySectionBinding

class SectionActivity : AppCompatActivity(), SectionClickListener {

    private lateinit var binding: ActivitySectionBinding
    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_open) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addingBtn.setOnClickListener {
            Toast.makeText(this, "New Section added", Toast.LENGTH_SHORT).show()
            addSection("kitchen", R.drawable.spain)
            binding.addingBtn.startAnimation(rotateOpen)

        }
        val mainActivity = this
        binding.recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        binding.recyclerView.apply {
            adapter = CardAdapter(sectionList, mainActivity)
        }


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