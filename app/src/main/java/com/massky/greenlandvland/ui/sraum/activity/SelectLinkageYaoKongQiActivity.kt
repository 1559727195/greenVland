package com.massky.greenlandvland.ui.sraum.activity

import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.massky.greenlandvland.R
import com.massky.greenlandvland.common.SharedPreferencesUtils
import com.massky.greenlandvland.ui.sraum.AddTogenInterface.AddTogglenInterfacer
import com.massky.greenlandvland.ui.sraum.User
import com.massky.greenlandvland.ui.sraum.Util.DialogUtil
import com.massky.greenlandvland.ui.sraum.Util.MyOkHttp
import com.massky.greenlandvland.ui.sraum.Util.Mycallback
import com.massky.greenlandvland.ui.sraum.Utils.ApiHelper
import com.massky.greenlandvland.ui.sraum.adapter.SelectLinkageYaoKongQiAdapter
import com.massky.greenlandvland.ui.sraum.base.BaseActivity
import com.massky.greenlandvland.ui.sraum.view.PullToRefreshLayout
import com.yanzhenjie.statusview.StatusUtils
import com.yanzhenjie.statusview.StatusView
import java.util.ArrayList
import java.util.HashMap
import butterknife.BindView
import kotlinx.android.synthetic.main.selection_linkage_yaokongqi_lay.*
import okhttp3.Call

/**
 * Created by zhu on 2018/6/13.
 */

class SelectLinkageYaoKongQiActivity : BaseActivity(), AdapterView.OnItemClickListener, PullToRefreshLayout.OnRefreshListener {

    private var selectexcutesceneresultadapter: SelectLinkageYaoKongQiAdapter? = null
    private var list_hand_scene: MutableList<Map<*, *>> = ArrayList()
    private val mHandler = Handler()
    //    String[] again_elements = {"7", "8", "9",
    //            "10", "11", "12", "13", "14", "15", "16"};
    private val listint = ArrayList<Int>()
    private val listintwo = ArrayList<Int>()
    private val list_bool = ArrayList<Boolean>()
    private val position: Int = 0
    private var dialogUtil: DialogUtil? = null
    private val TAG = SelectLinkageYaoKongQiActivity::class.java.simpleName
    private val CONNWIFI = 101
    private var panelNumber = ""
    private var panelType = ""
    private var panelName: String? = ""
    private var sensor_map = HashMap<String,Any>()
    private var boxNumber: String? = null

    //
    //    intent =new
    //
    //    Intent(SelectInfraredForwardActivity.this, SelectControlApplianceActivity.class);
    //
    //    //        intent.putExtra("map_link", (Serializable) map);
    //    startActivity(intent);


