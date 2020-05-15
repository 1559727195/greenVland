package com.massky.greenlandvland.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.massky.greenlandvland.R;
import com.massky.greenlandvland.common.CallBackInterface;
import com.massky.greenlandvland.common.CommonUtil;
import com.massky.greenlandvland.common.DialogThridUtils;
import com.massky.greenlandvland.common.GetToken;
import com.massky.greenlandvland.common.SharedPreferencesUtils;
import com.massky.greenlandvland.common.ToastUtil;
import com.massky.greenlandvland.model.entity.Sc_deviceControl;
import com.massky.greenlandvland.model.entity.Sc_myRoom;
import com.massky.greenlandvland.model.entity.Sc_myRoomDevice;
import com.massky.greenlandvland.model.httpclient.HttpUrl.HttpClient;
import com.massky.greenlandvland.model.httpclient.UICallback;


/**
 * Created by masskywcy on 2018-11-13.
 */

public class Fragment_content extends Fragment {
    private View view;
    private Button[] button;
    //    private Button btn_1,btn_2,btn_3,btn_4;
    private LinearLayout viewgroup;
    private LinearLayout ll_led_detail, ll_curtains_detail, ll_dimmer_detail;
    private Button bt_upled, bt_upcurtains, bt_updimmerled;
    private ImageView led, iv_led;
    private ImageView iv_curtains;
    private Button clothcurtains, lacecurtains;
    private Button iv_close, iv_stop, iv_start;
    private ImageView iv_dimmer;
    private TextView tv_dimmer;
    private SeekBar seekBar;
    private Dialog mDialog;
    private Sc_myRoom.MyRoomResult.RoomListBean scenes;
    private String token;
    private String projectCode;
    private String deviceId;
    private String boxNumber;
    private int id = 1;
    private boolean isledSelect;
    private boolean isairSelect;
    private boolean isdimmerSelect;
    private boolean isclothSelect = false;
    private boolean islaceSelect = false;
    private int statues;
    private String dimmer;
    private AnimationDrawable animationDrawable;
    Sc_myRoomDevice.MyRoomDeviceResult.DeviceInfoBean deviceInfo;

