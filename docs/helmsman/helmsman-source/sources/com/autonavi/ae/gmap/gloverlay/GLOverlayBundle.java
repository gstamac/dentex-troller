package com.autonavi.ae.gmap.gloverlay;

import android.util.SparseArray;
import com.autonavi.ae.gmap.gloverlay.BaseMapOverlay;
import com.autonavi.amap.mapcore.interfaces.IAMap;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class GLOverlayBundle<E extends BaseMapOverlay<?, ?>> {
    private int mEngineID;
    IAMap mGLMapView;
    private long mNativeInstance;
    private final List<E> mOverlayList = new ArrayList();
    private SparseArray<GLOverlayTexture> mTextureCaches = new SparseArray<>();

    public static class GLAmapFocusHits {
        public long mOverlayHashCode = 0;
        public long mHitedIndex = 0;
        public long mHitedTimes = 1000;
    }

    private static native void nativeAddGLOverlay(long j, long j2, long j3);

    private static native void nativeClearAllGLOverlay(long j, boolean z);

    private static native boolean nativeOnSingleTapLineOverlay(long j, int i, int i2, long[] jArr);

    private static native boolean nativeOnSingleTapPointOverlay(long j, int i, int i2, long[] jArr);

    private static native void nativeRemoveGLOverlay(long j, long j2);

    public boolean hasOverlayTextureItem(int i, int i2) {
        return false;
    }

    public static void IntClr2PVRClr(float[] fArr, int i) {
        fArr[2] = (i & 255) / 255.0f;
        fArr[1] = ((i >> 8) & 255) / 255.0f;
        fArr[0] = ((i >> 16) & 255) / 255.0f;
        fArr[3] = ((i >> 24) & 255) / 255.0f;
    }

    public GLOverlayBundle(int i, IAMap iAMap) {
        this.mGLMapView = null;
        this.mNativeInstance = 0L;
        this.mEngineID = i;
        this.mGLMapView = iAMap;
        this.mNativeInstance = iAMap.getGlOverlayMgrPtr();
    }

    public int getOverlayCount() {
        int size;
        synchronized (this.mOverlayList) {
            size = this.mOverlayList.size();
        }
        return size;
    }

    public boolean cotainsOverlay(E e) {
        boolean zContains;
        if (e == null) {
            return false;
        }
        synchronized (this.mOverlayList) {
            zContains = this.mOverlayList.contains(e);
        }
        return zContains;
    }

    public E getOverlay(int i) {
        synchronized (this.mOverlayList) {
            if (i >= 0) {
                if (i <= this.mOverlayList.size() - 1) {
                    return this.mOverlayList.get(i);
                }
            }
            return null;
        }
    }

    public void addOverlay(E e) {
        if (e == null) {
            return;
        }
        nativeAddGLOverlay(this.mNativeInstance, e.getGLOverlay().getNativeInstatnce(), e.getGLOverlay().getCode());
        synchronized (this.mOverlayList) {
            this.mOverlayList.add(e);
        }
    }

    public void removeOverlay(E e) {
        if (e == null) {
            return;
        }
        nativeRemoveGLOverlay(this.mNativeInstance, e.getGLOverlay().getNativeInstatnce());
        synchronized (this.mOverlayList) {
            this.mOverlayList.remove(e);
        }
    }

    public void removeAll(boolean z) {
        nativeClearAllGLOverlay(this.mNativeInstance, z);
        synchronized (this.mOverlayList) {
            this.mOverlayList.clear();
        }
    }

    public void clearFocus() {
        List<E> list = this.mOverlayList;
        if (list != null) {
            synchronized (list) {
                for (int i = 0; i < this.mOverlayList.size(); i++) {
                    E e = this.mOverlayList.get(i);
                    if (e != null) {
                        e.clearFocus();
                    }
                }
            }
        }
    }

    public GLOverlayTexture getOverlayTextureItem(int i, int i2) {
        GLOverlayTexture gLOverlayTexture;
        synchronized (this.mTextureCaches) {
            gLOverlayTexture = this.mTextureCaches.get(i2);
        }
        return gLOverlayTexture;
    }

    public boolean addOverlayTextureItem(int i, int i2, int i3, int i4) {
        GLOverlayTexture gLOverlayTexture = new GLOverlayTexture(i, i2, i3, i4);
        synchronized (this.mTextureCaches) {
            this.mTextureCaches.put(i, gLOverlayTexture);
        }
        return true;
    }

    public boolean addOverlayTextureItem(int i, int i2, float f, float f2, int i3, int i4) {
        GLOverlayTexture gLOverlayTexture = new GLOverlayTexture(i, i2, f, f2, i3, i4);
        synchronized (this.mTextureCaches) {
            this.mTextureCaches.put(i, gLOverlayTexture);
        }
        return true;
    }

    public void clearOverlayTexture() {
        synchronized (this.mTextureCaches) {
            this.mTextureCaches.clear();
        }
    }
}
