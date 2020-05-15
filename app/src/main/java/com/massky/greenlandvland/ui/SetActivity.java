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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.massky.greenlandvland.R;
import com.massky.greenlandvland.common.CommonUtil;
import com.massky.greenlandvland.common.LocalBroadcastManager;
import com.massky.greenlandvland.common.SharedPreferencesUtils;
import com.massky.greenlandvland.model.entity.Message;
import com.massky.greenlandvland.model.entity.Sc_isLoginNew;
import com.massky.greenlandvland.model.httpclient.HttpUrl.HttpClient;
import com.massky.greenlandvland.model.httpclient.UICallback;

public class SetActivity extends AppCompatActivity {
    private ImageView back;//回退键
    private LinearLayout ll_retroaction,ll_notification,ll_cleanclash;
    //    private PopupWindow popupWindow;
//    private View view;
    private Button exit;
    public static SetActivity instance=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        instance=this;
        registerMessageReceiver();
        //获取控件
        back= (ImageView) findViewById(R.id.back);
        ll_retroaction= (LinearLayout) findViewById(R.id.ll_retroaction);
        ll_notification= (LinearLayout) findViewById(R.id.ll_notification);
        ll_cleanclash= (LinearLayout) findViewById(R.id.ll_cleanclash);
        exit= (Button) findViewById(R.id.exit);
        //添加监听
        back.setOnClickListener(clickListener);
        ll_retroaction.setOnClickListener(clickListener);
        ll_notification.setOnClickListener(clickListener);
        ll_cleanclash.setOnClickListener(clickListener);
        exit.setOnClickListener(clickListener);

//        view = View.inflate(SetActivity.this,R.layout.item_popup_cleanclash, null);
//        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);
    }

    View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.back://回退键
                    onBackPressed();
                    break;
                case R.id.ll_retroaction://反馈
                    startActivity(new Intent(SetActivity.this,RetroactionActivity.class));
                    break;
                case R.id.ll_notification://消息提醒
                    startActivity(new Intent(SetActivity.this,NotificationActivity.class));
                    break;
                case R.id.ll_cleanclash://清理缓存
                    showcleanclashdialog();

//                    //点击其他地方popupwindow消失
//                    popupWindow.setBackgroundDrawable(new BitmapDrawable());
//                    popupWindow.setFocusable(false);
//                    popupWindow.setOutsideTouchable(false);
//                    popupWindow.showAtLocation(view, Gravity.CENTER,0,0);
//
//                    final Button btn_cancel,btn_ensure;
//                    //获取控件
//                    btn_cancel= (Button) view.findViewById(R.id.btn_cancel);
//                    btn_ensure= (Button) view.findViewById(R.id.btn_ensure);
//                    btn_ensure.setSelected(true);
//                    //添加监听
//                    btn_cancel.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            btn_cancel.setSelected(true);
//                            btn_ensure.setSelected(false);
////                            popupWindow.dismiss();
//                        }
//                    });
//                    btn_ensure.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            btn_ensure.setSelected(true);
//                            btn_cancel.setSelected(false);
////                            popupWindow.dismiss();
//                        }
//                    });
                    break;
                case R.id.exit:
                    showexitdialog();
                    break;
            }
        }
    };

    private void showcleanclashdialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(SetActivity.this,3);
        builder.setTitle("提示");
        builder.setMessage("确定清理吗？");
        builder.setPositiveButton("取消",null);
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    private void showexitdialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(SetActivity.this,3);
        builder.setTitle("提示");
        builder.setMessage("是否确定退出");
        builder.setPositiveButton("取消",null);
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferencesUtils.cleanToken(SetActivity.this);
                SharedPreferencesUtils.cleanPassword(SetActivity.this);
                startActivity(new Intent(SetActivity.this,LoginActivity.class));
                MainActivity.instance.finish();
                finish();
            }
        });
        builder.create().show();
    }

    @Override
    public void onBackPressed() {
//        if (popupWindow.isShowing()){
//            popupWindow.dismiss();
//        }else {
        finish();
//        }

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

    private void showdialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(SetActivity.this,1);
        builder.setTitle("提示");
        builder.setMessage("该账号已被其他设备登录");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startActivity(new Intent(SetActivity.this, LoginActivity.class));
                SharedPreferencesUtils.cleanPassword(SetActivity.this);
                MainActivity.instance.finish();
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
