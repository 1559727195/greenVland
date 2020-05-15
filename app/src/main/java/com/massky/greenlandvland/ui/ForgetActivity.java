package com.massky.greenlandvland.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.massky.greenlandvland.R;
import com.massky.greenlandvland.common.CallBackInterface;
import com.massky.greenlandvland.common.CommonUtil;
import com.massky.greenlandvland.common.DialogThridUtils;
import com.massky.greenlandvland.common.GetToken;
import com.massky.greenlandvland.common.MD5Utils;
import com.massky.greenlandvland.common.PhoneFormatCheckUtils;
import com.massky.greenlandvland.common.SharedPreferencesUtils;
import com.massky.greenlandvland.common.ToastUtil;
import com.massky.greenlandvland.model.entity.Sc_checkMobilePhoneNew;
import com.massky.greenlandvland.model.entity.Sc_getTokenNew;
import com.massky.greenlandvland.model.entity.Sc_newLoginNew;
import com.massky.greenlandvland.model.entity.Sc_updatePwdNew;
import com.massky.greenlandvland.model.httpclient.HttpUrl.HttpClient;
import com.massky.greenlandvland.model.httpclient.UICallback;

import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class ForgetActivity extends AppCompatActivity {
    private Spinner spinner;//下拉框
    private ImageView img_back;//回退键
    private TextView tv_next,tv_next2;//下一步
    RelativeLayout r1_forget,r2_forget,r3_forget,r4_forget;
    private EditText et_1,et_2,et_3,et_4;//四个edittext
    private Button btn_login;//登录按钮
    private EditText et_number,et_password;
    Boolean isNull=true;
    private TextView tv_text;

    private String IMEI = null;
    private String token;
    private String giId;
    private int id = 1;
    private String loginAccount;
    private String password;
    private String timeStamp;
    private String signature;

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
        setContentView(R.layout.activity_forget);

        giId = JPushInterface.getRegistrationID(ForgetActivity.this);
        while (TextUtils.isEmpty(giId)) {
            mDialog = DialogThridUtils.showWaitDialog(this, false);
            giId = JPushInterface.getRegistrationID(ForgetActivity.this);
        }
        mHandler.sendEmptyMessage(1);
        IMEI = giId;
        SMSSDK.registerEventHandler(eh); //注册短信回调（记得销毁，避免泄露内存）
        initSpinner();
        init();//初始化控件
    }

    //初始化控件
    private void init() {
        //获取控件
        spinner= findViewById(R.id.spinner);
        img_back= findViewById(R.id.img_back);
        r1_forget= findViewById(R.id.r1_forget);
        r2_forget= findViewById(R.id.r2_forget);
        r3_forget= findViewById(R.id.r3_forget);
        r4_forget= findViewById(R.id.r4_forget);
        tv_next= findViewById(R.id.tv_next);
        et_1= findViewById(R.id.et_1);
        et_2= findViewById(R.id.et_2);
        et_3= findViewById(R.id.et_3);
        et_4= findViewById(R.id.et_4);
        tv_next2= findViewById(R.id.tv_next2);
        btn_login= findViewById(R.id.btn_login);
        et_number= findViewById(R.id.et_number);
        et_password= findViewById(R.id.et_password);
        tv_text= findViewById(R.id.tv_text);

        et_number.setKeyListener(DigitsKeyListener.getInstance("1234567890"));

        if(et_1.getText().length()==0&&et_2.getText().length()==0&&et_3.getText().length()==0&&et_4.getText().length()==0){
            et_1.setFocusable(true);
            et_2.setFocusable(false);
            et_3.setFocusable(false);
            et_4.setFocusable(false);
        }


        //添加监听
        img_back.setOnClickListener(clickListener);
        r1_forget.setOnClickListener(clickListener);
        r2_forget.setOnClickListener(clickListener);
        r3_forget.setOnClickListener(clickListener);
        r4_forget.setOnClickListener(clickListener);
        tv_next.setOnClickListener(clickListener);
        et_1.addTextChangedListener(mTextWatcher);
        et_2.addTextChangedListener(mTextWatcher);
        et_3.addTextChangedListener(mTextWatcher);
        et_4.addTextChangedListener(mTextWatcher);
        tv_next2.setOnClickListener(clickListener);
        btn_login.setOnClickListener(clickListener);
        tv_text.setOnClickListener(clickListener);

        tv_text.setClickable(false);

        //注册手机号下一步字进行判断
        et_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length=s.toString().length();
                if(length==11){
                    tv_next.setTextColor(Color.parseColor("#52CBC6"));
                }else {
                    tv_next.setTextColor(Color.parseColor("#6C6C6C"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //将光标移动到末尾
                et_number.setSelection(et_number.getText().toString().length());
            }
        });

        //注册密码下一步字进行判断
        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(CommonUtil.verifyPassword(s.toString())){
                    tv_next2.setTextColor(Color.parseColor("#52CBC6"));
                }else {
                    tv_next2.setTextColor(Color.parseColor("#6C6C6C"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //将光标移动到末尾
                et_password.setSelection(et_password.getText().toString().length());
            }
        });
    }

    View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.img_back://回退键
                    onBackPressed();
                    break;
                case R.id.tv_next://下一步
                    if(!(CommonUtil.verifyPhoneNumber(et_number.getText().toString()) &&  PhoneFormatCheckUtils.isChinaPhoneLegal(et_number.getText().toString()))){
                        ToastUtil.showToast(ForgetActivity.this,"请输入正确的手机号");
                    }else{
                        mDialog=DialogThridUtils.showWaitDialog(ForgetActivity.this,true);
                        HttpClient.post(CommonUtil.APPURL, "sc_checkMobilePhoneNew"
                                , new Gson().toJson(new Sc_checkMobilePhoneNew.CheckMobilePhoneNewParams(et_number.getText().toString(),3))
                                , new UICallback() {
                                    @Override
                                    public void process(String data) {
                                        Log.e("TAG","data="+data);
                                        Log.e("TAG","phoneNumber="+et_number.getText().toString());
                                        Sc_checkMobilePhoneNew.CheckMobilePhoneNewResult checkMobilePhoneResult=new Gson().fromJson(data, Sc_checkMobilePhoneNew.CheckMobilePhoneNewResult.class);
                                        int result=checkMobilePhoneResult.getResult();
                                        if(result==1){
                                            Log.e("TAG","1-json解析失败");
                                            mHandler.sendEmptyMessage(1);
                                        }else if(result==100){
                                            Log.e("TAG","100-成功");
                                            ToastUtil.showToast(ForgetActivity.this,"操作失败");
                                            mHandler.sendEmptyMessage(1);
                                        }else if(result==101){
                                            Log.e("TAG","101");
                                            ToastUtil.showToast(ForgetActivity.this,"号码未注册");
                                            mHandler.sendEmptyMessage(1);
                                        }else if(result==103){
                                            Log.e("TAG","103-手机号码已存在");
                                            getCode();
                                        }else {
                                            ToastUtil.showToast(ForgetActivity.this,"操作失败");
                                            mHandler.sendEmptyMessage(1);
                                        }
                                    }

                                    @Override
                                    public void onError(String data) {
                                        ToastUtil.showToast(ForgetActivity.this,"网络操作失败");
                                        mHandler.sendEmptyMessage(1);
                                    }
                                });
                    }
                    break;
                case R.id.tv_next2://确认密码
                    if(CommonUtil.verifyPassword(et_password.getText().toString())==false){
                        ToastUtil.showToast(ForgetActivity.this,"请输入6-16位密码");
                    }else{
                        //更新密码操作
                        HttpClient.post(CommonUtil.APPURL, "sc_updatePwdNew"
                                , new Gson().toJson(new Sc_updatePwdNew.UpdatePwdNewParams(et_number.getText().toString(), et_password.getText().toString(),3))
                                , new UICallback() {
                                    @Override
                                    public void process(String data) {
                                        Log.e("TAG","updatePwddata="+data);
                                        Log.e("TAG","mobilePhone="+et_number.getText().toString());
                                        Log.e("TAG","newPed="+et_password.getText().toString());
                                        Sc_updatePwdNew.UpdatePwdNewResult updatePwdResult=new Gson().fromJson(data, Sc_updatePwdNew.UpdatePwdNewResult.class);
                                        int result=updatePwdResult.getResult();
                                        if(result==1){
                                            Log.e("TAG","1-解析错误");
                                            ToastUtil.showToast(ForgetActivity.this,"修改错误");
                                        }else if(result==100){
                                            Log.e("TAG","100-成功");
                                            r3_forget.setVisibility(View.GONE);
                                            r4_forget.setVisibility(View.VISIBLE);
                                        }else if(result==103){
                                            Log.e("TAG","103-手机号不存在");
                                            ToastUtil.showToast(ForgetActivity.this,"修改错误");
                                        }else {
                                            ToastUtil.showToast(ForgetActivity.this,"修改错误");
                                        }
                                    }

                                    @Override
                                    public void onError(String data) {
                                        ToastUtil.showToast(ForgetActivity.this,"修改错误");
                                    }
                                });
                    }
                    break;
                case R.id.btn_login://立即登录按钮
                    loginAccount = et_number.getText().toString();
                    password = et_password.getText().toString();
                    timeStamp = CommonUtil.getDate();
                    getToken();
                    break;
                case R.id.tv_text:
                    getCode();
                    break;
            }
        }
    };


    private void getCode() {
        //  通过sdk发送短信验证（请求获取短信验证码，在监听（eh）中返回）
        SMSSDK.getVerificationCode(country, et_number.getText().toString());
        tm = new Timer();
        tt = new TimerTask() {
            @Override
            public void run() {
                hd.sendEmptyMessage(TIME--);
            }
        };
        tm.schedule(tt,0,1000);
    }

    private void getToken() {
        //登录操作
        mDialog = DialogThridUtils.showWaitDialog(ForgetActivity.this, true);
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
                            SharedPreferencesUtils.saveLoginAccount(ForgetActivity.this, loginAccount);
                            SharedPreferencesUtils.savePassWord(ForgetActivity.this, password);

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
                            SharedPreferencesUtils.saveToken(ForgetActivity.this, tokenResult.getToken());
                            Login();
                        } else if (result == 101) {
                            Log.d("TAG", "result=101-失败,账号或密码错误");
                            ToastUtil.showToast(ForgetActivity.this, "账号或密码错误");
                            mHandler.sendEmptyMessage(1);
                        } else if (result == 102) {
                            Log.e("TAG", "result=102签名错误");
                            ToastUtil.showToast(ForgetActivity.this, "账号或密码错误,或该账号不存在");
                            mHandler.sendEmptyMessage(1);
                        } else if (result == 103) {
                            Log.e("TAG", "result=103时间戳错误");
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(ForgetActivity.this,"登录失败");
                        }else if(result==104){
                            Log.e("TAG", "result=104appCode错误");
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(ForgetActivity.this,"登录失败");
                        }else {
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(ForgetActivity.this,"登录失败");
                        }
                    }

                    @Override
                    public void onError(String data) {
                        ToastUtil.showToast(ForgetActivity.this, "网络连接异常");
                        mHandler.sendEmptyMessage(1);
                    }
                });
    }

    private void Login() {
//        mDialog = DialogThridUtils.showWaitDialog(ForgetActivity.this, true);
        HttpClient.post(CommonUtil.APPURL, "sc_newLoginNew"
                , new Gson().toJson(new Sc_newLoginNew.NewLoginNewParams(token, giId, IMEI, 3))
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
                            id = 1;
                        } else if (result == 100) {
                            Log.e("TAG", "100-成功");
                            id = 1;
//                            SharedPreferencesUtils.saveLogin(LoginActivity.this, loginResult);
                            SharedPreferencesUtils.savePhoneNumber(ForgetActivity.this, loginResult.getPhoneNumber());
                            SharedPreferencesUtils.saveAvatar(ForgetActivity.this, loginResult.getAvatar());
                            SharedPreferencesUtils.saveUserName(ForgetActivity.this, loginResult.getUserName());
//                            SharedPreferencesUtils.saveProjectList(LoginActivity.this, loginResult.getProjectList());
                            SharedPreferencesUtils.saveProjectRoomType(ForgetActivity.this, loginResult.getProjectRoomType());
                            startActivity(new Intent(ForgetActivity.this, MainActivity.class));
                            mHandler.sendEmptyMessage(1);
//                            LoginActivity.instance.finish();
                            finish();

                        } else if (result == 101) {
                            Log.e("TAG", "101-token错误");
                            if (id == 1) {
                                id = id + 1;
                                new GetToken(new CallBackInterface() {
                                    @Override
                                    public void gettoken(String str) {
                                        token = str;
                                        Log.e("TAG", "token=" + token);
                                        if (!TextUtils.isEmpty(token)) {
                                            Login();
                                        } else {
                                            showerror();
                                        }
                                    }
                                }, ForgetActivity.this);
                            } else {
                                id = 1;
                                showerror();
                            }
                        } else {
                            id = 1;
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(ForgetActivity.this, "注册失败");
                        }
                    }

                    @Override
                    public void onError(String data) {
                        id = 1;
                        ToastUtil.showToast(ForgetActivity.this, "网络连接异常");
                        mHandler.sendEmptyMessage(1);
                    }
                });
    }

    private void showerror() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ForgetActivity.this, 1);
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

    private void initSpinner() {
        spinner= findViewById(R.id.spinner);
        int layout=android.R.layout.simple_spinner_dropdown_item;
        String data[]=new String[]{"+86"};
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,layout,data);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                country= (String) spinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }



    TextWatcher mTextWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.length()==1) {
                if (et_1.isFocusable()) {
                    et_2.setFocusable(true);
                    et_2.setFocusableInTouchMode(true);
                } else if (et_2.isFocusable()) {
                    et_3.setFocusable(true);
                    et_3.setFocusableInTouchMode(true);
                } else if (et_3.isFocusable()) {
                    et_4.setFocusable(true);
                    et_4.setFocusableInTouchMode(true);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if(s.toString().length()==1){
                if(et_1.isFocused()){
                    et_1.setFocusable(false);
                    et_2.requestFocus();
                }else if(et_2.isFocused()){
                    et_2.setFocusable(false);
                    et_3.requestFocus();
                }else if(et_3.isFocused()){
                    et_3.setFocusable(false);
                    et_4.requestFocus();
                }else if(et_4.isFocused()){
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(et_4.getWindowToken(), 0);
//                    getEditNumber();//获取验证码
                    SMSSDK.submitVerificationCode(country,et_number.getText().toString(),getEditNumber());

                }
            }
        }
    };


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_DEL) {
            if(et_4.isFocused()){
                if (!et_4.getText().toString().equals("")) {
                    et_4.getText().clear();
                    et_4.requestFocus();
                    isNull = false;
                } else if (!isNull) {
                    et_4.clearFocus();
                    et_4.setFocusable(false);
                    et_3.setFocusableInTouchMode(true);
                    et_3.getText().clear();
                    et_3.requestFocus();
                    isNull = true;
                } else {
                    et_4.getText().clear();
                    et_4.requestFocus();
                    isNull = false;
                }
            } else if (et_3.isFocused()) {
                et_3.clearFocus();
                et_3.setFocusable(false);
                et_2.setFocusableInTouchMode(true);
                et_2.getText().clear();
                et_2.requestFocus();
            } else if (et_2.isFocused()) {
                et_2.clearFocus();
                et_2.setFocusable(false);
                et_1.setFocusableInTouchMode(true);
                et_1.getText().clear();
                et_1.requestFocus();
            }
        }

        if(keyCode == KeyEvent.KEYCODE_BACK){
            onBackPressed();
        }
        return false;

    }

    //获取验证码
    public String getEditNumber() {
        String number = et_1.getText().toString();
        number += et_2.getText().toString();
        number += et_3.getText().toString();
        number += et_4.getText().toString();
        Log.e("TAG", "number="+number);
        return number;
    }

    //监听物理回退键
    @Override
    public void onBackPressed() {
        if (r2_forget.isShown()){
            r2_forget.setVisibility(View.GONE);
            r1_forget.setVisibility(View.VISIBLE);
            tm.cancel();//取消任务
            tt.cancel();//取消任务
            TIME = 60;//时间重置
        }else if(r3_forget.isShown()){
            r3_forget.setVisibility(View.GONE);
            r2_forget.setVisibility(View.VISIBLE);
        }else if(r4_forget.isShown()){
            r4_forget.setVisibility(View.GONE);
            r3_forget.setVisibility(View.VISIBLE);
        }else{
            startActivity(new Intent(ForgetActivity.this,LoginActivity.class));
            finish();
        }
    }

    private TimerTask tt;
    private Timer tm;
    private int TIME = 60;//倒计时60s这里应该多设置些因为mob后台需要60s,我们前端会有差异的建议设置90，100或者120
    public String country;//这是中国区号，如果需要其他国家列表，可以使用getSupportedCountries();获得国家区号
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
            }else {
                tv_text.setTextColor(0xFF666666);
                tv_text.setText(et_number.getText().toString() + "  " + TIME + "秒后重新发送验证码");
                tv_text.setClickable(false);
            }
        }
    };

    //回调
    EventHandler eh=new EventHandler(){
        @Override
        public void afterEvent(int event, int result, Object data) {
            //回调完成
            if (result == SMSSDK.RESULT_COMPLETE) {
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    toast("验证成功");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            r2_forget.setVisibility(View.GONE);
                            r3_forget.setVisibility(View.VISIBLE);
                            tm.cancel();//取消任务
                            tt.cancel();//取消任务
                            TIME = 60;//时间重置
                        }
                    });
                }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){       //获取验证码成功
                    toast("获取验证码成功");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(r1_forget.isShown()){
                                mHandler.sendEmptyMessage(1);
                                r1_forget.setVisibility(View.GONE);
                                r2_forget.setVisibility(View.VISIBLE);
                            }else {

                            }
                        }
                    });

                }
            }else{//错误等在这里（包括验证失败）
                //错误码请参照http://wiki.mob.com/android-api-错误码参考/这里我就不再继续写了
                ((Throwable)data).printStackTrace();
                String str = data.toString();
                Log.e("TAG","str="+str);
                mHandler.sendEmptyMessage(1);
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
                Toast.makeText(ForgetActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }
    //销毁短信注册
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销回调接口registerEventHandler必须和unregisterEventHandler配套使用，否则可能造成内存泄漏。
        SMSSDK.unregisterEventHandler(eh);
    }
}
