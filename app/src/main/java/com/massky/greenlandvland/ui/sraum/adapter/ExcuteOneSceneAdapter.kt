package com.massky.greenlandvland.ui.sraum.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.massky.greenlandvland.R
import com.massky.greenlandvland.ui.sraum.User
import java.util.ArrayList


/**
 * Created by masskywcy on 2017-03-21.
 */
//我的场景adapter
class ExcuteOneSceneAdapter(private val context: Context, list: List<User.scenelist>, listint: List<Int>,
                            private var viewFlag: Boolean) : android.widget.BaseAdapter() {
    private var list: List<User.scenelist> = ArrayList()
    private var listint: List<Int> = ArrayList()
    private var listintwo: List<Int> = ArrayList()

    init {
        this.list = list
        this.listint = listint
        this.listintwo = listintwo
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        var viewHolderContentType: ViewHolder? = null
        if (null == convertView) {
            viewHolderContentType = ViewHolder()
            convertView = LayoutInflater.from(context).inflate(R.layout.ecute_somescene_item, null)
            viewHolderContentType.img_guan_scene = convertView!!.findViewById<View>(R.id.img_guan_scene) as ImageView
            viewHolderContentType.panel_scene_name_txt = convertView.findViewById<View>(R.id.panel_scene_name_txt) as TextView
            viewHolderContentType.execute_scene_txt = convertView.findViewById<View>(R.id.execute_scene_txt) as TextView
            viewHolderContentType.checkbox = convertView.findViewById<View>(R.id.checkbox) as CheckBox
            //gateway_name_txt
            viewHolderContentType.gateway_name_txt = convertView.findViewById<View>(R.id.gateway_name_txt) as TextView
            convertView.tag = viewHolderContentType
        } else {
            viewHolderContentType = convertView.tag as ViewHolder
        }

        viewHolderContentType.panel_scene_name_txt!!.text = list[position].sceneName
        viewHolderContentType.gateway_name_txt!!.text = list[position].boxName

        viewHolderContentType.img_guan_scene!!.setImageResource(listint[position])

        return convertView
    }

    fun setList_s(scenelist: List<User.scenelist>, listint: List<Int>, listintwo: List<Int>, b: Boolean) {
        this.list = ArrayList<User.scenelist>()
        this.listint = ArrayList()
        this.listintwo = ArrayList()
        this.list = scenelist
        this.listint = listint
        this.listintwo = listintwo
        this.viewFlag = b
    }

    internal inner class ViewHolder {
        var img_guan_scene: ImageView? = null
        var panel_scene_name_txt: TextView? = null
        var execute_scene_txt: TextView? = null
        var checkbox: CheckBox? = null
        var gateway_name_txt: TextView? = null
    }

}