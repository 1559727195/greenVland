package com.massky.greenlandvland.ui;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.massky.greenlandvland.R;
import com.massky.greenlandvland.View.MyGallyPageTransformer;
import com.massky.greenlandvland.ui.adapter.MyPagerAdapter;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by masskywcy on 2018-11-09.
 */

public class Fragment_Tenement extends Fragment {
    private View rootView;
    private ViewPager viewpager;//滑动页面
    private ImageView sliding;
    private ArrayList<Fragment> fragments;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //防止多次切换时调用
        if(rootView==null){
            rootView=inflater.inflate(R.layout.fragment_tenement,container,false);
            //获取控件
            sliding= (ImageView) rootView.findViewById(R.id.sliding);
            sliding.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.slidingMenu.showMenu();
                }
            });
            viewpager= (ViewPager) rootView.findViewById(R.id.viewpager);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initDatas();//获取数据
                        }
                    });
                }
            }).start();


        }else {
            /**
             * 缓存的rootView需要判断是否已经被加过parent，
             * 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
             */
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        return rootView;
    }

    //获取数据
    private void initDatas() {
        fragments=new ArrayList<>();
        fragments.add(new Fragment_contentcomplain());
        fragments.add(new Fragment_contentinform());
        fragments.add(new Fragment_contentpayment());


        viewpager.setOffscreenPageLimit(fragments.size());//卡片数量


        int pagerWidth = (int) (getResources().getDisplayMetrics().widthPixels * 3.0f / 5.0f);
        ViewGroup.LayoutParams lp = viewpager.getLayoutParams();
        if (lp == null) {
            lp = new ViewGroup.LayoutParams(pagerWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        } else {
            lp.width = pagerWidth;
        }
        viewpager.setLayoutParams(lp);
        //setPageMargin表示设置图片之间的间距
        viewpager.setPageMargin(22);//两个卡片之间的距离，单位dp
        viewpager.setPageTransformer(true,new MyGallyPageTransformer());



        FragmentManager fm=(getActivity()).getSupportFragmentManager();//获取FragmentManager管理器
        MyPagerAdapter adapter=new MyPagerAdapter(fm,fragments);
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(0);

//        //由于ViewPager 没有点击事件，可以通过对VIewPager的setOnTouchListener进行监听已达到实现点击事件的效果
//        viewpager.setOnTouchListener(new View.OnTouchListener() {
//            int flage = 0 ;
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()){
//                    case MotionEvent.ACTION_DOWN:
//                        flage = 0 ;
//                        break ;
//                    case MotionEvent.ACTION_MOVE:
//                        flage = 1 ;
//                        break ;
//                    case  MotionEvent.ACTION_UP :
//                        if (flage == 0) {
//                            int item = viewpager.getCurrentItem();
//                            if (item == 0) {
//                                ((MainActivity)getActivity()).showFragment_Complain();
//                            } else if (item == 1) {
//                                ((MainActivity)getActivity()).showFragment_Counselor();
//                            } else if (item == 2) {
//                                ((MainActivity)getActivity()).showFragment_Inform();
//                            }else if (item == 3) {
//                                ((MainActivity)getActivity()).showFragment_Maintain();
//                            }
//                        }
//                        break ;
//
//
//                }
//                return false;
//            }
//        });


        //给滑动监听器设置监听
        viewpager.addOnPageChangeListener(onChangeListener);
    }

    ViewPager.OnPageChangeListener onChangeListener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {//页面滑动结束
            switch (position) {
                case 0:
                    MainActivity.slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                    break;
                case 1:
                    MainActivity.slidingMenu.setTouchModeAbove(SlidingMenu.LEFT);
                    break;
                case 2:
                    MainActivity.slidingMenu.setTouchModeAbove(SlidingMenu.LEFT);
                    break;
                case 3:
                    MainActivity.slidingMenu.setTouchModeAbove(SlidingMenu.LEFT);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

}
