package com.massky.greenlandvland.ui.adapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.massky.greenlandvland.R
import com.massky.greenlandvland.common.SharedPreferencesUtils

import com.massky.greenlandvland.common.ToastUtil
import com.massky.greenlandvland.ui.sraum.AddTogenInterface.AddTogglenInterfacer
import com.massky.greenlandvland.ui.sraum.activity.AssociatedpanelActivity
import com.massky.greenlandvland.ui.sraum.User
import com.massky.greenlandvland.ui.sraum.Util.*
import com.massky.greenlandvland.ui.sraum.Utils.ApiHelper
import com.massky.greenlandvland.ui.sraum.activity.EditLinkDeviceResultActivity
import com.massky.greenlandvland.ui.sraum.view.ClearLengthEditText
import com.massky.greenlandvland.util.IntentUtil
import com.massky.greenlandvland.util.SharedPreferencesUtil
import com.mcxtzhang.swipemenulib.SwipeMenuLayout

import java.io.Serializable
import java.util.ArrayList
import java.util.HashMap
import okhttp3.Call

/**
 * Created by masskywcy on 2017-05-16.
 */

class HandSceneAdapter(context: Context, list: List<Map<*,*>>,
                       listint: List<Int>,
                       listintwo: List<Int>,
                       private var dialogUtil: DialogUtil,
                       private var vibflag: Boolean,
                       private var musicflag: Boolean, private val refreshListener: RefreshListener?) : BaseAdapter<Map<*,*>>(context, list) {
    private var lists:List<Map<*,*>> = ArrayList<Map<*,*>>()
    private var is_open_to_close: Boolean = false
    private var listint: List<Int> = ArrayList()
    private var listintwo: List<Int> = ArrayList()

    init {
        this.lists = list
        this.listint = listint
        this.listintwo = listintwo
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        var viewHolderContentType: ViewHolderContentType? = null
        if (null == convertView) {
            viewHolderContentType = ViewHolderContentType()
            convertView = LayoutInflater.from(context).inflate(R.layout.hand_scene_item, null)
            viewHolderContentType.device_type_pic = convertView!!.findViewById<View>(R.id.device_type_pic) as ImageView
            viewHolderContentType.hand_device_content = convertView.findViewById<View>(R.id.hand_device_content) as TextView
            viewHolderContentType.swipe_context = convertView.findViewById<View>(R.id.swipe_context) as LinearLayout
            viewHolderContentType.hand_gateway_content = convertView.findViewById<View>(R.id.hand_gateway_content) as TextView
            viewHolderContentType.hand_scene_btn = convertView.findViewById<View>(R.id.hand_scene_btn) as ImageView
            viewHolderContentType.swipe_layout = convertView.findViewById<View>(R.id.swipe_layout) as SwipeMenuLayout
            viewHolderContentType.delete_btn = convertView.findViewById<View>(R.id.delete_btn) as Button
            viewHolderContentType.edit_btn = convertView.findViewById<View>(R.id.edit_btn) as Button
            viewHolderContentType.rename_btn = convertView.findViewById<View>(R.id.rename_btn) as Button

            convertView.tag = viewHolderContentType
        } else {
            viewHolderContentType = convertView.tag as ViewHolderContentType
        }

        //        int element = (Integer) list.get(position).get("image");
        viewHolderContentType.device_type_pic!!.setImageResource(listint[position])
        viewHolderContentType.hand_device_content!!.setText(lists.get(position).get("name")!!.toString())

        val finalViewHolderContentType1 = viewHolderContentType
        val type = lists.get(position).get("type")!!.toString()
        if (type != null)
            when (type) {
                "101" -> {
                    viewHolderContentType.hand_gateway_content!!.text = "云场景"
                    viewHolderContentType.edit_btn!!.visibility = View.GONE
                }
                else -> {
                    viewHolderContentType.hand_gateway_content!!.text = "网关场景"
                    viewHolderContentType.edit_btn!!.visibility = View.VISIBLE
                }
            }

        val authType = SharedPreferencesUtil.getData(context, "authType", "") as String
        when (authType) {
            "1" -> viewHolderContentType.swipe_layout!!.isSwipeEnable = true
            "2" -> viewHolderContentType.swipe_layout!!.isSwipeEnable = false
        }//业主
        //家庭成员
        (convertView as SwipeMenuLayout).setOnMenuClickListener(object : SwipeMenuLayout.OnMenuClickListener {


            override fun onItemClick() {
                goto_editlink(position)
            }

            override fun onItemClick_By_btn(is_open_to_close1: Boolean) {//SwipeLayout是否在打开到关闭的过程
                is_open_to_close = is_open_to_close1
            }
        })


        viewHolderContentType.rename_btn!!.setOnClickListener {
            //                finalViewHolderContentType.popwindowUtil.loadPopupwindow();
            showRenameDialog(lists.get(position).get("number")!!.toString(), lists.get(position).get("name")!!.toString(), position)
            finalViewHolderContentType1.swipe_layout!!.quickClose()
        }

        val finalViewHolderContentType2 = viewHolderContentType
        viewHolderContentType.edit_btn!!.setOnClickListener {
            //                goto_editlink(position);
            //去关联面板
            val bundle1 = Bundle()
            bundle1.putString("sceneName", lists.get(position).get("name")!!.toString())
            bundle1.putString("sceneType", "1")
            bundle1.putString("boxNumber", lists.get(position).get("boxNumber")!!.toString())
            bundle1.putString("panelType", lists.get(position).get("panelType")!!.toString())
            bundle1.putString("panelNumber", lists.get(position).get("panelNumber")!!.toString())
            bundle1.putString("buttonNumber", lists.get(position).get("buttonNumber")!!.toString())
            IntentUtil.startActivity(context, AssociatedpanelActivity::class.java, bundle1)
            finalViewHolderContentType2.swipe_layout!!.quickClose()
        }

        viewHolderContentType.delete_btn!!.setOnClickListener {
            showCenterDeleteDialog(lists.get(position).get("number")!!.toString(), lists.get(position).get("name")!!.toString())
            finalViewHolderContentType1.swipe_layout!!.quickClose()
        }

        val finalViewHolderContentType = viewHolderContentType
        viewHolderContentType.hand_scene_btn!!.setOnClickListener {
            if (!is_open_to_close) {//SwipeLayout是否在打开到关闭的过程
                sraum_manualSceneControl(lists.get(position).get("number")!!.toString())
                //                    finalViewHolderContentType.hand_scene_btn.setImageResource(R.drawable.icon_root);
                //                    ToastUtil.showToast(context, "click");
            }
        }
        return convertView
    }

    //自定义dialog,自定义重命名dialog

    fun showRenameDialog(id: String, name: String, position: Int) {
        //        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //        // 布局填充器
        //        LayoutInflater inflater = LayoutInflater.from(getActivity());
        //        View view = inflater.inflate(R.layout.user_name_dialog, null);
        //        // 设置自定义的对话框界面
        //        builder.setView(view);
        //
        //        cus_dialog = builder.create();
        //        cus_dialog.show();


        val view = LayoutInflater.from(context).inflate(R.layout.edit_handscene_dialog, null)
        val confirm: TextView //确定按钮
        val cancel: TextView //确定按钮
        val tv_title: TextView
        //        final TextView content; //内容
        cancel = view.findViewById<View>(R.id.call_cancel) as TextView
        confirm = view.findViewById<View>(R.id.call_confirm) as TextView
        val edit_password_gateway = view.findViewById<View>(R.id.edit_password_gateway) as ClearLengthEditText
        edit_password_gateway.setText(name)
        edit_password_gateway.setSelection(edit_password_gateway.text.length)
        //        tv_title = (TextView) view.findViewById(R.id.tv_title);
        //        tv_title.setText("是否拨打119");
        //        content.setText(message);
        //显示数据
        val dialog = Dialog(context, R.style.BottomDialog)
        dialog.setContentView(view)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        val dm = context.resources.displayMetrics
        val displayWidth = dm.widthPixels
        val displayHeight = dm.heightPixels
        val p = dialog.window!!.attributes //获取对话框当前的参数值
        p.width = (displayWidth * 0.8).toInt() //宽度设置为屏幕的0.5
        //        p.height = (int) (displayHeight * 0.5); //宽度设置为屏幕的0.5
        //        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        dialog.window!!.attributes = p  //设置生效
        dialog.show()

        confirm.setOnClickListener { dialog.dismiss() }

        cancel.setOnClickListener(View.OnClickListener {
            if (edit_password_gateway.text.toString() == null || edit_password_gateway.text.toString().trim { it <= ' ' } == "") {
                ToastUtil.showToast(context, "云场景名称为空")
                return@OnClickListener
            }

            var isexist = false
            val `var` = edit_password_gateway.text.toString()
            for (i in lists.indices) {
                if (i == position) {
                    continue
                }

                if (`var` == lists.get(position).get("name")!!.toString()) {//list.get(position).get("number").toString(), list.get(position).get("name").toString()
                    isexist = true
                }
            }

            if (isexist) {
                ToastUtil.showToast(context, "云场景名称已存在")
                return@OnClickListener
            }

            if (name == `var`) {
                dialog.dismiss()
            } else {
                linkage_rename(id, if (edit_password_gateway.text.toString() == null)
                    ""
                else
                    edit_password_gateway.text.toString(), dialog)
            }
        })
    }

    //自定义dialog,centerDialog删除对话框
    fun showCenterDeleteDialog(linkId: String, name: String) {
        //        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //        // 布局填充器
        //        LayoutInflater inflater = LayoutInflater.from(getActivity());
        //        View view = inflater.inflate(R.layout.user_name_dialog, null);
        //        // 设置自定义的对话框界面
        //        builder.setView(view);
        //
        //        cus_dialog = builder.create();
        //        cus_dialog.show();

        val view = LayoutInflater.from(context).inflate(R.layout.promat_dialog, null)
        val confirm: TextView //确定按钮
        val cancel: TextView //确定按钮
        val tv_title: TextView
        val name_gloud: TextView
        //        final TextView content; //内容
        cancel = view.findViewById<View>(R.id.call_cancel) as TextView
        confirm = view.findViewById<View>(R.id.call_confirm) as TextView
        tv_title = view.findViewById<View>(R.id.tv_title) as TextView//name_gloud
        name_gloud = view.findViewById<View>(R.id.name_gloud) as TextView
        //        name_gloud.setVisibility(View.VISIBLE);
        //        name_gloud.setText(name);
        tv_title.text = name
        //        tv_title.setText("是否拨打119");
        //        content.setText(message);
        //显示数据
        val dialog = Dialog(context, R.style.BottomDialog)
        dialog.setContentView(view)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        val dm = context.resources.displayMetrics
        val displayWidth = dm.widthPixels
        val displayHeight = dm.heightPixels
        val p = dialog.window!!.attributes //获取对话框当前的参数值
        p.width = (displayWidth * 0.8).toInt() //宽度设置为屏幕的0.5
        //        p.height = (int) (displayHeight * 0.5); //宽度设置为屏幕的0.5
        //        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        dialog.window!!.attributes = p  //设置生效
        dialog.show()

        confirm.setOnClickListener { dialog.dismiss() }

        cancel.setOnClickListener { linkage_delete(linkId, dialog) }
    }


    /**
     * 删除联动设备
     *
     * @param
     * @param dialog
     */
    private fun linkage_delete(linkId: String, dialog: Dialog) {
        val mapdevice = HashMap<String, String>()
        val areaNumber = SharedPreferencesUtil.getData(context, "areaNumber", "") as String
        mapdevice["token"] = SharedPreferencesUtils.getToken(context)
        mapdevice["areaNumber"] = areaNumber
        mapdevice["number"] = linkId
        //        mapdevice.put("boxNumber", TokenUtil.getBoxnumber(LinkageListActivity.this));
        MyOkHttp.postMapString(ApiHelper.sraum_deleteManuallyScene, mapdevice, object : Mycallback(AddTogglenInterfacer {
            //刷新togglen数据
            linkage_delete(linkId, dialog)
        }, context, dialogUtil) {
            override fun onError(call: Call, e: Exception, id: Int) {
                super.onError(call, e, id)
            }

            override fun pullDataError() {
                super.pullDataError()
            }

            override fun emptyResult() {
                super.emptyResult()
            }

            override fun wrongToken() {
                super.wrongToken()
                //重新去获取togglen,这里是因为没有拉到数据所以需要重新获取togglen
            }

            override fun wrongBoxnumber() {
                super.wrongBoxnumber()
                ToastUtil.showToast(context, "areaNumber\n" + "不存在")
            }

            override fun threeCode() {
                super.threeCode()
                ToastUtil.showToast(context, "number 不存在")
            }

            override fun fourCode() {
                ToastUtil.showToast(context, "删除失败")
            }

            override fun onSuccess(user: User) {
                dialog.dismiss()
                //                refreshLayout.autoRefresh();
                refreshListener?.refresh()
            }
        })
    }

    /**
     * 联动重命名
     *
     * @param
     * @param dialog
     */
    private fun linkage_rename(linkId: String, newName: String, dialog: Dialog) {
        val mapdevice = HashMap<String, String>()
        val areaNumber = SharedPreferencesUtil.getData(context, "areaNumber", "") as String
        mapdevice["token"] = SharedPreferencesUtils.getToken(context)
        mapdevice["number"] = linkId
        mapdevice["newName"] = newName
        mapdevice["areaNumber"] = areaNumber
        //        mapdevice.put("boxNumber", TokenUtil.getBoxnumber(LinkageListActivity.this));
        MyOkHttp.postMapString(ApiHelper.sraum_reNameManuallyScene, mapdevice, object : Mycallback(AddTogglenInterfacer {
            //刷新togglen数据
            linkage_rename(linkId, newName, dialog)
        }, context, dialogUtil) {
            override fun onError(call: Call, e: Exception, id: Int) {
                super.onError(call, e, id)
            }

            override fun pullDataError() {
                super.pullDataError()
            }

            override fun emptyResult() {
                super.emptyResult()
            }

            override fun wrongToken() {
                super.wrongToken()
                //重新去获取togglen,这里是因为没有拉到数据所以需要重新获取togglen
            }

            override fun fourCode() {
                ToastUtil.showToast(context, "名字已存在")
            }

            override fun wrongBoxnumber() {
                super.wrongBoxnumber()
                ToastUtil.showToast(context, "areaNumber\n" + "不存在")
            }

            override fun threeCode() {
                super.threeCode()
                ToastUtil.showToast(context, "number 不存在")
            }

            override fun onSuccess(user: User) {
                dialog.dismiss()
                //                refreshLayout.autoRefresh();
                refreshListener?.refresh()
            }
        })
    }


    /**
     * 去编辑联动
     *
     * @param position
     */
    private fun goto_editlink(position: Int) {

        //        map.put("name", us.name);
        //        map.put("number", us.number);
        val intent = Intent(context, EditLinkDeviceResultActivity::class.java)
        val map = HashMap<String,Any>()
        map.put("link_edit", true)
        map.put("linkId", list.get(position).get("number")!!.toString())
        map.put("linkName", list.get(position).get("name")!!.toString())
        map.put("type", "101")
        intent.putExtra("link_information", map as Serializable)
        context.startActivity(intent)
        SharedPreferencesUtil.saveData(context, "link_first", true)
    }


    /**
     * 控制手动场景
     */
    private fun sraum_manualSceneControl(sceneId: String) {
        val map = HashMap<String,Any>()
        map.put("token", SharedPreferencesUtils.getToken(context))
        val areaNumber = SharedPreferencesUtil.getData(context, "areaNumber", "") as String
        map.put("areaNumber", areaNumber)
        map.put("sceneId", sceneId)
        MyOkHttp.postMapObject(ApiHelper.sraum_manualSceneControl, map, object : Mycallback(AddTogglenInterfacer { sraum_manualSceneControl(sceneId) }, context, dialogUtil) {
            override fun fourCode() {
                super.fourCode()
                ToastUtil.showToast(context, "控制失败")
            }

            override fun threeCode() {
                super.threeCode()
                ToastUtil.showToast(context, "sceneId 错误")
            }


            override fun onSuccess(user: User) {
                super.onSuccess(user)
                ToastUtil.showToast(context, "操作成功")
                if (vibflag) {
                    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                    vibrator.vibrate(200)
                }

                if (musicflag) {
                    LogUtil.i("铃声响起")
                    MusicUtil.startMusic(context, 1, "")
                } else {
                    MusicUtil.stopMusic(context, "")
                }
            }

            override fun wrongToken() {
                super.wrongToken()
            }
        })
    }

    fun setList_s(list: List<Map<*, *>>, listint: List<Int>, listintwo: List<Int>, b: Boolean) {
        this.list = ArrayList<Map<*,*>>()
        this.listint = ArrayList()
        this.listintwo = ArrayList()
        this.list = list
        this.listint = listint
        this.listintwo = listintwo
    }

    fun setflag(vibflag: Boolean, musicflag: Boolean) {
        this.vibflag = vibflag
        this.musicflag = musicflag
    }

    internal inner class ViewHolderContentType {
        var delete_btn: Button? = null
        var edit_btn: Button? = null
        var rename_btn: Button? = null
        var device_type_pic: ImageView? = null
        var hand_device_content: TextView? = null
        var hand_gateway_content: TextView? = null
        var hand_scene_btn: ImageView? = null
        var swipe_context: LinearLayout? = null
        var swipe_layout: SwipeMenuLayout? = null

    }

    interface RefreshListener {
        fun refresh()
    }
}
