package com.massky.greenlandvland.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

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
import com.massky.greenlandvland.model.entity.Sc_insertDoorRecord;
import com.massky.greenlandvland.model.entity.Sc_isLoginNew;
import com.massky.greenlandvland.model.entity.Sc_myDoor;
import com.massky.greenlandvland.model.httpclient.HttpUrl.HttpClient;
import com.massky.greenlandvland.model.httpclient.UICallback;
import com.massky.greenlandvland.ui.adapter.MyTransitAdapter;
import com.massky.ywx.ackpasslibrary.AckpassClass;
import com.massky.ywx.ackpasslibrary.OnOpenDeviceListener;
import com.massky.ywx.ackpasslibrary.OnScanListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TransitActivity extends AppCompatActivity {
    private ImageView back;//回退键
    private Button btn_open;
    public AckpassClass mAckpassClass;
    private ListView lv_door;
    private MyTransitAdapter adapter;
    private List<Sc_myDoor.MyDoorResult.Door> doorList;
    private List<Sc_myDoor.MyDoorResult.Door> list=new ArrayList<>();
    private List<Sc_myDoor.MyDoorResult.Door> doors;

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    Handler handler = new Handler();
    private String mstatus="";

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
    List<Sc_insertDoorRecord.InsertDoorRecordParams.Open> openList=new ArrayList<>();
    private int doorId;
    private String floor=null;
    private String openTime;
    private String openResult;
    private Date d;
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private String token;
    private String projectCode;
    private String roomNo;
    private int id=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transit);
        token=SharedPreferencesUtils.getToken(this);
        projectCode=SharedPreferencesUtils.getProjectCode(this);
        roomNo=SharedPreferencesUtils.getRoomNo(this);
        doorList= SharedPreferencesUtils.getDoorList(TransitActivity.this, Sc_myDoor.MyDoorResult.Door.class);
        Log.e("TAG","doorList="+doorList);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
            }
        }

        mAckpassClass=new AckpassClass(TransitActivity.this);
        mAckpassClass.setOnOpenDeviceListener(mOnOpenDevice);
        //获取控件
        back= (ImageView) findViewById(R.id.back);
        btn_open= (Button) findViewById(R.id.btn_open);

        lv_door= (ListView) findViewById(R.id.lv_door);
        btn_open.setClickable(true);
        //添加监听
        back.setOnClickListener(clickListener);
        btn_open.setOnClickListener(new MyButtonListener());


        if (!mAckpassClass.Initialize()){
            Toast.makeText(TransitActivity.this, "Initialize fail", Toast.LENGTH_SHORT).show();
            finish();
        }

        registerMessageReceiver();
        isLogin();
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


    class MyButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            mDialog=DialogThridUtils.showWaitDialog(TransitActivity.this,true);
            btn_open.setClickable(false);
            mAckpassClass.StopScan();
            mAckpassClass.StartScan();
            mAckpassClass.setOnScanListener(mOnScan);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    /**
                     *要执行的操作
                     */
                    mAckpassClass.StopScan();
