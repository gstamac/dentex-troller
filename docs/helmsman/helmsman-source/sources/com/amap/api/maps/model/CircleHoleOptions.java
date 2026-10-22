package com.amap.api.maps.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class CircleHoleOptions extends BaseHoleOptions implements Parcelable {
    public static final Parcelable.Creator<CircleHoleOptions> CREATOR = new Parcelable.Creator<CircleHoleOptions>() { // from class: com.amap.api.maps.model.CircleHoleOptions.1
        @Override // android.os.Parcelable.Creator
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public CircleHoleOptions createFromParcel(Parcel parcel) {
            return new CircleHoleOptions(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public CircleHoleOptions[] newArray(int i) {
            return new CircleHoleOptions[i];
        }
    };
    private LatLng a;
    private double b;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public CircleHoleOptions() {
        this.a = null;
        this.b = 0.0d;
    }

    public CircleHoleOptions center(LatLng latLng) {
        this.a = latLng;
        return this;
    }

    public CircleHoleOptions radius(double d) {
        this.b = d;
        return this;
    }

    public LatLng getCenter() {
        return this.a;
    }

    public double getRadius() {
        return this.b;
    }

    protected CircleHoleOptions(Parcel parcel) {
        this.a = null;
        this.b = 0.0d;
        this.a = (LatLng) parcel.readParcelable(LatLng.class.getClassLoader());
        this.b = parcel.readDouble();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        Bundle bundle = new Bundle();
        LatLng latLng = this.a;
        if (latLng != null) {
            bundle.putDouble("lat", latLng.latitude);
            bundle.putDouble("lng", this.a.longitude);
        }
        parcel.writeBundle(bundle);
        parcel.writeDouble(this.b);
    }
}
