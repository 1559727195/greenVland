package com.massky.greenlandvland.ui;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.ContactsContract;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.massky.greenlandvland.R;
import com.massky.greenlandvland.View.DateChangeWheelView.TimePickerDialog;
import com.massky.greenlandvland.View.DateChangeWheelView.data.Type;
import com.massky.greenlandvland.View.DateChangeWheelView.listener.OnDateSetListener;
import com.massky.greenlandvland.common.CallBackInterface;
import com.massky.greenlandvland.common.CommonUtil;
import com.massky.greenlandvland.common.DialogThridUtils;
import com.massky.greenlandvland.common.GetToken;
import com.massky.greenlandvland.common.LocalBroadcastManager;
import com.massky.greenlandvland.common.SharedPreferencesUtils;
import com.massky.greenlandvland.common.ToastUtil;
import com.massky.greenlandvland.model.entity.Message;
import com.massky.greenlandvland.model.entity.Sc_inviteVisitor;
import com.massky.greenlandvland.model.entity.Sc_isLoginNew;
import com.massky.greenlandvland.model.entity.Sc_myDoor;
import com.massky.greenlandvland.model.httpclient.HttpUrl.HttpClient;
import com.massky.greenlandvland.model.httpclient.UICallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.system.text.ShortMessage;
import cn.sharesdk.wechat.friends.Wechat;

public class InvitevisitorActivity extends AppCompatActivity implements OnDateSetListener {
    private ImageView iv_back;
    private RelativeLayout rl_allocationkey;
    private ImageView iv_minus, iv_add;
    private TextView tv_number;
    private TextView tv_usernumber;
    private Button btn_createinvitation;
    private ImageView iv_inviterecord;
    private ImageView contacts;
    private EditText et_visitorname, et_visitorphone;
    private RelativeLayout rl_starttime, rl_endtime;
    private TextView tv_start_time, tv_end_time;
    private TextView allocationkey;
    private String username, usernumber;
    private String token;
    private String projectCode;
    private String roomNo;
    private int id=1;
    private List<Sc_inviteVisitor.InviteVisitorParams.Quanxian> quanxian = new ArrayList<>();

    List<Sc_myDoor.MyDoorResult.Door> doorList = new ArrayList<>();
//    final List<Sc_myDoor.MyDoorResult.Door> doors=new ArrayList<>();

    TimePickerDialog mDialogAll1, mDialogAll2;
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    long tenYears = 10L * 365 * 1000 * 60 * 60 * 24L;

