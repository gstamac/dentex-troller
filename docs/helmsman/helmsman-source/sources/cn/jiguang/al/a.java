package cn.jiguang.al;

import android.content.Context;
import android.text.TextUtils;
import android.util.Pair;
import com.qiniu.android.common.Constants;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes.dex */
public final class a {
    public static Pair<c, ByteBuffer> a(Context context, byte[] bArr, String str) {
        ByteBuffer byteBufferWrap;
        if (bArr.length < 20) {
            cn.jiguang.ad.d.g("JCommands", "Error: received body-length short than common head-length, return null");
            return null;
        }
        byte[] bArr2 = new byte[20];
        System.arraycopy(bArr, 0, bArr2, 0, 20);
        c cVar = new c(false, bArr2);
        cn.jiguang.ad.d.b("JCommands", "parsed head - " + cVar.toString());
        int i = cVar.a - 20;
        if (i < 0) {
            cn.jiguang.ad.d.g("JCommands", "Error: totalBytes length error with no encrypted, return null");
            return null;
        }
        cn.jiguang.ad.d.a("JCommands", "body size:" + i);
        if (TextUtils.isEmpty(str)) {
            str = b.a(context);
        }
        byte[] bArr3 = new byte[i];
        System.arraycopy(bArr, 20, bArr3, 0, i);
        cn.jiguang.ad.d.b("JCommands", "decode algorithm:" + ((int) cVar.d) + ", pwd:" + str);
        if (TextUtils.isEmpty(str)) {
            System.arraycopy(bArr, 20, bArr3, 0, i);
            byteBufferWrap = ByteBuffer.wrap(bArr3);
        } else {
            try {
                byteBufferWrap = ByteBuffer.wrap(cVar.d == 2 ? new cn.jiguang.ar.c().b(bArr3, cn.jiguang.ar.c.a(str), str.substring(0, 16).getBytes(Constants.UTF_8)) : cn.jiguang.ar.d.a(bArr3, str, str.substring(0, 16), false));
            } catch (Exception e) {
                cn.jiguang.ad.d.b("JCommands", "decryptBytes error:" + e.getMessage());
                return null;
            }
        }
        return new Pair<>(cVar, byteBufferWrap);
    }

    public static boolean a(Context context, byte[] bArr) {
        try {
            Pair<c, ByteBuffer> pairA = a(context, bArr, "");
            if (pairA == null) {
                return false;
            }
            long jD = cn.jiguang.d.a.d(context);
            long j = ((c) pairA.first).g;
            cn.jiguang.ad.d.b("JCommands", "uid:" + jD + ",msgUid:" + j + ",cmd:" + ((c) pairA.first).c);
            if (jD == 0 || j == 0 || jD == j) {
                cn.jiguang.aj.b.a();
                cn.jiguang.aj.b.a(context, (c) pairA.first, (ByteBuffer) pairA.second);
                return true;
            }
            cn.jiguang.ad.d.b("JCommands", "recv other app msg");
            cn.jiguang.e.a.a().a(context, j, bArr);
            return true;
        } catch (Throwable th) {
            cn.jiguang.ad.d.g("JCommands", "dispatchMessage error:" + th.getMessage());
            return false;
        }
    }
}
