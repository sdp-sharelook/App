package com.github.sdpsharelook.section

import android.content.Context
import android.net.wifi.WifiManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.github.sdpsharelook.R
import com.github.sdpsharelook.Word

class WordAdapter(context: Context, wordList: List<Word?>) :
    ArrayAdapter<Word>(context, R.layout.wordlist_section, wordList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val word = getItem(position)
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.wordlist_section, parent, false)

        val sourceWord = view.findViewById<TextView>(R.id.sourceWord)
        val translatedWord = view.findViewById<TextView>(R.id.translatedWord)

        if (word != null) {
            Log.e("Source word", word.source!!)
        }
        if (word != null) {
            Log.e("Source word", word.source!!)
        }
        sourceWord?.text = word?.source
        translatedWord?.text = word?.target
        return view
    }
}