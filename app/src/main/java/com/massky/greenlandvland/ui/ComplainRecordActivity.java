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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.massky.greenlandvland.R;
import com.massky.greenlandvland.View.LoadMoreListView;
import com.massky.greenlandvland.View.swipelayout.SwipeMenuLayout;
import com.massky.greenlandvland.View.swipelayout.ViewHolder;
import com.massky.greenlandvland.common.CallBackInterface;
import com.massky.greenlandvland.common.CommonUtil;
import com.massky.greenlandvland.common.DialogThridUtils;
import com.massky.greenlandvland.common.GetToken;
import com.massky.greenlandvland.common.LocalBroadcastManager;
import com.massky.greenlandvland.common.SharedPreferencesUtils;
import com.massky.greenlandvland.common.ToastUtil;
import com.massky.greenlandvland.model.entity.Sc_deleteComplaint;
import com.massky.greenlandvland.model.entity.Sc_isLoginNew;
import com.massky.greenlandvland.model.entity.Sc_myComplaint;
import com.massky.greenlandvland.model.httpclient.HttpUrl.HttpClient;
import com.massky.greenlandvland.model.httpclient.UICallback;
import com.massky.greenlandvland.ui.adapter.RecordAdapter;

import java.util.ArrayList;
import java.util.List;

