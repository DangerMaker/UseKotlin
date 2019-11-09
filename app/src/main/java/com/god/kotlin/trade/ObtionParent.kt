package com.god.kotlin.trade

import androidx.lifecycle.LiveData
import com.god.kotlin.data.entity.TradeHandEntity

interface TradeParent {
    fun getHand():LiveData<MutableList<TradeHandEntity>>
    fun triggerHand()
}