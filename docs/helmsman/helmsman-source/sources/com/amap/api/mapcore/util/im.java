package com.amap.api.mapcore.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.location.Location;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.SparseArray;
import androidx.core.view.MotionEventCompat;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.internal.ServerProtocol;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.Random;
import org.json.JSONObject;

/* JADX INFO: compiled from: Utils.java */
/* JADX INFO: loaded from: classes.dex */
public final class im {
    private static int c;
    private static String[] d;
    private static Hashtable<String, Long> e = new Hashtable<>();
    private static SparseArray<String> f = null;
    private static SimpleDateFormat g = null;
    private static String[] h = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"};
    static String a = null;
    static WifiManager b = null;

    public static double a(double d2) {
        return ((long) (d2 * 1000000.0d)) / 1000000.0d;
    }

    public static float a(float f2) {
        return (float) (((long) (((double) f2) * 100.0d)) / 100.0d);
    }

    public static float a(double[] dArr) {
        if (dArr.length != 4) {
            return 0.0f;
        }
        float[] fArr = new float[1];
        Location.distanceBetween(dArr[0], dArr[1], dArr[2], dArr[3], fArr);
        return fArr[0];
    }

    public static int a(int i) {
        return (i * 2) - 113;
    }

    public static int a(NetworkInfo networkInfo) {
        if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()) {
            return networkInfo.getType();
        }
        return -1;
    }

    public static long a() {
        return System.currentTimeMillis();
    }

    public static Object a(Context context, String str) {
        if (context == null) {
            return null;
        }
        try {
            return context.getApplicationContext().getSystemService(str);
        } catch (Throwable th) {
            ij.a(th, "Utils", "getServ");
            return null;
        }
    }

    public static boolean a(Context context) {
        if (context == null) {
            return false;
        }
        try {
            return c() < 17 ? b(context, "android.provider.Settings$System") : b(context, "android.provider.Settings$Global");
        } catch (Throwable unused) {
            return false;
        }
    }

    public static boolean a(String str) {
        return (TextUtils.isEmpty(str) || str.equals("00:00:00:00:00:00") || str.contains(" :")) ? false : true;
    }

    public static boolean a(JSONObject jSONObject, String str) {
        return fg.a(jSONObject, str);
    }

    public static byte[] a(int i, byte[] bArr) {
        if (bArr == null || bArr.length < 2) {
            bArr = new byte[2];
        }
        bArr[0] = (byte) (i & 255);
        bArr[1] = (byte) ((i & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8);
        return bArr;
    }

    public static byte[] a(long j) {
        byte[] bArr = new byte[8];
        for (int i = 0; i < 8; i++) {
            bArr[i] = (byte) ((j >> (i * 8)) & 255);
        }
        return bArr;
    }

    public static byte[] a(byte[] bArr) {
        try {
            return fg.b(bArr);
        } catch (Throwable th) {
            ij.a(th, "Utils", "gz");
            return null;
        }
    }

    public static String[] a(TelephonyManager telephonyManager) {
        int i;
        String[] strArr;
        String networkOperator = telephonyManager != null ? telephonyManager.getNetworkOperator() : null;
        String[] strArr2 = {AppEventsConstants.EVENT_PARAM_VALUE_NO, AppEventsConstants.EVENT_PARAM_VALUE_NO};
        if (!TextUtils.isEmpty(networkOperator) && TextUtils.isDigitsOnly(networkOperator) && networkOperator.length() > 4) {
            strArr2[0] = networkOperator.substring(0, 3);
            char[] charArray = networkOperator.substring(3).toCharArray();
            int i2 = 0;
            while (i2 < charArray.length && Character.isDigit(charArray[i2])) {
                i2++;
            }
            strArr2[1] = networkOperator.substring(3, i2 + 3);
        }
        try {
            i = Integer.parseInt(strArr2[0]);
        } catch (Throwable th) {
            ij.a(th, "Utils", "getMccMnc");
            i = 0;
        }
        if (i == 0) {
            strArr2[0] = AppEventsConstants.EVENT_PARAM_VALUE_NO;
        }
        if (strArr2[0].equals(AppEventsConstants.EVENT_PARAM_VALUE_NO) || strArr2[1].equals(AppEventsConstants.EVENT_PARAM_VALUE_NO)) {
            return (strArr2[0].equals(AppEventsConstants.EVENT_PARAM_VALUE_NO) && strArr2[1].equals(AppEventsConstants.EVENT_PARAM_VALUE_NO) && (strArr = d) != null) ? strArr : strArr2;
        }
        d = strArr2;
        return strArr2;
    }

    public static double b(double d2) {
        return ((long) (d2 * 100.0d)) / 100.0d;
    }

    public static long b() {
        return SystemClock.elapsedRealtime();
    }

    public static String b(int i) {
        switch (i) {
            case 0:
                return GraphResponse.SUCCESS_KEY;
            case 1:
                return "重要参数为空";
            case 2:
                return "WIFI信息不足";
            case 3:
                return "请求参数获取出现异常";
            case 4:
                return "网络连接异常";
            case 5:
                return "解析数据异常";
            case 6:
                return "定位结果错误";
            case 7:
                return "KEY错误";
            case 8:
            case 16:
            case 17:
            default:
                return "其他错误";
            case 9:
                return "初始化异常";
            case 10:
                return "定位服务启动失败";
            case 11:
                return "错误的基站信息，请检查是否插入SIM卡";
            case 12:
                return "缺少定位权限";
            case 13:
                return "网络定位失败，请检查设备是否插入sim卡，是否开启移动网络或开启了wifi模块";
            case 14:
                return "GPS 定位失败，由于设备当前 GPS 状态差,建议持设备到相对开阔的露天场所再次尝试";
            case 15:
                return "当前返回位置为模拟软件返回，请关闭模拟软件，或者在option中设置允许模拟";
            case 18:
                return "定位失败，飞行模式下关闭了WIFI开关，请关闭飞行模式或者打开WIFI开关";
            case 19:
                return "定位失败，没有检查到SIM卡，并且关闭了WIFI开关，请打开WIFI开关或者插入SIM卡";
        }
    }

    public static String b(Context context) {
        PackageInfo packageInfo;
        if (!TextUtils.isEmpty(ij.g)) {
            return ij.g;
        }
        if (context == null) {
            return null;
        }
        try {
            packageInfo = context.getPackageManager().getPackageInfo(ew.c(context), 64);
        } catch (Throwable th) {
            ij.a(th, "Utils", "getAppName part");
            packageInfo = null;
        }
        try {
            if (TextUtils.isEmpty(ij.h)) {
                ij.h = null;
            }
        } catch (Throwable th2) {
            ij.a(th2, "Utils", "getAppName");
        }
        StringBuilder sb = new StringBuilder();
        if (packageInfo != null) {
            CharSequence charSequenceLoadLabel = packageInfo.applicationInfo != null ? packageInfo.applicationInfo.loadLabel(context.getPackageManager()) : null;
            if (charSequenceLoadLabel != null) {
                sb.append(charSequenceLoadLabel.toString());
            }
            if (!TextUtils.isEmpty(packageInfo.versionName)) {
                sb.append(packageInfo.versionName);
            }
        }
        String strC = ew.c(context);
        if (!TextUtils.isEmpty(strC)) {
            sb.append(",").append(strC);
        }
        if (!TextUtils.isEmpty(ij.h)) {
            sb.append(",").append(ij.h);
        }
        String string = sb.toString();
        ij.g = string;
        return string;
    }

    public static String b(TelephonyManager telephonyManager) {
        if (f == null) {
            SparseArray<String> sparseArray = new SparseArray<>();
            f = sparseArray;
            sparseArray.append(0, "UNKWN");
            f.append(1, "GPRS");
            f.append(2, "EDGE");
            f.append(3, "UMTS");
            f.append(4, "CDMA");
            f.append(5, "EVDO_0");
            f.append(6, "EVDO_A");
            f.append(7, "1xRTT");
            f.append(8, "HSDPA");
            f.append(9, "HSUPA");
            f.append(10, "HSPA");
            f.append(11, "IDEN");
            f.append(12, "EVDO_B");
            f.append(13, "LTE");
            f.append(14, "EHRPD");
            f.append(15, "HSPAP");
        }
        return f.get(telephonyManager != null ? telephonyManager.getNetworkType() : 0, "UNKWN");
    }

    private static boolean b(Context context, String str) throws Throwable {
        return ((Integer) ik.a(str, "getInt", new Object[]{context.getContentResolver(), ((String) ik.a(str, "AIRPLANE_MODE_ON")).toString()}, (Class<?>[]) new Class[]{ContentResolver.class, String.class})).intValue() == 1;
    }

    public static byte[] b(int i, byte[] bArr) {
        if (bArr == null || bArr.length < 4) {
            bArr = new byte[4];
        }
        for (int i2 = 0; i2 < bArr.length; i2++) {
            bArr[i2] = (byte) ((i >> (i2 * 8)) & 255);
        }
        return bArr;
    }

    public static byte[] b(String str) {
        return a(Integer.parseInt(str), (byte[]) null);
    }

    public static int c() {
        int i = c;
        if (i > 0) {
            return i;
        }
        try {
            try {
                return ik.b("android.os.Build$VERSION", "SDK_INT");
            } catch (Throwable unused) {
                return 0;
            }
        } catch (Throwable unused2) {
            return Integer.parseInt(ik.a("android.os.Build$VERSION", "SDK").toString());
        }
    }

    public static boolean c(Context context) {
        boolean zIsWifiEnabled;
        if (context == null) {
            return true;
        }
        if (b == null) {
            b = (WifiManager) a(context, "wifi");
        }
        try {
            zIsWifiEnabled = b.isWifiEnabled();
        } catch (Throwable unused) {
            zIsWifiEnabled = false;
        }
        if (zIsWifiEnabled || c() <= 17) {
            return zIsWifiEnabled;
        }
        try {
            return String.valueOf(ik.a(b, "isScanAlwaysAvailable", new Object[0])).equals(ServerProtocol.DIALOG_RETURN_SCOPES_TRUE);
        } catch (Throwable unused2) {
            return zIsWifiEnabled;
        }
    }

    public static byte[] c(String str) {
        return b(Integer.parseInt(str), (byte[]) null);
    }

    public static String d() {
        return Build.MODEL;
    }

    public static String e() {
        return Build.VERSION.RELEASE;
    }

    public static int f() {
        return new Random().nextInt(65536) - 32768;
    }
}
