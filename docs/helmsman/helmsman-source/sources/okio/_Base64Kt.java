package okio;

import java.util.Arrays;
import kotlin.Metadata;
import kotlin.UByte;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: -Base64.kt */
/* JADX INFO: loaded from: classes2.dex */
@Metadata(d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0012\n\u0002\b\b\n\u0002\u0010\u000e\n\u0002\b\u0003\u001a\u000e\u0010\t\u001a\u0004\u0018\u00010\u0001*\u00020\nH\u0000\u001a\u0016\u0010\u000b\u001a\u00020\n*\u00020\u00012\b\b\u0002\u0010\f\u001a\u00020\u0001H\u0000\"\u001c\u0010\u0000\u001a\u00020\u00018\u0000X\u0081\u0004¢\u0006\u000e\n\u0000\u0012\u0004\b\u0002\u0010\u0003\u001a\u0004\b\u0004\u0010\u0005\"\u001c\u0010\u0006\u001a\u00020\u00018\u0000X\u0081\u0004¢\u0006\u000e\n\u0000\u0012\u0004\b\u0007\u0010\u0003\u001a\u0004\b\b\u0010\u0005¨\u0006\r"}, d2 = {"BASE64", "", "getBASE64$annotations", "()V", "getBASE64", "()[B", "BASE64_URL_SAFE", "getBASE64_URL_SAFE$annotations", "getBASE64_URL_SAFE", "decodeBase64ToArray", "", "encodeBase64", "map", "okio"}, k = 2, mv = {1, 5, 1}, xi = 48)
public final class _Base64Kt {
    private static final byte[] BASE64 = ByteString.Companion.encodeUtf8("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/").getData$okio();
    private static final byte[] BASE64_URL_SAFE = ByteString.Companion.encodeUtf8("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_").getData$okio();

    public static /* synthetic */ void getBASE64$annotations() {
    }

    public static /* synthetic */ void getBASE64_URL_SAFE$annotations() {
    }

    public static final byte[] getBASE64() {
        return BASE64;
    }

    public static final byte[] getBASE64_URL_SAFE() {
        return BASE64_URL_SAFE;
    }

