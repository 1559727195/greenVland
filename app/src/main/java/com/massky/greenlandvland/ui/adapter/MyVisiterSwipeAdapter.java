
package com.massky.greenlandvland.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.massky.greenlandvland.View.swipelayout.ViewHolder;
import com.massky.greenlandvland.model.entity.Sc_inviteRecord;

import java.util.ArrayList;
import java.util.List;


public abstract class MyVisiterSwipeAdapter extends BaseAdapter {
    private final LayoutInflater mInflater;
    private List<Sc_inviteRecord.InviteRecordResult.Invite> inviteList=new ArrayList<>();
    private Context mContext;
    private int layoutId;

    public MyVisiterSwipeAdapter(Context context, List<Sc_inviteRecord.InviteRecordResult.Invite> inviteList, int layoutId) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.inviteList=inviteList;
        this.layoutId=layoutId;
    }

    public void setListData(List<Sc_inviteRecord.InviteRecordResult.Invite> lists) {
        inviteList = lists;
        notifyDataSetChanged();
    }
    public void appendData(List<Sc_inviteRecord.InviteRecordResult.Invite> list, boolean isClearOld){
        if(list==null){//没有传过来的这条数据
            return;//结束方法
        }
        if(isClearOld){//是否清除老数据
            inviteList.clear();//清空集合
            inviteList.addAll(list);//向集合中添加数据
            Log.d("TAG","更新适配器");
        }else{
            inviteList.addAll(list);//不清空数据直接添加
        }
    }
    //更新适配器
    public void upData(){
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return inviteList==null ? 0 : inviteList.size();
    }

    @Override
    public Object getItem(int position) {
        return inviteList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder= ViewHolder.get(mContext,convertView,parent,
                layoutId,position);
        convert(holder,getItem(position),position,convertView);
        return holder.getConvertView();
    }

    public abstract void convert(ViewHolder holder, Object object, int position, View convertView);
}
