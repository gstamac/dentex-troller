package cn.jiguang.i;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import cn.jiguang.f.i;
import cn.jiguang.l.e;
import cn.jiguang.l.h;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class b extends cn.jiguang.l.a {
    private static volatile b c;
    private Context a;
    private JSONObject b;
    private String d;
    private boolean e;

    public static b a() {
        if (c == null) {
            synchronized (b.class) {
                c = new b();
            }
        }
        return c;
    }

    private void a(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, List<cn.jiguang.j.a> list) {
        try {
            if (this.b == null) {
                this.b = new JSONObject();
            }
            this.b.put("ssid", str);
            this.b.put("bssid", str2);
            this.b.put("local_ip", str3);
            this.b.put("local_mac", str4);
            this.b.put("netmask", str5);
            this.b.put("gateway", str8);
            this.b.put("dhcp", str9);
            JSONArray jSONArray = new JSONArray();
            if (!TextUtils.isEmpty(str6)) {
                jSONArray.put(str6);
            }
            if (!TextUtils.isEmpty(str7)) {
                jSONArray.put(str7);
            }
            this.b.put("dns", jSONArray);
            JSONArray jSONArray2 = new JSONArray();
            for (cn.jiguang.j.a aVar : list) {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("ip", aVar.a);
                jSONObject.put("mac", aVar.d);
                jSONArray2.put(jSONObject);
            }
            this.b.put("data", jSONArray2);
        } catch (JSONException e) {
            cn.jiguang.w.a.d("JArp", "packageJson exception: " + e.getMessage());
        }
    }

    @Override // cn.jiguang.l.a
    protected final String a(Context context) {
        this.a = context;
        return "JArp";
    }

    @Override // cn.jiguang.l.a
    protected final void a(JSONObject jSONObject) {
        this.e = true;
        JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("content");
        boolean z = !jSONObjectOptJSONObject.optBoolean("disable");
        e.a(this.a, "JArp", z);
        if (z) {
            long jOptLong = jSONObjectOptJSONObject.optLong("frequency", 0L) * 1000;
            if (jOptLong == 0) {
                e.a(this.a, true);
            } else {
                e.a(this.a, false);
                e.b(this.a, "JArp", jOptLong);
            }
        }
    }

    @Override // cn.jiguang.l.a
    protected final boolean a(Context context, String str) {
        return true;
    }

    @Override // cn.jiguang.l.a
    protected final void b(Context context, String str) {
        String bssid;
        String strB;
        if (!cn.jiguang.f.a.c(context, "android.permission.ACCESS_WIFI_STATE")) {
            cn.jiguang.w.a.d("JArp", "collect arp failed because has no permission Manifest.permission.ACCESS_WIFI_STATE");
            return;
        }
        if (!cn.jiguang.f.a.j(context).toUpperCase().startsWith("WIFI")) {
            cn.jiguang.w.a.d("JArp", "collect arp failed because networkType is not wifi");
            return;
        }
        if (TextUtils.isEmpty(cn.jiguang.d.a.b(context))) {
            cn.jiguang.w.a.d("JArp", "collect arp failed because can't get registerId");
            return;
        }
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService("wifi");
        if (wifiManager == null) {
            cn.jiguang.w.a.a("JArp", "collect arp failed because get wifiManager failed");
            return;
        }
        WifiInfo connectionInfo = wifiManager.getConnectionInfo();
        if (connectionInfo != null) {
            strB = i.b(connectionInfo.getSSID());
            bssid = connectionInfo.getBSSID();
        } else {
            bssid = "";
            strB = bssid;
        }
        if (TextUtils.isEmpty(bssid)) {
            bssid = "";
        }
        if (TextUtils.isEmpty(strB)) {
            strB = "";
        }
        this.d = TextUtils.isEmpty(bssid) ? strB : bssid;
        if (e.a(context) && e.m(context, this.d)) {
            cn.jiguang.w.a.d("JArp", "collect arp failed because this wifi 【" + this.d + "】 can't report twice");
            return;
        }
        boolean zC = c(context, "JArp");
        if (!this.e && !zC) {
            cn.jiguang.w.a.d("JArp", "collect arp failed because this wifi 【" + this.d + "】 is not in report time");
            return;
        }
        this.e = false;
        DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
        if (dhcpInfo == null) {
            return;
        }
        long j = dhcpInfo.ipAddress;
        byte[] bArr = {(byte) (j & 255), (byte) ((j >> 8) & 255), (byte) ((j >> 16) & 255), (byte) ((j >> 24) & 255)};
        String strA = cn.jiguang.k.a.a(dhcpInfo.ipAddress);
        String str2 = TextUtils.equals(strA, "0.0.0.0") ? "" : strA;
        String strC = cn.jiguang.f.a.c("");
        String strA2 = cn.jiguang.k.a.a(dhcpInfo.netmask);
        String str3 = TextUtils.equals(strA2, "0.0.0.0") ? "" : strA2;
        String strA3 = cn.jiguang.k.a.a(dhcpInfo.dns1);
        String str4 = TextUtils.equals(strA3, "0.0.0.0") ? "" : strA3;
        String strA4 = cn.jiguang.k.a.a(dhcpInfo.dns2);
        String str5 = TextUtils.equals(strA4, "0.0.0.0") ? "" : strA4;
        String strA5 = cn.jiguang.k.a.a(dhcpInfo.gateway);
        String str6 = TextUtils.equals(strA5, "0.0.0.0") ? "" : strA5;
        String strA6 = cn.jiguang.k.a.a(dhcpInfo.serverAddress);
        String str7 = TextUtils.equals(strA6, "0.0.0.0") ? "" : strA6;
        cn.jiguang.k.a.a(str7, bArr);
        List<cn.jiguang.j.a> listA = cn.jiguang.k.a.a(str7);
        if (listA == null || listA.isEmpty()) {
            cn.jiguang.w.a.d("JArp", "collect arp failed because can't get arp info");
        } else {
            cn.jiguang.w.a.a("JArp", "collect arp success");
            a(strB, bssid, str2, strC, str3, str4, str5, str6, str7, listA);
        }
    }

    @Override // cn.jiguang.l.a
    protected final boolean b() {
        return e.h(this.a, "JArp");
    }

    @Override // cn.jiguang.l.a
    protected final boolean c(Context context, String str) {
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (TextUtils.isEmpty(this.d)) {
            this.d = "";
        }
        return jCurrentTimeMillis - e.f(context, new StringBuilder("JArp").append(this.d).toString()) > e.g(context, "JArp");
    }

    @Override // cn.jiguang.l.a
    protected final void d(Context context, String str) {
        JSONObject jSONObject = this.b;
        if (jSONObject == null) {
            cn.jiguang.w.a.a("JArp", "there are no data to report");
            return;
        }
        cn.jiguang.d.a.a(context, jSONObject, "mac_list");
        h.a(context, this.b, new a(this.d, context, "JArp", str));
        this.b = null;
    }
}
