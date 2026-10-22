package cn.jpush.android.data;

import android.text.TextUtils;
import cn.jpush.android.api.JThirdPlatFormInterface;
import cn.jpush.android.helper.Logger;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class JPushLocalNotification implements Serializable {
    private static final long serialVersionUID = 1472982106750878137L;
    private String f;
    private String g;
    private String h;
    private long i;
    private int a = 1;
    private String b = "";
    private String c = "00";
    private String d = "00";
    private long e = 0;
    private long j = 1;
    private int k = 1;
    private String l = "";
    private String m = "";

    private void a(String str, String str2, JSONObject jSONObject) throws JSONException {
        if (TextUtils.isEmpty(str2)) {
            return;
        }
        jSONObject.put(str, str2);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && getClass() == obj.getClass() && this.j == ((JPushLocalNotification) obj).j;
    }

    public long getBroadcastTime() {
        return this.e;
    }

    public long getBuilderId() {
        return this.i;
    }

    public String getContent() {
        return this.f;
    }

    public String getExtras() {
        return this.h;
    }

    public long getNotificationId() {
        return this.j;
    }

    public String getTitle() {
        return this.g;
    }

    public int hashCode() {
        long j = this.j;
        return (int) (j ^ (j >>> 32));
    }

    public void setBroadcastTime(int i, int i2, int i3, int i4, int i5, int i6) {
        if (i < 0 || i2 < 1 || i2 > 12 || i3 < 1 || i3 > 31 || i4 < 0 || i4 > 23 || i5 < 0 || i5 > 59 || i6 < 0 || i6 > 59) {
            Logger.ee("JPushLocalNotification", "Set time fail! Please check your args!");
            return;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(i, i2 - 1, i3, i4, i5, i6);
        Date time = calendar.getTime();
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (time.getTime() < jCurrentTimeMillis) {
            this.e = jCurrentTimeMillis;
        } else {
            this.e = time.getTime();
        }
    }

    public void setBroadcastTime(long j) {
        this.e = j;
    }

    public void setBroadcastTime(Date date) {
        this.e = date.getTime();
    }

    public void setBuilderId(long j) {
        this.i = j;
    }

    public void setContent(String str) {
        this.f = str;
    }

    public void setExtras(String str) {
        this.h = str;
    }

    public void setNotificationId(long j) {
        this.j = (int) j;
    }

    public void setTitle(String str) {
        this.g = str;
    }

    public String toJSON() {
        JSONObject jSONObject = new JSONObject();
        try {
            JSONObject jSONObject2 = new JSONObject();
            if (!TextUtils.isEmpty(this.h)) {
                jSONObject2.put("n_extras", new JSONObject(this.h));
            }
            a("n_content", this.f, jSONObject2);
            a("n_title", this.g, jSONObject2);
            a("n_content", this.f, jSONObject2);
            jSONObject2.put("ad_t", 0);
            jSONObject.put("m_content", jSONObject2);
            a(JThirdPlatFormInterface.KEY_MSG_ID, "" + this.j, jSONObject);
            a("content_type", this.m, jSONObject);
            a("override_msg_id", this.l, jSONObject);
            jSONObject.put("n_only", this.k);
            jSONObject.put("n_builder_id", this.i);
            jSONObject.put("show_type", 3);
            jSONObject.put("notificaion_type", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject.toString();
    }
}
