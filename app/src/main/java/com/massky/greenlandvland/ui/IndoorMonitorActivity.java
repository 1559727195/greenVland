package com.massky.greenlandvland.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.massky.greenlandvland.R;
import com.massky.greenlandvland.View.swipelayout.SwipeMenuLayout;
import com.massky.greenlandvland.View.swipelayout.ViewHolder;
import com.massky.greenlandvland.common.CallBackInterface;
import com.massky.greenlandvland.common.CommonUtil;
import com.massky.greenlandvland.common.DialogThridUtils;
import com.massky.greenlandvland.common.GetToken;
import com.massky.greenlandvland.common.LocalBroadcastManager;
import com.massky.greenlandvland.common.PictureUtil;
import com.massky.greenlandvland.common.SharedPreferencesUtils;
import com.massky.greenlandvland.common.ToastUtil;
import com.massky.greenlandvland.model.entity.Sc_deleteCamera;
import com.massky.greenlandvland.model.entity.Sc_isLoginNew;
import com.massky.greenlandvland.model.entity.Sc_myCamera;
import com.massky.greenlandvland.model.httpclient.HttpUrl.HttpClient;
import com.massky.greenlandvland.model.httpclient.UICallback;
import com.massky.greenlandvland.ui.adapter.HomeMonitorAdapter;
import java.util.ArrayList;
import java.util.List;

public class IndoorMonitorActivity extends AppCompatActivity {
    private ImageView back;//回退键
    private ImageView iv_add;
    private HomeMonitorAdapter homeMonitorAdapter;
    private ListView listview;
    private String token;
    private String projectCode;
    private String roomNo;
    private Bitmap bitmap;
    private int nDevID;
    private Sc_myCamera.MyCameraResult myCameraResult;
    private List<Sc_myCamera.MyCameraResult.Camera> cameraList=new ArrayList<Sc_myCamera.MyCameraResult.Camera>();
    public static IndoorMonitorActivity instance;
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


    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            cameraList= (List<Sc_myCamera.MyCameraResult.Camera>) msg.obj;
            Log.e("TAG","cameraList="+ cameraList);

            //设置适配器
            homeMonitorAdapter=new HomeMonitorAdapter(IndoorMonitorActivity.this,cameraList,R.layout.item_media_listview){
                @Override
                public void convert(final ViewHolder holder, Object object, final int position, View convertView) {
                    holder.setText(R.id.tv_name,cameraList.get(position).getStrName()+"");
                    bitmap= PictureUtil.base64ToBitmap(cameraList.get(position).getCameraImg()+"");
                    holder.setImageBitmap(R.id.iv_image,bitmap);

                    holder.setOnClickListener(R.id.fl_play, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(IndoorMonitorActivity.this,PlayerItemActivity.class);
                            Sc_myCamera.MyCameraResult.Camera camera= cameraList.get(position);
                            Log.e("TAG","camera="+camera);
                            intent.putExtra("camera",camera);
                            startActivity(intent);
                        }
                    });

