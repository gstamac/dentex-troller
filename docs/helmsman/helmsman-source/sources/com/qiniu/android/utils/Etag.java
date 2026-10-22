package com.qiniu.android.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/* JADX INFO: loaded from: classes2.dex */
public final class Etag {
    /* JADX WARN: Code duplicated, block: B:32:0x002c A[EXC_TOP_SPLITTER, SYNTHETIC] */
    public static String data(byte[] bArr, int i, int i2) throws Throwable {
        ByteArrayInputStream byteArrayInputStream;
        ByteArrayInputStream byteArrayInputStream2 = null;
        try {
            byteArrayInputStream = new ByteArrayInputStream(bArr, i, i2);
            try {
                try {
                    String strStream = stream(byteArrayInputStream, i2);
                    try {
                        byteArrayInputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return strStream;
                } catch (IOException e2) {
                    e = e2;
                    e.printStackTrace();
                    if (byteArrayInputStream != null) {
                        try {
                            byteArrayInputStream.close();
                        } catch (Exception e3) {
                            e3.printStackTrace();
                        }
                    }
                    return null;
                }
            } catch (Throwable th) {
                th = th;
                byteArrayInputStream2 = byteArrayInputStream;
                if (byteArrayInputStream2 != null) {
                    try {
                        byteArrayInputStream2.close();
                    } catch (Exception e4) {
                        e4.printStackTrace();
                    }
                }
                throw th;
            }
        } catch (IOException e5) {
            e = e5;
            byteArrayInputStream = null;
        } catch (Throwable th2) {
            th = th2;
            if (byteArrayInputStream2 != null) {
                byteArrayInputStream2.close();
            }
            throw th;
        }
    }

    public static String data(byte[] bArr) {
        return data(bArr, 0, bArr.length);
    }

    public static String file(File file) throws Throwable {
        FileInputStream fileInputStream = null;
        try {
            FileInputStream fileInputStream2 = new FileInputStream(file);
            try {
                String strStream = stream(fileInputStream2, file.length());
                try {
                    fileInputStream2.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return strStream;
            } catch (Throwable th) {
                th = th;
                fileInputStream = fileInputStream2;
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public static String file(String str) throws IOException {
        return file(new File(str));
    }

    public static String stream(InputStream inputStream, long j) throws IOException {
        if (j == 0) {
            return "Fto5o-5ea0sNMlW_75VgGJCv2AcJ";
        }
        byte[] bArr = new byte[65536];
        int i = (int) (((j + 4194304) - 1) / 4194304);
        byte[][] bArr2 = new byte[i][];
        for (int i2 = 0; i2 < i; i2++) {
            long j2 = j - (((long) i2) * 4194304);
            if (j2 > 4194304) {
                j2 = 4194304;
            }
            bArr2[i2] = oneBlock(bArr, inputStream, (int) j2);
        }
        return resultEncode(bArr2);
    }

    private static byte[] oneBlock(byte[] bArr, InputStream inputStream, int i) throws IOException {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("sha-1");
            int length = bArr.length;
            while (i != 0) {
                int i2 = length > i ? i : length;
                inputStream.read(bArr, 0, i2);
                messageDigest.update(bArr, 0, i2);
                i -= i2;
            }
            return messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String resultEncode(byte[][] bArr) {
        byte b;
        byte[] bArrDigest = bArr[0];
        int length = bArrDigest.length;
        byte[] bArr2 = new byte[length + 1];
        if (bArr.length != 1) {
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("sha-1");
                for (byte[] bArr3 : bArr) {
                    messageDigest.update(bArr3);
                }
                bArrDigest = messageDigest.digest();
                b = -106;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            b = 22;
        }
        bArr2[0] = b;
        System.arraycopy(bArrDigest, 0, bArr2, 1, length);
        return UrlSafeBase64.encodeToString(bArr2);
    }
}
