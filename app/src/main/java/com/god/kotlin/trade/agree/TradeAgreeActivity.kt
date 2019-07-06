package com.god.kotlin.trade.agree

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.god.kotlin.R
import com.god.kotlin.trade.SellFragment
import com.god.kotlin.trade.SellViewModel
import com.god.kotlin.trade.TradeParent
import com.god.kotlin.util.Constant
import com.god.kotlin.util.getEasyFragment
import com.god.kotlin.util.obtainViewModel
import com.god.kotlin.widget.tablayout.EasyFragment
import com.god.kotlin.widget.tablayout.FragmentAdapter
import kotlinx.android.synthetic.main.activity_trade.*

class TradeAgreeActivity : AppCompatActivity(), TradeParent {

    private val fragmentList: MutableList<EasyFragment> = mutableListOf()
    private lateinit var adapter: FragmentAdapter
    private var _type = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trade)

        _type = intent?.getIntExtra(Constant.TRADE_TYPE,0) ?: return

        fragmentList.add(getEasyFragment(BUY_TAG, "要约申报") {
            TradeAgreeFragment.newInstance(
                true
            )
        })
        fragmentList.add(getEasyFragment(SELL_TAG, "要约解除") {
            TradeAgreeFragment.newInstance(
                false
            )
        })
        adapter = FragmentAdapter(supportFragmentManager, fragmentList)

        with(tab_pager) {
            offscreenPageLimit = fragmentList.size
            adapter = this@TradeAgreeActivity.adapter
            sliding_tabs.setViewPager(tab_pager)
            currentItem = _type
        }

        img_back.setOnClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()
        obtainViewModel().getHandList("fundsId")
    }


    override fun obtainViewModel(): SellViewModel = obtainViewModel(SellViewModel::class.java)

}


private const val BUY_TAG = "TradeAgreeActivity_BuyFragment"
private const val SELL_TAG = "TradeAgreeActivity_SellFragment"
