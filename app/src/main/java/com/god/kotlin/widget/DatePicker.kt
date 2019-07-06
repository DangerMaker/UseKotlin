package com.god.kotlin.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class DatePicker @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    TextView(context, attrs, defStyleAttr) {

    private val mContext: Context = context
    private val calendar = Calendar.getInstance()
    private val viewFormat = SimpleDateFormat("yyyy-MM-dd")
    private val netFormat = SimpleDateFormat("yyyyMMdd")
    private var year = 2019
    private var month = 5
    private var day = 12
    private var dataView = "2019-05-12"
    private var dataNet = "20190512"

    init {
        val date = Date()
        format2Pick(date)
        format2View(date)
        format2Net(date)
        setOnClickListener {
            val fragment = DatePickerFragment.newInstance(year, month, day)
            fragment.setCallback { year, month, day ->
                toLocal(year, month, day)
            }
            fragment.show((mContext as AppCompatActivity).supportFragmentManager, "DatePicker")
        }
    }

    fun getNetData(): String {
        return dataNet
    }

    private fun toLocal(_year: Int, _month: Int, _day: Int) {
        year = _year
        month = _month
        day = _day
        calendar.clear()
        calendar.set(year, month, day)
        dataView = viewFormat.format(calendar.time)
        text = dataView
        dataNet = netFormat.format(calendar.time)
    }

    private fun format2Pick(date: Date) {
        calendar.clear()
        calendar.time = date
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        day = calendar.get(Calendar.DAY_OF_MONTH)
    }

    private fun format2View(date: Date) {
        dataView = viewFormat.format(date)
        text = dataView
    }

    private fun format2Net(date: Date) {
        dataNet = netFormat.format(date)
    }
}
