package com.massky.greenlandvland.ui.sraum.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView


import com.massky.greenlandvland.R
import com.massky.greenlandvland.ui.adapter.BaseAdapter
import com.massky.greenlandvland.ui.sraum.User
import com.massky.greenlandvland.ui.sraum.Util.LogUtil

import butterknife.BindView
import butterknife.ButterKnife

/**
 * Created by masskywcy on 2017-03-22.
 */
//关联面板适配器管理
class AsccociatedpanelAdapter(context: Context, list: ArrayList<User.panellist>, private val checkList: List<Boolean>) : BaseAdapter<User.panellist>(context, list) {
    private var lists = ArrayList<User.panellist>()

    init {
        this.lists = list
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        var mHolder: ViewHolder? = null
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.addsignitem, null)
            mHolder = ViewHolder(convertView)
            convertView!!.tag = mHolder
        } else {
            mHolder = convertView.tag as ViewHolder
        }
        val type = lists.get(position).type.toString().trim({ it <= ' ' })
        val name = lists.get(position).name.toString().trim({ it <= ' ' })
        LogUtil.eLength("数据查看", name + "数据查看" + type)
        when (type) {
            "A201" -> mHolder.imageone!!.setImageResource(R.drawable.mianban01)
            "A202" -> mHolder.imageone!!.setImageResource(R.drawable.mianban02)
            "A203" -> mHolder.imageone!!.setImageResource(R.drawable.mianban03)
            "A204" -> mHolder.imageone!!.setImageResource(R.drawable.mianban04)
            "A302" -> mHolder.imageone!!.setImageResource(R.drawable.mianban04)
            "A301" -> mHolder.imageone!!.setImageResource(R.drawable.mianban04)
            else -> mHolder.imageone!!.setImageResource(R.drawable.mianban04)
        }
        mHolder.macname_id!!.setText(lists.get(position).name)
        mHolder.cb!!.isChecked = checkList[position]
        return convertView
    }

    internal inner class ViewHolder(view: View) {
        @BindView(R.id.imageone)
        var imageone: ImageView? = null
        @BindView(R.id.macname_id)
        var macname_id: TextView? = null
        @BindView(R.id.checkbox)
        var cb: CheckBox? = null

        init {
            ButterKnife.bind(this, view)
        }
    }
}
