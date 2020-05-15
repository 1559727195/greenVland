package com.massky.greenlandvland.ui.sraum.activity

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.massky.greenlandvland.R
import com.massky.greenlandvland.ui.sraum.base.BaseActivity
import com.yanzhenjie.statusview.StatusUtils
import com.yanzhenjie.statusview.StatusView
import java.io.Serializable
import java.util.HashMap

import butterknife.BindView
import kotlinx.android.synthetic.main.select_pm_one_lay.*

/**
 * Created by zhu on 2018/6/19.
 */

class SelectPmOneActivity : BaseActivity() {


    private val condition = "0"
    private var map_link  = HashMap<String,Any>()
    private var deviceType: String? = null

    override fun viewId(): Int {
        return R.layout.select_pm_one_lay
    }

    override fun onView() {
        StatusUtils.setFullToStatusBar(this)  // StatusBar.
        onEvent()
        map_link = intent.getSerializableExtra("map_link") as HashMap<String, Any>
        if (map_link == null) return
        deviceType = map_link!!["deviceType"]!!.toString()
        when (deviceType) {
            "10" -> {
                panel_scene_name_txt!!.text = "温度"
                door_open_txt!!.text = "湿度"
                door_close_txt!!.text = "PM2.5"
            }
            "AD02" -> {
                panel_scene_name_txt!!.text = "PM1.0"
                door_open_txt!!.text = "PM2.5"
                door_close_txt!!.text = "PM10"
            }
        }
        setPicture()
    }

    override fun onEvent() {
        back!!.setOnClickListener(this)
        next_step_txt!!.setOnClickListener(this)
        wendu_linear!!.setOnClickListener(this)
        shidu_rel!!.setOnClickListener(this)
        pm25_rel!!.setOnClickListener(this)
    }

    override fun onData() {

    }

    private fun setPicture() {
        project_select!!.text = map_link!!["name"]!!.toString()
    }

    override fun onClick(v: View) {

        when (v.id) {
            R.id.back -> {
                this@SelectPmOneActivity.finish()
                return
            }
            R.id.wendu_linear -> map_link!!["pm_action"] = "0"
            R.id.shidu_rel -> map_link!!["pm_action"] = "1"
            R.id.pm25_rel -> map_link!!["pm_action"] = "2"
        }
        init_intent()
    }


    private fun init_intent() {
        var intent: Intent? = null
        intent = Intent(this@SelectPmOneActivity, SelectPmTwoActivity::class.java)
        map_link!!["condition"] = condition
        map_link!!["minValue"] = ""
        map_link!!["maxValue"] = ""
        map_link!!["name1"] = map_link!!["name"] as String

        intent.putExtra("map_link", map_link as Serializable?)
        startActivity(intent)
    }
}