    private String uri="";
    String beginTime;
    String endTime ;
    String useTime;
    String name;
    List<Sc_inviteVisitor.InviteVisitorParams.VisitorName> visitorName = new ArrayList<>();
    List<Sc_inviteVisitor.InviteVisitorParams.VisitorPhone> visitorPhone = new ArrayList<>();
    String qx="";
    Date d;
    boolean selected[];
    List<String> names=new ArrayList<>();;
    String items[];
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


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    public static InvitevisitorActivity instance=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitevisitor);
        instance=this;
        doorList= SharedPreferencesUtils.getDoorList(InvitevisitorActivity.this, Sc_myDoor.MyDoorResult.Door.class);
        Log.e("TAG","doorost="+doorList);
        selected=new boolean[doorList.size()];
        for(int i=0;i<doorList.size();i++){
            selected[i]=false;
            Log.e("TAG","selected="+selected[i]);
        }
        items = new String[doorList.size()];
        for (int i=0;i<doorList.size();i++){
            items[i]=doorList.get(i).getDoorName();
        }

        token= SharedPreferencesUtils.getToken(InvitevisitorActivity.this);
        projectCode=SharedPreferencesUtils.getProjectCode(this);
        roomNo=SharedPreferencesUtils.getRoomNo(this);
        name=SharedPreferencesUtils.getUserName(this);

        //获取控件
        iv_back = (ImageView) findViewById(R.id.iv_back);
        rl_allocationkey = (RelativeLayout) findViewById(R.id.rl_allocationkey);
        tv_number = (TextView) findViewById(R.id.tv_number);
        iv_minus = (ImageView) findViewById(R.id.iv_minus);
        iv_add = (ImageView) findViewById(R.id.iv_add);
        tv_usernumber = (TextView) findViewById(R.id.tv_usernumber);
        btn_createinvitation = (Button) findViewById(R.id.btn_createinvitation);
        iv_inviterecord = (ImageView) findViewById(R.id.iv_inviterecord);
        contacts = (ImageView) findViewById(R.id.contacts);
        et_visitorname = (EditText) findViewById(R.id.et_visitorname);
        et_visitorphone = (EditText) findViewById(R.id.et_visitorphone);
        rl_starttime = (RelativeLayout) findViewById(R.id.rl_starttime);
        rl_endtime = (RelativeLayout) findViewById(R.id.rl_endtime);
        tv_start_time = (TextView) findViewById(R.id.tv_start_time);
        tv_end_time = (TextView) findViewById(R.id.tv_end_time);
        allocationkey= (TextView) findViewById(R.id.allocationkey);
        //添加监听
        iv_back.setOnClickListener(clickListener);
        rl_allocationkey.setOnClickListener(clickListener);
        iv_add.setOnClickListener(clickListener);
        iv_minus.setOnClickListener(clickListener);
        btn_createinvitation.setOnClickListener(clickListener);
        iv_inviterecord.setOnClickListener(clickListener);
        rl_starttime.setOnClickListener(clickListener);
        rl_endtime.setOnClickListener(clickListener);
        contacts.setOnClickListener(clickListener);
        et_visitorphone.addTextChangedListener(mTextWatcher);

        et_visitorphone.setKeyListener(DigitsKeyListener.getInstance("1234567890"));


        mDialogAll1 = new TimePickerDialog.Builder()
                .setCallBack((OnDateSetListener) this)
                .setCancelStringId("取消")
                .setSureStringId("确定")
                .setTitleStringId("起始时间")
                .setYearText("年")
                .setMonthText("月")
                .setDayText("日")
                .setHourText("时")
                .setMinuteText("分")
                .setCyclic(false)
                .setMinMillseconds(System.currentTimeMillis())
                .setMaxMillseconds(System.currentTimeMillis() + tenYears)
                .setCurrentMillseconds(System.currentTimeMillis())
                .setThemeColor(getResources().getColor(R.color.timepicker_dialog_bg))
                .setType(Type.ALL)
                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.timepicker_toolbar_bg))
                .setWheelItemTextSize(14)
                .build();

        mDialogAll2 = new TimePickerDialog.Builder()
                .setCallBack((OnDateSetListener) this)
                .setCancelStringId("取消")
                .setSureStringId("确定")
                .setTitleStringId("终止时间")
                .setYearText("年")
                .setMonthText("月")
                .setDayText("日")
                .setHourText("时")
                .setMinuteText("分")
                .setCyclic(false)
                .setMinMillseconds(System.currentTimeMillis())
                .setMaxMillseconds(System.currentTimeMillis() + tenYears)
                .setCurrentMillseconds(System.currentTimeMillis())
                .setThemeColor(getResources().getColor(R.color.timepicker_dialog_bg))
                .setType(Type.ALL)
                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.timepicker_toolbar_bg))
                .setWheelItemTextSize(14)
                .build();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        registerMessageReceiver();
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_back://回退键
                    onBackPressed();
                    break;
                case R.id.rl_allocationkey://钥匙分配

