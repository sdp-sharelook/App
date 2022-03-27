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
import java.util.*

class LanguageAdapter(
    private val ctx: Context,
    private val languages: Set<Language>,
    private val sr: SpeechRecognizer? = null,
    private val tts: TextToSpeech? = null
) : BaseAdapter() {

    private val sortedLanguages = languages.sortedBy { it.displayName }.toList()

    override fun getCount(): Int = languages.size

    override fun getItem(i: Int): Any = sortedLanguages[i]

    override fun getItemId(i: Int): Long = sortedLanguages[i].hashCode().toLong()

    override fun getView(i: Int, convertView: View?, parent: ViewGroup?): View =
        LayoutInflater.from(ctx).inflate(R.layout.language_row, parent, false).apply {
            val language = sortedLanguages[i]
            val flagId = language.flagId(ctx)
            if (flagId != 0)
                findViewById<ImageView>(R.id.image_view_flag).setImageResource(flagId)

            sr?.let {
                // fixme this doesn't looks to work
                val view = this
                GlobalScope.launch {
                    if (language.isAvailableForSR(it))
                        view.findViewById<ImageView>(R.id.image_view_available_sr).visibility =
                            View.VISIBLE
                }
            }

            tts?.let {
                if (language.isAvailableForTTS(it))
                    findViewById<ImageView>(R.id.image_view_available_tts).visibility = View.VISIBLE
            }
            findViewById<TextView>(R.id.text_view_display_name).text = language.displayName
            findViewById<TextView>(R.id.text_view_tag_name).text = language.languageTag
            if (language.isAvailableForTranslator)
                findViewById<ImageView>(R.id.image_view_available_translator).visibility=View.VISIBLE
        }
}