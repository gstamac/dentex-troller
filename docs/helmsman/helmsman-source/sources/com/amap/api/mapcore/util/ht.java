package com.amap.api.mapcore.util;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import com.autonavi.amap.mapcore.Inner_3dMap_location;
import com.autonavi.amap.mapcore.Inner_3dMap_locationListener;
import com.autonavi.amap.mapcore.Inner_3dMap_locationManagerBase;
import com.autonavi.amap.mapcore.Inner_3dMap_locationOption;
import java.util.ArrayList;
import java.util.Iterator;

/* JADX INFO: compiled from: MapLocationManager.java */
/* JADX INFO: loaded from: classes.dex */
public class ht implements Inner_3dMap_locationManagerBase {
    Context a;
    ArrayList<Inner_3dMap_locationListener> b = new ArrayList<>();
    Object c = new Object();
    Handler d = null;
    a e = null;
    Handler f = null;
    Inner_3dMap_locationOption g = new Inner_3dMap_locationOption();
    hw h = null;
    Inner_3dMap_locationOption.Inner_3dMap_Enum_LocationMode i = Inner_3dMap_locationOption.Inner_3dMap_Enum_LocationMode.Hight_Accuracy;
    boolean j = false;

    /* JADX INFO: compiled from: MapLocationManager.java */
    static class a extends HandlerThread {
        ht a;

        public a(String str, ht htVar) {
            super(str);
            this.a = htVar;
        }

        @Override // android.os.HandlerThread
        protected final void onLooperPrepared() {
            try {
                this.a.h = new hw(this.a.a, this.a.d);
                super.onLooperPrepared();
            } catch (Throwable unused) {
            }
        }
    }

    public ht(Context context) {
        this.a = null;
        if (context == null) {
            throw new IllegalArgumentException("Context参数不能为null");
        }
        this.a = context.getApplicationContext();
        e();
    }

    private Handler a(Looper looper) {
        hu huVar;
        synchronized (this.c) {
            huVar = new hu(looper, this);
            this.f = huVar;
        }
        return huVar;
    }

    private void a(int i) {
        synchronized (this.c) {
            Handler handler = this.f;
            if (handler != null) {
                handler.removeMessages(i);
            }
        }
    }

    private void a(int i, Object obj, long j) {
        synchronized (this.c) {
            if (this.f != null) {
                Message messageObtain = Message.obtain();
                messageObtain.what = i;
                messageObtain.obj = obj;
                this.f.sendMessageDelayed(messageObtain, j);
            }
        }
    }

    private void e() {
        try {
            this.d = Looper.myLooper() == null ? new hv(this.a.getMainLooper(), this) : new hv(this);
        } catch (Throwable th) {
            ij.a(th, "LocationClientManager", "initResultHandler");
        }
        try {
            a aVar = new a("locaitonClientActionThread", this);
            this.e = aVar;
            aVar.setPriority(5);
            this.e.start();
            this.f = a(this.e.getLooper());
        } catch (Throwable th2) {
            ij.a(th2, "LocationClientManager", "initActionThreadAndActionHandler");
        }
    }

