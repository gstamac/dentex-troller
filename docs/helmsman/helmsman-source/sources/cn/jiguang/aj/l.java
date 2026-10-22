package cn.jiguang.aj;

import android.content.Context;
import android.text.TextUtils;
import android.util.Pair;
import cn.jiguang.api.ReportCallBack;
import java.io.Closeable;
import java.io.File;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class l {
    private static l a;
    private static int e;
    private final AtomicBoolean b = new AtomicBoolean(false);
    private final Map<cn.jiguang.ah.h, Pair<Integer, cn.jiguang.am.a>> c = new ConcurrentHashMap();
    private final Map<cn.jiguang.ah.h, ConcurrentHashMap<Integer, n>> d = new ConcurrentHashMap();
    private Map<String, Set<cn.jiguang.ah.h>> f = new HashMap();

    private l() {
    }

    private static synchronized int a(Context context) {
        Integer numValueOf;
        Integer numValueOf2 = (Integer) cn.jiguang.g.b.b(context, cn.jiguang.g.a.I());
        if (numValueOf2 == null) {
            numValueOf2 = Integer.valueOf(Math.abs(new SecureRandom().nextInt(10000)));
        }
        numValueOf = Integer.valueOf((numValueOf2.intValue() + 1) % 10000);
        cn.jiguang.g.b.a(context, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{cn.jiguang.g.a.I().a(numValueOf)});
        return numValueOf.intValue();
    }

    public static l a() {
        if (a == null) {
            synchronized (l.class) {
                if (a == null) {
                    a = new l();
                }
            }
        }
        return a;
    }

    public static n a(Context context, JSONObject jSONObject, byte[] bArr, int i, File file, Set<String> set, ReportCallBack reportCallBack) {
        n nVar = new n();
        nVar.b = jSONObject;
        nVar.a = i;
        nVar.c = bArr;
        nVar.d = file;
        nVar.e = set;
        nVar.f = a(context);
        nVar.j = reportCallBack;
        return nVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public n a(cn.jiguang.ah.h hVar, int i) {
        ConcurrentHashMap<Integer, n> concurrentHashMap;
        if (hVar == null || (concurrentHashMap = this.d.get(hVar)) == null) {
            return null;
        }
        n nVar = concurrentHashMap.get(Integer.valueOf(i));
        concurrentHashMap.remove(Integer.valueOf(i));
        return nVar;
    }

    private Set<cn.jiguang.ah.h> a(Context context, Set<String> set) {
        Map<String, Set<cn.jiguang.ah.h>> map = this.f;
        if (map == null || map.isEmpty()) {
            String str = (String) cn.jiguang.g.b.a(context, cn.jiguang.g.a.R());
            if (!TextUtils.isEmpty(str)) {
                try {
                    a(new JSONObject(str));
                } catch (JSONException unused) {
                }
            }
        }
        if (set != null && !set.isEmpty()) {
            Set<cn.jiguang.ah.h> set2 = null;
            for (String str2 : set) {
                if (!TextUtils.isEmpty(str2)) {
                    str2.hashCode();
                    switch (str2) {
                        case "active_launch":
                        case "active_user":
                        case "active_terminate":
                            str2 = "active_user";
                            break;
                        case "account":
                        case "detach_account":
                        case "identify_account":
                            str2 = "account";
                            break;
                        case "android_awake_target2":
                        case "android_awake":
                        case "android_awake2":
                        case "awake":
                        case "android_awake_target":
                            str2 = "awake";
                            break;
                        case "normal":
                            str2 = "normal";
                            break;
                        default:
                            if (!this.f.containsKey(str2)) {
                                str2 = "normal";
                                break;
                            }
                            break;
                    }
                } else {
                    str2 = "normal";
                }
                Set<cn.jiguang.ah.h> set3 = this.f.get(str2);
                if (set3 != null && !set3.isEmpty()) {
                    if (set2 == null) {
                        set2 = set3;
                    } else {
                        set2.retainAll(set3);
                    }
                    if (set2.isEmpty()) {
                    }
                }
            }
            return set2;
        }
        return this.f.get("normal");
    }

    private void a(Context context, int i, cn.jiguang.am.a aVar, cn.jiguang.ah.h hVar, n nVar) {
        byte[] bArrB;
        byte[] bArr = nVar.c;
        int i2 = nVar.a;
        int i3 = nVar.f;
        if (bArr == null || bArr.length == 0 || bArr.length > 30683) {
            bArrB = null;
        } else {
            cn.jiguang.ao.b bVar = new cn.jiguang.ao.b(bArr.length + 37);
            bVar.b(0);
            bVar.a(1);
            b.a();
            bVar.a(b.b());
            long jLongValue = ((Long) cn.jiguang.g.b.a(context, cn.jiguang.g.a.c())).longValue();
            bVar.b(jLongValue);
            byte[] bArrC = cn.jiguang.f.i.c(jLongValue + cn.jiguang.f.i.e((String) cn.jiguang.g.b.a(context, cn.jiguang.g.a.e())) + cn.jiguang.f.i.a(bArr));
            if (bArrC == null) {
                bArrC = new byte[16];
            } else if (bArrC.length != 16) {
                byte[] bArr2 = new byte[16];
                System.arraycopy(bArr, 0, bArr2, 0, Math.min(bArrC.length, 16));
                bArrC = bArr2;
            }
            bVar.a(bArrC);
            bVar.a(i2);
            bVar.b(i3);
            bVar.a(bArr);
            bVar.b(bVar.a(), 0);
            bArrB = bVar.b();
        }
        if (bArrB == null || bArrB.length == 0) {
            cn.jiguang.ad.d.b("TcpReporter", "package data failed, give up, data=" + nVar);
            a(context, nVar, -1);
            return;
        }
        cn.jiguang.ad.d.b("TcpReporter", "send tcp data, len=" + bArrB.length + ", data=" + nVar);
        nVar.i = true;
        aVar.a(bArrB);
        cn.jiguang.an.b.a().b(60000 + i, 31000L, new m(context, hVar));
        cn.jiguang.an.b.a().b(nVar.f + 50000, 15000L, new m(context, nVar));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(Context context, cn.jiguang.ah.h hVar, int i) {
        int iIntValue;
        StringBuilder sb;
        StringBuilder sbAppend;
        Pair<Integer, cn.jiguang.am.a> pairRemove = this.c.remove(hVar);
        if (pairRemove != null) {
            iIntValue = ((Integer) pairRemove.first).intValue();
            cn.jiguang.f.k.a((Closeable) pairRemove.second);
        } else {
            iIntValue = 0;
        }
        ConcurrentHashMap<Integer, n> concurrentHashMapRemove = this.d.remove(hVar);
        if (i == 0 && !cn.jiguang.f.a.d(context)) {
            i = -2;
        }
        if (concurrentHashMapRemove == null || concurrentHashMapRemove.size() <= 0) {
            if (iIntValue > 0) {
                cn.jiguang.ad.d.b("TcpReporter", "socket(" + iIntValue + ") at " + hVar + " is disconnected, no task left");
                return;
            }
            return;
        }
        if (i == 0) {
            sb = new StringBuilder("socket(");
            sbAppend = sb.append(iIntValue).append(") at ").append(hVar).append(" is disconnected, go on send waiting request");
        } else {
            sb = new StringBuilder("socket(");
            sbAppend = sb.append(iIntValue).append(") at ").append(hVar).append(" is disconnected, finish waiting request, code=").append(i);
        }
        cn.jiguang.ad.d.b("TcpReporter", sbAppend.toString());
        for (n nVar : concurrentHashMapRemove.values()) {
            if (nVar != null) {
                cn.jiguang.an.b.a().b(nVar.f + 50000);
                if (i == 0) {
                    a(context, nVar);
                } else {
                    m mVar = new m(context, 3, nVar);
                    mVar.f = i;
                    cn.jiguang.af.b.a(mVar);
                }
            }
        }
    }

    private synchronized void a(Context context, cn.jiguang.ah.h hVar, n nVar) {
        Pair<Integer, cn.jiguang.am.a> pair = this.c.get(hVar);
        cn.jiguang.am.a aVar = pair != null ? (cn.jiguang.am.a) pair.second : null;
        if (aVar == null) {
            this.c.put(hVar, new Pair<>(Integer.valueOf(b()), new cn.jiguang.am.b(30720, 19)));
            cn.jiguang.af.b.a("TCP_REPORT", new m(context, 1, hVar));
        } else {
            if (aVar.a()) {
                a(context, ((Integer) pair.first).intValue(), aVar, hVar, nVar);
            }
        }
    }

    private void a(Context context, n nVar, int i) {
        ConcurrentHashMap<Integer, n> concurrentHashMap;
        if (nVar.g != null && (concurrentHashMap = this.d.get(nVar.g)) != null) {
            concurrentHashMap.remove(Integer.valueOf(nVar.f));
        }
        cn.jiguang.an.b.a().b(nVar.f + 50000);
        m mVar = new m(context, 3, nVar);
        mVar.f = i;
        cn.jiguang.af.b.a(mVar);
    }

    static /* synthetic */ void a(l lVar, Context context, cn.jiguang.ah.h hVar) {
        cn.jiguang.am.a aVar;
        if (hVar == null) {
            return;
        }
        try {
            Pair<Integer, cn.jiguang.am.a> pair = lVar.c.get(hVar);
            if (pair != null && (aVar = (cn.jiguang.am.a) pair.second) != null && !aVar.a()) {
                cn.jiguang.ad.d.b("TcpReporter", "start tcp socket(" + pair.first + "):" + hVar);
                if (aVar.a(hVar.a, hVar.b) != 0) {
                    lVar.a(context, hVar, 0);
                } else {
                    cn.jiguang.af.b.a(new m(context, 2, hVar));
                    m mVar = new m(context, hVar);
                    while (!lVar.b.get()) {
                        try {
                            ByteBuffer byteBufferA = aVar.a(0);
                            try {
                                cn.jiguang.ad.d.b("TcpReporter", "Received bytes - len:" + byteBufferA.array().length);
                                byte b = byteBufferA.get(2);
                                if (b == 1 || b == 0) {
                                    short s = byteBufferA.getShort(15);
                                    short s2 = byteBufferA.getShort(17);
                                    cn.jiguang.ad.d.b("TcpReporter", "onResult seqId=" + ((int) s) + " code=" + ((int) s2));
                                    cn.jiguang.an.b.a().b(50000 + s);
                                    n nVarA = lVar.a(hVar, s);
                                    if (nVarA != null) {
                                        if (s2 == 0) {
                                            m mVar2 = new m(context, 3, nVarA);
                                            mVar2.f = s2;
                                            cn.jiguang.af.b.a(mVar2);
                                        } else if (s2 == 401) {
                                            lVar.a(context, hVar, s2);
                                        } else {
                                            lVar.a(context, nVarA);
                                        }
                                    }
                                } else {
                                    cn.jiguang.ad.d.f("TcpReporter", "wrong version");
                                }
                            } catch (Throwable th) {
                                cn.jiguang.ad.d.f("TcpReporter", "tcp reporter onReceive err:" + th);
                            }
                            cn.jiguang.an.b.a().b(((Integer) pair.first).intValue() + 60000, 31000L, mVar);
                        } catch (f e2) {
                            if (e2.a != -997) {
                                cn.jiguang.ad.d.f("TcpReporter", "recv failed with error:" + e2);
                            }
                        }
                    }
                }
                lVar.a(context, hVar, 0);
                if (lVar.b.get()) {
                    cn.jiguang.ad.d.f("TcpReporter", "Break receiving by wantStop");
                } else {
                    cn.jiguang.ad.d.f("TcpReporter", "disconnected");
                }
            }
        } catch (Throwable th2) {
            cn.jiguang.ad.d.a("TcpReporter", "socket exception", th2);
        }
    }

    private void a(JSONObject jSONObject) {
        if (jSONObject.length() == 0) {
            return;
        }
        try {
            HashMap map = new HashMap();
            Iterator<String> itKeys = jSONObject.keys();
            while (itKeys.hasNext()) {
                String next = itKeys.next();
                JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray(next);
                LinkedHashSet linkedHashSet = new LinkedHashSet();
                if (jSONArrayOptJSONArray != null) {
                    for (int i = 0; i < jSONArrayOptJSONArray.length(); i++) {
                        cn.jiguang.ah.h hVarA = cn.jiguang.ah.h.a(jSONArrayOptJSONArray.getString(i));
                        if (hVarA != null && hVarA.a()) {
                            linkedHashSet.add(hVarA);
                        }
                    }
                }
                map.put(next, linkedHashSet);
            }
            if (map.isEmpty()) {
                return;
            }
            this.f = map;
        } catch (JSONException unused) {
        }
    }

    private static synchronized int b() {
        int i;
        i = (e + 1) % 10000;
        e = i;
        return i;
    }

    static /* synthetic */ void b(l lVar, Context context, cn.jiguang.ah.h hVar) {
        cn.jiguang.am.a aVar;
        cn.jiguang.ad.d.b("TcpReporter", "socket at " + hVar + " is connected, deal with waiting request");
        ConcurrentHashMap<Integer, n> concurrentHashMap = lVar.d.get(hVar);
        Pair<Integer, cn.jiguang.am.a> pair = lVar.c.get(hVar);
        if (pair != null) {
            if (concurrentHashMap != null && (aVar = (cn.jiguang.am.a) pair.second) != null && aVar.a()) {
                for (n nVar : concurrentHashMap.values()) {
                    if (nVar != null && !nVar.i) {
                        lVar.a(context, ((Integer) pair.first).intValue(), aVar, hVar, nVar);
                    }
                }
            }
            cn.jiguang.an.b.a().b(((Integer) pair.first).intValue() + 60000, 31000L, new m(context, hVar));
        }
    }

    public final void a(Context context, n nVar) {
        cn.jiguang.ah.h next;
        try {
            boolean z = nVar.h == null;
            if (z) {
                cn.jiguang.an.b.a().a(context);
                Set<String> set = nVar.e;
                LinkedHashSet<cn.jiguang.ah.h> linkedHashSet = new LinkedHashSet<>();
                cn.jiguang.ah.h hVarA = cn.jiguang.ah.h.a(cn.jiguang.ag.a.d());
                if (hVarA == null || !hVarA.a()) {
                    Set<cn.jiguang.ah.h> setA = a(context, set);
                    if (setA != null) {
                        for (cn.jiguang.ah.h hVar : setA) {
                            if (hVar != null && hVar.a()) {
                                linkedHashSet.add(hVar);
                            }
                        }
                    }
                } else {
                    linkedHashSet.add(hVarA);
                }
                cn.jiguang.ad.d.b("TcpReporter", "tcp report find urls=" + linkedHashSet);
                nVar.h = linkedHashSet;
                cn.jiguang.ad.d.b("TcpReporter", "tcp report begin=" + nVar);
            }
            if (!cn.jiguang.f.a.d(context)) {
                a(context, nVar, -2);
                return;
            }
            if (nVar.h != null && !nVar.h.isEmpty()) {
                Iterator<cn.jiguang.ah.h> it = this.c.keySet().iterator();
                while (true) {
                    if (!it.hasNext()) {
                        next = null;
                        break;
                    }
                    next = it.next();
                    if (nVar.h.contains(next)) {
                        cn.jiguang.ad.d.b("TcpReporter", "use exist ipPort=" + next);
                        break;
                    }
                }
                if (next == null) {
                    next = nVar.h.iterator().next();
                    cn.jiguang.ad.d.b("TcpReporter", "use next ipPort=" + next);
                }
                nVar.g = next;
                nVar.h.remove(next);
                if (z) {
                    cn.jiguang.an.b.a().b(nVar.f + 50000, 15000L, new m(context, nVar));
                }
                ConcurrentHashMap<Integer, n> concurrentHashMap = this.d.get(next);
                if (concurrentHashMap == null) {
                    concurrentHashMap = new ConcurrentHashMap<>();
                    this.d.put(next, concurrentHashMap);
                }
                nVar.i = false;
                concurrentHashMap.put(Integer.valueOf(nVar.f), nVar);
                a(context, next, nVar);
                return;
            }
            a(context, nVar, -1);
        } catch (Throwable th) {
            cn.jiguang.ad.d.f("TcpReporter", "tcp upload e:" + th);
        }
    }
}
