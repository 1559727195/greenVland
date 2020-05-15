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
import kotlinx.android.synthetic.main.select_pm_two_lay.*

/**
 * Created by zhu on 2018/6/19.
 */

class SelectPmTwoActivity : BaseActivity() {


    private val condition = "0"
    private var map_link = HashMap<String,Any>()

    override fun viewId(): Int {
        return R.layout.select_pm_two_lay
    }

    override fun onView() {
        StatusUtils.setFullToStatusBar(this)  // StatusBar.
        onEvent()
        map_link = intent.getSerializableExtra("map_link") as HashMap<String, Any>
        if (map_link == null) return
        setPicture()
    }

    override fun onEvent() {
        back!!.setOnClickListener(this)
        next_step_txt!!.setOnClickListener(this)
        big_linear!!.setOnClickListener(this)
        small_rel!!.setOnClickListener(this)
    }

    override fun onData() {

    }

    private fun setPicture() {
        //        switch (map_link.get("pm_action").toString()) {
        //            case "0":
        //                project_select.setText("温度");
        //                break;
        //            case "1":
        //                project_select.setText("湿度");
        //                break;
        //            case "2":
        //                project_select.setText("PM2.5");
        //                break;
        //        }

        val deviceType = map_link!!["deviceType"]!!.toString()
        when (deviceType) {
            "10" -> when (map_link!!["pm_action"]!!.toString()) {
                "0" -> project_select!!.text = "温度"
                "1" -> project_select!!.text = "湿度"
                "2" -> project_select!!.text = "PM2.5"
            }
            "AD02" -> when (map_link!!["pm_action"]!!.toString()) {
                "0" -> project_select!!.text = "PM1.0"
                "1" -> project_select!!.text = "PM2.5"
                "2" -> project_select!!.text = "PM10"
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.back -> {
                this@SelectPmTwoActivity.finish()
                return
            }
            R.id.big_linear -> map_link!!["pm_condition"] = "0"
            R.id.small_rel -> map_link!!["pm_condition"] = "1"
        }
        init_intent()
    }

    private fun init_intent() {
        var intent: Intent? = null
        intent = Intent(this@SelectPmTwoActivity, SelectPmDataActivity::class.java)
        intent.putExtra("map_link", map_link as Serializable?)
        startActivity(intent)
    }
}
