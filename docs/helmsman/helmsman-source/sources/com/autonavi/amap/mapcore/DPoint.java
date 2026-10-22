package com.autonavi.amap.mapcore;

import com.autonavi.ae.gmap.maploader.Pools;

/* JADX INFO: loaded from: classes.dex */
public class DPoint {
    private static final Pools.SynchronizedPool<DPoint> mPool = new Pools.SynchronizedPool<>(32);
    public double x;
    public double y;

    public static DPoint obtain() {
        DPoint dPointAcquire = mPool.acquire();
        if (dPointAcquire == null) {
            return new DPoint();
        }
        dPointAcquire.set(0.0d, 0.0d);
        return dPointAcquire;
    }

    public static DPoint obtain(double d, double d2) {
        DPoint dPointAcquire = mPool.acquire();
        if (dPointAcquire == null) {
            return new DPoint(d, d2);
        }
        dPointAcquire.set(d, d2);
        return dPointAcquire;
    }

    public void recycle() {
        mPool.release(this);
    }

    public DPoint() {
    }

    public DPoint(double d, double d2) {
        this.x = d;
        this.y = d2;
    }

    private void set(double d, double d2) {
        this.x = d;
        this.y = d2;
    }
}
