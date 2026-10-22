package com.amap.api.mapcore.util;

import android.text.TextUtils;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/* JADX INFO: compiled from: UnZipFile.java */
/* JADX INFO: loaded from: classes.dex */
public class bi {
    private b a;

    /* JADX INFO: compiled from: UnZipFile.java */
    public static class a {
        public boolean a = false;
    }

    /* JADX INFO: compiled from: UnZipFile.java */
    public interface c {
        void a();

        void a(long j);
    }

    public bi(bf bfVar, be beVar) {
        this.a = new b(bfVar, beVar);
    }

    public void a() {
        b bVar = this.a;
        if (bVar != null) {
            bVar.f();
        }
    }

    public void b() {
        b bVar = this.a;
        if (bVar != null) {
            a(bVar);
        }
    }

    private static void a(b bVar) {
        if (bVar == null) {
            return;
        }
        final be beVarD = bVar.d();
        if (beVarD != null) {
            beVarD.q();
        }
        String strA = bVar.a();
        String strB = bVar.b();
        if (TextUtils.isEmpty(strA) || TextUtils.isEmpty(strB)) {
            if (bVar.e().a) {
                if (beVarD != null) {
                    beVarD.s();
                    return;
                }
                return;
            } else {
                if (beVarD != null) {
                    beVarD.r();
                    return;
                }
                return;
            }
        }
        File file = new File(strA);
        if (!file.exists()) {
            if (bVar.e().a) {
                if (beVarD != null) {
                    beVarD.s();
                    return;
                }
                return;
            } else {
                if (beVarD != null) {
                    beVarD.r();
                    return;
                }
                return;
            }
        }
        File file2 = new File(strB);
        if (!file2.exists()) {
            file2.mkdirs();
        }
        c cVar = new c() { // from class: com.amap.api.mapcore.util.bi.1
            @Override // com.amap.api.mapcore.util.bi.c
            public void a(long j) {
                try {
                    be beVar = beVarD;
                    if (beVar != null) {
                        beVar.a(j);
                    }
                } catch (Exception unused) {
                }
            }

            @Override // com.amap.api.mapcore.util.bi.c
            public void a() {
                be beVar = beVarD;
                if (beVar != null) {
                    beVar.r();
                }
            }
        };
        try {
            if (bVar.e().a && beVarD != null) {
                beVarD.s();
            }
            a(file, file2, cVar, bVar);
            if (bVar.e().a) {
                if (beVarD != null) {
                    beVarD.s();
                }
            } else if (beVarD != null) {
                beVarD.b(bVar.c());
            }
        } catch (Throwable unused) {
            if (bVar.e().a) {
                if (beVarD != null) {
                    beVarD.s();
                }
            } else if (beVarD != null) {
                beVarD.r();
            }
        }
    }

    private static void a(File file, File file2, c cVar, b bVar) throws Exception {
        StringBuffer stringBuffer = new StringBuffer();
        a aVarE = bVar.e();
        long size = 0;
        if (cVar != null) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                CheckedInputStream checkedInputStream = new CheckedInputStream(fileInputStream, new CRC32());
                ZipInputStream zipInputStream = new ZipInputStream(checkedInputStream);
                while (true) {
                    ZipEntry nextEntry = zipInputStream.getNextEntry();
                    if (nextEntry != null) {
                        if (aVarE != null && aVarE.a) {
                            zipInputStream.closeEntry();
                            zipInputStream.close();
                            checkedInputStream.close();
                            fileInputStream.close();
                            break;
                        }
                        if (!nextEntry.isDirectory()) {
                            if (!a(nextEntry.getName())) {
                                cVar.a();
                                break;
                            }
                            stringBuffer.append(nextEntry.getName()).append(";");
                        }
                        size += nextEntry.getSize();
                        zipInputStream.closeEntry();
                    } else {
                        break;
                    }
                }
                bVar.a(stringBuffer.toString());
                zipInputStream.close();
                checkedInputStream.close();
                fileInputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        FileInputStream fileInputStream2 = new FileInputStream(file);
        CheckedInputStream checkedInputStream2 = new CheckedInputStream(fileInputStream2, new CRC32());
        ZipInputStream zipInputStream2 = new ZipInputStream(checkedInputStream2);
        a(file2, zipInputStream2, size, cVar, aVarE);
        zipInputStream2.close();
        checkedInputStream2.close();
        fileInputStream2.close();
    }

    private static void a(File file, ZipInputStream zipInputStream, long j, c cVar, a aVar) throws Exception {
        int iA = 0;
        while (true) {
            ZipEntry nextEntry = zipInputStream.getNextEntry();
            if (nextEntry == null) {
                return;
            }
            if (aVar != null && aVar.a) {
                zipInputStream.closeEntry();
                return;
            }
            String str = file.getPath() + File.separator + nextEntry.getName();
            if (!a(str)) {
                if (cVar != null) {
                    cVar.a();
                    return;
                }
                return;
            } else {
                File file2 = new File(str);
                a(file2);
                if (nextEntry.isDirectory()) {
                    file2.mkdirs();
                } else {
                    iA += a(file2, zipInputStream, iA, j, cVar, aVar);
                }
                zipInputStream.closeEntry();
            }
        }
    }

    private static boolean a(String str) {
        return !str.contains("../");
    }

    private static int a(File file, ZipInputStream zipInputStream, long j, long j2, c cVar, a aVar) throws Exception {
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
        byte[] bArr = new byte[1024];
        int i = 0;
        while (true) {
            int i2 = zipInputStream.read(bArr, 0, 1024);
            if (i2 != -1) {
                if (aVar != null && aVar.a) {
                    bufferedOutputStream.close();
                    return i;
                }
                bufferedOutputStream.write(bArr, 0, i2);
                i += i2;
                if (j2 > 0 && cVar != null) {
                    long j3 = ((((long) i) + j) * 100) / j2;
                    if (aVar == null || !aVar.a) {
                        cVar.a(j3);
                    }
                }
            } else {
                bufferedOutputStream.close();
                return i;
            }
        }
    }

    private static void a(File file) {
        File parentFile = file.getParentFile();
        if (parentFile.exists()) {
            return;
        }
        a(parentFile);
        parentFile.mkdir();
    }

    /* JADX INFO: compiled from: UnZipFile.java */
    private class b {
        private String b;
        private String c;
        private be d;
        private a e = new a();
        private String f;

        public b(bf bfVar, be beVar) {
            this.d = null;
            this.b = bfVar.B();
            this.c = bfVar.C();
            this.d = beVar;
        }

        public void a(String str) {
            if (str.length() > 1) {
                this.f = str;
            }
        }

        public String a() {
            return this.b;
        }

        public String b() {
            return this.c;
        }

        public String c() {
            return this.f;
        }

        public be d() {
            return this.d;
        }

        public a e() {
            return this.e;
        }

        public void f() {
            this.e.a = true;
        }
    }
}
