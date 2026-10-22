package cn.jpush.android.t;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import cn.jpush.android.helper.Logger;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.Comparator;

/* JADX INFO: loaded from: classes.dex */
public class c {
    public static File a(Context context, String str) {
        File filesDir;
        if (context != null && (filesDir = context.getFilesDir()) != null) {
            return new File(filesDir, str);
        }
        Logger.w("FileUtils", "can't get file :" + str);
        return null;
    }

    public static Object a(File file) throws Throwable {
        ObjectInputStream objectInputStream = null;
        if (file == null || !file.exists() || file.isDirectory()) {
            return null;
        }
        try {
            ObjectInputStream objectInputStream2 = new ObjectInputStream(new FileInputStream(file));
            try {
                Object object = objectInputStream2.readObject();
                f.a((Closeable) objectInputStream2);
                return object;
            } catch (Throwable th) {
                th = th;
                objectInputStream = objectInputStream2;
                f.a((Closeable) objectInputStream);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private static void a(Context context) {
        File[] fileArrListFiles;
        File fileA = a(context, "rich");
        if (fileA != null && fileA.exists() && fileA.isDirectory() && (fileArrListFiles = fileA.listFiles()) != null && fileArrListFiles.length > 10) {
            Arrays.sort(fileArrListFiles, new Comparator<File>() { // from class: cn.jpush.android.t.c.1
                @Override // java.util.Comparator
                /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
                public int compare(File file, File file2) {
                    if (file.lastModified() > file2.lastModified()) {
                        return -1;
                    }
                    return file.lastModified() < file2.lastModified() ? 1 : 0;
                }
            });
            c(fileArrListFiles[fileArrListFiles.length - 1]);
        }
    }

    public static boolean a(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        try {
            return new File(str).exists();
        } catch (Throwable th) {
            Logger.w("FileUtils", "FileUtils e:" + th);
            return false;
        }
    }

    public static boolean a(String str, String str2) {
        byte[] bytes;
        if (str2 != null) {
            try {
                bytes = str2.getBytes("UTF-8");
            } catch (Exception e) {
                Logger.w("FileUtils", "getBytes exception:" + e);
                return false;
            }
        } else {
            bytes = null;
        }
        return b(str, bytes);
    }

    public static boolean a(String str, byte[] bArr) {
        if (bArr == null || bArr.length <= 0) {
            return false;
        }
        return b(str, bArr);
    }

    public static String b(Context context, String str) {
        try {
            File fileA = a(context, str);
            if (fileA == null) {
                Logger.w("FileUtils", str + " can't be null");
                return "";
            }
            if (fileA.isFile()) {
                fileA.delete();
            }
            if (!fileA.exists()) {
                fileA.mkdirs();
            }
            return fileA.getAbsolutePath() + File.separator;
        } catch (Throwable unused) {
            return "";
        }
    }

    public static String b(String str) {
        return TextUtils.isEmpty(str) ? "" : str.substring(str.lastIndexOf("/") + 1);
    }

    public static void b(File file) {
        if (file == null || file.exists()) {
            return;
        }
        File parentFile = file.getParentFile();
        if (parentFile != null && !parentFile.exists()) {
            parentFile.mkdirs();
        }
        try {
            file.createNewFile();
        } catch (Throwable unused) {
        }
    }

    public static boolean b(String str, String str2) {
        Logger.v("FileUtils", "action:createHtmlFile - filePath:" + str + ", content:" + str2);
        if (TextUtils.isEmpty(str2)) {
            return false;
        }
        return a(str, str2);
    }

    private static boolean b(String str, byte[] bArr) throws Throwable {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        if (bArr == null) {
            bArr = new byte[0];
        }
        FileOutputStream fileOutputStream = null;
        try {
            try {
                File file = new File(str);
                b(file);
                FileOutputStream fileOutputStream2 = new FileOutputStream(file);
                try {
                    fileOutputStream2.write(bArr);
                    f.a(fileOutputStream2);
                    return true;
                } catch (Exception e) {
                    e = e;
                    fileOutputStream = fileOutputStream2;
                    Logger.w("FileUtils", "save to file exception:" + e + " path = " + str);
                    f.a(fileOutputStream);
                    return false;
                } catch (Throwable th) {
                    th = th;
                    fileOutputStream = fileOutputStream2;
                    f.a(fileOutputStream);
                    throw th;
                }
            } catch (Exception e2) {
                e = e2;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public static String c(Context context, String str) {
        try {
            if (!a.a()) {
                a(context);
                return b(context, "rich" + File.separator + str);
            }
            String str2 = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + File.separator + str + File.separator;
            File file = new File(str2);
            if (!file.exists()) {
                file.mkdirs();
            }
            return str2;
        } catch (Throwable th) {
            th.printStackTrace();
            return "";
        }
    }

    public static boolean c(File file) {
        try {
            if (!file.exists()) {
                return false;
            }
            if (file.isFile()) {
                return file.delete();
            }
            String[] list = file.list();
            if (list != null) {
                for (String str : list) {
                    File file2 = new File(file, str);
                    if (file2.isDirectory()) {
                        c(file2);
                    } else {
                        file2.delete();
                    }
                }
            }
            return file.delete();
        } catch (Exception unused) {
            Logger.e("FileUtils", "Delete dir error");
            return false;
        }
    }

    public static String d(Context context, String str) {
        try {
            if (!a.a()) {
                return "";
            }
            String str2 = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + File.separator + str;
            if (new File(str2).exists()) {
                return str2;
            }
            Logger.ww("FileUtils", "Can't find developer picture resource in SDCard.");
            return "";
        } catch (Throwable th) {
            Logger.ww("FileUtils", "Get developer picture resource failed.");
            th.printStackTrace();
            return "";
        }
    }
}
