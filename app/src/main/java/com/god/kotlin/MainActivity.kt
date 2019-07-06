package com.god.kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.god.kotlin.login.LoginFragment
import com.god.kotlin.util.addFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addFragment(TAG,R.id.container,true){
            LoginFragment.newInstance("123")
        }
    }
}

private const val TAG = "MainActivity_LoginFragment"
