package com.autonavi.ae.gmap.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.text.TextUtils;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* JADX INFO: loaded from: classes.dex */
public class GLFileUtil {
    public static void copyAssetsFile(Context context, String str, String str2) {
        try {
            if (new File(str2).exists()) {
                return;
            }
            InputStream inputStreamOpen = context.getAssets().open(str);
            if (inputStreamOpen.available() == 0) {
                return;
            }
            FileOutputStream fileOutputStream = new FileOutputStream(str2);
            byte[] bArr = new byte[1024];
            for (int i = inputStreamOpen.read(bArr); i > 0; i = inputStreamOpen.read(bArr)) {
                fileOutputStream.write(bArr, 0, i);
            }
            fileOutputStream.flush();
            inputStreamOpen.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void copy(Context context, String str, File file) throws Exception {
        file.delete();
        InputStream inputStreamOpen = context.getAssets().open(str);
        byte[] bArr = new byte[inputStreamOpen.available()];
        try {
            inputStreamOpen.read(bArr);
            closeQuietly(inputStreamOpen);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            try {
                fileOutputStream.write(bArr);
            } finally {
                closeQuietly(fileOutputStream);
            }
        } catch (Throwable th) {
            closeQuietly(inputStreamOpen);
            throw th;
        }
    }

    public static void deleteFile(File file) {
        if (file == null) {
            return;
        }
        File[] fileArrListFiles = file.listFiles();
        if (file.isDirectory() && fileArrListFiles != null) {
            for (File file2 : fileArrListFiles) {
                deleteFile(file2);
            }
        }
        file.delete();
    }

    public static String getCurrentOfflineDataStorage(Context context) {
        return context.getSharedPreferences("base_path", 0).getString("offline_data_storage", "");
    }

    public static boolean checkPathIsCanUse(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        File file = new File(str);
        return file.exists() && file.isDirectory() && file.canWrite() && file.canRead();
    }

    public static String getMapBaseStorage(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("base_path", 0);
        String string = sharedPreferences.getString("map_base_path_v44", "");
        if (string != null && string.length() > 2) {
            File file = new File(string);
            if (file.isDirectory()) {
                if (file.canWrite()) {
                    createNoMediaFileIfNotExist(string);
                    return string;
                }
                String string2 = getCacheDir(context).toString();
                if (string2 != null && string2.length() > 2 && new File(string2).isDirectory()) {
                    return string2;
                }
            }
        }
        String externalStroragePath = getExternalStroragePath(context);
        if (externalStroragePath != null && externalStroragePath.length() > 2 && new File(externalStroragePath).isDirectory()) {
            SharedPreferences.Editor editorEdit = sharedPreferences.edit();
            editorEdit.putString("map_base_path_v44", externalStroragePath);
            editorEdit.apply();
            createNoMediaFileIfNotExist(externalStroragePath);
            return externalStroragePath;
        }
        String string3 = getCacheDir(context).toString();
        if (string3 != null && string3.length() > 2) {
            new File(string3).isDirectory();
        }
        return string3;
    }

    public static String getExternalStroragePath(Context context) {
        String str;
        try {
            StorageManager storageManager = (StorageManager) context.getSystemService("storage");
            Method method = StorageManager.class.getMethod("getVolumeList", new Class[0]);
            int i = 1;
            Method method2 = StorageManager.class.getMethod("getVolumeState", String.class);
            Object[] objArr = (Object[]) method.invoke(storageManager, new Object[0]);
            Boolean.valueOf(false);
            int length = objArr.length;
            String str2 = "";
            int i2 = 0;
            String str3 = "";
            while (true) {
                if (i2 >= length) {
                    str = null;
                    break;
                }
                Object obj = objArr[i2];
                Method method3 = obj.getClass().getMethod("getPath", new Class[0]);
                Method method4 = obj.getClass().getMethod("isRemovable", new Class[0]);
                str = (String) method3.invoke(obj, new Object[0]);
                Object[] objArr2 = new Object[i];
                objArr2[0] = method3.invoke(obj, new Object[0]);
                String str4 = (String) method2.invoke(storageManager, objArr2);
                Boolean bool = (Boolean) method4.invoke(obj, new Object[0]);
                if (!str.toLowerCase(Locale.US).contains("private")) {
                    if (bool.booleanValue()) {
                        if (str != null && str4 != null && str4.equals("mounted")) {
                            try {
                                File[] externalFilesDirs = context.getExternalFilesDirs(null);
                                if (externalFilesDirs == null || externalFilesDirs.length <= 0) {
                                    break;
                                    break;
                                }
                                String str5 = str;
                                for (File file : externalFilesDirs) {
                                    if (file != null) {
                                        String absolutePath = file.getAbsolutePath();
                                        if (!TextUtils.isEmpty(absolutePath) && absolutePath.contains(str)) {
                                            str5 = absolutePath;
                                        }
                                    }
                                }
                                str = str5;
                                break;
                            } catch (Throwable unused) {
                                break;
                            }
                        }
                    } else {
                        str3 = str4;
                        str2 = str;
                    }
                }
                i2++;
                i = 1;
            }
            return (str2 == null || str3 == null || !str3.equals("mounted")) ? str : str2;
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException unused2) {
            if (Environment.getExternalStorageState().equals("mounted")) {
                return Environment.getExternalStorageDirectory().toString();
            }
            return null;
        }
    }

    public static void writeDatasToFile(String str, byte[] bArr) throws Throwable {
        ReentrantReadWriteLock.WriteLock writeLock = new ReentrantReadWriteLock().writeLock();
        writeLock.lock();
        FileOutputStream fileOutputStream = null;
        if (bArr != null) {
            try {
                if (bArr.length != 0) {
                    File file = new File(str);
                    File parentFile = file.getParentFile();
                    if (!parentFile.exists()) {
                        parentFile.mkdirs();
                    }
                    if (file.exists()) {
                        file.delete();
                    }
                    file.createNewFile();
                    FileOutputStream fileOutputStream2 = new FileOutputStream(file);
                    try {
                        fileOutputStream2.write(bArr);
                        fileOutputStream2.flush();
                        writeLock.unlock();
                        closeQuietly(fileOutputStream2);
                        return;
                    } catch (Exception unused) {
                        fileOutputStream = fileOutputStream2;
                    } catch (Throwable th) {
                        fileOutputStream = fileOutputStream2;
                        th = th;
                        writeLock.unlock();
                        closeQuietly(fileOutputStream);
                        throw th;
                    }
                }
            } catch (Exception unused2) {
            } catch (Throwable th2) {
                th = th2;
            }
            writeLock.unlock();
            closeQuietly(fileOutputStream);
            return;
        }
        writeLock.unlock();
        closeQuietly(null);
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception unused) {
            }
        }
    }

