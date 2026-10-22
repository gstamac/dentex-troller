package com.amap.api.mapcore.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ServiceInfo;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.autonavi.amap.mapcore.Inner_3dMap_locationListener;
import com.autonavi.amap.mapcore.Inner_3dMap_locationManagerBase;
import com.autonavi.amap.mapcore.Inner_3dMap_locationOption;

/* JADX INFO: compiled from: AMapLocationClient.java */
/* JADX INFO: loaded from: classes.dex */
public class en {
    Context a;
    Inner_3dMap_locationManagerBase b = null;
    Object c = null;
    boolean d = false;
    Object e = null;
    ff f;

    public en(Context context) {
        this.f = null;
        try {
            this.f = hz.a();
        } catch (Throwable unused) {
        }
        a(context);
    }

    private void a(Context context) {
        ServiceInfo serviceInfo;
        try {
            if (context == null) {
                throw new IllegalArgumentException("Context参数不能为null");
            }
            this.a = context.getApplicationContext();
            try {
                Class<?> cls = Class.forName("com.amap.api.location.AMapLocationClient");
                try {
                    serviceInfo = this.a.getPackageManager().getServiceInfo(new ComponentName(this.a, "com.amap.api.location.APSService"), 128);
                } catch (Throwable unused) {
                    serviceInfo = null;
                }
                if (cls != null && serviceInfo != null) {
                    this.d = true;
                }
            } catch (Throwable unused2) {
                this.d = false;
            }
            if (this.d) {
                this.c = new AMapLocationClient(this.a);
            } else {
                this.b = b(this.a);
            }
        } catch (Throwable th) {
            ij.a(th, "AMapLocationClient", "AMapLocationClient 1");
        }
    }

    private Inner_3dMap_locationManagerBase b(Context context) {
        Inner_3dMap_locationManagerBase htVar;
        try {
            htVar = (Inner_3dMap_locationManagerBase) gi.a(context, this.f, fg.c("YY29tLmFtYXAuYXBpLndyYXBwZXIuSW5uZXJfM2RNYXBfbG9jYXRpb25NYW5hZ2VyV3JhcHBlcg=="), ht.class, new Class[]{Context.class}, new Object[]{context});
        } catch (Throwable unused) {
            htVar = new ht(context);
        }
        return htVar == null ? new ht(context) : htVar;
    }

    public void a() {
        try {
            if (this.d) {
                ((AMapLocationClient) this.c).startLocation();
            } else {
                this.b.startLocation();
            }
        } catch (Throwable th) {
            ij.a(th, "AMapLocationClient", "startLocation");
        }
    }

    public void a(Inner_3dMap_locationListener inner_3dMap_locationListener) {
        try {
            if (inner_3dMap_locationListener == null) {
                throw new IllegalArgumentException("listener参数不能为null");
            }
            if (this.d) {
                ho.a(this.c, inner_3dMap_locationListener);
            } else {
                this.b.setLocationListener(inner_3dMap_locationListener);
            }
        } catch (Throwable th) {
            ij.a(th, "AMapLocationClient", "setLocationListener");
        }
    }

    public void a(Inner_3dMap_locationOption inner_3dMap_locationOption) {
        try {
            if (inner_3dMap_locationOption == null) {
                throw new IllegalArgumentException("LocationManagerOption参数不能为null");
            }
            if (!this.d) {
                this.b.setLocationOption(inner_3dMap_locationOption);
                return;
            }
            AMapLocationClientOption aMapLocationClientOptionA = ho.a();
            ho.a(aMapLocationClientOptionA, inner_3dMap_locationOption);
            ((AMapLocationClient) this.c).setLocationOption(aMapLocationClientOptionA);
        } catch (Throwable th) {
            ij.a(th, "AMapLocationClient", "setLocationOption");
        }
    }

    public void b() {
        try {
            if (this.d) {
                ((AMapLocationClient) this.c).stopLocation();
            } else {
                this.b.stopLocation();
            }
        } catch (Throwable th) {
            ij.a(th, "AMapLocationClient", "stopLocation");
        }
    }

    public void c() {
        try {
            if (this.d) {
                ((AMapLocationClient) this.c).onDestroy();
            } else {
                this.b.destroy();
            }
        } catch (Throwable th) {
            ij.a(th, "AMapLocationClient", "onDestroy");
        }
    }
}
