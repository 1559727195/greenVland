package com.massky.greenlandvland.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Message

import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.percentlayout.widget.PercentRelativeLayout
import com.google.gson.Gson
import com.massky.greenlandvland.R
import com.massky.greenlandvland.common.CallBackInterface
import com.massky.greenlandvland.common.CommonUtil
import com.massky.greenlandvland.common.DialogThridUtils
import com.massky.greenlandvland.common.GetToken
import com.massky.greenlandvland.common.SharedPreferencesUtils
import com.massky.greenlandvland.common.ToastUtil
import com.massky.greenlandvland.model.entity.Sc_deviceControl
import com.massky.greenlandvland.model.entity.Sc_myRoom
import com.massky.greenlandvland.model.entity.Sc_myRoomDevice
import com.massky.greenlandvland.model.httpclient.HttpUrl.HttpClient
import com.massky.greenlandvland.model.httpclient.UICallback
import java.util.HashMap

/**
 * Created by masskywcy on 2018-11-13.
 */

class Fragment_content1 : Fragment() {
    private var name2: String? = null
    private var views: View? = null;
    private var button: Array<Button?>? = null
    //    private Button btn_1,btn_2,btn_3,btn_4;
    private var viewgroup: LinearLayout? = null
    private var ll_led_detail: LinearLayout? = null
    private var ll_curtains_detail: LinearLayout? = null
    private var ll_dimmer_detail: LinearLayout? = null
    private var bt_upled: Button? = null
    private var bt_upcurtains: Button? = null
    private var bt_updimmerled: Button? = null
    private var led: ImageView? = null
    private var iv_led: ImageView? = null
    private var iv_curtains: ImageView? = null
    private var clothcurtains: Button? = null
    private var lacecurtains: Button? = null
    private var iv_close: Button? = null
    private var iv_stop: Button? = null
    private var iv_start: Button? = null
    private var iv_dimmer: ImageView? = null
    private var tv_dimmer: TextView? = null
    private var seekBar: SeekBar? = null
    private var mDialog: Dialog? = null
    private var scroll_light: ScrollView? = null
    private var scenes: Sc_myRoom.MyRoomResult.RoomListBean? = null
    private var token: String? = null
    private var projectCode: String? = null
    private var deviceId: String? = null
    private var boxNumber: String? = null
    private var id: Int? = 1
    private var isledSelect: Boolean = false
    private val isairSelect: Boolean = false
    // private var isdimmerSelect: Boolean = false
    private var isclothSelect = false
    private var islaceSelect = false
    private var statues: Int = 0
    private var dimmer: String? = null
    private var animationDrawable: AnimationDrawable? = null
    private var deviceInfo: Sc_myRoomDevice.MyRoomDeviceResult.DeviceInfoBean? = null

