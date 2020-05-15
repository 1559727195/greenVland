package com.massky.greenlandvland.ui.sraum.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.massky.greenlandvland.R
import com.massky.greenlandvland.ui.sraum.Util.SharedPreferencesUtil
import com.mcxtzhang.swipemenulib.SwipeMenuLayout

import java.util.ArrayList
import java.util.HashMap

/**
 * Created by masskywcy on 2017-05-16.
 */

class EditLinkDeviceCondinationAndResultAdapter(private val context: Context, listint: MutableList<Map<*, *>>, internal var excutecuteListener: ExcutecuteListener) : android.widget.BaseAdapter() {

    private var listint: MutableList<Map<*, *>> = ArrayList()
    private val listintwo = ArrayList<String>()
    internal var isSelected = HashMap<Int, Boolean>()

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
            convertView = LayoutInflater.from(context).inflate(R.layout.edit_linkage_condination_result_item, null)
            //viewHolderContentType.img_guan_scene = convertView!!.findViewById<View>(R.id.img_guan_scene) as ImageView
            viewHolderContentType.panel_scene_name_txt = convertView.findViewById<View>(R.id.panel_scene_name_txt) as TextView
            viewHolderContentType.execute_scene_txt = convertView.findViewById<View>(R.id.execute_scene_txt) as TextView
            //gateway_name_txt
            viewHolderContentType.gateway_name_txt = convertView.findViewById<View>(R.id.gateway_name_txt) as TextView
            //tiaoguang_value
            viewHolderContentType.tiaoguang_value = convertView.findViewById<View>(R.id.tiaoguang_value) as TextView
            viewHolderContentType.scene_set = convertView.findViewById<View>(R.id.scene_set) as ImageView
            //viewHolderContentType.hand_scene_btn = convertView.findViewById<View>(R.id.slide_btn) as SlideSwitchButton
            viewHolderContentType.swipemenu_layout = convertView.findViewById<View>(R.id.swipemenu_layout) as SwipeMenuLayout
            viewHolderContentType.delete_btn = convertView.findViewById<View>(R.id.delete_btn) as Button
            convertView.tag = viewHolderContentType
            //            viewHolderContentType.tiaoguang_value.setTag(R.id.tiaoguang_open, position_index++);
        } else {
            viewHolderContentType = convertView.tag as ViewHolderContentType
        }


        //        int element = (Integer) list.get(position).get("image");
        //        viewHolderContentType.img_guan_scene.setImageResource(element);
        viewHolderContentType.panel_scene_name_txt!!.text = listint[position]["name1"]!!.toString()
        //        switch (listint.get(position).get("name1").toString()) {
        //            case "手动执行":
        //                viewHolderContentType.swipemenu_layout.setLeftSwipe(false);
        //                break;
        //            default:
        //                viewHolderContentType.swipemenu_layout.setLeftSwipe(true);
        //                break;
        //        }
        viewHolderContentType.execute_scene_txt!!.text = listint[position]["action"]!!.toString()
        if (listint[position]["boxName"] == null || listint[position]["boxName"]!!.toString() == "") {
            viewHolderContentType.gateway_name_txt!!.visibility = View.GONE
        } else {
            viewHolderContentType.gateway_name_txt!!.text = listint[position]["boxName"]!!.toString()
        }

        val finalViewHolderContentType = viewHolderContentType
        viewHolderContentType.delete_btn!!.setOnClickListener {
            if (listint[position]["condition"] != null) {//是执行条件
                delete_item("list_condition", position)
            } else {//执行结果
                delete_item("list_result", position)
            }

            finalViewHolderContentType.swipemenu_layout!!.quickClose()
            //                    delete_item();
        }

        return convertView!!
    }

    /**
     * 是删除条件还是结果
     *
     * @param excute_condition
     * @param position
     */
    private fun delete_item(excute_condition: String, position: Int) {
        //        switch (excute_condition) {
        //            case "执行条件":
        //
        //                break;
        //            case "执行结果":
        //
        //                break;
        //        }
        listint.removeAt(position)
        notifyDataSetChanged()
        excutecuteListener.excute_cordition()
        SharedPreferencesUtil.remove_current_index(context, excute_condition, position)
    }

    fun setlist(listint: MutableList<Map<*, *>>) {
        this.listint = listint
        notifyDataSetChanged()
    }


    internal inner class ViewHolderContentType {
      //  var img_guan_scene: ImageView? = null
        var panel_scene_name_txt: TextView? = null//name
        var execute_scene_txt: TextView? = null//action
        var gateway_name_txt: TextView? = null
        var tiaoguang_value: TextView? = null
        var scene_set: ImageView? = null
        //var hand_scene_btn: SlideSwitchButton? = null
        var swipemenu_layout: SwipeMenuLayout? = null
        var delete_btn: Button? = null
    }


    interface ExcutecuteListener {
        fun excute_cordition()

        fun excute_result()
    }

    companion object {

        //    public static HashMap<Integer, Boolean> getIsSelected() {
        //        return isSelected;
        //    }

        fun setIsSelected(isSelected: HashMap<Int, Boolean>) {
            var isSelected = isSelected
            isSelected = isSelected
        }
    }
}
