package com.massky.greenlandvland.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.massky.greenlandvland.View.swipelayout.ViewHolder;
import com.massky.greenlandvland.model.entity.Sc_myCamera;

import java.util.List;

/**
 * Created by masskywcy on 2017-10-26.
 */

public abstract class HomeMonitorAdapter extends BaseAdapter {
    protected Context context;
    protected LayoutInflater layoutInflater;
    private List<Sc_myCamera.MyCameraResult.Camera> cameraList;
//    private Bitmap bitmap;
    private int layoutId;

    public HomeMonitorAdapter(Context context, List<Sc_myCamera.MyCameraResult.Camera> cameraList, int layoutId){
        this.context=context;
        this.cameraList=cameraList;
        layoutInflater = LayoutInflater.from(context);
        this.layoutId=layoutId;
    }
    @Override
    public int getCount() {
        return cameraList==null ? 0:cameraList.size();
    }

    @Override
    public Object getItem(int position) {
        return cameraList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder viewHolder;
//        if(convertView==null){
//            convertView=layoutInflater.inflate(R.layout.item_media_listview,null);
//            viewHolder=new ViewHolder(convertView);
//            convertView.setTag(viewHolder);
//        }else{
//            viewHolder= (ViewHolder) convertView.getTag();
//        }
//        Sc_myCamera.MyCameraResult.Camera camera=cameraList.get(position);
//        viewHolder.tv_name.setText(camera.getStrName());
//        bitmap= PictureUtil.base64ToBitmap(camera.getCameraImg()+"");
//        viewHolder.iv_image.setImageBitmap(bitmap);
//        return convertView;
        ViewHolder holder= ViewHolder.get(context, convertView, parent,
                layoutId, position);
        Log.d("TAG", "getView() called with: " + "position = [" + position + "], convertView = [" + convertView + "], parent = [" + parent + "]");
        convert(holder, getItem(position), position, convertView);
        return holder.getConvertView();
    }


//    class ViewHolder{
//        private TextView tv_name;
//        private ImageView iv_image;
//        public ViewHolder(View view){
//            this.tv_name= (TextView) view.findViewById(R.id.tv_name);
//            this.iv_image= (ImageView) view.findViewById(R.id.iv_image);
//        }
//    }

    public abstract void convert(ViewHolder holder, Object object, int position, View convertView);
}
