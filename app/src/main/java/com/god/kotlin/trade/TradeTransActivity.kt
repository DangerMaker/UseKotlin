package com.god.kotlin.trade

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.god.kotlin.BaseActivity
import com.god.kotlin.R
import com.god.kotlin.util.addFragment
import com.god.kotlin.util.obtainViewModel
import kotlinx.android.synthetic.main.toolbar_normal.*

class TradeTransActivity : BaseActivity(),TradeParent {

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
    }

    override fun onResume() {
        super.onResume()
        obtainViewModel().getHandList("fundsId")
    }

    override fun obtainViewModel(): SellViewModel = obtainViewModel(SellViewModel::class.java)

}

private const val TAG = "TradeTransActivity_LellFragmentt"
