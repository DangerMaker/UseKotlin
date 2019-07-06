package com.god.kotlin.trade

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import com.god.kotlin.R
import com.god.kotlin.util.hideSoftBoard
import com.god.kotlin.util.showSoftBoard

class InputCodeView : RelativeLayout {

    private var editText: EditText
    private var textView: TextView

    var listener: (String) -> Unit = {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        View.inflate(context, R.layout.trade_view_input_code, this)
        editText = findViewById(R.id.input)
        textView = findViewById(R.id.text)
        textView.setOnClickListener {
            textView.visibility = View.GONE
            editText.visibility = View.VISIBLE
            editText.requestFocus()
            editText.showSoftBoard(getContext())
        }
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                if (editText.text.length == 6) {
                    listener(editText.text.toString())
                }
            }
        })
    }

    fun setData(code: String, name: String) {
        textView.visibility = View.VISIBLE
        editText.visibility = View.GONE
        textView.text = "$code  $name"
        editText.setText("")
        editText.hideSoftBoard(context)
    }

    fun setOnSearchListener(listener: (String) -> Unit) {
        this.listener = listener
    }
}
