package com.amap.api.mapcore.util;

import android.content.ContentResolver;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import cn.jiguang.internal.JConstants;
import com.zhy.http.okhttp.OkHttpUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

/* JADX INFO: compiled from: WifiManagerWrapper.java */
/* JADX INFO: loaded from: classes.dex */
public final class ie {
    static long e;
    static long f;
    static long g;
    static long h;
    WifiManager a;
    Context i;
    Object b = new Object();
    ArrayList<ScanResult> c = new ArrayList<>();
    ArrayList<ScanResult> d = new ArrayList<>();
    boolean j = false;
    StringBuilder k = null;
    boolean l = true;
    boolean m = true;
    private volatile WifiInfo s = null;
    String n = "isScanAlwaysAvailable";
    String o = null;
    TreeMap<Integer, ScanResult> p = null;
    public boolean q = true;
    ConnectivityManager r = null;

    public ie(Context context, WifiManager wifiManager) {
        this.a = wifiManager;
        this.i = context;
    }

    private static boolean a(int i) {
        int iCalculateSignalLevel = 20;
        try {
            iCalculateSignalLevel = WifiManager.calculateSignalLevel(i, 20);
        } catch (ArithmeticException e2) {
            ij.a(e2, "APS", "wifiSigFine");
        }
        return iCalculateSignalLevel > 0;
    }

    public static boolean a(WifiInfo wifiInfo) {
        return (wifiInfo == null || TextUtils.isEmpty(wifiInfo.getSSID()) || !im.a(wifiInfo.getBSSID())) ? false : true;
    }

    public static String i() {
        return String.valueOf(im.b() - h);
    }

    private List<ScanResult> j() {
        WifiManager wifiManager = this.a;
        if (wifiManager != null) {
            try {
                List<ScanResult> scanResults = wifiManager.getScanResults();
                this.o = null;
                return scanResults;
            } catch (SecurityException e2) {
                this.o = e2.getMessage();
            } catch (Throwable th) {
                this.o = null;
                ij.a(th, "WifiManagerWrapper", "getScanResults");
            }
        }
        return null;
    }

    private WifiInfo k() {
        try {
            WifiManager wifiManager = this.a;
            if (wifiManager != null) {
                return wifiManager.getConnectionInfo();
            }
            return null;
        } catch (Throwable th) {
            ij.a(th, "WifiManagerWrapper", "getConnectionInfo");
            return null;
        }
    }

    private int l() {
        WifiManager wifiManager = this.a;
        if (wifiManager != null) {
            return wifiManager.getWifiState();
        }
        return 4;
    }

    private boolean m() {
        if (im.b() - e < 4900) {
            return false;
        }
        if ((n() && im.b() - e < 9900) || this.a == null) {
            return false;
        }
        e = im.b();
        return this.a.startScan();
    }

    private boolean n() {
        if (this.r == null) {
            this.r = (ConnectivityManager) im.a(this.i, "connectivity");
        }
        return a(this.r);
    }

    private boolean o() {
        if (this.a == null) {
            return false;
        }
        return im.c(this.i);
    }

    private void p() {
        String strValueOf;
        ArrayList<ScanResult> arrayList = this.c;
        if (arrayList == null || arrayList.isEmpty()) {
            return;
        }
        if (im.b() - h > JConstants.HOUR) {
            b();
            this.c.clear();
        }
        if (this.p == null) {
            this.p = new TreeMap<>(Collections.reverseOrder());
        }
        this.p.clear();
        int size = this.c.size();
        for (int i = 0; i < size; i++) {
            ScanResult scanResult = this.c.get(i);
            if (im.a(scanResult != null ? scanResult.BSSID : "") && (size <= 20 || a(scanResult.level))) {
                if (!TextUtils.isEmpty(scanResult.SSID)) {
                    strValueOf = "<unknown ssid>".equals(scanResult.SSID) ? "unkwn" : String.valueOf(i);
                    this.p.put(Integer.valueOf((scanResult.level * 25) + i), scanResult);
                }
                scanResult.SSID = strValueOf;
                this.p.put(Integer.valueOf((scanResult.level * 25) + i), scanResult);
            }
        }
        this.c.clear();
        Iterator<ScanResult> it = this.p.values().iterator();
        while (it.hasNext()) {
            this.c.add(it.next());
        }
        this.p.clear();
    }

