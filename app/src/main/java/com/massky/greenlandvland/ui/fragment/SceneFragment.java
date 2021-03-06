package com.massky.greenlandvland.ui.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.massky.greenlandvland.R;
import com.massky.greenlandvland.common.SharedPreferencesUtils;
import com.massky.greenlandvland.event.MyDialogEvent;
import com.massky.greenlandvland.event.MyEvent;
import com.massky.greenlandvland.ui.base.BaseFragment1;
import com.massky.greenlandvland.ui.sraum.AddTogenInterface.AddTogglenInterfacer;
import com.massky.greenlandvland.ui.sraum.User;
import com.massky.greenlandvland.ui.sraum.Util.MyOkHttp;
import com.massky.greenlandvland.ui.sraum.Util.Mycallback;
import com.massky.greenlandvland.ui.sraum.Utils.ApiHelper;
import com.massky.greenlandvland.ui.sraum.Utils.AppManager;
import com.massky.greenlandvland.ui.sraum.activity.EditLinkDeviceResultActivity;
import com.massky.greenlandvland.ui.sraum.activity.SelectSensorActivity;
import com.massky.greenlandvland.ui.sraum.activity.SelectiveLinkageActivity;
import com.massky.greenlandvland.ui.sraum.adapter.DynamicFragmentViewPagerAdapter;
import com.massky.greenlandvland.util.SharedPreferencesUtil;
import com.yanzhenjie.statusview.StatusUtils;
import com.yanzhenjie.statusview.StatusView;


import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import de.greenrobot.event.EventBus;

import static com.massky.greenlandvland.ui.sraum.view.ClearLengthEditText.dip2px;


/**
 * Created by zhu on 2017/11/30.
 */

public class SceneFragment extends BaseFragment1 {
    private List<Fragment> _fragments = new ArrayList<>();
    @BindView(R.id.status_view)
    StatusView statusView;
    @BindView(R.id.add_scene)
    ImageView add_scene;
    @BindView(R.id.paixu_img)
    ImageView paixu_img;
    private LinearLayout[] _navItemLayouts;
    private HandSceneFragment handSceneFragment;
    private AutoSceneFragment autoSceneFragment;
    private View[] views;
    private PopupWindow popupWindow;

    @BindView(R.id.tab_FindFragment_title)
    TabLayout tab_FindFragment_title;
    @BindView(R.id.vp_FindFragment_pager)
    ViewPager vp_FindFragment_pager;
    private List<Fragment> list_smart_frag;
    private List<String> list_title;
    private int mCurrentPageIndex;
    private DynamicFragmentViewPagerAdapter fragmentViewPagerAdapter;
    public static String ACTION_INTENT_RECEIVER = "com.massky.sraum.sceceiver";
    private String manuallyCount;
    private String autoCount;
    private int intfirst;
    private String authType;
    private boolean isleft = true;
    private PopupWindow popupWindow_sort;

    @Override
    protected void onData() {
        //成员，业主accountType->addrelative_id
        String accountType = (String) SharedPreferencesUtil.getData(getActivity(), "accountType", "");
        switch (accountType) {
            case "1":
                add_scene.setVisibility(View.VISIBLE);
                break;//业主
            case "2":
                add_scene.setVisibility(View.GONE);
                break;//家庭成员
        }
    }

    @Override
    protected void onEvent() {
        add_scene.setOnClickListener(this);
        paixu_img.setOnClickListener(this);
    }

    @Override
    public void onEvent(MyDialogEvent eventData) {

    }

    @Override
    protected int viewId() {
        return R.layout.scene_frag;
    }

    @Override
    protected void onView(View view) {
        StatusUtils.setFullToStatusBar(getActivity());  // StatusBar.
        EventBus.getDefault().register(this);
//        initView();
        intfirst = 1;

        initControls();
    }

