package cn.jiguang.ad;

import android.os.Environment;
import android.util.Log;
import cn.jiguang.f.i;
import cn.jiguang.internal.JConstants;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public final class b {
    private static String a = ".jpush";
    private static String b = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + a + File.separator;
    private static String c = b + a;
    private static final SimpleDateFormat d = new SimpleDateFormat("MM.dd_HH:mm:ss_SSS", Locale.ENGLISH);
    private static ArrayList<String> e = new ArrayList<>();
    private static boolean f = false;
    private static boolean g = false;

    public static void a(String str, String str2, String str3, Throwable th) {
        if (str2 == null || str2.trim().equals("")) {
            str2 = "Logger";
        }
        if (str3 == null) {
            return;
        }
        try {
            String str4 = d.format(new Date());
            BufferedReader bufferedReader = new BufferedReader(new StringReader(str3), 256);
            String strA = i.a("[" + str2 + "]", 24);
            while (true) {
                try {
                    String line = bufferedReader.readLine();
                    if (line == null) {
                        return;
                    }
                    String str5 = ((Object) str4) + " " + i.a(str, 5) + " " + strA + " " + line;
                    if (!g) {
                        try {
                            e.add(str5);
                            if (e.size() == 500) {
                                ArrayList<String> arrayList = e;
                                e = new ArrayList<>();
                                boolean zC = cn.jiguang.f.a.c(JConstants.mApplicationContext, "android.permission.WRITE_EXTERNAL_STORAGE");
                                f = zC;
                                if (zC) {
                                    d.a("Logger", "have writable external storage, write log file");
                                    a(arrayList);
                                } else {
                                    d.a("Logger", "no writable external storage");
                                }
                            }
                        } catch (ArrayIndexOutOfBoundsException unused) {
                            e = new ArrayList<>();
                        } catch (Throwable th2) {
                            th2.printStackTrace();
                        }
                    }
                } catch (IOException e2) {
                    d.h("Logger", e2.getMessage());
                    return;
                }
            }
        } catch (Throwable th3) {
            Log.w("Logger", "logtofile call failed:" + th3.getMessage());
        }
    }

    private static void a(ArrayList<String> arrayList) {
        try {
            if (JConstants.mApplicationContext == null || cn.jiguang.f.a.c(JConstants.mApplicationContext, "android.permission.WRITE_EXTERNAL_STORAGE")) {
                cn.jiguang.aq.a.a("ASYNC", new c(arrayList));
            } else {
                d.f("Logger", "WRITE_EXTERNAL_STORAGE not get");
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    static /* synthetic */ void c() {
        try {
            File file = new File(b);
            if (file.exists()) {
                int length = a.length() + 1;
                int length2 = cn.jiguang.f.c.a.length() + length;
                if (file.listFiles() != null) {
                    for (File file2 : file.listFiles()) {
                        if (cn.jiguang.f.c.a(cn.jiguang.f.c.b(file2.getName().substring(length, length2)), 2)) {
                            file2.delete();
                        }
                    }
                }
            }
        } catch (Throwable th) {
            d.h("Logger", th.getMessage());
        }
    }
}
