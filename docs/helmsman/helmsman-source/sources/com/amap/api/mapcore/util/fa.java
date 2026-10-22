package com.amap.api.mapcore.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Xml;
import android.view.WindowManager;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.places.model.PlaceFields;
import com.qiniu.android.common.Constants;
import java.io.File;
import java.io.FileInputStream;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import kotlin.UByte;
import org.xmlpull.v1.XmlPullParser;

/* JADX INFO: compiled from: DeviceInfo.java */
/* JADX INFO: loaded from: classes.dex */
public class fa {
    private static String a = "";
    private static boolean b = false;
    private static String c = null;
    private static String d = "";
    private static String e = "";
    private static String f = "";
    private static String g = "";
    private static String h = "";
    private static String i = "";

    public static String a() {
        return c;
    }

    public static String a(Context context) {
        try {
            return v(context);
        } catch (Throwable th) {
            th.printStackTrace();
            return "";
        }
    }

    public static String b(Context context) {
        try {
            return y(context);
        } catch (Throwable th) {
            th.printStackTrace();
            return "";
        }
    }

    public static int c(Context context) {
        try {
            return z(context);
        } catch (Throwable th) {
            th.printStackTrace();
            return -1;
        }
    }

    public static int d(Context context) {
        try {
            return w(context);
        } catch (Throwable th) {
            th.printStackTrace();
            return -1;
        }
    }

    public static String e(Context context) {
        try {
            return u(context);
        } catch (Throwable th) {
            th.printStackTrace();
            return "";
        }
    }

    public static void b() {
        try {
            TrafficStats.class.getDeclaredMethod("setThreadStatsTag", Integer.TYPE).invoke(null, 40964);
        } catch (Throwable th) {
            fk.a(th, "DeviceInfo", "setTraficTag");
        }
    }

    private static String t(Context context) {
        FileInputStream fileInputStream = null;
        try {
            if (fg.a(context, "android.permission.READ_EXTERNAL_STORAGE") && "mounted".equals(Environment.getExternalStorageState())) {
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/.UTSystemConfig/Global/Alvin2.xml");
                XmlPullParser xmlPullParserNewPullParser = Xml.newPullParser();
                FileInputStream fileInputStream2 = new FileInputStream(file);
                try {
                    xmlPullParserNewPullParser.setInput(fileInputStream2, Constants.UTF_8);
                    boolean z = false;
                    for (int eventType = xmlPullParserNewPullParser.getEventType(); 1 != eventType; eventType = xmlPullParserNewPullParser.next()) {
                        if (eventType != 2) {
                            if (eventType == 3) {
                                z = false;
                            } else if (eventType == 4 && z) {
                                String text = xmlPullParserNewPullParser.getText();
                                try {
                                    fileInputStream2.close();
                                } catch (Throwable unused) {
                                }
                                return text;
                            }
                        } else if (xmlPullParserNewPullParser.getAttributeCount() > 0) {
                            int attributeCount = xmlPullParserNewPullParser.getAttributeCount();
                            for (int i2 = 0; i2 < attributeCount; i2++) {
                                String attributeValue = xmlPullParserNewPullParser.getAttributeValue(i2);
                                if ("UTDID2".equals(attributeValue) || "UTDID".equals(attributeValue)) {
                                    z = true;
                                }
                            }
                        }
                    }
                    fileInputStream = fileInputStream2;
                } catch (Throwable unused2) {
                    fileInputStream = fileInputStream2;
                    if (fileInputStream == null) {
                        return "";
                    }
                }
            }
            if (fileInputStream == null) {
                return "";
            }
        } catch (Throwable unused3) {
        }
        try {
            fileInputStream.close();
            return "";
        } catch (Throwable unused4) {
            return "";
        }
    }

    public static String f(Context context) {
        try {
            String str = a;
            if (str != null && !"".equals(str)) {
                return a;
            }
            if (a(context, "android.permission.WRITE_SETTINGS")) {
                a = Settings.System.getString(context.getContentResolver(), "mqBRboGZkQPcAkyk");
            }
            String str2 = a;
            if (str2 != null && !"".equals(str2)) {
                return a;
            }
            try {
                a = t(context);
            } catch (Throwable unused) {
            }
            String str3 = a;
            return str3 == null ? "" : str3;
        } catch (Throwable unused2) {
        }
    }

    public static String c() {
        if (!TextUtils.isEmpty(e)) {
            return e;
        }
        try {
            e = Build.SERIAL;
        } catch (Throwable unused) {
        }
        String str = e;
        return str == null ? "" : str;
    }

    private static boolean a(Context context, String str) {
        return context != null && context.checkCallingOrSelfPermission(str) == 0;
    }

