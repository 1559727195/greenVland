package com.massky.greenlandvland.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.google.gson.Gson;
import com.massky.greenlandvland.R;
import com.massky.greenlandvland.View.LoadMoreListView;
import com.massky.greenlandvland.View.swipelayout.SwipeMenuLayout;
import com.massky.greenlandvland.View.swipelayout.ViewHolder;
import com.massky.greenlandvland.common.CallBackInterface;
import com.massky.greenlandvland.common.CommonUtil;
import com.massky.greenlandvland.common.DialogThridUtils;
import com.massky.greenlandvland.common.GetToken;
import com.massky.greenlandvland.common.SharedPreferencesUtils;
import com.massky.greenlandvland.common.ToastUtil;
import com.massky.greenlandvland.model.entity.Sc_deleteHuiTie;
import com.massky.greenlandvland.model.entity.Sc_myFourmDiscuss;
import com.massky.greenlandvland.model.httpclient.HttpUrl.HttpClient;
import com.massky.greenlandvland.model.httpclient.UICallback;
import com.massky.greenlandvland.ui.adapter.ResponseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by masskywcy on 2018-11-13.
 */

public class ResponseFragment extends Fragment {
    private View view;
    private LoadMoreListView listView;
    private ResponseAdapter adapter;
    private String token;
    private String projectCode;
    private String page;
    private int pages;
    private String roomNo;
    private int id=1;
    private List<Sc_myFourmDiscuss.MyFourmDiscussResult.Huitie> huitieList=new ArrayList<>();
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
            huitieList= (List<Sc_myFourmDiscuss.MyFourmDiscussResult.Huitie>) msg.obj;
            adapter=new ResponseAdapter(getContext(),huitieList, R.layout.item_response_listview){

                @Override
                public void convert(final ViewHolder holder, Object object, final int position, View convertView) {
                    holder.setText(R.id.title,huitieList.get(position).getForumTitle()+"");
                    holder.setText(R.id.contents,"回复："+huitieList.get(position).getForumContent()+"");
                    holder.setText(R.id.style,huitieList.get(position).getForumCategory()+"");
                    holder.setText(R.id.comment,huitieList.get(position).getDiscussCount()+"");
                    String[] string=huitieList.get(position).getPushTime().split("-");
                    String[] string2=string[2].split(" ");
                    holder.setText(R.id.nianyue,string[0]+"\n"+string[1]+"月");
                    holder.setText(R.id.ri,string2[0]);

                    holder.setOnClickListener(R.id.content, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.e("TAG","点击");
                            Sc_myFourmDiscuss.MyFourmDiscussResult.Huitie huitie=huitieList.get(position) ;
                            Intent intent=new Intent(getContext(),ResponseDetialActivity.class);
                            intent.putExtra("huitie",huitie);
                            startActivity(intent);
                        }
                    });

                    holder.setOnClickListener(R.id.btnDelete, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final int discussId=huitieList.get(position).getId();
                            HttpClient.post(CommonUtil.APPURL, "sc_deleteHuiTie"
                                    , new Gson().toJson(new Sc_deleteHuiTie.DeleteHuiTieParams(token, projectCode, discussId))
                                    , new UICallback() {
                                        @Override
                                        public void process(String data) {
                                            Log.e("TAG","data="+data);
                                            Log.e("TAG","token="+token);
                                            Log.e("TAG","projectCode="+projectCode);
                                            Log.e("TAG","discussId="+discussId);
                                            Sc_deleteHuiTie.DeleteHuiTieResult deleteHuiTieResult=new Gson().fromJson(data, Sc_deleteHuiTie.DeleteHuiTieResult.class);
                                            int result=deleteHuiTieResult.getResult();
                                            if(result==1){
                                                Log.e("TAG","1-json解析失败");
                                                id=1;
                                            }else if(result==100){
                                                Log.e("TAG","100-成功");
                                                id=1;
                                                ((SwipeMenuLayout) holder.getConvertView()).quickClose();
                                                huitieList.remove(position);
                                                notifyDataSetChanged();
                                            }else if(result==101){
                                                Log.e("TAG","101-token错误");
                                                if(id==1) {
                                                    id = id + 1;
                                                    new GetToken(new CallBackInterface() {
                                                        @Override
                                                        public void gettoken(String str) {
                                                            token = str;
                                                            HttpClient.post(CommonUtil.APPURL, "sc_deleteHuiTie"
                                                                    , new Gson().toJson(new Sc_deleteHuiTie.DeleteHuiTieParams(token, projectCode, discussId))
                                                                    , new UICallback() {
                                                                        @Override
                                                                        public void process(String data) {
                                                                            Log.e("TAG","data="+data);
                                                                            Log.e("TAG","token="+token);
                                                                            Log.e("TAG","projectCode="+projectCode);
                                                                            Log.e("TAG","discussId="+discussId);
                                                                            Sc_deleteHuiTie.DeleteHuiTieResult deleteHuiTieResult=new Gson().fromJson(data, Sc_deleteHuiTie.DeleteHuiTieResult.class);
                                                                            int result=deleteHuiTieResult.getResult();
                                                                            if(result==1){
                                                                                Log.e("TAG","1-json解析失败");
                                                                                id=1;
                                                                            }else if(result==100){
                                                                                Log.e("TAG","100-成功");
                                                                                id=1;
                                                                                ((SwipeMenuLayout) holder.getConvertView()).quickClose();
                                                                                huitieList.remove(position);
                                                                                notifyDataSetChanged();
                                                                            }else if(result==101){
                                                                                Log.e("TAG","101-token错误");
                                                                                id=1;
                                                                                showerror();
                                                                            }else if(result==102){
                                                                                Log.e("TAG","102-projectCode错误");
                                                                                id=1;
                                                                                ToastUtil.showToast(getContext(),"操作失败");
                                                                            }else {
                                                                                id=1;
                                                                                ToastUtil.showToast(getContext(),"操作失败");
                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onError(String data) {
                                                                            id=1;
                                                                            ToastUtil.showToast(getContext(),"网络连接失败");
                                                                        }
                                                                    });
                                                        }
                                                    },getContext());
                                                }else {
                                                    id = 1;
                                                    showerror();
                                                }
                                            }else if(result==102){
                                                Log.e("TAG","102-projectCode错误");
                                                id=1;
                                                ToastUtil.showToast(getContext(),"操作失败");
                                            }else {
                                                id=1;
                                                ToastUtil.showToast(getContext(),"操作失败");
                                            }
                                        }

                                        @Override
                                        public void onError(String data) {
                                            id=1;
                                            ToastUtil.showToast(getContext(),"网络连接失败");
                                        }
                                    });
                        }
                    });
                }
            };
            listView.setAdapter(adapter);
