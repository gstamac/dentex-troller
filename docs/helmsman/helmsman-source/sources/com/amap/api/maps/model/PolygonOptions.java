package com.amap.api.maps.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.core.view.ViewCompat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class PolygonOptions implements Parcelable {
    public static final PolygonOptionsCreator CREATOR = new PolygonOptionsCreator();
    String a;
    private float c = 10.0f;
    private int d = ViewCompat.MEASURED_STATE_MASK;
    private int e = ViewCompat.MEASURED_STATE_MASK;
    private float f = 0.0f;
    private boolean g = true;
    private final List<LatLng> b = new ArrayList();
    private List<BaseHoleOptions> h = new ArrayList();

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(this.b);
        parcel.writeFloat(this.c);
        parcel.writeInt(this.d);
        parcel.writeInt(this.e);
        parcel.writeFloat(this.f);
        parcel.writeByte(this.g ? (byte) 1 : (byte) 0);
        parcel.writeString(this.a);
    }

    public PolygonOptions add(LatLng latLng) {
        try {
            this.b.add(latLng);
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return this;
    }

    public PolygonOptions add(LatLng... latLngArr) {
        try {
            this.b.addAll(Arrays.asList(latLngArr));
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return this;
    }

    public PolygonOptions addAll(Iterable<LatLng> iterable) {
        try {
            Iterator<LatLng> it = iterable.iterator();
            while (it.hasNext()) {
                this.b.add(it.next());
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return this;
    }

    public PolygonOptions addHoles(BaseHoleOptions... baseHoleOptionsArr) {
        try {
            this.h.addAll(Arrays.asList(baseHoleOptionsArr));
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return this;
    }

    public PolygonOptions addHoles(Iterable<BaseHoleOptions> iterable) {
        try {
            Iterator<BaseHoleOptions> it = iterable.iterator();
            while (it.hasNext()) {
                this.h.add(it.next());
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return this;
    }

    public PolygonOptions strokeWidth(float f) {
        this.c = f;
        return this;
    }

    public PolygonOptions strokeColor(int i) {
        this.d = i;
        return this;
    }

    public PolygonOptions fillColor(int i) {
        this.e = i;
        return this;
    }

    public PolygonOptions zIndex(float f) {
        this.f = f;
        return this;
    }

    public PolygonOptions visible(boolean z) {
        this.g = z;
        return this;
    }

    public List<LatLng> getPoints() {
        return this.b;
    }

    public float getStrokeWidth() {
        return this.c;
    }

    public int getStrokeColor() {
        return this.d;
    }

    public int getFillColor() {
        return this.e;
    }

    public float getZIndex() {
        return this.f;
    }

    public boolean isVisible() {
        return this.g;
    }

    public List<BaseHoleOptions> getHoleOptions() {
        return this.h;
    }
}
