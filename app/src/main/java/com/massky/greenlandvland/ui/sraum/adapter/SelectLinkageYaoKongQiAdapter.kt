package com.massky.greenlandvland.ui.sraum.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.massky.greenlandvland.R
import com.massky.greenlandvland.ui.sraum.Util.DialogUtil
import com.massky.greenlandvland.ui.sraum.activity.LinkageItemYaoKongQiActivity
import com.massky.greenlandvland.ui.sraum.widget.SlideSwitchButton

import java.io.Serializable
import java.util.ArrayList
import java.util.HashMap


/**
 * Created by masskywcy on 2017-05-16.
 */

class SelectLinkageYaoKongQiAdapter(context: AppCompatActivity, list: List<Map<*, *>>, listint: List<Int>, listintwo: List<Int>, internal var dialogUtil: DialogUtil,
                                    internal var refreshListener: RefreshListener) : android.widget.BaseAdapter() {
    private var list: List<Map<*, *>> = ArrayList()
    private var listint: List<Int> = ArrayList()
    private var listintwo: List<Int> = ArrayList()
    private val temp = -1
    private var activity: AppCompatActivity? = null//上下文
    private var sensor_map = HashMap<String,Any>()

    init {
        this.list = list
        this.listint = listint
        this.listintwo = listintwo
        this.activity = context
        this.activity = context
    }
    //
    //    // 初始化isSelected的数据
    //    private void initDate() {
    //        for (int i = 0; i < list_bool.size(); i++) {
    //            getIsSelected().put(i, false);
    //        }
    //    }


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
        var viewHolderContentType: ViewHolderContentType? = null
        if (null == convertView) {
            viewHolderContentType = ViewHolderContentType()
            convertView = LayoutInflater.from(activity).inflate(R.layout.select_linkage_yaokongqi_item, null)
            viewHolderContentType.img_guan_scene = convertView!!.findViewById<View>(R.id.img_guan_scene) as ImageView
            viewHolderContentType.panel_scene_name_txt = convertView.findViewById<View>(R.id.panel_scene_name_txt) as TextView
            viewHolderContentType.execute_scene_txt = convertView.findViewById<View>(R.id.execute_scene_txt) as TextView
            viewHolderContentType.checkbox = convertView.findViewById<View>(R.id.checkbox) as CheckBox
            //gateway_name_txt
            viewHolderContentType.gateway_name_txt = convertView.findViewById<View>(R.id.gateway_name_txt) as TextView
            //            viewHolderContentType.swipemenu_layout = (SwipeMenuLayout) convertView.findViewById(R.id.swipemenu_layout);
            viewHolderContentType.swipe_content_linear = convertView.findViewById<View>(R.id.swipe_content_linear) as LinearLayout

            convertView.tag = viewHolderContentType
        } else {
            viewHolderContentType = convertView.tag as ViewHolderContentType
        }

        viewHolderContentType.panel_scene_name_txt!!.text = list[position]["name"] as String?
        viewHolderContentType.gateway_name_txt!!.text = ""
        viewHolderContentType.img_guan_scene!!.setImageResource(listint[position])
        viewHolderContentType.swipe_content_linear!!.setOnClickListener {
            val map = HashMap<String,Any>()

            map.put("type", list[position]["type"]!!.toString())
            map.put("number", list[position]["number"]!!.toString())
            map.put("status", "")
            map.put("dimmer", "")
            map.put("mode", "")
            map.put("temperature", "")
            map.put("speed", "")
            map.put("name", list[position]["name"]!!.toString())
            map.put("name1", list[position]["name"]!!.toString())
            map.put("action", "")
            map.put("tiaoguang", "")
            map.put("tabname", "")
            map.put("value", "")
            map.put("position", "")
            map.put("panelMac", "")
            map.put("gatewayMac", "")
            map.put("boxNumber", "")
            val intent = Intent(activity, LinkageItemYaoKongQiActivity::class.java)//     Intent intent = new Intent(SelectLinkageYaoKongQiActivity.this,
            // LinkageItemYaoKongQiActivity.class)

            //   Logger.d(TAG, "uid或token为空或ProductKeyList为空或productSecret为空或mac为空,无法绑定");
            intent.putExtra("device_map", map as Serializable)
            intent.putExtra("sensor_map", sensor_map as Serializable)
            activity!!.startActivity(intent)
        }
        return convertView
    }

    fun setLists(list_hand_scene: List<Map<*, *>>, listint: MutableList<Int>, listintwo: MutableList<Int>, sensor_map: HashMap<String, Any>) {
        this.list = list_hand_scene
        this.listint = listint
        this.listintwo = listintwo
        this.sensor_map = sensor_map
    }

    //    public static HashMap<Integer, Boolean> getIsSelected() {
    //        return isSelected;
    //    }
    //
    //    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
    //        SelectSensorSingleAdapter.isSelected = isSelected;
    //    }


    class ViewHolderContentType {
        var img_guan_scene: ImageView? = null
        var panel_scene_name_txt: TextView? = null
        var execute_scene_txt: TextView? = null
        var checkbox: CheckBox? = null
        var gateway_name_txt: TextView? = null
        internal var hand_scene_btn: SlideSwitchButton? = null
        //        SwipeMenuLayout swipemenu_layout;
        internal var swipe_content_linear: LinearLayout? = null
    }

    interface RefreshListener {
        fun refresh()
    }

    companion object {
        // 用来控制CheckBox的选中状况
        private val isSelected = HashMap<Int, Boolean>()
    }
}
