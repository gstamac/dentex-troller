package cn.jiguang.u;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
final class i {
    private Context a;

    i(Context context) {
        this.a = context;
    }

    final List<cn.jiguang.v.c> a() {
        String str;
        if (!cn.jiguang.l.h.a(this.a)) {
            return null;
        }
        WifiManager wifiManager = (WifiManager) this.a.getApplicationContext().getSystemService("wifi");
        if (wifiManager == null) {
            cn.jiguang.w.a.d("JLocationWifi", "get wifiManager failed");
            return null;
        }
        if (!wifiManager.isWifiEnabled()) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        WifiInfo connectionInfo = wifiManager.getConnectionInfo();
        cn.jiguang.v.c cVar = new cn.jiguang.v.c();
        cVar.a = cn.jiguang.d.b.b(this.a);
        cVar.b = cn.jiguang.f.i.b(connectionInfo.getSSID());
        cVar.c = "connect";
        cVar.d = connectionInfo.getRssi();
        cVar.e = connectionInfo.getBSSID();
        cn.jiguang.w.a.a("JLocationWifi", "connectingWifi:" + cVar.toString());
        arrayList.add(cVar);
        if (cn.jiguang.f.a.c(this.a, "android.permission.ACCESS_COARSE_LOCATION") || cn.jiguang.f.a.c(this.a, "android.permission.ACCESS_FINE_LOCATION")) {
            List<ScanResult> scanResults = wifiManager.getScanResults();
            if (scanResults != null && scanResults.size() != 0) {
                cn.jiguang.w.a.a("JLocationWifi", "scan wifi list success:" + scanResults);
                ArrayList<ScanResult> arrayList2 = new ArrayList(scanResults);
                for (ScanResult scanResult : arrayList2) {
                    if (!(cVar.b.equals(cn.jiguang.f.i.b(scanResult.SSID)) && cVar.e.equals(scanResult.BSSID)) && scanResult.level >= -200) {
                        for (ScanResult scanResult2 : arrayList2) {
                            if (scanResult2 != scanResult && scanResult.SSID.equals(scanResult2.SSID) && scanResult.BSSID.equals(scanResult2.BSSID)) {
                                scanResults.remove(scanResult);
                            }
                        }
                    } else {
                        scanResults.remove(scanResult);
                    }
                }
                arrayList2.clear();
                Collections.sort(scanResults, new j(this));
                for (int i = 0; i < scanResults.size() && i != 9; i++) {
                    ScanResult scanResult3 = scanResults.get(i);
                    String strB = cn.jiguang.f.i.b(scanResult3.SSID);
                    cn.jiguang.v.c cVar2 = new cn.jiguang.v.c();
                    cVar2.a = cn.jiguang.d.b.b(this.a);
                    cVar2.b = strB;
                    cVar2.c = null;
                    if (i == 0) {
                        cVar2.c = "strongest";
                    }
                    cVar2.d = scanResult3.level;
                    cVar2.e = scanResult3.BSSID;
                    arrayList.add(cVar2);
                }
                return arrayList;
            }
            str = "scan wifi list failed";
        } else {
            str = "scan wifi list failed because has no Manifest.permission.LOCATION";
        }
        cn.jiguang.w.a.d("JLocationWifi", str);
        return arrayList;
    }
}
