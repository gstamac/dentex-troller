package cn.jiguang.m;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import cn.jiguang.l.h;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class c extends cn.jiguang.l.a {
    private static volatile c c;
    private Context a;
    private JSONObject b;

    public static c a() {
        if (c == null) {
            synchronized (c.class) {
                c = new c();
            }
        }
        return c;
    }

    @Override // cn.jiguang.l.a
    protected final String a(Context context) {
        this.a = context;
        return "JDeviceBattery";
    }

    /* JADX WARN: Code duplicated, block: B:18:0x0051 A[PHI: r10
  0x0051: PHI (r10v1 int) = (r10v0 int), (r10v2 int) binds: [B:9:0x0043, B:15:0x004c] A[DONT_GENERATE, DONT_INLINE]] */
    @Override // cn.jiguang.l.a
    protected final void b(Context context, String str) {
        try {
            Intent intentRegisterReceiver = context.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"), context.getPackageName() + cn.jiguang.a.a.a, null);
            if (intentRegisterReceiver == null) {
                return;
            }
            int intExtra = intentRegisterReceiver.getIntExtra("level", -1);
            int intExtra2 = intentRegisterReceiver.getIntExtra("scale", -1);
            int intExtra3 = intentRegisterReceiver.getIntExtra("status", -1);
            if (intExtra3 != 1) {
                int i = 2;
                if (intExtra3 != 2) {
                    i = 3;
                    if (intExtra3 == 3 || intExtra3 == 4) {
                        intExtra3 = 1;
                    } else if (intExtra3 == 5) {
                        intExtra3 = i;
                    }
                } else {
                    intExtra3 = i;
                }
            } else {
                intExtra3 = 0;
            }
            int intExtra4 = intentRegisterReceiver.getIntExtra("voltage", -1);
            int intExtra5 = intentRegisterReceiver.getIntExtra("temperature", -1);
            if (this.b == null) {
                this.b = new JSONObject();
            }
            this.b.put("level", intExtra);
            this.b.put("scale", intExtra2);
            this.b.put("status", intExtra3);
            this.b.put("voltage", intExtra4);
            this.b.put("temperature", intExtra5);
            cn.jiguang.w.a.a("JDeviceBattery", "collect success:" + this.b);
        } catch (JSONException e) {
            cn.jiguang.w.a.d("JDeviceBattery", "packageJson exception: " + e.getMessage());
        }
    }

    @Override // cn.jiguang.l.a
    protected final void d(Context context, String str) {
        JSONObject jSONObject = this.b;
        if (jSONObject == null) {
            cn.jiguang.w.a.d("JDeviceBattery", "there are no data to report");
            return;
        }
        cn.jiguang.d.a.a(context, jSONObject, "battery");
        h.a(context, (Object) this.b);
        super.d(context, str);
        this.b = null;
    }
}
