package com.massky.greenlandvland.ui.sraum.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.massky.greenlandvland.R
import com.massky.greenlandvland.ui.adapter.BaseAdapter
import com.massky.greenlandvland.ui.sraum.activity.GuanLianSceneRealBtnActivity

import java.util.ArrayList

/**
 * Created by masskywcy on 2017-05-16.
 */

class GuanLianSceneAdapter(context: Context, list: ArrayList<Map<String, Any>>) : BaseAdapter<Map<String,Any>>(context, list) {
    private var lists = ArrayList<Map<String, Any>>()

    init {
        this.lists = list
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        var viewHolderContentType: ViewHolderContentType? = null
        if (null == convertView) {
            viewHolderContentType = ViewHolderContentType()
            convertView = LayoutInflater.from(context).inflate(R.layout.guanlian_scene_item, null)
            viewHolderContentType.img_guan_scene = convertView!!.findViewById<View>(R.id.img_guan_scene) as ImageView
            viewHolderContentType.panel_scene_name_txt = convertView.findViewById<View>(R.id.panel_scene_name_txt) as TextView
            viewHolderContentType.execute_scene_txt = convertView.findViewById<View>(R.id.execute_scene_txt) as TextView
            convertView.tag = viewHolderContentType
        } else {
            viewHolderContentType = convertView.tag as ViewHolderContentType
        }

        val element = lists.get(position).get("image") as Int
        viewHolderContentType.img_guan_scene!!.setImageResource(element)
        viewHolderContentType.panel_scene_name_txt!!.setText(lists.get(position).get("name")!!.toString())

        convertView.setOnClickListener {
            val intent = Intent(context, GuanLianSceneRealBtnActivity::class.java)
            context.startActivity(intent)
        }
        return convertView
    }

    internal inner class ViewHolderContentType {
        var img_guan_scene: ImageView? = null
        var panel_scene_name_txt: TextView? = null
        var execute_scene_txt: TextView? = null
    }
}
