package com.autonavi.ae.gmap.glanimation;

import android.graphics.PointF;
import android.os.SystemClock;
import com.autonavi.ae.gmap.GLMapState;
import com.autonavi.amap.mapcore.FPoint;
import com.autonavi.amap.mapcore.IPoint;

/* JADX INFO: loaded from: classes.dex */
public class ADGLMapAnimGroup extends ADGLAnimation {
    public static final int CAMERA_MAX_DEGREE = 60;
    public static final int CAMERA_MIN_DEGREE = 0;
    public static final int MAXMAPLEVEL = 20;
    public static final int MINMAPLEVEL = 3;
    int _endZoomDuration;
    boolean _hasCheckParams;
    boolean _hasMidZoom;
    int _midZoomDuration;
    public boolean _needMove;
    boolean _needRotateCamera;
    boolean _needRotateMap;
    boolean _needZoom;
    int _startZoomDuration;
    ADGLAnimationParam1V _zoomStartParam = null;
    ADGLAnimationParam1V _zoomEndParam = null;
    ADGLAnimationParam2V _moveParam = null;
    ADGLAnimationParam1V _rotateMapParam = null;
    ADGLAnimationParam1V _rotateCameraParam = null;

    public static boolean checkLevel(float f) {
        return f >= 3.0f && f <= 20.0f;
    }

    public ADGLMapAnimGroup(int i) {
        reset();
        this._duration = i;
    }

    public void setDuration(int i) {
        this._duration = i;
    }

    public void reset() {
        this._isOver = false;
        this._hasCheckParams = false;
        this._needZoom = false;
        this._needMove = false;
        this._moveParam = null;
        this._needRotateMap = false;
        this._rotateMapParam = null;
        this._hasMidZoom = false;
        this._duration = 0;
        ADGLAnimationParam1V aDGLAnimationParam1V = this._rotateMapParam;
        if (aDGLAnimationParam1V != null) {
            aDGLAnimationParam1V.reset();
        }
        ADGLAnimationParam2V aDGLAnimationParam2V = this._moveParam;
        if (aDGLAnimationParam2V != null) {
            aDGLAnimationParam2V.reset();
        }
        ADGLAnimationParam1V aDGLAnimationParam1V2 = this._zoomStartParam;
        if (aDGLAnimationParam1V2 != null) {
            aDGLAnimationParam1V2.reset();
        }
        ADGLAnimationParam1V aDGLAnimationParam1V3 = this._zoomEndParam;
        if (aDGLAnimationParam1V3 != null) {
            aDGLAnimationParam1V3.reset();
        }
        ADGLAnimationParam1V aDGLAnimationParam1V4 = this._rotateCameraParam;
        if (aDGLAnimationParam1V4 != null) {
            aDGLAnimationParam1V4.reset();
        }
    }

    @Override // com.autonavi.ae.gmap.glanimation.ADGLAnimation
    public boolean isValid() {
        return this._needRotateCamera || this._needRotateMap || this._needMove || this._needZoom;
    }

    public void setToMapAngle(float f, int i) {
        float f2 = f % 360.0f;
        this._needRotateMap = true;
        if (this._rotateMapParam == null) {
            this._rotateMapParam = new ADGLAnimationParam1V();
        }
        this._rotateMapParam.reset();
        this._rotateMapParam.setInterpolatorType(i, 1.0f);
        this._rotateMapParam.setToValue(f2);
    }

    public void setToMapCenterGeo(int i, int i2, int i3) {
        if (i <= 0 || i2 <= 0) {
            return;
        }
        this._needMove = true;
        if (this._moveParam == null) {
            this._moveParam = new ADGLAnimationParam2V();
        }
        this._moveParam.reset();
        this._moveParam.setInterpolatorType(i3, 1.0f);
        this._moveParam.setToValue(i, i2);
    }

    public void setToMapLevel(float f, int i) {
        this._needZoom = true;
        this._midZoomDuration = 0;
        this._hasMidZoom = false;
        if (checkLevel(f)) {
            initZoomStartParam(f, i);
        } else {
            this._needZoom = false;
        }
    }