    static String g(Context context) {
        WifiManager wifiManager;
        if (context == null) {
            return "";
        }
        try {
            return (a(context, "android.permission.ACCESS_WIFI_STATE") && (wifiManager = (WifiManager) context.getSystemService("wifi")) != null && wifiManager.isWifiEnabled()) ? wifiManager.getConnectionInfo().getBSSID() : "";
        } catch (Throwable th) {
            fk.a(th, "DeviceInfo", "getWifiMacs");
            return "";
        }
    }

    static String h(Context context) {
        StringBuilder sb = new StringBuilder();
        if (context != null) {
            try {
                if (a(context, "android.permission.ACCESS_WIFI_STATE")) {
                    WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
                    if (wifiManager == null) {
                        return "";
                    }
                    if (wifiManager.isWifiEnabled()) {
                        List<ScanResult> scanResults = wifiManager.getScanResults();
                        if (scanResults != null && scanResults.size() != 0) {
                            List<ScanResult> listA = a(scanResults);
                            boolean z = true;
                            for (int i2 = 0; i2 < listA.size() && i2 < 7; i2++) {
                                ScanResult scanResult = listA.get(i2);
                                if (z) {
                                    z = false;
                                } else {
                                    sb.append(";");
                                }
                                sb.append(scanResult.BSSID);
                            }
                        }
                        return sb.toString();
                    }
                    return sb.toString();
                }
            } catch (Throwable th) {
                fk.a(th, "DeviceInfo", "getWifiMacs");
            }
        }
        return sb.toString();
    }

    public static String i(Context context) {
        try {
            String str = f;
            if (str != null && !"".equals(str)) {
                return f;
            }
            if (!a(context, "android.permission.ACCESS_WIFI_STATE")) {
                return f;
            }
            WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
            if (wifiManager == null) {
                return "";
            }
            String macAddress = wifiManager.getConnectionInfo().getMacAddress();
            f = macAddress;
            if ("02:00:00:00:00:00".equals(macAddress) || "00:00:00:00:00:00".equals(f)) {
                f = d();
            }
            return f;
        } catch (Throwable th) {
            fk.a(th, "DeviceInfo", "getDeviceMac");
        }
    }

