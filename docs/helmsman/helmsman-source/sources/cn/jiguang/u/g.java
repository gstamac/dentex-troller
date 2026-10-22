package cn.jiguang.u;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/* JADX INFO: loaded from: classes.dex */
final class g implements LocationListener {
    final /* synthetic */ f a;

    g(f fVar) {
        this.a = fVar;
    }

    @Override // android.location.LocationListener
    public final void onLocationChanged(Location location) {
        try {
            cn.jiguang.w.a.a("JLocationGps", "onLocationChanged:" + location + ",provider:" + this.a.g);
            this.a.a(location);
            f.b(this.a);
        } catch (Throwable th) {
            cn.jiguang.w.a.d("JLocationGps", "onLocationChanged failed:" + th.getMessage());
        }
    }

    @Override // android.location.LocationListener
    public final void onProviderDisabled(String str) {
        cn.jiguang.w.a.a("JLocationGps", "onProviderDisabled:" + str);
        f.b(this.a);
    }

    @Override // android.location.LocationListener
    public final void onProviderEnabled(String str) {
        cn.jiguang.w.a.a("JLocationGps", "onProviderEnabled:" + str);
    }

    @Override // android.location.LocationListener
    public final void onStatusChanged(String str, int i, Bundle bundle) {
        cn.jiguang.w.a.a("JLocationGps", "onStatusChanged status:" + i);
        if (i == 0) {
            f.b(this.a);
        }
    }
}