public class ComplainRecordActivity extends AppCompatActivity {
    private ImageView back;//回退键
    private RelativeLayout rl_norecord;
    private LoadMoreListView listView;
    private RecordAdapter adapter;
    private String token;
    private String projectCode;
    private String page;
    private int pages;
    private int id=1;
    private int complaintId;
    private List<Sc_myComplaint.MyComplaintResult.Complaint> complaintList=new ArrayList<>();
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
    public static ComplainRecordActivity instance=null;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            complaintList= (List<Sc_myComplaint.MyComplaintResult.Complaint>) msg.obj;
            if(complaintList==null){
                rl_norecord.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            }else {
                rl_norecord.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                adapter=new RecordAdapter(ComplainRecordActivity.this,complaintList,R.layout.item_record_listview){
                    @Override
                    public void convert(final ViewHolder holder, Object object, final int position, View convertView) {
                        holder.setText(R.id.record,complaintList.get(position).getComplaintCategory()+"");
                        holder.setText(R.id.title,complaintList.get(position).getComplaintTitle()+"");
                        holder.setText(R.id.time,complaintList.get(position).getComplaintTime()+"");
                        holder.setOnClickListener(R.id.btnDelete, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ComplainRecordActivity.this, 1);
                                builder.setTitle("删除设备");
                                builder.setMessage("是否删除该记录" );
                                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mDialog=DialogThridUtils.showWaitDialog(ComplainRecordActivity.this,true);
                                        complaintId=complaintList.get(position).getId();
                                        HttpClient.post(CommonUtil.APPURL, "sc_deleteComplaint"
                                                , new Gson().toJson(new Sc_deleteComplaint.DeleteComplaintParams(token, projectCode,complaintId ))
                                                , new UICallback() {
                                                    @Override
                                                    public void process(String data) {
                                                        Log.e("TAG","data="+data);
                                                        Log.e("TAG","token="+token);
                                                        Log.e("TAG","projectCode="+projectCode);
                                                        Log.e("TAG","complaintId="+complaintId);
                                                        Sc_deleteComplaint.DeleteComplaintResult deleteComplaintResult=new Gson().fromJson(data, Sc_deleteComplaint.DeleteComplaintResult.class);
                                                        int result=deleteComplaintResult.getResult();
                                                        if(result==1){
                                                            id=1;
                                                            mHandler.sendEmptyMessage(1);
                                                            Log.e("TAG","1-json解析失败");
                                                        }else if(result==100){
                                                            id=1;
                                                            mHandler.sendEmptyMessage(1);
                                                            ((SwipeMenuLayout) holder.getConvertView()).quickClose();
                                                            complaintList.remove(position);
                                                            notifyDataSetChanged();
                                                            Log.e("TAG","100-成功");
                                                        }else if(result==101){
                                                            Log.e("TAG", "101-token错误");
                                                            if(id==1) {
                                                                id = id + 1;
                                                                new GetToken(new CallBackInterface() {
                                                                    @Override
                                                                    public void gettoken(String str) {
                                                                        token=str;
                                                                        HttpClient.post(CommonUtil.APPURL, "sc_deleteComplaint"
                                                                                , new Gson().toJson(new Sc_deleteComplaint.DeleteComplaintParams(token, projectCode,complaintId ))
                                                                                , new UICallback() {
                                                                                    @Override
                                                                                    public void process(String data) {
                                                                                        Sc_deleteComplaint.DeleteComplaintResult deleteComplaintResult=new Gson().fromJson(data, Sc_deleteComplaint.DeleteComplaintResult.class);
                                                                                        int result=deleteComplaintResult.getResult();
                                                                                        if(result==1){
                                                                                            id=1;
                                                                                            mHandler.sendEmptyMessage(1);
                                                                                            Log.e("TAG","1-json解析失败");
                                                                                        }else if(result==100){
                                                                                            id=1;
                                                                                            mHandler.sendEmptyMessage(1);
                                                                                            ((SwipeMenuLayout) holder.getConvertView()).quickClose();
                                                                                            complaintList.remove(position);
                                                                                            notifyDataSetChanged();
                                                                                            Log.e("TAG","100-成功");
                                                                                        }else if(result==101){
                                                                                            Log.e("TAG", "101-token错误");
                                                                                            id=1;
                                                                                            showerror();
                                                                                        }else if(result==102){
                                                                                            id=1;
                                                                                            mHandler.sendEmptyMessage(1);
                                                                                            Log.e("TAG","102-projectCode错误");
                                                                                            ToastUtil.showToast(ComplainRecordActivity.this,"删除失败");
                                                                                        }else if(result==103){
                                                                                            id=1;
                                                                                            mHandler.sendEmptyMessage(1);
                                                                                            Log.e("TAG","103-删除失败");
                                                                                            ToastUtil.showToast(ComplainRecordActivity.this,"删除失败");
                                                                                        }else {
                                                                                            id=1;
                                                                                            mHandler.sendEmptyMessage(1);
                                                                                            ToastUtil.showToast(ComplainRecordActivity.this,"删除失败");
                                                                                        }
                                                                                    }

                                                                                    @Override
                                                                                    public void onError(String data) {
                                                                                        id=1;
                                                                                        mHandler.sendEmptyMessage(1);

                                                                                    }
                                                                                });
                                                                    }
                                                                },ComplainRecordActivity.this);
                                                            }else {
                                                                id = 1;
                                                                showerror();
                                                            }
                                                        }else if(result==102){
                                                            id=1;
                                                            mHandler.sendEmptyMessage(1);
                                                            Log.e("TAG","102-projectCode错误");
                                                            ToastUtil.showToast(ComplainRecordActivity.this,"删除失败");
                                                        }else if(result==103){
                                                            id=1;
                                                            mHandler.sendEmptyMessage(1);
                                                            Log.e("TAG","103-删除失败");
                                                            ToastUtil.showToast(ComplainRecordActivity.this,"删除失败");
                                                        }else {
                                                            id=1;
                                                            mHandler.sendEmptyMessage(1);
                                                            ToastUtil.showToast(ComplainRecordActivity.this,"删除失败");
                                                        }
                                                    }

                                                    @Override
                                                    public void onError(String data) {
                                                        id=1;
                                                        mHandler.sendEmptyMessage(1);

                                                    }
                                                });
                                    }
                                });
                                builder.setNegativeButton("取消", null);
                                builder.create().show();

                            }
                        });

                        holder.setOnClickListener(R.id.content, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Sc_myComplaint.MyComplaintResult.Complaint complaint=complaintList.get(position);
                                Intent intent=new Intent(ComplainRecordActivity.this,ComplainRecordDetialActivity.class);
                                intent.putExtra("complaint",complaint);
                                startActivity(intent);
                            }
                        });
                    }
                };
                listView.setAdapter(adapter);
