package com.autonavi.amap.mapcore.message;

import com.autonavi.ae.gmap.GLMapState;
import com.autonavi.ae.gmap.maploader.Pools;
import com.autonavi.amap.mapcore.IPoint;

/* JADX INFO: loaded from: classes.dex */
public class RotateGestureMapMessage extends GestureMapMessage {
    private static final Pools.SynchronizedPool<RotateGestureMapMessage> mPool = new Pools.SynchronizedPool<>(256);
    public float angle_delta;
    public int pivot_x;
    public int pivot_y;

    @Override // com.autonavi.amap.mapcore.message.GestureMapMessage, com.autonavi.ae.gmap.MapMessage
    public int getType() {
        return 2;
    }

    public static RotateGestureMapMessage obtain(int i, float f, int i2, int i3) {
        RotateGestureMapMessage rotateGestureMapMessageAcquire = mPool.acquire();
        if (rotateGestureMapMessageAcquire == null) {
            return new RotateGestureMapMessage(i, f, i2, i3);
        }
        rotateGestureMapMessageAcquire.reset();
        rotateGestureMapMessageAcquire.setParams(i, f, i2, i3);
        return rotateGestureMapMessageAcquire;
    }

    public void recycle() {
        mPool.release(this);
    }

    public static void destory() {
        mPool.destory();
    }

    private void setParams(int i, float f, int i2, int i3) {
        setState(i);
        this.angle_delta = f;
        this.pivot_x = i2;
        this.pivot_y = i3;
    }

    public RotateGestureMapMessage(int i, float f, int i2, int i3) {
        super(i);
        this.pivot_x = 0;
        this.pivot_y = 0;
        this.angle_delta = 0.0f;
        setParams(i, f, i2, i3);
        this.angle_delta = f;
        this.pivot_x = i2;
        this.pivot_y = i3;
    }

    @Override // com.autonavi.amap.mapcore.message.GestureMapMessage
    public void runCameraUpdate(GLMapState gLMapState) {
        IPoint iPointObtain;
        IPoint iPointObtain2;
        float mapAngle = gLMapState.getMapAngle() + this.angle_delta;
        if (this.isGestureScaleByMapCenter) {
            gLMapState.setMapAngle(mapAngle);
            gLMapState.recalculate();
            return;
        }
        int i = this.pivot_x;
        int i2 = this.pivot_y;
        if (this.isUseAnchor) {
            i = this.anchorX;
            i2 = this.anchorY;
        }
        if (i > 0 || i2 > 0) {
            iPointObtain = IPoint.obtain();
            iPointObtain2 = IPoint.obtain();
            win2geo(gLMapState, i, i2, iPointObtain);
            gLMapState.setMapGeoCenter(iPointObtain.x, iPointObtain.y);
        } else {
            iPointObtain = null;
            iPointObtain2 = null;
        }
        gLMapState.setMapAngle(mapAngle);
        gLMapState.recalculate();
        if (i > 0 || i2 > 0) {
            win2geo(gLMapState, i, i2, iPointObtain2);
            gLMapState.setMapGeoCenter((iPointObtain.x * 2) - iPointObtain2.x, (iPointObtain.y * 2) - iPointObtain2.y);
            gLMapState.recalculate();
        }
        if (iPointObtain != null) {
            iPointObtain.recycle();
        }
        if (iPointObtain2 != null) {
            iPointObtain2.recycle();
        }
    }
}
