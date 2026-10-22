package com.autonavi.ae.gmap.gesture.message;

/* JADX INFO: loaded from: classes.dex */
public class ScaleGestureMapMessage extends GestureMapMessage {
    public int mPivotX;
    public int mPivotY;
    public float mScaleDelta;

    @Override // com.autonavi.ae.gmap.MapMessage
    public int getType() {
        return 1;
    }

    public ScaleGestureMapMessage(int i, float f, int i2, int i3) {
        super(i);
        this.mScaleDelta = f;
        this.mPivotX = i2;
        this.mPivotY = i3;
    }
}
