package com.github.sdpsharelook.section

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.github.sdpsharelook.R
import com.github.sdpsharelook.databinding.AdapterLanguageBinding
import com.github.sdpsharelook.databinding.CountrySpinnerRowBinding
import com.github.sdpsharelook.language.Language

class SpinnerAdapter(
    private val ctx: Context,
    private val languages: List<Language>
) : BaseAdapter()
{
    override fun getCount(): Int = languages.size

    override fun getItem(i: Int): Language = languages[i]

    fun getItemFlag(i:Int): Int = languages[i].flagId(ctx)!!

    override fun getItemId(i: Int): Long = languages[i].hashCode().toLong()

    override fun getView(i: Int, convertView: View?, parent: ViewGroup?): View =
        convertView ?: CountrySpinnerRowBinding.inflate(LayoutInflater.from(ctx))
            .apply {
                val language = languages[i]
                language.flagId(ctx)?.let { imageViewFlag.setImageResource(it) }
                textViewTagName.text = language.tag
            }.root
}