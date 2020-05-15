package com.massky.greenlandvland.ui.sraum.activity

import android.content.Intent
import android.os.Handler
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.TextView
import com.massky.greenlandvland.R
import com.massky.greenlandvland.ui.sraum.AddTogenInterface.AddTogglenInterfacer
import com.massky.greenlandvland.ui.sraum.User
import com.massky.greenlandvland.ui.sraum.Util.DialogUtil
import com.massky.greenlandvland.ui.sraum.Util.MyOkHttp
import com.massky.greenlandvland.ui.sraum.Util.Mycallback
import com.massky.greenlandvland.ui.sraum.Utils.ApiHelper
import com.massky.greenlandvland.ui.sraum.Utils.AppManager
import com.massky.greenlandvland.ui.sraum.adapter.SelectSensorSingleAdapter
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
import kotlinx.android.synthetic.main.selection_sensor_lay.*
import okhttp3.Call

/**
 * Created by zhu on 2018/6/13.
 */

class SelectSensorActivity : BaseActivity(), AdapterView.OnItemClickListener, PullToRefreshLayout.OnRefreshListener {

    private var selectexcutesceneresultadapter: SelectSensorSingleAdapter? = null
    private var list_hand_scene: MutableList<Map<*, *>> = ArrayList()
    private val mHandler = Handler()
    //    String[] again_elements = {"7", "8", "9",
    //            "10", "11", "12", "13", "14", "15", "16"};
    private val listint = ArrayList<Int>()
    private val listintwo = ArrayList<Int>()
    private val list_bool = ArrayList<Boolean>()
    private var position: Int = 0
    private var dialogUtil: DialogUtil? = null
    private var type: String? = null

    override fun viewId(): Int {
        return R.layout.selection_sensor_lay
    }

    override fun onView() {
        dialogUtil = DialogUtil(this)
        StatusUtils.setFullToStatusBar(this)  // StatusBar.
        back!!.setOnClickListener(this)
        next_step_txt!!.setOnClickListener(this)
        maclistview_id!!.onItemClickListener = this
        refresh_view!!.setOnRefreshListener(this)
        rel_scene_set!!.setOnClickListener(this)
        //        refresh_view.autoRefresh();
        type = intent.getSerializableExtra("type") as String
        if (type != null) {
            when (type) {
                "100" -> {
                    hand_txt!!.visibility = View.GONE
                    hand_linear!!.visibility = View.GONE
                }
                "100||102" -> {
                    panel_scene_name_txt!!.text = "定时执行"
                    img_guan_scene!!.setImageResource(R.drawable.icon_dingshizhixing_sm)
                    hand_txt!!.text = "选择定时执行"
                }
            }//ding shi
        } else {
            hand_txt!!.visibility = View.VISIBLE
            hand_linear!!.visibility = View.VISIBLE

        }
    }

    override fun onEvent() {

    }


    //7-门磁，8-人体感应，9-水浸检测器，10-入墙PM2.5
    //11-紧急按钮，12-久坐报警器，13-烟雾报警器，14-天然气报警器，15-智能门锁，16-直流电阀机械手
    //    R.drawable.magnetic_door_s,
    //    R.drawable.human_induction_s, R.drawable.water_s, R.drawable.pm25_s,
    //    R.drawable.emergency_button_s
    override fun onData() {

        getOtherDevices("")
        list_hand_scene = ArrayList()

        selectexcutesceneresultadapter = SelectSensorSingleAdapter(this@SelectSensorActivity,
                list_hand_scene, listint, listintwo, object : SelectSensorSingleAdapter.SelectSensorListener {

            override fun selectsensor(position: Int) {
                this@SelectSensorActivity.position = position
            }
        })
        maclistview_id!!.adapter = selectexcutesceneresultadapter
        //        xListView_scan.setPullLoadEnable(false);
        //        xListView_scan.setFootViewHide();
        //        xListView_scan.setXListViewListener(this);
    }

