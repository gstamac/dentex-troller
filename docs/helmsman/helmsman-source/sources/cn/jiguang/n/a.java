package cn.jiguang.n;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Point;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import cn.jiguang.l.h;
import cn.jpush.android.api.JThirdPlatFormInterface;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileReader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public final class a {
    private static String a = "";
    private static String b = "";
    private static final FileFilter c = new b();

    private static int a(String str, FileInputStream fileInputStream) {
        byte[] bArr = new byte[1024];
        try {
            int i = fileInputStream.read(bArr);
            int i2 = 0;
            while (i2 < i) {
                byte b2 = bArr[i2];
                if (b2 == 10 || i2 == 0) {
                    if (b2 == 10) {
                        i2++;
                    }
                    int i3 = i2;
                    while (i3 < i) {
                        int i4 = i3 - i2;
                        if (bArr[i3] != str.charAt(i4)) {
                            break;
                        }
                        if (i4 == str.length() - 1) {
                            while (i3 < 1024) {
                                byte b3 = bArr[i3];
                                if (b3 == 10) {
                                    break;
                                }
                                if (b3 >= 48 && b3 <= 57) {
                                    int i5 = i3 + 1;
                                    while (i5 < 1024) {
                                        byte b4 = bArr[i5];
                                        if (b4 < 48 || b4 > 57) {
                                            break;
                                        }
                                        i5++;
                                    }
                                    return Integer.parseInt(new String(bArr, 0, i3, i5 - i3));
                                }
                                i3++;
                            }
                            return -1;
                        }
                        i3++;
                    }
                }
                i2++;
            }
        } catch (Throwable unused) {
        }
        return -1;
    }

    private static long a(String str) {
        String line;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("/proc/meminfo"), 4096);
            do {
                line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
            } while (!line.contains(str));
            bufferedReader.close();
            return Integer.valueOf(line.split("\\s+")[1]).intValue();
        } catch (Throwable unused) {
            return -1L;
        }
    }

    public static String a() {
        if (!TextUtils.isEmpty(a)) {
            return a;
        }
        g();
        return a;
    }

    public static String a(Context context) {
        DisplayMetrics displayMetrics;
        if (context == null || context.getResources() == null || (displayMetrics = context.getResources().getDisplayMetrics()) == null) {
            return "0*0";
        }
        return displayMetrics.widthPixels + "*" + displayMetrics.heightPixels;
    }

    public static String a(Context context, int i) {
        String str;
        if (i != 53) {
            return null;
        }
        try {
            String str2 = (String) cn.jiguang.g.b.b(context, new cn.jiguang.g.a("cn.jpush.preferences.v2", "n_udp_report_device_info", ""));
            if (TextUtils.isEmpty(str2)) {
                str = (String) cn.jiguang.g.b.b(context, new cn.jiguang.g.a("cn.jpush.preferences.v2", "udp_report_device_info", ""));
                if (!TextUtils.isEmpty(str)) {
                    a(context, str);
                }
            } else {
                str = new String(Base64.decode(str2, 2));
            }
            cn.jiguang.w.a.a("JDeviceHelper", "read deviceinfo:" + str);
            if (TextUtils.isEmpty(str)) {
                str = d(context);
            }
            JSONObject jSONObject = new JSONObject(str);
            JSONObject jSONObject2 = new JSONObject();
            cn.jiguang.aj.b.a();
            jSONObject2.put("rom_type", (int) cn.jiguang.aj.b.a(context));
            cn.jiguang.aj.b.a();
            jSONObject2.put("regid", cn.jiguang.aj.b.b(context));
            jSONObject.put("rom_info", jSONObject2);
            JSONObject jSONObject3 = new JSONObject();
            jSONObject3.put("cmd", 53);
            jSONObject3.put("content", jSONObject);
            return jSONObject3.toString();
        } catch (JSONException unused) {
            return null;
        }
    }

    public static void a(Context context, String str) {
        cn.jiguang.g.b.a(context, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{new cn.jiguang.g.a("cn.jpush.preferences.v2", "n_udp_report_device_info", "").a(Base64.encodeToString(str.getBytes(), 2))});
        cn.jiguang.w.a.a("JDeviceHelper", "save device info:" + str);
    }

    public static double b(Context context) {
        double dPow;
        int i;
        Point point = new Point();
        boolean z = context instanceof Activity;
        if (z) {
            ((Activity) context).getWindowManager().getDefaultDisplay().getRealSize(point);
        }
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        if (z) {
            dPow = Math.pow(point.x / displayMetrics.xdpi, 2.0d);
            i = point.y;
        } else {
            dPow = Math.pow(displayMetrics.widthPixels / displayMetrics.xdpi, 2.0d);
            i = displayMetrics.heightPixels;
        }
        return Math.sqrt(dPow + Math.pow(i / displayMetrics.ydpi, 2.0d));
    }

    public static String b() {
        if (!TextUtils.isEmpty(b)) {
            return b;
        }
        g();
        return b;
    }

    public static long c() {
        try {
            StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
            return (((long) statFs.getBlockCount()) * ((long) statFs.getBlockSize())) / 1024;
        } catch (Throwable unused) {
            return -1L;
        }
    }

    public static long c(Context context) {
        try {
            ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
            activityManager.getProcessMemoryInfo(new int[]{0});
            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
            activityManager.getMemoryInfo(memoryInfo);
            return memoryInfo.totalMem / 1024;
        } catch (Throwable unused) {
            return a("MemTotal");
        }
    }

    public static int d() {
        try {
            return new File("/sys/devices/system/cpu/").listFiles(c).length;
        } catch (Throwable unused) {
            return -1;
        }
    }

    public static String d(Context context) {
        try {
            cn.jiguang.ao.a aVarA = cn.jiguang.ao.a.a(context);
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("sim_slots", new JSONArray());
            jSONObject.put("packagename", context.getPackageName());
            jSONObject.put("appkey", cn.jiguang.ac.a.a(context));
            jSONObject.put(JThirdPlatFormInterface.KEY_PLATFORM, 0);
            jSONObject.put("apkversion", aVarA.a);
            jSONObject.put("systemversion", aVarA.b);
            jSONObject.put("modelnumber", aVarA.c);
            jSONObject.put("basebandversion", aVarA.d);
            jSONObject.put("buildnumber", aVarA.e);
            jSONObject.put("channel", aVarA.f);
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("PushSDKVer", h.a(0));
            jSONObject2.put("StatisticSDKVer", h.a(1));
            jSONObject2.put("ShareSDKVer", h.a(2));
            jSONObject2.put("CoreSDKVer", h.a(3));
            jSONObject2.put("SspSDKVer", h.a(4));
            jSONObject2.put("VerificationSDKVer", h.a(5));
            jSONObject.put("sdkver", jSONObject2);
            jSONObject.put("installation", aVarA.g);
            jSONObject.put("resolution", aVarA.h);
            cn.jiguang.aj.b.a();
            jSONObject.put("business", cn.jiguang.aj.b.b());
            jSONObject.put("device_id_status", cn.jiguang.ar.b.a);
            jSONObject.put("device_id", cn.jiguang.d.a.e(context));
            jSONObject.put("android_id", aVarA.i);
            jSONObject.put("mac_address", cn.jiguang.f.a.c(""));
            jSONObject.put("serial_number", aVarA.j);
            return jSONObject.toString();
        } catch (JSONException unused) {
            return null;
        }
    }

    public static int e() {
        int iIntValue = -1;
        for (int i = 0; i < d(); i++) {
            try {
                File file = new File("/sys/devices/system/cpu/cpu" + i + "/cpufreq/cpuinfo_max_freq");
                if (file.exists()) {
                    byte[] bArr = new byte[128];
                    FileInputStream fileInputStream = new FileInputStream(file);
                    try {
                        fileInputStream.read(bArr);
                        int i2 = 0;
                        while (true) {
                            byte b2 = bArr[i2];
                            if (b2 < 48 || b2 > 57 || i2 >= 128) {
                                break;
                            }
                            i2++;
                        }
                        Integer numValueOf = Integer.valueOf(Integer.parseInt(new String(bArr, 0, i2)));
                        if (numValueOf.intValue() > iIntValue) {
                            iIntValue = numValueOf.intValue();
                        }
                    } catch (Throwable unused) {
                    }
                    fileInputStream.close();
                }
            } catch (Throwable unused2) {
                return -1;
            }
        }
        if (iIntValue == -1) {
            FileInputStream fileInputStream2 = new FileInputStream("/proc/cpuinfo");
            try {
                int iA = a("cpu MHz", fileInputStream2) * 1000;
                if (iA > iIntValue) {
                    iIntValue = iA;
                }
            } finally {
                fileInputStream2.close();
            }
        }
        return iIntValue;
    }

    public static JSONArray f() {
        return new JSONArray();
    }

    private static void g() {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            if (new File("/proc/cpuinfo").exists()) {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("/proc/cpuinfo")));
                String strTrim = null;
                while (true) {
                    String line = bufferedReader.readLine();
                    if (line == null) {
                        break;
                    }
                    if (line.contains("Processor")) {
                        int iIndexOf = line.indexOf(":");
                        if (iIndexOf >= 0 && iIndexOf < line.length() - 1) {
                            strTrim = line.substring(iIndexOf + 1).trim();
                        }
                        if (strTrim != null && !stringBuffer.toString().contains(strTrim)) {
                            stringBuffer.append(strTrim);
                        }
                    }
                    if (line.contains("Hardware")) {
                        try {
                            String strTrim2 = line.substring(line.indexOf(":") + 1).trim();
                            if (!TextUtils.isEmpty(strTrim2)) {
                                b = strTrim2;
                            }
                        } catch (Throwable unused) {
                        }
                    }
                }
                bufferedReader.close();
            }
        } catch (Throwable unused2) {
        }
        a = stringBuffer.toString();
    }
}
