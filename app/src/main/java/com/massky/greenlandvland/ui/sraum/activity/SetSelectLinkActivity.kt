package com.massky.greenlandvland.ui.sraum.activity

import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
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
import com.massky.greenlandvland.ui.sraum.base.BaseActivity
import com.massky.greenlandvland.ui.sraum.view.ClearLengthEditText
import com.massky.greenlandvland.util.IntentUtil
import com.yanzhenjie.statusview.StatusUtils
import com.yanzhenjie.statusview.StatusView
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Date
import java.util.HashMap
import butterknife.BindView
import com.bigkoo.pickerview.TimePickerView
import com.bigkoo.pickerview.listener.CustomListener
import com.massky.greenlandvland.common.SharedPreferencesUtils
import com.massky.greenlandvland.ui.MainActivity
import kotlinx.android.synthetic.main.set_select_link_lay.*
import okhttp3.Call

/**
 * Created by zhu on 2018/6/20.
 */

class SetSelectLinkActivity : BaseActivity() {

    //    @BindView(R.id.sleep_time_txt)
    //    TextView sleep_time_txt;
    //    @BindView(R.id.get_up_time_txt)
    //    TextView get_up_time_txt;
    private var hourPicker: String? = null
    private var minutePicker: String? = null
   private var time_content: String ? = null

    private var index_select: Int = 0

    private var dialogUtil: DialogUtil? = null
    private var list_condition: List<Map<*, *>> = ArrayList()
    private var list_result: List<Map<*, *>> = ArrayList()
    private var startTime = ""
    private var endTime = ""
    private var is_editlink: Boolean = false
    private var linkId = ""
    private var link_information= HashMap<String,Any>()
    private var linkName = ""
    private var type: String? = null

    private var pvCustomTime: TimePickerView? = null

