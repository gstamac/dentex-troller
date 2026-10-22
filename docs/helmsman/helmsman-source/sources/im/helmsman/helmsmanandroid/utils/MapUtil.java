package im.helmsman.helmsmanandroid.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;
import com.amap.api.maps.AMap;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import im.helmsman.helmsmanandroid.config.Config;
import im.helmsman.helmsmanandroid.dao.MLatLng;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class MapUtil {
    public static void UpdateMapCenter(GoogleMap googleMap, double d, double d2) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(d, d2), 13.0f));
    }

    public static void UpdateMapCenter(GoogleMap googleMap, LatLng latLng) {
        UpdateMapCenter(googleMap, latLng.latitude, latLng.longitude);
    }

    public static void moveToCenter(AMap aMap, GoogleMap googleMap, List<MLatLng> list) {
        MLatLng mLatLngFindNorthEastLatLng = findNorthEastLatLng(list);
        MLatLng mLatLngFindSouthWestLatLng = findSouthWestLatLng(list);
        int i = Config.MAP_TYPE;
        if (i == 1) {
            LatLngBounds latLngBounds = new LatLngBounds(mLatLngFindSouthWestLatLng.toGoogleLatlng(), mLatLngFindNorthEastLatLng.toGoogleLatlng());
            for (int i2 = 2; i2 < list.size(); i2++) {
                latLngBounds.including(list.get(i2).toGoogleLatlng());
            }
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 300));
            return;
        }
        if (i != 2) {
            return;
        }
        com.amap.api.maps.model.LatLngBounds latLngBounds2 = new com.amap.api.maps.model.LatLngBounds(mLatLngFindSouthWestLatLng.toAmapLatlng(), mLatLngFindNorthEastLatLng.toAmapLatlng());
        Iterator<MLatLng> it = list.iterator();
        while (it.hasNext()) {
            latLngBounds2.including(it.next().toAmapLatlng());
        }
        aMap.moveCamera(com.amap.api.maps.CameraUpdateFactory.newLatLngBounds(latLngBounds2, 300));
    }

    public static void checkGooglePlayServices(Context context) {
        int iIsGooglePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (iIsGooglePlayServicesAvailable == 0) {
            Toast.makeText(context.getApplicationContext(), "SUCCESS", 0).show();
            return;
        }
        if (iIsGooglePlayServicesAvailable == 1) {
            Toast.makeText(context.getApplicationContext(), "SERVICE_MISSING", 0).show();
            GooglePlayServicesUtil.getErrorDialog(1, (Activity) context, 0).show();
            return;
        }
        if (iIsGooglePlayServicesAvailable == 2) {
            Toast.makeText(context.getApplicationContext(), "SERVICE_VERSION_UPDATE_REQUIRED", 0).show();
            GooglePlayServicesUtil.getErrorDialog(2, (Activity) context, 0).show();
        } else if (iIsGooglePlayServicesAvailable == 3) {
            Toast.makeText(context.getApplicationContext(), "SERVICE_DISABLED", 0).show();
            GooglePlayServicesUtil.getErrorDialog(3, (Activity) context, 0).show();
        } else {
            if (iIsGooglePlayServicesAvailable != 9) {
                return;
            }
            Toast.makeText(context.getApplicationContext(), "SERVICE_INVALID", 0).show();
            GooglePlayServicesUtil.getErrorDialog(9, (Activity) context, 0).show();
        }
    }

    public static LatLngBounds buildLatLngBound(List<Marker> list) {
        if (list.size() < 2) {
            return null;
        }
        LatLngBounds latLngBounds = new LatLngBounds(findSouthWestMarker(list), findNorthEastMarker(list));
        for (int i = 2; i < list.size(); i++) {
            latLngBounds.including(list.get(i).getPosition());
        }
        return latLngBounds;
    }

    public static LatLng findSouthWestMarker(List<Marker> list) {
        double d = list.get(0).getPosition().latitude;
        double d2 = list.get(0).getPosition().longitude;
        for (Marker marker : list) {
            if (marker.getPosition().latitude < d) {
                d = marker.getPosition().latitude;
            }
            if (marker.getPosition().longitude < d2) {
                d2 = marker.getPosition().longitude;
            }
        }
        return new LatLng(d, d2);
    }

    public static LatLng findNorthEastMarker(List<Marker> list) {
        double d = list.get(0).getPosition().latitude;
        double d2 = list.get(0).getPosition().longitude;
        for (Marker marker : list) {
            if (marker.getPosition().latitude > d) {
                d = marker.getPosition().latitude;
            }
            if (marker.getPosition().longitude > d2) {
                d2 = marker.getPosition().longitude;
            }
        }
        return new LatLng(d, d2);
    }

    public static MLatLng findNorthEastLatLng(List<MLatLng> list) {
        if (list.size() <= 0) {
            return new MLatLng(0.0f, 0.0f);
        }
        double d = list.get(0).latitude;
        double d2 = list.get(0).longitude;
        for (MLatLng mLatLng : list) {
            if (mLatLng.latitude > d) {
                d = mLatLng.latitude;
            }
            if (mLatLng.longitude > d2) {
                d2 = mLatLng.longitude;
            }
        }
        return new MLatLng((float) d, (float) d2);
    }

    public static MLatLng findSouthWestLatLng(List<MLatLng> list) {
        if (list.size() <= 0) {
            return new MLatLng(0.0f, 0.0f);
        }
        double d = list.get(0).latitude;
        double d2 = list.get(0).longitude;
        for (MLatLng mLatLng : list) {
            if (mLatLng.latitude < d) {
                d = mLatLng.latitude;
            }
            if (mLatLng.longitude < d2) {
                d2 = mLatLng.longitude;
            }
        }
        return new MLatLng((float) d, (float) d2);
    }
}
