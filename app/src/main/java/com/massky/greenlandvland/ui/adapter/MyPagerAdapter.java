package com.massky.greenlandvland.ui.adapter;



import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * Created by masskywcy on 2017-09-18.
 */

public class MyPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragments;
    public MyPagerAdapter(FragmentManager fm , ArrayList<Fragment> fragments) {
        super(fm);
        this.fragments=fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }
}
