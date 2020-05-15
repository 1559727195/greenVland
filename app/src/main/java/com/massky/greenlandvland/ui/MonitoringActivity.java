package com.massky.greenlandvland.ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.annotation.Size;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.massky.greenlandvland.R;
import com.massky.greenlandvland.common.CommonUtil;
import com.massky.greenlandvland.common.LocalBroadcastManager;
import com.massky.greenlandvland.common.Position;
import com.massky.greenlandvland.common.SharedPreferencesUtils;
import com.massky.greenlandvland.common.StatusbarUtils;
import com.massky.greenlandvland.model.entity.Message;
import com.massky.greenlandvland.model.entity.Sc_isLoginNew;
import com.massky.greenlandvland.model.httpclient.HttpUrl.HttpClient;
import com.massky.greenlandvland.model.httpclient.UICallback;

public class MonitoringActivity extends AppCompatActivity {
    private RelativeLayout rl_homemonitoring;
    private RelativeLayout rl_topmonitor;
    private ImageView drop;
    private Button btn_enter;
    private boolean isPlaying = false;
    private int height;
    private int top;
    private int width;
    private AlphaAnimation mShowAnimation;
    private AlphaAnimation mHideAnimation;
    private static final String EXTRA_IMAGE = "Bitmap";
    private static final String EXTRA_POSITION = "EXTRA_POSITION";
    public static MonitoringActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring);
        instance=this;
        //获取控件
        drop= (ImageView) findViewById(R.id.drop);
        btn_enter= (Button) findViewById(R.id.btn_enter);
        rl_homemonitoring= (RelativeLayout) findViewById(R.id.rl_homemonitoring);
        rl_topmonitor= (RelativeLayout) findViewById(R.id.rl_topmonitor);


        //添加监听
        drop.setOnClickListener(clickListener);
        btn_enter.setOnClickListener(clickListener);

        int [] string = {R.drawable.bg_intelligenceenter,R.drawable.bg_invitevisitor,R.drawable.bg_monitor,R.drawable.safe_location};
        int position = getIntent().getIntExtra(EXTRA_IMAGE,1);
        height = getIntent().getIntExtra("height",0);
        width = getIntent().getIntExtra("width",0);
        top = getIntent().getIntExtra("top",0);

        rl_homemonitoring.setBackgroundResource(string[position]);

        rl_homemonitoring.post(new Runnable() {
            @Override public void run() {
                anim(width,height,top, true,
                        new Runnable() {
                            @Override public void run() {

                            }
                        }, rl_homemonitoring);
            }
        });

        registerMessageReceiver();
    }
    View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.drop://下拉键
                    onBackPressed();
                    break;
                case R.id.btn_enter:
                    Intent intent=new Intent(MonitoringActivity.this,IndoorMonitorActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };


    /**
     * 动画封装，千万不要剁手改正负
     */
    private void anim(final int width,final int height,final int top, final boolean in,
                      final Runnable runnable, @Size(value = 3) View... views) {
        if (isPlaying) {
            return;
        }
        final View detailImg = views[0];
        //记住括号哦，我这里调试了一小时
        float delta = ((float) (width)) / ((float) (height));
        //243 - 168(navi) = 75 = status_bar
        float[] y_img = {
                top - (detailImg.getY() + (in ? (StatusbarUtils.getStatusBarOffsetPx(this)) : 0)),
                0
        };
        float[] s_img = {1f, 1f};

        float[] t_img = {-621.0f, 0.0f};

//        float[] y_icn = { views[guide1].getHeight() * guide4, 0 };

        detailImg.setPivotX(detailImg.getWidth() / 2);
        detailImg.setPivotY(0);
//        views[guide1].setPivotX(views[guide1].getWidth() / guide2);
//        views[guide1].setPivotY(0);
//        ImageView bg_smart_home = ((ImageView) views[guide2]);
//        if (drawable != null) {
//            bg_smart_home.setImageDrawable(drawable);
//        }
        Animator trans_Y =
                ObjectAnimator.ofFloat(detailImg, View.TRANSLATION_Y, in ? y_img[0] : y_img[1],
                        in ? y_img[1] : y_img[0]);
        Animator scale_X = ObjectAnimator.ofFloat(detailImg, View.SCALE_X, in ? s_img[0] : s_img[1],
                in ? s_img[1] : s_img[0]);
        Animator scale_Y = ObjectAnimator.ofFloat(detailImg, View.SCALE_Y, in ? s_img[0] : s_img[1],
                in ? s_img[1] : s_img[0]);
        Animator trans_T = ObjectAnimator.ofFloat(rl_topmonitor, View.TRANSLATION_Y, in ? t_img[0] : t_img[1],
                in ? t_img[1] : t_img[0]);

//        Animator alpha_icn = ObjectAnimator.ofFloat(views[guide1], View.ALPHA, in ? 0f : 1f, in ? 1f : 0f);
//        Animator alpha_bg = ObjectAnimator.ofFloat(views[guide2], View.ALPHA, in ? 0f : 1f, in ? 1f : 0f);
//
//        Animator trans_icn_Y =
//                ObjectAnimator.ofFloat(views[guide1], View.TRANSLATION_Y, in ? y_icn[0] : y_icn[guide1],
//                        in ? y_icn[guide1] : y_icn[0]);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(trans_Y, scale_X, scale_Y,trans_T);
//        set.playTogether(trans_icn_Y, alpha_icn, alpha_bg);
        set.setDuration(800);
        set.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animation) {
                isPlaying = true;
            }

            @Override public void onAnimationEnd(Animator animation) {
                isPlaying = false;
                runnable.run();
                if(in) {//说明动画刚进来
//                    top_img.clearAnimation();
//                    setShowAnimation (top_img,500);
//                    pull_down.setVisibility(View.VISIBLE);
                } else {//动画消失

                }
            }

            @Override public void onAnimationCancel(Animator animation) {
                isPlaying = false;
            }

            @Override public void onAnimationRepeat(Animator animation) {
            }
        });
        set.setInterpolator(new AccelerateInterpolator());
        set.start();
    }


    void anim(View view, Position position, final Runnable runnable, boolean isEnter,
              Animation.AnimationListener listener) {
        //记住括号哦，我这里调试了一小时
        float delta = ((float) position.width) / ((float) position.height);//wdith,height,top
        float fromDelta, toDelta, fromY, toY;
        if (isEnter) {
            fromDelta = 1f;
            toDelta = delta;
            fromY = position.top;
            toY = 0;
        } else {
            fromDelta = delta;
            toDelta = 1f;
            fromY = 0;
            toY = position.top;
        }
        Animation anim = new ScaleAnimation(fromDelta, toDelta,
                // Start and end values for the X axis scaling
                fromDelta, toDelta, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0.5f, // scale from mid of x
                Animation.RELATIVE_TO_SELF, 0f); // scale from start of y
        Animation trans = new TranslateAnimation(0, 0f, fromY, toY);
        AnimationSet set = new AnimationSet(true);
        //添加并行动画
        set.addAnimation(anim);
        set.addAnimation(trans);
        //动画结束后保持原样
        set.setFillEnabled(true);
        set.setFillAfter(true);
        //监听器
        set.setAnimationListener(listener);
        set.setDuration(1000);
        view.startAnimation(set);
    }

    private static Position getPosition(Intent intent) {
        return intent.getParcelableExtra(EXTRA_POSITION);
    }

    public static void startActivity(Context context, int height, int top, int width, int position) {
        Intent intent = new Intent(context, MonitoringActivity.class);
        intent.putExtra("height",height);
        intent.putExtra("top",top);
        intent.putExtra("width",width);
        intent.putExtra(EXTRA_IMAGE, position);
        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        back_to_main();
    }

    /**
     * View渐隐动画效果
     */
    public  void setHideAnimation( final View view, int duration)
    {
        if (null == view || duration < 0)
        {
            return;
        }

        if (null != mHideAnimation)
        {
            mHideAnimation.cancel();
        }
        // 监听动画结束的操作
        mHideAnimation = new AlphaAnimation(1.0f, 0.0f);//AlphaAnimation 是透明淡入淡出的动画
        mHideAnimation.setDuration(duration);
        mHideAnimation.setFillAfter(true);
        mHideAnimation.setAnimationListener(new Animation.AnimationListener()
        {

            @Override
            public void onAnimationStart(Animation arg0)
            {

            }

            @Override
            public void onAnimationRepeat(Animation arg0)
            {

            }

            @Override
            public void onAnimationEnd(Animation arg0)
            {
                view.setVisibility(View.GONE);
                drop.setVisibility(View.GONE);
                anim(width, height, top, false, new Runnable() {
                    @Override
                    public void run() {
                        MonitoringActivity.super.onBackPressed();
                        overridePendingTransition(0, 0);
                    }
                }, rl_homemonitoring);
            }
        });
        view.startAnimation(mHideAnimation);
    }


    /**
     * View渐现动画效果
     */
    public  void setShowAnimation( final View view, int duration)
    {
        if (null == view || duration < 0)
        {
            return;
        }
        if (null != mShowAnimation)
        {
            mShowAnimation.cancel();
        }
        mShowAnimation = new AlphaAnimation(0.0f, 1.0f);
        mShowAnimation.setDuration(duration);
        mShowAnimation.setFillAfter(true);
        mShowAnimation.setAnimationListener(null);
        view.setVisibility(View.VISIBLE);
        view.startAnimation(mShowAnimation);
    }


    private void back_to_main() {
//        top_img.clearAnimation();
//        setHideAnimation(top_img, 200);
        anim(width, height, top, false, new Runnable() {
            @Override
            public void run() {
                MonitoringActivity.super.onBackPressed();
                overridePendingTransition(0, 0);
            }
        }, rl_homemonitoring);
    }

    @Override
    protected void onResume() {
        isForeground = true;
        isLogin();
        super.onResume();
    }

    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    public static boolean isForeground = false;
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.massky.greenlandvland.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                    if (!TextUtils.isEmpty(extras)) {
                        showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                        Log.e("TAG", "showMsg=" + showMsg);
                        Message message = new Gson().fromJson(extras, Message.class);
                        Log.e("TAG", "message=" + message);
                        if (message.getType().equals("2")) {
                            showdialog();
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("TAG","错误");
            }
        }
    }

    private void isLogin() {
        final String phoneNumber= SharedPreferencesUtils.getPhoneNumber(this);
        final String phoneId = SharedPreferencesUtils.getPhoneId(this);
        HttpClient.post(CommonUtil.APPURL, "sc_isLoginNew"
                , new Gson().toJson(new Sc_isLoginNew.LoginNewParams(phoneNumber,phoneId,3))
                , new UICallback() {
                    @Override
                    public void process(String data) {
                        Log.e("TAG","data="+data);
                        Log.e("TAG","phoneNumber="+phoneNumber);
                        Log.e("TAG","phoneId="+phoneId);
                        Sc_isLoginNew.LoginNewResult isLoginResult=new Gson().fromJson(data, Sc_isLoginNew.LoginNewResult.class);
                        int result=isLoginResult.getResult();
                        if(result==1){
                            Log.e("TAG","1-json格式解析失败");
                        }else if(result==100){
                            Log.e("TAG","100-成功");
                        }else if(result==103){
                            Log.e("TAG","103-已登录");
                            showdialog();
                        }else {

                        }
                    }

                    @Override
                    public void onError(String data) {

                    }
                });
    }

    private void showdialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(MonitoringActivity.this,1);
        builder.setTitle("提示");
        builder.setMessage("该账号已被其他设备登录");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startActivity(new Intent(MonitoringActivity.this, LoginActivity.class));
                SharedPreferencesUtils.cleanPassword(MonitoringActivity.this);
                MainActivity.instance.finish();
                finish();
            }
        });
        builder.create().show();
    }
}
