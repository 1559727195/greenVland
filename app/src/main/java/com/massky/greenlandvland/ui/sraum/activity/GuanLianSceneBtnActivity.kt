package com.massky.greenlandvland.ui.sraum.activity

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.massky.greenlandvland.R
import com.massky.greenlandvland.ui.sraum.base.BaseActivity
import com.yanzhenjie.statusview.StatusUtils
import java.io.Serializable
import java.util.ArrayList
import butterknife.BindView
import kotlinx.android.synthetic.main.guanlian_scene_btn.*

/**
 * Created by zhu on 2018/1/8.
 */

class GuanLianSceneBtnActivity : BaseActivity() {

    private var list_result: Serializable? = ArrayList<Any>()
    override fun viewId(): Int {
        return R.layout.guanlian_scene_btn
    }

    override fun onView() {
        StatusUtils.setFullToStatusBar(this)  // StatusBar.
    }

    override fun onEvent() {
        rel_scene_set!!.setOnClickListener(this)
        back!!.setOnClickListener(this)
    }

    override fun onData() {
        next_step_txt!!.setOnClickListener(this)
        val intent = intent ?: return
//        String excute = (String) intent.getSerializableExtra("excute");
        //
        //        switch (excute) {
        //            case "auto"://自动
        //                rel_scene_set.setVisibility(View.GONE);
        //                break;
        //            default:
        //                rel_scene_set.setVisibility(View.VISIBLE);
        //                break;
        //        }
        list_result = intent.getSerializableExtra("deviceList")

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.rel_scene_set ->
                //                GuanLianSceneActivity
                startActivity(Intent(this@GuanLianSceneBtnActivity,
                        GuanLianSceneActivity::class.java))
            R.id.next_step_txt -> {
            }
            R.id.back -> this@GuanLianSceneBtnActivity.finish()
        }//GuanLianSceneBtnActivity.this.finish();
        //ApplicationContext.getInstance().finishButActivity(MainGateWayActivity.class);
    }
}
