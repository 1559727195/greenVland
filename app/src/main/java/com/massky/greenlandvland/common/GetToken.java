package com.massky.greenlandvland.common;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.massky.greenlandvland.model.entity.Sc_getTokenNew;
import com.massky.greenlandvland.model.httpclient.HttpUrl.HttpClient;
import com.massky.greenlandvland.model.httpclient.UICallback;

/**
 * Created by masskywcy on 2018-02-11.
 */

public class GetToken {
    //持有接口变量
    public static CallBackInterface callBackInterface;
    public Context context;
    public GetToken(CallBackInterface callBackInterface, Context context){
        this.callBackInterface=callBackInterface;
        this.context=context;
        gettoken(context);
    }

    public static String gettoken(final Context context){
        final String loginAccount= SharedPreferencesUtils.getLoginAccount(context);
        String password= SharedPreferencesUtils.getPassWord(context);
        final String timeStamp=CommonUtil.getDate();
        final String signature = MD5Utils.getMD5Str(loginAccount + password + timeStamp);
        HttpClient.post(CommonUtil.APPURL, "sc_getTokenNew",
                new Gson().toJson(new Sc_getTokenNew.GetTokenNewParams(loginAccount, timeStamp, signature,3))
                , new UICallback() {
                    @Override
                    public void process(String data) {
                        Log.e("TAG","data="+data);
                        Log.e("TAG","loginAccount="+loginAccount);
                        Log.e("TAG","timeStamp="+timeStamp);
                        Log.e("TAG","signature="+signature);
                        Sc_getTokenNew.GetTokenNewResult tokenResult=new Gson().fromJson(data, Sc_getTokenNew.GetTokenNewResult.class);
                        int result=tokenResult.getResult();
                        if (result == 1) {
                            Log.e("TAG", "1-json解析错误");
                            SharedPreferencesUtils.saveToken(context, tokenResult.getToken());
                        } else if (result == 100) {
                            Log.e("TAG", "成功");
                            SharedPreferencesUtils.saveToken(context, tokenResult.getToken());
                            callBackInterface.gettoken(tokenResult.getToken());
                            Log.e("TAG","token3="+ SharedPreferencesUtils.getToken(context));
                        } else if (result == 101) {
                            Log.e("TAG", "101-失败,userId或loginPwd错误");
                            SharedPreferencesUtils.saveToken(context, tokenResult.getToken());
                        }else if(result==102){
                            Log.e("TAG","102-不知道什么失败");
                            SharedPreferencesUtils.saveToken(context, tokenResult.getToken());
                        }else if(result==103){
                            Log.e("TAG","103-时间戳错误");
                            SharedPreferencesUtils.saveToken(context, tokenResult.getToken());
                        }else if(result==104){
                            Log.e("TAG","104-appCode不正确");
                            SharedPreferencesUtils.saveToken(context, tokenResult.getToken());
                        }else {
                            ToastUtil.showToast(context,"网络操作失败");
                            SharedPreferencesUtils.saveToken(context, tokenResult.getToken());
                        }
                    }

                    @Override
                    public void onError(String data) {
                        ToastUtil.showToast(context,"网络操作失败");
                    }
                });
        return SharedPreferencesUtils.getToken(context);
    }
}
