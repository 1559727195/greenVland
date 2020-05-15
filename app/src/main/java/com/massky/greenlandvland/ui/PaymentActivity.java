package com.massky.greenlandvland.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.massky.greenlandvland.R;
import com.massky.greenlandvland.common.CallBackInterface;
import com.massky.greenlandvland.common.CommonUtil;
import com.massky.greenlandvland.common.DialogThridUtils;
import com.massky.greenlandvland.common.GetToken;
import com.massky.greenlandvland.common.LocalBroadcastManager;
import com.massky.greenlandvland.common.SharedPreferencesUtils;
import com.massky.greenlandvland.common.ToastUtil;
import com.massky.greenlandvland.model.entity.Message;
import com.massky.greenlandvland.model.entity.Sc_addPayRecord;
import com.massky.greenlandvland.model.entity.Sc_getPayRecord;
import com.massky.greenlandvland.model.entity.Sc_isLoginNew;
import com.massky.greenlandvland.model.httpclient.HttpUrl.HttpClient;
import com.massky.greenlandvland.model.httpclient.UICallback;
import com.massky.greenlandvland.ui.adapter.PayAdapter;

import java.util.List;

public class PaymentActivity extends AppCompatActivity {
    private ImageView back;//回退键
    private Button water,electric,gas,tingche,wuye;
    private String token;
    private String projectCode;
    private String roomNo;
    private Button bt_pay;
    private EditText et_pay;
    private SpannableString ss;
    private AbsoluteSizeSpan ass;
    private int type;
    private String message="";
    private String money;

