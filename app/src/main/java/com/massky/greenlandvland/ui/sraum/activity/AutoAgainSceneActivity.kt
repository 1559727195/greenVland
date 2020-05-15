package com.massky.greenlandvland.ui.sraum.activity

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.massky.greenlandvland.R
import com.massky.greenlandvland.common.ToastUtil
import com.massky.greenlandvland.ui.sraum.Utils.AppManager
import com.massky.greenlandvland.ui.sraum.adapter.AgainAutoSceneAdapter
import com.massky.greenlandvland.ui.sraum.base.BaseActivity
import com.yanzhenjie.statusview.StatusUtils
import java.io.Serializable
import java.util.ArrayList
import java.util.HashMap
import butterknife.BindView
import com.massky.greenlandvland.ui.sraum.activity.TimeExcuteCordinationActivity.Companion.MESSAGE_TIME_EXCUTE_ACTION
import kotlinx.android.synthetic.main.autoagain_scene_act.*


/**
 * Created by zhu on 2018/1/9.
 */

class AutoAgainSceneActivity : BaseActivity() {
    private var againAutoSceneAdapter: AgainAutoSceneAdapter? = null
    private var list_hand_scene =  ArrayList<HashMap<String,Any>>()
    internal var again_elements = arrayOf("每天", "工作日", "周末", "自定义")
    private var condition: String? = null
    private var current_map: Map<*, *> = HashMap<String,Any>()

    override fun viewId(): Int {
        return R.layout.autoagain_scene_act//
    }

    override fun onView() {
        StatusUtils.setFullToStatusBar(this)  // StatusBar.
        list_hand_scene = ArrayList()
        for (element in again_elements) {
            val map = HashMap<String,Any>()
            map.put("name", element)
            when (element) {
                "每天" -> condition = "2"
                "工作日" -> condition = "3"
                "周末" -> condition = "4"
                "自定义" -> condition = "5"
            }
            map.put("type", "0")
            map.put("condition", condition!!)
            list_hand_scene!!.add(map)//
        }
        againAutoSceneAdapter = AgainAutoSceneAdapter(this@AutoAgainSceneActivity, list_hand_scene!!, object : AgainAutoSceneAdapter.AgainAutoSceneListener {
            override fun again_auto_listen(position: Int) {
                current_map = list_hand_scene!![position]
            }
        })
        xListView_scan!!.adapter = againAutoSceneAdapter
    }

    /*
     * 通知
     * */
    private fun sendBroad(map: Map<*, *>) {
        val mIntent = Intent(MESSAGE_TIME_EXCUTE_ACTION)
        mIntent.putExtra("time_map", map as Serializable)
        sendBroadcast(mIntent)
    }

    override fun onEvent() {
        custom_again_time!!.setOnClickListener(this)
        next_step_txt!!.setOnClickListener(this)
        back!!.setOnClickListener(this)
    }

    override fun onData() {

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.custom_again_time -> startActivity(Intent(this@AutoAgainSceneActivity, CustomDefineDaySceneActivity::class.java))
            R.id.next_step_txt//下一步
            -> {
                val map = HashMap<String,Any>()
                if (current_map["name"] == null) {
                    ToastUtil.showToast(this@AutoAgainSceneActivity, "请选择执行条件")
                    return
                }
                map.put("name", current_map["name"]!!)
                map.put("condition", current_map["condition"]!!)
                map.put("minValue", "")
                sendBroad(map)
                //                AutoAgainSceneActivity.this.finish();
                AppManager.getAppManager().finishActivity_current(AutoAgainSceneActivity::class.java)
            }
            R.id.back -> this@AutoAgainSceneActivity.finish()
        }//自定义是哪天执行，
    }
}
