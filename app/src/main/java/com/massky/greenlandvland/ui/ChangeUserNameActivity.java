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
import com.massky.greenlandvland.model.entity.Sc_updateUserName;
import com.massky.greenlandvland.model.httpclient.HttpUrl.HttpClient;
import com.massky.greenlandvland.model.httpclient.UICallback;

public class ChangeUserNameActivity extends AppCompatActivity {
    private ImageView iv_close;
    private Button bt_save;
    private ClearableEditText et_username;
    private String token;
    private String projectCode;
    private String username;
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
        setContentView(R.layout.activity_change_user_name);
        token= SharedPreferencesUtils.getToken(this);
        projectCode=SharedPreferencesUtils.getProjectCode(this);

        //获取控件
        iv_close= (ImageView) findViewById(R.id.iv_close);
        bt_save= (Button) findViewById(R.id.bt_save);
        et_username= (ClearableEditText) findViewById(R.id.et_username);
        //添加监听
        iv_close.setOnClickListener(clickListener);
        bt_save.setOnClickListener(clickListener);

        et_username.setText(SharedPreferencesUtils.getUserName(ChangeUserNameActivity.this));
    }
    View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_close:
                    onBackPressed();
                    break;
                case R.id.bt_save:
                    username=et_username.getText().toString();
                    if(et_username.length()<=0||et_username==null){
                        ToastUtil.showToast(ChangeUserNameActivity.this,"请输入用户名");
                    }else {
                        AlertDialog.Builder builder=new AlertDialog.Builder(ChangeUserNameActivity.this,1);
                        builder.setTitle("提示");
                        builder.setMessage("确定修改用户名吗？");
                        builder.setCancelable(false);
                        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                changeusername();
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

    private void changeusername() {
        mDialog= DialogThridUtils.showWaitDialog(ChangeUserNameActivity.this,true);
//                        String token=SharedPreferencesUtils.getToken(ChangeUserNameActivity.this);
//                        String projectCode=SharedPreferencesUtils.getProjectCode(ChangeUserNameActivity.this);
        HttpClient.post(CommonUtil.APPURL, "sc_updateUserName"
                , new Gson().toJson(new Sc_updateUserName.UpdateUserNameParams(token,projectCode , username))
                , new UICallback() {
                    @Override
                    public void process(String data) {
//                                    Log.e("TAG","data="+data);
//                                    Log.e("TAG","token="+SharedPreferencesUtils.getToken(ChangeUserNameActivity.this));
//                                    Log.e("TAG","projectCode="+SharedPreferencesUtils.getProjectCode(ChangeUserNameActivity.this));
//                                    Log.e("TAG","userName="+et_username.getText().toString());
                        Sc_updateUserName.UpdateUserNameResult updateUserNameResult=new Gson().fromJson(data, Sc_updateUserName.UpdateUserNameResult.class);
                        int result=updateUserNameResult.getResult();
                        if(result==1){
                            id=1;
                            Log.e("TAG","1-json解析错误");
                            mHandler.sendEmptyMessage(1);
                        }else if(result==100){
                            Log.e("TAG","100-成功");
                            SharedPreferencesUtils.saveUserName(ChangeUserNameActivity.this,username);
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
                                            changeusername();
                                        }else {
                                            showerror();
                                        }
                                    }
                                },ChangeUserNameActivity.this);
                            }else {
                                id=1;
                                showerror();
                            }
                        }else if(result==102){
                            Log.e("TAG","102-projectCode错误");
                            id=1;
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(ChangeUserNameActivity.this,"修改失败");
                        }else if(result==103){
                            Log.e("TAG","103-userName重复");
                            id=1;
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(ChangeUserNameActivity.this,"userName重复");
                        }else {
                            id=1;
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(ChangeUserNameActivity.this,"用户名修改失败");
                        }
                    }

                    @Override
                    public void onError(String data) {
                        mHandler.sendEmptyMessage(1);
                        id=1;
                        ToastUtil.showToast(ChangeUserNameActivity.this,"修改失败");
                    }
                });
    }

    private void showerror() {
        AlertDialog.Builder builder=new AlertDialog.Builder(ChangeUserNameActivity.this,1);
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
