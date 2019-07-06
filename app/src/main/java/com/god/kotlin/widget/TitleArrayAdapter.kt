package com.god.kotlin.widget

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.god.kotlin.R
import com.god.kotlin.util.inflate

class TitleArrayAdapter(private val _context: Context, private val resId: Int, data: Array<String>) :
    ArrayAdapter<String>(_context, resId, data) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val item = getItem(position)
        val rowView = convertView ?: _context.inflate(resId)
        rowView.findViewById<TextView>(R.id.title).text = item
        return rowView
    }

}