                    holder.setOnClickListener(R.id.btnDelete, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(IndoorMonitorActivity.this, 1);
                            builder.setTitle("删除设备");
                            builder.setMessage("是否删除设备" + cameraList.get(position).getStrName()+"");
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    nDevID=cameraList.get(position).getnDevID();
                                    mDialog=DialogThridUtils.showWaitDialog(IndoorMonitorActivity.this,true);
                                    HttpClient.post(CommonUtil.APPURL, "sc_deleteCamera"
                                            , new Gson().toJson(new Sc_deleteCamera.DeleteCameraParams(token, projectCode, nDevID))
                                            , new UICallback() {
                                                @Override
                                                public void process(String data) {
                                                    Sc_deleteCamera.DeleteCameraResult deleteCameraResult=new Gson().fromJson(data, Sc_deleteCamera.DeleteCameraResult.class);
                                                    int result=deleteCameraResult.getResult();
                                                    if(result==1){
                                                        id=1;
                                                        mHandler.sendEmptyMessage(1);
                                                        Log.e("TAG","1-json解析错误");
                                                    }else if(result==100){
                                                        id=1;
                                                        mHandler.sendEmptyMessage(1);
                                                        Log.e("TAG","100-成功");
                                                        ((SwipeMenuLayout) holder.getConvertView()).quickClose();
                                                        cameraList.remove(position);
                                                        notifyDataSetChanged();
                                                    }else if(result==101){
                                                        Log.e("TAG", "101-token错误");
                                                        HttpClient.post(CommonUtil.APPURL, "sc_deleteCamera"
                                                                , new Gson().toJson(new Sc_deleteCamera.DeleteCameraParams(token, projectCode, nDevID))
                                                                , new UICallback() {
                                                                    @Override
                                                                    public void process(String data) {
                                                                        Sc_deleteCamera.DeleteCameraResult deleteCameraResult=new Gson().fromJson(data, Sc_deleteCamera.DeleteCameraResult.class);
                                                                        int result=deleteCameraResult.getResult();
                                                                        if(result==1){
                                                                            id=1;
                                                                            mHandler.sendEmptyMessage(1);
                                                                            Log.e("TAG","1-json解析错误");
                                                                        }else if(result==100){
                                                                            id=1;
                                                                            mHandler.sendEmptyMessage(1);
                                                                            Log.e("TAG","100-成功");
                                                                            ((SwipeMenuLayout) holder.getConvertView()).quickClose();
                                                                            cameraList.remove(position);
                                                                            notifyDataSetChanged();
                                                                        }else if(result==101){
                                                                            Log.e("TAG", "101-token错误");
                                                                            if(id==1){
                                                                                id=id+1;
                                                                                new GetToken(new CallBackInterface() {
                                                                                    @Override
                                                                                    public void gettoken(String str) {
                                                                                        token=str;
                                                                                        if(!TextUtils.isEmpty(token)){
                                                                                            HttpClient.post(CommonUtil.APPURL, "sc_deleteCamera"
                                                                                                    , new Gson().toJson(new Sc_deleteCamera.DeleteCameraParams(token, projectCode, nDevID))
                                                                                                    , new UICallback() {
                                                                                                        @Override
                                                                                                        public void process(String data) {
                                                                                                            Sc_deleteCamera.DeleteCameraResult deleteCameraResult=new Gson().fromJson(data, Sc_deleteCamera.DeleteCameraResult.class);
                                                                                                            int result=deleteCameraResult.getResult();
                                                                                                            if(result==1){
                                                                                                                id=1;
                                                                                                                mHandler.sendEmptyMessage(1);
                                                                                                                Log.e("TAG","1-json解析错误");
                                                                                                            }else if(result==100){
                                                                                                                id=1;
                                                                                                                mHandler.sendEmptyMessage(1);
                                                                                                                Log.e("TAG","100-成功");
                                                                                                                ((SwipeMenuLayout) holder.getConvertView()).quickClose();
                                                                                                                cameraList.remove(position);
                                                                                                                notifyDataSetChanged();
                                                                                                            }else if(result==101){
                                                                                                                Log.e("TAG", "101-token错误");
                                                                                                                id=1;
                                                                                                                showerror();
                                                                                                            }else if(result==102){
                                                                                                                id=1;
                                                                                                                mHandler.sendEmptyMessage(1);
                                                                                                                Log.e("TAG","102-projectCode错误");
                                                                                                                ToastUtil.showToast(IndoorMonitorActivity.this,"操作失败");
                                                                                                            }else if(result==103){
                                                                                                                id=1;
                                                                                                                mHandler.sendEmptyMessage(1);
                                                                                                                Log.e("TAG","103-删错失败");
                                                                                                                ToastUtil.showToast(IndoorMonitorActivity.this,"删除失败");
                                                                                                            }else {
                                                                                                                id=1;
                                                                                                                mHandler.sendEmptyMessage(1);
                                                                                                                ToastUtil.showToast(IndoorMonitorActivity.this,"操作失败");
                                                                                                            }
                                                                                                        }

                                                                                                        @Override
                                                                                                        public void onError(String data) {
                                                                                                            id=1;
                                                                                                            mHandler.sendEmptyMessage(1);
                                                                                                            ToastUtil.showToast(IndoorMonitorActivity.this,"网络操作失败");
                                                                                                        }
                                                                                                    });
                                                                                        }else {
                                                                                            id = 1;
                                                                                            showerror();
                                                                                        }
                                                                                    }
                                                                                },IndoorMonitorActivity.this);
                                                                            }else {
                                                                                id = 1;
                                                                                showerror();
                                                                            }
                                                                        }else if(result==102){
                                                                            id=1;
                                                                            mHandler.sendEmptyMessage(1);
                                                                            Log.e("TAG","102-projectCode错误");
                                                                            ToastUtil.showToast(IndoorMonitorActivity.this,"操作失败");
                                                                        }else if(result==103){
                                                                            id=1;
                                                                            mHandler.sendEmptyMessage(1);
                                                                            Log.e("TAG","103-删错失败");
                                                                            ToastUtil.showToast(IndoorMonitorActivity.this,"删除失败");
                                                                        }else {
                                                                            id=1;
                                                                            mHandler.sendEmptyMessage(1);
                                                                            ToastUtil.showToast(IndoorMonitorActivity.this,"操作失败");
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onError(String data) {
                                                                        id=1;
                                                                        mHandler.sendEmptyMessage(1);
                                                                        ToastUtil.showToast(IndoorMonitorActivity.this,"网络操作失败");
                                                                    }
                                                                });
                                                    }else if(result==102){
                                                        id=1;
                                                        mHandler.sendEmptyMessage(1);
                                                        Log.e("TAG","102-projectCode错误");
                                                        ToastUtil.showToast(IndoorMonitorActivity.this,"操作失败");
                                                    }else if(result==103){
                                                        id=1;
                                                        mHandler.sendEmptyMessage(1);
                                                        Log.e("TAG","103-删错失败");
                                                        ToastUtil.showToast(IndoorMonitorActivity.this,"删除失败");
                                                    }else {
                                                        id=1;
                                                        mHandler.sendEmptyMessage(1);
                                                        ToastUtil.showToast(IndoorMonitorActivity.this,"操作失败");
                                                    }
                                                }

                                                @Override
                                                public void onError(String data) {
                                                    id=1;
                                                    mHandler.sendEmptyMessage(1);
                                                    ToastUtil.showToast(IndoorMonitorActivity.this,"网络操作失败");
                                                }
                                            });
                                }
                            });
                            builder.setNegativeButton("取消", null);
                            builder.create().show();

                        }
                    });
                }
            };
            listview.setAdapter(homeMonitorAdapter);

