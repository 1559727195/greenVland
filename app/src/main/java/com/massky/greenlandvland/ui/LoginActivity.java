package com.massky.greenlandvland.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.massky.greenlandvland.R;
import com.massky.greenlandvland.View.ClearableEditText;
import com.massky.greenlandvland.common.CommonUtil;
import com.massky.greenlandvland.common.DialogThridUtils;
import com.massky.greenlandvland.common.MD5Utils;
import com.massky.greenlandvland.common.SharedPreferencesUtils;
import com.massky.greenlandvland.common.ToastUtil;
import com.massky.greenlandvland.model.entity.Sc_getTokenNew;
import com.massky.greenlandvland.model.entity.Sc_newLoginNew;
import com.massky.greenlandvland.model.httpclient.HttpUrl.HttpClient;
import com.massky.greenlandvland.model.httpclient.UICallback;

import cn.jpush.android.api.JPushInterface;

public class LoginActivity extends AppCompatActivity {
    private Button btn_login;
    private TextView tv_register, tv_forget;
    private ClearableEditText et_username, et_password;
    private static final int REQUEST_PHONE_STATE = 2;
    private String IMEI = null;
    private String token;
    private String giId;
    private String loginAccount;
    private String password;
    private String timeStamp;
    private String signature;
    private ImageView hintpassword, showpassword;
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
//    public static LoginActivity instance=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        instance=this;
        //获取控件
        initReveal();
        btn_login = (Button) findViewById(R.id.btn_login);
        tv_register = (TextView) findViewById(R.id.tv_register);
        tv_forget = (TextView) findViewById(R.id.tv_forget);
        et_username = (ClearableEditText) findViewById(R.id.et_username);
        et_password = (ClearableEditText) findViewById(R.id.et_password);
        hintpassword = (ImageView) findViewById(R.id.hintpassword);
        showpassword = (ImageView) findViewById(R.id.showpassword);


        giId = JPushInterface.getRegistrationID(LoginActivity.this);
        if (TextUtils.isEmpty(giId)){
            mDialog=DialogThridUtils.showWaitDialog(this,false);
            giId = JPushInterface.getRegistrationID(LoginActivity.this);
        }
        mHandler.sendEmptyMessage(1);
        IMEI = giId;
        Log.e("TAG", "giId=" + giId);