    internal var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
        }
    }


    override fun viewId(): Int {
        return R.layout.selection_linkage_yaokongqi_lay
    }


    override fun onView() {
        dialogUtil = DialogUtil(this)
        StatusUtils.setFullToStatusBar(this)  // StatusBar.
        back!!.setOnClickListener(this)
        next_step_txt!!.setOnClickListener(this)
        maclistview_id!!.onItemClickListener = this
        refresh_view!!.setOnRefreshListener(this)
        //        refresh_view.autoRefresh();
        panelNumber = intent.getSerializableExtra("panelNumber") as String
        panelType = intent.getSerializableExtra("panelType") as String
        panelName = intent.getSerializableExtra("panelName") as String
        boxNumber = intent.getSerializableExtra("boxNumber") as String
        sensor_map = intent.getSerializableExtra("sensor_map") as HashMap<String, Any>
        if (panelName != null) project_select!!.text = panelName
        onData()
        //        initWifiConect();
    }

    override fun onEvent() {

    }


    //7-门磁，8-人体感应，9-水浸检测器，10-入墙PM2.5
    //11-紧急按钮，12-久坐报警器，13-烟雾报警器，14-天然气报警器，15-智能门锁，16-直流电阀机械手
    //    R.drawable.magnetic_door_s,
    //    R.drawable.human_induction_s, R.drawable.water_s, R.drawable.pm25_s,
    //    R.drawable.emergency_button_s
    override fun onData() {
        //        controllerNumber = (String) getIntent().getSerializableExtra("controllerNumber");
        getOtherDevices("")
        list_hand_scene = ArrayList()


        selectexcutesceneresultadapter = SelectLinkageYaoKongQiAdapter(this@SelectLinkageYaoKongQiActivity,
                list_hand_scene as List<Map<*, *>>, listint, listintwo, dialogUtil!!, object : SelectLinkageYaoKongQiAdapter.RefreshListener {

            override fun refresh() {
                getOtherDevices("")
            }
        })
        maclistview_id!!.adapter = selectexcutesceneresultadapter
        //        xListView_scan.setPullLoadEnable(false);
        //        xListView_scan.setFootViewHide();
        //        xListView_scan.setXListViewListener(this);
    }

    private fun setPicture(type: String) {
        when (type) {
            "AA02" -> {
                listint.add(R.drawable.icon_zhuwo_60)
                listintwo.add(R.drawable.icon_zhuwo_60)
            }
            "7" -> {
                listint.add(R.drawable.icon_menci_40)
                listintwo.add(R.drawable.icon_menci_40_active)
            }
            "8" -> {
                listint.add(R.drawable.icon_rentiganying_40)
                listintwo.add(R.drawable.icon_rentiganying_40_active)
            }
            "9" -> {
                listint.add(R.drawable.icon_shuijin_40)
                listintwo.add(R.drawable.icon_shuijin_40_active)
            }
            "10" -> {
                listint.add(R.drawable.icon_pm25_40)
                listintwo.add(R.drawable.icon_pm25_40_active)
            }
            "11" -> {
                listint.add(R.drawable.icon_jinjianniu_40)
                listintwo.add(R.drawable.icon_jinjianniu_40_active)
            }
            "12" -> {
                listint.add(R.drawable.icon_rucebjq_40)
                listintwo.add(R.drawable.icon_rucebjq_40_active)
            }
            "13" -> {
                listint.add(R.drawable.icon_yanwubjq_40)
                listintwo.add(R.drawable.icon_yanwubjq_40_active)
            }
            "14" -> {
                listint.add(R.drawable.icon_ranqibjq_40)
                listintwo.add(R.drawable.icon_ranqibjq_40_active)
            }
            "15" -> {
                listint.add(R.drawable.icon_zhinengmensuo_40)
                listintwo.add(R.drawable.icon_zhinengmensuo_40_active)
            }
            "16" -> {
                listint.add(R.drawable.ic_default_image)
                listintwo.add(R.drawable.ic_default_image)
            }
            "202", "206" -> {
                listint.add(R.drawable.icon_yaokongqi_40)
                listintwo.add(R.drawable.icon_yaokongqi_40)
            }
        }
    }

    /**
     * 获取门磁等第三方设备
     *
     * @param doit
     */
    private fun getOtherDevices(doit: String) {
        val mapdevice = HashMap<String, String>()
        mapdevice["token"] = SharedPreferencesUtils.getToken(this@SelectLinkageYaoKongQiActivity)
        mapdevice["controllerNumber"] = panelNumber
        MyOkHttp.postMapString(ApiHelper.sraum_getWifiAppleDeviceInfos, mapdevice, object : Mycallback(AddTogglenInterfacer {
            //刷新togglen数据
            getOtherDevices("load")
        }, this@SelectLinkageYaoKongQiActivity, dialogUtil) {
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
                list_hand_scene = ArrayList()
                listint.clear()
                listintwo.clear()
                for (i in user.deviceList.indices) {
                    val mapdevice = HashMap<String, String>()
                    mapdevice["name"] = user.deviceList[i].device_name
                    mapdevice["number"] = user.deviceList[i].number
                    mapdevice["type"] = user.deviceList[i].type
                    mapdevice["deviceId"] = user.deviceList[i].deviceId
                    list_hand_scene.add(mapdevice)
                    setPicture(user.deviceList[i].type)
                }

                selectexcutesceneresultadapter!!.setLists(list_hand_scene, listint, listintwo, sensor_map)
                selectexcutesceneresultadapter!!.notifyDataSetChanged()
                when (doit) {
                    "refresh" -> {
                    }
                    "load" -> {
                    }
                }
            }
        })
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.back -> this@SelectLinkageYaoKongQiActivity.finish()
            R.id.next_step_txt -> {
            }
            R.id.rel_scene_set -> {
            }
        }//执行某些手动场景
    }


    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {


    }


    override fun onRefresh(pullToRefreshLayout: PullToRefreshLayout) {
        getOtherDevices("refresh")
        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED)
    }

    override fun onLoadMore(pullToRefreshLayout: PullToRefreshLayout) {

    }

    override fun onResume() {
        super.onResume()
    }
}
