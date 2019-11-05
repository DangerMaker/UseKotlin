package com.ez08.trade.ui.bank;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ez08.trade.Constant;
import com.ez08.trade.R;
import com.ez08.trade.net.Callback;
import com.ez08.trade.net.Client;
import com.ez08.trade.tools.YCParser;
import com.ez08.trade.ui.BaseActivity;
import com.ez08.trade.ui.BaseAdapter;
import com.ez08.trade.ui.bank.adpater.TradeTransAdapter;
import com.ez08.trade.ui.bank.entity.TransferEntity;
import com.ez08.trade.ui.bank.entity.TransferTitleEntity;
import com.ez08.trade.ui.user.entity.ShareHoldersEntity;
import com.ez08.trade.ui.user.entity.TradeShareHoldersTitle;
import com.ez08.trade.ui.view.LinearItemDecoration;
import com.ez08.trade.user.UserHelper;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.*;

public class TradeBankQueryActivity extends BaseActivity implements View.OnClickListener {

    ImageView imgBack;
    TextView titleView;

    RecyclerView recyclerView;
    LinearLayoutManager manager;
    List<Object> mList;
    BaseAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trade_activity_query_bank_record);
        titleView = findViewById(R.id.title);
        titleView.setText("转账查询");
        imgBack = findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter = new TradeTransAdapter(context);
        recyclerView.setAdapter(adapter);
        LinearItemDecoration divider = new LinearItemDecoration(this);
        recyclerView.addItemDecoration(divider);

        getInfo();
    }


    public void getInfo() {
        String body = "FUN=410608&TBL_IN=fundid,moneytype,sno,extsno,qryoperway;" +
                UserHelper.getUser().fundid + "," +
                "" + "," +
                "" + "," +
                "" + "," +
                ";";

        Client.getInstance().sendBiz(body, (success, data) -> {
            Log.e("sendBiz", data);
            if (success) {
                mList = new ArrayList<>();
                mList.add(new TransferTitleEntity());
                List<Map<String, String>> result = YCParser.parseArray(data);
                List<TransferEntity> tempList = new ArrayList<>();
                for (int i = 0; i < result.size(); i++) {
                    TransferEntity entity = new TransferEntity();
                    entity.operdate = result.get(i).get("operdate");
                    entity.opertime = result.get(i).get("opertime");
                    entity.status = result.get(i).get("status");
                    entity.fundeffect = result.get(i).get("fundeffect");
//                    mList.add(entity);
                    tempList.add(entity);
                }

                Collections.sort(tempList,new DateComparator());
                mList.addAll(tempList);
                adapter.clearAndAddAll(mList);
            }
        });

    }

    public static class DateComparator implements Comparator<TransferEntity> {

        @Override
        public int compare(TransferEntity transferEntity, TransferEntity t1) {
            long time1 = convertTimeToLong(transferEntity.operdate+transferEntity.opertime);
            long time2 = convertTimeToLong(t1.operdate+t1.opertime);
            if(time1 > time2){
                return 1;
            }else if(time1 < time2){
                return  -1;
            }else{
                return 0;
            }
        }

        public static Long convertTimeToLong(String time) {
            Date date = null;
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH:mm:ss");
                date = sdf.parse(time);
                return date.getTime();
            } catch (Exception e) {
                e.printStackTrace();
                return 0L;
            }
        }


    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_back) {
            finish();
        }
    }
}
