package cn.jpush.android.b;

import android.content.Context;
import android.text.TextUtils;
import cn.jiguang.api.JCoreManager;
import cn.jiguang.api.ReportCallBack;
import cn.jpush.android.cache.Key;
import cn.jpush.android.cache.Sp;
import cn.jpush.android.helper.Logger;
import cn.jpush.android.local.JPushConstants;
import cn.jpush.android.t.f;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public abstract class c {
    protected Context a;
    protected LinkedHashMap<String, b> b;
    protected int c;

    public c(Context context) {
        this.c = 100;
        this.a = context.getApplicationContext();
        this.c = cn.jpush.android.cache.a.e(context, 10);
        a(context);
        b(this.c);
    }

    private static b a(cn.jpush.android.d.d dVar) {
        b bVar = new b();
        bVar.a = dVar.Z;
        bVar.b = dVar.aa;
        bVar.c = dVar.ab;
        bVar.d = dVar.ac;
        bVar.e = dVar.ae;
        bVar.f = dVar.af;
        bVar.g = dVar.ag;
        bVar.h = dVar.ah;
        return bVar;
    }

    /* JADX WARN: Code duplicated, block: B:33:0x00ce  */
    /* JADX WARN: Multi-variable type inference failed */
    private void a(Context context) {
        LinkedHashMap<String, b> linkedHashMapB;
        try {
            File fileA = cn.jpush.android.t.c.a(context, "jpush_geofence_v5");
            if (fileA == null || !fileA.exists()) {
                int iIntValue = ((Integer) Sp.get(context, Key.PushVerCode())).intValue();
                if (iIntValue >= 318 && iIntValue <= 330) {
                    LinkedHashMap linkedHashMap = (LinkedHashMap) cn.jpush.android.t.c.a(cn.jpush.android.t.c.a(context, "jpush_geofence_v3"));
                    if (linkedHashMap != null && !linkedHashMap.isEmpty()) {
                        Logger.d("GeofenceAction", "old geofences:" + linkedHashMap);
                        LinkedHashMap<String, b> linkedHashMap2 = new LinkedHashMap<>();
                        for (Map.Entry entry : linkedHashMap.entrySet()) {
                            linkedHashMap2.put(entry.getKey(), a((cn.jpush.android.d.d) entry.getValue()));
                        }
                        if (!linkedHashMap2.isEmpty()) {
                            this.b = linkedHashMap2;
                            c();
                        }
                    }
                } else if (iIntValue > 330 && iIntValue < 350) {
                    linkedHashMapB = (LinkedHashMap) cn.jpush.android.t.c.a(cn.jpush.android.t.c.a(context, "jpush_geofence_v4"));
                }
                Sp.set(context, Key.PushVerCode().set(Integer.valueOf(cn.jpush.android.l.a.a)));
                if (this.b == null) {
                    this.b = new LinkedHashMap<>();
                }
                d();
                Logger.dd("GeofenceAction", "Recover geofence size:" + this.b.size());
            }
            linkedHashMapB = b(context);
            this.b = linkedHashMapB;
            Sp.set(context, Key.PushVerCode().set(Integer.valueOf(cn.jpush.android.l.a.a)));
        } catch (Throwable th) {
            Logger.dd("GeofenceAction", "recover geofence failed:" + th);
        }
        if (this.b == null) {
            this.b = new LinkedHashMap<>();
        }
        d();
        Logger.dd("GeofenceAction", "Recover geofence size:" + this.b.size());
    }

    private void a(Context context, b bVar) {
        try {
            Logger.d("GeofenceAction", "geofence report id=" + bVar.a);
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("geofence_id", bVar.a);
            if (JCoreManager.onEvent(null, null, 26, null, null, jSONObject, "geo_fence") instanceof JSONObject) {
                JCoreManager.onEvent(context, JPushConstants.SDK_TYPE, 15, null, null, jSONObject, new ReportCallBack() { // from class: cn.jpush.android.b.c.1
                    @Override // cn.jiguang.api.ReportCallBack
                    public void onFinish(int i) {
                        Logger.d("GeofenceAction", "report with callback:" + i);
                    }
                });
            }
        } catch (Throwable th) {
            Logger.w("GeofenceAction", "report geofence error:" + th);
        }
    }

    private LinkedHashMap<String, b> b(Context context) {
        FileInputStream fileInputStream = null;
        if (context == null) {
            Logger.w("GeofenceAction", "unexcepted , context is null");
            return null;
        }
        LinkedHashMap<String, b> linkedHashMap = new LinkedHashMap<>();
        try {
            File fileA = cn.jpush.android.t.c.a(context, "jpush_geofence_v5");
            if (fileA.exists()) {
                FileInputStream fileInputStream2 = new FileInputStream(fileA);
                try {
                    JSONObject jSONObject = new JSONObject(new String(f.a((InputStream) fileInputStream2)));
                    Iterator<String> itKeys = jSONObject.keys();
                    while (itKeys.hasNext()) {
                        b bVarA = b.a(context, jSONObject.getJSONObject(itKeys.next()));
                        if (bVarA != null) {
                            linkedHashMap.put(bVarA.a, bVarA);
                        }
                    }
                    fileInputStream = fileInputStream2;
                } catch (Throwable th) {
                    th = th;
                    fileInputStream = fileInputStream2;
                    try {
                        Logger.w("GeofenceAction", "load objects error:" + th.getMessage());
                    } finally {
                        f.a((Closeable) fileInputStream);
                    }
                }
            }
        } catch (Throwable th2) {
            th = th2;
        }
        return linkedHashMap;
    }

    private void b(int i) {
        int size = this.b.size();
        if (size > i) {
            Logger.dd("GeofenceAction", "Geofence count= " + size + ",limit=" + i + ",need remove earliest");
            Iterator<b> it = this.b.values().iterator();
            while (it.hasNext() && size > i) {
                b next = it.next();
                if (next != null) {
                    b(next);
                }
                it.remove();
                size--;
            }
        }
    }

    private void d() {
        Iterator<Map.Entry<String, b>> it = this.b.entrySet().iterator();
        boolean z = false;
        while (it.hasNext()) {
            b value = it.next().getValue();
            if (value != null && value.e * 1000 <= System.currentTimeMillis()) {
                Logger.dd("GeofenceAction", "Geofence " + value.a + " is out of date!");
                it.remove();
                b(value);
                z = true;
            }
        }
        if (z) {
            c();
        }
    }

    private void e() {
        c();
        if (this.b.size() == 0) {
            a();
        } else {
            b();
        }
    }

    private boolean e(b bVar) {
        if (bVar != null && !TextUtils.isEmpty(bVar.a) && bVar.b != -1 && bVar.g >= -90.0d && bVar.g <= 90.0d && bVar.f >= -180.0d && bVar.f <= 180.0d) {
            return true;
        }
        Logger.ww("GeofenceAction", "The geofence is invalid, won't operate!");
        return false;
    }

    protected void a() {
    }

    public void a(int i) {
        cn.jpush.android.cache.a.d(this.a, i);
        this.c = i;
        b(i);
    }

    public void a(long j) {
    }

    protected abstract void a(b bVar);

    protected abstract void a(b bVar, b bVar2);

    public void a(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        b bVarRemove = this.b.remove(str);
        if (bVarRemove == null) {
            Logger.d("GeofenceAction", "geofence=" + str + " already not exists");
        } else {
            b(bVarRemove);
            e();
        }
    }

    public void a(String str, JSONObject jSONObject) {
        String str2;
        if (TextUtils.isEmpty(str) || jSONObject == null || jSONObject.length() == 0) {
            return;
        }
        Logger.dd("GeofenceAction", "Current geofence size:" + this.b.size());
        b bVar = this.b.get(str);
        if (bVar != null) {
            if (jSONObject.has("type")) {
                str2 = bVar.i != jSONObject.optInt("type", 1) ? "can not update geofence because type can't change" : "can not update geofence because origin one not found";
            }
            bVar.b(jSONObject);
            if (bVar.e * 1000 <= System.currentTimeMillis()) {
                this.b.remove(str);
                b(bVar);
            } else {
                a(bVar, bVar);
            }
            e();
            return;
        }
        Logger.ww("GeofenceAction", str2);
    }

    public void b() {
    }

    protected abstract void b(b bVar);

    protected void c() {
        Logger.dd("GeofenceAction", "save geofence to file");
        LinkedHashMap<String, b> linkedHashMap = this.b;
        if (linkedHashMap == null || linkedHashMap.isEmpty()) {
            return;
        }
        try {
            File fileA = cn.jpush.android.t.c.a(this.a, "jpush_geofence_v5");
            JSONObject jSONObject = new JSONObject();
            for (Map.Entry<String, b> entry : this.b.entrySet()) {
                jSONObject.put(entry.getKey(), entry.getValue().a());
            }
            cn.jpush.android.t.c.a(fileA.getAbsolutePath(), jSONObject.toString());
        } catch (Throwable th) {
            Logger.ww("GeofenceAction", "save geo to file error:" + th.getMessage());
        }
    }

    public void c(b bVar) {
        Logger.dd("GeofenceAction", "Current geofence size:" + this.b.size());
        if (e(bVar)) {
            b bVar2 = this.b.get(bVar.a);
            long j = bVar.e * 1000;
            if (bVar2 != null) {
                if (j <= System.currentTimeMillis()) {
                    this.b.remove(bVar.a);
                    b(bVar2);
                } else {
                    bVar.a(bVar2);
                    this.b.put(bVar.a, bVar);
                    a(bVar2, bVar);
                }
            } else if (j <= System.currentTimeMillis()) {
                Logger.dd("GeofenceAction", "The geofence " + bVar.a + " is out of date, will not create!");
                return;
            } else {
                b(this.c - 1);
                this.b.put(bVar.a, bVar);
                a(bVar);
            }
            e();
        }
    }

    protected void d(b bVar) {
        if (bVar != null) {
            try {
                if (bVar.i == 2) {
                    a(this.a, bVar);
                } else if (bVar.t != null) {
                    cn.jpush.android.d.b.a(this.a, bVar.t);
                } else {
                    Logger.w("GeofenceAction", "there is no push entity, won't show notification");
                }
            } catch (Throwable th) {
                Logger.ww("GeofenceAction", "process geofence error:" + th);
            }
        }
    }
}
