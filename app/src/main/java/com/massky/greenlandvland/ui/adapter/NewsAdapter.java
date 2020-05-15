package com.massky.greenlandvland.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.massky.greenlandvland.R;
import com.massky.greenlandvland.model.entity.Sc_forumThreads;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by masskywcy on 2017-11-28.
 */

public class NewsAdapter extends BaseAdapter {
    protected Context context;
    protected LayoutInflater layoutInflater;
    private List<Sc_forumThreads.ForumThreadsResult.ForumThreads> forumThreadsList=new ArrayList<>();


    public NewsAdapter(Context context){
        this.context=context;
        layoutInflater= LayoutInflater.from(context);
    }
    public void appendData(List<Sc_forumThreads.ForumThreadsResult.ForumThreads> list, boolean isClearOld){
        if(list==null){//没有传过来的这条数据
            return;//结束方法
        }
        if(isClearOld){//是否清除老数据
            forumThreadsList.clear();//清空集合
            forumThreadsList.addAll(list);//向集合中添加数据
            Log.d("TAG","更新适配器");
        }else{
            forumThreadsList.addAll(list);//不清空数据直接添加
        }
    }
    //更新适配器
    public void upData(){
        this.notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return forumThreadsList.size();
    }

    @Override
    public Object getItem(int position) {
        return forumThreadsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView==null){
            convertView=layoutInflater.inflate(R.layout.item_forum_listview,null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        if(forumThreadsList.get(position).getForumImage().size()<=0||forumThreadsList.get(position).getForumImage()==null){
            viewHolder.image.setImageResource(R.mipmap.ic_launcher);
        }else {
            Glide.with(context).load(forumThreadsList.get(position).getForumImage().get(0).getImageUrl()).into(viewHolder.image);
        }
        viewHolder.title.setText(forumThreadsList.get(position).getForumTitle()+"");
        viewHolder.content.setText(forumThreadsList.get(position).getForumContent()+"");
        viewHolder.discuss.setText(forumThreadsList.get(position).getDiscussCount()+"");
        viewHolder.name.setText(forumThreadsList.get(position).getUserName());

        //获取当前毫秒数  1秒=1000毫秒
        long time1= System.currentTimeMillis();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long time2 = 0;
        try {
            Date d = sdf.parse(forumThreadsList.get(position).getPushTime());
            time2=d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long time;
        time=time1-time2;
        if (time/1000/60/60/24/365>0){
            viewHolder.time.setText(time/1000/60/60/24/365+"年前");
        }else if(time/1000/60/60/24>0){
            viewHolder.time.setText(time/1000/60/60/24+"天前");
        }else if(time/1000/60/60>0){
            viewHolder.time.setText(time/1000/60/60+"小时前");
        }else if(time/1000/60>0){
            viewHolder.time.setText(time/1000/60+"分钟前");
        }else {
            viewHolder.time.setText("刚刚");
        }

//        viewHolder.time.setText(forumThreadsList.get(position).getPushTime());
        return convertView;

//        ViewHolder holder=ViewHolder.get(mContext,convertView,parent,
//                layoutId,position);
//        convert(holder,getItem(position),position,convertView);
//        return holder.getConvertView();
    }


//    public abstract void convert(ViewHolder holder, Object object, int position, View convertView);
    class ViewHolder{
        private ImageView image;
        private TextView title,content,discuss,name,time;
        public ViewHolder(View view){
            this.image= (ImageView) view.findViewById(R.id.image);
            this.title= (TextView) view.findViewById(R.id.title);
            this.content= (TextView) view.findViewById(R.id.content);
            this.discuss= (TextView) view.findViewById(R.id.discuss);
            this.name= (TextView) view.findViewById(R.id.name);
            this.time= (TextView) view.findViewById(R.id.time);
        }
    }
}
