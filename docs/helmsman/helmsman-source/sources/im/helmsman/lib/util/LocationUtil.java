package im.helmsman.lib.util;

import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import androidx.core.app.ActivityCompat;
import com.facebook.places.model.PlaceFields;
import im.helmsman.lib.HelmsmanSDK;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class LocationUtil {
    /* JADX WARN: Code duplicated, block: B:13:0x0041  */
    /* JADX WARN: Code duplicated, block: B:15:0x00a2  */
    /* JADX WARN: Code duplicated, block: B:7:0x0023 A[PHI: r3
  0x0023: PHI (r3v1 java.lang.String) = (r3v0 java.lang.String), (r3v7 java.lang.String) binds: [B:3:0x0018, B:6:0x0021] A[DONT_GENERATE, DONT_INLINE]] */
    public static double[] getPosition() {
        Location lastKnownLocation;
        LocationManager locationManager = (LocationManager) HelmsmanSDK.getContext().getSystemService(PlaceFields.LOCATION);
        List<String> providers = locationManager.getProviders(true);
        String str = "gps";
        if (!providers.contains("gps")) {
            str = "network";
            if (providers.contains("network")) {
                if (ActivityCompat.checkSelfPermission(HelmsmanSDK.getContext(), "android.permission.ACCESS_FINE_LOCATION") != 0) {
                    lastKnownLocation = locationManager.getLastKnownLocation(str);
                    if (lastKnownLocation != null) {
                        Log.e("lat", lastKnownLocation.getLatitude() + "");
                        Log.e("lon", lastKnownLocation.getLongitude() + "");
                        Log.e("alt", lastKnownLocation.getAltitude() + "");
                        return new double[]{lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()};
                    }
                    Log.e("获取不到", "获取不到位置");
                }
            }
        } else if (ActivityCompat.checkSelfPermission(HelmsmanSDK.getContext(), "android.permission.ACCESS_FINE_LOCATION") != 0 && ActivityCompat.checkSelfPermission(HelmsmanSDK.getContext(), "android.permission.ACCESS_COARSE_LOCATION") != 0) {
            lastKnownLocation = locationManager.getLastKnownLocation(str);
            if (lastKnownLocation != null) {
                Log.e("lat", lastKnownLocation.getLatitude() + "");
                Log.e("lon", lastKnownLocation.getLongitude() + "");
                Log.e("alt", lastKnownLocation.getAltitude() + "");
                return new double[]{lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()};
            }
            Log.e("获取不到", "获取不到位置");
        }
        return null;
    }
}
