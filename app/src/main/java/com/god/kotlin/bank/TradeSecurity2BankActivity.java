package com.god.kotlin.bank;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.ez08.trade.ui.BaseActivity;
import com.god.kotlin.R;

public class TradeSecurity2BankActivity extends BaseActivity implements View.OnClickListener {
    ImageView img_back;
    TextView titleView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trade_activity_security2_bank);
        img_back = findViewById(R.id.toolbar_back);
        img_back.setOnClickListener(this);
        titleView = findViewById(R.id.toolbar_title);
        titleView.setText("证券转银行");
    }

    @Override
    public void onClick(View v) {
        if(v == img_back){
            finish();
        }
    }
}
