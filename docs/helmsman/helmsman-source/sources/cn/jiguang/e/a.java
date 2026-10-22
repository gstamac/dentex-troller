package cn.jiguang.e;

import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Pair;
import androidx.exifinterface.media.ExifInterface;
import cn.jiguang.ad.d;
import cn.jiguang.aj.o;
import cn.jiguang.al.c;
import cn.jiguang.f.e;
import cn.jiguang.f.i;
import cn.jiguang.internal.JConstants;
import cn.jpush.android.service.DownloadProvider;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.UserDataStore;
import java.io.File;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public final class a implements Runnable {
    private static final String b = ".jpush" + File.separator + ".shareinfo" + File.separator;
    private static final Object f = new Object();
    private static Boolean g;
    private static Boolean h;
    private static a j;
    Context a;
    private int c;
    private long[] d;
    private String e;
    private volatile boolean i;
    private boolean k;

    private a() {
        cn.jiguang.af.b.a("share_process_executor");
    }

    private static ActivityInfo a(String str, Context context) {
        String str2;
        try {
            Intent intent = new Intent();
            intent.setAction("cn.jpush.android.intent.DownloadActivity");
            intent.addCategory(str);
            intent.setPackage(str);
            ActivityInfo activityInfo = context.getPackageManager().resolveActivity(intent, 0).activityInfo;
            if ((activityInfo instanceof ActivityInfo) && ((ComponentInfo) activityInfo).exported && ((ComponentInfo) activityInfo).enabled) {
                if ("jpush.custom".equals(activityInfo.taskAffinity)) {
                    if (activityInfo.theme == 16973840) {
                        return activityInfo;
                    }
                    str2 = "download activity theme must config as @android:style/Theme.Translucent.NoTitleBar";
                } else {
                    str2 = "download activity need config taskAffinity is jpush.custom";
                }
                d.b("ShareProcessManager", str2);
            }
        } catch (Throwable th) {
            d.f("ShareProcessManager", "check downloadActivity error:" + th.getMessage());
        }
        d.b("ShareProcessManager", "DownloadActivity is invalid in " + str);
        return null;
    }

    public static a a() {
        if (j == null) {
            synchronized (f) {
                if (j == null) {
                    j = new a();
                }
            }
        }
        return j;
    }

    private b a(String str) {
        Throwable th;
        b bVar;
        try {
            if (TextUtils.isEmpty(str)) {
                return null;
            }
            JSONObject jSONObject = new JSONObject(str);
            bVar = new b(this);
            try {
                long jOptLong = jSONObject.optLong("u");
                String strOptString = jSONObject.optString("ak");
                String strOptString2 = jSONObject.optString("pn");
                String strOptString3 = jSONObject.optString("ud");
                int iOptInt = jSONObject.optInt("idc", -1);
                int iOptInt2 = jSONObject.optInt("sv");
                long jOptLong2 = jSONObject.optLong("uct", -1L);
                bVar.d = jOptLong;
                bVar.c = strOptString3;
                bVar.f = strOptString;
                bVar.b = iOptInt;
                bVar.e = strOptString2;
                bVar.g = iOptInt2;
                bVar.h = jOptLong2;
            } catch (Throwable th2) {
                th = th2;
                d.f("ShareProcessManager", "parse json to shareBean failed:" + th.getMessage());
            }
            return bVar;
        } catch (Throwable th3) {
            th = th3;
            bVar = null;
        }
        d.f("ShareProcessManager", "parse json to shareBean failed:" + th.getMessage());
        return bVar;
    }

    public static String a(Context context, Uri uri) {
        try {
            if (uri == null) {
                return cn.jiguang.a.a.b;
            }
            String queryParameter = uri.getQueryParameter("kpgt");
            if (TextUtils.isEmpty(queryParameter)) {
                return cn.jiguang.a.a.b;
            }
            String strB = cn.jiguang.ar.d.b(queryParameter);
            if (TextUtils.isEmpty(strB)) {
                return "-6";
            }
            JSONObject jSONObject = new JSONObject(strB);
            String strOptString = jSONObject.optString("kta");
            d.b("ShareProcessManager", "action:" + strOptString);
            if (!TextUtils.isEmpty(strOptString)) {
                if (strOptString.equals("asai")) {
                    return e(context);
                }
                if (strOptString.equals("asm")) {
                    d.b("ShareProcessManager", "recv msg:" + jSONObject.toString());
                    if (g(context) && ((Integer) cn.jiguang.g.b.a(context, cn.jiguang.g.a.k())).intValue() != 1) {
                        Bundle bundle = new Bundle();
                        bundle.putString("data", jSONObject.toString());
                        cn.jiguang.c.b.a(context, JConstants.SDK_TYPE, "asm", bundle);
                        return AppEventsConstants.EVENT_PARAM_VALUE_NO;
                    }
                    d.b("ShareProcessManager", "share process is closed!");
                    return "-4";
                }
                if (strOptString.equals("asmr")) {
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("data", jSONObject.toString());
                    cn.jiguang.c.b.a(context, JConstants.SDK_TYPE, "asmr", bundle2);
                }
            }
            return cn.jiguang.a.a.b;
        } catch (Throwable th) {
            d.f("ShareProcessManager", "parseUriFromProvider failed:" + th.getMessage());
        }
    }

    private static String a(Context context, String str, String str2, HashMap<String, String> map) {
        try {
            ContentResolver contentResolver = context.getApplicationContext().getContentResolver();
            String str3 = str + ".DownloadProvider";
            if (!str3.startsWith("content://")) {
                str3 = "content://" + str3;
            }
            Uri uri = Uri.parse(str3);
            JSONObject jSONObject = new JSONObject();
            Uri.Builder builderBuildUpon = uri.buildUpon();
            if (!TextUtils.isEmpty(str2)) {
                jSONObject.put("kta", str2);
            }
            if (map != null && !map.isEmpty()) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    jSONObject.put(entry.getKey(), entry.getValue());
                }
            }
            builderBuildUpon.appendQueryParameter("kpgt", cn.jiguang.ar.d.a(jSONObject.toString()));
            return contentResolver.getType(builderBuildUpon.build());
        } catch (Throwable th) {
            d.f("ShareProcessManager", "callUriToDownloadProvider error:" + th.getMessage());
            return null;
        }
    }

    private static JSONObject a(Map<String, String> map) {
        JSONObject jSONObject = new JSONObject();
        try {
            if (!map.isEmpty()) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    jSONObject.put(entry.getKey(), entry.getValue());
                }
            }
        } catch (Throwable th) {
            d.f("ShareProcessManager", "mapToJSONObject error:" + th.getMessage());
        }
        return jSONObject;
    }

    public static void a(Context context, String str) {
        if (context == null || TextUtils.isEmpty(str)) {
            d.f("ShareProcessManager", "deletFileIfUninstall failed ,context is null or pkgname is empty");
            return;
        }
        try {
            if (cn.jiguang.f.a.c(context, "android.permission.WRITE_EXTERNAL_STORAGE")) {
                File fileB = b(str);
                if (fileB.exists()) {
                    fileB.delete();
                } else {
                    d.b("ShareProcessManager", "not found file in sdcard,filepath:" + fileB.getAbsolutePath());
                }
            } else {
                d.b("ShareProcessManager", "no write sdcard permission when deletFileIfUninstall");
            }
        } catch (Throwable unused) {
        }
    }

    private b b(Context context, String str) {
        String str2;
        try {
            String strA = a(context, str, "asai", null);
            d.b("ShareProcessManager", "get type from:" + str + ",info:" + strA);
            if (strA != null) {
                if (!TextUtils.isEmpty(strA) && strA.length() > 10) {
                    String strB = cn.jiguang.ar.d.b(strA);
                    if (TextUtils.isEmpty(strB)) {
                        d.b("ShareProcessManager", "decrypt error");
                        return null;
                    }
                    d.b("ShareProcessManager", "parse success:" + strB);
                    return a(strB);
                }
                str2 = "is not shareprocessbean info";
            } else {
                if (!f(context) || JConstants.isAndroidQ(context, false, "do not get share info from SD")) {
                    return null;
                }
                if (cn.jiguang.f.a.c(context, "android.permission.READ_EXTERNAL_STORAGE")) {
                    File fileB = b(str);
                    String strB2 = e.b(fileB);
                    if (TextUtils.isEmpty(strB2)) {
                        str2 = "read info is empty from :" + fileB.getAbsolutePath();
                    } else {
                        b bVarA = a(cn.jiguang.ar.d.b(strB2));
                        if (bVarA == null) {
                            str2 = "parse share process bean with target app:" + str;
                        } else {
                            if (!cn.jiguang.f.a.d(context, bVarA.e)) {
                                d.b("ShareProcessManager", "found target app is uninsatll when scan sdcard,pkgname:" + bVarA.e);
                                a(context, bVarA.e);
                                return null;
                            }
                            if (a(bVarA.e, context) != null) {
                                d.b("ShareProcessManager", "get share bean info from sdcard:" + bVarA.toString());
                                return bVarA;
                            }
                            str2 = "not config DownloadActivity in target app:" + str;
                        }
                    }
                } else {
                    str2 = "no read sdcard permission";
                }
            }
            d.b("ShareProcessManager", str2);
            return null;
        } catch (Throwable th) {
            d.g("ShareProcessManager", "scanShareProcessBean error:" + th.getMessage());
            return null;
        }
    }

    private static File b(String str) {
        String strD = i.d(str);
        if (!TextUtils.isEmpty(strD)) {
            str = strD;
        }
        return new File(Environment.getExternalStorageDirectory(), b + str);
    }

    private Set<b> c(Context context) {
        boolean z;
        HashSet hashSet = new HashSet();
        try {
            PackageManager packageManager = context.getPackageManager();
            Intent intent = new Intent();
            intent.setAction("cn.jpush.android.intent.DaemonService");
            List<ResolveInfo> listQueryIntentServices = packageManager.queryIntentServices(intent, 0);
            if (listQueryIntentServices != null && listQueryIntentServices.size() != 0) {
                List<String> arrayList = new ArrayList();
                for (int i = 0; i < listQueryIntentServices.size(); i++) {
                    ServiceInfo serviceInfo = listQueryIntentServices.get(i).serviceInfo;
                    String str = serviceInfo.name;
                    String str2 = serviceInfo.packageName;
                    if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2) && !context.getPackageName().equals(str2)) {
                        ProviderInfo providerInfoB = cn.jiguang.f.a.b(context, str2, (Class<? extends ContentProvider>) DownloadProvider.class);
                        if (providerInfoB == null || !(providerInfoB instanceof ProviderInfo)) {
                            z = false;
                        } else {
                            ProviderInfo providerInfo = providerInfoB;
                            d.a("ShareProcessManager", "scan exported:" + providerInfoB.exported + ",enable:" + providerInfoB.enabled + ",authority:" + providerInfoB.authority + ",process:" + providerInfoB.processName);
                            if (providerInfoB.exported && providerInfoB.enabled && !TextUtils.isEmpty(providerInfoB.authority) && TextUtils.equals(str2 + ".DownloadProvider", providerInfoB.authority)) {
                                z = true;
                            } else {
                                d.b("ShareProcessManager", "downloadprovider config error,exported:" + providerInfoB.exported + ",enable:" + providerInfoB.enabled + ",authority:" + providerInfoB.authority + ",process:" + providerInfoB.processName);
                                z = false;
                            }
                        }
                        if (z) {
                            arrayList.add(str2);
                        }
                    }
                }
                d.b("ShareProcessManager", "valid size:" + arrayList.size());
                Object objA = cn.jiguang.aj.e.a(context, "filter_pkg_list", arrayList);
                if (objA instanceof List) {
                    arrayList = (List) objA;
                }
                d.b("ShareProcessManager", "valid end size:" + arrayList.size());
                for (String str3 : arrayList) {
                    b bVarB = b(context, str3);
                    d.b("ShareProcessManager", "scan share bean from:" + str3);
                    if (bVarB != null) {
                        hashSet.add(bVarB);
                    }
                }
                d.b("ShareProcessManager", "end share bean list size:" + hashSet.size());
                return hashSet;
            }
            d.b("ShareProcessManager", "query service size is empty");
            return hashSet;
        } catch (Throwable th) {
            d.f("ShareProcessManager", "scanOtherApp error:" + th.getMessage());
        }
    }

    private static void d(Context context) {
        try {
            if (JConstants.isAndroidQ(context, false, "do not save ShareInfo to SD")) {
                return;
            }
            if (!cn.jiguang.f.a.c(context, "android.permission.WRITE_EXTERNAL_STORAGE")) {
                d.b("ShareProcessManager", "no write sdcard permission");
                return;
            }
            File fileB = b(context.getPackageName());
            if (!f(context)) {
                e.a(fileB);
                return;
            }
            String strE = e(context);
            d.b("ShareProcessManager", "save info to sdcard:" + fileB.getAbsolutePath());
            if (TextUtils.isEmpty(strE) || strE.length() <= 10) {
                return;
            }
            e.a(fileB);
            e.a(fileB, strE);
        } catch (Throwable th) {
            d.f("ShareProcessManager", "saveShareInfoToSdCard failed:" + th.getMessage());
        }
    }

    private static String e(Context context) {
        if (context == null) {
            return "-1";
        }
        if (!g(context) || cn.jiguang.d.a.j(context) == 1) {
            d.b("ShareProcessManager", "[getTypeJson]share process is close by action");
            return "-4";
        }
        if (!cn.jiguang.ac.b.a().d()) {
            d.b("ShareProcessManager", "getAttachJson,is not support jpush or jmessage ");
            return "-7";
        }
        int iH = cn.jiguang.d.a.h(context);
        if (iH < 0) {
            d.b("ShareProcessManager", "[getTypeJson]idc<0,need login to get it");
            return "-3";
        }
        long jD = cn.jiguang.d.a.d(context);
        if (jD <= 0) {
            d.b("ShareProcessManager", "[getTypeJson]uid<=0,need login to get it");
            return "-2";
        }
        Map map = (Map) cn.jiguang.d.a.g(context);
        String str = (String) map.get("uuid");
        long jLongValue = ((Long) map.get(UserDataStore.CITY)).longValue();
        String strA = cn.jiguang.ac.a.a(context);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("u", jD);
            jSONObject.put("p", cn.jiguang.d.a.f(context));
            jSONObject.put("ud", str);
            jSONObject.put("ak", strA);
            jSONObject.put("idc", iH);
            jSONObject.put("pn", context.getPackageName());
            jSONObject.put("sv", cn.jiguang.a.a.c);
            jSONObject.put("uct", jLongValue);
            return cn.jiguang.ar.d.a(jSONObject.toString());
        } catch (JSONException unused) {
            d.b("ShareProcessManager", "[getTypeJson] to json error");
            return cn.jiguang.a.a.b;
        }
    }

    private static boolean f(Context context) {
        Boolean bool = h;
        if (bool != null) {
            return bool.booleanValue();
        }
        try {
            String str = Build.MANUFACTURER;
            String lowerCase = "Xiaomi".toLowerCase();
            if (!TextUtils.isEmpty(str) && TextUtils.equals(lowerCase, str.toLowerCase())) {
                d.b("ShareProcessManager", "xiaomi not use activity and sdcard");
                Boolean bool2 = false;
                h = bool2;
                return bool2.booleanValue();
            }
        } catch (Throwable th) {
            d.f("ShareProcessManager", "get MANUFACTURER failed - error:" + th.getMessage());
        }
        h = a(context.getPackageName(), context) != null;
        return h.booleanValue();
    }

    private static boolean g(Context context) {
        Boolean bool = g;
        if (bool != null) {
            return bool.booleanValue();
        }
        if (context == null) {
            d.f("ShareProcessManager", "context is null");
            return true;
        }
        try {
            ProviderInfo providerInfoB = cn.jiguang.f.a.b(context, context.getPackageName(), (Class<? extends ContentProvider>) DownloadProvider.class);
            if (providerInfoB == null) {
                d.b("ShareProcessManager", "not found download provider in manifest");
                Boolean bool2 = false;
                g = bool2;
                return bool2.booleanValue();
            }
            if (((ComponentInfo) providerInfoB).enabled && ((ComponentInfo) providerInfoB).exported) {
                ProviderInfo providerInfo = providerInfoB;
                if (!TextUtils.isEmpty(providerInfoB.authority)) {
                    Intent intent = new Intent();
                    intent.setPackage(context.getPackageName());
                    intent.setAction("cn.jiguang.android.share.close");
                    List<ResolveInfo> listQueryIntentServices = context.getPackageManager().queryIntentServices(intent, 0);
                    g = listQueryIntentServices == null || listQueryIntentServices.isEmpty();
                    return g.booleanValue();
                }
            }
            ProviderInfo providerInfo2 = providerInfoB;
            d.b("ShareProcessManager", "download provider config error,enable" + ((ComponentInfo) providerInfoB).enabled + ",exported:" + ((ComponentInfo) providerInfoB).exported + ",authority:" + providerInfoB.authority);
            Boolean bool3 = false;
            g = bool3;
            return bool3.booleanValue();
        } catch (Throwable th) {
            d.b("ShareProcessManager", "Get isShareProcessModeOpen error#:" + th.getMessage());
            return true;
        }
    }

    public final void a(int i, int i2) {
        d.b("ShareProcessManager", "requestsuccess,cmd:" + i2 + ",code:0");
        if (i2 == 30) {
            this.c = 0;
        }
    }

    public final synchronized void a(Context context) {
        if (g(context) && cn.jiguang.af.b.c(context) != 1) {
            if (!cn.jiguang.ac.b.a().d()) {
                d.b("ShareProcessManager", "is not support jpush or jmessage ");
                return;
            }
            Object objA = cn.jiguang.aj.e.a(context, "getwakeenable", null);
            if ((objA instanceof Boolean) && !((Boolean) objA).booleanValue()) {
                d.b("ShareProcessManager", "wake disable,not scan share app");
                return;
            }
            if (this.i) {
                d.b("ShareProcessManager", "isAttaching");
                return;
            }
            this.a = context;
            this.i = true;
            d.b("ShareProcessManager", "scanOtherApp...");
            this.c = 0;
            cn.jiguang.af.b.a("share_process_executor", this);
            return;
        }
        d.b("ShareProcessManager", "share process is close by action");
        a(context, context.getPackageName());
    }

    public final void a(Context context, int i) {
        d.b("ShareProcessManager", "requestTimeOut,cmd:" + i + ",isAttaching:" + this.i);
        if (i != 30 || this.i) {
            return;
        }
        this.c++;
        d.b("ShareProcessManager", "attachTimeoutTimes:" + this.c + ",requestUIDS:" + this.d + ",shareProcessUUID:" + this.e);
        if (this.c > 2) {
            d.b("ShareProcessManager", "attach too many times by once scan");
            return;
        }
        long[] jArr = this.d;
        if (jArr == null || jArr.length <= 0 || TextUtils.isEmpty(this.e)) {
            return;
        }
        d.b("ShareProcessManager", "will retry attach");
        cn.jiguang.af.b.a(context, JConstants.SDK_TYPE, 30, 0, o.b(), 0L, cn.jiguang.al.b.a(cn.jiguang.d.a.d(context), this.e, this.d));
    }

    public final void a(Context context, long j2) {
        try {
            if (TextUtils.isEmpty(this.e)) {
                d.b("ShareProcessManager", "dettachUid error,shareUUID is empty");
            } else {
                d.b("ShareProcessManager", "dettach uid:" + j2);
                cn.jiguang.af.b.a(context, JConstants.SDK_TYPE, 32, 0, o.b(), 0L, cn.jiguang.al.b.a(this.e, new long[]{j2}));
            }
        } catch (Throwable th) {
            d.b("ShareProcessManager", "dettach uid error:" + th.getMessage());
        }
    }

    public final void a(Context context, long j2, byte[] bArr) {
        long j3;
        if (j2 == 0 || bArr == null) {
            return;
        }
        try {
            Pair<c, ByteBuffer> pairA = cn.jiguang.al.a.a(context, bArr, "");
            if (pairA != null && ((c) pairA.first).c == 3) {
                ByteBuffer byteBuffer = (ByteBuffer) pairA.second;
                byteBuffer.get();
                long j4 = byteBuffer.getLong();
                String strA = cn.jiguang.al.b.a(byteBuffer);
                if (TextUtils.isEmpty(strA)) {
                    d.f("ShareProcessManager", "msgContent is empty");
                    return;
                }
                LineNumberReader lineNumberReader = new LineNumberReader(new StringReader(strA));
                String line = lineNumberReader.readLine();
                if (TextUtils.isEmpty(line)) {
                    d.h("ShareProcessManager", "appid is empty");
                    return;
                }
                String line2 = lineNumberReader.readLine();
                if (TextUtils.isEmpty(line2)) {
                    d.h("ShareProcessManager", "senderId is empty");
                    return;
                }
                if (g(context) && cn.jiguang.af.b.c(context) != 1) {
                    if (!cn.jiguang.f.a.d(context, line)) {
                        a(context, ((c) pairA.first).g);
                        d.b("ShareProcessManager", "app not installed:" + line);
                        a(context, line);
                        return;
                    }
                    String strEncodeToString = Base64.encodeToString(bArr, 10);
                    Long lValueOf = Long.valueOf(((c) pairA.first).e);
                    HashMap map = new HashMap();
                    map.put("ktm", strEncodeToString);
                    map.put("ktp", cn.jiguang.ar.d.a(cn.jiguang.d.a.d(context)));
                    map.put("ktma", line2);
                    map.put("mtmmi", new StringBuilder().append(j4).toString());
                    map.put("ktmfp", context.getPackageName());
                    map.put("ktmr", new StringBuilder().append(lValueOf).toString());
                    d.b("ShareProcessManager", "dispatch share msg,appkey:" + line2 + ",msgid:" + j4 + ",rid:" + lValueOf);
                    String strA2 = a(context, line, "asm", map);
                    d.b("ShareProcessManager", "dispatch result:" + strA2);
                    if (TextUtils.isEmpty(strA2)) {
                        if (!f(context)) {
                            d.b("ShareProcessManager", "app can not use downloadActivity dispatch msg");
                            j3 = ((c) pairA.first).g;
                        } else {
                            if (!cn.jiguang.af.b.a && JConstants.isAndroidQ(context, false, "do not startActivity in BackGround")) {
                                this.k = true;
                                a(context, cn.jiguang.d.a.d(context));
                                return;
                            }
                            ActivityInfo activityInfoA = a(line, context);
                            if (activityInfoA != null) {
                                d.b("ShareProcessManager", "will try use downloadActivity");
                                JSONObject jSONObjectA = a(map);
                                Intent intent = new Intent("asm");
                                intent.setComponent(new ComponentName(activityInfoA.packageName, activityInfoA.name));
                                intent.setFlags(268435456);
                                intent.addCategory(line);
                                intent.putExtra("data", jSONObjectA.toString());
                                context.startActivity(intent);
                            } else {
                                j3 = ((c) pairA.first).g;
                            }
                        }
                        a(context, j3);
                    } else if (strA2.equals("-4")) {
                        j3 = ((c) pairA.first).g;
                        a(context, j3);
                    } else {
                        d.b("ShareProcessManager", strA2.equals(AppEventsConstants.EVENT_PARAM_VALUE_NO) ? "wait the msg reponse" : "provider is :" + strA2 + ",app is less than jcore_v125");
                    }
                    lineNumberReader.close();
                    return;
                }
                a(context, cn.jiguang.d.a.d(context));
                d.b("ShareProcessManager", " share process is close,will not dispatch the msg and dettach mine uid");
                return;
            }
            d.b("ShareProcessManager", "share msg cmd is not 3");
        } catch (Throwable th) {
            d.f("ShareProcessManager", "dispatchMsg error:" + th.getMessage());
        }
    }

    public final void a(Context context, Bundle bundle) {
        String str;
        try {
            d.b("ShareProcessManager", "doMsg");
            if (bundle != null) {
                String string = bundle.getString("data");
                if (TextUtils.isEmpty(string)) {
                    return;
                }
                JSONObject jSONObject = new JSONObject(string);
                d.b("ShareProcessManager", "doMsg json:" + jSONObject.toString());
                String strOptString = jSONObject.optString("ktm");
                String strOptString2 = jSONObject.optString("ktp");
                String strOptString3 = jSONObject.optString("mtmmi");
                String strOptString4 = jSONObject.optString("ktmfp");
                String strOptString5 = jSONObject.optString("ktma");
                String strOptString6 = jSONObject.optString("ktmr");
                if (TextUtils.isEmpty(strOptString) || TextUtils.isEmpty(strOptString3) || TextUtils.isEmpty(strOptString4) || TextUtils.isEmpty(strOptString5)) {
                    return;
                }
                HashMap map = new HashMap();
                map.put("mtmmi", strOptString3);
                map.put("ktmfp", strOptString4);
                map.put("ktma", strOptString5);
                map.put("ktmr", strOptString6);
                Pair<c, ByteBuffer> pairA = cn.jiguang.al.a.a(context, Base64.decode(strOptString, 10), strOptString2);
                if (pairA != null) {
                    map.put("ktmu", new StringBuilder().append(((c) pairA.first).g).toString());
                    if (!g(context) || cn.jiguang.af.b.c(context) == 1) {
                        d.b("ShareProcessManager", "share process is closed");
                        str = ExifInterface.GPS_MEASUREMENT_3D;
                    } else if (((c) pairA.first).g != cn.jiguang.d.a.d(context)) {
                        d.b("ShareProcessManager", "this msg uid is :" + ((c) pairA.first).g + ",is not this app msg");
                        str = "1";
                    } else {
                        map.put("asmrc", AppEventsConstants.EVENT_PARAM_VALUE_NO);
                        cn.jiguang.aj.b.a();
                        cn.jiguang.aj.b.a(context, (c) pairA.first, (ByteBuffer) pairA.second);
                    }
                    map.put("asmrc", str);
                }
                a(context, strOptString4, "asmr", map);
            }
        } catch (Throwable th) {
            d.b("ShareProcessManager", "doMsg error:" + th.getMessage());
        }
    }

    public final void b(Context context) {
        if (this.k) {
            this.k = false;
            if (cn.jiguang.aj.i.a().d()) {
                d.b("ShareProcessManager", "attach mine while app in foreground from background");
                cn.jiguang.af.b.a(context, JConstants.SDK_TYPE, 30, 0, o.b(), 0L, cn.jiguang.al.b.a(cn.jiguang.d.a.d(context), this.e, new long[]{cn.jiguang.d.a.d(context)}));
            }
        }
    }

    @Override // java.lang.Runnable
    public final void run() {
        boolean z;
        long jCurrentTimeMillis;
        boolean z2;
        String str;
        try {
            Map mapB = cn.jiguang.af.b.b(this.a);
            if (mapB != null) {
                this.e = (String) mapB.get("uuid");
                jCurrentTimeMillis = ((Long) mapB.get(UserDataStore.CITY)).longValue();
            } else {
                jCurrentTimeMillis = -1;
            }
            d.b("ShareProcessManager", "sp uuid:" + this.e + ",createTime:" + jCurrentTimeMillis);
            if (TextUtils.isEmpty(this.e) || jCurrentTimeMillis < 0) {
                d.b("ShareProcessManager", "not found uuid,create uuid");
                this.e = UUID.randomUUID().toString();
                jCurrentTimeMillis = System.currentTimeMillis();
                d.b("ShareProcessManager", "save uuid and createtime to sp,uuid:" + this.e + ",createtime:" + jCurrentTimeMillis);
                cn.jiguang.af.b.a(this.a, this.e, jCurrentTimeMillis);
                z2 = true;
            } else {
                z2 = false;
            }
            Set<b> setC = c(this.a);
            if (setC.isEmpty()) {
                str = "scan app list is empty";
            } else {
                int iH = cn.jiguang.d.a.h(this.a);
                d.b("ShareProcessManager", "mine idc:" + iH);
                ArrayList arrayList = new ArrayList();
                for (b bVar : setC) {
                    if (iH == bVar.b && bVar.d > 0 && !this.a.getPackageName().equals(bVar.e)) {
                        arrayList.add(bVar);
                    }
                }
                if (arrayList.size() != 0) {
                    this.d = new long[arrayList.size()];
                    String str2 = "";
                    String str3 = str2;
                    long j2 = -1;
                    for (int i = 0; i < arrayList.size(); i++) {
                        d.b("ShareProcessManager", "found same idc app :" + arrayList.get(i));
                        this.d[i] = ((b) arrayList.get(i)).d;
                        str2 = str2 + ((b) arrayList.get(i)).d + ",";
                        if (!TextUtils.isEmpty(((b) arrayList.get(i)).c) && ((b) arrayList.get(i)).h > 0) {
                            if (((b) arrayList.get(i)).h < j2 || j2 == -1) {
                                d.b("ShareProcessManager", "found older uuid from:" + ((b) arrayList.get(i)).e);
                                str3 = ((b) arrayList.get(i)).c;
                                j2 = ((b) arrayList.get(i)).h;
                            }
                        }
                    }
                    d.b("ShareProcessManager", "oldestUUID:" + str3 + ",oldestTime:" + j2 + ",localTime:" + jCurrentTimeMillis + ",localUUID:" + this.e);
                    if (TextUtils.isEmpty(str3)) {
                        d.b("ShareProcessManager", "not found other app(contains uuid) ");
                    } else if (z2) {
                        if (j2 > jCurrentTimeMillis) {
                            d.b("ShareProcessManager", "jump time");
                        }
                        this.e = str3;
                        cn.jiguang.af.b.a(this.a, str3, j2);
                    } else {
                        if (jCurrentTimeMillis != j2) {
                            d.b("ShareProcessManager", "the time exception");
                            cn.jiguang.af.b.a(this.a, this.e, jCurrentTimeMillis);
                        } else if (!this.e.equals(str3)) {
                            d.b("ShareProcessManager", "same time but uuid is not same");
                            cn.jiguang.af.b.a(this.a, "", System.currentTimeMillis());
                        }
                        this.e = str3;
                    }
                    d.b("ShareProcessManager", "use uuid:" + this.e + ",uids:" + str2);
                    cn.jiguang.af.b.a(this.a, JConstants.SDK_TYPE, 30, 0, o.b(), 0L, cn.jiguang.al.b.a(cn.jiguang.d.a.d(this.a), this.e, this.d));
                    z = false;
                    this.i = z;
                }
                str = "not found app by same idc";
            }
            d.b("ShareProcessManager", str);
            this.i = false;
        } catch (Throwable th) {
            try {
                d.h("ShareProcessManager", "#unception, execute ScanAppAction failed:" + th);
                z = false;
            } finally {
                this.i = false;
                d(this.a);
            }
        }
    }
}
