package com.god.kotlin.trade

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.god.kotlin.R
import com.god.kotlin.trade.funds.FundsFragment
import com.god.kotlin.trade.order.OrderFragment
import com.god.kotlin.util.Constant
import com.god.kotlin.util.getEasyFragment
import com.god.kotlin.util.obtainViewModel
import com.god.kotlin.widget.tablayout.EasyFragment
import com.god.kotlin.widget.tablayout.FragmentAdapter
import kotlinx.android.synthetic.main.activity_trade.*

class TradeMarketActivity : AppCompatActivity(),TradeParent {

    private val fragmentList: MutableList<EasyFragment> = mutableListOf()
    private lateinit var adapter: FragmentAdapter
    private var _type = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trade)

        _type = intent?.getIntExtra(Constant.TRADE_TYPE,0) ?: return

        fragmentList.add(getEasyFragment(BUY_TAG, "市价买入") { SellFragment.newInstance(true, 1) })
        fragmentList.add(getEasyFragment(SELL_TAG, "市价卖出") { SellFragment.newInstance(false, 1) })
        adapter = FragmentAdapter(supportFragmentManager, fragmentList)

        with(tab_pager) {
            offscreenPageLimit = fragmentList.size
            addOnPageChangeListener(PageChangeListener())
            adapter = this@TradeMarketActivity.adapter
            sliding_tabs.setViewPager(tab_pager)
            currentItem = _type
        }

        img_back.setOnClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()
        obtainViewModel().getHandList("fundsId")
    }


    class PageChangeListener : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        }

        override fun onPageSelected(position: Int) {
        }
    }

    override fun obtainViewModel(): SellViewModel = obtainViewModel(SellViewModel::class.java)

}


private const val BUY_TAG = "TradeActivity_BuyFragment"
private const val SELL_TAG = "TradeActivity_SellFragment"
