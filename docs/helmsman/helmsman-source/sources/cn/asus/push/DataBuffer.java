package cn.asus.push;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class DataBuffer implements Parcelable {
    public static final Parcelable.Creator<DataBuffer> CREATOR = new a();
    public Bundle bundle;
    public String cmd;

    protected DataBuffer(Parcel parcel) {
        this.cmd = parcel.readString();
        this.bundle = parcel.readBundle();
    }

    public DataBuffer(String str, Bundle bundle) {
        this.cmd = str;
        this.bundle = bundle;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.cmd);
        parcel.writeBundle(this.bundle);
    }
}
