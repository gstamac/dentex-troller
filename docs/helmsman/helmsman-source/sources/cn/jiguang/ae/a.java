package cn.jiguang.ae;

import android.content.Context;
import android.text.TextUtils;
import cn.jiguang.internal.JConstants;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public final class a {
    private static volatile a c;
    private static final Object d = new Object();
    private long a;
    private Map<String, Set<String>> b = new HashMap();

    private a() {
    }

    public static a a() {
        if (c == null) {
            synchronized (d) {
                if (c == null) {
                    c = new a();
                }
            }
        }
        return c;
    }

    private String a(String str) {
        if (TextUtils.isEmpty(str)) {
            return "normal";
        }
        str.hashCode();
        switch (str) {
            case "active_launch":
            case "active_user":
            case "active_terminate":
                return "active_user";
            case "account":
            case "detach_account":
            case "identify_account":
                return "account";
            case "android_awake_target2":
            case "android_awake":
            case "android_awake2":
            case "awake":
            case "android_awake_target":
                return "awake";
            case "normal":
                break;
            default:
                if (this.b.containsKey(str)) {
                    return str;
                }
                break;
        }
        return "normal";
    }

    private void a(JSONObject jSONObject) {
        if (jSONObject == null || jSONObject.length() == 0) {
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
                        linkedHashSet.add(jSONArrayOptJSONArray.getString(i));
                    }
                }
                map.put(next, linkedHashSet);
            }
            if (map.isEmpty()) {
                return;
            }
            this.b = map;
        } catch (JSONException unused) {
        }
    }

    public final a a(Context context) {
        try {
            long jLongValue = ((Long) cn.jiguang.g.b.a(context, cn.jiguang.g.a.u())).longValue();
            long j = this.a;
            if (j == 0 || j != jLongValue) {
                this.a = jLongValue;
                String str = (String) cn.jiguang.g.b.a(context, cn.jiguang.g.a.s());
                if (!TextUtils.isEmpty(str)) {
                    a(new JSONObject(str));
                }
            }
        } catch (Throwable unused) {
        }
        return this;
    }

    public final String a(Set<String> set) {
        if (set != null) {
            try {
                if (!set.isEmpty()) {
                    Iterator<String> it = set.iterator();
                    String str = null;
                    while (it.hasNext()) {
                        String strA = a(it.next());
                        if (str == null) {
                            str = strA;
                        } else if (!str.equals(strA)) {
                            cn.jiguang.ad.d.f("AddressGroupManager", "Report JSONArray belong more than one space, using normal-space");
                            return "normal";
                        }
                    }
                    return str;
                }
            } catch (Throwable unused) {
            }
        }
        return "normal";
    }

    public final void a(Context context, JSONObject jSONObject) {
        JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray("sis_ips");
        long j = JConstants.HOUR;
        try {
            long j2 = jSONObject.getLong("ttl");
            if (j2 >= 0) {
                j = j2;
            }
        } catch (JSONException unused) {
        }
        JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("ips");
        cn.jiguang.g.a[] aVarArr = new cn.jiguang.g.a[4];
        aVarArr[0] = cn.jiguang.g.a.t().a(jSONArrayOptJSONArray.toString());
        aVarArr[1] = cn.jiguang.g.a.v().a(Long.valueOf(j * 1000));
        aVarArr[2] = cn.jiguang.g.a.u().a(Long.valueOf(System.currentTimeMillis()));
        aVarArr[3] = cn.jiguang.g.a.s().a(jSONObjectOptJSONObject != null ? jSONObjectOptJSONObject.toString() : "");
        cn.jiguang.g.b.a(context, (cn.jiguang.g.a<?>[]) aVarArr);
        a(jSONObjectOptJSONObject);
    }

    public final a b(Context context) {
        try {
            long jCurrentTimeMillis = System.currentTimeMillis();
            a(context);
            long jLongValue = ((Long) cn.jiguang.g.b.a(context, cn.jiguang.g.a.v())).longValue();
            if (jLongValue < 0) {
                jLongValue = JConstants.HOUR;
            } else if (jLongValue < JConstants.MIN) {
                jLongValue = 60000;
            }
            if (jLongValue > 604800000) {
                jLongValue = 604800000;
            }
            SimpleDateFormat simpleDateFormatA = cn.jiguang.f.c.a("yyyy-MM-dd HH:mm:ss");
            cn.jiguang.ad.d.b("AddressGroupManager", "lastUpdateTime=" + simpleDateFormatA.format(new Date(this.a)) + " now=" + simpleDateFormatA.format(new Date(jCurrentTimeMillis)) + " expire=" + (jLongValue / 1000));
            long j = this.a;
            if (j == 0 || j + jLongValue < jCurrentTimeMillis) {
                cn.jiguang.ad.d.b("AddressGroupManager", "cache invalid, fetch new urls");
                Map<String, Set<String>> map = this.b;
                i.a(context, map == null || map.isEmpty());
            }
        } catch (Throwable th) {
            cn.jiguang.ad.d.f("AddressGroupManager", "refresh e" + th);
        }
        return this;
    }

    public final Set<String> b(Set<String> set) {
        if (set != null && !set.isEmpty()) {
            Iterator<String> it = set.iterator();
            Set<String> set2 = null;
            while (it.hasNext()) {
                Set<String> set3 = this.b.get(a(it.next()));
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
        return this.b.get("normal");
    }
}
