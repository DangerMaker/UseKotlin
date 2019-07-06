package com.god.kotlin.query

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.god.kotlin.R
import com.god.kotlin.data.entity.Order
import com.god.kotlin.trade.order.OrderAdapter
import com.god.kotlin.util.inflate
import kotlinx.android.synthetic.main.fragment_order.*

class QueryOrderFragment : Fragment() {

    private lateinit var orderAdapter: OrderAdapter
    private var list: MutableList<Order> = mutableListOf()
    private lateinit var viewModel: QueryViewModel

    companion object {
        fun newInstance(): QueryOrderFragment {
            return QueryOrderFragment()
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
            addHeaderView(context.inflate(R.layout.trade_holder_entrust_title))
            orderAdapter = OrderAdapter(list, context!!)
            adapter = orderAdapter
        }

        viewModel = (activity as QueryTradeActivity).obtainViewModel()

        viewModel.orderList.observe(this, Observer {
            list.clear()
            list.addAll(it)
            orderAdapter.notifyDataSetChanged()
        })

    }
}