package com.god.kotlin.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.telephony.TelephonyManager
import androidx.core.app.ActivityCompat

fun getInfo(context: Context, phoneNum: String): String {
    val imei = getIMEI(context)
    val systemVersion = getSystemVersion()
    val packageName = packageName(context)
    val systemModel = getSystemModel()
    return "$phoneNum|ZNZ_ANDROID|$imei|$systemVersion|$packageName|$systemModel"
}

/**
 * 获取当前手机系统版本号
 *
 * @return 系统版本号
 */
fun getSystemVersion(): String {
    return android.os.Build.VERSION.RELEASE
}

/**
 * 获取手机型号
 *
 * @return 手机型号
 */
fun getSystemModel(): String {
    return android.os.Build.MODEL
}

/**
 * 获取手机厂商
 *
 * @return 手机厂商
 */
fun getDeviceBrand(): String {
    return android.os.Build.BRAND
}

/**
 * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
 *
 * @return 手机IMEI
 */
fun getIMEI(ctx: Context): String {
    var imei = ""
    val tm = ctx.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    if (tm != null) {
        if (ActivityCompat.checkSelfPermission(
                ctx,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return imei
        } else {
            imei = tm.deviceId
        }
    }
    return imei
}


fun packageName(context: Context): String? {
    val manager = context.packageManager
    var name: String? = null
    try {
        val info = manager.getPackageInfo(context.packageName, 0)
        name = info.versionName
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }

    return name
}

