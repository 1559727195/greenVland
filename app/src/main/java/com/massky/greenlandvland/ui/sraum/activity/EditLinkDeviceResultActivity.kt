package com.massky.greenlandvland.ui.sraum.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Message
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.TextView

import com.massky.greenlandvland.R
import com.massky.greenlandvland.common.ToastUtil
import com.massky.greenlandvland.ui.sraum.AddTogenInterface.AddTogglenInterfacer
import com.massky.greenlandvland.ui.sraum.User

import com.massky.greenlandvland.ui.sraum.Util.DialogUtil
import com.massky.greenlandvland.ui.sraum.Util.MyOkHttp
import com.massky.greenlandvland.ui.sraum.Util.Mycallback
import com.massky.greenlandvland.ui.sraum.Util.SharedPreferencesUtil
import com.massky.greenlandvland.ui.sraum.Utils.ApiHelper
import com.massky.greenlandvland.ui.sraum.Utils.AppManager
import com.massky.greenlandvland.ui.sraum.adapter.EditLinkDeviceCondinationAndResultAdapter
import com.massky.greenlandvland.ui.sraum.base.BaseActivity
import com.massky.greenlandvland.util.IntentUtil
import com.yanzhenjie.statusview.StatusUtils
import com.yanzhenjie.statusview.StatusView

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Collections
import java.util.Date
import java.util.HashMap

import butterknife.BindView
import com.bigkoo.pickerview.TimePickerView
import com.bigkoo.pickerview.listener.CustomListener


import com.google.gson.internal.bind.ObjectTypeAdapter
import com.massky.greenlandvland.common.SharedPreferencesUtils
import com.massky.greenlandvland.ui.MainActivity
import com.massky.greenlandvland.ui.sraum.Util.Timeuti.getTime
import com.massky.greenlandvland.ui.sraum.bean.TimeSelectBean
import kotlinx.android.synthetic.main.edit_link_dev_result_act.*
import okhttp3.Call

/**
 * Created by zhu on 2018/7/3.
 */

class EditLinkDeviceResultActivity : BaseActivity() {

    //    @BindView(R.id.sleep_time_txt)
    //    TextView sleep_time_txt;
    //    @BindView(R.id.get_up_time_txt)
    //    TextView get_up_time_txt;
    private var hourPicker: String? = null
    private var minutePicker: String? = null
    private var time_content: String? = null
    private var dialogUtil: DialogUtil? = null
    private var device_map: Map<*, *>? = HashMap<String, Object>()
    private var sensor_map: Map<*, *>? = HashMap<String, Object>()//传感器map
    private var editLinkDeviceCondinationAndResultAdapter_condition: EditLinkDeviceCondinationAndResultAdapter? = null
    private var editLinkDeviceCondinationAndResultAdapter_result: EditLinkDeviceCondinationAndResultAdapter? = null
    private var list_condition: MutableList<Map<*, *>>? = ArrayList()
    private var list_result: MutableList<Map<*, *>>? = ArrayList()
    private var linkId = ""
    private var link_information = HashMap<String, Any>()
    private var pvCustomTime: TimePickerView? = null
    private var index_select: Int = 0
    private var startTime = ""
    private var endTime = ""
    private var linkName = ""
    private var link_information_list: MutableList<Map<*, *>> = ArrayList()//
    private val add_condition: Boolean = false
    private var link_first: Boolean = false
    private var type = ""

    private val editLinkDeviceCondinationAndResultAdapter_asscoted: EditLinkDeviceCondinationAndResultAdapter? = null
    private val list_asscoted: List<Map<*, *>>? = null