    private int id=1;
    private Dialog mDialog;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    DialogThridUtils.closeDialog(mDialog);
                    break;
            }
        }
    };
    private PayAdapter adapter;
    private List<Sc_getPayRecord.GetPayRecordResult.PayRecordList> payRecordList;
    private ListView listView;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            payRecordList= (List<Sc_getPayRecord.GetPayRecordResult.PayRecordList>) msg.obj;
            if(payRecordList!=null&&payRecordList.size()>0){
                adapter=new PayAdapter(PaymentActivity.this,payRecordList);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        token=SharedPreferencesUtils.getToken(this);
        projectCode=SharedPreferencesUtils.getProjectCode(this);
        roomNo=SharedPreferencesUtils.getRoomNo(this);
        registerMessageReceiver();
        //获取控件
        back= (ImageView) findViewById(R.id.back);
        water=findViewById(R.id.water);
        electric=findViewById(R.id.electric);
        gas=findViewById(R.id.gas);
        tingche=findViewById(R.id.tingche);
        wuye=findViewById(R.id.wuye);
        bt_pay=findViewById(R.id.bt_pay);
        et_pay=findViewById(R.id.et_pay);
        listView=findViewById(R.id.listview);
        //添加监听
        back.setOnClickListener(clickListener);
        water.setOnClickListener(clickListener);
        electric.setOnClickListener(clickListener);
        gas.setOnClickListener(clickListener);
        tingche.setOnClickListener(clickListener);
        wuye.setOnClickListener(clickListener);
        bt_pay.setOnClickListener(clickListener);

        water.performClick();

        mDialog=DialogThridUtils.showWaitDialog(PaymentActivity.this,true);
        getPayRecord();
    }

    private void getPayRecord() {

        HttpClient.post(CommonUtil.APPURL, "sc_getPayRecord"
                , new Gson().toJson(new Sc_getPayRecord.GetPayRecordParams(token, projectCode, roomNo))
                , new UICallback() {
                    @Override
                    public void process(String data) {
                        Log.e("TAG","data="+data);
                        Sc_getPayRecord.GetPayRecordResult getPayRecordResult=new Gson().fromJson(data, Sc_getPayRecord.GetPayRecordResult.class);
                        int result=getPayRecordResult.getResult();
                        if(result==1){
                            Log.e("TAG", "1-json解析错误");
                            mHandler.sendEmptyMessage(1);
                            id=1;
                            ToastUtil.showToast(PaymentActivity.this,"操作失败");
                        }else if(result==100){
                            Log.e("TAG", "100-成功");
                            id=1;
                            mHandler.sendEmptyMessage(1);
                            android.os.Message message=handler.obtainMessage();
                            message.obj=getPayRecordResult.getPayRecordList();
                            handler.sendMessage(message);
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
                                            getPayRecord();
                                        }else {
                                            showerror();
                                        }
                                    }
                                },PaymentActivity.this);
                            }else {
                                id=1;
                                showerror();
                            }
                        } else if (result == 102) {
                            Log.e("TAG", "102-projectCode错误");
                            mHandler.sendEmptyMessage(1);
                            id=1;
                            ToastUtil.showToast(PaymentActivity.this,"操作失败");
                        }else {
                            id=1;
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(PaymentActivity.this,"操作失败");
                        }
                    }

                    @Override
                    public void onError(String data) {
                        ToastUtil.showToast(PaymentActivity.this, "网络连接异常");
                        mHandler.sendEmptyMessage(1);
                        id=1;
                    }
                });
    }

    View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.back://回退键
                    onBackPressed();
                    break;
                case R.id.water://水费
                    water.setSelected(true);
                    electric.setSelected(false);
                    gas.setSelected(false);
                    tingche.setSelected(false);
                    wuye.setSelected(false);
                    type=1;
                    // 新建一个可以添加属性的文本对象
                    ss = new SpannableString("水费");
                    // 新建一个属性对象,设置文字的大小
                    ass = new AbsoluteSizeSpan(15, true);
                    // 附加属性到文本
                    ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    // 设置hint
                    et_pay.setHint(new SpannedString(ss)); // 一定要进行转换,否则属性会消失

                    break;
                case R.id.electric://电费
                    water.setSelected(false);
                    electric.setSelected(true);
                    gas.setSelected(false);
                    tingche.setSelected(false);
                    wuye.setSelected(false);
                    type=2;
                    ss = new SpannableString("电费");
                    // 新建一个属性对象,设置文字的大小
                    ass = new AbsoluteSizeSpan(15, true);
                    // 附加属性到文本
                    ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    // 设置hint
                    et_pay.setHint(new SpannedString(ss)); // 一定要进行转换,否则属性会消失
                    break;
                case R.id.gas://天然气
                    water.setSelected(false);
                    electric.setSelected(false);
                    gas.setSelected(true);
                    tingche.setSelected(false);
                    wuye.setSelected(false);
                    type=3;
                    ss = new SpannableString("燃气");
                    // 新建一个属性对象,设置文字的大小
                    ass = new AbsoluteSizeSpan(15, true);
                    // 附加属性到文本
                    ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    // 设置hint
                    et_pay.setHint(new SpannedString(ss)); // 一定要进行转换,否则属性会消失
                    break;
                case R.id.tingche://停车费
                    water.setSelected(false);
                    electric.setSelected(false);
                    gas.setSelected(false);
                    tingche.setSelected(true);
                    wuye.setSelected(false);
                    type=4;
                    ss = new SpannableString("停车费");
                    // 新建一个属性对象,设置文字的大小
                    ass = new AbsoluteSizeSpan(15, true);
                    // 附加属性到文本
                    ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    // 设置hint
                    et_pay.setHint(new SpannedString(ss)); // 一定要进行转换,否则属性会消失
                    break;
                case R.id.wuye://物业费
                    water.setSelected(false);
                    electric.setSelected(false);
                    gas.setSelected(false);
                    tingche.setSelected(false);
                    wuye.setSelected(true);
                    type=5;
                    ss = new SpannableString("物业费");
                    // 新建一个属性对象,设置文字的大小
                    ass = new AbsoluteSizeSpan(15, true);
                    // 附加属性到文本
                    ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    // 设置hint
                    et_pay.setHint(new SpannedString(ss)); // 一定要进行转换,否则属性会消失
                    break;
                case R.id.bt_pay://代缴费
                    if(type==1){
                        message="水费";
                    }else if(type==2){
                        message="电费";
                    }else if(type==3){
                        message="燃气";
                    }else if(type==4){
                        message="停车费";
                    }else if(type==5){
                        message="物业费";
                    }
                    paydialog();
                    break;
            }
        }
    };

    private void paydialog() {
        money=et_pay.getText().toString()+"";
        if (message.equals("")){
            ToastUtil.showToast(PaymentActivity.this,"请选择代缴项目");
        }else if(money.equals("")){
            ToastUtil.showToast(PaymentActivity.this,"请输入金额");
        }else {
            AlertDialog.Builder builder=new AlertDialog.Builder(PaymentActivity.this,1);
            builder.setTitle("代缴费");
            builder.setMessage("请求代缴："+message+"￥"+money);
            builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    mDialog=DialogThridUtils.showWaitDialog(PaymentActivity.this,true);
                    addpayrecord();
                }
            });
            builder.create().show();
        }
    }

    private void addpayrecord() {
        HttpClient.post(CommonUtil.APPURL, "sc_addPayRecord",
                new Gson().toJson(new Sc_addPayRecord.AddPayRecordParams(token, projectCode, roomNo, type, money)),
                new UICallback() {
                    @Override
                    public void process(String data) {
                        Log.e("TAG","data="+data);
                        Sc_addPayRecord.AddPayRecordResult recordResult=new Gson().fromJson(data, Sc_addPayRecord.AddPayRecordResult.class);
                        int result=recordResult.getResult();
                        if(result==1){
                            Log.e("TAG", "1-json解析错误");
                            mHandler.sendEmptyMessage(1);
                            id=1;
                            ToastUtil.showToast(PaymentActivity.this,"操作失败");
                        }else if(result==100){
                            Log.e("TAG", "100-成功");
                            id=1;
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(PaymentActivity.this,"付款成功");
                            getPayRecord();
                            et_pay.setText(null);
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
                                            addpayrecord();
                                        }else {
                                            showerror();
                                        }
                                    }
                                },PaymentActivity.this);
                            }else {
                                id=1;
                                showerror();
                            }
                        } else if (result == 102) {
                            Log.e("TAG", "102-projectCode错误");
                            mHandler.sendEmptyMessage(1);
                            id=1;
                            ToastUtil.showToast(PaymentActivity.this,"操作失败");
                        }else {
                            id=1;
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(PaymentActivity.this,"操作失败");
                        }
                    }

                    @Override
                    public void onError(String data) {
                        ToastUtil.showToast(PaymentActivity.this, "网络连接异常");
                        mHandler.sendEmptyMessage(1);
                        id=1;
                    }
                });
    }

    private void showerror() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this, 1);
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
    public void onBackPressed() {
        finish();
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
        AlertDialog.Builder builder=new AlertDialog.Builder(PaymentActivity.this,1);
        builder.setTitle("提示");
        builder.setMessage("该账号已被其他设备登录");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startActivity(new Intent(PaymentActivity.this, LoginActivity.class));
                SharedPreferencesUtils.cleanPassword(PaymentActivity.this);
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
