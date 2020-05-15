package com.massky.greenlandvland.ui;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.massky.greenlandvland.R;
import com.massky.greenlandvland.View.ClearableEditText;
import com.massky.greenlandvland.common.CommonUtil;
import com.massky.greenlandvland.common.LocalBroadcastManager;
import com.massky.greenlandvland.common.SharedPreferencesUtils;
import com.massky.greenlandvland.model.entity.Message;
import com.massky.greenlandvland.model.entity.Sc_isLoginNew;
import com.massky.greenlandvland.model.entity.Sc_uploadCamera;
import com.massky.greenlandvland.model.httpclient.HttpUrl.HttpClient;
import com.massky.greenlandvland.model.httpclient.UICallback;

import java.util.ArrayList;
import java.util.List;

public class AddCamaraActivity extends AppCompatActivity {
    private ImageView back;
    private TextView et_name;
    private ClearableEditText et_password;
    private Button btn_next,btn_return;
    private LinearLayout ll_add;
    private RelativeLayout rl_mate;
    private ImageView iv_mate;
    private Animation animation;
    private int deviceVersion;
    private int _nConfigID=1;
    final List<Sc_uploadCamera.UploadCameraParams.Camera> cameraList=new ArrayList<>();
    private String token;
    private String projectCode;
    private String roomNo;
    private int id=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_camara);

        token= SharedPreferencesUtils.getToken(AddCamaraActivity.this);
        projectCode=SharedPreferencesUtils.getProjectCode(AddCamaraActivity.this);
        roomNo=SharedPreferencesUtils.getRoomNo(this);

        init();//初始化控件

        registerMessageReceiver();
    }

    //初始化控件
    private void init() {
        //获取控件
        back= (ImageView) findViewById(R.id.back);
        et_name= (TextView) findViewById(R.id.et_name);
        et_password= (ClearableEditText) findViewById(R.id.et_password);
        btn_next= (Button) findViewById(R.id.btn_next);
        btn_return= (Button) findViewById(R.id.btn_return);
        ll_add= (LinearLayout) findViewById(R.id.ll_add);
        rl_mate= (RelativeLayout) findViewById(R.id.rl_mate);
        iv_mate= (ImageView) findViewById(R.id.iv_mate);
        //添加监听
        back.setOnClickListener(clickListener);
        btn_next.setOnClickListener(clickListener);
        btn_return.setOnClickListener(clickListener);

        animation= AnimationUtils.loadAnimation(this,R.anim.roatmate_animation);
        LinearInterpolator lir = new LinearInterpolator();
        animation.setInterpolator(lir);

    }

    //判断是否无线网连接
    private boolean isWifi() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }
    //根据Android的版本判断获取到的SSID是否有双引号
    public String whetherToRemoveTheDoubleQuotationMarks(String ssid) {

        //获取Android版本号
        deviceVersion = Build.VERSION.SDK_INT;

        if (deviceVersion >= 17) {

            if (ssid.startsWith("\"") && ssid.endsWith("\"")) {

                ssid = ssid.substring(1, ssid.length() - 1);
            }

        }
        return ssid;
    }


    View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.back://回退键
                    onBackPressed();
                    break;
                case R.id.btn_next:
                    //判断是否连接wifi
                    if(isWifi()==true&&!et_name.getText().equals("正在搜索")){
                        ll_add.setVisibility(View.GONE);
                        rl_mate.setVisibility(View.VISIBLE);
                        iv_mate.startAnimation(animation);

                    }else {

                    }
                    break;
                case R.id.btn_return:
                    rl_mate.setVisibility(View.GONE);
                    ll_add.setVisibility(View.VISIBLE);
                    iv_mate.clearAnimation();

                    break;
            }
        }
    };

    private Handler handler=new Handler();
    Runnable wifirunnable=new Runnable() {
        @Override
        public void run() {
            if(isWifi()==true){
                //获取无线网络数据
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                et_name.setText(whetherToRemoveTheDoubleQuotationMarks(wifiInfo.getSSID().toString()));
            }else {
                et_name.setText("正在搜索");
            }
            handler.postDelayed(this,100);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        isForeground = true;
        isLogin();
        handler.postDelayed(wifirunnable,0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isForeground = false;
        handler.removeCallbacks(wifirunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        handler.removeCallbacks(wifirunnable);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    public void onBackPressed() {
        if(rl_mate.isShown()){
            rl_mate.setVisibility(View.GONE);
            ll_add.setVisibility(View.VISIBLE);
            iv_mate.clearAnimation();
        }else{
            finish();
        }
    }



    public static boolean isForeground = false;
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.massky.greenlandvland.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                    if (!TextUtils.isEmpty(extras)) {
                        showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                        Log.e("TAG", "showMsg=" + showMsg);
                        Message message = new Gson().fromJson(extras, Message.class);
                        Log.e("TAG", "message=" + message);
                        if (message.getType().equals("2")) {
                            showdialog();
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("TAG","错误");
            }
        }
    }

    private void showdialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(AddCamaraActivity.this,1);
        builder.setTitle("提示");
        builder.setMessage("该账号已被其他设备登录");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startActivity(new Intent(AddCamaraActivity.this, LoginActivity.class));
                SharedPreferencesUtils.cleanPassword(AddCamaraActivity.this);
                MainActivity.instance.finish();
                MonitoringActivity.instance.finish();
                IndoorMonitorActivity.instance.finish();
                finish();
            }
        });
        builder.create().show();
    }

    private void isLogin() {
        final String phoneNumber= SharedPreferencesUtils.getPhoneNumber(this);
        final String phoneId = SharedPreferencesUtils.getPhoneId(this);
        HttpClient.post(CommonUtil.APPURL, "sc_isLoginNew"
                , new Gson().toJson(new Sc_isLoginNew.LoginNewParams(phoneNumber,phoneId,3))
                , new UICallback() {
                    @Override
                    public void process(String data) {
                        Log.e("TAG","data="+data);
                        Log.e("TAG","phoneNumber="+phoneNumber);
                        Log.e("TAG","phoneId="+phoneId);
                        Sc_isLoginNew.LoginNewResult isLoginResult=new Gson().fromJson(data, Sc_isLoginNew.LoginNewResult.class);
                        int result=isLoginResult.getResult();
                        if(result==1){
                            Log.e("TAG","1-json格式解析失败");
                        }else if(result==100){
                            Log.e("TAG","100-成功");
                        }else if(result==103){
                            Log.e("TAG","103-已登录");
                            showdialog();
                        }else {

                        }
                    }

                    @Override
                    public void onError(String data) {

                    }
                });
    }
}