    internal var handler: android.os.Handler = object : android.os.Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                0 -> {
                    start_time_txt!!.text = time_content
                    startTime = start_time_txt!!.text.toString()
                }
                1 -> {
                    end_time_txt!!.text = time_content
                    endTime = end_time_txt!!.text.toString()
                }
            }//开始
            //结束
        }
    }
    //    private  List<Map> list_link = new ArrayList<>();

    override fun viewId(): Int {
        return R.layout.edit_link_dev_result_act
    }

    override fun onView() {
        dialogUtil = DialogUtil(this)
        StatusUtils.setFullToStatusBar(this)  // StatusBar.
        onData1()
        onEvent1()
        //        linkId = (String) SharedPreferencesUtil.getData(EditLinkDeviceResultActivity.this, "linkId", "");

        //          SharedPreferencesUtil.saveInfo_List(EditLinkDeviceResultActivity.this, "device_map",list_result);
        //          SharedPreferencesUtil.getInfo_List(EditLinkDeviceResultActivity.this, "device_map");
        //        ToastUtil.showToast(EditLinkDeviceResultActivity.this,"ss:" + ss);
    }

    override fun onEvent() {

    }

    override fun onData() {

    }

    internal fun onEvent1() {
        editLinkDeviceCondinationAndResultAdapter_condition = EditLinkDeviceCondinationAndResultAdapter(this@EditLinkDeviceResultActivity,
                list_condition!!, object : EditLinkDeviceCondinationAndResultAdapter.ExcutecuteListener {

            override fun excute_cordition() {
                condition_add!!.visibility = View.VISIBLE
                list_condition = SharedPreferencesUtil.getInfo_List(this@EditLinkDeviceResultActivity, "list_condition")
            }

            override fun excute_result() {

            }
        })

        maclistview_id_condition!!.adapter = editLinkDeviceCondinationAndResultAdapter_condition
        editLinkDeviceCondinationAndResultAdapter_result = EditLinkDeviceCondinationAndResultAdapter(this@EditLinkDeviceResultActivity,
                list_result!!, object : EditLinkDeviceCondinationAndResultAdapter.ExcutecuteListener {
            override fun excute_cordition() {

            }

            override fun excute_result() {
                list_result = SharedPreferencesUtil.getInfo_List(this@EditLinkDeviceResultActivity, "list_result")
            }
        })
        maclistview_id_result!!.adapter = editLinkDeviceCondinationAndResultAdapter_result
        back!!.setOnClickListener(this)
        result_add!!.setOnClickListener(this)
        condition_add!!.setOnClickListener(this)
        next_step_txt!!.setOnClickListener(this)

        initCustomTimePicker()
        sleep_time_rel!!.setOnClickListener(this)
        get_up_rel!!.setOnClickListener(this)
        startTime = start_time_txt!!.text.toString()
        endTime = end_time_txt!!.text.toString()
    }

    internal fun onData1() {
        link_information = intent.getSerializableExtra("link_information") as HashMap<String, Any>
        link_information_list = SharedPreferencesUtil.getInfo_List(this@EditLinkDeviceResultActivity, "link_information_list")
        if (link_information != null || link_information_list.size != 0) {//来自联动列表的编辑按钮
            //            list_result = SharedPreferencesUtil.getInfo_List(EditLinkDeviceResultActivity.this, "list_result");
            //            list_condition = SharedPreferencesUtil.getInfo_List(EditLinkDeviceResultActivity.this, "list_condition");
            //获取接口的联动列表信息，设备联动信息，sraum_deviceLinkInfo，
            //            linkId = (String) getIntent().getSerializableExtra("linkId");
            if (link_information != null) {
                link_information_list = ArrayList()
                link_information_list.add(link_information!!)
                SharedPreferencesUtil.saveInfo_List(this@EditLinkDeviceResultActivity, "link_information_list", link_information_list)
            } else if (link_information_list.size != 0) {
                link_information = link_information_list[0] as HashMap<String, Any>
            }
            next_step_txt!!.text = "保存"
            back!!.text = "返回"
            //            time_select_linear.setVisibility(View.VISIBLE);
            linkId = link_information!!["linkId"] as String
            linkName = link_information!!["linkName"] as String
            startTime = link_information!!["startTime"] as String
            endTime = link_information!!["endTime"] as String
            start_time_txt!!.text = startTime
            end_time_txt!!.text = endTime
            link_first = SharedPreferencesUtil.getData(this@EditLinkDeviceResultActivity, "link_first", false) as Boolean
            type = if (link_information!!["type"] as String? == null) "" else link_information!!["type"] as String
            if (link_first) { //第一
                // 次编辑联动
                when (type) {
                    "101" -> link_edit_from_shoudongbutton(linkId)
                    "100", "102" -> link_edit_from_tokenbutton(linkId)
                }
            } else {
                sensor_map = intent.getSerializableExtra("sensor_map") as Map<*, *>
                if (sensor_map != null) {
                    list_condition!!.add(sensor_map!!)//执行条件只有一个
                    SharedPreferencesUtil.saveInfo_List(this@EditLinkDeviceResultActivity, "list_condition", list_condition!!)
                }
                list_condition = SharedPreferencesUtil.getInfo_List(this@EditLinkDeviceResultActivity, "list_condition")
                list_result = SharedPreferencesUtil.getInfo_List(this@EditLinkDeviceResultActivity, "list_result")
                when (type) {
                    "100"//自动
                    -> {
                        startTime = link_information!!["startTime"] as String
                        endTime = link_information!!["endTime"] as String
                    }
                    "102"//定时
                    -> {
                        startTime = list_condition!![0]["startTime"] as String
                        endTime = list_condition!![0]["endTime"] as String
                    }
                }
                add_device()
                SharedPreferencesUtil.saveInfo_List(this@EditLinkDeviceResultActivity, "list_result", list_result!!)
                start_time_txt!!.text = startTime
                end_time_txt!!.text = endTime
                if (list_condition!!.size != 0) condition_add!!.visibility = View.GONE
                if (sensor_map == null) {
                    time_select_linear!!.visibility = View.GONE
                } else
                    when (type) {
                        "100"//自动执行条件
                        -> time_select_linear!!.visibility = View.VISIBLE
                        "101"//手动执行条件
                            , "102" -> time_select_linear!!.visibility = View.GONE
                    }
            }
            SharedPreferencesUtil.saveData(this@EditLinkDeviceResultActivity, "linkId", linkId)
            SharedPreferencesUtil.saveData(this@EditLinkDeviceResultActivity, "editlink", true)//
            SharedPreferencesUtil.saveData(this@EditLinkDeviceResultActivity, "link_first", false)
            SharedPreferencesUtil.saveData(this@EditLinkDeviceResultActivity, "add_condition", false)
            //add_condition
            //为编辑联动动作
        } else {
            back!!.text = "取消"
            time_select_linear!!.visibility = View.GONE
            next_step_txt!!.text = "下一步"
            device_map = intent.getSerializableExtra("device_map") as Map<*, *>
            sensor_map = intent.getSerializableExtra("sensor_map") as Map<*, *>
            type = if (sensor_map!!["type"] as String? == null) "" else sensor_map!!["type"] as String
            list_condition!!.add(sensor_map!!)//执行条件只有一个
            list_result = SharedPreferencesUtil.getInfo_List(this@EditLinkDeviceResultActivity, "list_result")
            if (device_map != null) {
                if (list_result!!.size != 0) {
                    var isexist = false
                    for (i in list_result!!.indices) {
                        if (device_map!!["number"]!!.toString() == list_result!![i]["number"]) {
                            list_result!!.removeAt(i)
                            list_result!!.add(device_map!!)
                            isexist = true
                        } else {
                            isexist = false
                        }
                    }
                    if (!isexist) {
                        list_result!!.add(device_map!!)
                    }
                } else {
                    list_result!!.add(device_map!!)
                }
            }
            if (list_condition!!.size != 0) condition_add!!.visibility = View.GONE
            SharedPreferencesUtil.saveInfo_List(this@EditLinkDeviceResultActivity, "list_condition", list_condition!!)
            SharedPreferencesUtil.saveInfo_List(this@EditLinkDeviceResultActivity, "list_result", list_result!!)
        }
    }

    /**
     * 获取手动场景关联信息
     *
     * @param linkId
     */
    private fun link_edit_from_shoudongbutton(linkId: String) {
        val map = HashMap<String, Any>()
        val areaNumber = SharedPreferencesUtil.getData(this@EditLinkDeviceResultActivity, "areaNumber", "") as String
        map["token"] = SharedPreferencesUtils.getToken(this@EditLinkDeviceResultActivity)
        map["number"] = linkId
        map["areaNumber"] = areaNumber
        dialogUtil!!.loadDialog()
        MyOkHttp.postMapObject(ApiHelper.sraum_getOneSceneInfo, map,
                object : Mycallback(AddTogglenInterfacer { }, this@EditLinkDeviceResultActivity, dialogUtil) {
                    override fun onError(call: Call, e: Exception, id: Int) {
                        super.onError(call, e, id)
                        //                        refresh_view.stopRefresh(false);
                    }

                    override fun onSuccess(user: User) {
                        super.onSuccess(user)
                        val map = HashMap<Any, Any>()
                        //                        map.put("deviceId", user.deviceLinkInfo.deviceId);
                        map["deviceType"] = ""
                        map["deviceName"] = ""
                        map["name1"] = ""
                        map["deviceId"] = ""
                        //                        map.put("linkName", user.deviceLinkInfo.linkName);
                        map["condition"] = ""
                        map["minValue"] = ""
                        map["maxValue"] = ""
                        map["startTime"] = ""
                        map["endTime"] = ""
                        map["type"] = ""
                        map["boxName"] = ""
                        val action_map = HashMap<String, Any>()
                        action_map.put("action", "执行")
                        map["deviceName"] = "手动执行"
                        map["name1"] = "手动执行"
                        time_select_linear!!.visibility = View.GONE


                        map["action"] = action_map.get("action")!!
                        link_information["startTime"] = ""
                        link_information!!["endTime"] = ""
                        if (link_information != null) {
                            link_information_list = ArrayList()
                            link_information_list.add(link_information)
                            SharedPreferencesUtil.saveInfo_List(this@EditLinkDeviceResultActivity, "link_information_list", link_information_list)
                        }
                        //添加条件
                        list_condition!!.clear()
                        list_result!!.clear()
                        list_condition!!.add(map)

                        for (udsce in user.list) {
                            val map_device = HashMap<String, Any>()

                            map_device.put("type", udsce.type)
                            map_device.put("number", udsce.number)
                            map_device.put("name", udsce.name)
                            map_device.put("status", udsce.status)
                            map_device.put("dimmer", udsce.dimmer)
                            map_device.put("mode", udsce.mode)
                            map_device.put("temperature", udsce.temperature)
                            map_device.put("speed", udsce.speed)
                            map_device.put("panelName", udsce.panelName)
                            map_device.put("boxName", if (udsce.boxName == null) "" else udsce.boxName)
                            map_device.put("panelMac", udsce.panelMac)
                            map_device.put("gatewayMac", if (udsce.gatewayMac == null) "" else udsce.gatewayMac)

                            map_device.put("name1", udsce.name)
                            map_device.put("action", get_action(map_device))
                            if (udsce.type == "100") {
                                map_device.put("name", "场景")
                                map_device.put("name1", "场景")
                                map_device.put("status", "1")
                            } else {
                                map_device.put("name", udsce.name)
                                map_device.put("name1", udsce.name)
                            }
                            list_result!!.add(map_device)
                        }
                        //添加结果
                        if (list_condition!!.size != 0) condition_add!!.visibility = View.GONE
                        SharedPreferencesUtil.saveInfo_List(this@EditLinkDeviceResultActivity, "list_condition", list_condition!!)
                        SharedPreferencesUtil.saveInfo_List(this@EditLinkDeviceResultActivity, "list_result", list_result!!)

                        add_device()
                        editLinkDeviceCondinationAndResultAdapter_condition!!.setlist(list_condition!!)
                        editLinkDeviceCondinationAndResultAdapter_result!!.setlist(list_result!!)
                    }

                    override fun wrongToken() {
                        super.wrongToken()
                    }
                })
    }

    private fun add_device() {
        device_map = intent.getSerializableExtra("device_map") as Map<*, *>
        if (device_map == null) return
        list_result = SharedPreferencesUtil.getInfo_List(this@EditLinkDeviceResultActivity, "list_result")
        if (list_result!!.size != 0) {
            var isexist = false
            for (i in list_result!!.indices) {
                if (device_map!!["number"]!!.toString() == list_result!![i]["number"]) {
                    list_result!!.removeAt(i)
                    list_result!!.add(device_map!!)
                    isexist = true
                } else {
                    isexist = false
                }
            }
            if (!isexist) {
                list_result!!.add(device_map!!)
            }
        } else {
            list_result!!.add(device_map!!)
        }
    }

    /**
     * 这个是点击联动列表，之后获取的联动设备信息
     *
     * @param linkId
     */
    private fun link_edit_from_tokenbutton(linkId: String) {
        val map = HashMap<String, Any>()
        val areaNumber = SharedPreferencesUtil.getData(this@EditLinkDeviceResultActivity, "areaNumber", "") as String
        map["token"] = SharedPreferencesUtils.getToken(this@EditLinkDeviceResultActivity)
        map["linkId"] = linkId
        map["areaNumber"] = areaNumber
        dialogUtil!!.loadDialog()
        MyOkHttp.postMapObject(ApiHelper.sraum_deviceLinkInfo, map,
                object : Mycallback(AddTogglenInterfacer { }, this@EditLinkDeviceResultActivity, dialogUtil) {


                    override fun onError(call: Call, e: Exception, id: Int) {
                        super.onError(call, e, id)
                        //                        refresh_view.stopRefresh(false);
                    }

                    override fun onSuccess(user: User) {
                        super.onSuccess(user)
                        val map = HashMap<String, Any>()
                        //                        map.put("deviceId", user.deviceLinkInfo.deviceId);
                        map["deviceType"] = user.deviceLinkInfo.deviceType
                        if (user.deviceLinkInfo.deviceName == null) {
                            map["deviceName"] = ""
                            map["name1"] = ""
                        } else {
                            map["deviceName"] = user.deviceLinkInfo.deviceName
                            map["name1"] = user.deviceLinkInfo.deviceName
                        }
                        map["deviceId"] = user.deviceLinkInfo.deviceId
                        //                        map.put("linkName", user.deviceLinkInfo.linkName);
                        map["condition"] = user.deviceLinkInfo.condition
                        map["minValue"] = user.deviceLinkInfo.minValue
                        map["maxValue"] = user.deviceLinkInfo.maxValue
                        map["startTime"] = user.deviceLinkInfo.startTime
                        map["endTime"] = user.deviceLinkInfo.endTime
                        map["type"] = user.deviceLinkInfo.type
                        map["boxName"] = if (user.deviceLinkInfo.boxName == null) "" else user.deviceLinkInfo.boxName
                        var action_map = HashMap<String, Any>()
                        type = user.deviceLinkInfo.type
                        when (user.deviceLinkInfo.type) {
                            "100"//自动执行条件
                            -> {
                                action_map = setAction(user.deviceLinkInfo.deviceType, user.deviceLinkInfo.condition, map)//action字段
                                time_select_linear!!.visibility = View.VISIBLE
                            }
                            "102"//定时场景
                            -> {
                                map["name1"] = user.deviceLinkInfo.startTime
                                when (user.deviceLinkInfo.condition) {
                                    "5"//自定义
                                    -> get_time_dingshi(user, action_map)
                                    "2" -> action_map["action"] = "每天"
                                    "3" -> action_map["action"] = "工作日"
                                    "4" -> action_map["action"] = "周末"
                                }
                                time_select_linear!!.visibility = View.GONE
                            }
                            "101"//手动执行条件
                            -> {
                                action_map["action"] = "执行"
                                map["deviceName"] = "手动执行"
                                map["name1"] = "手动执行"
                                time_select_linear!!.visibility = View.GONE
                            }
                        }

                        map["action"] = if (action_map["action"] == null) "" else action_map["action"]!!
                        link_information["startTime"] = user.deviceLinkInfo.startTime
                        link_information!!["endTime"] = user.deviceLinkInfo.endTime
                        if (link_information != null) {
                            link_information_list = ArrayList<Map<*, *>>()
                            link_information_list.add(link_information)
                            SharedPreferencesUtil.saveInfo_List(this@EditLinkDeviceResultActivity, "link_information_list", link_information_list)
                        }
                        startTime = link_information!!["startTime"] as String
                        endTime = link_information!!["endTime"] as String
                        start_time_txt!!.text = startTime
                        end_time_txt!!.text = endTime
                        //添加条件

                        list_condition!!.clear()
                        list_result!!.clear()
                        list_condition!!.add(map)
                        for (i in user.deviceLinkInfo.deviceList.indices) {
                            val map_device = HashMap<String, Any>()
                            map_device.put("type", user.deviceLinkInfo.deviceList[i].type)
                            map_device.put("number", user.deviceLinkInfo.deviceList[i].number)
                            map_device.put("status", user.deviceLinkInfo.deviceList[i].status)
                            map_device.put("dimmer", user.deviceLinkInfo.deviceList[i].dimmer)
                            map_device.put("mode", user.deviceLinkInfo.deviceList[i].mode)
                            map_device.put("temperature", user.deviceLinkInfo.deviceList[i].temperature)
                            map_device.put("speed", user.deviceLinkInfo.deviceList[i].speed)
                            map_device.put("name", user.deviceLinkInfo.deviceList[i].name)
                            map_device.put("boxName", if (user.deviceLinkInfo.deviceList[i].boxName == null)
                                ""
                            else
                                user.deviceLinkInfo.deviceList[i].boxName)
                            map_device.put("name1", user.deviceLinkInfo.deviceList[i].name)
                            map_device.put("action", get_action(map_device))
                            if (user.deviceLinkInfo.deviceList[i].type == "100") {
                                map_device.put("name", "场景")
                                map_device.put("name1", "场景")
                                map_device.put("status", "1")
                            } else {
                                map_device.put("name", user.deviceLinkInfo.deviceList[i].name)
                                map_device.put("name1", user.deviceLinkInfo.deviceList[i].name)
                            }
                            map_device.put("panelMac", user.deviceLinkInfo.deviceList[i].panelMac)
                            map_device.put("gatewayMac", user.deviceLinkInfo.deviceList[i].gatewayMac)
                            list_result!!.add(map_device)
                        }

                        //添加结果
                        if (list_condition!!.size != 0) condition_add!!.visibility = View.GONE
                        SharedPreferencesUtil.saveInfo_List(this@EditLinkDeviceResultActivity, "list_condition", list_condition!!)
                        SharedPreferencesUtil.saveInfo_List(this@EditLinkDeviceResultActivity, "list_result", list_result!!)
                        add_device()
                        editLinkDeviceCondinationAndResultAdapter_condition!!.setlist(list_condition!!)
                        editLinkDeviceCondinationAndResultAdapter_result!!.setlist(list_result!!)
                    }

                    override fun wrongToken() {
                        super.wrongToken()
                    }
                })
    }

    /**
     * 获取定时
     *
     * @param user
     * @param action_map
     */
    private fun get_time_dingshi(user: User, action_map: HashMap<String, Any>) {
        val splits = user.deviceLinkInfo.minValue.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val list_hand_scene = ArrayList<Map<*, *>>()
        val list = ArrayList<TimeSelectBean>()
        for (element in splits) {
            val map1 = HashMap<String, Any>()
            map1.put("name", Integer.parseInt(element))
            when (Integer.parseInt(element)) {
                2 -> map1.put("time", "周一")
                3 -> map1.put("time", "周二")
                4 -> map1.put("time", "周三")
                5 -> map1.put("time", "周四")
                6 -> map1.put("time", "周五")
                7 -> map1.put("time", "周六")
                1 -> map1.put("time", "周日")
            }
            list_hand_scene.add(map1)
        }

        list.clear()
        for (map2 in list_hand_scene) {
            list.add(TimeSelectBean(map2["time"] as String?, map2["name"] as Int))
        }
        Collections.sort(list)
        val stringBuffer_name = StringBuffer()
        for (i in list.indices) {
            if (i != list.size - 1) {
                stringBuffer_name.append(list[i].getName() + ",")
            } else {
                stringBuffer_name.append(list[i].getName())
            }
        }
        action_map["action"] = stringBuffer_name.toString()
    }


    //7-门磁，8-人体感应，9-水浸检测器，10-入墙PM2.5
    //11-紧急按钮，12-久坐报警器，13-烟雾报警器，14-天然气报警器，15-智能门锁，16-直流电阀机械手
    private fun setAction(type: String, action: String, map_link: HashMap<String, Any>): HashMap<String, Any> {
        var text_pm = ""
        val minValue = map_link["minValue"] as String?
        val maxValue = map_link["maxValue"] as String?
        var temp = ""
        when (type) {
            "7" -> if (action == "1") {
                map_link["action"] = "打开"
            } else {
                map_link["action"] = "闭合"
            }
            "8" -> if (action == "1") {
                map_link["action"] = "有人经过"
            }
            "9" ->

                if (action == "1") {
                    map_link["action"] = "报警"
                }
            "10" -> {
                if (minValue != "") {
                    text_pm = minValue!!
                    temp = "小于等于 "
                }

                if (maxValue != "") {
                    text_pm = maxValue!!
                    temp = "大于等于 "
                }

                when (action) {
                    "1" -> map_link["action"] = "PM2.5 $temp$text_pm"
                    "2" -> map_link["action"] = "温度 $temp$text_pm℃"
                    "3" -> map_link["action"] = "湿度 $temp$text_pm%"
                }
            }
            "102"//桌面PM2.5
            -> {
                if (minValue != "") {
                    text_pm = minValue!!
                    temp = "小于等于 "
                }

                if (maxValue != "") {
                    text_pm = maxValue!!
                    temp = "大于等于 "
                }

                when (action) {
                    "1" -> map_link["action"] = "PM2.5 $temp$text_pm"
                    "2" -> map_link["action"] = "PM1.0 $temp$text_pm"
                    "3" -> map_link["action"] = "PM10 $temp$text_pm"
                }
            }
            "11" -> if (action == "1") {
                map_link["action"] = "按下"
            }
            "12" -> if (action == "1") {
                map_link["action"] = "报警"
            }
            "13" -> if (action == "1") {
                map_link["action"] = "报警"
            }
            "14" -> if (action == "1") {
                map_link["action"] = "报警"
            }
            "15" ->
                //            case "17":
                if (action == "1") {
                    map_link["action"] = "打开"
                } else {
                    map_link["action"] = "闭合"
                }
            "16" -> {
            }
        }
        return map_link
    }

    /**
     * 针对调光灯带的打开下拉显示
     *
     * @param map
     */
    private fun get_action(map: Map<*, *>): String {
        var action = ""
        val type = map["type"]!!.toString()
        when (type) {
            "1"//调光关闭
                , "17" -> action = init_action_light((map["status"] as String?)!!)
            "100"//打开-》添加调光值数据，
            -> action = map["name1"] as String
            "2" -> action = init_action_tiaoguang((map["status"] as String?)!!, map["dimmer"] as String)
            "3" -> action = init_action_kongtiao("3", (map["status"] as String?)!!, map["mode"] as String, map["speed"] as String, map["temperature"] as String)
            "4" -> action = init_action_curtain(map["status"] as String, "4")
            "18" -> action = init_action_curtain(map["status"] as String, "18")
            "5" -> action = init_action_kongtiao("5", (map["status"] as String?)!!, map["mode"] as String, map["speed"] as String, map["temperature"] as String)
            "6" -> action = init_action_kongtiao("6", (map["status"] as String?)!!, map["mode"] as String, map["speed"] as String, map["temperature"] as String)
            "202"//wifi电视
                , "206"//wifi空调
            -> action = init_action_wifi_device((map["status"] as String?)!!)
            "15", "16" ->
                //            case "17":
                action = init_action_smart_door_lock((map["status"] as String?)!!)
            "19", "20", "21" -> action = init_action_smart_pingyi(map["status"] as String, type)
        }
        return action
    } //

    /**
     * 窗帘
     */
    /**
     * 窗帘
     */
    private fun init_action_curtain(status: String, type: String): String {
        var action = ""
        when (type) {
            "4" -> action = old_curtain_window(status, action)
            "18" -> action = new_curtain_window(status, action)
        }
        return action
    }

    /**
     * A411-A414
     *
     * @param status
     * @param action
     * @return
     */
    private fun new_curtain_window(status: String, action: String): String {
        var action = action
        when (status) {
            "0" -> action = "关闭"
            "1" -> action = "打开"
        }
        return action
    }

    /**
     * A401
     *
     * @param status
     * @param action
     * @return
     */
    private fun old_curtain_window(status: String, action: String): String {
        var action = action
        when (status) {
            "0" -> action = "全部关闭"
            "1" -> action = "全部打开"

            "4" -> action = "内纱打开"

            "6" -> action = "内纱关闭"
            "7" -> action = "外纱关闭"
            "8" -> action = "外纱打开"
        }
        return action
    }


    /**
     * wifi设备
     */
    private fun init_action_wifi_device(status: String): String {
        var action = ""
        when (status) {
            "0" -> action = "关"
            "1" -> action = "开"
        }
        return action
    }

    /**
     * 智能门锁
     */
    private fun init_action_smart_door_lock(status: String): String {
        var action = ""
        when (status) {
            "0" -> action = "关闭"
            "1" -> action = "打开"
        }
        return action
    }


    /**
     * 平移控制器
     */
    private fun init_action_smart_pingyi(status: String, type: String): String {
        var action = ""
        when (type) {
            "19" -> action = common_select_pingyi(status, action, "上升", "下降")
            "20" -> action = common_select_pingyi(status, action, "向左", "向右")
            "21" -> when (status) {
                "1" -> action = "高位"
                "2" -> action = "中位"
                "3" -> action = "低位"
                "0" -> action = "暂停"
            }
        }
        return action
    }

    private fun common_select_pingyi(status: String, action: String, action1: String, action2: String): String {
        var action = action
        when (status) {
            "1" -> action = action1
            "2" -> action = action2
            "0" -> action = "停"
        }
        return action
    }


    /**
     * 空调
     *
     * @param
     * @param type
     */
    private fun init_action_kongtiao(type: String, status: String, model: String, speed: String, tempature: String): String {
        var action = ""
        when (status) {
            "0" -> action = "关闭"
            "1" -> action = init_action_air(type, model, speed, tempature)
        }

        return action
    }

    /**
     * 初始化空调动作
     *
     * @param type
     * @param model
     * @param speed
     * @param tempature
     */
    private fun init_action_air(type: String, model: String, speed: String, tempature: String): String {
        val temp = StringBuffer()
        //        String speed = (String) air_control_map.get("speed");
        when (speed) {
            "1" -> temp.append("低风")
            "2" -> temp.append("中风")
            "3" -> temp.append("高风")
            "4" -> temp.append("强力")
            "5" -> temp.append("送风")
            "6" -> temp.append("自动")
        }
        //        String temperature = (String) air_control_map.get("temperature");

        temp.append("  $tempature℃")
        when (type) {
            "3" -> init_common_air(model, temp)
            "6" -> common_mode_dinuan(model, temp)
        }
        //        common_doit("action", temp.toString());
        return temp.toString()
    }

    private fun init_common_air(model: String, temp: StringBuffer) {//亮堂着
        when (model) {
            "1" -> temp.append("  " + "制冷")
            "2" -> temp.append("  " + "制热")
            "3" -> temp.append("  " + "除湿")
            "4" -> temp.append("  " + "自动")
            "5" -> temp.append("  " + "通风")
        }
    }

    private fun common_mode_dinuan(model: String, temp: StringBuffer) {

        when (model) {
            "1" -> temp.append("  " + "加热")
            "2" -> temp.append("  " + "睡眠")
            "3" -> temp.append("  " + "外出")
        }
    }


    /**
     * 调光灯
     *
     * @param dimmer
     */
    private fun init_action_tiaoguang(status: String, dimmer: String): String {
        var action = ""
        when (status) {
            "0" -> action = "关闭"
            "1" -> action = "调光值:$dimmer"
        }
        return action
    }


    override fun onDestroy() {
        super.onDestroy()
        //        common_second();
    }

    /**
     * 灯控
     */
    private fun init_action_light(status: String): String {
        var action = ""
        when (status) {
            "0" -> action = "关闭"
            "1" -> action = "打开"
            "3" -> action = "切换"
        }

        return action
    }

    override fun onClick(v: View) {
        var intent: Intent? = null
        list_condition = SharedPreferencesUtil.getInfo_List(this@EditLinkDeviceResultActivity, "list_condition")
        when (v.id) {
            R.id.back//点击取消弹出框
            -> when (back!!.text.toString()) {
                "返回" ->
                    //                        common_second();
                    //                        EditLinkDeviceResultActivity.this.finish();
                    //                        AppManager.getAppManager().finishActivity_current(EditLinkDeviceResultActivity.class);
                    common()
                "取消" -> showCenterDeleteDialog()
            }

            R.id.result_add//添加执行结果
            ->
                //                AppManager.getAppManager().finishActivity_current(AirLinkageControlActivity.class);
                //                AppManager.getAppManager().finishActivity_current(SelectiveLinkageDeviceDetailSecondActivity.class);
                //                AppManager.getAppManager().finishActivity_current(ExcuteSomeOneSceneActivity.class);
                //                AppManager.getAppManager().finishActivity_current(SelectiveLinkageActivity.class);
                if (list_condition == null || list_condition!!.size == 0) {
                    ToastUtil.showToast(this@EditLinkDeviceResultActivity, "请添加执行条件")
                } else {
                    //                    AppManager.getAppManager().removeActivity_but_activity_cls(MainfragmentActivity.class);
                    intent = Intent(this@EditLinkDeviceResultActivity, SelectiveLinkageActivity::class.java)
                    intent.putExtra("link_map", list_condition!![0] as Serializable)
                    startActivity(intent)
                    //                    EditLinkDeviceResultActivity.this.finish();
                }
            R.id.condition_add -> {
                //                AppManager.getAppManager().removeActivity_but_activity_cls(MainfragmentActivity.class);

                SharedPreferencesUtil.saveData(this@EditLinkDeviceResultActivity, "add_condition", true)

                when (type) {
                    "102" -> intent = Intent(this@EditLinkDeviceResultActivity, TimeExcuteCordinationActivity::class.java)
                    else -> intent = Intent(this@EditLinkDeviceResultActivity, SelectSensorActivity::class.java)
                }
                startActivity(intent)
            }
            R.id.next_step_txt -> {
                if (list_condition == null || list_condition!!.size == 0) {
                    ToastUtil.showToast(this@EditLinkDeviceResultActivity, "请添加执行条件")
                    return
                }

                if (list_result == null || list_result!!.size == 0) {
                    ToastUtil.showToast(this@EditLinkDeviceResultActivity, "请添加执行结果")
                    return
                }

                when (next_step_txt!!.text.toString()) {
                    "下一步" -> {
                        intent = Intent(this@EditLinkDeviceResultActivity, SetSelectLinkActivity::class.java)
                        intent.putExtra("link_information", link_information as Serializable?)
                        startActivity(intent)
                    }
                    "保存" -> when (type) {
                        "100", "102" -> getData(true, linkName, linkId, ApiHelper.sraum_updateDeviceLink)
                        "101" -> set_Hand_Data(true, "", linkId, ApiHelper.sraum_editManuallyScene)
                    }
                }
            }

            R.id.sleep_time_rel//睡觉
            -> {
                index_select = 1
                pvCustomTime!!.show() //弹出自定义时间选择器
            }
            R.id.get_up_rel//起床
            -> {
                index_select = 0
                pvCustomTime!!.show() //弹出自定义时间选择器
            }
        }//                EditLinkDeviceResultActivity.this.finish();
        //                SharedPreferencesUtil.remove_current_index(EditLinkDeviceResultActivity.this, "list_condition", 0);
        //                SharedPreferencesUtil.saveInfo_List(EditLinkDeviceResultActivity.this, "list_result", new ArrayList<Map>());
    }

    /**
     * 更新
     *
     * @param flag
     * @param linkName
     * @param linkId
     * @param apiname
     */
    private fun getData(flag: Boolean, linkName: String, linkId: String, apiname: String) {
        val map = HashMap<String, Any>()
        val areaNumber = SharedPreferencesUtil.getData(this@EditLinkDeviceResultActivity, "areaNumber", "") as String
        map["token"] = SharedPreferencesUtils.getToken(this@EditLinkDeviceResultActivity)
        map["areaNumber"] = areaNumber
        map["deviceId"] = list_condition!![0]["deviceId"]!!
        map["deviceType"] = list_condition!![0]["deviceType"]!!
        map["linkName"] = linkName
        map["type"] = list_condition!![0]["type"]!!
        map["condition"] = list_condition!![0]["condition"]!!
        map["minValue"] = list_condition!![0]["minValue"]!!
        map["maxValue"] = list_condition!![0]["maxValue"]!!
        map["startTime"] = startTime
        map["endTime"] = endTime
        if (flag) {
            map["linkId"] = linkId
        } else {

        }
        val list = ArrayList<Map<*, *>>()
        for (i in list_result!!.indices) {
            val map_device = HashMap<String, Any>()
            map_device.put("type", list_result!![i]["type"]!!)
            map_device.put("number", list_result!![i]["number"]!!)
            map_device.put("status", list_result!![i]["status"]!!)
            map_device.put("dimmer", list_result!![i]["dimmer"]!!)
            map_device.put("mode", list_result!![i]["mode"]!!)
            map_device.put("temperature", list_result!![i]["temperature"]!!)
            map_device.put("speed", list_result!![i]["speed"]!!)
            map_device.put("panelMac", list_result!![i]["panelMac"]!!)
            map_device.put("gatewayMac", list_result!![i]["gatewayMac"]!!)
            list.add(map_device)
        }
        map["deviceList"] = list

        dialogUtil!!.loadDialog()
        MyOkHttp.postMapObject(apiname, map,
                object : Mycallback(AddTogglenInterfacer { getData(flag, linkName, linkId, apiname) }, this@EditLinkDeviceResultActivity, dialogUtil) {
                    override fun onError(call: Call, e: Exception, id: Int) {
                        super.onError(call, e, id)
                        //                        refresh_view.stopRefresh(false);
                        common()
                    }

                    override fun onSuccess(user: User) {
                        super.onSuccess(user)
                        common()
                    }

                    override fun wrongToken() {
                        super.wrongToken()
                        common()
                    }

                    override fun pullDataError() {
                        common()
                    }

                    override fun sevenCode() {
                        common()
                    }

                    override fun fiveCode() {
                        common()
                    }

                    override fun fourCode() {
                        common()
                        ToastUtil.showToast(this@EditLinkDeviceResultActivity, "104 deviceList 信息有误")
                    }

                    override fun threeCode() {
                        common()
                        ToastUtil.showToast(this@EditLinkDeviceResultActivity, "linkId 错误 ")
                    }

                    override fun wrongBoxnumber() {
                        common()
                        ToastUtil.showToast(this@EditLinkDeviceResultActivity, "areaNumber不正确,")
                    }
                })
    }


    /**
     * 编辑手动场景关联信息
     *
     * @param flag
     * @param linkName
     * @param linkId
     * @param apiname
     */
    private fun set_Hand_Data(flag: Boolean, linkName: String, linkId: String, apiname: String) {
        val map = HashMap<String, Any>()
        val areaNumber = SharedPreferencesUtil.getData(this@EditLinkDeviceResultActivity, "areaNumber", "") as String
        map["token"] = SharedPreferencesUtils.getToken(this@EditLinkDeviceResultActivity)
        map["areaNumber"] = areaNumber

        if (flag) {
            map["number"] = linkId
        } else {
            map["sceneName"] = linkName
            map["sceneType"] = "1"
            map["panelNumber"] = ""
            map["buttonNumber"] = ""
        }
        val list = ArrayList<Map<*, *>>()
        for (i in list_result!!.indices) {
            val map_device = HashMap<String, Any>()
            map_device.put("type", list_result!![i]["type"]!!)
            map_device.put("number", list_result!![i]["number"]!!)
            map_device.put("status", list_result!![i]["status"]!!)
            map_device.put("dimmer", list_result!![i]["dimmer"]!!)
            map_device.put("mode", list_result!![i]["mode"]!!)
            map_device.put("temperature", list_result!![i]["temperature"]!!)
            map_device.put("speed", list_result!![i]["speed"]!!)
            map_device.put("panelMac", if (list_result!![i]["panelMac"] == null) "" else list_result!![i]["panelMac"]!!)
            map_device.put("gatewayMac", if (list_result!![i]["gatewayMac"] == null) "" else list_result!![i]["gatewayMac"]!!)
            list.add(map_device)
        }
        map["deviceList"] = list

        dialogUtil!!.loadDialog()
        MyOkHttp.postMapObject(apiname, map,
                object : Mycallback(AddTogglenInterfacer { getData(flag, linkName, linkId, apiname) }, this@EditLinkDeviceResultActivity, dialogUtil) {

                    override fun onError(call: Call, e: Exception, id: Int) {
                        super.onError(call, e, id)
                        //                        refresh_view.stopRefresh(false);
                        common()
                    }

                    override fun onSuccess(user: User) {
                        super.onSuccess(user)
                        common()
                        val boxNumber = user.boxNumber
                        //                      手动场景 添加的时候我会返回一个boxNumber,这个参数有值，代表你加的是网关场景，然后你可以做关联面板，这个参数没有值，那就是云场景
                        //                                没有关联面板这个操作
                        if (boxNumber != null && boxNumber != "") {//去关联面板
                            val bundle1 = Bundle()
                            bundle1.putString("sceneName", linkName)
                            bundle1.putString("sceneType", "1")
                            bundle1.putString("boxNumber", boxNumber)
                            bundle1.putString("panelType", "")
                            bundle1.putString("panelNumber", "")
                            bundle1.putString("buttonNumber", "")
                            IntentUtil.startActivity(this@EditLinkDeviceResultActivity, AssociatedpanelActivity::class.java, bundle1)

                        }
                    }

                    override fun wrongToken() {
                        super.wrongToken()
                        common()
                    }

                    override fun pullDataError() {
                        common()
                    }

                    override fun sevenCode() {
                        common()
                    }

                    override fun fiveCode() {
                        common()
                        ToastUtil.showToast(this@EditLinkDeviceResultActivity, "更新失败")
                    }

                    override fun fourCode() {
                        common()
                        ToastUtil.showToast(this@EditLinkDeviceResultActivity, "deviceList 信息不正确,")
                    }

                    override fun threeCode() {
                        common()
                        ToastUtil.showToast(this@EditLinkDeviceResultActivity, "number 不存在")
                    }

                    override fun wrongBoxnumber() {
                        common()
                        ToastUtil.showToast(this@EditLinkDeviceResultActivity, "areaNumber\n" + "不存在")
                    }
                })
    }


    /**
     * 公共方法
     */
    private fun common() {
        AppManager.getAppManager().removeActivity_but_activity_cls(MainActivity::class.java)
        //        Intent intent = new Intent(EditLinkDeviceResultActivity.this, LinkageListActivity.class);
        //        startActivity(intent);
        this@EditLinkDeviceResultActivity.finish()
        common_second()
    }


    //自定义dialog,centerDialog删除对话框
    fun showCenterDeleteDialog() {
        //        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //        // 布局填充器
        //        LayoutInflater inflater = LayoutInflater.from(getActivity());
        //        View view = inflater.inflate(R.layout.user_name_dialog, null);
        //        // 设置自定义的对话框界面
        //        builder.setView(view);
        //
        //        cus_dialog = builder.create();
        //        cus_dialog.show();

        val view = LayoutInflater.from(this@EditLinkDeviceResultActivity).inflate(R.layout.cancel_dialog, null)
        val confirm: TextView //确定按钮
        val cancel: TextView //确定按钮
        val tv_title: TextView
        //        final TextView content; //内容
        cancel = view.findViewById(R.id.call_cancel)
        confirm = view.findViewById(R.id.call_confirm)
        tv_title = view.findViewById(R.id.tv_title)
        //        tv_title.setText("是否拨打119");
        //        content.setText(message);
        //显示数据
        val dialog = Dialog(this@EditLinkDeviceResultActivity, R.style.BottomDialog)
        dialog.setContentView(view)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        val dm = resources.displayMetrics
        val displayWidth = dm.widthPixels
        val displayHeight = dm.heightPixels
        val p = dialog.window!!.attributes //获取对话框当前的参数值
        p.width = (displayWidth * 0.8).toInt() //宽度设置为屏幕的0.5
        //        p.height = (int) (displayHeight * 0.5); //宽度设置为屏幕的0.5
        //        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        dialog.window!!.attributes = p  //设置生效
        dialog.show()

        cancel.setOnClickListener { dialog.dismiss() }

        confirm.setOnClickListener {
            common()
            dialog.dismiss()
        }
    }


    private fun common_second() {
        SharedPreferencesUtil.saveData(this@EditLinkDeviceResultActivity, "linkId", "")
        SharedPreferencesUtil.saveInfo_List(this@EditLinkDeviceResultActivity, "list_result", ArrayList())
        SharedPreferencesUtil.saveInfo_List(this@EditLinkDeviceResultActivity, "list_condition", ArrayList())
        SharedPreferencesUtil.saveData(this@EditLinkDeviceResultActivity, "editlink", false)
        SharedPreferencesUtil.saveInfo_List(this@EditLinkDeviceResultActivity, "link_information_list", ArrayList())
        SharedPreferencesUtil.saveData(this@EditLinkDeviceResultActivity, "add_condition", false)
    }


    private fun initCustomTimePicker() {

        /**
         * @description
         *
         * 注意事项：
         * 1.自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针.
         * 具体可参考demo 里面的两个自定义layout布局。
         * 2.因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
         * setRangDate方法控制起始终止时间(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
         */
        val selectedDate = Calendar.getInstance()//系统当前时间
        val startDate = Calendar.getInstance()
        startDate.set(2014, 1, 23)
        val endDate = Calendar.getInstance()
        endDate.set(2027, 2, 28)
        //时间选择器 ，自定义布局
        pvCustomTime = TimePickerView.Builder(this, TimePickerView.OnTimeSelectListener { date, v ->
            //选中事件回调
            getTime(date)
            Log.e("robin debug", "getTime(date):" + getTime(date))
            hourPicker = date.hours.toString()
            minutePicker = date.minutes.toString()
            when (minutePicker.toString().length) {
                1 -> minutePicker = "0$minutePicker"
            }

            when (hourPicker.toString().length) {
                1 -> hourPicker = "0$hourPicker"
            }

            time_content = "$hourPicker:$minutePicker"
            handler.sendEmptyMessage(index_select)
            //                start_scenetime.setText(hourPicker + ":" + minutePicker);
        }).setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.pickerview_custom_time) { v ->
                    val tvSubmit = v.findViewById(R.id.ok_bt) as ImageView
                    val ivCancel = v.findViewById(R.id.finish_bt) as ImageView
                    tvSubmit.setOnClickListener {
                        pvCustomTime!!.returnData()
                        pvCustomTime!!.dismiss()
                    }

                    ivCancel.setOnClickListener { pvCustomTime!!.dismiss() }
                }
                .setContentSize(18)
                .setType(booleanArrayOf(false, false, false, true, true, false))
                .setLabel("年", "月", "日", "小时", "分钟", "秒")
                .setLineSpacingMultiplier(1.2f)
                .setTextXOffset(0, 0, 0, 0, 0, 0)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(-0xdb5263)
                .build()
    }

    private fun getTime(date: Date): String {//可根据需要自行截取数据显示
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return format.format(date)
    }

    public override fun onBackPressed() {
        //        showCenterDeleteDialog();
        when (back!!.getText().toString()) {
            "返回" ->
                //                common_second();
                //                EditLinkDeviceResultActivity.this.finish();
                common()
            "取消" -> showCenterDeleteDialog()
        }
    }
}
