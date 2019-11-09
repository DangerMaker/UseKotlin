package com.god.kotlin.trade

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.viewpager.widget.ViewPager
import com.god.kotlin.BaseActivity
import com.god.kotlin.EmptyFragment
import com.god.kotlin.R
import com.god.kotlin.data.entity.TradeHandEntity
import com.god.kotlin.trade.funds.FundsFragment
import com.god.kotlin.trade.order.OrderFragment
import com.god.kotlin.trade.order.OrderViewModel
import com.god.kotlin.user.UserHelper
import com.god.kotlin.util.Constant
import com.god.kotlin.util.getEasyFragment
import com.god.kotlin.util.obtainViewModel
import com.god.kotlin.util.showSimpleDialog
import com.god.kotlin.widget.tablayout.EasyFragment
import com.god.kotlin.widget.tablayout.FragmentAdapter
import com.xuhao.didi.socket.common.interfaces.basic.AbsLoopThread
import kotlinx.android.synthetic.main.activity_trade.*
import java.lang.Exception

class TradeActivity : BaseActivity(), TradeParent {
    private val fragmentList: MutableList<EasyFragment> = mutableListOf()
    private lateinit var adapter: FragmentAdapter
    private var _type = 0
    private var _current = 0

    @Volatile
    private lateinit var mReconnectTestingThread: CycleLoopThread


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trade)

        _type = intent?.getIntExtra(Constant.TRADE_TYPE, 0) ?: return
        _current = intent?.getIntExtra(Constant.TRADE_MAIN_PAGE, 0) ?: return

        when (_type) {
            0 -> {
                fragmentList.add(getEasyFragment(BUY_TAG, "买入") { SellFragment.newInstance(true, 0) })
                fragmentList.add(getEasyFragment(SELL_TAG, "卖出") { SellFragment.newInstance(false, 0) })
                fragmentList.add(getEasyFragment(CANCEL_TAG, "撤单") { OrderFragment.newInstance() })
                fragmentList.add(getEasyFragment(HOLD_TAG, "持仓") { FundsFragment.newInstance() })
            }
            1 -> {
                fragmentList.add(getEasyFragment(BUY_TAG, "市价买入") { SellFragment.newInstance(true, 1) })
                fragmentList.add(getEasyFragment(SELL_TAG, "市价卖出") { SellFragment.newInstance(false, 1) })
            }
            2 -> {
                fragmentList.add(getEasyFragment(BUY_TAG, "批量买入") { SellFragment.newInstance(true, 4) })
                fragmentList.add(getEasyFragment(SELL_TAG, "批量卖出") { SellFragment.newInstance(false, 4) })
            }
            3 -> {
                fragmentList.add(getEasyFragment(SELL_TAG, "转股回售") { SellFragment.newInstance(true, 2) })
            }
        }


        adapter = FragmentAdapter(supportFragmentManager, fragmentList)

        with(tab_pager) {
            offscreenPageLimit = fragmentList.size
            addOnPageChangeListener(PageChangeListener())
            adapter = this@TradeActivity.adapter
            sliding_tabs.setViewPager(this)
            currentItem = _current
        }

        img_back.setOnClickListener { finish() }
        mReconnectTestingThread = CycleLoopThread()
//        if (_type == 2) {
//            obtainHandViewModel().getHandList(UserHelper.getUser().fundid)
//        }

        start()
    }

    override fun onDestroy() {
        super.onDestroy()
        stop()
    }


    @Synchronized
    private fun stop() {
        if (mReconnectTestingThread != null) {
            mReconnectTestingThread.shutdown()
        }
    }

    private fun start() {
        synchronized(mReconnectTestingThread) {
            if (mReconnectTestingThread.isShutdown) {
                mReconnectTestingThread.start()
            }
        }
    }

    private inner class PageChangeListener : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        }

        override fun onPageSelected(position: Int) {
            tab_pager.currentItem = position
        }
    }

    private inner class CycleLoopThread : AbsLoopThread() {

        override fun runInLoopThread() {
//            if(tab_pager.currentItem == 2){
//                obtainOrderModel().query(100, 1)
//            }else{
            obtainHandViewModel().getHandList(UserHelper.getUser().fundid)
//            }
            Thread.sleep(5000)
        }

        override fun loopFinish(e: Exception?) {
        }

    }

    override fun getHand(): LiveData<MutableList<TradeHandEntity>> {
        return obtainHandViewModel().handStockList
    }

    override fun triggerHand() {
        obtainHandViewModel().getHandList(UserHelper.getUser().fundid)
    }

    private fun obtainHandViewModel() = obtainViewModel(HandViewModel::class.java)
//    fun obtainOrderModel() : OrderViewModel = obtainViewModel(OrderViewModel::class.java)

}


private const val BUY_TAG = "TradeActivity_BuyFragment"
private const val SELL_TAG = "TradeActivity_SellFragment"
private const val CANCEL_TAG = "TradeActivity_cancelFragment"
private const val HOLD_TAG = "TradeActivity_HoldFragment"