    public static Fragment_content newInstance(Sc_myRoom.MyRoomResult.RoomListBean scenes) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("scenes", scenes);
        Fragment_content fragment = new Fragment_content();
        fragment.setArguments(bundle);
        return fragment;
    }

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

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            deviceInfo = (Sc_myRoomDevice.MyRoomDeviceResult.DeviceInfoBean) msg.obj;
            switch (msg.what) {
                case 1://灯
                    ll_led_detail.setVisibility(View.VISIBLE);
                    if (deviceInfo.getStatus() == 1) {
                        led.setSelected(true);
                        iv_led.setSelected(true);
                        isledSelect = true;
                    } else if (deviceInfo.getStatus() == 0) {
                        led.setSelected(false);
                        iv_led.setSelected(false);
                        isledSelect = false;
                    }
                    break;
                case 2://调光灯
                    ll_dimmer_detail.setVisibility(View.VISIBLE);
                    if (dimmer == null) {
                        dimmer = deviceInfo.getDimmer();
                    }
                    if (deviceInfo.getStatus() == 1) {
                        iv_dimmer.setSelected(true);
                        seekBar.setProgress(Integer.parseInt(dimmer));
                        tv_dimmer.setText(dimmer);
                        isdimmerSelect = true;
                        seekBar.setEnabled(isdimmerSelect);
                    } else if (deviceInfo.getStatus() == 0) {
                        iv_dimmer.setSelected(false);
                        tv_dimmer.setText(" ");
                        seekBar.setProgress(Integer.parseInt(dimmer));
                        isdimmerSelect = false;
                        seekBar.setEnabled(isdimmerSelect);
                    }
                    break;
//                case 3://空调
//                    ll_aircontrol_detail.setVisibility(View.VISIBLE);
//                    if(deviceInfo.getStatus()==1){
//                        iv_aironoff.setSelected(true);
//                        tv_temperature.setText(deviceInfo.getTemperature());
//                        isairSelect=true;
//                    }else if(deviceInfo.getStatus()==0){
//                        iv_aironoff.setSelected(false);
//                        tv_temperature.setText(" ");
//                        isairSelect=false;
//                    }
//                    break;
                case 4://窗帘
                    ll_curtains_detail.setVisibility(View.VISIBLE);
                    iv_curtains.setImageResource(R.drawable.c1);
                    clothcurtains.setEnabled(true);
                    lacecurtains.setEnabled(true);
                    iv_close.setEnabled(true);
                    iv_start.setEnabled(true);
                    iv_stop.setEnabled(true);
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        scenes = (Sc_myRoom.MyRoomResult.RoomListBean) bundle.getSerializable("scenes");
//        Log.e("TAG","11111="+scenes);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_content, container, false);
//        List<Sc_myRoom.MyRoomResult.Scene> roomList= SharedPreferencesUtils.getMyRoom(getActivity(), Sc_myRoom.MyRoomResult.Scene.class);
//        Log.e("TAG","roomList="+roomList);
//        Log.e("TAG","22222="+scenes);
        token = SharedPreferencesUtils.getToken(getContext());
        projectCode = SharedPreferencesUtils.getProjectCode(getContext());
        boxNumber = SharedPreferencesUtils.getBoxNumber(getContext());
        init();//初始化控件

        viewgroup = view.findViewById(R.id.viewgroup);
        button = new Button[scenes.getDeviceList().size()];
        for (int i = 0; i < scenes.getDeviceList().size(); i++) {
            button[i] = new Button(getActivity());
            button[i].setBackgroundResource(R.drawable.bg_yuan);
            button[i].setText(scenes.getDeviceList().get(i).getDeviceName());
            button[i].setTextSize(15);
            button[i].setTextColor(Color.WHITE);
            final int finalI = i;
            button[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deviceId = scenes.getDeviceList().get(finalI).getDeviceId();
                    myRoomDevice();
                    mDialog = DialogThridUtils.showWaitDialog(getContext(), true);
                }
            });

            LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lParams.gravity = Gravity.CENTER_HORIZONTAL;
            lParams.topMargin = 50;
            viewgroup.addView(button[i], lParams);
        }
        return view;
    }

    private void myRoomDevice() {
        HttpClient.post(CommonUtil.APPURL, "sc_myRoomDevice"
                , new Gson().toJson(new Sc_myRoomDevice.MyRoomDeviceParams(token, projectCode, deviceId))
                , new UICallback() {
                    @Override
                    public void process(String data) {
//                                            Log.e("TAG","data="+data);
//                                            Log.e("TAG","token="+token);
//                                            Log.e("TAG","projectCode="+projectCode);
//                                            Log.e("TAG","deviceId="+deviceId);
                        Sc_myRoomDevice.MyRoomDeviceResult myRoomDeviceResult = new Gson().fromJson(data, Sc_myRoomDevice.MyRoomDeviceResult.class);
                        String result = myRoomDeviceResult.getResult();
                        if (result.equals("1")) {
                            Log.e("TAG", "1-解析错误");
                            mHandler.sendEmptyMessage(1);
                            id = 1;
                        } else if (result.equals("100")) {
                            Log.e("TAG", "100-成功");
                            id = 1;
//                                                SharedPreferencesUtils.saveDimmer(getContext(), myRoomDeviceResult.getDeviceInfo().getDimmer());
                            Message message = new Message();
                            message.what = myRoomDeviceResult.getDeviceInfo().getType();
                            message.obj = myRoomDeviceResult.getDeviceInfo();
                            handler.sendMessage(message);
                            for (int i = 0; i < scenes.getDeviceList().size(); i++) {
                                button[i].setVisibility(View.GONE);
                            }
                            mHandler.sendEmptyMessage(1);
                        } else if (result.equals("101")) {
                            Log.e("TAG", "101-token错误");
                            mHandler.sendEmptyMessage(1);
                            if (id == 1) {
                                id = id + 1;
                                new GetToken(new CallBackInterface() {
                                    @Override
                                    public void gettoken(String str) {
                                        token = str;
                                        Log.e("TAG", "token=" + token);
                                        if (!TextUtils.isEmpty(token)) {
                                            myRoomDevice();
                                        } else {
                                            showerror();
                                        }
                                    }
                                }, getContext());
                            } else {
                                id = 1;
                                showerror();
                            }
                        } else if (result.equals("102")) {
                            Log.e("TAG", "102-projectCode错误");
                            id = 1;
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(getContext(), "操作失败");
                        } else if (result.equals("103")) {
                            Log.e("TAG", "103-deviceId错误");
                            id = 1;
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(getContext(), "操作失败");
                        } else {
                            id = 1;
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(getContext(), "操作失败");
                        }
                    }

                    @Override
                    public void onError(String data) {
                        id = 1;
                        mHandler.sendEmptyMessage(1);
                        ToastUtil.showToast(getContext(), "网络连接失败");
                    }
                });
    }

    //初始化控件
    private void init() {
        //获取控件


//        btn_1= (Button) view.findViewById(R.id.btn_1);
//        btn_2= (Button) view.findViewById(R.id.btn_2);
//        btn_3= (Button) view.findViewById(R.id.btn_3);
//        btn_4= (Button) view.findViewById(R.id.btn_4);

        ll_led_detail = view.findViewById(R.id.ll_led_detail);
        ll_curtains_detail = view.findViewById(R.id.ll_curtains_detial);
        ll_dimmer_detail = view.findViewById(R.id.ll_dimmer_detail);

        bt_upled = view.findViewById(R.id.bt_upled);
        bt_upcurtains = view.findViewById(R.id.bt_upcurtains);
        bt_updimmerled = view.findViewById(R.id.bt_updimmerled);
        led = view.findViewById(R.id.led);
        iv_led = view.findViewById(R.id.iv_led);
//        iv_aironoff= (ImageView) view.findViewById(R.id.iv_aironoff);
//        tv_temperature= (TextView) view.findViewById(R.id.tv_temperature);
        iv_curtains = view.findViewById(R.id.iv_curtains);
        clothcurtains = view.findViewById(R.id.clothcurtains);
        lacecurtains = view.findViewById(R.id.lacecurtains);
        iv_close = view.findViewById(R.id.iv_close);
        iv_stop = view.findViewById(R.id.iv_stop);
        iv_start = view.findViewById(R.id.iv_start);
        tv_dimmer = view.findViewById(R.id.tv_dimmer);
        iv_dimmer = view.findViewById(R.id.iv_dimmer);
        seekBar = view.findViewById(R.id.seekBar);
        //添加监听
//        btn_1.setOnClickListener(clickListener);
//        btn_2.setOnClickListener(clickListener);
//        btn_3.setOnClickListener(clickListener);
//        btn_4.setOnClickListener(clickListener);

        ll_led_detail.setOnClickListener(clickListener);
        ll_curtains_detail.setOnClickListener(clickListener);
        ll_dimmer_detail.setOnClickListener(clickListener);
        iv_led.setOnClickListener(clickListener);
//        iv_aironoff.setOnClickListener(clickListener);
        clothcurtains.setOnClickListener(clickListener);
        lacecurtains.setOnClickListener(clickListener);
        iv_close.setOnClickListener(clickListener);
        iv_stop.setOnClickListener(clickListener);
        iv_start.setOnClickListener(clickListener);
        iv_dimmer.setOnClickListener(clickListener);
        bt_upled.setOnClickListener(clickListener);
        bt_upcurtains.setOnClickListener(clickListener);
        bt_updimmerled.setOnClickListener(clickListener);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (isdimmerSelect == true) {
                    tv_dimmer.setText(Integer.toString(progress));
                } else if (isdimmerSelect == false) {
                    tv_dimmer.setText("");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (isdimmerSelect == true) {
                    mDialog = DialogThridUtils.showWaitDialog(getContext(), true);
                    statues = 1;
                    dimmer = tv_dimmer.getText().toString();
                    dimmerControl();
                }
            }
        });
    }

    private void dimmerControl() {
        HttpClient.post(CommonUtil.APPURL, "sc_deviceControl"
                , new Gson().toJson(new Sc_deviceControl.DeviceControlParams(token, projectCode, boxNumber
                        , new Sc_deviceControl.DeviceControlParams.DeviceInfo(deviceInfo.getType(), deviceInfo.getNumber(), statues, dimmer, deviceInfo.getMode(), deviceInfo.getTemperature(), deviceInfo.getSpeed())))
                , new UICallback() {
                    @Override
                    public void process(String data) {
//                                    Log.e("TAG", "data=" + data);
//                                    Log.e("TAG", "token=" + token);
//                                    Log.e("TAG", "projectCode=" + projectCode);
//                                    Log.e("TAG", "boxNumber=" + boxNumber);
                        Sc_deviceControl.DeviceControlResult deviceControlResult = new Gson().fromJson(data, Sc_deviceControl.DeviceControlResult.class);
                        int result = deviceControlResult.getResult();
                        if (result == 1) {
                            Log.e("TAG", "1-解析错误");
                            mHandler.sendEmptyMessage(1);
                            id = 1;
                        } else if (result == 100) {
                            Log.e("TAG", "100-成功");
                            id = 1;
                            mHandler.sendEmptyMessage(1);
                        } else if (result == 101) {
                            Log.e("TAG", "101-token错误");
                            mHandler.sendEmptyMessage(1);
                            if (id == 1) {
                                id = id + 1;
                                new GetToken(new CallBackInterface() {
                                    @Override
                                    public void gettoken(String str) {
                                        token = str;
                                        Log.e("TAG", "token=" + token);
                                        if (!TextUtils.isEmpty(token)) {
                                            dimmerControl();
                                        } else {
                                            showerror();
                                        }
                                    }
                                }, getContext());
                            } else {
                                id = 1;
                                showerror();
                            }
                        } else if (result == 102) {
                            Log.e("TAG", "102-projectCode错误");
                            id = 1;
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(getContext(), "操作失败");
                        } else if (result == 103) {
                            Log.e("TAG", "103-deviceInfo错误");
                            id = 1;
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(getContext(), "操作失败");
                        } else if (result == 104) {
                            Log.e("TAG", "104-boxNumber错误");
                            id = 1;
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(getContext(), "设备断线");
                        } else {
                            id = 1;
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(getContext(), "操作失败");
                        }
                    }

                    @Override
                    public void onError(String data) {
                        id = 1;
                        mHandler.sendEmptyMessage(1);
                        ToastUtil.showToast(getContext(), "网络连接失败");
                    }
                });
    }


    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_led://灯
                    mDialog = DialogThridUtils.showWaitDialog(getContext(), true);
                    if (isledSelect == true) {
                        statues = 0;
                        isledSelect = false;
                    } else if (isledSelect == false) {
                        statues = 1;
                        isledSelect = true;
                    }
                    ledControl();
                    break;

