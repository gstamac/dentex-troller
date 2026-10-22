package cn.jiguang.q;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Looper;
import android.text.TextUtils;
import com.bun.miitmdid.core.MdidSdkHelper;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public final class a {
    /* JADX WARN: Code duplicated, block: B:16:0x0032 A[Catch: all -> 0x007e, TryCatch #0 {all -> 0x007e, blocks: (B:3:0x0002, B:6:0x000f, B:14:0x002c, B:16:0x0032, B:28:0x006c, B:32:0x007a, B:17:0x0038, B:19:0x0040, B:21:0x004c, B:23:0x0056, B:24:0x005c, B:27:0x0069, B:7:0x0015, B:9:0x001b, B:12:0x0026), top: B:37:0x0002 }] */
    /* JADX WARN: Code duplicated, block: B:17:0x0038 A[Catch: all -> 0x007e, TryCatch #0 {all -> 0x007e, blocks: (B:3:0x0002, B:6:0x000f, B:14:0x002c, B:16:0x0032, B:28:0x006c, B:32:0x007a, B:17:0x0038, B:19:0x0040, B:21:0x004c, B:23:0x0056, B:24:0x005c, B:27:0x0069, B:7:0x0015, B:9:0x001b, B:12:0x0026), top: B:37:0x0002 }] */
    /* JADX WARN: Code duplicated, block: B:19:0x0040 A[Catch: all -> 0x007e, TryCatch #0 {all -> 0x007e, blocks: (B:3:0x0002, B:6:0x000f, B:14:0x002c, B:16:0x0032, B:28:0x006c, B:32:0x007a, B:17:0x0038, B:19:0x0040, B:21:0x004c, B:23:0x0056, B:24:0x005c, B:27:0x0069, B:7:0x0015, B:9:0x001b, B:12:0x0026), top: B:37:0x0002 }] */
    /* JADX WARN: Code duplicated, block: B:21:0x004c A[Catch: all -> 0x007e, TryCatch #0 {all -> 0x007e, blocks: (B:3:0x0002, B:6:0x000f, B:14:0x002c, B:16:0x0032, B:28:0x006c, B:32:0x007a, B:17:0x0038, B:19:0x0040, B:21:0x004c, B:23:0x0056, B:24:0x005c, B:27:0x0069, B:7:0x0015, B:9:0x001b, B:12:0x0026), top: B:37:0x0002 }] */
    /* JADX WARN: Code duplicated, block: B:23:0x0056 A[Catch: all -> 0x007e, TryCatch #0 {all -> 0x007e, blocks: (B:3:0x0002, B:6:0x000f, B:14:0x002c, B:16:0x0032, B:28:0x006c, B:32:0x007a, B:17:0x0038, B:19:0x0040, B:21:0x004c, B:23:0x0056, B:24:0x005c, B:27:0x0069, B:7:0x0015, B:9:0x001b, B:12:0x0026), top: B:37:0x0002 }] */
    /* JADX WARN: Code duplicated, block: B:24:0x005c A[Catch: all -> 0x007e, TryCatch #0 {all -> 0x007e, blocks: (B:3:0x0002, B:6:0x000f, B:14:0x002c, B:16:0x0032, B:28:0x006c, B:32:0x007a, B:17:0x0038, B:19:0x0040, B:21:0x004c, B:23:0x0056, B:24:0x005c, B:27:0x0069, B:7:0x0015, B:9:0x001b, B:12:0x0026), top: B:37:0x0002 }] */
    /* JADX WARN: Code duplicated, block: B:26:0x0068  */
    /* JADX WARN: Code duplicated, block: B:30:0x0078  */
    /* JADX WARN: Code duplicated, block: B:31:0x0079  */
    public static JSONObject a(Context context) {
        String strD;
        String str;
        String strE;
        String strA;
        String strF;
        try {
            JSONObject jSONObject = new JSONObject();
            String str2 = "";
            if (!b()) {
                if (a()) {
                    strD = d(context);
                    if (TextUtils.isEmpty(strD)) {
                        cn.jiguang.w.a.e("IdHelper", "not get ids by mitts");
                    }
                }
                if (TextUtils.isEmpty(strD)) {
                    str = Build.MANUFACTURER;
                    if (!TextUtils.isEmpty(str)) {
                        if (str.toLowerCase().equals("xiaomi")) {
                            strA = cn.jiguang.t.a.a(context);
                            if (!TextUtils.isEmpty(strA)) {
                                jSONObject = new JSONObject(strA);
                            }
                        } else {
                            strE = e(context);
                            if (TextUtils.isEmpty(strE)) {
                                strE = "";
                            }
                            jSONObject.put("oaid", strE);
                        }
                    }
                } else {
                    jSONObject = new JSONObject(strD);
                }
                strF = f(context);
                if (TextUtils.isEmpty(strF)) {
                    str2 = strF;
                }
                jSONObject.put("gadid", str2);
                return jSONObject;
            }
            cn.jiguang.w.a.f("IdHelper", "not get id in main thread");
            strD = "";
            if (TextUtils.isEmpty(strD)) {
                jSONObject = new JSONObject(strD);
            } else {
                str = Build.MANUFACTURER;
                if (!TextUtils.isEmpty(str)) {
                    if (str.toLowerCase().equals("xiaomi")) {
                        strA = cn.jiguang.t.a.a(context);
                        if (!TextUtils.isEmpty(strA)) {
                            jSONObject = new JSONObject(strA);
                        }
                    } else {
                        strE = e(context);
                        if (TextUtils.isEmpty(strE)) {
                            strE = "";
                        }
                        jSONObject.put("oaid", strE);
                    }
                }
            }
            strF = f(context);
            if (TextUtils.isEmpty(strF)) {
                str2 = strF;
            }
            jSONObject.put("gadid", str2);
            return jSONObject;
        } catch (Throwable th) {
            cn.jiguang.w.a.e("IdHelper", "[getAllIds] failed：" + th.getMessage());
            return null;
        }
    }

    private static boolean a() {
        try {
            cn.jiguang.w.a.a("IdHelper", "MdidSdkHelper name:" + MdidSdkHelper.class.getCanonicalName());
            return true;
        } catch (Throwable unused) {
            cn.jiguang.w.a.e("IdHelper", "not found mitt class");
            return false;
        }
    }

    private static boolean b() {
        try {
            return Looper.getMainLooper().getThread().getId() == Thread.currentThread().getId();
        } catch (Throwable th) {
            cn.jiguang.w.a.e("IdHelper", "[isMainThread] failed:" + th.getMessage());
            return true;
        }
    }

    private static String d(Context context) {
        try {
            FutureTask futureTask = new FutureTask(new d(context));
            new Thread(futureTask).start();
            return (String) futureTask.get(1L, TimeUnit.SECONDS);
        } catch (Throwable th) {
            cn.jiguang.w.a.e("IdHelper", "getMittIds failed:" + th.getMessage());
            return "";
        }
    }

    private static String e(Context context) {
        try {
            if (b()) {
                cn.jiguang.w.a.e("IdHelper", "not get id in main thread");
                return "";
            }
            FutureTask futureTask = new FutureTask(new c(context));
            new Thread(futureTask).start();
            return (String) futureTask.get(1L, TimeUnit.SECONDS);
        } catch (Throwable th) {
            cn.jiguang.w.a.e("IdHelper", "getOaidByLocal failed:" + th.getMessage());
            return "";
        }
    }

    private static String f(Context context) {
        try {
            if (b()) {
                cn.jiguang.w.a.e("IdHelper", "not get id in main thread");
                return "";
            }
            FutureTask futureTask = new FutureTask(new b(context));
            new Thread(futureTask).start();
            return (String) futureTask.get(1L, TimeUnit.SECONDS);
        } catch (Throwable th) {
            cn.jiguang.w.a.e("IdHelper", "getGoogleAdid failed:" + th.getMessage());
            return "";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String g(Context context) {
        if (b()) {
            cn.jiguang.w.a.f("IdHelper", "not get id in main thread");
            return "";
        }
        try {
            context.getPackageManager().getPackageInfo("com.android.vending", 0);
            try {
                Intent intent = new Intent("com.google.android.gms.ads.identifier.service.START");
                intent.setPackage("com.google.android.gms");
                cn.jiguang.r.b bVar = new cn.jiguang.r.b();
                if (context.bindService(intent, bVar, 1)) {
                    try {
                        if (bVar.a) {
                            cn.jiguang.w.a.f("IdHelper", "repeat bind");
                            context.unbindService(bVar);
                            return "";
                        }
                        cn.jiguang.r.a aVar = new cn.jiguang.r.a(bVar.b.take());
                        cn.jiguang.w.a.a("IdHelper", "google ad id:" + aVar.a());
                        String strA = aVar.a();
                        context.unbindService(bVar);
                        return strA;
                    } catch (Throwable unused) {
                        context.unbindService(bVar);
                    }
                } else {
                    cn.jiguang.w.a.e("IdHelper", "bind failed");
                }
            } catch (Throwable th) {
                cn.jiguang.w.a.e("IdHelper", "getAdvertisingIdInfo Exception: " + th.toString());
            }
            return "";
        } catch (Throwable unused2) {
            cn.jiguang.w.a.b("IdHelper", "not supported google");
            return "";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String h(Context context) {
        try {
            context.getPackageManager().getPackageInfo("com.huawei.hwid", 0);
            try {
                Intent intent = new Intent("com.uodis.opendevice.OPENIDS_SERVICE");
                intent.setPackage("com.huawei.hwid");
                cn.jiguang.s.b bVar = new cn.jiguang.s.b();
                if (context.bindService(intent, bVar, 1)) {
                    try {
                        if (bVar.a) {
                            cn.jiguang.w.a.f("IdHelper", "repeat bind");
                            context.unbindService(bVar);
                            return "";
                        }
                        cn.jiguang.s.a aVar = new cn.jiguang.s.a(bVar.b.take());
                        cn.jiguang.w.a.a("IdHelper", "oaid:" + aVar.a());
                        String strA = aVar.a();
                        context.unbindService(bVar);
                        return strA;
                    } catch (Throwable unused) {
                        context.unbindService(bVar);
                    }
                } else {
                    cn.jiguang.w.a.e("IdHelper", "bind failed");
                }
            } catch (Throwable th) {
                cn.jiguang.w.a.e("IdHelper", "getAdvertisingIdInfo Exception: " + th.toString());
            }
        } catch (Throwable unused2) {
        }
        return "";
    }
}
