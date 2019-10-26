package com.god.kotlin.data.entity

data class Funds(
    val fundavl: Double, //资金余额
    val fundbal: Double, //资金可用金额
    val marketvalue: Double, //资产总值
    val stkvalue: Double, //市值
    val fundfrz: Double
) //冻结金额