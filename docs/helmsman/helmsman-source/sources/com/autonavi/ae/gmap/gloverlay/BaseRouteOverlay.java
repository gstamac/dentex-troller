package com.autonavi.ae.gmap.gloverlay;

import android.content.Context;
import android.graphics.Bitmap;
import com.autonavi.amap.mapcore.interfaces.IAMap;

/* JADX INFO: loaded from: classes.dex */
public class BaseRouteOverlay extends BaseMapOverlay<GLRouteOverlay, Object> {
    @Override // com.autonavi.ae.gmap.gloverlay.BaseMapOverlay
    public void addItem(Object obj) {
    }

    @Override // com.autonavi.ae.gmap.gloverlay.BaseMapOverlay
    public void resumeMarker(Bitmap bitmap) {
    }

    public BaseRouteOverlay(int i, Context context, IAMap iAMap) {
        super(i, context, iAMap);
    }

    @Override // com.autonavi.ae.gmap.gloverlay.BaseMapOverlay
    protected void iniGLOverlay() {
        this.mGLOverlay = new GLRouteOverlay(this.mEngineID, this.mMapView, hashCode());
    }

    public void addRouteItem(int i, GLRouteProperty[] gLRoutePropertyArr, boolean z, long j, int i2) {
        if (this.mGLOverlay == 0 || ((GLRouteOverlay) this.mGLOverlay).getNativeInstatnce() == 0) {
            return;
        }
        ((GLRouteOverlay) this.mGLOverlay).addRouteItem(i, gLRoutePropertyArr, z, j, i2, true);
    }

    public void removeRouteName() {
        if (this.mGLOverlay == 0 || ((GLRouteOverlay) this.mGLOverlay).getNativeInstatnce() == 0) {
            return;
        }
        ((GLRouteOverlay) this.mGLOverlay).removeRouteName(true);
    }

    public void remove() {
        setVisible(false);
        releaseInstance();
    }
}
