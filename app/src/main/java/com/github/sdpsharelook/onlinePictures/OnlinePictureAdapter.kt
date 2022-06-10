package com.github.sdpsharelook.onlinePictures


import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.github.sdpsharelook.databinding.AdapterOnlinepictureBinding

class OnlinePictureAdapter(
    private val ctx: Context,
    private val pictures: List<OnlinePicture>,
) : BaseAdapter() {
    override fun getCount(): Int = pictures.size

    override fun getItem(i: Int): Any = pictures[i]

    override fun getItemId(i: Int): Long = pictures[i].hashCode().toLong()

    @SuppressLint("ViewHolder")
    override fun getView(i: Int, view: View?, group: ViewGroup?): View =
        view ?: AdapterOnlinepictureBinding.inflate(LayoutInflater.from(ctx)).apply {
            val picture = pictures[i]
            imageViewThumbnail.setImageBitmap(picture.thumbnail)
            textViewTitle.text = ""
        }.root
}