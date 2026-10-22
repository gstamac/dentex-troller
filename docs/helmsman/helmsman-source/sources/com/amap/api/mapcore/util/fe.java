package com.amap.api.mapcore.util;

import android.content.Context;
import android.text.TextUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Map;

/* JADX INFO: compiled from: SDKCoordinatorDownload.java */
/* JADX INFO: loaded from: classes.dex */
public class fe extends Thread implements gy.a {
    private static String h = "sodownload";
    private static String i = "sofail";
    private gy a;
    private a b;
    private RandomAccessFile c;
    private String d;
    private String e;
    private String f;
    private Context g;

    public fe(Context context, String str, String str2, String str3) {
        this.g = context;
        this.f = str3;
        this.d = a(context, str + "temp.so");
        this.e = a(context, "libwgs2gcj.so");
        this.b = new a(str2);
        this.a = new gy(this.b);
    }

    public static String a(Context context, String str) {
        return context.getFilesDir().getAbsolutePath() + File.separator + "libso" + File.separator + str;
    }

    private static String b(Context context, String str) {
        return a(context, str);
    }

    public void a() {
        a aVar = this.b;
        if (aVar == null || TextUtils.isEmpty(aVar.c()) || !this.b.c().contains("libJni_wgs2gcj.so") || !this.b.c().contains(fg.a(this.g)) || new File(this.e).exists()) {
            return;
        }
        start();
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            File file = new File(b(this.g, "tempfile"));
            if (file.exists()) {
                file.delete();
            }
            this.a.a(this);
        } catch (Throwable th) {
            fk.a(th, "SDKCoordinatorDownload", "run");
            b();
        }
    }

    @Override // com.amap.api.mapcore.util.gy.a
    public void a(byte[] bArr, long j) {
        try {
            if (this.c == null) {
                File file = new File(this.d);
                File parentFile = file.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
                try {
                    this.c = new RandomAccessFile(file, "rw");
                } catch (FileNotFoundException e) {
                    fk.a(e, "SDKCoordinatorDownload", "onDownload");
                    b();
                }
            }
            RandomAccessFile randomAccessFile = this.c;
            if (randomAccessFile == null) {
                return;
            }
            try {
                randomAccessFile.seek(j);
                this.c.write(bArr);
            } catch (IOException e2) {
                b();
                fk.a(e2, "SDKCoordinatorDownload", "onDownload");
            }
        } catch (Throwable th) {
            b();
            fk.a(th, "SDKCoordinatorDownload", "onDownload");
        }
    }

    @Override // com.amap.api.mapcore.util.gy.a
    public void d() {
        b();
    }

    @Override // com.amap.api.mapcore.util.gy.a
    public void e() {
        try {
            RandomAccessFile randomAccessFile = this.c;
            if (randomAccessFile != null) {
                randomAccessFile.close();
            }
            String strA = fc.a(this.d);
            if (strA != null && strA.equalsIgnoreCase(this.f)) {
                if (new File(this.e).exists()) {
                    b();
                    return;
                } else {
                    new File(this.d).renameTo(new File(this.e));
                    return;
                }
            }
            b();
        } catch (Throwable th) {
            b();
            File file = new File(this.e);
            if (file.exists()) {
                file.delete();
            }
            fk.a(th, "SDKCoordinatorDownload", "onFinish");
        }
    }

    @Override // com.amap.api.mapcore.util.gy.a
    public void a(Throwable th) {
        try {
            RandomAccessFile randomAccessFile = this.c;
            if (randomAccessFile != null) {
                randomAccessFile.close();
            }
            b();
            File file = new File(b(this.g, "tempfile"));
            if (file.exists()) {
                return;
            }
            try {
                File parentFile = file.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdir();
                }
                file.createNewFile();
            } catch (Throwable th2) {
                fk.a(th2, "SDKCoordinatorDownload", "onException");
            }
        } catch (Throwable th3) {
            fk.a(th3, "SDKCoordinatorDownload", "onException");
        }
    }

    private void b() {
        File file = new File(this.d);
        if (file.exists()) {
            file.delete();
        }
    }

    /* JADX INFO: compiled from: SDKCoordinatorDownload.java */
    private static class a extends hc {
        private String a;

        @Override // com.amap.api.mapcore.util.hc
        public Map<String, String> a() {
            return null;
        }

        @Override // com.amap.api.mapcore.util.hc
        public Map<String, String> b() {
            return null;
        }

        a(String str) {
            this.a = str;
        }

        @Override // com.amap.api.mapcore.util.hc
        public String c() {
            return this.a;
        }
    }
}
