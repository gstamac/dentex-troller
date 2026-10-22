package com.capricorn;

import android.view.animation.Animation;
import android.view.animation.Transformation;

/* JADX INFO: loaded from: classes.dex */
public class RotateAndTranslateAnimation extends Animation {
    private float mFromDegrees;
    private float mFromXDelta;
    private float mFromXValue;
    private float mFromYDelta;
    private float mFromYValue;
    private float mPivotX;
    private float mPivotY;
    private float mToDegrees;
    private float mToXDelta;
    private float mToXValue;
    private float mToYDelta;
    private float mToYValue;
    private int mFromXType = 0;
    private int mToXType = 0;
    private int mFromYType = 0;
    private int mToYType = 0;
    private float mPivotXValue = 0.5f;
    private int mPivotXType = 1;
    private float mPivotYValue = 0.5f;
    private int mPivotYType = 1;

    public RotateAndTranslateAnimation(float f, float f2, float f3, float f4, float f5, float f6) {
        this.mFromXValue = f;
        this.mToXValue = f2;
        this.mFromYValue = f3;
        this.mToYValue = f4;
        this.mFromDegrees = f5;
        this.mToDegrees = f6;
    }

    @Override // android.view.animation.Animation
    protected void applyTransformation(float f, Transformation transformation) {
        float f2 = this.mFromDegrees;
        float f3 = f2 + ((this.mToDegrees - f2) * f);
        if (this.mPivotX == 0.0f && this.mPivotY == 0.0f) {
            transformation.getMatrix().setRotate(f3);
        } else {
            transformation.getMatrix().setRotate(f3, this.mPivotX, this.mPivotY);
        }
        float f4 = this.mFromXDelta;
        float f5 = this.mFromYDelta;
        float f6 = this.mToXDelta;
        if (f4 != f6) {
            f4 += (f6 - f4) * f;
        }
        float f7 = this.mToYDelta;
        if (f5 != f7) {
            f5 += (f7 - f5) * f;
        }
        transformation.getMatrix().postTranslate(f4, f5);
    }

    @Override // android.view.animation.Animation
    public void initialize(int i, int i2, int i3, int i4) {
        super.initialize(i, i2, i3, i4);
        this.mFromXDelta = resolveSize(this.mFromXType, this.mFromXValue, i, i3);
        this.mToXDelta = resolveSize(this.mToXType, this.mToXValue, i, i3);
        this.mFromYDelta = resolveSize(this.mFromYType, this.mFromYValue, i2, i4);
        this.mToYDelta = resolveSize(this.mToYType, this.mToYValue, i2, i4);
        this.mPivotX = resolveSize(this.mPivotXType, this.mPivotXValue, i, i3);
        this.mPivotY = resolveSize(this.mPivotYType, this.mPivotYValue, i2, i4);
    }
}
