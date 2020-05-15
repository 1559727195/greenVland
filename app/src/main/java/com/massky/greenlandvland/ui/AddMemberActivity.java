package com.massky.greenlandvland.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.massky.greenlandvland.R;
import com.massky.greenlandvland.View.ClearableEditText;
import com.massky.greenlandvland.common.CallBackInterface;
import com.massky.greenlandvland.common.CommonUtil;
import com.massky.greenlandvland.common.DialogThridUtils;
import com.massky.greenlandvland.common.GetToken;
import com.massky.greenlandvland.common.LocalBroadcastManager;
import com.massky.greenlandvland.common.PhoneFormatCheckUtils;
import com.massky.greenlandvland.common.SharedPreferencesUtils;
import com.massky.greenlandvland.common.ToastUtil;
import com.massky.greenlandvland.model.entity.Sc_addFamily;
import com.massky.greenlandvland.model.entity.Sc_getFamily;
import com.massky.greenlandvland.model.entity.Sc_isLoginNew;
import com.massky.greenlandvland.model.httpclient.HttpUrl.HttpClient;
import com.massky.greenlandvland.model.httpclient.UICallback;

import java.util.ArrayList;
import java.util.List;

public class AddMemberActivity extends AppCompatActivity {
    private ImageView iv_close;
    private ClearableEditText et_username,et_number;
    private Button bt_save;
    private String token;
    private String projectCode;
    private String mobilePhone;
    private String familyName;
    private String roomNo;
    private int id=1;
    private Dialog mDialog;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    DialogThridUtils.closeDialog(mDialog);
                    break;
            }
        }
    };
    private List<Sc_getFamily.GetFamilyResult.Family> familyLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);
        token= SharedPreferencesUtils.getToken(this);
        projectCode=SharedPreferencesUtils.getProjectCode(this);
        roomNo=SharedPreferencesUtils.getRoomNo(this);
        registerMessageReceiver();

        //获取控件
        iv_close= (ImageView) findViewById(R.id.iv_close);
        et_username= (ClearableEditText) findViewById(R.id.et_username);
        et_number= (ClearableEditText) findViewById(R.id.et_number);
        bt_save= (Button) findViewById(R.id.bt_save);
        //添加监听
        iv_close.setOnClickListener(clickListener);
        bt_save.setOnClickListener(clickListener);

        et_number.setKeyListener(DigitsKeyListener.getInstance("1234567890"));
    }
    View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_close:
                    onBackPressed();
                    break;
                case R.id.bt_save:

                    mobilePhone=et_number.getText().toString()+"";
                    familyName=et_username.getText().toString()+"";
                    if(familyName.length()<=0||familyName.toString()==null){
                        ToastUtil.showToast(AddMemberActivity.this,"请输入正确的姓名");
                    }else {
                        if (!(CommonUtil.verifyPhoneNumber(mobilePhone) &&  PhoneFormatCheckUtils.isChinaPhoneLegal(mobilePhone))) {
                            ToastUtil.showToast(AddMemberActivity.this, "请输入正确的手机号");
                        } else {
                            addFamily();
                        }
                    }
                    break;
            }
        }
    };

    private void addFamily() {
        mDialog=DialogThridUtils.showWaitDialog(AddMemberActivity.this,true);
        HttpClient.post(CommonUtil.APPURL, "sc_addFamily"
                , new Gson().toJson(new Sc_addFamily.AddFamilyParams(token, projectCode, mobilePhone, familyName))
                , new UICallback() {
                    @Override
                    public void process(String data) {
                                            Log.e("TAG", "data=" + data);
//                                            Log.e("TAG", "token=" + token);
//                                            Log.e("TAG", "projectCode=" + projectCode);
//                                            Log.e("TAG", "mobilePhone=" + mobilePhone);
//                                            Log.e("TAG", "familyName=" + familyName);
                        Sc_addFamily.AddFamilyResult addFamilyResult = new Gson().fromJson(data, Sc_addFamily.AddFamilyResult.class);
                        int result = addFamilyResult.getResult();
                        if (result == 1) {
                            id=1;
                            Log.e("TAG", "1-json解析错误");
                            mHandler.sendEmptyMessage(1);
                        } else if (result == 100) {
                            id=1;
                            Log.e("TAG", "100-成功");
//                            getFamily();
                            mHandler.sendEmptyMessage(1);
                            finish();
                        } else if (result == 101) {
                            Log.e("TAG", "101-token错误");
                            mHandler.sendEmptyMessage(1);
                            if(id==1){
                                id=id+1;
                                new GetToken(new CallBackInterface() {
                                    @Override
                                    public void gettoken(String str) {
                                        token=str;
                                        Log.e("TAG","token="+token);
                                        if(!TextUtils.isEmpty(token)){
                                            addFamily();
                                        }else {
                                            showerror();
                                        }
                                    }
                                },AddMemberActivity.this);
                            }else {
                                id=1;
                                showerror();
                            }
                        } else if (result == 102) {
                            id=1;
                            Log.e("TAG", "102-projectCode错误");
                            mHandler.sendEmptyMessage(1);
                        } else if (result == 103) {
                            id=1;
                            Log.e("TAG", "103-手机号码已存在");
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(AddMemberActivity.this, "手机号码已存在");
                        } else if(result==104){
                            id=1;
                            Log.e("TAG", "104- familyName重复");
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(AddMemberActivity.this, "姓名重复");
                        }else {
                            id=1;
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(AddMemberActivity.this,"操作失败");
                        }
                    }

                    @Override
                    public void onError(String data) {
                        id=1;
                        mHandler.sendEmptyMessage(1);
                        ToastUtil.showToast(AddMemberActivity.this,"操作失败");
                    }
                });
    }

