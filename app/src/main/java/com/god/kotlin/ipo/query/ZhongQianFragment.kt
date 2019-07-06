package com.god.kotlin.ipo.query

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.god.kotlin.R
import com.god.kotlin.data.entity.Deal
import com.god.kotlin.data.entity.PeiHao
import com.god.kotlin.data.entity.ZhongQian
import com.god.kotlin.util.inflate
import kotlinx.android.synthetic.main.fragment_order.*

class ZhongQianFragment : Fragment() {

    private lateinit var queryAdapter: ZhongQianAdapter
    private var list: MutableList<ZhongQian> = mutableListOf()
    private lateinit var viewModel: IpoQueryViewModel

    companion object {
        fun newInstance(): ZhongQianFragment {
            return ZhongQianFragment()
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
            addHeaderView(context.inflate(R.layout.trade_holder_zq_title))
            queryAdapter = ZhongQianAdapter(list, context!!)
            adapter = queryAdapter
        }

        viewModel = (activity as QueryIpoActivity).obtainViewModel()

        viewModel.zhongQian.observe(this, Observer {
            list.clear()
            list.addAll(it)
            queryAdapter.notifyDataSetChanged()
        })

    }
}