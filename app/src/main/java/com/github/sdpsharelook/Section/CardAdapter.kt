package com.github.sdpsharelook.Section

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.sdpsharelook.databinding.CardSectionBinding

class CardAdapter(
    private val sections: List<Section>,
    private val clickListener: SectionClickListener
)
    : RecyclerView.Adapter<CardViewHolder>()

{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = CardSectionBinding.inflate(from, parent, false)
        return CardViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bindBook(sections[position])
    }

    override fun getItemCount(): Int = sections.size
}