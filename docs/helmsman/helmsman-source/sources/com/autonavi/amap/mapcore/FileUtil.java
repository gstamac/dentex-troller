package com.autonavi.amap.mapcore;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.storage.StorageManager;
import com.amap.api.maps.MapsInitializer;
import com.qiniu.android.common.Constants;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* JADX INFO: loaded from: classes.dex */
public class FileUtil {
    private static final String TAG = "FileUtil";

    public static void createNoMediaFileIfNotExist(String str) {
    }

    public static void copy(Context context, String str, File file) throws Exception {
        file.delete();
        InputStream inputStreamOpen = context.getAssets().open(str);
        byte[] bArr = new byte[inputStreamOpen.available()];
        inputStreamOpen.read(bArr);
        inputStreamOpen.close();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(bArr);
        fileOutputStream.close();
    }

    public static boolean deleteFile(File file) {
        File[] fileArrListFiles;
        if (file == null || !file.exists()) {
            return false;
        }
        if (file.isDirectory() && (fileArrListFiles = file.listFiles()) != null) {
            for (int i = 0; i < fileArrListFiles.length; i++) {
                if (fileArrListFiles[i].isFile()) {
                    if (!fileArrListFiles[i].delete()) {
                        return false;
                    }
                } else {
                    if (!deleteFile(fileArrListFiles[i])) {
                        return false;
                    }
                    fileArrListFiles[i].delete();
                }
            }
        }
        file.delete();
        return true;
    }

    public static String getMapBaseStorage(Context context) {
        String string;
        if (context == null) {
            return null;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences("base_path", 0);
        if (MapsInitializer.sdcardDir != null && MapsInitializer.sdcardDir.trim().length() > 0) {
            string = MapsInitializer.sdcardDir;
        } else {
            string = sharedPreferences.getString("map_base_path_v44", "");
        }
        if (string != null && string.length() > 2) {
            File file = new File(string);
            if (!file.exists()) {
                file.mkdir();
            }
            if (file.isDirectory()) {
                if (checkCanWrite(file)) {
                    return string;
                }
                String str = context.getCacheDir().toString() + File.separator + "amap" + File.separator;
                if (str != null && str.length() > 2) {
                    File file2 = new File(str);
                    if (!file2.exists()) {
                        file2.mkdir();
                    }
                    if (file2.isDirectory()) {
                        return str;
                    }
                }
            }
        }
        String str2 = getExternalStroragePath(context) + File.separator + "amap" + File.separator;
        if (str2 != null && str2.length() > 2) {
            File file3 = new File(str2);
            if (!file3.exists()) {
                file3.mkdir();
            }
            if (file3.isDirectory() && file3.canWrite()) {
                SharedPreferences.Editor editorEdit = sharedPreferences.edit();
                editorEdit.putString("map_base_path_v44", str2);
                editorEdit.commit();
                createNoMediaFileIfNotExist(str2);
                return str2;
            }
        }
        String str3 = context.getCacheDir().toString() + File.separator + "amap" + File.separator;
        if (str3 != null && str3.length() > 2) {
            File file4 = new File(str3);
            if (!file4.exists()) {
                file4.mkdir();
            }
            file4.isDirectory();
        }
        return str3;
    }

    public static boolean checkCanWrite(File file) {
        if (file == null) {
            return false;
        }
        if (!file.canWrite()) {
            return true;
        }
        File file2 = new File(file, "amap.tmp");
        try {
            file2.createNewFile();
            if (!file2.exists()) {
                return false;
            }
            try {
                file2.delete();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } catch (IOException e2) {
            e2.printStackTrace();
            return false;
        }
    }

    public static String getExternalStroragePath(Context context) {
        String absolutePath;
        int i;
        try {
            StorageManager storageManager = (StorageManager) context.getSystemService("storage");
            Method method = StorageManager.class.getMethod("getVolumeList", new Class[0]);
            int i2 = 1;
            Method method2 = StorageManager.class.getMethod("getVolumeState", String.class);
            Object[] objArr = (Object[]) method.invoke(storageManager, new Object[0]);
            Boolean.valueOf(false);
            int length = objArr.length;
            String str = "";
            int i3 = 0;
            String str2 = "";
            while (true) {
                if (i3 < length) {
                    Object obj = objArr[i3];
                    Method method3 = obj.getClass().getMethod("getPath", new Class[0]);
                    Method method4 = obj.getClass().getMethod("isRemovable", new Class[0]);
                    absolutePath = (String) method3.invoke(obj, new Object[0]);
                    Object[] objArr2 = new Object[i2];
                    objArr2[0] = method3.invoke(obj, new Object[0]);
                    String str3 = (String) method2.invoke(storageManager, objArr2);
                    Boolean bool = (Boolean) method4.invoke(obj, new Object[0]);
                    if (absolutePath.toLowerCase().contains("private")) {
                        i = 1;
                    } else if (bool.booleanValue()) {
                        if (absolutePath != null && str3 != null && str3.equals("mounted")) {
                            try {
                                File[] externalFilesDirs = context.getExternalFilesDirs(null);
                                if (externalFilesDirs != null) {
                                    if (externalFilesDirs.length <= 1) {
                                        break;
                                    }
                                    absolutePath = externalFilesDirs[1].getAbsolutePath();
                                    break;
                                }
                            } catch (Exception unused) {
                                break;
                            }
                        }
                        i = 1;
                    } else {
                        i = 1;
                        str2 = str3;
                        str = absolutePath;
                    }
                    i3++;
                    i2 = i;
                }
                absolutePath = null;
                break;
            }
            return (str == null || str2 == null || !str2.equals("mounted")) ? absolutePath : str;
        } catch (Throwable unused2) {
            if (Environment.getExternalStorageState().equals("mounted")) {
                return Environment.getExternalStorageDirectory().toString();
            }
            return null;
        }
    }

    public static void writeDatasToFile(String str, byte[] bArr) {
        ReentrantReadWriteLock.WriteLock writeLock = new ReentrantReadWriteLock().writeLock();
        writeLock.lock();
        try {
            if (bArr != null) {
                try {
                    if (bArr.length != 0) {
                        File file = new File(str);
                        if (file.exists()) {
                            file.delete();
                        }
                        file.createNewFile();
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        fileOutputStream.write(bArr);
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } finally {
            writeLock.unlock();
        }
    }

    public static byte[] readFileContents(String str) {
        try {
            File file = new File(str);
            if (!file.exists()) {
                return null;
            }
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] bArr = new byte[1024];
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            while (true) {
                int i = fileInputStream.read(bArr);
                if (i != -1) {
                    byteArrayOutputStream.write(bArr, 0, i);
                } else {
                    byteArrayOutputStream.close();
                    fileInputStream.close();
                    return byteArrayOutputStream.toByteArray();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void saveFile(String str, String str2, boolean z) {
        try {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + str2);
            if (!file.exists()) {
                new File(file.getParent()).mkdirs();
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file, z);
            fileOutputStream.write(str.getBytes(Constants.UTF_8));
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }
}
