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

/**
 * Created by masskywcy on 2017-05-16.
 */

class AgainAutoDaysSelectSceneAdapter(context: Context, list: List<Map<*, *>>) : BaseAdapter<Map<*, *>>(context, list) {
    //private val list:List<Map<*,*>>= ArrayList()

    private var lists: List<Map<*,*>> = ArrayList()

    init {
        this.lists = list
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        var viewHolderContentType: ViewHolderContentType? = null
        if (null == convertView) {
            viewHolderContentType = ViewHolderContentType()
            convertView = LayoutInflater.from(context).inflate(R.layout.again_auto_days_select_scene_item, null)

            viewHolderContentType.txt_again_autoscene = convertView!!.findViewById<View>(R.id.txt_again_autoscene) as TextView
            viewHolderContentType.img_again_autoscene = convertView.findViewById<View>(R.id.img_again_autoscene) as ImageView
            convertView.tag = viewHolderContentType
        } else {
            viewHolderContentType = convertView.tag as ViewHolderContentType
        }

        //        String type = (String) list.get(position).get("type");
        //        switch (type) {
        //            case "0":
        //                viewHolderContentType.img_again_autoscene.setVisibility(View.GONE);//
        //                break;
        //            case "1":
        //                viewHolderContentType.img_again_autoscene.setVisibility(View.VISIBLE);//
        //                break;
        //        }
        viewHolderContentType.txt_again_autoscene!!.setText(lists.get(position).get("name")!!.toString())

        val finalViewHolderContentType = viewHolderContentType
        //        convertView.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        ////                Intent intent = new Intent(context, ShangChuanBaoJingActivity.class);
        ////                intent.putExtra("id", (Serializable) list.get(position).get("id").toString());
        ////                context.startActivity(intent);
        ////                for (int i = 0; i < list.size(); i++) {
        ////                    list.get(i).put("type", "0");
        ////                    if (i == position) {
        ////                        if (finalViewHolderContentType.img_again_autoscene.getVisibility() == View.VISIBLE) {
        ////                            list.get(i).put("type", "0");
        ////                        } else {
        ////                            list.get(i).put("type", "1");
        ////                        }
        ////                    }
        ////                }
        ////                notifyDataSetChanged();
        //            }
        //        });
        return convertView
    }

    internal inner class ViewHolderContentType {
        var img_again_autoscene: ImageView? = null
        var txt_again_autoscene: TextView? = null
    }
}
