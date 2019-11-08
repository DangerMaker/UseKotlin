package com.god.kotlin.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class UnScrollViewpager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs){

    override fun onTouchEvent(arg0: MotionEvent): Boolean {
        return false
    }

    override  fun onInterceptTouchEvent(arg0: MotionEvent): Boolean {
        return false
    }
}