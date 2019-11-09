package com.god.kotlin.util

import java.text.DecimalFormat

fun Double.format2(): String {
    val format = DecimalFormat("0.00")
    return format.format(this)
}

fun Double.format3(): String {
    val format = DecimalFormat("0.000")
    return format.format(this)
}

fun Int.save100(): String {
    return (this / 100 * 100).toString()
}

fun Int.saveInt100(): Int {
    return this / 100 * 100
}

fun String?.toIntOrZero(): Int {
    return try {
        this?.toInt() ?: 0
    } catch (e: Exception) {
        0
    }
}

fun String?.toDoubleOrZero(): Double {
    return try {
        this?.toDouble() ?: 0.0
    } catch (e: Exception) {
        0.0
    }
}