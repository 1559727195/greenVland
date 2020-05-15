package com.massky.greenlandvland.ui;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.massky.greenlandvland.R;
import com.massky.greenlandvland.View.RoundImageView;
import com.massky.greenlandvland.common.PictureUtil;
import com.massky.greenlandvland.common.SharedPreferencesUtils;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Created by masskywcy on 2018-11-08.
 */

public class FragmentMenuLeft extends Fragment {
    private View view;
    private ImageView iv_home,iv_tenement,iv_forum,iv_mine,iv_openrecord;
    public static RoundImageView iv_self;
    private TextView tv_home,tv_tenement,tv_forum,tv_mine,tv_name,tv_openrecord;
    private RelativeLayout rl_home,rl_tenement,rl_forum,rl_mine,rl_openrecord;
    private Bitmap bitmap;
    private String avatar;

    //加载Fragment布局
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.menu_left,container,false);
        //获取控件
        rl_home= (RelativeLayout) view.findViewById(R.id.rl_home);
        rl_tenement= (RelativeLayout) view.findViewById(R.id.rl_tenement);
        rl_forum= (RelativeLayout) view.findViewById(R.id.rl_forum);
        rl_mine= (RelativeLayout) view.findViewById(R.id.rl_mine);
        rl_openrecord= (RelativeLayout) view.findViewById(R.id.rl_openrecord);

        iv_home= (ImageView) view.findViewById(R.id.iv_home);
        iv_tenement= (ImageView) view.findViewById(R.id.iv_tenement);
        iv_forum= (ImageView) view.findViewById(R.id.iv_forum);
        iv_mine= (ImageView) view.findViewById(R.id.iv_mine);
        tv_home= (TextView) view.findViewById(R.id.tv_home);
        tv_tenement= (TextView) view.findViewById(R.id.tv_tenement);
        tv_forum= (TextView) view.findViewById(R.id.tv_forum);
        tv_mine= (TextView) view.findViewById(R.id.tv_mine);
        iv_openrecord= (ImageView) view.findViewById(R.id.iv_openrecord);
        tv_openrecord= (TextView) view.findViewById(R.id.tv_openrecord);
        iv_self= (RoundImageView) view.findViewById(R.id.iv_self);
        tv_name= (TextView) view.findViewById(R.id.tv_name);

        //设置监听
        rl_home.setOnClickListener(clickListener);
        rl_tenement.setOnClickListener(clickListener);
        rl_forum.setOnClickListener(clickListener);
        rl_mine.setOnClickListener(clickListener);
        rl_openrecord.setOnClickListener(clickListener);
        iv_self.setOnClickListener(clickListener);
        tv_name.setOnClickListener(clickListener);

        //设置头像信息
//        Log.e("TAG","avatar="+SharedPreferencesUtils.getAvatar(getContext()));
//        bitmap=PictureUtil.base64ToBitmap(SharedPreferencesUtils.getAvatar(getContext())+"");
//        iv_self.setImageBitmap(bitmap);
//        //设置数据
//        tv_name.setText(SharedPreferencesUtils.getUserName(getContext()));
        rl_home.performClick();
        return view;
    }
    public View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_self://头像键
                case R.id.tv_name://昵称
                    iv_home.setSelected(false);
                    tv_home.setSelected(false);
                    iv_tenement.setSelected(false);
                    tv_tenement.setSelected(false);
                    iv_forum.setSelected(false);
                    tv_forum.setSelected(false);
                    iv_mine.setSelected(false);
                    tv_mine.setSelected(false);
                    iv_openrecord.setSelected(false);
                    tv_openrecord.setSelected(false);
                    ((MainActivity)getActivity()).showFragment_Detials();
                    break;
                case R.id.rl_home://首页
                    iv_home.setSelected(true);
                    tv_home.setSelected(true);

                    iv_tenement.setSelected(false);
                    tv_tenement.setSelected(false);
                    iv_forum.setSelected(false);
                    tv_forum.setSelected(false);
                    iv_mine.setSelected(false);
                    tv_mine.setSelected(false);
                    iv_openrecord.setSelected(false);
                    tv_openrecord.setSelected(false);
                    ((MainActivity)getActivity()).showFragmentMain();
                    break;
                case R.id.rl_tenement://物业
                    iv_tenement.setSelected(true);
                    tv_tenement.setSelected(true);

                    iv_home.setSelected(false);
                    tv_home.setSelected(false);
                    iv_forum.setSelected(false);
                    tv_forum.setSelected(false);
                    iv_mine.setSelected(false);
                    tv_mine.setSelected(false);
                    iv_openrecord.setSelected(false);
                    tv_openrecord.setSelected(false);
                    ((MainActivity)getActivity()).showFragment_tenement();
                    break;
                case R.id.rl_forum://论坛
                    iv_forum.setSelected(true);
                    tv_forum.setSelected(true);

                    iv_home.setSelected(false);
                    tv_home.setSelected(false);
                    iv_tenement.setSelected(false);
                    tv_tenement.setSelected(false);
                    iv_mine.setSelected(false);
                    tv_mine.setSelected(false);
                    iv_openrecord.setSelected(false);
                    tv_openrecord.setSelected(false);
                    ((MainActivity)getActivity()).showFragment_Forum();
                    break;
                case R.id.rl_mine://我的
                    iv_mine.setSelected(true);
                    tv_mine.setSelected(true);

                    iv_home.setSelected(false);
                    tv_home.setSelected(false);
                    iv_tenement.setSelected(false);
                    tv_tenement.setSelected(false);
                    iv_forum.setSelected(false);
                    tv_forum.setSelected(false);
                    iv_openrecord.setSelected(false);
                    tv_openrecord.setSelected(false);
                    ((MainActivity)getActivity()).showFragment_Mine();
                    break;
                case R.id.rl_openrecord://开门记录
                    iv_openrecord.setSelected(true);
                    tv_openrecord.setSelected(true);

                    iv_home.setSelected(false                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 );
                    tv_home.setSelected(false);
                    iv_tenement.setSelected(false);
                    tv_tenement.setSelected(false);
                    iv_forum.setSelected(false);
                    tv_forum.setSelected(false);
                    iv_mine.setSelected(false);
                    tv_mine.setSelected(false);
                    ((MainActivity)getActivity()).showFragment_OpenRecord();
                    break;
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        tv_name.setText(SharedPreferencesUtils.getUserName(getContext())+"");
        avatar=SharedPreferencesUtils.getAvatar(getContext());
        bitmap= PictureUtil.base64ToBitmap(avatar);
        if (bitmap!=null){
            iv_self.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("TAG","打开onStart");
        avatar=SharedPreferencesUtils.getAvatar(getContext());
        bitmap= PictureUtil.base64ToBitmap(avatar);
        if (bitmap!=null){
            iv_self.setImageBitmap(bitmap);
        }
    }
}
