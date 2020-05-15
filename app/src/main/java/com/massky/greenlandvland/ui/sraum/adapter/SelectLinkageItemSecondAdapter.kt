package com.massky.greenlandvland.ui.sraum.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.massky.greenlandvland.R
import java.util.ArrayList
import java.util.HashMap

/**
 * Created by masskywcy on 2017-05-16.
 */

class SelectLinkageItemSecondAdapter(private val context: Context, listint: List<Map<*, *>>) : android.widget.BaseAdapter() {

    private var listint: List<Map<*, *>> = ArrayList()
    private val listintwo = ArrayList<String>()

    init {

        this.listint = listint
    }


    override fun getCount(): Int {
        return listint.size
    }

    override fun getItem(position: Int): Any {
        return listint[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        var viewHolderContentType: ViewHolderContentType? = null
        if (null == convertView) {
            viewHolderContentType = ViewHolderContentType()
            convertView = LayoutInflater.from(context).inflate(R.layout.select_linkage_second_item, null)
            viewHolderContentType.img_guan_scene = convertView!!.findViewById<View>(R.id.img_guan_scene) as ImageView
            viewHolderContentType.panel_scene_name_txt = convertView.findViewById<View>(R.id.panel_scene_name_txt) as TextView
            viewHolderContentType.execute_scene_txt = convertView.findViewById<View>(R.id.execute_scene_txt) as TextView
            //gateway_name_txt
            viewHolderContentType.gateway_name_txt = convertView.findViewById<View>(R.id.gateway_name_txt) as TextView
            //tiaoguang_value
            viewHolderContentType.tiaoguang_value = convertView.findViewById<View>(R.id.tiaoguang_value) as TextView
            viewHolderContentType.scene_set = convertView.findViewById<View>(R.id.scene_set) as ImageView
            convertView.tag = viewHolderContentType
            //            viewHolderContentType.tiaoguang_value.setTag(R.id.tiaoguang_open, position_index++);
        } else {
            viewHolderContentType = convertView.tag as ViewHolderContentType
        }

        //        int element = (Integer) list.get(position).get("image");
        //        viewHolderContentType.img_guan_scene.setImageResource(element);
        viewHolderContentType.panel_scene_name_txt!!.text = listint[position]["name"]!!.toString()

        if (listint[position]["tabname"] != null)
            viewHolderContentType.gateway_name_txt!!.text = listint[position]["tabname"]!!.toString()
        //        convertView.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        ////                Intent intent = new Intent(context, DeviceExcuteOpenActivity.class);
        ////                context.startActivity(intent);
        //
        //
        //            }
        //        });
        //        viewHolderContentType.panel_scene_name_txt.setText(list.get(position).panelName);
        //        viewHolderContentType.gateway_name_txt.setText(list.get(position).boxName);
        //            viewHolderContentType.checkbox.setChecked(getIsSelected().get(position));
        //            if (getIsSelected().get(position)) {
        //                viewHolderContentType.img_guan_scene.setImageResource(listintwo.get(position));
        //            } else {
        //                viewHolderContentType.img_guan_scene.setImageResource(listint.get(position));
        //            }
        if (listint[position]["value"] != null)
            when (listint[position]["value"]!!.toString()) {
                "" -> {
                    val tiaoguang = listint[position]["tiaoguang"] as String?
                    if (tiaoguang != null) {
                        when (tiaoguang) {
                            "open" -> viewHolderContentType.scene_set!!.setImageResource(R.drawable.wode_right_arrow)
                            "close" -> viewHolderContentType.scene_set!!.setImageResource(R.drawable.btn_xiala)
                        }
                    }
                    //              convertView.setTag(R.id.tiaoguang_open,"close");
                    viewHolderContentType.tiaoguang_value!!.text = ""//backright
                }
                else -> {
                    viewHolderContentType.tiaoguang_value!!.text = "调光灯值" + listint[position]["value"]!!.toString()
                    viewHolderContentType.panel_scene_name_txt!!.text = ""
                    viewHolderContentType.gateway_name_txt!!.text = ""
                    viewHolderContentType.scene_set!!.setImageResource(R.drawable.wode_right_arrow)
                }
            }

        return convertView
    }

    fun setlist(listint: List<Map<*, *>>) {

        this.listint = listint

    }


    internal inner class ViewHolderContentType {
        var img_guan_scene: ImageView? = null
        var panel_scene_name_txt: TextView? = null
        var execute_scene_txt: TextView? = null
        var gateway_name_txt: TextView? = null
        var tiaoguang_value: TextView? = null
        var scene_set: ImageView? = null
    }

    companion object {
        // 用来控制CheckBox的选中状况
        var isSelected = HashMap<Int, Boolean>()
            set(isSelected) {
                var isSelected = isSelected
                isSelected = isSelected
            }
    }
}
