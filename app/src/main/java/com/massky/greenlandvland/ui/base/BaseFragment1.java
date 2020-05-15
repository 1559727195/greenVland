package com.massky.greenlandvland.ui.base;


import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.massky.greenlandvland.event.MyDialogEvent;

import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by zhu on 2017/7/27.
 */

public abstract class BaseFragment1 extends Fragment implements View.OnClickListener {
    public static boolean isDestroy = false;
    //控件是否已经初始化
    private boolean isCreateView = false;
    //是否已经加载过数据
    private boolean isLoadData = false;

    public static boolean isForegrounds = false;
    private Unbinder mUnbinder;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(viewId(), null);
        mUnbinder = ButterKnife.bind(this,rootView);
        isDestroy = false;
        onView(rootView);
        onEvent();
        onData();
        isCreateView = true;
        return rootView;
    }

    protected abstract void onData();


    protected abstract void onEvent();

    public abstract void onEvent(MyDialogEvent eventData);


    @Override
    public void onStart() {
        super.onStart();
    }

    protected abstract int viewId();

    protected abstract void onView(View view);

    private void initViews() {
        //初始化控件
    }

    //此方法在控件初始化前调用，所以不能在此方法中直接操作控件会出现空指针
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isCreateView) {
            lazyLoad();
        }
    }



    private void lazyLoad() {
        //如果没有加载过就加载，否则就不再加载了
        if (!isLoadData) {
            //加载数据操作
            isLoadData = true;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //第一个fragment会调用
        if (getUserVisibleHint())
            lazyLoad();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onPause() {
        isForegrounds = false;
//        getActivity().unregisterReceiver(mReceiver);
        super.onPause();
    }

    @Override
    public void onResume() {
        isForegrounds = true;
//        getActivity().registerReceiver(mReceiver, mIntentFilter);
        super.onResume();
    }


    @Override
    public void onDestroy() {
        isDestroy = true;
        if (mUnbinder != null && mUnbinder != Unbinder.EMPTY) {
            mUnbinder.unbind();
        }
        this.mUnbinder = null;
        super.onDestroy();
    }

}
