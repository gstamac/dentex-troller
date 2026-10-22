package com.amap.api.mapcore.util;

import android.content.Context;
import android.text.TextUtils;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

/* JADX INFO: compiled from: Log.java */
/* JADX INFO: loaded from: classes.dex */
public class fl {
    public static final String a = "/a/";
    static final String b = "b";
    static final String c = "c";
    static final String d = "d";
    public static final String e = "g";
    public static final String f = "h";
    public static final String g = "e";
    public static final String h = "f";

    public static String a(Context context, String str) {
        return context.getFilesDir().getAbsolutePath() + a + str;
    }

    public static Class<? extends gc> a(int i) {
        if (i == 0) {
            return fx.class;
        }
        if (i == 1) {
            return fz.class;
        }
        if (i != 2) {
            return null;
        }
        return fw.class;
    }

    public static gc b(int i) {
        if (i == 0) {
            return new fx();
        }
        if (i == 1) {
            return new fz();
        }
        if (i != 2) {
            return null;
        }
        return new fw();
    }

    public static String c(int i) {
        if (i == 0) {
            return c;
        }
        if (i != 1) {
            return i != 2 ? "" : d;
        }
        return b;
    }

    static fr a(Context context, int i) {
        if (i == 0) {
            return new fp(i);
        }
        if (i == 1) {
            return new fq(i);
        }
        if (i != 2) {
            return null;
        }
        return new fo(i);
    }

    static void a(final Context context, final ff ffVar, final String str, final String str2) {
        ExecutorService executorServiceC;
        try {
            if (ffVar.f() && (executorServiceC = fn.c()) != null && !executorServiceC.isShutdown()) {
                executorServiceC.submit(new Runnable() { // from class: com.amap.api.mapcore.util.fl.1
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            fr frVarA = fl.a(context, 1);
                            if (TextUtils.isEmpty(str2)) {
                                frVarA.a(ffVar, context, new Throwable("gpsstatistics"), str, (String) null, (String) null);
                            } else {
                                frVarA.a(ffVar, context, str2, str, (String) null, (String) null);
                            }
                        } catch (Throwable th) {
                            th.printStackTrace();
                        }
                    }
                });
            }
        } catch (RejectedExecutionException unused) {
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    static void a(final Context context, final Throwable th, final int i, final String str, final String str2) {
        try {
            ExecutorService executorServiceC = fn.c();
            if (executorServiceC != null && !executorServiceC.isShutdown()) {
                executorServiceC.submit(new Runnable() { // from class: com.amap.api.mapcore.util.fl.2
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            fr frVarA = fl.a(context, i);
                            if (frVarA == null) {
                                return;
                            }
                            frVarA.a(context, th, str, str2);
                        } catch (Throwable th2) {
                            th2.printStackTrace();
                        }
                    }
                });
            }
        } catch (RejectedExecutionException unused) {
        } catch (Throwable th2) {
            th2.printStackTrace();
        }
    }

    static void a(Context context) {
        try {
            fr frVarA = a(context, 2);
            if (frVarA == null) {
                return;
            }
            frVarA.b(context);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    static void b(final Context context) {
        try {
            ExecutorService executorServiceC = fn.c();
            if (executorServiceC != null && !executorServiceC.isShutdown()) {
                executorServiceC.submit(new Runnable() { // from class: com.amap.api.mapcore.util.fl.3
                    @Override // java.lang.Runnable
                    public void run() {
                        fr frVar;
                        fr frVarA;
                        fr frVar2;
                        fr frVarA2 = null;
                        try {
                            fr frVarA3 = fl.a(context, 0);
                            try {
                                frVarA = fl.a(context, 1);
                                try {
                                    frVarA2 = fl.a(context, 2);
                                    frVarA3.c(context);
                                    frVarA.c(context);
                                    frVarA2.c(context);
                                    hj.a(context);
                                    hh.a(context);
                                    List<fr.b> listA = fr.a();
                                    if (listA.size() > 0) {
                                        Iterator<fr.b> it = listA.iterator();
                                        while (it.hasNext()) {
                                            it.next().a(context);
                                        }
                                    }
                                    if (frVarA3 != null) {
                                        frVarA3.d();
                                    }
                                    if (frVarA != null) {
                                        frVarA.d();
                                    }
                                    if (frVarA2 != null) {
                                        frVarA2.d();
                                    }
                                } catch (RejectedExecutionException unused) {
                                    frVar2 = frVarA2;
                                    frVarA2 = frVarA3;
                                    if (frVarA2 != null) {
                                        frVarA2.d();
                                    }
                                    if (frVarA != null) {
                                        frVarA.d();
                                    }
                                    if (frVar2 != null) {
                                        frVar2.d();
                                    }
                                } catch (Throwable th) {
                                    th = th;
                                    frVar = frVarA2;
                                    frVarA2 = frVarA3;
                                    try {
                                        fk.a(th, "Log", "processLog");
                                    } finally {
                                        if (frVarA2 != null) {
                                            frVarA2.d();
                                        }
                                        if (frVarA != null) {
                                            frVarA.d();
                                        }
                                        if (frVar != null) {
                                            frVar.d();
                                        }
                                    }
                                }
                            } catch (RejectedExecutionException unused2) {
                                frVarA = null;
                                frVarA2 = frVarA3;
                                frVar2 = null;
                            } catch (Throwable th2) {
                                th = th2;
                                frVarA = null;
                                frVarA2 = frVarA3;
                                frVar = null;
                            }
                        } catch (RejectedExecutionException unused3) {
                            frVar2 = null;
                            frVarA = null;
                        } catch (Throwable th3) {
                            th = th3;
                            frVar = null;
                            frVarA = null;
                        }
                    }
                });
            }
        } catch (Throwable th) {
            fk.a(th, "Log", "processLog");
        }
    }
}
