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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.massky.greenlandvland.R;
import com.massky.greenlandvland.View.ClearableEditText;
import com.massky.greenlandvland.common.CallBackInterface;
import com.massky.greenlandvland.common.CommonUtil;
import com.massky.greenlandvland.common.DialogThridUtils;
import com.massky.greenlandvland.common.GetToken;
import com.massky.greenlandvland.common.LocalBroadcastManager;
import com.massky.greenlandvland.common.SharedPreferencesUtils;
import com.massky.greenlandvland.common.ToastUtil;
import com.massky.greenlandvland.model.entity.Sc_isLoginNew;
import com.massky.greenlandvland.model.entity.Sc_setPwd;
import com.massky.greenlandvland.model.httpclient.HttpUrl.HttpClient;
import com.massky.greenlandvland.model.httpclient.UICallback;

import java.util.Timer;
import java.util.TimerTask;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class ChangePassWordActivity extends AppCompatActivity {
    private ImageView back;
    private LinearLayout ll1_changepassword,ll2_changepassword,ll3_changepassword,ll4_changepassword;
    private ClearableEditText et_oldpassword,et_newpassword,et_passwordagain,et_gettext;
    private Button btn_next1,btn_sure,btn_next2;
    private TextView tv_identify;
    private TextView tv_phonenumber;
    private TextView tv_title;
    private String token;
    private String projectCode;
    private int id=1;
    private String phonenumber;
    private TextView tv_text;
    private Button btn_relogin;
    private String oldPassword;
    private String newPassword;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass_word);
        token= SharedPreferencesUtils.getToken(this);
        projectCode=SharedPreferencesUtils.getProjectCode(this);
        registerMessageReceiver();
        SMSSDK.registerEventHandler(eh); //注册短信回调（记得销毁，避免泄露内存）

        init();//初始化控件
    }

    //初始化控件
    private void init() {
        //获取控件
        back= (ImageView) findViewById(R.id.back);
        ll1_changepassword= (LinearLayout) findViewById(R.id.ll1_changepassword);
        ll2_changepassword= (LinearLayout) findViewById(R.id.ll2_changepassword);
        ll3_changepassword= (LinearLayout) findViewById(R.id.ll3_changepassword);
        ll4_changepassword= (LinearLayout) findViewById(R.id.ll4_changepassword);
        et_oldpassword= (ClearableEditText) findViewById(R.id.et_oldpassword);
        et_newpassword= (ClearableEditText) findViewById(R.id.et_newpassword);
        et_passwordagain= (ClearableEditText) findViewById(R.id.et_passwordagain);
        et_gettext= (ClearableEditText) findViewById(R.id.et_gettext);
        btn_next1= (Button) findViewById(R.id.btn_next1);
        tv_identify= (TextView) findViewById(R.id.tv_identify);
        btn_sure= (Button) findViewById(R.id.btn_sure);
        btn_next2= (Button) findViewById(R.id.btn_next2);
        tv_text= findViewById(R.id.tv_text);
        tv_phonenumber= (TextView) findViewById(R.id.tv_phonenumber);
        tv_title= (TextView) findViewById(R.id.tv_title);
        btn_relogin=findViewById(R.id.btn_relogin);


        //添加监听
        back.setOnClickListener(clickListener);
        btn_next1.setOnClickListener(clickListener);
        tv_identify.setOnClickListener(clickListener);
        btn_sure.setOnClickListener(clickListener);
        btn_next2.setOnClickListener(clickListener);
        tv_text.setOnClickListener(clickListener);
        btn_relogin.setOnClickListener(clickListener);

        et_gettext.setKeyListener(DigitsKeyListener.getInstance("1234567890"));

        tv_text.setClickable(false);
    }

    View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.back:
                    onBackPressed();
                    break;
                case R.id.btn_next1:
                    if(CommonUtil.verifyPassword(et_oldpassword.getText().toString())==false){
                        ToastUtil.showToast(ChangePassWordActivity.this,"请输入6-16位密码");
                    }else if(SharedPreferencesUtils.getPassWord(ChangePassWordActivity.this).equals(et_oldpassword.getText().toString())==false){
                        ToastUtil.showToast(ChangePassWordActivity.this,"密码错误");
                    }else{
                        oldPassword=et_oldpassword.getText().toString()+"";
                        ll1_changepassword.setVisibility(View.GONE);
                        ll2_changepassword.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.tv_identify://手机验证
                    phonenumber=SharedPreferencesUtils.getMobilePhone(ChangePassWordActivity.this);
                    Log.e("TAG","phoneNumber="+phonenumber);
                    tv_phonenumber.setText("验证手机号"+ CommonUtil.getHintNumber(phonenumber).toString());

                    getCode();
                    break;
                case R.id.btn_sure:
                    if(CommonUtil.verifyPassword(et_newpassword.getText().toString())==false){
                        ToastUtil.showToast(ChangePassWordActivity.this,"请输入6-16位新密码");
                    }else if(CommonUtil.verifyPassword(et_passwordagain.getText().toString())==false){
                        ToastUtil.showToast(ChangePassWordActivity.this,"请再次输入6-16位新密码");
                    }else if(!et_passwordagain.getText().toString().equals(et_newpassword.getText().toString())){
                        ToastUtil.showToast(ChangePassWordActivity.this,"密码不一致");
                    }else{
                        newPassword=et_passwordagain.getText().toString()+"";
                        showchangedialog();
                    }
                    break;
                case R.id.btn_next2:
                    Log.e("TAG","country="+country);
                    Log.e("TAG","phonenumber="+phonenumber);
                    Log.e("TAG","yanzhengma="+et_gettext.getText().toString()+"");
                    SMSSDK.submitVerificationCode(country, phonenumber, et_gettext.getText().toString()+"");
                    break;
                case R.id.tv_text:
                    getCode();
                    break;
                case R.id.btn_relogin:
                    MainActivity.instance.finish();
                    finish();
                    startActivity(new Intent(ChangePassWordActivity.this,LoginActivity.class));
                    break;
            }
        }
    };

    //获取验证码
    private void getCode() {
        //  通过sdk发送短信验证（请求获取短信验证码，在监听（eh）中返回）
        SMSSDK.getVerificationCode(country, phonenumber);
        tm = new Timer();
        tt = new TimerTask() {
            @Override
            public void run() {
                hd.sendEmptyMessage(TIME--);
            }
        };
        tm.schedule(tt, 0, 1000);
    }

    private TimerTask tt;
    private Timer tm;
    private int TIME = 60;//倒计时60s这里应该多设置些因为mob后台需要60s,我们前端会有差异的建议设置90，100或者120
    public String country="86";//这是中国区号，如果需要其他国家列表，可以使用getSupportedCountries();获得国家区号
    private static final int CODE_REPEAT = 1; //重新发送
    Handler hd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == CODE_REPEAT) {
                tm.cancel();//取消任务
                tt.cancel();//取消任务
                TIME = 60;//时间重置
                tv_text.setText("重新发送验证码");
                tv_text.setTextColor(0xFF4cc9c3);
                tv_text.setClickable(true);
            } else {
                tv_text.setTextColor(0xFF666666);
                tv_text.setText( TIME + "秒后重新发送验证码");
                tv_text.setClickable(false);
            }
        }
    };
    //回调
    EventHandler eh = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            //回调完成
            if (result == SMSSDK.RESULT_COMPLETE) {
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    toast("验证成功");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            oldPassword=SharedPreferencesUtils.getPassWord(ChangePassWordActivity.this);
                            ll3_changepassword.setVisibility(View.GONE);
                            ll2_changepassword.setVisibility(View.VISIBLE);
                            tm.cancel();//取消任务
                            tt.cancel();//取消任务
                            TIME = 60;//时间重置
                        }
                    });
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {       //获取验证码成功
                    toast("获取验证码成功");
                    if(ll1_changepassword.isShown()){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv_title.setText("手机验证");
                                ll1_changepassword.setVisibility(View.GONE);
                                ll3_changepassword.setVisibility(View.VISIBLE);
                            }
                        });
                    }else {

                    }
                }
            } else {//错误等在这里（包括验证失败）
                //错误码请参照http://wiki.mob.com/android-api-错误码参考/这里我就不再继续写了
                ((Throwable) data).printStackTrace();
                String str = data.toString();
                Log.e("TAG","str="+str);
                str=str.substring(str.indexOf("\"")+1);
                str=str.substring(str.indexOf(":")+1);
                str=str.substring(0,str.indexOf(","));
                Log.e("TAG","str="+str);
                if(str.equals("476")|| str.equals("462")||str.equals("463")||str.equals("464")||str.equals("477")){
                    toast("验证码获取失败");
                }else if(str.equals("467")){
                    toast("验证失败");
                }else {
                    toast("验证码错误");
                }
            }
        }
    };

    //吐司的一个小方法
    private void toast(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ChangePassWordActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //销毁短信注册
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销回调接口registerEventHandler必须和unregisterEventHandler配套使用，否则可能造成内存泄漏。
        SMSSDK.unregisterEventHandler(eh);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    private void showchangedialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(ChangePassWordActivity.this,1);
        builder.setTitle("提示");
        builder.setMessage("确定修改密码吗？");
        builder.setCancelable(false);
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                changePassword();
            }
        });
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void changePassword() {
//        final String token=SharedPreferencesUtils.getToken(ChangePassWordActivity.this);
//                    final String projectCode=SharedPreferencesUtils.getProjectCode(ChangePassWordActivity.this);
        mDialog=DialogThridUtils.showWaitDialog(ChangePassWordActivity.this,true);
        HttpClient.post(CommonUtil.APPURL, "sc_setPwd", new Gson().toJson(new Sc_setPwd.SetPwdParams(token, oldPassword, newPassword, projectCode))
                , new UICallback() {
                    @Override
                    public void process(String data) {
//                                    Log.e("TAG","data="+data);
//                                    Log.e("TAG","token="+token);
//                                    Log.e("TAG","oldpassword="+oldPassword);
//                                    Log.e("TAG","newpassword="+newPassword);
//                                    Log.e("TAG","projectCode="+projectCode);
                        Sc_setPwd.SetPwdResult setPwdResult=new Gson().fromJson(data, Sc_setPwd.SetPwdResult.class);
                        int result=setPwdResult.getResult();
                        if(result==1){
                            Log.e("TAG","1-json解析错误");
                            id=1;
                            mHandler.sendEmptyMessage(1);
                        }else if(result==100){
                            Log.e("TAG","100-解析成功");
                            id=1;
                            SharedPreferencesUtils.cleanPassword(ChangePassWordActivity.this);
                            if(ll2_changepassword.isShown()){
                                ll2_changepassword.setVisibility(View.GONE);
                            }else if(ll3_changepassword.isShown()){
                                ll3_changepassword.setVisibility(View.GONE);
                            }

                            ll4_changepassword.setVisibility(View.VISIBLE);
                            tv_title.setText("修改成功");
                            mHandler.sendEmptyMessage(1);
                        }else if(result==101){
                            Log.e("TAG","101-token错误");
                            if(id==1){
                                id=id+1;
                                new GetToken(new CallBackInterface() {
                                    @Override
                                    public void gettoken(String str) {
                                        token=str;
                                        Log.e("TAG","token="+token);
                                        if(!TextUtils.isEmpty(token)){
                                            changePassword();
                                        }else {
                                            showerror();
                                        }
                                    }
                                },ChangePassWordActivity.this);
                            }else {
                                id=1;
                                showerror();
                                mHandler.sendEmptyMessage(1);
                            }
                        }else if(result==102){
                            Log.e("TAG","102-projectCode错误");
                            id=1;
                            ToastUtil.showToast(ChangePassWordActivity.this,"密码修改失败");
                            mHandler.sendEmptyMessage(1);
                        }else {
                            id=1;
                            ToastUtil.showToast(ChangePassWordActivity.this,"密码修改失败");
                            mHandler.sendEmptyMessage(1);

                        }
                    }

                    @Override
                    public void onError(String data) {
                        id=1;
                        ToastUtil.showToast(ChangePassWordActivity.this,"网络操作失败");
                        mHandler.sendEmptyMessage(1);
                    }
                });
    }

    private void showerror() {
        AlertDialog.Builder builder=new AlertDialog.Builder(ChangePassWordActivity.this,1);
        builder.setTitle("提示");
        builder.setMessage("服务器错误");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.create().show();
    }

    @Override
    public void onBackPressed() {
        if(ll4_changepassword.isShown()){
            exitTwice();
        }else if(ll3_changepassword.isShown()){
            ll3_changepassword.setVisibility(View.GONE);
            ll1_changepassword.setVisibility(View.VISIBLE);
            tv_title.setText("修改密码");
            tm.cancel();//取消任务
            tt.cancel();//取消任务
            TIME = 60;//时间重置
        }else if(ll2_changepassword.isShown()){
            ll2_changepassword.setVisibility(View.GONE);
            ll1_changepassword.setVisibility(View.VISIBLE);
        }else {
            finish();
        }
    }

    private boolean isFirstExit=true;//默认2秒内第一次按下退出
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
            MainActivity.instance.finish();
            finish();
        }
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
        AlertDialog.Builder builder=new AlertDialog.Builder(ChangePassWordActivity.this,1);
        builder.setTitle("提示");
        builder.setMessage("该账号已被其他设备登录");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startActivity(new Intent(ChangePassWordActivity.this, LoginActivity.class));
                SharedPreferencesUtils.cleanPassword(ChangePassWordActivity.this);
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