    //空调新风地暖
    private var air_rels: PercentRelativeLayout? = null
    private var imag_air: Button? = null
    private var kong_panel: LinearLayout? = null
    private var zhire_kongtiao: TextView? = null
    private var wendu_kongtiao: TextView? = null
    private var low_fengsu_kongtiao: TextView? = null
    private var low_fengsu_kongtiao_1: TextView? = null
    private var kong_mode: LinearLayout? = null
    private var moshi_kongtiao: ImageView? = null
    private var close_kongtiao: ImageView? = null
    private var wendu: LinearLayout? = null
    private var wendu_add_kongtiao: ImageView? = null
    private var wendu_delete_kongtia: ImageView? = null
    private var fengsu_add_kongtiao: ImageView? = null
    private var fengsu_delete_kongtiao: ImageView? = null
    private var rel_mode_btn: RelativeLayout? = null
    private var fengsu_linear: LinearLayout? = null
    private var zhire_kongtiao_1: TextView? = null
    private var ll_sensor: LinearLayout? = null
    private var btn_sensor: Button? = null
    private var sensor_renti_img: ImageView? = null
    private var btn_sensor_open: Button? = null
    private var btn_sensor_close: Button? = null
    private var linear_reset: LinearLayout? = null
    private var btn_reset: Button? = null
    private var ll_pm: LinearLayout? = null
    private var btn_pm: Button? = null
    private var temp_txt: TextView? = null
    private var pm_txt: TextView? = null
    private var hum_txt: TextView? = null
    private var in_out_sha_linear: LinearLayout? = null


    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                1 -> DialogThridUtils.closeDialog(mDialog)
            }
        }
    }

    internal var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            deviceInfo = msg.obj as Sc_myRoomDevice.MyRoomDeviceResult.DeviceInfoBean
            when (msg.what) {
                1//灯
                -> {
                    ll_led_detail!!.visibility = View.VISIBLE
                    if (deviceInfo!!.status == 1) {
                        led!!.isSelected = true
                        iv_led!!.isSelected = true
                        isledSelect = true
                    } else if (deviceInfo!!.status == 0) {
                        led!!.isSelected = false
                        iv_led!!.isSelected = false
                        isledSelect = false
                    }
                }
                2//调光灯
                -> {
                    ll_dimmer_detail!!.visibility = View.VISIBLE
                    dimmer = deviceInfo!!.dimmer
                    if (deviceInfo!!.status == 1) {
                        iv_dimmer!!.isSelected = true
                        seekBar!!.progress = Integer.parseInt(dimmer!!)
                        tv_dimmer!!.text = dimmer
                        statusbo = true
                        seekBar!!.isEnabled = statusbo
                    } else if (deviceInfo!!.status == 0) {
                        iv_dimmer!!.isSelected = false
                        tv_dimmer!!.text = " "
                        seekBar!!.progress = Integer.parseInt(dimmer!!)
                        statusbo = false
                        seekBar!!.isEnabled = statusbo
                    }
                }

                3, 5, 6 -> {//空调
                    air_rels!!.visibility = View.VISIBLE
                }


                4//窗帘
                -> {
                    ll_curtains_detail!!.visibility = View.VISIBLE
                    iv_curtains!!.setImageResource(R.drawable.c1)
                    clothcurtains!!.isEnabled = true
                    lacecurtains!!.isEnabled = true
                    iv_close!!.isEnabled = true
                    iv_start!!.isEnabled = true
                    iv_stop!!.isEnabled = true
                }
                18 -> {//一路窗帘
                    in_out_sha_linear!!.visibility = View.GONE
                    ll_curtains_detail!!.visibility = View.VISIBLE
                    iv_curtains!!.setImageResource(R.drawable.c1)
                    iv_close!!.isEnabled = true
                    iv_start!!.isEnabled = true
                    iv_stop!!.isEnabled = true

                }
            }
            init_Data()
        }
    }


    private var addflag: Boolean = false
    private var statusbo: Boolean = false
    private var statusflag: Int? = -1

    private fun init_Data() {
        if (deviceInfo != null) {
            name2 = deviceInfo!!.name2
            statusflag = deviceInfo!!.status
            type = deviceInfo!!.type
            modeflag = deviceInfo!!.mode
            windflag = deviceInfo!!.speed
            temperature = deviceInfo!!.temperature
            dimmer = deviceInfo!!.dimmer
            when (type) {
                3//空调
                -> {
//                    moshi_rel!!.visibility = View.VISIBLE
//                    fengsu_rel!!.visibility = View.VISIBLE
//                    project_select!!.text = "空调"
                    low_fengsu_kongtiao_1!!.visibility = View.GONE
                    low_fengsu_kongtiao!!.visibility = View.VISIBLE
                    zhire_kongtiao!!.visibility = View.VISIBLE
                    rel_mode_btn!!.visibility = View.VISIBLE
                    fengsu_linear!!.visibility = View.VISIBLE

                    zhire_kongtiao_1!!.visibility = View.GONE
                    zhire_kongtiao!!.visibility = View.VISIBLE

                    imag_air!!.setText("空调")
                }
                5//新风
                -> {
//                    project_select!!.text = "新风"
//                    moshi_rel!!.visibility = View.GONE
//                    fengsu_rel!!.visibility = View.VISIBLE
                    low_fengsu_kongtiao_1!!.visibility = View.VISIBLE
                    low_fengsu_kongtiao!!.visibility = View.GONE
                    zhire_kongtiao!!.visibility = View.GONE
                    rel_mode_btn!!.visibility = View.GONE
                    fengsu_linear!!.visibility = View.VISIBLE
                    imag_air!!.setText("新风")
                }
                6//地暖
                -> {
                    zhire_kongtiao!!.visibility = View.GONE
                    low_fengsu_kongtiao_1!!.visibility = View.GONE
                    low_fengsu_kongtiao!!.visibility = View.GONE
                    rel_mode_btn!!.visibility = View.VISIBLE
                    fengsu_linear!!.visibility = View.GONE
                    zhire_kongtiao_1!!.visibility = View.VISIBLE
                    imag_air!!.setText("地暖")


//                    project_select!!.text = "地暖"
//                    fengsu_rel!!.visibility = View.GONE
//                    moshi_rel!!.visibility = View.VISIBLE
                }
                10 -> {//入墙pm2.5
                    ll_pm!!.visibility = View.VISIBLE
                    btn_sensor!!.setText("入墙PM2.5")
                    temp_txt!!.setText(temperature + "℃")
                    pm_txt!!.setText(dimmer)
                    hum_txt!!.setText(windflag)
                }
                8, 9, 11, 12, 13, 14, 15, 16, 17 -> {//传感器类
                    ll_sensor!!.visibility = View.VISIBLE
                    kong_panel!!.visibility = View.VISIBLE
                    btn_sensor_open!!.visibility = View.VISIBLE
                    btn_sensor_close!!.visibility = View.VISIBLE
                    linear_reset!!.visibility = View.GONE
                    when (name2) {
                        "1" -> {
                            sensor_renti_img!!.isSelected = true
                            btn_sensor_open!!.isSelected = true
                            btn_sensor_close!!.isSelected = false
                        }
                        "0" -> {
                            sensor_renti_img!!.isSelected = false
                            btn_sensor_open!!.isSelected = false
                            btn_sensor_close!!.isSelected = true
                        }
                    }

                    when (type) {
                        8 -> {//人体检测
                            btn_sensor!!.setText("人体感应")
                            sensor_renti_img!!.setImageDrawable(context!!.resources.getDrawable(R.drawable.drawable_renti_selector))
                            btn_sensor_open!!.setText("启用")
                            btn_sensor_close!!.setText("不启用")
                        }
                        9 -> {//水浸检测
                            btn_sensor!!.setText("水浸检测")
                            sensor_renti_img!!.setImageDrawable(context!!.resources.getDrawable(R.drawable.drawable_shuijin_selector))
                            btn_sensor_open!!.setText("启用")
                            btn_sensor_close!!.setText("不启用")
                        }
                        11 -> {//紧急按妞
                            btn_sensor!!.setText("紧急按钮")
                            kong_panel!!.visibility = View.GONE
                            btn_sensor_open!!.visibility = View.GONE
                            btn_sensor_close!!.visibility = View.GONE
                            linear_reset!!.visibility = View.VISIBLE
                            sensor_renti_img!!.setImageDrawable(context!!.resources.getDrawable(R.drawable.drawable_jinjianniu_selector))

                            when (name2) {
                                "1" -> {
                                    sensor_renti_img!!.isSelected = false
                                    btn_reset!!.isSelected = false
                                }
                                "0" -> {
                                    sensor_renti_img!!.isSelected = true
                                    btn_reset!!.isSelected = true
                                }
                            }
                        }
                        12 -> {//久坐报警器
                            btn_sensor!!.setText("久坐报警")
                            btn_sensor_open!!.setText("启用")
                            btn_sensor_close!!.setText("不启用")
                            sensor_renti_img!!.setImageDrawable(context!!.resources.getDrawable(R.drawable.drawable_jiuzuo_selector))
                        }
                        13 -> {//烟雾报警器
                            btn_sensor!!.setText("烟雾报警")
                            btn_sensor_open!!.setText("启用")
                            btn_sensor_close!!.setText("不启用")
                            sensor_renti_img!!.setImageDrawable(context!!.resources.getDrawable(R.drawable.drawable_yanwu_selector))
                        }
                        14 -> {//天然气报警器
                            btn_sensor!!.setText("燃气报警")
                            btn_sensor_open!!.setText("启用")
                            btn_sensor_close!!.setText("不启用")
                            sensor_renti_img!!.setImageDrawable(context!!.resources.getDrawable(R.drawable.drawable_tianranqi_selector))
                        }
                        15 -> {//智能门锁
                            btn_sensor!!.setText("智能门锁")
                            btn_sensor_open!!.setText("启用")
                            btn_sensor_close!!.setText("不启用")
                            sensor_renti_img!!.setImageDrawable(context!!.resources.getDrawable(R.drawable.drawable_zhinengmensuo_selector))
                        }
                        16 -> {//直流电阀机械
                            //手
                            btn_sensor_open!!.setText("打开")
                            btn_sensor_close!!.setText("关闭")
                            btn_sensor!!.setText("机械手")
                            sensor_renti_img!!.setImageDrawable(context!!.resources.getDrawable(R.drawable.drawable_jixie_selector))
                        }

                        17 -> {//入墙式智能插座
                            btn_sensor!!.setText("智能插座")
                            btn_sensor_open!!.setText("启用")
                            btn_sensor_close!!.setText("不启用")
                            sensor_renti_img!!.setImageDrawable(context!!.resources.getDrawable(R.drawable.drawable_zhinengchazuo_selector))
                        }
                    }
                }
            }

            if (type == 3 || type == 5 || type == 6) {
                //初始化窗帘参数
                //空调
                //判断展示值是否加16
                addflag = false
                //   bar1!!.setCurrentValues(Integer.parseInt(temperature!!).toFloat(), 0f)

                //volumeView!!.set_temperature(Integer.parseInt(temperature!!))
                wendu_kongtiao!!.setText(temperature + "℃")
                setModetwo()
                setSpeed()
                doit_open(statusflag!!)
            }
        }
    }

    private fun doit_open(status: Int) {
        if (status == 1) {
            close_kongtiao!!.setImageResource(R.drawable.btn_kt_open)//icon_cl_close
            statusflag = 0
            statusbo = true
        } else {
            //调光灯开关状态
            close_kongtiao!!.setImageResource(R.drawable.btn_kt_close)
            statusflag = 1
            statusbo = false
        }
    }

    private fun doit_open_tiaoguang(status: Int) {
        if (status == 1) {
            iv_dimmer!!.isSelected = true

            statusflag = 0
            statusbo = true
        } else {
            //调光灯开关状态
            iv_dimmer!!.isSelected = false
            statusflag = 1
            statusbo = false
        }
    }


    internal var clickListener: View.OnClickListener = View.OnClickListener { v ->
        when (v.id) {
            R.id.iv_led//灯
            -> {
                mDialog = DialogThridUtils.showWaitDialog(context, true)
                if (isledSelect == true) {
                    statues = 0
                    isledSelect = false
                } else if (isledSelect == false) {
                    statues = 1
                    isledSelect = true
                }
                ledControl()
            }

            R.id.clothcurtains//布帘
            -> if (isclothSelect == false) {
                clothcurtains!!.isSelected = true
                isclothSelect = true
            } else if (isclothSelect == true) {
                clothcurtains!!.isSelected = false
                isclothSelect = false
            }
            R.id.lacecurtains//纱帘
            -> if (islaceSelect == false) {
                lacecurtains!!.isSelected = true
                islaceSelect = true
            } else if (islaceSelect == true) {
                lacecurtains!!.isSelected = false
                islaceSelect = false
            }

            R.id.iv_close//关闭窗帘
            -> {
                when(type) {
                    4-> {
                        if (isclothSelect == true || islaceSelect == true) {
                            mDialog = DialogThridUtils.showWaitDialog(context, true)
                            clothcurtains!!.isEnabled = false
                            lacecurtains!!.isEnabled = false
                            iv_close!!.isEnabled = false
                            iv_start!!.isEnabled = false
                            iv_stop!!.isEnabled = true
                            if (isclothSelect == true && islaceSelect == false) {
                                statues = 4
                            } else if (isclothSelect == false && islaceSelect == true) {
                                statues = 6
                            } else if (isclothSelect == true && islaceSelect == true) {
                                statues = 0
                            }
                            closecurtains()
                        } else if (isclothSelect == false && islaceSelect == false) {
                            ToastUtil.showToast(context, "请选择窗帘")
                        }
                    }
                    18-> {
                        statues = 0
                        closecurtains()
                    }
                }
            }
            R.id.iv_stop//窗帘停止
            -> {
                when (type) {
                    4 -> {
                        if (isclothSelect == true || islaceSelect == true) {
                            mDialog = DialogThridUtils.showWaitDialog(context, true)
                            clothcurtains!!.isEnabled = true
                            lacecurtains!!.isEnabled = true
                            iv_close!!.isEnabled = true
                            iv_start!!.isEnabled = true
                            iv_stop!!.isEnabled = false
                            if (isclothSelect == true || islaceSelect == true) {
                                statues = 2
                            }
                            stopCurtains()
                        } else if (isclothSelect == false && islaceSelect == false) {
                            ToastUtil.showToast(context, "请选择窗帘")
                        }
                    }
                    18 -> {//一路窗帘
                       statues = 2
                        stopCurtains()
                    }
                }
            }
            R.id.iv_start//打开窗帘
            -> {
                mDialog = DialogThridUtils.showWaitDialog(context, true)
                when (type) {
                    4 -> {
                        if (isclothSelect == true || islaceSelect == true) {
                            clothcurtains!!.isEnabled = false
                            lacecurtains!!.isEnabled = false
                            iv_close!!.isEnabled = false
                            iv_start!!.isEnabled = false
                            iv_stop!!.isEnabled = true
                            if (isclothSelect == true && islaceSelect == false) {
                                statues = 3
                            } else if (isclothSelect == false && islaceSelect == true) {
                                statues = 5
                            } else if (isclothSelect == true && islaceSelect == true) {
                                statues = 1
                            }
                            openCurtains()
                        } else if (isclothSelect == false && islaceSelect == false) {
                            ToastUtil.showToast(context, "请选择窗帘")
                        }
                    }
                    18 -> {
                        statues = 1
                        openCurtains()
                    }
                }
            }
            R.id.iv_dimmer//调光灯
            -> {
                Control("")
            }

            R.id.bt_upled//灯布局收回
            -> {
                ll_led_detail!!.visibility = View.GONE
                for (i in 0 until scenes!!.deviceList.size) {
                    button!![i]?.visibility = View.VISIBLE
                }
            }

            R.id.bt_upcurtains//窗帘布局收回
            -> {
                ll_curtains_detail!!.visibility = View.GONE
                for (i in 0 until scenes!!.deviceList.size) {
                    button!![i]?.visibility = View.VISIBLE
                }
            }

            R.id.bt_updimmerled//调光灯布局收回
            -> {
                ll_dimmer_detail!!.visibility = View.GONE
                for (i in 0 until scenes!!.deviceList.size) {
                    button!![i]?.visibility = View.VISIBLE
                }
            }

            R.id.imag_air//空调
            -> {
                air_rels!!.visibility = View.GONE

                for (i in 0 until scenes!!.deviceList.size) {
                    button!![i]?.visibility = View.VISIBLE
                }
            }

            R.id.moshi_kongtiao//模式
            -> {
                moshi_kongtiao()
            }

            R.id.close_kongtiao//开关
            -> {
                //doit_open(statusflag!!)
                control_air("onclick")
            }
            //fengsu_add_kongtiao
            R.id.fengsu_add_kongtiao//风速+
            -> {
                speed_kongtiao(0)
            }

            //fengsu_add_kongtiao
            R.id.fengsu_delete_kongtiao//风速-
            -> {
                speed_kongtiao(1)
            }

            //fengsu_add_kongtiao
            R.id.wendu_add_kongtiao//温度+
            -> {
                wendu_add_kongtiao_method("add")
                Control("")
            }
            //fengsu_add_kongtiao
            R.id.wendu_delete_kongtiao//温度-
            -> {
                wendu_add_kongtiao_method("delete")
                Control("")
            }

            R.id.btn_sensor //传感器点击
            -> {
                ll_sensor!!.visibility = View.GONE

                for (i in 0 until scenes!!.deviceList.size) {
                    button!![i]?.visibility = View.VISIBLE
                }
            }
            R.id.btn_sensor_open -> {//启用
                sensor_set_protection("1")
            }

            R.id.btn_sensor_close -> {//不启用
                sensor_set_protection("0")
            }

            R.id.btn_reset -> {//复位
                when (name2) {
                    "1" -> sensor_set_protection("0")
                    "0" -> sensor_set_protection("1")
                }
            }
            R.id.btn_pm -> {//pm2.5点击
                ll_pm!!.visibility = View.GONE
                for (i in 0 until scenes!!.deviceList.size) {
                    button!![i]?.visibility = View.VISIBLE
                }
            }
        }
    }


    /**
     * 控制空调开关
     */
    private fun control_air(doit: String) {
        Control(doit)
    }


    /**
     * 空调风速选择
     */
    private fun speed_kongtiao(add_delete: Int) {
        //风速状态
        if (statusbo) {
            control_wind(add_delete)
            Control("")
        }
    }

    /**
     * 控制风速
     */
    private fun control_wind(add_delete: Int) {
        when (add_delete) {
            0 -> {
                common_wind_low()
            }
            1 -> {
                common_wind()
            }
        }
    }

    private fun common_wind_low() {
        var tex = ""
        when (windflag) {
            "1" -> {
                //                    windspeed_id.setText("中风");
                tex = "中风"
                windflag = "2"
            }
            "2" -> {
                //                    windspeed_id.setText("高风");
                tex = "高风"
                windflag = "3"
            }
            "3" -> {
                tex = "强力"
                //                    windspeed_id.setText("强力");
                windflag = "4"
            }
            "4" -> {
                tex = "送风"
                //                    windspeed_id.setText("送风");
                windflag = "5"
            }
            "5" -> {
                //                    windspeed_id.setText("自动");
                tex = "自动"
                //                        windflag = "6"
            }
            "6" -> {
                //                    windspeed_id.setText("低风");
                //                low_fengsu_kongtiao!!.setText("低风")
                //                windflag = "1"
                //                        low_fengsu_kongtiao!!.setText("自动")
                //                        windflag = "6"
            }
            else -> {

            }

        }

        when (type) {
            3//空调
            -> low_fengsu_kongtiao!!.setText(tex)
            5//新风
            -> low_fengsu_kongtiao_1!!.setText(tex)
        }
    }

    private fun common_wind() {
        var tex = ""
        when (windflag) {
            "5" -> {
                //                    windspeed_id.setText("高风");
                tex = "送风"
                windflag = "4"
            }
            "4" -> {
                tex = "强力"
                //                    windspeed_id.setText("强力");
                windflag = "3"
            }
            "3" -> {
                tex = "高风"
                //                    windspeed_id.setText("送风");
                windflag = "2"
            }
            "2" -> {
                //                    windspeed_id.setText("自动");
                tex = "中风"
                windflag = "1"
            }
            "1" -> {
                //                    windspeed_id.setText("低风");
                tex = "低风"
            }
            else -> {

            }
        }

        when (type) {
            3//空调
            -> low_fengsu_kongtiao!!.setText(tex)
            5//新风
            -> low_fengsu_kongtiao_1!!.setText(tex)
        }
    }


    /**
     * 温度+
     */
    private fun wendu_add_kongtiao_method(add_delete: String?) {

        if (!statusbo) {
            return
        }
        var temp = 16
        var temps = Integer.parseInt(temperature!!)
        when (add_delete) {
            "add" -> if (temps >= 16 && temps < 30) {
                temp = temps + 1
            } else if (temps != 255) {
                temp = 30
            }
            "delete" -> if (temps > 16 && temps <= 30) {
                temp = temps - 1
            } else if (temps != 255) {
                temp = 16
            }
        }

        temperature = "$temp"
        wendu_kongtiao!!.setText("$temp" + "℃")
    }


    /**
     * 空调模式选择
     */
    private fun moshi_kongtiao() {
        if (statusbo) {
            when (type) {
                3//空调
                -> setMode()
                6//地暖
                -> setMode_DiNuan()
            }
            Control("")
        }
    }


    /**
     * 地暖模式
     */
    private fun setMode_DiNuan() {

        //模式状态
        if (statusbo)
            when (modeflag) {
                "1" -> {
                    //                tempstate_id.setText("制热");
                    zhire_kongtiao_1!!.setText("睡眠")
                    modeflag = "2"
                }
                "2" -> {
                    //                tempstate_id.setText("除湿");
                    zhire_kongtiao_1!!.setText("外出")
                    modeflag = "3"
                }
                "3" -> {
                    //                tempstate_id.setText("自动");
                    zhire_kongtiao_1!!.setText("加热")
                    modeflag = "1"
                }
                else -> {

                }
            }
    }


    //
    private fun setMode() {
        //模式状态
        if (statusbo)
            when (modeflag) {
                "1" -> {
                    //                tempstate_id.setText("制热");
                    //bar1!!.setMode("制热")
                    zhire_kongtiao!!.setText("制热")
                    modeflag = "2"
                }
                "2" -> {
                    //                tempstate_id.setText("除湿");
                    zhire_kongtiao!!.setText("除湿")
                    modeflag = "3"
                }
                "3" -> {
                    //                tempstate_id.setText("自动");
                    zhire_kongtiao!!.setText("自动")
                    modeflag = "4"
                }
                "4" -> {
                    //                tempstate_id.setText("通风");
                    zhire_kongtiao!!.setText("通风")
                    modeflag = "5"
                }
                "5" -> {
                    //                tempstate_id.setText("制冷");
                    zhire_kongtiao!!.setText("制冷")
                    modeflag = "1"
                }
                else -> {

                }
            }
    }


    private var status: String? = null
    private var number: String? = null
    private var type: Int? = null
    private var name: String? = null
    //    @BindView(R.id.project_select)
