package com.autonavi.ae.gmap.glanimation;

import android.graphics.Point;
import android.os.SystemClock;
import com.autonavi.ae.gmap.GLMapState;
import com.autonavi.amap.mapcore.FPoint;
import com.autonavi.amap.mapcore.IPoint;

/* JADX INFO: loaded from: classes.dex */
public class ADGLMapAnimPivotZoom extends ADGLAnimation {
    private int _beginCenterGeoX;
    private int _beginCenterGeoY;
    private boolean _hasCheckParams;
    private float _mapLevelTo;
    private int _pivotGeoX;
    private int _pivotGeoY;
    private float _winPivotX;
    private float _winPivotY;
    private boolean _zoomCenter;
    ADGLAnimationParam1V _zoomParam = null;

    public void reset() {
        this._isOver = false;
        this._hasCheckParams = false;
        this._zoomCenter = true;
        this._mapLevelTo = 0.0f;
        this._beginCenterGeoX = 0;
        this._beginCenterGeoY = 0;
        this._pivotGeoX = 0;
        this._pivotGeoY = 0;
        this._winPivotX = 0.0f;
        this._winPivotY = 0.0f;
        ADGLAnimationParam1V aDGLAnimationParam1V = this._zoomParam;
        if (aDGLAnimationParam1V != null) {
            aDGLAnimationParam1V.reset();
        }
    }

    public void commitAnimation(Object obj) {
        this._isOver = true;
        this._hasCheckParams = false;
        GLMapState gLMapState = (GLMapState) obj;
        if (gLMapState == null) {
            return;
        }
        float mapZoomer = gLMapState.getMapZoomer();
        if (Math.abs(this._mapLevelTo - mapZoomer) < 1.0E-6d) {
            this._isOver = true;
            this._hasCheckParams = true;
            return;
        }
        this._zoomParam.setFromValue(mapZoomer);
        this._zoomParam.setToValue(this._mapLevelTo);
        if (!this._zoomCenter) {
            IPoint iPointObtain = IPoint.obtain();
            gLMapState.getGeoCenter(iPointObtain);
            this._beginCenterGeoX = iPointObtain.x;
            this._beginCenterGeoY = iPointObtain.y;
            FPoint fPointObtain = FPoint.obtain();
            IPoint iPointObtain2 = IPoint.obtain();
            gLMapState.win2Map((int) this._winPivotX, (int) this._winPivotY, fPointObtain);
            gLMapState.map2Geo(fPointObtain.x, fPointObtain.y, iPointObtain2);
            this._pivotGeoX = iPointObtain2.x;
            this._pivotGeoY = iPointObtain2.y;
            iPointObtain.recycle();
            fPointObtain.recycle();
            iPointObtain2.recycle();
        }
        this._hasCheckParams = true;
        this._isOver = false;
        this._startTime = SystemClock.uptimeMillis();
    }

    public ADGLMapAnimPivotZoom(int i) {
        reset();
        this._duration = i;
    }

    public void setToMapZoomAndPivot(float f, int i, Point point) {
        ADGLAnimationParam1V aDGLAnimationParam1V = new ADGLAnimationParam1V();
        this._zoomParam = aDGLAnimationParam1V;
        aDGLAnimationParam1V.setInterpolatorType(i, 1.0f);
        if (f > 20.0f) {
            f = 20.0f;
        }
        if (f < 3.0f) {
            f = 3.0f;
        }
        this._mapLevelTo = f;
        if (point != null) {
            this._winPivotX = point.x;
            this._winPivotY = point.y;
            this._zoomCenter = false;
        }
    }

    @Override // com.autonavi.ae.gmap.glanimation.ADGLAnimation
    public void doAnimation(Object obj) {
        GLMapState gLMapState = (GLMapState) obj;
        if (gLMapState == null) {
            return;
        }
        if (!this._hasCheckParams) {
            commitAnimation(obj);
        }
        if (this._isOver) {
            return;
        }
        this._offsetTime = SystemClock.uptimeMillis() - this._startTime;
        float f = this._offsetTime / this._duration;
        if (f > 1.0f) {
            this._isOver = true;
            f = 1.0f;
        }
        if (f < 0.0f || f > 1.0f) {
            return;
        }
        this._zoomParam.setNormalizedTime(f);
        float curValue = this._zoomParam.getCurValue();
        if (curValue > 20.0f) {
            this._isOver = true;
            curValue = 20.0f;
        }
        if (curValue < 3.0f) {
            this._isOver = true;
            curValue = 3.0f;
        }
        if (!this._zoomCenter) {
            float fPow = (float) Math.pow(2.0d, curValue - this._zoomParam.getFromValue());
            int i = this._pivotGeoX;
            int i2 = this._beginCenterGeoX;
            float f2 = 1.0f - (1.0f / fPow);
            int i3 = (int) ((i - i2) * f2);
            int i4 = this._pivotGeoY;
            int i5 = this._beginCenterGeoY;
            gLMapState.setMapGeoCenter(i2 + i3, i5 + ((int) ((i4 - i5) * f2)));
        }
        gLMapState.setMapZoomer(curValue);
    }
}
