package com.massky.greenlandvland.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.massky.greenlandvland.R;
import com.massky.greenlandvland.common.CallBackInterface;
import com.massky.greenlandvland.common.CommonUtil;
import com.massky.greenlandvland.common.DialogThridUtils;
import com.massky.greenlandvland.common.GetToken;
import com.massky.greenlandvland.common.SharedPreferencesUtils;
import com.massky.greenlandvland.common.ToastUtil;
import com.massky.greenlandvland.model.entity.Sc_getForumCategory;
import com.massky.greenlandvland.model.httpclient.HttpUrl.HttpClient;
import com.massky.greenlandvland.model.httpclient.UICallback;
import com.massky.greenlandvland.ui.adapter.NewsFragmentAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by masskywcy on 2018-11-13.
 */

public class Fragment_Forum extends Fragment {
    private View view;
    private ImageView post;
    private ImageView sliding;
    private TabLayout newstype;
    private ViewPager viewPager;
    private ImageView iv_mynote;
    private NewsFragmentAdapter adapter;
    private String token;
    private String projectCode;
    private PopupWindow popupWindow;
    private Intent intent;
    private int id = 1;
    private Dialog mDialog;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    DialogThridUtils.closeDialog(mDialog);
                    break;
            }
        }
    };
    List<Sc_getForumCategory.GetForumCategoryResult.BbsCategory> bbsCategoryList = new ArrayList<>();

    private String type;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            bbsCategoryList = (List<Sc_getForumCategory.GetForumCategoryResult.BbsCategory>) msg.obj;
            Log.e("TAG","bbsCategoryList=="+bbsCategoryList);
            adapter = new NewsFragmentAdapter(getActivity(), bbsCategoryList, getChildFragmentManager());
            adapter.notifyDataSetChanged();
            viewPager.setAdapter(adapter);
            newstype.setupWithViewPager(viewPager);//把tablelayout和fragment绑定
            newstype.setTabMode(TabLayout.MODE_FIXED);//多个Tab无滑动效果
            viewPager.setCurrentItem(0);
            SharedPreferencesUtils.saveType(getContext(),"交流");

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    if (position == 0) {
                        MainActivity.slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                        SharedPreferencesUtils.saveType(getContext(),"交流");
                    } else {
                        MainActivity.slidingMenu.setTouchModeAbove(SlidingMenu.LEFT);
                        if(position==1){
                            SharedPreferencesUtils.saveType(getContext(),"二手");
                        }else if(position==2){
                            SharedPreferencesUtils.saveType(getContext(),"育儿");
                        }else if(position==3){
                            SharedPreferencesUtils.saveType(getContext(),"活动");
                        }else if(position==4){
                            SharedPreferencesUtils.saveType(getContext(),"养老");
                        }
                    }


                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_forum, container, false);
        token = SharedPreferencesUtils.getToken(getContext());
        projectCode = SharedPreferencesUtils.getProjectCode(getContext());

        //获取控件
        sliding = (ImageView) view.findViewById(R.id.sliding);
        newstype = (TabLayout) view.findViewById(R.id.newstype);
        iv_mynote = (ImageView) view.findViewById(R.id.iv_mynote);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        post = (ImageView) view.findViewById(R.id.post);
        //添加监听
        sliding.setOnClickListener(clickListener);

        post.setOnClickListener(clickListener);
        iv_mynote.setOnClickListener(clickListener);

        initforumType();
        return view;
    }


    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.sliding:
                    MainActivity.slidingMenu.showMenu();
                    break;
                case R.id.iv_mynote:
                    startActivity(new Intent(getActivity(),MyPostActivity.class));
                    break;
                case R.id.post:
                    View views = View.inflate(getActivity(), R.layout.fragment_post, null);
