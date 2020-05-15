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
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.massky.greenlandvland.R;
import com.massky.greenlandvland.common.CommonUtil;
import com.massky.greenlandvland.common.DialogThridUtils;
import com.massky.greenlandvland.common.LocalBroadcastManager;
import com.massky.greenlandvland.common.SharedPreferencesUtils;
import com.massky.greenlandvland.model.entity.Sc_isLoginNew;
import com.massky.greenlandvland.model.entity.Sc_myComplaint;
import com.massky.greenlandvland.model.entity.Sc_myFourmList;
import com.massky.greenlandvland.model.httpclient.HttpUrl.HttpClient;
import com.massky.greenlandvland.model.httpclient.UICallback;
import com.massky.greenlandvland.ui.adapter.BasePagerAdapter;

public class ComplainRecordDetialActivity extends AppCompatActivity {
    private Sc_myComplaint.MyComplaintResult.Complaint complaint;
    private ImageView back;
    private TextView title,name,time,content;
    private ImageView discuss;
    private ViewPager viewPager;
    private BasePagerAdapter leadPagerAdapter;


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
        setContentView(R.layout.activity_complain_record_detial);
        registerMessageReceiver();
        Intent intent=getIntent();
        complaint= (Sc_myComplaint.MyComplaintResult.Complaint) intent.getSerializableExtra("complaint");
        Log.e("TAG","complaint="+complaint);
        //获取控件
        back= (ImageView) findViewById(R.id.back);
        title= (TextView) findViewById(R.id.title);
        name= (TextView) findViewById(R.id.name);
        time= (TextView) findViewById(R.id.time);
        content= (TextView) findViewById(R.id.content);
        initViewPager();
        initPagerData();

        //添加监听
        back.setOnClickListener(clickListener);

        //设置数据
        title.setText(complaint.getComplaintCategory()+"");
        name.setText(complaint.getComplaintTitle()+"");
        time.setText(complaint.getComplaintTime()+"");
        content.setText("\t\t"+complaint.getComplaintContent()+"");
    }

    View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.back://返回
                    finish();
                    break;
            }
        }
    };

    private void initViewPager() {
        viewPager= (ViewPager) findViewById(R.id.viewpager);
        leadPagerAdapter=new BasePagerAdapter(this);
        viewPager.setAdapter(leadPagerAdapter);
    }

    private void initPagerData() {
        ImageView imageView=null;
        if(complaint.getComplaintImage().get(0).size()>0&&complaint.getComplaintImage()!=null){
            viewPager.setVisibility(View.VISIBLE);
            for (int i=0;i<complaint.getComplaintImage().get(0).size();i++){
                imageView=(ImageView) getLayoutInflater().inflate(R.layout.lead_item, null);
                Glide.with(this).load(complaint.getComplaintImage().get(0).get(i).getImageUrl()).error(R.mipmap.ic_launcher).into(imageView);
                leadPagerAdapter.addViwToAdapter(imageView);
            }
            leadPagerAdapter.notifyDataSetChanged();
        }else {
            viewPager.setVisibility(View.GONE);
        }
    }

    private void showerror() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ComplainRecordDetialActivity.this, 1);
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
        AlertDialog.Builder builder=new AlertDialog.Builder(ComplainRecordDetialActivity.this,1);
        builder.setTitle("提示");
        builder.setMessage("该账号已被其他设备登录");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startActivity(new Intent(ComplainRecordDetialActivity.this, LoginActivity.class));
                SharedPreferencesUtils.cleanPassword(ComplainRecordDetialActivity.this);
                MainActivity.instance.finish();
                ComplainRecordActivity.instance.finish();
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
