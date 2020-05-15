package com.massky.greenlandvland.ui.sraum.activity

import android.content.Intent
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView

import com.massky.greenlandvland.R
import com.massky.greenlandvland.ui.sraum.Util.SharedPreferencesUtil
import com.massky.greenlandvland.ui.sraum.Utils.AppManager
import com.massky.greenlandvland.ui.sraum.base.BaseActivity
import com.yanzhenjie.statusview.StatusUtils
import com.yanzhenjie.statusview.StatusView

import java.io.Serializable
import java.util.HashMap

import butterknife.BindView
import kotlinx.android.synthetic.main.under_water_lay.*

/**
 * Created by zhu on 2018/6/19.
 */

class UnderWaterActivity : BaseActivity() {

    private var condition = "0"
    private var map_link= HashMap<String,Any>()

    override fun viewId(): Int {
        return R.layout.under_water_lay
    }

    override fun onView() {
        StatusUtils.setFullToStatusBar(this)  // StatusBar.
        back!!.setOnClickListener(this)
        next_step_txt!!.setOnClickListener(this)
        rel_scene_set!!.setOnClickListener(this)
        //        onEvent();
        map_link = intent.getSerializableExtra("map_link") as HashMap<String, Any>
        if (map_link == null) return
        setPicture(map_link!!["deviceType"]!!.toString())
    }

    override fun onEvent() {
        rel_fangdao_open!!.setOnClickListener(this)
        rel_fangdao_close!!.setOnClickListener(this)
        rel_humancheck!!.setOnClickListener(this)
        rel_jiuzuo!!.setOnClickListener(this)
        rel_gas!!.setOnClickListener(this)
        rel_smoke!!.setOnClickListener(this)
        rel_emergcybtn!!.setOnClickListener(this)
    }

    override fun onData() {

    }

    //7-门磁，8-人体感应，9-水浸检测器，10-入墙PM2.5
    //11-紧急按钮，12-久坐报警器，13-烟雾报警器，14-天然气报警器，15-智能门锁，16-直流电阀机械手
    private fun setPicture(type: String) {
        when (type) {
            "7" ->
                //                project_select.setText(map_link.get("name").toString());// project_select.setText(map_link.get("name").toString());
                fangdao_linear!!.visibility = View.VISIBLE
            "8" -> {
                humancheck_linear!!.visibility = View.VISIBLE
                //                project_select.setText("人体检测");
                humancheck_txt!!.text = "有人经过"
            }
            "9" -> {
                humancheck_linear!!.visibility = View.VISIBLE
                //                project_select.setText("主卧水浸");
                humancheck_txt!!.text = "报警"
            }
            //            case "10":
            //
            //                break;
            "11" -> {
                humancheck_linear!!.visibility = View.VISIBLE
                //                project_select.setText("紧急按钮");
                humancheck_txt!!.text = "按下"
            }
            "12" -> {
                humancheck_linear!!.visibility = View.VISIBLE
                //                project_select.setText("如厕检测");
                humancheck_txt!!.text = "报警"
            }
            "13" -> {
                humancheck_linear!!.visibility = View.VISIBLE
                //                project_select.setText("烟雾检测");
                humancheck_txt!!.text = "报警"
            }
            "14" -> {
                humancheck_linear!!.visibility = View.VISIBLE
                //                project_select.setText("天然气检测");
                humancheck_txt!!.text = "报警"
            }
            "15" -> {
                //                project_select.setText("智能门锁");
                fangdao_linear!!.visibility = View.VISIBLE
                rel_fangdao_close!!.visibility = View.GONE
            }
            "16" -> {
            }
        }
        project_select!!.text = map_link!!["name"]!!.toString()
    }

