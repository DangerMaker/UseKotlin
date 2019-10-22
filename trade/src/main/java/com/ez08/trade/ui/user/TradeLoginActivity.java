package com.ez08.trade.ui.user;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ez08.trade.Constant;
import com.ez08.trade.R;
import com.ez08.trade.net.Callback;
import com.ez08.trade.net.Client;
import com.ez08.trade.net.NetUtil;
import com.ez08.trade.net.login.STradeGateLogin;
import com.ez08.trade.net.login.STradeGateLoginA;
import com.ez08.trade.net.verification.STradeVerificationCode;
import com.ez08.trade.net.verification.STradeVerificationCodeA;
import com.ez08.trade.tools.CommonUtils;
import com.ez08.trade.tools.JumpActivity;
import com.ez08.trade.tools.SharedPreferencesHelper;
import com.ez08.trade.tools.SystemUtil;
import com.ez08.trade.ui.BaseActivity;
import com.ez08.trade.ui.TradeMenuActivity;
import com.ez08.trade.ui.view.BlueUnderLineClickableSpan;
import com.ez08.trade.user.TradeUser;
import com.ez08.trade.user.UserHelper;
import com.xuhao.didi.core.pojo.OriginalData;

import java.util.ArrayList;
import java.util.List;

import static com.ez08.trade.Constant.STORE_LOGIN_NAME;

public class TradeLoginActivity extends BaseActivity implements View.OnClickListener,
        TradePhoneCheckWindows.OnPhoneCheckListener{

    public static final String TAG = "TradeLoginFragment";
    TextView registerBtn;
    Button loginBtn;
    String host = Constant.SERVER_IP;
    ImageView verityImageView;
    ImageView pickAccount;
    TextView account;
    AppCompatCheckBox checkBox;
    LinearLayout accountLayout;

    EditText usernameEdit;
    EditText passwordEdit;
    EditText checkEdit;

    boolean isCanSaved;
    String accountValue;
    private String code;
    String verity;
    final String[] items = new String[]{"资金账户", "深A", "沪Ａ", "深Ｂ", "沪Ｂ", "沪港通", "股转Ａ", "股转Ｂ", "开放式基金", "深港通"};
    final String[] itemsCode = new String[]{"Z", "0", "1", "2", "3", "5", "6", "7", "J", "S"};

    String defaultItem = "Z";
    private int accoutIndex;

    SharedPreferencesHelper sharedPreferencesHelper;

    //1、首先声明一个数组permissions，将需要的权限都放在里面
    private String[] permissions = new String[]{Manifest.permission.READ_PHONE_STATE};
    private List<String> mPermissionList = new ArrayList<>();
    private final int mRequestCode = 100;//权限请求码
    private String phoneNum = "";
    private TradePhoneCheckWindows checkWindows;
    private boolean isNeedCheck;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trade_fragment_login);
        ImageView btnBack = findViewById(R.id.img_back);
        btnBack.setOnClickListener(this);
        TextView title = findViewById(R.id.title);
        title.setText(R.string.trade_login_title);
        loginBtn = findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(this);
        registerBtn = findViewById(R.id.register_btn);
        verityImageView = findViewById(R.id.safe_code_iv);
        pickAccount = findViewById(R.id.account_type_nav);
        pickAccount.setOnClickListener(this);
        account = findViewById(R.id.account_type);
        checkBox = findViewById(R.id.login_auto_check);
        usernameEdit = findViewById(R.id.username_edit);
        passwordEdit = findViewById(R.id.password_edit);
        checkEdit = findViewById(R.id.check_code);
        verityImageView.setOnClickListener(this);
        accountLayout = findViewById(R.id.account_layout);
        accountLayout.setOnClickListener(this);
        checkWindows = new TradePhoneCheckWindows(this);
        checkWindows.setPhoneCheckListener(this);
        SpannableStringBuilder builder = new SpannableStringBuilder(getString(R.string.trade_register_tips));
        builder.setSpan(new BlueUnderLineClickableSpan() {
            @Override
            public void onClick(View widget) {
                Log.e("SpannableStringBuilder", "onClick");

            }
        }, builder.length() - 4, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        registerBtn.setText(builder);
        registerBtn.setMovementMethod(LinkMovementMethod.getInstance());

        sharedPreferencesHelper = new SharedPreferencesHelper(this, STORE_LOGIN_NAME);
        isCanSaved = (boolean) sharedPreferencesHelper.getSharedPreference("isCanSaved", true);
        accountValue = (String) sharedPreferencesHelper.getSharedPreference("accountValue", "");
        accoutIndex = (int) sharedPreferencesHelper.getSharedPreference("accountIndex", 0);
        defaultItem = itemsCode[accoutIndex];
        account.setText(items[accoutIndex]);
        checkBox.setChecked(isCanSaved);
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isCanSaved = isChecked;
            sharedPreferencesHelper.put("isCanSaved", isCanSaved);
        });

        if (isCanSaved) {
            usernameEdit.setText(accountValue);
        }
