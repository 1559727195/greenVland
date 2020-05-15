package com.massky.greenlandvland.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

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
import com.massky.greenlandvland.model.entity.Sc_deleteNotification;
import com.massky.greenlandvland.model.entity.Sc_isLoginNew;
import com.massky.greenlandvland.model.entity.Sc_notification;
import com.massky.greenlandvland.model.httpclient.HttpUrl.HttpClient;
import com.massky.greenlandvland.model.httpclient.UICallback;
import com.massky.greenlandvland.ui.adapter.InformAdapter;

import java.util.List;

public class InformActivity extends AppCompatActivity {
    private ImageView back;//回退键
    private TextView tv_shareinform;
    private PopupWindow popupWindow;
    private Dialog mDialog;
    private LoadMoreListView listView;
    private InformAdapter adapter;
    private String token;
    private String projectCode;
    private String page;
    private String roomNo;
    public static InformActivity instance=null;
    private int id=1;
    private int pages;
    private String deleteid;
    private List<Sc_notification.NotificationResult.Notifacation> notifacationList;
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
            notifacationList= (List<Sc_notification.NotificationResult.Notifacation>) msg.obj;
            Log.e("TAG","notificationList="+notifacationList);
            adapter=new InformAdapter(InformActivity.this,notifacationList,R.layout.item_inform_listview){

                @Override
                public void convert(final ViewHolder holder, Object object, final int position, View convertView) {
                    holder.setText(R.id.tv_time,notifacationList.get(position).getNotificationTime()+"");
                    holder.setText(R.id.tv_title,notifacationList.get(position).getNotificationTitle()+"");
                    holder.setText(R.id.tv_content,notifacationList.get(position).getNotificationContent()+"");
                    if(notifacationList.get(position).getId()==1){
                        holder.setImagevisible(R.id.iv_hint,true);
                    }else {
                        holder.setImagevisible(R.id.iv_hint,false);
                    }
                    holder.setOnClickListener(R.id.btnDelete, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(InformActivity.this, 1);
                            builder.setMessage("是否删除该记录" );
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteid=notifacationList.get(position).getId()+"";
                                    mDialog=DialogThridUtils.showWaitDialog(InformActivity.this,true);
                                    HttpClient.post(CommonUtil.APPURL, "sc_deleteNotification"
                                            , new Gson().toJson(new Sc_deleteNotification.DeleteNotificationParams(token, projectCode, deleteid))
                                            , new UICallback() {
                                                @Override
                                                public void process(String data) {
                                                    Sc_deleteNotification.DeleteNotificationResult deleteNotificationResult=new Gson().fromJson(data, Sc_deleteNotification.DeleteNotificationResult.class);
                                                    int result=deleteNotificationResult.getResult();
                                                    if(result==1){
                                                        id=1;
                                                        mHandler.sendEmptyMessage(1);
                                                        Log.e("TAG","1-json解析错误");
                                                    }else if(result==100){
                                                        id=1;
                                                        mHandler.sendEmptyMessage(1);
                                                        Log.e("TAG","100-成功");
                                                        ((SwipeMenuLayout) holder.getConvertView()).quickClose();
                                                        notifacationList.remove(position);
                                                        notifyDataSetChanged();
                                                    }else if(result==101){
                                                        Log.e("TAG", "101-token错误");
                                                        if(id==1) {
                                                            id = id + 1;
                                                            new GetToken(new CallBackInterface() {
                                                                @Override
                                                                public void gettoken(String str) {
                                                                    token=str;
                                                                    HttpClient.post(CommonUtil.APPURL, "sc_deleteNotification"
                                                                            , new Gson().toJson(new Sc_deleteNotification.DeleteNotificationParams(token, projectCode, deleteid))
                                                                            , new UICallback() {
                                                                                @Override
                                                                                public void process(String data) {
                                                                                    Sc_deleteNotification.DeleteNotificationResult deleteNotificationResult=new Gson().fromJson(data, Sc_deleteNotification.DeleteNotificationResult.class);
                                                                                    int result=deleteNotificationResult.getResult();
                                                                                    if(result==1){
                                                                                        id=1;
                                                                                        mHandler.sendEmptyMessage(1);
                                                                                        Log.e("TAG","1-json解析错误");
                                                                                    }else if(result==100){
                                                                                        id=1;
                                                                                        mHandler.sendEmptyMessage(1);
                                                                                        Log.e("TAG","100-成功");
                                                                                        ((SwipeMenuLayout) holder.getConvertView()).quickClose();
                                                                                        notifacationList.remove(position);
                                                                                        notifyDataSetChanged();
                                                                                    }else if(result==101){
                                                                                        Log.e("TAG", "101-token错误");
                                                                                        id=1;
                                                                                        mHandler.sendEmptyMessage(1);
                                                                                        showerror();
                                                                                    }else if(result==102){
                                                                                        id=1;
                                                                                        mHandler.sendEmptyMessage(1);
                                                                                        Log.e("TAG","102-projectCode错误");
                                                                                        ToastUtil.showToast(InformActivity.this,"操作失败");
                                                                                    }else if(result==103){
                                                                                        id=1;
                                                                                        mHandler.sendEmptyMessage(1);
                                                                                        Log.e("TAG","103-删除失败");
                                                                                        ToastUtil.showToast(InformActivity.this,"删除失败");
                                                                                    }else {
                                                                                        id=1;
                                                                                        mHandler.sendEmptyMessage(1);
                                                                                        ToastUtil.showToast(InformActivity.this,"删除失败");
                                                                                    }
                                                                                }

                                                                                @Override
                                                                                public void onError(String data) {
                                                                                    id=1;
                                                                                    mHandler.sendEmptyMessage(1);
                                                                                    ToastUtil.showToast(InformActivity.this,"网络操作失败");
                                                                                }
                                                                            });

                                                                }
                                                            },InformActivity.this);
                                                        }else {
                                                            id = 1;
                                                            showerror();
                                                        }
                                                    }else if(result==102){
                                                        id=1;
                                                        mHandler.sendEmptyMessage(1);
                                                        Log.e("TAG","102-projectCode错误");
                                                        ToastUtil.showToast(InformActivity.this,"操作失败");
                                                    }else if(result==103){
                                                        id=1;
                                                        mHandler.sendEmptyMessage(1);
                                                        Log.e("TAG","103-删除失败");
                                                        ToastUtil.showToast(InformActivity.this,"删除失败");
                                                    }else {
                                                        id=1;
                                                        mHandler.sendEmptyMessage(1);
                                                        ToastUtil.showToast(InformActivity.this,"删除失败");
                                                    }
                                                }

                                                @Override
                                                public void onError(String data) {
                                                    id=1;
                                                    mHandler.sendEmptyMessage(1);
                                                    ToastUtil.showToast(InformActivity.this,"网络操作失败");
                                                }
                                            });
                                }
                            });
                            builder.setNegativeButton("取消",null);
                            builder.create().show();
                        }
                    });

                    holder.setOnClickListener(R.id.content, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(InformActivity.this,InformDetialActivity.class);
                            Sc_notification.NotificationResult.Notifacation notification=notifacationList.get(position) ;
                            intent.putExtra("information",notification);
                            startActivity(intent);
                        }
                    });


                }
            };
            listView.setAdapter(adapter);

