package im.helmsman.helmsmanandroid.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;
import im.helmsman.helmsmanandroid.ui.activity.CommentContentActivity;
import im.helmsman.helmsmanandroid.ui.activity.FavouriteMessageActivity;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class JpushMessageReceiver extends BroadcastReceiver {
    private String TAG = "JpushMessageReceiver";
    private NotificationManager nm;

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        Intent intent2;
        Bundle extras = intent.getExtras();
        if (this.nm == null) {
            this.nm = (NotificationManager) context.getSystemService("notification");
        }
        Log.d(this.TAG, "onReceive - " + intent.getAction() + ", extras: ");
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            Log.d(this.TAG, "JPush用户注册成功");
            return;
        }
        if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(this.TAG, "接受到推送下来的自定义消息");
            return;
        }
        if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            if (extras != null) {
                Log.d(this.TAG, "接受到推送下来的通知:" + extras.getString(JPushInterface.EXTRA_EXTRA));
                return;
            }
            return;
        }
        if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(this.TAG, "用户点击打开了通知");
            if (extras == null) {
                return;
            }
            try {
                JSONObject jSONObject = new JSONObject(extras.getString(JPushInterface.EXTRA_EXTRA));
                if (Integer.parseInt(jSONObject.getString("type")) == 0) {
                    intent2 = new Intent(context, (Class<?>) CommentContentActivity.class);
                    intent2.putExtra("sinceid", jSONObject.getInt("comment_id"));
                } else {
                    intent2 = new Intent(context, (Class<?>) FavouriteMessageActivity.class);
                }
                intent2.putExtra("messageid", jSONObject.getInt("message_id"));
                intent2.setFlags(268435456);
                context.startActivity(intent2);
                return;
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }
        }
        Log.d(this.TAG, "Unhandled intent - " + intent.getAction());
    }
}
