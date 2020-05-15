package com.massky.greenlandvland.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.massky.greenlandvland.View.swipelayout.ViewHolder;
import com.massky.greenlandvland.model.entity.Sc_forumThreadsDiscuss;

import java.util.List;

/**
 * Created by masskywcy on 2018-05-29.
 */

public abstract class DiscussAdapter extends BaseAdapter {
    private Context context;
    protected LayoutInflater layoutInflater;
    private List<Sc_forumThreadsDiscuss.ForumThreadsDiscussResult.ForumDiscuss> forumDiscussList;
    private int layoutId;

    public DiscussAdapter(Context context, List<Sc_forumThreadsDiscuss.ForumThreadsDiscussResult.ForumDiscuss> forumDiscussList, int layoutId){
        this.context=context;
        this.forumDiscussList=forumDiscussList;
        this.layoutId=layoutId;
        layoutInflater = LayoutInflater.from(context);
    }

    public void appendData(List<Sc_forumThreadsDiscuss.ForumThreadsDiscussResult.ForumDiscuss> list, boolean isClearOld){
        if(list==null){//没有传过来的这条数据
            return;//结束方法
        }
        if(isClearOld){//是否清除老数据
            forumDiscussList.clear();//清空集合
            forumDiscussList.addAll(list);//向集合中添加数据
            Log.d("TAG","更新适配器");
        }else{
            forumDiscussList.addAll(list);//不清空数据直接添加
        }
    }

    //更新适配器
    public void upData(){
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return forumDiscussList==null ?0:forumDiscussList.size();
    }

    @Override
    public Object getItem(int position) {
        return forumDiscussList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder= ViewHolder.get(context, convertView, parent,
                layoutId, position);
        Log.d("TAG", "getView() called with: " + "position = [" + position + "], convertView = [" + convertView + "], parent = [" + parent + "]");
        convert(holder, getItem(position), position, convertView);
        return holder.getConvertView();
    }

    public abstract void convert(ViewHolder holder, Object object, int position, View convertView);
}
