package com.massky.greenlandvland.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.massky.greenlandvland.R;
import com.massky.greenlandvland.model.entity.Sc_getPayRecord;

import java.util.List;

/**
 * Created by liudong on 2019/1/11.
 */

public class PayAdapter extends BaseAdapter {
    protected Context context;
    protected LayoutInflater layoutInflater;
    private List<Sc_getPayRecord.GetPayRecordResult.PayRecordList> list;

    public PayAdapter(Context context,List<Sc_getPayRecord.GetPayRecordResult.PayRecordList> list){
        this.context=context;
        layoutInflater= LayoutInflater.from(context);
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            convertView=layoutInflater.inflate(R.layout.item_payment_listview,null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        Sc_getPayRecord.GetPayRecordResult.PayRecordList payRecordList=list.get(position);
        viewHolder.time.setText(payRecordList.getDt()+"");
        viewHolder.payment.setText("￥"+payRecordList.getMoney()+"");
        if(payRecordList.getType()==1){
            viewHolder.photo.setImageResource(R.drawable.fee_1);
            viewHolder.type.setText("水费");
        }else if(payRecordList.getType()==2){
            viewHolder.photo.setImageResource(R.drawable.fee_2);
            viewHolder.type.setText("电费");
        }else if(payRecordList.getType()==3){
            viewHolder.photo.setImageResource(R.drawable.fee_3);
            viewHolder.type.setText("燃气");
        }else if(payRecordList.getType()==4){
            viewHolder.photo.setImageResource(R.drawable.tingchefeiyong);
            viewHolder.type.setText("停车费");
        }else if(payRecordList.getType()==5){
            viewHolder.photo.setImageResource(R.drawable.wuyefei);
            viewHolder.type.setText("物业费");
        }
        return convertView;
    }


    class ViewHolder{
        private ImageView photo;
        private TextView type,payment,time;
        public ViewHolder(View view){
            this.photo=view.findViewById(R.id.photo);
            this.type=view.findViewById(R.id.type);
            this.payment=view.findViewById(R.id.payment);
            this.time=view.findViewById(R.id.time);
        }
    }
}
