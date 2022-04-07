package com.github.sdpsharelook.section

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.github.sdpsharelook.R

class CountryAdapter(context: Context, countryList: List<CountryItem>) : ArrayAdapter<CountryItem>( context, 0, countryList)
{
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View =
        getView(position, convertView, parent)

    private fun initView(position: Int, @Suppress("UNUSED_PARAMETER")convertView: View?, parent: ViewGroup ) : View {
        val currentCountry = getItem(position)
        val view = LayoutInflater.from(context).inflate(R.layout.country_spinner_row, parent, false)
        view.findViewById<ImageView>(R.id.image_view_flag).setImageResource(currentCountry!!.flag)

        return view
    }

}