//                    final String items[] = {"JAVA", "C++", "JavaScript", "MySQL"};


                    AlertDialog.Builder builder = new AlertDialog.Builder(InvitevisitorActivity.this,3);
                    builder.setTitle("多选");
                    builder.setIcon(R.mipmap.ic_launcher);
                    builder.setMultiChoiceItems(items, selected,
                            new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                    if(isChecked){
//                                        doors.add(doorList.get(which));		//选择的时候要保存起来
                                        names.add(items[which]);
//                                        ToastUtil.showToast(InvitevisitorActivity.this,"which="+which);
//                                        Toast.makeText(InvitevisitorActivity.this,
//                                                items[which] + isChecked, Toast.LENGTH_SHORT)
//                                                .show();
                                        quanxian.add(new Sc_inviteVisitor.InviteVisitorParams.Quanxian(doorList.get(which).getId()));
                                        Log.e("TAG","which="+which);
                                    }else {
//                                        doors.remove(doorList.get(which));
                                        names.remove(items[which]);
//                                        Toast.makeText(InvitevisitorActivity.this,
//                                                items[which] + isChecked, Toast.LENGTH_SHORT)
//                                                .show();
//                                        quanxian.remove(doorList.get(which).getId());
                                        Log.e("TAG","doorList.get(which).getId()="+doorList.get(which).getId());
                                        if(quanxian!=null&&quanxian.size()>0){
                                            for (int i=0;i<quanxian.size();i++){
                                                Log.e("TAG","quanxian.get(i).getDoorId()="+quanxian.get(i).getDoorId());
                                                if(quanxian.get(i).getDoorId()==doorList.get(which).getId()){
                                                    quanxian.remove(i);
                                                }
                                            }
                                        }

//                                        quanxian.remove(which);
                                    }
                                    Log.e("TAG","quanxian="+quanxian);
//                                    Log.e("TAG","selected="+selected[which]);
                                    Log.e("TAG","which="+which);
                                }
                            });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.e("TAG","quanxian1111="+quanxian);
                            Log.e("TAG","names="+names);
                            String name=names.toString().replace("[","").replace("]","");
                            Log.e("TAG","name="+name);
                            if(names!=null&&names.size()>0){
                                allocationkey.setText(name);
                            }else {
                                allocationkey.setText("未设置");
                            }

                            if(quanxian!=null&&quanxian.size()>0){
                                for (int i=0;i<quanxian.size();i++){
                                    if(quanxian.get(i).getDoorId()==doorList.get(i).getId()){
                                        selected[i]=true;
                                    }
                                }
                            }


                            dialog.dismiss();
//                            Toast.makeText(InvitevisitorActivity.this, "确定", Toast.LENGTH_SHORT)
//                                    .show();
                            // android会自动根据你选择的改变selected数组的值。
//                for (int i = 0; i < selected.length; i++) {
//                    Log.e("hongliang", "" + selected[i]);
//                }
                        }
                    });
                    builder.create().show();



//                    AlertDialog.Builder builder = new AlertDialog.Builder(InvitevisitorActivity.this);
////                    builder.setIcon(R.drawable.ic_launcher);
//                    builder.setTitle("爱好");
//                    final String[] hobbies = {"篮球", "足球", "网球", "斯诺克"};
//                    //    设置一个单项选择下拉框
//                    /**
//                     * 第一个参数指定我们要显示的一组下拉多选框的数据集合
//                     * 第二个参数代表哪几个选项被选择，如果是null，则表示一个都不选择，如果希望指定哪一个多选选项框被选择，
//                     * 需要传递一个boolean[]数组进去，其长度要和第一个参数的长度相同，例如 {true, false, false, true};
//                     * 第三个参数给每一个多选项绑定一个监听器
//                     */
//                    builder.setMultiChoiceItems(hobbies, null, new DialogInterface.OnMultiChoiceClickListener()
//                    {
//                        StringBuffer sb = new StringBuffer(100);
//                        @Override
//                        public void onClick(DialogInterface dialog, int which, boolean isChecked)
//                        {
//                            if(isChecked)
//                            {
//                                sb.append(hobbies[which] + ", ");
//                            }
//                            Toast.makeText(InvitevisitorActivity.this, "爱好为：" + sb.toString(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
//                    {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which)
//                        {
//
//                        }
//                    });
//                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
//                    {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which)
//                        {
//
//                        }
//                    });
//                    builder.show();



//                    View view= LayoutInflater.from(InvitevisitorActivity.this).inflate(R.layout.item_popup_allocationkey,null);
//                    popupWindow=new PopupWindow(view,ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
//                    popupWindow.setBackgroundDrawable(new BitmapDrawable());
//                    popupWindow.setOutsideTouchable(true);
//                    popupWindow.showAtLocation(view, Gravity.CENTER,0,0);
//                    PopAdapter popAdapter=new PopAdapter(InvitevisitorActivity.this,doorList);
//                    ListView lv;
//                    lv= (ListView) view.findViewById(R.id.lv);
//                    lv.setAdapter(popAdapter);
//                    lv.setItemsCanFocus(false);
//                    lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

