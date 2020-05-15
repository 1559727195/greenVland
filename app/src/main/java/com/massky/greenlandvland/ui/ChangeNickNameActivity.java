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

public class ChangeNickNameActivity extends AppCompatActivity {
    private ImageView iv_close;
    private Button bt_save;
    private ClearableEditText et_nickname;
    private String token;
    private String projectCode;
    private String nickName;
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
        setContentView(R.layout.activity_change_nick_name);
        token= SharedPreferencesUtils.getToken(this);
        projectCode=SharedPreferencesUtils.getProjectCode(this);

        //获取控件
        iv_close= (ImageView) findViewById(R.id.iv_close);
        bt_save= (Button) findViewById(R.id.bt_save);
        et_nickname= (ClearableEditText) findViewById(R.id.et_nickname);
        //添加监听
        iv_close.setOnClickListener(clickListener);
        bt_save.setOnClickListener(clickListener);


        et_nickname.setText(SharedPreferencesUtils.getNickName(ChangeNickNameActivity.this));
    }
    View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_close:
                    onBackPressed();
                    break;
                case R.id.bt_save:
                    nickName=et_nickname.getText().toString();
                    if (nickName.length()<=0||nickName==null){
                        ToastUtil.showToast(ChangeNickNameActivity.this,"请输入昵称");
                    }else {
                        AlertDialog.Builder builder=new AlertDialog.Builder(ChangeNickNameActivity.this,1);
                        builder.setTitle("提示");
                        builder.setMessage("确定修改昵称吗？");
                        builder.setCancelable(false);
                        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                changeNickname();
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

    private void changeNickname() {
        mDialog= DialogThridUtils.showWaitDialog(ChangeNickNameActivity.this,true);
//                        final String token=SharedPreferencesUtils.getToken(ChangeNickNameActivity.this);
//                        final String projectCode=SharedPreferencesUtils.getProjectCode(ChangeNickNameActivity.this);
        final String gender=SharedPreferencesUtils.getGender(ChangeNickNameActivity.this);
        final String birthday=SharedPreferencesUtils.getBirthday(ChangeNickNameActivity.this);
        final String address=SharedPreferencesUtils.getAddress(ChangeNickNameActivity.this);
        final String mobilePhone=SharedPreferencesUtils.getMobilePhone(ChangeNickNameActivity.this);
        HttpClient.post(CommonUtil.APPURL,"sc_updateAccount",new Gson().toJson(new Sc_updateAccount.UpdateAccountParams(token,projectCode
                        , new  Sc_updateAccount.UpdateAccountParams.AccountInfo(nickName,gender,birthday,address,mobilePhone)))
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
                            mHandler.sendEmptyMessage(1);
                            id=1;
                            SharedPreferencesUtils.saveNickName(ChangeNickNameActivity.this,nickName);
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
                                            changeNickname();
                                        }else {
                                            showerror();
                                        }
                                    }
                                },ChangeNickNameActivity.this);
                            }else {
                                id=1;
                                showerror();
                            }
                        }else if(result==102){
                            Log.e("TAG","102-projectCode错误");
                            id=1;
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(ChangeNickNameActivity.this,"修改失败");
                        }else {
                            id=1;
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(ChangeNickNameActivity.this,"昵称修改失败");
                        }
                    }

                    @Override
                    public void onError(String data) {
                        mHandler.sendEmptyMessage(1);
                        id=1;
                        ToastUtil.showToast(ChangeNickNameActivity.this,"修改失败");
                    }
                });
    }

    private void showerror() {
        AlertDialog.Builder builder=new AlertDialog.Builder(ChangeNickNameActivity.this,1);
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
