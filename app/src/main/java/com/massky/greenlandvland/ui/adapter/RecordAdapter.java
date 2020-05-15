package com.massky.greenlandvland.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.massky.greenlandvland.View.swipelayout.ViewHolder;
import com.massky.greenlandvland.model.entity.Sc_myComplaint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/4 0004.
 */

public abstract class RecordAdapter extends BaseAdapter {
    private Context context;
    protected LayoutInflater layoutInflater;
    private List<Sc_myComplaint.MyComplaintResult.Complaint> complaintList=new ArrayList<>();
    private int layoutId;

    public RecordAdapter(Context context, List<Sc_myComplaint.MyComplaintResult.Complaint> complaintList , int layoutId){
        this.context=context;
        layoutInflater = LayoutInflater.from(context);
        this.complaintList=complaintList;
        this.layoutId=layoutId;
    }

    public void appendData(List<Sc_myComplaint.MyComplaintResult.Complaint> list, boolean isClearOld){
        if(list==null){//没有传过来的这条数据
            return;//结束方法
        }
        if(isClearOld){//是否清除老数据
            complaintList.clear();//清空集合
            complaintList.addAll(list);//向集合中添加数据
            Log.d("TAG","更新适配器");
        }else{
            complaintList.addAll(list);//不清空数据直接添加
        }
    }

    //更新适配器
    public void upData(){
        this.notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return complaintList==null ?0:complaintList.size();
    }

    @Override
    public Object getItem(int position) {
        return complaintList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder viewHolder=null;
//        if(convertView==null){
//            convertView=layoutInflater.inflate(R.layout.item_record_listview,null);
//            viewHolder=new ViewHolder(convertView);
//            convertView.setTag(viewHolder);
//        }else {
//            viewHolder= (ViewHolder) convertView.getTag();
//        }
//        Sc_myComplaint.MyComplaintResult.Complaint complaint=complaintList.get(position);
//        viewHolder.record.setText(complaint.getComplaintCategory());
//        viewHolder.title.setText(complaint.getComplaintTitle());
//        viewHolder.time.setText(complaint.getComplaintTime());
//        return convertView;
        ViewHolder holder= ViewHolder.get(context, convertView, parent,
                layoutId, position);
        Log.d("TAG", "getView() called with: " + "position = [" + position + "], convertView = [" + convertView + "], parent = [" + parent + "]");
        convert(holder, getItem(position), position, convertView);
        return holder.getConvertView();
    }
//    class ViewHolder{
//        private TextView record;
//        private TextView title;
//        private TextView time;
//        public ViewHolder(View view){
//            this.record= (TextView) view.findViewById(R.id.record);
//            this.title= (TextView) view.findViewById(R.id.title);
//            this.time= (TextView) view.findViewById(R.id.time);
//        }
//    }
public abstract void convert(ViewHolder holder, Object object, int position, View convertView);

}
