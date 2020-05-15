package com.massky.greenlandvland.common;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.massky.greenlandvland.R;


/**
 * @author ：程序员小冰
 * @新浪微博 ：http://weibo.com/mcxiaobing
 * @GitHub: https://github.com/QQ986945193
 * @CSDN博客: http://blog.csdn.net/qq_21376985
 */
public class DialogThridUtils {
    /**
     * 显示Dialog
     * @param context  上下文
//     * @param msg  显示内容
//     * @param isTransBg 是否透明
     * @param isCancelable 是否可以点击取消
     * @return
     */
    public static Dialog showWaitDialog(Context context, boolean isCancelable) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.thridlogin_dialog_loading, null);             // 得到加载view
        RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.dialog_view);// 加载布局

        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
//        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);   // 提示文字
        // 加载动画
        spaceshipImage.setBackgroundResource(R.drawable.loading);
        AnimationDrawable animationDrawable= (AnimationDrawable) spaceshipImage.getBackground();
        // 使用ImageView显示动画
        animationDrawable.start();
//        tipTextView.setText(msg);// 设置加载信息

        Dialog loadingDialog = new Dialog(context, R.style.TransDialogStyle );    // 创建自定义样式dialog
        loadingDialog.setContentView(layout);
        loadingDialog.setCancelable(isCancelable);
        loadingDialog.setCanceledOnTouchOutside(false);

        Window window = loadingDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.PopWindowAnimStyle);
        loadingDialog.show();
        return loadingDialog;
    }

    /**
     * 关闭dialog
     *
     * http://blog.csdn.net/qq_21376985
     *
     * @param mDialogUtils
     */
    public static void closeDialog(Dialog mDialogUtils) {
        if (mDialogUtils != null && mDialogUtils.isShowing()) {
            mDialogUtils.dismiss();
        }
    }
}
