package com.autonavi.amap.mapcore.message;

import com.autonavi.ae.gmap.GLMapState;
import com.autonavi.ae.gmap.maploader.Pools;

/* JADX INFO: loaded from: classes.dex */
public class HoverGestureMapMessage extends GestureMapMessage {
    private static final Pools.SynchronizedPool<HoverGestureMapMessage> mPool = new Pools.SynchronizedPool<>(256);
    public float angle_delta;

    @Override // com.autonavi.amap.mapcore.message.GestureMapMessage, com.autonavi.ae.gmap.MapMessage
    public int getType() {
        return 3;
    }

    public static HoverGestureMapMessage obtain(int i, float f) {
        HoverGestureMapMessage hoverGestureMapMessageAcquire = mPool.acquire();
        if (hoverGestureMapMessageAcquire == null) {
            hoverGestureMapMessageAcquire = new HoverGestureMapMessage(i, f);
        } else {
            hoverGestureMapMessageAcquire.reset();
        }
        hoverGestureMapMessageAcquire.setParams(i, f);
        return hoverGestureMapMessageAcquire;
    }

    public void recycle() {
        mPool.release(this);
    }

    public static void destory() {
        mPool.destory();
    }

    private void setParams(int i, float f) {
        setState(i);
        this.angle_delta = f;
    }

    public HoverGestureMapMessage(int i, float f) {
        super(i);
        this.angle_delta = f;
    }

    /* JADX WARN: Code duplicated, block: B:4:0x000c A[PHI: r1
  0x000c: PHI (r1v8 float) = (r1v1 float), (r1v2 float) binds: [B:3:0x000a, B:6:0x0012] A[DONT_GENERATE, DONT_INLINE]] */
    @Override // com.autonavi.amap.mapcore.message.GestureMapMessage
    public void runCameraUpdate(GLMapState gLMapState) {
        float cameraHeaderAngle = gLMapState.getCameraHeaderAngle() + this.angle_delta;
        float f = 0.0f;
        if (cameraHeaderAngle < 0.0f) {
            cameraHeaderAngle = f;
        } else {
            f = 65.0f;
            if (cameraHeaderAngle > 65.0f) {
                cameraHeaderAngle = f;
            } else if (gLMapState.getCameraHeaderAngle() > 40.0f && cameraHeaderAngle > 40.0f && gLMapState.getCameraHeaderAngle() > cameraHeaderAngle) {
                cameraHeaderAngle = 40.0f;
            }
        }
        gLMapState.setCameraDegree(cameraHeaderAngle);
        gLMapState.recalculate();
    }
}
