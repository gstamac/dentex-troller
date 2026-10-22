package cn.jpush.android.b;

import android.content.Context;
import android.text.TextUtils;
import cn.jpush.android.helper.Logger;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class d {
    private static d a;
    private static final Object b = new Object();
    private c c;

    private d(Context context) {
        b(context);
    }

    public static d a(Context context) {
        if (a == null) {
            synchronized (b) {
                if (a == null) {
                    a = new d(context);
                }
            }
        }
        return a;
    }

    private void b(Context context) {
        if (context == null) {
            Logger.dd("GeofenceManager", "context is null,init failed");
        } else {
            this.c = new a(context);
        }
    }

    public void a() {
        this.c.b();
    }

    public void a(int i) {
        this.c.a(i);
    }

    public void a(long j) {
        this.c.a(j);
    }

    public void a(b bVar) {
        Logger.dd("GeofenceManager", "recv geofence...");
        this.c.c(bVar);
    }

    public void a(String str) {
        this.c.a(str);
    }

    /* JADX WARN: Code duplicated, block: B:28:0x0060  */
    public void a(JSONObject jSONObject) {
        byte b2;
        try {
            JSONObject jSONObject2 = jSONObject.getJSONObject("content");
            if (jSONObject2 == null || jSONObject2.length() <= 0) {
                return;
            }
            String strOptString = jSONObject2.optString("op");
            String strOptString2 = jSONObject2.optString("geofenceid");
            if (!TextUtils.isEmpty(strOptString) && !TextUtils.isEmpty(strOptString2)) {
                int iHashCode = strOptString.hashCode();
                if (iHashCode != 96417) {
                    if (iHashCode != 99339) {
                        if (iHashCode == 116009 && strOptString.equals("upd")) {
                            b2 = 1;
                        } else {
                            b2 = -1;
                        }
                    } else if (strOptString.equals("del")) {
                        b2 = 2;
                    } else {
                        b2 = -1;
                    }
                } else if (strOptString.equals("add")) {
                    b2 = 0;
                } else {
                    b2 = -1;
                }
                if (b2 == 0) {
                    this.c.c(b.a(jSONObject2));
                    return;
                } else if (b2 == 1) {
                    this.c.a(strOptString2, jSONObject2);
                    return;
                } else if (b2 != 2) {
                    Logger.w("GeofenceManager", "operation " + strOptString + " not supported");
                    return;
                } else {
                    this.c.a(strOptString2);
                    return;
                }
            }
            Logger.w("GeofenceManager", "json error: required key not found");
        } catch (Throwable th) {
            Logger.w("GeofenceManager", "json error:" + th);
        }
    }

    public void b() {
        this.c.a();
    }
}
