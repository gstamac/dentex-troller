package com.autonavi.ae.gmap.glanimation;

import android.os.SystemClock;

/* JADX INFO: loaded from: classes.dex */
public class ADGLAnimation2V extends ADGLAnimation {
    private float _curValue1;
    private float _curValue2;
    private ADGLAnimationParam2V _v2Param = null;

    public ADGLAnimation2V(int i) {
        reset();
        this._duration = i;
        this._curValue1 = 0.0f;
        this._curValue2 = 0.0f;
    }

    public void reset() {
        this._isOver = false;
        this._duration = 0;
        this._curValue1 = 0.0f;
        this._curValue2 = 0.0f;
        ADGLAnimationParam2V aDGLAnimationParam2V = this._v2Param;
        if (aDGLAnimationParam2V != null) {
            aDGLAnimationParam2V.reset();
        }
    }

    public void setAnimationValue(float f, float f2, float f3, float f4, int i) {
        if (this._v2Param == null) {
            this._v2Param = new ADGLAnimationParam2V();
        }
        this._v2Param.reset();
        this._v2Param.setInterpolatorType(i, 1.0f);
        this._v2Param.setFromValue(f, f3);
        this._v2Param.setToValue(f2, f4);
        this._startTime = SystemClock.uptimeMillis();
        this._isOver = false;
    }

    public float getCurValue(int i) {
        if (i == 0) {
            return this._curValue1;
        }
        return this._curValue2;
    }

    public float getStartValue(int i) {
        if (i == 0) {
            ADGLAnimationParam2V aDGLAnimationParam2V = this._v2Param;
            if (aDGLAnimationParam2V != null) {
                return aDGLAnimationParam2V.getFromXValue();
            }
            return 0.0f;
        }
        ADGLAnimationParam2V aDGLAnimationParam2V2 = this._v2Param;
        if (aDGLAnimationParam2V2 != null) {
            return aDGLAnimationParam2V2.getFromYValue();
        }
        return 0.0f;
    }

    public float getEndValue(int i) {
        if (i == 0) {
            ADGLAnimationParam2V aDGLAnimationParam2V = this._v2Param;
            if (aDGLAnimationParam2V != null) {
                return aDGLAnimationParam2V.getToXValue();
            }
            return 0.0f;
        }
        ADGLAnimationParam2V aDGLAnimationParam2V2 = this._v2Param;
        if (aDGLAnimationParam2V2 != null) {
            return aDGLAnimationParam2V2.getToYValue();
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
        ADGLAnimationParam2V aDGLAnimationParam2V = this._v2Param;
        if (aDGLAnimationParam2V != null) {
            aDGLAnimationParam2V.setNormalizedTime(f);
            this._curValue1 = this._v2Param.getCurXValue();
            this._curValue2 = this._v2Param.getCurYValue();
        }
    }
}
