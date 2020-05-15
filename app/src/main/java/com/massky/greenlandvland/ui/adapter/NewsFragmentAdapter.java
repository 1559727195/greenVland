package com.massky.greenlandvland.ui.adapter;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import android.util.Log;

import com.massky.greenlandvland.model.entity.Sc_getForumCategory;
import com.massky.greenlandvland.ui.FragmentNews;

import java.util.List;

/**
 * Created by masskywcy on 2017-11-27.
 */

public class NewsFragmentAdapter extends FragmentPagerAdapter {
    private Context context;
    private List<Sc_getForumCategory.GetForumCategoryResult.BbsCategory> bbsCategoryList;

    public NewsFragmentAdapter(Context context, List<Sc_getForumCategory.GetForumCategoryResult.BbsCategory> bbsCategoryList, FragmentManager fm){
        super(fm);
        this.context=context;
        this.bbsCategoryList=bbsCategoryList;
        Log.e("TAG","执行");
    }

    @Override
    public Fragment getItem(int position) {
        Log.e("TAG","position="+position);
        return FragmentNews.newInstance(bbsCategoryList.get(position));
    }

    @Override
    public int getCount() {
        return bbsCategoryList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return bbsCategoryList.get(position).getCategoryName();
    }
}
