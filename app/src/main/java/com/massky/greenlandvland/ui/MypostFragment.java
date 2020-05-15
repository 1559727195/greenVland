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
import android.widget.AdapterView;

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
import com.massky.greenlandvland.model.entity.Sc_deleteForum;
import com.massky.greenlandvland.model.entity.Sc_myFourmList;
import com.massky.greenlandvland.model.httpclient.HttpUrl.HttpClient;
import com.massky.greenlandvland.model.httpclient.UICallback;
import com.massky.greenlandvland.ui.adapter.MyPostAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by masskywcy on 2018-11-13.
 */

public class MypostFragment extends Fragment {
    private View view;
    private LoadMoreListView listView;
    private MyPostAdapter postAdapter;
    private String token;
    private String projectCode;
    private String page;
    private int pages;
    private String roomNo;
    private int id=1;
    private List<Sc_myFourmList.MyFourmListResult.MyFourm> myFourm=new ArrayList<>();
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
            myFourm= (List<Sc_myFourmList.MyFourmListResult.MyFourm>) msg.obj;
            postAdapter=new MyPostAdapter(getContext(),myFourm, R.layout.item_mypost_listview){

                @Override
                public void convert(final ViewHolder holder, Object object, final int position, View convertView) {
                    if(myFourm.get(position).getForumImage().size()<=0||myFourm.get(position)==null){
                        holder.setImageResource(R.id.image,R.mipmap.ic_launcher);
                    }else {
//                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        holder.setImageGlide(R.id.image,myFourm.get(position).getForumImage().get(0).getImageUrl());
                    }
                    holder.setText(R.id.title,myFourm.get(position).getForumTitle()+"");
                    holder.setText(R.id.contents,myFourm.get(position).getForumContent()+"");
                    holder.setText(R.id.style,myFourm.get(position).getForumCategory()+"");
                    holder.setText(R.id.comment,myFourm.get(position).getDiscussCount()+"");
                    String[] string=myFourm.get(position).getPushTime().split("-");
                    String[] string2=string[2].split(" ");
                    holder.setText(R.id.nianyue,string[0]+"\n"+string[1]+"月");
                    holder.setText(R.id.ri,string2[0]);
                    holder.setText(R.id.time,string2[1]);


                    holder.setOnClickListener(R.id.btnDelete, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final int forumId=myFourm.get(position).getId();
                            HttpClient.post(CommonUtil.APPURL, "sc_deleteForum"
                                    , new Gson().toJson(new Sc_deleteForum.DeleteForumParams(token, projectCode, forumId))
                                    , new UICallback() {
                                        @Override
                                        public void process(String data) {
                                            Log.e("TAG","data="+data);
                                            Log.e("TAG","token="+token);
                                            Log.e("TAG","projectCode="+projectCode);
                                            Log.e("TAG","forumId="+forumId);
                                            Sc_deleteForum.DeleteForumResult deleteForumResult=new Gson().fromJson(data, Sc_deleteForum.DeleteForumResult.class);
                                            int result=deleteForumResult.getResult();
                                            if(result==1){
                                                Log.e("TAG","1-json解析失败");
                                                id=1;
                                            }else if(result==100){
                                                Log.e("TAG","100-成功");
                                                id=1;
                                                ((SwipeMenuLayout) holder.getConvertView()).quickClose();
                                                myFourm.remove(position);
                                                notifyDataSetChanged();
                                            }else if(result==101){
                                                Log.e("TAG","101-token错误");
                                                if(id==1) {
                                                    id = id + 1;
                                                    new GetToken(new CallBackInterface() {
                                                        @Override
                                                        public void gettoken(String str) {
                                                            token=str;
                                                            HttpClient.post(CommonUtil.APPURL, "sc_deleteForum"
                                                                    , new Gson().toJson(new Sc_deleteForum.DeleteForumParams(token, projectCode, forumId))
                                                                    , new UICallback() {
                                                                        @Override
                                                                        public void process(String data) {
                                                                            Log.e("TAG","data="+data);
                                                                            Log.e("TAG","token="+token);
                                                                            Log.e("TAG","projectCode="+projectCode);
                                                                            Log.e("TAG","forumId="+forumId);
                                                                            Sc_deleteForum.DeleteForumResult deleteForumResult=new Gson().fromJson(data, Sc_deleteForum.DeleteForumResult.class);
                                                                            int result=deleteForumResult.getResult();
                                                                            if(result==1){
                                                                                Log.e("TAG","1-json解析失败");
                                                                                id=1;
                                                                            }else if(result==100){
                                                                                Log.e("TAG","100-成功");
                                                                                id=1;
                                                                                ((SwipeMenuLayout) holder.getConvertView()).quickClose();
                                                                                myFourm.remove(position);
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

                    holder.setOnClickListener(R.id.content, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Sc_myFourmList.MyFourmListResult.MyFourm myFourms=myFourm.get(position) ;
                            Intent intent=new Intent(getContext(),PostDetialActivity.class);
                            intent.putExtra("myFourm",myFourms);
                            startActivity(intent);
                        }
                    });
                }
            };
            listView.setAdapter(postAdapter);
//            postAdapter.appendData(myFourm,true);
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
        HttpClient.post(CommonUtil.APPURL, "sc_myFourmList",
                new Gson().toJson(new Sc_myFourmList.MyFourmListParams(token, projectCode, page, roomNo))
                , new UICallback() {
                    @Override
                    public void process(String data) {
//                        Log.e("TAG","data="+data);
//                        Log.e("TAG","token="+token);
//                        Log.e("TAG","projectCode="+projectCode);
//                        Log.e("TAG","page="+page);
                        Sc_myFourmList.MyFourmListResult myFourmListResult=new Gson().fromJson(data, Sc_myFourmList.MyFourmListResult.class);
                        int result=myFourmListResult.getResult();
                        if(result==1){
                            id=1;
                            Log.e("TAG","1-json解析错误");
                        }else if(result==100){
                            Log.e("TAG","100-成功");
                            id=1;
                            if(myFourmListResult.getMyFourmList()==null||myFourmListResult.getMyFourmList().size()<=0){
                                ToastUtil.showToast(getContext(),"没有数据了");
                                listView.setLoadCompleted();
                            }else {
                                myFourm.addAll(myFourmListResult.getMyFourmList());
                                postAdapter.appendData(myFourm,false);
                                postAdapter.upData();
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
        view=inflater.inflate(R.layout.fragment_mypost,container,false);
        token= SharedPreferencesUtils.getToken(getContext());
        projectCode=SharedPreferencesUtils.getProjectCode(getContext());
        roomNo=SharedPreferencesUtils.getRoomNo(getContext());
        listView= (LoadMoreListView) view.findViewById(R.id.listview);

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Sc_myFourmList.MyFourmListResult.MyFourm myFourm= (Sc_myFourmList.MyFourmListResult.MyFourm) parent.getItemAtPosition(position);
//                Intent intent=new Intent(getContext(),PostDetialActivity.class);
//                intent.putExtra("myFourm",myFourm);
//                startActivity(intent);
//            }
//        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //获取数据
        initPosy();
    }

    //获取数据
    private void initPosy() {
        mDialog= DialogThridUtils.showWaitDialog(getContext(),true);
        page="01";
        HttpClient.post(CommonUtil.APPURL, "sc_myFourmList",
                new Gson().toJson(new Sc_myFourmList.MyFourmListParams(token, projectCode, page, roomNo))
                , new UICallback() {
                    @Override
                    public void process(String data) {
//                        Log.e("TAG","data="+data);
//                        Log.e("TAG","token="+token);
//                        Log.e("TAG","projectCode="+projectCode);
//                        Log.e("TAG","page="+page);
                        Sc_myFourmList.MyFourmListResult myFourmListResult=new Gson().fromJson(data, Sc_myFourmList.MyFourmListResult.class);
                        int result=myFourmListResult.getResult();
                        if(result==1){
                            Log.e("TAG","1-json解析错误");
                            id=1;
                            mHandler.sendEmptyMessage(1);
                        }else if(result==100){
                            Log.e("TAG","100-成功");
                            id=1;
                            Message message=handler.obtainMessage();
                            message.obj=myFourmListResult.getMyFourmList();
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
                                            initPosy();
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
                            Log.e("TAG","102-projectCode错误");
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
