package com.massky.greenlandvland.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View

import com.massky.greenlandvland.R
import com.massky.greenlandvland.View.XListView
import com.massky.greenlandvland.common.SharedPreferencesUtils
import com.massky.greenlandvland.event.MyDialogEvent
import com.massky.greenlandvland.event.MyEvent
import com.massky.greenlandvland.ui.adapter.HandSceneAdapter
import com.massky.greenlandvland.ui.base.BaseFragment1
import com.massky.greenlandvland.ui.sraum.AddTogenInterface.AddTogglenInterfacer
import com.massky.greenlandvland.ui.sraum.User
import com.massky.greenlandvland.ui.sraum.Util.DialogUtil
import com.massky.greenlandvland.ui.sraum.Util.MyOkHttp
import com.massky.greenlandvland.ui.sraum.Util.Mycallback
import com.massky.greenlandvland.ui.sraum.Utils.ApiHelper

import com.mob.commons.SHARESDK

import java.util.ArrayList
import java.util.HashMap

import butterknife.BindView
import com.massky.greenlandvland.ui.sraum.Util.SharedPreferencesUtil
import de.greenrobot.event.EventBus
import okhttp3.Call

import org.greenrobot.eventbus.Subscribe

/**
 * Created by zhu on 2017/11/30.
 */
class HandSceneFragment : BaseFragment1(), XListView.IXListViewListener {
    private  var dialogUtil: DialogUtil? = null
    @BindView(R.id.xListView_scan)
    internal var xListView_scan: XListView? = null
    private val mHandler = Handler()
    private val list_hand_scene = ArrayList<Map<*, *>>()
    private var handSceneAdapter: HandSceneAdapter? = null
    internal var again_elements = arrayOf("全屋灯光全开", "全屋灯光全开", "回家模式", "离家", "吃饭模式", "看电视", "睡觉", "K歌")
    private var scenelist: List<User.scenelist> = ArrayList()
    private val list = ArrayList<Map<*, *>>()


    private val listint = ArrayList<Int>()
    private val listintwo = ArrayList<Int>()
    private var first_add: Int = 0

    private var loginPhone: String? = null
    private var vibflag: Boolean = false
    private var musicflag: Boolean = false

    internal var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            if (list.size == 0) {
                xListView_scan!!.visibility = View.GONE
            } else {
                xListView_scan!!.visibility = View.VISIBLE
            }

