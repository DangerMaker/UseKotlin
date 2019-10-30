package com.god.kotlin.ipo

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.ez08.trade.tools.DialogUtils
import com.god.kotlin.BaseActivity
import com.god.kotlin.R
import com.god.kotlin.data.entity.NewStock
import com.god.kotlin.user.UserHelper
import com.god.kotlin.util.obtainViewModel
import com.god.kotlin.util.startActivityExt
import kotlinx.android.synthetic.main.activity_ipo_order.*


class IpoOrderActivity : BaseActivity() {

    private lateinit var viewModel: IpoViewModel
    private lateinit var newStockAdapter: NewStockAdapter
    private val list = mutableListOf<NewStock>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ipo_order)
        ipo_img_back.setOnClickListener { finish() }
        ipo_sub_title.setOnClickListener {
            startActivityExt(IpoMenuActivity::class.java)
        }

        viewModel = obtainViewModel(IpoViewModel::class.java)
        viewModel.tips.observe(this, Observer {
            tips.text = it
            tips.visibility = View.VISIBLE
        })

        with(ipo_list) {
            choiceMode = ListView.CHOICE_MODE_MULTIPLE
            newStockAdapter = NewStockAdapter(list, context, this)
            adapter = newStockAdapter
            setOnItemClickListener { _, _, _, _ ->
                newStockAdapter.notifyDataSetChanged()
            }
        }

        viewModel.newStockList.observe(this, Observer {
            list.clear()
            list.addAll(it)
            newStockAdapter.notifyDataSetChanged()
        })

        viewModel.queryStockList()

        val user = UserHelper.getUser()
        viewModel.queryQuota(user.secuid)

        ipo_submit.setOnClickListener {
            viewModel.transaction(user.secuid,user.fundid,list)
        }

        ipo_checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) selectAll() else unSelectAll()
        }

        viewModel.result.observe(this, Observer {
            DialogUtils.showSimpleDialog(this,it)
        })
    }

    private fun selectAll() {
        for (i in 0 until newStockAdapter.count) {
            ipo_list.setItemChecked(i, true)
        }
    }

    private fun unSelectAll() {
        ipo_list.clearChoices()
        newStockAdapter.notifyDataSetChanged()
    }
}