    override fun onClick(v: View) {

        when (v.id) {
            R.id.back -> this@UnderWaterActivity.finish()
            R.id.next_step_txt -> {
            }
            //            case R.id.rel_scene_set:
            //                checkbox.toggle();
            //                if (checkbox.isChecked()) {
            //                    panel_scene_name_txt.setTextColor(getResources().getColor(R.color.gold_color));
            //                } else {
            //                    panel_scene_name_txt.setTextColor(getResources().getColor(R.color.black_color));
            //                }
            //                break;
            R.id.rel_fangdao_open -> {
                condition = "1"
                //                checkbox_fangdao_open.setChecked(true);
                //                checkbox_fangdao_close.setChecked(false);
                //                door_open_txt.setTextColor(getResources().getColor(R.color.gold_color));
                //                door_close_txt.setTextColor(getResources().getColor(R.color.black_color));
                init_intent()
            }
            R.id.rel_fangdao_close -> {
                condition = "0"
                //                checkbox_fangdao_open.setChecked(false);
                //                checkbox_fangdao_close.setChecked(true);
                //                door_close_txt.setTextColor(getResources().getColor(R.color.gold_color));
                //                door_open_txt.setTextColor(getResources().getColor(R.color.black_color));
                init_intent()
            }

            R.id.rel_humancheck -> {
                //                checkbox_humancheck.toggle();
                //                if (checkbox_humancheck.isChecked()) {
                //                    condition = "1";
                //                    humancheck_txt.setTextColor(getResources().getColor(R.color.gold_color));
                //                } else {
                //                    condition = "0";
                //                    humancheck_txt.setTextColor(getResources().getColor(R.color.black_color));
                //                }
                condition = "1"
                init_intent()
            }
        }//                map_link.put("condition", condition);
        //                map_link.put("minValue", "");
        //                map_link.put("maxValue", "");
        //                Intent intent = new Intent(UnderWaterActivity.this,
        //                        SelectiveLinkageActivity.class);
        //                intent.putExtra("link_map", (Serializable) map_link);
        //                startActivity(intent);
        //            case R.id.rel_jiuzuo:
        //                checkbox_jiuzuo.toggle();
        //                if (checkbox_jiuzuo.isChecked()) {
        //                    jiuzuo_txt.setTextColor(getResources().getColor(R.color.gold_color));
        //                } else {
        //                    jiuzuo_txt.setTextColor(getResources().getColor(R.color.black_color));
        //                }
        //                break;
        //            case R.id.rel_gas:
        //                checkbox_gas.toggle();
        //                if (checkbox_gas.isChecked()) {
        //                    gas_txt.setTextColor(getResources().getColor(R.color.gold_color));
        //                } else {
        //                    gas_txt.setTextColor(getResources().getColor(R.color.black_color));
        //                }
        //                break;
        //            case R.id.rel_smoke:
        //                checkbox_smoke.toggle();
        //                if (checkbox_smoke.isChecked()) {
        //                    smoke_txt.setTextColor(getResources().getColor(R.color.gold_color));
        //                } else {
        //                    smoke_txt.setTextColor(getResources().getColor(R.color.black_color));
        //                }
        //                break;
        //            case R.id.rel_emergcybtn:
        //                checkbox_emergcybtn.toggle();
        //                if (checkbox_emergcybtn.isChecked()) {
        //                    emergcybtn_txt.setTextColor(getResources().getColor(R.color.gold_color));
        //                } else {
        //                    emergcybtn_txt.setTextColor(getResources().getColor(R.color.black_color));
        //                }
        //                break;
    }

    private fun init_intent() {
        var intent: Intent? = null
        setAction(map_link!!["deviceType"]!!.toString(), condition)
        map_link!!["condition"] = condition
        map_link!!["minValue"] = ""
        map_link!!["maxValue"] = ""
        map_link!!["name1"] = map_link!!["name"]!!

        val add_condition = SharedPreferencesUtil.getData(this@UnderWaterActivity, "add_condition", false) as Boolean
        if (add_condition) {
            //            AppManager.getAppManager().removeActivity_but_activity_cls(MainfragmentActivity.class);
            AppManager.getAppManager().finishActivity_current(SelectSensorActivity::class.java)
            AppManager.getAppManager().finishActivity_current(EditLinkDeviceResultActivity::class.java)
            intent = Intent(this@UnderWaterActivity, EditLinkDeviceResultActivity::class.java)
            intent.putExtra("sensor_map", map_link as Serializable?)
            startActivity(intent)
            this@UnderWaterActivity.finish()
        } else {
            intent = Intent(this@UnderWaterActivity,
                    SelectiveLinkageActivity::class.java)
            intent.putExtra("link_map", map_link as Serializable?)
            startActivity(intent)
        }
    }

    //7-门磁，8-人体感应，9-水浸检测器，10-入墙PM2.5
    //11-紧急按钮，12-久坐报警器，13-烟雾报警器，14-天然气报警器，15-智能门锁，16-直流电阀机械手
    private fun setAction(type: String, action: String): Map<*, *> {
        when (type) {
            "7" -> if (action == "1") {
                map_link!!["action"] = "打开"
            } else {
                map_link!!["action"] = "闭合"
            }
            "8" -> if (action == "1") {
                map_link!!["action"] = "有人经过"
            }
            "9" ->

                if (action == "1") {
                    map_link!!["action"] = "报警"
                }
            "10" -> {
            }
            "11" -> if (action == "1") {
                map_link!!["action"] = "按下"
            }
            "12" -> if (action == "1") {
                map_link!!["action"] = "报警"
            }
            "13" -> if (action == "1") {
                map_link!!["action"] = "报警"
            }
            "14" -> if (action == "1") {
                map_link!!["action"] = "报警"
            }
            "15" -> if (action == "1") {
                map_link!!["action"] = "打开"
            } else {
                map_link!!["action"] = "闭合"
            }
            "16" -> {
            }
        }
        return map_link
    }
}
