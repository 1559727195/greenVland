package com.massky.greenlandvland.ui;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.massky.greenlandvland.R;
import com.massky.greenlandvland.View.LoadMoreListView;
import com.massky.greenlandvland.View.swipelayout.ViewHolder;
import com.massky.greenlandvland.common.CallBackInterface;
import com.massky.greenlandvland.common.CommonUtil;
import com.massky.greenlandvland.common.DialogThridUtils;
import com.massky.greenlandvland.common.GetToken;
import com.massky.greenlandvland.common.LocalBroadcastManager;
import com.massky.greenlandvland.common.PictureUtil;
import com.massky.greenlandvland.common.SharedPreferencesUtils;
import com.massky.greenlandvland.common.ToastUtil;
import com.massky.greenlandvland.model.entity.Sc_forumThreadsDiscuss;
import com.massky.greenlandvland.model.entity.Sc_isLoginNew;
import com.massky.greenlandvland.model.httpclient.HttpUrl.HttpClient;
import com.massky.greenlandvland.model.httpclient.UICallback;
import com.massky.greenlandvland.ui.adapter.DiscussAdapter;

import java.util.List;

public class DiscussActivity extends AppCompatActivity {
    private ImageView back;
    private String token;
    private String projectCode;
    private String roomNo;
    private int id=1;
    private String page;
    private int pages;
    private int fourmId;
    private DiscussAdapter adapter;
    private LoadMoreListView listView;
    private List<Sc_forumThreadsDiscuss.ForumThreadsDiscussResult.ForumDiscuss> forumDiscussList;

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

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            forumDiscussList= (List<Sc_forumThreadsDiscuss.ForumThreadsDiscussResult.ForumDiscuss>) msg.obj;
            Log.e("TAG","forumDiscussList"+forumDiscussList);
            adapter=new DiscussAdapter(DiscussActivity.this,forumDiscussList,R.layout.item_discuss_listview) {
                @Override
                public void convert(ViewHolder holder, Object object, int position, View convertView) {
                    holder.setText(R.id.name,forumDiscussList.get(position).getDiscusser()+"");
                    holder.setText(R.id.time,forumDiscussList.get(position).getDiscussTime());
                    holder.setText(R.id.content,forumDiscussList.get(position).getDiscussContent()+"");
//                    holder.setText(R.id.commentcount,forumDiscussList.get(position).getSubDiscussCount()+"");
//                    holder.setText(R.id.laudcount,forumDiscussList.get(position).getDiscussLaudCount()+"");
                    if(forumDiscussList.get(position).getDiscusserHead()!=null){
                        holder.setImageBitmap(R.id.head, PictureUtil.base64ToBitmap(forumDiscussList.get(position).getDiscusserHead()));
                    }
                }
            };
            listView.setAdapter(adapter);
            listView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
                @Override
                public void onloadMore() {
                    loadMore();
                }
            });
        }
    };

    private void loadMore() {
        pages=Integer.parseInt(page)+1;
        page="0"+String.valueOf(pages);
        HttpClient.post(CommonUtil.APPURL, "sc_forumThreadsDiscuss"
                , new Gson().toJson(new Sc_forumThreadsDiscuss.ForumThreadsDiscussParams(token, projectCode, fourmId, page))
                , new UICallback() {
                    @Override
                    public void process(String data) {
                        Log.e("TAG","data="+data);
                        Sc_forumThreadsDiscuss.ForumThreadsDiscussResult forumThreadsDiscussResult=new Gson().fromJson(data, Sc_forumThreadsDiscuss.ForumThreadsDiscussResult.class);
                        int result=forumThreadsDiscussResult.getResult();
                        if(result==1){
                            id=1;
                            mHandler.sendEmptyMessage(1);
                            Log.e("TAG","1-json解析错误");
                        }else if(result==100){
                            Log.e("TAG","100-成功");
                            if(forumThreadsDiscussResult.getForumDiscussList()==null||forumThreadsDiscussResult.getForumDiscussList().size()<=0){
                                ToastUtil.showToast(DiscussActivity.this,"没有数据了");
                                listView.setLoadCompleted();
                            }else {
                                forumDiscussList.addAll(forumThreadsDiscussResult.getForumDiscussList());
                                adapter.appendData(forumDiscussList,false);
                                adapter.upData();
                                listView.setLoadCompleted();
                            }
                            id=1;
                            mHandler.sendEmptyMessage(1);
                        }else if(result==101){
                            Log.e("TAG","101-token错误");
                            page=String.valueOf((pages-1));
                            if(id==1){
                                id=id+1;
                                new GetToken(new CallBackInterface() {
                                    @Override
                                    public void gettoken(String str) {
                                        token=str;
                                        Log.e("TAG","token="+token);
                                        if(!TextUtils.isEmpty(token)){
                                            loadMore();
                                        }else {
                                            listView.setLoadCompleted();
                                            showerror();
                                        }
                                    }
                                },DiscussActivity.this);
                            }else {
                                id=1;
                                listView.setLoadCompleted();
                                showerror();
                            }
                        }else if(result==102){
                            id=1;
                            mHandler.sendEmptyMessage(1);
                            Log.e("TAG","102-projectCode错误");
                            ToastUtil.showToast(DiscussActivity.this,"操作失败");
                        }else {
                            id=1;
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(DiscussActivity.this,"操作失败");
                        }
                    }

                    @Override
                    public void onError(String data) {
                        id=1;
                        mHandler.sendEmptyMessage(1);
                        ToastUtil.showToast(DiscussActivity.this,"网络操作失败");
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discuss);
        token= SharedPreferencesUtils.getToken(this);
        projectCode=SharedPreferencesUtils.getProjectCode(this);
        roomNo=SharedPreferencesUtils.getRoomNo(this);

        registerMessageReceiver();

        Bundle bundle=getIntent().getExtras();
        fourmId=bundle.getInt("forumId");
        Log.e("TAG","forumId="+fourmId);

        back= (ImageView) findViewById(R.id.back);
        listView= (LoadMoreListView) findViewById(R.id.listview);

        back.setOnClickListener(clickListener);
        mDialog=DialogThridUtils.showWaitDialog(DiscussActivity.this,true);
        initForumdiscuss();
    }

    private void initForumdiscuss() {

        page="01";
        HttpClient.post(CommonUtil.APPURL, "sc_forumThreadsDiscuss"
                , new Gson().toJson(new Sc_forumThreadsDiscuss.ForumThreadsDiscussParams(token, projectCode, fourmId, page))
                , new UICallback() {
                    @Override
                    public void process(String data) {
                        Log.e("TAG","data="+data);
                        Sc_forumThreadsDiscuss.ForumThreadsDiscussResult forumThreadsDiscussResult=new Gson().fromJson(data, Sc_forumThreadsDiscuss.ForumThreadsDiscussResult.class);
                        int result=forumThreadsDiscussResult.getResult();
                        if(result==1){
                            id=1;
                            mHandler.sendEmptyMessage(1);
                            Log.e("TAG","1-json解析错误");
                        }else if(result==100){
                            id=1;
                            mHandler.sendEmptyMessage(1);
                            Message msg=handler.obtainMessage();
                            msg.obj=forumThreadsDiscussResult.getForumDiscussList();
                            handler.sendMessage(msg);
                            Log.e("TAG","100-成功");
                        }else if(result==101){
                            Log.e("TAG","101-token错误");
                            page=String.valueOf((pages-1));
                            if(id==1){
                                id=id+1;
                                new GetToken(new CallBackInterface() {
                                    @Override
                                    public void gettoken(String str) {
                                        token=str;
                                        Log.e("TAG","token="+token);
                                        if(!TextUtils.isEmpty(token)){
                                            initForumdiscuss();
                                        }else {
                                            listView.setLoadCompleted();
                                            showerror();
                                        }
                                    }
                                },DiscussActivity.this);
                            }else {
                                id=1;
                                listView.setLoadCompleted();
                                showerror();
                            }
                        }else if(result==102){
                            id=1;
                            mHandler.sendEmptyMessage(1);
                            Log.e("TAG","102-projectCode错误");
                            ToastUtil.showToast(DiscussActivity.this,"操作失败");
                        }else {
                            id=1;
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(DiscussActivity.this,"操作失败");
                        }
                    }

                    @Override
                    public void onError(String data) {
                        id=1;
                        mHandler.sendEmptyMessage(1);
                        ToastUtil.showToast(DiscussActivity.this,"网络操作失败");
                    }
                });
    }

    View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.back:
                    finish();
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void showerror() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DiscussActivity.this, 1);
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
        AlertDialog.Builder builder=new AlertDialog.Builder(DiscussActivity.this,1);
        builder.setTitle("提示");
        builder.setMessage("该账号已被其他设备登录");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startActivity(new Intent(DiscussActivity.this, LoginActivity.class));
                SharedPreferencesUtils.cleanPassword(DiscussActivity.this);
                MainActivity.instance.finish();
                if(isExsitMianActivity(ForumDetialActivity.class)){
                    ForumDetialActivity.instance.finish();
                }
                if(isExsitMianActivity(PostDetialActivity.class)){
                    PostDetialActivity.instance.finish();
                }
                if(isExsitMianActivity(MyPostActivity.class)){
                    MyPostActivity.instance.finish();
                }
                if(isExsitMianActivity(ResponseDetialActivity.class)){
                    ResponseDetialActivity.instance.finish();
                }

                finish();
            }
        });
        builder.create().show();
    }

    /**
     * 判断某一个类是否存在任务栈里面
     * @return
     */
    private boolean isExsitMianActivity(Class<?> cls){
        Intent intent = new Intent(this, cls);
        ComponentName cmpName = intent.resolveActivity(getPackageManager());
        boolean flag = false;
        if (cmpName != null) { // 说明系统中存在这个activity
            ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfoList = am.getRunningTasks(10);
            for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
                if (taskInfo.baseActivity.equals(cmpName)) { // 说明它已经启动了
                    flag = true;
                    break;  //跳出循环，优化效率
                }
            }
        }
        return flag;
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
