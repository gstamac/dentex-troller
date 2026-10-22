package com.amap.api.mapcore.util;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/* JADX INFO: compiled from: DiskLruCache.java */
/* JADX INFO: loaded from: classes.dex */
public final class gs implements Closeable {
    static final Pattern a = Pattern.compile("[a-z0-9_-]{1,120}");
    static ThreadPoolExecutor b;
    private static final ThreadFactory q;
    private static final OutputStream s;
    private final File c;
    private final File d;
    private final File e;
    private final File f;
    private final int g;
    private long h;
    private final int i;
    private Writer k;
    private int n;
    private gt o;
    private long j = 0;
    private int l = 1000;
    private final LinkedHashMap<String, c> m = new LinkedHashMap<>(0, 0.75f, true);
    private long p = 0;
    private final Callable<Void> r = new Callable<Void>() { // from class: com.amap.api.mapcore.util.gs.2
        @Override // java.util.concurrent.Callable
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public Void call() throws Exception {
            synchronized (gs.this) {
                if (gs.this.k == null) {
                    return null;
                }
                gs.this.m();
                if (gs.this.k()) {
                    gs.this.j();
                    gs.this.n = 0;
                }
                return null;
            }
        }
    };

    static {
        ThreadFactory threadFactory = new ThreadFactory() { // from class: com.amap.api.mapcore.util.gs.1
            private final AtomicInteger a = new AtomicInteger(1);

            @Override // java.util.concurrent.ThreadFactory
            public Thread newThread(Runnable runnable) {
                return new Thread(runnable, "disklrucache#" + this.a.getAndIncrement());
            }
        };
        q = threadFactory;
        b = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue(), threadFactory);
        s = new OutputStream() { // from class: com.amap.api.mapcore.util.gs.3
            @Override // java.io.OutputStream
            public void write(int i) throws IOException {
            }
        };
    }

    public void a(gt gtVar) {
        this.o = gtVar;
    }

    /* JADX WARN: Code duplicated, block: B:4:0x0004 A[PHI: r0
  0x0004: PHI (r0v2 int) = (r0v0 int), (r0v1 int) binds: [B:3:0x0002, B:6:0x0008] A[DONT_GENERATE, DONT_INLINE]] */
    public void a(int i) {
        int i2 = 10;
        if (i < 10) {
            i = i2;
        } else {
            i2 = 10000;
            if (i > 10000) {
                i = i2;
            }
        }
        this.l = i;
    }

    public static void a() {
        ThreadPoolExecutor threadPoolExecutor = b;
        if (threadPoolExecutor == null || threadPoolExecutor.isShutdown()) {
            return;
        }
        b.shutdown();
    }

    public static ThreadPoolExecutor b() {
        try {
            ThreadPoolExecutor threadPoolExecutor = b;
            if (threadPoolExecutor == null || threadPoolExecutor.isShutdown()) {
                b = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue(), q);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return b;
    }

    private gs(File file, int i, int i2, long j) {
        this.c = file;
        this.g = i;
        this.d = new File(file, "journal");
        this.e = new File(file, "journal.tmp");
        this.f = new File(file, "journal.bkp");
        this.i = i2;
        this.h = j;
    }

    public static gs a(File file, int i, int i2, long j) throws IOException {
        if (j <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        if (i2 <= 0) {
            throw new IllegalArgumentException("valueCount <= 0");
        }
        File file2 = new File(file, "journal.bkp");
        if (file2.exists()) {
            File file3 = new File(file, "journal");
            if (file3.exists()) {
                file2.delete();
            } else {
                a(file2, file3, false);
            }
        }
        gs gsVar = new gs(file, i, i2, j);
        if (gsVar.d.exists()) {
            try {
                gsVar.h();
                gsVar.i();
                gsVar.k = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(gsVar.d, true), gv.a));
                return gsVar;
            } catch (Throwable unused) {
                gsVar.f();
            }
        }
        file.mkdirs();
        gs gsVar2 = new gs(file, i, i2, j);
        gsVar2.j();
        return gsVar2;
    }

    private void h() throws IOException {
        gu guVar = new gu(new FileInputStream(this.d), gv.a);
        try {
            String strA = guVar.a();
            String strA2 = guVar.a();
            String strA3 = guVar.a();
            String strA4 = guVar.a();
            String strA5 = guVar.a();
            if (!"libcore.io.DiskLruCache".equals(strA) || !"1".equals(strA2) || !Integer.toString(this.g).equals(strA3) || !Integer.toString(this.i).equals(strA4) || !"".equals(strA5)) {
                throw new IOException("unexpected journal header: [" + strA + ", " + strA2 + ", " + strA4 + ", " + strA5 + "]");
            }
            int i = 0;
            while (true) {
                try {
                    d(guVar.a());
                    i++;
                } catch (EOFException unused) {
                    this.n = i - this.m.size();
                    gv.a(guVar);
                    return;
                }
            }
        } catch (Throwable th) {
            gv.a(guVar);
            throw th;
        }
    }

    private void d(String str) throws IOException {
        String strSubstring;
        int iIndexOf = str.indexOf(32);
        if (iIndexOf == -1) {
            throw new IOException("unexpected journal line: " + str);
        }
        int i = iIndexOf + 1;
        int iIndexOf2 = str.indexOf(32, i);
        if (iIndexOf2 == -1) {
            strSubstring = str.substring(i);
            if (iIndexOf == 6 && str.startsWith("REMOVE")) {
                this.m.remove(strSubstring);
                return;
            }
        } else {
            strSubstring = str.substring(i, iIndexOf2);
        }
        c cVar = this.m.get(strSubstring);
        if (cVar == null) {
            cVar = new c(strSubstring);
            this.m.put(strSubstring, cVar);
        }
        if (iIndexOf2 != -1 && iIndexOf == 5 && str.startsWith("CLEAN")) {
            String[] strArrSplit = str.substring(iIndexOf2 + 1).split(" ");
            cVar.d = true;
            cVar.e = null;
            cVar.a(strArrSplit);
            return;
        }
        if (iIndexOf2 != -1 || iIndexOf != 5 || !str.startsWith("DIRTY")) {
            if (iIndexOf2 != -1 || iIndexOf != 4 || !str.startsWith("READ")) {
                throw new IOException("unexpected journal line: " + str);
            }
            return;
        }
        cVar.e = new a(cVar);
    }

    private void i() throws IOException {
        a(this.e);
        Iterator<c> it = this.m.values().iterator();
        while (it.hasNext()) {
            c next = it.next();
            int i = 0;
            if (next.e == null) {
                while (i < this.i) {
                    this.j += next.c[i];
                    i++;
                }
            } else {
                next.e = null;
                while (i < this.i) {
                    a(next.a(i));
                    a(next.b(i));
                    i++;
                }
                it.remove();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void j() throws IOException {
        Writer writer = this.k;
        if (writer != null) {
            writer.close();
        }
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.e), gv.a));
        try {
            bufferedWriter.write("libcore.io.DiskLruCache");
            bufferedWriter.write("\n");
            bufferedWriter.write("1");
            bufferedWriter.write("\n");
            bufferedWriter.write(Integer.toString(this.g));
            bufferedWriter.write("\n");
            bufferedWriter.write(Integer.toString(this.i));
            bufferedWriter.write("\n");
            bufferedWriter.write("\n");
            for (c cVar : this.m.values()) {
                if (cVar.e != null) {
                    bufferedWriter.write("DIRTY " + cVar.b + '\n');
                } else {
                    bufferedWriter.write("CLEAN " + cVar.b + cVar.a() + '\n');
                }
            }
            bufferedWriter.close();
            if (this.d.exists()) {
                a(this.d, this.f, true);
            }
            a(this.e, this.d, false);
            this.f.delete();
            this.k = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.d, true), gv.a));
        } catch (Throwable th) {
            bufferedWriter.close();
            throw th;
        }
    }

    private static void a(File file) throws IOException {
        if (file.exists() && !file.delete()) {
            throw new IOException();
        }
    }

    private static void a(File file, File file2, boolean z) throws IOException {
        if (z) {
            a(file2);
        }
        if (!file.renameTo(file2)) {
            throw new IOException();
        }
    }

    public synchronized b a(String str) throws IOException {
        InputStream inputStream;
        l();
        e(str);
        c cVar = this.m.get(str);
        if (cVar == null) {
            return null;
        }
        if (!cVar.d) {
            return null;
        }
        InputStream[] inputStreamArr = new InputStream[this.i];
        for (int i = 0; i < this.i; i++) {
            try {
                inputStreamArr[i] = new FileInputStream(cVar.a(i));
            } catch (FileNotFoundException unused) {
                for (int i2 = 0; i2 < this.i && (inputStream = inputStreamArr[i2]) != null; i2++) {
                    gv.a(inputStream);
                }
                return null;
            }
        }
        this.n++;
        this.k.append((CharSequence) ("READ " + str + '\n'));
        if (k()) {
            b().submit(this.r);
        }
        return new b(str, cVar.f, inputStreamArr, cVar.c);
    }

    public a b(String str) throws IOException {
        return a(str, -1L);
    }

    private synchronized a a(String str, long j) throws IOException {
        l();
        e(str);
        c cVar = this.m.get(str);
        if (j != -1 && (cVar == null || cVar.f != j)) {
            return null;
        }
        if (cVar == null) {
            cVar = new c(str);
            this.m.put(str, cVar);
        } else if (cVar.e != null) {
            return null;
        }
        a aVar = new a(cVar);
        cVar.e = aVar;
        this.k.write("DIRTY " + str + '\n');
        this.k.flush();
        return aVar;
    }

    public File c() {
        return this.c;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void a(a aVar, boolean z) throws IOException {
        c cVar = aVar.b;
        if (cVar.e != aVar) {
            throw new IllegalStateException();
        }
        if (z && !cVar.d) {
            for (int i = 0; i < this.i; i++) {
                if (!aVar.c[i]) {
                    aVar.b();
                    throw new IllegalStateException("Newly created entry didn't create value for index " + i);
                }
                if (!cVar.b(i).exists()) {
                    aVar.b();
                    return;
                }
            }
        }
        for (int i2 = 0; i2 < this.i; i2++) {
            File fileB = cVar.b(i2);
            if (z) {
                if (fileB.exists()) {
                    File fileA = cVar.a(i2);
                    fileB.renameTo(fileA);
                    long j = cVar.c[i2];
                    long length = fileA.length();
                    cVar.c[i2] = length;
                    this.j = (this.j - j) + length;
                }
            } else {
                a(fileB);
            }
        }
        this.n++;
        cVar.e = null;
        if (cVar.d | z) {
            cVar.d = true;
            this.k.write("CLEAN " + cVar.b + cVar.a() + '\n');
            if (z) {
                long j2 = this.p;
                this.p = 1 + j2;
                cVar.f = j2;
            }
        } else {
            this.m.remove(cVar.b);
            this.k.write("REMOVE " + cVar.b + '\n');
        }
        this.k.flush();
        if (this.j > this.h || k()) {
            b().submit(this.r);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean k() {
        int i = this.n;
        return i >= 2000 && i >= this.m.size();
    }

    public synchronized boolean c(String str) throws IOException {
        l();
        e(str);
        c cVar = this.m.get(str);
        if (cVar != null && cVar.e == null) {
            for (int i = 0; i < this.i; i++) {
                File fileA = cVar.a(i);
                if (fileA.exists() && !fileA.delete()) {
                    throw new IOException("failed to delete " + fileA);
                }
                this.j -= cVar.c[i];
                cVar.c[i] = 0;
            }
            this.n++;
            this.k.append((CharSequence) ("REMOVE " + str + '\n'));
            this.m.remove(str);
            if (k()) {
                b().submit(this.r);
            }
            return true;
        }
        return false;
    }

    public synchronized boolean d() {
        return this.k == null;
    }

    private void l() {
        if (this.k == null) {
            throw new IllegalStateException("cache is closed");
        }
    }

    public synchronized void e() throws IOException {
        l();
        m();
        this.k.flush();
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public synchronized void close() throws IOException {
        if (this.k == null) {
            return;
        }
        for (c cVar : new ArrayList(this.m.values())) {
            if (cVar.e != null) {
                cVar.e.b();
            }
        }
        m();
        this.k.close();
        this.k = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void m() throws IOException {
        while (true) {
            if (this.j <= this.h && this.m.size() <= this.l) {
                return;
            }
            String key = this.m.entrySet().iterator().next().getKey();
            c(key);
            gt gtVar = this.o;
            if (gtVar != null) {
                gtVar.a(key);
            }
        }
    }

    public void f() throws IOException {
        close();
        gv.a(this.c);
    }

    private void e(String str) {
        if (!a.matcher(str).matches()) {
            throw new IllegalArgumentException("keys must match regex [a-z0-9_-]{1,120}: \"" + str + "\"");
        }
    }

    /* JADX INFO: compiled from: DiskLruCache.java */
    public final class b implements Closeable {
        private final String b;
        private final long c;
        private final InputStream[] d;
        private final long[] e;

        private b(String str, long j, InputStream[] inputStreamArr, long[] jArr) {
            this.b = str;
            this.c = j;
            this.d = inputStreamArr;
            this.e = jArr;
        }

        public InputStream a(int i) {
            return this.d[i];
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            for (InputStream inputStream : this.d) {
                gv.a(inputStream);
            }
        }
    }

    /* JADX INFO: compiled from: DiskLruCache.java */
    public final class a {
        private final c b;
        private final boolean[] c;
        private boolean d;
        private boolean e;

        private a(c cVar) {
            this.b = cVar;
            this.c = cVar.d ? null : new boolean[gs.this.i];
        }

        public OutputStream a(int i) throws IOException {
            FileOutputStream fileOutputStream;
            C0016a c0016a;
            if (i < 0 || i >= gs.this.i) {
                throw new IllegalArgumentException("Expected index " + i + " to be greater than 0 and less than the maximum value count of " + gs.this.i);
            }
            synchronized (gs.this) {
                if (this.b.e != this) {
                    throw new IllegalStateException();
                }
                if (!this.b.d) {
                    this.c[i] = true;
                }
                File fileB = this.b.b(i);
                try {
                    fileOutputStream = new FileOutputStream(fileB);
                } catch (FileNotFoundException unused) {
                    gs.this.c.mkdirs();
                    try {
                        fileOutputStream = new FileOutputStream(fileB);
                    } catch (FileNotFoundException unused2) {
                        return gs.s;
                    }
                }
                c0016a = new C0016a(fileOutputStream);
            }
            return c0016a;
        }

        public void a() throws IOException {
            if (this.d) {
                gs.this.a(this, false);
                gs.this.c(this.b.b);
            } else {
                gs.this.a(this, true);
            }
            this.e = true;
        }

        public void b() throws IOException {
            gs.this.a(this, false);
        }

        /* JADX INFO: renamed from: com.amap.api.mapcore.util.gs$a$a, reason: collision with other inner class name */
        /* JADX INFO: compiled from: DiskLruCache.java */
        private class C0016a extends FilterOutputStream {
            private C0016a(OutputStream outputStream) {
                super(outputStream);
            }

            @Override // java.io.FilterOutputStream, java.io.OutputStream
            public void write(int i) {
                try {
                    this.out.write(i);
                } catch (IOException unused) {
                    a.this.d = true;
                }
            }

            @Override // java.io.FilterOutputStream, java.io.OutputStream
            public void write(byte[] bArr, int i, int i2) {
                try {
                    this.out.write(bArr, i, i2);
                } catch (IOException unused) {
                    a.this.d = true;
                }
            }

            @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
            public void close() {
                try {
                    this.out.close();
                } catch (IOException unused) {
                    a.this.d = true;
                }
            }

            @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Flushable
            public void flush() {
                try {
                    this.out.flush();
                } catch (IOException unused) {
                    a.this.d = true;
                }
            }
        }
    }

    /* JADX INFO: compiled from: DiskLruCache.java */
    private final class c {
        private final String b;
        private final long[] c;
        private boolean d;
        private a e;
        private long f;

        private c(String str) {
            this.b = str;
            this.c = new long[gs.this.i];
        }

        public String a() throws IOException {
            StringBuilder sb = new StringBuilder();
            for (long j : this.c) {
                sb.append(' ').append(j);
            }
            return sb.toString();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void a(String[] strArr) throws IOException {
            if (strArr.length != gs.this.i) {
                throw b(strArr);
            }
            for (int i = 0; i < strArr.length; i++) {
                try {
                    this.c[i] = Long.parseLong(strArr[i]);
                } catch (NumberFormatException unused) {
                    throw b(strArr);
                }
            }
        }

        private IOException b(String[] strArr) throws IOException {
            throw new IOException("unexpected journal line: " + Arrays.toString(strArr));
        }

        public File a(int i) {
            return new File(gs.this.c, this.b + "." + i);
        }

        public File b(int i) {
            return new File(gs.this.c, this.b + "." + i + ".tmp");
        }
    }
}
