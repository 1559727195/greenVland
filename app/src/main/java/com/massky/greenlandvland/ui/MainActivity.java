package com.massky.greenlandvland.ui;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.massky.greenlandvland.R;
import com.massky.greenlandvland.common.CommonUtil;
import com.massky.greenlandvland.common.LocalBroadcastManager;
import com.massky.greenlandvland.common.SharedPreferencesUtils;
import com.massky.greenlandvland.common.ToastUtil;
import com.massky.greenlandvland.model.entity.Message;
import com.massky.greenlandvland.model.entity.Sc_isLoginNew;
import com.massky.greenlandvland.model.httpclient.HttpUrl.HttpClient;
import com.massky.greenlandvland.model.httpclient.UICallback;
import com.massky.greenlandvland.ui.base.BaseActivity;

import java.util.List;

public class MainActivity extends BaseActivity {
    private FragmentMain fragmentMain;//添加首页页面
    private boolean isFirstExit=true;//默认2秒内第一次按下退出
    public static MainActivity instance=null;
    private Fragment_Tenement fragment_tenement;//物业页面
    private Fragment_Forum fragment_forum;//论坛页面
    private Fragment_Mine fragment_mine;//我的页面
    private Fragment_OpenRecord fragment_openRecord;//开门记录
    private Fragment_Detials fragment_detials;//详细资料页面

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance=this;
        registerMessageReceiver();

        //侧滑菜单的实现
        initSlidingMenu();
        //添加FragmentMain
        initFragmentmain();
    }

    private void initFragmentmain() {
        fragmentMain=new FragmentMain();
        //将fragmentMain添加到MainActivity中的R.id.layout_content容器中
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout_content,fragmentMain).commit();
    }

    @Override
    public void onBackPressed() {//监听回退键
        //当现实侧拉菜单时
        if(slidingMenu.isMenuShowing()){
            slidingMenu.showContent();//实现绑定的activity内容
        }else{//在2秒内连续按两次退出
            exitTwice();
        }
    }

    private void exitTwice() {
        if(isFirstExit){
            ToastUtil.showToast(this,"再按一次退出");
            isFirstExit=false;
            new Thread(new Runnable() {//构建一个子线程，阻塞2秒
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                        isFirstExit=true;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }else{
            finish();
        }
    }

    //首页页面
    public void showFragmentMain(){
        slidingMenu.showContent();//将MainActivity主页面先显示出来
        if(fragmentMain==null){//当引用为空 构建实例赋值给引用
            fragmentMain=new FragmentMain();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_content,fragmentMain).commit();
    }

    //物业页面
    public void showFragment_tenement(){
        slidingMenu.showContent();//将MainActivity主页面先显示出来
        if(fragment_tenement==null){//当引用为空 构建实例赋值给引用
            fragment_tenement=new Fragment_Tenement();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_content,fragment_tenement).commit();
    }

    //论坛页面
    public void showFragment_Forum(){
        slidingMenu.showContent();//将MainActivity主页面先显示出来
        if(fragment_forum==null){//当引用为空 构建实例赋值给引用
            fragment_forum=new Fragment_Forum();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_content,fragment_forum).commit();
    }

    //开门记录页面
    public void showFragment_OpenRecord(){
        slidingMenu.showContent();//将MainActivity主页面先显示出来
        if(fragment_openRecord==null){//当引用为空 构建实例赋值给引用
            fragment_openRecord=new Fragment_OpenRecord();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_content,fragment_openRecord).commit();
    }

    //我的页面
    public void showFragment_Mine(){
        slidingMenu.showContent();//将MainActivity主页面先显示出来
        if(fragment_mine==null){//当引用为空 构建实例赋值给引用
            fragment_mine=new Fragment_Mine();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_content,fragment_mine).commit();
    }

    //详细资料页面
    public void showFragment_Detials(){
        slidingMenu.showContent();//将MainActivity主页面先显示出来
        if(fragment_detials==null){//当引用为空 构建实例赋值给引用
            fragment_detials=new Fragment_Detials();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_content,fragment_detials).commit();
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
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this,1);
        builder.setTitle("提示");
        builder.setMessage("该账号已被其他设备登录");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                SharedPreferencesUtils.cleanPassword(MainActivity.this);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 获取到Activity下的Fragment
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments == null)
        {
            return;
        }
        // 查找在Fragment中onRequestPermissionsResult方法并调用
        for (Fragment fragment : fragments)
        {
            if (fragment != null)
            {
                // 这里就会调用我们Fragment中的onRequestPermissionsResult方法
                fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }
}