//                adapter.appendData(complaintList,true);
                listView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
                    @Override
                    public void onloadMore() {
                        loadMore();
                    }
                });
            }

        }
    };

    private void loadMore() {
        pages=Integer.parseInt(page)+1;
        page="0"+String.valueOf(pages);
        HttpClient.post(CommonUtil.APPURL, "sc_myComplaint"
                , new Gson().toJson(new Sc_myComplaint.MyComplaintParams(token, projectCode, page))
                , new UICallback() {
                    @Override
                    public void process(String data) {
                        Log.e("TAG","data="+data);
                        Sc_myComplaint.MyComplaintResult myComplaintResult=new Gson().fromJson(data, Sc_myComplaint.MyComplaintResult.class);
                        int result=myComplaintResult.getResult();
                        if(result==1){
                            Log.e("TAG","1-json错误");
                            id=1;
                            page=String.valueOf((pages-1));
                        }else if(result==100){
                            Log.e("TAG","100-成功");
                            if(myComplaintResult.getComplaintList()==null||myComplaintResult.getComplaintList().size()<=0){
                                ToastUtil.showToast(ComplainRecordActivity.this,"没有数据了");
                                listView.setLoadCompleted();
                            }else {
                                complaintList.addAll(myComplaintResult.getComplaintList());
                                adapter.appendData(complaintList,false);
                                adapter.upData();
                                listView.setLoadCompleted();
                            }
                            id=1;

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
                                },ComplainRecordActivity.this);
                            }else {
                                id=1;
                                listView.setLoadCompleted();
                                showerror();
                            }
                        }else {
                            listView.setLoadCompleted();
                            page=String.valueOf((pages-1));
                            id=1;
                            ToastUtil.showToast(ComplainRecordActivity.this,"操作失败");
                        }
                    }

                    @Override
                    public void onError(String data) {
                        listView.setLoadCompleted();
                        ToastUtil.showToast(ComplainRecordActivity.this,"操作失败");
                        page=String.valueOf((pages-1));
                        id=1;
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain_record);
        instance=this;
        token= SharedPreferencesUtils.getToken(this);
        projectCode=SharedPreferencesUtils.getProjectCode(this);

        //获取控件
        back= (ImageView) findViewById(R.id.back);
        listView= (LoadMoreListView) findViewById(R.id.listview);
        rl_norecord= (RelativeLayout) findViewById(R.id.rl_norecord);
        //添加监听
        back.setOnClickListener(clickListener);
        rl_norecord.setOnClickListener(clickListener);
        mDialog= DialogThridUtils.showWaitDialog(ComplainRecordActivity.this,true);
        initmyComplaint();

        registerMessageReceiver();
    }

    private void initmyComplaint() {
        page="01";
        HttpClient.post(CommonUtil.APPURL, "sc_myComplaint"
                , new Gson().toJson(new Sc_myComplaint.MyComplaintParams(token, projectCode, page))
                , new UICallback() {
                    @Override
                    public void process(String data) {
                        Log.e("TAG","data="+data);
//                        Log.e("TAG","token="+token);
//                        Log.e("TAG","projectCode="+projectCode);
//                        Log.e("TAG","page="+page);
                        Sc_myComplaint.MyComplaintResult myComplaintResult=new Gson().fromJson(data, Sc_myComplaint.MyComplaintResult.class);
                        int result=myComplaintResult.getResult();
                        if(result==1){
                            Log.e("TAG","1-json解析错误");
                            mHandler.sendEmptyMessage(1);
                            id=1;
                        }else if(result==100){
                            Log.e("TAG","100-成功");
                            Message msg=handler.obtainMessage();
                            msg.obj=myComplaintResult.getComplaintList();
                            handler.sendMessage(msg);
                            id=1;
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
                                            initmyComplaint();
                                        }else {
                                            showerror();
                                        }
                                    }
                                },ComplainRecordActivity.this);
                            }else {
                                id=1;
                                showerror();
                            }
                        }else if(result==102){
                            Log.e("TAG","102-projectCode解析错误");
                            mHandler.sendEmptyMessage(1);
                            id=1;
                        }else{
                            id=1;
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(ComplainRecordActivity.this,"操作失败");
                        }
                    }

                    @Override
                    public void onError(String data) {
                        id=1;
                        mHandler.sendEmptyMessage(1);
                        ToastUtil.showToast(ComplainRecordActivity.this,"网络连接失败");
                    }
                });
    }

    private void showerror() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ComplainRecordActivity.this, 1);
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

    View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.back://回退键
                    onBackPressed();
                    break;
            }
        }
    };

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
        AlertDialog.Builder builder=new AlertDialog.Builder(ComplainRecordActivity.this,1);
        builder.setTitle("提示");
        builder.setMessage("该账号已被其他设备登录");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startActivity(new Intent(ComplainRecordActivity.this, LoginActivity.class));
                SharedPreferencesUtils.cleanPassword(ComplainRecordActivity.this);
                MainActivity.instance.finish();
                ComplainActivity.instance.finish();
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
