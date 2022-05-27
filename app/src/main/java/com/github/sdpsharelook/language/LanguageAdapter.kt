package com.github.sdpsharelook.language

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.github.sdpsharelook.R
import com.github.sdpsharelook.databinding.AdapterLanguageBinding

class LanguageAdapter(
    private val ctx: Context,
    private val languages: List<Language>
) : BaseAdapter() {


    override fun getCount(): Int = languages.size

    override fun getItem(i: Int): Any = languages[i]

    override fun getItemId(i: Int): Long = languages[i].hashCode().toLong()

    override fun getView(i: Int, convertView: View?, parent: ViewGroup?): View =
        convertView ?: AdapterLanguageBinding.inflate(LayoutInflater.from(ctx))
            .apply {
                val language = languages[i]
                language.flagId(ctx)
                    ?.let { imageViewFlag.setImageResource(it) }
                textViewDisplayName.text = language.displayName
                textViewTagName.text = language.tag
            }.root
}