        if (!TextUtils.isEmpty(SharedPreferencesUtils.getLoginAccount(this)) && !TextUtils.isEmpty(SharedPreferencesUtils.getPassWord(this))) {
            et_username.setText(SharedPreferencesUtils.getLoginAccount(this));
            et_username.clearFocus();
            et_password.setText(SharedPreferencesUtils.getPassWord(this));
            et_password.requestFocus();
        } else if (!TextUtils.isEmpty(SharedPreferencesUtils.getLoginAccount(this)) && TextUtils.isEmpty(SharedPreferencesUtils.getPassWord(this))) {
            et_username.setText(SharedPreferencesUtils.getLoginAccount(this));
            et_username.clearFocus();
            et_password.setText("");
            et_password.requestFocus();
        } else {
            et_username.setText("");
            et_username.requestFocus();
        }
    }

    private void initReveal() {
        final ImageView iv_reveal = (ImageView) findViewById(R.id.iv_reveal);
        final RelativeLayout rl_login = (RelativeLayout) findViewById(R.id.rl_login);
        iv_reveal.post(new Runnable() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                final int width = iv_reveal.getMeasuredWidth();
                final int height = iv_reveal.getMeasuredHeight();
                final float radius = (float) Math.sqrt(width * width + height * height) / 2;//半径
                final Animator animator = ViewAnimationUtils.createCircularReveal(iv_reveal, width / 2, height / 2, 0, radius);
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        iv_reveal.setVisibility(View.GONE);
                        rl_login.setVisibility(View.VISIBLE);
                        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(rl_login, "translationY", 2000, 0);
                        objectAnimator.setDuration(1500);
                        objectAnimator.start();
                    }
                });
                animator.setDuration(1000);
                animator.start();
                //添加监听
                btn_login.setOnClickListener(clickListener);
                tv_register.setOnClickListener(clickListener);
                tv_forget.setOnClickListener(clickListener);
                hintpassword.setOnClickListener(clickListener);
                showpassword.setOnClickListener(clickListener);

            }
        });
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_login://登录键
                    loginAccount = et_username.getText().toString();
                    password = et_password.getText().toString();
                    timeStamp = CommonUtil.getDate();
                    if (TextUtils.isEmpty(loginAccount)) {
                        ToastUtil.showToast(LoginActivity.this, "请输入账号");
                    } else if (TextUtils.isEmpty(password)) {
                        ToastUtil.showToast(LoginActivity.this, "请输入密码");
                    } else {
                        getToken();
                    }
                    break;
                case R.id.tv_register://注册键
                    startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                    finish();
                    break;
                case R.id.tv_forget://忘记密码
                    startActivity(new Intent(LoginActivity.this, ForgetActivity.class));
                    finish();
                    break;
                case R.id.showpassword://显示密码
                    et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showpassword.setVisibility(View.GONE);
                    hintpassword.setVisibility(View.VISIBLE);
                    break;
                case R.id.hintpassword://隐藏密码
                    et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showpassword.setVisibility(View.VISIBLE);
                    hintpassword.setVisibility(View.GONE);
                    break;
            }
        }
    };

    private void getToken() {
        //登录操作
        mDialog = DialogThridUtils.showWaitDialog(LoginActivity.this, true);
        signature = MD5Utils.getMD5Str(loginAccount + password + timeStamp);
        HttpClient.post(CommonUtil.APPURL, "sc_getTokenNew"
                , new Gson().toJson(new Sc_getTokenNew.GetTokenNewParams(loginAccount, timeStamp, signature,3))
                , new UICallback() {
                    @Override
                    public void process(String data) {
//                                        Log.e("TAG","data="+data);
//                                        Log.e("TAG","timeStamp="+timeStamp);
//                                        Log.e("TAG","signature="+signature);
                        Sc_getTokenNew.GetTokenNewResult tokenResult = new Gson().fromJson(data, Sc_getTokenNew.GetTokenNewResult.class);
//                                        Sc_getToken.TokenResult tokenResult=new Gson().fromJson(data, new TypeToken<Sc_getToken.TokenResult>(){}.getType());
                        int result = tokenResult.getResult();
                        if (result == 1) {
                            Log.e("TAG", "1-json解析错误");
                            mHandler.sendEmptyMessage(1);
                        } else if (result == 100) {
                            Log.e("TAG", "tokenresult=100-成功");
                            SharedPreferencesUtils.saveLoginAccount(LoginActivity.this, loginAccount);
                            SharedPreferencesUtils.savePassWord(LoginActivity.this, password);

                            //登录的操作

//                                if (ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                                    // toast("需要动态获取权限");
//                                    ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_PHONE_STATE);
//                                } else {
//                                    // toast("不需要动态获取权限");
//                                    TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
//                                    IMEI = TelephonyMgr.getDeviceId();
//                                }

                            token = tokenResult.getToken();
                            SharedPreferencesUtils.saveToken(LoginActivity.this, tokenResult.getToken());
                            Login();
                        } else if (result == 101) {
                            Log.d("TAG", "result=101-失败,账号或密码错误");
                            ToastUtil.showToast(LoginActivity.this, "账号或密码错误");
                            mHandler.sendEmptyMessage(1);
                        } else if (result == 102) {
                            Log.e("TAG", "result=102签名错误");
                            ToastUtil.showToast(LoginActivity.this, "账号或密码错误,或该账号不存在");
                            mHandler.sendEmptyMessage(1);
                        } else if (result == 103) {
                            Log.e("TAG", "result=103时间戳错误");
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(LoginActivity.this,"登录失败");
                        }else if(result==104){
                            Log.e("TAG", "result=104appCode不正确");
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(LoginActivity.this,"登录失败");
                        }else {
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(LoginActivity.this,"登录失败");
                        }
                    }

                    @Override
                    public void onError(String data) {
                        ToastUtil.showToast(LoginActivity.this, "网络连接异常");
                        mHandler.sendEmptyMessage(1);
                    }
                });

    }

    //登录
    private void Login() {
        HttpClient.post(CommonUtil.APPURL, "sc_newLoginNew"
                , new Gson().toJson(new Sc_newLoginNew.NewLoginNewParams(token, giId, IMEI,3))
                , new UICallback() {
                    @Override
                    public void process(String data) {
//                                                            Log.e("TAG","login="+data);
//                                                            Log.e("TAG","token="+token);
//                                                            Log.e("TAG","giId="+giId);
//                                                            Log.e("TAG","IMEI="+IMEI);
                        Sc_newLoginNew.NewLoginNewResult loginResult = new Gson().fromJson(data, Sc_newLoginNew.NewLoginNewResult.class);
//                                                            Sc_login.LoginResult loginResult=new Gson().fromJson(data, new TypeToken<Sc_login.LoginResult>(){}.getType());
//                                                            Log.e("TAG","phoneNumber="+loginResult.getPhoneNumber());
//                                                            Log.e("TAG","accountType="+loginResult.getAccountType());
//                                                            Log.e("TAG","avatar="+loginResult.getAvatar());
//                                                            Log.e("TAG","userName="+loginResult.getUserName());
                        int result = loginResult.getResult();
                        if (result == 1) {
                            Log.e("TAG", "1-解析错误");
                            mHandler.sendEmptyMessage(1);
                        } else if (result == 100) {
                            Log.e("TAG", "100-成功");
//                            SharedPreferencesUtils.saveLogin(LoginActivity.this, loginResult);
                            SharedPreferencesUtils.savePhoneNumber(LoginActivity.this,loginResult.getPhoneNumber());
                            SharedPreferencesUtils.saveAvatar(LoginActivity.this, loginResult.getAvatar());
                            SharedPreferencesUtils.saveUserName(LoginActivity.this,loginResult.getUserName());
//                            SharedPreferencesUtils.saveProjectList(LoginActivity.this, loginResult.getProjectList());
                            SharedPreferencesUtils.saveProjectRoomType(LoginActivity.this,loginResult.getProjectRoomType());
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            mHandler.sendEmptyMessage(1);
                            finish();

                        } else if (result == 101) {
                            Log.e("TAG", "101-token错误");
                            mHandler.sendEmptyMessage(1);
                        }else {
                            mHandler.sendEmptyMessage(1);
                        }
                    }

                    @Override
                    public void onError(String data) {
                        ToastUtil.showToast(LoginActivity.this, "网络连接异常");
                        mHandler.sendEmptyMessage(1);
                    }
                });

    }
}
