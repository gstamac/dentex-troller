package com.amap.api.mapcore.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.StatFs;
import com.qiniu.android.common.Constants;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.security.MessageDigest;
import java.util.HashMap;
import kotlin.UByte;

/* JADX INFO: compiled from: ImageCache.java */
/* JADX INFO: loaded from: classes.dex */
public class dx {
    private static final Bitmap.CompressFormat a = Bitmap.CompressFormat.PNG;
    private gs b;
    private dn<String, Bitmap> c;
    private a d;
    private final Object e = new Object();
    private boolean f = true;
    private HashMap<String, WeakReference<Bitmap>> g;

    private dx(a aVar) {
        b(aVar);
    }

    public static dx a(a aVar) {
        return new dx(aVar);
    }

    private void b(a aVar) {
        this.d = aVar;
        if (aVar.f) {
            if (eb.a()) {
                this.g = new HashMap<>();
            }
            this.c = new dn<String, Bitmap>(this.d.a) { // from class: com.amap.api.mapcore.util.dx.1
                /* JADX INFO: Access modifiers changed from: protected */
                @Override // com.amap.api.mapcore.util.dn
                public void a(boolean z, String str, Bitmap bitmap, Bitmap bitmap2) {
                    if (!dw.c() || dx.this.g == null || bitmap == null || bitmap.isRecycled()) {
                        return;
                    }
                    dx.this.g.put(str, new WeakReference(bitmap));
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @Override // com.amap.api.mapcore.util.dn
                /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
                public int b(String str, Bitmap bitmap) {
                    int iA = dx.a(bitmap);
                    if (iA == 0) {
                        return 1;
                    }
                    return iA;
                }
            };
        }
        if (aVar.h) {
            a();
        }
    }

    /* JADX WARN: Code duplicated, block: B:21:0x0042 A[Catch: all -> 0x004c, TryCatch #1 {, blocks: (B:4:0x0003, B:6:0x0007, B:21:0x0042, B:22:0x004a, B:8:0x000d, B:15:0x0025, B:20:0x003d, B:18:0x0031), top: B:30:0x0003, inners: #2 }] */
    public void a() {
        synchronized (this.e) {
            gs gsVar = this.b;
            if (gsVar == null || gsVar.d()) {
                File file = this.d.c;
                if (this.d.g && file != null) {
                    try {
                        if (file.exists()) {
                            b(file);
                        }
                        file.mkdir();
                    } catch (Throwable unused) {
                    }
                    if (a(file) > this.d.b) {
                        try {
                            this.b = gs.a(file, 1, 1, this.d.b);
                        } catch (Throwable unused2) {
                            this.d.c = null;
                        }
                        this.f = false;
                        this.e.notifyAll();
                    } else {
                        this.f = false;
                        this.e.notifyAll();
                    }
                } else {
                    this.f = false;
                    this.e.notifyAll();
                }
            } else {
                this.f = false;
                this.e.notifyAll();
            }
            throw th;
        }
    }

    private void b(File file) throws IOException {
        File[] fileArrListFiles = file.listFiles();
        if (fileArrListFiles == null) {
            throw new IOException("not a readable directory: " + file);
        }
        for (File file2 : fileArrListFiles) {
            if (file2.isDirectory()) {
                b(file2);
            }
            if (!file2.delete()) {
                throw new IOException("failed to delete file: " + file2);
            }
        }
    }

    public void a(String str, Bitmap bitmap) {
        if (str == null || bitmap == null || bitmap.isRecycled()) {
            return;
        }
        dn<String, Bitmap> dnVar = this.c;
        if (dnVar != null) {
            dnVar.a(str, bitmap);
        }
        synchronized (this.e) {
            if (this.b != null) {
                String strC = c(str);
                OutputStream outputStreamA = null;
                try {
                    gs.b bVarA = this.b.a(strC);
                    if (bVarA == null) {
                        gs.a aVarB = this.b.b(strC);
                        if (aVarB != null) {
                            outputStreamA = aVarB.a(0);
                            bitmap.compress(this.d.d, this.d.e, outputStreamA);
                            aVarB.a();
                            outputStreamA.close();
                        }
                    } else {
                        bVarA.a(0).close();
                    }
                    if (outputStreamA != null) {
                        try {
                            outputStreamA.close();
                        } catch (Throwable unused) {
                        }
                    }
                } catch (Throwable unused2) {
                    if (0 != 0) {
                    }
                }
            }
        }
    }

    public Bitmap a(String str) {
        Bitmap bitmapA;
        dn<String, Bitmap> dnVar;
        HashMap<String, WeakReference<Bitmap>> map;
        WeakReference<Bitmap> weakReference;
        if (!dw.c() || (map = this.g) == null || (weakReference = map.get(str)) == null) {
            bitmapA = null;
        } else {
            bitmapA = weakReference.get();
            if (bitmapA == null || bitmapA.isRecycled()) {
                bitmapA = null;
            }
            this.g.remove(str);
        }
        if (bitmapA == null && (dnVar = this.c) != null) {
            bitmapA = dnVar.a(str);
        }
        if (bitmapA == null || bitmapA.isRecycled()) {
            return null;
        }
        return bitmapA;
    }

    public Bitmap b(String str) {
        Bitmap bitmap;
        InputStream inputStreamA;
        Bitmap bitmapA;
        String strC = c(str);
        synchronized (this.e) {
            while (this.f) {
                this.e.wait();
            }
            gs gsVar = this.b;
            bitmap = null;
            InputStream inputStream = null;
            bitmap = null;
            bitmap = null;
            bitmap = null;
            if (gsVar != null) {
                try {
                    gs.b bVarA = gsVar.a(strC);
                    if (bVarA != null) {
                        inputStreamA = bVarA.a(0);
                        if (inputStreamA != null) {
                            try {
                                bitmapA = dz.a(((FileInputStream) inputStreamA).getFD(), Integer.MAX_VALUE, Integer.MAX_VALUE, this);
                            } catch (Throwable unused) {
                                if (inputStreamA != null) {
                                    try {
                                        inputStreamA.close();
                                    } catch (Throwable unused2) {
                                    }
                                }
                            }
                        } else {
                            bitmapA = null;
                        }
                        inputStream = inputStreamA;
                    } else {
                        bitmapA = null;
                    }
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (Throwable unused3) {
                        }
                    }
                    bitmap = bitmapA;
                } catch (Throwable unused4) {
                    inputStreamA = null;
                }
            }
        }
        return bitmap;
    }