//                case R.id.iv_aironoff:
//                    if(isairSelect==true){
//                        HttpClient.post(CommonUtil.APPURL, "sc_deviceControl"
//                                , new Gson().toJson(new Sc_deviceControl.DeviceControlParams(token, projectCode, boxNumber
//                                        , new Sc_deviceControl.DeviceControlParams.DeviceInfo(deviceInfo.getType(), deviceInfo.getNumber(), 0, deviceInfo.getDimmer(), deviceInfo.getMode(), deviceInfo.getTemperature(), deviceInfo.getSpeed())))
//                                , new UICallback() {
//                                    @Override
//                                    public void process(String data) {
//                                        Log.e("TAG","data="+data);
//                                        Log.e("TAG","token="+token);
//                                        Log.e("TAG","projectCode="+projectCode);
//                                        Log.e("TAG","boxNumber="+boxNumber);
//                                        Sc_deviceControl.DeviceControlResult deviceControlResult=new Gson().fromJson(data, Sc_deviceControl.DeviceControlResult.class);
//                                        int result=deviceControlResult.getResult();
//                                        if(result==1){
//                                            Log.e("TAG","1-解析错误");
//                                        }else if(result==100){
//                                            Log.e("TAG","100-成功");
//                                            iv_aironoff.setSelected(false);
//                                            isairSelect=false;
//                                        }else if(result==101){
//                                            Log.e("TAG","101-token错误");
//                                        }else if(result==102){
//                                            Log.e("TAG","102-projectCode错误");
//                                        }else if(result==103){
//                                            Log.e("TAG","103-deviceInfo错误");
//                                        }else if(result==104){
//                                            Log.e("TAG","104-boxNumber错误");
//                                        }
//                                    }
//                                });
//                    }else if(isairSelect==false){
//                        HttpClient.post(CommonUtil.APPURL, "sc_deviceControl"
//                                , new Gson().toJson(new Sc_deviceControl.DeviceControlParams(token, projectCode, boxNumber
//                                        , new Sc_deviceControl.DeviceControlParams.DeviceInfo(deviceInfo.getType(), deviceInfo.getNumber(), 1, deviceInfo.getDimmer(), deviceInfo.getMode(), deviceInfo.getTemperature(), deviceInfo.getSpeed())))
//                                , new UICallback() {
//                                    @Override
//                                    public void process(String data) {
//                                        Log.e("TAG","data="+data);
//                                        Log.e("TAG","token="+token);
//                                        Log.e("TAG","projectCode="+projectCode);
//                                        Log.e("TAG","boxNumber="+boxNumber);
//                                        Sc_deviceControl.DeviceControlResult deviceControlResult=new Gson().fromJson(data, Sc_deviceControl.DeviceControlResult.class);
//                                        int result=deviceControlResult.getResult();
//                                        if(result==1){
//                                            Log.e("TAG","1-解析错误");
//                                        }else if(result==100){
//                                            Log.e("TAG","100-成功");
//                                            iv_aironoff.setSelected(true);
//                                            isairSelect=true;
//                                        }else if(result==101){
//                                            Log.e("TAG","101-token错误");
//                                        }else if(result==102){
//                                            Log.e("TAG","102-projectCode错误");
//                                        }else if(result==103){
//                                            Log.e("TAG","103-deviceInfo错误");
//                                        }else if(result==104){
//                                            Log.e("TAG","104-boxNumber错误");
//                                        }
//                                    }
//                                });
//                    }
//                    break;
                case R.id.clothcurtains://布帘
                    if (isclothSelect == false) {
                        clothcurtains.setSelected(true);
                        isclothSelect = true;
                    } else if (isclothSelect == true) {
                        clothcurtains.setSelected(false);
                        isclothSelect = false;
                    }
                    break;
                case R.id.lacecurtains://纱帘
                    if (islaceSelect == false) {
                        lacecurtains.setSelected(true);
                        islaceSelect = true;
                    } else if (islaceSelect == true) {
                        lacecurtains.setSelected(false);
                        islaceSelect = false;
                    }
                    break;

                case R.id.iv_close://关闭窗帘
                    if (isclothSelect == true || islaceSelect == true) {
                        mDialog = DialogThridUtils.showWaitDialog(getContext(), true);
                        clothcurtains.setEnabled(false);
                        lacecurtains.setEnabled(false);
                        iv_close.setEnabled(false);
                        iv_start.setEnabled(false);
                        iv_stop.setEnabled(true);
                        if (isclothSelect == true && islaceSelect == false) {
                            statues = 4;
                        } else if (isclothSelect == false && islaceSelect == true) {
                            statues = 6;
                        } else if (isclothSelect == true && islaceSelect == true) {
                            statues = 0;
                        }
                        closecurtains();


                    } else if (isclothSelect == false && islaceSelect == false) {
                        ToastUtil.showToast(getContext(), "请选择窗帘");
                    }

                    break;
                case R.id.iv_stop://窗帘停止
                    if (isclothSelect == true || islaceSelect == true) {
                        mDialog = DialogThridUtils.showWaitDialog(getContext(), true);
                        clothcurtains.setEnabled(true);
                        lacecurtains.setEnabled(true);
                        iv_close.setEnabled(true);
                        iv_start.setEnabled(true);
                        iv_stop.setEnabled(false);
                        if (isclothSelect == true || islaceSelect == true) {
                            statues = 2;
                        }

                        stopCurtains();

                    } else if (isclothSelect == false && islaceSelect == false) {
                        ToastUtil.showToast(getContext(), "请选择窗帘");
                    }

                    break;

                case R.id.iv_start://打开窗帘
                    if (isclothSelect == true || islaceSelect == true) {
                        mDialog = DialogThridUtils.showWaitDialog(getContext(), true);
                        clothcurtains.setEnabled(false);
                        lacecurtains.setEnabled(false);
                        iv_close.setEnabled(false);
                        iv_start.setEnabled(false);
                        iv_stop.setEnabled(true);
                        if (isclothSelect == true && islaceSelect == false) {
                            statues = 3;
                        } else if (isclothSelect == false && islaceSelect == true) {
                            statues = 5;
                        } else if (isclothSelect == true && islaceSelect == true) {
                            statues = 1;
                        }
                        openCurtains();
                    } else if (isclothSelect == false && islaceSelect == false) {
                        ToastUtil.showToast(getContext(), "请选择窗帘");
                    }

                    break;
                case R.id.iv_dimmer://调光灯
                    mDialog = DialogThridUtils.showWaitDialog(getContext(), true);
                    if (isdimmerSelect == true) {
                        statues = 0;
                        isdimmerSelect = false;
                    } else if (isdimmerSelect == false) {
                        statues = 1;
                        isdimmerSelect = true;
                    }

                    dimmerControl();
