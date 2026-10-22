package im.helmsman.lib.util;

/* JADX INFO: loaded from: classes2.dex */
public class MathUtil {
    public static byte[] double2Bytes(double d) {
        long jDoubleToRawLongBits = Double.doubleToRawLongBits(d);
        byte[] bArr = new byte[8];
        for (int i = 0; i < 8; i++) {
            bArr[i] = (byte) ((jDoubleToRawLongBits >> (i * 8)) & 255);
        }
        return bArr;
    }
}
