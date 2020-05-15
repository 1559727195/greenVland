package com.massky.greenlandvland.ui.sraum.activity

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.TextView
import com.massky.greenlandvland.R
import com.massky.greenlandvland.common.SharedPreferencesUtils
import com.massky.greenlandvland.common.ToastUtil
import com.massky.greenlandvland.ui.sraum.AddTogenInterface.AddTogglenInterfacer
import com.massky.greenlandvland.ui.sraum.User
import com.massky.greenlandvland.ui.sraum.Util.DialogUtil
import com.massky.greenlandvland.ui.sraum.Util.LengthUtil
import com.massky.greenlandvland.ui.sraum.Util.LogUtil
import com.massky.greenlandvland.ui.sraum.Util.MyOkHttp
import com.massky.greenlandvland.ui.sraum.Util.Mycallback
import com.massky.greenlandvland.ui.sraum.Util.StringUtils
import com.massky.greenlandvland.ui.sraum.Utils.ApiHelper
import com.massky.greenlandvland.ui.sraum.adapter.AsccociatedpanelAdapter
import com.massky.greenlandvland.ui.sraum.base.BaseActivity
import com.massky.greenlandvland.util.IntentUtil
import com.massky.greenlandvland.util.SharedPreferencesUtil
import com.yanzhenjie.statusview.StatusUtils
import java.util.HashMap
import butterknife.BindView
import kotlinx.android.synthetic.main.asspanel.*
import kotlinx.android.synthetic.main.backtitle.*


/**
 * Created by masskywcy on 2017-03-22.
 */
//关联面板界面
class AssociatedpanelActivity : BaseActivity(), AdapterView.OnItemClickListener {

    private var dialogUtil: DialogUtil? = null
    private var dialogUtilview: DialogUtil? = null
    private val boxNumber: String? = null
    private var panelNumber: String? = ""
    private var type: String? = null
    private var button5Type: String? = null
    private var button6Type: String? = null
    private var button7Type: String? = null
    private var button8Type: String? = null
    private var sceneName: String? = null
    private var sceType: String? = null
    private var buttonNumber = ""
    private var flagimagefive = ""
    private var flagimagesix = ""
    private var flagimageseven = ""
    private var flagimageight = ""
    private var panelid: String? = null
    private val assopanelname = ""
    private var gatewayid: String? = null
    private var panelType: String? = null
    private var btnflag = ""
    private var adapter: AsccociatedpanelAdapter? = null
    private val checkList = ArrayList<Boolean>()
    private var panelList: MutableList<User.panellist> = ArrayList()
    private var flagfive = true
    private var flagsix = true
    private var flagseven = true
    private var flageight = true
    private var bundle: Bundle? = null
    private var dtext_id: TextView? = null
    private var belowtext_id: TextView? = null
    private var qxbutton_id: Button? = null
    private var checkbutton_id: Button? = null
    private var position: Int = 0


    override fun viewId(): Int {
        return R.layout.asspanel
    }

