package com.massky.greenlandvland.ui.sraum.activity

import android.os.Handler
import android.view.View
import android.widget.ImageView
import com.massky.greenlandvland.R
import com.massky.greenlandvland.View.XListView
import com.massky.greenlandvland.ui.sraum.adapter.GuanLianSceneAdapter
import com.massky.greenlandvland.ui.sraum.base.BaseActivity
import com.yanzhenjie.statusview.StatusUtils
import java.util.ArrayList
import java.util.HashMap

import butterknife.BindView
import kotlinx.android.synthetic.main.guanlian_scene.*

/**
 * Created by zhu on 2018/1/8.
 */

class GuanLianSceneActivity : BaseActivity(), XListView.IXListViewListener {
    private var list_hand_scene =  ArrayList<Map<String,Any>>()
    private var guanlianSceneAdapter: GuanLianSceneAdapter? = null

    private val mHandler = Handler()
    internal var again_elements = arrayOf("客厅开关", "主卧开关", "儿童房开关", "书房开关", "客厅窗帘", "餐厅开关")

    override fun viewId(): Int {
        return R.layout.guanlian_scene
    }

    override fun onView() {
        StatusUtils.setFullToStatusBar(this)  // StatusBar.
        list_hand_scene = ArrayList()
        for (element in again_elements) {
            val map = HashMap<String,Any>()
            map.put("name", element)
            when (element) {
                "客厅开关" -> map.put("image", R.drawable.icon_deng_sm)
                "主卧开关" -> map.put("image", R.drawable.icon_deng_sm)
                "儿童房开关" -> map.put("image", R.drawable.icon_deng_sm)
                "书房开关" -> map.put("image", R.drawable.icon_deng_sm)
                "客厅窗帘" -> map.put("image", R.drawable.icon_chuanglian_sm)
                "餐厅开关" -> map.put("image", R.drawable.icon_deng_sm)
            }
            list_hand_scene!!.add(map)
        }

        guanlianSceneAdapter = GuanLianSceneAdapter(this@GuanLianSceneActivity, list_hand_scene)
        xListView_scan!!.adapter = guanlianSceneAdapter
        xListView_scan!!.setPullLoadEnable(false)
        xListView_scan!!.setFootViewHide()
        xListView_scan!!.setXListViewListener(this)
    }

    override fun onEvent() {
        back!!.setOnClickListener(this)
    }

    override fun onData() {

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.back -> this@GuanLianSceneActivity.finish()
        }
    }

    private fun onLoad() {
        xListView_scan!!.stopRefresh()
        xListView_scan!!.stopLoadMore()
        xListView_scan!!.setRefreshTime("刚刚")
    }

    override fun onRefresh() {
        onLoad()
    }

    override fun onLoadMore() {
        mHandler.postDelayed({ onLoad() }, 1000)
    }
}
