package com.qiniu.android.common;

import java.net.URI;
import java.util.Iterator;

/* JADX INFO: loaded from: classes2.dex */
public abstract class Zone {

    public interface QueryHandler {
        void onFailure(int i);

        void onSuccess();
    }

    public abstract void frozenDomain(String str);

    public abstract void preQuery(String str, QueryHandler queryHandler);

    public abstract boolean preQuery(String str);

    public abstract String upHost(String str, boolean z, String str2);

    protected synchronized String upHost(ZoneInfo zoneInfo, boolean z, String str) {
        String str2;
        String str3;
        if (str != null) {
            try {
                zoneInfo.frozenDomain(URI.create(str).getHost());
            } catch (Throwable th) {
                throw th;
            }
        }
        int i = 0;
        while (true) {
            str2 = null;
            if (i >= zoneInfo.upDomainsList.size()) {
                str3 = null;
                break;
            }
            str3 = zoneInfo.upDomainsList.get(i);
            long jLongValue = zoneInfo.upDomainsMap.get(str3).longValue();
            if (jLongValue == 0 || jLongValue <= System.currentTimeMillis() / 1000) {
                break;
                break;
            }
            i++;
        }
        if (str3 != null) {
            zoneInfo.upDomainsMap.put(str3, 0L);
        } else {
            Iterator<String> it = zoneInfo.upDomainsList.iterator();
            while (it.hasNext()) {
                zoneInfo.upDomainsMap.put(it.next(), 0L);
            }
            if (zoneInfo.upDomainsList.size() > 0) {
                str3 = zoneInfo.upDomainsList.get(0);
            }
        }
        if (str3 != null) {
            if (z) {
                str2 = String.format("https://%s", str3);
            } else {
                str2 = String.format("http://%s", str3);
            }
        }
        return str2;
    }
}
