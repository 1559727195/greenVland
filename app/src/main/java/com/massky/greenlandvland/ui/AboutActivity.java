package com.massky.greenlandvland.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.massky.greenlandvland.R;
import com.massky.greenlandvland.common.CallBackInterface;
import com.massky.greenlandvland.common.CommonUtil;
import com.massky.greenlandvland.common.DialogThridUtils;
import com.massky.greenlandvland.common.GetToken;
import com.massky.greenlandvland.common.LocalBroadcastManager;
import com.massky.greenlandvland.common.SharedPreferencesUtils;
import com.massky.greenlandvland.common.ToastUtil;
import com.massky.greenlandvland.model.entity.Sc_getVersionNew;
import com.massky.greenlandvland.model.entity.Sc_isLoginNew;
import com.massky.greenlandvland.model.httpclient.HttpUrl.HttpClient;
import com.massky.greenlandvland.model.httpclient.UICallback;
import com.massky.greenlandvland.service.DownLoadService;
import com.tbruyelle.rxpermissions.RxPermissions;

public class AboutActivity extends AppCompatActivity {
    private ImageView back;//回退键
    private LinearLayout ll_developer;//开发人员页面
    private LinearLayout secret;
    private String token;
    private int id=1;
    private int newversionCode;
    private String newversion;
    private int versionCode;
    private String versions;
    private TextView version;
    private ImageView iv_version;
    private LinearLayout check;//检查更新


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
    public static AboutActivity instance=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        instance=this;
        token= SharedPreferencesUtils.getToken(this);
        Log.e("TAG","token="+token);
        registerMessageReceiver();
        //获取控件
        back= (ImageView) findViewById(R.id.back);
        ll_developer= (LinearLayout) findViewById(R.id.ll_developer);
        secret= (LinearLayout) findViewById(R.id.secret);
        version= (TextView) findViewById(R.id.version);
        iv_version=findViewById(R.id.iv_version);
        check=findViewById(R.id.check);
        //添加监听
        back.setOnClickListener(clickListener);
        ll_developer.setOnClickListener(clickListener);
        secret.setOnClickListener(clickListener);
        check.setOnClickListener(clickListener);
    }
    View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.back://回退键
                    onBackPressed();
                    break;
                case R.id.ll_developer://开发人员
                    startActivity(new Intent(AboutActivity.this,DeveloperActivity.class));
                    break;
                case R.id.secret://隐私声明
                    startActivity(new Intent(AboutActivity.this,SecretActivity.class));
                    break;
                case R.id.check://更新
                    if(newversionCode>versionCode){
                        AlertDialog.Builder builder = new AlertDialog.Builder(AboutActivity.this, 1);
                        builder.setTitle("提示");
                        builder.setMessage("是否升级到绿地朝阳中心V" + newversion + "版本");
                        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                download();
                            }
                        });
                        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                    }else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AboutActivity.this, 1);
                        builder.setTitle("提示");
                        builder.setMessage("当前已是最新版本");
                        builder.setPositiveButton("确定", null);
                        builder.create().show();
                    }
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        finish();
    }

    //下载
    private void download() {
        Intent service = new Intent(AboutActivity.this, DownLoadService.class);
        service.putExtra("downloadurl", "https://massky-download.oss-cn-hangzhou.aliyuncs.com/chaoyang" + newversion + ".apk");
        Log.e("TAG", "version111=" + newversion);
        RxPermissions.getInstance(AboutActivity.this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(granted -> {
            if (granted) {
                Toast.makeText(AboutActivity.this, "正在下载中", Toast.LENGTH_SHORT).show();
                startService(service);
            } else {
                Toast.makeText(AboutActivity.this, "SD卡下载权限被拒绝", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static int packageCode(Context context) {
        PackageManager manager = context.getPackageManager();
        int code = 0;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            code = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return code;
    }

    public static String packageName(Context context) {
        PackageManager manager = context.getPackageManager();
        String name = null;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            name = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return name;
    }

    private void getVersion() {
        mDialog=DialogThridUtils.showWaitDialog(AboutActivity.this,true);
        HttpClient.post(CommonUtil.APPURL, "sc_getVersionNew"
                , new Gson().toJson(new Sc_getVersionNew.GetVersionNewParams(token,3))
                , new UICallback() {
                    @Override
                    public void process(String data) {
                        Log.e("TAG","data="+data);
                        Sc_getVersionNew.GetVersionNewResult getVersionResult=new Gson().fromJson(data, Sc_getVersionNew.GetVersionNewResult.class);
                        int result=getVersionResult.getResult();
                        if(result==1){
                            id=1;
                            mHandler.sendEmptyMessage(1);
                            Log.e("TAG","1-json解析错误");
                        }else if(result==100){
                            id=1;
                            mHandler.sendEmptyMessage(1);
                            newversionCode=getVersionResult.getVersionCode();
                            newversion=getVersionResult.getVersion();
                            Log.e("TAG","100-成功");
                            if(newversionCode>versionCode){
                                iv_version.setVisibility(View.VISIBLE);
                            }else {
                                iv_version.setVisibility(View.GONE);
                            }
                        }else if(result==101){
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
                                            getVersion();
                                        }else {
                                            showerror();
                                        }
                                    }
                                },AboutActivity.this);
                            }else {
                                id=1;
                                showerror();
                            }
                        }else {
                            id=1;
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(AboutActivity.this,"操作失败");
                        }

                    }

                    @Override
                    public void onError(String data) {
                        id=1;
                        mHandler.sendEmptyMessage(1);
                        Log.e("TAG","网络操作失败");
                        ToastUtil.showToast(AboutActivity.this,"网络操作失败");
                    }
                });
    }

    private void showerror() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AboutActivity.this, 1);
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
        versionCode=packageCode(this);
        versions=packageName(this);
        Log.e("TAG","versionCode="+versionCode);
        Log.e("TAG","versions="+versions);
        version.setText("绿地朝阳中心V"+versions);

        getVersion();
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
        AlertDialog.Builder builder=new AlertDialog.Builder(AboutActivity.this,1);
        builder.setTitle("提示");
        builder.setMessage("该账号已被其他设备登录");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startActivity(new Intent(AboutActivity.this, LoginActivity.class));
                SharedPreferencesUtils.cleanPassword(AboutActivity.this);
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
