package com.god.kotlin.data.entity

data class Deal(
    val trddate: String,//成交日期
    var matchtime: String,//成交时间
    var matchprice: Double,//成交价格
    var matchqty: Int,//成交数量
    var stkname: String,
    var stkcode: String,
    var bsFlag: String,
    var matchtype:Int
)
