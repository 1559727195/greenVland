package com.massky.greenlandvland.ui.adapter;

import androidx.viewpager.widget.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by masskywcy on 2017-08-22.
 */

public class LeadImageAdapter extends PagerAdapter {
    private List<View> list;//存储5个原点指示器
    public LeadImageAdapter(List<View> list){
        this.list=list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    /**
     * @param container ViewPager容器
     * @param position  ImageView显示的位置
     * @param object    构建的ImageView
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {//销毁不关联的页面
        //从ViewPager容器中销毁页面
        container.removeView(list.get(position));
    }

    //将ImageView视图添加到ViewPager容器中
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = list.get(position);
        container.addView(view);
        return view;
    }
}
