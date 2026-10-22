package okio.internal;

import androidx.constraintlayout.core.motion.utils.TypedValues;
import com.facebook.internal.FacebookRequestErrorClassification;
import java.util.Arrays;
import kotlin.Metadata;
import kotlin.UByte;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.ByteCompanionObject;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import okio.Buffer;
import okio.ByteString;
import okio._Base64Kt;
import okio._JvmPlatformKt;
import okio._UtilKt;

/* JADX INFO: compiled from: -ByteString.kt */
/* JADX INFO: loaded from: classes2.dex */
@Metadata(d1 = {"\u0000R\n\u0000\n\u0002\u0010\u0019\n\u0002\b\u0005\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\f\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0005\n\u0002\b\u0017\n\u0002\u0018\u0002\n\u0000\u001a\u0018\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u0007H\u0002\u001a\u0011\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\tH\u0080\b\u001a\u0010\u0010\u000e\u001a\u00020\u00072\u0006\u0010\u000f\u001a\u00020\u0010H\u0002\u001a\r\u0010\u0011\u001a\u00020\u0012*\u00020\fH\u0080\b\u001a\r\u0010\u0013\u001a\u00020\u0012*\u00020\fH\u0080\b\u001a\u0015\u0010\u0014\u001a\u00020\u0007*\u00020\f2\u0006\u0010\u0015\u001a\u00020\fH\u0080\b\u001a-\u0010\u0016\u001a\u00020\u0017*\u00020\f2\u0006\u0010\u0018\u001a\u00020\u00072\u0006\u0010\u0019\u001a\u00020\t2\u0006\u0010\u001a\u001a\u00020\u00072\u0006\u0010\u001b\u001a\u00020\u0007H\u0080\b\u001a\u000f\u0010\u001c\u001a\u0004\u0018\u00010\f*\u00020\u0012H\u0080\b\u001a\r\u0010\u001d\u001a\u00020\f*\u00020\u0012H\u0080\b\u001a\r\u0010\u001e\u001a\u00020\f*\u00020\u0012H\u0080\b\u001a\u0015\u0010\u001f\u001a\u00020 *\u00020\f2\u0006\u0010!\u001a\u00020\tH\u0080\b\u001a\u0015\u0010\u001f\u001a\u00020 *\u00020\f2\u0006\u0010!\u001a\u00020\fH\u0080\b\u001a\u0017\u0010\"\u001a\u00020 *\u00020\f2\b\u0010\u0015\u001a\u0004\u0018\u00010#H\u0080\b\u001a\u0015\u0010$\u001a\u00020%*\u00020\f2\u0006\u0010&\u001a\u00020\u0007H\u0080\b\u001a\r\u0010'\u001a\u00020\u0007*\u00020\fH\u0080\b\u001a\r\u0010(\u001a\u00020\u0007*\u00020\fH\u0080\b\u001a\r\u0010)\u001a\u00020\u0012*\u00020\fH\u0080\b\u001a\u001d\u0010*\u001a\u00020\u0007*\u00020\f2\u0006\u0010\u0015\u001a\u00020\t2\u0006\u0010+\u001a\u00020\u0007H\u0080\b\u001a\r\u0010,\u001a\u00020\t*\u00020\fH\u0080\b\u001a\u001d\u0010-\u001a\u00020\u0007*\u00020\f2\u0006\u0010\u0015\u001a\u00020\t2\u0006\u0010+\u001a\u00020\u0007H\u0080\b\u001a\u001d\u0010-\u001a\u00020\u0007*\u00020\f2\u0006\u0010\u0015\u001a\u00020\f2\u0006\u0010+\u001a\u00020\u0007H\u0080\b\u001a-\u0010.\u001a\u00020 *\u00020\f2\u0006\u0010\u0018\u001a\u00020\u00072\u0006\u0010\u0015\u001a\u00020\t2\u0006\u0010/\u001a\u00020\u00072\u0006\u0010\u001b\u001a\u00020\u0007H\u0080\b\u001a-\u0010.\u001a\u00020 *\u00020\f2\u0006\u0010\u0018\u001a\u00020\u00072\u0006\u0010\u0015\u001a\u00020\f2\u0006\u0010/\u001a\u00020\u00072\u0006\u0010\u001b\u001a\u00020\u0007H\u0080\b\u001a\u0015\u00100\u001a\u00020 *\u00020\f2\u0006\u00101\u001a\u00020\tH\u0080\b\u001a\u0015\u00100\u001a\u00020 *\u00020\f2\u0006\u00101\u001a\u00020\fH\u0080\b\u001a\u001d\u00102\u001a\u00020\f*\u00020\f2\u0006\u00103\u001a\u00020\u00072\u0006\u00104\u001a\u00020\u0007H\u0080\b\u001a\r\u00105\u001a\u00020\f*\u00020\fH\u0080\b\u001a\r\u00106\u001a\u00020\f*\u00020\fH\u0080\b\u001a\r\u00107\u001a\u00020\t*\u00020\fH\u0080\b\u001a\u001d\u00108\u001a\u00020\f*\u00020\t2\u0006\u0010\u0018\u001a\u00020\u00072\u0006\u0010\u001b\u001a\u00020\u0007H\u0080\b\u001a\r\u00109\u001a\u00020\u0012*\u00020\fH\u0080\b\u001a\r\u0010:\u001a\u00020\u0012*\u00020\fH\u0080\b\u001a$\u0010;\u001a\u00020\u0017*\u00020\f2\u0006\u0010<\u001a\u00020=2\u0006\u0010\u0018\u001a\u00020\u00072\u0006\u0010\u001b\u001a\u00020\u0007H\u0000\"\u001c\u0010\u0000\u001a\u00020\u00018\u0000X\u0081\u0004¢\u0006\u000e\n\u0000\u0012\u0004\b\u0002\u0010\u0003\u001a\u0004\b\u0004\u0010\u0005¨\u0006>"}, d2 = {"HEX_DIGIT_CHARS", "", "getHEX_DIGIT_CHARS$annotations", "()V", "getHEX_DIGIT_CHARS", "()[C", "codePointIndexToCharIndex", "", "s", "", "codePointCount", "commonOf", "Lokio/ByteString;", "data", "decodeHexDigit", "c", "", "commonBase64", "", "commonBase64Url", "commonCompareTo", FacebookRequestErrorClassification.KEY_OTHER, "commonCopyInto", "", TypedValues.CycleType.S_WAVE_OFFSET, TypedValues.AttributesType.S_TARGET, "targetOffset", "byteCount", "commonDecodeBase64", "commonDecodeHex", "commonEncodeUtf8", "commonEndsWith", "", "suffix", "commonEquals", "", "commonGetByte", "", "pos", "commonGetSize", "commonHashCode", "commonHex", "commonIndexOf", "fromIndex", "commonInternalArray", "commonLastIndexOf", "commonRangeEquals", "otherOffset", "commonStartsWith", "prefix", "commonSubstring", "beginIndex", "endIndex", "commonToAsciiLowercase", "commonToAsciiUppercase", "commonToByteArray", "commonToByteString", "commonToString", "commonUtf8", "commonWrite", "buffer", "Lokio/Buffer;", "okio"}, k = 2, mv = {1, 5, 1}, xi = 48)
public final class _ByteStringKt {
    private static final char[] HEX_DIGIT_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static /* synthetic */ void getHEX_DIGIT_CHARS$annotations() {
    }

