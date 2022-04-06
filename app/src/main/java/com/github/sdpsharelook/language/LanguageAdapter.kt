package com.github.sdpsharelook.language

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.github.sdpsharelook.R
import com.github.sdpsharelook.speechRecognition.SpeechRecognizer
import com.github.sdpsharelook.textToSpeech.TextToSpeech
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.random.Random

class LanguageAdapter(
    private val ctx: Context,
    private val languages: Set<Language>,
) : BaseAdapter() {

    private val sortedLanguages =
        when {
            Language.auto in languages ->
                listOf(Language.auto) + (languages.filterNot { it == Language.auto }
                    .sortedBy { it.displayName }.toList())
            else -> languages.sortedBy { it.displayName }.toList()
        }

    override fun getCount(): Int = languages.size

    override fun getItem(i: Int): Any = sortedLanguages[i]

    override fun getItemId(i: Int): Long = sortedLanguages[i].hashCode().toLong()

    override fun getView(i: Int, convertView: View?, parent: ViewGroup?): View =
        LayoutInflater.from(ctx).inflate(R.layout.language_row, parent, false).apply {
            val language = sortedLanguages[i]
            language.flagId(ctx)
                ?.let { findViewById<ImageView>(R.id.image_view_flag).setImageResource(it) }

            findViewById<TextView>(R.id.text_view_display_name).text = language.displayName
            findViewById<TextView>(R.id.text_view_tag_name).text = language.tag

            if (Random.nextBoolean())
                findViewById<ImageView>(R.id.image_view_available_sr).visibility =
                    View.VISIBLE

            if (Random.nextBoolean())
                findViewById<ImageView>(R.id.image_view_available_tts).visibility = View.VISIBLE

            if (Random.nextBoolean())
                findViewById<ImageView>(R.id.image_view_available_translator).visibility =
                    View.VISIBLE
        }
}