    override fun onView() {
        StatusUtils.setFullToStatusBar(this)  // StatusBar.
        bundle = IntentUtil.getIntentBundle(this@AssociatedpanelActivity)
        sceneName = bundle!!.getString("sceneName", "")
        sceType = bundle!!.getString("sceneType", "")
        gatewayid = bundle!!.getString("boxNumber", "")
        panelType = bundle!!.getString("panelType", "")
        panelNumber = bundle!!.getString("panelNumber", "")
        buttonNumber = bundle!!.getString("buttonNumber", "")

        //        bundle1.putString("sceneName", linkName);
        //        bundle1.putString("sceneType", "1");
        //        bundle1.putString("boxNumber", boxNumber);
        //        bundle1.putString("panelType", "");
        //        bundle1.putString("panelNumber", "");
        //        bundle1.putString("buttonNumber", "");

        LogUtil.eLength("查看数据panelNumber", panelNumber + "数据问题" + buttonNumber)
        //        boxNumber = (String) SharedPreferencesUtil.getData(AssociatedpanelActivity.this, "boxnumber", "");
        dialogUtil = DialogUtil(this@AssociatedpanelActivity)
        backrela_id!!.setOnClickListener(this)
        paonerela!!.setOnClickListener(this)
        patworela!!.setOnClickListener(this)
        pathreerela!!.setOnClickListener(this)
        pafourrela!!.setOnClickListener(this)
        pafiverela!!.setOnClickListener(this)
        pasixrela!!.setOnClickListener(this)
        pasevenrela!!.setOnClickListener(this)
        paeightrela!!.setOnClickListener(this)
        panelistview!!.onItemClickListener = this
        backsave!!.setOnClickListener(this)
        ptlitone!!.setOnClickListener(this)
        ptlittwo!!.setOnClickListener(this)
        ptlitthree!!.setOnClickListener(this)
        ptlittwoone!!.setOnClickListener(this)
        ptlittwotwo!!.setOnClickListener(this)
        ptlitoneone!!.setOnClickListener(this)

        //三路调光
        paonerela_sanlu!!.setOnClickListener(this)
        patwobtn_sanlu!!.setOnClickListener(this)
        pathreebtn_sanlu!!.setOnClickListener(this)
        pafourbtn_sanlu!!.setOnClickListener(this)
        back!!.setOnClickListener(this)
        titlecen_id!!.text = "关联面板"
        getData(1)
        replacePanel()
    }

    override fun onEvent() {

    }

    override fun onData() {

    }

    private fun replacePanel() {
        val view = layoutInflater.inflate(R.layout.check, null)
        dtext_id = view.findViewById(R.id.dtext_id)
        belowtext_id = view.findViewById(R.id.belowtext_id)
        qxbutton_id = view.findViewById(R.id.qxbutton_id)
        checkbutton_id = view.findViewById(R.id.checkbutton_id)
        dtext_id!!.text = "替换面板"
        qxbutton_id!!.setOnClickListener(this)
        checkbutton_id!!.setOnClickListener(this)
        dialogUtilview = DialogUtil(this@AssociatedpanelActivity, view)
    }

    //设置选中的位置，将其他位置设置为未选
    fun checkPosition(position: Int) {
        for (i in checkList.indices) {
            if (position == i) {// 设置已选位置
                checkList[i] = true
            } else {
                checkList[i] = false
            }
        }
        adapter!!.notifyDataSetChanged()
    }

