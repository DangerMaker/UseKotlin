package com.god.kotlin.trade.order

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.god.kotlin.R
import com.god.kotlin.data.entity.Order
import com.god.kotlin.data.entity.TradeHandEntity
import com.god.kotlin.trade.HandAdapter
import com.god.kotlin.trade.TradeView
import com.god.kotlin.util.*
import kotlinx.android.synthetic.main.fragment_order.*

class OrderFragment : Fragment() {

    private lateinit var orderAdapter: OrderAdapter
    private var list: MutableList<Order> = mutableListOf()
    private lateinit var viewModel: OrderViewModel

    companion object {
        fun newInstance(): OrderFragment {
            return OrderFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_order, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        with(order_recycler) {
            order_recycler.addHeaderView(context.inflate(R.layout.trade_holder_entrust_title))
            orderAdapter = OrderAdapter(list, context!!)
            order_recycler.adapter = orderAdapter
            setOnItemClickListener { _, _, position, _ ->
                Log.e("onActivityCreated",position.toString())
                val order = list[position - 1]
                showTwoButtonDialog(context, "撤单", "确定",
                    "操作类型：" + "撤单" + "\n" +
                            "买卖方向：" + getBSStringByTag(order.bsflag) + "\n" +
                            "证券代码：" + order.stkcode + " " + order.stkname + "\n" +
                            "合同编号：" + order.ordersno
                    , DialogInterface.OnClickListener { _, _ ->
                        viewModel.post(order.orderdate, order.fundid, order.ordersno, order.bsflag)
                    }
                )
            }
        }

        viewModel = obtainViewModel(OrderViewModel::class.java)
        viewModel.orderList.observe(this, Observer {
            list.clear()
            list.addAll(it)
            orderAdapter.notifyDataSetChanged()
        })

        viewModel.result.observe(this, Observer {
            showSimpleDialog(context, it)
        })

        viewModel.query(20, 0)
    }
}