    public static final String commonUtf8(ByteString byteString) {
        Intrinsics.checkNotNullParameter(byteString, "<this>");
        String utf8 = byteString.getUtf8();
        if (utf8 != null) {
            return utf8;
        }
        String utf8String = _JvmPlatformKt.toUtf8String(byteString.internalArray$okio());
        byteString.setUtf8$okio(utf8String);
        return utf8String;
    }

    public static final String commonBase64(ByteString byteString) {
        Intrinsics.checkNotNullParameter(byteString, "<this>");
        return _Base64Kt.encodeBase64$default(byteString.getData(), null, 1, null);
    }

    public static final String commonBase64Url(ByteString byteString) {
        Intrinsics.checkNotNullParameter(byteString, "<this>");
        return _Base64Kt.encodeBase64(byteString.getData(), _Base64Kt.getBASE64_URL_SAFE());
    }

    public static final char[] getHEX_DIGIT_CHARS() {
        return HEX_DIGIT_CHARS;
    }

    public static final String commonHex(ByteString byteString) {
        Intrinsics.checkNotNullParameter(byteString, "<this>");
        char[] cArr = new char[byteString.getData().length * 2];
        byte[] data = byteString.getData();
        int length = data.length;
        int i = 0;
        int i2 = 0;
        while (i < length) {
            byte b = data[i];
            i++;
            int i3 = i2 + 1;
            cArr[i2] = getHEX_DIGIT_CHARS()[(b >> 4) & 15];
            i2 = i3 + 1;
            cArr[i3] = getHEX_DIGIT_CHARS()[b & 15];
        }
        return StringsKt.concatToString(cArr);
    }

