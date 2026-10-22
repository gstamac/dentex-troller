package com.amap.api.maps.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.core.view.ViewCompat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class CircleOptions implements Parcelable {
    public static final CircleOptionsCreator CREATOR = new CircleOptionsCreator();
    String a;
    private LatLng b = null;
    private double c = 0.0d;
    private float d = 10.0f;
    private int e = ViewCompat.MEASURED_STATE_MASK;
    private int f = 0;
    private float g = 0.0f;
    private boolean h = true;
    private List<BaseHoleOptions> i = new ArrayList();

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        Bundle bundle = new Bundle();
        LatLng latLng = this.b;
        if (latLng != null) {
            bundle.putDouble("lat", latLng.latitude);
            bundle.putDouble("lng", this.b.longitude);
        }
        parcel.writeBundle(bundle);
        parcel.writeDouble(this.c);
        parcel.writeFloat(this.d);
        parcel.writeInt(this.e);
        parcel.writeInt(this.f);
        parcel.writeFloat(this.g);
        parcel.writeByte(this.h ? (byte) 1 : (byte) 0);
        parcel.writeString(this.a);
    }

    public CircleOptions center(LatLng latLng) {
        this.b = latLng;
        return this;
    }

    public CircleOptions radius(double d) {
        this.c = d;
        return this;
    }

    public CircleOptions strokeWidth(float f) {
        this.d = f;
        return this;
    }

    public CircleOptions strokeColor(int i) {
        this.e = i;
        return this;
    }

    public CircleOptions fillColor(int i) {
        this.f = i;
        return this;
    }

    public CircleOptions zIndex(float f) {
        this.g = f;
        return this;
    }

    public CircleOptions visible(boolean z) {
        this.h = z;
        return this;
    }

    public LatLng getCenter() {
        return this.b;
    }

    public double getRadius() {
        return this.c;
    }

    public float getStrokeWidth() {
        return this.d;
    }

    public int getStrokeColor() {
        return this.e;
    }

    public int getFillColor() {
        return this.f;
    }

    public float getZIndex() {
        return this.g;
    }

    public boolean isVisible() {
        return this.h;
    }

    public CircleOptions addHoles(BaseHoleOptions... baseHoleOptionsArr) {
        try {
            this.i.addAll(Arrays.asList(baseHoleOptionsArr));
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return this;
    }

    public CircleOptions addHoles(Iterable<BaseHoleOptions> iterable) {
        try {
            Iterator<BaseHoleOptions> it = iterable.iterator();
            while (it.hasNext()) {
                this.i.add(it.next());
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return this;
    }

    public List<BaseHoleOptions> getHoleOptions() {
        return this.i;
    }
}
