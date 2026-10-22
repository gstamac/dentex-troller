package com.amap.api.mapcore.util;

import android.content.Context;
import android.os.Build;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import cn.jpush.android.api.JThirdPlatFormInterface;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import org.json.JSONObject;

/* JADX INFO: compiled from: OfflineLocManager.java */
/* JADX INFO: loaded from: classes.dex */
public class hh {
    static int a = 1000;
    static boolean b = false;

    public static synchronized void a(int i, boolean z) {
        a = i;
        b = z;
    }

    public static synchronized void a(final hg hgVar, final Context context) {
        fn.c().submit(new Runnable() { // from class: com.amap.api.mapcore.util.hh.1
            /* JADX WARN: Code duplicated, block: B:62:0x0082 A[EXC_TOP_SPLITTER, SYNTHETIC] */
            @Override // java.lang.Runnable
            public void run() {
                gs gsVarA;
                synchronized (hh.class) {
                    OutputStream outputStreamA = null;
                    try {
                        byte[] bArrA = hgVar.a();
                        gsVarA = gs.a(new File(fl.a(context, fl.h)), 1, 1, PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE);
                        try {
                            gsVarA.a(hh.a);
                            gs.a aVarB = gsVarA.b(Long.toString(System.currentTimeMillis()));
                            outputStreamA = aVarB.a(0);
                            outputStreamA.write(bArrA);
                            aVarB.a();
                            gsVarA.e();
                            if (outputStreamA != null) {
                                try {
                                    outputStreamA.close();
                                } catch (Throwable th) {
                                    th.printStackTrace();
                                }
                            }
                            if (gsVarA != null) {
                                try {
                                    gsVarA.close();
                                } catch (Throwable th2) {
                                    th = th2;
                                    th.printStackTrace();
                                }
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            try {
                                fk.a(th, "OfflineLocManager", "applyOfflineLocEntity");
                                if (outputStreamA != null) {
                                    try {
                                        outputStreamA.close();
                                    } catch (Throwable th4) {
                                        th4.printStackTrace();
                                    }
                                }
                                if (gsVarA != null) {
                                    try {
                                        gsVarA.close();
                                    } catch (Throwable th5) {
                                        th = th5;
                                        th.printStackTrace();
                                    }
                                }
                            } catch (Throwable th6) {
                                if (outputStreamA == null) {
                                    if (gsVarA != null) {
                                        gsVarA.close();
                                    }
                                    throw th6;
                                }
                                try {
                                    outputStreamA.close();
                                } catch (Throwable th7) {
                                    th7.printStackTrace();
                                }
                                if (gsVarA != null) {
                                    try {
                                        gsVarA.close();
                                    } catch (Throwable th8) {
                                        th8.printStackTrace();
                                    }
                                }
                                throw th6;
                            }
                        }
                    } catch (Throwable th9) {
                        th = th9;
                        gsVarA = null;
                    }
                }
            }
        });
    }

    /* JADX WARN: Code duplicated, block: B:36:0x0088 A[Catch: all -> 0x0076, TRY_ENTER, TryCatch #2 {all -> 0x0076, blocks: (B:25:0x006c, B:27:0x0072, B:36:0x0088, B:38:0x008e), top: B:54:0x0003 }] */
    /* JADX WARN: Code duplicated, block: B:38:0x008e A[Catch: all -> 0x0076, TRY_LEAVE, TryCatch #2 {all -> 0x0076, blocks: (B:25:0x006c, B:27:0x0072, B:36:0x0088, B:38:0x008e), top: B:54:0x0003 }] */
    /* JADX WARN: Code duplicated, block: B:66:? A[RETURN, SYNTHETIC] */
    public static void a(Context context) {
        gs gsVar = null;
        try {
            try {
                try {
                    if (!c(context)) {
                        return;
                    }
                    gs gsVarA = gs.a(new File(fl.a(context, fl.h)), 1, 1, PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE);
                    try {
                        String strA = a(context, gsVarA);
                        if (TextUtils.isEmpty(strA)) {
                            if (gsVarA != null) {
                                try {
                                    if (gsVarA.d()) {
                                        return;
                                    }
                                    gsVarA.close();
                                    return;
                                } catch (Throwable th) {
                                    th.printStackTrace();
                                    return;
                                }
                            }
                            return;
                        }
                        JSONObject jSONObject = new JSONObject(new String(gw.a().b(new fm(fg.c(fg.a(strA)), "6"))));
                        if (jSONObject.has(JThirdPlatFormInterface.KEY_CODE) && jSONObject.getInt(JThirdPlatFormInterface.KEY_CODE) == 1) {
                            b(gsVarA);
                        } else {
                            gsVar = gsVarA;
                        }
                        if (gsVar != null) {
                            if (!gsVar.d()) {
                                gsVar.close();
                            }
                        }
                        return;
                    } catch (Throwable th2) {
                        th = th2;
                        gsVar = gsVarA;
                        fk.a(th, "OfflineLocManager", "updateOfflineLocData");
                        if (gsVar == null) {
                        }
                        if (!gsVar.d()) {
                            gsVar.close();
                        }
                    }
                } catch (Throwable th3) {
                    th3.printStackTrace();
                    return;
                }
            } catch (Throwable th4) {
                th = th4;
            }
            fk.a(th, "OfflineLocManager", "updateOfflineLocData");
            if (gsVar == null) {
                if (!gsVar.d()) {
                    gsVar.close();
                }
            }
        } catch (Throwable th5) {
            if (gsVar != null) {
                try {
                    if (!gsVar.d()) {
                        gsVar.close();
                    }
                } catch (Throwable th6) {
                    th6.printStackTrace();
                }
            }
            throw th5;
        }
    }

    private static void a(Context context, long j) {
        FileOutputStream fileOutputStream = null;
        try {
            try {
                File file = new File(fl.a(context, "f.log"));
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                FileOutputStream fileOutputStream2 = new FileOutputStream(file);
                try {
                    fileOutputStream2.write(fg.a(String.valueOf(j)));
                    fileOutputStream2.close();
                } catch (Throwable th) {
                    fileOutputStream = fileOutputStream2;
                    th = th;
                    try {
                        fk.a(th, "OfflineLocManager", "updateLogUpdateTime");
                        if (fileOutputStream == null) {
                        } else {
                            fileOutputStream.close();
                        }
                    } catch (Throwable th2) {
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.close();
                            } catch (Throwable th3) {
                                th3.printStackTrace();
                            }
                        }
                        throw th2;
                    }
                }
            } catch (Throwable th4) {
                th4.printStackTrace();
            }
        } catch (Throwable th5) {
            th = th5;
        }
    }

    private static String a(gs gsVar) {
        StringBuilder sb = new StringBuilder();
        try {
            File fileC = gsVar.c();
            if (fileC != null && fileC.exists()) {
                boolean z = true;
                for (String str : fileC.list()) {
                    if (str.contains(".0")) {
                        String strA = fg.a(hk.a(gsVar, str.split("\\.")[0], false));
                        if (z) {
                            z = false;
                        } else {
                            sb.append(",");
                        }
                        sb.append("{\"log\":\"").append(strA).append("\"}");
                    }
                }
            }
        } catch (Throwable th) {
            fk.a(th, "StatisticsManager", "getContent");
        }
        return sb.toString();
    }

    private static void b(gs gsVar) {
        if (gsVar != null) {
            try {
                gsVar.f();
            } catch (Throwable th) {
                fk.a(th, "StatisticsManager", "getContent");
            }
        }
    }

    private static String a(Context context, gs gsVar) {
        String strA = a(gsVar);
        if (TextUtils.isEmpty(strA)) {
            return null;
        }
        String strD = d(context);
        StringBuilder sb = new StringBuilder("{\"pinfo\":\"");
        sb.append(strD).append("\",\"els\":[");
        sb.append(strA);
        sb.append("]}");
        return sb.toString();
    }

    private static long b(Context context) {
        FileInputStream fileInputStream;
        Throwable th;
        File file = new File(fl.a(context, "f.log"));
        if (!file.exists()) {
            return 0L;
        }
        try {
            fileInputStream = new FileInputStream(file);
            try {
                byte[] bArr = new byte[fileInputStream.available()];
                fileInputStream.read(bArr);
                long j = Long.parseLong(fg.a(bArr));
                try {
                    fileInputStream.close();
                } catch (Throwable th2) {
                    th2.printStackTrace();
                }
                return j;
            } catch (Throwable th3) {
                th = th3;
                try {
                    fk.a(th, "OfflineLocManager", "getUpdateTime");
                    try {
                        if (file.exists()) {
                            file.delete();
                        }
                    } catch (Throwable th4) {
                        th4.printStackTrace();
                    }
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (Throwable th5) {
                            th5.printStackTrace();
                        }
                    }
                    return System.currentTimeMillis();
                } catch (Throwable th6) {
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (Throwable th7) {
                            th7.printStackTrace();
                        }
                    }
                    throw th6;
                }
            }
        } catch (Throwable th8) {
            fileInputStream = null;
            th = th8;
        }
    }

    private static synchronized boolean c(Context context) {
        try {
            if (fa.m(context) == 1 || b) {
                if (System.currentTimeMillis() - b(context) < 14400000) {
                    return false;
                }
                a(context, System.currentTimeMillis());
                return true;
            }
        } catch (Throwable th) {
            fk.a(th, "StatisticsManager", "isUpdate");
        }
        return false;
    }

    private static String d(Context context) {
        return ez.b(context, fg.a(e(context)));
    }

    private static String e(Context context) {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("\"key\":\"").append(ew.f(context)).append("\",\"platform\":\"android\",\"diu\":\"").append(fa.q(context)).append("\",\"mac\":\"").append(fa.i(context)).append("\",\"tid\":\"").append(fa.f(context)).append("\",\"umidt\":\"").append(fa.a()).append("\",\"manufacture\":\"").append(Build.MANUFACTURER).append("\",\"device\":\"").append(Build.DEVICE).append("\",\"sim\":\"").append(fa.r(context)).append("\",\"pkg\":\"").append(ew.c(context)).append("\",\"model\":\"").append(Build.MODEL).append("\",\"appversion\":\"").append(ew.d(context)).append("\",\"appname\":\"").append(ew.b(context)).append("\"");
        } catch (Throwable th) {
            fk.a(th, "CInfo", "getPublicJSONInfo");
        }
        return sb.toString();
    }
}
