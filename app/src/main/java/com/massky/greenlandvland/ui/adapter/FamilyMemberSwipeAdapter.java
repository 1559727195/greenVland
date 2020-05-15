package com.massky.greenlandvland.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.massky.greenlandvland.View.swipelayout.ViewHolder;
import com.massky.greenlandvland.model.entity.Sc_getFamily;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by honjane on 2015/12/25.
 */
public abstract class FamilyMemberSwipeAdapter extends BaseAdapter {
    private final LayoutInflater mInflater;
    private List<Sc_getFamily.GetFamilyResult.Family> familyLists=new ArrayList<>();
    private Context mContext;
    private int layoutId;


    public FamilyMemberSwipeAdapter(Context context, List<Sc_getFamily.GetFamilyResult.Family> familyLists, int layoutId) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        this.familyLists=familyLists;
        this.layoutId=layoutId;
    }

//    public void setListData(List<Sc_getFamily.GetFamilyResult.Family> lists) {
//        familyLists = lists;
//        notifyDataSetChanged();
//    }

    //更新适配器
    public void upData() {
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return familyLists==null ? 0 : familyLists.size();
    }

    @Override
    public Object getItem(int position) {
        return familyLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder= ViewHolder.get(mContext, convertView, parent,
                layoutId, position);
        convert(holder, getItem(position), position, convertView);
        return holder.getConvertView();
    }
    public abstract void convert(ViewHolder holder, Object object, int position, View convertView);
}
