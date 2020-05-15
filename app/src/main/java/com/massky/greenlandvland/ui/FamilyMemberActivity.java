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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.massky.greenlandvland.R;
import com.massky.greenlandvland.View.swipelayout.SwipeMenuLayout;
import com.massky.greenlandvland.View.swipelayout.ViewHolder;
import com.massky.greenlandvland.common.CallBackInterface;
import com.massky.greenlandvland.common.CommonUtil;
import com.massky.greenlandvland.common.DialogThridUtils;
import com.massky.greenlandvland.common.GetToken;
import com.massky.greenlandvland.common.LocalBroadcastManager;
import com.massky.greenlandvland.common.SharedPreferencesUtils;
import com.massky.greenlandvland.common.ToastUtil;
import com.massky.greenlandvland.model.entity.Sc_deleteFamily;
import com.massky.greenlandvland.model.entity.Sc_getFamily;
import com.massky.greenlandvland.model.entity.Sc_isLoginNew;
import com.massky.greenlandvland.model.httpclient.HttpUrl.HttpClient;
import com.massky.greenlandvland.model.httpclient.UICallback;
import com.massky.greenlandvland.ui.adapter.FamilyMemberSwipeAdapter;

import java.util.ArrayList;
import java.util.List;

public class FamilyMemberActivity extends AppCompatActivity {
    private ImageView back;//回退键
    private ListView swipelistview;
    private ImageView iv_addmember;
    private List<Sc_getFamily.GetFamilyResult.Family> familyLists = new ArrayList<>();
//    private List<Sc_getFamily.GetFamilyResult.Family> familyList = new ArrayList<>();
    private String token;
    private String projectCode;
    private String roomNo;
    private String mobilePhone;
    private int id=1;
    private TextView name,number;
    private View line;

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
    public static FamilyMemberActivity instance=null;
    private int familynumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_member);
        instance=this;
        token= SharedPreferencesUtils.getToken(this);
        projectCode=SharedPreferencesUtils.getProjectCode(this);
        roomNo=SharedPreferencesUtils.getRoomNo(this);
        registerMessageReceiver();

        //获取控件
        back= (ImageView) findViewById(R.id.back);
        iv_addmember= (ImageView) findViewById(R.id.iv_addmember);
        swipelistview= (ListView) findViewById(R.id.swipelistview);
        name=findViewById(R.id.name);
        number=findViewById(R.id.number);
        line=findViewById(R.id.line);

        //添加监听
        back.setOnClickListener(clickListener);
        iv_addmember.setOnClickListener(clickListener);

    }

    View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.back://回退键
                    onBackPressed();
                    break;
                case R.id.iv_addmember://添加按钮
                    startActivity(new Intent(FamilyMemberActivity.this,AddMemberActivity.class));
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        finish();
    }

//    private void getFamily() {
//        mDialog=DialogThridUtils.showWaitDialog(FamilyMemberActivity.this,true);
//        HttpClient.post(CommonUtil.APPURL, "sc_getFamily"
//                , new Gson().toJson(new Sc_getFamily.GetFamilyParams(token, projectCode, roomNo))
//                , new UICallback() {
//                    @Override
//                    public void process(String data) {
//                        Log.e("TAG","data="+data);
//                        Sc_getFamily.GetFamilyResult getFamilyResult=new Gson().fromJson(data, Sc_getFamily.GetFamilyResult.class);
//                        int result=getFamilyResult.getResult();
//                        if(result==1){
//                            Log.e("TAG","1-json解析错误");
//                            id=1;
//                            mHandler.sendEmptyMessage(1);
//                        }else if(result==100) {
//                            Log.e("TAG", "100-成功");
//                            id = 1;
//                            mHandler.sendEmptyMessage(1);
//                            familyLists = getFamilyResult.getFamilyList();
//                            SharedPreferencesUtils.saveFamilyList(FamilyMemberActivity.this,familyLists);
//                            SharedPreferencesUtils.saveFamily(FamilyMemberActivity.this,familyLists.size());
//                        }else if(result==101){
//                            Log.e("TAG","101-token错误");
//                            mHandler.sendEmptyMessage(1);
//                            if(id==1){
//                                id=id+1;
//                                new GetToken(new CallBackInterface() {
//                                    @Override
//                                    public void gettoken(String str) {
//                                        token=str;
//                                        SharedPreferencesUtils.saveToken(FamilyMemberActivity.this,token);
//                                        Log.e("TAG","token="+token);
//                                        if(!TextUtils.isEmpty(token)){
//                                            getFamily();
//                                        }else {
//                                            showerror();
//                                        }
//                                    }
//                                },FamilyMemberActivity.this);
//                            }else {
//                                id=1;
//                                showerror();
//                            }
//                        }else if(result==102){
//                            Log.e("TAG","102-projectCode错误");
//                            mHandler.sendEmptyMessage(1);
//                            id=1;
//                            ToastUtil.showToast(FamilyMemberActivity.this,"操作失败");
//                        }else {
//                            mHandler.sendEmptyMessage(1);
//                            id=1;
//                            ToastUtil.showToast(FamilyMemberActivity.this,"操作失败");
//                        }
//                    }
//
//                    @Override
//                    public void onError(String data) {
//                        mHandler.sendEmptyMessage(1);
//                        id=1;
//                        Log.e("TAG","getfamily");
//                        ToastUtil.showToast(FamilyMemberActivity.this,"网络连接失败");
//                    }
//                });
//    }