    private fun getData(index: Int) {
        dialogUtil!!.loadDialog()
        val map = HashMap<String, Any>()
        val areaNumber = SharedPreferencesUtil.getData(this@AssociatedpanelActivity,
                "areaNumber", "") as String
        map["areaNumber"] = areaNumber
        map["boxNumber"] = gatewayid!!

        map["token"] = SharedPreferencesUtils.getToken(this@AssociatedpanelActivity)
        MyOkHttp.postMapObject(ApiHelper.sraum_getAllPanel,
                map, object : Mycallback(AddTogglenInterfacer { getData(index) }, this@AssociatedpanelActivity, dialogUtil) {
            override fun onSuccess(user: User) {
                super.onSuccess(user)
                panelList = user.panelList
                checkList.clear()
                val panelListnew = ArrayList<User.panellist>()
                for (i in panelList.indices) {
                    val upanel = panelList[i]
                    if (upanel.type.trim { it <= ' ' } == "A401" || upanel.type.trim { it <= ' ' } == "A501" ||
                            upanel.type.trim { it <= ' ' } == "A601" || upanel.type.trim { it <= ' ' } == "A701"
                            || upanel.type.trim { it <= ' ' } == "A611"
                            || upanel.type.trim { it <= ' ' } == "A711"
                            || upanel.type.trim { it <= ' ' } == "A511") {
                        panelListnew.add(upanel)
                    }
                }
                panelList.removeAll(panelListnew)
                var flag = false
                if (panelList.size != 0) {
                    val upone = panelList[0]
                    for (i in panelList.indices) {
                        val up = panelList[i]
                        checkList.add(false)
                        if (panelNumber == up.id) {//说明该面板已经关联了该场景，置顶该面板
                            flag = true
                            panelid = up.id
                            panelList[0] = up
                            panelList[i] = upone//替换位置
                            LogUtil.eLength("改变图片", "数据问题")
                            //                                panelrela.setVisibility(View.VISIBLE);//主布局显示
                            setPicture(up.type, up.button5Type, pafivebtn!!, 5)//pafivebtn为下面第五个relativelayout里面的图片
                            setPicture(up.type, up.button6Type, pasixbtn!!, 6)
                            setPicture(up.type, up.button7Type, pasevenbtn!!, 7)
                            setPicture(up.type, up.button8Type, paeightbtn!!, 8)
                            setLinear(up.type)
                            pafivetext!!.text = LengthUtil.doit_spit_str(if (up.button5Name == null)
                                ""
                            else
                                up.button5Name)
                            pasixtext!!.text = LengthUtil.doit_spit_str(if (up.button6Name == null)
                                ""
                            else
                                up.button6Name)
                            paseventext!!.text = LengthUtil.doit_spit_str(if (up.button7Name == null)
                                ""
                            else
                                up.button7Name)
                            paeighttext!!.text = LengthUtil.doit_spit_str(if (up.button8Name == null)
                                ""
                            else
                                up.button8Name)
                            setFlag(up.button5Type, up.button6Type, up.button7Type, up.button8Type)
                            when (index) {
                                1 -> checkList[0] = true
                            }
                        } else {
                            //                                checkList.add(false);
                            when (index) {
                                1 -> checkList[i] = false
                            }
                        }
                    }
                }

                if (flag) {//该场景关联了面板，实现如果该场景未关联该面板的按钮，则面板框不显示，面板不被选中（待实现）
                    panelrela!!.visibility = View.VISIBLE//主布局显示
                } else {
                    panelrela!!.visibility = View.GONE//主布局显示
                }

                adapter = AsccociatedpanelAdapter(this@AssociatedpanelActivity, panelList as ArrayList<User.panellist>, checkList)
                panelistview!!.adapter = adapter

                //
                //                        switch (index) {
                //                            case 1:
                //                                onitemclick(0);
                //                                break;
                //                        }
                when (index) {
                    1 -> for (i in checkList.indices) {
                        if (checkList[i]) {
                            onitemclick(0)
                            break
                        }
                    }
                }
            }

            override fun wrongToken() {
                super.wrongToken()
            }
        })
    }

    /**
     * 显示那种布局
     *
     * @param linearType
     */
    private fun setLinear(linearType: String) {
        clear()
        when (linearType) {
            "A201" -> {
                panelinearone!!.visibility = View.VISIBLE
                LogUtil.eLength("这是进入A201", "看看操作")
            }
            "A202", "A411", "A311" -> {
                panelineartwo!!.visibility = View.VISIBLE
                LogUtil.eLength("这是进入A202", "进入了")
            }
            "A203", "A412", "A312", "A321" -> {
                panelinearthree!!.visibility = View.VISIBLE
                LogUtil.eLength("这是进入A203", "看看操作")
            }
            "A204", "A313", "A322", "A331", "A413", "A414" -> panelinearfour!!.visibility = View.VISIBLE
            "A303"//三路调光
            -> {
                flagfive = false
                flagsix = false
                flagseven = false
                flageight = true
                paneThreeLuTiaoGuang!!.visibility = View.VISIBLE
            }
            else -> panelinearfour!!.visibility = View.VISIBLE
        }//                paonerela.setVisibility(View.GONE);
    }

    private fun setFlag(fivetype: String?, sixtype: String?,
                        sevemtype: String?, eighttype: String?) {
        if (fivetype == null) {
            flagimagefive = "1"
        } else {
            flagimagefive = "3"
        }
        if (sixtype == null) {
            flagimagesix = "1"
        } else {
            flagimagesix = "3"
        }
        if (sevemtype == null) {
            flagimageseven = "1"
        } else {
            flagimageseven = "3"
        }
        if (eighttype == null) {
            flagimageight = "1"
        } else {
            flagimageight = "3"
        }
        val scenename = LengthUtil.doit_spit_str(if (sceneName == null)
            ""
        else
            sceneName)
        when (buttonNumber) {
            "5" -> {
                pafivetext!!.text = scenename
                flagimagefive = "2"
            }
            "6" -> {
                pasixtext!!.text = scenename
                flagimagesix = "2"
            }
            "7"////说明该面板7按钮已经关联了该场景，置顶该面板-flagimageseven = "2";
            -> {
                paseventext!!.text = scenename
                flagimageseven = "2"
            }
            "8" -> {
                paeighttext!!.text = scenename
                flagimageight = "2"
            }
        }
        //
    }

