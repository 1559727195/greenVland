package com.massky.greenlandvland.ui.sraum.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.massky.greenlandvland.R
import com.massky.greenlandvland.ui.adapter.BaseAdapter
import java.util.ArrayList
import java.util.HashMap

/**
 * Created by masskywcy on 2017-05-16.
 */

class ExecuteSomeHandSceneAdapter(context: Context, list: List<Map<*, *>>) : BaseAdapter<Map<*,*>>(context, list) {
    private var lists : List<Map<*, *>>  = ArrayList()

    init {
        this.lists = list
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        var viewHolderContentType: ViewHolderContentType? = null
        if (null == convertView) {
            viewHolderContentType = ViewHolderContentType()
            convertView = LayoutInflater.from(context).inflate(R.layout.excutesomehand_scene_item, null)

            viewHolderContentType.txt_again_autoscene = convertView!!.findViewById<View>(R.id.txt_again_autoscene) as TextView
            viewHolderContentType.img_again_autoscene = convertView.findViewById<View>(R.id.img_again_autoscene) as ImageView
            //device_type_pic
            viewHolderContentType.device_type_pic = convertView.findViewById<View>(R.id.device_type_pic) as ImageView
            convertView.tag = viewHolderContentType
        } else {
            viewHolderContentType = convertView.tag as ViewHolderContentType
        }

        val type = lists.get(position).get("type") as String
        when (type) {
            "0" -> viewHolderContentType.img_again_autoscene!!.visibility = View.GONE//
            "1" -> viewHolderContentType.img_again_autoscene!!.visibility = View.VISIBLE//
        }
        viewHolderContentType.txt_again_autoscene!!.setText(lists.get(position).get("name")!!.toString())

        val element = lists.get(position).get("image") as Int
        viewHolderContentType.device_type_pic!!.setImageResource(element)

        val finalViewHolderContentType = viewHolderContentType
        convertView.setOnClickListener {
            //                Intent intent = new Intent(context, ShangChuanBaoJingActivity.class);
            //                intent.putExtra("id", (Serializable) list.get(position).get("id").toString());
            //                context.startActivity(intent);
            for (i in lists.indices) {
             //   lists.get(i).put("type", "0")
                (lists[position] as HashMap<String, Any>)["type"] = "0"
                if (i == position) {
                    if (finalViewHolderContentType.img_again_autoscene!!.visibility == View.VISIBLE) {
                        //lists.get(i).put("type", "0")
                        (lists[position] as HashMap<String, Any>)["type"] = "0"
                    } else {
                        //lists.get(i).put("type", "1")
                        (lists[position] as HashMap<String, Any>)["type"] = "1"
                    }
                }
            }
            notifyDataSetChanged()
        }
        return convertView
    }

    internal inner class ViewHolderContentType {
        var device_type_pic: ImageView? = null
        var img_again_autoscene: ImageView? = null
        var txt_again_autoscene: TextView? = null
    }
}
