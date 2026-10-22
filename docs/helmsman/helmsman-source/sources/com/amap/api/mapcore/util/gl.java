package com.amap.api.mapcore.util;

import android.content.Context;
import android.text.TextUtils;
import dalvik.system.DexFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.util.List;

/* JADX INFO: compiled from: DexFileManager.java */
/* JADX INFO: loaded from: classes.dex */
public class gl {

    /* JADX INFO: compiled from: DexFileManager.java */
    public static class a {
        public static void a(ft ftVar, gp gpVar, String str) {
            ftVar.a(gpVar, str);
        }

        public static gp a(ft ftVar, String str) {
            List listB = ftVar.b(gp.b(str), gp.class);
            if (listB == null || listB.size() <= 0) {
                return null;
            }
            return (gp) listB.get(0);
        }

        public static List<gp> a(ft ftVar, String str, String str2) {
            return ftVar.b(gp.b(str, str2), gp.class);
        }
    }

    static String a(String str) {
        return str + ".o";
    }

    static String a(Context context, String str, String str2) {
        return fc.b(str + str2 + fa.q(context)) + ".jar";
    }

    static String b(Context context, String str, String str2) {
        return a(context, a(context, str, str2));
    }

    static String a(Context context, String str) {
        return a(context) + File.separator + str;
    }

    static String a(Context context) {
        return context.getFilesDir().getAbsolutePath() + File.separator + "pngex";
    }

    static void a(Context context, ff ffVar) {
        try {
            String strB = b(context, ffVar.a(), ffVar.b());
            if (TextUtils.isEmpty(strB)) {
                return;
            }
            File file = new File(strB);
            File parentFile = file.getParentFile();
            if (!file.exists()) {
                if (parentFile == null || !parentFile.exists()) {
                    return;
                }
                c(context, ffVar.a(), ffVar.b());
                return;
            }
            String strA = a(context, a(file.getName()));
            DexFile dexFileLoadDex = DexFile.loadDex(strB, strA, 0);
            if (dexFileLoadDex != null) {
                dexFileLoadDex.close();
                a(context, file, strA, ffVar);
            }
        } catch (Throwable th) {
            gr.a(th, "BaseLoader", "getInstanceByThread()");
        }
    }

    static void b(Context context, String str) {
        ft ftVar = new ft(context, gn.a());
        List<gp> listA = a.a(ftVar, str, "copy");
        gr.a(listA);
        if (listA != null) {
            if (listA.size() > 1) {
                int size = listA.size();
                for (int i = 1; i < size; i++) {
                    c(context, ftVar, listA.get(i).a());
                }
            }
        }
    }

    static void a(Context context, ft ftVar, String str) {
        c(context, ftVar, str);
        c(context, ftVar, a(str));
    }

