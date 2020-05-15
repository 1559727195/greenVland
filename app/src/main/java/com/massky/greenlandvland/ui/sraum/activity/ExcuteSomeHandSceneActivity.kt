package com.massky.greenlandvland.ui.sraum.activity

import android.content.Intent
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.massky.greenlandvland.R
import com.massky.greenlandvland.View.XListView
import com.massky.greenlandvland.ui.sraum.base.BaseActivity
import com.yanzhenjie.statusview.StatusUtils
import java.util.ArrayList
import java.util.HashMap
import butterknife.BindView
import com.massky.greenlandvland.ui.sraum.adapter.ExecuteSomeHandSceneAdapter
import kotlinx.android.synthetic.main.execute_somehand_scene.*

/**
 * Created by zhu on 2018/1/12.
 */

class ExcuteSomeHandSceneActivity : BaseActivity(), XListView.IXListViewListener {

    private var executesome_handsceneadapter: ExecuteSomeHandSceneAdapter? = null
    internal var again_elements = arrayOf("全屋灯光全开", "全屋灯光全关", "回家模式", "离家", "吃饭模式", "看电视", "睡觉")
    private var list_hand_scene: MutableList<Map<*, *>> = ArrayList()
    private val mHandler = Handler()

    override fun viewId(): Int {
        return R.layout.execute_somehand_scene
    }

    override fun onView() {
        StatusUtils.setFullToStatusBar(this)  // StatusBar.
    }

    override fun onEvent() {
        next_step_txt!!.setOnClickListener(this)
        back!!.setOnClickListener(this)
    }

    override fun onData() {
        list_hand_scene = ArrayList()
        for (element in again_elements) {
            val map = HashMap<String,Any>()
            map.put("name", element)
            map.put("type", "0")
            when (element) {
                "全屋灯光全开" -> map.put("image", R.drawable.icon_deng_sm)
                "全屋灯光全关" -> map.put("image", R.drawable.icon_guandeng_sm)
                "回家模式" -> map.put("image", R.drawable.icon_huijia_sm)
                "离家" -> map.put("image", R.drawable.icon_lijia_sm)
                "吃饭模式" -> map.put("image", R.drawable.icon_chifan_sm)
                "看电视" -> map.put("image", R.drawable.icon_kandiashi_sm)
                "睡觉" -> map.put("image", R.drawable.icon_shuijiao_sm)
                "K歌" -> map.put("image", R.drawable.icon_kge_sm)
            }
            list_hand_scene.add(map)
        }

        executesome_handsceneadapter = ExecuteSomeHandSceneAdapter(this@ExcuteSomeHandSceneActivity, list_hand_scene)
        xListView_scan!!.adapter = executesome_handsceneadapter
        xListView_scan!!.setPullLoadEnable(false)
        xListView_scan!!.setFootViewHide()
        xListView_scan!!.setXListViewListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.back -> this@ExcuteSomeHandSceneActivity.finish()
            R.id.next_step_txt -> {
                val intent = Intent(this@ExcuteSomeHandSceneActivity,
                        GuanLianSceneBtnActivity::class.java)
                intent.putExtra("excute", "auto")//自动的
                startActivity(intent)
            }
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
