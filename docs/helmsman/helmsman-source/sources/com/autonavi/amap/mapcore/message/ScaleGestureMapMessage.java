package com.autonavi.amap.mapcore.message;

import com.autonavi.ae.gmap.GLMapState;
import com.autonavi.ae.gmap.maploader.Pools;
import com.autonavi.amap.mapcore.IPoint;

/* JADX INFO: loaded from: classes.dex */
public class ScaleGestureMapMessage extends GestureMapMessage {
    private static final Pools.SynchronizedPool<ScaleGestureMapMessage> mPool = new Pools.SynchronizedPool<>(256);
    public int pivot_x;
    public int pivot_y;
    public float scale_delta;

    @Override // com.autonavi.amap.mapcore.message.GestureMapMessage, com.autonavi.ae.gmap.MapMessage
    public int getType() {
        return 1;
    }

    public static ScaleGestureMapMessage obtain(int i, float f, int i2, int i3) {
        ScaleGestureMapMessage scaleGestureMapMessageAcquire = mPool.acquire();
        if (scaleGestureMapMessageAcquire == null) {
            return new ScaleGestureMapMessage(i, f, i2, i3);
        }
        scaleGestureMapMessageAcquire.reset();
        scaleGestureMapMessageAcquire.setParams(i, f, i2, i3);
        return scaleGestureMapMessageAcquire;
    }

    public void recycle() {
        mPool.release(this);
    }

    public static void destory() {
        mPool.destory();
    }

    private void setParams(int i, float f, int i2, int i3) {
        setState(i);
        this.scale_delta = f;
        this.pivot_x = i2;
        this.pivot_y = i3;
    }

    public ScaleGestureMapMessage(int i, float f, int i2, int i3) {
        super(i);
        this.scale_delta = 0.0f;
        this.pivot_x = 0;
        this.pivot_y = 0;
        setParams(i, f, i2, i3);
    }

    @Override // com.autonavi.amap.mapcore.message.GestureMapMessage
    public void runCameraUpdate(GLMapState gLMapState) {
        IPoint iPointObtain;
        IPoint iPointObtain2;
        if (this.isGestureScaleByMapCenter) {
            setMapZoomer(gLMapState);
            return;
        }
        int i = this.pivot_x;
        int i2 = this.pivot_y;
        if (i > 0 || i2 > 0) {
            iPointObtain = IPoint.obtain();
            iPointObtain2 = IPoint.obtain();
            win2geo(gLMapState, i, i2, iPointObtain);
            gLMapState.setMapGeoCenter(iPointObtain.x, iPointObtain.y);
        } else {
            iPointObtain = null;
            iPointObtain2 = null;
        }
        setMapZoomer(gLMapState);
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

    private void setMapZoomer(GLMapState gLMapState) {
        gLMapState.setMapZoomer(gLMapState.getMapZoomer() + this.scale_delta);
        gLMapState.recalculate();
    }
}
