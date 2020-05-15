package com.massky.greenlandvland.ui.sraum.activity

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.massky.greenlandvland.R
import com.massky.greenlandvland.ui.sraum.AddTogenInterface.AddTogglenInterfacer
import com.massky.greenlandvland.ui.sraum.User
import com.massky.greenlandvland.ui.sraum.Util.DialogUtil
import com.massky.greenlandvland.ui.sraum.Util.MyOkHttp
import com.massky.greenlandvland.ui.sraum.Util.Mycallback
import com.massky.greenlandvland.ui.sraum.Utils.ApiHelper
import com.massky.greenlandvland.ui.sraum.Utils.AppManager
import com.massky.greenlandvland.ui.sraum.adapter.SelectLinkageItemSecondAdapter
import com.massky.greenlandvland.ui.sraum.base.BaseActivity
import com.massky.greenlandvland.ui.sraum.view.PullToRefreshLayout
import com.yanzhenjie.statusview.StatusUtils
import com.yanzhenjie.statusview.StatusView
import java.io.Serializable
import java.util.ArrayList
import java.util.HashMap
import butterknife.BindView
import com.massky.greenlandvland.common.SharedPreferencesUtils
import com.massky.greenlandvland.ui.sraum.Util.SharedPreferencesUtil
import kotlinx.android.synthetic.main.selective_link_devdetail_second.*
import okhttp3.Call

/**
 * Created by zhu on 2018/1/5.
 */

class SelectiveLinkageDeviceDetailSecondActivity : BaseActivity(), AdapterView.OnItemClickListener, PullToRefreshLayout.OnRefreshListener {


    private var dialogUtil: DialogUtil? = null
    private var panelNumber: String? = null
    private val deviceList = ArrayList<User.device>()
    private val deviceActionList = ArrayList<Map<*, *>>()//执行动作集合
    private val list_type_index = ArrayList<Map<*, *>>()//收集某种设备类型的索引集合
    private var selectexcutesceneresultadapter: SelectLinkageItemSecondAdapter? = null
    private val listint = ArrayList<Map<*, *>>()
    private val listintwo = ArrayList<Map<*, *>>()
    private var panelType: String? = null
    private val list_map = ArrayList<Map<String, Any>>()
    private var panelName: String? = null
    private var sensor_map = HashMap<String,Any>()//传感器map
    private var boxName: String? = null
    private var boxNumber: String? = null
    private val panelType1: String? = null
    private val panelName1: String? = null
    private var panelMAC: String? = null
    private var gatewayMAC: String? = null

    /**
     * 空调
     *
     * @return
     */

    override fun viewId(): Int {
        return R.layout.selective_link_devdetail_second
    }

    override fun onView() {
        dialogUtil = DialogUtil(this)
        StatusUtils.setFullToStatusBar(this)  // StatusBar.
        back!!.setOnClickListener(this)
        panelNumber = intent.getSerializableExtra("panelNumber") as String
        panelType = intent.getSerializableExtra("panelType") as String
        panelName = intent.getSerializableExtra("panelName") as String
        sensor_map = intent.getSerializableExtra("sensor_map") as HashMap<String, Any>
        //  intent.putExtra("boxName", (Serializable) listbox.get(position));
        boxName = intent.getSerializableExtra("boxName") as String
        boxNumber = intent.getSerializableExtra("boxNumber") as String
        gatewayMAC = intent.getSerializableExtra("gatewayMac") as String
        if (panelName != null) project_select!!.text = panelName
        getData(true)
        selectexcutesceneresultadapter = SelectLinkageItemSecondAdapter(this@SelectiveLinkageDeviceDetailSecondActivity,
                list_map)
        maclistview_id!!.adapter = selectexcutesceneresultadapter
        maclistview_id!!.onItemClickListener = this
        //        refresh_view.autoRefresh();
        refresh_view!!.noreleasePull()
    }

