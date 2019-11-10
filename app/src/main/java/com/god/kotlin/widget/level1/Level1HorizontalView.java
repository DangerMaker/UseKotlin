package com.god.kotlin.widget.level1;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ez08.trade.tools.MathUtils;
import com.ez08.trade.tools.ScreenUtil;
import com.god.kotlin.R;
import com.god.kotlin.data.entity.TradeStockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Level1HorizontalView extends LinearLayout implements Level, View.OnClickListener {

    TextView buyPrice1;
    TextView buyPrice2;
    TextView buyPrice3;
    TextView buyPrice4;
    TextView buyPrice5;

    TextView sellPrice1;
    TextView sellPrice2;
    TextView sellPrice3;
    TextView sellPrice4;
    TextView sellPrice5;

    TextView buyVol1;
    TextView buyVol2;
    TextView buyVol3;
    TextView buyVol4;
    TextView buyVol5;

    TextView sellVol1;
    TextView sellVol2;
    TextView sellVol3;
    TextView sellVol4;
    TextView sellVol5;

    public Level1HorizontalView(Context context) {
        super(context);
    }

    public Level1HorizontalView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.trade_view_horizontal_level1, this);

        buyPrice1 = findViewById(R.id.buy_price1);
        buyPrice2 = findViewById(R.id.buy_price2);
        buyPrice3 = findViewById(R.id.buy_price3);
        buyPrice4 = findViewById(R.id.buy_price4);
        buyPrice5 = findViewById(R.id.buy_price5);
        sellPrice1 = findViewById(R.id.sell_price1);
        sellPrice2 = findViewById(R.id.sell_price2);
        sellPrice3 = findViewById(R.id.sell_price3);
        sellPrice4 = findViewById(R.id.sell_price4);
        sellPrice5 = findViewById(R.id.sell_price5);
        buyVol1 = findViewById(R.id.buy_volume1);
        buyVol2 = findViewById(R.id.buy_volume2);
        buyVol3 = findViewById(R.id.buy_volume3);
        buyVol4 = findViewById(R.id.buy_volume4);
        buyVol5 = findViewById(R.id.buy_volume5);
        sellVol1 = findViewById(R.id.sell_volume1);
        sellVol2 = findViewById(R.id.sell_volume2);
        sellVol3 = findViewById(R.id.sell_volume3);
        sellVol4 = findViewById(R.id.sell_volume4);
        sellVol5 = findViewById(R.id.sell_volume5);
        findViewById(R.id.sell5_layout).setOnClickListener(this);
        findViewById(R.id.sell4_layout).setOnClickListener(this);
        findViewById(R.id.sell3_layout).setOnClickListener(this);
        findViewById(R.id.sell2_layout).setOnClickListener(this);
        findViewById(R.id.sell1_layout).setOnClickListener(this);
        findViewById(R.id.buy1_layout).setOnClickListener(this);
        findViewById(R.id.buy2_layout).setOnClickListener(this);
        findViewById(R.id.buy3_layout).setOnClickListener(this);
        findViewById(R.id.buy4_layout).setOnClickListener(this);
        findViewById(R.id.buy5_layout).setOnClickListener(this);
    }

    OnItemClickListener listener;

    @Override
    public void setOnItemClickListener(@NotNull OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void update(@NotNull List<TradeStockEntity.Dang> list) {
        this.list.clear();
        this.list.addAll(list);
        setItem(sellPrice5, sellVol5, 0);
        setItem(sellPrice4, sellVol4, 1);
        setItem(sellPrice3, sellVol3, 2);
        setItem(sellPrice2, sellVol2, 3);
        setItem(sellPrice1, sellVol1, 4);
        setItem(buyPrice1, buyVol1, 5);
        setItem(buyPrice2, buyVol2, 6);
        setItem(buyPrice3, buyVol3, 7);
        setItem(buyPrice4, buyVol4, 8);
        setItem(buyPrice5, buyVol5, 9);
    }

    List<TradeStockEntity.Dang> list = new ArrayList<>();

    private void setItem(TextView price, TextView volume, int position) {
        TradeStockEntity.Dang dang = list.get(position);
        if (dang.fOrder == 0.0) {
            price.setText("- - ");
            price.setTextColor(ScreenUtil.setTextColor(getContext(),R.color.trade_black));
            volume.setText("- - ");
        } else {
            price.setText(MathUtils.format2Num(dang.fPrice));
            price.setTextColor(dang.fPrice > openPrice ? ScreenUtil.setTextColor(getContext(), R.color.trade_red) : ScreenUtil.setTextColor(getContext(), R.color.trade_green));
            volume.setText(MathUtils.getMost4Character(dang.fOrder / 100d));
        }
    }

    Double openPrice;

    @Override
    public void setOpenPrice(double price) {
        this.openPrice = price;
    }

    @Override
    public void onClick(View v) {
        if (listener == null || list.isEmpty()) {
            return;
        }
        if (v.getId() == com.god.kotlin.R.id.sell5_layout) {
            invoke(0);
        } else if (v.getId() == com.god.kotlin.R.id.sell4_layout) {
            invoke(1);
        } else if (v.getId() == com.god.kotlin.R.id.sell3_layout) {
            invoke(2);
        } else if (v.getId() == com.god.kotlin.R.id.sell2_layout) {
            invoke(3);
        } else if (v.getId() == com.god.kotlin.R.id.sell1_layout) {
            invoke(4);
        } else if (v.getId() == com.god.kotlin.R.id.buy1_layout) {
            invoke(5);
        } else if (v.getId() == com.god.kotlin.R.id.buy2_layout) {
            invoke(6);
        } else if (v.getId() == com.god.kotlin.R.id.buy3_layout) {
            invoke(7);
        } else if (v.getId() == com.god.kotlin.R.id.buy4_layout) {
            invoke(8);
        } else if (v.getId() == com.god.kotlin.R.id.buy5_layout) {
            invoke(9);
        }
    }

    private void invoke(int position) {
        if (list.get(position).fOrder != 0.0) {
            listener.onClick(position);
        }
    }
}