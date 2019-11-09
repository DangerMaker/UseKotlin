package com.god.kotlin.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.god.kotlin.R
import com.god.kotlin.util.saveInt100
import kotlinx.android.synthetic.main.view_trade_ratio.view.*

class RatioView(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    init {
        View.inflate(context, R.layout.view_trade_ratio, this)

        full.setOnClickListener {
            listener(max.saveInt100())
        }

        half.setOnClickListener {
            listener((max / 2).saveInt100())
        }

        one_three.setOnClickListener {
            listener((max / 3).saveInt100())
        }

        one_fourth.setOnClickListener {
            listener((max / 4).saveInt100())
        }
    }

    private var max: Int = 0

    fun setMax(max: Int) {
        this.max = max
    }

    private var listener: (Int) -> Unit = {}

    fun setOnMaxListener(listener: (Int) -> Unit) {
        this.listener = listener
    }
}