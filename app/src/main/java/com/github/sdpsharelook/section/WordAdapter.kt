package com.github.sdpsharelook.section

import android.content.Context
import android.net.wifi.WifiManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import coil.load
import com.github.sdpsharelook.R
import com.github.sdpsharelook.Word
import kotlinx.coroutines.NonDisposableHandle.parent
import okhttp3.internal.notify

class WordAdapter(
    private val cxt: Context,
    private val wordList: List<Word?>) :

    ArrayAdapter<Word>(cxt, R.layout.wordlist_section, wordList) {

//    val view = convertView ?: LayoutInflater.from(cxt)
//        .inflate(R.layout.wordlist_section, parent, false)

//      val sourceWord =
//      val translatedWord =

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        Log.e("getView called", "IND")
        val word = getItem(position)
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.wordlist_section, parent, false)

        val sourceWord = view.findViewById<TextView>(R.id.sourceWord)
        val translatedWord = view.findViewById<TextView>(R.id.translatedWord)
        val imageView3 = view.findViewById<ImageView>(R.id.imageView3)

        if (word?.picture != null) {
            imageView3.load(word.picture)
        }
 //       imageView3.isShown
        sourceWord?.text = word?.source
        translatedWord?.text = word?.target
        return view
    }



}


