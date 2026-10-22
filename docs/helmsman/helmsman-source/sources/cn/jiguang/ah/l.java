package cn.jiguang.ah;

import android.content.Context;
import android.os.Bundle;
import cn.jiguang.internal.JConstants;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.json.JSONArray;

/* JADX INFO: loaded from: classes.dex */
public class l {
    private static l c;
    Context a;
    public final LinkedList<o> b;
    private int d = 0;
    private byte[] e;
    private int f;

    private l(Context context) {
        this.a = context;
        this.b = o.a((String) cn.jiguang.g.b.a(context, cn.jiguang.g.a.G()));
    }

    public static l a(Context context) {
        if (c == null) {
            synchronized (l.class) {
                if (c == null) {
                    c = new l(context);
                }
            }
        }
        return c;
    }

    private p a(int i) {
        double d;
        long j;
        String strA = cn.jiguang.ac.a.a(this.a);
        long jD = cn.jiguang.d.a.d(this.a);
        String strB = cn.jiguang.f.j.b(this.a);
        long jCurrentTimeMillis = System.currentTimeMillis();
        Object objA = cn.jiguang.aj.e.a(this.a, "get_loc_info", null);
        double d2 = 200.0d;
        if (objA instanceof Bundle) {
            try {
                Bundle bundle = (Bundle) objA;
                double d3 = bundle.getDouble("lat");
                try {
                    d2 = bundle.getDouble("lot");
                    j = bundle.getLong("time");
                    d2 = d3;
                    d = d2;
                } catch (Throwable unused) {
                    double d4 = d2;
                    d2 = d3;
                    d = d4;
                    j = jCurrentTimeMillis;
                }
            } catch (Throwable unused2) {
                d = 200.0d;
            }
        } else {
            j = jCurrentTimeMillis;
            d = 200.0d;
        }
        return new p(i, strA, cn.jiguang.a.a.b, jD, strB, d2, d, j);
    }

    private synchronized void a(o oVar) {
        this.b.add(oVar);
        cn.jiguang.ad.d.b("SisConnContext", "addSisReportInfo:" + oVar.a().toString());
        while (this.b.size() > 30) {
            this.b.removeFirst();
        }
        JSONArray jSONArray = new JSONArray();
        Iterator<o> it = this.b.iterator();
        while (it.hasNext()) {
            jSONArray.put(it.next().a());
        }
        cn.jiguang.g.b.a(this.a, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{cn.jiguang.g.a.G().a(jSONArray.toString())});
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean a(InetAddress inetAddress, int i, DatagramSocket datagramSocket, byte[] bArr) {
        int i2;
        try {
            byte[] bArrA = c.a(c.a(datagramSocket, new DatagramPacket(bArr, bArr.length, inetAddress, i)));
            if (bArrA == 0 || bArrA.length == 0) {
                throw new Exception("byte could not be empty");
            }
            if (bArrA.length == 1) {
                i2 = bArrA[0];
            } else {
                i2 = ((short) (bArrA[1] & 255)) | ((short) ((bArrA[0] & 255) << 8));
            }
            return ((short) i2) == 0;
        } catch (Throwable th) {
            cn.jiguang.ad.d.h("SisConnContext", "report failed : " + th);
            return false;
        }
    }

    public final int a() {
        if (this.d == 0) {
            this.d = cn.jiguang.ak.c.a().a(this.a);
        }
        cn.jiguang.ak.c.a();
        int iB = cn.jiguang.ak.c.b(this.d);
        cn.jiguang.ad.d.b("SisConnContext", "ipvsupport=" + this.d + ", prefer=" + iB);
        return iB;
    }

    public final n a(long j) {
        FutureTask futureTask = new FutureTask(new j(this));
        this.d = 0;
        cn.jiguang.af.b.a(futureTask);
        try {
            return (n) futureTask.get(j, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException unused) {
            return null;
        }
    }

    public final void a(int i, String str, int i2, long j, long j2, int i3) {
        if (h.a(str, i2)) {
            o oVar = new o();
            oVar.a = cn.jiguang.ac.a.a(this.a);
            oVar.b = i;
            oVar.c = new h(str, i2);
            oVar.e = j;
            oVar.f = j2;
            oVar.k = i3;
            oVar.g = cn.jiguang.f.j.a(this.a);
            oVar.d = cn.jiguang.d.a.d(this.a);
            oVar.h = 200.0d;
            oVar.i = 200.0d;
            oVar.j = System.currentTimeMillis();
            Object objA = cn.jiguang.aj.e.a(this.a, "get_loc_info", null);
            if (objA instanceof Bundle) {
                try {
                    Bundle bundle = (Bundle) objA;
                    oVar.h = bundle.getDouble("lat");
                    oVar.i = bundle.getDouble("lot");
                    oVar.j = bundle.getLong("time");
                } catch (Throwable unused) {
                }
            }
            a(oVar);
        }
    }

    public final void a(h hVar) {
        if (((Boolean) cn.jiguang.g.b.a(this.a, cn.jiguang.g.a.L())).booleanValue()) {
            if (cn.jiguang.f.k.a(((Long) cn.jiguang.g.b.a(this.a, cn.jiguang.g.a.N())).longValue(), JConstants.HOUR)) {
                cn.jiguang.af.b.a(new m(this, hVar));
            } else {
                cn.jiguang.ad.d.b("SisConnContext", "sis report: not yet");
            }
        }
    }

    public final void a(boolean z) {
        this.d = (z ? 1 : 2) | this.d;
        cn.jiguang.ak.c.a().a(this.a, this.d);
    }

    public final byte[] a(Set<String> set) throws cn.jiguang.aj.f {
        int iA = cn.jiguang.f.j.a(this.a);
        if (this.e == null || iA != this.f) {
            this.f = iA;
            try {
                this.e = c.a("UG", a(iA).a(set).toString());
            } catch (Exception e) {
                throw new cn.jiguang.aj.f(2, "Failed to package data - " + e);
            }
        }
        return this.e;
    }
}
