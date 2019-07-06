package com.god.kotlin.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.god.kotlin.R
import com.god.kotlin.util.obtainViewModel
import kotlinx.android.synthetic.main.activity_risk_query.*
import kotlinx.android.synthetic.main.toolbar_normal.*

class RiskQueryActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_risk_query)

        toolbar_back.setOnClickListener { finish() }
        toolbar_title.text = "客户风险级别查询"

        val viewModel = obtainViewModel(RiskQueryViewModel::class.java)
        viewModel.level.observe(this, Observer {
            risk_type.text = it.type
            risk_score.text = it.score
        })

        viewModel.query("123")
    }
}