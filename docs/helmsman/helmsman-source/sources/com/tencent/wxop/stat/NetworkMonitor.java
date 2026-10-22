package com.tencent.wxop.stat;

import com.facebook.appevents.UserDataStore;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class NetworkMonitor {
    private long a = 0;
    private int b = 0;
    private String c = "";
    private int d = 0;
    private String e = "";

    public String getDomain() {
        return this.c;
    }

    public long getMillisecondsConsume() {
        return this.a;
    }

    public int getPort() {
        return this.d;
    }

    public String getRemoteIp() {
        return this.e;
    }

    public int getStatusCode() {
        return this.b;
    }

    public void setDomain(String str) {
        this.c = str;
    }

    public void setMillisecondsConsume(long j) {
        this.a = j;
    }

    public void setPort(int i) {
        this.d = i;
    }

    public void setRemoteIp(String str) {
        this.e = str;
    }

    public void setStatusCode(int i) {
        this.b = i;
    }

    public JSONObject toJSONObject() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("tm", this.a);
            jSONObject.put(UserDataStore.STATE, this.b);
            String str = this.c;
            if (str != null) {
                jSONObject.put("dm", str);
            }
            jSONObject.put("pt", this.d);
            String str2 = this.e;
            if (str2 != null) {
                jSONObject.put("rip", str2);
            }
            jSONObject.put("ts", System.currentTimeMillis() / 1000);
        } catch (JSONException unused) {
        }
        return jSONObject;
    }
}
