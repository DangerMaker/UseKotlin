package com.god.kotlin.query

import com.god.kotlin.data.entity.Deal

interface DealListView {
    fun show(data: MutableList<Deal>)
}