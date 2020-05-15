package com.massky.greenlandvland.ui;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.massky.greenlandvland.R;
import com.massky.greenlandvland.common.CommonUtil;
import com.massky.greenlandvland.common.LocalBroadcastManager;
import com.massky.greenlandvland.common.SharedPreferencesUtils;
import com.massky.greenlandvland.model.entity.Message;
import com.massky.greenlandvland.model.entity.Sc_isLoginNew;
import com.massky.greenlandvland.model.entity.Weather;
import com.massky.greenlandvland.model.httpclient.HttpUrl.HttpClient;
import com.massky.greenlandvland.model.httpclient.UICallback;

public class EnvironmentActivity extends AppCompatActivity {
    private ImageView back;
    private TextView pm25,level;
    private TextView airCondition;
    private TextView temperature,humidity;
    private String condition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_environment);
        registerMessageReceiver();


        initwidght();//初始化控件
    }

    private void initwidght() {
        back=findViewById(R.id.back);
        pm25=findViewById(R.id.pm25);
        level=findViewById(R.id.level);
        airCondition=findViewById(R.id.airCondition);
        temperature=findViewById(R.id.temperature);
        humidity=findViewById(R.id.humidity);

        back.setOnClickListener(clickListener);
        getData();
    }

    View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.back:
                    onBackPressed();
                    break;
            }
        }
    };


    @Override
    public void onBackPressed() {
        finish();
    }


    public void getData(){
        HttpClient.get("http://apicloud.mob.com/v1/weather/query?key=1a65fe6cfd250", "南昌", "江西"
                , new UICallback() {
                    @Override
                    public void process(String data) {
                        Log.e("TAG","data="+data);
                        Weather weather=new Gson().fromJson(data,Weather.class);
                        if(weather.getMsg().equals("success")){
                            condition=weather.getResult().get(0).getAirCondition();
                            pm25.setText(weather.getResult().get(0).getAirQuality().getPm25()+"");
                            temperature.setText(weather.getResult().get(0).getTemperature()+"");
                            humidity.setText(weather.getResult().get(0).getHumidity()+"");
                            level.setText(condition+"");

                            if(condition.equals("优")){
                                airCondition.setText("难得好空气，有时间就多参加户外活动");
                            }else if(condition.equals("良")){
                                airCondition.setText("可以正常在户外活动，易敏感人群应减少外出");
                            }else if(condition.equals("轻度污染")){
                                airCondition.setText("敏感人群应尽量减少体力消耗大的户外活动");
                            }else if(condition.equals("中度污染")){
                                airCondition.setText("应减少户外活动，外出时佩戴口罩，敏感人群应尽量避免外出");
                            }else if(condition.equals("重度污染")){
                                airCondition.setText("敏感人群应停留在室内，停止户外运动，一般人群尽量减少户外运动");
                            }else if(condition.equals("严重污染")){
                                airCondition.setText("敏感人群应留在室内，避免体力消耗，除有特殊需要的人群外，一般人群尽量不要停留在室外");
                            }else {
                                airCondition.setText("");
                            }
                        }else {
                            getData();
                        }
                    }

                    @Override
                    public void onError(String data) {
                        getData();
                    }
                });
    }

    @Override
    protected void onResume() {
        isForeground = true;
        isLogin();
        super.onResume();
    }

    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
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

    private void showdialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(EnvironmentActivity.this,1);
        builder.setTitle("提示");
        builder.setMessage("该账号已被其他设备登录");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startActivity(new Intent(EnvironmentActivity.this, LoginActivity.class));
                SharedPreferencesUtils.cleanPassword(EnvironmentActivity.this);
                MainActivity.instance.finish();
                EnvironmentmonitorActivity.instance.finish();
                finish();
            }
        });
        builder.create().show();
    }
}
