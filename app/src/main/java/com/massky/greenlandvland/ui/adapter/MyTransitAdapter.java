package com.massky.greenlandvland.ui.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.massky.greenlandvland.R;
import com.massky.greenlandvland.common.CallBackInterface;
import com.massky.greenlandvland.common.CommonUtil;
import com.massky.greenlandvland.common.DialogThridUtils;
import com.massky.greenlandvland.common.GetToken;
import com.massky.greenlandvland.common.SharedPreferencesUtils;
import com.massky.greenlandvland.common.ToastUtil;
import com.massky.greenlandvland.model.entity.Sc_insertDoorRecord;
import com.massky.greenlandvland.model.entity.Sc_myDoor;
import com.massky.greenlandvland.model.httpclient.HttpUrl.HttpClient;
import com.massky.greenlandvland.model.httpclient.UICallback;
import com.massky.greenlandvland.ui.MyVisiterActivity;
import com.massky.ywx.ackpasslibrary.AckpassClass;
import com.massky.ywx.ackpasslibrary.OnOpenDeviceListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by masskywcy on 2017-11-08.
 */

public class MyTransitAdapter extends BaseAdapter {
    protected Context context;
    protected LayoutInflater layoutInflater;
    private List<Sc_myDoor.MyDoorResult.Door> doorList=new ArrayList<>();
    private AckpassClass mAckpassClass;
    private String deviceMac,paramId, paramFloor, paramInout;
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

    private Handler handler=new Handler();
    private String mstatus="";
    private String token;
    private String projectCode;
    private String roomNo;
    private List<Sc_insertDoorRecord.InsertDoorRecordParams.Open> openList=new ArrayList<>();
    private int id=1;
    private int doorId;
    private String floor;
    private String openTime;
    private String openResult;
    private Date d;
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");


    public MyTransitAdapter(Context context, AckpassClass mAckpassClass){
        this.context=context;
        layoutInflater= LayoutInflater.from(context);
        this.mAckpassClass=mAckpassClass;
        mAckpassClass.setOnOpenDeviceListener(mOnOpenDevice);
        token= SharedPreferencesUtils.getToken(context);
        projectCode=SharedPreferencesUtils.getProjectCode(context);
        roomNo=SharedPreferencesUtils.getRoomNo(context);
    }

    public void appendData(List<Sc_myDoor.MyDoorResult.Door> list, boolean isClearOld){
        if(list==null){//没有传过来的这条数据
            return;//结束方法
        }
        if(isClearOld){//是否清除老数据
            doorList.clear();//清空集合
            doorList.addAll(list);//向集合中添加数据
            Log.d("TAG","更新适配器");
        }else{
            doorList.addAll(list);//不清空数据直接添加
        }
    }
    //清空所有数据
    public void clear(){
        doorList.clear();//清空集合
    }
    //更新适配器
    public void upData(){
        this.notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return doorList.size();
    }