    public void setToMapLevel(float f, float f2, int i) {
        this._needZoom = true;
        this._midZoomDuration = 0;
        this._hasMidZoom = false;
        if (i > 0 && i < this._duration) {
            this._midZoomDuration = i;
        }
        if (checkLevel(f) && checkLevel(f2)) {
            this._hasMidZoom = true;
            initZoomStartParam(f2, 0);
            initZoomEndParam(f2, f, 0);
        } else if (checkLevel(f)) {
            this._hasMidZoom = false;
            initZoomStartParam(f, 0);
        } else if (checkLevel(f2)) {
            this._hasMidZoom = false;
            initZoomStartParam(f2, 0);
        } else {
            this._needZoom = false;
        }
    }

    public void setToCameraDegree(float f, int i) {
        this._needRotateCamera = false;
        if (f > 60.0f || f < 0.0f) {
            return;
        }
        this._needRotateCamera = true;
        if (this._rotateCameraParam == null) {
            this._rotateCameraParam = new ADGLAnimationParam1V();
        }
        this._rotateCameraParam.reset();
        this._rotateCameraParam.setInterpolatorType(i, 1.0f);
        this._rotateCameraParam.setToValue(f);
    }

    private void initZoomStartParam(float f, int i) {
        if (this._zoomStartParam == null) {
            this._zoomStartParam = new ADGLAnimationParam1V();
        }
        this._zoomStartParam.reset();
        this._zoomStartParam.setInterpolatorType(i, 1.0f);
        this._zoomStartParam.setToValue(f);
    }

    private void initZoomEndParam(float f, float f2, int i) {
        if (this._zoomEndParam == null) {
            this._zoomEndParam = new ADGLAnimationParam1V();
        }
        this._zoomEndParam.reset();
        this._zoomEndParam.setInterpolatorType(i, 1.0f);
        this._zoomEndParam.setToValue(f2);
        this._zoomEndParam.setFromValue(f);
    }

    public void commitAnimation(Object obj) {
        this._isOver = true;
        this._hasCheckParams = false;
        GLMapState gLMapState = (GLMapState) obj;
        if (gLMapState == null) {
            return;
        }
        if (this._needZoom) {
            if (this._zoomStartParam == null) {
                this._hasCheckParams = true;
                return;
            }
            float mapZoomer = gLMapState.getMapZoomer();
            this._zoomStartParam.setFromValue(mapZoomer);
            if (this._hasMidZoom) {
                float toValue = this._zoomStartParam.getToValue() - mapZoomer;
                float fromValue = this._zoomEndParam.getFromValue() - this._zoomEndParam.getToValue();
                if (Math.abs(toValue) < 1.0E-6d || Math.abs(fromValue) < 1.0E-6d) {
                    this._hasMidZoom = false;
                    this._zoomStartParam.setToValue(this._zoomEndParam.getToValue());
                    this._zoomStartParam.needToCaculate();
                    this._zoomEndParam = null;
                } else {
                    this._zoomStartParam.needToCaculate();
                    this._zoomEndParam.needToCaculate();
                }
            }
            if (!this._hasMidZoom && Math.abs(this._zoomStartParam.getFromValue() - this._zoomStartParam.getToValue()) < 1.0E-6d) {
                this._needZoom = false;
            }
            if (this._needZoom) {
                if (this._hasMidZoom) {
                    int i = (this._duration - this._midZoomDuration) >> 1;
                    this._startZoomDuration = i;
                    this._endZoomDuration = i;
                } else {
                    this._startZoomDuration = this._duration;
                }
            }
        }
        if (this._needMove && this._moveParam != null) {
            IPoint iPointObtain = IPoint.obtain();
            gLMapState.getGeoCenter(iPointObtain);
            int i2 = iPointObtain.x;
            int i3 = iPointObtain.y;
            iPointObtain.recycle();
            this._moveParam.setFromValue(i2, i3);
            this._needMove = this._moveParam.needToCaculate();
        }
        if (this._needRotateMap && this._rotateMapParam != null) {
            float mapAngle = gLMapState.getMapAngle();
            float toValue2 = this._rotateMapParam.getToValue();
            if (mapAngle > 180.0f && toValue2 == 0.0f) {
                toValue2 = 360.0f;
            }
            int i4 = ((int) toValue2) - ((int) mapAngle);
            if (i4 > 180) {
                toValue2 -= 360.0f;
            } else if (i4 < -180) {
                toValue2 += 360.0f;
            }
            this._rotateMapParam.setFromValue(mapAngle);
            this._rotateMapParam.setToValue(toValue2);
            this._needRotateMap = this._rotateMapParam.needToCaculate();
        }
        if (this._needRotateCamera && this._rotateCameraParam != null) {
            this._rotateCameraParam.setFromValue(gLMapState.getCameraHeaderAngle());
            this._needRotateCamera = this._rotateCameraParam.needToCaculate();
        }
        if (!this._needMove && !this._needZoom && !this._needRotateMap && !this._needRotateCamera) {
            this._isOver = true;
        } else {
            this._isOver = false;
        }
        this._hasCheckParams = true;
        this._startTime = SystemClock.uptimeMillis();
    }