    /* JADX WARN: Code duplicated, block: B:63:0x00b7 A[LOOP:1: B:16:0x003d->B:63:0x00b7, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:87:0x00b3 A[SYNTHETIC] */
    public static final byte[] decodeBase64ToArray(String str) {
        int i;
        int i2;
        int i3;
        int i4;
        char cCharAt;
        Intrinsics.checkNotNullParameter(str, "<this>");
        int length = str.length();
        while (length > 0 && ((cCharAt = str.charAt(length - 1)) == '=' || cCharAt == '\n' || cCharAt == '\r' || cCharAt == ' ' || cCharAt == '\t')) {
            length--;
        }
        int i5 = (int) ((((long) length) * 6) / 8);
        byte[] bArr = new byte[i5];
        if (length > 0) {
            int i6 = 0;
            i2 = 0;
            int i7 = 0;
            int i8 = 0;
            while (true) {
                int i9 = i6 + 1;
                char cCharAt2 = str.charAt(i6);
                if ('A' <= cCharAt2 && cCharAt2 <= 'Z') {
                    i4 = cCharAt2 - 'A';
                } else {
                    if ('a' <= cCharAt2 && cCharAt2 <= 'z') {
                        i4 = cCharAt2 - 'G';
                    } else {
                        if ('0' <= cCharAt2 && cCharAt2 <= '9') {
                            i4 = cCharAt2 + 4;
                        } else if (cCharAt2 == '+' || cCharAt2 == '-') {
                            i4 = 62;
                        } else {
                            if (cCharAt2 == '/' || cCharAt2 == '_') {
                                i4 = 63;
                            } else if (cCharAt2 != '\n' && cCharAt2 != '\r' && cCharAt2 != ' ' && cCharAt2 != '\t') {
                                return null;
                            }
                            if (i9 >= length) {
                                i = i7;
                                i3 = i8;
                                break;
                            }
                            i6 = i9;
                        }
                    }
                }
                i8 = (i8 << 6) | i4;
                i7++;
                if (i7 % 4 == 0) {
                    int i10 = i2 + 1;
                    bArr[i2] = (byte) (i8 >> 16);
                    int i11 = i10 + 1;
                    bArr[i10] = (byte) (i8 >> 8);
                    i2 = i11 + 1;
                    bArr[i11] = (byte) i8;
                }
                if (i9 >= length) {
                    i = i7;
                    i3 = i8;
                    break;
                }
                i6 = i9;
            }
        } else {
            i = 0;
            i2 = 0;
            i3 = 0;
        }
        int i12 = i % 4;
        if (i12 == 1) {
            return null;
        }
        if (i12 == 2) {
            bArr[i2] = (byte) ((i3 << 12) >> 16);
            i2++;
        } else if (i12 == 3) {
            int i13 = i3 << 6;
            int i14 = i2 + 1;
            bArr[i2] = (byte) (i13 >> 16);
            i2 = i14 + 1;
            bArr[i14] = (byte) (i13 >> 8);
        }
        if (i2 == i5) {
            return bArr;
        }
        byte[] bArrCopyOf = Arrays.copyOf(bArr, i2);
        Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "java.util.Arrays.copyOf(this, newSize)");
        return bArrCopyOf;
    }

    public static /* synthetic */ String encodeBase64$default(byte[] bArr, byte[] bArr2, int i, Object obj) {
        if ((i & 1) != 0) {
            bArr2 = BASE64;
        }
        return encodeBase64(bArr, bArr2);
    }

    public static final String encodeBase64(byte[] bArr, byte[] map) {
        Intrinsics.checkNotNullParameter(bArr, "<this>");
        Intrinsics.checkNotNullParameter(map, "map");
        byte[] bArr2 = new byte[((bArr.length + 2) / 3) * 4];
        int length = bArr.length - (bArr.length % 3);
        int i = 0;
        int i2 = 0;
        while (i < length) {
            int i3 = i + 1;
            byte b = bArr[i];
            int i4 = i3 + 1;
            byte b2 = bArr[i3];
            int i5 = i4 + 1;
            byte b3 = bArr[i4];
            int i6 = i2 + 1;
            bArr2[i2] = map[(b & UByte.MAX_VALUE) >> 2];
            int i7 = i6 + 1;
            bArr2[i6] = map[((b & 3) << 4) | ((b2 & UByte.MAX_VALUE) >> 4)];
            int i8 = i7 + 1;
            bArr2[i7] = map[((b2 & 15) << 2) | ((b3 & UByte.MAX_VALUE) >> 6)];
            i2 = i8 + 1;
            bArr2[i8] = map[b3 & Utf8.REPLACEMENT_BYTE];
            i = i5;
        }
        int length2 = bArr.length - length;
        if (length2 == 1) {
            byte b4 = bArr[i];
            int i9 = i2 + 1;
            bArr2[i2] = map[(b4 & UByte.MAX_VALUE) >> 2];
            int i10 = i9 + 1;
            bArr2[i9] = map[(b4 & 3) << 4];
            byte b5 = (byte) 61;
            bArr2[i10] = b5;
            bArr2[i10 + 1] = b5;
        } else if (length2 == 2) {
            int i11 = i + 1;
            byte b6 = bArr[i];
            byte b7 = bArr[i11];
            int i12 = i2 + 1;
            bArr2[i2] = map[(b6 & UByte.MAX_VALUE) >> 2];
            int i13 = i12 + 1;
            bArr2[i12] = map[((b6 & 3) << 4) | ((b7 & UByte.MAX_VALUE) >> 4)];
            bArr2[i13] = map[(b7 & 15) << 2];
            bArr2[i13 + 1] = (byte) 61;
        }
        return _JvmPlatformKt.toUtf8String(bArr2);
    }
}
