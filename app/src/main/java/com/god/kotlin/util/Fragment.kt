package com.god.kotlin.util

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.god.kotlin.widget.tablayout.EasyFragment

fun AppCompatActivity.addFragment(
    tag: String, layoutId: Int, allowStateLoss: Boolean = false,
    newInstance: () -> Fragment
) {
    val fragment = supportFragmentManager.findFragmentByTag(tag) ?: newInstance()
    val transaction = supportFragmentManager.beginTransaction()
        .replace(layoutId, fragment, tag)
    if (allowStateLoss) {
        transaction.commitAllowingStateLoss()
    } else {
        transaction.commit()
    }
}

fun AppCompatActivity.getEasyFragment(
    tag: String, title: String,newInstance: () -> Fragment
): EasyFragment {
    //todo always newInstance()
    return EasyFragment(supportFragmentManager.findFragmentByTag(tag) ?: newInstance(),title)
}