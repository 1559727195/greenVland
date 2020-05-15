package com.massky.greenlandvland.View;

import android.view.View;

import androidx.viewpager.widget.ViewPager;

/**
 * Created by Administrator on 2017/9/17 0017.
 */

public class MyGallyPageTransformer implements ViewPager.PageTransformer{
    private static final float min_scale = 0.78f;
    @Override
    public void transformPage(View page, float position) {
        float scaleFactor = Math.max(min_scale, 1 - Math.abs(position));
        float rotate = 10 * Math.abs(position);
        if (position < -1) {
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);
            page.setRotationY(rotate);
        } else if (position < 0) {
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);
            page.setRotationY(rotate);
        } else if (position >= 0 && position < 1) {
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);
            page.setRotationY(-rotate);
        } else if (position >= 1 ) {
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);
            page.setRotationY(-rotate);
        }
    }
}
