package cn.jiguang.z;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import cn.jpush.android.service.WakedResultReceiver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class a extends cn.jiguang.l.a {
    private static volatile a a;
    private static WakedResultReceiver c;
    private Context b;

    private a() {
        cn.jiguang.af.b.a("JWake");
    }

    public static a a() {
        if (a == null) {
            synchronized (a.class) {
                a = new a();
            }
        }
        return a;
    }

    public static void a(Context context, int i) {
        if (context == null) {
            cn.jiguang.w.a.d("JWake", "context is null,can not notify waked");
            return;
        }
        WakedResultReceiver wakedResultReceiverI = i(context);
        c = wakedResultReceiverI;
        if (wakedResultReceiverI == null) {
            cn.jiguang.w.a.d("JWake", "waked receiver is null");
            return;
        }
        HashMap map = new HashMap(2);
        map.put("1", context);
        map.put("2", Integer.valueOf(i));
        c.onWakeMap(map);
    }

    private void a(Context context, List<cn.jiguang.aa.b> list) {
        StringBuilder sbAppend;
        String message;
        Intent intent;
        if (list == null || list.size() <= 0) {
            cn.jiguang.w.a.d("JWake", "there are no wakeTarget");
            return;
        }
        HashMap map = new HashMap();
        map.put("from_package", context.getPackageName());
        for (cn.jiguang.aa.b bVar : list) {
            if (bVar.c == 2) {
                sbAppend = new StringBuilder("command this app is not allow to wake package:").append(bVar.a).append(",service:");
                message = bVar.b;
            } else {
                int i = bVar.c < 26 ? 3 : 2;
                if (!TextUtils.isEmpty(bVar.d)) {
                    i |= 4;
                }
                if (!cn.jiguang.ab.a.a(context) && cn.jiguang.ab.a.b(context)) {
                    i |= 8;
                }
                ComponentName componentName = new ComponentName(bVar.a, bVar.b);
                int i2 = i & 2;
                if (i2 == 0 && (i & 1) == 0) {
                    intent = null;
                } else {
                    Intent intent2 = new Intent();
                    intent2.setComponent(componentName);
                    intent2.setFlags(32);
                    Bundle bundleA = cn.jiguang.ab.c.a((HashMap<String, String>) map);
                    if (bundleA != null) {
                        intent2.putExtras(bundleA);
                    }
                    intent = intent2;
                }
                if (i2 != 0) {
                    try {
                        context.getApplicationContext().bindService(intent, new e(this, (byte) 0), 1);
                    } catch (Throwable th) {
                        cn.jiguang.w.a.d("JWake", "bindService throwable:" + th.getMessage());
                    }
                }
                if ((i & 1) != 0) {
                    try {
                        context.startService(intent);
                    } catch (Throwable th2) {
                        cn.jiguang.w.a.d("JWake", "startService throwable:" + th2.getMessage());
                    }
                }
                if ((i & 4) != 0) {
                    try {
                        if (!TextUtils.isEmpty(bVar.d)) {
                            ContentResolver contentResolver = context.getApplicationContext().getContentResolver();
                            String str = bVar.d;
                            if (!str.startsWith("content://")) {
                                str = "content://" + str;
                            }
                            String strB = cn.jiguang.ab.c.b(map);
                            if (!TextUtils.isEmpty(strB)) {
                                str = str + strB;
                            }
                            Cursor cursorQuery = contentResolver.query(Uri.parse(str), null, null, null, null);
                            if (cursorQuery != null && !cursorQuery.isClosed()) {
                                cursorQuery.close();
                            }
                        }
                    } catch (Throwable th3) {
                        sbAppend = new StringBuilder("getContentResolver throwable:");
                        message = th3.getMessage();
                        cn.jiguang.w.a.d("JWake", sbAppend.append(message).toString());
                    }
                }
            }
            cn.jiguang.w.a.d("JWake", sbAppend.append(message).toString());
        }
    }

    static /* synthetic */ void a(a aVar, Context context, JSONObject jSONObject) {
        List<cn.jiguang.aa.b> arrayList;
        ApplicationInfo applicationInfo;
        try {
            String strB = cn.jiguang.o.c.b(context, "bwct.catch.v2");
            cn.jiguang.w.a.a("JWake", "read cmd wakeTarget:" + strB);
            if (jSONObject != null) {
                try {
                    JSONObject jSONObject2 = jSONObject.getJSONObject("content");
                    int iOptInt = jSONObject2.optInt("type", 1);
                    String strOptString = jSONObject2.optString("pkgName", "");
                    String strOptString2 = jSONObject2.optString("serviceName", "");
                    arrayList = null;
                    if (iOptInt == 1) {
                        try {
                            applicationInfo = context.getPackageManager().getApplicationInfo(strOptString, 128);
                        } catch (Throwable unused) {
                            cn.jiguang.w.a.e("JWake", "not found application:" + strOptString);
                            applicationInfo = null;
                        }
                        if (applicationInfo != null) {
                            arrayList = new ArrayList<>();
                            cn.jiguang.aa.b bVar = new cn.jiguang.aa.b();
                            bVar.a = strOptString;
                            bVar.b = strOptString2;
                            bVar.c = applicationInfo.targetSdkVersion;
                            bVar.f = 1;
                            arrayList.add(bVar);
                        }
                    }
                    String strA = cn.jiguang.ab.c.a(cn.jiguang.ab.d.a(strB, strOptString, strOptString2, iOptInt));
                    cn.jiguang.w.a.a("JWake", "write cmd wakeTarget:" + strA);
                    cn.jiguang.o.c.a(context, "bwct.catch.v2", strA);
                } catch (Throwable th) {
                    cn.jiguang.w.a.d("JWake", "stop wake,the json form cmd is illegal:" + th.getMessage());
                    return;
                }
            } else {
                if (strB == null || TextUtils.isEmpty(strB)) {
                    cn.jiguang.w.a.d("JWake", "there are no cache cmd wakeTarget");
                    return;
                }
                arrayList = cn.jiguang.ab.c.a(context, cn.jiguang.ab.c.a(strB));
            }
            if (arrayList != null) {
                aVar.a(context, arrayList);
            }
        } catch (Throwable th2) {
            cn.jiguang.w.a.d("JWake", "parse throwable:" + th2.getMessage());
        }
    }

    public static void b(Context context, Bundle bundle) {
        try {
            cn.jiguang.w.a.a("JWake", "executeWakedAction.");
            if (bundle != null) {
                a(context, bundle.getInt("type"));
            }
        } catch (Throwable th) {
            cn.jiguang.w.a.a("JWake", "executeWakedAction failed:" + th.getLocalizedMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public cn.jiguang.aa.a h(Context context) {
        JSONObject jSONObjectA;
        boolean zA = cn.jiguang.l.e.a(context, "JWakeConfigHelper");
        cn.jiguang.w.a.a("JWake", "isRequestWakeConfigEnable:" + zA);
        if (!zA || (jSONObjectA = cn.jiguang.ab.b.a(context)) == null) {
            return cn.jiguang.ab.b.a(cn.jiguang.o.c.a(context, "bwc.catch"));
        }
        cn.jiguang.aa.a aVarA = cn.jiguang.ab.b.a(jSONObjectA);
        cn.jiguang.w.a.a("JWake", "wakeConfig:" + aVarA);
        cn.jiguang.ab.b.a(context, jSONObjectA);
        cn.jiguang.l.e.a(this.b, "JWakeConfigHelper", aVarA.f);
        cn.jiguang.l.e.a(this.b, "JWake", aVarA.e);
        cn.jiguang.l.e.b(this.b, "JWake", aVarA.g);
        cn.jiguang.l.e.a(this.b, "JWake", aVarA.a && aVarA.c);
        boolean z = aVarA.b && aVarA.d;
        cn.jiguang.l.e.a(this.b, "JWakeComponentHelper", z);
        cn.jiguang.ab.a.a(this.b, z);
        return aVarA;
    }

    private static WakedResultReceiver i(Context context) {
        WakedResultReceiver wakedResultReceiver = c;
        if (wakedResultReceiver != null) {
            return wakedResultReceiver;
        }
        try {
            Intent intent = new Intent();
            intent.setAction("cn.jpush.android.intent.WakedReceiver");
            intent.setPackage(context.getPackageName());
            intent.addCategory(context.getPackageName());
            List<ResolveInfo> listQueryBroadcastReceivers = context.getPackageManager().queryBroadcastReceivers(intent, 0);
            if (listQueryBroadcastReceivers == null || listQueryBroadcastReceivers.size() == 0) {
                return null;
            }
            return (WakedResultReceiver) Class.forName(listQueryBroadcastReceivers.get(0).activityInfo.name).newInstance();
        } catch (Throwable th) {
            cn.jiguang.w.a.d("JWake", "find waked receiver throwable:" + th.getMessage());
            return null;
        }
    }

    @Override // cn.jiguang.l.a
    public final String a(Context context) {
        this.b = context;
        return "JWake";
    }

    @Override // cn.jiguang.l.a
    protected final boolean a(Context context, String str) {
        boolean zA = cn.jiguang.l.e.a(context, "JWake");
        if (h(context) == null) {
            cn.jiguang.w.a.d("JWake", "wakeConfig is null");
            return false;
        }
        boolean zH = cn.jiguang.l.e.h(context, "JWakeComponentHelper");
        cn.jiguang.w.a.a("JWake", "isComponentEnable:" + zH);
        cn.jiguang.ab.a.a(context, zH);
        return cn.jiguang.l.e.h(context, "JWake") && zA;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // cn.jiguang.l.a
    protected final void b(Context context, String str) {
        cn.jiguang.aa.a aVarA = cn.jiguang.ab.b.a(cn.jiguang.o.c.a(this.b, "bwc.catch"));
        List<cn.jiguang.aa.b> listA = cn.jiguang.ab.c.a(this.b);
        if (listA == null || listA.isEmpty()) {
            listA = null;
        } else {
            HashMap map = new HashMap();
            for (cn.jiguang.aa.b bVar : listA) {
                map.put(bVar.a, bVar);
            }
            List<String> listA2 = cn.jiguang.ab.c.a(aVarA, new ArrayList(map.keySet()));
            listA.clear();
            for (String str2 : listA2) {
                if (map.containsKey(str2)) {
                    listA.add(map.get(str2));
                }
            }
        }
        if (listA == null || listA.isEmpty()) {
            cn.jiguang.w.a.a("JWake", "there are no app need wake");
        } else {
            a(context, (List<cn.jiguang.aa.b>) listA);
            super.b(context, str);
        }
    }

    public final void b(Context context, JSONObject jSONObject) {
        this.b = context;
        cn.jiguang.w.a.a("JWake", jSONObject == null ? "executeWakeAction: [JWake] from cmd" : "executeWakeAction: [JWake] from heartBeat");
        boolean zC = c();
        cn.jiguang.w.a.a("JWake", "JWake isActionUserEnable:" + zC);
        if (zC) {
            cn.jiguang.af.b.a("JWake", new d(this, context, jSONObject));
        }
    }

    @Override // cn.jiguang.l.a
    protected final boolean c(Context context, String str) {
        return super.c(context, str);
    }

    @Override // cn.jiguang.l.a
    protected final void d(Context context, String str) {
    }

    @Override // cn.jiguang.l.a
    protected final boolean d(Context context) {
        return true;
    }

    @Override // cn.jiguang.l.a
    public final Object e(Context context) {
        return Boolean.valueOf(cn.jiguang.l.e.h(context, "JWake"));
    }

    public final void f(Context context) {
        this.b = context;
        cn.jiguang.w.a.a("JWake", "executeWakeAction: [JWake] from first launch");
        cn.jiguang.af.b.a("JWake", new b(this, context));
    }

    public final void g(Context context) {
        cn.jiguang.af.b.a("JWake", new c(this, context, (byte) 0));
    }
}
