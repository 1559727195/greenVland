package com.massky.greenlandvland.ui.base;


import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.massky.greenlandvland.R;
import com.massky.greenlandvland.ui.FragmentMenuLeft;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by masskywcy on 2017-10-10.
 */

public class BaseActivity extends AppCompatActivity {
    public static SlidingMenu slidingMenu;//侧滑菜单
    private FragmentMenuLeft fragmentMenuLeft;//左侧菜单的Fragment


    public void initSlidingMenu() {
        //构建侧滑菜单
        slidingMenu=new SlidingMenu(this);
        //侧滑菜单支持左右滑动
        slidingMenu.setMode(SlidingMenu.LEFT);
        //支持全屏的手势滑动
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //主页面保留的尺寸，以及设置侧拉菜单的偏移量
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offsets);
        //将侧拉菜单与对应的Activity进行绑定
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);

        //给左侧拉菜单一个布局
        slidingMenu.setMenu(R.layout.layout_menu_left);
        //构建左侧Fragment实例
        fragmentMenuLeft=new FragmentMenuLeft();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout_menu_left,fragmentMenuLeft).commit();
    }
}
