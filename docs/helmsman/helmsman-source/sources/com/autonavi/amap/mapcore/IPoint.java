package com.autonavi.amap.mapcore;

import android.graphics.Point;
import com.autonavi.ae.gmap.maploader.Pools;

/* JADX INFO: loaded from: classes.dex */
public class IPoint extends Point implements Cloneable {
    private static final Pools.SynchronizedPool<IPoint> mPool = new Pools.SynchronizedPool<>(32);

    public static IPoint obtain() {
        IPoint iPointAcquire = mPool.acquire();
        if (iPointAcquire == null) {
            return new IPoint();
        }
        iPointAcquire.set(0, 0);
        return iPointAcquire;
    }

    public static IPoint obtain(int i, int i2) {
        IPoint iPointAcquire = mPool.acquire();
        if (iPointAcquire == null) {
            return new IPoint(i, i2);
        }
        iPointAcquire.set(i, i2);
        return iPointAcquire;
    }

    public void recycle() {
        mPool.release(this);
    }

    public IPoint() {
    }

    public IPoint(int i, int i2) {
        this.x = i;
        this.y = i2;
    }

    public Object clone() {
        try {
            return (IPoint) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
