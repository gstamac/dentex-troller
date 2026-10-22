package com.autonavi.ae.gmap.glanimation;

/* JADX INFO: loaded from: classes.dex */
public class ADGLAnimationParam1V extends ADGLAnimationParam {
    private float _fromValue;
    private float _toValue;

    public ADGLAnimationParam1V() {
        reset();
    }

    @Override // com.autonavi.ae.gmap.glanimation.ADGLAnimationParam
    public void reset() {
        super.reset();
        this._fromValue = 0.0f;
        this._toValue = 0.0f;
    }

    public void setFromValue(float f) {
        this._fromValue = f;
        this._hasFromValue = true;
        this._hasCheckedParam = false;
    }

    public void setToValue(float f) {
        this._toValue = f;
        this._hasToValue = true;
        this._hasCheckedParam = false;
    }

    public float getFromValue() {
        return this._fromValue;
    }

    public float getToValue() {
        return this._toValue;
    }

    public float getCurValue() {
        float f = this._fromValue;
        return f + ((this._toValue - f) * this._mult);
    }

    @Override // com.autonavi.ae.gmap.glanimation.ADGLAnimationParam
    public void checkParam() {
        this._needToCaculate = false;
        if (this._hasFromValue && this._hasToValue && Math.abs(this._toValue - this._fromValue) > 1.0E-4d) {
            this._needToCaculate = true;
        }
        this._hasCheckedParam = true;
    }
}
