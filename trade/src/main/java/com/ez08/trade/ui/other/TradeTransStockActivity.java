package com.ez08.trade.ui.other;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ez08.trade.R;
import com.ez08.trade.ui.BaseActivity;
import com.ez08.trade.ui.trade.TradeOptionFragment;

public class TradeTransStockActivity extends BaseActivity implements View.OnClickListener {
    ImageView backBtn;
    TextView titleView;

    FragmentManager fragmentManager;
    RelativeLayout container;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trade_activity_trans_stock);

        container = findViewById(R.id.container);
        titleView = findViewById(R.id.title);
        titleView.setText("转股回售");
        backBtn = findViewById(R.id.img_back);
        backBtn.setOnClickListener(this);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = TradeOptionFragment.newInstance(7);
        fragmentTransaction.add(R.id.container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        if(v == backBtn){
            finish();
        }
    }
}