//    private void getFamily() {
//        HttpClient.post(CommonUtil.APPURL, "sc_getFamily"
//                , new Gson().toJson(new Sc_getFamily.GetFamilyParams(token, projectCode, roomNo))
//                , new UICallback() {
//                    @Override
//                    public void process(String data) {
//                        Log.e("TAG","data="+data);
//                        Sc_getFamily.GetFamilyResult getFamilyResult=new Gson().fromJson(data, Sc_getFamily.GetFamilyResult.class);
//                        int result=getFamilyResult.getResult();
//                        if(result==1){
//                            Log.e("TAG","1-json解析错误");
//                            id=1;
//                            mHandler.sendEmptyMessage(1);
//                        }else if(result==100) {
//                            Log.e("TAG", "100-成功");
//                            id = 1;
//                            mHandler.sendEmptyMessage(1);
//                            familyLists = getFamilyResult.getFamilyList();
//                            SharedPreferencesUtils.saveFamily(AddMemberActivity.this,familyLists);
//                            finish();
//                        }else if(result==101){
//                            Log.e("TAG","101-token错误");
//                            mHandler.sendEmptyMessage(1);
//                            if(id==1){
//                                id=id+1;
//                                new GetToken(new CallBackInterface() {
//                                    @Override
//                                    public void gettoken(String str) {
//                                        token=str;
//                                        SharedPreferencesUtils.saveToken(AddMemberActivity.this,token);
//                                        Log.e("TAG","token="+token);
//                                        if(!TextUtils.isEmpty(token)){
//                                            getFamily();
//                                        }else {
//                                            showerror();
//                                        }
//                                    }
//                                },AddMemberActivity.this);
//                            }else {
//                                id=1;
//                                showerror();
//                            }
//                        }else if(result==102){
//                            Log.e("TAG","102-projectCode错误");
//                            mHandler.sendEmptyMessage(1);
//                            id=1;
//                            ToastUtil.showToast(AddMemberActivity.this,"操作失败");
//                        }else {
//                            mHandler.sendEmptyMessage(1);
//                            id=1;
//                            ToastUtil.showToast(AddMemberActivity.this,"操作失败");
//                        }
//                    }
//
//                    @Override
//                    public void onError(String data) {
//                        mHandler.sendEmptyMessage(1);
//                        id=1;
//                        Log.e("TAG","getfamily");
//                        ToastUtil.showToast(AddMemberActivity.this,"网络连接失败");
//                    }
//                });
//    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void showerror() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddMemberActivity.this, 1);
        builder.setTitle("提示");
        builder.setMessage("服务器错误");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
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
                        com.massky.greenlandvland.model.entity.Message message = new Gson().fromJson(extras, com.massky.greenlandvland.model.entity.Message.class);
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
        AlertDialog.Builder builder=new AlertDialog.Builder(AddMemberActivity.this,1);
        builder.setTitle("提示");
        builder.setMessage("该账号已被其他设备登录");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startActivity(new Intent(AddMemberActivity.this, LoginActivity.class));
                SharedPreferencesUtils.cleanPassword(AddMemberActivity.this);
                MainActivity.instance.finish();
                FamilyMemberActivity.instance.finish();
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
