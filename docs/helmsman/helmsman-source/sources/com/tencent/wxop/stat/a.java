package com.tencent.wxop.stat;

import android.content.Context;
import android.content.IntentFilter;
import com.tencent.wxop.stat.common.StatConstants;
import com.tencent.wxop.stat.common.StatLogger;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;
import org.apache.http.HttpHost;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class a {
    private static a g;
    private com.tencent.wxop.stat.common.e e;
    private Context h;
    private StatLogger i;
    private List<String> a = null;
    private volatile int b = 2;
    private volatile String c = "";
    private volatile HttpHost d = null;
    private int f = 0;

    private a(Context context) {
        this.e = null;
        this.h = null;
        this.i = null;
        this.h = context.getApplicationContext();
        this.e = new com.tencent.wxop.stat.common.e();
        i.a(context);
        this.i = com.tencent.wxop.stat.common.k.b();
        l();
        i();
        g();
    }

    public static a a(Context context) {
        if (g == null) {
            synchronized (a.class) {
                if (g == null) {
                    g = new a(context);
                }
            }
        }
        return g;
    }

    private boolean b(String str) {
        return Pattern.compile("(2[5][0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})").matcher(str).matches();
    }

    private void i() {
        ArrayList arrayList = new ArrayList(10);
        this.a = arrayList;
        arrayList.add("117.135.169.101");
        this.a.add("140.207.54.125");
        this.a.add("180.153.8.53");
        this.a.add("120.198.203.175");
        this.a.add("14.17.43.18");
        this.a.add("163.177.71.186");
        this.a.add("111.30.131.31");
        this.a.add("123.126.121.167");
        this.a.add("123.151.152.111");
        this.a.add("113.142.45.79");
        this.a.add("123.138.162.90");
        this.a.add("103.7.30.94");
    }

    private String j() {
        try {
            return !b(StatConstants.MTA_SERVER_HOST) ? InetAddress.getByName(StatConstants.MTA_SERVER_HOST).getHostAddress() : "";
        } catch (Exception e) {
            this.i.e((Throwable) e);
            return "";
        }
    }

    private void k() {
        String strJ = j();
        if (StatConfig.isDebugEnable()) {
            this.i.i("remoteIp ip is " + strJ);
        }
        if (com.tencent.wxop.stat.common.k.c(strJ)) {
            if (!this.a.contains(strJ)) {
                String str = this.a.get(this.f);
                if (StatConfig.isDebugEnable()) {
                    this.i.w(strJ + " not in ip list, change to:" + str);
                }
                strJ = str;
            }
            StatConfig.setStatReportUrl("http://" + strJ + ":80/mstat/report");
        }
    }

    private void l() {
        this.b = 0;
        this.d = null;
        this.c = null;
    }

    public HttpHost a() {
        return this.d;
    }

    public void a(String str) {
        if (StatConfig.isDebugEnable()) {
            this.i.i("updateIpList " + str);
        }
        try {
            if (com.tencent.wxop.stat.common.k.c(str)) {
                JSONObject jSONObject = new JSONObject(str);
                if (jSONObject.length() > 0) {
                    Iterator<String> itKeys = jSONObject.keys();
                    while (itKeys.hasNext()) {
                        String string = jSONObject.getString(itKeys.next());
                        if (com.tencent.wxop.stat.common.k.c(string)) {
                            for (String str2 : string.split(";")) {
                                if (com.tencent.wxop.stat.common.k.c(str2)) {
                                    String[] strArrSplit = str2.split(":");
                                    if (strArrSplit.length > 1) {
                                        String str3 = strArrSplit[0];
                                        if (b(str3) && !this.a.contains(str3)) {
                                            if (StatConfig.isDebugEnable()) {
                                                this.i.i("add new ip:" + str3);
                                            }
                                            this.a.add(str3);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            this.i.e((Throwable) e);
        }
        this.f = new Random().nextInt(this.a.size());
    }

    public String b() {
        return this.c;
    }

    public int c() {
        return this.b;
    }

    public void d() {
        this.f = (this.f + 1) % this.a.size();
    }

    public boolean e() {
        return this.b == 1;
    }

    public boolean f() {
        return this.b != 0;
    }

    void g() {
        if (!com.tencent.wxop.stat.common.q.f(this.h)) {
            if (StatConfig.isDebugEnable()) {
                this.i.i("NETWORK TYPE: network is close.");
            }
            l();
            return;
        }
        if (StatConfig.g) {
            k();
        }
        this.c = com.tencent.wxop.stat.common.k.l(this.h);
        if (StatConfig.isDebugEnable()) {
            this.i.i("NETWORK name:" + this.c);
        }
        if (com.tencent.wxop.stat.common.k.c(this.c)) {
            this.b = "WIFI".equalsIgnoreCase(this.c) ? 1 : 2;
            this.d = com.tencent.wxop.stat.common.k.a(this.h);
        }
        if (StatServiceImpl.a()) {
            StatServiceImpl.d(this.h);
        }
    }

    public void h() {
        this.h.getApplicationContext().registerReceiver(new b(this), new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }
}