//    //设置数据
//    public void setFamily(){
//        if(familyLists!=null&&familyLists.size()>0){
//            if(familyLists!=null&&familyLists.size()>1){
//                line.setVisibility(View.VISIBLE);
//            }else {
//                line.setVisibility(View.GONE);
//            }
//
//            for(int i=0;i<familyLists.size();i++){
//
//                if (familyLists.get(i).getType()==2){
//                    if (!TextUtils.isEmpty(familyLists.get(i).getName())){
//                        name.setText(familyLists.get(i).getName()+"(业主)");
//                    }else {
//                        name.setText("(业主)");
//                    }
//                    number.setText(familyLists.get(i).getMobilePhone());
//                    familyLists.remove(i);
//                }
//            }
//
//        }else {
//            name.setText("");
//            number.setText("");
//        }
//
//        FamilyMemberSwipeAdapter swipeAdapter = new FamilyMemberSwipeAdapter(FamilyMemberActivity.this,familyLists,R.layout.item_swipelist_familymenmber) {
//            @Override
//            public void convert(final ViewHolder holder, Object object, final int position, View convertView) {
//                holder.setText(R.id.tv_name,familyLists.get(position).getName());
//                holder.setText(R.id.tv_number,familyLists.get(position).getMobilePhone());
//
//                holder.setOnClickListener(R.id.btnDelete, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mDialog=DialogThridUtils.showWaitDialog(FamilyMemberActivity.this,true);
//                        mobilePhone=familyLists.get(position).getMobilePhone();
//                        HttpClient.post(CommonUtil.APPURL, "sc_deleteFamily"
//                                , new Gson().toJson(new Sc_deleteFamily.DeleteFamilyParams(token, projectCode, mobilePhone))
//                                , new UICallback() {
//                                    @Override
//                                    public void process(String data) {
//                                        Log.e("TAG","data="+data);
////                                                            Log.e("TAG","token="+token);
////                                                            Log.e("TAG","projectCode="+projectCode);
//                                        Log.e("TAG","mobilePhone="+mobilePhone);
//                                        Sc_deleteFamily.DeleteFamilyResult deleteFamilyResult=new Gson().fromJson(data, Sc_deleteFamily.DeleteFamilyResult.class);
//                                        int result=deleteFamilyResult.getResult();
//                                        if(result==1){
//                                            id=1;
//                                            Log.e("TAG","1-json解析失败");
//                                            mHandler.sendEmptyMessage(1);
//                                        }else if(result==100){
//                                            id=1;
////                                                                mHandler.sendEmptyMessage(1);
//                                            Log.e("TAG","100-成功");
////                                                                getFamily();
//                                            ((SwipeMenuLayout) holder.getConvertView()).quickClose();
//                                            familyLists.remove(position);
//                                            notifyDataSetChanged();
////                                            SharedPreferencesUtils.saveFamily(FamilyMemberActivity.this,familyLists);
//                                            SharedPreferencesUtils.saveFamily(FamilyMemberActivity.this,familyLists.size()-1);
//                                            mHandler.sendEmptyMessage(1);
//                                        }else if(result==101){
//                                            Log.e("TAG","101-token错误");
//                                            if(id==1){
//                                                id=id+1;
//                                                new GetToken(new CallBackInterface() {
//                                                    @Override
//                                                    public void gettoken(String str) {
//                                                        token=str;
//                                                        Log.e("TAG","token="+token);
//                                                        if(!TextUtils.isEmpty(token)){
//                                                            HttpClient.post(CommonUtil.APPURL, "sc_deleteFamily"
//                                                                    , new Gson().toJson(new Sc_deleteFamily.DeleteFamilyParams(token, projectCode, mobilePhone))
//                                                                    , new UICallback() {
//                                                                        @Override
//                                                                        public void process(String data) {
////                                                            Log.e("TAG","data="+data);
////                                                            Log.e("TAG","token="+token);
////                                                            Log.e("TAG","projectCode="+projectCode);
////                                                            Log.e("TAG","mobilePhone="+mobilePhone);
//                                                                            Sc_deleteFamily.DeleteFamilyResult deleteFamilyResult=new Gson().fromJson(data, Sc_deleteFamily.DeleteFamilyResult.class);
//                                                                            int result=deleteFamilyResult.getResult();
//                                                                            if(result==1){
//                                                                                id=1;
//                                                                                mHandler.sendEmptyMessage(1);
//                                                                                Log.e("TAG","1-json解析失败");
//                                                                            }else if(result==100){
//                                                                                Log.e("TAG","100-成功");
//                                                                                ((SwipeMenuLayout) holder.getConvertView()).quickClose();
//                                                                                familyLists.remove(position);
//                                                                                notifyDataSetChanged();
//                                                                                mHandler.sendEmptyMessage(1);
////                                                                                SharedPreferencesUtils.saveFamilyList(FamilyMemberActivity.this,familyLists);
//                                                                                SharedPreferencesUtils.saveFamily(FamilyMemberActivity.this,familyLists.size()-1);
//                                                                            }else if(result==101){
//                                                                                Log.e("TAG","101-token错误");
//                                                                                id=1;
//                                                                                mHandler.sendEmptyMessage(1);
//                                                                                showerror();
//                                                                            }else if(result==102){
//                                                                                id=1;
//                                                                                mHandler.sendEmptyMessage(1);
//                                                                                Log.e("TAG","102-projectCode错误");
//                                                                                ToastUtil.showToast(FamilyMemberActivity.this,"操作失败");
//                                                                            }else {
//                                                                                id=1;
//                                                                                mHandler.sendEmptyMessage(1);
//                                                                                ToastUtil.showToast(FamilyMemberActivity.this,"操作失败");
//                                                                            }
//                                                                        }
//
//                                                                        @Override
//                                                                        public void onError(String data) {
//                                                                            id=1;
//                                                                            mHandler.sendEmptyMessage(1);
//                                                                            ToastUtil.showToast(FamilyMemberActivity.this,"网络连接失败");
//                                                                        }
//                                                                    });
//                                                        }else {
//                                                            showerror();
//                                                        }
//                                                    }
//                                                },FamilyMemberActivity.this);
//                                            }else {
//                                                id=1;
//                                                showerror();
//                                            }
//                                        }else if(result==102){
//                                            id=1;
//                                            mHandler.sendEmptyMessage(1);
//                                            Log.e("TAG","102-projectCode错误");
//                                            ToastUtil.showToast(FamilyMemberActivity.this,"操作失败");
//                                        }else {
//                                            id=1;
//                                            mHandler.sendEmptyMessage(1);
//                                            ToastUtil.showToast(FamilyMemberActivity.this,"操作失败");
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onError(String data) {
//                                        id=1;
//                                        mHandler.sendEmptyMessage(1);
//                                        ToastUtil.showToast(FamilyMemberActivity.this,"网络连接失败");
//                                    }
//                                });
//                    }
//                });
//            }
//        };
//
//        swipelistview.setAdapter(swipeAdapter);
//    }

    //我的家庭成员：下载成员
    public void initFamily() {
        mDialog = DialogThridUtils.showWaitDialog(FamilyMemberActivity.this, true);
        HttpClient.post(CommonUtil.APPURL, "sc_getFamily"
                , new Gson().toJson(new Sc_getFamily.GetFamilyParams(token, projectCode, roomNo))
                , new UICallback() {
                    @Override
                    public void process(String data) {
                        Log.e("TAG", "data=" + data);
                        Sc_getFamily.GetFamilyResult getFamilyResult = new Gson().fromJson(data, Sc_getFamily.GetFamilyResult.class);
                        int result = getFamilyResult.getResult();
                        if (result == 1) {
                            mHandler.sendEmptyMessage(1);
                            Log.e("TAG", "1-json解析错误");
                            id = 1;
                        } else if (result == 100) {
                            Log.e("TAG", "100-成功");
                            mHandler.sendEmptyMessage(1);
                            id = 1;
                            familyLists = getFamilyResult.getFamilyList();
                            if (familyLists != null && familyLists.size() > 0) {
                                if (familyLists != null && familyLists.size() > 1) {
                                    line.setVisibility(View.VISIBLE);
                                } else {
                                    line.setVisibility(View.GONE);
                                }

                                for (int i = 0; i < familyLists.size(); i++) {
                                    if (familyLists.get(i).getType() == 2) {
                                        if (!TextUtils.isEmpty(familyLists.get(i).getName())) {
                                            name.setText(familyLists.get(i).getName() + "(业主)");
                                        } else {
                                            name.setText("(业主)");
                                        }
                                        number.setText(familyLists.get(i).getMobilePhone());
                                        familyLists.remove(i);
                                        SharedPreferencesUtils.saveFamily(FamilyMemberActivity.this,familyLists.size());
                                    }
                                }

                            } else {
                                name.setText("");
                                number.setText("");
                            }

                            FamilyMemberSwipeAdapter swipeAdapter = new FamilyMemberSwipeAdapter(FamilyMemberActivity.this, familyLists, R.layout.item_swipelist_familymenmber) {
                                @Override
                                public void convert(final ViewHolder holder, Object object, final int position, View convertView) {
                                    holder.setText(R.id.tv_name, familyLists.get(position).getName());
                                    holder.setText(R.id.tv_number, familyLists.get(position).getMobilePhone());

                                    holder.setOnClickListener(R.id.btnDelete, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mDialog = DialogThridUtils.showWaitDialog(FamilyMemberActivity.this, true);
                                            mobilePhone = familyLists.get(position).getMobilePhone();
                                            HttpClient.post(CommonUtil.APPURL, "sc_deleteFamily"
                                                    , new Gson().toJson(new Sc_deleteFamily.DeleteFamilyParams(token, projectCode, mobilePhone))
                                                    , new UICallback() {
                                                        @Override
                                                        public void process(String data) {
                                                            Log.e("TAG", "data=" + data);
//                                                            Log.e("TAG","token="+token);
//                                                            Log.e("TAG","projectCode="+projectCode);
                                                            Log.e("TAG", "mobilePhone=" + mobilePhone);
                                                            Sc_deleteFamily.DeleteFamilyResult deleteFamilyResult = new Gson().fromJson(data, Sc_deleteFamily.DeleteFamilyResult.class);
                                                            int result = deleteFamilyResult.getResult();
                                                            if (result == 1) {
                                                                id = 1;
                                                                Log.e("TAG", "1-json解析失败");
                                                                mHandler.sendEmptyMessage(1);
                                                            } else if (result == 100) {
                                                                id = 1;
//                                                                mHandler.sendEmptyMessage(1);
                                                                Log.e("TAG", "100-成功");
//                                                                getFamily();
                                                                ((SwipeMenuLayout) holder.getConvertView()).quickClose();
                                                                familyLists.remove(position);
                                                                notifyDataSetChanged();
                                                                familynumber=SharedPreferencesUtils.getFamily(FamilyMemberActivity.this);
                                                                SharedPreferencesUtils.saveFamily(FamilyMemberActivity.this, familynumber-1);
                                                                mHandler.sendEmptyMessage(1);
                                                            } else if (result == 101) {
                                                                Log.e("TAG", "101-token错误");
                                                                if (id == 1) {
                                                                    id = id + 1;
                                                                    new GetToken(new CallBackInterface() {
                                                                        @Override
                                                                        public void gettoken(String str) {
                                                                            token = str;
                                                                            Log.e("TAG", "token=" + token);
                                                                            if (!TextUtils.isEmpty(token)) {
                                                                                HttpClient.post(CommonUtil.APPURL, "sc_deleteFamily"
                                                                                        , new Gson().toJson(new Sc_deleteFamily.DeleteFamilyParams(token, projectCode, mobilePhone))
                                                                                        , new UICallback() {
                                                                                            @Override
                                                                                            public void process(String data) {
//                                                            Log.e("TAG","data="+data);
//                                                            Log.e("TAG","token="+token);
//                                                            Log.e("TAG","projectCode="+projectCode);
//                                                            Log.e("TAG","mobilePhone="+mobilePhone);
                                                                                                Sc_deleteFamily.DeleteFamilyResult deleteFamilyResult = new Gson().fromJson(data, Sc_deleteFamily.DeleteFamilyResult.class);
                                                                                                int result = deleteFamilyResult.getResult();
                                                                                                if (result == 1) {
                                                                                                    id = 1;
                                                                                                    mHandler.sendEmptyMessage(1);
                                                                                                    Log.e("TAG", "1-json解析失败");
                                                                                                } else if (result == 100) {
                                                                                                    Log.e("TAG", "100-成功");
                                                                                                    ((SwipeMenuLayout) holder.getConvertView()).quickClose();
                                                                                                    familyLists.remove(position);
                                                                                                    notifyDataSetChanged();
                                                                                                    mHandler.sendEmptyMessage(1);
                                                                                                    familynumber=SharedPreferencesUtils.getFamily(FamilyMemberActivity.this);
                                                                                                    SharedPreferencesUtils.saveFamily(FamilyMemberActivity.this,familynumber-1);
                                                                                                } else if (result == 101) {
                                                                                                    Log.e("TAG", "101-token错误");
                                                                                                    id = 1;
                                                                                                    mHandler.sendEmptyMessage(1);
                                                                                                    showerror();
                                                                                                } else if (result == 102) {
                                                                                                    id = 1;
                                                                                                    mHandler.sendEmptyMessage(1);
                                                                                                    Log.e("TAG", "102-projectCode错误");
                                                                                                    ToastUtil.showToast(FamilyMemberActivity.this, "操作失败");
                                                                                                } else {
                                                                                                    id = 1;
                                                                                                    mHandler.sendEmptyMessage(1);
                                                                                                    ToastUtil.showToast(FamilyMemberActivity.this, "操作失败");
                                                                                                }
                                                                                            }

                                                                                            @Override
                                                                                            public void onError(String data) {
                                                                                                id = 1;
                                                                                                mHandler.sendEmptyMessage(1);
                                                                                                ToastUtil.showToast(FamilyMemberActivity.this, "网络连接失败");
                                                                                            }
                                                                                        });
                                                                            } else {
                                                                                showerror();
                                                                            }
                                                                        }
                                                                    }, FamilyMemberActivity.this);
                                                                } else {
                                                                    id = 1;
                                                                    showerror();
                                                                }
                                                            } else if (result == 102) {
                                                                id = 1;
                                                                mHandler.sendEmptyMessage(1);
                                                                Log.e("TAG", "102-projectCode错误");
                                                                ToastUtil.showToast(FamilyMemberActivity.this, "操作失败");
                                                            } else {
                                                                id = 1;
                                                                mHandler.sendEmptyMessage(1);
                                                                ToastUtil.showToast(FamilyMemberActivity.this, "操作失败");
                                                            }
                                                        }

                                                        @Override
                                                        public void onError(String data) {
                                                            id = 1;
                                                            mHandler.sendEmptyMessage(1);
                                                            ToastUtil.showToast(FamilyMemberActivity.this, "网络连接失败");
                                                        }
                                                    });
                                        }
                                    });
                                }
                            };

                            swipelistview.setAdapter(swipeAdapter);

                        } else if (result == 101) {
                            mHandler.sendEmptyMessage(1);
                            Log.e("TAG", "101-token错误");
                            if (id == 1) {
                                id = id + 1;
                                new GetToken(new CallBackInterface() {
                                    @Override
                                    public void gettoken(String str) {
                                        token = str;
                                        Log.e("TAG", "token=" + token);
                                        if (!TextUtils.isEmpty(token)) {
                                            initFamily();
                                        } else {
                                            showerror();
                                        }
                                    }
                                }, FamilyMemberActivity.this);
                            } else {
                                id = 1;
                                showerror();
                            }
                        } else if (result == 102) {
                            mHandler.sendEmptyMessage(1);
                            Log.e("TAG", "102-projectCode错误");
                            id = 1;
                            ToastUtil.showToast(FamilyMemberActivity.this, "操作失败");
                        } else {
                            mHandler.sendEmptyMessage(1);
                            id = 1;
                            ToastUtil.showToast(FamilyMemberActivity.this, "操作失败");
                        }
                    }

                    @Override
                    public void onError(String data) {
                        mHandler.sendEmptyMessage(1);
                        id = 1;
                        ToastUtil.showToast(FamilyMemberActivity.this, "网络连接失败");
                    }
                });
    }

    private void showerror() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FamilyMemberActivity.this, 1);
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

