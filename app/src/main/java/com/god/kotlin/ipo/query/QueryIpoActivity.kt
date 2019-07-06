package com.god.kotlin.ipo.query

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.god.kotlin.R
import com.god.kotlin.data.entity.TransferRecord
import com.god.kotlin.query.QueryDealFragment
import com.god.kotlin.query.QueryOrderFragment
import com.god.kotlin.query.QueryViewModel
import com.god.kotlin.util.Constant.Companion.QUERY_TYPE
import com.god.kotlin.util.addFragment
import com.god.kotlin.util.obtainViewModel
import kotlinx.android.synthetic.main.activity_query_trade.*
import kotlinx.android.synthetic.main.toolbar_normal.*

class QueryIpoActivity : AppCompatActivity() {

    val list = mutableListOf<TransferRecord>()
    private var _type = 0 //"配号", "中签", "代缴""
    private lateinit var viewModel: IpoQueryViewModel
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
                toolbar_title.text = "配号查询"
                date_layout.visibility = View.VISIBLE
                invoke = { viewModel.queryPeiHao("fundid", 0, 100, startValue, endValue) }
                addFragment(TAG0, R.id.container, true) {
                    PeiHaoFragment.newInstance()
                }
            }

            1 -> {
                toolbar_title.text = "中签查询"
                date_layout.visibility = View.VISIBLE
                invoke = { viewModel.queryZhongQian("fundid", 0, 100,startValue,endValue) }
                addFragment(TAG1, R.id.container, true) {
                    ZhongQianFragment.newInstance()
                }
            }

            2 -> {
                toolbar_title.text = "代缴款查询"
                date_layout.visibility = View.GONE
                invoke = { viewModel.queryDaiJiao() }
                addFragment(TAG2, R.id.container, true) {
                    DaiJiaoFragment.newInstance()
                }
            }
        }

    }

    fun obtainViewModel() = obtainViewModel(IpoQueryViewModel::class.java)

    override fun onResume() {
        super.onResume()
        invoke()

    }

    private fun getPickDate() {
        startValue = date_begin.getNetData()
        endValue = date_end.getNetData()

    }
}

private const val TAG0 = "QueryIpoActivity_0"
private const val TAG1 = "QueryIpoActivity_1"
private const val TAG2 = "QueryIpoActivity_2"


