package com.autonavi.ae.gmap.gloverlay;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.TypedValue;
import com.MAVLink.common.msg_statustext;
import com.MAVLink.enums.MAV_COMPONENT;
import com.MAVLink.helmsman.msg_record_route_data;
import com.autonavi.amap.mapcore.interfaces.IAMap;

/* JADX INFO: loaded from: classes.dex */
public class CrossVectorOverlay extends BaseMapOverlay<GLCrossVector, Object> {
    GLCrossVector.AVectorCrossAttr attr;
    int mIndex;

    @Override // com.autonavi.ae.gmap.gloverlay.BaseMapOverlay
    public void addItem(Object obj) {
    }

    public CrossVectorOverlay(int i, Context context, IAMap iAMap) {
        super(i, context, iAMap);
        this.mIndex = 0;
        this.attr = null;
    }

    @Override // com.autonavi.ae.gmap.gloverlay.BaseMapOverlay
    protected void iniGLOverlay() {
        this.mGLOverlay = new GLCrossVector(this.mEngineID, this.mMapView, hashCode());
    }

    @Override // com.autonavi.ae.gmap.gloverlay.BaseMapOverlay
    public void resumeMarker(Bitmap bitmap) {
        AddOverlayTexture(bitmap, 12345, 4);
        ((GLCrossVector) this.mGLOverlay).setArrowResId(false, 12345);
        ((GLCrossVector) this.mGLOverlay).setCarResId(12345);
    }

    public void AddOverlayTexture(Bitmap bitmap, int i, int i2) {
        GLTextureProperty gLTextureProperty = new GLTextureProperty();
        gLTextureProperty.mId = i;
        gLTextureProperty.mAnchor = i2;
        gLTextureProperty.mBitmap = bitmap;
        gLTextureProperty.mXRatio = 0.0f;
        gLTextureProperty.mYRatio = 0.0f;
        gLTextureProperty.isGenMimps = true;
        this.mMapView.addOverlayTexture(this.mEngineID, gLTextureProperty);
    }

    public int dipToPixel(Context context, int i) {
        if (context == null) {
            return i;
        }
        try {
            return (int) TypedValue.applyDimension(1, i, context.getResources().getDisplayMetrics());
        } catch (Exception unused) {
            return i;
        }
    }

    public void setAttribute(GLCrossVector.AVectorCrossAttr aVectorCrossAttr) {
        this.attr = aVectorCrossAttr;
    }

    public void setData(byte[] bArr) {
        if (this.attr == null) {
            GLCrossVector.AVectorCrossAttr aVectorCrossAttr = new GLCrossVector.AVectorCrossAttr();
            aVectorCrossAttr.nCenterX = 0;
            aVectorCrossAttr.nCenterY = 0;
            aVectorCrossAttr.stRectMax = new Rect();
            aVectorCrossAttr.stRectMin = new Rect();
            aVectorCrossAttr.nAngle = 0;
            aVectorCrossAttr.stAreaRect = new Rect(0, 0, this.mMapView.getMapWidth(), (this.mMapView.getMapHeight() * 4) / 11);
            aVectorCrossAttr.stAreaColor = Color.argb(217, 95, 95, 95);
            aVectorCrossAttr.fImportBorderWidth = dipToPixel(this.mContext, 22);
            aVectorCrossAttr.stImportBorderColor = Color.argb(255, 255, 255, 255);
            aVectorCrossAttr.fUnImportBorderWidth = aVectorCrossAttr.fImportBorderWidth;
            aVectorCrossAttr.stUnImportBorderColor = aVectorCrossAttr.stImportBorderColor;
            aVectorCrossAttr.fArrowBorderWidth = dipToPixel(this.mContext, 22);
            aVectorCrossAttr.stArrowBorderColor = Color.argb(0, 0, 50, 20);
            aVectorCrossAttr.fImportLineWidth = dipToPixel(this.mContext, 18);
            aVectorCrossAttr.stImportLineColor = Color.argb(255, MAV_COMPONENT.MAV_COMP_ID_SERVO11, msg_record_route_data.MAVLINK_MSG_ID_RECORD_ROUTE_DATA, 200);
            aVectorCrossAttr.fUnImportLineWidth = aVectorCrossAttr.fImportLineWidth;
            aVectorCrossAttr.stUnImportLineColor = aVectorCrossAttr.stImportLineColor;
            aVectorCrossAttr.fDashLineWidth = dipToPixel(this.mContext, 2);
            aVectorCrossAttr.stDashLineColor = aVectorCrossAttr.stUnImportBorderColor;
            aVectorCrossAttr.fArrowLineWidth = dipToPixel(this.mContext, 18);
            aVectorCrossAttr.stArrowLineColor = Color.argb(255, 255, msg_statustext.MAVLINK_MSG_ID_STATUSTEXT, 65);
            aVectorCrossAttr.dayMode = false;
        }
        if (bArr == null || this.attr == null) {
            return;
        }
        ((GLCrossVector) this.mGLOverlay).addVectorItem(this.attr, bArr, bArr.length);
        ((GLCrossVector) this.mGLOverlay).setVisible(true);
    }

    public void remove() {
        setVisible(false);
        releaseInstance();
    }
}
