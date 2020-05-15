package com.massky.greenlandvland.ui.sraum.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.massky.greenlandvland.R
import com.massky.greenlandvland.common.ToastUtil
import com.massky.greenlandvland.ui.sraum.Util.SharedPreferencesUtil
import com.massky.greenlandvland.ui.sraum.Utils.AppManager
import com.massky.greenlandvland.ui.sraum.base.BaseActivity
import com.yanzhenjie.statusview.StatusUtils
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.HashMap

import butterknife.BindView
import com.bigkoo.pickerview.TimePickerView
import com.bigkoo.pickerview.listener.CustomListener

/**
 * Created by zhu on 2018/1/9.
 */

class TimeExcuteCordinationActivity : BaseActivity() {
    @BindView(R.id.back)
    internal var back: ImageView? = null
    @BindView(R.id.next_step_txt)
    internal var next_step_txt: TextView? = null
    @BindView(R.id.again_time_exeute_set)
    internal var again_time_exeute_set: RelativeLayout? = null
    @BindView(R.id.rel_scene_set)
    internal var rel_scene_set: RelativeLayout? = null
    @BindView(R.id.root_layout)
    internal var root_layout: LinearLayout? = null
    private var pvCustomTime: TimePickerView? = null
    @BindView(R.id.start_scenetime)
    internal var start_scenetime: TextView? = null
    private var time_map = HashMap<String,Any>()
    @BindView(R.id.again_time_exeute)
    internal var again_time_exeute: TextView? = null

    //for receive customer msg from jpush server
    private var mMessageReceiver: MessageReceiver? = null

    override fun viewId(): Int {
        return R.layout.timeexecute_cordination_act
    }

    override fun onView() {
        StatusUtils.setFullToStatusBar(this)  // StatusBar.
        initCustomTimePicker()
        registerMessageReceiver()
    }

    fun registerMessageReceiver() {
        mMessageReceiver = MessageReceiver()
        val filter = IntentFilter()
        filter.priority = IntentFilter.SYSTEM_HIGH_PRIORITY
        filter.addAction(MESSAGE_TIME_EXCUTE_ACTION)
        registerReceiver(mMessageReceiver, filter)
    }

    inner class MessageReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {

            if (MESSAGE_TIME_EXCUTE_ACTION == intent.action) {

                //                Map map = new HashMap();
                //                map.put("name",stringBuffer_name);
                //                map.put("condition","5");
                //                map.put("minValue",stringBuffer_value);
                //                sendBroad(map);
                //                break;
                time_map = intent.getSerializableExtra("time_map") as HashMap<String, Any>
                again_time_exeute!!.text = time_map["name"]!!.toString()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mMessageReceiver)
    }

    override fun onEvent() {
        back!!.setOnClickListener(this)
        next_step_txt!!.setOnClickListener(this)
        again_time_exeute_set!!.setOnClickListener(this)
        rel_scene_set!!.setOnClickListener(this)
    }

    override fun onData() {

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.back -> this@TimeExcuteCordinationActivity.finish()
            R.id.next_step_txt -> {

                //                startActivity(new Intent(TimeExcuteCordinationActivity.this,
                //                        SelectExecuteSceneResultActivity.class));
                val startTime = start_scenetime!!.text.toString()
                if (startTime == null || startTime == "") {
                    ToastUtil.showToast(this@TimeExcuteCordinationActivity, "请选择定时时间")
                    return
                }
                val map = HashMap<String,Any>()
                map.put("deviceType", "")
                map.put("deviceId", "")
                map.put("name", startTime)
                map.put("boxName", "")
                map.put("type", "102")

                if (time_map["name"] == null) {
                    ToastUtil.showToast(this@TimeExcuteCordinationActivity, "请选择定时执行条件")
                    return
                }
                map.put("action", time_map["name"]!!)
                map.put("condition", time_map["condition"]!!)
                map.put("minValue", time_map["minValue"]!!)
                map.put("maxValue", "")
                map.put("name1", startTime)
                map.put("startTime", startTime)
                map.put("endTime", "")

                var intent: Intent? = null
                val add_condition = SharedPreferencesUtil.getData(this@TimeExcuteCordinationActivity, "add_condition", false) as Boolean
                if (add_condition) {
                    //            AppManager.getAppManager().removeActivity_but_activity_cls(MainfragmentActivity.class);
                    AppManager.getAppManager().finishActivity_current(SelectSensorActivity::class.java)
                    AppManager.getAppManager().finishActivity_current(EditLinkDeviceResultActivity::class.java)
                    intent = Intent(this@TimeExcuteCordinationActivity, EditLinkDeviceResultActivity::class.java)
                    intent.putExtra("sensor_map", map as Serializable)
                    startActivity(intent)
                    this@TimeExcuteCordinationActivity.finish()
                } else {
                    intent = Intent(this@TimeExcuteCordinationActivity,
                            SelectiveLinkageActivity::class.java)
                    intent.putExtra("link_map", map as Serializable)
                    startActivity(intent)
                }
            }
            R.id.again_time_exeute_set//重复设置方式，执行一次
            -> startActivity(Intent(this@TimeExcuteCordinationActivity, AutoAgainSceneActivity::class.java))
            R.id.rel_scene_set ->
                //                showPopFormBottom(null);
                pvCustomTime!!.show() //弹出自定义时间选择器
        }//设置时间
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
                var hourPicker = date.hours.toString()
                var minutePicker = date.minutes.toString()
                when (minutePicker.length) {
                    1 -> minutePicker = "0$minutePicker"
                }

                when (hourPicker.length) {
                    1 -> hourPicker = "0$hourPicker"
                }

                start_scenetime!!.text = "$hourPicker:$minutePicker"
            }
        }).setCancelText("Cancel")
                .setSubmitText("Sure")
                .setContentSize(18)
                .setTitleSize(20)
                .setTitleText("Title")
                .setTitleColor(Color.BLACK)
                /*.setDividerColor(Color.WHITE)//设置分割线的颜色
                 .setTextColorCenter(Color.LTGRAY)//设置选中项的颜色
                 .setLineSpacingMultiplier(1.6f)//设置两横线之间的间隔倍数
                 .setTitleBgColor(Color.DKGRAY)//标题背景颜色 Night mode
                 .setBgColor(Color.BLACK)//滚轮背景颜色 Night mode
                 .setSubmitColor(Color.WHITE)
                 .setCancelColor(Color.WHITE)*/
                /*.gravity(Gravity.RIGHT)// default is center*/
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.pickerview_custom_time, object : CustomListener {

                 override   fun customLayout(v: View) {
                        val tvSubmit = v.findViewById<View>(R.id.ok_bt) as ImageView
                        val ivCancel = v.findViewById<View>(R.id.finish_bt) as ImageView
                        tvSubmit.setOnClickListener(object : View.OnClickListener {
                            public override fun onClick(v: View) {
                                pvCustomTime!!.returnData()
                                pvCustomTime!!.dismiss()
                            }
                        })

                        ivCancel.setOnClickListener(object : View.OnClickListener {
                            public override fun onClick(v: View) {
                                pvCustomTime!!.dismiss()
                            }
                        })
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

    companion object {
        val MESSAGE_TIME_EXCUTE_ACTION = "com.massky.sraum.time_excute"
        val KEY_TITLE = "title"
    }
}
