package com.ez08.trade.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ez08.trade.R;
import com.ez08.trade.net.Client;
import com.ez08.trade.tools.JumpActivity;

public class TradeMenuActivity extends BaseActivity implements View.OnClickListener {
    FragmentManager fragmentManager;
    RelativeLayout container;
    TradeMenuFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trade_activity_menu);
        container = findViewById(R.id.container);
        ImageView btnBack = findViewById(R.id.img_back);
        btnBack.setOnClickListener(this);
        TextView title = findViewById(R.id.title);
        title.setText(R.string.trade_main_title);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (savedInstanceState != null) {
            fragment = (TradeMenuFragment) fragmentManager.findFragmentByTag("tradeMenuFragment");
        }else {
            fragment = TradeMenuFragment.newInstance();
            fragmentTransaction.add(R.id.container, fragment);
            fragmentTransaction.commit();
        }

//        if (Client.sessionId!=null){
//            if (!Client.getInstance().isConnect()){
//                Client.getInstance().unBind();
//                Client.getInstance().connect(null);
//            }
//        }else {
            JumpActivity.start(this,"登录");
//            this.finish();
//        }
    }

    @Override
    public void onClick(View v) {
        onBackPressed();
    }
}
