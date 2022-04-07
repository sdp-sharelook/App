package com.github.sdpsharelook.section

import androidx.recyclerview.widget.RecyclerView
import com.github.sdpsharelook.databinding.CardSectionBinding


class CardViewHolder(
    private val cardCellBinding: CardSectionBinding,
    private val clickListener: SectionClickListener
): RecyclerView.ViewHolder(cardCellBinding.root) {

    var onDeletClick: ((RecyclerView.ViewHolder) -> Unit)? = null
    var onEditClick: ((RecyclerView.ViewHolder) -> Unit)? = null

    init {
//        cardCellBinding.editButton.setOnClickListener {
//            onDeletClick?.let { onDeletClick -> onDeletClick(this) }
//        }

//        cardCellBinding.sectionTitle.setOnClickListener {
//
//        }
        cardCellBinding.deleteButton.setOnClickListener{
            onAnyClick(onDeletClick)
        }

        cardCellBinding.editButton.setOnClickListener {
            onAnyClick(onEditClick)
        }
    }

    private fun onAnyClick(onClickParam: ((RecyclerView.ViewHolder) -> Unit)?) {
        onClickParam?.let { f -> f(this) }
    }

    fun bindBook(section: Section){
        cardCellBinding.sectionTitle.text = section.title
        cardCellBinding.sectionFlag.setImageResource(section.flag)

        cardCellBinding.cardView.setOnClickListener{
            clickListener.onClick(section)
        }
    }
}