package com.autonavi.amap.mapcore;

import android.content.Context;
import android.text.TextUtils;
import com.autonavi.ae.utils.NaviUtils;
import com.qiniu.android.common.Constants;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/* JADX INFO: loaded from: classes.dex */
public class AEUtil {
    private static final int BUFFER = 1024;
    public static final String CONFIGNAME = "GNaviConfig.xml";
    public static final boolean IS_AE = true;
    public static final String RESZIPNAME = "res.zip";
    private static final String TAG;
    private static String currentPath;

    public static class UnZipFileBrake {
        public boolean mIsAborted = false;
    }

    public interface ZipCompressProgressListener {
        void onFinishProgress(long j);
    }

    private static String getDataVersion() {
        return "";
    }

    public static String getEngineVersion() {
        return "n/a";
    }

    public static String getMapVersion() {
        return "n/a";
    }

    public static String getNaviRouteVersion() {
        return "n/a";
    }

    public static String getPosVersion() {
        return "n/a";
    }

    static {
        System.loadLibrary("GNaviUtils");
        System.loadLibrary("GNaviData");
        System.loadLibrary("GNaviSearch");
        System.loadLibrary("RoadLineRebuildAPI");
        System.loadLibrary("GNaviMap");
        System.loadLibrary("GNaviMapex");
        TAG = "AEUtil";
        currentPath = null;
    }

