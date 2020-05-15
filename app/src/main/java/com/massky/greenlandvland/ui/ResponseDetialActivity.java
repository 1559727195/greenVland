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
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.massky.greenlandvland.R;
import com.massky.greenlandvland.common.CallBackInterface;
import com.massky.greenlandvland.common.CommonUtil;
import com.massky.greenlandvland.common.DialogThridUtils;
import com.massky.greenlandvland.common.GetToken;
import com.massky.greenlandvland.common.LocalBroadcastManager;
import com.massky.greenlandvland.common.SharedPreferencesUtils;
import com.massky.greenlandvland.common.ToastUtil;
import com.massky.greenlandvland.model.entity.Sc_discussForumThread;
import com.massky.greenlandvland.model.entity.Sc_isLoginNew;
import com.massky.greenlandvland.model.entity.Sc_laudForumThread;
import com.massky.greenlandvland.model.entity.Sc_myFourmDiscuss;
import com.massky.greenlandvland.model.entity.Sc_myFourmList;
import com.massky.greenlandvland.model.httpclient.HttpUrl.HttpClient;
import com.massky.greenlandvland.model.httpclient.UICallback;
import com.massky.greenlandvland.ui.adapter.BasePagerAdapter;

public class ResponseDetialActivity extends AppCompatActivity {
    private Sc_myFourmDiscuss.MyFourmDiscussResult.Huitie huitie;
    private ImageView back;
    private TextView title,name,time,content;
    private EditText comment;
    private ImageView discuss;
    private ViewPager viewPager;
    private BasePagerAdapter leadPagerAdapter;
    private TextView laducount,discusscount;
    private ImageView ladu;
    private String token;
    private String projectCode;
    private String roomNo;
    private int id=1;
    private int discussid;
    private int laduingcount;
    private String discussContent;
    private int disscusses;

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

