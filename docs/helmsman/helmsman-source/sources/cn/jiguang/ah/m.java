package cn.jiguang.ah;

import android.os.SystemClock;
import android.text.TextUtils;
import java.net.DatagramSocket;

/* JADX INFO: loaded from: classes.dex */
final class m implements Runnable {
    final /* synthetic */ h a;
    final /* synthetic */ l b;

    m(l lVar, h hVar) {
        this.b = lVar;
        this.a = hVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        DatagramSocket datagramSocket;
        Object th;
        StringBuilder sb;
        try {
            datagramSocket = new DatagramSocket();
            try {
                String str = (String) cn.jiguang.g.b.a(this.b.a, cn.jiguang.g.a.G());
                if (TextUtils.isEmpty(str)) {
                    cn.jiguang.ad.d.b("SisConnContext", "reportInfo is Empty, quit report");
                    try {
                        datagramSocket.close();
                        return;
                    } catch (Throwable th2) {
                        th = th2;
                        sb = new StringBuilder("sisReport ,close udpsocket error:");
                    }
                } else {
                    byte[] bArrA = c.a("DG", str);
                    cn.jiguang.ad.d.b("SisConnContext", "sis report data(" + bArrA.length + ") at " + this.a.c + ":" + this.a.b);
                    l lVar = this.b;
                    if (l.a(this.a.c, this.a.b, datagramSocket, bArrA)) {
                        cn.jiguang.ad.d.b("SisConnContext", "report succeed : " + str);
                        cn.jiguang.g.b.a(this.b.a, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{cn.jiguang.g.a.N().a(Long.valueOf(SystemClock.elapsedRealtime()))});
                        cn.jiguang.g.b.a(this.b.a, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{cn.jiguang.g.a.G().a((String) null)});
                    } else {
                        cn.jiguang.ad.d.f("SisConnContext", "report failed" + str);
                    }
                    try {
                        datagramSocket.close();
                        return;
                    } catch (Throwable th3) {
                        th = th3;
                        sb = new StringBuilder("sisReport ,close udpsocket error:");
                    }
                }
            } catch (Throwable th4) {
                th = th4;
                try {
                    cn.jiguang.ad.d.f("SisConnContext", "sisReport failed, error:" + th);
                    if (datagramSocket == null) {
                        return;
                    }
                    try {
                        return;
                    } catch (Throwable th5) {
                        th = th5;
                        sb = new StringBuilder("sisReport ,close udpsocket error:");
                    }
                } finally {
                    if (datagramSocket != null) {
                        try {
                            datagramSocket.close();
                        } catch (Throwable th6) {
                            cn.jiguang.ad.d.f("SisConnContext", "sisReport ,close udpsocket error:" + th6.getMessage());
                        }
                    }
                }
            }
        } catch (Throwable th7) {
            datagramSocket = null;
            th = th7;
        }
        StringBuilder sbAppend = sb.append(th.getMessage());
    }
}