//    public void getFamily(){
//        HttpClient.post(CommonUtil.APPURL, "sc_getFamily"
//                , new Gson().toJson(new Sc_getFamily.GetFamilyParams(token, projectCode, roomNo))
//                , new UICallback() {
//                    @Override
//                    public void process(String data) {
//                        Log.e("TAG","data="+data);
//                        Sc_getFamily.GetFamilyResult getFamilyResult=new Gson().fromJson(data, Sc_getFamily.GetFamilyResult.class);
//                        int result=getFamilyResult.getResult();
//                        if(result==1){
//                            Log.e("TAG","1-json解析错误");
//                            id=1;
//                            mHandler.sendEmptyMessage(1);
//                        }else if(result==100) {
//                            Log.e("TAG", "100-成功");
//                            id = 1;
//                            mHandler.sendEmptyMessage(1);
//                            familyList = getFamilyResult.getFamilyList();
//                            if(!familyList.isEmpty()&&familyList!=null){
//                                SharedPreferencesUtils.saveFamily(FamilyMemberActivity.this,familyList.size()+"");
//                            }else {
//                                SharedPreferencesUtils.saveFamily(FamilyMemberActivity.this,"0");
//                            }
//                        }else if(result==101){
//                            Log.e("TAG","101-token错误");
//                            mHandler.sendEmptyMessage(1);
//                            if(id==1){
//                                id=id+1;
//                                new GetToken(new CallBackInterface() {
//                                    @Override
//                                    public void gettoken(String str) {
//                                        token=str;
//                                        Log.e("TAG","token="+token);
//                                        if(!TextUtils.isEmpty(token)){
//                                            getFamily();
//                                        }else {
//                                            showerror();
//                                        }
//                                    }
//                                },FamilyMemberActivity.this);
//                            }else {
//                                id=1;
//                                showerror();
//                            }
//                        }else if(result==102){
//                            Log.e("TAG","102-projectCode错误");
//                            mHandler.sendEmptyMessage(1);
//                            id=1;
//                            ToastUtil.showToast(FamilyMemberActivity.this,"操作失败");
//                        }else {
//                            mHandler.sendEmptyMessage(1);
//                            id=1;
//                            ToastUtil.showToast(FamilyMemberActivity.this,"操作失败");
//                        }
//                    }
//
//                    @Override
//                    public void onError(String data) {
//                        mHandler.sendEmptyMessage(1);
//                        id=1;
//                        Log.e("TAG","getfamily");
//                        ToastUtil.showToast(FamilyMemberActivity.this,"网络连接失败");
//                    }
//                });
//    }

    @Override
    protected void onResume() {
        isForeground = true;
        isLogin();
        //我的家庭成员：下载成员
        initFamily();
//        familyLists=SharedPreferencesUtils.getFamilyList(FamilyMemberActivity.this, Sc_getFamily.GetFamilyResult.Family.class);
//        Log.e("TAG","familyLists="+familyLists);
//        //设置数据
//        setFamily();
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
        AlertDialog.Builder builder=new AlertDialog.Builder(FamilyMemberActivity.this,1);
        builder.setTitle("提示");
        builder.setMessage("该账号已被其他设备登录");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startActivity(new Intent(FamilyMemberActivity.this, LoginActivity.class));
                SharedPreferencesUtils.cleanPassword(FamilyMemberActivity.this);
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
