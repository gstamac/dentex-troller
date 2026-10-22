package com.autonavi.amap.mapcore;

import android.graphics.PointF;
import com.autonavi.ae.gmap.maploader.Pools;

/* JADX INFO: loaded from: classes.dex */
public class FPoint extends PointF {
    private static final Pools.SynchronizedPool<FPoint> mPool = new Pools.SynchronizedPool<>(32);

    public static FPoint obtain() {
        FPoint fPointAcquire = mPool.acquire();
        if (fPointAcquire == null) {
            return new FPoint();
        }
        fPointAcquire.set(0.0f, 0.0f);
        return fPointAcquire;
    }

    public static FPoint obtain(float f, float f2) {
        FPoint fPointAcquire = mPool.acquire();
        if (fPointAcquire == null) {
            return new FPoint(f, f2);
        }
        fPointAcquire.set(f, f2);
        return fPointAcquire;
    }

    public void recycle() {
        mPool.release(this);
    }

    public FPoint() {
    }

    public FPoint(float f, float f2) {
        this.x = f;
        this.y = f2;
    }

    @Override // android.graphics.PointF
    public boolean equals(Object obj) {
        FPoint fPoint = (FPoint) obj;
        return fPoint != null && this.x == fPoint.x && this.y == fPoint.y;
    }

    @Override // android.graphics.PointF
    public int hashCode() {
        Float.floatToIntBits(this.x);
        return Float.floatToIntBits(this.y) * 37;
    }
}
