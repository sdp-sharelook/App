package com.github.sdpsharelook.Section

import android.os.Bundle
import android.widget.BaseAdapter
import androidx.appcompat.app.AppCompatActivity
import com.github.sdpsharelook.databinding.ActivitySectionDetailBinding
import kotlinx.coroutines.*

var addWordToSection = false

class SectionDetail : AppCompatActivity() {

    private lateinit var binding: ActivitySectionDetailBinding

    private val wordList = mutableListOf<SectionWord>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySectionDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sectionID = intent.getIntExtra(SECTION_ID, -1)
        val section = sectionFromId(sectionID)

        /**set the section detail**/
        if (section != null){
            binding.sectionTitle.text = section.title
            binding.sectionFlag.setImageResource(section.flag)
        }

        binding.wordList.adapter = SectionWordAdapter(this, wordList)

        /**Check if we are adding a word from the translator Activity**/
        addWordFromTranslator(section)

        CoroutineScope(Dispatchers.IO).launch {
            collectSectionWordFlow(section!!)
        }


    }

    private fun addWordFromTranslator(section: Section?) {
        if(addWordToSection){
            val wordTranslated = intent.getSerializableExtra(TRANSLATOR_WORD) as SectionWord
            CoroutineScope(Dispatchers.IO).launch {
                // add the word to the database
                section!!.databaseRepo.insert(section.sectionRepo, wordTranslated)
            }
            addWordToSection = false
        }
    }

    private suspend fun collectSectionWordFlow(section: Section) {
        section.databaseRepo.flow(section.sectionRepo).collect {
            when {
                it.isSuccess -> {
                    val word = it.getOrNull() as SectionWord
                    addSectionWord(word)
                }
                it.isFailure -> {
                    it.exceptionOrNull()?.printStackTrace()
                }
            }
            runOnUiThread {
                (binding.wordList.adapter as BaseAdapter).notifyDataSetChanged()
            }
        }

    }

    fun addSectionWord(sw : SectionWord) {
        wordList.add(sw)
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
