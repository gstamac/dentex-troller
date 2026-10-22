package com.autonavi.ae.gmap.gesture.message;

/* JADX INFO: loaded from: classes.dex */
public class HoverGestureMapMessage extends GestureMapMessage {
    public float mAngleDelta;

    @Override // com.autonavi.ae.gmap.MapMessage
    public int getType() {
        return 3;
    }

    public HoverGestureMapMessage(int i, float f) {
        super(i);
        this.mAngleDelta = f;
    }
}
