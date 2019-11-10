package com.god.kotlin.trade

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.god.kotlin.BaseActivity
import com.god.kotlin.BaseFragment
import com.god.kotlin.R
import com.god.kotlin.data.entity.TradeHandEntity
import com.god.kotlin.user.UserHelper
import com.god.kotlin.util.inflate
import com.god.kotlin.util.obtainViewModel
import com.god.kotlin.util.showSimpleDialog
import com.god.kotlin.util.toast
import kotlinx.android.synthetic.main.fragment_sell.*

/**
 *  newInstance()
 *  flag true买
 *       false卖
 */

class SellFragment : BaseFragment() {

    private lateinit var viewModel: SellViewModel
    private lateinit var tradeView: ITradeView
    private lateinit var tradeParent: TradeParent
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
                0 -> TradeView1(context)
                1 -> TradeMarketView1(context)
                2 -> TradeTransView1(context)
                3 -> TradeBothView1(context)
                4 -> TradeSeveralView1(context)
                else -> throw Exception()
            }
            sell_recycler.addHeaderView(tradeView.getView())
            sell_recycler.addHeaderView(context.inflate(R.layout.trade_holder_handing_title))
            handAdapter = HandAdapter(list, context!!)
            sell_recycler.adapter = handAdapter
            setOnItemClickListener { _, _, position, _ ->
                tradeView.setStockCode(list[position - 2].stkcode)
                setSelection(0)
            }
        }

        tradeParent = (activity as TradeParent)

        viewModel = obtainViewModel(SellViewModel::class.java)

        tradeView.initData(_flag, viewModel)

        viewModel.stockEntity.observe(this, Observer {
            if (!TextUtils.isEmpty(it.stkcode)) {
                tradeView.setData(it)
                viewModel.getHQ(it.market, it.stkcode)
            } else {
                "没有当前股票".toast(context)
            }
        })

        viewModel.available.observe(this, Observer {
            tradeView.setAvailable(it)
        })

        viewModel.level.observe(this, Observer {
            tradeView.updateHQ(it)
        })

        viewModel.loading.observe(this, Observer {
            if (it) {
                showBusyDialog()
            } else {
                dismissBusyDialog()
            }
        })

        viewModel.order.observe(this, Observer {
            showSimpleDialog(
                context, "委托成功" + "\n" +
                        "委托序号：" + it.ordersno + "\n" +
                        "合同序号：" + it.orderid + "\n" +
                        "委托批号：" + it.ordergroup
            )
            tradeParent.triggerHand()
        })

        viewModel.serverealResult.observe(this, Observer {
            showSimpleDialog(
                context, it
            )
            tradeParent.triggerHand()
        })

        viewModel.toast.observe(this, Observer {
            it.toast(context, Toast.LENGTH_LONG)
        })

        tradeParent.getHand().observe(this, Observer {
            list.clear()
            list.addAll(it)
            handAdapter.notifyDataSetChanged()
        })
    }
}

private const val TRADE_DIRECTION = "trade_direction"
private const val TRADE_TYPE = "trade_type"


