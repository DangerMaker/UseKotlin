package com.god.kotlin.util

import android.os.Handler

fun async(func: () -> Unit) {
    Handler().postDelayed(func, SERVICE_LATENCY_IN_MILLIS)
}

private const val SERVICE_LATENCY_IN_MILLIS = 3000L