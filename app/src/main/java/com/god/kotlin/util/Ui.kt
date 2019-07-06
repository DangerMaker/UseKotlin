package com.god.kotlin.util

import android.content.Context
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog

val Float.dp: Float                 // [xxhdpi](360 -> 1080)
    get() = android.util.TypedValue.applyDimension(
        android.util.TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics
    )

val Int.dp: Int
    get() = android.util.TypedValue.applyDimension(
        android.util.TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics
    ).toInt()


val Float.sp: Float                 // [xxhdpi](360 -> 1080)
    get() = android.util.TypedValue.applyDimension(
        android.util.TypedValue.COMPLEX_UNIT_SP, this, Resources.getSystem().displayMetrics
    )


val Int.sp: Int
    get() = android.util.TypedValue.applyDimension(
        android.util.TypedValue.COMPLEX_UNIT_SP, this.toFloat(), Resources.getSystem().displayMetrics
    ).toInt()

fun ViewGroup.inflate(@LayoutRes resource: Int, attachToRoot: Boolean = false): View =
    LayoutInflater.from(context).inflate(resource, this, attachToRoot)

fun Context.inflate(@LayoutRes resource: Int): View =
    LayoutInflater.from(this).inflate(resource, null)

fun Context.inflate(@LayoutRes resource: Int, viewGroup: ViewGroup): View =
    LayoutInflater.from(this).inflate(resource, viewGroup)

fun String.toast(context: Context?, duration: Int = Toast.LENGTH_SHORT) {
    context?.let {
        Toast.makeText(context, this, duration).apply { show() }
    }
}

fun showSimpleDialog(context: Context?, message: String) {
    context?.let {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("提示")
        builder.setMessage(message)
        builder.setCancelable(false)
        builder.setPositiveButton("确定") { dialog, _ -> dialog.dismiss() }
        builder.create().show()
    }
}

fun showTwoButtonDialog(
    context: Context?,
    title: String,
    negative: String,
    message: String,
    listener: DialogInterface.OnClickListener
) {
    context?.let {
        val builder = AlertDialog.Builder(
            context
        )
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setCancelable(false)
        builder.setPositiveButton("取消") { dialog, _ -> dialog.dismiss() }
        builder.setNegativeButton(negative) { dialog, which ->
            listener.onClick(dialog, which)
            dialog.dismiss()
        }
        builder.create().show()
    }
}

fun showSelectDialog(context: Context?, items: Array<String>, listener: DialogInterface.OnClickListener) {
    context?.let {
        val builder = AlertDialog.Builder(it)
        builder.setItems(
            items
        ) { dialog, which -> listener.onClick(dialog, which) }
        builder.setNegativeButton(
            "取消"
        ) { dialog, _ -> dialog.dismiss() }
        builder.create().show()
    }
}