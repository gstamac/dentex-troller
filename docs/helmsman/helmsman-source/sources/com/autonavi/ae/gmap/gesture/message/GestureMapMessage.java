package com.autonavi.ae.gmap.gesture.message;

import com.autonavi.ae.gmap.MapMessage;

/* JADX INFO: loaded from: classes.dex */
public abstract class GestureMapMessage extends MapMessage {
    private int state_;

    public GestureMapMessage(int i) {
        this.state_ = i;
    }

    public int getMapGestureState() {
        return this.state_;
    }
}
