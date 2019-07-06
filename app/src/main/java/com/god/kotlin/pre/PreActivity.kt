package com.god.kotlin.pre

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.god.kotlin.R
import com.god.kotlin.util.obtainViewModel
import com.god.kotlin.util.startActivityExt
import com.god.kotlin.util.toast
import kotlinx.android.synthetic.main.activity_information_change.*
import kotlinx.android.synthetic.main.activity_pre.*
import kotlinx.android.synthetic.main.toolbar_normal.*

class PreActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var _adapter: PreAdapter
    private val list = mutableListOf<PreData>()
    val context: Context = this

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.pre_submit ->
                "submit".toast(this)
            R.id.pre_copy ->
                "submit".toast(this)
            R.id.pre_new_item -> {
                val intent = Intent(context, AddPreActivity::class.java)
                startActivityForResult(intent, 1)
            }
            R.id.pre_alter ->
                "submit".toast(this)
            R.id.pre_delete ->
                "submit".toast(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre)

        toolbar_title.text = "预埋单"
        toolbar_back.setOnClickListener { finish() }
        pre_submit.setOnClickListener(this)
        pre_copy.setOnClickListener(this)
        pre_new_item.setOnClickListener(this)
        pre_alter.setOnClickListener(this)
        pre_delete.setOnClickListener(this)

        with(pre_list_view) {
            _adapter = PreAdapter(list, context, this)
            adapter = _adapter
            choiceMode = ListView.CHOICE_MODE_MULTIPLE
            setOnItemClickListener { _, _, _, _ ->
                _adapter.notifyDataSetChanged()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == 2) {
            data?.let {
                val entity = data.getParcelableExtra<PreData>("entity")
                if (data.getIntExtra("position", -1) == -1) {
                    list.add(entity)
                } else {
                    val pos = data.getIntExtra("position", -1)
                    list.removeAt(pos)
                    list.add(pos, entity)
                }
                _adapter.notifyDataSetChanged()
            }
        }
    }
}