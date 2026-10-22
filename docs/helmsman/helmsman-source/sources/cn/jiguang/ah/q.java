package cn.jiguang.ah;

import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.Set;
import java.util.concurrent.Callable;

/* JADX INFO: loaded from: classes.dex */
public final class q implements Callable<n> {
    private final l a;
    private final r b;
    private final g c;
    private Set<String> d;
    private h e;

    public q(l lVar, h hVar, Set<String> set) {
        this.a = lVar;
        this.b = null;
        this.c = null;
        this.e = hVar;
        this.d = set;
    }

    public q(l lVar, r rVar, g gVar) {
        this.a = lVar;
        this.b = rVar;
        this.c = gVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @Override // java.util.concurrent.Callable
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public n call() {
        try {
            if (this.c == null) {
                return a(this.e);
            }
            return a(this.c.a(this.a.a()));
        } catch (Throwable th) {
            cn.jiguang.ad.d.f("SisTask", "run e:" + th);
            return null;
        }
    }

    private n a(h hVar) {
        DatagramSocket datagramSocket;
        if (hVar == null || hVar.c == null) {
            return null;
        }
        try {
            datagramSocket = new DatagramSocket();
            try {
                if (!TextUtils.isEmpty(cn.jiguang.ag.a.a)) {
                    hVar.c = InetAddress.getByName(cn.jiguang.ag.a.a);
                }
                if (cn.jiguang.ag.a.b > 0) {
                    hVar.b = cn.jiguang.ag.a.b;
                }
                cn.jiguang.ad.d.b("SisTask", "send sis:" + hVar.c + " port:" + hVar.b);
                Object[] objArrA = a(datagramSocket, hVar.c, hVar.b);
                Object obj = objArrA[0];
                if (!(obj instanceof n)) {
                    int iIntValue = ((Integer) obj).intValue();
                    long jLongValue = ((Long) objArrA[1]).longValue();
                    cn.jiguang.ad.d.f("SisTask", "sis failed(" + iIntValue + "):" + hVar.c + " port:" + hVar.b);
                    this.a.a(1, hVar.c.getHostAddress(), hVar.b, cn.jiguang.d.b.b(this.a.a), jLongValue, iIntValue);
                    return null;
                }
                boolean z = hVar.c instanceof Inet4Address;
                this.a.a(z);
                cn.jiguang.g.b.a(this.a.a, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{cn.jiguang.g.a.b(z).a(hVar.toString())});
                r rVar = this.b;
                if (rVar != null) {
                    rVar.a(objArrA[0]);
                }
                n nVar = (n) objArrA[0];
                try {
                    datagramSocket.close();
                } catch (Throwable unused) {
                }
                return nVar;
            } catch (Throwable th) {
                th = th;
                try {
                    cn.jiguang.ad.d.f("SisTask", "sis e:" + th);
                    return null;
                } finally {
                    if (datagramSocket != null) {
                        try {
                            datagramSocket.close();
                        } catch (Throwable unused2) {
                        }
                    }
                }
            }
        } catch (Throwable th2) {
            th = th2;
            datagramSocket = null;
        }
    }

    private Object[] a(DatagramSocket datagramSocket, InetAddress inetAddress, int i) {
        Object[] objArr = new Object[2];
        objArr[1] = 0;
        try {
            byte[] bArrA = this.a.a(this.d);
            DatagramPacket datagramPacket = new DatagramPacket(bArrA, bArrA.length, inetAddress, i);
            long jUptimeMillis = SystemClock.uptimeMillis();
            try {
                byte[] bArrA2 = c.a(datagramSocket, datagramPacket);
                objArr[1] = Long.valueOf(SystemClock.uptimeMillis() - jUptimeMillis);
                try {
                    n nVar = new n(new String(c.a(bArrA2)));
                    if (nVar.a()) {
                        objArr[0] = 6;
                        return objArr;
                    }
                    Context context = this.a.a;
                    if (!nVar.a()) {
                        String strA = h.a(nVar.a);
                        String strA2 = h.a(nVar.b);
                        String strC = cn.jiguang.f.j.c(context);
                        cn.jiguang.ad.d.b("SisTask", "updateSisInfo ips=" + strA + " sslIps=" + strA2 + " net=" + strC);
                        cn.jiguang.g.a[] aVarArr = new cn.jiguang.g.a[8];
                        aVarArr[0] = cn.jiguang.g.a.J().a(strA);
                        aVarArr[1] = cn.jiguang.g.a.K().a(strA2);
                        aVarArr[2] = cn.jiguang.g.a.d(false).a(h.a(nVar.c));
                        aVarArr[3] = cn.jiguang.g.a.d(true).a(h.a(nVar.d));
                        aVarArr[4] = cn.jiguang.g.a.O().a(h.a(nVar.e));
                        aVarArr[5] = cn.jiguang.g.a.R().a(nVar.f != null ? nVar.f.toString() : null);
                        aVarArr[6] = cn.jiguang.g.a.L().a(Boolean.valueOf(nVar.h));
                        aVarArr[7] = cn.jiguang.g.a.M().a(Long.valueOf(SystemClock.elapsedRealtime()));
                        cn.jiguang.g.b.a(context, (cn.jiguang.g.a<?>[]) aVarArr);
                        cn.jiguang.g.b.a(context, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{cn.jiguang.g.a.F().a(strC)});
                    }
                    nVar.g = new h(inetAddress, i);
                    objArr[0] = nVar;
                    return objArr;
                } catch (cn.jiguang.aj.f e) {
                    objArr[0] = Integer.valueOf(e.a);
                    return objArr;
                }
            } catch (Exception unused) {
                objArr[0] = 3;
                objArr[1] = Long.valueOf(SystemClock.uptimeMillis() - jUptimeMillis);
                return objArr;
            }
        } catch (cn.jiguang.aj.f e2) {
            objArr[0] = Integer.valueOf(e2.a);
            return objArr;
        }
    }
}
