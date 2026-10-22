package im.helmsman.helmsmanandroid.utils;

import android.os.Environment;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

/* JADX INFO: loaded from: classes2.dex */
public class FileUtils {
    /* JADX WARN: Type inference failed for: r0v0, types: [im.helmsman.helmsmanandroid.utils.FileUtils$1] */
    public static void zipFile(final File file, final String str) {
        new Thread() { // from class: im.helmsman.helmsmanandroid.utils.FileUtils.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() throws Throwable {
                FileOutputStream fileOutputStream;
                GZIPOutputStream gZIPOutputStream;
                Throwable th;
                FileInputStream fileInputStream;
                GZIPOutputStream gZIPOutputStream2;
                IOException e;
                super.run();
                try {
                    try {
                        try {
                            fileInputStream = new FileInputStream(file);
                            try {
                                fileOutputStream = new FileOutputStream(file.getParentFile().getPath() + "/" + str);
                                try {
                                    gZIPOutputStream2 = new GZIPOutputStream(fileOutputStream);
                                    try {
                                        byte[] bArr = new byte[1024];
                                        while (true) {
                                            int i = fileInputStream.read(bArr);
                                            if (i == -1) {
                                                break;
                                            } else {
                                                gZIPOutputStream2.write(bArr, 0, i);
                                            }
                                        }
                                        file.delete();
                                        try {
                                            gZIPOutputStream2.flush();
                                            gZIPOutputStream2.close();
                                        } catch (IOException e2) {
                                            e2.printStackTrace();
                                        }
                                        try {
                                            fileOutputStream.close();
                                        } catch (IOException e3) {
                                            e3.printStackTrace();
                                        }
                                        fileInputStream.close();
                                    } catch (IOException e4) {
                                        e = e4;
                                        e.printStackTrace();
                                        if (gZIPOutputStream2 != null) {
                                            try {
                                                gZIPOutputStream2.flush();
                                                gZIPOutputStream2.close();
                                            } catch (IOException e5) {
                                                e5.printStackTrace();
                                            }
                                        }
                                        if (fileOutputStream != null) {
                                            try {
                                                fileOutputStream.close();
                                            } catch (IOException e6) {
                                                e6.printStackTrace();
                                            }
                                        }
                                        if (fileInputStream == null) {
                                        } else {
                                            fileInputStream.close();
                                        }
                                    }
                                } catch (IOException e7) {
                                    gZIPOutputStream2 = null;
                                    e = e7;
                                } catch (Throwable th2) {
                                    gZIPOutputStream = null;
                                    th = th2;
                                    if (gZIPOutputStream != null) {
                                        try {
                                            gZIPOutputStream.flush();
                                            gZIPOutputStream.close();
                                        } catch (IOException e8) {
                                            e8.printStackTrace();
                                        }
                                    }
                                    if (fileOutputStream != null) {
                                        try {
                                            fileOutputStream.close();
                                        } catch (IOException e9) {
                                            e9.printStackTrace();
                                        }
                                    }
                                    if (fileInputStream != null) {
                                        try {
                                            fileInputStream.close();
                                            throw th;
                                        } catch (IOException e10) {
                                            e10.printStackTrace();
                                            throw th;
                                        }
                                    }
                                    throw th;
                                }
                            } catch (IOException e11) {
                                gZIPOutputStream2 = null;
                                e = e11;
                                fileOutputStream = null;
                            } catch (Throwable th3) {
                                gZIPOutputStream = null;
                                th = th3;
                                fileOutputStream = null;
                            }
                        } catch (Throwable th4) {
                            th = th4;
                        }
                    } catch (IOException e12) {
                        fileOutputStream = null;
                        gZIPOutputStream2 = null;
                        e = e12;
                        fileInputStream = null;
                    } catch (Throwable th5) {
                        fileOutputStream = null;
                        gZIPOutputStream = null;
                        th = th5;
                        fileInputStream = null;
                    }
                } catch (IOException e13) {
                    e13.printStackTrace();
                }
            }
        }.start();
    }

    public static void zipFileList(String str) {
        File[] fileArrListFiles;
        File file = new File(str);
        if (file.exists() && file.isDirectory() && (fileArrListFiles = file.listFiles()) != null) {
            for (int i = 0; i < fileArrListFiles.length && !fileArrListFiles[i].getName().endsWith(".gz"); i++) {
                zipFile(fileArrListFiles[i], fileArrListFiles[i].getName() + ".gz");
            }
        }
    }

    public static String getInnerSDCardPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }
}