    @Override
    public void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                sraum_getAllScenesCount();
            }
        }).start();
        authType = (String) SharedPreferencesUtil.getData(getActivity(), "authType", "");
        switch (authType) {//（1 业主 2 成员）
            case "1":
                add_scene.setVisibility(View.VISIBLE);
                break;
            case "2":
                add_scene.setVisibility(View.GONE);
                break;
        }
        change_select();
    }

    /**
     * 选择切换
     */
    private void change_select() {
        switch (fragmentViewPagerAdapter.getCurrentPageIndex()) {
            case 0:
                if (!isleft) {//判断当前viewpager选中的是否是leftFragment,
                    vp_FindFragment_pager.setCurrentItem(1);
                }
                break;
            case 1:
                if (isleft) {//判断当前viewpager选中的是否是leftFragment,
                    vp_FindFragment_pager.setCurrentItem(0);
                }
                break;
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            init_tab_layout();
        }
    };

    /**
     * 获取第二页场景详情
     */
    private void get_scene_second_page() {
        MyEvent event = new MyEvent();
        event.setContent("scene_second");
//...设置event
        EventBus.getDefault().post(event);
    }

    /**
     * 获取场景数量
     */
    /**
     * 获取网关在线状态
     */
    private void sraum_getAllScenesCount() {
        Map map = new HashMap();
        String areaNumber = (String) SharedPreferencesUtil.getData(getActivity(), "areaNumber", "");
        map.put("areaNumber", areaNumber);
        map.put("token", SharedPreferencesUtils.getToken(getActivity()));
//        if (dialogUtil != null) {
//            dialogUtil.loadDialog();
//        }
        MyOkHttp.postMapString(ApiHelper.sraum_getAllScenesCount, map, new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {//这个是获取togglen来刷新数据
                sraum_getAllScenesCount();
            }
        }, getActivity(), null) {
            @Override
            public void onSuccess(User user) {
                super.onSuccess(user);
                manuallyCount = user.manuallyCount;
                autoCount = user.autoCount;
                handler.sendEmptyMessage(0);
            }
        });
    }

    /**
     * 初始化tablayout的数据加载
     */
    private void init_tab_layout() {
        //将名称加载tab名字列表，正常情况下，我们应该在values/arrays.xml中进行定义然后调用
        list_title = new ArrayList<>();
        manuallyCount = manuallyCount == null ? "0" : manuallyCount;
        autoCount = autoCount == null ? "0" : autoCount;
        list_title.add("手动(" + manuallyCount + ")");
        list_title.add("自动(" + autoCount + ")");
        if (tab_FindFragment_title == null) return;
        int tabCount = tab_FindFragment_title.getTabCount();
        for (int i = 0; i < tabCount; i++) {
            //这里tab可能为null 根据实际情况处理吧
            final TabLayout.Tab tab = tab_FindFragment_title.getTabAt(i);
            //这里使用到反射，拿到Tab对象后获取Class
            tab.setText(list_title.get(i));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_scene:
                showPopWindow();
                break;
            case R.id.paixu_img:
                showPopSortingWindow();
                break;
        }
    }

    public static SceneFragment newInstance() {
        SceneFragment newFragment = new SceneFragment();
        Bundle bundle = new Bundle();
        newFragment.setArguments(bundle);
        return newFragment;
    }

    /**
     * 初始化各控件
     *
     * @param
     */
    private void initControls() {

        handSceneFragment = HandSceneFragment.Companion.newInstance();
        autoSceneFragment = AutoSceneFragment.newInstance();//new PropertyFragment () 物业
        _fragments.add(handSceneFragment);
        _fragments.add(autoSceneFragment);

        //将名称加载tab名字列表，正常情况下，我们应该在values/arrays.xml中进行定义然后调用
        list_title = new ArrayList<>();
        manuallyCount = manuallyCount == null ? "0" : manuallyCount;
        autoCount = autoCount == null ? "0" : autoCount;
        list_title.add("手动(" + manuallyCount + ")");
        list_title.add("自动(" + autoCount + ")");

        //设置TabLayout的模式
        tab_FindFragment_title.setTabMode(TabLayout.MODE_FIXED);
//        tab_FindFragment_title.setTabMode(TabLayout.MODE_SCROLLABLE);
        //为TabLayout添加tab名称
        for (int i = 0; i < 2; i++) {
            tab_FindFragment_title.addTab(tab_FindFragment_title.newTab().setText(list_title.get(i)));
        }
//        tab_FindFragment_title.addTab(tab_FindFragment_title.newTab().setText(list_title.get(0)));
//        tab_FindFragment_title.addTab(tab_FindFragment_title.newTab().setText(list_title.get(1)));
//        tab_FindFragment_title.addTab(tab_FindFragment_title.newTab().setText(list_title.get(2)));
//        tab_FindFragment_title.addTab(tab_FindFragment_title.newTab().setText(list_title.get(3)));

//        fAdapter = new Find_Smart_Home_Adapter(getSupportFragmentManager(),list_smart_frag,list_title);
        fragmentViewPagerAdapter = new DynamicFragmentViewPagerAdapter(getActivity().getSupportFragmentManager(),
                vp_FindFragment_pager, _fragments, list_title);
        //viewpager加载adapter
        vp_FindFragment_pager.setAdapter(fragmentViewPagerAdapter);
        vp_FindFragment_pager.setOffscreenPageLimit(2);//设置这句话的好处就是在viewapger下可以同时刷新3个fragment
        //tab_FindFragment_title.setViewPager(vp_FindFragment_pager);
        //TabLayout加载viewpager
        tab_FindFragment_title.setupWithViewPager(vp_FindFragment_pager);
        //tab_FindFragment_title.set
        setPageChangeListener();

        tab_FindFragment_title.post(new Runnable() {
            @Override
            public void run() {
                setIndicator_new(tab_FindFragment_title);
            }
        });
    }

    /**
     * 添加tablayout指引器事件监听
     *
     * @param tab_findFragment_title
     */
    private void setIndicator_new(TabLayout tab_findFragment_title) {
        int tabCount = tab_findFragment_title.getTabCount();
        for (int i = 0; i < tabCount; i++) {
            //这里tab可能为null 根据实际情况处理吧
            final TabLayout.Tab tab = tab_findFragment_title.getTabAt(i);
            //这里使用到反射，拿到Tab对象后获取Class

            Class c = tab.getClass();
            try {
                //c.getDeclaredField 获取私有属性。

                //“mView”是Tab的私有属性名称，类型是 TabView ，TabLayout私有内部类。

                Field field = c.getDeclaredField("view");

                if (field == null) {

                    continue;

                }

                field.setAccessible(true);

                final View view = (View) field.get(tab);

                if (view == null) {

                    continue;

                }

                view.setTag(i);

                view.setOnClickListener(new View.OnClickListener() {

                    @Override

                    public void onClick(View v) {
                        //这里就可以根据业务需求处理事件了。
                        mCurrentPageIndex = (int) view.getTag();
                        vp_FindFragment_pager.setCurrentItem(mCurrentPageIndex, false);
                    }
                });

            } catch (NoSuchFieldException e) {
                e.printStackTrace();

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


    //更新ViewPager的Title信息
    private void upgrate_title() {
        if (fragmentViewPagerAdapter != null) {
            for (int i = 0; i < 2; i++)
                fragmentViewPagerAdapter.setPageTitle(i, "");
        }
    }


    private int index = 0;

    private void setPageChangeListener() {
        vp_FindFragment_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                mCurrentViewPagerName = mChannelNames.get(position);
//                mCurrentPageIndex = position;
                switch (position) {
                    case 1:
                        isleft = false;
                        break;
                    case 0:
                        isleft = true;
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 通过反射调整TabLayout指引器的宽度
     *
     * @param tabs
     * @param leftDip
     * @param rightDip
     */
    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            final View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
            child.setTag(i);
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (Integer) child.getTag();
//                    vp_FindFragment_pager.setCurrentItem(position,false);
                    Log.e("robin debug", "position:" + position);//监听viewpager+Tablayout -》item点击事件
                }
            });
        }
    }


    /**
     * Android popupwindow在指定控件正下方滑动弹出效果
     */
    private void showPopWindow() {
        try {
            View view = LayoutInflater.from(getActivity()).inflate(
                    R.layout.add_scene_pop_lay, null);
            //add_hand_scene_txt, add_auto_scene_txt
            TextView add_auto_scene_txt = (TextView) view.findViewById(R.id.add_auto_scene_txt);
            TextView add_hand_scene_txt = (TextView) view.findViewById(R.id.add_hand_scene_txt);


            popupWindow = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            ColorDrawable cd = new ColorDrawable(0x00ffffff);// 背景颜色全透明
            popupWindow.setBackgroundDrawable(cd);
            int[] location = new int[2];
            add_scene.getLocationOnScreen(location);//获得textview的location位置信息，绝对位置
            popupWindow.setAnimationStyle(R.style.style_pop_animation);// 动画效果必须放在showAsDropDown()方法上边，否则无效
            backgroundAlpha(0.5f);// 设置背景半透明 ,0.0f->1.0f为不透明到透明变化。
            int xoff = dip2px(getActivity(), 20);
            popupWindow.showAsDropDown(add_scene, 0, 0);
//            popupWindow.showAtLocation(tv_pop, Gravity.NO_GRAVITY, location[0]+tv_pop.getWidth(),location[1]);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    popupWindow = null;// 当点击屏幕时，使popupWindow消失
                    backgroundAlpha(1.0f);// 当点击屏幕时，使半透明效果取消，1.0f为透明
                }
            });

            add_hand_scene_txt.setOnClickListener(new View.OnClickListener() {//while(bool) {postdelay { }}也可以起到定时器的作用
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
//                    startActivity(new Intent(getActivity(), AddHandSceneActivity.class));
                    Intent intent = null;
                    Map map_link = new HashMap();
                    map_link.put("type", "101");
                    map_link.put("deviceType", "");
                    map_link.put("deviceId", "");
                    map_link.put("name", "手动执行");
                    map_link.put("action", "执行");
                    map_link.put("condition", "");
                    map_link.put("minValue", "");
                    map_link.put("maxValue", "");
                    map_link.put("boxName", "");
                    map_link.put("name1", "手动执行");
                    boolean add_condition = (boolean) SharedPreferencesUtil.getData(getActivity(), "add_condition", false);
                    if (add_condition) {
//            AppManager.getAppManager().removeActivity_but_activity_cls(MainfragmentActivity.class);
                        AppManager.getAppManager().finishActivity_current(EditLinkDeviceResultActivity.class);
                        intent = new Intent(getActivity(), EditLinkDeviceResultActivity.class);
                        intent.putExtra("sensor_map", (Serializable) map_link);
                        startActivity(intent);
                    } else {
                        intent = new Intent(getActivity(),
                                SelectiveLinkageActivity.class);
                        intent.putExtra("link_map", (Serializable) map_link);
                        startActivity(intent);
                    }

                    isleft = true;

                }
            });

            add_auto_scene_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//添加自动场景
                    popupWindow.dismiss();
