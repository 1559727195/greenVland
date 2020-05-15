package com.massky.greenlandvland;

import android.util.Log;

import com.mob.MobApplication;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import okhttp3.OkHttpClient;

/**
 * Created by masskywcy on 2018-11-08.
 */

public class Application extends MobApplication{
    private static final String TAG = "JPush";
    private static Application _instance;

    @Override
    public void onCreate() {
        super.onCreate();
        _instance = this;


        Log.d(TAG, "[ExampleApplication] onCreate");

        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush

        //okhttp网络配置
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(5000, TimeUnit.MILLISECONDS)
                .readTimeout(5000, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }

    public static Application getInstance(){
        return _instance;
    }
}
