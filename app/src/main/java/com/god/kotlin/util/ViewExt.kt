package com.god.kotlin.util

import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.startActivityExt(cls: Class<*>, initializer: (Intent.() -> Unit)? = null) {
    val intent = Intent(this, cls)
    initializer?.let {
        intent.it()
    }
    startActivity(intent)
}

fun <T : AppCompatActivity> View.startActivity(cls: Class<T>, initializer: (Intent.() -> Unit)? = null) {

    with(this.context) {
        val intent = Intent(this, cls)
        initializer?.let {
            intent.it()
        }
        startActivity(intent)
    }
}