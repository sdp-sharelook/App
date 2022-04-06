package com.github.sdpsharelook.Section

import android.app.Dialog
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.sdpsharelook.databinding.CardSectionBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CardAdapter(
    private val sections: List<Section>,
    private val clickListener: SectionClickListener,
    private val dialog: Dialog
)
    : RecyclerView.Adapter<CardViewHolder>()

{
    private var editPosition = 0
    private lateinit var binding: CardSectionBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val from = LayoutInflater.from(parent.context)
        binding = CardSectionBinding.inflate(from, parent, false)
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

    fun editItem(name: String, flag: Int) {
        val section = sectionList[editPosition]
        section.title = name
        section.flag = flag
        edit = false
        notifyItemChanged(editPosition)
    }

    fun removeItem(viewHolder: RecyclerView.ViewHolder, index: Int) {
        val section = sectionList[index]
        Log.d("INDEX", index.toString())
        sectionList.removeAt(index)
        CoroutineScope(Dispatchers.IO).launch{
            section.databaseRepo.delete(section.sectionRepo)
        }
        notifyItemRemoved(viewHolder.adapterPosition)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = sections.size
}