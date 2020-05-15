package com.massky.greenlandvland.ui.adapter;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by masskywcy on 2017-12-06.
 */

public class PostAdapter extends FragmentPagerAdapter {
    private Context context;
    private String[] tabTitles=new String[]{"主贴","回帖"};
    private List<Fragment> fragmentList=new ArrayList<>();
    public PostAdapter(Context context, FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.context=context;
        this.fragmentList=fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
