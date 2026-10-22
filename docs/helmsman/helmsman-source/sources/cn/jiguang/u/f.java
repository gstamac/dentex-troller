package cn.jiguang.u;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.HandlerThread;
import com.facebook.places.model.PlaceFields;

/* JADX INFO: loaded from: classes.dex */
final class f {
    private static final Object e = new Object();
    private static f h;
    protected Handler a;
    private Context b;
    private LocationManager c;
    private cn.jiguang.v.b d;
    private boolean f;
    private String g;
    private LocationListener i = new g(this);

    private f(Context context) {
        this.b = context;
        this.c = (LocationManager) context.getSystemService(PlaceFields.LOCATION);
    }

    public static f a(Context context) {
        if (h == null) {
            synchronized (e) {
                if (h == null) {
                    h = new f(context);
                }
            }
        }
        return h;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(Location location) {
        if (location == null) {
            this.d = null;
            return;
        }
        cn.jiguang.w.a.a("JLocationGps", "updateGpsInfo location time:" + location.getTime());
        if (this.d == null) {
            this.d = new cn.jiguang.v.b();
        }
        this.d.a = cn.jiguang.d.b.a(this.b, location.getTime());
        this.d.b = location.getProvider();
        this.d.c = location.getLatitude();
        this.d.d = location.getLongitude();
        this.d.f = location.getBearing();
        this.d.g = location.getAccuracy();
    }

    private boolean a(Location location, Location location2) {
        boolean zEquals;
        if (location == null) {
            return false;
        }
        if (location2 == null) {
            return true;
        }
        long time = location.getTime() - location2.getTime();
        boolean z = time > 120000;
        boolean z2 = time < -120000;
        boolean z3 = time > 0;
        if (z) {
            return true;
        }
        if (z2) {
            return false;
        }
        int accuracy = (int) (location.getAccuracy() - location2.getAccuracy());
        boolean z4 = accuracy > 0;
        boolean z5 = accuracy < 0;
        boolean z6 = accuracy > 200;
        String provider = location.getProvider();
        String provider2 = location2.getProvider();
        if (provider == null) {
            zEquals = provider2 == null;
        } else {
            zEquals = provider.equals(provider2);
        }
        if (z5) {
            return true;
        }
        if (!z3 || z4) {
            return z3 && !z6 && zEquals;
        }
        return true;
    }

    static /* synthetic */ void b(f fVar) {
        cn.jiguang.w.a.a("JLocationGps", "gps will done");
        fVar.f = true;
        fVar.f();
        Handler handler = fVar.a;
        if (handler == null) {
            cn.jiguang.w.a.e("JLocationGps", "cellLocationManager is null,please check it");
            return;
        }
        if (handler.hasMessages(1004)) {
            fVar.a.removeMessages(1004);
        }
        if (fVar.a.hasMessages(1003)) {
            fVar.a.removeMessages(1003);
        }
        if (fVar.a.hasMessages(1001)) {
            fVar.a.removeMessages(1001);
        }
        if (fVar.a.hasMessages(1005)) {
            fVar.a.removeMessages(1005);
        }
        try {
            fVar.a.getLooper().quit();
        } catch (Throwable th) {
            try {
                cn.jiguang.w.a.f("JLocationGps", "quit handler failed:" + th.getMessage());
            } finally {
                fVar.a = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f() {
        try {
            LocationListener locationListener = this.i;
            if (locationListener == null) {
                cn.jiguang.w.a.d("JLocationGps", "Location listener is null , do nothing!");
                return;
            }
            LocationManager locationManager = this.c;
            if (locationManager != null) {
                locationManager.removeUpdates(locationListener);
            } else {
                cn.jiguang.w.a.d("JLocationGps", "locationManager is null , do nothing!");
            }
        } catch (Throwable th) {
            cn.jiguang.w.a.d("JLocationGps", "remove location listener failed  e:" + th.getMessage());
        }
    }

    final cn.jiguang.v.b a() {
        return this.d;
    }

    final boolean b() {
        return this.f;
    }

    final void c() {
        this.f = false;
        this.d = null;
        if (this.c == null) {
            cn.jiguang.w.a.d("JLocationGps", "get locationManager failed");
            this.f = true;
            return;
        }
        try {
            if (this.a == null) {
                HandlerThread handlerThread = new HandlerThread(PlaceFields.LOCATION);
                handlerThread.start();
                this.a = new h(this, handlerThread.getLooper());
            }
        } catch (Throwable th) {
            cn.jiguang.w.a.f("JLocationGps", "start load loc-info failed - error:" + th);
        }
        if (this.a == null) {
            cn.jiguang.w.a.d("JLocationGps", " mAsyncHandler is empty");
            this.f = true;
        } else if (this.c.isProviderEnabled("network")) {
            this.g = "network";
            this.a.sendEmptyMessage(1003);
        } else if (this.c.isProviderEnabled("gps")) {
            this.g = "gps";
            this.a.sendEmptyMessage(1003);
        } else {
            this.g = "network";
            this.a.sendEmptyMessage(1004);
        }
    }

    /* JADX WARN: Code duplicated, block: B:11:0x002f  */
    public final cn.jiguang.v.b d() {
        try {
            Location lastKnownLocation = this.c.getLastKnownLocation("gps");
            Location lastKnownLocation2 = this.c.getLastKnownLocation("network");
            Location lastKnownLocation3 = this.c.getLastKnownLocation("passive");
            if (a(lastKnownLocation, lastKnownLocation2)) {
                if (!a(lastKnownLocation, lastKnownLocation3)) {
                    lastKnownLocation = lastKnownLocation3;
                }
            } else if (a(lastKnownLocation2, lastKnownLocation3)) {
                lastKnownLocation = lastKnownLocation2;
            } else {
                lastKnownLocation = lastKnownLocation3;
            }
            if (lastKnownLocation == null || System.currentTimeMillis() - lastKnownLocation.getTime() >= 30000) {
                return null;
            }
            a(lastKnownLocation);
            cn.jiguang.w.a.a("JLocationGps", "bestLocation:" + this.d + ",curTime:" + System.currentTimeMillis());
            return this.d;
        } catch (Throwable th) {
            cn.jiguang.w.a.d("JLocationGps", "loadLastGpsInfo failed: " + th.getMessage());
            return null;
        }
    }

    public final boolean e() {
        String str;
        try {
            LocationManager locationManager = this.c;
            if (locationManager != null) {
                return locationManager.isProviderEnabled("gps") || this.c.isProviderEnabled("network") || this.c.isProviderEnabled("passive");
            }
            return false;
        } catch (IllegalArgumentException unused) {
            str = "The provider [gps] is illegal argument!";
            cn.jiguang.w.a.d("JLocationGps", str);
            return false;
        } catch (SecurityException unused2) {
            str = "No suitable permission is present when get GPS_PROVIDER!";
            cn.jiguang.w.a.d("JLocationGps", str);
            return false;
        } catch (Exception unused3) {
            str = "The ILocationManager is null!";
            cn.jiguang.w.a.d("JLocationGps", str);
            return false;
        }
    }
}
