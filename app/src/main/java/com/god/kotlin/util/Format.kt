package com.god.kotlin.util

import java.text.DecimalFormat

fun Double.format2(): String {
    val format = DecimalFormat("0.00")
    return format.format(this)
}

fun Int.save100(): String {
    return (this / 100 * 100).toString()
}