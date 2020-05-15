package com.massky.greenlandvland.ui.sraum.activity

import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView

import com.massky.greenlandvland.R
import com.massky.greenlandvland.common.ToastUtil
import com.massky.greenlandvland.ui.sraum.Utils.AppManager
import com.massky.greenlandvland.ui.sraum.adapter.AgainAutoDaysSelectSceneAdapter
import com.massky.greenlandvland.ui.sraum.base.BaseActivity
import com.massky.greenlandvland.ui.sraum.bean.TimeSelectBean
import com.yanzhenjie.statusview.StatusUtils

import java.io.Serializable
import java.util.ArrayList
import java.util.Collections
import java.util.HashMap
import butterknife.BindView
import com.massky.greenlandvland.ui.sraum.activity.TimeExcuteCordinationActivity.Companion.MESSAGE_TIME_EXCUTE_ACTION
import kotlinx.android.synthetic.main.custom_define_dayscene_act.*


/**
 * Created by zhu on 2018/1/9.
 */

class CustomDefineDaySceneActivity : BaseActivity(), AdapterView.OnItemClickListener {

    internal var again_elements = arrayOf("周一", "周二", "周三", "周四", "周五", "周六", "周日")
    private var list: MutableList<TimeSelectBean>? = null
    private var list_hand_scene: MutableList<Map<*, *>>? = null
    private var againAutoSceneAdapter: AgainAutoDaysSelectSceneAdapter? = null
    private var list_select: MutableList<Map<*, *>> = ArrayList()

    override fun viewId(): Int {
        return R.layout.custom_define_dayscene_act
    }

    override fun onView() {
        StatusUtils.setFullToStatusBar(this)  // StatusBar.
        list_hand_scene = ArrayList()
        list = ArrayList()
        list_select = ArrayList()
        for (element in again_elements) {
            val map = HashMap<String,Any>()
            map.put("name", element)
            //            map.put("type", "0");
            when (element) {
                "周一" -> map.put("time", 2)
                "周二" -> map.put("time", 3)
                "周三" -> map.put("time", 4)
                "周四" -> map.put("time", 5)
                "周五" -> map.put("time", 6)
                "周六" -> map.put("time", 7)
                "周日" -> map.put("time", 1)
            }
            list_hand_scene!!.add(map)
        }
        againAutoSceneAdapter = AgainAutoDaysSelectSceneAdapter(this@CustomDefineDaySceneActivity, list_hand_scene!!)
        xListView_scan!!.adapter = againAutoSceneAdapter
        xListView_scan!!.onItemClickListener = this
    }

    override fun onEvent() {
        back!!.setOnClickListener(this)
        next_step_txt!!.setOnClickListener(this)
    }

    override fun onData() {

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.back -> this@CustomDefineDaySceneActivity.finish()
            R.id.next_step_txt//下一步
            -> next_step()
        }
    }

    private fun next_step() {
        list!!.clear()
        if (list_select.size == 0) {
            ToastUtil.showToast(this@CustomDefineDaySceneActivity, "请选择执行条件")
            return
        }
        for (map in list_select) {
            list!!.add(TimeSelectBean(map["name"] as String?, map["time"] as Int))
        }
        Collections.sort(list!!)
        val stringBuffer_name = StringBuffer()
        val stringBuffer_value = StringBuffer()
        for (i in list!!.indices) {
            if (i != list!!.size - 1) {
                stringBuffer_name.append(list!![i].name + ",")
                stringBuffer_value.append(list!![i].age.toString() + ",")
            } else {
                stringBuffer_name.append(list!![i].name)
                stringBuffer_value.append(list!![i].age)
            }
        }
        val map = HashMap<String,Any>()
        map.put("name", stringBuffer_name)
        map.put("condition", "5")
        map.put("minValue", stringBuffer_value)
        sendBroad(map)
        AppManager.getAppManager().finishActivity_current(AutoAgainSceneActivity::class.java)
        this@CustomDefineDaySceneActivity.finish()
    }

    /*
     * 通知
     * */
    private fun sendBroad(map: Map<*, *>) {
        val mIntent = Intent(MESSAGE_TIME_EXCUTE_ACTION)
        mIntent.putExtra("time_map", map as Serializable)
        sendBroadcast(mIntent)
    }


    override fun onItemClick(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
        val cb = view.findViewById<CheckBox>(R.id.scene_checkbox)
        cb.toggle()
        if (cb.isChecked) {
            list_select.add(list_hand_scene!![i])
        } else {
            for (map in list_select) {
                if (map["time"] == list_hand_scene!![i]["time"]) {
                    list_select.remove(map)
                    break
                }
            }
        }
    }
}
