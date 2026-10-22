package im.helmsman.helmsmanandroid.utils;

import com.amap.api.maps.model.LatLng;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class WayPointPaser {
    public static LatLng googleToAmap(com.google.android.gms.maps.model.LatLng latLng) {
        return new LatLng(latLng.latitude, latLng.longitude);
    }

    public static List<LatLng> googleWayPointToAmap(List<com.google.android.gms.maps.model.LatLng> list) {
        ArrayList arrayList = new ArrayList();
        Iterator<com.google.android.gms.maps.model.LatLng> it = list.iterator();
        while (it.hasNext()) {
            arrayList.add(googleToAmap(it.next()));
        }
        return arrayList;
    }
}
