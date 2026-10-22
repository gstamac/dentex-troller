package org.bouncycastle.util;

import com.autonavi.ae.gmap.utils.GLMapStaticValue;
import java.io.ByteArrayOutputStream;
import java.util.Vector;
import kotlin.UByte;
import okio.Utf8;

/* JADX INFO: loaded from: classes2.dex */
public final class Strings {
    public static String fromUTF8ByteArray(byte[] bArr) {
        char c;
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        while (i2 < bArr.length) {
            i3++;
            byte b = bArr[i2];
            if ((b & 240) == 240) {
                i3++;
                i2 += 4;
            } else if ((b & 224) == 224) {
                i2 += 3;
            } else {
                i2 = (b & 192) == 192 ? i2 + 2 : i2 + 1;
            }
        }
        char[] cArr = new char[i3];
        int i4 = 0;
        while (i < bArr.length) {
            byte b2 = bArr[i];
            if ((b2 & 240) == 240) {
                int i5 = (((((b2 & 3) << 18) | ((bArr[i + 1] & Utf8.REPLACEMENT_BYTE) << 12)) | ((bArr[i + 2] & Utf8.REPLACEMENT_BYTE) << 6)) | (bArr[i + 3] & Utf8.REPLACEMENT_BYTE)) - 65536;
                char c2 = (char) ((i5 >> 10) | 55296);
                c = (char) ((i5 & GLMapStaticValue.AM_PARAMETERNAME_PROCESS_ROADARROW) | Utf8.LOG_SURROGATE_HEADER);
                cArr[i4] = c2;
                i += 4;
                i4++;
            } else if ((b2 & 224) == 224) {
                c = (char) (((b2 & 15) << 12) | ((bArr[i + 1] & Utf8.REPLACEMENT_BYTE) << 6) | (bArr[i + 2] & Utf8.REPLACEMENT_BYTE));
                i += 3;
            } else if ((b2 & 208) == 208 || (b2 & 192) == 192) {
                int i6 = (b2 & 31) << 6;
                byte b3 = bArr[i + 1];
                c = (char) (i6 | (b3 & Utf8.REPLACEMENT_BYTE));
                i += 2;
            } else {
                c = (char) (b2 & UByte.MAX_VALUE);
                i++;
            }
            cArr[i4] = c;
            i4++;
        }
        return new String(cArr);
    }

    public static String[] split(String str, char c) {
        int i;
        Vector vector = new Vector();
        boolean z = true;
        while (true) {
            if (!z) {
                break;
            }
            int iIndexOf = str.indexOf(c);
            if (iIndexOf > 0) {
                vector.addElement(str.substring(0, iIndexOf));
                str = str.substring(iIndexOf + 1);
            } else {
                vector.addElement(str);
                z = false;
            }
        }
        int size = vector.size();
        String[] strArr = new String[size];
        for (i = 0; i != size; i++) {
            strArr[i] = (String) vector.elementAt(i);
        }
        return strArr;
    }

    public static byte[] toByteArray(String str) {
        int length = str.length();
        byte[] bArr = new byte[length];
        for (int i = 0; i != length; i++) {
            bArr[i] = (byte) str.charAt(i);
        }
        return bArr;
    }

    public static String toLowerCase(String str) {
        char[] charArray = str.toCharArray();
        boolean z = false;
        for (int i = 0; i != charArray.length; i++) {
            char c = charArray[i];
            if ('A' <= c && 'Z' >= c) {
                charArray[i] = (char) ((c - 'A') + 97);
                z = true;
            }
        }
        return z ? new String(charArray) : str;
    }

    public static byte[] toUTF8ByteArray(String str) {
        return toUTF8ByteArray(str.toCharArray());
    }

    public static byte[] toUTF8ByteArray(char[] cArr) {
        int i;
        int i2;
        int i3;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i4 = 0;
        while (i4 < cArr.length) {
            char c = cArr[i4];
            if (c >= 128) {
                if (c < 2048) {
                    i = c;
                    i2 = (c >> 6) | 192;
                } else {
                    if (c < 55296 || c > 57343) {
                        i = c;
                        i = c;
                        byteArrayOutputStream.write((c >> '\f') | 224);
                        i2 = ((c >> 6) & 63) | 128;
                    } else {
                        i4++;
                        if (i4 >= cArr.length) {
                            i = c;
                            throw new IllegalStateException("invalid UTF-16 codepoint");
                        }
                        char c2 = cArr[i4];
                        if (c > 56319) {
                            i = c;
                            throw new IllegalStateException("invalid UTF-16 codepoint");
                        }
                        i = c;
                        int i5 = (((c & 1023) << 10) | (c2 & 1023)) + 65536;
                        byteArrayOutputStream.write((i5 >> 18) | 240);
                        byteArrayOutputStream.write(((i5 >> 12) & 63) | 128);
                        byteArrayOutputStream.write(((i5 >> 6) & 63) | 128);
                        i3 = i5;
                    }
                    i = (i3 & 63) | 128;
                }
                byteArrayOutputStream.write(i2);
                i3 = c;
                i = (i3 & 63) | 128;
            }
            i = c;
            byteArrayOutputStream.write(i);
            i4++;
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static String toUpperCase(String str) {
        char[] charArray = str.toCharArray();
        boolean z = false;
        for (int i = 0; i != charArray.length; i++) {
            char c = charArray[i];
            if ('a' <= c && 'z' >= c) {
                charArray[i] = (char) ((c - 'a') + 65);
                z = true;
            }
        }
        return z ? new String(charArray) : str;
    }
}
