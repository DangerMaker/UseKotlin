package com.god.kotlin.trade

import android.os.Bundle
import com.god.kotlin.BaseActivity
import com.god.kotlin.R
import com.god.kotlin.user.UserHelper
import com.god.kotlin.util.Constant
import com.god.kotlin.util.obtainViewModel
import com.xuhao.didi.socket.common.interfaces.basic.AbsLoopThread
import kotlinx.android.synthetic.main.activity_trade.*
import kotlinx.android.synthetic.main.activity_trade_both.*
import kotlinx.android.synthetic.main.toolbar_normal.*

class TradeBothActivity : BaseActivity(), TradeParent {

    private var _type = 0

    @Volatile
    private lateinit var mReconnectTestingThread: CycleLoopThread


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trade_both)

        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.add(R.id.container,SellFragment.newInstance(true, 3))
        transaction.commit()

        toolbar_back.setOnClickListener { finish() }
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


