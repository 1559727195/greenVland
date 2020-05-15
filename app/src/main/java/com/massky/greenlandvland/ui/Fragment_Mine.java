package com.massky.greenlandvland.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.massky.greenlandvland.R;
import com.massky.greenlandvland.View.RoundImageView;
import com.massky.greenlandvland.common.CallBackInterface;
import com.massky.greenlandvland.common.CommonUtil;
import com.massky.greenlandvland.common.DialogThridUtils;
import com.massky.greenlandvland.common.GetToken;
import com.massky.greenlandvland.common.PictureUtil;
import com.massky.greenlandvland.common.SharedPreferencesUtils;
import com.massky.greenlandvland.common.ToastUtil;
import com.massky.greenlandvland.model.entity.Sc_getFamily;
import com.massky.greenlandvland.model.httpclient.HttpUrl.HttpClient;
import com.massky.greenlandvland.model.httpclient.UICallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by masskywcy on 2018-11-09.
 */

public class Fragment_Mine extends Fragment {
    private View view;
    private ImageView iv_set;//设置按钮
    private RoundImageView iv_head;//头像
    private TextView tv_name;//昵称
    private LinearLayout ll_familymember,ll_about,ll_myvisiter,ll_mypost;
    private ImageView sliding;
    private TextView tv_family;
    private Bitmap bitmap;
    private String avatar;
    private List<Sc_getFamily.GetFamilyResult.Family> familyLists = new ArrayList<>();
    private int familynumber;

    private int accountType;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_mine,container,false);
        init();//初始化控件
        accountType=SharedPreferencesUtils.getAccountType(getContext());
        Log.e("TAG","accountType="+accountType);
        return view;
    }

    //初始化控件
    private void init() {
        //获取控件
        iv_set= (ImageView) view.findViewById(R.id.iv_set);
        iv_head= (RoundImageView) view.findViewById(R.id.iv_head);
        tv_name= (TextView) view.findViewById(R.id.tv_name);
        ll_familymember= (LinearLayout) view.findViewById(R.id.ll_familymember);
        ll_about= (LinearLayout) view.findViewById(R.id.ll_about);
        ll_myvisiter= (LinearLayout) view.findViewById(R.id.ll_myvisiter);
        ll_mypost= (LinearLayout) view.findViewById(R.id.ll_mypost);
        sliding= (ImageView) view.findViewById(R.id.sliding);
        tv_family= (TextView) view.findViewById(R.id.tv_family);
        //添加监听
        iv_set.setOnClickListener(clickListener);
        iv_head.setOnClickListener(clickListener);
        tv_name.setOnClickListener(clickListener);
        ll_familymember.setOnClickListener(clickListener);
        ll_about.setOnClickListener(clickListener);
        ll_myvisiter.setOnClickListener(clickListener);
        ll_mypost.setOnClickListener(clickListener);
        sliding.setOnClickListener(clickListener);

    }



    View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id .iv_set://设置按钮
                    startActivity(new Intent(getContext(),SetActivity.class));
                    break;
                case R.id.iv_head://头像
                case R.id.tv_name://昵称
                    ((MainActivity)getActivity()).showFragment_Detials();
                    break;
                case R.id.ll_familymember://家庭成员
                    startActivity(new Intent(getContext(),FamilyMemberActivity.class));
                    break;
                case R.id.ll_about://关于
                    startActivity(new Intent(getContext(),AboutActivity.class));
                    break;
                case R.id.sliding://侧滑菜单
                    MainActivity.slidingMenu.showMenu();
                    break;
                case R.id.ll_myvisiter://我的访客页面
                    startActivity(new Intent(getContext(),MyVisiterActivity.class));
                    break;
                case R.id.ll_mypost://我的帖子
                    startActivity(new Intent(getContext(),MyPostActivity.class));
                    break;
            }
        }
    };


    @Override
    public void onResume() {
        super.onResume();
        tv_name.setText(SharedPreferencesUtils.getUserName(getContext()));
        avatar=SharedPreferencesUtils.getAvatar(getContext());
        bitmap= PictureUtil.base64ToBitmap(avatar+"");
        if(bitmap!=null){
            iv_head.setImageBitmap(bitmap);
        }

        if(accountType==2){
            ll_familymember.setVisibility(View.VISIBLE);
            familynumber=SharedPreferencesUtils.getFamily(getContext());
            Log.e("TAG","familynumber="+familynumber);
            tv_family.setText(familynumber+"");
        }else {
            ll_familymember.setVisibility(View.GONE);
        }
    }
}
