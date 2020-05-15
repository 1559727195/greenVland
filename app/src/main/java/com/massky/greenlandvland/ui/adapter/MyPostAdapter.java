package com.massky.greenlandvland.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.massky.greenlandvland.View.swipelayout.ViewHolder;
import com.massky.greenlandvland.model.entity.Sc_myFourmList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by masskywcy on 2017-12-06.
 */

public abstract class MyPostAdapter extends BaseAdapter {
    private Context mContext;
    private final LayoutInflater mInflater;
    private List<Sc_myFourmList.MyFourmListResult.MyFourm> myFourm=new ArrayList<>();
    private int layoutId;

    public MyPostAdapter(Context context, List<Sc_myFourmList.MyFourmListResult.MyFourm> myFourm, int layoutId){
        this.mContext=context;
        mInflater= LayoutInflater.from(context);
        this.myFourm=myFourm;
        this.layoutId=layoutId;
    }
    public void appendData(List<Sc_myFourmList.MyFourmListResult.MyFourm> list, boolean isClearOld){
        if(list==null){//没有传过来的这条数据
            return;//结束方法
        }
        if(isClearOld){//是否清除老数据
            myFourm.clear();//清空集合
            myFourm.addAll(list);//向集合中添加数据
            Log.d("TAG","更新适配器");
        }else{
            myFourm.addAll(list);//不清空数据直接添加
        }
    }
    //更新适配器
    public void upData(){
        this.notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return myFourm.size();
    }

    @Override
    public Object getItem(int position) {
        return myFourm.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder viewHolder=null;
//        if(convertView==null){
//            convertView=layoutInflater.inflate(R.layout.item_mypost_listview,null);
//            viewHolder=new ViewHolder(convertView);
//            convertView.setTag(viewHolder);
//        }else {
//            viewHolder= (ViewHolder) convertView.getTag();
//        }
//        if(myFourm.get(position).getForumImage().size()<=0||myFourm.get(position)==null){
//            viewHolder.image.setImageResource(R.mipmap.ic_launcher);
//        }else {
//            Glide.with(context).load(myFourm.get(position).getForumImage().get(0).getImageUrl()).into(viewHolder.image);
//        }
//        viewHolder.title.setText(myFourm.get(position).getForumTitle()+"");
//        viewHolder.content.setText(myFourm.get(position).getForumContent()+"");
//        viewHolder.style.setText(myFourm.get(position).getForumCategory());
//        viewHolder.comment.setText(myFourm.get(position).getDiscussCount()+"");
//        String[] string=myFourm.get(position).getPushTime().split("-");
//        String[] string2=string[2].split(" ");
//        viewHolder.nianyue.setText(string[0]+"\n"+string[1]+"月");
//        viewHolder.ri.setText(string2[0]);
//        viewHolder.time.setText(string2[1]);
////        Log.e("TAG","string[0]="+string[0]+"  string[1]="+string[1]+"  string[2]"+string[2]+"  string2[0]="+string2[0]+"  string2[1]="+string2[1]);
//        return convertView;
        ViewHolder holder= ViewHolder.get(mContext,convertView,parent,
                layoutId,position);
        convert(holder,getItem(position),position,convertView);
        return holder.getConvertView();
    }
    public abstract void convert(ViewHolder holder, Object object, int position, View convertView);
//    class ViewHolder{
//        private ImageView image;
//        private TextView nianyue,ri,title,content,style,comment,time;
//        public ViewHolder(View view){
//            this.image= (ImageView) view.findViewById(R.id.image);
//            this.nianyue= (TextView) view.findViewById(R.id.nianyue);
//            this.ri= (TextView) view.findViewById(R.id.ri);
//            this.title= (TextView) view.findViewById(R.id.title);
//            this.content= (TextView) view.findViewById(R.id.content);
//            this.style= (TextView) view.findViewById(R.id.style);
//            this.comment= (TextView) view.findViewById(R.id.comment);
//            this.time= (TextView) view.findViewById(R.id.time);
//        }
//    }
}
