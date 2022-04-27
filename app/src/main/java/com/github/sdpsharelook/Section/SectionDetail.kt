package com.github.sdpsharelook.Section

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
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


        CoroutineScope(Dispatchers.IO).launch {
            collectSectionWordFlow(section!!)
        }

        /**Check if we are adding a word from the translator Activity**/
        if(addWordToSection){
            val wordTranslated = intent.getSerializableExtra(TRANSLATOR_WORD) as SectionWord
            CoroutineScope(Dispatchers.IO).launch {
                // add the word to the database
                section!!.databaseRepo.insert(section.sectionRepo, wordTranslated.toList())
            }
            addSectionWord(wordTranslated)
            addWordToSection = false
        }

    }

    private suspend fun collectSectionWordFlow(section: Section) {

        Log.d("FONCTION", "message");

        section.databaseRepo.flow().collect {
            Log.d("Mnaf", it.toString())
            when {
                it.isSuccess -> {
                    Log.d("FUCK", it.toString())
                    val message = it.getOrNull().toString()
                    addSectionWord(SectionWord(message, message))
                }
                it.isFailure -> {
                    Log.d("FAIL", it.toString())
                    it.exceptionOrNull()?.printStackTrace()
                }
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
