package com.massky.greenlandvland.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.massky.greenlandvland.R;
import com.massky.greenlandvland.View.LoadMoreListView;
import com.massky.greenlandvland.common.CallBackInterface;
import com.massky.greenlandvland.common.CommonUtil;
import com.massky.greenlandvland.common.DialogThridUtils;
import com.massky.greenlandvland.common.GetToken;
import com.massky.greenlandvland.common.SharedPreferencesUtils;
import com.massky.greenlandvland.common.ToastUtil;
import com.massky.greenlandvland.model.entity.Sc_getDoorRecord;
import com.massky.greenlandvland.model.httpclient.HttpUrl.HttpClient;
import com.massky.greenlandvland.model.httpclient.UICallback;
import com.massky.greenlandvland.ui.adapter.OpenRecordAdapter;

import java.util.List;

/**
 * Created by masskywcy on 2018-11-09.
 */

public class Fragment_OpenRecord extends Fragment {
    private View view;
    private ImageView sliding;//回退键
    private LoadMoreListView listView;
    private OpenRecordAdapter adapter;
    private List<Sc_getDoorRecord.GetDoorRecordResult.Open> openList;
    private String token;
    private String projectCode;
    private String roomNo;
    private String page;
    private int pages;
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
            openList= (List<Sc_getDoorRecord.GetDoorRecordResult.Open>) msg.obj;
            adapter=new OpenRecordAdapter(getContext());
            listView.setAdapter(adapter);
//            opens.addAll(openList);
            adapter.appendData(openList,true);
//            adapter.upData();
//            listView.setLoadCompleted();
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
        HttpClient.post(CommonUtil.APPURL, "sc_getDoorRecord"
                , new Gson().toJson(new Sc_getDoorRecord.GetDoorRecordParams(token, projectCode, page,roomNo))
                , new UICallback() {
                    @Override
                    public void process(String data) {
//                        Log.e("TAG","data="+data);
//                        Log.e("TAG","page"+page);
                        Sc_getDoorRecord.GetDoorRecordResult getDoorRecordResult=new Gson().fromJson(data, Sc_getDoorRecord.GetDoorRecordResult.class);
                        int result=getDoorRecordResult.getResult();
                        if(result==1){
                            id=1;
                            listView.setLoadCompleted();
                            Log.e("TAG","1-json解析错误");
                            page=String.valueOf((pages-1));
                        }else if(result==100){
                            Log.e("TAG","100-成功");
                            id=1;
                            if(getDoorRecordResult.getOpenList()==null||getDoorRecordResult.getOpenList().size()<=0){
                                ToastUtil.showToast(getContext(),"没有数据了");
                                listView.setLoadCompleted();
                            }else {
                                openList.addAll(getDoorRecordResult.getOpenList());
                                adapter.appendData(openList,false);
                                adapter.upData();
                                listView.setLoadCompleted();
                            }
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
                                },getContext());
                            }else {
                                id=1;
                                listView.setLoadCompleted();
                                showerror();
                            }
                        }else if(result==102){
                            Log.e("TAG","102-projectCode错误");
                            id=1;
                            page=String.valueOf((pages-1));
                            listView.setLoadCompleted();
                        }else {
                            id=1;
                            ToastUtil.showToast(getContext(),"操作失败");
                            page=String.valueOf((pages-1));
                            listView.setLoadCompleted();
                        }
                    }

                    @Override
                    public void onError(String data) {
                        id=1;
                        listView.setLoadCompleted();
                        page=String.valueOf((pages-1));
                        ToastUtil.showToast(getContext(),"操作失败");
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_openrecord,container,false);
        token= SharedPreferencesUtils.getToken(getContext());
        projectCode=SharedPreferencesUtils.getProjectCode(getContext());
        roomNo=SharedPreferencesUtils.getRoomNo(getContext());
        //获取控件
        sliding= (ImageView) view.findViewById(R.id.sliding);
        listView= (LoadMoreListView) view.findViewById(R.id.listview);
        //添加监听器
        sliding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.slidingMenu.showMenu();
            }
        });
        initgetDoorRecord();
        return view;
    }

    private void initgetDoorRecord() {
        mDialog= DialogThridUtils.showWaitDialog(getContext(),true);
        page="01";
        HttpClient.post(CommonUtil.APPURL, "sc_getDoorRecord"
                , new Gson().toJson(new Sc_getDoorRecord.GetDoorRecordParams(token, projectCode, page,roomNo))
                , new UICallback() {
                    @Override
                    public void process(String data) {
//                        Log.e("TAG","data="+data);
//                        Log.e("TAG","token="+token);
//                        Log.e("TAG","projectCode="+projectCode);
//                        Log.e("TAG","page="+page);
                        Sc_getDoorRecord.GetDoorRecordResult getDoorRecordResult=new Gson().fromJson(data, Sc_getDoorRecord.GetDoorRecordResult.class);
                        int result=getDoorRecordResult.getResult();
                        if(result==1){
                            id=1;
                            Log.e("TAG","1-json解析错误");
                            mHandler.sendEmptyMessage(1);
                        }else if(result==100){
                            Log.e("TAG","100-成功");
                            id=1;
                            Message message=handler.obtainMessage();
                            message.obj=getDoorRecordResult.getOpenList();
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
                                            initgetDoorRecord();
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
                            id=1;
                            Log.e("TAG","102-projectCode错误");
                            mHandler.sendEmptyMessage(1);
                        }else {
                            id=1;
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(getContext(),"操作失败");
                        }
                    }

                    @Override
                    public void onError(String data) {
                        mHandler.sendEmptyMessage(1);
                        id=1;
                        ToastUtil.showToast(getContext(),"网络操作失败");
                    }
                });
    }
}
