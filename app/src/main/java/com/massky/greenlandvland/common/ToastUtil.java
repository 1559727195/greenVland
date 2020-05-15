package com.massky.greenlandvland.common;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Toast工具类
 *
 * @author yuanc
 */
public class ToastUtil {
	/*用于展示toast中部位置*/
	public static void showToast(Context context, String toastmsg) {
		Toast toast = Toast.makeText(context,
				toastmsg, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	/*用于展示toast默认位置*/
	public static void showDelToast(Context context, String toastmag) {
		Toast.makeText(context, toastmag,
				Toast.LENGTH_SHORT).show();
	}
}