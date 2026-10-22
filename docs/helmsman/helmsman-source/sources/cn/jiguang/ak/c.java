package cn.jiguang.ak;

import android.content.Context;
import android.text.TextUtils;
import cn.jiguang.ad.d;
import cn.jiguang.f.i;
import cn.jiguang.f.j;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class c {
    private static int a;
    private static c b;
    private final Map<String, Integer> c = new HashMap();

    private c() {
    }

    private static int a(boolean z) {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            boolean z2 = false;
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterfaceNextElement = networkInterfaces.nextElement();
                if (!i.a(networkInterfaceNextElement.getName(), "dummy")) {
                    boolean zA = i.a(networkInterfaceNextElement.getName(), "wlan");
                    if (!z || zA) {
                        Enumeration<InetAddress> inetAddresses = networkInterfaceNextElement.getInetAddresses();
                        while (inetAddresses.hasMoreElements()) {
                            InetAddress inetAddressNextElement = inetAddresses.nextElement();
                            if (!inetAddressNextElement.isLoopbackAddress() && a(inetAddressNextElement)) {
                                if (zA) {
                                    return 3;
                                }
                                z2 = true;
                            }
                        }
                    }
                }
            }
            return z2 ? 0 : 1;
        } catch (Exception unused) {
            d.f("IpvxHelper", "checkIpvxSupport:");
            return 0;
        }
    }

    public static c a() {
        if (b == null) {
            synchronized (c.class) {
                if (b == null) {
                    b = new c();
                }
            }
        }
        return b;
    }

    public static void a(int i) {
        if (i > 3 || i < 0) {
            return;
        }
        a = i;
    }

    private static boolean a(InetAddress inetAddress) {
        try {
            return (inetAddress instanceof Inet6Address) && !inetAddress.getHostAddress().substring(0, 4).equalsIgnoreCase("fe80");
        } catch (Throwable unused) {
        }
    }

    public static int b(int i) {
        int i2 = a;
        if (i2 == 2 || i2 == 1) {
            return i2;
        }
        if (i == 1) {
            return 1;
        }
        if (i != 2) {
            return i2;
        }
        return 2;
    }

    public final int a(Context context) {
        Integer num;
        int iA;
        int i = 0;
        try {
            String strC = j.c(context);
            String strK = "";
            boolean zEquals = "wifi".equals(strC);
            if (zEquals && cn.jiguang.d.a.i(context)) {
                strK = cn.jiguang.f.a.k(context);
            }
            boolean z = !TextUtils.isEmpty(strK);
            if ((!zEquals || z) && (num = this.c.get(strC + strK)) != null && num.intValue() != 0) {
                d.b("IpvxHelper", "net=" + strC + " " + strK + " get cache support=" + num);
                return num.intValue();
            }
            if (z) {
                iA = ((Integer) cn.jiguang.g.b.a(context, cn.jiguang.g.a.g(strK))).intValue();
                try {
                    d.b("IpvxHelper", "net=" + strC + " " + strK + " get wifi history support=" + iA);
                } catch (Throwable th) {
                    th = th;
                    i = iA;
                    d.f("IpvxHelper", "getPreferVx e:" + th);
                }
            } else {
                iA = 0;
            }
            if (iA == 0) {
                iA = a(zEquals);
                d.b("IpvxHelper", "net=" + strC + " " + strK + " get networkinterface support=" + iA);
                if (z) {
                    cn.jiguang.g.b.a(context, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{cn.jiguang.g.a.g(strK).a(Integer.valueOf(iA))});
                }
            }
            i = iA;
            if (!zEquals || z) {
                this.c.put(strC + strK, Integer.valueOf(i));
            }
            return i;
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public final void a(Context context, int i) {
        String strC = j.c(context);
        boolean zEquals = "wifi".equals(strC);
        String strK = (zEquals && cn.jiguang.d.a.i(context)) ? cn.jiguang.f.a.k(context) : "";
        boolean z = !TextUtils.isEmpty(strK);
        if (!zEquals || z) {
            this.c.put(strC + strK, Integer.valueOf(i));
        }
        if (z) {
            cn.jiguang.g.b.a(context, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{cn.jiguang.g.a.g(strK).a(Integer.valueOf(i))});
        }
    }
}
