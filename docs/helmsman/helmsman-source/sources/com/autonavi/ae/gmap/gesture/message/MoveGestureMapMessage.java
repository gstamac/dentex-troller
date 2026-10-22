package com.autonavi.ae.gmap.gesture.message;

/* JADX INFO: loaded from: classes.dex */
public class MoveGestureMapMessage extends GestureMapMessage {
    public float mTouchDeltaX;
    public float mTouchDeltaY;

    @Override // com.autonavi.ae.gmap.MapMessage
    public int getType() {
        return 0;
    }

    public MoveGestureMapMessage(int i, float f, float f2) {
        super(i);
        this.mTouchDeltaX = f;
        this.mTouchDeltaY = f2;
    }
}