//            adapter.appendData(huitieList,true);
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
        HttpClient.post(CommonUtil.APPURL, "sc_myFourmDiscuss",
                new Gson().toJson(new Sc_myFourmDiscuss.MyFourmDiscussParams(token, projectCode, page, roomNo))
                , new UICallback() {
                    @Override
                    public void process(String data) {
//                        Log.e("TAG","data="+data);
//                        Log.e("TAG","token="+token);
//                        Log.e("TAG","projectCode="+projectCode);
//                        Log.e("TAG","page="+page);
                        Sc_myFourmDiscuss.MyFourmDiscussResult myFourmDiscussResult=new Gson().fromJson(data, Sc_myFourmDiscuss.MyFourmDiscussResult.class);
                        int result=myFourmDiscussResult.getResult();
                        if(result==1){
                            id=1;
                            Log.e("TAG","1-json解析错误");
                            page=String.valueOf((pages-1));
                            listView.setLoadCompleted();
                        }else if(result==100){
                            Log.e("TAG","100-成功");
                            if(myFourmDiscussResult.getHuitieList()==null||myFourmDiscussResult.getHuitieList().size()<=0){
                                ToastUtil.showToast(getContext(),"没有数据了");
                                listView.setLoadCompleted();
                            }else {
                                huitieList.addAll(myFourmDiscussResult.getHuitieList());
                                adapter.appendData(huitieList,false);
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
                            Log.e("TAG","102-projectCode 不正确");
                            id=1;
                            page=String.valueOf((pages-1));
                            listView.setLoadCompleted();
                        }else {
                            id=1;
                            listView.setLoadCompleted();
                            page=String.valueOf((pages-1));
                            ToastUtil.showToast(getContext(),"操作失败");
                        }
                    }

                    @Override
                    public void onError(String data) {
                        id=1;
                        listView.setLoadCompleted();
                        page=String.valueOf((pages-1));
                        ToastUtil.showToast(getContext(),"网络操作失败");
                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_response,container,false);
        token= SharedPreferencesUtils.getToken(getContext());
        projectCode=SharedPreferencesUtils.getProjectCode(getContext());
        roomNo=SharedPreferencesUtils.getRoomNo(getContext());
        listView= (LoadMoreListView) view.findViewById(R.id.listview);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //获取数据
        initResponse();
    }

    //获取数据
    private void initResponse() {
        mDialog= DialogThridUtils.showWaitDialog(getContext(),true);
        page="01";
        HttpClient.post(CommonUtil.APPURL, "sc_myFourmDiscuss",
                new Gson().toJson(new Sc_myFourmDiscuss.MyFourmDiscussParams(token, projectCode, page, roomNo))
                , new UICallback() {
                    @Override
                    public void process(String data) {
//                        Log.e("TAG","data="+data);
//                        Log.e("TAG","token="+token);
//                        Log.e("TAG","projectCode="+projectCode);
//                        Log.e("TAG","page="+page);
                        Sc_myFourmDiscuss.MyFourmDiscussResult myFourmDiscussResult=new Gson().fromJson(data, Sc_myFourmDiscuss.MyFourmDiscussResult.class);
                        int result=myFourmDiscussResult.getResult();
                        if(result==1){
                            id=1;
                            Log.e("TAG","1-json解析错误");
                            mHandler.sendEmptyMessage(1);
                        }else if(result==100){
                            Log.e("TAG","100-成功");
                            id=1;
                            Message message=handler.obtainMessage();
                            message.obj=myFourmDiscussResult.getHuitieList();
                            handler.sendMessage(message);
                            mHandler.sendEmptyMessage(1);
                        }else if(result==101){
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
                                            initResponse();
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
                            Log.e("TAG", "102-projectCode 错误");
                            id=1;
                            mHandler.sendEmptyMessage(1);
                        }else {
                            id=1;
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(getContext(),"操作失败");
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
}