    private void q() {
        if (t()) {
            long jB = im.b();
            if (jB - f >= OkHttpUtils.DEFAULT_MILLISECONDS) {
                synchronized (this.b) {
                    this.d.clear();
                }
            }
            s();
            if (jB - f >= OkHttpUtils.DEFAULT_MILLISECONDS) {
                for (int i = 20; i > 0 && this.d.isEmpty(); i--) {
                    try {
                        Thread.sleep(150L);
                    } catch (Throwable unused) {
                    }
                }
            }
            synchronized (this.b) {
            }
        }
    }

    private void r() {
        ArrayList<ScanResult> arrayList = this.c;
        ArrayList<ScanResult> arrayList2 = this.d;
        arrayList.clear();
        synchronized (this.b) {
            if (arrayList2 != null) {
                if (arrayList2.size() > 0) {
                    arrayList.addAll(arrayList2);
                }
            }
        }
    }

    private void s() {
        if (t()) {
            try {
                if (m()) {
                    g = im.b();
                }
            } catch (Throwable th) {
                ij.a(th, "APS", "updateWifi");
            }
        }
    }

    private boolean t() {
        boolean zO = o();
        this.q = zO;
        if (!zO || !this.l) {
            return false;
        }
        if (g != 0) {
            if (im.b() - g < 4900 || im.b() - h < 1500) {
                return false;
            }
            im.b();
        }
        return true;
    }

    public final ArrayList<ScanResult> a() {
        return this.c;
    }

    public final void a(boolean z) {
        Context context = this.i;
        if (this.a == null || context == null || !z || im.c() <= 17) {
            return;
        }
        ContentResolver contentResolver = context.getContentResolver();
        try {
            if (((Integer) ik.a("android.provider.Settings$Global", "getInt", new Object[]{contentResolver, "wifi_scan_always_enabled"}, (Class<?>[]) new Class[]{ContentResolver.class, String.class})).intValue() == 0) {
                ik.a("android.provider.Settings$Global", "putInt", new Object[]{contentResolver, "wifi_scan_always_enabled", 1}, (Class<?>[]) new Class[]{ContentResolver.class, String.class, Integer.TYPE});
            }
        } catch (Throwable th) {
            ij.a(th, "WifiManagerWrapper", "enableWifiAlwaysScan");
        }
    }

    public final boolean a(ConnectivityManager connectivityManager) {
        WifiManager wifiManager = this.a;
        if (wifiManager == null) {
            return false;
        }
        try {
            return im.a(connectivityManager.getActiveNetworkInfo()) == 1 && a(wifiManager.getConnectionInfo());
        } catch (Throwable th) {
            ij.a(th, "WifiManagerWrapper", "wifiAccess");
            return false;
        }
    }

    public final void b() {
        this.s = null;
        synchronized (this.b) {
            this.d.clear();
        }
    }

    public final void b(boolean z) {
        if (z) {
            q();
        } else {
            s();
        }
        if (im.b() - h > 20000) {
            synchronized (this.b) {
                this.d.clear();
            }
        }
        f = im.b();
        if (this.d.isEmpty()) {
            h = im.b();
            List<ScanResult> listJ = j();
            if (listJ != null) {
                synchronized (this.b) {
                    this.d.addAll(listJ);
                }
            }
        }
        r();
        p();
    }

    public final void c() {
        List<ScanResult> listJ;
        if (this.a != null && im.b() - h > 4900) {
            try {
                listJ = j();
            } catch (Throwable th) {
                ij.a(th, "APS", "onReceive part1");
                listJ = null;
            }
            if (listJ == null) {
                synchronized (this.b) {
                    this.d.clear();
                }
            } else {
                synchronized (this.b) {
                    this.d.clear();
                    this.d.addAll(listJ);
                    h = im.b();
                }
            }
        }
    }

    public final void c(boolean z) {
        this.l = z;
        this.m = true;
    }

    public final void d() {
        int iL;
        if (this.a == null) {
            return;
        }
        try {
            iL = l();
        } catch (Throwable th) {
            ij.a(th, "APS", "onReceive part");
            iL = 4;
        }
        if (this.d == null) {
            this.d = new ArrayList<>();
        }
        if (iL == 0) {
            b();
        } else if (iL == 1) {
            b();
        } else {
            if (iL != 4) {
                return;
            }
            b();
        }
    }

    public final boolean e() {
        return this.q;
    }

    public final WifiInfo f() {
        this.s = k();
        return this.s;
    }

    public final boolean g() {
        return this.j;
    }

    public final void h() {
        b();
        this.c.clear();
    }
}
