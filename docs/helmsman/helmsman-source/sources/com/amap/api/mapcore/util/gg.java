package com.amap.api.mapcore.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.List;

/* JADX INFO: compiled from: DexDownLoad.java */
/* JADX INFO: loaded from: classes.dex */
public class gg extends Thread implements gy.a {
    private gh a;
    private gy b;
    private ff c;
    private String d;
    private RandomAccessFile e;
    private Context f;

    @Override // com.amap.api.mapcore.util.gy.a
    public void d() {
    }

    public gg(Context context, gh ghVar, ff ffVar) {
        try {
            this.f = context.getApplicationContext();
            this.c = ffVar;
            if (ghVar == null) {
                return;
            }
            this.a = ghVar;
            this.b = new gy(new gk(this.a));
            this.d = gl.a(context, this.a.a);
        } catch (Throwable th) {
            gr.a(th, "dDownLoad", "DexDownLoad()");
        }
    }

    public void a() {
        try {
            start();
        } catch (Throwable th) {
            gr.a(th, "dDownLoad", "startDownload()");
        }
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            if (c()) {
                hi hiVar = new hi(this.f, this.c.a(), this.c.b(), "O008");
                hiVar.a("{\"param_int_first\":0}");
                hj.a(hiVar, this.f);
                this.b.a(this);
            }
        } catch (Throwable th) {
            gr.a(th, "dDownLoad", "run()");
        }
    }

    private boolean a(ft ftVar, gp gpVar, gh ghVar) {
        String str = ghVar.b;
        String str2 = ghVar.c;
        String str3 = ghVar.d;
        String str4 = ghVar.e;
        if ("errorstatus".equals(gpVar.f())) {
            b(ftVar);
            return true;
        }
        if (!new File(this.d).exists()) {
            return false;
        }
        List listB = ftVar.b(gp.a(gl.a(this.f, str, str2), str, str2, str3), gp.class);
        if (listB != null && listB.size() > 0) {
            return true;
        }
        try {
            gl.a(this.f, str, this.c.b());
            gl.a(this.f, ftVar, this.c, this.d, str3);
            gl.a(this.f, this.c);
        } catch (Throwable th) {
            gr.a(th, "dDownLoad", "processDownloadedFile()");
        }
        return true;
    }

    private boolean a(ft ftVar) {
        try {
            List<gp> listA = gl.a.a(ftVar, this.a.b, "used");
            return listA != null && listA.size() > 0 && gr.a(listA.get(0).e(), this.a.d) > 0;
        } catch (Throwable th) {
            gr.a(th, "dDownLoad", "isUsed()");
        }
    }

    private boolean f() {
        ft ftVar = new ft(this.f, gn.a());
        if (a(ftVar)) {
            return true;
        }
        gp gpVarA = gl.a.a(ftVar, this.a.a);
        if (gpVarA != null) {
            return a(ftVar, gpVarA, this.a);
        }
        return false;
    }

    boolean b() {
        ff ffVar = this.c;
        return ffVar != null && ffVar.a().equals(this.a.b) && this.c.b().equals(this.a.c);
    }

    private boolean g() {
        return Build.VERSION.SDK_INT >= this.a.g && Build.VERSION.SDK_INT <= this.a.f;
    }

    private boolean a(Context context) {
        return fa.m(context) == 1;
    }

    boolean c() {
        try {
            if (!b() || !g() || !a(this.f) || f()) {
                return false;
            }
            gl.b(this.f, this.c.a());
            return true;
        } catch (Throwable th) {
            gr.a(th, "dDownLoad", "isNeedDownload()");
            return false;
        }
    }

    @Override // com.amap.api.mapcore.util.gy.a
    public void a(byte[] bArr, long j) {
        try {
            if (this.e == null) {
                File file = new File(this.d);
                File parentFile = file.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
                this.e = new RandomAccessFile(file, "rw");
            }
            this.e.seek(j);
            this.e.write(bArr);
        } catch (Throwable th) {
            gr.a(th, "dDownLoad", "onDownload()");
        }
    }

    @Override // com.amap.api.mapcore.util.gy.a
    public void a(Throwable th) {
        try {
            gr.a(this.e);
        } catch (Throwable th2) {
            th2.printStackTrace();
        }
    }

    @Override // com.amap.api.mapcore.util.gy.a
    public void e() {
        try {
            RandomAccessFile randomAccessFile = this.e;
            if (randomAccessFile == null) {
                return;
            }
            gr.a(randomAccessFile);
            String strB = this.a.b();
            if (gr.b(this.d, strB)) {
                a(strB);
                hi hiVar = new hi(this.f, this.c.a(), this.c.b(), "O008");
                hiVar.a("{\"param_int_first\":1}");
                hj.a(hiVar, this.f);
            } else {
                try {
                    new File(this.d).delete();
                } catch (Throwable th) {
                    gr.a(th, "dDownLoad", "onFinish");
                }
            }
        } catch (Throwable th2) {
            gr.a(th2, "dDownLoad", "onFinish()");
        }
    }

    private void b(ft ftVar) {
        if (new File(gl.b(this.f, this.c.a(), this.c.b())).exists() || TextUtils.isEmpty(gl.a(this.f, ftVar, this.c))) {
            return;
        }
        try {
            gl.a(this.f, this.c);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private void a(String str) {
        String strC = this.a.c();
        ft ftVar = new ft(this.f, gn.a());
        gl.a.a(ftVar, new gp.a(this.a.a, str, this.a.b, strC, this.a.d).a("copy").a(), gp.a(this.a.a, this.a.b, strC, this.a.d));
        a(this.f, this.a.b);
        try {
            gl.a(this.f, ftVar, this.c, this.d, this.a.d);
            gl.a(this.f, this.c);
        } catch (Throwable th) {
            gr.a(th, "dDownLoad", "onFinish1");
        }
    }

    private void a(Context context, String str) {
        try {
            SharedPreferences.Editor editorEdit = context.getSharedPreferences(str, 0).edit();
            editorEdit.clear();
            editorEdit.commit();
        } catch (Throwable th) {
            gr.a(th, "dDownLoad", "clearMarker()");
        }
    }
}