//            adapter.appendData(notifacationList,true);
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
        HttpClient.post(CommonUtil.APPURL, "sc_notification"
                , new Gson().toJson(new Sc_notification.NotificationParams(token, projectCode, page, roomNo))
                , new UICallback() {
                    @Override
                    public void process(String data) {
//                        Log.e("TAG","data="+data);
//                        Log.e("TAG","token="+token);
//                        Log.e("TAG","projectCode="+projectCode);
//                        Log.e("TAG","page="+page);
                        Sc_notification.NotificationResult notificationResult=new Gson().fromJson(data, Sc_notification.NotificationResult.class);
                        int result=notificationResult.getResult();
                        if(result==1){
                            Log.e("TAG","1-json解析错误");
                            id=1;
                            page=String.valueOf((pages-1));
                            mHandler.sendEmptyMessage(1);
                        }else if(result==100){
                            Log.e("TAG","100-成功");
                            id=1;
                            if(notificationResult.getNotifacationList()==null||notificationResult.getNotifacationList().size()<=0){
                                ToastUtil.showToast(InformActivity.this,"没有数据了");
                                listView.setLoadCompleted();
                            }else {
                                notifacationList.addAll(notificationResult.getNotifacationList());
                                adapter.appendData(notifacationList,false);
                                adapter.upData();
                                listView.setLoadCompleted();
                            }

                        }else if(result==101){
                            Log.e("TAG","101-token错误");
                            mHandler.sendEmptyMessage(1);
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
                                },InformActivity.this);
                            }else {
                                id=1;
                                listView.setLoadCompleted();
                                showerror();
                            }
                        }else if(result==102){
                            Log.e("TAG","102-projectCode错误");
                            id=1;
                            mHandler.sendEmptyMessage(1);
                        }else {
                            id=1;
                            listView.setLoadCompleted();
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(InformActivity.this,"操作失败");
                        }
                    }

                    @Override
                    public void onError(String data) {
                        id=1;
                        listView.setLoadCompleted();
                        ToastUtil.showToast(InformActivity.this,"网络操作失败");
                        page=String.valueOf((pages-1));
                    }
                });
    }

    private void showerror() {
        AlertDialog.Builder builder = new AlertDialog.Builder(InformActivity.this, 1);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inform);
        instance=this;
        token= SharedPreferencesUtils.getToken(this);
        projectCode=SharedPreferencesUtils.getProjectCode(this);
        roomNo=SharedPreferencesUtils.getRoomNo(this);

        registerMessageReceiver();

        //获取控件
        back= (ImageView) findViewById(R.id.back);
        tv_shareinform= (TextView) findViewById(R.id.tv_shareinform);
        listView= (LoadMoreListView) findViewById(R.id.listview);
        //添加监听
        back.setOnClickListener(clickListener);
        tv_shareinform.setOnClickListener(clickListener);
        initnotification();

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent=new Intent(InformActivity.this,InformDetialActivity.class);
//                Sc_notification.NotificationResult.Notifacation notification= (Sc_notification.NotificationResult.Notifacation) parent.getItemAtPosition(position);
//                intent.putExtra("information",notification);
//                startActivity(intent);
//            }
//        });
    }

    private void initnotification() {
        mDialog= DialogThridUtils.showWaitDialog(InformActivity.this,true);
        page="01";
        HttpClient.post(CommonUtil.APPURL, "sc_notification"
                , new Gson().toJson(new Sc_notification.NotificationParams(token, projectCode, page,roomNo))
                , new UICallback() {
                    @Override
                    public void process(String data) {
                        Log.e("TAG","data="+data);
//                        Log.e("TAG","token="+token);
//                        Log.e("TAG","projectCode="+projectCode);
//                        Log.e("TAG","page="+page);
                        Sc_notification.NotificationResult notificationResult=new Gson().fromJson(data, Sc_notification.NotificationResult.class);
                        Log.e("TAG","notificationResult111="+notificationResult);
                        int result=notificationResult.getResult();
                        if(result==1){
                            Log.e("TAG","1-json解析错误");
                            id=1;
                            mHandler.sendEmptyMessage(1);
                        }else if(result==100){
                            Log.e("TAG","100-成功");
                            id=1;
                            Message message = handler.obtainMessage();
//                            notificationList=notificationResult.getNotificationList();
                            Log.e("TAG","message="+notificationResult.getNotifacationList());
                            message.obj=notificationResult.getNotifacationList();
                            handler.sendMessage(message);
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
                                            initnotification();
                                        }else {
                                            showerror();
                                        }
                                    }
                                },InformActivity.this);
                            }else {
                                id=1;
                                showerror();
                            }
                        }else if(result==102){
                            Log.e("TAG","102-projectCode错误");
                            id=1;
                            mHandler.sendEmptyMessage(1);
                        }else {
                            id=1;
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(InformActivity.this,"操作失误");
                        }
                    }

                    @Override
                    public void onError(String data) {
                        id=1;
                        mHandler.sendEmptyMessage(1);
                        ToastUtil.showToast(InformActivity.this,"网络连接失败");
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
                case R.id.tv_shareinform://编辑按钮
                    View view1 = View.inflate(InformActivity.this,R.layout.item_popup_tenentedit, null);
                    popupWindow = new PopupWindow(view1, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                    //点击其他地方popupwindow消失
                    popupWindow.setBackgroundDrawable(new BitmapDrawable());
                    popupWindow.setOutsideTouchable(true);
                    popupWindow.showAtLocation(view1, Gravity.BOTTOM,0,0);
                    final ImageView iv_noselect,iv_allselect;
                    iv_noselect=(ImageView) view1.findViewById(R.id.iv_noselect);
                    iv_allselect=(ImageView) view1.findViewById(R.id.iv_allselect);
                    iv_noselect.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            iv_noselect.setVisibility(View.GONE);
                            iv_allselect.setVisibility(View.VISIBLE);
                        }
                    });
                    iv_allselect.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            iv_noselect.setVisibility(View.VISIBLE);
                            iv_allselect.setVisibility(View.GONE);
                        }
                    });
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
        AlertDialog.Builder builder=new AlertDialog.Builder(InformActivity.this,1);
        builder.setTitle("提示");
        builder.setMessage("该账号已被其他设备登录");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startActivity(new Intent(InformActivity.this, LoginActivity.class));
                SharedPreferencesUtils.cleanPassword(InformActivity.this);
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
