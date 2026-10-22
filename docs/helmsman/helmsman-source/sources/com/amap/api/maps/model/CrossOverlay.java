package com.amap.api.maps.model;

import com.autonavi.ae.gmap.gloverlay.CrossVectorOverlay;
import com.autonavi.ae.gmap.gloverlay.GLCrossVector;

/* JADX INFO: loaded from: classes.dex */
public class CrossOverlay {
    CrossVectorOverlay a;
    CrossOverlayOptions b;

    public CrossOverlay(CrossOverlayOptions crossOverlayOptions, CrossVectorOverlay crossVectorOverlay) {
        this.b = crossOverlayOptions;
        this.a = crossVectorOverlay;
    }

    public void setData(byte[] bArr) {
        CrossVectorOverlay crossVectorOverlay;
        if (bArr == null || (crossVectorOverlay = this.a) == null) {
            return;
        }
        crossVectorOverlay.setData(bArr);
    }

    public void setAttribute(GLCrossVector.AVectorCrossAttr aVectorCrossAttr) {
        this.a.setAttribute(aVectorCrossAttr);
    }

    public void setVisible(boolean z) {
        CrossVectorOverlay crossVectorOverlay = this.a;
        if (crossVectorOverlay != null) {
            crossVectorOverlay.setVisible(z);
        }
    }

    public void remove() {
        CrossVectorOverlay crossVectorOverlay = this.a;
        if (crossVectorOverlay != null) {
            crossVectorOverlay.remove();
        }
    }
}