    static void c(final Context context, final String str, final String str2) {
        fn.c().submit(new Runnable() { // from class: com.amap.api.mapcore.util.gl.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    ft ftVar = new ft(context, gn.a());
                    List<gp> listB = ftVar.b(gp.a(str), gp.class);
                    if (listB == null || listB.size() <= 0) {
                        return;
                    }
                    for (gp gpVar : listB) {
                        if (!str2.equalsIgnoreCase(gpVar.d())) {
                            gl.c(context, ftVar, gpVar.a());
                        }
                    }
                } catch (Throwable th) {
                    gr.a(th, "FileManager", "clearUnSuitableV");
                }
            }
        });
    }

    static void a(ft ftVar, Context context, String str) {
        List<gp> listA = a.a(ftVar, str, "used");
        if (listA == null || listA.size() <= 0) {
            return;
        }
        for (gp gpVar : listA) {
            if (gpVar != null && gpVar.c().equals(str)) {
                a(context, ftVar, gpVar.a());
                List listB = ftVar.b(gp.a(str, gpVar.e()), gp.class);
                if (listB != null && listB.size() > 0) {
                    gp gpVar2 = (gp) listB.get(0);
                    gpVar2.c("errorstatus");
                    a.a(ftVar, gpVar2, gp.b(gpVar2.a()));
                    File file = new File(a(context, gpVar2.a()));
                    if (file.exists()) {
                        file.delete();
                    }
                }
            }
        }
    }

    static void a(Context context, ft ftVar, ff ffVar, String str, String str2) throws Throwable {
        RandomAccessFile randomAccessFile;
        FileInputStream fileInputStream = null;
        try {
            String strA = ffVar.a();
            String strA2 = a(context, strA, ffVar.b());
            a(context, ftVar, strA2);
            FileInputStream fileInputStream2 = new FileInputStream(new File(str));
            try {
                fileInputStream2.read(new byte[32]);
                File file = new File(b(context, strA, ffVar.b()));
                randomAccessFile = new RandomAccessFile(file, "rw");
                try {
                    byte[] bArr = new byte[1024];
                    int i = 0;
                    while (true) {
                        int i2 = fileInputStream2.read(bArr);
                        if (i2 <= 0) {
                            break;
                        }
                        if (i2 == 1024) {
                            randomAccessFile.seek(i);
                            randomAccessFile.write(bArr);
                        } else {
                            byte[] bArr2 = new byte[i2];
                            System.arraycopy(bArr, 0, bArr2, 0, i2);
                            randomAccessFile.seek(i);
                            randomAccessFile.write(bArr2);
                        }
                        i += i2;
                    }
                    gp gpVarA = new gp.a(strA2, fc.a(file.getAbsolutePath()), strA, ffVar.b(), str2).a("used").a();
                    a.a(ftVar, gpVarA, gp.b(gpVarA.a()));
                    try {
                        gr.a(fileInputStream2);
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                    try {
                        gr.a(randomAccessFile);
                    } catch (Throwable th2) {
                        th2.printStackTrace();
                    }
                } catch (Throwable th3) {
                    th = th3;
                    fileInputStream = fileInputStream2;
                    try {
                        throw th;
                    } catch (Throwable th4) {
                        try {
                            gr.a(fileInputStream);
                        } catch (Throwable th5) {
                            th5.printStackTrace();
                        }
                        try {
                            gr.a(randomAccessFile);
                            throw th4;
                        } catch (Throwable th6) {
                            th6.printStackTrace();
                            throw th4;
                        }
                    }
                }
            } catch (Throwable th7) {
                th = th7;
                randomAccessFile = null;
            }
        } catch (Throwable th8) {
            th = th8;
            randomAccessFile = null;
        }
    }

    static String a(Context context, ft ftVar, ff ffVar) {
        List listB = ftVar.b(gp.b(ffVar.a(), "copy"), gp.class);
        String strE = null;
        if (listB != null && listB.size() != 0) {
            gr.a((List<gp>) listB);
            for (int i = 0; i < listB.size(); i++) {
                gp gpVar = (gp) listB.get(i);
                if (gr.a(context, ftVar, gpVar.a(), ffVar)) {
                    try {
                        a(context, ftVar, ffVar, a(context, gpVar.a()), gpVar.e());
                        strE = gpVar.e();
                        break;
                    } catch (Throwable th) {
                        gr.a(th, "FileManager", "loadAvailableD");
                    }
                } else {
                    c(context, ftVar, gpVar.a());
                }
            }
        }
        return strE;
    }

    static void a(Context context, File file, ff ffVar) {
        File parentFile = file.getParentFile();
        if (file.exists() || parentFile == null || !parentFile.exists()) {
            return;
        }
        c(context, ffVar.a(), ffVar.b());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void c(Context context, ft ftVar, String str) {
        File file = new File(a(context, str));
        if (file.exists()) {
            file.delete();
        }
        ftVar.a(gp.b(str), gp.class);
    }

    private static void a(Context context, File file, String str, ff ffVar) {
        ft ftVar = new ft(context, gn.a());
        gp gpVarA = a.a(ftVar, file.getName());
        String strE = gpVarA != null ? gpVarA.e() : null;
        File file2 = new File(str);
        if (TextUtils.isEmpty(strE) || !file2.exists()) {
            return;
        }
        String strA = fc.a(str);
        String name = file2.getName();
        a.a(ftVar, new gp.a(name, strA, ffVar.a(), ffVar.b(), strE).a("useod").a(), gp.b(name));
    }
}
