package com.autonavi.amap.mapcore.message;

import com.autonavi.ae.gmap.GLMapState;
import com.autonavi.ae.gmap.MapMessage;
import com.autonavi.amap.mapcore.FPoint;
import com.autonavi.amap.mapcore.IPoint;

/* JADX INFO: loaded from: classes.dex */
public abstract class GestureMapMessage extends MapMessage {
    public int anchorX;
    public int anchorY;
    private int state_;
    public int width = 0;
    public int height = 0;
    public boolean isGestureScaleByMapCenter = false;
    public boolean isUseAnchor = false;

    @Override // com.autonavi.ae.gmap.MapMessage
    public abstract int getType();

    public abstract void runCameraUpdate(GLMapState gLMapState);

    public GestureMapMessage(int i) {
        this.state_ = i;
    }

    public void setState(int i) {
        this.state_ = i;
    }

    public void reset() {
        this.width = 0;
        this.height = 0;
        this.state_ = 0;
        this.isGestureScaleByMapCenter = false;
        this.isUseAnchor = false;
        this.anchorX = 0;
        this.anchorY = 0;
    }

    protected void win2geo(GLMapState gLMapState, int i, int i2, IPoint iPoint) {
        FPoint fPointObtain = FPoint.obtain();
        gLMapState.win2Map(i, i2, fPointObtain);
        gLMapState.map2Geo(fPointObtain.x, fPointObtain.y, iPoint);
        fPointObtain.recycle();
    }

    public int getMapGestureState() {
        return this.state_;
    }
}
