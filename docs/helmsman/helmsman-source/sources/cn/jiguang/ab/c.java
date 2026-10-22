package cn.jiguang.ab;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class c {
    public static Bundle a(HashMap<String, String> map) {
        if (map.isEmpty()) {
            return null;
        }
        Bundle bundle = new Bundle();
        for (String str : map.keySet()) {
            bundle.putString(str, map.get(str));
        }
        return bundle;
    }

    public static String a(List<cn.jiguang.aa.b> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            String str = list.get(i).a;
            String str2 = list.get(i).b;
            sb.append(str);
            sb.append("|");
            sb.append(str2);
            sb.append("$");
        }
        return sb.toString();
    }

    public static List<cn.jiguang.aa.b> a(Context context) {
        cn.jiguang.aa.b bVarA;
        try {
            ArrayList arrayList = new ArrayList();
            PackageManager packageManager = context.getPackageManager();
            Intent intent = new Intent();
            intent.setAction("cn.jpush.android.intent.DaemonService");
            List<ResolveInfo> listQueryIntentServices = packageManager.queryIntentServices(intent, 0);
            if (listQueryIntentServices != null && listQueryIntentServices.size() != 0) {
                for (int i = 0; i < listQueryIntentServices.size(); i++) {
                    ServiceInfo serviceInfo = listQueryIntentServices.get(i).serviceInfo;
                    String str = serviceInfo.name;
                    String str2 = serviceInfo.packageName;
                    if (str != null && str2 != null && !TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2) && serviceInfo.exported && serviceInfo.enabled && !context.getPackageName().equals(str2) && (bVarA = a.a(context, packageManager, str2, str)) != null) {
                        cn.jiguang.w.a.a("JWakeHelper", "wakeTarget:" + bVarA.toString());
                        arrayList.add(bVarA);
                    }
                }
                return arrayList;
            }
            return null;
        } catch (Throwable th) {
            cn.jiguang.w.a.d("JWakeHelper", "getWakeTargetList throwable:" + th.getMessage());
            return null;
        }
    }

    public static List<cn.jiguang.aa.b> a(Context context, List<cn.jiguang.aa.b> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        for (int i = 0; i < list.size(); i++) {
            try {
                ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(list.get(i).a, 128);
                list.get(i).c = applicationInfo.targetSdkVersion;
            } catch (Throwable unused) {
            }
        }
        return list;
    }

    public static List<String> a(cn.jiguang.aa.a aVar, List<String> list) {
        if (aVar == null) {
            return list;
        }
        if (!TextUtils.isEmpty(aVar.h) && !aVar.h.equals("disable")) {
            List<String> list2 = aVar.i;
            String str = aVar.h;
            str.hashCode();
            if (str.equals("exclude")) {
                list = a(list2, list, false);
            } else if (str.equals("include")) {
                list = a(list2, list, true);
            }
        }
        return a(aVar.j, list, false);
    }

    public static List<cn.jiguang.aa.b> a(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String[] strArrSplit = str.split("\\$");
        ArrayList arrayList = new ArrayList();
        for (String str2 : strArrSplit) {
            String[] strArrSplit2 = str2.split("\\|");
            String str3 = strArrSplit2[0];
            String str4 = strArrSplit2[1];
            cn.jiguang.aa.b bVar = new cn.jiguang.aa.b();
            bVar.a = str3;
            bVar.b = str4;
            bVar.f = 1;
            arrayList.add(bVar);
        }
        return arrayList;
    }

    private static List<String> a(List<String> list, List<String> list2, boolean z) {
        if (list == null || list.size() == 0) {
            return list2;
        }
        ArrayList arrayList = new ArrayList();
        for (String str : list2) {
            if (list.contains(str)) {
                if (z) {
                    cn.jiguang.w.a.a("JWakeHelper", str + " in the white list");
                    arrayList.add(str);
                } else {
                    cn.jiguang.w.a.a("JWakeHelper", str + " in the black list");
                }
            }
            if (!z) {
                cn.jiguang.w.a.a("JWakeHelper", str + " not in the global black list");
                arrayList.add(str);
            }
        }
        return arrayList;
    }

    public static String b(HashMap<String, String> map) {
        if (map.isEmpty()) {
            return null;
        }
        Uri.Builder builder = new Uri.Builder();
        for (String str : map.keySet()) {
            builder.appendQueryParameter(str, map.get(str));
        }
        return builder.toString();
    }
}
