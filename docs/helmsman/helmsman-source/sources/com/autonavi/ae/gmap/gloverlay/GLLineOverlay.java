package com.autonavi.ae.gmap.gloverlay;

import com.autonavi.ae.gmap.GLMapEngine;
import com.autonavi.ae.gmap.glinterface.GLGeoPoint;
import com.autonavi.amap.mapcore.interfaces.IAMap;

/* JADX INFO: loaded from: classes.dex */
public class GLLineOverlay extends GLOverlay {
    private int mBgColor;
    private int mBgResID;
    private boolean mCanCovered;

    public static class TextureGenedInfo {
        public boolean m_genMimps = false;
        public boolean m_isRepeat = false;
    }

    private static native void nativeAddPolylineItem(long j, long j2);

    protected static native long nativeCreatePolyLineParams();

    protected static native void nativeDestoryPolyLineParams(long j);

    private static native void nativeSetPolyLineParamsBool(long j, boolean z, boolean z2, boolean z3, boolean z4);

    private static native void nativeSetPolyLineParamsCapTextureInfo(long j, float f, float f2, float f3, float f4);

    private static native void nativeSetPolyLineParamsPoints(long j, int[] iArr, int[] iArr2, int[] iArr3, int[] iArr4);

    private static native void nativeSetPolyLineParamsPointsWithLonLat(long j, int[] iArr);

    private static native void nativeSetPolyLineParamsTextureInfo(long j, float f, float f2, float f3, float f4, float f5, float f6);

    private static native void nativeSetPolyLineParamsWAC(long j, int i, int i2, int i3, int i4, int i5);

    public GLLineOverlay(int i, IAMap iAMap, int i2, boolean z) {
        super(i, iAMap, i2);
        this.mBgResID = -1;
        this.mBgColor = 0;
        this.mCanCovered = true;
    }

    public GLLineOverlay(int i, IAMap iAMap, int i2) {
        super(i, iAMap, i2);
        this.mBgResID = -1;
        this.mBgColor = 0;
        this.mCanCovered = true;
        this.mNativeInstance = iAMap.createGLOverlay(GLOverlay.EAMapOverlayTpye.AMAPOVERLAY_POLYLINE.ordinal());
    }

    private void addLineItem(GLGeoPoint[] gLGeoPointArr, int i, int i2, int i3, int i4, int i5, boolean z) {
        addLineItem(gLGeoPointArr, i, i2, i3, i4, i5, z, this.mCanCovered);
    }

    private void doAddLineItemWithLonLat(int[] iArr, int i, int i2, int i3, int i4, int i5, boolean z, boolean z2, boolean z3) {
        long jNativeCreatePolyLineParams = nativeCreatePolyLineParams();
        nativeSetPolyLineParamsPointsWithLonLat(jNativeCreatePolyLineParams, iArr);
        setLineColorWithParams(jNativeCreatePolyLineParams, i, i2, i3, i4, i5, z, z2);
        nativeAddPolylineItem(this.mNativeInstance, jNativeCreatePolyLineParams);
        nativeDestoryPolyLineParams(jNativeCreatePolyLineParams);
        if (true == z3) {
            this.mGLMapView.requestRender();
        }
    }

    public void addLineItemWithLonLat(int[] iArr, int i, int i2, int i3, int i4, int i5, boolean z, boolean z2) {
        doAddLineItemWithLonLat(iArr, i, i2, i3, i4, i5, z, z2, true);
    }

    public void addLineItemWithLonLat(int[] iArr, int i, int i2, int i3, int i4, int i5, boolean z, boolean z2, boolean z3) {
        doAddLineItemWithLonLat(iArr, i, i2, i3, i4, i5, z, z2, z3);
    }

    private void doAddLineItemWithP20(int[] iArr, int[] iArr2, int i, int i2, int i3, int i4, int i5, boolean z, boolean z2, boolean z3) {
        int length;
        if (this.mNativeInstance != 0 && (length = iArr.length) >= 2 && length == iArr2.length) {
            long jNativeCreatePolyLineParams = nativeCreatePolyLineParams();
            nativeSetPolyLineParamsPoints(jNativeCreatePolyLineParams, iArr, iArr2, null, null);
            setLineColorWithParams(jNativeCreatePolyLineParams, i, i2, i3, i4, i5, z, z2);
            nativeAddPolylineItem(this.mNativeInstance, jNativeCreatePolyLineParams);
            nativeDestoryPolyLineParams(jNativeCreatePolyLineParams);
            if (true == z3) {
                this.mGLMapView.requestRender();
            }
        }
    }

