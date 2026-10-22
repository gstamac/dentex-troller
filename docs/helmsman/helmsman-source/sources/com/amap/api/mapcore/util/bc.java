package com.amap.api.mapcore.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/* JADX INFO: compiled from: FileAccessI.java */
/* JADX INFO: loaded from: classes.dex */
class bc {
    RandomAccessFile a;

    public bc() throws IOException {
        this("", 0L);
    }

    public bc(String str, long j) throws IOException {
        File file = new File(str);
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
            } catch (IOException e) {
                fn.b(e, "FileAccessI", "create");
                e.printStackTrace();
            }
        }
        RandomAccessFile randomAccessFile = new RandomAccessFile(str, "rw");
        this.a = randomAccessFile;
        randomAccessFile.seek(j);
    }

    public synchronized int a(byte[] bArr) throws IOException {
        this.a.write(bArr);
        return bArr.length;
    }

    public void a() {
        RandomAccessFile randomAccessFile = this.a;
        if (randomAccessFile != null) {
            try {
                randomAccessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.a = null;
        }
    }
}
