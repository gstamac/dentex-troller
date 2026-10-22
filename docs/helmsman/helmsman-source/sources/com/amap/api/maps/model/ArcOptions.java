package com.amap.api.maps.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.core.view.ViewCompat;

/* JADX INFO: loaded from: classes.dex */
public final class ArcOptions implements Parcelable {
    public static final ArcOptionsCreator CREATOR = new ArcOptionsCreator();
    String a;
    private LatLng b;
    private LatLng c;
    private LatLng d;
    private float e = 10.0f;
    private int f = ViewCompat.MEASURED_STATE_MASK;
    private float g = 0.0f;
    private boolean h = true;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        Bundle bundle = new Bundle();
        LatLng latLng = this.b;
        if (latLng != null) {
            bundle.putDouble("startlat", latLng.latitude);
            bundle.putDouble("startlng", this.b.longitude);
        }
        LatLng latLng2 = this.c;
        if (latLng2 != null) {
            bundle.putDouble("passedlat", latLng2.latitude);
            bundle.putDouble("passedlng", this.c.longitude);
        }
        LatLng latLng3 = this.d;
        if (latLng3 != null) {
            bundle.putDouble("endlat", latLng3.latitude);
            bundle.putDouble("endlng", this.d.longitude);
        }
        parcel.writeBundle(bundle);
        parcel.writeFloat(this.e);
        parcel.writeInt(this.f);
        parcel.writeFloat(this.g);
        parcel.writeByte(this.h ? (byte) 1 : (byte) 0);
        parcel.writeString(this.a);
    }

    public ArcOptions point(LatLng latLng, LatLng latLng2, LatLng latLng3) {
        this.b = latLng;
        this.c = latLng2;
        this.d = latLng3;
        return this;
    }

    public ArcOptions strokeWidth(float f) {
        this.e = f;
        return this;
    }

    public ArcOptions strokeColor(int i) {
        this.f = i;
        return this;
    }

    public ArcOptions zIndex(float f) {
        this.g = f;
        return this;
    }

    public ArcOptions visible(boolean z) {
        this.h = z;
        return this;
    }

    public float getStrokeWidth() {
        return this.e;
    }

    public int getStrokeColor() {
        return this.f;
    }

    public float getZIndex() {
        return this.g;
    }

    public boolean isVisible() {
        return this.h;
    }

    public LatLng getStart() {
        return this.b;
    }

    public LatLng getPassed() {
        return this.c;
    }

    public LatLng getEnd() {
        return this.d;
    }
}
