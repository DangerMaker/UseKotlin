package com.god.kotlin.pre

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PreData(
    val market: String,
    val code: String,
    val name: String,
    val bsflag: String,
    val price: Double,
    val qty: Int,
    val dict: String,
    val date: String
): Parcelable