    @Override
    public Object getItem(int position) {
        return doorList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            convertView=layoutInflater.inflate(R.layout.item_transit_listview,null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        final Sc_myDoor.MyDoorResult.Door door=doorList.get(position);
        viewHolder.tv_name.setText(door.getDoorName());
        if (door.getType()==1){
            viewHolder.iv_icon.setImageResource(R.drawable.door);
            viewHolder.tv_out.setVisibility(View.GONE);
            viewHolder.tv_in.setText("开");
            viewHolder.tv_in.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog= DialogThridUtils.showWaitDialog(context,true);
                    deviceMac=door.getMac();
                    paramId="11111111";
                    paramFloor="0000";
                    paramInout="0010";
                    Log.e("TAG","deviceMac="+deviceMac+"paramId="+paramId+"paramFloor="+paramFloor+"paramInout="+paramInout);
                    doorId=door.getId();
                    floor=null;
                    openResult="开门成功";
                    mAckpassClass.OpenDevice(deviceMac.toUpperCase(),paramId,paramFloor,paramInout);
                    mstatus="";
                    noGet();


                }
            });
        }else if(door.getType()==2){
            viewHolder.iv_icon.setImageResource(R.drawable.swinggate);
            viewHolder.tv_in.setVisibility(View.VISIBLE);
            viewHolder.tv_out.setVisibility(View.VISIBLE);
            viewHolder.tv_in.setText("进");
            viewHolder.tv_out.setText("出");
            viewHolder.tv_in.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog= DialogThridUtils.showWaitDialog(context,true);
                    deviceMac=door.getMac();
                    paramId="11111111";
                    paramFloor="0000";
                    paramInout="0001";
                    doorId=door.getId();
                    floor=null;
                    openResult="进闸成功";
                    mAckpassClass.OpenDevice(deviceMac.toUpperCase(),paramId,paramFloor,paramInout);
                    mstatus="";
                    noGet();


                }
            });
            viewHolder.tv_out.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog= DialogThridUtils.showWaitDialog(context,true);
                    deviceMac=door.getMac();
                    paramId="11111111";
                    paramFloor="0000";
                    paramInout="0000";
                    doorId=door.getId();
                    floor=null;
                    openResult="出闸成功";
                    mAckpassClass.OpenDevice(deviceMac.toUpperCase(),paramId,paramFloor,paramInout);
                    mstatus="";
                    noGet();


                }
            });
        }else if(door.getType()==3){
            viewHolder.iv_icon.setImageResource(R.drawable.carbarn);
            viewHolder.tv_in.setVisibility(View.VISIBLE);
            viewHolder.tv_out.setVisibility(View.VISIBLE);
            viewHolder.tv_in.setText("开");
            viewHolder.tv_out.setText("关");
            viewHolder.tv_in.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog= DialogThridUtils.showWaitDialog(context,true);
                    deviceMac=door.getMac();
                    paramId="11111111";
                    paramFloor="0000";
                    paramInout="0001";
                    doorId=door.getId();
                    floor=null;
                    openResult="进库成功";
                    mAckpassClass.OpenDevice(deviceMac.toUpperCase(),paramId,paramFloor,paramInout);
                    mstatus="";
                    noGet();


                }
            });
            viewHolder.tv_out.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog= DialogThridUtils.showWaitDialog(context,true);
                    deviceMac=door.getMac();
                    paramId="11111111";
                    paramFloor="0000";
                    paramInout="0000";
                    doorId=door.getId();
                    floor=null;
                    openResult="出库成功";
                    mAckpassClass.OpenDevice(deviceMac.toUpperCase(),paramId,paramFloor,paramInout);
                    mstatus="";
                    noGet();


                }
            });

        }else if(door.getType()==6){
            viewHolder.iv_icon.setImageResource(R.drawable.lift);
            viewHolder.tv_name.setText(door.getDoorName());
            String floor=door.getFloor();
            List<String> floors= Arrays.asList(floor.split(","));
            viewHolder.tv_in.setText(floors.get(0));
            if(floors.size()==1){
                viewHolder.tv_out.setVisibility(View.GONE);
            }else {
                viewHolder.tv_out.setText("更多");
            }
        }
        return convertView;
    }

    private void noGet() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               if(mstatus.equals("")){
                   mHandler.sendEmptyMessage(1);
                   ToastUtil.showToast(context,"连接失败，打开失败");
               }
            }
        },10000);
    }

    class ViewHolder{
        private ImageView iv_icon;
        private TextView tv_name;
        private TextView tv_in;
        private TextView tv_out;
        public ViewHolder(View view){
            this.iv_icon= (ImageView) view.findViewById(R.id.iv_icon);
            this.tv_name= (TextView) view.findViewById(R.id.tv_name);
            this.tv_in= (TextView) view.findViewById(R.id.tv_in);
            this.tv_out= (TextView) view.findViewById(R.id.tv_out);
        }
    }

    private OnOpenDeviceListener mOnOpenDevice= new OnOpenDeviceListener() {
        @Override
        public void OnOpenDevice(int status) {
            mstatus=status+"";

            if (status==0) {
                d=new Date(System.currentTimeMillis());
                openTime=sf.format(d);
                insertDoor();
            } else {
                mHandler.sendEmptyMessage(1);
                Toast.makeText(context, "打开失败 "+ String.valueOf(status), Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void insertDoor() {
        openList.clear();
        openList.add(new Sc_insertDoorRecord.InsertDoorRecordParams.Open(doorId,floor,openTime,openResult));

        Log.e("TAG","openList="+openList);
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
                            Toast.makeText(context, openResult, Toast.LENGTH_SHORT).show();
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
                                },context);
                            }else {
                                id=1;
                                showerror();
                            }
                        }else if(result==102){
                            Log.e("TAG", "102-projectCode错误");
                            id=1;
                            mHandler.sendEmptyMessage(1);
                            Toast.makeText(context, "开门失败", Toast.LENGTH_SHORT).show();
                        }else{
                            id=1;
                            Toast.makeText(context, "开门失败", Toast.LENGTH_SHORT).show();
                            mHandler.sendEmptyMessage(1);
                        }
                    }

                    @Override
                    public void onError(String data) {
                        id=1;
                        mHandler.sendEmptyMessage(1);
                        ToastUtil.showToast(context,"网络操作失败");
                    }
                });
    }

    private void showerror() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, 1);
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
