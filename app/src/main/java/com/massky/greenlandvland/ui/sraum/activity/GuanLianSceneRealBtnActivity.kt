package com.massky.greenlandvland.ui.sraum.activity

import android.view.View
import android.widget.ImageView
import android.widget.TextView


import com.massky.greenlandvland.R
import com.massky.greenlandvland.ui.sraum.base.BaseActivity
import com.yanzhenjie.statusview.StatusUtils

import butterknife.BindView
import kotlinx.android.synthetic.main.guanlian_scenereal_btn.*

/**
 * Created by zhu on 2018/1/8.
 */

class GuanLianSceneRealBtnActivity : BaseActivity() {


    override fun viewId(): Int {
        return R.layout.guanlian_scenereal_btn
    }

    override fun onView() {
        StatusUtils.setFullToStatusBar(this)
    }

    override fun onEvent() {
        back!!.setOnClickListener(this)
        next_step_txt!!.setOnClickListener(this)
    }

    override fun onData() {

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.back -> this@GuanLianSceneRealBtnActivity.finish()
            R.id.next_step_txt//保存
            ->
                //                ApplicationContext.getInstance().finishActivity(GuanLianSceneActivity.class);
                this@GuanLianSceneRealBtnActivity.finish()
        }
    }
}