    public void addLineItemWithP20(int[] iArr, int[] iArr2, int i, int i2, int i3, int i4, int i5, boolean z, boolean z2) {
        doAddLineItemWithP20(iArr, iArr2, i, i2, i3, i4, i5, z, z2, true);
    }

    public void addLineItemWithP20(int[] iArr, int[] iArr2, int i, int i2, int i3, int i4, int i5, boolean z, boolean z2, boolean z3) {
        doAddLineItemWithP20(iArr, iArr2, i, i2, i3, i4, i5, z, z2, z3);
    }

    private void doAddLineItem(GLGeoPoint[] gLGeoPointArr, int i, int i2, int i3, int i4, int i5, boolean z, boolean z2, boolean z3) {
        int length;
        if (this.mNativeInstance != 0 && (length = gLGeoPointArr.length) >= 2) {
            int[] iArr = new int[length];
            int[] iArr2 = new int[length];
            for (int i6 = 0; i6 < length; i6++) {
                GLGeoPoint gLGeoPoint = gLGeoPointArr[i6];
                if (gLGeoPoint != null) {
                    iArr[i6] = gLGeoPoint.x;
                    iArr2[i6] = gLGeoPointArr[i6].y;
                }
            }
            long jNativeCreatePolyLineParams = nativeCreatePolyLineParams();
            nativeSetPolyLineParamsPoints(jNativeCreatePolyLineParams, iArr, iArr2, null, null);
            setLineColorWithParams(jNativeCreatePolyLineParams, i, i2, i3, i4, i5, z, z2);
            nativeAddPolylineItem(this.mNativeInstance, jNativeCreatePolyLineParams);
            nativeDestoryPolyLineParams(jNativeCreatePolyLineParams);
            if (true == z3) {
                this.mGLMapView.requestRender();
            }
        }
    }

    public void addLineItem(GLGeoPoint[] gLGeoPointArr, int i, int i2, int i3, int i4, int i5, boolean z, boolean z2) {
        doAddLineItem(gLGeoPointArr, i, i2, i3, i4, i5, z, z2, true);
    }

    public void addLineItem(GLGeoPoint[] gLGeoPointArr, int i, int i2, int i3, int i4, int i5, boolean z, boolean z2, boolean z3) {
        doAddLineItem(gLGeoPointArr, i, i2, i3, i4, i5, z, z2, z3);
    }

    protected void setLineColorWithParams(long j, int i, int i2, int i3, int i4, int i5, boolean z, boolean z2) {
        float f = i2 * 4;
        if (i3 == 3000 || i3 == 3050) {
            nativeSetPolyLineParamsTextureInfo(j, 0.05f, 0.5f, 0.95f, 0.5f, 0.0f, 32.0f);
            nativeSetPolyLineParamsCapTextureInfo(j, 0.05f, 0.5f, 0.95f, 0.75f);
            nativeSetPolyLineParamsWAC(j, (int) f, i, i5, i3, i4);
            nativeSetPolyLineParamsBool(j, z, true, true, z2);
            return;
        }
        if (i3 < 3000) {
            nativeSetPolyLineParamsTextureInfo(j, 0.0f, 0.5f, 1.0f, 0.5f, 0.0f, 32.0f);
            nativeSetPolyLineParamsCapTextureInfo(j, 0.0f, 0.5f, 1.0f, 0.75f);
            nativeSetPolyLineParamsWAC(j, (int) f, i, i5, i3, i4);
            nativeSetPolyLineParamsBool(j, z, false, true, z2);
            return;
        }
        if (i3 < 3003) {
            nativeSetPolyLineParamsTextureInfo(j, 0.0f, 1.0f, 0.5f, 0.0f, 0.0f, 64.0f);
            nativeSetPolyLineParamsCapTextureInfo(j, 0.5f, 0.25f, 1.0f, 0.6f);
            nativeSetPolyLineParamsWAC(j, (int) f, i, i5, i3, i4);
            nativeSetPolyLineParamsBool(j, z, false, true, z2);
            return;
        }
        if (i3 == 3003) {
            nativeSetPolyLineParamsTextureInfo(j, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 64.0f);
            nativeSetPolyLineParamsWAC(j, (int) f, i, i5, i3, i4);
            nativeSetPolyLineParamsBool(j, z, false, false, z2);
        } else if (3010 > i3 && i3 > 3003) {
            nativeSetPolyLineParamsTextureInfo(j, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 64.0f);
            nativeSetPolyLineParamsWAC(j, (int) f, i, i5, i3, i4);
            nativeSetPolyLineParamsBool(j, z, false, false, z2);
        } else if (i3 >= 3010) {
            nativeSetPolyLineParamsTextureInfo(j, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 32.0f);
            nativeSetPolyLineParamsWAC(j, (int) f, i, i5, i3, i4);
            nativeSetPolyLineParamsBool(j, z, true, false, z2);
        }
    }

