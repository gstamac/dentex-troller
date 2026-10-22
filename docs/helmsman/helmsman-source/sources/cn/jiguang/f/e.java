package cn.jiguang.f;

import android.content.Context;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.LinkedList;

/* JADX INFO: loaded from: classes.dex */
public final class e {
    public static File a(Context context, String str) {
        File filesDir;
        if (context != null && (filesDir = context.getFilesDir()) != null) {
            return new File(filesDir, str);
        }
        cn.jiguang.ad.d.f("FileUtils", "can't get file :" + str);
        return null;
    }

    public static File a(File[] fileArr) {
        File file = null;
        if (fileArr != null && fileArr.length != 0) {
            for (File file2 : fileArr) {
                if (file2.lastModified() > (file != null ? file.lastModified() : 0L)) {
                    file = file2;
                }
            }
        }
        return file;
    }

    public static void a(File file) {
        if (file != null) {
            try {
                if (file.exists()) {
                    file.delete();
                    cn.jiguang.ad.d.b("FileUtils", "delete File:" + file.getPath());
                }
            } catch (Throwable unused) {
            }
        }
    }

    public static boolean a(File file, String str) {
        byte[] bytes;
        if (str != null) {
            try {
                bytes = str.getBytes("UTF-8");
            } catch (Throwable th) {
                cn.jiguang.ad.d.f("FileUtils", "getBytes exception:" + th);
                return false;
            }
        } else {
            bytes = null;
        }
        return a(file, bytes);
    }

    private static boolean a(File file, byte[] bArr) {
        Object th;
        if (file == null || file.isDirectory()) {
            cn.jiguang.ad.d.f("FileUtils", "file should not be null or a directory");
            return false;
        }
        FileOutputStream fileOutputStream = null;
        if (bArr == null) {
            try {
                bArr = new byte[0];
            } catch (Throwable th2) {
                th = th2;
                try {
                    cn.jiguang.ad.d.f("FileUtils", "save to file exception:" + th + " path = " + file.getAbsolutePath());
                    return false;
                } finally {
                    k.a(fileOutputStream);
                }
            }
        }
        if (file != null && !file.exists()) {
            File parentFile = file.getParentFile();
            if (parentFile != null && !parentFile.exists()) {
                parentFile.mkdirs();
            }
            try {
                file.createNewFile();
            } catch (Throwable unused) {
            }
        }
        FileOutputStream fileOutputStream2 = new FileOutputStream(file);
        try {
            fileOutputStream2.write(bArr);
            k.a(fileOutputStream2);
            return true;
        } catch (Throwable th3) {
            th = th3;
            fileOutputStream = fileOutputStream2;
            cn.jiguang.ad.d.f("FileUtils", "save to file exception:" + th + " path = " + file.getAbsolutePath());
            return false;
        }
    }

    public static File[] a(File file, FileFilter... fileFilterArr) {
        if (file == null || !file.exists() || !file.isDirectory()) {
            return null;
        }
        if (fileFilterArr == null || fileFilterArr.length == 0 || (fileFilterArr.length == 1 && fileFilterArr[0] == null)) {
            return file.listFiles();
        }
        if (fileFilterArr.length == 1) {
            return file.listFiles(fileFilterArr[0]);
        }
        LinkedList<File> linkedList = new LinkedList();
        linkedList.add(file);
        int length = fileFilterArr.length;
        int i = 0;
        while (i < length) {
            FileFilter fileFilter = fileFilterArr[i];
            LinkedList linkedList2 = new LinkedList();
            for (File file2 : linkedList) {
                File[] fileArrListFiles = fileFilter != null ? file2.listFiles(fileFilter) : file2.listFiles();
                if (fileArrListFiles != null && fileArrListFiles.length > 0) {
                    Collections.addAll(linkedList2, fileArrListFiles);
                }
            }
            if (linkedList2.isEmpty()) {
                return null;
            }
            i++;
            linkedList = linkedList2;
        }
        return (File[]) linkedList.toArray(new File[0]);
    }

    public static File[] a(String str, FileFilter... fileFilterArr) {
        try {
            return a(new File(str), fileFilterArr);
        } catch (Throwable unused) {
            return null;
        }
    }

    public static String b(File file) {
        byte[] bArrD = d(file);
        if (bArrD == null) {
            return null;
        }
        try {
            return new String(bArrD, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            cn.jiguang.ad.d.b("FileUtils", "can't encoding, give up read :" + e);
            return null;
        }
    }

    public static void c(File file) {
        File[] fileArrListFiles;
        if (file == null || !file.exists()) {
            return;
        }
        if (file.isDirectory() && (fileArrListFiles = file.listFiles()) != null) {
            for (File file2 : fileArrListFiles) {
                c(file2);
                file2.delete();
            }
        }
        file.delete();
    }

    private static byte[] d(File file) {
        FileInputStream fileInputStream;
        if (file == null || !file.exists() || file.isDirectory()) {
            return null;
        }
        try {
            fileInputStream = new FileInputStream(file);
            try {
                byte[] bArr = new byte[fileInputStream.available()];
                fileInputStream.read(bArr);
                k.a((Closeable) fileInputStream);
                return bArr;
            } catch (Throwable th) {
                th = th;
                try {
                    cn.jiguang.ad.d.b("FileUtils", "can't read, give up read. e:" + th);
                    return null;
                } finally {
                    k.a((Closeable) fileInputStream);
                }
            }
        } catch (Throwable th2) {
            th = th2;
            fileInputStream = null;
        }
    }
}