    public static byte[] readFileContents(String str) throws Throwable {
        Throwable th;
        FileInputStream fileInputStream;
        try {
            File file = new File(str);
            if (file.exists()) {
                fileInputStream = new FileInputStream(file);
                try {
                    byte[] bArr = new byte[1024];
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    while (true) {
                        int i = fileInputStream.read(bArr);
                        if (i != -1) {
                            byteArrayOutputStream.write(bArr, 0, i);
                        } else {
                            byteArrayOutputStream.close();
                            byte[] byteArray = byteArrayOutputStream.toByteArray();
                            closeQuietly(fileInputStream);
                            return byteArray;
                        }
                    }
                } catch (Exception unused) {
                } catch (Throwable th2) {
                    th = th2;
                    closeQuietly(fileInputStream);
                    throw th;
                }
            } else {
                closeQuietly(null);
                return null;
            }
        } catch (Exception unused2) {
            fileInputStream = null;
        } catch (Throwable th3) {
            th = th3;
            fileInputStream = null;
        }
        closeQuietly(fileInputStream);
        return null;
    }

    public static void createNoMediaFileIfNotExist(String str) {
        try {
            File file = new File(str + "/autonavi/.nomedia");
            if (!file.exists()) {
                file.createNewFile();
            }
            if (file.lastModified() > 0) {
                file.setLastModified(0L);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getAppSDCardFileDir() {
        File file;
        if (Environment.getExternalStorageState().equals("mounted")) {
            file = new File(Environment.getExternalStorageDirectory(), "autonavi");
            if (!file.exists()) {
                file.mkdir();
            }
        } else {
            file = null;
        }
        if (file == null) {
            return null;
        }
        return file.toString();
    }

    public static File getCacheDir(Context context) {
        File cacheDir = context.getCacheDir();
        if (cacheDir == null) {
            cacheDir = context.getDir("cache", 0);
        }
        if (cacheDir == null) {
            cacheDir = new File("/data/data/" + context.getPackageName() + "/app_cache");
        }
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        return cacheDir;
    }

    public static File getFilesDir(Context context) {
        File filesDir = context.getFilesDir();
        if (filesDir == null) {
            filesDir = context.getDir("files", 0);
        }
        if (filesDir == null) {
            filesDir = new File("/data/data/" + context.getPackageName() + "/app_files");
        }
        if (!filesDir.exists()) {
            filesDir.mkdirs();
        }
        return filesDir;
    }
}
