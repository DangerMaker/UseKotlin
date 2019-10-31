package com.god.kotlin.trade

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.god.kotlin.BaseActivity
import com.god.kotlin.R
import com.god.kotlin.user.UserHelper
import com.god.kotlin.util.Constant
import com.god.kotlin.util.addFragment
import com.god.kotlin.util.getEasyFragment
import com.god.kotlin.util.obtainViewModel
import com.god.kotlin.widget.tablayout.EasyFragment
import com.god.kotlin.widget.tablayout.FragmentAdapter
import com.xuhao.didi.socket.common.interfaces.basic.AbsLoopThread
import kotlinx.android.synthetic.main.activity_trade.*
import kotlinx.android.synthetic.main.toolbar_normal.*
import java.lang.Exception

class TradeSeveralActivity : BaseActivity(),TradeParent {

    private val fragmentList: MutableList<EasyFragment> = mutableListOf()
    private lateinit var adapter: FragmentAdapter
    private var _type = 0
    private lateinit var mReconnectTestingThread: CycleLoopThread


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trade)

        fragmentList.add(getEasyFragment(BUY_TAG, "批量买入") { SellFragment.newInstance(true, 4) })
        fragmentList.add(getEasyFragment(SELL_TAG, "批量卖出") { SellFragment.newInstance(false, 4) })
        adapter = FragmentAdapter(supportFragmentManager, fragmentList)

        with(tab_pager) {
            offscreenPageLimit = fragmentList.size
            addOnPageChangeListener(PageChangeListener())
            adapter = this@TradeSeveralActivity.adapter
            sliding_tabs.setViewPager(tab_pager)
            currentItem = _type
        }

        img_back.setOnClickListener { finish() }
        mReconnectTestingThread = CycleLoopThread()
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

            obtainViewModel().getHandList(UserHelper.getUser().fundid)
            Thread.sleep(5000)
        }

        override fun loopFinish(e: Exception?) {
        }

    }

    override fun obtainViewModel(): SellViewModel = obtainViewModel(SellViewModel::class.java)
}


private const val BUY_TAG = "TradeActivity_BuyFragment"
private const val SELL_TAG = "TradeActivity_SellFragment"