//                    Log.e("TAG","doors22222="+doors);
//                    Log.e("TAG","MAC="+doors.get(0).getMac().toUpperCase());
                    if(doors!=null){
                        if(doors.size()==1&&doors.get(0).getType()==1){
                            btn_open.setVisibility(View.VISIBLE);
                            lv_door.setVisibility(View.INVISIBLE);
                            mDialog=DialogThridUtils.showWaitDialog(TransitActivity.this,true);
                            doorId=doors.get(0).getId();
                            mAckpassClass.OpenDevice(doors.get(0).getMac().toUpperCase(),"11111111","0000","0010");
//                            mAckpassClass.OpenDevice("41:4B:50:00:00:66","11111111","0000","0010");
                            mstatus="";
                            noGet();
                        }else if(doors!=null&&doors.size()>0){
                            btn_open.setVisibility(View.INVISIBLE);
                            lv_door.setVisibility(View.VISIBLE);
                            adapter.appendData(doors,true);
                            adapter.upData();
                        }else {
//                            btn_open.setVisibility(View.INVISIBLE);
//                            lv_door.setVisibility(View.VISIBLE);
                            ToastUtil.showToast(TransitActivity.this,"没有开门权限");
                        }

                    }else {
                        ToastUtil.showToast(TransitActivity.this,"没有开门权限");
                    }
                    btn_open.setClickable(true);
                    mHandler.sendEmptyMessage(1);
                }
            }, 2000);//10秒后执行Runnable中的run方法


            adapter=new MyTransitAdapter(TransitActivity.this,mAckpassClass);
            list.clear();
            lv_door.setAdapter(adapter);
        }
    }

    private void noGet() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mstatus.equals("")){
                    mHandler.sendEmptyMessage(1);
                    ToastUtil.showToast(TransitActivity.this,"连接失败，打开失败");
                }
            }
        },10000);
    }

    private OnScanListener mOnScan=new OnScanListener() {
        @Override
        public void OnScan(String deviceMac,String deviceName,String deviceType) {
            for (int i=0;i<doorList.size();i++){
                if(deviceMac.toUpperCase().equals(doorList.get(i).getMac().toUpperCase())==true){
                    list.add(doorList.get(i));
                }
            }
//            Log.e("TAG","list="+list);
            Set set = new HashSet();
            doors=new ArrayList<>();
            set.addAll(list);
            doors.addAll(set);
            Log.e("TAG","doors="+doors);


        }
    };


    private OnOpenDeviceListener mOnOpenDevice = new OnOpenDeviceListener(){
        @Override
        public void OnOpenDevice(int status) {
            mstatus=status+"";
            if (status==0) {
                d=new Date(System.currentTimeMillis());
                openTime=sf.format(d);
                openResult="开门成功";
                insertDoor();
            } else {
                Toast.makeText(TransitActivity.this, "开门失败"+ String.valueOf(status), Toast.LENGTH_SHORT).show();
            }
        }
    };


    @Override
    protected void onResume() {
        isForeground = true;
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
        AlertDialog.Builder builder=new AlertDialog.Builder(TransitActivity.this,1);
        builder.setTitle("提示");
        builder.setMessage("该账号已被其他设备登录");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startActivity(new Intent(TransitActivity.this, LoginActivity.class));
                SharedPreferencesUtils.cleanPassword(TransitActivity.this);
                IntelligenceEnterActivity.instance.finish();
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

    private void insertDoor() {
        openList.clear();
        openList.add(new Sc_insertDoorRecord.InsertDoorRecordParams.Open(doorId,floor,openTime,openResult));
        HttpClient.post(CommonUtil.APPURL, "sc_insertDoorRecord",
                new Gson().toJson(new Sc_insertDoorRecord.InsertDoorRecordParams(token,projectCode,openList,roomNo )),
                new UICallback() {
                    @Override
                    public void process(String data) {
                        Log.e("TAG","data="+data);
                        Sc_insertDoorRecord.InsertDoorRecordResult recordResult=new Gson().fromJson(data, Sc_insertDoorRecord.InsertDoorRecordResult.class);
                        int result=recordResult.getResult();
                        if(result==1){
                            Log.e("TAG", "1-json解析错误");
                            id=1;
                            mHandler.sendEmptyMessage(1);
                        }else if(result==100){
                            Log.e("TAG", "1-100成功");
                            id=1;
                            mHandler.sendEmptyMessage(1);
                            Toast.makeText(TransitActivity.this, openResult, Toast.LENGTH_SHORT).show();
                        }else if(result==101){
                            Log.e("TAG", "101-token错误");
                            if(id==1){
                                id=id+1;
                                new GetToken(new CallBackInterface() {
                                    @Override
                                    public void gettoken(String str) {
                                        token=str;
                                        Log.e("TAG","token="+token);
                                        if(!TextUtils.isEmpty(token)){
                                            insertDoor();
                                        }else {
                                            showerror();
                                        }
                                    }
                                },TransitActivity.this);
                            }else {
                                id=1;
                                showerror();
                            }
                        }else if(result==102){
                            Log.e("TAG", "102-projectCode错误");
                            id=1;
                            mHandler.sendEmptyMessage(1);
                            Toast.makeText(TransitActivity.this, "开门失败", Toast.LENGTH_SHORT).show();
                        }else{
                            id=1;
                            Toast.makeText(TransitActivity.this, "开门失败", Toast.LENGTH_SHORT).show();
                            mHandler.sendEmptyMessage(1);
                        }
                    }

                    @Override
                    public void onError(String data) {
                        id=1;
                        mHandler.sendEmptyMessage(1);
                        ToastUtil.showToast(TransitActivity.this,"网络操作失败");
                    }
                });
    }

    private void showerror() {
        AlertDialog.Builder builder = new AlertDialog.Builder(TransitActivity.this, 1);
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
}