    override fun onClick(v: View) {
        val scenename = LengthUtil.doit_spit_str(if (sceneName == null)
            ""
        else
            sceneName)
        when (v.id) {
            R.id.ptlitone -> ToastUtil.showToast(this@AssociatedpanelActivity, "不可以设置场景")
            R.id.ptlittwo -> ToastUtil.showToast(this@AssociatedpanelActivity, "不可以设置场景")
            R.id.ptlitthree -> ToastUtil.showToast(this@AssociatedpanelActivity, "不可以设置场景")
            R.id.ptlittwoone -> ToastUtil.showToast(this@AssociatedpanelActivity, "不可以设置场景")
            R.id.ptlittwotwo -> ToastUtil.showToast(this@AssociatedpanelActivity, "不可以设置场景")
            R.id.ptlitoneone -> ToastUtil.showToast(this@AssociatedpanelActivity, "不可以设置场景")
            //三路调光
            R.id.paonerela_sanlu -> ToastUtil.showToast(this@AssociatedpanelActivity, "不可以设置场景")
            R.id.patworela_sanlu -> ToastUtil.showToast(this@AssociatedpanelActivity, "不可以设置场景")
            R.id.pathreerela_sanlu -> ToastUtil.showToast(this@AssociatedpanelActivity, "不可以设置场景")
            R.id.pafourrela_sanlu -> ToastUtil.showToast(this@AssociatedpanelActivity, "不可以设置场景")


            R.id.qxbutton_id -> dialogUtilview!!.removeviewDialog()
            R.id.checkbutton_id//当两个场景不一样时，弹出切换场景关联
            -> {
                dialogUtilview!!.removeviewDialog()
                when (btnflag) {
                    "5" -> {
                        buttonNumber = "5"
                        panelRelation(panelNumber)
                    }
                    "6" -> {
                        buttonNumber = "6"
                        panelRelation(panelNumber)
                    }
                    "7" -> {
                        buttonNumber = "7"
                        panelRelation(panelNumber)
                    }
                    "8" -> {
                        buttonNumber = "8"
                        panelRelation(panelNumber)
                    }
                }
            }
            R.id.backrela_id, R.id.back -> this@AssociatedpanelActivity.finish()
            R.id.paonerela -> ToastUtil.showToast(this@AssociatedpanelActivity, "不可以设置场景")
            R.id.patworela -> ToastUtil.showToast(this@AssociatedpanelActivity, "不可以设置场景")
            R.id.pathreerela -> ToastUtil.showToast(this@AssociatedpanelActivity, "不可以设置场景")
            R.id.pafourrela -> ToastUtil.showToast(this@AssociatedpanelActivity, "不可以设置场景")
            R.id.pafiverela -> if (flagfive) {//第5个按钮可以设置场景
                //1代表为空值2非空值代表场景一致3非空值不相等
                when (flagimagefive) {
                    "1" -> {
                        flagimagefive = "2"
                        buttonNumber = "5"
                        panelNumber = panelid
                        pafivetext!!.text = scenename
                        panelRelation(panelNumber)
                    }
                    "2" -> {
                        flagimagefive = "1"
                        buttonNumber = "0"
                        panelRelation("0")
                    }
                    "3" -> if (StringUtils.replaceBlank(pafivetext!!.text.toString()) == sceneName) {
                        flagimagefive = "1"
                        buttonNumber = "0"
                        panelRelation("0")
                    } else {
                        belowtext_id!!.text = "确定从 " +
                                StringUtils.replaceBlank(pafivetext!!.text.toString()) + " 替换成 " + sceneName + " 吗？"
                        btnflag = "5"
                        dialogUtilview!!.loadViewdialog()
                    }
                    else -> {
                    }
                }

            } else {
                ToastUtil.showToast(this@AssociatedpanelActivity, "不可以设置场景")
            }
            R.id.pasixrela -> if (flagsix) { //1代表为空值或者非空值不相等2非空值代表场景一致
                LogUtil.eLength("数据查看", panelNumber)
                when (flagimagesix) {
                    "1" -> {
                        LogUtil.eLength("进入行为", "行为操作")
                        flagimagesix = "2"
                        buttonNumber = "6"
                        panelNumber = panelid
                        pasixtext!!.text = scenename
                        panelRelation(panelNumber)
                    }
                    "2" -> {
                        LogUtil.eLength("取消行为", "取消数据")
                        flagimagesix = "1"
                        buttonNumber = "0"
                        panelRelation("0")
                    }
                    "3" -> if (StringUtils.replaceBlank(pasixtext!!.text.toString()) == sceneName) {
                        LogUtil.eLength("取消行为", "取消数据")
                        flagimagesix = "1"
                        buttonNumber = "0"
                        panelRelation("0")
                    } else {
                        belowtext_id!!.text = "确定从 " +
                                StringUtils.replaceBlank(pasixtext!!.text.toString()) + " 替换成 " + sceneName + " 吗？"
                        btnflag = "6"
                        dialogUtilview!!.loadViewdialog()
                    }
                    else -> {
                    }
                }
            } else {
                ToastUtil.showToast(this@AssociatedpanelActivity, "不可以设置场景")
            }
            R.id.pasevenrela -> {
                LogUtil.eLength("查看数据", sceType + flagseven + "数据" + flagimageseven)
                if (flagseven) {
                    when (flagimageseven) {
                        "1"//该面板上的7按钮没有关联场景
                        -> {
                            LogUtil.eLength("直接选中空白", "第七数据判断")
                            flagimageseven = "2"
                            buttonNumber = "7"
                            panelNumber = panelid
                            paseventext!!.text = scenename
                            panelRelation(panelNumber)
                        }
                        "2"//取消该面板关联的场景
                        -> {
                            LogUtil.eLength("直接取消状态", "取消行为")
                            flagimageseven = "1"
                            buttonNumber = "0"
                            panelRelation("0")
                        }
                        "3"//将该面板按钮7关联的场景切换为最近场景
                        -> if (StringUtils.replaceBlank(paseventext!!.text.toString()) == sceneName) {
                            LogUtil.eLength("直接取消状态", "取消行为")
                            flagimageseven = "1"
                            buttonNumber = "0"
                            panelRelation("0")
                        } else {
                            belowtext_id!!.text = "确定从 " +
                                    StringUtils.replaceBlank(paseventext!!.text.toString()) + " 替换成 " + sceneName + " 吗？"
                            btnflag = "7"
                            dialogUtilview!!.loadViewdialog()
                        }
                        else -> {
                        }
                    }
                } else {
                    ToastUtil.showToast(this@AssociatedpanelActivity, "不可以设置场景")
                }
            }
            R.id.paeightrela -> {
                LogUtil.eLength("查看数据", sceType + flageight + "第二次数据" + panelNumber)
                if (flageight) {
                    when (flagimageight) {
                        "1" -> {
                            LogUtil.eLength("确定关联数据", "传入")
                            flagimageight = "2"
                            buttonNumber = "8"
                            panelNumber = panelid
                            paeighttext!!.text = scenename
                            panelRelation(panelNumber)
                        }
                        "2" -> {
                            LogUtil.eLength("相等传输数据", "传入")
                            flagimageight = "1"
                            buttonNumber = "0"
                            panelRelation("0")
                        }
                        "3" -> if (StringUtils.replaceBlank(paeighttext!!.text.toString()) == sceneName) {
                            LogUtil.eLength("相等传输数据", "传入")
                            flagimageight = "1"
                            buttonNumber = "0"
                            panelRelation("0")
                        } else {
                            belowtext_id!!.text = "确定从 " +
                                    StringUtils.replaceBlank(paeighttext!!.text.toString()) + " 替换成 " + sceneName + " 吗？"
                            btnflag = "8"
                            dialogUtilview!!.loadViewdialog()
                        }
                        else -> {
                        }
                    }
                } else {
                    ToastUtil.showToast(this@AssociatedpanelActivity, "不可以设置场景")
                }
            }
        }
    }


