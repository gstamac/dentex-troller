package com.autonavi.ae.gmap.gloverlay;

import android.graphics.Rect;
import com.autonavi.amap.mapcore.interfaces.IAMap;

/* JADX INFO: loaded from: classes.dex */
public class GLCrossVector extends GLOverlay {

    public static class AVectorCrossAttr {
        public boolean dayMode = true;
        public int fArrowBorderWidth;
        public int fArrowLineWidth;
        public int fDashLineWidth;
        public int fImportBorderWidth;
        public int fImportLineWidth;
        public int fUnImportBorderWidth;
        public int fUnImportLineWidth;
        public int nAngle;
        public int nCenterX;
        public int nCenterY;
        public int stAreaColor;
        public Rect stAreaRect;
        public int stArrowBorderColor;
        public int stArrowLineColor;
        public int stDashLineColor;
        public int stImportBorderColor;
        public int stImportLineColor;
        public Rect stRectMax;
        public Rect stRectMin;
        public int stUnImportBorderColor;
        public int stUnImportLineColor;
    }

    public static class AVectorCrossLink {
        public int[] xs;
        public int[] ys;
        public int[] zs;
        public AVectorCrossLinkAttr stAttr = new AVectorCrossLinkAttr();
        public int nCount = 0;
    }

    public static class AVectorCrossLinkAttr {
        public int nRoadClass = 0;
        public int nFormway = 0;
        public int IsRoute = 0;
        public int IsExitLink = 0;
    }

    private static native void nativeAddVectorCar(long j, int i, int i2, int i3);

    private static native int nativeAddVectorData(long j, int[] iArr, byte[] bArr);

    private static native void nativeAddVectorItem(long j, int[] iArr, int[] iArr2);

    private static native void nativeAddVectorRemainDis(long j, int i);

    private static native void nativeSetArrowResId(long j, boolean z, int i);

    private static native void nativeSetCarResId(long j, int i);

    private static native void nativeSetRoadResId(long j, boolean z, int i);

    private static native void nativeSetSkyResId(long j, boolean z, int i);

    public GLCrossVector(int i, IAMap iAMap, int i2) {
        super(i, iAMap, i2);
        this.mNativeInstance = iAMap.createGLOverlay(GLOverlay.EAMapOverlayTpye.AMAPOVERLAY_VECTOR.ordinal());
    }

    private int copy2Inter(int[] iArr, int i, AVectorCrossLink aVectorCrossLink) {
        int i2 = aVectorCrossLink.nCount;
        System.arraycopy(aVectorCrossLink.xs, 0, iArr, i, i2);
        int i3 = i + i2;
        System.arraycopy(aVectorCrossLink.ys, 0, iArr, i3, i2);
        int i4 = i3 + i2;
        System.arraycopy(aVectorCrossLink.zs, 0, iArr, i4, i2);
        return i4 + i2;
    }

