package com.massky.greenlandvland.ui.sraum.activity

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.TextView
import com.massky.greenlandvland.R
import com.massky.greenlandvland.ui.sraum.Utils.AppManager
import com.massky.greenlandvland.ui.sraum.base.BaseActivity
import com.yanzhenjie.statusview.StatusUtils
import com.yanzhenjie.statusview.StatusView
import java.io.Serializable
import java.util.HashMap
import butterknife.BindView
import kotlinx.android.synthetic.main.air_linkage_control_act.*

/**
 * Created by zhu on 2018/6/6.
 */

class AirLinkageControlActivity : BaseActivity(), SeekBar.OnSeekBarChangeListener {



    /**
     * 空调
     *
     * @return
     */



    private var map_panel = HashMap<String,Any>()
    private var type: String? = null
    private var air_control_map = HashMap<String,Any>()
    private var sensor_map = HashMap<String,Any>()
    private var tempture:Int = 0
    /**
     * 空调
     *
     * @return
     */
    override fun viewId(): Int {
        return R.layout.air_linkage_control_act
    }

    override fun onView() {
        // radio_one_model.setText("gdfklgmdfdfg");

        StatusUtils.setFullToStatusBar(this)  // StatusBar.
        //        String type = (String) getIntent().getSerializableExtra("type");
        air_control_map = intent.getSerializableExtra("air_control_map") as HashMap<String,Any>


        //        map_panel.put("panelType", panelType);
        //        map_panel.put("panelName", panelName);
        //        map_panel.put("panelMAC", panelMAC);
        //        map_panel.put("gatewayMAC", gatewayMAC);
        //
        //
        //        intent.putExtra("air_control_map", (Serializable) list.get(0));
        //        intent.putExtra("panel_map", (Serializable) map_panel);

        map_panel = intent.getSerializableExtra("panel_map") as HashMap<String,Any>
        //

        type = air_control_map["type"]!!.toString()
        when (type) {
            "3" -> mode_linear!!.visibility = View.VISIBLE
            "5" -> mode_linear!!.visibility = View.GONE
            "6" -> {
                speed_linear!!.visibility = View.GONE
                view_speed!!.visibility = View.GONE
            }
        }
        project_select!!.text = air_control_map["name"]!!.toString()
        air_control_map["name1"] = air_control_map["name"]!!
        air_control_map["panelName"] = map_panel!!["panelName"]!!
        //intent.putExtra("sensor_map", (Serializable)  sensor_map);
        sensor_map = intent.getSerializableExtra("sensor_map") as HashMap<String,Any>
        back!!.setOnClickListener(this)

        onEvent()
        next_step_txt!!.setOnClickListener(this)
        tmp_txt!!.text = air_control_map["temperature"]!!.toString()
        //
        //        getData(true);
        init_action_select()
    }

    override fun onEvent() {
        air_control_radio_model()
        air_control_speed()
        air_control_radio_open_close()
        air_control_tmp_del!!.setOnClickListener(this)
        air_control_tmp_add!!.setOnClickListener(this)
    }

    override fun onData() {

    }


    /**
     * 初始化空调动作
     */
    private fun init_action_select() {

        //温度选择
        var tempture: String = air_control_map["temperature"] as String
        if (Integer.parseInt(tempture) < 16) {
            tempture = 16.toString() + ""
        }
        tmp_txt!!.text = tempture + ""

        //状态，在线，离线
        val status = air_control_map["status"] as String?
        when (status) {
            "1" -> air_control_radio_open_close!!.check(R.id.radio_status_one)
            "0" -> air_control_radio_open_close!!.check(R.id.radio_status_two)
        }

        speed()
        when (type) {
            "3" -> air_mode()
            "6" -> dinuan_mode()
        }
    }

    private fun speed() {
        val speed = air_control_map["speed"] as String?
        when (speed) {
            "1" -> air_control_speed!!.check(R.id.radio_one_speed)
            "2" -> air_control_speed!!.check(R.id.radio_two_speed)
            "3" -> air_control_speed!!.check(R.id.radio_three_speed)
            "6" -> air_control_speed!!.check(R.id.radio_four_speed)
            else -> air_control_speed!!.check(R.id.radio_four_speed)
        }
    }

    private fun air_mode() {
        val model = air_control_map["mode"] as String?
        //

        when (model) {
            "1" -> air_control_radio_model!!.check(R.id.radio_one_model)
            "2" -> air_control_radio_model!!.check(R.id.radio_two_model)
            "3" -> air_control_radio_model!!.check(R.id.radio_three_model)
            "4" -> air_control_radio_model!!.check(R.id.radio_four_model)
            else -> air_control_radio_model!!.check(R.id.radio_four_model)
        }
    }


    private fun dinuan_mode() {
        val model = air_control_map["mode"] as String?
        radio_one_model!!.text = "加热"
        radio_two_model!!.text = "睡眠"
        radio_three_model!!.text = "外出"
        radio_four_model!!.visibility = View.GONE
        when (model) {
            "1" -> air_control_radio_model!!.check(R.id.radio_one_model)
            "2" -> air_control_radio_model!!.check(R.id.radio_two_model)
            "3" -> air_control_radio_model!!.check(R.id.radio_three_model)
        }
    }


