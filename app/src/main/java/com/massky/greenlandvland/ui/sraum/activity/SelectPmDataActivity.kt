package com.massky.greenlandvland.ui.sraum.activity

import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.massky.greenlandvland.R
import com.massky.greenlandvland.ui.sraum.Util.SharedPreferencesUtil
import com.massky.greenlandvland.ui.sraum.Utils.AppManager
import com.massky.greenlandvland.ui.sraum.base.BaseActivity
import com.wheel.Utils
import com.wheel.widget.TosAdapterView
import com.wheel.widget.TosGallery
import com.wheel.widget.WheelView
import com.yanzhenjie.statusview.StatusUtils
import com.yanzhenjie.statusview.StatusView
import java.io.Serializable
import java.util.HashMap
import butterknife.BindView
import kotlinx.android.synthetic.main.select_pm_data.*

/**
 * Created by masskywcy on 2016-11-14.
 */

class SelectPmDataActivity : BaseActivity() {
    //    @BindView(R.id.datePicker)
    //    DatePicker datePicker;
    private val yearb: Int = 0
    private val monthb: Int = 0
    private val dayb: Int = 0

    internal var mData: IntArray? = intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    internal var text_pm = ""
    private var map_link = HashMap<String,Any>()
    private var condition = "0"

    internal var mDecorView: View? = null

    internal var mStart = false