//                    HttpClient.post(CommonUtil.APPURL, "sc_deviceControl"
//                            , new Gson().toJson(new Sc_deviceControl.DeviceControlParams(token, projectCode, boxNumber
//                                    , new Sc_deviceControl.DeviceControlParams.DeviceInfo(deviceInfo.getType(), deviceInfo.getNumber(), statues, deviceInfo.getDimmer(), deviceInfo.getMode(), deviceInfo.getTemperature(), deviceInfo.getSpeed())))
//                            , new UICallback() {
//                                @Override
//                                public void process(String data) {
////                                    Log.e("TAG", "data=" + data);
////                                    Log.e("TAG", "token=" + token);
////                                    Log.e("TAG", "projectCode=" + projectCode);
////                                    Log.e("TAG", "boxNumber=" + boxNumber);
//                                    Sc_deviceControl.DeviceControlResult deviceControlResult = new Gson().fromJson(data, Sc_deviceControl.DeviceControlResult.class);
//                                    int result = deviceControlResult.getResult();
//                                    if (result == 1) {
//                                        Log.e("TAG", "1-解析错误");
//                                        mHandler.sendEmptyMessage(1);
//                                    } else if (result == 100) {
//                                        Log.e("TAG", "100-成功");
//                                        iv_dimmer.setSelected(isdimmerSelect);
//                                        if (isdimmerSelect == true) {
//                                            tv_dimmer.setText(dimmer);
//                                        } else if (isdimmerSelect == false) {
//                                            tv_dimmer.setText("");
//                                        }
//                                        seekBar.setEnabled(isdimmerSelect);
//                                        mHandler.sendEmptyMessage(1);
//                                    } else if (result == 101) {
//                                        Log.e("TAG", "101-token错误");
//                                        mHandler.sendEmptyMessage(1);
//                                    } else if (result == 102) {
//                                        Log.e("TAG", "102-projectCode错误");
//                                        mHandler.sendEmptyMessage(1);
//                                    } else if (result == 103) {
//                                        Log.e("TAG", "103-deviceInfo错误");
//                                        mHandler.sendEmptyMessage(1);
//                                    } else if (result == 104) {
//                                        Log.e("TAG", "104-boxNumber错误");
//                                        mHandler.sendEmptyMessage(1);
//                                        ToastUtil.showToast(getContext(), "设备断线");
//                                    }
//                                }
//
//                                @Override
//                                public void onError(String data) {
//
//                                }
//                            });
                    break;

                case R.id.bt_upled://灯布局收回
                    ll_led_detail.setVisibility(View.GONE);
                    for (int i = 0; i < scenes.getDeviceList().size(); i++) {
                        button[i].setVisibility(View.VISIBLE);
                    }
                    break;

                case R.id.bt_upcurtains://窗帘布局收回
                    ll_curtains_detail.setVisibility(View.GONE);
                    for (int i = 0; i < scenes.getDeviceList().size(); i++) {
                        button[i].setVisibility(View.VISIBLE);
                    }
                    break;

                case R.id.bt_updimmerled://调光灯布局收回
                    ll_dimmer_detail.setVisibility(View.GONE);
                    for (int i = 0; i < scenes.getDeviceList().size(); i++) {
                        button[i].setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    };

    private void openCurtains() {
        HttpClient.post(CommonUtil.APPURL, "sc_deviceControl"
                , new Gson().toJson(new Sc_deviceControl.DeviceControlParams(token, projectCode, boxNumber
                        , new Sc_deviceControl.DeviceControlParams.DeviceInfo(deviceInfo.getType(), deviceInfo.getNumber(), statues, deviceInfo.getDimmer(), deviceInfo.getMode(), deviceInfo.getTemperature(), deviceInfo.getSpeed())))
                , new UICallback() {
                    @Override
                    public void process(String data) {
//                                                Log.e("TAG", "data=" + data);
//                                                Log.e("TAG", "token=" + token);
//                                                Log.e("TAG", "projectCode=" + projectCode);
//                                                Log.e("TAG", "boxNumber=" + boxNumber);
                        Sc_deviceControl.DeviceControlResult deviceControlResult = new Gson().fromJson(data, Sc_deviceControl.DeviceControlResult.class);
                        int result = deviceControlResult.getResult();
                        if (result == 1) {
                            Log.e("TAG", "1-解析错误");
                            id = 1;
                            mHandler.sendEmptyMessage(1);
                        } else if (result == 100) {
                            Log.e("TAG", "100-成功");
                            id = 1;
                            mHandler.sendEmptyMessage(1);
                            iv_curtains.setBackgroundResource(R.drawable.curtains_start);
                            animationDrawable = (AnimationDrawable) iv_curtains.getBackground();
                            animationDrawable.start();
                            iv_close.setSelected(false);
                            iv_stop.setSelected(false);
                            iv_start.setSelected(true);
                        } else if (result == 101) {
                            Log.e("TAG", "101-token错误");
                            mHandler.sendEmptyMessage(1);
                            if (id == 1) {
                                id = id + 1;
                                new GetToken(new CallBackInterface() {
                                    @Override
                                    public void gettoken(String str) {
                                        token = str;
                                        Log.e("TAG", "token=" + token);
                                        if (!TextUtils.isEmpty(token)) {
                                            openCurtains();
                                        } else {
                                            showerror();
                                        }
                                    }
                                }, getContext());
                            } else {
                                id = 1;
                                showerror();
                            }
                        } else if (result == 102) {
                            Log.e("TAG", "102-projectCode错误");
                            id = 1;
                            mHandler.sendEmptyMessage(1);
                        } else if (result == 103) {
                            Log.e("TAG", "103-deviceInfo错误");
                            id = 1;
                            mHandler.sendEmptyMessage(1);
                        } else if (result == 104) {
                            Log.e("TAG", "104-boxNumber错误");
                            mHandler.sendEmptyMessage(1);
                            id = 1;
                            ToastUtil.showToast(getContext(), "设备断线");
                        } else {
                            id = 1;
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(getContext(), "操作失败");
                        }
                    }

                    @Override
                    public void onError(String data) {
                        id = 1;
                        mHandler.sendEmptyMessage(1);
                        ToastUtil.showToast(getContext(), "网络连接失败");
                    }
                });
    }

    private void stopCurtains() {
        HttpClient.post(CommonUtil.APPURL, "sc_deviceControl"
                , new Gson().toJson(new Sc_deviceControl.DeviceControlParams(token, projectCode, boxNumber
                        , new Sc_deviceControl.DeviceControlParams.DeviceInfo(deviceInfo.getType(), deviceInfo.getNumber(), statues, deviceInfo.getDimmer(), deviceInfo.getMode(), deviceInfo.getTemperature(), deviceInfo.getSpeed())))
                , new UICallback() {
                    @Override
                    public void process(String data) {
//                                                Log.e("TAG", "data=" + data);
//                                                Log.e("TAG", "token=" + token);
//                                                Log.e("TAG", "projectCode=" + projectCode);
//                                                Log.e("TAG", "boxNumber=" + boxNumber);
                        Sc_deviceControl.DeviceControlResult deviceControlResult = new Gson().fromJson(data, Sc_deviceControl.DeviceControlResult.class);
                        int result = deviceControlResult.getResult();
                        if (result == 1) {
                            Log.e("TAG", "1-解析错误");
                            id = 1;
                            mHandler.sendEmptyMessage(1);
                        } else if (result == 100) {
                            Log.e("TAG", "100-成功");
                            id = 1;
                            mHandler.sendEmptyMessage(1);
                            iv_curtains.setImageResource(R.drawable.c15);
                            iv_close.setSelected(true);
                            iv_stop.setSelected(false);
                            iv_start.setSelected(false);
                        } else if (result == 101) {
                            Log.e("TAG", "101-token错误");
                            mHandler.sendEmptyMessage(1);
                            if (id == 1) {
                                id = id + 1;
                                new GetToken(new CallBackInterface() {
                                    @Override
                                    public void gettoken(String str) {
                                        token = str;
                                        Log.e("TAG", "token=" + token);
                                        if (!TextUtils.isEmpty(token)) {
                                            stopCurtains();
                                        } else {
                                            showerror();
                                        }
                                    }
                                }, getContext());
                            } else {
                                id = 1;
                                showerror();
                            }
                        } else if (result == 102) {
                            Log.e("TAG", "102-projectCode错误");
                            id = 1;
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(getContext(), "操作失败");
                        } else if (result == 103) {
                            Log.e("TAG", "103-deviceInfo错误");
                            id = 1;
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(getContext(), "操作失败");
                        } else if (result == 104) {
                            Log.e("TAG", "104-boxNumber错误");
                            mHandler.sendEmptyMessage(1);
                            id = 1;
                            ToastUtil.showToast(getContext(), "设备断线");
                        } else {
                            id = 1;
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(getContext(), "操作失败");
                        }
                    }

                    @Override
                    public void onError(String data) {
                        id = 1;
                        mHandler.sendEmptyMessage(1);
                        ToastUtil.showToast(getContext(), "网络连接失败");
                    }
                });
    }

    private void closecurtains() {
        HttpClient.post(CommonUtil.APPURL, "sc_deviceControl"
                , new Gson().toJson(new Sc_deviceControl.DeviceControlParams(token, projectCode, boxNumber
                        , new Sc_deviceControl.DeviceControlParams.DeviceInfo(deviceInfo.getType(), deviceInfo.getNumber(), statues, deviceInfo.getDimmer(), deviceInfo.getMode(), deviceInfo.getTemperature(), deviceInfo.getSpeed())))
                , new UICallback() {
                    @Override
                    public void process(String data) {
//                                        Log.e("TAG", "data=" + data);
//                                        Log.e("TAG", "token=" + token);
//                                        Log.e("TAG", "projectCode=" + projectCode);
//                                        Log.e("TAG", "boxNumber=" + boxNumber);
                        Sc_deviceControl.DeviceControlResult deviceControlResult = new Gson().fromJson(data, Sc_deviceControl.DeviceControlResult.class);
                        int result = deviceControlResult.getResult();
                        if (result == 1) {
                            Log.e("TAG", "1-解析错误");
                            mHandler.sendEmptyMessage(1);
                            id = 1;
                        } else if (result == 100) {
                            Log.e("TAG", "100-成功");
                            id = 1;
                            mHandler.sendEmptyMessage(1);
                            iv_curtains.setBackgroundResource(R.drawable.curtains_close);
                            animationDrawable = (AnimationDrawable) iv_curtains.getBackground();
                            animationDrawable.start();
                            iv_close.setSelected(true);
                            iv_stop.setSelected(false);
                            iv_start.setSelected(false);
                        } else if (result == 101) {
                            Log.e("TAG", "101-token错误");
                            mHandler.sendEmptyMessage(1);
                            if (id == 1) {
                                id = id + 1;
                                new GetToken(new CallBackInterface() {
                                    @Override
                                    public void gettoken(String str) {
                                        token = str;
                                        Log.e("TAG", "token=" + token);
                                        if (!TextUtils.isEmpty(token)) {
                                            closecurtains();
                                        } else {
                                            showerror();
                                        }
                                    }
                                }, getContext());
                            } else {
                                id = 1;
                                showerror();
                            }
                        } else if (result == 102) {
                            Log.e("TAG", "102-projectCode错误");
                            id = 1;
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(getContext(), "操作失败");
                        } else if (result == 103) {
                            Log.e("TAG", "103-deviceInfo错误");
                            id = 1;
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(getContext(), "操作失败");
                        } else if (result == 104) {
                            Log.e("TAG", "104-boxNumber错误");
                            id = 1;
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(getContext(), "设备断线");
                        } else {
                            id = 1;
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(getContext(), "操作失败");
                        }
                    }

                    @Override
                    public void onError(String data) {
                        id = 1;
                        mHandler.sendEmptyMessage(1);
                        ToastUtil.showToast(getContext(), "网络连接失败");
                    }
                });
    }

    private void ledControl() {
        HttpClient.post(CommonUtil.APPURL, "sc_deviceControl"
                , new Gson().toJson(new Sc_deviceControl.DeviceControlParams(token, projectCode, boxNumber
                        , new Sc_deviceControl.DeviceControlParams.DeviceInfo(deviceInfo.getType(), deviceInfo.getNumber(), statues, deviceInfo.getDimmer(), deviceInfo.getMode(), deviceInfo.getTemperature(), deviceInfo.getSpeed())))
                , new UICallback() {
                    @Override
                    public void process(String data) {
//                                    Log.e("TAG", "data=" + data);
//                                    Log.e("TAG", "token=" + token);
//                                    Log.e("TAG", "projectCode=" + projectCode);
//                                    Log.e("TAG", "boxNumber=" + boxNumber);
                        Sc_deviceControl.DeviceControlResult deviceControlResult = new Gson().fromJson(data, Sc_deviceControl.DeviceControlResult.class);
                        int result = deviceControlResult.getResult();
                        if (result == 1) {
                            Log.e("TAG", "1-解析错误");
                            mHandler.sendEmptyMessage(1);
                        } else if (result == 100) {
                            Log.e("TAG", "100-成功");
                            mHandler.sendEmptyMessage(1);
                            led.setSelected(isledSelect);
                            iv_led.setSelected(isledSelect);
                        } else if (result == 101) {
                            Log.e("TAG", "101-token错误");
                            mHandler.sendEmptyMessage(1);
                            if (id == 1) {
                                id = id + 1;
                                new GetToken(new CallBackInterface() {
                                    @Override
                                    public void gettoken(String str) {
                                        token = str;
                                        Log.e("TAG", "token=" + token);
                                        if (!TextUtils.isEmpty(token)) {
                                            ledControl();
                                        } else {
                                            showerror();
                                        }
                                    }
                                }, getContext());
                            } else {
                                id = 1;
                                showerror();
                            }
                        } else if (result == 102) {
                            Log.e("TAG", "102-projectCode错误");
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(getContext(), "操作失败");
                        } else if (result == 103) {
                            Log.e("TAG", "103-deviceInfo错误");
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(getContext(), "操作失败");
                        } else if (result == 104) {
                            Log.e("TAG", "104-boxNumber错误");
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(getContext(), "设备断线");
                        } else {
                            id = 1;
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(getContext(), "操作失败");
                        }
                    }

                    @Override
                    public void onError(String data) {
                        id = 1;
                        mHandler.sendEmptyMessage(1);
                        ToastUtil.showToast(getContext(), "网络连接失败");
                    }
                });
    }

    private void showerror() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), 1);
        builder.setTitle("提示");
        builder.setMessage("服务器错误");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ((MyHouseActivity) getContext()).finish();
            }
        });
        builder.create().show();
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//
//        if (isVisibleToUser){
//            Bundle bundle = getArguments();
//            if(bundle!=null){
//                scenes= (Sc_myRoom.MyRoomResult.Scene) bundle.getSerializable("scenes");
//                Log.e("TAG","scenes="+scenes);
//            }
//        }else {
//
//        }
//        super.setUserVisibleHint(isVisibleToUser);
//    }

