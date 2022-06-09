package com.github.sdpsharelook.revision

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.sdpsharelook.R

class SectionSelectAdapter(
    var sections: MutableList<SectionSelect>
) : RecyclerView.Adapter<SectionSelectAdapter.SectionSelectViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionSelectViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_section_checkbox, parent, false)
        return SectionSelectViewHolder(view)
    }

    override fun onBindViewHolder(holder: SectionSelectViewHolder, position: Int) {
        holder.itemView.apply {
            sections[position].let {
                findViewById<ImageView>(R.id.sectionFlag).setBackgroundResource(it.section.flag)
                findViewById<TextView>(R.id.sectionTitle).text = it.section.title
                findViewById<TextView>(R.id.wordsToReview).text = it.wordsToReview.toString()
                findViewById<CheckBox>(R.id.sectionCheckBox).apply {
                    isChecked = it.isChecked
                    setOnClickListener { _ ->
                        it.isChecked = isChecked
                    }
                }

            }
        }
    }


    override fun getItemCount(): Int = sections.size

    inner class SectionSelectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}