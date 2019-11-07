package com.god.kotlin.trade

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.viewpager.widget.ViewPager
import com.god.kotlin.BaseActivity
import com.god.kotlin.R
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

class TradeActivity : BaseActivity(),TradeParent {

    private val fragmentList: MutableList<EasyFragment> = mutableListOf()
    private lateinit var adapter: FragmentAdapter
    private var _type = 0

    @Volatile
    private lateinit var mReconnectTestingThread: CycleLoopThread


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trade)

        _type = intent?.getIntExtra(Constant.TRADE_TYPE,0) ?: return

        fragmentList.add(getEasyFragment(BUY_TAG, "买入") { SellFragment.newInstance(true, 0) })
        fragmentList.add(getEasyFragment(SELL_TAG, "卖出") { SellFragment.newInstance(false, 0) })
        fragmentList.add(getEasyFragment(CANCEL_TAG, "撤单") { OrderFragment.newInstance() })
        fragmentList.add(getEasyFragment(HOLD_TAG, "持仓") { FundsFragment.newInstance() })
        adapter = FragmentAdapter(supportFragmentManager, fragmentList)

        with(tab_pager) {
            offscreenPageLimit = fragmentList.size
            addOnPageChangeListener(PageChangeListener())
            adapter = this@TradeActivity.adapter
            sliding_tabs.setViewPager(this)
            currentItem = _type
        }

        obtainViewModel().loading.observe(this, Observer {
            if(it){
                showBusyDialog()
            }else{
                dismissBusyDialog()
            }
        })

        obtainViewModel().order.observe(this, Observer {
            showSimpleDialog(
                context, "委托成功" + "\n" +
                        "委托序号：" + it.ordersno + "\n" +
                        "合同序号：" + it.orderid + "\n" +
                        "委托批号：" + it.ordergroup
            )

            obtainViewModel().getHandList(UserHelper.getUser().fundid)
        })

        img_back.setOnClickListener { finish() }
        mReconnectTestingThread = CycleLoopThread()
        if(_type == 2){
            obtainViewModel().getHandList(UserHelper.getUser().fundid)
        }

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

   private  inner class PageChangeListener : ViewPager.OnPageChangeListener {
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
            if(tab_pager.currentItem == 2){
                obtainOrderModel().query(100, 1)
            }else{
                obtainViewModel().getHandList(UserHelper.getUser().fundid)
            }
            Thread.sleep(5000)
        }

        override fun loopFinish(e: Exception?) {
        }

    }

    override fun obtainViewModel(): SellViewModel = obtainViewModel(SellViewModel::class.java)
    fun obtainOrderModel() : OrderViewModel = obtainViewModel(OrderViewModel::class.java)

}


private const val BUY_TAG = "TradeActivity_BuyFragment"
private const val SELL_TAG = "TradeActivity_SellFragment"
private const val CANCEL_TAG = "TradeActivity_cancelFragment"
private const val HOLD_TAG = "TradeActivity_HoldFragment"


