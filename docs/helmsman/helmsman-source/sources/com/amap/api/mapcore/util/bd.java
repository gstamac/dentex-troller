package com.amap.api.mapcore.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/* JADX INFO: compiled from: FileCopy.java */
/* JADX INFO: loaded from: classes.dex */
public class bd {

    /* JADX INFO: compiled from: FileCopy.java */
    public interface a {
        void a(String str, String str2);

        void a(String str, String str2, float f);

        void a(String str, String str2, int i);

        void b(String str, String str2);
    }

    private float a(long j, long j2) {
        return (j / j2) * 100.0f;
    }

    public long a(File file, File file2, long j, long j2, a aVar) {
        long jA;
        if (j == 0) {
            System.err.println("sizeOfDirectory is the total Size,  must be a positive number");
            if (aVar != null) {
                aVar.a("", "", -1);
            }
            return 0L;
        }
        String absolutePath = file.getAbsolutePath();
        String absolutePath2 = file2.getAbsolutePath();
        try {
            if (file.isDirectory()) {
                if (!file2.exists() && !file2.mkdirs()) {
                    throw new IOException("Cannot create dir " + file2.getAbsolutePath());
                }
                String[] list = file.list();
                jA = j;
                if (list != null) {
                    int i = 0;
                    while (i < list.length) {
                        try {
                            int i2 = i;
                            String[] strArr = list;
                            jA = a(new File(file, list[i]), new File(file2, list[i]), jA, j2, aVar);
                            i = i2 + 1;
                            list = strArr;
                        } catch (Exception e) {
                            e = e;
                        }
                    }
                }
                return jA;
            }
            File parentFile = file2.getParentFile();
            if (parentFile != null && !parentFile.exists() && !parentFile.mkdirs()) {
                throw new IOException("Cannot create dir " + parentFile.getAbsolutePath());
            }
            if (aVar != null && j <= 0) {
                aVar.a(absolutePath, absolutePath2);
            }
            FileInputStream fileInputStream = new FileInputStream(file);
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            byte[] bArr = new byte[1024];
            long j3 = j;
            while (true) {
                try {
                    int i3 = fileInputStream.read(bArr);
                    if (i3 <= 0) {
                        break;
                    }
                    fileOutputStream.write(bArr, 0, i3);
                    j3 += (long) i3;
                    if (aVar != null) {
                        try {
                            aVar.a(absolutePath, absolutePath2, a(j3, j2));
                        } catch (Exception e2) {
                            e = e2;
                        }
                    }
                    e = e2;
                } catch (Exception e3) {
                    e = e3;
                }
                jA = j3;
            }
            fileInputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();
            if (aVar != null && j3 >= j2 - 1) {
                aVar.b(absolutePath, absolutePath2);
            }
            return j3;
        } catch (Exception e4) {
            e = e4;
            jA = j;
        }
        e.printStackTrace();
        if (aVar == null) {
            return jA;
        }
        aVar.a(absolutePath, absolutePath2, -1);
        return jA;
    }
}
