package com.github.sdpsharelook.section

import android.app.Dialog
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.sdpsharelook.Word
import com.github.sdpsharelook.databinding.CardSectionBinding
import com.github.sdpsharelook.storage.IRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CardAdapter constructor(
    private val clickListener: SectionClickListener,
    private val dialog: Dialog,
    private val wordRTDB : IRepository<List<Word>>
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

        holder.bindBook(sectionList[position])
    }

    fun editItem(name: String, flag: Int) {
        val oldSection = sectionList[editPosition]
        val newSection = Section(name,  flag, oldSection.id)

        CoroutineScope(Dispatchers.IO).launch{
            wordRTDB.deleteSection(oldSection)
            wordRTDB.insertSection(newSection)
        }

        edit = false
    }

    private fun removeItem(viewHolder: RecyclerView.ViewHolder, index: Int) {
        val section = sectionList[index]
        CoroutineScope(Dispatchers.IO).launch{
            wordRTDB.deleteSection(section)
        }
    }

    override fun getItemCount(): Int = sectionList.size
}