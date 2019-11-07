package com.god.kotlin.query

import com.god.kotlin.data.entity.Order

interface OrderListView {
    fun show(data: MutableList<Order>)
}