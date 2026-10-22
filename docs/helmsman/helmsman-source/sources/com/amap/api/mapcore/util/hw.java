package com.amap.api.mapcore.util;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import com.autonavi.amap.mapcore.Inner_3dMap_location;
import com.autonavi.amap.mapcore.Inner_3dMap_locationOption;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: compiled from: MapLocationService.java */
/* JADX INFO: loaded from: classes.dex */
public final class hw {
    private static int k = 200;
    private static boolean l = true;
    Context a;
    b d;
    Handler e;
    Handler f;
    Inner_3dMap_locationOption i;
    hr b = null;
    hx c = null;
    boolean g = false;
    boolean h = false;
    private JSONArray m = null;
    Object j = new Object();

    /* JADX INFO: compiled from: MapLocationService.java */
    public class a extends Handler {
        public a(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public final void handleMessage(Message message) {
            if (message.what != 1) {
                return;
            }
            hw.this.b();
        }
    }

    /* JADX INFO: compiled from: MapLocationService.java */
    class b extends HandlerThread {
        public b(String str) {
            super(str);
        }

        @Override // android.os.HandlerThread
        protected final void onLooperPrepared() {
            super.onLooperPrepared();
        }
    }

    public hw(Context context, Handler handler) {
        this.a = null;
        this.d = null;
        this.e = null;
        this.f = null;
        this.i = null;
        try {
            if (context == null) {
                throw new IllegalArgumentException("Context参数不能为null");
            }
            this.a = context.getApplicationContext();
            this.f = handler;
            this.i = new Inner_3dMap_locationOption();
            e();
            b bVar = new b("locServiceAction");
            this.d = bVar;
            bVar.setPriority(5);
            this.d.start();
            this.e = new a(this.d.getLooper());
        } catch (Throwable th) {
            ij.a(th, "LocationService", "<init>");
        }
    }

    private void a(Inner_3dMap_location inner_3dMap_location) {
        try {
            if (l && inner_3dMap_location != null && inner_3dMap_location.getErrorCode() == 0 && inner_3dMap_location.getLocationType() == 1) {
                if (this.m == null) {
                    this.m = new JSONArray();
                }
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("lon", inner_3dMap_location.getLongitude());
                jSONObject.put("lat", inner_3dMap_location.getLatitude());
                jSONObject.put("type", 0);
                jSONObject.put("timestamp", im.a());
                JSONArray jSONArrayPut = this.m.put(jSONObject);
                this.m = jSONArrayPut;
                if (jSONArrayPut.length() >= k) {
                    g();
                }
            }
        } catch (Throwable th) {
            ij.a(th, "LocationService", "recordOfflineLocLog");
        }
    }

    private void e() {
        try {
            if (this.i == null) {
                this.i = new Inner_3dMap_locationOption();
            }
            if (this.h) {
                return;
            }
            this.b = new hr(this.a);
            hx hxVar = new hx(this.a);
            this.c = hxVar;
            hxVar.a(this.i);
            f();
            this.h = true;
        } catch (Throwable th) {
            ij.a(th, "LocationService", "init");
        }
    }

    private void f() {
        try {
            l = il.b(this.a, "maploc", "ue");
            int iA = il.a(this.a, "maploc", "opn");
            k = iA;
            if (iA > 500) {
                k = 500;
            }
            if (k < 30) {
                k = 30;
            }
        } catch (Throwable th) {
            ij.a(th, "LocationService", "getSPConfig");
        }
    }

    private synchronized void g() {
        try {
            JSONArray jSONArray = this.m;
            if (jSONArray != null && jSONArray.length() > 0) {
                hh.a(new hg(this.a, ij.b(), this.m.toString()), this.a);
                this.m = null;
            }
        } catch (Throwable th) {
            ij.a(th, "LocationService", "writeOfflineLog");
        }
    }

    private void h() {
        synchronized (this.j) {
            Handler handler = this.e;
            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
            }
            this.e = null;
        }
    }

    private void i() {
        synchronized (this.j) {
            Handler handler = this.e;
            if (handler != null) {
                handler.removeMessages(1);
            }
        }
    }

    public final void a() {
        try {
            e();
            if (!this.i.getLocationMode().equals(Inner_3dMap_locationOption.Inner_3dMap_Enum_LocationMode.Battery_Saving) && !this.g) {
                this.g = true;
                this.b.a();
            }
            Handler handler = this.e;
            if (handler != null) {
                handler.sendEmptyMessage(1);
            }
        } catch (Throwable th) {
            ij.a(th, "LocationService", "getLocation");
        }
    }

    public final void a(Inner_3dMap_locationOption inner_3dMap_locationOption) {
        this.i = inner_3dMap_locationOption;
        if (inner_3dMap_locationOption == null) {
            this.i = new Inner_3dMap_locationOption();
        }
        hx hxVar = this.c;
        if (hxVar != null) {
            hxVar.a(inner_3dMap_locationOption);
        }
    }

    final void b() {
        Inner_3dMap_location inner_3dMap_locationA;
        try {
            if (this.i.getLocationMode().equals(Inner_3dMap_locationOption.Inner_3dMap_Enum_LocationMode.Battery_Saving) && this.g) {
                this.b.b();
                this.g = false;
            }
            if (this.b.c()) {
                inner_3dMap_locationA = this.b.d();
            } else {
                inner_3dMap_locationA = !this.i.getLocationMode().equals(Inner_3dMap_locationOption.Inner_3dMap_Enum_LocationMode.Device_Sensors) ? this.c.a() : null;
            }
            if (this.f != null && inner_3dMap_locationA != null) {
                Message messageObtain = Message.obtain();
                messageObtain.obj = inner_3dMap_locationA;
                messageObtain.what = 1;
                this.f.sendMessage(messageObtain);
            }
            a(inner_3dMap_locationA);
        } catch (Throwable th) {
            ij.a(th, "LocationService", "doGetLocation");
        }
    }

    public final void c() {
        this.g = false;
        try {
            i();
            hr hrVar = this.b;
            if (hrVar != null) {
                hrVar.b();
            }
        } catch (Throwable th) {
            ij.a(th, "LocationService", "stopLocation");
        }
    }

    public final void d() {
        try {
            c();
            h();
            if (this.d != null) {
                try {
                    ik.a(this.d, (Class<?>) HandlerThread.class, "quitSafely", new Object[0]);
                } catch (Throwable unused) {
                    this.d.quit();
                }
            }
            this.d = null;
            this.c.b();
            g();
        } catch (Throwable th) {
            ij.a(th, "LocationService", "destroy");
        }
    }
}
