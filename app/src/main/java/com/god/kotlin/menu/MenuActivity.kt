package com.god.kotlin.menu

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.god.kotlin.R
import com.god.kotlin.util.addFragment

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        addFragment(TAG, R.id.container,true){
            MenuFragment.newInstance()
        }
    }
}

private const val TAG = "MenuActivity_MenuFragment"