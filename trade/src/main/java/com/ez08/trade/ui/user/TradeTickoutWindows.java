package com.ez08.trade.ui.user;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.ez08.trade.R;
import com.ez08.trade.TradeInitalizer;
import com.ez08.trade.tools.JumpActivity;

/**
 * 重复登录，踢出提示
 */
public class TradeTickoutWindows extends Dialog implements View.OnClickListener {

    private Activity context;
    public TradeTickoutWindows(Activity context) {
        super(context,R.style.dialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trade_login_tickout);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        TextView btnTickoutCancel = findViewById(R.id.btnTickoutCancel);
        btnTickoutCancel.setOnClickListener(this);
        TextView btnTickoutRelogin = findViewById(R.id.btnTickoutRelogin);
        btnTickoutRelogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        JumpActivity.start(context,"登录");
        context.finish();
        dismiss();
    }
}
