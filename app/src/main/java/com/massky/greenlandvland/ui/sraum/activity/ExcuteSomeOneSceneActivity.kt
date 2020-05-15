package com.massky.greenlandvland.ui.sraum.activity

import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.massky.greenlandvland.R
import com.massky.greenlandvland.common.SharedPreferencesUtils
import com.massky.greenlandvland.ui.sraum.AddTogenInterface.AddTogglenInterfacer
import com.massky.greenlandvland.ui.sraum.User
import com.massky.greenlandvland.ui.sraum.Util.DialogUtil
import com.massky.greenlandvland.ui.sraum.Util.MyOkHttp
import com.massky.greenlandvland.ui.sraum.Util.Mycallback
import com.massky.greenlandvland.ui.sraum.Util.SharedPreferencesUtil
import com.massky.greenlandvland.ui.sraum.Utils.ApiHelper
import com.massky.greenlandvland.ui.sraum.Utils.AppManager
import com.massky.greenlandvland.ui.sraum.adapter.ExcuteOneSceneAdapter
import com.massky.greenlandvland.ui.sraum.base.BaseActivity
import com.massky.greenlandvland.ui.sraum.view.PullToRefreshLayout
import com.yanzhenjie.statusview.StatusUtils
import com.yanzhenjie.statusview.StatusView
import java.io.Serializable
import java.util.ArrayList
import java.util.HashMap
import butterknife.BindView
import kotlinx.android.synthetic.main.excute_someone_scenelay.*
import okhttp3.Call

/**
 * Created by zhu on 2018/6/27.
 */

class ExcuteSomeOneSceneActivity : BaseActivity(), AdapterView.OnItemClickListener, PullToRefreshLayout.OnRefreshListener {

    private var dialogUtil: DialogUtil? = null
    private var scenelist: MutableList<User.scenelist> = ArrayList()
    private val listint = ArrayList<Int>()
    private val listtype = ArrayList<String>()
    private var adapter: ExcuteOneSceneAdapter? = null
    private var sensor_map: Map<*, *> = HashMap<String,Any>()//传感器map
    private val listbox = ArrayList<String>()

    override fun viewId(): Int {
        return R.layout.excute_someone_scenelay
    }

    override fun onView() {
        dialogUtil = DialogUtil(this)
        StatusUtils.setFullToStatusBar(this)  // StatusBar.
        back!!.setOnClickListener(this)
        maclistview_id!!.onItemClickListener = this
        refresh_view!!.setOnRefreshListener(this)
        sensor_map = intent.getSerializableExtra("sensor_map") as Map<*, *>
        //        refresh_view.autoRefresh();
        dialogUtil = DialogUtil(this)
        ondata()
    }

    override fun onEvent() {

    }

    override fun onData() {

    }

    private fun ondata() {

        adapter = ExcuteOneSceneAdapter(this@ExcuteSomeOneSceneActivity, scenelist, listint, false)
        maclistview_id!!.adapter = adapter
        maclistview_id!!.adapter = adapter
        //        xListView_scan.setPullLoadEnable(false);
        //        xListView_scan.setFootViewHide();
        //        xListView_scan.setXListViewListener(this);

        //        uploader_refresh();
        getData()
    }

    private fun getData() {

        val map = HashMap<String, Any>()
        map["token"] = SharedPreferencesUtils.getToken(this@ExcuteSomeOneSceneActivity)
        val areaNumber = SharedPreferencesUtil.getData(this@ExcuteSomeOneSceneActivity, "areaNumber",
                "") as String
        map["areaNumber"] = areaNumber
        MyOkHttp.postMapObject(ApiHelper.sraum_getLinkScene, map, object : Mycallback(AddTogglenInterfacer {
            val mapdevice = HashMap<String, Any>()
            val boxNumber = SharedPreferencesUtil.getData(this@ExcuteSomeOneSceneActivity, "boxnumber", "") as String
            mapdevice["token"] = SharedPreferencesUtils.getToken(this@ExcuteSomeOneSceneActivity)
            mapdevice["boxNumber"] = boxNumber
            getData()
        }, this@ExcuteSomeOneSceneActivity, dialogUtil) {
            override fun onError(call: Call, e: Exception, id: Int) {
                super.onError(call, e, id)

            }

            override fun onSuccess(user: User) {
                super.onSuccess(user)
                scenelist.clear()
                listint.clear()
                scenelist = user.sceneList
                for (ud in user.sceneList) {
                    listtype.add(ud.sceneStatus)
                }
                for (us in scenelist) {
                    setPicture(us.sceneType)
                    listbox.add(us.boxName)
                }
                adapter = ExcuteOneSceneAdapter(this@ExcuteSomeOneSceneActivity, scenelist, listint, false)
                maclistview_id!!.adapter = adapter
            }

            override fun wrongToken() {
                super.wrongToken()
            }
        })
    }

    private fun setPicture(type: String) {
        when (type) {
            "1" -> listint.add(R.drawable.add_scene_homein)
            "2" -> listint.add(R.drawable.add_scene_homeout)
            "3" -> listint.add(R.drawable.add_scene_sleep)
            "4" -> listint.add(R.drawable.add_scene_nightlamp)
            "5" -> listint.add(R.drawable.add_scene_getup)
            "6" -> listint.add(R.drawable.add_scene_cup)
            "7" -> listint.add(R.drawable.add_scene_book)
            "8" -> listint.add(R.drawable.add_scene_moive)
            "9" -> listint.add(R.drawable.add_scene_meeting)
            "10" -> listint.add(R.drawable.add_scene_cycle)
            "11" -> listint.add(R.drawable.add_scene_noddle)
            "12" -> listint.add(R.drawable.add_scene_lampon)
            "13" -> listint.add(R.drawable.add_scene_lampoff)
            "14" -> listint.add(R.drawable.defaultpic)
            else -> listint.add(R.drawable.defaultpic)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.back -> this@ExcuteSomeOneSceneActivity.finish()
        }
    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        val sceneId = scenelist[position].sceneId
        val sceneName = scenelist[position].sceneName
        val sceneType = scenelist[position].sceneType


        //    String scenelist.get(position);
        val map_value = HashMap<String,Any>()
        map_value.put("name", "场景")
        map_value.put("type", "100")
        map_value.put("name1", "场景")
        map_value.put("action", sceneName)
        map_value.put("boxName", listbox.get(position))
        map_value.put("number", sceneId)
        map_value.put("status", "1")
        map_value.put("dimmer", "")
        map_value.put("mode", "")
        map_value.put("temperature", "")
        map_value.put("speed", "")
        map_value.put("panelMac", "")
        map_value.put("gatewayMac", "")


        AppManager.getAppManager().finishActivity_current(SelectiveLinkageActivity::class.java)
        AppManager.getAppManager().finishActivity_current(EditLinkDeviceResultActivity::class.java)
        val intent = Intent(this@ExcuteSomeOneSceneActivity, EditLinkDeviceResultActivity::class.java)
        intent.putExtra("device_map", map_value as Serializable)
        intent.putExtra("sensor_map", sensor_map as Serializable)
        startActivity(intent)
        this@ExcuteSomeOneSceneActivity.finish()
    }

    override fun onRefresh(pullToRefreshLayout: PullToRefreshLayout) {
        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED)
        getData()
    }

    override fun onLoadMore(pullToRefreshLayout: PullToRefreshLayout) {

    }
}
