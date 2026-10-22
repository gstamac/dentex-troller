package cn.jiguang.u;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class a extends cn.jiguang.l.a {
    private static volatile a b;
    private Context a;

    private static Bundle a(long j, double d, double d2) {
        Bundle bundle = new Bundle();
        bundle.putLong("time", j);
        bundle.putDouble("lot", d);
        bundle.putDouble("lat", d2);
        return bundle;
    }

    public static a a() {
        if (b == null) {
            synchronized (a.class) {
                b = new a();
            }
        }
        return b;
    }

    private JSONObject a(List<cn.jiguang.v.c> list, cn.jiguang.v.a aVar, cn.jiguang.v.b bVar) {
        String str;
        boolean z;
        String str2;
        String str3;
        JSONObject jSONObject = new JSONObject();
        String str4 = "JLocation";
        boolean z2 = false;
        if (list == null || list.isEmpty()) {
            str = "JLocation";
        } else {
            JSONArray jSONArray = new JSONArray();
            int i = 0;
            boolean z3 = false;
            while (i < list.size()) {
                try {
                    JSONObject jSONObject2 = new JSONObject();
                    cn.jiguang.v.c cVar = list.get(i);
                    str2 = str4;
                    try {
                        jSONObject2.put("itime", cVar.a);
                        if (cVar.c != null) {
                            jSONObject2.put("tag", cVar.c);
                        }
                        jSONObject2.put("ssid", cVar.b);
                        jSONObject2.put("mac_address", cVar.e);
                        jSONObject2.put("signal_strength", cVar.d);
                        jSONObject2.put("age", 0);
                        jSONArray.put(jSONObject2);
                        jSONObject.put("wifi", jSONArray);
                        str3 = str2;
                        z3 = true;
                    } catch (JSONException e) {
                        e = e;
                        str3 = str2;
                        cn.jiguang.w.a.d(str3, "package wifi json exception:" + e.getMessage());
                    }
                } catch (JSONException e2) {
                    e = e2;
                    str2 = str4;
                }
                i++;
                str4 = str3;
            }
            str = str4;
            z2 = z3;
        }
        if (aVar != null) {
            try {
                JSONArray jSONArray2 = new JSONArray();
                JSONObject jSONObject3 = new JSONObject();
                jSONObject3.put("itime", aVar.a);
                jSONObject3.put("radio_type", aVar.g);
                jSONObject3.put("generation", aVar.h);
                jSONObject3.put("carrier", aVar.i);
                jSONObject3.put("mobile_country_code", aVar.b);
                jSONObject3.put("mobile_network_code", aVar.c);
                jSONObject3.put("signal_strength", aVar.f);
                jSONObject3.put("cell_id", aVar.e);
                jSONObject3.put("location_area_code", aVar.d);
                jSONArray2.put(jSONObject3);
                jSONObject.put("cell", jSONArray2);
                z2 = true;
            } catch (JSONException e3) {
                cn.jiguang.w.a.d(str, "package cell json exception:" + e3.getMessage());
            }
        }
        if (bVar != null) {
            try {
                JSONArray jSONArray3 = new JSONArray();
                JSONObject jSONObject4 = new JSONObject();
                jSONObject4.put("itime", bVar.a);
                jSONObject4.put("tag", bVar.b);
                jSONObject4.put("lat", bVar.c);
                jSONObject4.put("lng", bVar.d);
                jSONObject4.put("alt", bVar.e);
                jSONObject4.put("bear", bVar.f);
                jSONObject4.put("acc", bVar.g);
                jSONArray3.put(jSONObject4);
                jSONObject.put("gps", jSONArray3);
                z = true;
            } catch (JSONException e4) {
                cn.jiguang.w.a.d(str, "package gps json exception:" + e4.getMessage());
                z = z2;
            }
        } else {
            z = z2;
        }
        if (z) {
            try {
                cn.jiguang.d.a.a(this.a, jSONObject, "loc_info");
                jSONObject.put("local_dns", cn.jiguang.f.a.d());
                jSONObject.put("network_type", cn.jiguang.f.a.j(this.a));
            } catch (JSONException e5) {
                cn.jiguang.w.a.d(str, "package json exception:" + e5.getMessage());
            }
        }
        return jSONObject;
    }

    private JSONObject a(JSONObject jSONObject, JSONObject jSONObject2) {
        JSONArray jSONArray;
        cn.jiguang.w.a.a("JLocation", "currentJson:" + jSONObject + ",cacheJson:" + jSONObject2);
        if (jSONObject == null) {
            return jSONObject2;
        }
        if (jSONObject2 != null) {
            try {
                jSONArray = jSONObject2.getJSONArray("content");
            } catch (JSONException unused) {
                jSONArray = new JSONArray();
            }
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject jSONObjectOptJSONObject = jSONArray.optJSONObject(i);
                JSONArray jSONArrayOptJSONArray = jSONObjectOptJSONObject.optJSONArray("wifi");
                JSONArray jSONArrayOptJSONArray2 = jSONObjectOptJSONObject.optJSONArray("cell");
                JSONArray jSONArrayOptJSONArray3 = jSONObjectOptJSONObject.optJSONArray("gps");
                JSONArray jSONArrayOptJSONArray4 = jSONObject.optJSONArray("wifi");
                JSONArray jSONArrayOptJSONArray5 = jSONObject.optJSONArray("cell");
                JSONArray jSONArrayOptJSONArray6 = jSONObject.optJSONArray("gps");
                if (jSONArrayOptJSONArray != null && jSONArrayOptJSONArray.equals(jSONArrayOptJSONArray4)) {
                    jSONObject.remove("wifi");
                }
                if (jSONArrayOptJSONArray2 != null && jSONArrayOptJSONArray2.equals(jSONArrayOptJSONArray5)) {
                    jSONObject.remove("cell");
                }
                if (jSONArrayOptJSONArray3 != null && jSONArrayOptJSONArray3.equals(jSONArrayOptJSONArray6)) {
                    jSONObject.remove("gps");
                }
            }
            if (jSONObject.length() != 0) {
                try {
                    jSONObject.put("network_type", cn.jiguang.f.a.j(this.a));
                    jSONObject.put("local_dns", cn.jiguang.f.a.d());
                    cn.jiguang.d.a.a(this.a, jSONObject, "loc_info");
                    jSONArray.put(jSONObject);
                } catch (JSONException e) {
                    cn.jiguang.w.a.d("JLocation", "package json exception:" + e.getMessage());
                }
            }
            return jSONObject2;
        }
        try {
            JSONObject jSONObject3 = new JSONObject();
            try {
                JSONArray jSONArray2 = new JSONArray();
                jSONArray2.put(jSONObject);
                jSONObject3.put("content", jSONArray2);
                return jSONObject3;
            } catch (JSONException e2) {
                e = e2;
                jSONObject2 = jSONObject3;
                cn.jiguang.w.a.d("JLocation", "mergeJson exception:" + e.getMessage());
                return jSONObject2;
            }
        } catch (JSONException e3) {
            e = e3;
        }
    }

    @Override // cn.jiguang.l.a
    protected final String a(Context context) {
        this.a = context;
        return "JLocation";
    }

    @Override // cn.jiguang.l.a
    protected final void a(JSONObject jSONObject) {
        int iOptInt = jSONObject.optInt("cmd");
        JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("content");
        if (iOptInt != 5) {
            if (iOptInt == 45) {
                cn.jiguang.l.e.a(this.a, "JLocation", jSONObjectOptJSONObject.optLong("interval", 0L) * 1000);
                return;
            }
            return;
        }
        boolean z = !jSONObjectOptJSONObject.optBoolean("disable", true);
        cn.jiguang.l.e.a(this.a, "JLocation", z);
        if (z) {
            cn.jiguang.l.e.b(this.a, "JLocation", jSONObjectOptJSONObject.optLong("frequency", 0L) * 1000);
        }
    }

    @Override // cn.jiguang.l.a
    protected final boolean a(Context context, String str) {
        return cn.jiguang.l.e.a(context, str);
    }

    /* JADX WARN: Code duplicated, block: B:16:0x005b A[Catch: all -> 0x0098, InterruptedException -> 0x00aa, ExecutionException -> 0x00b6, TimeoutException -> 0x00ca, TryCatch #7 {InterruptedException -> 0x00aa, ExecutionException -> 0x00b6, TimeoutException -> 0x00ca, all -> 0x0098, blocks: (B:10:0x0048, B:12:0x004e, B:15:0x0055, B:16:0x005b, B:18:0x0061), top: B:100:0x0048 }] */
    /* JADX WARN: Code duplicated, block: B:18:0x0061 A[Catch: all -> 0x0098, InterruptedException -> 0x00aa, ExecutionException -> 0x00b6, TimeoutException -> 0x00ca, TRY_LEAVE, TryCatch #7 {InterruptedException -> 0x00aa, ExecutionException -> 0x00b6, TimeoutException -> 0x00ca, all -> 0x0098, blocks: (B:10:0x0048, B:12:0x004e, B:15:0x0055, B:16:0x005b, B:18:0x0061), top: B:100:0x0048 }] */
    /* JADX WARN: Code duplicated, block: B:27:0x0096  */
    /* JADX WARN: Code duplicated, block: B:44:0x0102 A[Catch: all -> 0x01aa, InterruptedException -> 0x01b7, ExecutionException -> 0x01c2, TimeoutException -> 0x01d5, TryCatch #8 {InterruptedException -> 0x01b7, ExecutionException -> 0x01c2, TimeoutException -> 0x01d5, all -> 0x01aa, blocks: (B:42:0x00d0, B:44:0x0102, B:47:0x0109, B:48:0x0110, B:50:0x0116, B:52:0x0120, B:54:0x012a, B:70:0x0183, B:68:0x016b), top: B:99:0x00d0 }] */
    /* JADX WARN: Code duplicated, block: B:48:0x0110 A[Catch: all -> 0x01aa, InterruptedException -> 0x01b7, ExecutionException -> 0x01c2, TimeoutException -> 0x01d5, TryCatch #8 {InterruptedException -> 0x01b7, ExecutionException -> 0x01c2, TimeoutException -> 0x01d5, all -> 0x01aa, blocks: (B:42:0x00d0, B:44:0x0102, B:47:0x0109, B:48:0x0110, B:50:0x0116, B:52:0x0120, B:54:0x012a, B:70:0x0183, B:68:0x016b), top: B:99:0x00d0 }] */
    /* JADX WARN: Code duplicated, block: B:50:0x0116 A[Catch: all -> 0x01aa, InterruptedException -> 0x01b7, ExecutionException -> 0x01c2, TimeoutException -> 0x01d5, TryCatch #8 {InterruptedException -> 0x01b7, ExecutionException -> 0x01c2, TimeoutException -> 0x01d5, all -> 0x01aa, blocks: (B:42:0x00d0, B:44:0x0102, B:47:0x0109, B:48:0x0110, B:50:0x0116, B:52:0x0120, B:54:0x012a, B:70:0x0183, B:68:0x016b), top: B:99:0x00d0 }] */
    /* JADX WARN: Code duplicated, block: B:54:0x012a A[Catch: all -> 0x01aa, InterruptedException -> 0x01b7, ExecutionException -> 0x01c2, TimeoutException -> 0x01d5, TRY_LEAVE, TryCatch #8 {InterruptedException -> 0x01b7, ExecutionException -> 0x01c2, TimeoutException -> 0x01d5, all -> 0x01aa, blocks: (B:42:0x00d0, B:44:0x0102, B:47:0x0109, B:48:0x0110, B:50:0x0116, B:52:0x0120, B:54:0x012a, B:70:0x0183, B:68:0x016b), top: B:99:0x00d0 }] */
    /* JADX WARN: Code duplicated, block: B:58:0x015b  */
    /* JADX WARN: Code duplicated, block: B:68:0x016b A[Catch: all -> 0x01aa, InterruptedException -> 0x01b7, ExecutionException -> 0x01c2, TimeoutException -> 0x01d5, TRY_ENTER, TryCatch #8 {InterruptedException -> 0x01b7, ExecutionException -> 0x01c2, TimeoutException -> 0x01d5, all -> 0x01aa, blocks: (B:42:0x00d0, B:44:0x0102, B:47:0x0109, B:48:0x0110, B:50:0x0116, B:52:0x0120, B:54:0x012a, B:70:0x0183, B:68:0x016b), top: B:99:0x00d0 }] */
    /* JADX WARN: Code duplicated, block: B:70:0x0183 A[Catch: all -> 0x01aa, InterruptedException -> 0x01b7, ExecutionException -> 0x01c2, TimeoutException -> 0x01d5, TRY_LEAVE, TryCatch #8 {InterruptedException -> 0x01b7, ExecutionException -> 0x01c2, TimeoutException -> 0x01d5, all -> 0x01aa, blocks: (B:42:0x00d0, B:44:0x0102, B:47:0x0109, B:48:0x0110, B:50:0x0116, B:52:0x0120, B:54:0x012a, B:70:0x0183, B:68:0x016b), top: B:99:0x00d0 }] */
    /* JADX WARN: Code duplicated, block: B:84:0x01e2 A[Catch: all -> 0x01e6, TRY_LEAVE, TryCatch #4 {all -> 0x01e6, blocks: (B:82:0x01dc, B:84:0x01e2), top: B:95:0x01dc }] */
    /* JADX WARN: Code duplicated, block: B:90:0x020d  */
    /* JADX WARN: Code duplicated, block: B:95:0x01dc A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:97:0x0144 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    @Override // cn.jiguang.l.a
    protected final void b(Context context, String str) {
        String str2;
        List<cn.jiguang.v.c> list;
        cn.jiguang.v.a aVar;
        StringBuilder sbAppend;
        String string;
        StringBuilder sb;
        String message;
        StringBuilder sb2;
        String message2;
        String string2;
        JSONObject jSONObjectA;
        cn.jiguang.v.b bVar;
        String str3;
        ExecutorService executorServiceNewSingleThreadExecutor = Executors.newSingleThreadExecutor();
        cn.jiguang.v.b bVarD = null;
        try {
            try {
                if (cn.jiguang.f.a.e(context, "android.permission.ACCESS_WIFI_STATE")) {
                    if (cn.jiguang.f.a.i(context)) {
                        List<cn.jiguang.v.c> listA = new i(context).a();
                        cn.jiguang.w.a.a("JLocation", "collect wifi success:" + listA);
                        list = listA;
                    } else {
                        str2 = "collect wifi failed because not enable to collect wifi list";
                    }
                    if (!cn.jiguang.f.a.c(context, "android.permission.ACCESS_COARSE_LOCATION") || cn.jiguang.f.a.c(context, "android.permission.ACCESS_FINE_LOCATION")) {
                        if (cn.jiguang.l.h.a(context)) {
                            FutureTask futureTask = new FutureTask(new b(this));
                            executorServiceNewSingleThreadExecutor.execute(futureTask);
                            aVar = (cn.jiguang.v.a) futureTask.get(20000L, TimeUnit.MILLISECONDS);
                            try {
                                cn.jiguang.w.a.a("JLocation", "collect cell success:" + aVar.toString());
                            } catch (InterruptedException e) {
                                e = e;
                                sb = new StringBuilder("collect cell exception:");
                                message = e.getMessage();
                                sbAppend = sb.append(message);
                                string = sbAppend.toString();
                                cn.jiguang.w.a.d("JLocation", string);
                            } catch (ExecutionException e2) {
                                e = e2;
                                sb = new StringBuilder("collect cell exception:");
                                message = e.getMessage();
                                sbAppend = sb.append(message);
                                string = sbAppend.toString();
                                cn.jiguang.w.a.d("JLocation", string);
                            } catch (TimeoutException unused) {
                                string = "collect cell time out";
                                cn.jiguang.w.a.d("JLocation", string);
                            } catch (Throwable th) {
                                th = th;
                                sbAppend = new StringBuilder("collect cell throwable:").append(th.getMessage());
                                string = sbAppend.toString();
                                cn.jiguang.w.a.d("JLocation", string);
                            }
                        }
                        cn.jiguang.w.a.a("JLocation", "coares:" + cn.jiguang.f.a.c(context, "android.permission.ACCESS_COARSE_LOCATION"));
                        cn.jiguang.w.a.a("JLocation", "fine:" + cn.jiguang.f.a.c(context, "android.permission.ACCESS_FINE_LOCATION"));
                        if (cn.jiguang.f.a.c(context, "android.permission.ACCESS_COARSE_LOCATION") && !cn.jiguang.f.a.c(context, "android.permission.ACCESS_FINE_LOCATION")) {
                            cn.jiguang.w.a.d("JLocation", "collect gps failed because has no Manifest.permission.LOCATION");
                        } else if (cn.jiguang.l.h.a(context) && f.a(context).e()) {
                            bVarD = f.a(context).d();
                            if (bVarD == null) {
                                FutureTask futureTask2 = new FutureTask(new c(this));
                                executorServiceNewSingleThreadExecutor.execute(futureTask2);
                                bVar = (cn.jiguang.v.b) futureTask2.get(31000L, TimeUnit.MILLISECONDS);
                                if (bVar != null) {
                                    try {
                                        str3 = "collect gps success:" + bVar.toString();
                                    } catch (InterruptedException e3) {
                                        e = e3;
                                        bVarD = bVar;
                                        sb2 = new StringBuilder("collect gps exception:");
                                        message2 = e.getMessage();
                                        string2 = sb2.append(message2).toString();
                                        cn.jiguang.w.a.d("JLocation", string2);
                                    } catch (ExecutionException e4) {
                                        e = e4;
                                        bVarD = bVar;
                                        sb2 = new StringBuilder("collect gps exception:");
                                        message2 = e.getMessage();
                                        string2 = sb2.append(message2).toString();
                                        cn.jiguang.w.a.d("JLocation", string2);
                                    } catch (TimeoutException unused2) {
                                        bVarD = bVar;
                                        string2 = "collect gps time out";
                                        cn.jiguang.w.a.d("JLocation", string2);
                                    } catch (Throwable th2) {
                                        th = th2;
                                        bVarD = bVar;
                                        sb2 = new StringBuilder("collect gps throwable:");
                                        message2 = th.getMessage();
                                        string2 = sb2.append(message2).toString();
                                        cn.jiguang.w.a.d("JLocation", string2);
                                    }
                                } else {
                                    str3 = "collect gps failed";
                                }
                                cn.jiguang.w.a.a("JLocation", str3);
                                bVarD = bVar;
                            } else {
                                cn.jiguang.w.a.a("JLocation", "load  last gps success:" + bVarD.toString());
                            }
                            if (bVarD != null) {
                                cn.jiguang.l.e.n(context, bVarD.a + "," + bVarD.d + "," + bVarD.c);
                            }
                        }
                        if (executorServiceNewSingleThreadExecutor != null) {
                            try {
                                if (!executorServiceNewSingleThreadExecutor.isShutdown()) {
                                    executorServiceNewSingleThreadExecutor.shutdown();
                                }
                            } catch (Throwable th3) {
                                cn.jiguang.w.a.d("JLocation", "shut down failed:" + th3.getMessage());
                            }
                        }
                        jSONObjectA = a(a(list, aVar, bVarD), cn.jiguang.o.c.a(context, "rl.catch"));
                        if (jSONObjectA != null) {
                            cn.jiguang.w.a.a("JLocation", "save json:" + jSONObjectA);
                            cn.jiguang.o.c.a(context, "rl.catch", jSONObjectA);
                        }
                        super.b(context, str);
                    }
                    cn.jiguang.w.a.d("JLocation", "collect cell failed because has no Manifest.permission.LOCATION");
                    aVar = null;
                    cn.jiguang.w.a.a("JLocation", "coares:" + cn.jiguang.f.a.c(context, "android.permission.ACCESS_COARSE_LOCATION"));
                    cn.jiguang.w.a.a("JLocation", "fine:" + cn.jiguang.f.a.c(context, "android.permission.ACCESS_FINE_LOCATION"));
                    if (cn.jiguang.f.a.c(context, "android.permission.ACCESS_COARSE_LOCATION")) {
                        if (cn.jiguang.l.h.a(context)) {
                            bVarD = f.a(context).d();
                            if (bVarD == null) {
                                FutureTask futureTask3 = new FutureTask(new c(this));
                                executorServiceNewSingleThreadExecutor.execute(futureTask3);
                                bVar = (cn.jiguang.v.b) futureTask3.get(31000L, TimeUnit.MILLISECONDS);
                                if (bVar != null) {
                                    str3 = "collect gps success:" + bVar.toString();
                                } else {
                                    str3 = "collect gps failed";
                                }
                                cn.jiguang.w.a.a("JLocation", str3);
                                bVarD = bVar;
                            } else {
                                cn.jiguang.w.a.a("JLocation", "load  last gps success:" + bVarD.toString());
                            }
                            if (bVarD != null) {
                                cn.jiguang.l.e.n(context, bVarD.a + "," + bVarD.d + "," + bVarD.c);
                            }
                        }
                    } else if (cn.jiguang.l.h.a(context)) {
                        bVarD = f.a(context).d();
                        if (bVarD == null) {
                            FutureTask futureTask4 = new FutureTask(new c(this));
                            executorServiceNewSingleThreadExecutor.execute(futureTask4);
                            bVar = (cn.jiguang.v.b) futureTask4.get(31000L, TimeUnit.MILLISECONDS);
                            if (bVar != null) {
                                str3 = "collect gps success:" + bVar.toString();
                            } else {
                                str3 = "collect gps failed";
                            }
                            cn.jiguang.w.a.a("JLocation", str3);
                            bVarD = bVar;
                        } else {
                            cn.jiguang.w.a.a("JLocation", "load  last gps success:" + bVarD.toString());
                        }
                        if (bVarD != null) {
                            cn.jiguang.l.e.n(context, bVarD.a + "," + bVarD.d + "," + bVarD.c);
                        }
                    }
                    if (executorServiceNewSingleThreadExecutor != null) {
                        if (!executorServiceNewSingleThreadExecutor.isShutdown()) {
                            executorServiceNewSingleThreadExecutor.shutdown();
                        }
                    }
                    jSONObjectA = a(a(list, aVar, bVarD), cn.jiguang.o.c.a(context, "rl.catch"));
                    if (jSONObjectA != null) {
                        cn.jiguang.w.a.a("JLocation", "save json:" + jSONObjectA);
                        cn.jiguang.o.c.a(context, "rl.catch", jSONObjectA);
                    }
                    super.b(context, str);
                }
                str2 = "collect wifi failed because has no android.permission.ACCESS_WIFI_STATE";
                if (cn.jiguang.f.a.c(context, "android.permission.ACCESS_COARSE_LOCATION")) {
                    if (cn.jiguang.l.h.a(context)) {
                        FutureTask futureTask5 = new FutureTask(new b(this));
                        executorServiceNewSingleThreadExecutor.execute(futureTask5);
                        aVar = (cn.jiguang.v.a) futureTask5.get(20000L, TimeUnit.MILLISECONDS);
                        cn.jiguang.w.a.a("JLocation", "collect cell success:" + aVar.toString());
                    } else {
                        aVar = null;
                    }
                } else if (cn.jiguang.l.h.a(context)) {
                    FutureTask futureTask6 = new FutureTask(new b(this));
                    executorServiceNewSingleThreadExecutor.execute(futureTask6);
                    aVar = (cn.jiguang.v.a) futureTask6.get(20000L, TimeUnit.MILLISECONDS);
                    cn.jiguang.w.a.a("JLocation", "collect cell success:" + aVar.toString());
                } else {
                    aVar = null;
                }
            } catch (InterruptedException e5) {
                e = e5;
                aVar = null;
            } catch (ExecutionException e6) {
                e = e6;
                aVar = null;
            } catch (TimeoutException unused3) {
                aVar = null;
            } catch (Throwable th4) {
                th = th4;
                aVar = null;
            }
            cn.jiguang.w.a.a("JLocation", "coares:" + cn.jiguang.f.a.c(context, "android.permission.ACCESS_COARSE_LOCATION"));
            cn.jiguang.w.a.a("JLocation", "fine:" + cn.jiguang.f.a.c(context, "android.permission.ACCESS_FINE_LOCATION"));
            if (cn.jiguang.f.a.c(context, "android.permission.ACCESS_COARSE_LOCATION")) {
                if (cn.jiguang.l.h.a(context)) {
                    bVarD = f.a(context).d();
                    if (bVarD == null) {
                        FutureTask futureTask7 = new FutureTask(new c(this));
                        executorServiceNewSingleThreadExecutor.execute(futureTask7);
                        bVar = (cn.jiguang.v.b) futureTask7.get(31000L, TimeUnit.MILLISECONDS);
                        if (bVar != null) {
                            str3 = "collect gps success:" + bVar.toString();
                        } else {
                            str3 = "collect gps failed";
                        }
                        cn.jiguang.w.a.a("JLocation", str3);
                        bVarD = bVar;
                    } else {
                        cn.jiguang.w.a.a("JLocation", "load  last gps success:" + bVarD.toString());
                    }
                    if (bVarD != null) {
                        cn.jiguang.l.e.n(context, bVarD.a + "," + bVarD.d + "," + bVarD.c);
                    }
                }
            } else if (cn.jiguang.l.h.a(context)) {
                bVarD = f.a(context).d();
                if (bVarD == null) {
                    FutureTask futureTask8 = new FutureTask(new c(this));
                    executorServiceNewSingleThreadExecutor.execute(futureTask8);
                    bVar = (cn.jiguang.v.b) futureTask8.get(31000L, TimeUnit.MILLISECONDS);
                    if (bVar != null) {
                        str3 = "collect gps success:" + bVar.toString();
                    } else {
                        str3 = "collect gps failed";
                    }
                    cn.jiguang.w.a.a("JLocation", str3);
                    bVarD = bVar;
                } else {
                    cn.jiguang.w.a.a("JLocation", "load  last gps success:" + bVarD.toString());
                }
                if (bVarD != null) {
                    cn.jiguang.l.e.n(context, bVarD.a + "," + bVarD.d + "," + bVarD.c);
                }
            }
        } catch (InterruptedException e7) {
            e = e7;
        } catch (ExecutionException e8) {
            e = e8;
        } catch (TimeoutException unused4) {
        } catch (Throwable th5) {
            th = th5;
        }
        cn.jiguang.w.a.d("JLocation", str2);
        list = null;
        if (executorServiceNewSingleThreadExecutor != null) {
            if (!executorServiceNewSingleThreadExecutor.isShutdown()) {
                executorServiceNewSingleThreadExecutor.shutdown();
            }
        }
        jSONObjectA = a(a(list, aVar, bVarD), cn.jiguang.o.c.a(context, "rl.catch"));
        if (jSONObjectA != null) {
            cn.jiguang.w.a.a("JLocation", "save json:" + jSONObjectA);
            cn.jiguang.o.c.a(context, "rl.catch", jSONObjectA);
        }
        super.b(context, str);
    }

    @Override // cn.jiguang.l.a
    protected final boolean b() {
        return cn.jiguang.l.e.h(this.a, "JLocation");
    }

    @Override // cn.jiguang.l.a
    protected final boolean c() {
        return cn.jiguang.l.e.i(this.a, "JLocation");
    }

    @Override // cn.jiguang.l.a
    protected final void d(Context context, String str) {
        String str2;
        String str3;
        JSONObject jSONObjectA = cn.jiguang.o.c.a(context, "rl.catch");
        if (jSONObjectA == null) {
            str2 = "JLocation";
            str3 = "there are no data to report";
        } else {
            JSONArray jSONArrayOptJSONArray = jSONObjectA.optJSONArray("content");
            if (jSONArrayOptJSONArray != null) {
                cn.jiguang.l.h.a(context, jSONArrayOptJSONArray);
                cn.jiguang.w.a.a("JLocation", "clean cache json");
                if (context != null && !TextUtils.isEmpty("rl.catch")) {
                    try {
                        if (!cn.jiguang.o.c.a(context, "rl.catch", (JSONObject) null)) {
                            synchronized ("rl.catch") {
                                context.deleteFile("rl.catch");
                            }
                        }
                    } catch (Throwable th) {
                        cn.jiguang.w.a.d("JCommonFileHelper", "cleanJson throwable:" + th.getMessage());
                    }
                }
                super.d(context, str);
                return;
            }
            str2 = "JLocation";
            str3 = "there are no content data to report";
        }
        cn.jiguang.w.a.a(str2, str3);
    }

    @Override // cn.jiguang.l.a
    protected final boolean d(Context context) {
        return true;
    }

    @Override // cn.jiguang.l.a
    /* JADX INFO: renamed from: f, reason: merged with bridge method [inline-methods] */
    public final Bundle e(Context context) {
        long jLongValue;
        double dDoubleValue;
        double dDoubleValue2;
        String strB = cn.jiguang.l.e.b(context);
        if (TextUtils.isEmpty(strB)) {
            jLongValue = 0;
            dDoubleValue = 200.0d;
            dDoubleValue2 = 200.0d;
        } else {
            String[] strArrSplit = strB.split(",");
            jLongValue = Long.valueOf(strArrSplit[0]).longValue();
            dDoubleValue = Double.valueOf(strArrSplit[1]).doubleValue();
            dDoubleValue2 = Double.valueOf(strArrSplit[2]).doubleValue();
        }
        return a(jLongValue, dDoubleValue, dDoubleValue2);
    }
}