//                    if (popupWindow == null) {
//                        popupWindow = new PopupWindow();
//                    }
                    // 创建PopupWindow对象，其中：
                    // 第一个参数是用于PopupWindow中的View，第二个参数是PopupWindow的宽度，
                    // 第三个参数是PopupWindow的高度，第四个参数指定PopupWindow能否获得焦点
                    popupWindow = new PopupWindow(views, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
                    // 设置PopupWindow的背景
                    popupWindow.setBackgroundDrawable(new BitmapDrawable());
                    // 设置PopupWindow是否能响应外部点击事件
                    popupWindow.setOutsideTouchable(true);
                    // 设置PopupWindow是否能响应点击事件
                    popupWindow.setTouchable(true);

                    TextView exchange,secondhand,childrearing,activity,yanglao;
                    exchange= (TextView) views.findViewById(R.id.exchange);
                    secondhand= (TextView) views.findViewById(R.id.secondhand);
                    childrearing= (TextView) views.findViewById(R.id.childrearing);
                    activity= (TextView) views.findViewById(R.id.activity);
                    yanglao= (TextView) views.findViewById(R.id.yanglao);
                    exchange.setOnClickListener(clickListener);
                    secondhand.setOnClickListener(clickListener);
                    childrearing.setOnClickListener(clickListener);
                    activity.setOnClickListener(clickListener);
                    yanglao.setOnClickListener(clickListener);

                    // 显示PopupWindow，其中：
                    // 第一个参数是PopupWindow的锚点，第二和第三个参数分别是PopupWindow相对锚点的x、y偏移
//                    window.showAsDropDown(anchor, xoff, yoff);
                    // 或者也可以调用此方法显示PopupWindow，其中：
                    // 第一个参数是PopupWindow的父View，第二个参数是PopupWindow相对父View的位置，
                    // 第三和第四个参数分别是PopupWindow相对父View的x、y偏移
                    popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                    break;

                case R.id.exchange:
                    intent=new Intent(getActivity(),PostActivity.class);
                    intent.putExtra("postname","交流");
                    startActivity(intent);
                    if(popupWindow!=null&&popupWindow.isShowing()){
                        popupWindow.dismiss();
                    }
                    break;
                case R.id.secondhand:
                    intent=new Intent(getActivity(),PostActivity.class);
                    intent.putExtra("postname","二手");
                    startActivity(intent);
                    if(popupWindow!=null&&popupWindow.isShowing()){
                        popupWindow.dismiss();
                    }
                    break;
                case R.id.childrearing:
                    intent=new Intent(getActivity(),PostActivity.class);
                    intent.putExtra("postname","育儿");
                    startActivity(intent);
                    if(popupWindow!=null&&popupWindow.isShowing()){
                        popupWindow.dismiss();
                    }
                    break;
                case R.id.activity:
                    intent=new Intent(getActivity(),PostActivity.class);
                    intent.putExtra("postname","活动");
                    startActivity(intent);
                    if(popupWindow!=null&&popupWindow.isShowing()){
                        popupWindow.dismiss();
                    }
                    break;
                case R.id.yanglao:
                    intent=new Intent(getActivity(),PostActivity.class);
                    intent.putExtra("postname","养老");
                    startActivity(intent);
                    if(popupWindow!=null&&popupWindow.isShowing()){
                        popupWindow.dismiss();
                    }
                    break;
            }
        }
    };

    private void initforumType() {
        mDialog = DialogThridUtils.showWaitDialog(getContext(), true);
//        final String token = SharedPreferencesUtils.getToken(getContext());
//        final String projectCode = SharedPreferencesUtils.getProjectCode(getContext());
        HttpClient.post(CommonUtil.APPURL, "sc_getForumCategory"
                , new Gson().toJson(new Sc_getForumCategory.GetForumCategoryParams(token, projectCode))
                , new UICallback() {
                    @Override
                    public void process(String data) {
                        Log.e("TAG","data="+data);
//                        Log.e("TAG","token="+token);
//                        Log.e("TAG","projectCode="+projectCode);
                        Sc_getForumCategory.GetForumCategoryResult getForumCategoryResult = new Gson().fromJson(data, Sc_getForumCategory.GetForumCategoryResult.class);
                        int result = getForumCategoryResult.getResult();
                        if (result == 1) {
                            Log.e("TAG", "1-json解析错误");
                            mHandler.sendEmptyMessage(1);
                            id = 1;
                        } else if (result == 100) {
                            Log.e("TAG", "100-成功");
                            id = 1;
                            Message msg = handler.obtainMessage();
                            msg.obj = getForumCategoryResult.getBbsCategoryList();
                            handler.sendMessage(msg);
                            mHandler.sendEmptyMessage(1);
                        } else if (result == 101) {
                            Log.e("TAG", "101-token错误");
                            mHandler.sendEmptyMessage(1);
                            if (id == 1) {
                                id = id + 1;
                                new GetToken(new CallBackInterface() {
                                    @Override
                                    public void gettoken(String str) {
                                        token = str;
                                        Log.e("TAG", "token=" + token);
                                        if (!TextUtils.isEmpty(token)) {
                                            initforumType();
                                        } else {
                                            showerror();
                                        }
                                    }
                                }, getContext());
                            } else {
                                id = 1;
                                showerror();
                            }
                        } else if (result == 102) {
                            Log.e("TAG", "102-projectCode错误");
                            mHandler.sendEmptyMessage(1);
                            id = 1;
                            ToastUtil.showToast(getContext(), "操作失败");
                        } else {
                            id = 1;
                            mHandler.sendEmptyMessage(1);
                            ToastUtil.showToast(getContext(), "网络连接失败");
                        }
                    }

                    @Override
                    public void onError(String data) {
                        mHandler.sendEmptyMessage(1);
                        id = 1;
                        ToastUtil.showToast(getContext(), "网络连接失败");
                    }
                });
    }

    private void showerror() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), 1);
        builder.setTitle("提示");
        builder.setMessage("服务器错误");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void onResume() {
        super.onResume();
        type=SharedPreferencesUtils.getType(getContext());
        Log.e("TAG","type="+type);
        if(type==null){
            viewPager.setCurrentItem(0);
        }else {
            if(type.equals("交流")){
                viewPager.setCurrentItem(0);
            }else if(type.equals("二手")){
                viewPager.setCurrentItem(1);
            }else if(type.equals("育儿")){
                viewPager.setCurrentItem(2);
            }else if(type.equals("活动")){
                viewPager.setCurrentItem(3);
            }else if(type.equals("养老")){
                viewPager.setCurrentItem(4);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferencesUtils.cleanType(getContext());
    }
}