    private void f() {
        synchronized (this.c) {
            Handler handler = this.f;
            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
            }
            this.f = null;
        }
    }

    final void a() {
        try {
            if (this.j) {
                return;
            }
            this.j = true;
            a(1005, null, 0L);
        } catch (Throwable th) {
            ij.a(th, "LocationClientManager", "doStartLocation");
        }
    }

    final void a(Inner_3dMap_location inner_3dMap_location) {
        try {
            if (this.j) {
                if (!"gps".equalsIgnoreCase(inner_3dMap_location.getProvider())) {
                    inner_3dMap_location.setProvider("lbs");
                }
                inner_3dMap_location.setAltitude(im.b(inner_3dMap_location.getAltitude()));
                inner_3dMap_location.setBearing(im.a(inner_3dMap_location.getBearing()));
                inner_3dMap_location.setSpeed(im.a(inner_3dMap_location.getSpeed()));
                Iterator<Inner_3dMap_locationListener> it = this.b.iterator();
                while (it.hasNext()) {
                    it.next().onLocationChanged(inner_3dMap_location);
                }
            }
            if (this.g.isOnceLocation()) {
                c();
            }
        } catch (Throwable th) {
            ij.a(th, "LocationClientManger", "callBackLocation");
        }
    }

    final void a(Inner_3dMap_locationListener inner_3dMap_locationListener) {
        try {
            if (inner_3dMap_locationListener == null) {
                throw new IllegalArgumentException("listener参数不能为null");
            }
            if (this.b == null) {
                this.b = new ArrayList<>();
            }
            if (this.b.contains(inner_3dMap_locationListener)) {
                return;
            }
            this.b.add(inner_3dMap_locationListener);
        } catch (Throwable th) {
            ij.a(th, "LocationClientManager", "doSetLocationListener");
        }
    }

    final void a(Inner_3dMap_locationOption inner_3dMap_locationOption) {
        this.g = inner_3dMap_locationOption;
        if (inner_3dMap_locationOption == null) {
            this.g = new Inner_3dMap_locationOption();
        }
        hw hwVar = this.h;
        if (hwVar != null) {
            hwVar.a(this.g);
        }
        if (this.j && !this.i.equals(inner_3dMap_locationOption.getLocationMode())) {
            c();
            a();
        }
        this.i = this.g.getLocationMode();
    }

    final void b() {
        long interval = 1000;
        try {
            hw hwVar = this.h;
            if (hwVar != null) {
                hwVar.a();
            }
            if (this.g.isOnceLocation()) {
                return;
            }
            a(1005, null, this.g.getInterval() >= 1000 ? this.g.getInterval() : 1000L);
        } catch (Throwable th) {
            try {
                ij.a(th, "LocationClientManager", "doGetLocation");
            } finally {
                if (!this.g.isOnceLocation()) {
                    a(1005, null, this.g.getInterval() >= 1000 ? this.g.getInterval() : 1000L);
                }
            }
        }
    }

    final void b(Inner_3dMap_locationListener inner_3dMap_locationListener) {
        if (inner_3dMap_locationListener != null) {
            try {
                if (!this.b.isEmpty() && this.b.contains(inner_3dMap_locationListener)) {
                    this.b.remove(inner_3dMap_locationListener);
                }
            } catch (Throwable th) {
                ij.a(th, "LocationClientManager", "doUnregisterListener");
                return;
            }
        }
        if (this.b.isEmpty()) {
            c();
        }
    }

    final void c() {
        try {
            this.j = false;
            a(1004);
            a(1005);
            hw hwVar = this.h;
            if (hwVar != null) {
                hwVar.c();
            }
        } catch (Throwable th) {
            ij.a(th, "LocationClientManager", "doStopLocation");
        }
    }

    final void d() {
        c();
        hw hwVar = this.h;
        if (hwVar != null) {
            hwVar.d();
        }
        f();
        if (this.e != null) {
            try {
                ik.a(this.e, (Class<?>) HandlerThread.class, "quitSafely", new Object[0]);
            } catch (Throwable unused) {
                this.e.quit();
            }
        }
        this.e = null;
    }

    @Override // com.autonavi.amap.mapcore.Inner_3dMap_locationManagerBase
    public void destroy() {
        try {
            a(1007, null, 0L);
        } catch (Throwable th) {
            ij.a(th, "LocationClientManager", "stopLocation");
        }
    }

    @Override // com.autonavi.amap.mapcore.Inner_3dMap_locationManagerBase
    public void setLocationListener(Inner_3dMap_locationListener inner_3dMap_locationListener) {
        try {
            a(1002, inner_3dMap_locationListener, 0L);
        } catch (Throwable th) {
            ij.a(th, "LocationClientManager", "setLocationListener");
        }
    }

    @Override // com.autonavi.amap.mapcore.Inner_3dMap_locationManagerBase
    public void setLocationOption(Inner_3dMap_locationOption inner_3dMap_locationOption) {
        try {
            a(1001, inner_3dMap_locationOption, 0L);
        } catch (Throwable th) {
            ij.a(th, "LocationClientManager", "setLocationOption");
        }
    }

    @Override // com.autonavi.amap.mapcore.Inner_3dMap_locationManagerBase
    public void startLocation() {
        try {
            a(1004, null, 0L);
        } catch (Throwable th) {
            ij.a(th, "LocationClientManager", "startLocation");
        }
    }

    @Override // com.autonavi.amap.mapcore.Inner_3dMap_locationManagerBase
    public void stopLocation() {
        try {
            a(1006, null, 0L);
        } catch (Throwable th) {
            ij.a(th, "LocationClientManager", "stopLocation");
        }
    }

    @Override // com.autonavi.amap.mapcore.Inner_3dMap_locationManagerBase
    public void unRegisterLocationListener(Inner_3dMap_locationListener inner_3dMap_locationListener) {
        try {
            a(1006, inner_3dMap_locationListener, 0L);
        } catch (Throwable th) {
            ij.a(th, "LocationClientManager", "stopLocation");
        }
    }
}