    public static void init(Context context) throws Throwable {
        currentPath = FileUtil.getMapBaseStorage(context);
        File file = new File(currentPath);
        if (!file.exists()) {
            file.mkdir();
        }
        File file2 = new File(currentPath, CONFIGNAME);
        if (file2.exists() && file2.isFile() && file2.length() > 0) {
            NaviUtils.nativeSetConfigFile(currentPath, currentPath + CONFIGNAME);
            return;
        }
        byte[] assetsFile = readAssetsFile("ae/GNaviConfig.xml", context);
        if (assetsFile == null || assetsFile.length <= 0) {
            return;
        }
        NaviUtils.nativeSetConfigMem(currentPath, getString(assetsFile, Constants.UTF_8));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v10 */
    /* JADX WARN: Type inference failed for: r2v11, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r2v13 */
    /* JADX WARN: Type inference failed for: r2v14 */
    /* JADX WARN: Type inference failed for: r2v15 */
    /* JADX WARN: Type inference failed for: r2v16 */
    /* JADX WARN: Type inference failed for: r2v17 */
    /* JADX WARN: Type inference failed for: r2v18 */
    /* JADX WARN: Type inference failed for: r2v3 */
    /* JADX WARN: Type inference failed for: r2v4, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r2v6, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r2v7, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r2v8, types: [java.io.IOException] */
    /* JADX WARN: Type inference failed for: r2v9 */
    private static void loadEngineRes(String str, Context context) {
        File file = new File(str, "res");
        if (!file.exists() || !file.isDirectory()) {
            file.mkdirs();
        }
        if (checkEngineRes(file)) {
            return;
        }
        ?? e = 0;
        e = 0;
        e = 0;
        try {
            try {
                try {
                    e = context.getAssets().open("ae/res.zip");
                    decompress(e, file.getAbsolutePath());
                    e = e;
                    if (e != 0) {
                        e.close();
                        e = e;
                    }
                } catch (IOException e2) {
                    e = e2;
                    e.printStackTrace();
                }
            } catch (Exception e3) {
                e3.printStackTrace();
                if (e != 0) {
                    e.close();
                    e = e;
                }
            } catch (OutOfMemoryError e4) {
                e4.printStackTrace();
                if (e != 0) {
                    e.close();
                    e = e;
                }
            }
        } catch (Throwable th) {
            if (e != 0) {
                try {
                    e.close();
                } catch (IOException e5) {
                    e5.printStackTrace();
                }
            }
            throw th;
        }
    }

    private static boolean checkEngineRes(File file) {
        try {
            File[] fileArrListFiles = file.listFiles();
            return fileArrListFiles != null && fileArrListFiles.length >= 4;
        } catch (Throwable th) {
            th.printStackTrace();
            return false;
        }
    }

    /* JADX WARN: Code duplicated, block: B:82:0x008a A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:86:0x0080 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:95:? A[SYNTHETIC] */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v0 */
    /* JADX WARN: Type inference failed for: r1v1, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r1v2 */
    /* JADX WARN: Type inference failed for: r5v0, types: [java.lang.CharSequence, java.lang.String] */
    /* JADX WARN: Type inference failed for: r5v11, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r5v4 */
    /* JADX WARN: Type inference failed for: r5v5 */
    /* JADX WARN: Type inference failed for: r5v6 */
    /* JADX WARN: Type inference failed for: r5v7, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r5v9, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r6v1, types: [java.io.ByteArrayOutputStream] */
    /* JADX WARN: Type inference failed for: r6v13 */
    /* JADX WARN: Type inference failed for: r6v3 */
    public static byte[] readAssetsFile(String str, Context context) throws Throwable {
        ?? r6;
        ByteArrayOutputStream byteArrayOutputStream;
        ?? r1 = 0;
        try {
            try {
                if (TextUtils.isEmpty(str)) {
                    return null;
                }
                try {
                    str = context.getAssets().open(str);
                    try {
                        byteArrayOutputStream = new ByteArrayOutputStream();
                        try {
                            byte[] bArr = new byte[1024];
                            while (true) {
                                int i = str.read(bArr, 0, 1024);
                                if (i <= 0) {
                                    break;
                                }
                                byteArrayOutputStream.write(bArr, 0, i);
                            }
                            byte[] byteArray = byteArrayOutputStream.toByteArray();
                            if (str != 0) {
                                try {
                                    str.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            try {
                                byteArrayOutputStream.close();
                            } catch (IOException e2) {
                                e2.printStackTrace();
                            }
                            return byteArray;
                        } catch (Exception e3) {
                            e = e3;
                            e.printStackTrace();
                            if (str != 0) {
                                try {
                                    str.close();
                                } catch (IOException e4) {
                                    e4.printStackTrace();
                                }
                            }
                            if (byteArrayOutputStream != null) {
                                byteArrayOutputStream.close();
                            }
                            return null;
                        } catch (OutOfMemoryError e5) {
                            e = e5;
                            e.printStackTrace();
                            if (str != 0) {
                                try {
                                    str.close();
                                } catch (IOException e6) {
                                    e6.printStackTrace();
                                }
                            }
                            if (byteArrayOutputStream != null) {
                                byteArrayOutputStream.close();
                            }
                            return null;
                        }
                    } catch (Exception e7) {
                        e = e7;
                        byteArrayOutputStream = null;
                    } catch (OutOfMemoryError e8) {
                        e = e8;
                        byteArrayOutputStream = null;
                    } catch (Throwable th) {
                        th = th;
                        context = null;
                        r1 = str;
                        r6 = context;
                        if (r1 != 0) {
                            try {
                                r1.close();
                            } catch (IOException e9) {
                                e9.printStackTrace();
                            }
                        }
                        if (r6 != 0) {
                            throw th;
                        }
                        try {
                            r6.close();
                            throw th;
                        } catch (IOException e10) {
                            e10.printStackTrace();
                            throw th;
                        }
                    }
                } catch (Exception e11) {
                    e = e11;
                    str = 0;
                    byteArrayOutputStream = null;
                } catch (OutOfMemoryError e12) {
                    e = e12;
                    str = 0;
                    byteArrayOutputStream = null;
                } catch (Throwable th2) {
                    th = th2;
                    r6 = 0;
                    if (r1 != 0) {
                        r1.close();
                    }
                    if (r6 != 0) {
                        throw th;
                    }
                    r6.close();
                    throw th;
                }
            } catch (IOException e13) {
                e13.printStackTrace();
            }
        } catch (Throwable th3) {
            th = th3;
        }
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
        } catch (Throwable unused) {
        }
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

    public static String getString(byte[] bArr, String str) {
        if (bArr == null) {
            throw new IllegalArgumentException("Parameter may not be null");
        }
        return getString(bArr, 0, bArr.length, str);
    }

    public static String getString(byte[] bArr, int i, int i2, String str) {
        if (bArr == null) {
            throw new IllegalArgumentException("Parameter may not be null");
        }
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("charset may not be null or empty");
        }
        try {
            return new String(bArr, i, i2, str);
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    public static void decompress(InputStream inputStream, String str) throws Exception {
        decompress(inputStream, str, 0L, null);
    }

    private static void decompress(InputStream inputStream, String str, long j, ZipCompressProgressListener zipCompressProgressListener) throws Exception {
        CheckedInputStream checkedInputStream = new CheckedInputStream(inputStream, new CRC32());
        ZipInputStream zipInputStream = new ZipInputStream(checkedInputStream);
        decompress(null, new File(str), zipInputStream, j, zipCompressProgressListener, null);
        zipInputStream.close();
        checkedInputStream.close();
    }

    private static void decompress(File file, File file2, ZipInputStream zipInputStream, long j, ZipCompressProgressListener zipCompressProgressListener, UnZipFileBrake unZipFileBrake) throws Exception {
        boolean z = false;
        int iDecompressFile = 0;
        while (true) {
            ZipEntry nextEntry = zipInputStream.getNextEntry();
            if (nextEntry == null) {
                break;
            }
            if (unZipFileBrake != null && unZipFileBrake.mIsAborted) {
                zipInputStream.closeEntry();
                return;
            }
            String name = nextEntry.getName();
            if (TextUtils.isEmpty(name) || name.contains("../")) {
                z = true;
                break;
            }
            File file3 = new File(file2.getPath() + File.separator + name);
            fileProber(file3);
            if (nextEntry.isDirectory()) {
                file3.mkdirs();
            } else {
                iDecompressFile += decompressFile(file3, zipInputStream, iDecompressFile, j, zipCompressProgressListener, unZipFileBrake);
            }
            zipInputStream.closeEntry();
        }
        if (!z || file == null) {
            return;
        }
        try {
            file.delete();
        } catch (Exception unused) {
        }
    }

    private static void fileProber(File file) {
        File parentFile = file.getParentFile();
        if (parentFile.exists()) {
            return;
        }
        fileProber(parentFile);
        parentFile.mkdir();
    }

    private static int decompressFile(File file, ZipInputStream zipInputStream, long j, long j2, ZipCompressProgressListener zipCompressProgressListener, UnZipFileBrake unZipFileBrake) throws Exception {
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
        byte[] bArr = new byte[1024];
        int i = 0;
        while (true) {
            int i2 = zipInputStream.read(bArr, 0, 1024);
            if (i2 != -1) {
                if (unZipFileBrake != null && unZipFileBrake.mIsAborted) {
                    bufferedOutputStream.close();
                    return i;
                }
                bufferedOutputStream.write(bArr, 0, i2);
                i += i2;
                if (j2 > 0 && zipCompressProgressListener != null) {
                    long j3 = ((((long) i) + j) * 100) / j2;
                    if (unZipFileBrake == null || !unZipFileBrake.mIsAborted) {
                        zipCompressProgressListener.onFinishProgress(j3);
                    }
                }
            } else {
                bufferedOutputStream.close();
                return i;
            }
        }
    }
}
