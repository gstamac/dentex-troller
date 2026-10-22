package com.zendesk.belvedere;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import java.io.File;

/* JADX INFO: loaded from: classes2.dex */
public class BelvedereResult implements Parcelable {
    public static final Parcelable.Creator<BelvedereResult> CREATOR = new Parcelable.Creator<BelvedereResult>() { // from class: com.zendesk.belvedere.BelvedereResult.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BelvedereResult createFromParcel(Parcel parcel) {
            return new BelvedereResult(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BelvedereResult[] newArray(int i) {
            return new BelvedereResult[i];
        }
    };
    private final File file;
    private final Uri uri;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public BelvedereResult(File file, Uri uri) {
        this.file = file;
        this.uri = uri;
    }

    public File getFile() {
        return this.file;
    }

    public Uri getUri() {
        return this.uri;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeSerializable(this.file);
        parcel.writeParcelable(this.uri, i);
    }

    private BelvedereResult(Parcel parcel) {
        this.file = (File) parcel.readSerializable();
        this.uri = (Uri) parcel.readParcelable(BelvedereResult.class.getClassLoader());
    }
}
