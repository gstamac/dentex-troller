package com.amap.api.mapcore.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import com.autonavi.amap.mapcore.Inner_3dMap_location;
import com.autonavi.amap.mapcore.Inner_3dMap_locationOption;
import com.zhy.http.okhttp.OkHttpUtils;

/* JADX INFO: compiled from: MapNetLocation.java */
/* JADX INFO: loaded from: classes.dex */
public final class hx {
    Context a;
    private ie h;
    private id i;
    private ig k;
    private ConnectivityManager l;
    private ii m;
    private Inner_3dMap_locationOption o;
    private a j = null;
    boolean b = false;
    private StringBuilder n = new StringBuilder();
    String c = null;
    private hp p = null;
    long d = 0;
    WifiInfo e = null;
    boolean f = true;
    private String q = "00:00:00:00:00:00";
    int g = 12;

    /* JADX INFO: compiled from: MapNetLocation.java */
    private class a extends BroadcastReceiver {
        private a() {
        }

        /* synthetic */ a(hx hxVar, byte b) {
            this();
        }

        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            if (context == null || intent == null) {
                return;
            }
            try {
                String action = intent.getAction();
                if (TextUtils.isEmpty(action)) {
                    return;
                }
                if (action.equals("android.net.wifi.SCAN_RESULTS")) {
                    if (hx.this.h != null) {
                        hx.this.h.c();
                    }
                } else {
                    if (!action.equals("android.net.wifi.WIFI_STATE_CHANGED") || hx.this.h == null) {
                        return;
                    }
                    hx.this.h.d();
                }
            } catch (Throwable th) {
                ij.a(th, "NetLocation", "onReceive");
            }
        }
    }

    public hx(Context context) {
        this.a = null;
        this.h = null;
        this.i = null;
        this.k = null;
        this.l = null;
        this.m = null;
        this.o = null;
        try {
            Context applicationContext = context.getApplicationContext();
            this.a = applicationContext;
            im.b(applicationContext);
            a(this.a);
            this.o = new Inner_3dMap_locationOption();
            if (this.h == null) {
                ie ieVar = new ie(this.a, (WifiManager) im.a(this.a, "wifi"));
                this.h = ieVar;
                ieVar.a(this.b);
            }
            if (this.i == null) {
                this.i = new id(this.a);
            }
            if (this.k == null) {
                this.k = ig.a(this.a);
            }
            if (this.l == null) {
                this.l = (ConnectivityManager) im.a(this.a, "connectivity");
            }
            this.m = new ii();
            c();
        } catch (Throwable th) {
            ij.a(th, "NetLocation", "<init>");
        }
    }

    private static hp a(hp hpVar, String... strArr) {
        if (strArr == null || strArr.length == 0) {
            return hs.a().a(hpVar);
        }
        if (strArr[0].equals("shake")) {
            return hs.a().a(hpVar);
        }
        if (!strArr[0].equals("fusion")) {
            return hpVar;
        }
        hs.a();
        return hs.b(hpVar);
    }

    private void a(Context context) {
        try {
            if (context.checkCallingOrSelfPermission("android.permission.WRITE_SECURE_SETTINGS") == 0) {
                this.b = true;
            }
        } catch (Throwable unused) {
        }
    }

    private boolean a(long j) {
        if (im.b() - j < 800) {
            if ((hz.a(this.p) ? im.a() - this.p.getTime() : 0L) <= OkHttpUtils.DEFAULT_MILLISECONDS) {
                return true;
            }
        }
        return false;
    }

    private void c() {
        try {
            byte b = 0;
            if (this.j == null) {
                this.j = new a(this, b);
            }
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
            intentFilter.addAction("android.net.wifi.SCAN_RESULTS");
            this.a.registerReceiver(this.j, intentFilter);
            this.h.b(false);
            this.i.f();
        } catch (Throwable th) {
            ij.a(th, "NetLocation", "initBroadcastListener");
        }
    }

    private hp d() throws Exception {
        byte[] bArr;
        String str;
        StringBuilder sb;
        String str2;
        hp hpVar = new hp("");
        if (this.h.g()) {
            hpVar.setErrorCode(15);
            return hpVar;
        }
        try {
            if (this.m == null) {
                this.m = new ii();
            }
            this.m.a(this.a, this.o.isNeedAddress(), this.o.isOffset(), this.i, this.h, this.l, this.q, this.c);
            hy hyVar = new hy();
            try {
                try {
                    he heVarA = this.k.a(this.k.a(this.a, this.m.a(), ij.a()));
                    if (heVarA != null) {
                        bArr = heVarA.a;
                        str = heVarA.c;
                    } else {
                        bArr = null;
                        str = "";
                    }
                    if (bArr == null || bArr.length == 0) {
                        hpVar.setErrorCode(4);
                        this.n.append("please check the network");
                        if (!TextUtils.isEmpty(str)) {
                            this.n.append(" #csid:" + str);
                        }
                        hpVar.setLocationDetail(this.n.toString());
                        return hpVar;
                    }
                    String str3 = new String(bArr, "UTF-8");
                    if (str3.contains("\"status\":\"0\"")) {
                        return hyVar.a(str3, this.a, heVarA);
                    }
                    if (str3.contains("</body></html>")) {
                        hpVar.setErrorCode(5);
                        ie ieVar = this.h;
                        if (ieVar == null || !ieVar.a(this.l)) {
                            sb = this.n;
                            str2 = "request may be intercepted";
                        } else {
                            sb = this.n;
                            str2 = "make sure you are logged in to the network";
                        }
                        sb.append(str2);
                        if (!TextUtils.isEmpty(str)) {
                            this.n.append(" #csid:" + str);
                        }
                        hpVar.setLocationDetail(this.n.toString());
                        return hpVar;
                    }
                    byte[] bArrA = Cif.a(bArr);
                    if (bArrA == null) {
                        hpVar.setErrorCode(5);
                        this.n.append("decrypt response data error");
                        if (!TextUtils.isEmpty(str)) {
                            this.n.append(" #csid:" + str);
                        }
                        hpVar.setLocationDetail(this.n.toString());
                        return hpVar;
                    }
                    hp hpVarA = hyVar.a(bArrA);
                    if (hpVarA == null) {
                        hp hpVar2 = new hp("");
                        hpVar2.setErrorCode(5);
                        this.n.append("location is null");
                        if (!TextUtils.isEmpty(str)) {
                            this.n.append(" #csid:" + str);
                        }
                        hpVar2.setLocationDetail(this.n.toString());
                        return hpVar2;
                    }
                    this.c = hpVarA.a();
                    if (hpVarA.getErrorCode() != 0) {
                        if (!TextUtils.isEmpty(str)) {
                            hpVarA.setLocationDetail(hpVarA.getLocationDetail() + " #csid:" + str);
                        }
                        return hpVarA;
                    }
                    if (!hz.a(hpVarA)) {
                        String strB = hpVarA.b();
                        hpVarA.setErrorCode(6);
                        StringBuilder sb2 = this.n;
                        StringBuilder sbAppend = new StringBuilder("location faile retype:").append(hpVarA.d()).append(" rdesc:");
                        if (strB == null) {
                            strB = "null";
                        }
                        sb2.append(sbAppend.append(strB).toString());
                        if (!TextUtils.isEmpty(str)) {
                            this.n.append(" #csid:" + str);
                        }
                        hpVarA.setLocationDetail(this.n.toString());
                        return hpVarA;
                    }
                    hpVarA.e();
                    if (hpVarA.getErrorCode() == 0 && hpVarA.getLocationType() == 0) {
                        if ("-5".equals(hpVarA.d()) || "1".equals(hpVarA.d()) || "2".equals(hpVarA.d()) || "14".equals(hpVarA.d()) || "24".equals(hpVarA.d()) || "-1".equals(hpVarA.d())) {
                            hpVarA.setLocationType(5);
                        } else {
                            hpVarA.setLocationType(6);
                        }
                        this.n.append(hpVarA.d());
                        if (!TextUtils.isEmpty(str)) {
                            this.n.append(" #csid:" + str);
                        }
                        hpVarA.setLocationDetail(this.n.toString());
                    }
                    return hpVarA;
                } catch (Throwable th) {
                    ij.a(th, "NetLocation", "getApsLoc req");
                    hpVar.setErrorCode(4);
                    this.n.append("please check the network");
                    hpVar.setLocationDetail(this.n.toString());
                    return hpVar;
                }
            } catch (Throwable th2) {
                ij.a(th2, "NetLocation", "getApsLoc buildV4Dot2");
                hpVar.setErrorCode(3);
                this.n.append("buildV4Dot2 error " + th2.getMessage());
                hpVar.setLocationDetail(this.n.toString());
                return hpVar;
            }
        } catch (Throwable th3) {
            ij.a(th3, "NetLocation", "getApsLoc");
            this.n.append("get parames error:" + th3.getMessage());
            hpVar.setErrorCode(3);
            hpVar.setLocationDetail(this.n.toString());
            return hpVar;
        }
    }

    public final Inner_3dMap_location a() {
        if (this.n.length() > 0) {
            StringBuilder sb = this.n;
            sb.delete(0, sb.length());
        }
        if (a(this.d) && hz.a(this.p)) {
            return this.p;
        }
        this.d = im.b();
        if (this.a == null) {
            this.n.append("context is null");
            Inner_3dMap_location inner_3dMap_location = new Inner_3dMap_location("");
            inner_3dMap_location.setErrorCode(1);
            inner_3dMap_location.setLocationDetail(this.n.toString());
            return inner_3dMap_location;
        }
        try {
            this.i.f();
        } catch (Throwable th) {
            ij.a(th, "NetLocation", "getLocation getCgiListParam");
        }
        try {
            this.h.b(true);
        } catch (Throwable th2) {
            ij.a(th2, "NetLocation", "getLocation getScanResultsParam");
        }
        try {
            hp hpVarD = d();
            this.p = hpVarD;
            this.p = a(hpVarD, new String[0]);
        } catch (Throwable th3) {
            ij.a(th3, "NetLocation", "getLocation getScanResultsParam");
        }
        return this.p;
    }

    public final void a(Inner_3dMap_locationOption inner_3dMap_locationOption) {
        this.o = inner_3dMap_locationOption;
        if (inner_3dMap_locationOption == null) {
            this.o = new Inner_3dMap_locationOption();
        }
        try {
            ie ieVar = this.h;
            this.o.isWifiActiveScan();
            ieVar.c(this.o.isWifiScan());
        } catch (Throwable unused) {
        }
        try {
            this.k.a(this.o.getHttpTimeOut(), this.o.getLocationProtocol().equals(Inner_3dMap_locationOption.Inner_3dMap_Enum_LocationProtocol.HTTPS));
        } catch (Throwable unused2) {
        }
    }

    public final void b() {
        a aVar;
        this.b = false;
        this.c = null;
        try {
            Context context = this.a;
            if (context != null && (aVar = this.j) != null) {
                context.unregisterReceiver(aVar);
            }
            id idVar = this.i;
            if (idVar != null) {
                idVar.g();
            }
            ie ieVar = this.h;
            if (ieVar != null) {
                ieVar.h();
            }
            this.j = null;
        } catch (Throwable unused) {
            this.j = null;
        }
    }
}