//                    new AlertDialog.Builder(InvitevisitorActivity.this).setSingleChoiceItems(
//                            new String[]{"masskey gate", "1单元门"}, 0,
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                }
//                            }).setNegativeButton("确定", null).setPositiveButton("取消", null).show();
                    break;
                case R.id.iv_add://加号键
                    int numberadd = Integer.parseInt(tv_number.getText().toString());
                    tv_number.setText(numberadd + 1 + "");
                    tv_usernumber.setText("使用次数(" + tv_number.getText().toString() + ")");
                    break;
                case R.id.iv_minus://减号键
                    int numberminus = Integer.parseInt(tv_number.getText().toString());
                    if(numberminus==0){
                        tv_number.setText("0");
                        tv_usernumber.setText("使用次数(0)");
                    }else {
                        tv_number.setText(numberminus - 1 + "");
                        tv_usernumber.setText("使用次数(" + tv_number.getText().toString() + ")");
                    }
                    break;
                case R.id.btn_createinvitation:
                    if(!TextUtils.isEmpty(et_visitorname.getText())){
                        if(!TextUtils.isEmpty(et_visitorphone.getText()) && et_visitorphone.getText().toString().length()==13){
                            if(!TextUtils.isEmpty(tv_start_time.getText())){
                                if(!TextUtils.isEmpty(tv_end_time.getText())){
                                    if(!TextUtils.isEmpty(allocationkey.getText())&&!allocationkey.getText().equals("未设置")){
                                        mDialog=DialogThridUtils.showWaitDialog(InvitevisitorActivity.this,true);
                                        invite();

                                    }else {
                                        ToastUtil.showToast(InvitevisitorActivity.this,"请分配权限");
                                    }
                                }else {
                                    ToastUtil.showToast(InvitevisitorActivity.this,"终止时间不能为空");
                                }
                            }else{
                                ToastUtil.showToast(InvitevisitorActivity.this,"起始时间不能为空");
                            }
                        }else {
                            ToastUtil.showToast(InvitevisitorActivity.this,"请输入正确的访客手机号");
                        }
                    }else {
                        ToastUtil.showToast(InvitevisitorActivity.this,"请输入访客姓名");
                    }


                    break;
                case R.id.iv_inviterecord:
                    startActivity(new Intent(InvitevisitorActivity.this, MyVisiterActivity.class));
                    break;
                case R.id.rl_starttime:
                    mDialogAll1.show(getSupportFragmentManager(), "all");
                    break;
                case R.id.rl_endtime:
                    mDialogAll2.show(getSupportFragmentManager(), "all");
                    break;
                case R.id.contacts:
                    if (ActivityCompat.checkSelfPermission(InvitevisitorActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED ){
                        ActivityCompat.requestPermissions(InvitevisitorActivity.this,
                                new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CONTACTS);
                    } else {
                        startActivityForResult(new Intent(Intent.ACTION_PICK,
                                ContactsContract.Contacts.CONTENT_URI), 0);
                    }
                    break;
            }
        }
    };
    private final int REQUEST_CONTACTS = 1;

    private void invite() {
        String name = et_visitorname.getText().toString();
        String phone = et_visitorphone.getText().toString();

        visitorName.clear();
        visitorName.add(new Sc_inviteVisitor.InviteVisitorParams.VisitorName(name));

        visitorPhone.clear();
        visitorPhone.add(new Sc_inviteVisitor.InviteVisitorParams.VisitorPhone(phone));
        useTime = tv_number.getText().toString();
        beginTime = tv_start_time.getText().toString();
        endTime = tv_end_time.getText().toString();

//                                        List<String> doorId = null;
//                                        for (int i=0;i<doors.size();i++){
//                                            doorId.add(doors.get(i).getDoorName());
//                                        }



        HttpClient.post(CommonUtil.APPURL, "sc_inviteVisitor"
                , new Gson().toJson(new Sc_inviteVisitor.InviteVisitorParams(token
                        , visitorName
                        , visitorPhone
                        , useTime
                        , beginTime
                        , endTime
                        , quanxian
                        , projectCode
                        ,roomNo))
                , new UICallback() {
                    @Override
                    public void process(String data) {
                        Log.e("TAG", "data=" + data);
                        Log.e("TAG", "token=" + token);
                        Log.e("TAG", "visitorName=" + visitorName);
                        Log.e("TAG", "visitorPhone=" + visitorPhone);
                        Log.e("TAG", "useTime=" + useTime);
                        Log.e("TAG", "beginTime=" + beginTime);
                        Log.e("TAG", "dendTimeata=" + endTime);
                        Log.e("TAG", "quanxian=" + quanxian);
                        Log.e("TAG", "projectCode=" + projectCode);
                        Sc_inviteVisitor.InviteVisitorResoult inviteVisitorResoult = new Gson().fromJson(data, Sc_inviteVisitor.InviteVisitorResoult.class);
                        int result = inviteVisitorResoult.getResult();
                        if (result == 1) {
                            Log.e("TAG", "1-json解析错误");
                            id=1;
                        } else if (result == 100) {
                            Log.e("TAG", "1-100成功");
                            id=1;
                            mHandler.sendEmptyMessage(1);

                            for (int i=0;i<quanxian.size();i++){
                                qx += quanxian.get(i).getDoorId()+",";
                            }
                            Log.e("TAG","qx="+qx);

//                            quanxian.clear();

                            AlertDialog.Builder builder=new AlertDialog.Builder(InvitevisitorActivity.this,1);
                            builder.setTitle("提示");
                            builder.setMessage("邀请成功，是否分享");
                            builder.setCancelable(false);
                            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    showShare();
                                }
                            });
                            builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.create().show();

                        } else if (result == 101) {
                            Log.e("TAG", "101-token错误");
                            if(id==1){
                                id=id+1;
                                new GetToken(new CallBackInterface() {
                                    @Override
                                    public void gettoken(String str) {
                                        token=str;
                                        Log.e("TAG","token="+token);
                                        if(!TextUtils.isEmpty(token)){
                                            invite();
                                        }else {
                                            showerror();
                                        }
                                    }
                                },InvitevisitorActivity.this);
                            }else {
                                id=1;
                                showerror();
                            }
                        } else if (result == 102) {
                            Log.e("TAG", "102-projectCode错误");
                            id=1;
                            ToastUtil.showToast(InvitevisitorActivity.this,"操作失败");
                        }else {
                            id=1;
                            ToastUtil.showToast(InvitevisitorActivity.this,"操作失败");
                        }
                    }

                    @Override
                    public void onError(String data) {
                        id=1;
                        ToastUtil.showToast(InvitevisitorActivity.this,"网络连接失败");
                    }
                });
    }

    private void showerror() {
        AlertDialog.Builder builder = new AlertDialog.Builder(InvitevisitorActivity.this, 1);
        builder.setTitle("提示");
        builder.setMessage("服务器错误");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.create().show();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        if (timePickerView == mDialogAll1) {
            String text = getDateToString(millseconds);
            tv_start_time.setText(text);
        } else if (timePickerView == mDialogAll2) {
            String text = getDateToString(millseconds);
            endTime=text;
            tv_end_time.setText(text);
        }

    }

    public String getDateToString(long time) {
        d = new Date(time);
        return sf.format(d);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            // ContentProvider展示数据类似一个单个数据库表
            // ContentResolver实例带的方法可实现找到指定的ContentProvider并获取到ContentProvider的数据
            ContentResolver reContentResolverol = getContentResolver();
            // URI,每个ContentProvider定义一个唯一的公开的URI,用于指定到它的数据集
            Uri contactData = data.getData();
            // 查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
            Cursor cursor = managedQuery(contactData, null, null, null, null);
            cursor.moveToFirst();
            // 获得DATA表中的名字
            username = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            // 条件为联系人ID
            String contactId = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts._ID));
            // 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
            Cursor phone = reContentResolverol.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
                            + contactId, null, null);
            while (phone.moveToNext()) {
                usernumber = phone
                        .getString(phone
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                et_visitorname.setText(username);
                if(usernumber.length()==11){
                    StringBuilder sb=new StringBuilder(usernumber);
                    sb.insert(3," ");
                    sb.insert(8," ");
                    usernumber=sb.toString();
                }else {

                }
                et_visitorphone.setText(usernumber);
            }
        }
    }


    private void showShare() {
        uri="https://smartapi.massky.com/SmartCommunityMIS/doorVisitor?doorAuth="+qx
                +"&useTime="+useTime+"&visitorName="+visitorName.get(0).getName()+"&userName="+name+"&projectCode="+projectCode+"&phoneNo="+visitorPhone.get(0).getPhone()
                +",&endTime="+endTime;
        Log.e("TAG","uri="+uri);
//        uri="http://www.baidu.com";

        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.app_name));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(uri);
        // text是分享文本，所有平台都需要这个字段
        oks.setText("绿地朝阳中心访客分享");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(uri);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(uri);

        /**
         * 真正分享出去的内容实际上是由下面的这些参数决定的，根据平台不同分别配置
         */
        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, cn.sharesdk.framework.Platform.ShareParams paramsToShare) {
                if (Wechat.NAME.equals(platform.getName())) {
                    paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
                    paramsToShare.setUrl(uri);
                    paramsToShare.setText("绿地朝阳中心访客邀请");
//                    paramsToShare.setImageUrl(Urls.BASE_URL + entity.pic);
                    paramsToShare.setTitle(getString(R.string.app_name));
                }
//                if (SinaWeibo.NAME.equals(platform.getName())) {
//                    paramsToShare.setText(entity.intro);
//                    paramsToShare.setUrl(entity.shoreUrl);
//                    paramsToShare.setImageUrl(Urls.BASE_URL + entity.pic);
//                }
//                if (QQ.NAME.equals(platform.getName())) {
//                    paramsToShare.setTitle("智慧社区");
////                    paramsToShare.setTitleUrl(entity.shoreUrl);
//                    paramsToShare.setText("智慧社区访客邀请");
//                    paramsToShare.setUrl("https://www.baidu.com");
////                    paramsToShare.setImageUrl(Urls.BASE_URL + entity.pic);
//                }
                if (ShortMessage.NAME.equals(platform.getName())){
                    paramsToShare.setText(uri);
//                    paramsToShare.setTitle(getString(R.string.app_name));
                }
            }
        });


        // 启动分享GUI
        oks.show(this);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Invitevisitor Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    @Override
    protected void onResume() {
        isForeground = true;
        isLogin();
        super.onResume();
        d=new Date(System.currentTimeMillis());
        tv_start_time.setText(sf.format(d));
        d=new Date(System.currentTimeMillis()+(long)(86400*1000));
        tv_end_time.setText(sf.format(d));
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
        AlertDialog.Builder builder=new AlertDialog.Builder(InvitevisitorActivity.this,1);
        builder.setTitle("提示");
        builder.setMessage("该账号已被其他设备登录");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startActivity(new Intent(InvitevisitorActivity.this, LoginActivity.class));
                SharedPreferencesUtils.cleanPassword(InvitevisitorActivity.this);
                InviteVisitorsActivity.instance.finish();
                if (isExsitMianActivity(MyVisiterActivity.class)){
                    MyVisiterActivity.instance.finish();
                }
                MainActivity.instance.finish();
                finish();
            }
        });
        builder.create().show();
    }

    /**
     * 判断某一个类是否存在任务栈里面
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
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

    TextWatcher mTextWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int length = s.toString().length();
            //删除数字
            if (count == 0) {
                if (length == 4) {
                    et_visitorphone.setText(s.subSequence(0, 3));
                }
                if (length == 9) {
                    et_visitorphone.setText(s.subSequence(0, 8));
                }
            }
            //添加数字
            if (count == 1) {
                if (length == 4) {
                    String part1 = s.subSequence(0, 3).toString();
                    String part2 = s.subSequence(3, length).toString();
                    et_visitorphone.setText(part1 + " " + part2);
                }
                if (length==9){
                    String part1 = s.subSequence(0, 8).toString();
                    String part2 = s.subSequence(8, length).toString();
                    et_visitorphone.setText(part1 + " " + part2);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            //将光标移动到末尾
            et_visitorphone.setSelection(et_visitorphone.getText().toString().length());
            //处理s
        }
    };
}
