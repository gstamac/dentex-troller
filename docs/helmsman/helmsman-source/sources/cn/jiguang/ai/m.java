package cn.jiguang.ai;

import android.content.Context;
import android.text.TextUtils;
import android.util.Pair;
import cn.jiguang.internal.JConstants;
import com.zhy.http.okhttp.OkHttpUtils;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
public final class m {
    private static volatile m a;
    private static final Object b = new Object();
    private long c = JConstants.DAY;
    private long d = 1800000;
    private final Map<String, Pair<LinkedHashSet<cn.jiguang.ah.h>, Long>> e = new HashMap();

    private m() {
    }

    public static m a() {
        if (a == null) {
            synchronized (b) {
                if (a == null) {
                    a = new m();
                }
            }
        }
        return a;
    }

    public static LinkedHashSet<cn.jiguang.ah.h> a(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            byte[] bArrB = f.a(j.a(h.a(h.a(str), h.a), 33, 1)).b(65535);
            cn.jiguang.ad.d.b("SRVLoader", "srv host:" + str);
            LinkedHashSet<cn.jiguang.ah.h> linkedHashSet = new LinkedHashSet<>();
            try {
                String[] strArrA = l.b().a();
                if (strArrA != null && strArrA.length != 0) {
                    LinkedHashSet<InetAddress> linkedHashSet2 = new LinkedHashSet();
                    cn.jiguang.ak.a aVarA = cn.jiguang.ak.a.a();
                    int length = strArrA.length;
                    int i = 0;
                    while (i < length) {
                        int i2 = i;
                        InetAddress[] inetAddressArrA = aVarA.a(null, strArrA[i], 3000L, false);
                        InetAddress inetAddress = (inetAddressArrA == null || inetAddressArrA.length <= 0) ? null : inetAddressArrA[0];
                        if (inetAddress != null) {
                            linkedHashSet2.add(inetAddress);
                        }
                        i = i2 + 1;
                    }
                    for (InetAddress inetAddress2 : linkedHashSet2) {
                        try {
                            f fVar = new f(q.a(null, new InetSocketAddress(inetAddress2, 53), bArrB, System.currentTimeMillis() + 1000));
                            j jVarA = fVar.a();
                            if (jVarA == null) {
                                break;
                            }
                            for (i iVar : fVar.a(1)) {
                                if (iVar.b().e() == jVarA.e() && iVar.b().d() == jVarA.c() && iVar.b().b().equals(jVarA.b())) {
                                    Iterator itA = iVar.a();
                                    while (itA.hasNext()) {
                                        o oVar = (o) itA.next();
                                        if (oVar.h() > 0) {
                                            String string = oVar.i().toString();
                                            if (!TextUtils.isEmpty(string)) {
                                                if (string.endsWith(".")) {
                                                    string = string.substring(0, string.length() - 1);
                                                }
                                                cn.jiguang.ah.h hVar = new cn.jiguang.ah.h(string, oVar.h());
                                                if (hVar.a()) {
                                                    linkedHashSet.add(hVar);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (IOException e) {
                            cn.jiguang.ad.d.f("SRVLoader", "tcp send to " + inetAddress2.getHostAddress() + " err:" + e);
                        }
                    }
                    return linkedHashSet;
                }
                return linkedHashSet;
            } catch (Throwable th) {
                cn.jiguang.ad.d.g("SRVLoader", "Get default ports error with Exception:" + th);
            }
        } catch (IOException e2) {
            cn.jiguang.ad.d.f("SRVLoader", "can't srv, create query:" + e2);
            return null;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    static /* synthetic */ void a(m mVar, String str, Pair pair) {
        if (pair.first == null || ((LinkedHashSet) pair.first).size() <= 0 || pair.second == null) {
            return;
        }
        mVar.e.put(str, pair);
        StringBuilder sb = new StringBuilder();
        Iterator it = ((LinkedHashSet) pair.first).iterator();
        while (it.hasNext()) {
            sb.append(((cn.jiguang.ah.h) it.next()).toString()).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        cn.jiguang.g.b.a((Context) null, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{cn.jiguang.g.a.d(str).a(sb.toString()), cn.jiguang.g.a.e(str).a(pair.second)});
    }

    /* JADX WARN: Code duplicated, block: B:39:0x00b0  */
    public final LinkedHashSet<cn.jiguang.ah.h> a(String str, long j) {
        Pair pair;
        LinkedHashSet<cn.jiguang.ah.h> linkedHashSet;
        LinkedHashSet<cn.jiguang.ah.h> linkedHashSet2;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        long j2 = this.c;
        long j3 = this.d;
        Pair<LinkedHashSet<cn.jiguang.ah.h>, Long> pair2 = this.e.get(str);
        boolean z = pair2 == null || pair2.first == null || ((LinkedHashSet) pair2.first).size() == 0;
        long jLongValue = ((Long) (z ? cn.jiguang.g.b.a((Context) null, cn.jiguang.g.a.e(str)) : pair2.second)).longValue();
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (jCurrentTimeMillis <= j2 + jLongValue) {
            LinkedHashSet linkedHashSet3 = new LinkedHashSet();
            if (z) {
                String str2 = (String) cn.jiguang.g.b.a((Context) null, cn.jiguang.g.a.d(str));
                if (!TextUtils.isEmpty(str2)) {
                    for (String str3 : str2.split(",")) {
                        cn.jiguang.ah.h hVarA = cn.jiguang.ah.h.a(str3);
                        if (hVarA != null && hVarA.a()) {
                            linkedHashSet3.add(hVarA);
                        }
                    }
                    this.e.put(str, new Pair<>(linkedHashSet3, Long.valueOf(System.currentTimeMillis())));
                }
            } else {
                linkedHashSet3 = (LinkedHashSet) pair2.first;
            }
            if (linkedHashSet3.isEmpty()) {
                pair = null;
            } else {
                pair = new Pair(linkedHashSet3, Boolean.valueOf(jCurrentTimeMillis > jLongValue + j3));
            }
        } else {
            pair = null;
        }
        if (pair != null) {
            linkedHashSet = (pair.first == null || ((LinkedHashSet) pair.first).size() <= 0) ? null : (LinkedHashSet) pair.first;
            if (!((Boolean) pair.second).booleanValue()) {
                return linkedHashSet;
            }
        } else {
            linkedHashSet = null;
        }
        FutureTask futureTask = new FutureTask(new n(str, this));
        cn.jiguang.af.b.a(futureTask);
        try {
            linkedHashSet2 = (LinkedHashSet) futureTask.get(OkHttpUtils.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        } catch (Throwable th) {
            cn.jiguang.ad.d.f("SRVLoader", "run futureTask e:" + th);
            linkedHashSet2 = null;
        }
        if (linkedHashSet2 == null || linkedHashSet2.size() <= 0) {
            cn.jiguang.ad.d.b("SRVLoader", "use cache=" + linkedHashSet);
            return linkedHashSet;
        }
        cn.jiguang.ad.d.b("SRVLoader", "use resolved result=" + linkedHashSet2);
        return linkedHashSet2;
    }
}