    public void b() {
        HashMap<String, WeakReference<Bitmap>> map;
        if (dw.c() && (map = this.g) != null) {
            map.clear();
        }
        dn<String, Bitmap> dnVar = this.c;
        if (dnVar != null) {
            dnVar.a();
        }
        synchronized (this.e) {
            this.f = true;
            gs gsVar = this.b;
            if (gsVar != null && !gsVar.d()) {
                try {
                    this.b.f();
                } catch (Throwable unused) {
                }
                this.b = null;
                a();
            }
        }
    }

    public void c() {
        synchronized (this.e) {
            gs gsVar = this.b;
            if (gsVar != null) {
                try {
                    gsVar.e();
                } catch (Throwable unused) {
                }
            }
        }
    }

    public void d() {
        HashMap<String, WeakReference<Bitmap>> map;
        if (dw.c() && (map = this.g) != null) {
            map.clear();
        }
        dn<String, Bitmap> dnVar = this.c;
        if (dnVar != null) {
            dnVar.a();
        }
        synchronized (this.e) {
            gs gsVar = this.b;
            if (gsVar != null) {
                try {
                    if (!gsVar.d()) {
                        this.b.f();
                        this.b = null;
                    }
                } catch (Throwable unused) {
                }
            }
        }
    }

    /* JADX INFO: compiled from: ImageCache.java */
    public static class a {
        public File c;
        public int a = 5242880;
        public long b = 10485760;
        public Bitmap.CompressFormat d = dx.a;
        public int e = 100;
        public boolean f = true;
        public boolean g = true;
        public boolean h = false;

        public a(Context context, String str) {
            this.c = dx.a(context, str);
        }

        public void a(int i) {
            this.a = i;
        }

        public void a(long j) {
            if (j <= 0) {
                this.g = false;
            }
            this.b = j;
        }

        public void a(String str) {
            this.c = new File(str);
        }

        public void a(boolean z) {
            this.f = z;
        }

        public void b(boolean z) {
            this.g = z;
        }
    }

    public static File a(Context context, String str) {
        String path;
        File fileA = a(context);
        if (("mounted".equals(Environment.getExternalStorageState()) || !e()) && fileA != null) {
            path = fileA.getPath();
        } else {
            path = context.getCacheDir().getPath();
        }
        return new File(path + File.separator + str);
    }

    public static String c(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(str.getBytes(Constants.UTF_8));
            return a(messageDigest.digest());
        } catch (Throwable unused) {
            return String.valueOf(str.hashCode());
        }
    }

    private static String a(byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bArr) {
            String hexString = Integer.toHexString(b & UByte.MAX_VALUE);
            if (hexString.length() == 1) {
                sb.append('0');
            }
            sb.append(hexString);
        }
        return sb.toString();
    }

    public static int a(Bitmap bitmap) {
        if (dw.d()) {
            return bitmap.getByteCount();
        }
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    public static boolean e() {
        if (dw.b()) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }

    public static File a(Context context) {
        try {
            if (dw.a()) {
                return context.getExternalCacheDir();
            }
            return new File(Environment.getExternalStorageDirectory().getPath() + ("/Android/data/" + context.getPackageName() + "/cache/"));
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    public static long a(File file) {
        if (dw.b()) {
            return file.getUsableSpace();
        }
        StatFs statFs = new StatFs(file.getPath());
        return ((long) statFs.getBlockSize()) * ((long) statFs.getAvailableBlocks());
    }
}
