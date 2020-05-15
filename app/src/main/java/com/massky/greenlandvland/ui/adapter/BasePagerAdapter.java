package com.massky.greenlandvland.ui.adapter;

import android.content.Context;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import androidx.viewpager.widget.PagerAdapter;

/**
 * Created by masskywcy on 2017-12-07.
 */

public class BasePagerAdapter extends PagerAdapter {
    private Context context;//上下文操作资源
    private ArrayList<View> listitem=new ArrayList<View>();

    public BasePagerAdapter(Context context){
        super();
        this.context=context;
    }
    @Override
    public int getCount() {
        return listitem.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view=listitem.get(position);
        container.removeView(view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view=listitem.get(position);
        container.addView(view);
        return view;
    }
    public void addViwToAdapter(ImageView imageView) {
        // TODO Auto-generated method stub
        listitem.add(imageView);
    }
}
