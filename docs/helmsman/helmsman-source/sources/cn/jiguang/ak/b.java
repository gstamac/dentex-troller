package cn.jiguang.ak;

import android.content.Context;
import android.util.Pair;
import cn.jiguang.ad.d;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Callable;

/* JADX INFO: loaded from: classes.dex */
final class b implements Callable<InetAddress[]> {
    private Context a;
    private String b;
    private a c;

    b(Context context, String str, a aVar) {
        this.a = context;
        this.b = str;
        this.c = aVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:25:0x004e  */
    @Override // java.util.concurrent.Callable
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public InetAddress[] call() {
        InetAddress[] allByName;
        boolean z;
        try {
            allByName = InetAddress.getAllByName(this.b);
        } catch (UnknownHostException e) {
            d.f("DNSLoader", "dns resolve failed:" + e);
            allByName = null;
        }
        if (allByName == null || allByName.length <= 0) {
            return allByName;
        }
        ArrayList arrayList = new ArrayList();
        StringBuilder sb = new StringBuilder();
        int i = 0;
        int i2 = 0;
        for (InetAddress inetAddress : allByName) {
            if (i >= 3 || !(inetAddress instanceof Inet4Address)) {
                if (i2 >= 3 || !(inetAddress instanceof Inet6Address)) {
                    z = false;
                } else {
                    i2++;
                }
                if (z) {
                    arrayList.add(inetAddress);
                    sb.append(inetAddress.getHostAddress()).append(",");
                }
                if (i != 3 && i2 == 3) {
                    break;
                }
            } else {
                i++;
            }
            z = true;
            if (z) {
                arrayList.add(inetAddress);
                sb.append(inetAddress.getHostAddress()).append(",");
            }
            if (i != 3) {
            }
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        InetAddress[] inetAddressArr = (InetAddress[]) arrayList.toArray(new InetAddress[0]);
        long jCurrentTimeMillis = System.currentTimeMillis();
        d.b("DNSLoader", "update dns cache url=" + this.b + " resolved=" + Arrays.toString(inetAddressArr));
        this.c.e.put(this.b, new Pair(inetAddressArr, Long.valueOf(jCurrentTimeMillis)));
        try {
            sb.deleteCharAt(sb.length() - 1);
            cn.jiguang.g.b.a(this.a, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{cn.jiguang.g.a.b(this.b).a(sb.toString()), cn.jiguang.g.a.c(this.b).a(Long.valueOf(jCurrentTimeMillis))});
            return inetAddressArr;
        } catch (Throwable unused) {
            return inetAddressArr;
        }
    }
}
