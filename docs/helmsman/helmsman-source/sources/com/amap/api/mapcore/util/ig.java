package com.amap.api.mapcore.util;

import android.content.Context;
import com.qiniu.android.http.Client;
import im.helmsman.helmsmanandroid.factory.OkHttpFactory;
import java.util.HashMap;
import java.util.Locale;

/* JADX INFO: compiled from: LocNetManager.java */
/* JADX INFO: loaded from: classes.dex */
public final class ig {
    private static ig b;
    gw a;
    private Context c;
    private int d = 0;
    private int e = ij.f;
    private boolean f = false;

    private ig(Context context) {
        this.a = null;
        this.c = null;
        this.c = context;
        this.a = gw.a();
    }

    public static ig a(Context context) {
        if (b == null) {
            b = new ig(context);
        }
        return b;
    }

    public final he a(ih ihVar) throws Throwable {
        long jB = im.b();
        he heVarA = this.a.a(ihVar, this.f);
        this.d = Long.valueOf(im.b() - jB).intValue();
        return heVarA;
    }

    public final ih a(Context context, byte[] bArr, String str) {
        try {
            HashMap map = new HashMap();
            ih ihVar = new ih(context, ij.b());
            try {
                map.put(Client.ContentTypeHeader, Client.DefaultMime);
                map.put("Accept-Encoding", "gzip");
                map.put("gzipped", "1");
                map.put("Connection", "Keep-Alive");
                map.put("User-Agent", "AMAP_Location_SDK_Android 3.6.0");
                map.put("KEY", ew.f(context));
                map.put("enginever", "4.7");
                String strA = ez.a();
                String strA2 = ez.a(context, strA, "key=" + ew.f(context));
                map.put("ts", strA);
                map.put("scode", strA2);
                map.put("encr", "1");
                ihVar.b(map);
                ihVar.o();
                ihVar.a(String.format(Locale.US, "platform=Android&sdkversion=%s&product=%s&loc_channel=%s", "3.6.0", "loc", 3));
                ihVar.d();
                ihVar.b(str);
                ihVar.b(im.a(bArr));
                ihVar.a(fd.a(context));
                HashMap map2 = new HashMap();
                map2.put("output", "bin");
                map2.put("policy", "3103");
                ihVar.a(map2);
                ihVar.a(this.e);
                ihVar.b(this.e);
                if (!this.f) {
                    return ihVar;
                }
                ihVar.b(ihVar.c().replace(OkHttpFactory.TYPE_HTTP, OkHttpFactory.TYPE_HTTPS));
                return ihVar;
            } catch (Throwable unused) {
                return ihVar;
            }
        } catch (Throwable unused2) {
            return null;
        }
    }

    public final void a(long j, boolean z) {
        try {
            this.f = z;
            this.e = Long.valueOf(j).intValue();
        } catch (Throwable th) {
            ij.a(th, "netmanager", "setOption");
        }
    }
}
