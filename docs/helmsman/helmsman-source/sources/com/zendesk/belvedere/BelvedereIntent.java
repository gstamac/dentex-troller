package com.zendesk.belvedere;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.fragment.app.Fragment;

/* JADX INFO: loaded from: classes2.dex */
public class BelvedereIntent implements Parcelable {
    public static final Parcelable.Creator<BelvedereIntent> CREATOR = new Parcelable.Creator<BelvedereIntent>() { // from class: com.zendesk.belvedere.BelvedereIntent.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BelvedereIntent createFromParcel(Parcel parcel) {
            return new BelvedereIntent(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BelvedereIntent[] newArray(int i) {
            return new BelvedereIntent[i];
        }
    };
    private final Intent intent;
    private final int requestCode;
    private final BelvedereSource source;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public BelvedereIntent(Intent intent, int i, BelvedereSource belvedereSource) {
        this.intent = intent;
        this.requestCode = i;
        this.source = belvedereSource;
    }

    public BelvedereSource getSource() {
        return this.source;
    }

    public Intent getIntent() {
        return this.intent;
    }

    public int getRequestCode() {
        return this.requestCode;
    }

    public void open(Activity activity) {
        activity.startActivityForResult(this.intent, this.requestCode);
    }

    public void open(Fragment fragment) {
        fragment.startActivityForResult(this.intent, this.requestCode);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.requestCode);
        parcel.writeParcelable(this.intent, i);
        parcel.writeSerializable(this.source);
    }

    private BelvedereIntent(Parcel parcel) {
        this.requestCode = parcel.readInt();
        this.intent = (Intent) parcel.readParcelable(BelvedereIntent.class.getClassLoader());
        this.source = (BelvedereSource) parcel.readSerializable();
    }
}
