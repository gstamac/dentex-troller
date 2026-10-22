package com.autonavi.ae.gmap.glinterface;

import java.io.Serializable;

/* JADX INFO: loaded from: classes.dex */
public class GLGeoPoint implements Serializable {
    private static final long serialVersionUID = 927014135610245467L;
    public int x;
    public int y;

    public GLGeoPoint() {
    }

    public GLGeoPoint(int i, int i2) {
        this.x = i;
        this.y = i2;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GLGeoPoint)) {
            return false;
        }
        GLGeoPoint gLGeoPoint = (GLGeoPoint) obj;
        return this.x == gLGeoPoint.x && this.y == gLGeoPoint.y;
    }

    public int hashCode() {
        return (this.x * 31) + this.y;
    }
}
