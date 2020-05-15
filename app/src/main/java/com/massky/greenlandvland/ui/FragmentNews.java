package com.massky.greenlandvland.ui;

import android.app.AlertDialog;
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
import com.massky.greenlandvland.common.CallBackInterface;
import com.massky.greenlandvland.common.CommonUtil;
import com.massky.greenlandvland.common.GetToken;
import com.massky.greenlandvland.common.SharedPreferencesUtils;
import com.massky.greenlandvland.common.ToastUtil;
import com.massky.greenlandvland.model.entity.Sc_forumThreads;
import com.massky.greenlandvland.model.entity.Sc_getForumCategory;
import com.massky.greenlandvland.model.httpclient.HttpUrl.HttpClient;
import com.massky.greenlandvland.model.httpclient.UICallback;
import com.massky.greenlandvland.ui.adapter.NewsAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by masskywcy on 2018-11-13.
 */

public class FragmentNews extends Fragment {
    private View view;
    Sc_getForumCategory.GetForumCategoryResult.BbsCategory bbsCategoryList;
    private LoadMoreListView listView;
    private List<Sc_forumThreads.ForumThreadsResult.ForumThreads> forumThreadsList=new ArrayList<>();
    private NewsAdapter newsAdapter;
    private String token;
    private String projectCode;
    private String forumType;
    private String page;
    private int pages;
    private String roomNo;
    private int id=1;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            forumThreadsList= (List<Sc_forumThreads.ForumThreadsResult.ForumThreads>) msg.obj;
            Log.e("TAG","forumThreadsList="+forumThreadsList);
//            newsAdapter=new NewsAdapter(getContext(),forumThreadsList,R.layout.item_forum_listview){
//
//                @Override
//                public void convert(ViewHolder holder, Object object, final int position, View convertView) {
//                    if(forumThreadsList.get(position).getForumImage().size()<=0||forumThreadsList.get(position).getForumImage()==null){
//                        holder.setImageResource(R.id.image,R.mipmap.ic_launcher);
//                    }else {
//                        holder.setImageGlide(R.id.image,forumThreadsList.get(position).getForumImage().get(0).getImageUrl());
//                    }
//                    holder.setText(R.id.title,forumThreadsList.get(position).getForumTitle()+"");
//                    holder.setText(R.id.contents,forumThreadsList.get(position).getForumContent()+"");
//                    holder.setText(R.id.discuss,forumThreadsList.get(position).getDiscussCount()+"");
//                    holder.setText(R.id.name,forumThreadsList.get(position).getUserName()+"");
//                    //获取当前毫秒数  1秒=1000毫秒
//                    long time1=System.currentTimeMillis();
//                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    long time2 = 0;
//                    try {
//                        Date d = sdf.parse(forumThreadsList.get(position).getPushTime());
//                        time2=d.getTime();
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                    long time;
//                    time=time1-time2;
//                    if (time/1000/60/60/24/365>0){
//                        holder.setText(R.id.time,time/1000/60/60/24/365+"年前");
//                    }else if(time/1000/60/60/24>0){
//                        holder.setText(R.id.time,time/1000/60/60/24+"天前");
//                    }else if(time/1000/60/60>0){
//                        holder.setText(R.id.time,time/1000/60/60+"小时前");
//                    }else if(time/1000/60>0){
//                        holder.setText(R.id.time,time/1000/60+"分钟前");
//                    }else {
//                        holder.setText(R.id.time,"刚刚");
//                    }
//
////                    holder.setOnClickListener(R.id.btnDelete, new View.OnClickListener() {
////                        @Override
////                        public void onClick(View v) {
////                            int forumId=forumThreadsList.get(position).getId();
////                            HttpClient.post(CommonUtil.APPURL,"");
////                        }
////                    });
//
//                    holder.setOnClickListener(R.id.content, new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent=new Intent(getContext(),ForumDetialActivity.class);
//                            intent.putExtra("forum",forumThreadsList.get(position));
//                            startActivity(intent);
//                        }
//                    });
////                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////                        @Override
////                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                            Sc_forumThreads.ForumThreadsResult.ForumThreads forumThreads= (Sc_forumThreads.ForumThreadsResult.ForumThreads) parent.getItemAtPosition(position);
////                            Intent intent=new Intent(getContext(),ForumDetialActivity.class);
////                            intent.putExtra("forum",forumThreads);
////                            startActivity(intent);
////                        }
////                    });
//                }
//            };
            newsAdapter=new NewsAdapter(getActivity());
            listView.setAdapter(newsAdapter);
            newsAdapter.appendData(forumThreadsList,true);
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
        HttpClient.post(CommonUtil.APPURL, "sc_forumThreads"
                , new Gson().toJson(new Sc_forumThreads.ForumThreadsParams(token, projectCode, forumType, page, roomNo))
                , new UICallback() {
                    @Override
                    public void process(String data) {
//                        Log.e("TAG","data="+data);
//                        Log.e("TAG","token="+token);
//                        Log.e("TAG","projectCode="+projectCode);
//                        Log.e("TAG","forumType="+forumType);
//                        Log.e("TAG","page="+page);
                        Sc_forumThreads.ForumThreadsResult forumThreadsResult=new Gson().fromJson(data, Sc_forumThreads.ForumThreadsResult.class);
                        int result=forumThreadsResult.getResult();
                        if(result==1){
                            id=1;
                            listView.setLoadCompleted();
                            Log.e("TAG","1-json解析错误");
                            page=String.valueOf((pages-1));
                        }else if(result==100){
                            Log.e("TAG","100-成功");
                            id=1;
                            if(forumThreadsResult.getForumThreadsList()==null||forumThreadsResult.getForumThreadsList().size()<=0){
                                ToastUtil.showToast(getContext(),"没有数据了");
                                listView.setLoadCompleted();
                            }else {
                                forumThreadsList.addAll(forumThreadsResult.getForumThreadsList());
                                newsAdapter.appendData(forumThreadsList,false);
                                newsAdapter.upData();
                                listView.setLoadCompleted();
                            }
                        }else if(result==101){
                            Log.e("TAG","101-token错误");page=String.valueOf((pages-1));
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


    public static FragmentNews newInstance(Sc_getForumCategory.GetForumCategoryResult.BbsCategory bbsCategoryList) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("bbsCategory",bbsCategoryList);
        FragmentNews fragment = new FragmentNews();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragmentnews,null);
        Bundle bundle=getArguments();
        bbsCategoryList= (Sc_getForumCategory.GetForumCategoryResult.BbsCategory) bundle.getSerializable("bbsCategory");
        Log.e("TAG","bbsCategoryList="+bbsCategoryList);
        listView= (LoadMoreListView) view.findViewById(R.id.listview);

        token= SharedPreferencesUtils.getToken(getContext());
        projectCode=SharedPreferencesUtils.getProjectCode(getContext());
        roomNo=SharedPreferencesUtils.getRoomNo(getContext());
        forumType=bbsCategoryList.getCategoryName();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Sc_forumThreads.ForumThreadsResult.ForumThreads forumThreads= (Sc_forumThreads.ForumThreadsResult.ForumThreads) parent.getItemAtPosition(position);
                Intent intent=new Intent(getContext(),ForumDetialActivity.class);
                intent.putExtra("forum",forumThreads);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initforumThreads();
    }

    private void initforumThreads() {
        page="01";
        HttpClient.post(CommonUtil.APPURL, "sc_forumThreads"
                , new Gson().toJson(new Sc_forumThreads.ForumThreadsParams(token, projectCode, forumType, page, roomNo))
                , new UICallback() {
                    @Override
                    public void process(String data) {
//                        Log.e("TAG","data="+data);
//                        Log.e("TAG","token="+token);
//                        Log.e("TAG","projectCode="+projectCode);
//                        Log.e("TAG","forumType="+forumType);
//                        Log.e("TAG","page="+page);
                        Sc_forumThreads.ForumThreadsResult forumThreadsResult=new Gson().fromJson(data, Sc_forumThreads.ForumThreadsResult.class);
                        int result=forumThreadsResult.getResult();
                        if(result==1){
                            id=1;
                            listView.setLoadCompleted();
                            Log.e("TAG","1-json解析错误");
                        }else if(result==100){
                            Log.e("TAG","100-成功");
                            id=1;
                            Message msg=handler.obtainMessage();
                            msg.obj=forumThreadsResult.getForumThreadsList();
                            handler.sendMessage(msg);
                        }else if(result==101){
                            Log.e("TAG","101-token错误");
                            if(id==1){
                                id=id+1;
                                new GetToken(new CallBackInterface() {
                                    @Override
                                    public void gettoken(String str) {
                                        token=str;
                                        Log.e("TAG","token="+token);
                                        if(!TextUtils.isEmpty(token)){
                                            initforumThreads();
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
                            listView.setLoadCompleted();
                        }else {
                            id=1;
                            listView.setLoadCompleted();
                            ToastUtil.showToast(getContext(),"操作失败");
                        }
                    }

                    @Override
                    public void onError(String data) {
                        id=1;
                        listView.setLoadCompleted();
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
}
