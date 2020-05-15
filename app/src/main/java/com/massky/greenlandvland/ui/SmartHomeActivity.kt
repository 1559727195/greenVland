package com.massky.greenlandvland.ui

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.annotation.Size
import androidx.appcompat.app.AppCompatActivity

import com.google.gson.Gson
import com.massky.greenlandvland.R
import com.massky.greenlandvland.common.CallBackInterface
import com.massky.greenlandvland.common.CommonUtil
import com.massky.greenlandvland.common.DialogThridUtils
import com.massky.greenlandvland.common.GetToken
import com.massky.greenlandvland.common.LocalBroadcastManager
import com.massky.greenlandvland.common.Position
import com.massky.greenlandvland.common.SharedPreferencesUtils
import com.massky.greenlandvland.common.StatusbarUtils
import com.massky.greenlandvland.common.ToastUtil
import com.massky.greenlandvland.model.entity.Sc_isLoginNew
import com.massky.greenlandvland.model.entity.Sc_myRoom
import com.massky.greenlandvland.model.entity.Sc_myScene
import com.massky.greenlandvland.model.entity.Sc_sc_myAreaNumber
import com.massky.greenlandvland.model.httpclient.HttpUrl.HttpClient
import com.massky.greenlandvland.model.httpclient.UICallback
import java.util.ArrayList

