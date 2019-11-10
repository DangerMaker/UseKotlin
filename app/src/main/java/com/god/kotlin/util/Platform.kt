package com.god.kotlin.util

import android.text.TextUtils
import com.god.kotlin.pre.PreData

fun getBSStringByTag(tag: String): String {
    return if (tag == "B") {
        "买入"
    } else {
        "卖出"
    }
}

fun getTime(time: String): String {
    if (time.length == 6 || time.length == 8) {
        val hour = time.substring(0, 2)
        val min = time.substring(2, 4)
        val sec = time.substring(4, 6)
        return "$hour:$min:$sec"
    }
    return ""
}

fun getMarketByTag(tag: String): String {
    return if (tag == "0" || tag == "2" || tag == "") {
        "SZHQ"
    } else {
        "SHHQ"
    }
}

fun getTransferStatus(type: String): String {
    return if (type == "0") {
        "未报"
    } else if (type == "1") {
        "已报"
    } else if (type == "2") {
        "成功"
    } else if (type == "3") {
        "失败"
    } else if (type == "4") {
        "超时"
    } else if (type == "5") {
        "待冲正"
    } else if (type == "6") {
        "已冲正"
    } else if (type == "7") {
        "调整为成功"
    } else if (type == "8") {
        "调整为失败"
    } else {
        "未知"
    }
}

fun getOrderStatus(data: String): String {
    return when (data) {
        "0" -> "未报"
        "1" -> "正报"
        "2" -> "已报"
        "3" -> "已报待撤"
        "4" -> "部成待撤"
        "5" -> "部撤"
        "6" -> "已撤"
        "7" -> "部成"
        "8" -> "已成"
        "9" -> "废单"
        "A" -> "待报"
        "B" -> "正报"
        else -> "未知"
    }
}

fun getZhongqianStatus(type: String): String {
    return when (type) {
        "0" -> "新股中签"
        "1" -> "中签缴款"
        "2" -> "中签确认"
        else -> "未知状态"
    }
}


fun getMarketType(type: String): String {
    return when (type) {
        "0" -> "深圳A股"
        "1" -> "上海A股"
        "2" -> "深圳B股"
        "3" -> "上海B股"
        "5" -> "沪港通"
        "6" -> "三板A"
        "7" -> "三板B"
        "S" -> "深股通"
        "J" -> "开放式基金"
        else -> ""
    }
}

fun getResponseQuote(flag: String): String {
    var result = ""
    when (flag) {
        "B" -> result = "买入"
        "S" -> result = "卖出"
        "a" -> result = "对方买入"
        "f" -> result = "对方卖出"
        "b" -> result = "本方买入"
        "g" -> result = "本方卖出"
        "c" -> result = "即成买入"
        "h" -> result = "即成卖出"
        "d" -> result = "五档剩撤"
        "i" -> result = "五档剩撤"
        "e" -> result = "全额买入"
        "j" -> result = "全额卖出"
        "q" -> result = "五档限价"
        "r" -> result = "五档限价"
    }

    return result
}

//0 红色 1蓝色
fun getResponseColor(flag: String): Int {
    var result = 0
    when (flag) {
        "B" -> result = 0
        "S" -> result = 1
        "a" -> result = 0
        "f" -> result = 1
        "b" -> result = 0
        "g" -> result = 1
        "c" -> result = 0
        "h" -> result = 1
        "d" -> result = 0
        "i" -> result = 1
        "e" -> result = 0
        "j" -> result = 1
        "q" -> result = 0
        "r" -> result = 1
    }

    return result
}

fun getTagByQuoteName(bsFlag: String, quoteType: String): String {
    if (TextUtils.isEmpty(quoteType) || quoteType == "报价方式") {
        return ""
    }

    if (quoteType == "可转债转股") {
        return "0G"
    }

    if (quoteType == "债券回售") {
        return "0H"
    }

    var result = ""
    if (bsFlag == "0B" || bsFlag == "0S") {
        val type = bsFlag == "0B"
        when (quoteType) {
            "限价委托" -> result = if (type) "0B" else "0S"
            "对方最优价格" -> result = if (type) "0a" else "0f"
            "本方最优价格" -> result = if (type) "0b" else "0g"
            "即时成交剩余撤销" -> result = if (type) "0c" else "0h"
            "五档即成剩撤" -> result = if (type) "0d" else "0i"
            "全额成交或撤销" -> result = if (type) "0e" else "0j"
            "五档即成转限价" -> result = if (type) "0q" else "0r"
        }
    }
    return result
}

fun getFlagByQuoteName(bsFlag: Boolean, quoteType: String): String {
    if (TextUtils.isEmpty(quoteType)) {
        return ""
    }

    if (quoteType == "可转债转股") {
        return "0G"
    }

    if (quoteType == "债券回售") {
        return "0H"
    }

    var result = ""
    when (quoteType) {
        "限价委托" -> result = if (bsFlag) "0B" else "0S"
        "对方最优价格" -> result = if (bsFlag) "0a" else "0f"
        "本方最优价格" -> result = if (bsFlag) "0b" else "0g"
        "即时成交剩余撤销" -> result = if (bsFlag) "0c" else "0h"
        "五档即成剩撤" -> result = if (bsFlag) "0d" else "0i"
        "全额成交或撤销" -> result = if (bsFlag) "0e" else "0j"
        "五档即成转限价" -> result = if (bsFlag) "0q" else "0r"
    }
    return result
}

val szQuoteType = arrayOf("对方最优价格", "本方最优价格", "即时成交剩余撤销", "五档即成剩撤", "全额成交或撤销")
val shQuoteType = arrayOf("五档即成剩撤", "五档即成转限价")
val conversionType = arrayOf("可转债转股", "债券回售")