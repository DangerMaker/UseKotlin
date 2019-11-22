package com.god.kotlin.query

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.god.kotlin.BaseActivity
import com.god.kotlin.R
import com.god.kotlin.util.Constant.Companion.QUERY_TYPE
import com.god.kotlin.util.addFragment
import com.god.kotlin.util.obtainViewModel
import kotlinx.android.synthetic.main.activity_query_trade.*
import kotlinx.android.synthetic.main.toolbar_normal.*

class QueryTradeActivity : BaseActivity() {

    private var _type = 0 //"当日成交", "当日委托", "历史成交", "历史委托"
    private lateinit var viewModel: QueryViewModel
    private lateinit var invoke: () -> Unit
    private lateinit var startValue: String
    private lateinit var endValue: String
    private lateinit var fragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_query_trade)

        _type = intent?.getIntExtra(QUERY_TYPE, 0) ?: 0

        getPickDate()

        toolbar_back.setOnClickListener {
            finish()
        }
        date_submit.setOnClickListener {
            getPickDate()
            invoke()
        }

        viewModel = obtainViewModel()

        when (_type) {
            0 -> {
                toolbar_title.text = "当日成交"
                date_layout.visibility = View.GONE
                invoke = {
                    viewModel.queryDeal("fundid", 100, 1)
                }
                fragment = QueryDealFragment.newInstance()
            }

            1 -> {
                toolbar_title.text = "当日委托"
                date_layout.visibility = View.GONE
                invoke = {
                    viewModel.queryOrder("fundid", 100, 1)
                }
                fragment = QueryOrderFragment.newInstance()
            }

            2 -> {
                toolbar_title.text = "历史成交"
                invoke = {
                    viewModel.queryDeal("fundid", 100, 1, startValue, endValue)
                }
                fragment = QueryDealFragment.newInstance()
            }

            3 -> {
                toolbar_title.text = "历史委托"
                invoke = {
                    viewModel.queryOrder("fundid", 100, 1, startValue, endValue)
                }
                fragment = QueryOrderFragment.newInstance()
            }
        }

        addFragment("tag", R.id.container, true) {
            fragment
        }

        viewModel.dealList.observe(this, Observer {
            val iterator = it.iterator()
            while (iterator.hasNext()) {
                val x = iterator.next()
                if (x.matchtype != 0) {
                    iterator.remove()
                }
            }

            it.reverse()
            (fragment as DealListView).show(it)
        })

        viewModel.orderList.observe(this, Observer {
            it.reverse()
            (fragment as OrderListView).show(it)
        })

        viewModel.loading.observe(this, Observer {
            if (it) {
                showBusyDialog()
            } else {
                dismissBusyDialog()
            }
        })

    }

    fun obtainViewModel() = obtainViewModel(QueryViewModel::class.java)

    override fun onResume() {
        super.onResume()
        invoke()

    }

    private fun getPickDate() {
        startValue = date_begin.getNetData()
        endValue = date_end.getNetData()

    }
}