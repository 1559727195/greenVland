package com.massky.greenlandvland.ui.sraum.activity

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.massky.greenlandvland.R
import com.massky.greenlandvland.ui.sraum.Utils.AppManager
import com.massky.greenlandvland.ui.sraum.base.BaseActivity
import com.yanzhenjie.statusview.StatusUtils
import com.yanzhenjie.statusview.StatusView
import java.io.Serializable
import java.util.HashMap
import butterknife.BindView
import kotlinx.android.synthetic.main.linkage_item_yaokongqi_lay.*

/**
 * Created by zhu on 2018/6/19.
 */

class LinkageItemYaoKongQiActivity : BaseActivity() {

    private var device_map = HashMap<String,Any>()
    private var sensor_map = HashMap<String,Any>()//传感器map


    override fun viewId(): Int {
        return R.layout.linkage_item_yaokongqi_lay
    }

    override fun onView() {
        StatusUtils.setFullToStatusBar(this)  // StatusBar.
        back!!.setOnClickListener(this)
        next_step_txt!!.setOnClickListener(this)
        onEvent()
        //        onData();
    }

    override fun onData() {
        device_map = intent.getSerializableExtra("device_map") as HashMap<String,Any>
        sensor_map = intent.getSerializableExtra("sensor_map") as HashMap<String,Any>

    }

    override fun onEvent() {
        rel_fangdao_open!!.setOnClickListener(this)
        rel_fangdao_close!!.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        val intent: Intent? = null
        when (v.id) {
            R.id.back -> this@LinkageItemYaoKongQiActivity.finish()
            R.id.rel_fangdao_open ->
                //                intent.putExtra("device_map", (Serializable) map);
                //                intent.putExtra("sensor_map", (Serializable) sensor_map);
                into_editlink_device_result_act("1", "开")
            R.id.rel_fangdao_close -> into_editlink_device_result_act("0", "关")
        }
    }

    private fun into_editlink_device_result_act(status: String, action: String) {
        val intent: Intent
        device_map["status"] = status
        device_map["action"] = action
        AppManager.getAppManager().finishActivity_current(SelectiveLinkageActivity::class.java)
        AppManager.getAppManager().finishActivity_current(EditLinkDeviceResultActivity::class.java)
        intent = Intent(
                this@LinkageItemYaoKongQiActivity,
                EditLinkDeviceResultActivity::class.java
        )
        intent.putExtra("device_map", device_map as Serializable)
        intent.putExtra("sensor_map", sensor_map as Serializable)
        startActivity(intent)
        this@LinkageItemYaoKongQiActivity.finish()
    }
}
