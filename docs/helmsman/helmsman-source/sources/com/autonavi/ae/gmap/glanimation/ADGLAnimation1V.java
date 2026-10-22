package com.autonavi.ae.gmap.glanimation;

import android.os.SystemClock;

/* JADX INFO: loaded from: classes.dex */
public class ADGLAnimation1V extends ADGLAnimation {
    private float _curValue;
    private ADGLAnimationParam1V _v1Param = null;

    public void reset() {
        this._isOver = false;
        this._duration = 0;
        ADGLAnimationParam1V aDGLAnimationParam1V = this._v1Param;
        if (aDGLAnimationParam1V != null) {
            aDGLAnimationParam1V.reset();
        }
    }

    public ADGLAnimation1V(int i) {
        reset();
        this._duration = i;
        this._curValue = 0.0f;
    }

    public void setAnimationValue(float f, float f2, int i) {
        if (this._v1Param == null) {
            this._v1Param = new ADGLAnimationParam1V();
        }
        this._v1Param.reset();
        this._v1Param.setInterpolatorType(i, 1.0f);
        this._v1Param.setFromValue(f);
        this._v1Param.setToValue(f2);
        this._startTime = SystemClock.uptimeMillis();
        this._isOver = false;
    }

    public float getCurValue() {
        return this._curValue;
    }

    public float getStartValue() {
        ADGLAnimationParam1V aDGLAnimationParam1V = this._v1Param;
        if (aDGLAnimationParam1V != null) {
            return aDGLAnimationParam1V.getFromValue();
        }
        return 0.0f;
    }

    public float getEndValue() {
        ADGLAnimationParam1V aDGLAnimationParam1V = this._v1Param;
        if (aDGLAnimationParam1V != null) {
            return aDGLAnimationParam1V.getToValue();
        }
        return 0.0f;
    }

    @Override // com.autonavi.ae.gmap.glanimation.ADGLAnimation
    public void doAnimation(Object obj) {
        if (this._isOver) {
            return;
        }
        this._offsetTime = SystemClock.uptimeMillis() - this._startTime;
        float f = this._offsetTime / this._duration;
        if (f > 1.0f) {
            this._isOver = true;
            f = 1.0f;
        } else if (f < 0.0f) {
            this._isOver = true;
            return;
        }
        ADGLAnimationParam1V aDGLAnimationParam1V = this._v1Param;
        if (aDGLAnimationParam1V != null) {
            aDGLAnimationParam1V.setNormalizedTime(f);
            this._curValue = this._v1Param.getCurValue();
        }
    }
}