    internal var handler: android.os.Handler = object : android.os.Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                0 -> start_time_txt!!.text = time_content
                1 -> end_time_txt!!.text = time_content
            }//开始
            //结束
        }
    }

    override fun viewId(): Int {
        return R.layout.set_select_link_lay
    }

    override fun onView() {
        StatusUtils.setFullToStatusBar(this)  // StatusBar.
        //        linkId = (String) getIntent().getSerializableExtra("linkId");

        init_get()
        init_data()
    }

    override fun onEvent() {

    }

    override fun onData() {

    }

    private fun init_get() {
        link_information = intent.getSerializableExtra("link_information") as HashMap<String,Any>
        //        String link_edit = (String) link_information.get("linkId");
        //        map.put("link_edit", true);
        //        map.put("linkId", list.get(position).id);
        //        map.put("linkName", list.get(position).name);
        ////                intent.putExtra("link_edit", true);
        ////                intent.putExtra("linkId", list.get(position).id);
        //        intent.putExtra("link_information", (Serializable) map);
        if (link_information != null) {//来自联动列表的编辑按钮
            //            list_result = SharedPreferencesUtil.getInfo_List(EditLinkDeviceResultActivity.this, "list_result");
            //            list_condition = SharedPreferencesUtil.getInfo_List(EditLinkDeviceResultActivity.this, "list_condition");
            //获取接口的联动列表信息，设备联动信息，sraum_deviceLinkInfo，
            //            linkId = (String) getIntent().getSerializableExtra("linkId");
            linkId = link_information!!["linkId"] as String
            linkName = link_information!!["linkName"] as String
            startTime = link_information!!["startTime"] as String
            endTime = link_information!!["endTime"] as String
            start_time_txt!!.text = startTime
            end_time_txt!!.text = endTime
            link_name_edit!!.setText(linkName)
        } else {
            linkId = SharedPreferencesUtil.getData(this@SetSelectLinkActivity, "linkId", "") as String
        }
    }

    private fun init_data() {
        back!!.setOnClickListener(this)
        next_step_txt!!.setOnClickListener(this)
        initCustomTimePicker()
        sleep_time_rel!!.setOnClickListener(this)
        get_up_rel!!.setOnClickListener(this)
        dialogUtil = DialogUtil(this)
        list_result = SharedPreferencesUtil.getInfo_List(this@SetSelectLinkActivity, "list_result")
        list_condition = SharedPreferencesUtil.getInfo_List(this@SetSelectLinkActivity, "list_condition")
        type = list_condition[0]["type"] as String?
        when (type) {
            "100"//自动执行条件
            -> {
                time_select_linear!!.visibility = View.VISIBLE
                startTime = start_time_txt!!.text.toString()
                endTime = end_time_txt!!.text.toString()
            }
            "101"//手动执行条件
            -> time_select_linear!!.visibility = View.GONE
            "102"//定时场景
            -> {
                time_select_linear!!.visibility = View.GONE
                startTime = list_condition[0]["startTime"] as String
                endTime = list_condition[0]["endTime"] as String
            }
        }
        is_editlink = SharedPreferencesUtil.getData(this@SetSelectLinkActivity, "editlink", false) as Boolean
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.back -> this@SetSelectLinkActivity.finish()
            R.id.next_step_txt//场景设置
            -> {
                val content = link_name_edit!!.text.toString()
                if (content == null || content == "") {
                    ToastUtil.showToast(this@SetSelectLinkActivity, "请输入联动名称")
                } else {
                    if (is_editlink) {//说明联动要更新了，编辑更新
                        when (type) {
                            "100"//自动执行条件
                            -> {
                                startTime = start_time_txt!!.text.toString()
                                endTime = end_time_txt!!.text.toString()
                                getData(is_editlink, content, linkId, ApiHelper.sraum_updateDeviceLink)
                            }
                            "102" -> getData(is_editlink, content, linkId, ApiHelper.sraum_updateDeviceLink)
                            "101"//手动执行条件
                            -> set_Hand_Data(is_editlink, content, linkId, ApiHelper.sraum_editManuallyScene
                            )
                        }
                    } else {//直接设置联动

                        when (type) {
                            "100"//自动执行条件
                            -> {
                                startTime = start_time_txt!!.text.toString()
                                endTime = end_time_txt!!.text.toString()
                                getData(is_editlink, content, linkId, ApiHelper.sraum_setDeviceLink)
                            }
                            "102" -> getData(is_editlink, content, linkId, ApiHelper.sraum_setDeviceLink)

                            "101"//手动执行条件
                            -> set_Hand_Data(is_editlink, content, linkId, ApiHelper.sraum_addManuallyScene
                            )
                        }
                    }
                    //                    SharedPreferencesUtil.saveInfo_List(SetSelectLinkActivity.this, "list_result", new ArrayList<Map>());
                    //                    SharedPreferencesUtil.saveInfo_List(SetSelectLinkActivity.this, "list_condition", new ArrayList<Map>());
                    //                }
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
        }
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
        pvCustomTime = TimePickerView.Builder(this, object : TimePickerView.OnTimeSelectListener {
            override fun onTimeSelect(date: Date, v: View) {//选中事件回调
                getTime(date)
                Log.e("robin debug", "getTime(date):" + getTime(date))
                hourPicker = date.hours.toString()
                minutePicker = date.minutes.toString()
                when (minutePicker.toString().length) {
                    1 -> minutePicker = "0" + minutePicker!!
                }

                when (hourPicker.toString().length) {
                    1 -> hourPicker = "0" + hourPicker!!
                }

                time_content = "$hourPicker:$minutePicker"
                handler.sendEmptyMessage(index_select)
                //                start_scenetime.setText(hourPicker + ":" + minutePicker);
            }
        })
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.pickerview_custom_time, object : CustomListener {
                  override  fun customLayout(v: View) {
                        val tvSubmit = v.findViewById<View>(R.id.ok_bt) as ImageView
                        val ivCancel = v.findViewById<View>(R.id.finish_bt) as ImageView
                        tvSubmit.setOnClickListener {
                            pvCustomTime!!.returnData()
                            pvCustomTime!!.dismiss()
                        }

                        ivCancel.setOnClickListener { pvCustomTime!!.dismiss() }
                    }
                })
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

    /**
     * 添加手动场景
     *
     * @param flag
     * @param linkName
     * @param linkId
     * @param apiname
     */
    private fun set_Hand_Data(flag: Boolean, linkName: String, linkId: String, apiname: String) {
        val map = HashMap<String, Any>()
        val areaNumber = SharedPreferencesUtil.getData(this@SetSelectLinkActivity, "areaNumber", "") as String
        map["token"] =SharedPreferencesUtils.getToken(this@SetSelectLinkActivity)
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
        for (i in list_result.indices) {
            val map_device = HashMap<String,Any>()
            map_device.put("type", list_result[i]["type"]!!)
            map_device.put("number", list_result[i]["number"]!!)
            map_device.put("status", list_result[i]["status"]!!)
            map_device.put("dimmer", list_result[i]["dimmer"]!!)
            map_device.put("mode", list_result[i]["mode"]!!)
            map_device.put("temperature", list_result[i]["temperature"]!!)
            map_device.put("speed", list_result[i]["speed"]!!)
            map_device.put("panelMac", if (list_result[i]["panelMac"] == null) "" else list_result[i]["panelMac"]!!)
            map_device.put("gatewayMac", if (list_result[i]["gatewayMac"] == null) "" else list_result[i]["gatewayMac"]!!)
            list.add(map_device)
        }
        map["deviceList"] = list
        dialogUtil!!.loadDialog()
        MyOkHttp.postMapObject(apiname, map,
                object : Mycallback(AddTogglenInterfacer { getData(flag, linkName, linkId, apiname) }, this@SetSelectLinkActivity, dialogUtil) {


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
                            IntentUtil.startActivity(this@SetSelectLinkActivity, AssociatedpanelActivity::class.java, bundle1)

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
                        ToastUtil.showToast(this@SetSelectLinkActivity, "添加失败（硬件\n" + "返回）")
                    }

                    override fun fourCode() {
                        common()
                        ToastUtil.showToast(this@SetSelectLinkActivity, "设备列表信息有误")
                    }

                    override fun threeCode() {
                        common()
                        ToastUtil.showToast(this@SetSelectLinkActivity, "sceneName 已存在")
                    }

                    override fun wrongBoxnumber() {
                        common()
                        ToastUtil.showToast(this@SetSelectLinkActivity, "areaNumber\n" + "不存在")
                    }
                })
    }


    /**
     * 添加自动场景
     *
     * @param flag
     * @param linkName
     * @param linkId
     * @param apiname
     */
    private fun getData(flag: Boolean, linkName: String, linkId: String, apiname: String) {
        val map = HashMap<String, Any>()
        val areaNumber = SharedPreferencesUtil.getData(this@SetSelectLinkActivity, "areaNumber", "") as String
        map["token"] = SharedPreferencesUtils.getToken(this@SetSelectLinkActivity)
        map["areaNumber"] = areaNumber
        map["deviceId"] = list_condition[0]["deviceId"]!!
        map["deviceType"] = list_condition[0]["deviceType"]!!
        map["linkName"] = linkName
        map["type"] = list_condition[0]["type"]!!

        when (list_condition[0]["deviceType"]!!.toString()) {
            "AD02" -> map["deviceType"] = "102"
            else -> {
            }
        }
        map["condition"] = list_condition[0]["condition"]!!
        map["minValue"] = list_condition[0]["minValue"]!!
        map["maxValue"] = list_condition[0]["maxValue"]!!
        map["startTime"] = startTime
        map["endTime"] = endTime
        if (flag) {
            map["linkId"] = linkId
        } else {

        }
        val list = ArrayList<Map<*, *>>()
        for (i in list_result.indices) {
            val map_device = HashMap<String,Any>()
            map_device.put("type", list_result[i]["type"]!!)
            map_device.put("number", list_result[i]["number"]!!)
            map_device.put("status", list_result[i]["status"]!!)
            map_device.put("dimmer", list_result[i]["dimmer"]!!)
            map_device.put("mode", list_result[i]["mode"]!!)
            map_device.put("temperature", list_result[i]["temperature"]!!)
            map_device.put("speed", list_result[i]["speed"]!!)
            list.add(map_device)
        }
        map["deviceList"] = list

        dialogUtil!!.loadDialog()
        MyOkHttp.postMapObject(apiname, map,
                object : Mycallback(AddTogglenInterfacer { getData(flag, linkName, linkId, apiname) }, this@SetSelectLinkActivity, dialogUtil) {


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
                        ToastUtil.showToast(this@SetSelectLinkActivity, "areaNumber 错\n" + "误")
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
                    }

                    override fun threeCode() {
                        common()
                        ToastUtil.showToast(this@SetSelectLinkActivity, "deviceId 错误")
                    }

                    override fun wrongBoxnumber() {
                        common()
                    }
                })
    }

    /**
     * 共同执行的代码
     */
    private fun common() {
        AppManager.getAppManager().removeActivity_but_activity_cls(MainActivity::class.java)
        //        Intent intent = new Intent(SetSelectLinkActivity.this, LinkageListActivity.class);
        //        startActivity(intent);
        this@SetSelectLinkActivity.finish()
        SharedPreferencesUtil.saveData(this@SetSelectLinkActivity, "linkId", "")
        SharedPreferencesUtil.saveInfo_List(this@SetSelectLinkActivity, "list_result", ArrayList())
        SharedPreferencesUtil.saveInfo_List(this@SetSelectLinkActivity, "list_condition", ArrayList())
        SharedPreferencesUtil.saveData(this@SetSelectLinkActivity, "editlink", false)
        SharedPreferencesUtil.saveInfo_List(this@SetSelectLinkActivity, "link_information_list", ArrayList())
    }
}
