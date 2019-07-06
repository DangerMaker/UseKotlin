package com.god.kotlin.trade.funds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.god.kotlin.R
import com.god.kotlin.data.entity.TradeHandEntity
import com.god.kotlin.trade.HandAdapter
import com.god.kotlin.trade.SellViewModel
import com.god.kotlin.trade.TradeActivity
import com.god.kotlin.trade.TradeView
import com.god.kotlin.util.inflate
import com.god.kotlin.util.obtainViewModel
import kotlinx.android.synthetic.main.fragment_sell.*

class FundsFragment : Fragment() {

    private lateinit var viewModel: SellViewModel
    private lateinit var fundsModel: FundsViewModel
    private lateinit var fundsView: FundsView
    private lateinit var handAdapter: HandAdapter
    private var list: MutableList<TradeHandEntity> = mutableListOf()

    companion object {
        fun newInstance(): FundsFragment {
            return FundsFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_sell, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        with(sell_recycler) {
            fundsView = FundsView(context)
            sell_recycler.addHeaderView(fundsView)
            sell_recycler.addHeaderView(context.inflate(R.layout.trade_holder_handing_title))
            handAdapter = HandAdapter(list, context!!)
            sell_recycler.adapter = handAdapter
        }


        //data layer
        viewModel = (activity as TradeActivity).obtainViewModel()
        fundsModel = obtainViewModel(FundsViewModel::class.java)

        viewModel.handStockList.observe(this, Observer {
            list.addAll(it)
            handAdapter.notifyDataSetChanged()
        })

        fundsModel.funds.observe(this, Observer {
            fundsView.setData(it)
        })

        fundsView.initViewModel(fundsModel)
        fundsModel.query(0)

    }
}


