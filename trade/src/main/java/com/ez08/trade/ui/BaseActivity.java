package com.ez08.trade.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ez08.trade.TradeInitalizer;
import com.ez08.trade.net.Client;
import com.ez08.trade.net.TickoutListener;
import com.ez08.trade.tools.NetworkReceiver;
import com.ez08.trade.ui.user.TradeTickoutWindows;

public class BaseActivity extends AppCompatActivity {

    protected String TAG;
    protected Context context;
    protected TradeTickoutWindows tradeTickoutWindows;
    private NetworkReceiver networkReceiver;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        TAG = this.getClass().getSimpleName();
        tradeTickoutWindows = new TradeTickoutWindows(this);
//        checkNetwork();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    protected void dismissBusyDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
        }
    }

    ProgressDialog pDialog;

    protected void showBusyDialog() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("请稍候...");
        pDialog.setCancelable(true);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            pDialog.show();
        } else {
            runOnUiThread(() -> pDialog.show());
        }
    }

    protected ColorStateList setTextColor(int color) {
        return context.getResources().getColorStateList(color);
    }

//    private void checkNetwork(){
//        networkReceiver = new NetworkReceiver(this, new NetworkReceiver.NetworkCallback() {
//            @Override
//            public void onAvailable() {
//                if (Client.sessionId!=null&&!Client.getInstance().isConnect()){
//                    Log.e("reconnet","重连");
//                    Client.getInstance().unBind();
//                    Client.getInstance().connect(null);
//                }
//            }
//            @Override
//            public void onLost() {
//                Log.e("reconnet","断网");
//                Client.getInstance().unBind();
//            }
//        });
//    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
//        networkReceiver.unRegister(this);
    }
}
