package cn.jiguang.ak;

import android.content.Context;
import android.text.TextUtils;
import android.util.Pair;
import cn.jiguang.ad.d;
import cn.jiguang.f.i;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
public final class a {
    private static volatile a a;
    private static final Object b = new Object();
    private long c = 36000000;
    private long d = 900000;
    private final Map<String, Pair<InetAddress[], Long>> e = new HashMap();

    private a() {
    }

    public static a a() {
        if (a == null) {
            synchronized (b) {
                if (a == null) {
                    a = new a();
                }
            }
        }
        return a;
    }

    private static InetAddress a(String str) {
        if (i.f(str) || i.g(str)) {
            try {
                return InetAddress.getByName(str);
            } catch (UnknownHostException e) {
                d.f("DNSLoader", "dns resolve failed:" + e);
            }
        }
        return null;
    }

    /* JADX WARN: Code duplicated, block: B:44:0x00b9  */
    public final InetAddress[] a(Context context, String str, long j, boolean z) {
        Pair pair;
        InetAddress[] inetAddressArr;
        InetAddress[] inetAddressArr2;
        StringBuilder sb;
        String string;
        InetAddress[] inetAddressArr3;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        InetAddress inetAddressA = a(str);
        if (inetAddressA != null) {
            return new InetAddress[]{inetAddressA};
        }
        long j2 = this.c;
        long j3 = this.d;
        Pair<InetAddress[], Long> pair2 = this.e.get(str);
        boolean z2 = pair2 == null || pair2.first == null;
        long jLongValue = ((Long) (z2 ? cn.jiguang.g.b.a(context, cn.jiguang.g.a.c(str)) : pair2.second)).longValue();
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (jCurrentTimeMillis > j2 + jLongValue) {
            pair = null;
        } else {
            if (z2) {
                String str2 = (String) cn.jiguang.g.b.a(context, cn.jiguang.g.a.b(str));
                if (TextUtils.isEmpty(str2)) {
                    inetAddressArr3 = null;
                } else {
                    String[] strArrSplit = str2.split(",");
                    LinkedList linkedList = new LinkedList();
                    for (String str3 : strArrSplit) {
                        InetAddress inetAddressA2 = a(str3);
                        if (inetAddressA2 != null) {
                            linkedList.add(inetAddressA2);
                        }
                    }
                    inetAddressArr3 = linkedList.isEmpty() ? null : (InetAddress[]) linkedList.toArray(new InetAddress[0]);
                }
                if (inetAddressArr3 != null) {
                    this.e.put(str, new Pair<>(inetAddressArr3, Long.valueOf(jLongValue)));
                }
            } else {
                inetAddressArr3 = (InetAddress[]) pair2.first;
            }
            if (inetAddressArr3 != null) {
                pair = new Pair(inetAddressArr3, Boolean.valueOf(jCurrentTimeMillis > jLongValue + j3));
            } else {
                pair = null;
            }
        }
        if (pair != null) {
            inetAddressArr = pair.first != null ? (InetAddress[]) pair.first : null;
            if (!((Boolean) pair.second).booleanValue()) {
                if (z) {
                    cn.jiguang.af.b.a(new FutureTask(new b(context, str, this)));
                }
                sb = new StringBuilder("use cache=");
                string = Arrays.toString(inetAddressArr);
            }
            d.b("DNSLoader", sb.append(string).toString());
            return inetAddressArr;
        }
        inetAddressArr = null;
        FutureTask futureTask = new FutureTask(new b(context, str, this));
        cn.jiguang.af.b.a(futureTask);
        if (j == 0) {
            sb = new StringBuilder("use cache=");
        } else {
            try {
                d.b("DNSLoader", "waiting dns for " + str);
                inetAddressArr2 = (InetAddress[]) futureTask.get(j, TimeUnit.MILLISECONDS);
            } catch (Throwable th) {
                d.f("DNSLoader", "run futureTask e:" + th);
                inetAddressArr2 = null;
            }
            if (inetAddressArr2 != null) {
                d.b("DNSLoader", "use resolved result=" + Arrays.toString(inetAddressArr2));
                return inetAddressArr2;
            }
            sb = new StringBuilder("use cache=");
        }
        string = Arrays.toString(inetAddressArr);
        d.b("DNSLoader", sb.append(string).toString());
        return inetAddressArr;
    }
}
