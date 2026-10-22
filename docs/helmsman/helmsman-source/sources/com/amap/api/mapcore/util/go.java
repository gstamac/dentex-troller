package com.amap.api.mapcore.util;

import android.content.Context;
import android.text.TextUtils;
import dalvik.system.DexFile;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Date;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/* JADX INFO: compiled from: DynamicLoader.java */
/* JADX INFO: loaded from: classes.dex */
class go extends gj {
    private PublicKey g;

    go(Context context, ff ffVar, boolean z) throws Exception {
        super(context, ffVar, z);
        this.g = null;
        String strB = gl.b(context, ffVar.a(), ffVar.b());
        String strA = gl.a(context);
        b(strB, strA);
        File file = new File(strB);
        if (z) {
            a(strB, strA + File.separator + gl.a(file.getName()));
            b(context, strB, strA);
        }
    }

    /* JADX WARN: Code duplicated, block: B:22:0x0028 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:23:0x0029 A[Catch: all -> 0x0053, ClassNotFoundException -> 0x0061, TRY_LEAVE, TryCatch #8 {ClassNotFoundException -> 0x0061, all -> 0x0053, blocks: (B:2:0x0000, B:23:0x0029, B:36:0x0047, B:37:0x004c, B:34:0x003f, B:20:0x001f, B:38:0x004d, B:39:0x0052, B:25:0x0031, B:26:0x0033, B:32:0x003d, B:27:0x0034, B:28:0x0039), top: B:56:0x0000, inners: #7 }] */
    /* JADX WARN: Code duplicated, block: B:36:0x0047 A[Catch: all -> 0x0053, ClassNotFoundException -> 0x0061, TryCatch #8 {ClassNotFoundException -> 0x0061, all -> 0x0053, blocks: (B:2:0x0000, B:23:0x0029, B:36:0x0047, B:37:0x004c, B:34:0x003f, B:20:0x001f, B:38:0x004d, B:39:0x0052, B:25:0x0031, B:26:0x0033, B:32:0x003d, B:27:0x0034, B:28:0x0039), top: B:56:0x0000, inners: #7 }] */
    /* JADX WARN: Code duplicated, block: B:50:0x0034 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:54:0x0031 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    @Override // java.lang.ClassLoader
    protected Class<?> findClass(String str) throws ClassNotFoundException {
        Throwable th;
        Class<?> clsLoadClass;
        try {
            if (this.c == null) {
                throw new ClassNotFoundException(str);
            }
            Class<?> cls = null;
            try {
                try {
                    synchronized (this.b) {
                        try {
                            cls = this.b.get(str);
                            if (cls != null) {
                                return cls;
                            }
                            clsLoadClass = this.c.loadClass(str, this);
                            if (clsLoadClass != null) {
                                throw new ClassNotFoundException(str);
                            }
                            try {
                                synchronized (this.b) {
                                    this.b.put(str, clsLoadClass);
                                }
                            } catch (Throwable th2) {
                                gr.a(th2, "dLoader", "findCl");
                            }
                            return clsLoadClass;
                        } catch (Throwable th3) {
                            th = th3;
                            try {
                                throw th;
                            } catch (Throwable th4) {
                                th = th4;
                                gr.a(th, "dLoader", "findCl");
                                if (cls != null) {
                                    return cls;
                                }
                                clsLoadClass = this.c.loadClass(str, this);
                                if (clsLoadClass != null) {
                                    throw new ClassNotFoundException(str);
                                }
                                synchronized (this.b) {
                                    this.b.put(str, clsLoadClass);
                                    return clsLoadClass;
                                }
                            }
                        }
                    }
                } catch (Throwable th5) {
                    th = th5;
                }
            } catch (Throwable th6) {
                th = th6;
            }
        } catch (ClassNotFoundException e) {
            throw e;
        } catch (Throwable th7) {
            gr.a(th7, "dLoader", "findCl");
            throw new ClassNotFoundException(str);
        }
    }

    void a(String str, String str2) throws Exception {
        try {
            if (this.c != null) {
                return;
            }
            b();
            this.c = DexFile.loadDex(str, str2, 0);
        } catch (Throwable th) {
            gr.a(th, "dLoader", "loadFile");
            throw new Exception("load file fail");
        }
    }

    private void c() {
        if (this.g != null) {
            return;
        }
        this.g = gr.a();
    }

    private void a(JarFile jarFile, JarEntry jarEntry) throws IOException {
        InputStream inputStream;
        try {
            try {
                inputStream = jarFile.getInputStream(jarEntry);
                try {
                    do {
                    } while (inputStream.read(new byte[8192]) > 0);
                    gr.a(inputStream);
                } catch (Throwable th) {
                    th = th;
                    try {
                        gr.a(th, "DyLoader", "loadJa");
                        gr.a(inputStream);
                    } catch (Throwable th2) {
                        try {
                            gr.a(inputStream);
                        } catch (Throwable th3) {
                            th3.printStackTrace();
                        }
                        throw th2;
                    }
                }
            } catch (Throwable th4) {
                th4.printStackTrace();
            }
        } catch (Throwable th5) {
            th = th5;
            inputStream = null;
        }
    }

    private boolean a(File file, Certificate[] certificateArr) {
        int length;
        try {
            if (certificateArr.length <= 0 || (length = certificateArr.length - 1) < 0) {
                return false;
            }
            certificateArr[length].verify(this.g);
            return true;
        } catch (Exception e) {
            gr.a(e, "DyLoader", "check");
            return false;
        }
    }

    private boolean a(File file) {
        JarFile jarFile = null;
        try {
            c();
            JarFile jarFile2 = new JarFile(file);
            try {
                JarEntry jarEntry = jarFile2.getJarEntry("classes.dex");
                if (jarEntry == null) {
                    try {
                        jarFile2.close();
                    } catch (Throwable unused) {
                    }
                    return false;
                }
                a(jarFile2, jarEntry);
                Certificate[] certificates = jarEntry.getCertificates();
                if (certificates == null) {
                    try {
                        jarFile2.close();
                    } catch (Throwable unused2) {
                    }
                    return false;
                }
                boolean zA = a(file, certificates);
                try {
                    jarFile2.close();
                } catch (Throwable unused3) {
                }
                return zA;
            } catch (Throwable th) {
                th = th;
                jarFile = jarFile2;
                try {
                    gr.a(th, "DyLoader", "verify");
                    return false;
                } finally {
                    if (jarFile != null) {
                        try {
                            jarFile.close();
                        } catch (Throwable unused4) {
                        }
                    }
                }
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private boolean a(ft ftVar, ff ffVar, String str) {
        if (a(new File(str))) {
            return gr.a(ftVar, gl.a(this.a, ffVar.a(), ffVar.b()), str, ffVar);
        }
        return false;
    }

    private boolean a(ft ftVar, String str, String str2) {
        String strA = gl.a(this.a, str);
        if (gr.a(ftVar, str, strA, this.e)) {
            return true;
        }
        if (gl.a.a(ftVar, str) != null) {
            return false;
        }
        if (!TextUtils.isEmpty(this.f)) {
            gl.a.a(ftVar, new gp.a(str, fc.a(strA), this.e.a(), this.e.b(), str2).a("useod").a(), gp.b(str));
        }
        return true;
    }

    private void b(String str, String str2) throws Exception {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            throw new Exception("dexPath or dexOutputDir is null.");
        }
    }

    private void b(final Context context, final String str, final String str2) {
        fn.c().submit(new Runnable() { // from class: com.amap.api.mapcore.util.go.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    go.this.a(context, str, str2);
                } catch (Throwable th) {
                    gr.a(th, "dLoader", "run()");
                }
            }
        });
    }

    private void a(ft ftVar, File file) {
        gp gpVarA = gl.a.a(ftVar, file.getName());
        if (gpVarA != null) {
            this.f = gpVarA.e();
        }
    }

    private void b(ft ftVar, File file) {
        this.d = false;
        gl.a(this.a, ftVar, file.getName());
        String strA = gl.a(this.a, ftVar, this.e);
        if (TextUtils.isEmpty(strA)) {
            return;
        }
        this.f = strA;
        gl.a(this.a, this.e);
    }

    void a(Context context, String str, String str2) throws Exception {
        new Date().getTime();
        try {
            ft ftVar = new ft(context, gn.a());
            File file = new File(str);
            a(ftVar, file);
            if (!a(ftVar, this.e, file.getAbsolutePath())) {
                b(ftVar, file);
            }
            if (!file.exists()) {
                return;
            }
            if (new File(str2 + File.separator + gl.a(file.getName())).exists() && !a(ftVar, gl.a(file.getName()), this.f)) {
                gl.a(this.a, this.e);
            }
        } catch (Throwable th) {
            gr.a(th, "dLoader", "verifyD()");
        }
        new Date().getTime();
    }
}
