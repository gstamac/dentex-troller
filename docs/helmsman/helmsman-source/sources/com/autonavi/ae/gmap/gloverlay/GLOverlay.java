package com.autonavi.ae.gmap.gloverlay;

import com.autonavi.ae.gmap.GLMapEngine;
import com.autonavi.amap.mapcore.interfaces.IAMap;

/* JADX INFO: loaded from: classes.dex */
public abstract class GLOverlay {
    protected int mCode;
    protected int mEngineID;
    protected IAMap mGLMapView;
    protected boolean isNightStyle = false;
    protected long mNativeInstance = 0;

    public enum EAMapOverlayTpye {
        AMAPOVERLAY_POINT,
        AMAPOVERLAY_POLYLINE,
        AMAPOVERLAY_POLYGON,
        AMAPOVERLAY_NAVI,
        AMAPOVERLAY_GPS,
        AMAPOVERLAY_ARC,
        AMAPOVERLAY_ARROW,
        AMAPOVERLAY_VECTOR,
        AMAPOVERLAY_MODEL,
        AMAPOVERLAY_RCTROUTE,
        AMAPOVERLAY_ROUTE
    }

    private static native int nativeGetCount(long j);

    private static native int nativeGetOverlayPriority(long j);

    private static native int nativeGetSubType(long j);

    private static native int nativeGetType(long j);

    private static native boolean nativeIsClickable(long j);

    private static native boolean nativeIsVisible(long j);

    private static native void nativeRemoveAll(long j);

    private static native void nativeRemoveItem(long j, int i);

    private static native void nativeSetClickable(long j, boolean z);

    private static native void nativeSetMaxDisplayLevel(long j, int i);

    private static native void nativeSetMinDisplayLevel(long j, int i);

    private static native void nativeSetOverlayItemPriority(long j, int i);

    private static native void nativeSetOverlayOnTop(long j, boolean z);

    private static native void nativeSetOverlayPriority(long j, int i);

    private static native void nativeSetShownMaxCount(long j, int i);

    protected static native void nativeSetVisible(long j, boolean z);

    public void clearFocus() {
    }

    public GLOverlay(int i, IAMap iAMap, int i2) {
        this.mEngineID = i;
        this.mGLMapView = iAMap;
        this.mCode = i2;
    }

    public long getNativeInstatnce() {
        return this.mNativeInstance;
    }

    public int getCode() {
        return this.mCode;
    }

    public int getType() {
        long j = this.mNativeInstance;
        if (j == 0) {
            return -1;
        }
        return nativeGetType(j);
    }

    public int getSubType() {
        long j = this.mNativeInstance;
        if (j == 0) {
            return -1;
        }
        return nativeGetSubType(j);
    }

    public void removeItem(int i) {
        long j = this.mNativeInstance;
        if (j == 0) {
            return;
        }
        nativeRemoveItem(j, i);
    }

    public void removeAll() {
        long j = this.mNativeInstance;
        if (j == 0) {
            return;
        }
        nativeRemoveAll(j);
        IAMap iAMap = this.mGLMapView;
        if (iAMap != null) {
            iAMap.setRunLowFrame(false);
        }
    }

    public int getSize() {
        long j = this.mNativeInstance;
        if (j == 0) {
            return 0;
        }
        return nativeGetCount(j);
    }

    public void setVisible(boolean z) {
        long j = this.mNativeInstance;
        if (j == 0) {
            return;
        }
        nativeSetVisible(j, z);
        this.mGLMapView.setRunLowFrame(false);
    }

    public boolean isVisible() {
        long j = this.mNativeInstance;
        if (j == 0) {
            return false;
        }
        return nativeIsVisible(j);
    }

    public void setClickable(boolean z) {
        long j = this.mNativeInstance;
        if (j == 0) {
            return;
        }
        nativeSetClickable(j, z);
    }

    public boolean isClickable() {
        long j = this.mNativeInstance;
        if (j == 0) {
            return false;
        }
        return nativeIsClickable(j);
    }

    public void setMaxCountShown(int i) {
        nativeSetShownMaxCount(this.mNativeInstance, i);
    }

    public void setOverlayOnTop(boolean z) {
        nativeSetOverlayOnTop(this.mNativeInstance, z);
    }

    public void setMinDisplayLevel(int i) {
        nativeSetMinDisplayLevel(this.mNativeInstance, i);
    }

    public void setMaxDisplayLevel(int i) {
        nativeSetMaxDisplayLevel(this.mNativeInstance, i);
    }

    public void setOverlayPriority(int i) {
        nativeSetOverlayPriority(this.mNativeInstance, i);
    }

    public int getOverlayPriority() {
        return nativeGetOverlayPriority(this.mNativeInstance);
    }

    public void setOverlayItemPriority(int i) {
        nativeSetOverlayItemPriority(this.mNativeInstance, i);
    }

    protected void finalize() throws Throwable {
        releaseInstance();
        super.finalize();
    }

    public void releaseInstance() {
        long j = this.mNativeInstance;
        if (j != 0) {
            GLMapEngine.destoryOverlay(this.mEngineID, j);
            this.mNativeInstance = 0L;
        }
    }

    public void useNightStyle(boolean z) {
        this.isNightStyle = z;
    }
}