//                    startActivity(new Intent(getActivity(), AddAutoSceneActivity.class));
                    Intent intent = new Intent(getActivity(), SelectSensorActivity.class);
                    intent.putExtra("type", "100||102");//自动场景
                    startActivity(intent);
                    isleft = false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Android popupwindow在指定控件正下方滑动弹出效果
     */
    private void showPopSortingWindow() {
        try {
            View view = LayoutInflater.from(getActivity()).inflate(
                    R.layout.add_sort_pop_lay, null);
            //add_hand_scene_txt, add_auto_scene_txt
            TextView add_auto_scene_txt = (TextView) view.findViewById(R.id.add_auto_scene_txt);
            TextView add_hand_scene_txt = (TextView) view.findViewById(R.id.add_hand_scene_txt);


            popupWindow_sort = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            popupWindow_sort.setFocusable(true);
            popupWindow_sort.setOutsideTouchable(true);
            ColorDrawable cd = new ColorDrawable(0x00ffffff);// 背景颜色全透明
            popupWindow_sort.setBackgroundDrawable(cd);
            int[] location = new int[2];
            paixu_img.getLocationOnScreen(location);//获得textview的location位置信息，绝对位置
            popupWindow_sort.setAnimationStyle(R.style.style_pop_animation);// 动画效果必须放在showAsDropDown()方法上边，否则无效
            backgroundAlpha(0.5f);// 设置背景半透明 ,0.0f->1.0f为不透明到透明变化。
            int xoff = dip2px(getActivity(), 15);
            popupWindow_sort.showAsDropDown(paixu_img, -location[0] / 3 - xoff, 0);
//            popupWindow.showAtLocation(tv_pop, Gravity.NO_GRAVITY, location[0]+tv_pop.getWidth(),location[1]);
            popupWindow_sort.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    popupWindow_sort = null;// 当点击屏幕时，使popupWindow消失
                    backgroundAlpha(1.0f);// 当点击屏幕时，使半透明效果取消，1.0f为透明
                }
            });

            add_hand_scene_txt.setOnClickListener(new View.OnClickListener() {//while(bool) {postdelay { }}也可以起到定时器的作用
                @Override
                public void onClick(View v) {//正序
                    popupWindow_sort.dismiss();
                    SharedPreferencesUtil.saveData(getActivity(), "order", "1");
                    get_scene_second_page();
                }
            });

            add_auto_scene_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//倒序
                    popupWindow_sort.dismiss();
                    SharedPreferencesUtil.saveData(getActivity(), "order", "2");
                    get_scene_second_page();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 设置popupWindow背景半透明
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha;// 0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (intfirst == 1) {
                intfirst = 2;
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sraum_getAllScenesCount();
                        get_scene_second_page();
                    }
                }).start();
                authType = (String) SharedPreferencesUtil.getData(getActivity(), "authType", "");
                switch (authType) {//（1 业主 2 成员）
                    case "1":
                        add_scene.setVisibility(View.VISIBLE);
                        break;
                    case "2":
                        add_scene.setVisibility(View.GONE);
                        break;
                }
            }
        }
    }

    @Subscribe
    public void onEvent(MyEvent event) {
        String status = event.getContent();
        switch (status) {
            case "刷新":
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sraum_getAllScenesCount();
                    }
                }).start();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}



