package com.god.kotlin.query

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.god.kotlin.R
import com.god.kotlin.data.entity.Deal
import com.god.kotlin.util.inflate
import kotlinx.android.synthetic.main.fragment_order.*

class QueryDealFragment : Fragment() {

    private lateinit var queryAdapter: QueryDealAdapter
    private var list: MutableList<Deal> = mutableListOf()
    private lateinit var viewModel: QueryViewModel

    companion object {
        fun newInstance(): QueryDealFragment {
            return QueryDealFragment()
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
            addHeaderView(context.inflate(R.layout.trade_holder_deal_title))
            queryAdapter = QueryDealAdapter(list, context!!)
            adapter = queryAdapter
        }

        viewModel = (activity as QueryTradeActivity).obtainViewModel()

        viewModel.dealList.observe(this, Observer {
            list.clear()
            list.addAll(it)
            queryAdapter.notifyDataSetChanged()
        })

    }
}