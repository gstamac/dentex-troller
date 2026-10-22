package okio.internal;

import java.util.Arrays;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import okio.Utf8;

/* JADX INFO: compiled from: -Utf8.kt */
/* JADX INFO: loaded from: classes2.dex */
@Metadata(d1 = {"\u0000\u0016\n\u0000\n\u0002\u0010\u0012\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\u001a\n\u0010\u0000\u001a\u00020\u0001*\u00020\u0002\u001a\u001e\u0010\u0003\u001a\u00020\u0002*\u00020\u00012\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0005¨\u0006\u0007"}, d2 = {"commonAsUtf8ToByteArray", "", "", "commonToUtf8String", "beginIndex", "", "endIndex", "okio"}, k = 2, mv = {1, 5, 1}, xi = 48)
public final class _Utf8Kt {
    public static /* synthetic */ String commonToUtf8String$default(byte[] bArr, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = bArr.length;
        }
        return commonToUtf8String(bArr, i, i2);
    }

    /* JADX WARN: Code restructure failed: missing block: B:49:0x0098, code lost:
    
        if (((r16[r5] & 192) == 128) == false) goto L23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:94:0x011c, code lost:
    
        if (((r16[r5] & 192) == 128) == false) goto L97;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final java.lang.String commonToUtf8String(byte[] r16, int r17, int r18) {
        /*
            Method dump skipped, instruction units count: 491
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: okio.internal._Utf8Kt.commonToUtf8String(byte[], int, int):java.lang.String");
    }

    public static final byte[] commonAsUtf8ToByteArray(String str) {
        int i;
        int i2;
        Intrinsics.checkNotNullParameter(str, "<this>");
        byte[] bArr = new byte[str.length() * 4];
        int length = str.length();
        if (length > 0) {
            int i3 = 0;
            while (true) {
                int i4 = i3 + 1;
                char cCharAt = str.charAt(i3);
                if (Intrinsics.compare((int) cCharAt, 128) >= 0) {
                    int length2 = str.length();
                    int i5 = i3;
                    while (i3 < length2) {
                        char cCharAt2 = str.charAt(i3);
                        if (Intrinsics.compare((int) cCharAt2, 128) < 0) {
                            int i6 = i5 + 1;
                            bArr[i5] = (byte) cCharAt2;
                            i3++;
                            while (true) {
                                i5 = i6;
                                if (i3 >= length2 || Intrinsics.compare((int) str.charAt(i3), 128) >= 0) {
                                    break;
                                }
                                i6 = i5 + 1;
                                bArr[i5] = (byte) str.charAt(i3);
                                i3++;
                            }
                        } else {
                            if (Intrinsics.compare((int) cCharAt2, 2048) < 0) {
                                int i7 = i5 + 1;
                                bArr[i5] = (byte) ((cCharAt2 >> 6) | 192);
                                byte b = (byte) ((cCharAt2 & '?') | 128);
                                i = i7 + 1;
                                bArr[i7] = b;
                            } else {
                                if (55296 <= cCharAt2 && cCharAt2 <= 57343) {
                                    if (Intrinsics.compare((int) cCharAt2, 56319) <= 0 && length2 > (i2 = i3 + 1)) {
                                        char cCharAt3 = str.charAt(i2);
                                        if (56320 <= cCharAt3 && cCharAt3 <= 57343) {
                                            int iCharAt = ((cCharAt2 << '\n') + str.charAt(i2)) - 56613888;
                                            int i8 = i5 + 1;
                                            bArr[i5] = (byte) ((iCharAt >> 18) | 240);
                                            int i9 = i8 + 1;
                                            bArr[i8] = (byte) (((iCharAt >> 12) & 63) | 128);
                                            int i10 = i9 + 1;
                                            bArr[i9] = (byte) (((iCharAt >> 6) & 63) | 128);
                                            byte b2 = (byte) ((iCharAt & 63) | 128);
                                            i = i10 + 1;
                                            bArr[i10] = b2;
                                            i3 += 2;
                                        }
                                        i5 = i;
                                    }
                                    i = i5 + 1;
                                    bArr[i5] = Utf8.REPLACEMENT_BYTE;
                                } else {
                                    int i11 = i5 + 1;
                                    bArr[i5] = (byte) ((cCharAt2 >> '\f') | 224);
                                    int i12 = i11 + 1;
                                    bArr[i11] = (byte) (((cCharAt2 >> 6) & 63) | 128);
                                    byte b3 = (byte) ((cCharAt2 & '?') | 128);
                                    i = i12 + 1;
                                    bArr[i12] = b3;
                                }
                            }
                            i3++;
                            i5 = i;
                        }
                    }
                    byte[] bArrCopyOf = Arrays.copyOf(bArr, i5);
                    Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "java.util.Arrays.copyOf(this, newSize)");
                    return bArrCopyOf;
                }
                bArr[i3] = (byte) cCharAt;
                if (i4 < length) {
                    i3 = i4;
                }
            }
        }
        byte[] bArrCopyOf2 = Arrays.copyOf(bArr, str.length());
        Intrinsics.checkNotNullExpressionValue(bArrCopyOf2, "java.util.Arrays.copyOf(this, newSize)");
        return bArrCopyOf2;
    }
}
