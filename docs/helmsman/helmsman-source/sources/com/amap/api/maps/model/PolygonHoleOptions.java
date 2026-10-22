package com.amap.api.maps.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class PolygonHoleOptions extends BaseHoleOptions implements Parcelable {
    public static final Parcelable.Creator<PolygonHoleOptions> CREATOR = new Parcelable.Creator<PolygonHoleOptions>() { // from class: com.amap.api.maps.model.PolygonHoleOptions.1
        @Override // android.os.Parcelable.Creator
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public PolygonHoleOptions createFromParcel(Parcel parcel) {
            return new PolygonHoleOptions(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public PolygonHoleOptions[] newArray(int i) {
            return new PolygonHoleOptions[i];
        }
    };
    private final List<LatLng> a;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public PolygonHoleOptions() {
        this.a = new ArrayList();
    }

    protected PolygonHoleOptions(Parcel parcel) {
        this.a = parcel.createTypedArrayList(LatLng.CREATOR);
    }

    public PolygonHoleOptions addAll(Iterable<LatLng> iterable) {
        try {
            Iterator<LatLng> it = iterable.iterator();
            while (it.hasNext()) {
                this.a.add(it.next());
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return this;
    }

    public List<LatLng> getPoints() {
        return this.a;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(this.a);
    }
}