//            listview.setOnItemClickListener(itemListener);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indoor_monitor);
        instance=this;
        //获取数据
//        Intent intent=getIntent();
//        List<Sc_myCamera.MyCameraResult.Camera> cameraList= (List<Sc_myCamera.MyCameraResult.Camera>) intent.getSerializableExtra("cameraList");
        token= SharedPreferencesUtils.getToken(this);
        projectCode=SharedPreferencesUtils.getProjectCode(this);
        roomNo=SharedPreferencesUtils.getRoomNo(this);
        //获取控件
        back= (ImageView) findViewById(R.id.back);
        iv_add= (ImageView) findViewById(R.id.iv_add);
        listview= (ListView) findViewById(R.id.listview);
        //添加监听
        back.setOnClickListener(clickListener);
        iv_add.setOnClickListener(clickListener);

        iv_add.setVisibility(View.GONE);
        iv_add.setClickable(false);

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
                case R.id.iv_add://添加摄像头
                    startActivity(new Intent(IndoorMonitorActivity.this,AddCamaraActivity.class));
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        finish();
    }

//    public AdapterView.OnItemClickListener itemListener=new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            Intent intent=new Intent(IndoorMonitorActivity.this,PlayerItemActivity.class);
//            Sc_myCamera.MyCameraResult.Camera camera= (Sc_myCamera.MyCameraResult.Camera) parent.getItemAtPosition(position);
//            Log.e("TAG","camera="+camera);
//            intent.putExtra("camera",camera);
//            startActivity(intent);
//        }
//    };

    @Override
    protected void onResume() {
        super.onResume();
        isForeground = true;
        myCamera();
    }

    private void myCamera() {
        mDialog=DialogThridUtils.showWaitDialog(IndoorMonitorActivity.this,true);
        HttpClient.post(CommonUtil.APPURL, "sc_myCamera"
                , new Gson().toJson(new Sc_myCamera.MyCameraParams(token, projectCode, roomNo))
                , new UICallback() {
                    @Override
                    public void process(String data) {
//                        Log.e("TAG","data="+data);
//                        Log.e("TAG","token="+token);
//                        Log.e("TAG","projectCode="+projectCode);
                        myCameraResult=new Gson().fromJson(data, Sc_myCamera.MyCameraResult.class);
                        int result=myCameraResult.getResult();
                        if(result==1){
                            Log.e("TAG","1-解析错误");
                            id=1;
                            mHandler.sendEmptyMessage(1);
                        }else if(result==100){
                            Log.e("TAG","100-成功");
                            id=1;
                            mHandler.sendEmptyMessage(1);
                            Message message=handler.obtainMessage();
                            message.obj=myCameraResult.getCameraList();
                            handler.sendMessage(message);
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
                                            myCamera();
                                        }else {
                                            showerror();
                                        }
                                    }
                                },IndoorMonitorActivity.this);
                            }else {
                                id=1;
                                showerror();
                            }
                        }else if(result==102){
                            Log.e("TAG","102-projectCode错误");
                            id=1;
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(IndoorMonitorActivity.this,"操作失败");
                        }else {
                            id=1;
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(IndoorMonitorActivity.this,"操作失败");
                        }
                    }

                    @Override
                    public void onError(String data) {
                        ToastUtil.showToast(IndoorMonitorActivity.this,"网络连接失败");
                        id=1;
                        mHandler.sendEmptyMessage(1);
                    }
                });
    }

    private void showerror() {
        AlertDialog.Builder builder = new AlertDialog.Builder(IndoorMonitorActivity.this, 1);
        builder.setTitle("提示");
        builder.setMessage("服务器错误");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startActivity(new Intent(IndoorMonitorActivity.this, LoginActivity.class));
                MainActivity.instance.finish();
                MonitoringActivity.instance.finish();
                finish();
            }
        });
        builder.create().show();
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
        AlertDialog.Builder builder=new AlertDialog.Builder(IndoorMonitorActivity.this,1);
        builder.setTitle("提示");
        builder.setMessage("该账号已被其他设备登录");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startActivity(new Intent(IndoorMonitorActivity.this, LoginActivity.class));
                SharedPreferencesUtils.cleanPassword(IndoorMonitorActivity.this);
                MonitoringActivity.instance.finish();
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
