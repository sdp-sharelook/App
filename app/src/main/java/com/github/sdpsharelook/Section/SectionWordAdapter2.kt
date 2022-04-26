package com.github.sdpsharelook.Section

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.github.sdpsharelook.R

class SectionWordAdapter2(private val context: Context, private val sectionWordList: List<SectionWord>
) : BaseAdapter() {
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


    override fun getCount(): Int {
        return sectionWordList.size
    }

    override fun getItem(position: Int) : Any {
        return sectionWordList[position]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup) : View {
//        val rowView = inflater.inflate(R.layout.wordlist_section, parent, false)
//
//        val sourceWord = rowView.findViewById<TextView>(R.id.sourceWord)
//        val translatedWord = rowView.findViewById<TextView>(R.id.translatedWord)
//        val image = rowView.findViewById<ImageView>(R.id.imageView3)

        val view: View
        val holder: ViewHolder

        if (convertView == null) {
             view = inflater.inflate(R.layout.wordlist_section, parent, false)

            holder = ViewHolder()
            holder.sourceWordView = view.findViewById(R.id.sourceWord)
            holder.translatedWordView = view.findViewById(R.id.translatedWord)
            holder.imageView = view.findViewById(R.id.imageView3)

            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as ViewHolder
        }

        val sourceWord = holder.sourceWordView
        val translatedWord = holder.translatedWordView
        val image = holder.imageView

        val sw : SectionWord = getItem(position) as SectionWord
        sourceWord.text = sw.sourceText
        translatedWord.text = sw.translatedText

        //TODO: Link this imageView with the database / add images to the database
        image.setImageResource(R.drawable.default_user_path)
        return view
    }

    private class ViewHolder {
        lateinit var sourceWordView: TextView
        lateinit var translatedWordView: TextView
        lateinit var imageView: ImageView
    }
}