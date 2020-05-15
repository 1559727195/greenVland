package com.massky.greenlandvland.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.massky.greenlandvland.View.swipelayout.ViewHolder;
import com.massky.greenlandvland.model.entity.Sc_myFourmDiscuss;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by masskywcy on 2017-12-07.
 */

public abstract class ResponseAdapter extends BaseAdapter {
    private Context mContext;
    private int layoutId;
    private final LayoutInflater mInflater;
    private List<Sc_myFourmDiscuss.MyFourmDiscussResult.Huitie> huitieList=new ArrayList<>();

    public ResponseAdapter(Context context, List<Sc_myFourmDiscuss.MyFourmDiscussResult.Huitie> huitieList, int layoutId){
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.huitieList=huitieList;
        this.layoutId=layoutId;
    }
    public void appendData(List<Sc_myFourmDiscuss.MyFourmDiscussResult.Huitie> list, boolean isClearOld){
        if(list==null){//没有传过来的这条数据
            return;//结束方法
        }
        if(isClearOld){//是否清除老数据
            huitieList.clear();//清空集合
            huitieList.addAll(list);//向集合中添加数据
            Log.d("TAG","更新适配器");
        }else{
            huitieList.addAll(list);//不清空数据直接添加
        }
    }
    //更新适配器
    public void upData(){
        this.notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return huitieList.size();
    }

    @Override
    public Object getItem(int position) {
        return huitieList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder viewHolder=null;
//        if(convertView==null){
//            convertView=layoutInflater.inflate(R.layout.item_response_listview,null);
//            viewHolder=new ViewHolder(convertView);
//            convertView.setTag(viewHolder);
//        }else {
//            viewHolder= (ViewHolder) convertView.getTag();
//        }
//        viewHolder.title.setText(huitieList.get(position).getForumTitle()+"");
//        viewHolder.content.setText(huitieList.get(position).getForumContent()+"");
//        viewHolder.style.setText(huitieList.get(position).getForumCategory()+"");
//        viewHolder.comment.setText(huitieList.get(position).getDiscussCount()+"");
//        String[] string=huitieList.get(position).getPushTime().split("-");
//        String[] string2=string[2].split(" ");
//        viewHolder.nianyue.setText(string[0]+"\n"+string[1]+"月");
//        viewHolder.ri.setText(string2[0]);
//        return convertView;
        ViewHolder holder= ViewHolder.get(mContext,convertView,parent,
                layoutId,position);
        convert(holder,getItem(position),position,convertView);
        return holder.getConvertView();
    }

    public abstract void convert(ViewHolder holder, Object object, int position, View convertView);
//    class ViewHolder{
//        private TextView nianyue,ri,title,content,style,comment;
//        public ViewHolder(View view){
//            this.nianyue= (TextView) view.findViewById(R.id.nianyue);
//            this.ri= (TextView) view.findViewById(R.id.ri);
//            this.title= (TextView) view.findViewById(R.id.title);
//            this.content= (TextView) view.findViewById(R.id.content);
//            this.style= (TextView) view.findViewById(R.id.style);
//            this.comment= (TextView) view.findViewById(R.id.comment);
//        }
//    }
}