    public void addLineItem(GLLineProperty gLLineProperty) {
        int length;
        int[] iArr;
        int[] iArr2;
        int length2;
        if (this.mNativeInstance == 0 || gLLineProperty == null || gLLineProperty.mGeoPoints == null || (length = gLLineProperty.mGeoPoints.length) < 2) {
            return;
        }
        int[] iArr3 = new int[length];
        int[] iArr4 = new int[length];
        for (int i = 0; i < length; i++) {
            if (gLLineProperty.mGeoPoints[i] != null) {
                iArr3[i] = gLLineProperty.mGeoPoints[i].x;
                iArr4[i] = gLLineProperty.mGeoPoints[i].y;
            }
        }
        if (gLLineProperty.mPassPoints == null || (length2 = gLLineProperty.mPassPoints.length) <= 0) {
            iArr = null;
            iArr2 = null;
        } else {
            int[] iArr5 = new int[length2];
            int[] iArr6 = new int[length2];
            for (int i2 = 0; i2 < length2; i2++) {
                iArr5[i2] = gLLineProperty.mPassPoints[i2].x;
                iArr6[i2] = gLLineProperty.mPassPoints[i2].y;
            }
            iArr = iArr5;
            iArr2 = iArr6;
        }
        long jNativeCreatePolyLineParams = nativeCreatePolyLineParams();
        nativeSetPolyLineParamsPoints(jNativeCreatePolyLineParams, iArr3, iArr4, iArr, iArr2);
        setLineColorWithParams(jNativeCreatePolyLineParams, gLLineProperty);
        nativeAddPolylineItem(this.mNativeInstance, jNativeCreatePolyLineParams);
        nativeDestoryPolyLineParams(jNativeCreatePolyLineParams);
        if (gLLineProperty.isRefreshMap) {
            this.mGLMapView.requestRender();
        }
    }

    protected void setLineColorWithParams(long j, GLLineProperty gLLineProperty) {
        if (gLLineProperty != null) {
            nativeSetPolyLineParamsTextureInfo(j, gLLineProperty.mX1, gLLineProperty.mY1, gLLineProperty.mX2, gLLineProperty.mY2, gLLineProperty.mGLStart, gLLineProperty.mTextureLen);
            if (gLLineProperty.isUseCap) {
                nativeSetPolyLineParamsCapTextureInfo(j, gLLineProperty.mCapX1, gLLineProperty.mCapY1, gLLineProperty.mCapX2, gLLineProperty.mCapY2);
            }
            nativeSetPolyLineParamsWAC(j, gLLineProperty.mLineWidth * 4, gLLineProperty.mFilledColor, gLLineProperty.mBgColor, gLLineProperty.mFilledResId, gLLineProperty.mBgResId);
            nativeSetPolyLineParamsBool(j, gLLineProperty.isLineExtract, gLLineProperty.isUseColor, gLLineProperty.isUseCap, gLLineProperty.isCanCovered);
        }
    }

    @Override // com.autonavi.ae.gmap.gloverlay.GLOverlay
    protected void finalize() throws Throwable {
        if (this.mNativeInstance != 0) {
            GLMapEngine.destoryOverlay(this.mEngineID, this.mNativeInstance);
        }
        super.finalize();
    }

    public static TextureGenedInfo CheckRepeat(int i) {
        TextureGenedInfo textureGenedInfo = new TextureGenedInfo();
        if (i == 3000) {
            textureGenedInfo.m_genMimps = true;
            textureGenedInfo.m_isRepeat = false;
        } else if (i < 3000) {
            textureGenedInfo.m_genMimps = false;
            textureGenedInfo.m_isRepeat = false;
        } else if (i < 3003 || i == 3003) {
            textureGenedInfo.m_genMimps = false;
            textureGenedInfo.m_isRepeat = true;
        } else if (3010 > i && i > 3003) {
            textureGenedInfo.m_genMimps = false;
            textureGenedInfo.m_isRepeat = true;
        } else if (i >= 3010 && i < 10000) {
            textureGenedInfo.m_genMimps = false;
            textureGenedInfo.m_isRepeat = true;
        } else if (i >= 10000) {
            textureGenedInfo.m_genMimps = false;
            textureGenedInfo.m_isRepeat = false;
        }
        return textureGenedInfo;
    }
}