    override fun onEvent() {

    }

    override fun onData() {

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.back -> this@SelectiveLinkageDeviceDetailSecondActivity.finish()
        }
    }

    private fun getData(flag: Boolean) {
        val map = HashMap<String, Any>()
        map["token"] = SharedPreferencesUtils.getToken(this@SelectiveLinkageDeviceDetailSecondActivity)
        val areaNumber = SharedPreferencesUtil.getData(this@SelectiveLinkageDeviceDetailSecondActivity, "areaNumber", "") as String
        map["boxNumber"] = boxNumber!!
        map["panelNumber"] = panelNumber!!
        map["areaNumber"] = areaNumber
        if (flag) {
            dialogUtil!!.loadDialog()
        }
        MyOkHttp.postMapObject(ApiHelper.sraum_getPanelDevices, map,
                object : Mycallback(AddTogglenInterfacer { getData(false) }, this@SelectiveLinkageDeviceDetailSecondActivity, dialogUtil) {


                    override fun onError(call: Call, e: Exception, id: Int) {
                        super.onError(call, e, id)
                        //                        refresh_view.stopRefresh(false);
                    }

                    override fun onSuccess(user: User) {
                        super.onSuccess(user)
                        //                        refresh_view.stopRefresh();

                        //                        adapter = new PanelAdapter(SelectiveLinkageActivity.this, panelList, mySlidingMenu, accountType
                        //                                , new PanelAdapter.PanelAdapterListener() {
                        //                            @Override
                        //                            public void panel_adapter_listener() {//删除item时刷新界面
                        //                                getData(false);
                        //                            }
                        //                        });

                        panelType = user.panelType
                        panelName = user.panelName
                        panelMAC = user.panelMAC
                        gatewayMAC = user.gatewayMAC
                        deviceList.clear()
                        deviceActionList.clear()
                        list_type_index.clear()

                        for (position in user.deviceList.indices) {
                            val mapdevice = HashMap<String, Any>()
                            val type_index = HashMap<String, Any>()
                            mapdevice["type"] = user.deviceList[position].type
                            mapdevice["number"] = user.deviceList[position].number
                            mapdevice["status"] = user.deviceList[position].status
                            mapdevice["dimmer"] = user.deviceList[position].dimmer
                            mapdevice["mode"] = user.deviceList[position].mode
                            mapdevice["temperature"] = user.deviceList[position].temperature
                            mapdevice["speed"] = user.deviceList[position].speed
                            mapdevice["name"] = user.deviceList[position].name
                            mapdevice["boxName"] = boxName!!
                            mapdevice["button"] = user.deviceList[position].button
                            mapdevice["panelName"] = user.deviceList[position].panelName
                            mapdevice["boxNumber"] = if (boxNumber == null) "" else boxNumber!!
                            type_index["type"] = user.deviceList[position].type
                            type_index["index"] = position
                            list_type_index.add(type_index)
                            deviceActionList.add(mapdevice)
                        }
                        deviceList.addAll(user.deviceList)
                        setPicture(panelType!!)
                        selectexcutesceneresultadapter!!.setlist(list_map)
                        selectexcutesceneresultadapter!!.notifyDataSetChanged()
                    }

                    override fun wrongToken() {
                        super.wrongToken()
                    }
                })
    }


    private fun setPicture(type: String) {
        when (type) {
            "A201", "A202", "A203", "A204" -> {
                light()
                panel_txt_promat!!.text = "执行开关"
            }
            "A301", "A302", "A303", "A311", "A312", "A313", "A321", "A322", "A331" -> {
                tiaoguang_light()
                panel_txt_promat!!.text = "执行开关"
            }
            "B401", "B402", "B403" -> {
                smart_move()
                panel_txt_promat!!.text = "平移控制器"
            }


            "A401", "A411", "A412", "A413", "A414" -> {
                curtain_window()
                panel_txt_promat!!.text = "执行开关"
            }
            "A501"// 501是空调面板，511是空调模块跳过去是空调列表，
            -> {
            }
            "A511" -> {
                panel_txt_promat!!.text = "选择空调"
                air_control_list(type)
            }
            "A611" -> {
                panel_txt_promat!!.text = "选择新风"
                air_control_list(type)
            }
            "A711" -> {
                panel_txt_promat!!.text = "选择地暖"
                air_control_list(type)
            }
            "A801" -> {
            }
            "A901" -> {
            }
            "A902",

            "AB01" -> {
            }
            "AB04" -> {
            }
            "AC01" -> {
            }
            "AD01" -> {
            }
            "AD02" -> {
            }
            "B001" -> {
            }

            "B101" -> {
                panel_txt_promat!!.text = "执行开关"
                //                air_control_list(type);
                chazuo_door()
            }
            "B201" -> {
                panel_txt_promat!!.text = "选择智能门锁"
                air_control_list(type)
            }
            "B301" -> {
                panel_txt_promat!!.text = "选择机械手"
                air_control_list(type)
            }
        }//直接跳转到空调设备界面，
        //        ToastUtil.showToast(SelectiveLinkageDeviceDetailSecondActivity.this, "list_map:" + list_map.size());
    }

    private fun smart_move() {
        for (i in deviceActionList.indices) {

            when (deviceActionList[i]["type"]!!.toString()) {
                "19" -> common_step_s("", i, deviceActionList[i]["type"]!!.toString(),
                        deviceActionList[i]["name"]!!.toString(), "上升", "上升", "下降")
                "20" -> common_step_s("", i, deviceActionList[i]["type"]!!.toString(),
                        deviceActionList[i]["name"]!!.toString(), "向左", "向左", "向右")
                "21" -> common_top_middle("", i, deviceActionList[i]["type"]!!.toString(),
                        deviceActionList[i]["name"]!!.toString())
            }
        }
    }


    private fun common_top_middle(tabname: String, position: Int, type: String, name: String) {
        for (j in 0..3) {
            val map = HashMap<String,Any>()
            when (j) {
                0//开
                -> {
                    map.put("name", "高位")
                    map.put("status", "1")
                    map.put("action", "高位")
                }
                1//关
                -> {
                    map.put("name", "中位")
                    map.put("status", "2")
                    //                    map.put("action", "关闭");
                    map.put("action", "中位")
                }
                2//切换
                -> {
                    map.put("name", "低位")
                    map.put("status", "3")
                    map.put("action", "低位")
                }
                3//切换
                -> {
                    map.put("name", "暂停")
                    map.put("status", "0")
                    map.put("action", "暂停")
                }
            }

            common_sensor_map(map, "name1", name, tabname, type, "value", "", "tiaoguang", "", "position", position, "number", deviceActionList[position], list_map)
        }
    }


    private fun common_sensor_map(map: MutableMap<String, Any>, name1: String, name2: Any?, tabname2: Any?, type2: Any?, value: String, s: Any?, tiaoguang: String, s2: Any?, position2: String, position3: Any?, number: String, map2: Map<*, *>, list_map: ArrayList<Map<String, Any>>) {
        map[name1] = name2!!
        map["tabname"] = tabname2!!
        map["type"] = type2!!
        map[value] = s!!
        map[tiaoguang] = s2!!
        map[position2] = position3!!
        map[number] = map2[number]!!
        //            map.put("status", deviceActionList.get(position).get("status"));
        map["dimmer"] = map2["dimmer"]!!
        map["mode"] = map2["mode"]!!
        map["temperature"] = map2["temperature"]!!
        map["speed"] = map2["speed"]!!
        map["boxName"] = map2["boxName"]!!
        //            map.put("name", deviceActionList.get(position).get("name"));
        list_map.add(map)
    }


    private fun common_step_s(tabname: String, position: Int, type: String, name: String, action1: String, action2: String, action3: String) {
        //String 左, String 左2, String 右
        for (j in 0..2) {
            val map = HashMap<String,Any>()
            when (j) {
                0//开
                -> {
                    map.put("name", action1)
                    map.put("status", "1")
                    map.put("action", action2)
                }
                1//关
                -> {
                    map.put("name", action3)
                    map.put("status", "2")
                    //                    map.put("action", "关闭");
                    map.put("action", action3)
                }
                2//切换
                -> {
                    map.put("name", "暂停")
                    map.put("status", "0")
                    map.put("action", "暂停")
                }
            }


            common_sensor_map(map, "name1", name, tabname, type, "value", "", "tiaoguang", "", "position", position, "number", deviceActionList[position], list_map)
        }
    }


    /**
     * 空调控制
     *
     * @param type
     */
    private fun air_control_list(type: String) {

        for (position in deviceActionList.indices) {
            val map = HashMap<String,Any>()
            map.put("type", deviceActionList[position]["type"]!!)
            map.put("number", deviceActionList[position]["number"]!!)
            map.put("status", deviceActionList[position]["status"]!!)
            map.put("dimmer", deviceActionList[position]["dimmer"]!!)
            map.put("mode", deviceActionList[position]["mode"]!!)
            map.put("temperature", deviceActionList[position]["temperature"]!!)
            map.put("speed", deviceActionList[position]["speed"]!!)
            map.put("boxName", deviceActionList[position]["boxName"]!!)
            when (type) {
                "A511", "A611", "A711" -> map.put("name", deviceActionList[position]["name"]!!)
                //                case "B101":
                "B201" -> {
                    map.put("name", deviceActionList[position]["name"]!!.toString() + "打开")
                    map.put("name1", deviceActionList[position]["name"]!!)
                }
                "B301" -> {
                    map.put("name", deviceActionList[position]["name"]!!.toString() + "关闭")
                    map.put("name1", deviceActionList[position]["name"]!!)
                }
            }
            map.put("boxName", deviceActionList[position]["boxName"]!!)
            list_map.add(map)
        }

        selectexcutesceneresultadapter!!.setlist(list_map)
        selectexcutesceneresultadapter!!.notifyDataSetChanged()
    }

    /**
     * 窗帘控制
     */
    private fun curtain_window() {
        //窗帘控制
        for (i in deviceActionList.indices) {

            when (deviceActionList[i]["type"]!!.toString()) {
                "1" -> common("", deviceActionList[i]["name"]!!.toString(), "1", i)
                "4" -> {
                    common("", "内纱", "4", i)
                    common("", "外纱", "4", i)
                    common("", "全部", "4", i)
                }
                "18" -> common("", deviceActionList[i]["name"]!!.toString(), "18", i)
            }
        }
    }

    /**
     * 调光灯
     */
    private fun tiaoguang_light() {
        for (i in deviceActionList.indices) {

            when (deviceActionList[i]["type"]!!.toString()) {
                "1" -> common("第" + (i + 1) + "路灯控", deviceActionList[i]["name"]!!.toString(), "1", i)
                "2" -> common("第" + (i + 1) + "路调光灯带", deviceActionList[i]["name"]!!.toString(), "2", i)
            }
        }
    }


    /**
     * 入墙式智能插座
     */
    private fun chazuo_door() {
        for (i in deviceActionList.indices) {

            when (deviceActionList[i]["type"]!!.toString()) {
                "17" -> common("", deviceActionList[i]["name"]!!.toString(), "17", i)
            }
        }
    }


    /**
     * common
     *
     * @param
     * @param
     * @param i
     */
    private fun common(tabname: String, name: String, type: String, i: Int) {//"第" + (i + 1) + content

        when (name) {
            "全部" -> common_second(tabname, name, type, "1", "0", "", i)
            "内纱" -> common_second(tabname, name, type, "4", "6", "", i)
            "外纱" -> common_second(tabname, name, type, "8", "7", "", i)
            else -> common_second(tabname, name, type, "1", "0", "3", i)
        }
    }

    /**
     * 设备开关状态status字段
     *
     * @param tabname
     * @param name
     * @param type
     * @param value2
     * @param value4
     * @param position
     */
    private fun common_second(tabname: String, name: String, type: String, value2: String, value4: String, value5: String, position: Int) {
        when (type) {
            "1" -> light_select(tabname, name, type, value2, value4, value5, position)
            else -> default_select(tabname, name, type, value2, value4, position)
        }
    }

    /**
     * 灯控选项
     *
     * @param tabname
     * @param name
     * @param type
     * @param value2
     * @param value4
     * @param value5
     * @param position
     */
    private fun light_select(tabname: String, name: String, type: String, value2: String, value4: String, value5: String, position: Int) {
        for (j in 0..2) {
            val map = HashMap<String,Any>()
            when (j) {
                0//开
                -> {
                    map.put("name", name + "打开")
                    map.put("status", value2)
                    if (type == "4") {
                        map.put("action", name + "打开")
                    } else {
                        map.put("action", "打开")
                    }
                }
                1//关
                -> {
                    map.put("name", name + "关闭")
                    map.put("status", value4)
                    //                    map.put("action", "关闭");
                    if (type == "4") {
                        map.put("action", name + "关闭")
                    } else {
                        map.put("action", "关闭")
                    }
                }
                2//切换
                -> {
                    map.put("name", name + "切换")
                    map.put("status", value5)
                    if (type == "4") {
                        map.put("action", name + "关闭")
                    } else {
                        map.put("action", "切换")
                    }
                }
            }


            if (type == "4") {
                map.put("name1", "窗帘")
            } else {
                map.put("name1", name)
            }
            map.put("tabname", tabname)
            map.put("type", type)
            map.put("value", "")
            map.put("tiaoguang", "")
            map.put("position", position)
            map.put("number", deviceActionList[position]["number"]!!)
            //            map.put("status", deviceActionList.get(position).get("status"));
            map.put("dimmer", deviceActionList[position]["dimmer"]!!)
            map.put("mode", deviceActionList[position]["mode"]!!)
            map.put("temperature", deviceActionList[position]["temperature"]!!)
            map.put("speed", deviceActionList[position]["speed"]!!)
            map.put("boxName", deviceActionList[position]["boxName"]!!)
            //            map.put("name", deviceActionList.get(position).get("name"));
            list_map.add(map)
        }
    }

    /**
     * 默认选项
     *
     * @param tabname
     * @param name
     * @param type
     * @param value2
     * @param value4
     * @param position
     */
    private fun default_select(tabname: String, name: String, type: String, value2: String, value4: String, position: Int) {
        for (j in 0..1) {
            val map = HashMap<String,Any>()
            when (j) {
                0//开
                -> {
                    map.put("name", name + "打开")
                    map.put("status", value2)
                    if (type == "4") {
                        map.put("action", name + "打开")
                    } else {
                        map.put("action", "打开")
                    }
                }
                1//关
                -> {
                    map.put("name", name + "关闭")
                    map.put("status", value4)
                    //                    map.put("action", "关闭");
                    if (type == "4") {
                        map.put("action", name + "关闭")
                    } else {
                        map.put("action", "关闭")
                    }
                }
            }

            if (type == "4") {
                map.put("name1", "窗帘")
            } else {
                map.put("name1", name)
            }
            map.put("tabname", tabname)
            map.put("type", type)
            map.put("value", "")
            map.put("tiaoguang", "")
            map.put("position", position)
            map.put("number", deviceActionList[position]["number"]!!)
            //            map.put("status", deviceActionList.get(position).get("status"));
            map.put("dimmer", deviceActionList[position]["dimmer"]!!)
            map.put("mode", deviceActionList[position]["mode"]!!)
            map.put("temperature", deviceActionList[position]["temperature"]!!)
            map.put("speed", deviceActionList[position]["speed"]!!)
            map.put("boxName", deviceActionList[position]["boxName"]!!)
            //            map.put("name", deviceActionList.get(position).get("name"));
            list_map.add(map)
        }
    }

    /**
     * 普通灯
     */
    private fun light() {
        //普通灯
        for (i in deviceActionList.indices) {
            common("第" + (i + 1) + "路灯控", deviceActionList[i]["name"]!!.toString(), "1", i)
        }
    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {

        when (panelType) {
            "A201", "A202", "A203", "A204", "A301", "A302", "A303", "A311", "A312", "A313", "A321", "A322", "A331", "B101" -> back_onclick(position, view)
            "A401"//窗帘的话
                , "A411"//
                , "A412"//
                , "A413"//
                , "A414"//
            -> back_onclick_window(position)
            "A511"//列表
            -> send_map_to_second(list_map[position] as HashMap<String, Any>)
            "A611" -> send_map_to_second(list_map[position] as HashMap<String, Any>)
            "A711" -> send_map_to_second(list_map[position] as HashMap<String, Any>)
            "A801" -> {
            }
            "A901" -> {
            }
            "A902",

            "AB01" -> {
            }
            "AB04" -> {
            }
            "AC01" -> {
            }
            "AD01" -> {
            }
            "AD02" -> {
            }
            "B001" -> {
            }
            "B201" ->
                //            case "B101":
                send_map_to_second(list_map[position] as HashMap<String, Any>)
            "B301", "B401", "B402", "B403" -> send_map_to_second(list_map[position] as HashMap<String, Any>)
        }
    }

    /**
     * 窗帘设备点击回调
     *
     * @param position
     */
    private fun back_onclick_window(position: Int) {
        val map = list_map[position]
        var index_device = 0
        for (i in deviceActionList.indices) {
            if (map["type"]!!.toString() == deviceActionList[i]["type"]) {
                index_device = i
                break
            }
        }
        when (map["type"]!!.toString()) {
            "1", "2" -> send_map_to_second(list_map[position] as HashMap<String, Any>)
            "3" -> {
            }
            "4", "18" ->
                //                init_device_upgrade_action(index_device, "status", map.get("status").toString());
                send_map_to_second(list_map[position] as HashMap<String, Any>)
            "5" -> {
            }
            "6" -> {
            }
        }
    }

    /**
     * 点击之后的返回响应
     *
     * @param position
     * @param view
     */
    private fun back_onclick(position: Int, view: View) {
        val map = list_map[position]
        when (map["type"]!!.toString()) {
            "1", "17" -> light_and_tiaogaung(position, map)
            "2" -> light_and_tiaogaung_second(position, map, view)
            "3" -> {
            }
            "4" -> {
            }
            "5" -> {
            }
            "6" -> {
            }
        }
    }

    /**
     * 针对调光灯带的打开下拉显示
     *
     * @param position
     * @param map
     * @param view
     */
    private fun light_and_tiaogaung_second(position: Int, map: Map<*, *>, view: View) {

        when (map["status"]!!.toString()) {
            "1"//打开-》添加调光值数据，
            -> {
                //                String open = (String) view.getTag();
                val open = list_map[position]["tiaoguang"] as String?
                if (open != null) {
                    when (open) {
                        "open"//调光展开
                        -> tiaoguang_open(position, map, view)
                        "close"//调光收缩
                        -> tiaoguang_close(position, map, view)
                        else -> tiaoguang_open(position, map, view)
                    }
                } else {//去执行，选择调光值

                    //                    Log.e("zhu", "value:" + list_map.get(position).get("value"));
                   // list_map[position].put("dimmer", list_map[position]["value"])
                   // var map = list_map[position]  as HashMap<String,Any>
                   // map["dimmer"] = list_map[position]["value"] !!
                    (list_map[position]  as HashMap<String,Any>)["dimmer"] = list_map[position]["value"] !!

                    //list_map[position] .put("action", "调光值:" + list_map[position]["value"]!!)//拼接调光值

                 //   (list_map[position]  as HashMap<String,Any>)["action"] = ""
                            //.put("action", "调光值:" + list_map[position]["value"]!!)//拼接调光值
                   // map["action"] = list_map[position]["action"] !!
                    (list_map[position]  as HashMap<String,Any>)["action"] = list_map[position]["value"] !!
                    //最终要选择发送的map的地方
                    send_map_to_second(list_map[position] as HashMap<String, Any>)
                }
            }
            "0"//调光关闭
            ->
                //                Log.e("zhu", "value:" + list_map.get(position).get("value"));
                //                list_map.get(position).put("dimmer", list_map.get(position).get("value"));
                send_map_to_second(list_map[position] as HashMap<String, Any>)
        }
    }

    /**
     * 最终发送位置
     *
     * @param map
     */
    private fun send_map_to_second(map: HashMap<String, Any>) {
        var intent: Intent? = null
        //        //传感器参数
        //        map.put("sensorType", sensor_map.get("sensorType"));
        //        map.put("sensorId", sensor_map.get("sensorId"));
        //        map.put("sensorName", sensor_map.get("sensorName"));
        //        map.put("sensorCondition", sensor_map.get("sensorCondition"));
        //        map.put("sensorMinValue", sensor_map.get("sensorMinValue"));
        //        map.put("sensorMaxValue", sensor_map.get("sensorMaxValue"));
        if (panelNumber != null) {
            map["panelNumber"] = panelNumber!!
        }
        if (panelMAC != null) {
            map["panelMac"] = panelMAC!!
        }
        if (gatewayMAC != null) {
            map["gatewayMac"] = gatewayMAC!!
        }
        when (map["type"]!!.toString()) {
            "3", "5", "6" -> {
                intent = Intent(
                        this@SelectiveLinkageDeviceDetailSecondActivity,
                        AirLinkageControlActivity::class.java
                )
                val map_panel = HashMap<String,Any>()
                map_panel.put("panelType", panelType!!)
                map_panel.put("panelName", panelName!!)
                map_panel.put("panelMac", panelMAC!!)
                map_panel.put("gatewayMac", gatewayMAC!!)
                map_panel.put("panelNumber", panelNumber!!)
                map_panel.put("boxNumber", boxNumber!!)
                intent.putExtra("panel_map", map_panel as Serializable)
                intent.putExtra("air_control_map", map as Serializable)
                intent.putExtra("sensor_map", sensor_map as Serializable)
                startActivity(intent)
            }
            "15", "16" -> init_intent(map)

            else -> {
                //
                AppManager.getAppManager().finishActivity_current(SelectiveLinkageActivity::class.java)
                AppManager.getAppManager().finishActivity_current(EditLinkDeviceResultActivity::class.java)
                intent = Intent(
                        this@SelectiveLinkageDeviceDetailSecondActivity,
                        EditLinkDeviceResultActivity::class.java
                )
                intent.putExtra("device_map", map as Serializable)
                intent.putExtra("sensor_map", sensor_map as Serializable)
                startActivity(intent)
                this@SelectiveLinkageDeviceDetailSecondActivity.finish()
            }
        }
    }

    private fun init_intent(air_control_map: MutableMap<*, *>) {
        init_action(air_control_map as HashMap<String, Any>)
        AppManager.getAppManager().finishActivity_current(SelectiveLinkageActivity::class.java)
        AppManager.getAppManager().finishActivity_current(SelectiveLinkageDeviceDetailSecondActivity::class.java)
        AppManager.getAppManager().finishActivity_current(EditLinkDeviceResultActivity::class.java)
        val intent = Intent(
                this@SelectiveLinkageDeviceDetailSecondActivity,
                EditLinkDeviceResultActivity::class.java)
        intent.putExtra("device_map", air_control_map as Serializable)
        intent.putExtra("sensor_map", sensor_map as Serializable)
        this@SelectiveLinkageDeviceDetailSecondActivity.finish()
        startActivity(intent)
    }

    private fun init_action(air_control_map: HashMap<String, Any>) {
        when (air_control_map["type"]!!.toString()) {
            "15" -> {
                air_control_map["action"] = "打开"
                air_control_map["status"] = "1"
            }
            "16" -> {
                air_control_map["action"] = "关闭"
                air_control_map["status"] = "0"
            }
        }//            case "17":
        //                air_control_map.put("action", "打开");
        //                air_control_map.put("status", "1");
        //                break;
    }

    /**
     * 调光灯关闭
     *
     * @param position
     * @param map
     * @param view
     */
    private fun tiaoguang_close(position: Int, map: Map<*, *>, view: View) {//关闭时，是应该关闭哪一个灯带的,告诉它这是第几路的灯带列表，
        val first = ArrayList<Map<*, *>>()
        val second = ArrayList<Map<*, *>>()
      //  list_map[position].put("tiaoguang", "open")
        (list_map[position] as HashMap<String,Any>)["tiaoguang"] = "open"
        Log.e("zhu", "position:$position,open-close:open")
        for (i in 0 until position + 1) {
            first.add(list_map[i])
        }
        for (i in position + 6 until list_map.size) {
            second.add(list_map[i])
        }
        list_map.clear()
        list_map.addAll(first as Collection<Map<String, Any>>)
        list_map.addAll(second as Collection<Map<String, Any>>)
        selectexcutesceneresultadapter!!.setlist(list_map)
        selectexcutesceneresultadapter!!.notifyDataSetChanged()
    }

    /**
     * 调光窗体展开
     *
     * @param position
     * @param map
     * @param view
     */
    private fun tiaoguang_open(position: Int, map: Map<*, *>, view: View) {
        val first = ArrayList<Map<String, Any>>()
       // list_map[position].put("tiaoguang", "close")
        (list_map[position] as HashMap<String,Any>)["tiaoguang"] = "close"
        Log.e("zhu", "position:$position,open-close:close")
        for (i in 0..4) {
            val map_value = HashMap<String,Any>()
            map_value.put("value", "" + (i + 1) * 20)
            map_value.put("name", map["name"]!!)
            common_sensor_map(map_value, "status", map["status"], map["tabname"], map["type"], "name1", map["name1"], "action", map["action"], "number", map["number"], "status", map, first)
        }

        list_map.addAll(position + 1, first as Collection<Map<String, Any>>)
        selectexcutesceneresultadapter!!.setlist(list_map)
        selectexcutesceneresultadapter!!.notifyDataSetChanged()
    }

    /**
     * 调光下面的灯的status控制
     *
     * @param position
     * @param map
     */
    private fun light_and_tiaogaung(position: Int, map: Map<*, *>) {
        var index_device = 0
        if (position % 2 == 0) {
            index_device = position / 2
        } else {
            index_device = (position - 1) / 2
        }
        //        init_device_upgrade_action(index_device, "status", map.get("status").toString());
        send_map_to_second(list_map[position] as HashMap<String, Any>)
    }

    //    /**
    //     * 更新执行设备的动作
    //     */
    //    private void init_device_upgrade_action(int position, String type, String content) {
    ////                mapdevice.put("type", doit == true ? content : deviceList.get(position).type);
    //        deviceActionList.get(position).put(type, content);
    ////        deviceActionList.add(mapdevice);
    //    }

    override fun onRefresh(pullToRefreshLayout: PullToRefreshLayout) {
        //        refresh_view.noreleasePull();
        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED)
    }

    override fun onLoadMore(pullToRefreshLayout: PullToRefreshLayout) {

    }
}