    private val mListener = object : TosAdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: TosAdapterView<*>, view: View, position: Int, id: Long) {
            formatData()
        }

        override fun onNothingSelected(parent: TosAdapterView<*>) {

        }
    }

    override fun viewId(): Int {
        return R.layout.select_pm_data
    }


    private fun formatData() {
        val pos1 = wheel1!!.selectedItemPosition
        val pos2 = wheel2!!.selectedItemPosition
        val pos3 = wheel3!!.selectedItemPosition

        if (pos1 == 0) {
            text_pm = String.format("%d%d", pos2, pos3)
            if (pos2 == 0) {
                text_pm = String.format("%d", pos3)
            } else {
                text_pm = String.format("%d%d", pos2, pos3)
            }
        } else {
            text_pm = String.format("%d%d%d", pos1, pos2, pos3)
        }
        Log.e("robin debug", "text_pm:$text_pm")
        //        mTextView.setText(text);
        //        condition = "1";
    }


    override fun onView() {
        StatusUtils.setFullToStatusBar(this)  // StatusBar.
        init_data()
    }

    override fun onEvent() {

    }

    override fun onData() {

    }

    private fun init_data() {
        init_view()
        map_link = intent.getSerializableExtra("map_link") as HashMap<String, Any>
        if (map_link == null) return
        when (map_link!!["pm_action"]!!.toString()) {
            "0" -> {
                init_common_data("温度", 0, 2, 6)
                condition = "2"
            }
            "1" -> {
                condition = "3"
                init_common_data("湿度", 0, 5, 0)
            }
            "2" -> {
                condition = "1"
                init_common_data("PM2.5", 1, 0, 0)
            }
        }
        init_select_title()
    }

    /**
     * 标题赋值
     */
    private fun init_select_title() {
        //选择PM值-project_select

        val deviceType = map_link!!["deviceType"]!!.toString()
        when (deviceType) {
            "10" -> when (map_link!!["pm_action"]!!.toString()) {
                "0" -> {
                    condition = "2"
                    project_select!!.text = "选择" + "温度" + "值"
                }
                "1" -> {
                    condition = "3"
                    project_select!!.text = "选择" + "湿度" + "值"
                }
                "2" -> {
                    condition = "1"
                    project_select!!.text = "选择" + "PM2.5" + "值"
                }
            }
            "AD02" -> when (map_link!!["pm_action"]!!.toString()) {
                "0" -> {
                    condition = "2"
                    project_select!!.text = "选择" + "PM1.0" + "值"
                }
                "1" -> {
                    condition = "1"
                    project_select!!.text = "选择" + "PM2.5" + "值"
                }
                "2" -> {
                    condition = "3"
                    project_select!!.text = "选择" + "PM10" + "值"
                }
            }
        }
    }

    private fun init_common_data(s: String, one: Int, two: Int, thee: Int) {
        project_select!!.text = s
        //        wheel1.setSelection(9);
        //        wheel2.setSelection(9);
        //        wheel3.setSelection(9);
        wheel1!!.setSelection(one)
        wheel2!!.setSelection(two)
        wheel3!!.setSelection(thee)
    }

    private fun init_view() {
        wheel1!!.isScrollCycle = true
        wheel2!!.isScrollCycle = true
        wheel3!!.isScrollCycle = true

        wheel1!!.adapter = NumberAdapter()
        wheel2!!.adapter = NumberAdapter()
        wheel3!!.adapter = NumberAdapter()

        wheel1!!.onItemSelectedListener = mListener
        wheel2!!.onItemSelectedListener = mListener
        wheel3!!.onItemSelectedListener = mListener


        //        formatData();

        mDecorView = window.decorView
        back!!.setOnClickListener(this)
        next_step_txt!!.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.back -> this@SelectPmDataActivity.finish()
            R.id.next_step_txt -> result_selectpmdata()
        }
    }

    private fun result_selectpmdata() {
        val add_condition = SharedPreferencesUtil.getData(this@SelectPmDataActivity, "add_condition", false) as Boolean
        var intent: Intent? = null
        map_link!!["condition"] = condition
        //                map_link.put("pm_condition", "0");
        var temp = ""
        when (map_link!!["pm_condition"]!!.toString()) {
            "0" -> temp = "大于等于 "
            "1" -> temp = "小于等于 "
        }
        when (map_link!!["pm_condition"]!!.toString()) {
            "0" ->

                map_link!!["maxValue"] = text_pm
            "1" ->

                map_link!!["minValue"] = text_pm
        }

        val deviceType = map_link!!["deviceType"]!!.toString()
        when (deviceType) {
            "10" -> when (map_link!!["pm_action"]!!.toString()) {
                "0" -> map_link!!["action"] = "温度 $temp$text_pm℃"
                "1" -> map_link!!["action"] = "湿度 $temp$text_pm%"
                "2" -> map_link!!["action"] = "PM2.5 $temp$text_pm"
            }
            "AD02" -> when (map_link!!["pm_action"]!!.toString()) {
                "0" -> map_link!!["action"] = "PM1.0 $temp$text_pm"
                "1" -> map_link!!["action"] = "PM2.5 $temp$text_pm"
                "2" -> map_link!!["action"] = "PM10 $temp$text_pm"
            }
        }

        if (add_condition) { //
            //                    AppManager.getAppManager().removeActivity_but_activity_cls(MainfragmentActivity.class);
            //                AppManager.getAppManager().finishActivity_current(AirLinkageControlActivity.class);
            //                AppManager.getAppManager().finishActivity_current(SelectiveLinkageDeviceDetailSecondActivity.class);
            AppManager.getAppManager().finishActivity_current(SelectSensorActivity::class.java)
            AppManager.getAppManager().finishActivity_current(EditLinkDeviceResultActivity::class.java)
            intent = Intent(this@SelectPmDataActivity, EditLinkDeviceResultActivity::class.java)
            intent.putExtra("sensor_map", map_link as Serializable?)
            startActivity(intent)
            this@SelectPmDataActivity.finish()
        } else { //
            intent = Intent(this@SelectPmDataActivity,
                    SelectiveLinkageActivity::class.java)
            intent.putExtra("link_map", map_link as Serializable?)
            startActivity(intent)
        }
    }

    private inner class NumberAdapter : BaseAdapter() {
        internal var mHeight = 50

        init {
            mHeight = Utils.pixelToDp(this@SelectPmDataActivity, mHeight.toFloat()).toInt()
        }

        override fun getCount(): Int {
            return if (null != mData) mData!!.size else 0
        }

        override fun getItem(arg0: Int): Any? {
            return null
        }

        override fun getItemId(arg0: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var convertView = convertView
            var txtView: TextView? = null

            if (null == convertView) {
                convertView = TextView(this@SelectPmDataActivity)
                convertView.layoutParams = TosGallery.LayoutParams(-1, mHeight)
                txtView = convertView
                txtView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25f)
                txtView.setTextColor(Color.GRAY)
                txtView.gravity = Gravity.CENTER
            }

            val text = mData!![position].toString()
            if (null == txtView) {
                txtView = convertView as TextView?
            }
            txtView!!.text = text
            return convertView
        }
    }
}
