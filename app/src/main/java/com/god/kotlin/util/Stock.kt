package com.god.kotlin.util

import android.widget.TextView
import androidx.core.content.ContextCompat
import com.god.kotlin.R

fun getPriceColor(price: Double, comparePrice: Double): Int {
    return if (price > comparePrice) R.color.trade_red else R.color.trade_green
}

fun TextView.setPriceColor(color: Int) {
    setTextColor(ContextCompat.getColor(this.context, color))
}