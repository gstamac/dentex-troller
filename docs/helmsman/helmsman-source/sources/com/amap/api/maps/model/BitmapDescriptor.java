package com.amap.api.maps.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public final class BitmapDescriptor implements Parcelable, Cloneable {
    public static final BitmapDescriptorCreator CREATOR = new BitmapDescriptorCreator();
    int a;
    int b;
    Bitmap c;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    BitmapDescriptor(Bitmap bitmap) {
        this.a = 0;
        this.b = 0;
        if (bitmap != null) {
            this.a = bitmap.getWidth();
            this.b = bitmap.getHeight();
            if (bitmap.getConfig() == null) {
                this.c = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            } else {
                this.c = bitmap.copy(bitmap.getConfig(), true);
            }
        }
    }

    private BitmapDescriptor(Bitmap bitmap, int i, int i2) {
        this.a = i;
        this.b = i2;
        this.c = bitmap;
    }

    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public BitmapDescriptor m42clone() {
        try {
            Bitmap bitmap = this.c;
            return new BitmapDescriptor(bitmap.copy(bitmap.getConfig(), true), this.a, this.b);
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    public Bitmap getBitmap() {
        return this.c;
    }

    public int getWidth() {
        return this.a;
    }

    public int getHeight() {
        return this.b;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.c, i);
        parcel.writeInt(this.a);
        parcel.writeInt(this.b);
    }

    public void recycle() {
        Bitmap bitmap = this.c;
        if (bitmap == null || bitmap.isRecycled()) {
            return;
        }
        this.c.recycle();
        this.c = null;
    }

    public boolean equals(Object obj) {
        BitmapDescriptor bitmapDescriptor;
        Bitmap bitmap;
        Bitmap bitmap2 = this.c;
        if (bitmap2 == null || bitmap2.isRecycled() || obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (getClass() == obj.getClass() && (bitmap = (bitmapDescriptor = (BitmapDescriptor) obj).c) != null && !bitmap.isRecycled() && this.a == bitmapDescriptor.getWidth() && this.b == bitmapDescriptor.getHeight()) {
            try {
                return this.c.sameAs(bitmapDescriptor.c);
            } catch (Throwable unused) {
            }
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }
}