            handSceneAdapter!!.setList_s(list, listint, listintwo, true)
            handSceneAdapter!!.setflag(vibflag, musicflag)
            handSceneAdapter!!.notifyDataSetChanged()
        }
    }

    override fun onData() {}

    override fun onEvent() {}

    override fun onEvent(eventData: MyDialogEvent) {

    }

    override fun onResume() {
        super.onResume()
        init_music_flag()
        Thread(Runnable { sraum_getManuallyScenes() }).start()
        common_second()
        if (first_add == 1) {
            first_add = 2
        } else {
            val event = MyEvent()
            event.setContent("刷新")
            //...设置event
            EventBus.getDefault().post(event)
        }
    }


    @Subscribe
    fun onEvent(event: MyEvent) {
        val status = event.getContent()
        when (status) {
            "scene_second" -> Thread(Runnable { sraum_getManuallyScenes() }).start()
        }
    }

    /**
     * 获取手动场景
     */
    private fun sraum_getManuallyScenes() {
        val map = HashMap<String,String>()
        val areaNumber = SharedPreferencesUtil.getData(activity, "areaNumber", "") as String
        val order = SharedPreferencesUtil.getData(activity, "order", "1") as String
        map.put("areaNumber", areaNumber)
        map.put("token", SharedPreferencesUtils.getToken(activity))
        map.put("order", order)
        //        if (dialogUtil != null) {
        //            dialogUtil.loadDialog();
        //        }

        //        mapdevice.put("boxNumber", TokenUtil.getBoxnumber(SelectSensorActivity.this));
        MyOkHttp.postMapString(ApiHelper.sraum_getManuallyScenes, map, object : Mycallback(AddTogglenInterfacer {
            //刷新togglen数据
            sraum_getManuallyScenes()
        }, activity, dialogUtil) {
            override fun onError(call: Call, e: Exception, id: Int) {
                super.onError(call, e, id)
            }

            override fun pullDataError() {
                super.pullDataError()
            }

            override fun emptyResult() {
                super.emptyResult()
            }

            override fun wrongToken() {
                super.wrongToken()
                //重新去获取togglen,这里是因为没有拉到数据所以需要重新获取togglen
            }

            override fun wrongBoxnumber() {
                super.wrongBoxnumber()
            }

            override fun onSuccess(user: User) {
                scenelist = user.sceneList
                list.clear()
                for (us in scenelist) {
                    val map = HashMap<Any,Any>()
                    map.put("type", us.type)
                    map.put("name", us.name)
                    map.put("number", us.number)
                    map.put("boxNumber", us.boxNumber)
                    map.put("panelNumber", us.panelNumber)
                    map.put("panelType", us.panelType)
                    //buttonNumber
                    map.put("buttonNumber", us.buttonNumber)
                    list.add(map)
                    setPicture(us.type)
                }

                handler.sendEmptyMessage(0)
            }
        })
    }


    private fun init_music_flag() {
        loginPhone = SharedPreferencesUtil.getData(activity, "loginPhone", "") as String
        val preferences = activity.getSharedPreferences("sraum" + loginPhone!!,
                Context.MODE_PRIVATE)
        vibflag = preferences.getBoolean("vibflag", false)
        //        musicflag = preferences.getBoolean("musicflag", false);
        musicflag = SharedPreferencesUtil.getData(activity, "musicflag", false) as Boolean
    }


    private fun setPicture(type: String) {
        when (type) {
            "1" -> {
                listint.add(R.drawable.add_scene_homein)
                listintwo.add(R.drawable.gohome2)
            }
            "2" -> {
                listint.add(R.drawable.add_scene_homeout)
                listintwo.add(R.drawable.leavehome2)
            }
            "3" -> {
                listint.add(R.drawable.add_scene_sleep)
                listintwo.add(R.drawable.sleep2)
            }
            "4" -> {
                listint.add(R.drawable.add_scene_nightlamp)
                listintwo.add(R.drawable.getup_night2)
            }
            "5" -> {
                listint.add(R.drawable.add_scene_getup)
                listintwo.add(R.drawable.getup_morning2)
            }
            "6" -> {
                listint.add(R.drawable.add_scene_cup)
                listintwo.add(R.drawable.rest2)
            }
            "7" -> {
                listint.add(R.drawable.add_scene_book)
                listintwo.add(R.drawable.study2)
            }
            "8" -> {
                listint.add(R.drawable.add_scene_moive)
                listintwo.add(R.drawable.movie2)
            }
            "9" -> {
                listint.add(R.drawable.add_scene_meeting)
                listintwo.add(R.drawable.meeting2)
            }
            "10" -> {
                listint.add(R.drawable.add_scene_cycle)
                listintwo.add(R.drawable.sport2)
            }
            "11" -> {
                listint.add(R.drawable.add_scene_noddle)
                listintwo.add(R.drawable.dinner2)
            }
            "12" -> {
                listint.add(R.drawable.add_scene_lampon)
                listintwo.add(R.drawable.open_all2)
            }
            "13" -> {
                listint.add(R.drawable.add_scene_lampoff)
                listintwo.add(R.drawable.close_all2)
            }
            "14" -> {
                listint.add(R.drawable.defaultpic)
                listintwo.add(R.drawable.defaultpicheck)
            }
            "101"//101-手动云场景
            -> {
                listint.add(R.drawable.defaultpic)
                listintwo.add(R.drawable.defaultpicheck)
            }
            else -> {
                listint.add(R.drawable.defaultpic)
                listintwo.add(R.drawable.defaultpicheck)
            }
        }
    }

    override fun viewId(): Int {
        return R.layout.hand_scene_lay
    }

    override fun onView(view: View) {
        dialogUtil = DialogUtil(activity)
        EventBus.getDefault().register(this)
        handSceneAdapter = HandSceneAdapter(activity, list, listint, listintwo, dialogUtil!!, vibflag, musicflag, object : HandSceneAdapter.RefreshListener {
            override fun refresh() {
                Thread(Runnable { sraum_getManuallyScenes() }).start()
                common_second()
                val event = MyEvent()
                event.setContent("刷新")
                EventBus.getDefault().post(event)
            }
        })
        xListView_scan!!.adapter = handSceneAdapter
        xListView_scan!!.setPullLoadEnable(false)
        xListView_scan!!.setXListViewListener(this)
        xListView_scan!!.setFootViewHide()
        first_add = 1
    }

    override fun onClick(v: View) {

    }

    private fun onLoad() {
        xListView_scan!!.stopRefresh()
        xListView_scan!!.stopLoadMore()
        xListView_scan!!.setRefreshTime("刚刚")
    }


    override fun onRefresh() {
        onLoad()
        Thread(Runnable { sraum_getManuallyScenes() }).start()
        common_second()
    }

    override fun onLoadMore() {
        mHandler.postDelayed({ onLoad() }, 1000)
    }

    /**
     * 清除联动信息
     */
    private fun common_second() {
        SharedPreferencesUtil.saveData(activity, "linkId", "")
        SharedPreferencesUtil.saveInfo_List(activity, "list_result", ArrayList<Map<*, *>>())
        SharedPreferencesUtil.saveInfo_List(activity, "list_condition", ArrayList<Map<*, *>>())
        SharedPreferencesUtil.saveData(activity, "editlink", false)
        SharedPreferencesUtil.saveInfo_List(activity, "link_information_list", ArrayList<Map<*, *>>())
        SharedPreferencesUtil.saveData(activity, "add_condition", false)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    companion object {

        fun newInstance(): HandSceneFragment {
            val newFragment = HandSceneFragment()
            val bundle = Bundle()
            newFragment.arguments = bundle
            return newFragment
        }
    }

}
