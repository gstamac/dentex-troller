package com.autonavi.ae.gmap.glanimation;

import android.os.SystemClock;
import com.autonavi.ae.gmap.GLMapState;
import com.autonavi.amap.mapcore.FPoint;
import com.autonavi.amap.mapcore.IPoint;

/* JADX INFO: loaded from: classes.dex */
public class ADGLMapAnimFling extends ADGLAnimation {
    private IPoint _fromCenter;
    private boolean _hasCheckParams;
    private int _lastMoveX;
    private int _lastMoveY;
    private ADGLAnimationParam2V _moveParam = null;
    private boolean _needMove;
    private int _screenCenterX;
    private int _screenCenterY;
    private float _velocityX;
    private float _velocityY;

    public ADGLMapAnimFling(int i, int i2, int i3) {
        this._screenCenterX = i2;
        this._screenCenterY = i3;
        this._lastMoveX = i2;
        this._lastMoveY = i3;
        reset();
        this._duration = i;
    }

    public void reset() {
        ADGLAnimationParam2V aDGLAnimationParam2V = this._moveParam;
        if (aDGLAnimationParam2V != null) {
            aDGLAnimationParam2V.reset();
        }
        this._velocityX = 0.0f;
        this._velocityY = 0.0f;
        this._needMove = false;
        this._hasCheckParams = false;
    }

    public void setPositionAndVelocity(float f, float f2) {
        this._moveParam = null;
        this._velocityX = f;
        this._velocityY = f2;
        ADGLAnimationParam2V aDGLAnimationParam2V = new ADGLAnimationParam2V();
        this._moveParam = aDGLAnimationParam2V;
        aDGLAnimationParam2V.setInterpolatorType(2, 1.2f);
        this._needMove = false;
        this._hasCheckParams = false;
    }

    public void commitAnimationold(Object obj) {
        GLMapState gLMapState = (GLMapState) obj;
        if (gLMapState == null) {
            return;
        }
        this._hasCheckParams = false;
        this._isOver = true;
        float f = this._velocityX;
        float f2 = this._velocityY;
        float fSqrt = ((float) Math.sqrt((f * f) + (f2 * f2))) / 1000.0f;
        if (fSqrt >= 0.1f) {
            float f3 = fSqrt * 0.02f;
            if (this._fromCenter == null) {
                this._fromCenter = IPoint.obtain();
            }
            gLMapState.getGeoCenter(this._fromCenter);
            this._isOver = false;
            this._moveParam.setFromValue(this._screenCenterX, this._screenCenterY);
            this._moveParam.setToValue(this._screenCenterX - (this._velocityX * f3), this._screenCenterY - (this._velocityY * f3));
            this._needMove = this._moveParam.needToCaculate();
        }
        this._hasCheckParams = true;
        this._startTime = SystemClock.uptimeMillis();
    }

    public void commitAnimation(Object obj) {
        GLMapState gLMapState = (GLMapState) obj;
        if (gLMapState == null) {
            return;
        }
        this._hasCheckParams = false;
        this._isOver = true;
        int i = (int) ((this._velocityX * this._duration) / 2000.0f);
        int i2 = (int) ((this._velocityY * this._duration) / 2000.0f);
        if (Math.abs(i) != 0 && Math.abs(i2) != 0) {
            if (this._fromCenter == null) {
                this._fromCenter = IPoint.obtain();
            }
            gLMapState.getGeoCenter(this._fromCenter);
            this._isOver = false;
            this._moveParam.setFromValue(this._screenCenterX, this._screenCenterY);
            this._moveParam.setToValue(this._screenCenterX - i, this._screenCenterY - i2);
            this._needMove = this._moveParam.needToCaculate();
        }
        this._hasCheckParams = true;
        this._startTime = SystemClock.uptimeMillis();
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
        if (f < 0.0f || f > 1.0f || !this._needMove) {
            return;
        }
        this._moveParam.setNormalizedTime(f);
        int curXValue = (int) this._moveParam.getCurXValue();
        int curYValue = (int) this._moveParam.getCurYValue();
        FPoint fPointObtain = FPoint.obtain();
        gLMapState.win2Map((this._screenCenterX + curXValue) - this._lastMoveX, (this._screenCenterY + curYValue) - this._lastMoveY, fPointObtain);
        gLMapState.setMapGlCenter(fPointObtain.x, fPointObtain.y);
        this._lastMoveX = curXValue;
        this._lastMoveY = curYValue;
        fPointObtain.recycle();
    }
}
