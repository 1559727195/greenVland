package com.massky.greenlandvland.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.massky.greenlandvland.R;
import com.massky.greenlandvland.common.CallBackInterface;
import com.massky.greenlandvland.common.CommonUtil;
import com.massky.greenlandvland.common.DialogThridUtils;
import com.massky.greenlandvland.common.GetToken;
import com.massky.greenlandvland.common.SharedPreferencesUtils;
import com.massky.greenlandvland.common.ToastUtil;
import com.massky.greenlandvland.model.entity.Sc_index;
import com.massky.greenlandvland.model.entity.Sc_insertDoorRecord;
import com.massky.greenlandvland.model.entity.Sc_myAccount;
import com.massky.greenlandvland.model.entity.Sc_myDoor;
import com.massky.greenlandvland.model.entity.Sc_newLoginNew;
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

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Created by masskywcy on 2018-11-08.
 */

public class FragmentMain extends Fragment {
    private String data[];
    private MainActivity mainActivity;//当前fragment绑定的Activity
    private View view;//Fragment要加载的页面
    private RoundedImageView riv_intelligenceenter, riv_invitevisitor,riv_monitoring,riv_environmentmonitor;
    private ImageView iv_opendoor;//快捷开门按钮
    private PopupWindow popupWindow;
    private ImageView sliding_menu;//侧滑按钮
    private Spinner spinner_main;
    private int page_index;
    private String projectCode;
    private String token;
    private String roomNo;
    private int id=1;
    private ListView listView;
    private MyTransitAdapter adapter;
    public AckpassClass mAckpassClass;
    private List<Sc_myDoor.MyDoorResult.Door> doorList;
    private List<Sc_myDoor.MyDoorResult.Door> list = new ArrayList<>();
    private List<Sc_myDoor.MyDoorResult.Door> doors;
    private List<Sc_newLoginNew.NewLoginNewResult.ProjectRoomType> projectRoomType;
    private List<Sc_newLoginNew.NewLoginNewResult.ProjectRoomType.RoomNoAndTypeArray> roomNoAndTypeArrayList;
    private List images=new ArrayList();
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
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
    private int [] background={R.drawable.bg_smart_homes,R.drawable.safe_location,R.drawable.bg_home_jiankong,R.drawable.bg_intelligenceenter,R.drawable.bg_invitevisitor,R.drawable.bg_monitor};
    Bitmap smallBitmap1,smallBitmap2,smallBitmap3,smallBitmap4;
    Handler handler = new Handler();
    private String mstatus="";
    List<Sc_insertDoorRecord.InsertDoorRecordParams.Open> openList=new ArrayList<>();
    private int doorId;
    private String floor=null;
    private String openTime;
    private String openResult;
    private Date d;
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private int accountType;

    private String realName;
    private String nickName;
    private String avatar;
    private String gender;
    private String birthday;
    private String mobilePhone;
    private String address;
    private int family;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainActivity = (MainActivity) getActivity();//获取绑定Activity
        view = inflater.inflate(R.layout.fragment_home, container, false);

//        imageUtils=new LoadImageUtils(getActivity(),this);
        token = SharedPreferencesUtils.getToken(getContext());

        projectRoomType=SharedPreferencesUtils.getProjectRoomType(getContext(), Sc_newLoginNew.NewLoginNewResult.ProjectRoomType.class);
        Log.e("TAG","projectRoomType="+projectRoomType);

        projectCode = projectRoomType.get(0).getProjectCode();
        SharedPreferencesUtils.saveProjectCode(getContext(), projectCode);
        Log.e("TAG","projectCode="+projectCode);
        initSpinner();//初始化spinner


        //获取控件

        riv_intelligenceenter = (RoundedImageView) view.findViewById(R.id.riv_intelligenceenter);
        riv_invitevisitor = (RoundedImageView) view.findViewById(R.id.riv_invitevisitor);
        riv_monitoring = (RoundedImageView) view.findViewById(R.id.riv_monitoring);
        riv_environmentmonitor=view.findViewById(R.id.riv_environmentmonitor);
        iv_opendoor = (ImageView) view.findViewById(R.id.iv_opendoor);
        sliding_menu = (ImageView) view.findViewById(R.id.sliding_menu);

//        if(!SystemUtils.getInstance(getActivity()).isNetConnection()){
        //裁剪图片
//        initCropPicture();
//        }else {
//
//        }


