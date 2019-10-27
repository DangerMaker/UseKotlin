package com.god.kotlin.data.entity

data class NewStock (
    val stkcode:String,
    val stkname:String,
    val linkstk:String,
    val minqty:Int,
    val maxqty:Int,
    val market:String,
    var isSelect:Boolean
)