    public static ResponseDetialActivity instance=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response_detial);
        instance=this;
        registerMessageReceiver();
        token= SharedPreferencesUtils.getToken(this);
        projectCode=SharedPreferencesUtils.getProjectCode(this);
        roomNo=SharedPreferencesUtils.getRoomNo(this);
        Intent intent=getIntent();
        huitie= (Sc_myFourmDiscuss.MyFourmDiscussResult.Huitie) intent.getSerializableExtra("huitie");
        Log.e("TAG","myFourm="+huitie);
        //获取控件
        back= (ImageView) findViewById(R.id.back);
        title= (TextView) findViewById(R.id.title);
        name= (TextView) findViewById(R.id.name);
        time= (TextView) findViewById(R.id.time);
        content= (TextView) findViewById(R.id.content);
        comment= (EditText) findViewById(R.id.comment);
        discuss= (ImageView) findViewById(R.id.discuss);
        discusscount= (TextView) findViewById(R.id.discusscount);
        laducount= (TextView) findViewById(R.id.laducount);
        ladu= (ImageView) findViewById(R.id.ladu);
        initViewPager();
        initPagerData();

        //添加监听
        back.setOnClickListener(clickListener);
        discuss.setOnClickListener(clickListener);
        ladu.setOnClickListener(clickListener);
        comment.setOnEditorActionListener(onEditorAction);

        //设置数据
        title.setText(huitie.getForumTitle()+"");
        name.setText(huitie.getUserName()+"");
        time.setText(huitie.getPushTime()+"");
        content.setText("\t\t"+huitie.getForumContent()+"");
        disscusses=huitie.getDiscussCount();
        discusscount.setText(disscusses+"");
        laduingcount=huitie.getLaudCount();
        laducount.setText(laduingcount+"");

        discussid=huitie.getId();
    }

    TextView.OnEditorActionListener onEditorAction=new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            switch (actionId){
                case EditorInfo.IME_ACTION_SEND:
                    ToastUtil.showToast(ResponseDetialActivity.this,"发送");
                    discussContent=comment.getText().toString()+"";
                    if(!TextUtils.isEmpty(discussContent)){
                        send();
                    }else {
                        ToastUtil.showToast(ResponseDetialActivity.this,"评论内容不能为空");
                    }
                    break;
            }
            return false;
        }
    };

    private void send() {
        mDialog=DialogThridUtils.showWaitDialog(ResponseDetialActivity.this,true);
        HttpClient.post(CommonUtil.APPURL, "sc_discussForumThread",
                new Gson().toJson(new Sc_discussForumThread.DiscussForumThreadParams(token, projectCode,1,discussid,discussContent,roomNo)),
                new UICallback() {
                    @Override
                    public void process(String data) {
                        Log.e("TAG","data="+data);
                        Log.e("TAG","projectCode="+projectCode);
                        Log.e("TAG","discussid="+discussid);
                        Log.e("TAG","discussContent="+discussContent);
                        Log.e("TAG","roomNo="+roomNo);
                        Sc_discussForumThread.DiscussForumThreadResult discussForumThreadResult=new Gson().fromJson(data, Sc_discussForumThread.DiscussForumThreadResult.class);
                        int result=discussForumThreadResult.getResult();
                        if(result==1){
                            id=1;
                            Log.e("TAG","1-json解析错误");
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(ResponseDetialActivity.this,"操作失败");
                        }else if(result==100){
                            id=1;
                            disscusses=disscusses+1;
                            Log.e("TAG","disscusses="+disscusses);
                            discusscount.setText(disscusses+"");
                            Log.e("TAG","100-成功");
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(ResponseDetialActivity.this,"成功");
                            comment.getText().clear();
                        }else if(result==101){
                            mHandler.sendEmptyMessage(1);
                            if (id == 1) {
                                id = id + 1;
                                new GetToken(new CallBackInterface() {
                                    @Override
                                    public void gettoken(String str) {
                                        token = str;
                                        Log.e("TAG", "token=" + token);
                                        if (!TextUtils.isEmpty(token)) {
                                            send();
                                        } else {
                                            showerror();
                                        }
                                    }
                                }, ResponseDetialActivity.this);
                            } else {
                                id = 1;
                                showerror();
                            }
                        }else if(result==102){
                            id=1;
                            Log.e("TAG","102-projectCode错误");
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(ResponseDetialActivity.this,"操作失败");
                        }else {
                            id=1;
                            ToastUtil.showToast(ResponseDetialActivity.this,"操作失败");
                            mHandler.sendEmptyMessage(1);
                        }
                    }

                    @Override
                    public void onError(String data) {
                        id=1;
                        mHandler.sendEmptyMessage(1);
                        ToastUtil.showToast(ResponseDetialActivity.this,"网络操作失败");
                    }
                });
    }

    View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.back://返回
                    finish();
                    break;
                case R.id.discuss://评论图标
                    Intent intent=new Intent(ResponseDetialActivity.this,DiscussActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putInt("forumId",discussid);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case R.id.ladu:
                    laduing();
                    break;
            }
        }
    };

    private void laduing() {
        mDialog=DialogThridUtils.showWaitDialog(ResponseDetialActivity.this,true);
        HttpClient.post(CommonUtil.APPURL, "sc_laudForumThread"
                , new Gson().toJson(new Sc_laudForumThread.LaudForumThreadParams(token, projectCode, 1, discussid))
                , new UICallback() {
                    @Override
                    public void process(String data) {
                        Log.e("TAG","data="+data);
                        Log.e("TAG","token="+token);
                        Log.e("TAG","projectCode="+projectCode);
                        Log.e("TAG","discussid="+discussid);
                        Sc_laudForumThread.LaudForumThreadResult laudForumThreadResult=new Gson().fromJson(data, Sc_laudForumThread.LaudForumThreadResult.class);
                        int result=laudForumThreadResult.getResult();
                        if(result==1){
                            id=1;
                            Log.e("TAG","1-json解析错误");
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(ResponseDetialActivity.this,"操作失败");
                        }else if(result==100){
                            id=1;
                            Log.e("TAG","100-成功");
                            laduingcount=laduingcount+1;
                            laducount.setText(laduingcount+"");
                            Log.e("TAG","laduingcount="+laduingcount);
                            ToastUtil.showToast(ResponseDetialActivity.this,"点赞成功");
                            mHandler.sendEmptyMessage(1);
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
                                            laduing();
                                        }else {
                                            showerror();
                                        }
                                    }
                                },ResponseDetialActivity.this);
                            }else {
                                id=1;
                                showerror();
                            }
                        }else if(result==102){
                            id=1;
                            Log.e("TAG","projectCode错误");
                            ToastUtil.showToast(ResponseDetialActivity.this,"操作失败");
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(ResponseDetialActivity.this,"操作失败");
                        }else if(result==103){
                            id=1;
                            Log.e("TAG","id错误");
                            ToastUtil.showToast(ResponseDetialActivity.this,"操作失败");
                            mHandler.sendEmptyMessage(1);
                        }else {
                            id=1;
                            ToastUtil.showToast(ResponseDetialActivity.this,"操作失败");
                            mHandler.sendEmptyMessage(1);
                        }

                    }

                    @Override
                    public void onError(String data) {
                        id=1;
                        ToastUtil.showToast(ResponseDetialActivity.this,"网络操作失败");
                        mHandler.sendEmptyMessage(1);
                    }
                });
    }

    private void initViewPager() {
        viewPager= (ViewPager) findViewById(R.id.viewpager);
        leadPagerAdapter=new BasePagerAdapter(this);
        viewPager.setAdapter(leadPagerAdapter);
    }

    private void initPagerData() {
        ImageView imageView=null;
        if(huitie.getForumImage().size()>0&&huitie.getForumImage()!=null){
            viewPager.setVisibility(View.VISIBLE);
            for (int i=0;i<huitie.getForumImage().size();i++){
                imageView=(ImageView) getLayoutInflater().inflate(R.layout.lead_item, null);
                Glide.with(this).load(huitie.getForumImage().get(i).getImageUrl()).error(R.mipmap.ic_launcher).into(imageView);
                leadPagerAdapter.addViwToAdapter(imageView);
            }
            leadPagerAdapter.notifyDataSetChanged();
        }else {
            viewPager.setVisibility(View.GONE);
        }
    }

    private void showerror() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ResponseDetialActivity.this, 1);
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
        AlertDialog.Builder builder=new AlertDialog.Builder(ResponseDetialActivity.this,1);
        builder.setTitle("提示");
        builder.setMessage("该账号已被其他设备登录");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startActivity(new Intent(ResponseDetialActivity.this, LoginActivity.class));
                SharedPreferencesUtils.cleanPassword(ResponseDetialActivity.this);
                MainActivity.instance.finish();
                MyPostActivity.instance.finish();
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
