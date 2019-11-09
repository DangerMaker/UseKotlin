package com.god.kotlin.trade

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.viewpager.widget.ViewPager
import com.god.kotlin.BaseActivity
import com.god.kotlin.R
import com.god.kotlin.data.entity.TradeHandEntity
import com.god.kotlin.user.UserHelper
import com.god.kotlin.util.addFragment
import com.god.kotlin.util.obtainViewModel
import com.xuhao.didi.socket.common.interfaces.basic.AbsLoopThread
import kotlinx.android.synthetic.main.activity_trade.*
import kotlinx.android.synthetic.main.toolbar_normal.*
import java.lang.Exception

class TradeTransActivity : BaseActivity(),TradeParent {
    override fun getHand(): LiveData<MutableList<TradeHandEntity>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun triggerHand() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private lateinit var mReconnectTestingThread: CycleLoopThread


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trade_trans)

        toolbar_title.text = "转股回售"
        toolbar_back.setOnClickListener{
            finish()
        }

        addFragment(TAG,R.id.container,true){
            SellFragment.newInstance(true,2)
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

    private inner class CycleLoopThread : AbsLoopThread() {

        override fun runInLoopThread() {
            obtainViewModel().getHandList(UserHelper.getUser().fundid)
            Thread.sleep(5000)
        }

        override fun loopFinish(e: Exception?) {
        }

    }

    fun obtainViewModel(): SellViewModel = obtainViewModel(SellViewModel::class.java)

}

private const val TAG = "TradeTransActivity_Fragment"
