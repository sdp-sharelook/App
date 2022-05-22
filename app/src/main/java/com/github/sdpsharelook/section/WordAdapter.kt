package com.github.sdpsharelook.section

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.github.sdpsharelook.R
import com.github.sdpsharelook.Word

class WordAdapter(context: Context, wordList: List<SectionWord?>) :
    ArrayAdapter<SectionWord>(context, R.layout.wordlist_section, wordList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val word = getItem(position)
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.wordlist_section, parent, false)

        val sourceWord = view.findViewById<TextView>(R.id.sourceWord)
        val translatedWord = view.findViewById<TextView>(R.id.translatedWord)

        sourceWord?.text = word?.sourceText
        translatedWord?.text = word?.translatedText
        return view
    }
}