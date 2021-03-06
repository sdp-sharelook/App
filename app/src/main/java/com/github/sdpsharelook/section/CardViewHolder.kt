package com.github.sdpsharelook.section

import androidx.recyclerview.widget.RecyclerView
import com.github.sdpsharelook.databinding.CardSectionBinding
import java.lang.ref.WeakReference


class CardViewHolder(
    private val cardCellBinding: CardSectionBinding,
    private val clickListener: SectionClickListener
): RecyclerView.ViewHolder(cardCellBinding.root) {

    var onDeletClick: ((RecyclerView.ViewHolder) -> Unit)? = null
    var onEditClick: ((RecyclerView.ViewHolder) -> Unit)? = null

    init {

        cardCellBinding.deleteButton.setOnClickListener{
            onDeletClick?.let { onDeletClick -> onDeletClick(this) }
        }

        cardCellBinding.editButton.setOnClickListener {
            onEditClick?.let { onEditClick -> onEditClick(this) }
        }
    }

    fun bindBook(section: Section){
        cardCellBinding.sectionTitle.text = section.title
        cardCellBinding.sectionFlag.setImageResource(section.flag.toInt())

        cardCellBinding.cardView.setOnClickListener{
            clickListener.onClick(section)
        }
    }
}