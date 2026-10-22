package com.amap.api.maps.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.amap.api.mapcore.util.dw;

/* JADX INFO: loaded from: classes.dex */
public final class LatLngBounds implements Parcelable {
    public static final LatLngBoundsCreator CREATOR = new LatLngBoundsCreator();
    private final int a;
    public final LatLng northeast;
    public final LatLng southwest;

    /* JADX INFO: Access modifiers changed from: private */
    public static double c(double d, double d2) {
        return ((d - d2) + 360.0d) % 360.0d;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static double d(double d, double d2) {
        return ((d2 - d) + 360.0d) % 360.0d;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    LatLngBounds(int i, LatLng latLng, LatLng latLng2) {
        boolean z;
        try {
            if (latLng == null) {
                throw new RuntimeRemoteException("null southwest");
            }
            if (latLng2 == null) {
                throw new RuntimeRemoteException("null northeast");
            }
            if (latLng2.latitude < latLng.latitude) {
                throw new RuntimeRemoteException("southern latitude exceeds northern latitude (" + latLng.latitude + " > " + latLng2.latitude + ")");
            }
            z = true;
            this.a = z ? i : 0;
            this.southwest = z ? latLng : null;
            this.northeast = z ? latLng2 : null;
        } catch (Throwable th) {
            Log.e("LatLngBounds", "the structure parameters are illegal!");
            th.printStackTrace();
            z = false;
        }
    }

    public LatLngBounds(LatLng latLng, LatLng latLng2) {
        this(1, latLng, latLng2);
    }

    int a() {
        return this.a;
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean contains(LatLng latLng) {
        if (latLng == null) {
            return false;
        }
        if (this.northeast != null && this.southwest != null) {
            return a(latLng.latitude) && b(latLng.longitude);
        }
        Log.e("LatLngBounds", "current LatLngBounds is invalid, please check the structure parameters are legal");
        return false;
    }

    public boolean contains(LatLngBounds latLngBounds) {
        return latLngBounds != null && contains(latLngBounds.southwest) && contains(latLngBounds.northeast);
    }

    public boolean intersects(LatLngBounds latLngBounds) {
        if (latLngBounds == null) {
            return false;
        }
        if (this.northeast != null && this.southwest != null) {
            return a(latLngBounds) || latLngBounds.a(this);
        }
        Log.e("LatLngBounds", "current LatLngBounds is invalid, please check the structure parameters are legal");
        return false;
    }

    private boolean a(LatLngBounds latLngBounds) {
        LatLng latLng;
        if (latLngBounds == null || (latLng = latLngBounds.northeast) == null || latLngBounds.southwest == null) {
            return false;
        }
        return Math.abs(((latLng.longitude + latLngBounds.southwest.longitude) - this.northeast.longitude) - this.southwest.longitude) < ((this.northeast.longitude - this.southwest.longitude) + latLngBounds.northeast.longitude) - this.southwest.longitude && Math.abs(((latLngBounds.northeast.latitude + latLngBounds.southwest.latitude) - this.northeast.latitude) - this.southwest.latitude) < ((this.northeast.latitude - this.southwest.latitude) + latLngBounds.northeast.latitude) - latLngBounds.southwest.latitude;
    }

    public LatLngBounds including(LatLng latLng) {
        LatLng latLng2;
        double d;
        if (latLng == null) {
            return this;
        }
        if (this.northeast == null || (latLng2 = this.southwest) == null) {
            Log.e("LatLngBounds", "current LatLngBounds is invalid, please check the structure parameters are legal");
            return this;
        }
        double dMin = Math.min(latLng2.latitude, latLng.latitude);
        double dMax = Math.max(this.northeast.latitude, latLng.latitude);
        double d2 = this.northeast.longitude;
        double d3 = this.southwest.longitude;
        double d4 = latLng.longitude;
        if (b(d4)) {
            d = d2;
        } else if (c(d3, d4) < d(d2, d4)) {
            d3 = d4;
            d = d2;
        } else {
            d = d4;
        }
        try {
            return new LatLngBounds(new LatLng(dMin, d3, false), new LatLng(dMax, d, false));
        } catch (Throwable th) {
            th.printStackTrace();
            return this;
        }
    }

    private boolean a(double d) {
        return this.southwest.latitude <= d && d <= this.northeast.latitude;
    }

    private boolean b(double d) {
        if (this.southwest.longitude <= this.northeast.longitude) {
            return this.southwest.longitude <= d && d <= this.northeast.longitude;
        }
        return this.southwest.longitude <= d || d <= this.northeast.longitude;
    }

    public int hashCode() {
        return dw.a(new Object[]{this.southwest, this.northeast});
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof LatLngBounds)) {
            return false;
        }
        LatLngBounds latLngBounds = (LatLngBounds) obj;
        return this.southwest.equals(latLngBounds.southwest) && this.northeast.equals(latLngBounds.northeast);
    }

    public String toString() {
        return dw.a(dw.a("southwest", this.southwest), dw.a("northeast", this.northeast));
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        LatLngBoundsCreator.a(this, parcel, i);
    }

    public static final class Builder {
        private double a = Double.POSITIVE_INFINITY;
        private double b = Double.NEGATIVE_INFINITY;
        private double c = Double.NaN;
        private double d = Double.NaN;

        public Builder include(LatLng latLng) {
            if (latLng == null) {
                return this;
            }
            this.a = Math.min(this.a, latLng.latitude);
            this.b = Math.max(this.b, latLng.latitude);
            double d = latLng.longitude;
            if (Double.isNaN(this.c)) {
                this.c = d;
                this.d = d;
            } else if (!a(d)) {
                if (LatLngBounds.c(this.c, d) < LatLngBounds.d(this.d, d)) {
                    this.c = d;
                } else {
                    this.d = d;
                }
            }
            return this;
        }

        private boolean a(double d) {
            double d2 = this.c;
            double d3 = this.d;
            if (d2 <= d3) {
                return d2 <= d && d <= d3;
            }
            return d2 <= d || d <= d3;
        }

        public LatLngBounds build() {
            if (Double.isNaN(this.c)) {
                Log.w("LatLngBounds", "no included points");
                return null;
            }
            double d = this.c;
            double d2 = this.d;
            if (d > d2) {
                this.c = d2;
                this.d = d;
            }
            double d3 = this.a;
            double d4 = this.b;
            if (d3 > d4) {
                this.a = d4;
                this.b = d3;
            }
            return new LatLngBounds(new LatLng(this.a, this.c, false), new LatLng(this.b, this.d, false));
        }
    }
}