class SmartHomeActivity : AppCompatActivity() {
    private var rl_smarthome: RelativeLayout? = null
    private var drop: ImageView? = null//下拉键
    private var btn_myscene: Button? = null
    private var btn_myhouse: Button? = null
    private var rl_topsmart: RelativeLayout? = null
    private var isPlaying = false
    private var height: Int = 0
    private var top: Int = 0
    private var width: Int = 0
    private var mShowAnimation: AlphaAnimation? = null
    private var mHideAnimation: AlphaAnimation? = null
    private var token: String? = null
    private var projectCode: String? = null
    private var boxNumber: String? = null
    private var id = 1
    private var mDialog: Dialog? = null
    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                1 -> DialogThridUtils.closeDialog(mDialog)
            }
        }
    }

    internal var clickListener: View.OnClickListener = View.OnClickListener { v ->
        when (v.id) {
            R.id.drop//下拉键
            -> onBackPressed()
            R.id.btn_myscene//我的场景
            -> {
                mDialog = DialogThridUtils.showWaitDialog(this@SmartHomeActivity, true)
                initmyScene()
            }
            R.id.btn_myhouse//我的房间
            -> {
                mDialog = DialogThridUtils.showWaitDialog(this@SmartHomeActivity, true)
                initmyRoom()
            }
        }
    }
    private var mMessageReceiver: MessageReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_smart_home)
        instance = this
        token = SharedPreferencesUtils.getToken(this)
        projectCode = SharedPreferencesUtils.getProjectCode(this)
        boxNumber = SharedPreferencesUtils.getBoxNumber(this)

        //智能家居：我的区域号
        initMyAreaNumber()


        //获取控件
        drop = findViewById(R.id.drop)
        rl_smarthome = findViewById(R.id.rl_smarthome)
        btn_myscene = findViewById(R.id.btn_myscene)
        btn_myhouse = findViewById(R.id.btn_myhouse)
        rl_topsmart = findViewById(R.id.rl_topsmart)


        //添加监听
        drop!!.setOnClickListener(clickListener)
        btn_myscene!!.setOnClickListener(clickListener)
        btn_myhouse!!.setOnClickListener(clickListener)


        val string = intArrayOf(R.drawable.bg_smart_homes, R.drawable.safe_location, R.drawable.bg_home_jiankong, R.drawable.bg_intelligenceenter, R.drawable.bg_invitevisitor, R.drawable.bg_monitor)

        val position = intent.getIntExtra(EXTRA_IMAGE, 0)
        height = intent.getIntExtra("height", 0)
        width = intent.getIntExtra("width", 0)
        top = intent.getIntExtra("top", 0)

        rl_smarthome!!.setBackgroundResource(string[position])

        rl_smarthome!!.post {
            anim(width, height, top, true,
                    Runnable { }, rl_smarthome!!)
        }

        registerMessageReceiver()
    }

    //智能家居：我的网关
    private fun initMyAreaNumber() {
        //mDialog= DialogThridUtils.showWaitDialog(SmartHomeActivity.this,true);
        //        final String token= SharedPreferencesUtils.getToken(SmartHomeActivity.this);
        //        final String projectCode=SharedPreferencesUtils.getProjectCode(SmartHomeActivity.this);
        val roomNo = SharedPreferencesUtils.getRoomNo(this)

        HttpClient.post(CommonUtil.APPURL, "sc_myAreaNumber", Gson().toJson(Sc_sc_myAreaNumber.Sc_myAreaNumber(token, projectCode, roomNo)), object : UICallback {
            override fun process(data: String) {
                //                        Log.e("TAG","data="+data);
                //                        Log.e("TAG","token="+token);
                //                        Log.e("TAG","projectCode="+projectCode);
                val myGateResult = Gson().fromJson(data, Sc_sc_myAreaNumber.Sc_myAreaNumberResult::class.java)
                val result = myGateResult.result

                SharedPreferencesUtils.save_myAreaNumber(this@SmartHomeActivity, myGateResult.areaNumber)
                //                        if(result.equals("1")){
                //                            Log.e("TAG","1-json解析错误");
                //                            id=1;
                //                            mHandler.sendEmptyMessage(1);
                //                        }else if(result.equals("100")){
                //                            Log.e("TAG","100-成功");
                //                            id=1;
                //                            SharedPreferencesUtils.save_myAreaNumber(SmartHomeActivity.this,myGateResult.getAreaNumber());
                //                            mHandler.sendEmptyMessage(1);
                //                        }else if(result.equals("101")){
                //                            Log.e("TAG","101-token错误");
                //                            mHandler.sendEmptyMessage(1);
                //                            if(id==1){
                //                                id=id+1;
                //                                new GetToken(new CallBackInterface() {
                //                                    @Override
                //                                    public void gettoken(String str) {
                //                                        token=str;
                //                                        Log.e("TAG","token="+token);
                //                                        if(!TextUtils.isEmpty(token)){
                //                                            initMyAreaNumber();
                //                                        }else {
                //                                            showerror();
                //                                        }
                //                                    }
                //                                },SmartHomeActivity.this);
                //                            }else {
                //                                id=1;
                //                                showerror();
                //                            }
                //                        }else if(result.equals("102")){
                //                            Log.e("TAG","102-projectCode错误");
                //                            id=1;
                //                            mHandler.sendEmptyMessage(1);
                //                            ToastUtil.showToast(SmartHomeActivity.this,"没有发现房间");
                //                        }else if(result.equals("103")){
                //                            Log.e("TAG","103-获取失败");
                //                            ToastUtil.showToast(SmartHomeActivity.this,"没有发现房间");
                //                            id=1;
                //                            mHandler.sendEmptyMessage(1);
                //                        }else {
                //                            id=1;
                //                            mHandler.sendEmptyMessage(1);
                //                            ToastUtil.showToast(SmartHomeActivity.this,"没有发现房间");
                //                        }
            }

            override fun onError(data: String) {
                id = 1
                mHandler.sendEmptyMessage(1)
                ToastUtil.showToast(this@SmartHomeActivity, "网络连接失败")
            }
        })
    }

    //我的场景子线程
    private fun initmyScene() {
        //        final String token= SharedPreferencesUtils.getToken(SmartHomeActivity.this);
        //        final String projectCode=SharedPreferencesUtils.getProjectCode(SmartHomeActivity.this);
        //        final String boxNumber=SharedPreferencesUtils.getBoxNumber(SmartHomeActivity.this);
        HttpClient.post(CommonUtil.APPURL, "sc_myScene", Gson().toJson(Sc_myScene.MySceneParams(token, projectCode, boxNumber)), object : UICallback {
            override fun process(data: String) {
                //                        Log.e("TAG","data="+data);
                //                        Log.e("TAG","token="+token);
                //                        Log.e("TAG","projectCode="+projectCode);
                //                        Log.e("TAG","boxNumber="+boxNumber);
                val mySceneResult = Gson().fromJson(data, Sc_myScene.MySceneResult::class.java)
                val result = mySceneResult.result!!
                if (result == 1) {
                    Log.e("TAG", "1-解析错误")
                    mHandler.sendEmptyMessage(1)
                    id = 1
                } else if (result == 100) {
                    Log.e("TAG", "100-成功")
                    id = 1
                    var sceneList: List<Sc_myScene.MySceneResult.Scene>? = ArrayList()
                    sceneList = mySceneResult.sceneList
                    SharedPreferencesUtils.saveMyScene<Sc_myScene.MySceneResult.Scene>(this@SmartHomeActivity, sceneList)
                    mHandler.sendEmptyMessage(1)
                    startActivity(Intent(this@SmartHomeActivity, MySceneActivity::class.java))
                } else if (result == 101) {
                    Log.e("TAG", "101-token错误")
                    mHandler.sendEmptyMessage(1)
                    if (id == 1) {
                        id = id + 1
                        GetToken(CallBackInterface { str ->
                            token = str
                            Log.e("TAG", "token=" + token!!)
                            if (!TextUtils.isEmpty(token)) {
                                initmyScene()
                            } else {
                                showerror()
                            }
                        }, this@SmartHomeActivity)
                    } else {
                        id = 1
                        showerror()
                    }
                } else if (result == 102) {
                    Log.e("TAG", "102-projectCode错误")
                    id = 1
                    mHandler.sendEmptyMessage(1)
                    ToastUtil.showToast(this@SmartHomeActivity, "获取场景失败")
                } else if (result == 103) {
                    Log.e("TAG", "103-secneId错误")
                    id = 1
                    mHandler.sendEmptyMessage(1)
                    ToastUtil.showToast(this@SmartHomeActivity, "获取场景失败")
                } else {
                    id = 1
                    mHandler.sendEmptyMessage(1)
                    ToastUtil.showToast(this@SmartHomeActivity, "获取场景失败")
                }
            }

            override fun onError(data: String) {
                id = 1
                mHandler.sendEmptyMessage(1)
                ToastUtil.showToast(this@SmartHomeActivity, "网络连接失败")
            }
        })
    }

    private fun showerror() {
        val builder = AlertDialog.Builder(this@SmartHomeActivity, 1)
        builder.setTitle("提示")
        builder.setMessage("服务器错误")
        builder.setCancelable(false)
        builder.setPositiveButton("确定") { dialog, which ->
            dialog.dismiss()
            finish()
        }
        builder.create().show()
    }

    //我的房间子线程
    private fun initmyRoom() {
        //        final String token= SharedPreferencesUtils.getToken(SmartHomeActivity.this);
        //        final String projectCode=SharedPreferencesUtils.getProjectCode(SmartHomeActivity.this);
        //        final String boxNumber=SharedPreferencesUtils.getBoxNumber(SmartHomeActivity.this);
        val areaNumber = SharedPreferencesUtils.get_myAreaNumber(this)
        val roomNumber = SharedPreferencesUtils.getRoomNo(this)
        HttpClient.post(CommonUtil.APPURL, "sc_myRoomNew", Gson().toJson(Sc_myRoom.MyRoomParams(token, projectCode, roomNumber, areaNumber)), object : UICallback {
            override fun process(data: String) {
                //                        Log.e("TAG","data111="+data);
                //                        Log.e("TAG","token="+token);
                //                        Log.e("TAG","projectCode="+projectCode);
                //                        Log.e("TAG","boxNumber="+boxNumber);
                val myRoomResult = Gson().fromJson(data, Sc_myRoom.MyRoomResult::class.java)
                val result = myRoomResult.result
                if (result == "1") {
                    Log.e("TAG", "1-json错误")
                    mHandler.sendEmptyMessage(1)
                    id = 1
                } else if (result == "100") {
                    //                            Log.e("TAG","100-成功");
                    //                            id=1;
                    var sceneList: List<Sc_myRoom.MyRoomResult.RoomListBean> = ArrayList()
                    sceneList = myRoomResult.roomList
                    //                                Log.e("TAG","roomList="+sceneList);
                    SharedPreferencesUtils.saveMyRoom<Sc_myRoom.MyRoomResult.RoomListBean>(this@SmartHomeActivity, sceneList)
                    mHandler.sendEmptyMessage(1)
                    startActivity(Intent(this@SmartHomeActivity, MyHouseActivity::class.java))
                } else if (result == "101") {
                    Log.e("TAG", "101-token错误")
                    mHandler.sendEmptyMessage(1)
                    if (id == 1) {
                        id = id + 1
                        GetToken(CallBackInterface { str ->
                            token = str
                            Log.e("TAG", "token=" + token!!)
                            if (!TextUtils.isEmpty(token)) {
                                initmyRoom()
                            } else {
                                showerror()
                            }
                        }, this@SmartHomeActivity)
                    } else {
                        id = 1
                        showerror()
                    }
                } else if (result == "102") {
                    Log.e("TAG", "102-projectCode错误")
                    id = 1
                    mHandler.sendEmptyMessage(1)
                    ToastUtil.showToast(this@SmartHomeActivity, "房间获取失败")
                } else {
                    id = 1
                    mHandler.sendEmptyMessage(1)
                    ToastUtil.showToast(this@SmartHomeActivity, "房间获取失败")
                }
            }

            override fun onError(data: String) {
                id = 1
                mHandler.sendEmptyMessage(1)
                ToastUtil.showToast(this@SmartHomeActivity, "网络连接失败")
            }
        })
    }


    /**
     * 动画封装，千万不要剁手改正负
     */
    private fun anim(width: Int, height: Int, top: Int, inter: Boolean,
                     runnable: Runnable, @Size(value = 3) vararg views: View) {
        if (isPlaying) {
            return
        }
        val detailImg = views[0]
        //记住括号哦，我这里调试了一小时
        val delta = width.toFloat() / height.toFloat()
        //243 - 168(navi) = 75 = status_bar
        val y_img = floatArrayOf(top - (detailImg.y + if (inter) StatusbarUtils.getStatusBarOffsetPx(this) else 0), 0f)
        val s_img = floatArrayOf(1f, 1f)

        val t_img = floatArrayOf(-621.0f, 0.0f)
        //        float[] y_icn = { views[guide1].getHeight() * guide4, 0 };

        detailImg.pivotX = (detailImg.width / 2).toFloat()
        detailImg.pivotY = 0f
        //        views[guide1].setPivotX(views[guide1].getWidth() / guide2);
        //        views[guide1].setPivotY(0);
        //        ImageView bg_smart_home = ((ImageView) views[guide2]);
        //        if (drawable != null) {
        //            bg_smart_home.setImageDrawable(drawable);
        //        }
        val trans_Y = ObjectAnimator.ofFloat(detailImg, View.TRANSLATION_Y, if (inter) y_img[0] else y_img[1],
                if (inter) y_img[1] else y_img[0])
        val scale_X = ObjectAnimator.ofFloat(detailImg, View.SCALE_X, if (inter) s_img[0] else s_img[1],
                if (inter) s_img[1] else s_img[0])
        val scale_Y = ObjectAnimator.ofFloat(detailImg, View.SCALE_Y, if (inter) s_img[0] else s_img[1],
                if (inter) s_img[1] else s_img[0])
        val trans_T = ObjectAnimator.ofFloat(rl_topsmart, View.TRANSLATION_Y, if (inter) t_img[0] else t_img[1],
                if (inter) t_img[1] else t_img[0])
        //        Animator alpha_icn = ObjectAnimator.ofFloat(views[guide1], View.ALPHA, in ? 0f : 1f, in ? 1f : 0f);
        //        Animator alpha_bg = ObjectAnimator.ofFloat(views[guide2], View.ALPHA, in ? 0f : 1f, in ? 1f : 0f);
        //
        //        Animator trans_icn_Y =
        //                ObjectAnimator.ofFloat(views[guide1], View.TRANSLATION_Y, in ? y_icn[0] : y_icn[guide1],
        //                        in ? y_icn[guide1] : y_icn[0]);
        val set = AnimatorSet()
        set.playTogether(trans_Y, scale_X, scale_Y, trans_T)
        //        set.playTogether(trans_icn_Y, alpha_icn, alpha_bg);
        set.duration = 800
        set.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                isPlaying = true
            }

            override fun onAnimationEnd(animation: Animator) {
                isPlaying = false
                runnable.run()
                if (inter) {//说明动画刚进来
                    //                    top_img.clearAnimation();
                    //                    setShowAnimation (top_img,500);
                    //                    pull_down.setVisibility(View.VISIBLE);
                } else {//动画消失

                }
            }

            override fun onAnimationCancel(animation: Animator) {
                isPlaying = false
            }

            override fun onAnimationRepeat(animation: Animator) {}
        })
        set.interpolator = AccelerateInterpolator()
        set.start()
    }


    internal fun anim(view: View, position: Position, runnable: Runnable, isEnter: Boolean,
                      listener: Animation.AnimationListener) {
        //记住括号哦，我这里调试了一小时
        val delta = position.width.toFloat() / position.height.toFloat()//wdith,height,top
        val fromDelta: Float
        val toDelta: Float
        val fromY: Float
        val toY: Float
        if (isEnter) {
            fromDelta = 1f
            toDelta = delta
            fromY = position.top.toFloat()
            toY = 0f
        } else {
            fromDelta = delta
            toDelta = 1f
            fromY = 0f
            toY = position.top.toFloat()
        }
        val anim = ScaleAnimation(fromDelta, toDelta,
                // Start and end values for the X axis scaling
                fromDelta, toDelta, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0.5f, // scale from mid of x
                Animation.RELATIVE_TO_SELF, 0f) // scale from start of y
        val trans = TranslateAnimation(0f, 0f, fromY, toY)
        val set = AnimationSet(true)
        //添加并行动画
        set.addAnimation(anim)
        set.addAnimation(trans)
        //动画结束后保持原样
        set.isFillEnabled = true
        set.fillAfter = true
        //监听器
        set.setAnimationListener(listener)
        set.duration = 3000
        view.startAnimation(set)
    }

    override fun onBackPressed() {
        back_to_main()
    }

    /**
     * View渐隐动画效果
     */
    fun setHideAnimation(view: View?, duration: Int) {
        if (null == view || duration < 0) {
            return
        }

        if (null != mHideAnimation) {
            mHideAnimation!!.cancel()
        }
        // 监听动画结束的操作
        mHideAnimation = AlphaAnimation(1.0f, 0.0f)//AlphaAnimation 是透明淡入淡出的动画
        mHideAnimation!!.duration = duration.toLong()
        mHideAnimation!!.fillAfter = true
        mHideAnimation!!.setAnimationListener(object : Animation.AnimationListener {

            override fun onAnimationStart(arg0: Animation) {

            }

            override fun onAnimationRepeat(arg0: Animation) {

            }

            override fun onAnimationEnd(arg0: Animation) {
                view.visibility = View.GONE
                drop!!.visibility = View.GONE
                anim(width, height, top, false, Runnable {
                    super@SmartHomeActivity.onBackPressed()
                    overridePendingTransition(0, 0)
                }, rl_smarthome!!)
            }
        })
        view.startAnimation(mHideAnimation)
    }


    /**
     * View渐现动画效果
     */
    fun setShowAnimation(view: View?, duration: Int) {
        if (null == view || duration < 0) {
            return
        }
        if (null != mShowAnimation) {
            mShowAnimation!!.cancel()
        }
        mShowAnimation = AlphaAnimation(0.0f, 1.0f)
        mShowAnimation!!.duration = duration.toLong()
        mShowAnimation!!.fillAfter = true
        mShowAnimation!!.setAnimationListener(null)
        view.visibility = View.VISIBLE
        view.startAnimation(mShowAnimation)
    }


    private fun back_to_main() {
        //        top_img.clearAnimation();
        //        setHideAnimation(top_img, 200);
        anim(width, height, top, false, Runnable {
            super@SmartHomeActivity.onBackPressed()
            overridePendingTransition(0, 0)
        }, rl_smarthome!!)
    }


    override fun onResume() {
        isForeground = true
        isLogin()
        super.onResume()
    }

    override fun onPause() {
        isForeground = false
        super.onPause()
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver)
        super.onDestroy()
    }

    fun registerMessageReceiver() {
        mMessageReceiver = MessageReceiver()
        val filter = IntentFilter()
        filter.priority = IntentFilter.SYSTEM_HIGH_PRIORITY
        filter.addAction(MESSAGE_RECEIVED_ACTION)
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter)
    }

    inner class MessageReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION == intent.action) {
                    val messge = intent.getStringExtra(KEY_MESSAGE)
                    val extras = intent.getStringExtra(KEY_EXTRAS)
                    val showMsg = StringBuilder()
                    showMsg.append("$KEY_MESSAGE : $messge\n")
                    if (!TextUtils.isEmpty(extras)) {
                        showMsg.append("$KEY_EXTRAS : $extras\n")
                        Log.e("TAG", "showMsg=$showMsg")
                        val message = Gson().fromJson(extras, com.massky.greenlandvland.model.entity.Message::class.java)
                        Log.e("TAG", "message=$message")
                        if (message.type == "2") {
                            showdialog()
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("TAG", "错误")
            }

        }
    }

    private fun isLogin() {
        val phoneNumber = SharedPreferencesUtils.getPhoneNumber(this)
        val phoneId = SharedPreferencesUtils.getPhoneId(this)
        HttpClient.post(CommonUtil.APPURL, "sc_isLoginNew", Gson().toJson(Sc_isLoginNew.LoginNewParams(phoneNumber, phoneId, 3)), object : UICallback {
            override fun process(data: String) {
                Log.e("TAG", "data=$data")
                Log.e("TAG", "phoneNumber=$phoneNumber")
                Log.e("TAG", "phoneId=$phoneId")
                val isLoginResult = Gson().fromJson(data, Sc_isLoginNew.LoginNewResult::class.java)
                val result = isLoginResult.result
                if (result == 1) {
                    Log.e("TAG", "1-json格式解析失败")
                } else if (result == 100) {
                    Log.e("TAG", "100-成功")
                } else if (result == 103) {
                    Log.e("TAG", "103-已登录")
                    showdialog()
                } else {

                }
            }

            override fun onError(data: String) {

            }
        })
    }

    private fun showdialog() {
        val builder = AlertDialog.Builder(this@SmartHomeActivity, 1)
        builder.setTitle("提示")
        builder.setMessage("该账号已被其他设备登录")
        builder.setCancelable(false)
        builder.setPositiveButton("确定") { dialog, which ->
            dialog.dismiss()
            startActivity(Intent(this@SmartHomeActivity, LoginActivity::class.java))
            SharedPreferencesUtils.cleanPassword(this@SmartHomeActivity)
            MainActivity.instance.finish()
            finish()
        }
        builder.create().show()
    }

    companion object {
        private val EXTRA_IMAGE = "Bitmap"
        private val EXTRA_POSITION = "EXTRA_POSITION"

        var instance: SmartHomeActivity? = null

        private fun getPosition(intent: Intent): Position? {
            return intent.getParcelableExtra(EXTRA_POSITION)
        }

        fun startActivity(context: Context, height: Int, top: Int, width: Int, position: Int) {
            val intent = Intent(context, SmartHomeActivity::class.java)
            intent.putExtra("height", height)
            intent.putExtra("top", top)
            intent.putExtra("width", width)
            intent.putExtra(EXTRA_IMAGE, position)
            context.startActivity(intent)
        }

        var isForeground = false
        val MESSAGE_RECEIVED_ACTION = "com.massky.smartcommunity.MESSAGE_RECEIVED_ACTION"
        val KEY_TITLE = "title"
        val KEY_MESSAGE = "message"
        val KEY_EXTRAS = "extras"
    }
}
