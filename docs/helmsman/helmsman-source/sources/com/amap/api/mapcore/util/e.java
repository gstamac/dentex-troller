package com.amap.api.mapcore.util;

import android.location.Location;
import com.amap.api.maps.LocationSource;

/* JADX INFO: compiled from: AMapOnLocationChangedListener.java */
/* JADX INFO: loaded from: classes.dex */
class e implements LocationSource.OnLocationChangedListener {
    Location a;
    private k b;

    e(k kVar) {
        this.b = kVar;
    }

    @Override // com.amap.api.maps.LocationSource.OnLocationChangedListener
    public void onLocationChanged(Location location) {
        this.a = location;
        try {
            if (this.b.isMyLocationEnabled()) {
                this.b.a(location);
            }
        } catch (Throwable th) {
            fn.b(th, "AMapOnLocationChangedListener", "onLocationChanged");
            th.printStackTrace();
        }
    }
}