    public static final ByteString commonToAsciiLowercase(ByteString byteString) {
        byte b;
        Intrinsics.checkNotNullParameter(byteString, "<this>");
        for (int i = 0; i < byteString.getData().length; i++) {
            byte b2 = byteString.getData()[i];
            byte b3 = (byte) 65;
            if (b2 >= b3 && b2 <= (b = (byte) 90)) {
                byte[] data = byteString.getData();
                byte[] bArrCopyOf = Arrays.copyOf(data, data.length);
                Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "java.util.Arrays.copyOf(this, size)");
                bArrCopyOf[i] = (byte) (b2 + 32);
                for (int i2 = i + 1; i2 < bArrCopyOf.length; i2++) {
                    byte b4 = bArrCopyOf[i2];
                    if (b4 >= b3 && b4 <= b) {
                        bArrCopyOf[i2] = (byte) (b4 + 32);
                    }
                }
                return new ByteString(bArrCopyOf);
            }
        }
        return byteString;
    }

    public static final ByteString commonToAsciiUppercase(ByteString byteString) {
        byte b;
        Intrinsics.checkNotNullParameter(byteString, "<this>");
        for (int i = 0; i < byteString.getData().length; i++) {
            byte b2 = byteString.getData()[i];
            byte b3 = (byte) 97;
            if (b2 >= b3 && b2 <= (b = (byte) 122)) {
                byte[] data = byteString.getData();
                byte[] bArrCopyOf = Arrays.copyOf(data, data.length);
                Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "java.util.Arrays.copyOf(this, size)");
                bArrCopyOf[i] = (byte) (b2 - 32);
                for (int i2 = i + 1; i2 < bArrCopyOf.length; i2++) {
                    byte b4 = bArrCopyOf[i2];
                    if (b4 >= b3 && b4 <= b) {
                        bArrCopyOf[i2] = (byte) (b4 - 32);
                    }
                }
                return new ByteString(bArrCopyOf);
            }
        }
        return byteString;
    }

    public static final ByteString commonSubstring(ByteString byteString, int i, int i2) {
        Intrinsics.checkNotNullParameter(byteString, "<this>");
        int iResolveDefaultParameter = _UtilKt.resolveDefaultParameter(byteString, i2);
        if (!(i >= 0)) {
            throw new IllegalArgumentException("beginIndex < 0".toString());
        }
        if (!(iResolveDefaultParameter <= byteString.getData().length)) {
            throw new IllegalArgumentException(("endIndex > length(" + byteString.getData().length + ')').toString());
        }
        if (iResolveDefaultParameter - i >= 0) {
            return (i == 0 && iResolveDefaultParameter == byteString.getData().length) ? byteString : new ByteString(ArraysKt.copyOfRange(byteString.getData(), i, iResolveDefaultParameter));
        }
        throw new IllegalArgumentException("endIndex < beginIndex".toString());
    }

    public static final byte commonGetByte(ByteString byteString, int i) {
        Intrinsics.checkNotNullParameter(byteString, "<this>");
        return byteString.getData()[i];
    }

    public static final int commonGetSize(ByteString byteString) {
        Intrinsics.checkNotNullParameter(byteString, "<this>");
        return byteString.getData().length;
    }

    public static final byte[] commonToByteArray(ByteString byteString) {
        Intrinsics.checkNotNullParameter(byteString, "<this>");
        byte[] data = byteString.getData();
        byte[] bArrCopyOf = Arrays.copyOf(data, data.length);
        Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "java.util.Arrays.copyOf(this, size)");
        return bArrCopyOf;
    }

    public static final byte[] commonInternalArray(ByteString byteString) {
        Intrinsics.checkNotNullParameter(byteString, "<this>");
        return byteString.getData();
    }

    public static final boolean commonRangeEquals(ByteString byteString, int i, ByteString other, int i2, int i3) {
        Intrinsics.checkNotNullParameter(byteString, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        return other.rangeEquals(i2, byteString.getData(), i, i3);
    }

    public static final boolean commonRangeEquals(ByteString byteString, int i, byte[] other, int i2, int i3) {
        Intrinsics.checkNotNullParameter(byteString, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        return i >= 0 && i <= byteString.getData().length - i3 && i2 >= 0 && i2 <= other.length - i3 && _UtilKt.arrayRangeEquals(byteString.getData(), i, other, i2, i3);
    }

    public static final void commonCopyInto(ByteString byteString, int i, byte[] target, int i2, int i3) {
        Intrinsics.checkNotNullParameter(byteString, "<this>");
        Intrinsics.checkNotNullParameter(target, "target");
        ArraysKt.copyInto(byteString.getData(), target, i2, i, i3 + i);
    }

    public static final boolean commonStartsWith(ByteString byteString, ByteString prefix) {
        Intrinsics.checkNotNullParameter(byteString, "<this>");
        Intrinsics.checkNotNullParameter(prefix, "prefix");
        return byteString.rangeEquals(0, prefix, 0, prefix.size());
    }

    public static final boolean commonStartsWith(ByteString byteString, byte[] prefix) {
        Intrinsics.checkNotNullParameter(byteString, "<this>");
        Intrinsics.checkNotNullParameter(prefix, "prefix");
        return byteString.rangeEquals(0, prefix, 0, prefix.length);
    }

    public static final boolean commonEndsWith(ByteString byteString, ByteString suffix) {
        Intrinsics.checkNotNullParameter(byteString, "<this>");
        Intrinsics.checkNotNullParameter(suffix, "suffix");
        return byteString.rangeEquals(byteString.size() - suffix.size(), suffix, 0, suffix.size());
    }

    public static final boolean commonEndsWith(ByteString byteString, byte[] suffix) {
        Intrinsics.checkNotNullParameter(byteString, "<this>");
        Intrinsics.checkNotNullParameter(suffix, "suffix");
        return byteString.rangeEquals(byteString.size() - suffix.length, suffix, 0, suffix.length);
    }

    public static final int commonIndexOf(ByteString byteString, byte[] other, int i) {
        Intrinsics.checkNotNullParameter(byteString, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        int length = byteString.getData().length - other.length;
        int iMax = Math.max(i, 0);
        if (iMax > length) {
            return -1;
        }
        while (true) {
            int i2 = iMax + 1;
            if (_UtilKt.arrayRangeEquals(byteString.getData(), iMax, other, 0, other.length)) {
                return iMax;
            }
            if (iMax == length) {
                return -1;
            }
            iMax = i2;
        }
    }

    public static final int commonLastIndexOf(ByteString byteString, ByteString other, int i) {
        Intrinsics.checkNotNullParameter(byteString, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        return byteString.lastIndexOf(other.internalArray$okio(), i);
    }

    public static final int commonLastIndexOf(ByteString byteString, byte[] other, int i) {
        Intrinsics.checkNotNullParameter(byteString, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        int iMin = Math.min(_UtilKt.resolveDefaultParameter(byteString, i), byteString.getData().length - other.length);
        if (iMin < 0) {
            return -1;
        }
        while (true) {
            int i2 = iMin - 1;
            if (_UtilKt.arrayRangeEquals(byteString.getData(), iMin, other, 0, other.length)) {
                return iMin;
            }
            if (i2 < 0) {
                return -1;
            }
            iMin = i2;
        }
    }

    public static final boolean commonEquals(ByteString byteString, Object obj) {
        Intrinsics.checkNotNullParameter(byteString, "<this>");
        if (obj == byteString) {
            return true;
        }
        if (obj instanceof ByteString) {
            ByteString byteString2 = (ByteString) obj;
            if (byteString2.size() == byteString.getData().length && byteString2.rangeEquals(0, byteString.getData(), 0, byteString.getData().length)) {
                return true;
            }
        }
        return false;
    }

    public static final int commonHashCode(ByteString byteString) {
        Intrinsics.checkNotNullParameter(byteString, "<this>");
        int hashCode = byteString.getHashCode();
        if (hashCode != 0) {
            return hashCode;
        }
        int iHashCode = Arrays.hashCode(byteString.getData());
        byteString.setHashCode$okio(iHashCode);
        return iHashCode;
    }

    public static final int commonCompareTo(ByteString byteString, ByteString other) {
        Intrinsics.checkNotNullParameter(byteString, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        int size = byteString.size();
        int size2 = other.size();
        int iMin = Math.min(size, size2);
        for (int i = 0; i < iMin; i++) {
            int i2 = byteString.getByte(i) & UByte.MAX_VALUE;
            int i3 = other.getByte(i) & UByte.MAX_VALUE;
            if (i2 != i3) {
                return i2 < i3 ? -1 : 1;
            }
        }
        if (size == size2) {
            return 0;
        }
        return size < size2 ? -1 : 1;
    }

    public static final ByteString commonOf(byte[] data) {
        Intrinsics.checkNotNullParameter(data, "data");
        byte[] bArrCopyOf = Arrays.copyOf(data, data.length);
        Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "java.util.Arrays.copyOf(this, size)");
        return new ByteString(bArrCopyOf);
    }

    public static final ByteString commonToByteString(byte[] bArr, int i, int i2) {
        Intrinsics.checkNotNullParameter(bArr, "<this>");
        _UtilKt.checkOffsetAndCount(bArr.length, i, i2);
        return new ByteString(ArraysKt.copyOfRange(bArr, i, i2 + i));
    }

    public static final ByteString commonEncodeUtf8(String str) {
        Intrinsics.checkNotNullParameter(str, "<this>");
        ByteString byteString = new ByteString(_JvmPlatformKt.asUtf8ToByteArray(str));
        byteString.setUtf8$okio(str);
        return byteString;
    }

    public static final ByteString commonDecodeBase64(String str) {
        Intrinsics.checkNotNullParameter(str, "<this>");
        byte[] bArrDecodeBase64ToArray = _Base64Kt.decodeBase64ToArray(str);
        if (bArrDecodeBase64ToArray != null) {
            return new ByteString(bArrDecodeBase64ToArray);
        }
        return null;
    }

    public static final ByteString commonDecodeHex(String str) {
        Intrinsics.checkNotNullParameter(str, "<this>");
        int i = 0;
        if (!(str.length() % 2 == 0)) {
            throw new IllegalArgumentException(Intrinsics.stringPlus("Unexpected hex string: ", str).toString());
        }
        int length = str.length() / 2;
        byte[] bArr = new byte[length];
        int i2 = length - 1;
        if (i2 >= 0) {
            while (true) {
                int i3 = i + 1;
                int i4 = i * 2;
                bArr[i] = (byte) ((decodeHexDigit(str.charAt(i4)) << 4) + decodeHexDigit(str.charAt(i4 + 1)));
                if (i3 > i2) {
                    break;
                }
                i = i3;
            }
        }
        return new ByteString(bArr);
    }

    public static final void commonWrite(ByteString byteString, Buffer buffer, int i, int i2) {
        Intrinsics.checkNotNullParameter(byteString, "<this>");
        Intrinsics.checkNotNullParameter(buffer, "buffer");
        buffer.write(byteString.getData(), i, i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final int decodeHexDigit(char c) {
        if ('0' <= c && c <= '9') {
            return c - '0';
        }
        char c2 = 'a';
        if (!('a' <= c && c <= 'f')) {
            c2 = 'A';
            if (!('A' <= c && c <= 'F')) {
                throw new IllegalArgumentException(Intrinsics.stringPlus("Unexpected hex digit: ", Character.valueOf(c)));
            }
        }
        return (c - c2) + 10;
    }

    public static final String commonToString(ByteString byteString) {
        ByteString byteString2 = byteString;
        Intrinsics.checkNotNullParameter(byteString2, "<this>");
        if (byteString.getData().length == 0) {
            return "[size=0]";
        }
        int iCodePointIndexToCharIndex = codePointIndexToCharIndex(byteString.getData(), 64);
        if (iCodePointIndexToCharIndex == -1) {
            if (byteString.getData().length <= 64) {
                return "[hex=" + byteString.hex() + ']';
            }
            StringBuilder sbAppend = new StringBuilder("[size=").append(byteString.getData().length).append(" hex=");
            int iResolveDefaultParameter = _UtilKt.resolveDefaultParameter(byteString2, 64);
            if (!(iResolveDefaultParameter <= byteString.getData().length)) {
                throw new IllegalArgumentException(("endIndex > length(" + byteString.getData().length + ')').toString());
            }
            if (!(iResolveDefaultParameter + 0 >= 0)) {
                throw new IllegalArgumentException("endIndex < beginIndex".toString());
            }
            if (iResolveDefaultParameter != byteString.getData().length) {
                byteString2 = new ByteString(ArraysKt.copyOfRange(byteString.getData(), 0, iResolveDefaultParameter));
            }
            return sbAppend.append(byteString2.hex()).append("…]").toString();
        }
        String strUtf8 = byteString.utf8();
        if (strUtf8 != null) {
            String strSubstring = strUtf8.substring(0, iCodePointIndexToCharIndex);
            Intrinsics.checkNotNullExpressionValue(strSubstring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
            String strReplace$default = StringsKt.replace$default(StringsKt.replace$default(StringsKt.replace$default(strSubstring, "\\", "\\\\", false, 4, (Object) null), "\n", "\\n", false, 4, (Object) null), "\r", "\\r", false, 4, (Object) null);
            if (iCodePointIndexToCharIndex < strUtf8.length()) {
                return "[size=" + byteString.getData().length + " text=" + strReplace$default + "…]";
            }
            return "[text=" + strReplace$default + ']';
        }
        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:101:0x00db  */
    /* JADX WARN: Code duplicated, block: B:103:0x00df  */
    /* JADX WARN: Code duplicated, block: B:104:0x00e2  */
    /* JADX WARN: Code duplicated, block: B:162:0x0163  */
    /* JADX WARN: Code duplicated, block: B:164:0x0167  */
    /* JADX WARN: Code duplicated, block: B:167:0x016d  */
    /* JADX WARN: Code duplicated, block: B:169:0x0171  */
    /* JADX WARN: Code duplicated, block: B:170:0x0174  */
    /* JADX WARN: Code duplicated, block: B:23:0x0041  */
    /* JADX WARN: Code duplicated, block: B:241:0x020e  */
    /* JADX WARN: Code duplicated, block: B:243:0x0212  */
    /* JADX WARN: Code duplicated, block: B:246:0x0218  */
    /* JADX WARN: Code duplicated, block: B:248:0x021c  */
    /* JADX WARN: Code duplicated, block: B:249:0x021f  */
    /* JADX WARN: Code duplicated, block: B:262:0x0081 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:269:0x00da A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:277:0x016c A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:288:0x0217 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:52:0x007b  */
    /* JADX WARN: Code duplicated, block: B:54:0x007f A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:56:0x0082 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:57:0x0084  */
    /* JADX WARN: Code duplicated, block: B:58:0x0087  */
    /* JADX WARN: Code duplicated, block: B:97:0x00d4  */
    /* JADX WARN: Code duplicated, block: B:99:0x00d8 A[DONT_INVERT] */
    public static final int codePointIndexToCharIndex(byte[] bArr, int i) {
        byte b;
        int i2;
        boolean z;
        boolean z2;
        int i3;
        int i4;
        boolean z3;
        int i5;
        boolean z4;
        int i6;
        boolean z5;
        int length = bArr.length;
        int i7 = 0;
        int i8 = 0;
        int i9 = 0;
        while (i7 < length) {
            byte b2 = bArr[i7];
            if (b2 >= 0) {
                int i10 = i9 + 1;
                if (i9 == i) {
                    return i8;
                }
                if (b2 != 10 && b2 != 13) {
                    if (b2 >= 0 && b2 <= 31) {
                        z2 = true;
                    } else {
                        if (127 <= b2 && b2 <= 159) {
                            z2 = true;
                        } else {
                            z2 = false;
                        }
                    }
                    if (!z2) {
                    }
                    return -1;
                }
                if (b2 == 65533) {
                    return -1;
                }
                i8 += b2 < 65536 ? 1 : 2;
                i7++;
                while (true) {
                    i9 = i10;
                    if (i7 >= length || (b = bArr[i7]) < 0) {
                        break;
                    }
                    i7++;
                    i10 = i9 + 1;
                    if (i9 == i) {
                        return i8;
                    }
                    if (b != 10 && b != 13) {
                        if (b >= 0 && b <= 31) {
                            z = true;
                        } else {
                            if (127 <= b && b <= 159) {
                                z = true;
                            } else {
                                z = false;
                            }
                        }
                        if (!z) {
                            if (b == 65533) {
                                if (b < 65536) {
                                    i2 = 1;
                                } else {
                                    i2 = 2;
                                }
                                i8 += i2;
                            }
                        }
                    } else if (b == 65533) {
                        if (b < 65536) {
                            i2 = 1;
                        } else {
                            i2 = 2;
                        }
                        i8 += i2;
                    }
                    return -1;
                }
            }
            if ((b2 >> 5) == -2) {
                int i11 = i7 + 1;
                if (length <= i11) {
                    if (i9 == i) {
                        return i8;
                    }
                    return -1;
                }
                byte b3 = bArr[i11];
                if (!((b3 & 192) == 128)) {
                    if (i9 == i) {
                        return i8;
                    }
                    return -1;
                }
                int i12 = (b3 ^ ByteCompanionObject.MIN_VALUE) ^ (b2 << 6);
                if (i12 < 128) {
                    if (i9 == i) {
                        return i8;
                    }
                    return -1;
                }
                i3 = i9 + 1;
                if (i9 == i) {
                    return i8;
                }
                if (i12 != 10 && i12 != 13) {
                    if (i12 >= 0 && i12 <= 31) {
                        z5 = true;
                    } else {
                        if (127 <= i12 && i12 <= 159) {
                            z5 = true;
                        } else {
                            z5 = false;
                        }
                    }
                    if (!z5) {
                        if (i12 == 65533) {
                            if (i12 < 65536) {
                                i6 = 1;
                            } else {
                                i6 = 2;
                            }
                            i8 += i6;
                            Unit unit = Unit.INSTANCE;
                            i7 += 2;
                        }
                    }
                } else if (i12 == 65533) {
                    if (i12 < 65536) {
                        i6 = 1;
                    } else {
                        i6 = 2;
                    }
                    i8 += i6;
                    Unit unit2 = Unit.INSTANCE;
                    i7 += 2;
                }
                return -1;
            }
            if ((b2 >> 4) == -2) {
                int i13 = i7 + 2;
                if (length <= i13) {
                    if (i9 == i) {
                        return i8;
                    }
                    return -1;
                }
                byte b4 = bArr[i7 + 1];
                if (!((b4 & 192) == 128)) {
                    if (i9 == i) {
                        return i8;
                    }
                    return -1;
                }
                byte b5 = bArr[i13];
                if (!((b5 & 192) == 128)) {
                    if (i9 == i) {
                        return i8;
                    }
                    return -1;
                }
                int i14 = ((b5 ^ ByteCompanionObject.MIN_VALUE) ^ (b4 << 6)) ^ (b2 << 12);
                if (i14 < 2048) {
                    if (i9 == i) {
                        return i8;
                    }
                    return -1;
                }
                if (55296 <= i14 && i14 <= 57343) {
                    if (i9 == i) {
                        return i8;
                    }
                    return -1;
                }
                i3 = i9 + 1;
                if (i9 == i) {
                    return i8;
                }
                if (i14 != 10 && i14 != 13) {
                    if (i14 >= 0 && i14 <= 31) {
                        z4 = true;
                    } else {
                        if (127 <= i14 && i14 <= 159) {
                            z4 = true;
                        } else {
                            z4 = false;
                        }
                    }
                    if (!z4) {
                        if (i14 == 65533) {
                            if (i14 < 65536) {
                                i5 = 1;
                            } else {
                                i5 = 2;
                            }
                            i8 += i5;
                            Unit unit3 = Unit.INSTANCE;
                            i7 += 3;
                        }
                    }
                } else if (i14 == 65533) {
                    if (i14 < 65536) {
                        i5 = 1;
                    } else {
                        i5 = 2;
                    }
                    i8 += i5;
                    Unit unit4 = Unit.INSTANCE;
                    i7 += 3;
                }
                return -1;
            }
            if ((b2 >> 3) != -2) {
                if (i9 == i) {
                    return i8;
                }
                return -1;
            }
            int i15 = i7 + 3;
            if (length <= i15) {
                if (i9 == i) {
                    return i8;
                }
                return -1;
            }
            byte b6 = bArr[i7 + 1];
            if (!((b6 & 192) == 128)) {
                if (i9 == i) {
                    return i8;
                }
                return -1;
            }
            byte b7 = bArr[i7 + 2];
            if (!((b7 & 192) == 128)) {
                if (i9 == i) {
                    return i8;
                }
                return -1;
            }
            byte b8 = bArr[i15];
            if (!((b8 & 192) == 128)) {
                if (i9 == i) {
                    return i8;
                }
                return -1;
            }
            int i16 = (((b8 ^ ByteCompanionObject.MIN_VALUE) ^ (b7 << 6)) ^ (b6 << 12)) ^ (b2 << 18);
            if (i16 > 1114111) {
                if (i9 == i) {
                    return i8;
                }
                return -1;
            }
            if (55296 <= i16 && i16 <= 57343) {
                if (i9 == i) {
                    return i8;
                }
                return -1;
            }
            if (i16 < 65536) {
                if (i9 == i) {
                    return i8;
                }
                return -1;
            }
            i3 = i9 + 1;
            if (i9 == i) {
                return i8;
            }
            if (i16 != 10 && i16 != 13) {
                if (i16 >= 0 && i16 <= 31) {
                    z3 = true;
                } else {
                    if (127 <= i16 && i16 <= 159) {
                        z3 = true;
                    } else {
                        z3 = false;
                    }
                }
                if (!z3) {
                    if (i16 == 65533) {
                        if (i16 < 65536) {
                            i4 = 1;
                        } else {
                            i4 = 2;
                        }
                        i8 += i4;
                        Unit unit5 = Unit.INSTANCE;
                        i7 += 4;
                    }
                }
            } else if (i16 == 65533) {
                if (i16 < 65536) {
                    i4 = 1;
                } else {
                    i4 = 2;
                }
                i8 += i4;
                Unit unit6 = Unit.INSTANCE;
                i7 += 4;
            }
            return -1;
            i9 = i3;
        }
        return i8;
    }
}