//    private var project_select: TextView? = null
    private var mode: String? = null
    private var speed: String? = null
//    @BindView(R.id.volumeView)
//    private var volumeView: VolumeView_New? = null

    //    @BindView(R.id.moshi_rel)
//    private var moshi_rel: RelativeLayout? = null
//    @BindView(R.id.fengsu_rel)
//    private var fengsu_rel: RelativeLayout? = null
//    @BindView(R.id.openbtn_tiao_guang)
//    private var openbtn_tiao_guang: ImageView? = null


    private var modeflag: String? = null
    private var temperature: String? = null
    private var windflag: String? = null


    /**
     * 设置模式
     */
    private fun setModetwo() {
        var strmode = ""
        when (type) {
            3//空调
            -> strmode = get_air_modetwo(strmode)
            6//地暖
            -> strmode = get_dinuan_modetwo(strmode)
        }
        zhire_kongtiao!!.setText(strmode);
        // bar1!!.setMode(strmode)
    }

    private fun get_air_modetwo(strmode: String): String {
        var strmode = strmode
        when (modeflag) {
            "1" -> strmode = "制冷"
            "2" -> strmode = "制热"
            "3" -> strmode = "除湿"
            "4" -> strmode = "自动"
            "5" -> strmode = "通风"
            else -> {

            }
        }
        return strmode
    }

    private fun get_dinuan_modetwo(strmode: String): String {
        var strmode = strmode
        when (modeflag) {
            "1" -> strmode = "加热"
            "2" -> strmode = "睡眠"
            "3" -> strmode = "外出"
            else -> {
            }
        }
        return strmode
    }

    private fun setSpeed() {
        var strwind = ""
        when (windflag) {
            "1" -> strwind = "低风"
            "2" -> strwind = "中风"
            "3" -> strwind = "高风"
            "4" -> strwind = "强力"
            "5" -> strwind = "送风"
            "6" -> strwind = "自动"
            else -> {

            }
        }
        //        windspeedtwo_id.setText(strwind);
        //        windspeed_id.setText(strwind);
        if (type == 6) {
            low_fengsu_kongtiao!!.setText("")
        } else {
            low_fengsu_kongtiao!!.setText(strwind)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        scenes = bundle!!.getSerializable("scenes") as Sc_myRoom.MyRoomResult.RoomListBean?
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        views = inflater.inflate(R.layout.fragment_content, container, false)
        token = SharedPreferencesUtils.getToken(context!!)
        projectCode = SharedPreferencesUtils.getProjectCode(context!!)
        boxNumber = SharedPreferencesUtils.getBoxNumber(context!!)
        init()//初始化控件
        init_buttons()
        return views
    }

    private fun init_buttons() {
        viewgroup = views!!.findViewById(R.id.viewgroup)
        var length = scenes!!.deviceList.size
        button = arrayOfNulls(length)
        for (i in 0 until scenes!!.deviceList.size) {
            button!![i] = Button(activity)
            button!![i]?.setBackgroundResource(R.drawable.bg_yuan)
            button!![i]?.text = scenes!!.deviceList[i].deviceName
            button!![i]?.textSize = 15f
            button!![i]?.setTextColor(Color.WHITE)
            button!![i]?.setOnClickListener {
                deviceId = scenes!!.deviceList[i].deviceId
                myRoomDevice()
                mDialog = DialogThridUtils.showWaitDialog(context, true)
            }

            val lParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            lParams.gravity = Gravity.CENTER_HORIZONTAL
            lParams.topMargin = 50
            viewgroup!!.addView(button!![i], lParams)
        }
    }


    private fun myRoomDevice() {
        HttpClient.post(CommonUtil.APPURL, "sc_myRoomDevice",
                Gson().toJson(Sc_myRoomDevice.MyRoomDeviceParams(token, projectCode, deviceId)), object : UICallback {
            override fun process(data: String) {
                val myRoomDeviceResult = Gson().fromJson(data, Sc_myRoomDevice.MyRoomDeviceResult::class.java)
                val result = myRoomDeviceResult.result
                if (result == "1") {
                    Log.e("TAG", "1-解析错误")
                    mHandler.sendEmptyMessage(1)
                    id = 1
                } else if (result == "100") {
                    Log.e("TAG", "100-成功")
                    id = 1
                    //                                                SharedPreferencesUtils.saveDimmer(getContext(), myRoomDeviceResult.getDeviceInfo().getDimmer());
                    val message = Message()
                    message.what = myRoomDeviceResult.deviceInfo.type
                    message.obj = myRoomDeviceResult.deviceInfo
                    handler.sendMessage(message)
                    for (i in 0 until scenes!!.deviceList.size) {
                        button!![i]?.visibility = View.GONE
                    }
                    mHandler.sendEmptyMessage(1)
                } else if (result == "101") {
                    Log.e("TAG", "101-token错误")
                    mHandler.sendEmptyMessage(1)
                    if (id == 1) {
                        id = id!! + 1
                        GetToken(CallBackInterface { str ->
                            token = str
                            Log.e("TAG", "token=" + token!!)
                            if (!TextUtils.isEmpty(token)) {
                                myRoomDevice()
                            } else {
                                showerror()
                            }
                        }, context)
                    } else {
                        id = 1
                        showerror()
                    }
                } else if (result == "102") {
                    Log.e("TAG", "102-projectCode错误")
                    id = 1
                    mHandler.sendEmptyMessage(1)
                    ToastUtil.showToast(context, "操作失败")
                } else if (result == "103") {
                    Log.e("TAG", "103-deviceId错误")
                    id = 1
                    mHandler.sendEmptyMessage(1)
                    ToastUtil.showToast(context, "操作失败")
                } else {
                    id = 1
                    mHandler.sendEmptyMessage(1)
                    ToastUtil.showToast(context, "操作失败")
                }
            }

            override fun onError(data: String) {
                id = 1
                mHandler.sendEmptyMessage(1)
                ToastUtil.showToast(context, "网络连接失败")
            }
        })
    }

    //初始化控件
    private fun init() {
        init_views()
        //添加监听
        init_event()
        seek_bar_event()
    }

    private fun seek_bar_event() {
        seekBar!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (statusbo == true) {
                    tv_dimmer!!.text = Integer.toString(progress)
                } else if (statusbo == false) {
                    tv_dimmer!!.text = ""
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                if (statusbo == true) {
                    mDialog = DialogThridUtils.showWaitDialog(context, true)
                    statues = 1
                    dimmer = tv_dimmer!!.text.toString()
                    Control("")
                }
            }
        })
    }

    private fun init_event() {
        ll_led_detail!!.setOnClickListener(clickListener)
        ll_curtains_detail!!.setOnClickListener(clickListener)
        ll_dimmer_detail!!.setOnClickListener(clickListener)
        iv_led!!.setOnClickListener(clickListener)
        //        iv_aironoff.setOnClickListener(clickListener);
        clothcurtains!!.setOnClickListener(clickListener)
        lacecurtains!!.setOnClickListener(clickListener)
        iv_close!!.setOnClickListener(clickListener)
        iv_stop!!.setOnClickListener(clickListener)
        iv_start!!.setOnClickListener(clickListener)
        iv_dimmer!!.setOnClickListener(clickListener)
        bt_upled!!.setOnClickListener(clickListener)

        bt_upcurtains!!.setOnClickListener(clickListener)
        bt_updimmerled!!.setOnClickListener(clickListener)


        //空调事件监听

        imag_air!!.setOnClickListener(clickListener);

        moshi_kongtiao!!.setOnClickListener(clickListener);
        wendu_add_kongtiao!!.setOnClickListener(clickListener);

        close_kongtiao!!.setOnClickListener(clickListener);
        fengsu_add_kongtiao!!.setOnClickListener(clickListener);
        fengsu_delete_kongtiao!!.setOnClickListener(clickListener);
        wendu_delete_kongtia!!.setOnClickListener(clickListener)
        btn_sensor!!.setOnClickListener(clickListener)

        btn_sensor_open!!.setOnClickListener(clickListener)
        btn_sensor_close!!.setOnClickListener(clickListener)

        btn_reset!!.setOnClickListener(clickListener)
        btn_pm!!.setOnClickListener(clickListener)

    }

    private fun init_views() {
        //获取控件
        scroll_light = views!!.findViewById(R.id.scroll_light)
        air_rels = views!!.findViewById(R.id.air_rels)
        low_fengsu_kongtiao_1 = views!!.findViewById(R.id.low_fengsu_kongtiao_1)

        imag_air = views!!.findViewById(R.id.imag_air);
        kong_panel = views!!.findViewById(R.id.kong_panel);
        zhire_kongtiao = views!!.findViewById(R.id.zhire_kongtiao);
        wendu_kongtiao = views!!.findViewById(R.id.wendu_kongtiao);
        low_fengsu_kongtiao = views!!.findViewById(R.id.low_fengsu_kongtiao);
        kong_mode = views!!.findViewById(R.id.kong_mode);
        moshi_kongtiao = views!!.findViewById(R.id.moshi_kongtiao);
        close_kongtiao = views!!.findViewById(R.id.close_kongtiao);
        wendu = views!!.findViewById(R.id.wendu);
        wendu_add_kongtiao = views!!.findViewById(R.id.wendu_add_kongtiao);
        wendu_delete_kongtia = views!!.findViewById(R.id.wendu_delete_kongtiao);
        fengsu_add_kongtiao = views!!.findViewById(R.id.fengsu_add_kongtiao);
        fengsu_delete_kongtiao = views!!.findViewById(R.id.fengsu_delete_kongtiao);

        ll_led_detail = views!!.findViewById(R.id.ll_led_detail)
        ll_curtains_detail = views!!.findViewById(R.id.ll_curtains_detial)
        ll_dimmer_detail = views!!.findViewById(R.id.ll_dimmer_detail)

        bt_upled = views!!.findViewById(R.id.bt_upled)
        bt_upcurtains = views!!.findViewById(R.id.bt_upcurtains)
        bt_updimmerled = views!!.findViewById(R.id.bt_updimmerled)
        led = views!!.findViewById(R.id.led)
        iv_led = views!!.findViewById(R.id.iv_led)
        //        iv_aironoff= (ImageView) view.findViewById(R.id.iv_aironoff);
        //        tv_temperature= (TextView) view.findViewById(R.id.tv_temperature);
        iv_curtains = views!!.findViewById(R.id.iv_curtains)
        clothcurtains = views!!.findViewById(R.id.clothcurtains)
        lacecurtains = views!!.findViewById(R.id.lacecurtains)
        iv_close = views!!.findViewById(R.id.iv_close)
        iv_stop = views!!.findViewById(R.id.iv_stop)
        iv_start = views!!.findViewById(R.id.iv_start)
        tv_dimmer = views!!.findViewById(R.id.tv_dimmer)
        iv_dimmer = views!!.findViewById(R.id.iv_dimmer)
        seekBar = views!!.findViewById(R.id.seekBar)
        rel_mode_btn = views!!.findViewById(R.id.rel_mode_btn)
        fengsu_linear = views!!.findViewById(R.id.fengsu_linear)
        zhire_kongtiao_1 = views!!.findViewById(R.id.zhire_kongtiao_1)
        ll_sensor = views!!.findViewById(R.id.ll_sensor)
        btn_sensor = views!!.findViewById(R.id.btn_sensor)
        sensor_renti_img = views!!.findViewById(R.id.sensor_renti_img)
        btn_sensor_open = views!!.findViewById(R.id.btn_sensor_open)
        btn_sensor_close = views!!.findViewById(R.id.btn_sensor_close)

        linear_reset = views!!.findViewById(R.id.linear_reset)
        btn_reset = views!!.findViewById(R.id.btn_reset)

        ll_pm = views!!.findViewById(R.id.ll_pm)
        btn_pm = views!!.findViewById(R.id.btn_pm)
        //temp_txt
        temp_txt = views!!.findViewById(R.id.temp_txt)
        pm_txt = views!!.findViewById(R.id.pm_txt)
        hum_txt = views!!.findViewById(R.id.hum_txt)

        in_out_sha_linear = views!!.findViewById(R.id.in_out_sha_linear)
    }


    /**
     * 初始化值
     */
    private fun init_air_value() {
        init_speed()
        init_mode()
        low_fengsu_kongtiao!!.setText(speed_txt)
        zhire_kongtiao!!.setText(mode)
        //volumeView
        //volumeView!!.set_temperature(Integer.parseInt(temperature!!))
        wendu_kongtiao!!.setText(temperature)
    }


    private var speed_txt: String? = null
    private var mode_txt: String? = null

    private fun init_speed() {
        when (speed) {
            "1" -> speed_txt = "低风"
            "2" -> speed_txt = "中风"
            "3" -> speed_txt = "高风"
            "4" -> speed_txt = "强力"
            "5" -> speed_txt = "送风"
            "6" -> speed_txt = "自动"
        }
    }

    private fun init_mode() {
        when (type) {
            3//空调
            -> air_mode()
            6//地暖
            -> dinuan_mode()
        }
    }


    private fun air_mode() {
        when (mode) {
            "1" -> mode_txt = "制冷"
            "2" -> mode_txt = "制热"
            "3" -> mode_txt = "除湿"
            "4" -> mode_txt = "自动"
            "5" -> mode_txt = "通风"
        }
    }


    private fun dinuan_mode() {
        when (mode) {
            "1" -> mode_txt = "加热"
            "2" -> mode_txt = "睡眠"
            "3" -> mode_txt = "外出"
        }
    }


    //var type: Int, var number: Int, var status: Int, var dimmer: String?,
    // var mode: String?, var temperature: String?, var speed: String?
    private fun Control(doit: String) {
        var status = 0;
        when (type) {
            2 -> {
                if (statusbo) {
                    status = 0
                } else
                    status = 1
            }
            else -> {
                when (doit) {
                    "onclick" -> //map.put("status", statusflag!!)
                        status = statusflag!!
                    else -> if (statusbo) {
                        //  map.put("status", "1")
                        status = 1
                    } else {
                        //map.put("status", "0")
                        status = 0
                    }
                }
            }
        }


        HttpClient.post(CommonUtil.APPURL, "sc_deviceControl", Gson().toJson(Sc_deviceControl.DeviceControlParams(token,
                projectCode, boxNumber,
                Sc_deviceControl.DeviceControlParams.DeviceInfo(
                        type!!,
                        deviceInfo!!.number,
                        status,
                        dimmer,
                        modeflag,
                        temperature,
                        windflag))), object : UICallback {
            override fun process(data: String) {
                val deviceControlResult = Gson().fromJson(data, Sc_deviceControl.DeviceControlResult::class.java)
                val result = deviceControlResult.result
                if (result == 1) {
                    Log.e("TAG", "1-解析错误")
                    mHandler.sendEmptyMessage(1)
                    id = 1
                } else if (result == 100) {
                    Log.e("TAG", "100-成功")
                    id = 1
                    when (doit) {
                        "onclick" -> doit_open(status)
                    }
                    when (type) {
                        2 -> doit_open_tiaoguang(status)
                    }
                    mHandler.sendEmptyMessage(1)
                } else if (result == 101) {
                    Log.e("TAG", "101-token错误")
                    mHandler.sendEmptyMessage(1)
                    if (id == 1) {
                        id = id!! + 1
                        GetToken(CallBackInterface { str ->
                            token = str
                            Log.e("TAG", "token=" + token!!)
                            if (!TextUtils.isEmpty(token)) {
                                Control(doit)
                            } else {
                                showerror()
                            }
                        }, context)
                    } else {
                        id = 1
                        showerror()
                    }
                } else if (result == 102) {
                    Log.e("TAG", "102-projectCode错误")
                    id = 1
                    mHandler.sendEmptyMessage(1)
                    ToastUtil.showToast(context, "操作失败")
                } else if (result == 103) {
                    Log.e("TAG", "103-deviceInfo错误")
                    id = 1
                    mHandler.sendEmptyMessage(1)
                    ToastUtil.showToast(context, "操作失败")
                } else if (result == 104) {
                    Log.e("TAG", "104-boxNumber错误")
                    id = 1
                    mHandler.sendEmptyMessage(1)
                    ToastUtil.showToast(context, "设备断线")
                } else {
                    id = 1
                    mHandler.sendEmptyMessage(1)
                    ToastUtil.showToast(context, "操作失败")
                }
            }

            override fun onError(data: String) {
                id = 1
                mHandler.sendEmptyMessage(1)
                ToastUtil.showToast(context, "网络连接失败")
            }
        })
    }


    /**
     * 传感器类设置是否启用布防
     */
    private fun sensor_set_protection(isUse: String) {
        //dialogUtil.loadDialog()
        var method = ""
        val mapbox = HashMap<String, Any>()

        mapbox["token"] = token!!
        mapbox["isUse"] = isUse
        mapbox["projectCode"] = projectCode!!
        mapbox["deviceId"] = deviceInfo!!.number
        method = "sc_setLinkSensorPanelIsUse"


        HttpClient.post(CommonUtil.APPURL, method, Gson().toJson(mapbox), object : UICallback {
            override fun process(data: String) {
                val deviceControlResult = Gson().fromJson(data, Sc_deviceControl.DeviceControlResult::class.java)
                val result = deviceControlResult.result
                if (result == 1) {
                    Log.e("TAG", "1-解析错误")
                    id = 1
                } else if (result == 100) {
                    when (type) {//人体检测
                        11 -> when (isUse) {//紧急按钮
                            "1" -> {
                                btn_reset!!.isSelected = false
                                sensor_renti_img!!.isSelected = false
                            }
                            "0" -> {
                                btn_reset!!.isSelected = true
                                sensor_renti_img!!.isSelected = true
                            }
                        }
                        8, 9, 10, 12, 13, 14, 15, 16, 17 -> when (isUse) {
                            "1" -> {
                                sensor_renti_img!!.isSelected = true
                                btn_sensor_open!!.isSelected = true
                                btn_sensor_close!!.isSelected = false
                            }
                            "0" -> {
                                sensor_renti_img!!.isSelected = false
                                btn_sensor_open!!.isSelected = false
                                btn_sensor_close!!.isSelected = true
                            }
                        }
                    }
                    name2 = isUse

                } else if (result == 101) {
                    Log.e("TAG", "101-token错误")
                    mHandler.sendEmptyMessage(1)
                    if (id == 1) {
                        id = id!! + 1
                        GetToken(CallBackInterface { str ->
                            token = str
                            Log.e("TAG", "token=" + token!!)
                            if (!TextUtils.isEmpty(token)) {
                                //  Control(doit)
                            } else {
                                //showerror()
                            }
                        }, context)
                    } else {
                        id = 1
                        //showerror()
                    }
                } else if (result == 102) {
                    Log.e("TAG", "102-projectCode错误")
                    id = 1

                    ToastUtil.showToast(context, "操作失败")
                } else if (result == 103) {
                    Log.e("TAG", "103-deviceInfo错误")
                    id = 1

                    ToastUtil.showToast(context, "操作失败")
                } else if (result == 104) {
                    Log.e("TAG", "104-boxNumber错误")
                    id = 1

                    ToastUtil.showToast(context, "设备断线")
                } else {
                    id = 1

                    ToastUtil.showToast(context, "操作失败")
                }
            }

            override fun onError(data: String) {
                id = 1

                ToastUtil.showToast(context, "网络连接失败")
            }
        })
    }

    private fun openCurtains() {
        HttpClient.post(CommonUtil.APPURL, "sc_deviceControl", Gson().toJson(Sc_deviceControl.DeviceControlParams(token, projectCode, boxNumber, Sc_deviceControl.DeviceControlParams.DeviceInfo(deviceInfo!!.type, deviceInfo!!.number, statues, deviceInfo!!.dimmer, deviceInfo!!.mode, deviceInfo!!.temperature, deviceInfo!!.speed))), object : UICallback {
            override fun process(data: String) {
                val deviceControlResult = Gson().fromJson(data, Sc_deviceControl.DeviceControlResult::class.java)
                val result = deviceControlResult.result
                if (result == 1) {
                    Log.e("TAG", "1-解析错误")
                    id = 1
                    mHandler.sendEmptyMessage(1)
                } else if (result == 100) {
                    Log.e("TAG", "100-成功")
                    id = 1
                    mHandler.sendEmptyMessage(1)
                    iv_curtains!!.setBackgroundResource(R.drawable.curtains_start)
                    animationDrawable = iv_curtains!!.background as AnimationDrawable
                    animationDrawable!!.start()
                    iv_close!!.isSelected = false
                    iv_stop!!.isSelected = false
                    iv_start!!.isSelected = true
                } else if (result == 101) {
                    Log.e("TAG", "101-token错误")
                    mHandler.sendEmptyMessage(1)
                    if (id == 1) {
                        id = id!! + 1
                        GetToken(CallBackInterface { str ->
                            token = str
                            Log.e("TAG", "token=" + token!!)
                            if (!TextUtils.isEmpty(token)) {
                                openCurtains()
                            } else {
                                showerror()
                            }
                        }, context)
                    } else {
                        id = 1
                        showerror()
                    }
                } else if (result == 102) {
                    Log.e("TAG", "102-projectCode错误")
                    id = 1
                    mHandler.sendEmptyMessage(1)
                } else if (result == 103) {
                    Log.e("TAG", "103-deviceInfo错误")
                    id = 1
                    mHandler.sendEmptyMessage(1)
                } else if (result == 104) {
                    Log.e("TAG", "104-boxNumber错误")
                    mHandler.sendEmptyMessage(1)
                    id = 1
                    ToastUtil.showToast(context, "设备断线")
                } else {
                    id = 1
                    mHandler.sendEmptyMessage(1)
                    ToastUtil.showToast(context, "操作失败")
                }
            }

            override fun onError(data: String) {
                id = 1
                mHandler.sendEmptyMessage(1)
                ToastUtil.showToast(context, "网络连接失败")
            }
        })
    }

    private fun stopCurtains() {
        HttpClient.post(CommonUtil.APPURL, "sc_deviceControl", Gson().toJson(Sc_deviceControl.DeviceControlParams(token, projectCode, boxNumber, Sc_deviceControl.DeviceControlParams.DeviceInfo(deviceInfo!!.type, deviceInfo!!.number, statues, deviceInfo!!.dimmer, deviceInfo!!.mode, deviceInfo!!.temperature, deviceInfo!!.speed))), object : UICallback {
            override fun process(data: String) {
                val deviceControlResult = Gson().fromJson(data, Sc_deviceControl.DeviceControlResult::class.java)
                val result = deviceControlResult.result
                if (result == 1) {
                    Log.e("TAG", "1-解析错误")
                    id = 1
                    mHandler.sendEmptyMessage(1)
                } else if (result == 100) {
                    Log.e("TAG", "100-成功")
                    id = 1
                    mHandler.sendEmptyMessage(1)
                    iv_curtains!!.setImageResource(R.drawable.c15)
                    iv_close!!.isSelected = true
                    iv_stop!!.isSelected = false
                    iv_start!!.isSelected = false
                } else if (result == 101) {
                    Log.e("TAG", "101-token错误")
                    mHandler.sendEmptyMessage(1)
                    if (id == 1) {
                        id = id!! + 1
                        GetToken(CallBackInterface { str ->
                            token = str
                            Log.e("TAG", "token=" + token!!)
                            if (!TextUtils.isEmpty(token)) {
                                stopCurtains()
                            } else {
                                showerror()
                            }
                        }, context)
                    } else {
                        id = 1
                        showerror()
                    }
                } else if (result == 102) {
                    Log.e("TAG", "102-projectCode错误")
                    id = 1
                    mHandler.sendEmptyMessage(1)
                    ToastUtil.showToast(context, "操作失败")
                } else if (result == 103) {
                    Log.e("TAG", "103-deviceInfo错误")
                    id = 1
                    mHandler.sendEmptyMessage(1)
                    ToastUtil.showToast(context, "操作失败")
                } else if (result == 104) {
                    Log.e("TAG", "104-boxNumber错误")
                    mHandler.sendEmptyMessage(1)
                    id = 1
                    ToastUtil.showToast(context, "设备断线")
                } else {
                    id = 1
                    mHandler.sendEmptyMessage(1)
                    ToastUtil.showToast(context, "操作失败")
                }
            }

            override fun onError(data: String) {
                id = 1
                mHandler.sendEmptyMessage(1)
                ToastUtil.showToast(context, "网络连接失败")
            }
        })
    }

    private fun closecurtains() {
        HttpClient.post(CommonUtil.APPURL, "sc_deviceControl", Gson().toJson(Sc_deviceControl.DeviceControlParams(token, projectCode, boxNumber, Sc_deviceControl.DeviceControlParams.DeviceInfo(deviceInfo!!.type, deviceInfo!!.number, statues, deviceInfo!!.dimmer,
                deviceInfo!!.mode, deviceInfo!!.temperature, deviceInfo!!.speed))), object : UICallback {
            override fun process(data: String) {
                val deviceControlResult = Gson().fromJson(data, Sc_deviceControl.DeviceControlResult::class.java)
                val result = deviceControlResult.result
                if (result == 1) {
                    Log.e("TAG", "1-解析错误")
                    mHandler.sendEmptyMessage(1)
                    id = 1
                } else if (result == 100) {
                    Log.e("TAG", "100-成功")
                    id = 1
                    mHandler.sendEmptyMessage(1)
                    iv_curtains!!.setBackgroundResource(R.drawable.curtains_close)
                    animationDrawable = iv_curtains!!.background as AnimationDrawable
                    animationDrawable!!.start()
                    iv_close!!.isSelected = true
                    iv_stop!!.isSelected = false
                    iv_start!!.isSelected = false
                } else if (result == 101) {
                    Log.e("TAG", "101-token错误")
                    mHandler.sendEmptyMessage(1)
                    if (id == 1) {
                        id = id!! + 1
                        GetToken(CallBackInterface { str ->
                            token = str
                            Log.e("TAG", "token=" + token!!)
                            if (!TextUtils.isEmpty(token)) {
                                closecurtains()
                            } else {
                                showerror()
                            }
                        }, context)
                    } else {
                        id = 1
                        showerror()
                    }
                } else if (result == 102) {
                    Log.e("TAG", "102-projectCode错误")
                    id = 1
                    mHandler.sendEmptyMessage(1)
                    ToastUtil.showToast(context, "操作失败")
                } else if (result == 103) {
                    Log.e("TAG", "103-deviceInfo错误")
                    id = 1
                    mHandler.sendEmptyMessage(1)
                    ToastUtil.showToast(context, "操作失败")
                } else if (result == 104) {
                    Log.e("TAG", "104-boxNumber错误")
                    id = 1
                    mHandler.sendEmptyMessage(1)
                    ToastUtil.showToast(context, "设备断线")
                } else {
                    id = 1
                    mHandler.sendEmptyMessage(1)
                    ToastUtil.showToast(context, "操作失败")
                }
            }

            override fun onError(data: String) {
                id = 1
                mHandler.sendEmptyMessage(1)
                ToastUtil.showToast(context, "网络连接失败")
            }
        })
    }

    private fun ledControl() {
        HttpClient.post(CommonUtil.APPURL, "sc_deviceControl", Gson().toJson(Sc_deviceControl.DeviceControlParams(token, projectCode, boxNumber, Sc_deviceControl.DeviceControlParams.DeviceInfo(deviceInfo!!.type, deviceInfo!!.number, statues, deviceInfo!!.dimmer,
                deviceInfo!!.mode, deviceInfo!!.temperature, deviceInfo!!.speed))), object : UICallback {
            override fun process(data: String) {
                val deviceControlResult = Gson().fromJson(data, Sc_deviceControl.DeviceControlResult::class.java)
                val result = deviceControlResult.result
                if (result == 1) {
                    Log.e("TAG", "1-解析错误")
                    mHandler.sendEmptyMessage(1)
                } else if (result == 100) {
                    Log.e("TAG", "100-成功")
                    mHandler.sendEmptyMessage(1)
                    led!!.isSelected = isledSelect
                    iv_led!!.isSelected = isledSelect
                } else if (result == 101) {
                    Log.e("TAG", "101-token错误")
                    mHandler.sendEmptyMessage(1)
                    if (id == 1) {
                        id = id!! + 1
                        GetToken(CallBackInterface { str ->
                            token = str
                            Log.e("TAG", "token=" + token!!)
                            if (!TextUtils.isEmpty(token)) {
                                ledControl()
                            } else {
                                showerror()
                            }
                        }, context)
                    } else {
                        id = 1
                        showerror()
                    }
                } else if (result == 102) {
                    Log.e("TAG", "102-projectCode错误")
                    mHandler.sendEmptyMessage(1)
                    ToastUtil.showToast(context, "操作失败")
                } else if (result == 103) {
                    Log.e("TAG", "103-deviceInfo错误")
                    mHandler.sendEmptyMessage(1)
                    ToastUtil.showToast(context, "操作失败")
                } else if (result == 104) {
                    Log.e("TAG", "104-boxNumber错误")
                    mHandler.sendEmptyMessage(1)
                    ToastUtil.showToast(context, "设备断线")
                } else {
                    id = 1
                    mHandler.sendEmptyMessage(1)
                    ToastUtil.showToast(context, "操作失败")
                }
            }

            override fun onError(data: String) {
                id = 1
                mHandler.sendEmptyMessage(1)
                ToastUtil.showToast(context, "网络连接失败")
            }
        })
    }

    private fun showerror() {
        val builder = AlertDialog.Builder(context, 1)
        builder.setTitle("提示")
        builder.setMessage("服务器错误")
        builder.setCancelable(false)
        builder.setPositiveButton("确定") { dialog, which ->
            dialog.dismiss()
            (context as MyHouseActivity).finish()
        }
        builder.create().show()
    }

    companion object {

        fun newInstance(scenes: Sc_myRoom.MyRoomResult.RoomListBean): Fragment_content1 {
            val bundle = Bundle()
            bundle.putSerializable("scenes", scenes)
            val fragment = Fragment_content1()
            fragment.arguments = bundle
            return fragment
        }
    }
}


