package com.god.kotlin.trade

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.god.kotlin.BaseActivity
import com.god.kotlin.R
import com.god.kotlin.data.entity.TradeHandEntity
import com.god.kotlin.util.inflate
import com.god.kotlin.util.showSimpleDialog
import com.god.kotlin.util.toast
import kotlinx.android.synthetic.main.fragment_sell.*

/**
 *  newInstance()
 *  flag true买
 *       false卖
 */

class SellFragment : Fragment() {

    private lateinit var viewModel: SellViewModel
    private lateinit var tradeView: View
    private lateinit var handAdapter: HandAdapter
    private var list: MutableList<TradeHandEntity> = mutableListOf()
    private var _flag: Boolean = true
    private var _type: Int = 0

    companion object {
        fun newInstance(flag: Boolean, type: Int): SellFragment {
            return SellFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(TRADE_DIRECTION, flag)
                    putInt(TRADE_TYPE, type)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_sell, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        _flag = arguments?.getBoolean(TRADE_DIRECTION) ?: return
        _type = arguments?.getInt(TRADE_TYPE) ?: return

        with(sell_recycler) {
            tradeView = when (_type) {
                0 -> TradeView(context)
                1 -> TradeMarketView(context)
                2 -> TradeTransView(context)
                3 -> TradeBothView(context)
                4 -> TradeSeveralView(context)
                else -> View(context)
            }
            sell_recycler.addHeaderView(tradeView)
            sell_recycler.addHeaderView(context.inflate(R.layout.trade_holder_handing_title))
            handAdapter = HandAdapter(list, context!!)
            sell_recycler.adapter = handAdapter
            setOnItemClickListener { _, _, position, _ ->
                (tradeView as ITradeView).setStockCode(list[position - 2].stkcode)
                setSelection(0)
            }
        }

        //data layer
        viewModel = (activity as TradeParent).obtainViewModel()
        (tradeView as ITradeView).initData(_flag, viewModel)

        viewModel.stockEntity.observe(this, Observer {
            if (!TextUtils.isEmpty(it.stkcode)) {
                (tradeView as ITradeView).setData(it)
                viewModel.getHQ(it.market, it.stkcode)
            } else {
                "没有当前股票".toast(context)
            }
        })

        viewModel.available.observe(this, Observer {
            (tradeView as ITradeView).setAvailable(it)
        })

        viewModel.currentHQ.observe(this, Observer {
            (tradeView as ITradeView).updateHQ(it)
        })

        viewModel.tips.observe(this, Observer {
            it.toast(context, Toast.LENGTH_LONG)
        })

        viewModel.handStockList.observe(this, Observer {
            list.clear()
            list.addAll(it)
            handAdapter.notifyDataSetChanged()
        })
    }
}

private const val TRADE_DIRECTION = "trade_direction"
private const val TRADE_TYPE = "trade_type"


