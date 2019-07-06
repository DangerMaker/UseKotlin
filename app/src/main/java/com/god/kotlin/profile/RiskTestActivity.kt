package com.god.kotlin.profile

import android.os.Bundle
import android.webkit.WebChromeClient
import androidx.appcompat.app.AppCompatActivity
import com.god.kotlin.R
import kotlinx.android.synthetic.main.activity_risk_test.*
import kotlinx.android.synthetic.main.toolbar_normal.*

class RiskTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_risk_test)

        toolbar_back.setOnClickListener { finish() }
        toolbar_title.text = "风险等级测评"
        with(risk_web) {
            webChromeClient = WebChromeClient()
            settings.javaScriptEnabled = true
            loadUrl("https://www.baidu.com")
        }
    }
}