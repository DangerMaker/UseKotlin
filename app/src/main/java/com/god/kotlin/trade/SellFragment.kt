package com.god.kotlin.trade

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.god.kotlin.R
import com.god.kotlin.data.entity.TradeHandEntity
import com.god.kotlin.util.inflate
import com.god.kotlin.util.showSimpleDialog
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
        fun newInstance(flag: Boolean,type: Int): SellFragment {
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
            tradeView = when(_type){
                0 -> TradeView(context)
                1 -> TradeMarketView(context)
                2 -> TradeTransView(context)
                else -> View(context)
            }
            sell_recycler.addHeaderView(tradeView)
            sell_recycler.addHeaderView(context.inflate(R.layout.trade_holder_handing_title))
            handAdapter = HandAdapter(list, context!!)
            sell_recycler.adapter = handAdapter
        }

        //data layer
        viewModel = (activity as TradeParent).obtainViewModel()

        viewModel.stockEntity.observe(this, Observer {
            (tradeView as ITradeView).setData(it)
        })

        viewModel.handStockList.observe(this, Observer {
            list.addAll(it)
            handAdapter.notifyDataSetChanged()
        })

        viewModel.available.observe(this, Observer {
            (tradeView as ITradeView).setAvailable(it)
        })

        viewModel.order.observe(this, Observer {
            showSimpleDialog(
                context, "委托成功" + "\n" +
                        "委托序号：" + it.ordersno + "\n" +
                        "合同序号：" + it.orderid + "\n" +
                        "委托批号：" + it.ordergroup
            )
        })

        (tradeView as ITradeView).initData(_flag, viewModel)
    }
}

private const val TRADE_DIRECTION = "trade_direction"
private const val TRADE_TYPE = "trade_type"


