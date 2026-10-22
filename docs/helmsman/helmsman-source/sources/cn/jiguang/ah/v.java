package cn.jiguang.ah;

import android.content.Context;

/* JADX INFO: loaded from: classes.dex */
final class v implements Runnable {
    private byte[] a;
    private Context b;

    v(Context context, byte[] bArr) {
        this.b = context;
        this.a = bArr;
    }

    @Override // java.lang.Runnable
    public final void run() {
        try {
            cn.jiguang.aj.i.a().a(this.b, "tcp_a22", null);
            cn.jiguang.al.a.a(this.b, this.a);
        } catch (Throwable th) {
            cn.jiguang.ad.d.f("TcpRecvAction", "TcpRecvAction failed:" + th.getMessage());
        }
    }
}