    public void addVectorItem(AVectorCrossAttr aVectorCrossAttr, AVectorCrossLink[] aVectorCrossLinkArr, int i) {
        if (aVectorCrossAttr == null || aVectorCrossLinkArr == null || i == 0) {
            return;
        }
        int iCopy2Inter = 1;
        int[] iArr = {aVectorCrossAttr.nCenterX, aVectorCrossAttr.nCenterY, aVectorCrossAttr.stRectMax.left, aVectorCrossAttr.stRectMax.top, aVectorCrossAttr.stRectMax.right, aVectorCrossAttr.stRectMax.bottom, aVectorCrossAttr.stRectMin.left, aVectorCrossAttr.stRectMin.top, aVectorCrossAttr.stRectMin.right, aVectorCrossAttr.stRectMin.bottom, aVectorCrossAttr.nAngle, aVectorCrossAttr.stAreaRect.left, aVectorCrossAttr.stAreaRect.top, aVectorCrossAttr.stAreaRect.right, aVectorCrossAttr.stAreaRect.bottom, aVectorCrossAttr.stAreaColor, aVectorCrossAttr.fImportBorderWidth, aVectorCrossAttr.stImportBorderColor, aVectorCrossAttr.fUnImportBorderWidth, aVectorCrossAttr.stUnImportBorderColor, aVectorCrossAttr.fArrowBorderWidth, aVectorCrossAttr.stArrowBorderColor, aVectorCrossAttr.fImportLineWidth, aVectorCrossAttr.stImportLineColor, aVectorCrossAttr.fUnImportLineWidth, aVectorCrossAttr.stUnImportLineColor, aVectorCrossAttr.fDashLineWidth, aVectorCrossAttr.stDashLineColor, aVectorCrossAttr.fArrowLineWidth, aVectorCrossAttr.stArrowLineColor, aVectorCrossAttr.dayMode ? 1 : 0};
        int i2 = 1;
        for (int i3 = 0; i3 < i; i3++) {
            i2 += (aVectorCrossLinkArr[i3].nCount * 3) + 5;
        }
        int[] iArr2 = new int[i2];
        iArr2[0] = i;
        for (int i4 = 0; i4 < i; i4++) {
            AVectorCrossLink aVectorCrossLink = aVectorCrossLinkArr[i4];
            int i5 = iCopy2Inter + 1;
            iArr2[iCopy2Inter] = aVectorCrossLink.stAttr.nRoadClass;
            int i6 = i5 + 1;
            iArr2[i5] = aVectorCrossLink.stAttr.nFormway;
            int i7 = i6 + 1;
            iArr2[i6] = aVectorCrossLink.stAttr.IsRoute;
            int i8 = i7 + 1;
            iArr2[i7] = aVectorCrossLink.stAttr.IsExitLink;
            int i9 = i8 + 1;
            iArr2[i8] = aVectorCrossLink.nCount;
            iCopy2Inter = i9 + copy2Inter(iArr2, i9, aVectorCrossLink);
        }
        nativeAddVectorItem(this.mNativeInstance, iArr, iArr2);
    }

    public boolean addVectorItem(AVectorCrossAttr aVectorCrossAttr, byte[] bArr, int i) {
        return (aVectorCrossAttr == null || bArr == null || i == 0 || nativeAddVectorData(this.mNativeInstance, new int[]{aVectorCrossAttr.nCenterX, aVectorCrossAttr.nCenterY, aVectorCrossAttr.stRectMax.left, aVectorCrossAttr.stRectMax.top, aVectorCrossAttr.stRectMax.right, aVectorCrossAttr.stRectMax.bottom, aVectorCrossAttr.stRectMin.left, aVectorCrossAttr.stRectMin.top, aVectorCrossAttr.stRectMin.right, aVectorCrossAttr.stRectMin.bottom, aVectorCrossAttr.nAngle, aVectorCrossAttr.stAreaRect.left, aVectorCrossAttr.stAreaRect.top, aVectorCrossAttr.stAreaRect.right, aVectorCrossAttr.stAreaRect.bottom, aVectorCrossAttr.stAreaColor, aVectorCrossAttr.fImportBorderWidth, aVectorCrossAttr.stImportBorderColor, aVectorCrossAttr.fUnImportBorderWidth, aVectorCrossAttr.stUnImportBorderColor, aVectorCrossAttr.fArrowBorderWidth, aVectorCrossAttr.stArrowBorderColor, aVectorCrossAttr.fImportLineWidth, aVectorCrossAttr.stImportLineColor, aVectorCrossAttr.fUnImportLineWidth, aVectorCrossAttr.stUnImportLineColor, aVectorCrossAttr.fDashLineWidth, aVectorCrossAttr.stDashLineColor, aVectorCrossAttr.fArrowLineWidth, aVectorCrossAttr.stArrowLineColor, aVectorCrossAttr.dayMode ? 1 : 0}, bArr) != 0) ? false : true;
    }

    public void addVectorRemainDis(int i) {
        nativeAddVectorRemainDis(this.mNativeInstance, i);
    }

    public void addVectorCar(int i, int i2, int i3) {
        nativeAddVectorCar(this.mNativeInstance, i, i2, i3);
    }

    public void setRoadResId(boolean z, int i) {
        nativeSetRoadResId(this.mNativeInstance, z, i);
    }

    public void setArrowResId(boolean z, int i) {
        nativeSetArrowResId(this.mNativeInstance, z, i);
    }

    public void setCarResId(int i) {
        nativeSetCarResId(this.mNativeInstance, i);
    }

    public void setSkyResId(boolean z, int i) {
        nativeSetSkyResId(this.mNativeInstance, z, i);
    }
}
