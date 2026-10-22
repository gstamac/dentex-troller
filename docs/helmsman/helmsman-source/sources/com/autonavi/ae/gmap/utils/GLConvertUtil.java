package com.autonavi.ae.gmap.utils;

import java.io.ByteArrayOutputStream;
import kotlin.UByte;

/* JADX INFO: loaded from: classes.dex */
public class GLConvertUtil {
    public static byte[] convertInt(int i) {
        return new byte[]{(byte) (i & 255), (byte) ((i >> 8) & 255), (byte) ((i >> 16) & 255), (byte) ((i >> 24) & 255)};
    }

    public static byte[] convertShort(int i) {
        return new byte[]{(byte) (i & 255), (byte) ((i >> 8) & 255)};
    }

    public static int getInt(byte[] bArr, int i) {
        return ((bArr[i + 3] & UByte.MAX_VALUE) << 24) + ((bArr[i + 2] & UByte.MAX_VALUE) << 16) + ((bArr[i + 1] & UByte.MAX_VALUE) << 8) + ((bArr[i + 0] & UByte.MAX_VALUE) << 0);
    }

    public static long getLong(byte[] bArr, int i) {
        return (((long) (bArr[i + 7] & UByte.MAX_VALUE)) << 56) + (((long) (bArr[i + 6] & UByte.MAX_VALUE)) << 48) + (((long) (bArr[i + 5] & UByte.MAX_VALUE)) << 40) + (((long) (bArr[i + 4] & UByte.MAX_VALUE)) << 32) + (((long) (bArr[i + 3] & UByte.MAX_VALUE)) << 24) + (((long) (bArr[i + 2] & UByte.MAX_VALUE)) << 16) + (((long) (bArr[i + 1] & UByte.MAX_VALUE)) << 8) + (((long) (bArr[i + 0] & UByte.MAX_VALUE)) << 0);
    }

    public static int getInt2(byte[] bArr, int i) {
        return ((bArr[i + 0] & UByte.MAX_VALUE) << 24) + ((bArr[i + 1] & UByte.MAX_VALUE) << 16) + ((bArr[i + 2] & UByte.MAX_VALUE) << 8) + ((bArr[i + 3] & UByte.MAX_VALUE) << 0);
    }

    public static short getShort(byte[] bArr, int i) {
        return (short) (((bArr[i + 1] & UByte.MAX_VALUE) << 8) + ((bArr[i + 0] & UByte.MAX_VALUE) << 0));
    }

    public static int getUShort(byte[] bArr, int i) {
        return ((bArr[i + 1] & UByte.MAX_VALUE) << 8) + ((bArr[i + 0] & UByte.MAX_VALUE) << 0);
    }

    public static short getShort2(byte[] bArr, int i) {
        return (short) (((bArr[i + 0] & UByte.MAX_VALUE) << 8) + ((bArr[i + 1] & UByte.MAX_VALUE) << 0));
    }

    public static double convertDouble(byte[] bArr, int i) {
        long j = 0;
        for (int i2 = 0; i2 < 8; i2++) {
            j += ((long) (bArr[i2 + i] & UByte.MAX_VALUE)) << (i2 * 8);
        }
        return Double.longBitsToDouble(j);
    }

    public static void writeInt(byte[] bArr, int i, int i2) {
        System.arraycopy(convertInt(i2), 0, bArr, i, 4);
    }

    public static void writeShort(byte[] bArr, int i, short s) {
        System.arraycopy(convertShort(s), 0, bArr, i, 2);
    }

    public static void moveArray(byte[] bArr, int i, byte[] bArr2, int i2, int i3) {
        byte[] bArr3 = new byte[i3];
        System.arraycopy(bArr, i, bArr3, 0, i3);
        System.arraycopy(bArr3, 0, bArr2, i2, i3);
    }

    public static byte[] get1BString(String str) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bytes = str.getBytes("UTF-8");
            byteArrayOutputStream.write(new byte[]{(byte) bytes.length});
            byteArrayOutputStream.write(bytes);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[1];
        }
    }

    public static String getString(byte[] bArr, int i, int i2) {
        try {
            return new String(bArr, i, i2, "UTF-8");
        } catch (Exception unused) {
            return "";
        }
    }

    public static byte[] getSubBytes(byte[] bArr, int i, int i2) {
        byte[] bArr2 = new byte[i2];
        System.arraycopy(bArr, i, bArr2, 0, i2);
        return bArr2;
    }
}
