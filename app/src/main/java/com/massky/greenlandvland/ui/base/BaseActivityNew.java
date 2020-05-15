package com.massky.greenlandvland.ui.base;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by zhu on 2017/7/18.
 */


public abstract class BaseActivityNew extends AppCompatActivity {
   public static boolean isForegrounds = false;

    private Unbinder mUnbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(viewId());
        //ButterKnife.inject(this);
       mUnbinder =  ButterKnife.bind(this);


        onView();
        onEvent();
        onData();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    /**
     * 手势密码两种状态（点击home键和手机屏幕状态进行判定）
     */
    @Override
    protected void onRestart() {
        super.onRestart();
    }



    @Override
    protected void onPause() {
        isForegrounds = false;
        super.onPause();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            /**
             * 点击空白位置 隐藏软键盘
             */
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    protected abstract int viewId();

    @Override
    protected void onResume() {
        isForegrounds = true;
        super.onResume();
    }

    protected abstract void onView();
    protected abstract void onEvent();
    protected abstract void onData();
    /**
     * 取消广播状态
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null && mUnbinder != Unbinder.EMPTY) {
            mUnbinder.unbind();
        }
        this.mUnbinder = null;
    }
}
