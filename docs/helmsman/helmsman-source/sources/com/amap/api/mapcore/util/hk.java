package com.amap.api.mapcore.util;

import android.content.Context;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Random;

/* JADX INFO: compiled from: Utils.java */
/* JADX INFO: loaded from: classes.dex */
public class hk {
    static byte[] a(gs gsVar, String str) {
        return a(gsVar, str, true);
    }

    static byte[] a(gs gsVar, String str, boolean z) {
        gs.b bVarA;
        byte[] bArr = new byte[0];
        InputStream inputStream = null;
        try {
            bVarA = gsVar.a(str);
            if (bVarA == null) {
                if (bVarA != null) {
                    try {
                        bVarA.close();
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }
                return bArr;
            }
            try {
                InputStream inputStreamA = bVarA.a(0);
                if (inputStreamA == null) {
                    if (inputStreamA != null) {
                        try {
                            inputStreamA.close();
                        } catch (Throwable th2) {
                            th2.printStackTrace();
                        }
                    }
                    if (bVarA != null) {
                        try {
                            bVarA.close();
                        } catch (Throwable th3) {
                            th3.printStackTrace();
                        }
                    }
                    return bArr;
                }
                bArr = new byte[inputStreamA.available()];
                inputStreamA.read(bArr);
                if (z) {
                    gsVar.c(str);
                }
                if (inputStreamA != null) {
                    try {
                        inputStreamA.close();
                    } catch (Throwable th4) {
                        th4.printStackTrace();
                    }
                }
                if (bVarA != null) {
                    try {
                        bVarA.close();
                    } catch (Throwable th5) {
                        th5.printStackTrace();
                    }
                }
                return bArr;
            } catch (Throwable th6) {
                th = th6;
                try {
                    fk.a(th, "Utils", "readSingleLog");
                    return bArr;
                } finally {
                    if (0 != 0) {
                        try {
                            inputStream.close();
                        } catch (Throwable th7) {
                            th7.printStackTrace();
                        }
                    }
                    if (bVarA != null) {
                        try {
                            bVarA.close();
                        } catch (Throwable th8) {
                            th8.printStackTrace();
                        }
                    }
                }
            }
        } catch (Throwable th9) {
            th = th9;
            bVarA = null;
        }
    }

    public static String a() {
        return fg.a(new Date().getTime());
    }

    public static String a(Context context, ff ffVar) {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("\"sim\":\"").append(fa.e(context)).append("\",\"sdkversion\":\"").append(ffVar.c()).append("\",\"product\":\"").append(ffVar.a()).append("\",\"ed\":\"").append(ffVar.e()).append("\",\"nt\":\"").append(fa.c(context)).append("\",\"np\":\"").append(fa.a(context)).append("\",\"mnc\":\"").append(fa.b(context)).append("\",\"ant\":\"").append(fa.d(context)).append("\"");
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return sb.toString();
    }

    public static String a(String str, String str2, String str3, int i, String str4, String str5) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str2).append(",\"timestamp\":\"");
        stringBuffer.append(str3);
        stringBuffer.append("\",\"et\":\"");
        stringBuffer.append(i);
        stringBuffer.append("\",\"classname\":\"");
        stringBuffer.append(str4);
        stringBuffer.append("\",\"detail\":\"");
        stringBuffer.append(str5);
        stringBuffer.append("\"");
        return stringBuffer.toString();
    }

    public static byte[] a(Context context, String str, int i) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] byteArray = new byte[0];
        String strA = fl.a(context, str);
        gs gsVarA = null;
        try {
            try {
                try {
                    gsVarA = gs.a(new File(strA), 1, 1, i);
                    File file = new File(strA);
                    if (file.exists()) {
                        for (String str2 : file.list()) {
                            if (str2.contains(".0")) {
                                byteArrayOutputStream.write(a(gsVarA, str2.split("\\.")[0]));
                            }
                        }
                    }
                    byteArray = byteArrayOutputStream.toByteArray();
                    try {
                        byteArrayOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (gsVarA != null) {
                        gsVarA.close();
                    }
                } catch (Throwable th) {
                    try {
                        byteArrayOutputStream.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                    if (gsVarA == null) {
                        throw th;
                    }
                    try {
                        gsVarA.close();
                        throw th;
                    } catch (Throwable th2) {
                        th2.printStackTrace();
                        throw th;
                    }
                }
            } catch (IOException e3) {
                fk.a(e3, "Statistics.Utils", "getContent");
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
                if (gsVarA != null) {
                    gsVarA.close();
                }
            } catch (Throwable th3) {
                fk.a(th3, "Statistics.Utils", "getContent");
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e5) {
                    e5.printStackTrace();
                }
                if (gsVarA != null) {
                    gsVarA.close();
                }
            }
        } catch (Throwable th4) {
            th4.printStackTrace();
        }
        return byteArray;
    }

    public static int a(Context context, String str) {
        try {
            File file = new File(fl.a(context, str));
            if (file.exists()) {
                return file.list().length;
            }
            return 0;
        } catch (Throwable th) {
            fk.a(th, "Statistics.Utils", "getFileNum");
            return 0;
        }
    }

    /* JADX WARN: Code duplicated, block: B:62:0x0095 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:75:? A[Catch: all -> 0x009e, SYNTHETIC, TryCatch #8 {, blocks: (B:8:0x000a, B:38:0x0086, B:21:0x0065, B:16:0x005b, B:32:0x007b, B:51:0x009d, B:50:0x009a, B:45:0x0090, B:27:0x006d, B:47:0x0095, B:29:0x0076, B:13:0x0056, B:42:0x008b), top: B:72:0x000a, inners: #2, #3, #5, #6, #9 }] */
    public static void a(Context context, String str, int i, byte[] bArr) {
        gs gsVarA;
        if (bArr == null || bArr.length == 0) {
            return;
        }
        synchronized (hk.class) {
            Random random = new Random();
            OutputStream outputStreamA = null;
            try {
                gsVarA = gs.a(new File(fl.a(context, str)), 1, 1, i);
                try {
                    gs.a aVarB = gsVarA.b(Integer.toString(random.nextInt(100)) + Long.toString(System.nanoTime()));
                    outputStreamA = aVarB.a(0);
                    outputStreamA.write(bArr);
                    aVarB.a();
                    gsVarA.e();
                    if (outputStreamA != null) {
                        try {
                            outputStreamA.close();
                        } catch (Throwable th) {
                            th.printStackTrace();
                        }
                    }
                    if (gsVarA != null) {
                        try {
                            gsVarA.close();
                        } catch (Throwable th2) {
                            th = th2;
                            th.printStackTrace();
                        }
                    }
                } catch (Throwable th3) {
                    th = th3;
                    try {
                        fk.a(th, "Statistics.Utils", "writeToDiskLruCache");
                        if (outputStreamA != null) {
                            try {
                                outputStreamA.close();
                            } catch (Throwable th4) {
                                th4.printStackTrace();
                            }
                        }
                        if (gsVarA != null) {
                            try {
                                gsVarA.close();
                            } catch (Throwable th5) {
                                th = th5;
                                th.printStackTrace();
                            }
                        }
                    } catch (Throwable th6) {
                        if (outputStreamA == null) {
                            if (gsVarA != null) {
                                gsVarA.close();
                                throw th6;
                            }
                            throw th6;
                        }
                        try {
                            outputStreamA.close();
                        } catch (Throwable th7) {
                            th7.printStackTrace();
                        }
                        if (gsVarA != null) {
                            try {
                                gsVarA.close();
                                throw th6;
                            } catch (Throwable th8) {
                                th8.printStackTrace();
                                throw th6;
                            }
                        }
                        throw th6;
                    }
                }
            } catch (Throwable th9) {
                th = th9;
                gsVarA = null;
            }
        }
    }

    public static long b(Context context, String str) {
        FileInputStream fileInputStream;
        Throwable th;
        IOException e;
        FileNotFoundException e2;
        File file = new File(fl.a(context, str));
        if (!file.exists()) {
            return 0L;
        }
        try {
            try {
                try {
                    fileInputStream = new FileInputStream(file);
                    try {
                        byte[] bArr = new byte[fileInputStream.available()];
                        fileInputStream.read(bArr);
                        long j = Long.parseLong(fg.a(bArr));
                        try {
                            fileInputStream.close();
                        } catch (Throwable th2) {
                            th2.printStackTrace();
                        }
                        return j;
                    } catch (FileNotFoundException e3) {
                        e2 = e3;
                        fk.a(e2, "StatisticsManager", "getUpdateTime");
                        if (fileInputStream != null) {
                            fileInputStream.close();
                        }
                        return 0L;
                    } catch (IOException e4) {
                        e = e4;
                        fk.a(e, "StatisticsManager", "getUpdateTime");
                        if (fileInputStream != null) {
                            fileInputStream.close();
                        }
                        return 0L;
                    } catch (Throwable th3) {
                        th = th3;
                        fk.a(th, "StatisticsManager", "getUpdateTime");
                        try {
                            if (file.exists()) {
                                file.delete();
                            }
                        } catch (Throwable th4) {
                            th4.printStackTrace();
                        }
                        if (fileInputStream != null) {
                            fileInputStream.close();
                        }
                        return 0L;
                    }
                } catch (Throwable th5) {
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (Throwable th6) {
                            th6.printStackTrace();
                        }
                    }
                    throw th5;
                }
            } catch (FileNotFoundException e5) {
                fileInputStream = null;
                e2 = e5;
            } catch (IOException e6) {
                fileInputStream = null;
                e = e6;
            } catch (Throwable th7) {
                fileInputStream = null;
                th = th7;
            }
        } catch (Throwable th8) {
            th8.printStackTrace();
        }
    }

    /* JADX WARN: Code duplicated, block: B:41:0x0057 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    public static void a(Context context, long j, String str) throws Throwable {
        FileOutputStream fileOutputStream;
        Throwable th;
        IOException e;
        FileNotFoundException e2;
        File file = new File(fl.a(context, str));
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            try {
                try {
                    fileOutputStream = new FileOutputStream(file);
                    try {
                        fileOutputStream.write(fg.a(String.valueOf(j)));
                        fileOutputStream.close();
                    } catch (FileNotFoundException e3) {
                        e2 = e3;
                        e2.printStackTrace();
                        if (fileOutputStream != null) {
                            fileOutputStream.close();
                        }
                    } catch (IOException e4) {
                        e = e4;
                        e.printStackTrace();
                        if (fileOutputStream != null) {
                            fileOutputStream.close();
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (Throwable th3) {
                            th3.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (FileNotFoundException e5) {
                fileOutputStream = null;
                e2 = e5;
            } catch (IOException e6) {
                fileOutputStream = null;
                e = e6;
            } catch (Throwable th4) {
                fileOutputStream = null;
                th = th4;
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                throw th;
            }
        } catch (Throwable th5) {
            th5.printStackTrace();
        }
    }
}
