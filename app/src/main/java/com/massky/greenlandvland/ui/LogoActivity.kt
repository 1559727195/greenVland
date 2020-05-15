package com.massky.greenlandvland.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log

import com.google.gson.Gson
import com.massky.greenlandvland.R
import com.massky.greenlandvland.common.CommonUtil
import com.massky.greenlandvland.common.MD5Utils
import com.massky.greenlandvland.common.SharedPreferencesUtils
import com.massky.greenlandvland.common.ToastUtil
import com.massky.greenlandvland.model.entity.Sc_getTokenNew
import com.massky.greenlandvland.model.entity.Sc_newLoginNew
import com.massky.greenlandvland.model.httpclient.HttpUrl.HttpClient
import com.massky.greenlandvland.model.httpclient.UICallback

import java.util.Timer
import java.util.TimerTask

import cn.jpush.android.api.JPushInterface

class LogoActivity : AppCompatActivity() {
    private var loginAccount: String? = null
    private var password: String? = null
    private var timeStamp: String? = null
    private var signature: String? = null
    private var token: String? = null
    private var giId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logo)
        loginAccount = SharedPreferencesUtils.getLoginAccount(this)
        password = SharedPreferencesUtils.getPassWord(this)
        giId = JPushInterface.getRegistrationID(this@LogoActivity)
        Log.e("gid", "gid:" + giId!!)
        if (TextUtils.isEmpty(giId)) {
            giId = JPushInterface.getRegistrationID(this@LogoActivity)
        }
        SharedPreferencesUtils.savePhoneId(this, giId)


        val task = object : TimerTask() {
            override fun run() {
                if (!TextUtils.isEmpty(loginAccount) && !TextUtils.isEmpty(password)) {
                    timeStamp = CommonUtil.getDate()
                    signature = MD5Utils.getMD5Str(loginAccount + password + timeStamp)
                    HttpClient.post(CommonUtil.APPURL, "sc_getTokenNew", Gson().toJson(Sc_getTokenNew.GetTokenNewParams(loginAccount, timeStamp, signature, 3)), object : UICallback {
                        override fun process(data: String) {
                            Log.e("TAG", "data=$data")
                            Log.e("TAG", "timeStamp=" + timeStamp!!)
                            Log.e("TAG", "signature=" + signature!!)
                            val tokenResult = Gson().fromJson(data, Sc_getTokenNew.GetTokenNewResult::class.java)
                            val result = tokenResult.result
                            if (result == 1) {
                                Log.e("TAG", "1-json解析错误")
                                ToastUtil.showToast(this@LogoActivity, "账号或密码错误")
                                startActivity(Intent(this@LogoActivity, LoginActivity::class.java))
                                finish()
                            } else if (result == 100) {
                                Log.e("TAG", "100-成功")
                                SharedPreferencesUtils.saveToken(this@LogoActivity, tokenResult.token)
                                token = tokenResult.token
                                Login()


                            } else if (result == 101) {
                                Log.d("TAG", "result=101-失败,账号或密码错误")
                                ToastUtil.showToast(this@LogoActivity, "账号或密码错误")
                                startActivity(Intent(this@LogoActivity, LoginActivity::class.java))
                                finish()
                            } else if (result == 102) {
                                Log.e("TAG", "result=102签名错误")
                                ToastUtil.showToast(this@LogoActivity, "账号或密码错误")
                                startActivity(Intent(this@LogoActivity, LoginActivity::class.java))
                                finish()
                            } else if (result == 103) {
                                Log.e("TAG", "result=103时间戳错误")
                                ToastUtil.showToast(this@LogoActivity, "账号或密码错误")
                                startActivity(Intent(this@LogoActivity, LoginActivity::class.java))
                                finish()
                            } else if (result == 104) {
                                Log.e("TAG", "result=104appCode不正确")
                                ToastUtil.showToast(this@LogoActivity, "账号或密码错误")
                                startActivity(Intent(this@LogoActivity, LoginActivity::class.java))
                                finish()
                            } else {
                                ToastUtil.showToast(this@LogoActivity, "账号或密码错误")
                                startActivity(Intent(this@LogoActivity, LoginActivity::class.java))
                                finish()
                            }
                        }

                        override fun onError(data: String) {
                            ToastUtil.showToast(this@LogoActivity, "网络连接失败")
                            startActivity(Intent(this@LogoActivity, LoginActivity::class.java))
                            finish()
                        }
                    })
                } else {
                    startActivity(Intent(this@LogoActivity, LoginActivity::class.java))
                    finish()
                }

            }
        }
        val timer = Timer()
        timer.schedule(task, 2000)
    }

    //登录
    private fun Login() {
        HttpClient.post(CommonUtil.APPURL, "sc_newLoginNew", Gson().toJson(Sc_newLoginNew.NewLoginNewParams(token, giId, giId, 3)), object : UICallback {
            override fun process(data: String) {
                //                                                            Log.e("TAG","login="+data);
                //                                                            Log.e("TAG","token="+token);
                //                                                            Log.e("TAG","giId="+giId);
                //                                                            Log.e("TAG","IMEI="+IMEI);
                val loginResult = Gson().fromJson(data, Sc_newLoginNew.NewLoginNewResult::class.java)
                //                                                            Sc_login.LoginResult loginResult=new Gson().fromJson(data, new TypeToken<Sc_login.LoginResult>(){}.getType());
                //                                                            Log.e("TAG","phoneNumber="+loginResult.getPhoneNumber());
                //                                                            Log.e("TAG","accountType="+loginResult.getAccountType());
                //                                                            Log.e("TAG","avatar="+loginResult.getAvatar());
                //                                                            Log.e("TAG","userName="+loginResult.getUserName());
                val result = loginResult.result
                if (result == 1) {
                    Log.e("TAG", "1-解析错误")
                    ToastUtil.showToast(this@LogoActivity, "登录失败")
                    startActivity(Intent(this@LogoActivity, LoginActivity::class.java))
                    finish()
                } else if (result == 100) {
                    Log.e("TAG", "100-成功")
                    //                            SharedPreferencesUtils.saveLogin(LoginActivity.this, loginResult);
                    SharedPreferencesUtils.savePhoneNumber(this@LogoActivity, loginResult.phoneNumber)
                    SharedPreferencesUtils.saveAvatar(this@LogoActivity, loginResult.avatar)
                    SharedPreferencesUtils.saveUserName(this@LogoActivity, loginResult.userName)
                    //                            SharedPreferencesUtils.saveProjectList(LoginActivity.this, loginResult.getProjectList());
                    SharedPreferencesUtils.saveProjectRoomType<Sc_newLoginNew.NewLoginNewResult.ProjectRoomType>(this@LogoActivity, loginResult.projectRoomType)
                    startActivity(Intent(this@LogoActivity, MainActivity::class.java))
                    finish()

                } else if (result == 101) {
                    Log.e("TAG", "101-token错误")
                    ToastUtil.showToast(this@LogoActivity, "登录失败")
                    startActivity(Intent(this@LogoActivity, LoginActivity::class.java))
                    finish()
                } else {
                    ToastUtil.showToast(this@LogoActivity, "登录失败")
                    startActivity(Intent(this@LogoActivity, LoginActivity::class.java))
                    finish()
                }
            }

            override fun onError(data: String) {
                ToastUtil.showToast(this@LogoActivity, "网络连接失败")
                startActivity(Intent(this@LogoActivity, LoginActivity::class.java))
                finish()
            }
        })

    }
}
