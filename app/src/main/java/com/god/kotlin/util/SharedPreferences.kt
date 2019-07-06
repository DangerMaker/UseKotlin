package com.god.kotlin.util

import android.content.Context

fun provideAccountSharedPref(context: Context?) =
    context?.applicationContext?.getSharedPreferences("login_account", Context.MODE_PRIVATE)
