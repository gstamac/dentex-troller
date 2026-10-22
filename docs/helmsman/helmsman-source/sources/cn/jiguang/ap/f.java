package cn.jiguang.ap;

/* JADX INFO: loaded from: classes.dex */
public final class f {
    public static String a(byte[] bArr) {
        if (bArr == null) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer(bArr.length * 2);
        for (byte b : bArr) {
            stringBuffer.append("0123456789ABCDEF".charAt((b >> 4) & 15)).append("0123456789ABCDEF".charAt(b & 15));
            stringBuffer.append(' ');
        }
        return stringBuffer.toString();
    }
}
