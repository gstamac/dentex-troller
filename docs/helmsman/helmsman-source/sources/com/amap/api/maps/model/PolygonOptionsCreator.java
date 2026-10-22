package com.amap.api.maps.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class PolygonOptionsCreator implements Parcelable.Creator<PolygonOptions> {
    public static final int CONTENT_DESCRIPTION = 0;

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public PolygonOptions createFromParcel(Parcel parcel) {
        PolygonOptions polygonOptions = new PolygonOptions();
        ArrayList arrayList = new ArrayList();
        parcel.readTypedList(arrayList, LatLng.CREATOR);
        float f = parcel.readFloat();
        int i = parcel.readInt();
        int i2 = parcel.readInt();
        float f2 = parcel.readFloat();
        boolean z = parcel.readByte() == 1;
        LatLng[] latLngArr = new LatLng[arrayList.size()];
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            latLngArr[i3] = (LatLng) arrayList.get(i3);
        }
        polygonOptions.add(latLngArr);
        polygonOptions.strokeWidth(f);
        polygonOptions.strokeColor(i);
        polygonOptions.fillColor(i2);
        polygonOptions.zIndex(f2);
        polygonOptions.visible(z);
        polygonOptions.a = parcel.readString();
        return polygonOptions;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public PolygonOptions[] newArray(int i) {
        return new PolygonOptions[i];
    }
}
