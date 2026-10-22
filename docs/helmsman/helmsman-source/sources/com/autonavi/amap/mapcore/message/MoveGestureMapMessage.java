package com.autonavi.amap.mapcore.message;

import com.autonavi.ae.gmap.GLMapState;
import com.autonavi.ae.gmap.maploader.Pools;
import com.autonavi.amap.mapcore.FPoint;

/* JADX INFO: loaded from: classes.dex */
public class MoveGestureMapMessage extends GestureMapMessage {
    private static final Pools.SynchronizedPool<MoveGestureMapMessage> mPool = new Pools.SynchronizedPool<>(1024);
    static int newCount;
    public float touch_delta_x;
    public float touch_delta_y;

    @Override // com.autonavi.amap.mapcore.message.GestureMapMessage, com.autonavi.ae.gmap.MapMessage
    public int getType() {
        return 0;
    }

    public static synchronized MoveGestureMapMessage obtain(int i, float f, float f2) {
        MoveGestureMapMessage moveGestureMapMessageAcquire;
        moveGestureMapMessageAcquire = mPool.acquire();
        if (moveGestureMapMessageAcquire == null) {
            moveGestureMapMessageAcquire = new MoveGestureMapMessage(i, f, f2);
        } else {
            moveGestureMapMessageAcquire.reset();
            moveGestureMapMessageAcquire.setParams(i, f, f2);
        }
        return moveGestureMapMessageAcquire;
    }

    public void recycle() {
        mPool.release(this);
    }

    public static void destory() {
        mPool.destory();
    }

    private void setParams(int i, float f, float f2) {
        setState(i);
        this.touch_delta_x = f;
        this.touch_delta_y = f2;
    }

    public MoveGestureMapMessage(int i, float f, float f2) {
        super(i);
        this.touch_delta_x = f;
        this.touch_delta_y = f2;
        newCount++;
    }

    @Override // com.autonavi.amap.mapcore.message.GestureMapMessage
    public void runCameraUpdate(GLMapState gLMapState) {
        int i = (int) this.touch_delta_x;
        int i2 = (int) this.touch_delta_y;
        int i3 = (int) ((this.width >> 1) - i);
        int i4 = (int) ((this.height >> 1) - i2);
        FPoint fPointObtain = FPoint.obtain();
        gLMapState.win2Map(i3, i4, fPointObtain);
        gLMapState.setMapGlCenter(fPointObtain.x, fPointObtain.y);
        gLMapState.recalculate();
        fPointObtain.recycle();
    }
}
