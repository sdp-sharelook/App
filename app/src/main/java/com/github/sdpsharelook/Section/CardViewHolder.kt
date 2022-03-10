package com.github.sdpsharelook.Section

import androidx.recyclerview.widget.RecyclerView
import com.github.sdpsharelook.databinding.CardSectionBinding


class CardViewHolder(
    private val cardCellBinding: CardSectionBinding,
    private val clickListener: SectionClickListener
): RecyclerView.ViewHolder(cardCellBinding.root) {
    fun bindBook(section: Section){
        cardCellBinding.sectionTitle.text = section.title
        cardCellBinding.sectionFlag.setImageResource(section.flag)

        cardCellBinding.cardView.setOnClickListener{
            clickListener.onClick(section)
        }
    }
}