//        requestPermission();
        createVerityClientAndSend();

    }

    private void setLoginPackage() {
        String info = SystemUtil.getInfo(this, phoneNum);
        STradeGateLogin tradeGateLogin = new STradeGateLogin();
        tradeGateLogin.setBody(defaultItem,accountValue, pwd,
                ss,info);
//        tradeGateLogin.setBody("Z","109000512","123123",checkEdit.getText().toString(),"ZNZ|000EC6CF8DA4|PLEXTOR");

        Client.getInstance().send(tradeGateLogin, (success, data) -> {
            Client.strNet2 = info;
            Client.strUserType = defaultItem;
            Client.userId =  usernameEdit.getText().toString();
            Client.password = passwordEdit.getText().toString();

            STradeGateLoginA gateLoginA = new STradeGateLoginA(data.getHeadBytes(), data.getBodyBytes(), Client.getInstance().aesKey);
            if (!gateLoginA.getbLoginSucc()){
                CommonUtils.show(this,gateLoginA.getSzErrMsg());
                return;
            }
            List<TradeUser> list = new ArrayList<>();
            for (int i = 0; i < gateLoginA.list.size(); i++) {
                TradeUser user = new TradeUser();
                user.market = NetUtil.byteToStr(gateLoginA.list.get(i).sz_market);
                user.name = NetUtil.byteToStr(gateLoginA.list.get(i).sz_name);
                user.fundid = gateLoginA.list.get(i).n64_fundid + "";
                user.custcert = NetUtil.byteToStr(gateLoginA.list.get(i).sz_custcert);
                user.custid = gateLoginA.list.get(i).n64_custid + "";
                user.secuid = NetUtil.byteToStr(gateLoginA.list.get(i).sz_secuid);
                list.add(user);
            }

            UserHelper.setUserList(list);
            startActivity(new Intent(this,TradeMenuActivity.class));
            finish();
        });
    }

    private void setLoginSessionPackage() {
        STradeGateLogin tradeGateLogin = new STradeGateLogin();
        tradeGateLogin.setBody(Client.strUserType, Client.userId, Client.password, Client.sessionId,Client.strNet2);
        Client.getInstance().send(tradeGateLogin, (success, data) -> {
            dismissBusyDialog();
            STradeGateLoginA gateLoginA = new STradeGateLoginA(data.getHeadBytes(), data.getBodyBytes(), Client.getInstance().aesKey);
            if (!gateLoginA.getbLoginSucc()){
                CommonUtils.show(this,gateLoginA.getSzErrMsg());
                Client.getInstance().shutDown();
                JumpActivity.start(this,"登录");
            }else{
                List<TradeUser> list = new ArrayList<>();
                for (int i = 0; i < gateLoginA.list.size(); i++) {
                    TradeUser user = new TradeUser();
                    user.market = NetUtil.byteToStr(gateLoginA.list.get(i).sz_market);
                    user.name = NetUtil.byteToStr(gateLoginA.list.get(i).sz_name);
                    user.fundid = gateLoginA.list.get(i).n64_fundid + "";
                    user.custcert = NetUtil.byteToStr(gateLoginA.list.get(i).sz_custcert);
                    user.custid = gateLoginA.list.get(i).n64_custid + "";
                    user.secuid = NetUtil.byteToStr(gateLoginA.list.get(i).sz_secuid);
                    list.add(user);
                }

                UserHelper.setUserList(list);
                startActivity(new Intent(this,TradeMenuActivity.class));
                finish();
            }
        });
    }

    private void createVerityClientAndSend() { //获取验证码图片 type = 0  验证验证码 type = 1
        Client.getInstance().connect(() -> {
            if (Client.sessionId == null) {
                sendVerityPicRequest();
            }else{

            }
        });
    }

    private void sendVerityPicRequest() {
        new Handler().postDelayed(new Runnable() {

            public void run() {
                Client.getInstance().send(new STradeVerificationCode(30,15), new Callback() {

                    @Override
                    public void onResult(boolean success, OriginalData data) {
                        STradeVerificationCodeA resp = new STradeVerificationCodeA(data.getHeadBytes(), data.getBodyBytes(),Client.getInstance().aesKey);
                        byte[] picReal = resp.getPic();
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(picReal, 0, picReal.length);
                        verityImageView.setImageBitmap(decodedByte);
                        Log.e("STradeVerificationCodeA", resp.toString());
                    }
                });
            }

        }, 0);
    }

    public void showPickDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(items,
                (dialog, which) -> {
                    accoutIndex = which;
                    account.setText(items[which]);
                    defaultItem = itemsCode[which];
                });
        builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    String pwd;
    String ss;
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login_btn) {
            accountValue = usernameEdit.getText().toString().trim();
            pwd = passwordEdit.getText().toString().trim();
            ss = checkEdit.getText().toString().trim();
//            verity = isNeedCheck?code:ss;
//            phoneNum = (String) sharedPreferencesHelper.getSharedPreference(defaultItem+"&"+accountValue, "");
//            sharedPreferencesHelper.put("accountValue", accountValue);
//            sharedPreferencesHelper.put("accountIndex", accoutIndex);
//            if (isInvalid(v, phoneNum, accountValue, pwd, ss)) {
//                isNeedCheck = false;
            if(Client.sessionId == null) {
                setLoginPackage();
            }else{
                setLoginSessionPackage();
            }
//            }
        } else if (v.getId() == R.id.account_type_nav) {
            showPickDialog();
        } else if (v.getId() == R.id.safe_code_iv) {
            sendVerityPicRequest();
        } else if (v.getId() == R.id.account_layout) {
            showPickDialog();
        } else if (v.getId() == R.id.img_back) {
            onBackPressed();
        }
    }

    private boolean isInvalid(View v, String phoneNum, String username, String pwd, String verity) {
//        if (TextUtils.isEmpty(username)) {
//            CommonUtils.show(this, R.string.trade_login_username);
//            return false;
//        }
//        if (TextUtils.isEmpty(phoneNum)) {
//            checkWindows.showPopupWindow(v);
//            isNeedCheck = true;
//            return false;
//        }
//        if (TextUtils.isEmpty(pwd)) {
//            CommonUtils.show(this, R.string.trade_login_pwd);
//            return false;
//        }
//        if (TextUtils.isEmpty(verity)) {
//            CommonUtils.show(this, R.string.trade_login_verity);
//            return false;
//        }
        return true;

    }

    private void requestPermission() {
        mPermissionList.clear();//清空没有通过的权限
        //逐个判断你要的权限是否已经通过
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);//添加还未授予的权限
            }
        }
        //申请权限
        if (mPermissionList.size() > 0) {//有权限没有通过，需要申请
            ActivityCompat.requestPermissions(this, permissions, mRequestCode);
        } else {
            String info = SystemUtil.getInfo(this, phoneNum);
            Log.e("TradeLoginActivity1", info);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasPermissionDismiss = false;//有权限没有通过
        if (mRequestCode == requestCode) {
            for (int grantResult : grantResults) {
                if (grantResult == -1) {
                    hasPermissionDismiss = true;
                }
            }
            //如果有权限没有被允许
            if (hasPermissionDismiss) {
                String info = SystemUtil.getInfo(this, phoneNum);
                Log.e("TradeLoginActivity2", "申请权限失败" + info);//跳转到系统设置权限页面，或者直接关闭页面，不让他继续访问
            } else {
                String info = SystemUtil.getInfo(this, phoneNum);
                Log.e("TradeLoginActivity2", info);
            }
        }
    }

    /**
     * 获取手机验证码
     *
     * @param phoneNum
     */
    @Override
    public void onSendNum(String phoneNum) {
//        this.phoneNum = phoneNum;
//        Client.getInstance().send(new STradeSMSSend(phoneNum), (success, data) -> {
//            STradeSMSSendA resp = new STradeSMSSendA(data.getHeadBytes(), data.getBodyBytes(),Client.getInstance().aesKey);
//            String szError = resp.getSzError();
//            Toast.makeText(context,szError,Toast.LENGTH_LONG).show();
//            Log.e("szError","szError="+szError);
//        });
    }

    /**
     * 验证码校验
     * @param phoneNum
     * @param code
     */
    @Override
    public void onCheckNum(String phoneNum, String code) {
//        this.phoneNum = phoneNum;
//        this.code = code;
//        showBusyDialog();
//        Client.getInstance().send(new STradeSMSCheck(phoneNum,code), (success, data) -> {
//            dismissBusyDialog();
//            STradeSMSCheckA resp = new STradeSMSCheckA(data.getHeadBytes(), data.getBodyBytes(),Client.getInstance().aesKey);
//            boolean b = resp.getbSuccess();
//            if (b) {
//                sharedPreferencesHelper.put(defaultItem+"&"+accountValue, phoneNum);
//                checkWindows.dismiss();
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Client.getInstance().shutDown();
    }
    private void checkLogin(){
//        if (Client.sessionId!=null){
//            if (!Client.getInstance().isConnect()){
//                Client.getInstance().unBind();
//                Client.getInstance().connect(null);
//            }
//        }else {
////            JumpActivity.start(this,"登录");
////            this.finish();
//        }
    }
}
