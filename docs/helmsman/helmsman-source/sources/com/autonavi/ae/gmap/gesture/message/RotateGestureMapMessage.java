package com.autonavi.ae.gmap.gesture.message;

/* JADX INFO: loaded from: classes.dex */
public class RotateGestureMapMessage extends GestureMapMessage {
    public float mAngleDelta;
    public int mPivotX;
    public int mPivotY;

    @Override // com.autonavi.ae.gmap.MapMessage
    public int getType() {
        return 2;
    }

    public RotateGestureMapMessage(int i, float f, int i2, int i3) {
        super(i);
        this.mAngleDelta = f;
        this.mPivotX = i2;
        this.mPivotY = i3;
    }
}
