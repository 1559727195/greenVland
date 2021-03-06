package com.massky.greenlandvland.ui.sraum.Utils;

import android.app.ActivityManager;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Stack;


/**
 * Created by masskywcy on 2016-08-29.
 */
public class AppManager {
    // Activity栈
    private static Stack<AppCompatActivity> activityStack;
    // 单例模式
    private static AppManager instance;

    private AppManager() {

    }

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(AppCompatActivity activity) {
        if (activityStack == null) {
            activityStack = new Stack<AppCompatActivity>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public AppCompatActivity currentActivity() {
        AppCompatActivity activity = activityStack.lastElement();
        return activity;
    }

    public void finishActivity_current(Class<?> cls) {

        for (int i = 0; i < activityStack.size(); i++) {
            AppCompatActivity activity = activityStack.get(i);
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        AppCompatActivity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    public void removeActivity_but_activity(AppCompatActivity activity_new){
        for(int i = activityStack.size() -1 ; i >= 0; i--){
            AppCompatActivity activity = activityStack.get(i);
            if (activity == activity_new){
                continue;
            }
            activityStack.remove(activity);
            activity.finish();
        }
    }


    public void removeActivity_but_activity_cls(Class<?> cls){
        for(int i = activityStack.size() -1 ; i >= 0; i--){
            AppCompatActivity activity = activityStack.get(i);
            if (activity.getClass().equals(cls)){
                continue;
            }
            activityStack.remove(activity);
            activity.finish();
        }
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(AppCompatActivity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (AppCompatActivity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0; i < activityStack.size(); i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.killBackgroundProcesses(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
        }
    }
}