    private fun panelRelation(panelrenumb: String?) {
        dialogUtil!!.loadDialog()
        sraum_panelRelation_(panelrenumb)
    }

    private fun sraum_panelRelation_(panelrenumb: String?) {
        val map = HashMap<String, Any>()
        map["token"] = SharedPreferencesUtils.getToken(this@AssociatedpanelActivity)
        val areaNumber = SharedPreferencesUtil.getData(this@AssociatedpanelActivity,
                "areaNumber", "") as String
        map["areaNumber"] = areaNumber
        map["boxNumber"] = gatewayid!!
        map["panelNumber"] = panelrenumb!!
        map["buttonNumber"] = buttonNumber//关联哪个面板上的哪个按钮
        map["sceneName"] = sceneName!!
        MyOkHttp.postMapObject(ApiHelper.sraum_panelRelation, map,
                object : Mycallback(AddTogglenInterfacer { sraum_panelRelation_(panelrenumb) }, this@AssociatedpanelActivity, dialogUtil) {
                    override fun onSuccess(user: User) {
                        super.onSuccess(user)
                        getData(1)
                    }

                    override fun wrongToken() {
                        super.wrongToken()
                    }
                })
    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        this.position = position
        onitemclick(position)
    }

    private fun onitemclick(position: Int) {
        panelNumber = panelList[position].id
        panelid = panelList[position].id
        type = panelList[position].type
        button5Type = panelList[position].button5Type
        button6Type = panelList[position].button6Type
        button7Type = panelList[position].button7Type
        button8Type = panelList[position].button8Type
        val fivename = panelList[position].button5Name
        val sixname = panelList[position].button6Name
        val sevenname = panelList[position].button7Name
        val eightname = panelList[position].button8Name
        LogUtil.eLength("查看name", fivename + "那么" + sixname + "数据" +
                sevenname + "查看" + eightname + "你看呢")
        compareName(fivename, sixname, sevenname, eightname)
        pafivetext!!.text = LengthUtil.doit_spit_str(fivename ?: "")
        pasixtext!!.text = LengthUtil.doit_spit_str(sixname ?: "")
        paseventext!!.text = LengthUtil.doit_spit_str(sevenname ?: "")
        paeighttext!!.text = LengthUtil.doit_spit_str(eightname ?: "")
        LogUtil.eLength("点击数据", type + "查看数据" + button5Type + "12" + button6Type + "34" +
                button7Type + "45" + button8Type + "67")
        checkPosition(position)
        panelrela!!.visibility = View.GONE
        panelrela!!.visibility = View.VISIBLE
        clear()
        setPicture(type, button5Type, pafivebtn!!, 5)
        setPicture(type, button6Type, pasixbtn!!, 6)
        setPicture(type, button7Type, pasevenbtn!!, 7)
        setPicture(type, button8Type, paeightbtn!!, 8)
        LogUtil.eLength("查看item", type + "数据" + position)
        when (type) {
            "A201" -> {
                panelinearone!!.visibility = View.VISIBLE
                LogUtil.eLength("这是进入A201", "看看操作")
            }
            "A202", "A311", "A411"//1窗帘相当于两个按钮
            -> {
                panelineartwo!!.visibility = View.VISIBLE
                LogUtil.eLength("这是进入A202", "进入了")
            }
            "A203", "A312", "A321", "A412"//1窗帘相当于两个按钮
            -> {
                panelinearthree!!.visibility = View.VISIBLE
                LogUtil.eLength("这是进入A203", "看看操作")
            }
            "A204", "A313", "A322", "A331", "A413"//1窗帘相当于两个按钮
                , "A414" -> panelinearfour!!.visibility = View.VISIBLE
            "A301" -> {
                panelinearfour!!.visibility = View.VISIBLE
                flagfive = false
                flagsix = true
                flagseven = true
                flageight = true
            }
            "A302" -> {
                panelinearfour!!.visibility = View.VISIBLE
                flagfive = false
                flagsix = false
                flagseven = true
                flageight = true
            }
            "A303" -> {
                flagfive = false
                flagsix = false
                flagseven = false
                flageight = true
                paneThreeLuTiaoGuang!!.visibility = View.VISIBLE
            }
            else -> panelinearfour!!.visibility = View.VISIBLE
        }//                paonerela.setVisibility(View.GONE);
    }

