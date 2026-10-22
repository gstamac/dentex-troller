package cn.jpush.android.b;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;
import cn.jpush.android.helper.Logger;
import cn.jpush.android.local.JPushConstants;
import cn.jpush.android.t.f;
import com.facebook.places.model.PlaceFields;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes.dex */
public class a extends c {
    private long d;
    private LocationManager e;
    private Handler f;
    private long g;
    private long h;
    private String i;
    private boolean j;
    private LocationListener k;
    private final AtomicBoolean l;

    public a(Context context) {
        super(context);
        this.g = 900000L;
        this.h = 20000L;
        this.j = false;
        this.k = new LocationListener() { // from class: cn.jpush.android.b.a.1
            @Override // android.location.LocationListener
            public void onLocationChanged(Location location) {
                a.this.e.removeUpdates(this);
                a.this.l.set(false);
                a.this.a(location);
            }

            @Override // android.location.LocationListener
            public void onProviderDisabled(String str) {
            }

            @Override // android.location.LocationListener
            public void onProviderEnabled(String str) {
            }

            @Override // android.location.LocationListener
            public void onStatusChanged(String str, int i, Bundle bundle) {
            }
        };
        this.l = new AtomicBoolean();
        this.e = (LocationManager) context.getSystemService(PlaceFields.LOCATION);
        this.g = cn.jpush.android.cache.a.c(context, 900000L);
        this.d = cn.jpush.android.cache.a.c(context, -1L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(Location location) {
        Logger.dd("CustomGeofenAction", "current location:" + location);
        Handler handler = this.f;
        if (handler != null && handler.hasMessages(1001)) {
            this.f.removeMessages(1001);
        }
        if (location == null) {
            return;
        }
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        Iterator<Map.Entry<String, b>> it = this.b.entrySet().iterator();
        boolean z = false;
        while (it.hasNext()) {
            b value = it.next().getValue();
            if (value.e * 1000 <= System.currentTimeMillis()) {
                Logger.dd("CustomGeofenAction", "Out of date geofence " + value.a);
                it.remove();
                b(value);
            } else {
                double d = latitude;
                double dA = f.a(longitude, latitude, value.f, value.g);
                Logger.dd("CustomGeofenAction", value.a + " distance to center:" + dA);
                String str = dA <= ((double) value.b) ? "in" : "out";
                if (Math.abs(dA - value.b) < 1000.0d) {
                    z = true;
                }
                Logger.d("CustomGeofenAction", "lastStatus:" + value.h + ",currentStatus:" + str);
                if (!str.equals(value.h)) {
                    if (value.h != null && str.equals(value.c) && f(value)) {
                        e(value);
                        d(value);
                        if (!value.d) {
                            Logger.dd("CustomGeofenAction", "No repeat geofence " + value.a);
                            this.b.remove(value.a);
                            c();
                            b(value);
                        }
                    }
                    value.h = str;
                }
                latitude = d;
            }
        }
        if (this.d == -1) {
            this.g = z ? 180000L : 900000L;
        }
        c();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(long j) {
        Logger.dd("CustomGeofenAction", "Scan geofence after " + j + "ms");
        Handler handler = this.f;
        if (handler == null) {
            return;
        }
        if (handler.hasMessages(1000)) {
            this.f.removeMessages(1000);
        }
        this.f.sendEmptyMessageDelayed(1000, j);
    }

    private void d() {
        try {
            HandlerThread handlerThread = new HandlerThread("CustomGeofenAction") { // from class: cn.jpush.android.b.a.2
                @Override // android.os.HandlerThread, java.lang.Thread, java.lang.Runnable
                public void run() {
                    try {
                        super.run();
                    } catch (RuntimeException e) {
                        Logger.e("CustomGeofenAction", "handler thread run e:" + e + "  t=" + Thread.currentThread().getName() + "_" + Thread.currentThread().getId());
                    }
                }
            };
            handlerThread.start();
            this.f = new Handler(handlerThread.getLooper()) { // from class: cn.jpush.android.b.a.3
                @Override // android.os.Handler
                public void handleMessage(Message message) {
                    String str;
                    switch (message.what) {
                        case 1000:
                            Logger.d("CustomGeofenAction", "period task tid " + Thread.currentThread().getId());
                            a aVar = a.this;
                            aVar.b(aVar.g);
                            break;
                        case 1001:
                            a.this.e();
                            try {
                                if (a.this.i == null || !a.this.i.equals("gps")) {
                                    str = "Network provider time out!";
                                } else {
                                    Logger.dd("CustomGeofenAction", "GPS provider time out!");
                                    a.this.i = "network";
                                    if (a.this.e.isProviderEnabled(a.this.i)) {
                                        a.this.e.requestLocationUpdates(a.this.i, 2000L, 0.0f, a.this.k);
                                        a.this.f.sendEmptyMessageDelayed(1001, a.this.h / 2);
                                        return;
                                    }
                                    str = "Network provider is disabled";
                                }
                                Logger.dd("CustomGeofenAction", str);
                            } catch (Throwable th) {
                                Logger.dd("CustomGeofenAction", "request location error#" + th);
                            }
                            a.this.l.set(false);
                            return;
                        case 1002:
                            break;
                        default:
                            return;
                    }
                    a.this.f();
                }
            };
        } catch (Throwable th) {
            Logger.ww("CustomGeofenAction", "init geofence handler failed:" + th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e() {
        String str;
        try {
            LocationListener locationListener = this.k;
            if (locationListener != null) {
                LocationManager locationManager = this.e;
                if (locationManager != null) {
                    locationManager.removeUpdates(locationListener);
                } else {
                    str = "locationManager is null , do nothing!";
                }
            }
            str = "Location listener is null , do nothing!";
            Logger.w("CustomGeofenAction", str);
        } catch (Throwable th) {
            Logger.w("CustomGeofenAction", "remove location listener failed  e:" + th.getMessage());
        }
    }

    private void e(b bVar) {
        long jCurrentTimeMillis = System.currentTimeMillis();
        bVar.o = jCurrentTimeMillis;
        Date date = new Date(jCurrentTimeMillis);
        String str = f.a("yyyy-DDD").format(date);
        if (str.equals(bVar.r)) {
            bVar.s++;
        } else {
            bVar.r = str;
            bVar.s = 1;
        }
        String str2 = f.a("yyyy-ww").format(date);
        if (str2.equals(bVar.p)) {
            bVar.q++;
        } else {
            bVar.p = str2;
            bVar.q = 1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f() {
        Logger.d("CustomGeofenAction", "try locate...");
        if (!cn.jpush.android.t.a.a(this.a, "android.permission.ACCESS_FINE_LOCATION")) {
            Logger.w("CustomGeofenAction", "Require the permissionandroid.permission.ACCESS_FINE_LOCATION");
            Logger.dd("CustomGeofenAction", "No location permission!");
            return;
        }
        Logger.d("CustomGeofenAction", "sdk int:" + Build.VERSION.SDK_INT + ",targetSdkVersion:" + this.a.getApplicationInfo().targetSdkVersion);
        if (JPushConstants.canGetLbsInBackGround(this.a)) {
            if (!h()) {
                Logger.dd("CustomGeofenAction", "No enabled provider");
                return;
            }
            if (this.b.size() <= 0) {
                a();
            } else if (this.l.get()) {
                Logger.d("CustomGeofenAction", "isLocating...");
            } else {
                g();
            }
        }
    }

    /* JADX WARN: Code duplicated, block: B:28:0x008c  */
    private boolean f(b bVar) {
        StringBuilder sbAppend;
        int i;
        StringBuilder sbAppend2;
        String string;
        if (bVar.o <= 0) {
            return true;
        }
        if (bVar.d) {
            long jCurrentTimeMillis = System.currentTimeMillis();
            if (bVar.n <= 0 || bVar.o <= 0 || jCurrentTimeMillis >= bVar.o + ((long) (bVar.n * 1000))) {
                if (bVar.m <= 0 || TextUtils.isEmpty(bVar.r)) {
                    if (bVar.l > 0 && !TextUtils.isEmpty(bVar.p)) {
                        if (bVar.p.equals(f.a("yyyy-ww").format(new Date(jCurrentTimeMillis))) && bVar.q >= bVar.l) {
                            sbAppend = new StringBuilder("this week already repeat enough:").append(bVar.q).append("/");
                            i = bVar.l;
                        }
                    }
                    return true;
                }
                if (!bVar.r.equals(f.a("yyyy-DDD").format(new Date(jCurrentTimeMillis))) || bVar.s < bVar.m) {
                    if (bVar.l > 0) {
                        if (bVar.p.equals(f.a("yyyy-ww").format(new Date(jCurrentTimeMillis)))) {
                            sbAppend = new StringBuilder("this week already repeat enough:").append(bVar.q).append("/");
                            i = bVar.l;
                        }
                    }
                    return true;
                }
                sbAppend = new StringBuilder("today already repeat enough:").append(bVar.s).append("/");
                i = bVar.m;
                sbAppend2 = sbAppend.append(i);
            } else {
                sbAppend2 = new StringBuilder("in ").append(bVar.n).append("s, can't repeat geofence");
            }
            string = sbAppend2.toString();
        } else {
            string = "can't repeat geofence";
        }
        Logger.d("CustomGeofenAction", string);
        return false;
    }

    /* JADX WARN: Code duplicated, block: B:23:0x0082  */
    private void g() {
        String str;
        try {
            LocationManager locationManager = this.e;
            if (locationManager == null) {
                Logger.ww("CustomGeofenAction", "locationManager is null");
                return;
            }
            Location lastKnownLocation = locationManager.getLastKnownLocation("gps");
            Location lastKnownLocation2 = this.e.getLastKnownLocation("network");
            Location lastKnownLocation3 = this.e.getLastKnownLocation("passive");
            Logger.v("CustomGeofenAction", "gpsLocation:" + lastKnownLocation);
            Logger.v("CustomGeofenAction", "netLocation:" + lastKnownLocation2);
            Logger.v("CustomGeofenAction", "passLocation:" + lastKnownLocation3);
            long time = lastKnownLocation == null ? 0L : lastKnownLocation.getTime();
            long time2 = lastKnownLocation2 == null ? 0L : lastKnownLocation2.getTime();
            long time3 = lastKnownLocation3 == null ? 0L : lastKnownLocation3.getTime();
            if (time > time2) {
                if (time <= time3) {
                    lastKnownLocation = lastKnownLocation3;
                }
            } else if (time2 > time3) {
                lastKnownLocation = lastKnownLocation2;
            } else {
                lastKnownLocation = lastKnownLocation3;
            }
            if (System.currentTimeMillis() - (lastKnownLocation != null ? lastKnownLocation.getTime() : 0L) < 30000) {
                Logger.dd("CustomGeofenAction", "need not restart gpslocation,the time with last:" + (System.currentTimeMillis() - (lastKnownLocation != null ? lastKnownLocation.getTime() : 0L)));
                a(lastKnownLocation);
                return;
            }
            this.i = null;
            if (this.e.isProviderEnabled("gps")) {
                this.i = "gps";
            } else if (this.e.isProviderEnabled("network")) {
                this.i = "network";
            }
            Logger.d("CustomGeofenAction", "provider " + this.i);
            if (TextUtils.isEmpty(this.i)) {
                Logger.dd("CustomGeofenAction", "not gps nor network provider,stop scan geofence");
                return;
            }
            this.l.set(true);
            this.e.requestLocationUpdates(this.i, 2000L, 0.0f, this.k);
            this.f.sendEmptyMessageDelayed(1001, this.h);
        } catch (SecurityException unused) {
            str = "No suitable permission when get last known location!";
            Logger.ww("CustomGeofenAction", str);
        } catch (Throwable th) {
            str = "The provider is illegal argument!" + th;
            Logger.ww("CustomGeofenAction", str);
        }
    }

    private boolean h() {
        String str;
        try {
            LocationManager locationManager = this.e;
            if (locationManager != null) {
                return locationManager.isProviderEnabled("gps") || this.e.isProviderEnabled("network") || this.e.isProviderEnabled("passive");
            }
            return false;
        } catch (IllegalArgumentException unused) {
            str = "The provider [gps] is illegal argument!";
            Logger.ww("CustomGeofenAction", str);
            return false;
        } catch (SecurityException unused2) {
            str = "No suitable permission is present when get GPS_PROVIDER!";
            Logger.ww("CustomGeofenAction", str);
            return false;
        } catch (Throwable th) {
            str = "Check GPS enable failed:" + th;
            Logger.ww("CustomGeofenAction", str);
            return false;
        }
    }

    @Override // cn.jpush.android.b.c
    protected synchronized void a() {
        Logger.dd("CustomGeofenAction", "geofence size:" + this.b.size());
        Logger.dd("CustomGeofenAction", "stop listen geofence");
        if (this.j) {
            Handler handler = this.f;
            if (handler != null) {
                handler.removeMessages(1000);
            }
            this.j = false;
        }
    }

    @Override // cn.jpush.android.b.c
    public void a(long j) {
        Logger.dd("CustomGeofenAction", "Set geofence interval " + j);
        this.g = j;
        this.d = j;
        cn.jpush.android.cache.a.b(this.a, j);
    }

    @Override // cn.jpush.android.b.c
    protected void a(b bVar) {
        Handler handler;
        Logger.dd("CustomGeofenAction", "Geofence create success, id=" + bVar.a);
        if (!this.j || (handler = this.f) == null) {
            return;
        }
        handler.sendEmptyMessage(1002);
    }

    @Override // cn.jpush.android.b.c
    protected void a(b bVar, b bVar2) {
        Handler handler;
        Logger.dd("CustomGeofenAction", "Geofence update success, id=" + bVar.a);
        if (!this.j || (handler = this.f) == null) {
            return;
        }
        handler.sendEmptyMessage(1002);
    }

    @Override // cn.jpush.android.b.c
    public synchronized void b() {
        Logger.dd("CustomGeofenAction", "start listen geofence");
        if (!cn.jpush.android.cache.a.k(this.a)) {
            Logger.ww("CustomGeofenAction", "lbs is disable!");
            return;
        }
        if (this.j) {
            Logger.ii("CustomGeofenAction", "geofence is running!");
            return;
        }
        if (this.b.size() == 0) {
            Logger.dd("CustomGeofenAction", "No geofence,not need listen");
            return;
        }
        if (this.f == null) {
            d();
        }
        b(0L);
        this.j = true;
    }

    @Override // cn.jpush.android.b.c
    protected void b(b bVar) {
        Logger.dd("CustomGeofenAction", "Geofence delete success, id=" + bVar.a);
    }
}
