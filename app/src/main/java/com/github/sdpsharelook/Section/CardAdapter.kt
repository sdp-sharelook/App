package com.github.sdpsharelook.Section

import android.app.Dialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.sdpsharelook.databinding.CardSectionBinding
import com.github.sdpsharelook.edit
import com.github.sdpsharelook.editPosition

class CardAdapter(
    private val sections: List<Section>,
    private val clickListener: SectionClickListener,
    private val dialog: Dialog
)
    : RecyclerView.Adapter<CardViewHolder>()

{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = CardSectionBinding.inflate(from, parent, false)
        return CardViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.onDeletClick = {
            removeItem(it, position)
        }

        holder.onEditClick = {
            editPosition = it.adapterPosition
            edit = true
            dialog.show()
        }

        holder.bindBook(sections[position])
    }

    fun editItem(section: Section) {
        edit = false
        sectionList.set(editPosition, section)
        notifyItemChanged(editPosition)
//        sectionList.set(viewHolder.adapterPosition, section)
//        notifyItemChanged(viewHolder.adapterPosition)
    }

    fun removeItem(viewHolder: RecyclerView.ViewHolder, index: Int) {
        sectionList.removeAt(index)
        notifyItemRemoved(viewHolder.adapterPosition)
    }

    override fun getItemCount(): Int = sections.size
}