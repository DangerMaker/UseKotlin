package com.god.kotlin.query

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.god.kotlin.BaseActivity
import com.god.kotlin.R
import com.god.kotlin.data.entity.TransferRecord
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_query_trade)

        _type = intent?.getIntExtra(QUERY_TYPE, 0) ?: 0

        getPickDate()

        toolbar_back.setOnClickListener { finish() }
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
                    showBusyDialog()
                    viewModel.queryDeal("fundid", 100, 1)
                }
                addFragment(TAG0, R.id.container, true) {
                    QueryDealFragment.newInstance()
                }
            }

            1 -> {
                toolbar_title.text = "当日委托"
                date_layout.visibility = View.GONE
                invoke = {
                    showBusyDialog()
                    viewModel.queryOrder("fundid", 100, 1) }
                addFragment(TAG1, R.id.container, true) {
                    QueryOrderFragment.newInstance()
                }
            }

            2 -> {
                toolbar_title.text = "历史成交"
                invoke = {
                    showBusyDialog()
                    viewModel.queryDeal("fundid", 100, 1, startValue, endValue)
                }
                addFragment(TAG2, R.id.container, true) {
                    QueryDealFragment.newInstance()
                }
            }

            3 -> {
                toolbar_title.text = "历史委托"
                invoke = {
                    showBusyDialog()
                    viewModel.queryOrder("fundid", 100, 1, startValue, endValue) }
                addFragment(TAG3, R.id.container, true) {
                    QueryOrderFragment.newInstance()
                }
            }
        }

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

private const val TAG0 = "QueryTradeActivity_0"
private const val TAG1 = "QueryTradeActivity_1"
private const val TAG2 = "QueryTradeActivity_2"
private const val TAG3 = "QueryTradeActivity_3"