    @Override // com.autonavi.ae.gmap.glanimation.ADGLAnimation
    public void doAnimation(Object obj) {
        float curValue;
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
        if (this._duration == 0.0f) {
            this._isOver = true;
            return;
        }
        float f = this._offsetTime / this._duration;
        if (f > 1.0f) {
            this._isOver = true;
            f = 1.0f;
        } else if (f < 0.0f) {
            this._isOver = true;
            return;
        }
        if (this._needZoom) {
            gLMapState.getMapZoomer();
            if (this._hasMidZoom) {
                if (this._offsetTime <= this._startZoomDuration) {
                    this._zoomStartParam.setNormalizedTime(this._offsetTime / this._startZoomDuration);
                    curValue = this._zoomStartParam.getCurValue();
                } else if (this._offsetTime <= this._startZoomDuration + this._midZoomDuration) {
                    curValue = this._zoomStartParam.getToValue();
                } else {
                    this._zoomEndParam.setNormalizedTime(((this._offsetTime - ((long) this._startZoomDuration)) - ((long) this._midZoomDuration)) / this._endZoomDuration);
                    curValue = this._zoomEndParam.getCurValue();
                }
                if (this._isOver) {
                    curValue = this._zoomEndParam.getToValue();
                }
            } else {
                this._zoomStartParam.setNormalizedTime(f);
                curValue = this._zoomStartParam.getCurValue();
            }
            gLMapState.setMapZoomer(curValue);
        }
        ADGLAnimationParam2V aDGLAnimationParam2V = this._moveParam;
        if (aDGLAnimationParam2V != null && this._needMove) {
            aDGLAnimationParam2V.setNormalizedTime(f);
            int fromXValue = (int) this._moveParam.getFromXValue();
            int fromYValue = (int) this._moveParam.getFromYValue();
            int toXValue = (int) this._moveParam.getToXValue();
            int toYValue = (int) this._moveParam.getToYValue();
            float curMult = this._moveParam.getCurMult();
            gLMapState.setMapGeoCenter(fromXValue, fromYValue);
            PointF mapGlCenter = gLMapState.getMapGlCenter();
            float f2 = mapGlCenter.x;
            float f3 = mapGlCenter.y;
            FPoint fPointObtain = FPoint.obtain();
            gLMapState.geo2Map(toXValue, toYValue, fPointObtain);
            gLMapState.setMapGlCenter(f2 + ((fPointObtain.x - f2) * curMult), f3 + ((fPointObtain.y - f3) * curMult));
            fPointObtain.recycle();
        }
        ADGLAnimationParam1V aDGLAnimationParam1V = this._rotateMapParam;
        if (aDGLAnimationParam1V != null && this._needRotateMap) {
            aDGLAnimationParam1V.setNormalizedTime(f);
            gLMapState.setMapAngle((int) this._rotateMapParam.getCurValue());
        }
        ADGLAnimationParam1V aDGLAnimationParam1V2 = this._rotateCameraParam;
        if (aDGLAnimationParam1V2 == null || !this._needRotateCamera) {
            return;
        }
        aDGLAnimationParam1V2.setNormalizedTime(f);
        gLMapState.setCameraDegree((int) this._rotateCameraParam.getCurValue());
    }

    public boolean typeEqueal(ADGLMapAnimGroup aDGLMapAnimGroup) {
        return this._needRotateCamera == aDGLMapAnimGroup._needRotateCamera && this._needRotateMap == aDGLMapAnimGroup._needRotateMap && this._needZoom == aDGLMapAnimGroup._needZoom && this._needMove == aDGLMapAnimGroup._needMove;
    }
}
