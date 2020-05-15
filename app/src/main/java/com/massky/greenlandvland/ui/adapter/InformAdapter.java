package com.massky.greenlandvland.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.massky.greenlandvland.View.swipelayout.ViewHolder;
import com.massky.greenlandvland.model.entity.Sc_notification;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by masskywcy on 2017-12-04.
 */

public abstract class InformAdapter extends BaseAdapter {
    private Context context;
    protected LayoutInflater layoutInflater;
    private List<Sc_notification.NotificationResult.Notifacation> notifacationList=new ArrayList<>();
    private int layoutId;

    public InformAdapter(Context context, List<Sc_notification.NotificationResult.Notifacation> notifacationList, int layoutId){
        this.context=context;
        layoutInflater = LayoutInflater.from(context);
        this.notifacationList=notifacationList;
        this.layoutId=layoutId;
    }

    public void appendData(List<Sc_notification.NotificationResult.Notifacation> list, boolean isClearOld){
        if(list==null){//没有传过来的这条数据
            return;//结束方法
        }
        if(isClearOld){//是否清除老数据
            notifacationList.clear();//清空集合
            notifacationList.addAll(list);//向集合中添加数据
            Log.d("TAG","更新适配器");
        }else{
            notifacationList.addAll(list);//不清空数据直接添加
        }
    }
    //更新适配器
    public void upData() {
        this.notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return notifacationList==null?0:notifacationList.size();
    }

    @Override
    public Object getItem(int position) {
        return notifacationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder viewHolder=null;
//        if(convertView==null){
//            convertView=layoutInflater.inflate(R.layout.item_inform_listview,null);
//            viewHolder=new ViewHolder(convertView);
//            convertView.setTag(viewHolder);
//        }else {
//            viewHolder= (ViewHolder) convertView.getTag();
//        }
//        Sc_notification.NotificationResult.Notification notification=notificationList.get(position);
//        viewHolder.tv_time.setText(notification.getNotificationTime());
//        viewHolder.tv_title.setText(notification.getNotificationTitle());
//        viewHolder.tv_content.setText(notification.getNotificationContent());
//        if(notification.getNotificationSign()==1){
//            viewHolder.iv_hint.setVisibility(View.VISIBLE);
//        }else {
//            viewHolder.iv_hint.setVisibility(View.GONE);
//        }
//        return convertView;
        ViewHolder holder=ViewHolder.get(context,convertView,parent,
                layoutId,position);
        convert(holder,getItem(position),position,convertView);
        return holder.getConvertView();
    }
    public abstract void convert(ViewHolder holder, Object object, int position, View convertView);

//    class ViewHolder{
//        private TextView tv_time;
//        private TextView tv_title;
//        private TextView tv_content;
//        private ImageView iv_hint;
//        public ViewHolder(View view){
//            this.tv_time= (TextView) view.findViewById(R.id.tv_time);
//            this.tv_title= (TextView) view.findViewById(R.id.tv_title);
//            this.tv_content= (TextView) view.findViewById(R.id.tv_content);
//            this.iv_hint= (ImageView) view.findViewById(R.id.iv_hint);
//        }
//    }
}