    private fun setPicture(type: String) {
        when (type) {
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
                listint.add(R.drawable.defaultpic)
                listintwo.add(R.drawable.defaultpic)
            }
            "AD02" -> {
                listint.add(R.drawable.icon_pmmofang_40_hs)
                listintwo.add(R.drawable.icon_pmmofang_40_hs)
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
        mapdevice["token"] = SharedPreferencesUtils.getToken(this@SelectSensorActivity)
        val areaNumber = SharedPreferencesUtil.getData(this@SelectSensorActivity, "areaNumber", "") as String
        mapdevice["areaNumber"] = areaNumber
        //        mapdevice.put("boxNumber", TokenUtil.getBoxnumber(SelectSensorActivity.this));
        MyOkHttp.postMapString(ApiHelper.sraum_getLinkSensor, mapdevice, object : Mycallback(AddTogglenInterfacer {
            //刷新togglen数据
            getOtherDevices("load")
        }, this@SelectSensorActivity, dialogUtil) {
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
                    mapdevice["name"] = user.deviceList[i].name
                    mapdevice["number"] = user.deviceList[i].number
                    mapdevice["type"] = user.deviceList[i].type
                    mapdevice["boxNumber"] = user.deviceList[i].boxNumber
                    mapdevice["boxName"] = user.deviceList[i].boxName
                    //                    if (user.deviceList.get(i).type.equals("10"))
                    //                        continue;
                    list_hand_scene.add(mapdevice)
                    setPicture(user.deviceList[i].type)
                }

                selectexcutesceneresultadapter!!.setLists(list_hand_scene, listint, listintwo)
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
            R.id.back -> this@SelectSensorActivity.finish()
            R.id.next_step_txt -> {
            }
            R.id.rel_scene_set -> when (panel_scene_name_txt!!.text.toString()) {
                "定时执行"//跳转到定时执行界面
                -> startActivity(Intent(this@SelectSensorActivity, TimeExcuteCordinationActivity::class.java))
                "手动执行" -> hand_do_scene()
            }
        }//执行某些手动场景
    }

    /**
     * hand_scene
     */
    private fun hand_do_scene() {
        var intent: Intent? = null
        val map_link = HashMap<String,Any>()
        map_link.put("type", "101")
        map_link.put("deviceType", "")
        map_link.put("deviceId", "")
        map_link.put("name", "手动执行")
        map_link.put("action", "执行")
        map_link.put("condition", "")
        map_link.put("minValue", "")
        map_link.put("maxValue", "")
        map_link.put("boxName", "")
        map_link.put("name1", "手动执行")
        val add_condition = SharedPreferencesUtil.getData(this@SelectSensorActivity, "add_condition", false) as Boolean
        if (add_condition) {
            //            AppManager.getAppManager().removeActivity_but_activity_cls(MainfragmentActivity.class);
            AppManager.getAppManager().finishActivity_current(SelectSensorActivity::class.java)
            AppManager.getAppManager().finishActivity_current(EditLinkDeviceResultActivity::class.java)
            intent = Intent(this@SelectSensorActivity, EditLinkDeviceResultActivity::class.java)
            intent.putExtra("sensor_map", map_link as Serializable)
            startActivity(intent)
            this@SelectSensorActivity.finish()
        } else {
            intent = Intent(this@SelectSensorActivity,
                    SelectiveLinkageActivity::class.java)
            intent.putExtra("link_map", map_link as Serializable)
            startActivity(intent)
        }
    }


    //    private void onLoad() {
    //        xListView_scan.stopRefresh();
    //        xListView_scan.stopLoadMore();
    //        xListView_scan.setRefreshTime("刚刚");
    //    }
    //
    //    @Override
    //    public void onRefresh() {
    //        onLoad();
    //    }

    //    @Override
    //    public void onLoadMore() {
    //        mHandler.postDelayed(new Runnable() {
    //            @Override
    //            public void run() {
    //                onLoad();
    //            }
    //        }, 1000);
    //    }


    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        //        View v = parent.getChildAt(position - maclistview_id.getFirstVisiblePosition());
        //        CheckBox cb = (CheckBox) v.findViewById(R.id.checkbox);
        //        ImageView img_guan_scene = (ImageView) v.findViewById(R.id.img_guan_scene);
        //        TextView panel_scene_name_txt = (TextView) v.findViewById(R.id.panel_scene_name_txt);
        //        cb.toggle();
        //设置checkbox现在状态
        //        SelectSensorAdapter.getIsSelected().put(position, cb.isChecked());
        //        if (cb.isChecked()) {
        //            Intent intent = new Intent(SelectSensorActivity.this,  UnderWaterActivity.class);
        //            intent.putExtra("type",(Serializable) again_elements[position]);
        //            startActivityForResult(intent, REQUEST_SENSOR);
        //            img_guan_scene.setImageResource(listintwo.get(position));
        //            panel_scene_name_txt.setTextColor(getResources().getColor(R.color.gold_color));
        //
        //        } else {
        //            img_guan_scene.setImageResource(listint.get(position));
        //            panel_scene_name_txt.setTextColor(getResources().getColor(R.color.black_color));
        //        }

        //
        //        SelectSensorSingleAdapter.ViewHolderContentType viewHolder = (SelectSensorSingleAdapter.ViewHolderContentType) view.getTag();
        //        viewHolder.checkbox.toggle();// 把CheckBox的选中状态改为当前状态的反,gridview确保是单一选
        var intent: Intent? = null
        val deviceType = list_hand_scene[position]["type"] as String?
        val deviceId = list_hand_scene[position]["number"] as String?
        val name = list_hand_scene[position]["name"] as String?
        val boxName = list_hand_scene[position]["boxName"] as String?
        val map = HashMap<String,Any>()
        map.put("deviceType", deviceType!!)
        map.put("deviceId", deviceId!!)
        map.put("name", name!!)
        map.put("boxName", boxName!!)
        map.put("type", "100")
        if (deviceType == null) return
        when (deviceType) {
            "10", "AD02" -> {
                intent = Intent(this@SelectSensorActivity, SelectPmOneActivity::class.java)
                intent.putExtra("map_link", map as Serializable)
                startActivity(intent)
            }
            else -> {
                intent = Intent(this@SelectSensorActivity, UnderWaterActivity::class.java)
                intent.putExtra("map_link", map as Serializable)
                startActivityForResult(intent, REQUEST_SENSOR)
            }
        }//pm2.5
    }

    override fun onRefresh(pullToRefreshLayout: PullToRefreshLayout) {
        getOtherDevices("refresh")
        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED)
    }

    override fun onLoadMore(pullToRefreshLayout: PullToRefreshLayout) {

    }

    companion object {

        private val REQUEST_SENSOR = 101
    }
}
