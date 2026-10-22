package cn.jpush.android.d;

import android.text.TextUtils;
import cn.jpush.android.api.JThirdPlatFormInterface;
import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class a implements Serializable {
    private static final long serialVersionUID = -942487107643335186L;
    public String a;
    public String b;

    public a() {
    }

    public a(d dVar) {
        this.a = dVar.c;
        this.b = dVar.f;
    }

    public a(String str, String str2) {
        this.a = str;
        this.b = str2;
    }

    public static a a(JSONObject jSONObject) {
        return new a(jSONObject.optString(JThirdPlatFormInterface.KEY_MSG_ID), jSONObject.optString("override_msg_id"));
    }

    public JSONObject a() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(JThirdPlatFormInterface.KEY_MSG_ID, this.a);
            jSONObject.put("override_msg_id", this.b);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof a)) {
            return false;
        }
        a aVar = (a) obj;
        if (TextUtils.isEmpty(this.a) || TextUtils.isEmpty(aVar.a) || !TextUtils.equals(this.a, aVar.a)) {
            return false;
        }
        if (TextUtils.isEmpty(this.b) && TextUtils.isEmpty(aVar.b)) {
            return true;
        }
        return (TextUtils.isEmpty(this.b) || TextUtils.isEmpty(aVar.b) || !TextUtils.equals(this.b, aVar.b)) ? false : true;
    }

    public String toString() {
        return "msg_id = " + this.a + ",  override_msg_id = " + this.b;
    }
}
