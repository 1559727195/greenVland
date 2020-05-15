package com.massky.greenlandvland.model.httpclient.HttpUrl;


import com.massky.greenlandvland.model.httpclient.UICallback;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by zhu on 2017/8/30.
 */

public class HttpClient {
    /**
     * Post请求
     */
    public static void post(final String url, final String serviceName, final String jsonContent, final UICallback callback) {
        //构建请求
        OkHttpUtils
                .postString()
                .url(url + serviceName)
                .content(jsonContent)
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callback.onError(e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        callback.process(response);
                    }
                });
    }

    public static void get(String url, String city,String province ,final UICallback callback){
        //构建请求
        OkHttpUtils
                .get()
                .url(url)
                .addParams("city",city)
                .addParams("province",province)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callback.onError(e.toString());

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        callback.process(response);

                    }
                });
    }
}