//    public void startAnimationDrawable(){
//        //创建帧动画
//        AnimationDrawable animationDrawable = new AnimationDrawable();
//        //添加帧
//        animationDrawable.addFrame(getResources().getDrawable(R.drawable.c1),100);
//        animationDrawable.addFrame(getResources().getDrawable(R.drawable.c2),100);
//        animationDrawable.addFrame(getResources().getDrawable(R.drawable.c3),100);
//        animationDrawable.addFrame(getResources().getDrawable(R.drawable.c4),100);
//        animationDrawable.addFrame(getResources().getDrawable(R.drawable.c5),100);
//        animationDrawable.addFrame(getResources().getDrawable(R.drawable.c6),100);
//        animationDrawable.addFrame(getResources().getDrawable(R.drawable.c7),100);
//        animationDrawable.addFrame(getResources().getDrawable(R.drawable.c8),100);
//        animationDrawable.addFrame(getResources().getDrawable(R.drawable.c9),100);
//        animationDrawable.addFrame(getResources().getDrawable(R.drawable.c10),100);
//        animationDrawable.addFrame(getResources().getDrawable(R.drawable.c11),100);
//        animationDrawable.addFrame(getResources().getDrawable(R.drawable.c12),100);
//        animationDrawable.addFrame(getResources().getDrawable(R.drawable.c13),100);
//        animationDrawable.addFrame(getResources().getDrawable(R.drawable.c14),100);
//        animationDrawable.addFrame(getResources().getDrawable(R.drawable.c15),100);
//        animationDrawable.addFrame(getResources().getDrawable(R.drawable.c16),100);
//        animationDrawable.addFrame(getResources().getDrawable(R.drawable.c17),100);
//        animationDrawable.addFrame(getResources().getDrawable(R.drawable.c18),100);
//        animationDrawable.addFrame(getResources().getDrawable(R.drawable.c19),100);
//        animationDrawable.addFrame(getResources().getDrawable(R.drawable.c20),100);
//        animationDrawable.addFrame(getResources().getDrawable(R.drawable.c21),100);
//        animationDrawable.addFrame(getResources().getDrawable(R.drawable.c22),100);
//        animationDrawable.addFrame(getResources().getDrawable(R.drawable.c23),100);
//        animationDrawable.addFrame(getResources().getDrawable(R.drawable.c24),100);
//        animationDrawable.addFrame(getResources().getDrawable(R.drawable.c25),100);
//        animationDrawable.addFrame(getResources().getDrawable(R.drawable.c26),100);
//        animationDrawable.addFrame(getResources().getDrawable(R.drawable.c27),100);
//        animationDrawable.addFrame(getResources().getDrawable(R.drawable.c28),100);
//        //设置动画是否只播放一次， 默认是false
//        animationDrawable.setOneShot(false);
//        //根据索引获取到那一帧的时长
//        int duration = animationDrawable.getDuration(2);
//        //根据索引获取到那一帧的图片
//        Drawable drawable = animationDrawable.getFrame(0);
//        //判断是否是在播放动画
//        boolean isRunning = animationDrawable.isRunning();
//        //获取这个动画是否只播放一次
//        boolean isOneShot = animationDrawable.isOneShot();
//        //获取到这个动画一共播放多少帧
////        int framesCount = animationDrawable.getNumberOfFrames();
//        //把这个动画设置为background，兼容更多版本写下面那句
//        iv_curtains.setBackground(animationDrawable);
//        iv_curtains.setBackgroundDrawable(animationDrawable);
//        //开始播放动画
//        animationDrawable.start();
//        //停止播放动画
//        animationDrawable.stop();
//    }
}
