package com.god.kotlin.trade.agree

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.god.kotlin.R
import com.god.kotlin.data.entity.TradeStockEntity
import com.god.kotlin.trade.SellViewModel
import com.god.kotlin.user.UserHelper
import kotlinx.android.synthetic.main.fragment_trade_agree.*

class TradeAgreeFragment : Fragment() {
    private lateinit var viewModel: SellViewModel
    private var _flag: Boolean = true
    private var title = "0Y"
    private var bsflag = "要约申报"
    private var stockEntity: TradeStockEntity? = null

    companion object {
        fun newInstance(flag: Boolean): TradeAgreeFragment {
            return TradeAgreeFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(TRADE_DIRECTION, flag)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_trade_agree, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        _flag = arguments?.getBoolean(TRADE_DIRECTION) ?: return
        if (_flag) {
            agree_max_title.text = "可用股数"
            agree_num_title.text = "预售数量"
            bsflag = "0Y"
            title = "要约申报"
        } else {
            agree_max_title.text = "解除上限"
            agree_num_title.text = "解除数量"
            bsflag = "0E"
            title = "要约解除"
        }

        viewModel = (activity as TradeAgreeActivity).obtainViewModel()

        agree_stock_code.setOnSearchListener {
            viewModel.search(it)
        }

        viewModel.stockEntity.observe(this, Observer {
            stockEntity = it
            agree_stock_code.setData(it.stkcode, it.stkname)
//            viewModel.getAvailable(it.stkcode, it.fixprice, bsflag)
        })

        viewModel.available.observe(this, Observer {
            agree_max.text = it.toString()
        })

        agree_submit.setOnClickListener {
            stockEntity?.let {
                val user =  UserHelper.getUserByMarket(it.market)

                viewModel.transaction(
                    "market", it.stkcode,user.secuid,user.fundid,
                    it.fixprice, 123, bsflag
                )
            }
        }


    }

}

private const val TRADE_DIRECTION = "trade_direction"