        iv_opendoor.setClickable(true);

        //添加监听器
        riv_intelligenceenter.setOnClickListener(clickListener);
        riv_invitevisitor.setOnClickListener(clickListener);
        riv_monitoring.setOnClickListener(clickListener);
        riv_environmentmonitor.setOnClickListener(clickListener);
        iv_opendoor.setOnClickListener(clickListener);
        sliding_menu.setOnClickListener(clickListener);


        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), background[0]);
                smallBitmap1 = Bitmap.createBitmap(bitmap1, 0, 0, bitmap1.getWidth(), bitmap1.getHeight() / 6);

                Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), background[1]);
                smallBitmap2 = Bitmap.createBitmap(bitmap2, 0, 0, bitmap2.getWidth(), bitmap2.getHeight() / 6);

                Bitmap bitmap3 = BitmapFactory.decodeResource(getResources(), background[2]);
                smallBitmap3 = Bitmap.createBitmap(bitmap3, 0, 0, bitmap3.getWidth(), bitmap3.getHeight() / 6);

                Bitmap bitmap4 = BitmapFactory.decodeResource(getResources(), background[3]);
                smallBitmap4 = Bitmap.createBitmap(bitmap4, 0, 0, bitmap4.getWidth(), bitmap4.getHeight() / 6);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initCropPicture();
                    }
                });
            }
        }).start();
        return view;
    }

    //裁剪图片
    private void initCropPicture() {
//        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.bg_intelligenceenter);
//        Bitmap smallBitmap1 = Bitmap.createBitmap(bitmap1, 0, 0, bitmap1.getWidth(), bitmap1.getHeight() / 6);
        riv_intelligenceenter.setImageBitmap(smallBitmap1);

//        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.bg_invitevisitor);
//        Bitmap smallBitmap2 = Bitmap.createBitmap(bitmap2, 0, 0, bitmap2.getWidth(), bitmap2.getHeight() / 6);
        riv_invitevisitor.setImageBitmap(smallBitmap2);

//        Bitmap bitmap3 = BitmapFactory.decodeResource(getResources(), R.drawable.bg_homemonitoring);
//        Bitmap smallBitmap3 = Bitmap.createBitmap(bitmap3, 0, 0, bitmap3.getWidth(), bitmap3.getHeight() / 6);
        riv_monitoring.setImageBitmap(smallBitmap3);

//        Bitmap bitmap4 = BitmapFactory.decodeResource(getResources(), R.drawable.safe_location);
//        Bitmap smallBitmap4 = Bitmap.createBitmap(bitmap4, 0, 0, bitmap4.getWidth(), bitmap4.getHeight() / 6);
        riv_environmentmonitor.setImageBitmap(smallBitmap4);

    }

    private void initSpinner() {
        spinner_main = view.findViewById(R.id.spinner_main);
        int layout = R.layout.simple_spinner_item;

        roomNoAndTypeArrayList=projectRoomType.get(0).getRoomNoAndTypeArray();
        Log.e("TAG","roomNoAndTypeArrayList="+roomNoAndTypeArrayList);


        data = new String[roomNoAndTypeArrayList.size()];
        for (int i = 0; i < roomNoAndTypeArrayList.size(); i++) {
            data[i] = roomNoAndTypeArrayList.get(i).getRoomNoName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), layout, data);
        adapter.setDropDownViewResource(R.layout.my_drop_down_item);
        spinner_main.setAdapter(adapter);


        spinner_main.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                roomNo=roomNoAndTypeArrayList.get(position).getRoomNo();
                SharedPreferencesUtils.saveRoomNo(getContext(),roomNo);
                Log.e("TAG","roomNo="+roomNo);
                accountType=roomNoAndTypeArrayList.get(position).getAccountType();
                SharedPreferencesUtils.saveAccountType(getContext(),accountType);
                Log.e("TAG","accountType="+accountType);

                //我的账号：下载个人信息
                initMyAccount();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_main.setSelection(0,false);
    }

    //我的账号：下载个人信息
    private void initMyAccount() {
        mDialog=DialogThridUtils.showWaitDialog(getContext(),true);
        HttpClient.post(CommonUtil.APPURL, "sc_myAccount"
                , new Gson().toJson(new Sc_myAccount.MyAccountParams(token, projectCode, roomNo))
                , new UICallback() {
                    @Override
                    public void process(String data) {
                        Log.e("TAG","myAccountResult="+data);
//                        Log.e("TAG","token="+SharedPreferencesUtils.getToken(MainActivity.this));
//                        Log.e("TAG","projectCode="+SharedPreferencesUtils.getProjectCode(MainActivity.this));
                        Sc_myAccount.MyAccountResult myAccountResult = new Gson().fromJson(data, Sc_myAccount.MyAccountResult.class);

                        int result = myAccountResult.getResult();
                        if(result==1){
                            Log.e("TAG", "1-json解析错误");
                            id=1;
                            mHandler.sendEmptyMessage(1);
                        }else if(result==100){
                            Log.e("TAG", "100-成功");
                            id=1;
                            realName=myAccountResult.getAccountInfo().getUserName()+"";
                            nickName=myAccountResult.getAccountInfo().getNickName()+"";
                            avatar=myAccountResult.getAccountInfo().getAvatar()+"";
                            gender=myAccountResult.getAccountInfo().getGender()+"";
                            birthday=myAccountResult.getAccountInfo().getBirthday()+"";
                            mobilePhone=myAccountResult.getAccountInfo().getMobilePhone()+"";
                            address=myAccountResult.getAccountInfo().getAddress()+"";
                            family=myAccountResult.getAccountInfo().getFamily();



                            SharedPreferencesUtils.saveUserName(getContext(),realName);
                            SharedPreferencesUtils.saveNickName(getContext(),nickName);
                            SharedPreferencesUtils.saveAvatar(getContext(),avatar);
                            SharedPreferencesUtils.saveGender(getContext(),gender);
                            SharedPreferencesUtils.saveBirthday(getContext(),birthday);
                            SharedPreferencesUtils.saveMobilePhone(getContext(),mobilePhone);
                            SharedPreferencesUtils.saveAddress(getContext(),address);
                            SharedPreferencesUtils.saveFamily(getContext(),family);

                            initGetmyDoor();
                        } else if (result == 101) {
                            Log.e("TAG", "101-token错误");
                            mHandler.sendEmptyMessage(1);
                            if(id==1){
                                id=id+1;
                                new GetToken(new CallBackInterface() {
                                    @Override
                                    public void gettoken(String str) {
                                        token=str;
                                        Log.e("TAG","token="+token);
                                        if(!TextUtils.isEmpty(token)){
                                            initMyAccount();
                                        }else {
                                            showerror();
                                        }
                                    }
                                },getContext());
                            }else {
                                id=1;
                                showerror();
                            }
                        } else if (result == 102) {
                            Log.e("TAG", "102-projectCode错误");
                            id=1;
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(getContext(),"操作失败");
                        }else {
                            id=1;
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(getContext(),"操作失败");
                        }
                    }

                    @Override
                    public void onError(String data) {
                        ToastUtil.showToast(getContext(), "网络连接异常");
                        mHandler.sendEmptyMessage(1);
                        id=1;
                    }
                });
    }

    private void getIndexDate() {
        HttpClient.post(CommonUtil.APPURL, "sc_index"
                , new Gson().toJson(new Sc_index.IndexParams(token, projectCode))
                , new UICallback() {
                    @Override
                    public void process(String data) {
                        Log.e("TAG","data="+data);
//                                        Log.e("TAG","token="+token);
//                                        Log.e("TAG","projectCode="+projectCode);
                        Sc_index.IndexResult indexResult = new Gson().fromJson(data, Sc_index.IndexResult.class);
                        int result = indexResult.getResult();
                        if (result == 1) {
                            Log.e("TAG", "1-解析错误");
//                            mHandler.sendEmptyMessage(1);
                            id=1;
                        } else if (result == 100) {
                            Log.e("TAG", "100-成功");
//                            mHandler.sendEmptyMessage(1);
                            id=1;
                            for (int i=0;i<indexResult.getAppMenuList().size();i++){
                                images.add(indexResult.getAppMenuList().get(i).getMenuImage());
                            }
//                            Log.e("TAG","1111="+indexResult.getAppMenuList().get(0).getMenuImage());
//                            Log.e("TAG","images="+images);
                            loadImage();//加载图片

                        } else if (result == 101) {
                            Log.e("TAG", "101-token错误");
//                            mHandler.sendEmptyMessage(1);
                            if(id==1){
                                id=id+1;
                                new GetToken(new CallBackInterface() {
                                    @Override
                                    public void gettoken(String str) {
                                        token=str;
                                        Log.e("TAG","token="+token);
                                        if(!TextUtils.isEmpty(token)){
                                            getIndexDate();
                                        }else {
                                            showerror();
                                        }
                                    }
                                },getContext());
                            }else {
                                id=1;
                                showerror();
                            }
                        } else if (result == 102) {
                            Log.e("TAG", "102-projectCode错误");
//                            mHandler.sendEmptyMessage(1);
                            id=1;
                            ToastUtil.showToast(getContext(),"信息获取失败");
                        }else {
//                            mHandler.sendEmptyMessage(1);
                            id=1;
                            ToastUtil.showToast(getContext(),"信息获取失败");
                        }
                    }

                    @Override
                    public void onError(String data) {
                        id=1;
//                        mHandler.sendEmptyMessage(1);
                        initCropPicture();
                        ToastUtil.showToast(getContext(),"网络连接失败");
                    }
                });
    }

    //加载图片
    private void loadImage() {
//        for (int i=0;i<5;i++){
//            final int finalI = i;
//            Glide.with(this)
//                    .load(images.get(i))
//                    .asBitmap()
//                    .into(new SimpleTarget<Bitmap>() {
//                        @Override
//                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                            if(resource!=null){
//                                bitmap = Bitmap.createBitmap(resource,0,0,resource.getWidth(),resource.getHeight()/6); //调用裁剪图片工具类进行裁剪
//                                if(bitmap!=null) {
//                                    if (finalI == 0) {
//                                        riv_smarthome.setImageBitmap(bitmap); //设置Bitmap到图片上
//                                    } else if (finalI == 1) {
//                                        riv_homemonitoring.setImageBitmap(bitmap);
//                                    } else if (finalI == 2) {
//                                        riv_intelligenceenter.setImageBitmap(bitmap);
//                                    } else if (finalI == 3) {
//                                        riv_safelocation.setImageBitmap(bitmap);
//                                    } else if (finalI == 4) {
//                                        riv_invitevisitor.setImageBitmap(bitmap);
//                                    }
//                                }else {
//
//                                }
//                            }
//                        }
//                    });
//        }


//        for(int i=0;i<5;i++){
//            if(i==0){
//                imageUtils.getBitmap(images.get(i)+"",riv_smarthome);
//            }else if(i==1){
//                imageUtils.getBitmap(images.get(i)+"",riv_homemonitoring);
//            }else if(i==2){
//                imageUtils.getBitmap(images.get(i)+"",riv_intelligenceenter);
//            }else if(i==3){
//                imageUtils.getBitmap(images.get(i)+"",riv_safelocation);
//            }else if(i==4){
//                imageUtils.getBitmap(images.get(i)+"",riv_invitevisitor);
//            }
//        }

    }


    public View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.sliding_menu://菜单键
                    mainActivity.slidingMenu.showMenu();
                    break;

                case R.id.riv_intelligenceenter://智能门禁
                    page_index = 0;
                    select_detail(v, "intelligenceenter");
                    break;
                case R.id.riv_invitevisitor://邀请访客
                    page_index = 1;
                    select_detail(v, "invitevisitor");
                    break;
                case R.id.riv_monitoring://家庭监控
                    page_index = 2;
                    select_detail(v, "homemonitoring");
                    break;
                case R.id.riv_environmentmonitor://环境监测
                    page_index = 3;
                    select_detail(v,"environmentmonitor");
                    break;
                case R.id.iv_opendoor://开门快捷键
                    doorList = SharedPreferencesUtils.getDoorList(getContext(), Sc_myDoor.MyDoorResult.Door.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        // Android M Permission check
                        if (getContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                        }
                    }

                    mAckpassClass = new AckpassClass(getContext());

                    if (!mAckpassClass.Initialize()) {
                        Toast.makeText(getContext(), "Initialize fail", Toast.LENGTH_SHORT).show();
//                        finish();
                    }else {
                        iv_opendoor.setClickable(false);
                        mDialog=DialogThridUtils.showWaitDialog(getContext(),true);
                        mAckpassClass.setOnOpenDeviceListener(mOnOpenDevice);
                        mAckpassClass.StopScan();
                        mAckpassClass.StartScan();

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                /**
                                 *要执行的操作
                                 */
                                mAckpassClass.StopScan();
                                Log.e("TAG","doors22222="+doors);
                                if(doors!=null) {
                                    if (doors.size() == 1 && doors.get(0).getType() == 1) {
                                        mDialog=DialogThridUtils.showWaitDialog(getContext(),true);
                                        doorId=doors.get(0).getId();
                                        mAckpassClass.OpenDevice(doors.get(0).getMac().toUpperCase(),"11111111","0000","0010");
                                        mstatus="";
                                        noGet();
                                    }else if(doors!=null&&doors.size()>0){
                                        View view1 = LayoutInflater.from(getContext()).inflate(R.layout.item_popup_opendoor, null);
                                        popupWindow = new PopupWindow(view1, ViewGroup.LayoutParams.WRAP_CONTENT, 800, true);
                                        //点击其他地方popupwindow消失
                                        popupWindow.setBackgroundDrawable(new BitmapDrawable());
                                        popupWindow.setOutsideTouchable(true);
                                        popupWindow.showAtLocation(view1, Gravity.CENTER, 0, 0);
                                        listView = view1.findViewById(R.id.listview);
                                        list.clear();
                                        adapter = new MyTransitAdapter(getContext(), mAckpassClass);
                                        adapter.clear();
                                        listView.setAdapter(adapter);

                                        adapter.appendData(doors, true);
                                        adapter.upData();
                                    }else {
                                        ToastUtil.showToast(getActivity(),"没有开门权限");
                                    }
                                }else {
                                    ToastUtil.showToast(getActivity(),"没有开门权限");
                                }
                                iv_opendoor.setClickable(true);
                                mHandler.sendEmptyMessage(1);
                            }
                        }, 2000);//10秒后执行Runnable中的run方法

                        mAckpassClass.setOnScanListener(mOnScan);

                    }

                    break;
            }
        }
    };

    private void noGet() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mstatus.equals("")){
                    mHandler.sendEmptyMessage(1);
                    ToastUtil.showToast(getContext(),"连接失败，打开失败");
                }
            }
        },10000);
    }

    private void select_detail(View v, String item) {
        Log.e("TAG", "page_index" + page_index);
        final int[] location = new int[2];
        v.getLocationOnScreen(location);
        switch (item) {
            case "intelligenceenter":
                SmartHomeActivity.Companion.startActivity(v.getContext(), v.getHeight(), location[1], v.getWidth(), page_index);
                break;
            case "invitevisitor":
                InviteVisitorsActivity.startActivity(v.getContext(), v.getHeight(), location[1], v.getWidth(), page_index);
                break;
            case "homemonitoring":
                MonitoringActivity.startActivity(v.getContext(), v.getHeight(), location[1], v.getWidth(), page_index);
                break;
            case "environmentmonitor":
                EnvironmentmonitorActivity.startActivity(v.getContext(), v.getHeight(), location[1], v.getWidth(), page_index);
                break;
        }
    }




    //我的门禁子线程
    private void initGetmyDoor() {
//        mDialog=DialogThridUtils.showWaitDialog(getContext(),true);
        HttpClient.post(CommonUtil.APPURL, "sc_myDoor"
                , new Gson().toJson(new Sc_myDoor.MyDoorParams(token, projectCode, roomNo))
                , new UICallback() {
                    @Override
                    public void process(String data) {
                        Log.e("TAG","data="+data);
//                        Log.e("TAG","token="+token);
//                        Log.e("TAG","projectCode="+projectCode);
                        Sc_myDoor.MyDoorResult myDoorResult = new Gson().fromJson(data, Sc_myDoor.MyDoorResult.class);
                        int result = myDoorResult.getResult();
                        if (result == 1) {
                            Log.e("TAG", "1-json解析错误");
                            mHandler.sendEmptyMessage(1);
                            id=1;
                        } else if (result == 100) {
                            mHandler.sendEmptyMessage(1);
//                            DialogThridUtils.closeDialog(mDialog);
                            Log.e("TAG", "100-成功");
                            id=1;
                            List<Sc_myDoor.MyDoorResult.Door> doorList = new ArrayList<>();
                            doorList=myDoorResult.getDoorList();
                            SharedPreferencesUtils.saveDoorList(getContext(), doorList);
//                            //我的账号：下载个人信息
//                            initMyAccount();
                        } else if (result == 101) {
                            Log.e("TAG", "101-token错误");
                            mHandler.sendEmptyMessage(1);
                            if(id==1){
                                id=id+1;
                                new GetToken(new CallBackInterface() {
                                    @Override
                                    public void gettoken(String str) {
                                        token=str;
                                        Log.e("TAG","token="+token);
                                        if(!TextUtils.isEmpty(token)){
                                            initGetmyDoor();
                                        }else {
                                            showerror();
                                        }
                                    }
                                },getContext());
                            }else {
                                id=1;
                                showerror();
                            }
                        } else if (result == 102) {
                            Log.e("TAG", "102-projectCode错误");
                            mHandler.sendEmptyMessage(1);
                            id=1;
                            ToastUtil.showToast(getContext(),"门禁列表获取失败");
                        }else {
                            id=1;
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(getContext(),"门禁列表获取失败");
                        }
                    }

                    @Override
                    public void onError(String data) {
                        ToastUtil.showToast(getContext(), "网络连接异常");
                        mHandler.sendEmptyMessage(1);
                        id=1;
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
            }
        });
        builder.create().show();
    }


    private OnScanListener mOnScan = new OnScanListener() {
        @Override
        public void OnScan(String deviceMac, String deviceName, String deviceType) {
            for (int i = 0; i < doorList.size(); i++) {
                if (deviceMac.toUpperCase().equals(doorList.get(i).getMac().toUpperCase()) == true) {
                    list.add(doorList.get(i));
                }
            }
//            Log.e("TAG","list="+list);
            Set set = new HashSet();
            doors = new ArrayList<>();
            set.addAll(list);
            doors.addAll(set);
            Log.e("TAG","doors="+doors);

        }
    };

    private OnOpenDeviceListener mOnOpenDevice = new OnOpenDeviceListener(){
        @Override
        public void OnOpenDevice(int status) {

            Log.e("TAG","status="+status);
            mstatus=status+"";
            if (status==0) {
                d=new Date(System.currentTimeMillis());
                openTime=sf.format(d);
                openResult="开门成功";
                insertDoor();

            } else {
                mHandler.sendEmptyMessage(1);
                Toast.makeText(getContext(), "开门失败"+ String.valueOf(status), Toast.LENGTH_SHORT).show();
            }
        }
    };

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
                            Toast.makeText(getContext(), openResult, Toast.LENGTH_SHORT).show();
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
                                },getContext());
                            }else {
                                id=1;
                                showerror();
                            }
                        }else if(result==102){
                            Log.e("TAG", "102-projectCode错误");
                            id=1;
                            mHandler.sendEmptyMessage(1);
                            Toast.makeText(getContext(), "开门失败", Toast.LENGTH_SHORT).show();
                        }else{
                            id=1;
                            Toast.makeText(getContext(), "开门失败", Toast.LENGTH_SHORT).show();
                            mHandler.sendEmptyMessage(1);
                        }
                    }

                    @Override
                    public void onError(String data) {
                        id=1;
                        mHandler.sendEmptyMessage(1);
                        ToastUtil.showToast(getContext(),"网络操作失败");
                    }
                });
    }

}
