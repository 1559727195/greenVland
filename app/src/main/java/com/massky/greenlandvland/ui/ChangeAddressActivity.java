package com.massky.greenlandvland.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.massky.greenlandvland.common.SharedPreferencesUtils;
import com.massky.greenlandvland.common.ToastUtil;
import com.massky.greenlandvland.model.entity.Sc_updateAccount;
import com.massky.greenlandvland.model.httpclient.HttpUrl.HttpClient;
import com.massky.greenlandvland.model.httpclient.UICallback;

public class ChangeAddressActivity extends AppCompatActivity {
    private ImageView iv_close;
    private Button bt_save;
    private ClearableEditText et_address;
    private String token;
    private String projectCode;
    private String address;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_address);
        token= SharedPreferencesUtils.getToken(this);
        projectCode=SharedPreferencesUtils.getProjectCode(this);

        //获取控件
        iv_close= (ImageView) findViewById(R.id.iv_close);
        bt_save= (Button) findViewById(R.id.bt_save);
        et_address= (ClearableEditText) findViewById(R.id.et_address);
        //添加监听
        iv_close.setOnClickListener(clickListener);
        bt_save.setOnClickListener(clickListener);

        et_address.setText(SharedPreferencesUtils.getAddress(ChangeAddressActivity.this));
    }
    View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_close:
                    onBackPressed();
                    break;
                case R.id.bt_save:
                    address=et_address.getText().toString();
                    if (address.length()<=0||address==null){
                        ToastUtil.showToast(ChangeAddressActivity.this,"请输入地址");
                    }else {
                        AlertDialog.Builder builder=new AlertDialog.Builder(ChangeAddressActivity.this,1);
                        builder.setTitle("提示");
                        builder.setMessage("确定修改地址吗？");
                        builder.setCancelable(false);
                        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                changeAddress();
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

                    break;
            }
        }
    };

    private void changeAddress() {
        mDialog= DialogThridUtils.showWaitDialog(ChangeAddressActivity.this,true);
//                        final String token=SharedPreferencesUtils.getToken(ChangeAddressActivity.this);
//                        final String projectCode=SharedPreferencesUtils.getProjectCode(ChangeAddressActivity.this);
        final String nickName=SharedPreferencesUtils.getNickName(ChangeAddressActivity.this);
        final String gender=SharedPreferencesUtils.getGender(ChangeAddressActivity.this);
        final String birthday=SharedPreferencesUtils.getBirthday(ChangeAddressActivity.this);
        final String mobilePhone=SharedPreferencesUtils.getMobilePhone(ChangeAddressActivity.this);
        HttpClient.post(CommonUtil.APPURL,"sc_updateAccount",new Gson().toJson(new Sc_updateAccount.UpdateAccountParams(token,projectCode
                        , new Sc_updateAccount.UpdateAccountParams.AccountInfo(nickName,gender,birthday,address,mobilePhone)))
                , new UICallback() {
                    @Override
                    public void process(String data) {
//                                    Log.e("TAG","data="+data);
//                                    Log.e("TAG","token="+token);
//                                    Log.e("TAG","projectCode="+projectCode);
//                                    Log.e("TAG","realName="+realName);
//                                    Log.e("TAG","gender="+gender);
//                                    Log.e("TAG","birthday="+birthday);
//                                    Log.e("TAG","address="+address);
//                                    Log.e("TAG","mobilePhone="+mobilePhone);
                        Sc_updateAccount.UpdateAccountResult updateAccountResult=new Gson().fromJson(data, Sc_updateAccount.UpdateAccountResult.class);
                        int result=updateAccountResult.getResult();
                        if(result==1){
                            Log.e("TAG","1-json解析错误");
                            id=1;
                            mHandler.sendEmptyMessage(1);
                        }else if(result==100){
                            Log.e("TAG","100-解析成功");
                            SharedPreferencesUtils.saveAddress(ChangeAddressActivity.this,address);
                            mHandler.sendEmptyMessage(1);
                            id=1;
                            onBackPressed();
                        }else if(result==101){
                            Log.e("TAG","101-token错误");
                            mHandler.sendEmptyMessage(1);
                            if(id==1){
                                id=id+1;
                                new GetToken(new CallBackInterface() {
                                    @Override
                                    public void gettoken(String str) {
                                        token=str;
                                        Log.e("TAG","token="+token);
                                        if(!TextUtils.isEmpty(token)){
                                            changeAddress();
                                        }else {
                                            showerror();
                                        }
                                    }
                                },ChangeAddressActivity.this);
                            }else {
                                id=1;
                                showerror();
                            }
                        }else if(result==102){
                            Log.e("TAG","102-projectCode错误");
                            id=1;
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(ChangeAddressActivity.this,"地址修改失败");
                        }else {
                            mHandler.sendEmptyMessage(1);
                            id=1;
                            ToastUtil.showToast(ChangeAddressActivity.this,"地址修改失败");
                        }
                    }

                    @Override
                    public void onError(String data) {
                        mHandler.sendEmptyMessage(1);
                        id=1;
                        ToastUtil.showToast(ChangeAddressActivity.this,"修改失败");
                    }
                });
    }

    private void showerror() {
        AlertDialog.Builder builder=new AlertDialog.Builder(ChangeAddressActivity.this,1);
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
        finish();
    }
}
