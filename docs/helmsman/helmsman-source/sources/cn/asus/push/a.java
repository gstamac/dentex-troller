package cn.asus.push;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
final class a implements Parcelable.Creator<DataBuffer> {
    a() {
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ DataBuffer createFromParcel(Parcel parcel) {
        return new DataBuffer(parcel);
    }

    @Override // android.os.Parcelable.Creator
    public final /* bridge */ /* synthetic */ DataBuffer[] newArray(int i) {
        return new DataBuffer[i];
    }
}
