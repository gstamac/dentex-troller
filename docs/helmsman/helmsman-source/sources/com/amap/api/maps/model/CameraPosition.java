package com.amap.api.maps.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import com.amap.api.mapcore.util.dr;
import com.amap.api.mapcore.util.dw;
import com.amap.api.mapcore.util.fn;

/* JADX INFO: loaded from: classes.dex */
public final class CameraPosition implements Parcelable {
    public static final CameraPositionCreator CREATOR = new CameraPositionCreator();
    public final float bearing;
    public final boolean isAbroad;
    public final LatLng target;
    public final float tilt;
    public final float zoom;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public CameraPosition(LatLng latLng, float f, float f2, float f3) {
        if (latLng == null) {
            Log.w("CameraPosition", "构建CameraPosition时,位置(target)不能为null");
        }
        this.target = latLng;
        this.zoom = f;
        this.tilt = f2;
        this.bearing = (((double) f3) <= 0.0d ? (f3 % 360.0f) + 360.0f : f3) % 360.0f;
        if (latLng != null) {
            this.isAbroad = !dr.a(latLng.latitude, latLng.longitude);
        } else {
            this.isAbroad = false;
        }
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeFloat(this.bearing);
        parcel.writeFloat((float) this.target.latitude);
        parcel.writeFloat((float) this.target.longitude);
        parcel.writeFloat(this.tilt);
        parcel.writeFloat(this.zoom);
    }

    public int hashCode() {
        return super.hashCode();
    }

    public static final CameraPosition fromLatLngZoom(LatLng latLng, float f) {
        return new CameraPosition(latLng, f, 0.0f, 0.0f);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(CameraPosition cameraPosition) {
        return new Builder(cameraPosition);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CameraPosition)) {
            return false;
        }
        CameraPosition cameraPosition = (CameraPosition) obj;
        return this.target.equals(cameraPosition.target) && Float.floatToIntBits(this.zoom) == Float.floatToIntBits(cameraPosition.zoom) && Float.floatToIntBits(this.tilt) == Float.floatToIntBits(cameraPosition.tilt) && Float.floatToIntBits(this.bearing) == Float.floatToIntBits(cameraPosition.bearing);
    }

    public String toString() {
        return dw.a(dw.a(TypedValues.AttributesType.S_TARGET, this.target), dw.a("zoom", Float.valueOf(this.zoom)), dw.a("tilt", Float.valueOf(this.tilt)), dw.a("bearing", Float.valueOf(this.bearing)));
    }

    public static final class Builder {
        private LatLng a;
        private float b;
        private float c;
        private float d;

        public Builder() {
        }

        public Builder(CameraPosition cameraPosition) {
            target(cameraPosition.target).bearing(cameraPosition.bearing).tilt(cameraPosition.tilt).zoom(cameraPosition.zoom);
        }

        public Builder target(LatLng latLng) {
            this.a = latLng;
            return this;
        }

        public Builder zoom(float f) {
            this.b = f;
            return this;
        }

        public Builder tilt(float f) {
            this.c = f;
            return this;
        }

        public Builder bearing(float f) {
            this.d = f;
            return this;
        }

        public CameraPosition build() {
            try {
                if (this.a == null) {
                    Log.w("CameraPosition", "target is null");
                    return null;
                }
                return new CameraPosition(this.a, this.b, this.c, this.d);
            } catch (Throwable th) {
                fn.b(th, "CameraPosition", "build");
                return null;
            }
        }
    }
}