    /**
     * 风速
     */
    private fun air_control_speed() {
        /**
         * 风速
         */
        for (i in 0 until air_control_speed!!.childCount) {
            val view = air_control_speed!!.getChildAt(i)
            view.tag = i
            view.setOnClickListener {
                val position = view.tag as Int
                when (position) {
                    0 -> common_doit("speed", "1")
                    1 -> common_doit("speed", "2")
                    2 -> common_doit("speed", "3")
                    3 -> common_doit("speed", "6")
                }
            }
        }
    }

    /**
     * 开关
     */
    private fun air_control_radio_open_close() {
        /**
         * 开关
         */
        for (i in 0 until air_control_radio_open_close!!.childCount) {
            val view = air_control_radio_open_close!!.getChildAt(i)
            view.tag = i
            view.setOnClickListener {
                val position = view.tag as Int
                when (position) {
                    0 -> common_doit("status", "1")
                    1 -> common_doit("status", "0")
                }
            }
        }
    }

    /**
     * 公共项
     *
     * @param status
     * @param value
     */
    private fun common_doit(status: String, value: String) {
        air_control_map[status] = value
    }


    /**
     * 模式
     */
    private fun air_control_radio_model() {
        /**
         * 模式
         */
        for (i in 0 until air_control_radio_model!!.childCount) {
            val view = air_control_radio_model!!.getChildAt(i)
            view.tag = i
            view.setOnClickListener {
                val position = view.tag as Int
                when (position) {
                    0 -> common_doit("mode", "1")
                    1 -> common_doit("mode", "2")
                    2 -> common_doit("mode", "3")
                    3 -> common_doit("mode", "4")
                }
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.back -> this@AirLinkageControlActivity.finish()
            R.id.next_step_txt -> {
                init_action()
                AppManager.getAppManager().finishActivity_current(SelectiveLinkageActivity::class.java)
                AppManager.getAppManager().finishActivity_current(SelectiveLinkageDeviceDetailSecondActivity::class.java)
                AppManager.getAppManager().finishActivity_current(EditLinkDeviceResultActivity::class.java)
                val intent = Intent(
                        this@AirLinkageControlActivity,
                        EditLinkDeviceResultActivity::class.java)
                //        map_panel.put("panelType", panelType);
                //        map_panel.put("panelName", panelName);
                //        map_panel.put("panelMAC", panelMAC);
                //        map_panel.put("gatewayMAC", gatewayMAC);
                //

                if (map_panel != null) {
                    val gatewayMAC = map_panel!!["gatewayMac"]!!.toString()
                    val panelMAC = map_panel!!["panelMac"]!!.toString()
                    val panelNumber = map_panel!!["panelNumber"]!!.toString()
                    val boxNumber = map_panel!!["boxNumber"]!!.toString()
                    //         map_panel.put("panelNumber", panelNumber);
                    air_control_map["gatewayMac"] = gatewayMAC
                    air_control_map["panelMac"] = panelMAC
                    air_control_map["panelNumber"] = panelNumber
                    //           map_panel.put("boxNumber", boxNumber);
                    air_control_map["boxNumber"] = boxNumber
                }

                intent.putExtra("device_map", air_control_map as Serializable)

                intent.putExtra("sensor_map", sensor_map as Serializable)
                this@AirLinkageControlActivity.finish()
                startActivity(intent)
            }
            R.id.air_control_tmp_add -> {
                 tempture = Integer.parseInt(air_control_map["temperature"]!!.toString())
                if (tempture!! < 16) {
                    tempture = 16
                }
                if (tempture!! < 30)
                    tempture++
                tmp_txt!!.text = tempture.toString() + ""
                common_doit("temperature", "" + tempture)
            }
            R.id.air_control_tmp_del -> {
                tempture = Integer.parseInt(air_control_map["temperature"]!!.toString())
                if (tempture!! > 16)
                    tempture--
                tmp_txt!!.text = tempture.toString() + ""
                common_doit("temperature", "" + tempture)
            }
        }
    }

    /**
     * 初始化空调动作
     */
    private fun init_action() {
        val status = air_control_map["status"] as String?
        var temp = StringBuffer()
        when (status) {
            "1" -> temp = onLine()
            "0" -> temp.append("关闭")
        }
        common_doit("action", temp.toString())
    }

    private fun onLine(): StringBuffer {
        val temp = StringBuffer()
        val speed = air_control_map["speed"] as String?
        when (speed) {
            "1" -> temp.append("低风")
            "2" -> temp.append("中风")
            "3" -> temp.append("高风")
            "6" -> temp.append("自动")
            else -> temp.append("自动")
        }
        val temperature = air_control_map["temperature"] as String?

        temp.append("  $temperature℃")
        val type = air_control_map["type"]!!.toString()
        when (type) {
            "3" -> common_mode(temp)
            "6" -> common_mode_dinuan(temp)
        }
        return temp
    }

    private fun common_mode(temp: StringBuffer) {
        val model = air_control_map["mode"] as String?
        //

        when (model) {
            "1" -> temp.append("  " + "制冷")
            "2" -> temp.append("  " + "制热")
            "3" -> temp.append("  " + "除湿")
            "4" -> temp.append("  " + "自动")
            else -> temp.append("自动")
        }
    }


    private fun common_mode_dinuan(temp: StringBuffer) {
        val model = air_control_map["mode"] as String?
        //

        when (model) {
            "1" -> temp.append("  " + "加热")
            "2" -> temp.append("  " + "睡眠")
            "3" -> temp.append("  " + "外出")
        }
    }


    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        val position_index = ""
        when (seekBar.id) {

        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {

    }
}
