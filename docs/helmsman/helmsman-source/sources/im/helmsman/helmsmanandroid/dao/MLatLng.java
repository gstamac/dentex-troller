package im.helmsman.helmsmanandroid.dao;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.maps.model.LatLng;
import im.helmsman.helmsmanandroid.utils.CoordinateTransform;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class MLatLng implements Parcelable {
    public static final Parcelable.Creator<MLatLng> CREATOR = new Parcelable.Creator<MLatLng>() { // from class: im.helmsman.helmsmanandroid.dao.MLatLng.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public MLatLng createFromParcel(Parcel parcel) {
            return new MLatLng(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public MLatLng[] newArray(int i) {
            return new MLatLng[i];
        }
    };
    public final float latitude;
    public final float longitude;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public MLatLng(float f, float f2) {
        this.latitude = f;
        this.longitude = f2;
    }

    public MLatLng(LatLng latLng) {
        this.latitude = (float) latLng.latitude;
        this.longitude = (float) latLng.longitude;
    }

    public MLatLng(com.amap.api.maps.model.LatLng latLng) {
        this.latitude = (float) latLng.latitude;
        this.longitude = (float) latLng.longitude;
    }

    protected MLatLng(Parcel parcel) {
        this.latitude = parcel.readFloat();
        this.longitude = parcel.readFloat();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeFloat(this.latitude);
        parcel.writeFloat(this.longitude);
    }

    public LatLng toGoogleLatlng() {
        return new LatLng(this.latitude, this.longitude);
    }

    public com.amap.api.maps.model.LatLng toAmapLatlng() {
        return new com.amap.api.maps.model.LatLng(this.latitude, this.longitude);
    }

    public static List<com.amap.api.maps.model.LatLng> toAmapLatlngs(List<MLatLng> list) {
        ArrayList arrayList = new ArrayList();
        Iterator<MLatLng> it = list.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next().toAmapLatlng());
        }
        return arrayList;
    }

    public static List<LatLng> toGoogleLatlngs(List<MLatLng> list) {
        ArrayList arrayList = new ArrayList();
        Iterator<MLatLng> it = list.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next().toGoogleLatlng());
        }
        return arrayList;
    }

    public static MLatLng toMLatLng(LatLng latLng) {
        if (latLng == null) {
            return null;
        }
        return new MLatLng((float) latLng.latitude, (float) latLng.longitude);
    }

    public static MLatLng toMLatLng(com.amap.api.maps.model.LatLng latLng) {
        return new MLatLng((float) latLng.latitude, (float) latLng.longitude);
    }

    public static List<WayPoint> LntLngToWayPointList(List<MLatLng> list) {
        ArrayList arrayList = new ArrayList();
        for (MLatLng mLatLng : list) {
            WayPoint wayPoint = new WayPoint();
            wayPoint.setLatitude(mLatLng.latitude);
            wayPoint.setLongitude(mLatLng.longitude);
            arrayList.add(wayPoint);
        }
        return arrayList;
    }

    public MLatLng convertToGcj02() {
        return CoordinateTransform.Wgs84ToGcj02(this);
    }

    public static List<MLatLng> convertListToGcj02(List<MLatLng> list) {
        return CoordinateTransform.latLngWGS84ToGCJ02(list);
    }
}
