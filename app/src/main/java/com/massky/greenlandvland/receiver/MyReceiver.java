package com.massky.greenlandvland.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.massky.greenlandvland.common.LocalBroadcastManager;
import com.massky.greenlandvland.ui.AboutActivity;
import com.massky.greenlandvland.ui.AddCamaraActivity;
import com.massky.greenlandvland.ui.AddMemberActivity;
import com.massky.greenlandvland.ui.ChangePassWordActivity;
import com.massky.greenlandvland.ui.ComplainActivity;
import com.massky.greenlandvland.ui.ComplainRecordActivity;
import com.massky.greenlandvland.ui.ComplainRecordDetialActivity;
import com.massky.greenlandvland.ui.DeveloperActivity;
import com.massky.greenlandvland.ui.DiscussActivity;
import com.massky.greenlandvland.ui.EnvironmentActivity;
import com.massky.greenlandvland.ui.EnvironmentmonitorActivity;
import com.massky.greenlandvland.ui.FamilyMemberActivity;
import com.massky.greenlandvland.ui.ForumDetialActivity;
import com.massky.greenlandvland.ui.IndoorMonitorActivity;
import com.massky.greenlandvland.ui.InformActivity;
import com.massky.greenlandvland.ui.InformDetialActivity;
import com.massky.greenlandvland.ui.IntelligenceEnterActivity;
import com.massky.greenlandvland.ui.InviteVisitorsActivity;
import com.massky.greenlandvland.ui.InvitevisitorActivity;
import com.massky.greenlandvland.ui.MainActivity;
import com.massky.greenlandvland.ui.PaymentActivity;
import com.massky.greenlandvland.ui.MonitoringActivity;
import com.massky.greenlandvland.ui.MyPostActivity;
import com.massky.greenlandvland.ui.MyVisiterActivity;
import com.massky.greenlandvland.ui.NotificationActivity;
import com.massky.greenlandvland.ui.PlayerItemActivity;
import com.massky.greenlandvland.ui.PostActivity;
import com.massky.greenlandvland.ui.PostDetialActivity;
import com.massky.greenlandvland.ui.ResponseDetialActivity;
import com.massky.greenlandvland.ui.RetroactionActivity;
import com.massky.greenlandvland.ui.SecretActivity;
import com.massky.greenlandvland.ui.SetActivity;
import com.massky.greenlandvland.ui.TransitActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			Bundle bundle = intent.getExtras();
			Log.e(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

			if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
				String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
				Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
				//send the Registration Id to your server...

			} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
				Log.e(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
				processCustomMessage(context, bundle);

			} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
				Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
				int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
				Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

			} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
				Log.d(TAG, "[MyReceiver] 用户点击打开了通知");

				//打开自定义的Activity
				Intent i = new Intent(context, MainActivity.class);
				i.putExtras(bundle);
				//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
				context.startActivity(i);

			} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
				Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
				//在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

			} else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
				boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
				Log.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
			} else {
				Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
			}
		} catch (Exception e){

		}

	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
					Log.i(TAG, "This message has no Extra data");
					continue;
				}

				try {
					JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
					Iterator<String> it =  json.keys();

					while (it.hasNext()) {
						String myKey = it.next().toString();
						sb.append("\nkey:" + key + ", value: [" +
								myKey + " - " +json.optString(myKey) + "]");
					}
				} catch (JSONException e) {
					Log.e(TAG, "Get message extra JSON error!");
				}

			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}
	
	//send msg to MainActivity
	private void processCustomMessage(Context context, Bundle bundle) {
		String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
		String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
		if (MainActivity.isForeground) {
			Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
			if (!TextUtils.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					Log.e("TAG","extraJson="+extraJson);
					if (extraJson.length() > 0) {
						msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
		}else if(IntelligenceEnterActivity.isForeground){
			Intent msgIntent = new Intent(IntelligenceEnterActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(IntelligenceEnterActivity.KEY_MESSAGE, message);
			if (!TextUtils.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					Log.e("TAG","extraJson="+extraJson);
					if (extraJson.length() > 0) {
						msgIntent.putExtra(IntelligenceEnterActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
		}else if(TransitActivity.isForeground){
			Intent msgIntent = new Intent(TransitActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(TransitActivity.KEY_MESSAGE, message);
			if (!TextUtils.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					Log.e("TAG","extraJson="+extraJson);
					if (extraJson.length() > 0) {
						msgIntent.putExtra(TransitActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
		}else if(InviteVisitorsActivity.isForeground){
			Intent msgIntent = new Intent(InviteVisitorsActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(InviteVisitorsActivity.KEY_MESSAGE, message);
			if (!TextUtils.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					Log.e("TAG","extraJson="+extraJson);
					if (extraJson.length() > 0) {
						msgIntent.putExtra(InviteVisitorsActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
		}else if(InvitevisitorActivity.isForeground){
			Intent msgIntent = new Intent(InvitevisitorActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(InvitevisitorActivity.KEY_MESSAGE, message);
			if (!TextUtils.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					Log.e("TAG","extraJson="+extraJson);
					if (extraJson.length() > 0) {
						msgIntent.putExtra(InvitevisitorActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
		}else if(EnvironmentmonitorActivity.isForeground){
			Intent msgIntent = new Intent(EnvironmentmonitorActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(EnvironmentmonitorActivity.KEY_MESSAGE, message);
			if (!TextUtils.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					Log.e("TAG","extraJson="+extraJson);
					if (extraJson.length() > 0) {
						msgIntent.putExtra(EnvironmentmonitorActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
		}else if(EnvironmentActivity.isForeground){
			Intent msgIntent = new Intent(EnvironmentActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(EnvironmentActivity.KEY_MESSAGE, message);
			if (!TextUtils.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					Log.e("TAG","extraJson="+extraJson);
					if (extraJson.length() > 0) {
						msgIntent.putExtra(EnvironmentActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
		}else if(MonitoringActivity.isForeground){
			Intent msgIntent = new Intent(MonitoringActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(MonitoringActivity.KEY_MESSAGE, message);
			if (!TextUtils.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					Log.e("TAG","extraJson="+extraJson);
					if (extraJson.length() > 0) {
						msgIntent.putExtra(MonitoringActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
		}else if(IndoorMonitorActivity.isForeground){
			Intent msgIntent = new Intent(IndoorMonitorActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(IndoorMonitorActivity.KEY_MESSAGE, message);
			if (!TextUtils.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					Log.e("TAG","extraJson="+extraJson);
					if (extraJson.length() > 0) {
						msgIntent.putExtra(IndoorMonitorActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
		}else if(AddCamaraActivity.isForeground){
			Intent msgIntent = new Intent(AddCamaraActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(AddCamaraActivity.KEY_MESSAGE, message);
			if (!TextUtils.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					Log.e("TAG","extraJson="+extraJson);
					if (extraJson.length() > 0) {
						msgIntent.putExtra(AddCamaraActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
		}else if(PlayerItemActivity.isForeground){
			Intent msgIntent = new Intent(PlayerItemActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(PlayerItemActivity.KEY_MESSAGE, message);
			if (!TextUtils.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					Log.e("TAG","extraJson="+extraJson);
					if (extraJson.length() > 0) {
						msgIntent.putExtra(PlayerItemActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
		}else if(ComplainActivity.isForeground){
			Intent msgIntent = new Intent(ComplainActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(ComplainActivity.KEY_MESSAGE, message);
			if (!TextUtils.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					Log.e("TAG","extraJson="+extraJson);
					if (extraJson.length() > 0) {
						msgIntent.putExtra(ComplainActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
		}else if(ComplainRecordActivity.isForeground){
			Intent msgIntent = new Intent(ComplainRecordActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(ComplainRecordActivity.KEY_MESSAGE, message);
			if (!TextUtils.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					Log.e("TAG","extraJson="+extraJson);
					if (extraJson.length() > 0) {
						msgIntent.putExtra(ComplainRecordActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
		}else if(InformActivity.isForeground){
			Intent msgIntent = new Intent(InformActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(InformActivity.KEY_MESSAGE, message);
			if (!TextUtils.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					Log.e("TAG","extraJson="+extraJson);
					if (extraJson.length() > 0) {
						msgIntent.putExtra(InformActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
		}else if(PaymentActivity.isForeground){
			Intent msgIntent = new Intent(PaymentActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(PaymentActivity.KEY_MESSAGE, message);
			if (!TextUtils.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					Log.e("TAG","extraJson="+extraJson);
					if (extraJson.length() > 0) {
						msgIntent.putExtra(PaymentActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
		}else if(MyPostActivity.isForeground){
			Intent msgIntent = new Intent(MyPostActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(MyPostActivity.KEY_MESSAGE, message);
			if (!TextUtils.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					Log.e("TAG","extraJson="+extraJson);
					if (extraJson.length() > 0) {
						msgIntent.putExtra(MyPostActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
		}else if(PostActivity.isForeground){
			Intent msgIntent = new Intent(PostActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(PostActivity.KEY_MESSAGE, message);
			if (!TextUtils.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					Log.e("TAG","extraJson="+extraJson);
					if (extraJson.length() > 0) {
						msgIntent.putExtra(PostActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
		}else if(ForumDetialActivity.isForeground){
			Intent msgIntent = new Intent(ForumDetialActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(ForumDetialActivity.KEY_MESSAGE, message);
			if (!TextUtils.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					Log.e("TAG","extraJson="+extraJson);
					if (extraJson.length() > 0) {
						msgIntent.putExtra(ForumDetialActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
		}else if(DiscussActivity.isForeground){
			Intent msgIntent = new Intent(DiscussActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(DiscussActivity.KEY_MESSAGE, message);
			if (!TextUtils.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					Log.e("TAG","extraJson="+extraJson);
					if (extraJson.length() > 0) {
						msgIntent.putExtra(DiscussActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
		}else if(SetActivity.isForeground){
			Intent msgIntent = new Intent(SetActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(SetActivity.KEY_MESSAGE, message);
			if (!TextUtils.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					Log.e("TAG","extraJson="+extraJson);
					if (extraJson.length() > 0) {
						msgIntent.putExtra(SetActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
		}else if(RetroactionActivity.isForeground){
			Intent msgIntent = new Intent(RetroactionActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(RetroactionActivity.KEY_MESSAGE, message);
			if (!TextUtils.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					Log.e("TAG","extraJson="+extraJson);
					if (extraJson.length() > 0) {
						msgIntent.putExtra(RetroactionActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
		}else if(NotificationActivity.isForeground){
			Intent msgIntent = new Intent(NotificationActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(NotificationActivity.KEY_MESSAGE, message);
			if (!TextUtils.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					Log.e("TAG","extraJson="+extraJson);
					if (extraJson.length() > 0) {
						msgIntent.putExtra(NotificationActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
		}else if(FamilyMemberActivity.isForeground){
			Intent msgIntent = new Intent(FamilyMemberActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(FamilyMemberActivity.KEY_MESSAGE, message);
			if (!TextUtils.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					Log.e("TAG","extraJson="+extraJson);
					if (extraJson.length() > 0) {
						msgIntent.putExtra(FamilyMemberActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
		}else if(AddMemberActivity.isForeground){
			Intent msgIntent = new Intent(AddMemberActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(AddMemberActivity.KEY_MESSAGE, message);
			if (!TextUtils.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					Log.e("TAG","extraJson="+extraJson);
					if (extraJson.length() > 0) {
						msgIntent.putExtra(AddMemberActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
		}else if(MyVisiterActivity.isForeground){
			Intent msgIntent = new Intent(MyVisiterActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(MyVisiterActivity.KEY_MESSAGE, message);
			if (!TextUtils.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					Log.e("TAG","extraJson="+extraJson);
					if (extraJson.length() > 0) {
						msgIntent.putExtra(MyVisiterActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
		}else if(AboutActivity.isForeground){
			Intent msgIntent = new Intent(AboutActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(AboutActivity.KEY_MESSAGE, message);
			if (!TextUtils.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					Log.e("TAG","extraJson="+extraJson);
					if (extraJson.length() > 0) {
						msgIntent.putExtra(AboutActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
		}else if(DeveloperActivity.isForeground){
			Intent msgIntent = new Intent(DeveloperActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(DeveloperActivity.KEY_MESSAGE, message);
			if (!TextUtils.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					Log.e("TAG","extraJson="+extraJson);
					if (extraJson.length() > 0) {
						msgIntent.putExtra(DeveloperActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
		}else if(SecretActivity.isForeground){
			Intent msgIntent = new Intent(SecretActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(SecretActivity.KEY_MESSAGE, message);
			if (!TextUtils.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					Log.e("TAG","extraJson="+extraJson);
					if (extraJson.length() > 0) {
						msgIntent.putExtra(SecretActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
		}else if(ChangePassWordActivity.isForeground){
			Intent msgIntent = new Intent(ChangePassWordActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(ChangePassWordActivity.KEY_MESSAGE, message);
			if (!TextUtils.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					Log.e("TAG","extraJson="+extraJson);
					if (extraJson.length() > 0) {
						msgIntent.putExtra(ChangePassWordActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
		}else if(InformDetialActivity.isForeground){
			Intent msgIntent = new Intent(InformDetialActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(InformDetialActivity.KEY_MESSAGE, message);
			if (!TextUtils.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					Log.e("TAG","extraJson="+extraJson);
					if (extraJson.length() > 0) {
						msgIntent.putExtra(InformDetialActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
		}else if(PostDetialActivity.isForeground){
			Intent msgIntent = new Intent(PostDetialActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(PostDetialActivity.KEY_MESSAGE, message);
			if (!TextUtils.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					Log.e("TAG","extraJson="+extraJson);
					if (extraJson.length() > 0) {
						msgIntent.putExtra(PostDetialActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
		}else if(ResponseDetialActivity.isForeground){
			Intent msgIntent = new Intent(ResponseDetialActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(ResponseDetialActivity.KEY_MESSAGE, message);
			if (!TextUtils.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					Log.e("TAG","extraJson="+extraJson);
					if (extraJson.length() > 0) {
						msgIntent.putExtra(ResponseDetialActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
		}else if(ComplainRecordDetialActivity.isForeground){
			Intent msgIntent = new Intent(ComplainRecordDetialActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(ComplainRecordDetialActivity.KEY_MESSAGE, message);
			if (!TextUtils.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					Log.e("TAG","extraJson="+extraJson);
					if (extraJson.length() > 0) {
						msgIntent.putExtra(ComplainRecordDetialActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
		}
	}
}
