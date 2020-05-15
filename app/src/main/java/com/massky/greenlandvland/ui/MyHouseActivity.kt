package com.massky.greenlandvland.ui

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

import com.google.gson.Gson
import com.massky.greenlandvland.R
import com.massky.greenlandvland.common.CommonUtil
import com.massky.greenlandvland.common.LocalBroadcastManager
import com.massky.greenlandvland.common.SharedPreferencesUtils
import com.massky.greenlandvland.model.entity.Sc_isLoginNew
import com.massky.greenlandvland.model.entity.Sc_myRoom
import com.massky.greenlandvland.model.httpclient.HttpUrl.HttpClient
import com.massky.greenlandvland.model.httpclient.UICallback
import com.massky.greenlandvland.ui.adapter.SimpleFragmentAdapter

class MyHouseActivity : AppCompatActivity() {
    private var back: ImageView? = null//回退键
    private var viewPager: ViewPager? = null//滑动页面
    private var sliding_taps: TabLayout? = null
    private var sceneList: List<Sc_myRoom.MyRoomResult.RoomListBean>? = null
    private var adapter: SimpleFragmentAdapter? = null


    internal var clickListener: View.OnClickListener = View.OnClickListener { v ->
        when (v.id) {
            R.id.back//回退键
            -> onBackPressed()
        }
    }
    private var mMessageReceiver: MessageReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_house)
        sceneList = SharedPreferencesUtils.getMyRoom(this@MyHouseActivity, Sc_myRoom.MyRoomResult.RoomListBean::class.java)
        initViews()//获取控件
        initDatas()//获取数据
        initCliclListener()//获取监听

    }

    //获取控件
    private fun initViews() {
        back = findViewById(R.id.back)
        sliding_taps = findViewById(R.id.sliding_taps)
        viewPager = findViewById(R.id.viewPager)
    }

    //获取监听器
    private fun initCliclListener() {
        back!!.setOnClickListener(clickListener)
    }

    //获取数据
    private fun initDatas() {

        adapter = SimpleFragmentAdapter(this@MyHouseActivity, sceneList, supportFragmentManager)
        adapter!!.notifyDataSetChanged()
        viewPager!!.adapter = adapter
        sliding_taps!!.setupWithViewPager(viewPager)//把tablelayout和fragment绑定
        sliding_taps!!.tabMode = TabLayout.MODE_FIXED//多个Tab无滑动效果
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onResume() {
        isForeground = true
        isLogin()
        super.onResume()
    }

    override fun onPause() {
        isForeground = false
        super.onPause()
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver)
        super.onDestroy()
    }

    fun registerMessageReceiver() {
        mMessageReceiver = MessageReceiver()
        val filter = IntentFilter()
        filter.priority = IntentFilter.SYSTEM_HIGH_PRIORITY
        filter.addAction(MESSAGE_RECEIVED_ACTION)
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter)
    }

    inner class MessageReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION == intent.action) {
                    val messge = intent.getStringExtra(KEY_MESSAGE)
                    val extras = intent.getStringExtra(KEY_EXTRAS)
                    val showMsg = StringBuilder()
                    showMsg.append("$KEY_MESSAGE : $messge\n")
                    if (!TextUtils.isEmpty(extras)) {
                        showMsg.append("$KEY_EXTRAS : $extras\n")
                        Log.e("TAG", "showMsg=$showMsg")
                        val message = Gson().fromJson(extras, com.massky.greenlandvland.model.entity.Message::class.java)
                        Log.e("TAG", "message=$message")
                        if (message.type == "2") {
                            showdialog()
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("TAG", "错误")
            }

        }
    }

    private fun isLogin() {
        val phoneNumber = SharedPreferencesUtils.getPhoneNumber(this)
        val phoneId = SharedPreferencesUtils.getPhoneId(this)
        HttpClient.post(CommonUtil.APPURL, "sc_isLoginNew", Gson().toJson(Sc_isLoginNew.LoginNewParams(phoneNumber, phoneId, 3)), object : UICallback {
            override fun process(data: String) {
                Log.e("TAG", "data=$data")
                Log.e("TAG", "phoneNumber=$phoneNumber")
                Log.e("TAG", "phoneId=$phoneId")
                val isLoginResult = Gson().fromJson(data, Sc_isLoginNew.LoginNewResult::class.java)
                val result = isLoginResult.result
                if (result == 1) {
                    Log.e("TAG", "1-json格式解析失败")
                } else if (result == 100) {
                    Log.e("TAG", "100-成功")
                } else if (result == 103) {
                    Log.e("TAG", "103-已登录")
                    showdialog()
                } else {

                }
            }

            override fun onError(data: String) {

            }
        })
    }

    private fun showdialog() {
        val builder = AlertDialog.Builder(this@MyHouseActivity, 1)
        builder.setTitle("提示")
        builder.setMessage("该账号已被其他设备登录")
        builder.setCancelable(false)
        builder.setPositiveButton("确定") { dialog, which ->
            dialog.dismiss()
            startActivity(Intent(this@MyHouseActivity, LoginActivity::class.java))
            SharedPreferencesUtils.cleanPassword(this@MyHouseActivity)
            MainActivity.instance.finish()
            SmartHomeActivity.instance!!.finish()
            finish()
        }
        builder.create().show()
    }

    companion object {

        var isForeground = false
        val MESSAGE_RECEIVED_ACTION = "com.massky.smartcommunity.MESSAGE_RECEIVED_ACTION"
        val KEY_TITLE = "title"
        val KEY_MESSAGE = "message"
        val KEY_EXTRAS = "extras"
    }
}