    private fun clear() {
        panelinearone!!.visibility = View.GONE
        panelineartwo!!.visibility = View.GONE
        panelinearthree!!.visibility = View.GONE
        panelinearfour!!.visibility = View.GONE
        paneThreeLuTiaoGuang!!.visibility = View.GONE

        pafivebtn!!.setImageBitmap(null)
        pasixbtn!!.setImageBitmap(null)
        pasevenbtn!!.setImageBitmap(null)
        paeightbtn!!.setImageBitmap(null)
    }

    private fun compareName(fivename: String?, sixname: String?, sevenname: String?, eightname: String?) {
        var fivename = fivename
        var sixname = sixname
        var sevenname = sevenname
        var eightname = eightname
        if (fivename == null) {
            fivename = ""
        }
        if (sixname == null) {
            sixname = ""
        }
        if (sevenname == null) {
            sevenname = ""
        }
        if (eightname == null) {
            eightname = ""
        }

        //1代表为空值或者2非空值代表场景一直3非空值不相等
        if (fivename == "") {
            flagimagefive = "1"
        } else {
            if (fivename == sceneName) {
                flagimagefive = "2"
            } else {
                flagimagefive = "3"
            }
        }
        if (sixname == "") {
            flagimagesix = "1"
        } else {
            if (sixname == sceneName) {
                flagimagesix = "2"
            } else {
                flagimagesix = "3"
            }
        }
        if (sevenname == "") {
            flagimageseven = "1"
        } else {
            if (sevenname == sceneName) {
                flagimageseven = "2"
            } else {
                flagimageseven = "3"
            }
        }
        if (eightname == "") {
            flagimageight = "1"
        } else {
            if (fivename == sceneName) {
                flagimageight = "2"
            } else {
                flagimageight = "3"
            }
        }
    }

