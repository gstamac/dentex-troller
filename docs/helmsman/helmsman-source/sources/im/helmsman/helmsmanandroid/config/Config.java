package im.helmsman.helmsmanandroid.config;

import android.content.pm.PackageManager;
import android.util.Log;
import im.helmsman.helmsmanandroid.BuildConfig;
import im.helmsman.helmsmanandroid.MyApplication;
import java.util.Map;
import javy.lib.jsave.JSave;

/* JADX INFO: loaded from: classes2.dex */
public class Config {
    public static int DISTANCE_UNIT = 0;
    public static final int FT_NM_DISTANCE_UNIT = 2;
    public static final int GAODE_MAP = 2;
    public static final int GOOGLE_MAP = 1;
    public static final int KPH_SPEED_UNIT = 0;
    public static int MAP_TYPE = 0;
    public static final int MPH_SPEED_UNIT = 1;
    public static final int M_KM_DISTANCE_UNIT = 0;
    public static final int M_NM_DISTANCE_UNIT = 1;
    public static int SPEED_UNIT = 0;
    public static final String WX_APPID = "wx6e8c1cff7f9272dd";
    public static final String WX_APPSECRET = "e5a111f85918b2e9e64ddbac919909f8";
    public static boolean isChineseCoordinateCorrection = false;
    public static boolean isUpLoadLog = true;
    public static boolean isVibration = true;

    public static void initConfig() {
        DISTANCE_UNIT = ((Integer) JSave.get("distance_unit", 0)).intValue();
        SPEED_UNIT = ((Integer) JSave.get("speed_unit", 0)).intValue();
        if (JSave.get("isVibration") == null) {
            isVibration = false;
        } else {
            isVibration = ((Boolean) JSave.get("isVibration")).booleanValue();
        }
        if (JSave.get("isChineseCoordinateCorrection") == null) {
            String applicationMetaData = getApplicationMetaData("CUSTOM_CHANNEL");
            Log.e("channel", applicationMetaData);
            if (applicationMetaData != null && applicationMetaData.equals(BuildConfig.FLAVOR)) {
                isChineseCoordinateCorrection = false;
            } else {
                isChineseCoordinateCorrection = true;
            }
        } else {
            isChineseCoordinateCorrection = ((Boolean) JSave.get("isChineseCoordinateCorrection")).booleanValue();
        }
        if (JSave.get("isUpLoadLog") == null) {
            isUpLoadLog = true;
        } else {
            isUpLoadLog = ((Boolean) JSave.get("isUpLoadLog")).booleanValue();
        }
        if (isChineseCoordinateCorrection) {
            MAP_TYPE = 2;
        } else {
            MAP_TYPE = 1;
        }
    }

    public static void saveConfig() {
        JSave.put("isVibration", Boolean.valueOf(isVibration));
        JSave.put("isChineseCoordinateCorrection", Boolean.valueOf(isChineseCoordinateCorrection));
        JSave.put("isUpLoadLog", Boolean.valueOf(isUpLoadLog));
        JSave.put("speed_unit", Integer.valueOf(SPEED_UNIT));
        JSave.put("distance_unit", Integer.valueOf(DISTANCE_UNIT));
    }

    public static Map<String, Boolean> getPushSetting() {
        return (Map) JSave.get("pushsetting");
    }

    public static void savePushSetting(Map<String, Boolean> map) {
        JSave.put("pushsetting", map);
    }

    public static String getApplicationMetaData(String str) {
        try {
            return MyApplication.getInstance().getPackageManager().getApplicationInfo(MyApplication.getInstance().getPackageName(), 128).metaData.getString(str);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
