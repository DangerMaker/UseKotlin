package com.god.kotlin

import android.content.Intent
import android.os.Bundle
import com.god.kotlin.menu.MenuActivity
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        button2.setOnClickListener {
            startActivity(Intent(context, MenuActivity::class.java))
        }
    }
}