    private static String d() {
        try {
            for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (networkInterface.getName().equalsIgnoreCase("wlan0")) {
                    byte[] hardwareAddress = networkInterface.getHardwareAddress();
                    if (hardwareAddress == null) {
                        return "";
                    }
                    StringBuilder sb = new StringBuilder();
                    for (byte b2 : hardwareAddress) {
                        String upperCase = Integer.toHexString(b2 & UByte.MAX_VALUE).toUpperCase();
                        if (upperCase.length() == 1) {
                            sb.append(AppEventsConstants.EVENT_PARAM_VALUE_NO);
                        }
                        sb.append(upperCase).append(":");
                    }
                    if (sb.length() > 0) {
                        sb.deleteCharAt(sb.length() - 1);
                    }
                    return sb.toString();
                }
            }
        } catch (Exception e2) {
            fk.a(e2, "DeviceInfo", "getMacAddr");
        }
        return "";
    }

    static String[] j(Context context) {
        try {
            if (a(context, "android.permission.READ_PHONE_STATE") && a(context, "android.permission.ACCESS_COARSE_LOCATION")) {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(PlaceFields.PHONE);
                if (telephonyManager == null) {
                    return new String[]{"", ""};
                }
                CellLocation cellLocation = telephonyManager.getCellLocation();
                if (cellLocation instanceof GsmCellLocation) {
                    GsmCellLocation gsmCellLocation = (GsmCellLocation) cellLocation;
                    return new String[]{gsmCellLocation.getLac() + "||" + gsmCellLocation.getCid(), "gsm"};
                }
                if (cellLocation instanceof CdmaCellLocation) {
                    CdmaCellLocation cdmaCellLocation = (CdmaCellLocation) cellLocation;
                    return new String[]{cdmaCellLocation.getSystemId() + "||" + cdmaCellLocation.getNetworkId() + "||" + cdmaCellLocation.getBaseStationId(), "cdma"};
                }
                return new String[]{"", ""};
            }
            return new String[]{"", ""};
        } catch (Throwable th) {
            fk.a(th, "DeviceInfo", "cellInfo");
        }
    }

    static String k(Context context) {
        TelephonyManager telephonyManagerA;
        try {
            if (!a(context, "android.permission.READ_PHONE_STATE") || (telephonyManagerA = A(context)) == null) {
                return "";
            }
            String networkOperator = telephonyManagerA.getNetworkOperator();
            if (!TextUtils.isEmpty(networkOperator) && networkOperator.length() >= 3) {
                return networkOperator.substring(3);
            }
            return "";
        } catch (Throwable th) {
            fk.a(th, "DeviceInfo", "getMNC");
            return "";
        }
    }

    public static int l(Context context) {
        try {
            return z(context);
        } catch (Throwable th) {
            fk.a(th, "DeviceInfo", "getNetWorkType");
            return -1;
        }
    }

    public static int m(Context context) {
        try {
            return w(context);
        } catch (Throwable th) {
            fk.a(th, "DeviceInfo", "getActiveNetWorkType");
            return -1;
        }
    }

    public static NetworkInfo n(Context context) {
        ConnectivityManager connectivityManagerX;
        if (a(context, "android.permission.ACCESS_NETWORK_STATE") && (connectivityManagerX = x(context)) != null) {
            return connectivityManagerX.getActiveNetworkInfo();
        }
        return null;
    }

    static String o(Context context) {
        try {
            NetworkInfo networkInfoN = n(context);
            if (networkInfoN == null) {
                return null;
            }
            return networkInfoN.getExtraInfo();
        } catch (Throwable th) {
            fk.a(th, "DeviceInfo", "getNetworkExtraInfo");
            return null;
        }
    }

    static String p(Context context) {
        try {
            String str = g;
            if (str != null && !"".equals(str)) {
                return g;
            }
            DisplayMetrics displayMetrics = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) context.getSystemService("window");
            if (windowManager == null) {
                return "";
            }
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            int i2 = displayMetrics.widthPixels;
            int i3 = displayMetrics.heightPixels;
            g = (i3 > i2 ? new StringBuilder().append(i2).append("*").append(i3) : new StringBuilder().append(i3).append("*").append(i2)).toString();
            return g;
        } catch (Throwable th) {
            fk.a(th, "DeviceInfo", "getReslution");
        }
    }

    public static String q(Context context) {
        try {
            String str = h;
            if (str != null && !"".equals(str)) {
                return h;
            }
            if (!a(context, "android.permission.READ_PHONE_STATE")) {
                return h;
            }
            TelephonyManager telephonyManagerA = A(context);
            if (telephonyManagerA == null) {
                return "";
            }
            String deviceId = telephonyManagerA.getDeviceId();
            h = deviceId;
            if (deviceId == null) {
                h = "";
            }
            return h;
        } catch (Throwable th) {
            fk.a(th, "DeviceInfo", "getDeviceID");
        }
    }

    public static String r(Context context) {
        try {
            return u(context);
        } catch (Throwable th) {
            fk.a(th, "DeviceInfo", "getSubscriberId");
            return "";
        }
    }

    static String s(Context context) {
        try {
            return v(context);
        } catch (Throwable th) {
            fk.a(th, "DeviceInfo", "getNetworkOperatorName");
            return "";
        }
    }

    private static String u(Context context) {
        String str = i;
        if (str != null && !"".equals(str)) {
            return i;
        }
        if (!a(context, "android.permission.READ_PHONE_STATE")) {
            return i;
        }
        TelephonyManager telephonyManagerA = A(context);
        if (telephonyManagerA == null) {
            return "";
        }
        String subscriberId = telephonyManagerA.getSubscriberId();
        i = subscriberId;
        if (subscriberId == null) {
            i = "";
        }
        return i;
    }

    private static String v(Context context) {
        if (!a(context, "android.permission.READ_PHONE_STATE")) {
            return null;
        }
        TelephonyManager telephonyManagerA = A(context);
        if (telephonyManagerA == null) {
            return "";
        }
        String simOperatorName = telephonyManagerA.getSimOperatorName();
        return TextUtils.isEmpty(simOperatorName) ? telephonyManagerA.getNetworkOperatorName() : simOperatorName;
    }

    private static int w(Context context) {
        ConnectivityManager connectivityManagerX;
        NetworkInfo activeNetworkInfo;
        if (context == null || !a(context, "android.permission.ACCESS_NETWORK_STATE") || (connectivityManagerX = x(context)) == null || (activeNetworkInfo = connectivityManagerX.getActiveNetworkInfo()) == null) {
            return -1;
        }
        return activeNetworkInfo.getType();
    }

    private static ConnectivityManager x(Context context) {
        return (ConnectivityManager) context.getSystemService("connectivity");
    }

    private static String y(Context context) {
        String strR = r(context);
        return (strR == null || strR.length() < 5) ? "" : strR.substring(3, 5);
    }

    private static int z(Context context) {
        TelephonyManager telephonyManagerA;
        if (a(context, "android.permission.READ_PHONE_STATE") && (telephonyManagerA = A(context)) != null) {
            return telephonyManagerA.getNetworkType();
        }
        return -1;
    }

    private static TelephonyManager A(Context context) {
        return (TelephonyManager) context.getSystemService(PlaceFields.PHONE);
    }

    private static List<ScanResult> a(List<ScanResult> list) {
        int size = list.size();
        for (int i2 = 0; i2 < size - 1; i2++) {
            for (int i3 = 1; i3 < size - i2; i3++) {
                int i4 = i3 - 1;
                if (list.get(i4).level > list.get(i3).level) {
                    ScanResult scanResult = list.get(i4);
                    list.set(i4, list.get(i3));
                    list.set(i3, scanResult);
                }
            }
        }
        return list;
    }
}