    private fun setPicture(panel_type: String?, type: String?, button: ImageView, index: Int) {//buttonType是按钮关联的场景类型
        LogUtil.eLength("这是类型数据", type!! + "查看类型")
        button.visibility = View.VISIBLE
        //        if (type != null) {
        //            switch (type) {
        //                case "1":
        //                    button.setImageResource(R.drawable.add_scene_homein);
        //                    break;
        //                case "2":
        //                    button.setImageResource(R.drawable.add_scene_homeout);
        //                    break;
        //                case "3":
        //                    button.setImageResource(R.drawable.add_scene_sleep);
        //                    break;
        //                case "4":
        //                    button.setImageResource(R.drawable.add_scene_nightlamp);
        //                    break;
        //                case "5":
        //                    button.setImageResource(R.drawable.add_scene_getup);
        //                    break;
        //                case "6":
        //                    button.setImageResource(R.drawable.add_scene_cup);
        //                    break;
        //                case "7":
        //                    button.setImageResource(R.drawable.add_scene_book);
        //                    break;
        //                case "8":
        //                    button.setImageResource(R.drawable.add_scene_moive);
        //                    break;
        //                case "9":
        //                    button.setImageResource(R.drawable.add_scene_meeting);
        //                    break;
        //                case "10":
        //                    button.setImageResource(R.drawable.add_scene_cycle);
        //                    break;
        //                case "11":
        //                    button.setImageResource(R.drawable.add_scene_noddle);
        //                    break;
        //                case "12":
        //                    button.setImageResource(R.drawable.add_scene_lampon);
        //                    break;
        //                case "13":
        //                    button.setImageResource(R.drawable.add_scene_lampoff);
        //                    break;
        //                case "14":
        //                    button.setImageResource(R.drawable.defaultpic);
        //                    break;
        //                default://没有设置场景
        //                    switch (panel_type) {
        //                        case "A303":
        //                            if (index != 8) {
        //                                button.setImageResource(R.drawable.light_turn_off);
        //                            } else {
        //                                button.setVisibility(View.GONE);
        //                            }
        //                            break;
        //                        default:
        //                            button.setVisibility(View.GONE);
        //                            break;
        //                    }
        //                    break;
        //            }
    }
}
