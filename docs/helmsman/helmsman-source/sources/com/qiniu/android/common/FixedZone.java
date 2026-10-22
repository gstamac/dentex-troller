package com.qiniu.android.common;

import android.util.Log;
import java.net.URI;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes2.dex */
public final class FixedZone extends Zone {
    private ZoneInfo zoneInfo;
    public static final Zone zone0 = new FixedZone(new String[]{"upload.qiniup.com", "upload-jjh.qiniup.com", "upload-xs.qiniup.com", "up.qiniup.com", "up-jjh.qiniup.com", "up-xs.qiniup.com", "upload.qbox.me", "up.qbox.me"});
    public static final Zone zone1 = new FixedZone(new String[]{"upload-z1.qiniup.com", "up-z1.qiniup.com", "upload-z1.qbox.me", "up-z1.qbox.me"});
    public static final Zone zone2 = new FixedZone(new String[]{"upload-z2.qiniup.com", "upload-dg.qiniup.com", "upload-fs.qiniup.com", "up-z2.qiniup.com", "up-dg.qiniup.com", "up-fs.qiniup.com", "upload-z2.qbox.me", "up-z2.qbox.me"});
    public static final Zone zoneNa0 = new FixedZone(new String[]{"upload-na0.qiniup.com", "up-na0.qiniup.com", "upload-na0.qbox.me", "up-na0.qbox.me"});
    public static final Zone zoneAs0 = new FixedZone(new String[]{"upload-as0.qiniup.com", "up-as0.qiniup.com", "upload-as0.qbox.me", "up-as0.qbox.me"});

    @Override // com.qiniu.android.common.Zone
    public boolean preQuery(String str) {
        return true;
    }

    public FixedZone(ZoneInfo zoneInfo) {
        this.zoneInfo = zoneInfo;
    }

    public FixedZone(String[] strArr) {
        this.zoneInfo = createZoneInfo(strArr);
    }

    public static ZoneInfo createZoneInfo(String[] strArr) {
        ArrayList arrayList = new ArrayList();
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        for (String str : strArr) {
            arrayList.add(str);
            concurrentHashMap.put(str, 0L);
        }
        return new ZoneInfo(0, arrayList, concurrentHashMap);
    }

    @Override // com.qiniu.android.common.Zone
    public synchronized String upHost(String str, boolean z, String str2) {
        String strUpHost;
        strUpHost = upHost(this.zoneInfo, z, str2);
        for (Map.Entry<String, Long> entry : this.zoneInfo.upDomainsMap.entrySet()) {
            Log.d("Qiniu.FixedZone", entry.getKey() + ", " + entry.getValue());
        }
        return strUpHost;
    }

    @Override // com.qiniu.android.common.Zone
    public void preQuery(String str, Zone.QueryHandler queryHandler) {
        queryHandler.onSuccess();
    }

    @Override // com.qiniu.android.common.Zone
    public synchronized void frozenDomain(String str) {
        if (str != null) {
            this.zoneInfo.frozenDomain(URI.create(str).getHost());
        }
    }
}
