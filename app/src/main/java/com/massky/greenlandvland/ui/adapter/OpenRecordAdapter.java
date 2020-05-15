package com.massky.greenlandvland.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.massky.greenlandvland.R;
import com.massky.greenlandvland.model.entity.Sc_getDoorRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by masskywcy on 2017-12-04.
 */

public class OpenRecordAdapter extends BaseAdapter {
    private Context context;
    protected LayoutInflater layoutInflater;
    private List<Sc_getDoorRecord.GetDoorRecordResult.Open> openList=new ArrayList<>();

    public OpenRecordAdapter(Context context){
        this.context=context;
        layoutInflater= LayoutInflater.from(context);
    }

    public void appendData(List<Sc_getDoorRecord.GetDoorRecordResult.Open> list, boolean isClearOld){
        if(list==null){//没有传过来的这条数据
            return;//结束方法
        }
        if(isClearOld){//是否清除老数据
            openList.clear();//清空集合
            openList.addAll(list);//向集合中添加数据
            Log.d("TAG","更新适配器");
        }else{
            openList.addAll(list);//不清空数据直接添加
        }
    }
    //更新适配器
    public void upData(){
        this.notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return openList.size();
    }

    @Override
    public Object getItem(int position) {
        return openList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView==null){
            convertView=layoutInflater.inflate(R.layout.item_openrecord_listview,null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        Sc_getDoorRecord.GetDoorRecordResult.Open open=openList.get(position);
        viewHolder.time.setText(open.getOpenTime());
        viewHolder.title.setText(open.getDoorName());
        viewHolder.name.setText(open.getUserName());
        if(open.getFloor()!=null&&open.getFloor().length()>0){
            viewHolder.statues.setText(open.getFloor()+"层"+open.getOpenResult());
        }else {
            viewHolder.statues.setText(open.getOpenResult());
        }
        return convertView;
    }
    class ViewHolder{
        private TextView time,title,name,statues;
        public ViewHolder(View view){
            this.time= (TextView) view.findViewById(R.id.time);
            this.title= (TextView) view.findViewById(R.id.title);
            this.name= (TextView) view.findViewById(R.id.name);
            this.statues= (TextView) view.findViewById(R.id.statues);
        }
    }
}
