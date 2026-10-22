package com.amap.api.mapcore.util;

import com.amap.api.maps.AMapException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

/* JADX INFO: compiled from: BaseNetManager.java */
/* JADX INFO: loaded from: classes.dex */
public class gw {
    private static gw a;

    /* JADX INFO: compiled from: BaseNetManager.java */
    public interface a {
        URLConnection a(Proxy proxy, URL url);
    }

    public static gw a() {
        if (a == null) {
            a = new gw();
        }
        return a;
    }

    public byte[] a(hc hcVar) throws ev {
        try {
            he heVarA = a(hcVar, true);
            if (heVarA != null) {
                return heVarA.a;
            }
            return null;
        } catch (ev e) {
            throw e;
        } catch (Throwable unused) {
            throw new ev(AMapException.ERROR_UNKNOWN);
        }
    }

    public byte[] b(hc hcVar) throws ev {
        try {
            he heVarA = a(hcVar, false);
            if (heVarA != null) {
                return heVarA.a;
            }
            return null;
        } catch (ev e) {
            throw e;
        } catch (Throwable th) {
            fk.a(th, "BaseNetManager", "makeSyncPostRequest");
            throw new ev(AMapException.ERROR_UNKNOWN);
        }
    }

    protected void c(hc hcVar) throws ev {
        if (hcVar == null) {
            throw new ev("requeust is null");
        }
        if (hcVar.c() == null || "".equals(hcVar.c())) {
            throw new ev("request url is empty");
        }
    }

    public he a(hc hcVar, boolean z) throws ev {
        try {
            c(hcVar);
            return new gz(hcVar.f, hcVar.g, hcVar.h == null ? null : hcVar.h, z).a(hcVar.m(), hcVar.a(), hcVar.n());
        } catch (ev e) {
            throw e;
        } catch (Throwable th) {
            th.printStackTrace();
            throw new ev(AMapException.ERROR_UNKNOWN);
        }
    }
}
