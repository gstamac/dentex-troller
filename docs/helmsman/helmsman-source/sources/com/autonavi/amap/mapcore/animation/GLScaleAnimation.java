package com.autonavi.amap.mapcore.animation;

/* JADX INFO: loaded from: classes.dex */
public class GLScaleAnimation extends GLAnimation {
    private float mFromX;
    private float mFromY;
    private float mToX;
    private float mToY;
    private int mFromXData = 0;
    private int mToXData = 0;
    private int mFromYData = 0;
    private int mToYData = 0;
    private float mPivotX = 0.0f;
    private float mPivotY = 0.0f;

    public GLScaleAnimation(float f, float f2, float f3, float f4) {
        this.mFromX = f;
        this.mToX = f2;
        this.mFromY = f3;
        this.mToY = f4;
    }

    @Override // com.autonavi.amap.mapcore.animation.GLAnimation
    protected void applyTransformation(float f, GLTransformation gLTransformation) {
        getScaleFactor();
        float f2 = this.mFromX;
        float f3 = (f2 == 1.0f && this.mToX == 1.0f) ? 1.0f : f2 + ((this.mToX - f2) * f);
        float f4 = this.mFromY;
        float f5 = (f4 == 1.0f && this.mToY == 1.0f) ? 1.0f : ((this.mToY - f4) * f) + f4;
        if (this.mPivotX == 0.0f && this.mPivotY == 0.0f) {
            gLTransformation.scale_x = f3;
            gLTransformation.scale_y = f5;
        } else {
            gLTransformation.scale_x = f3;
            gLTransformation.scale_y = f5;
        }
    }
}
