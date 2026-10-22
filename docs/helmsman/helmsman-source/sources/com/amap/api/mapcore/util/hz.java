package com.amap.api.mapcore.util;

import com.autonavi.amap.mapcore.Inner_3dMap_location;

/* JADX INFO: compiled from: Util.java */
/* JADX INFO: loaded from: classes.dex */
public final class hz {
    private static final String[] c = {"com.amap.api.maps", "com.amap.api.mapcore", "com.autonavi.amap.mapcore", "com.amap.api.3dmap.admic", "com.amap.api.trace", "com.amap.api.trace.core"};
    public static final String[] a = {"com.amap.api.mapcore2d", "com.amap.api.maps2d"};
    static ff b = null;

    public static ff a() throws ev {
        Class<?> cls;
        ff.a aVarA;
        ff ffVar = b;
        if (ffVar != null) {
            return ffVar;
        }
        try {
            cls = Class.forName("com.amap.api.maps.MapsInitializer");
        } catch (Throwable unused) {
            cls = null;
        }
        try {
            if (cls != null) {
                String str = (String) ik.a(cls, "getVersion", (Object[]) null, (Class<?>[]) null);
                aVarA = new ff.a("3dmap", str, "AMAP_SDK_Android_Map_" + str).a(c);
            } else {
                String str2 = (String) ik.a(Class.forName("com.amap.api.maps2d.MapsInitializer"), "getVersion", (Object[]) null, (Class<?>[]) null);
                aVarA = new ff.a("2dmap", str2, "AMAP_SDK_Android_2DMap_" + str2).a(a);
            }
            b = aVarA.a();
        } catch (Throwable unused2) {
        }
        return b;
    }

    public static boolean a(hp hpVar) {
        if (hpVar == null || hpVar.d().equals("8") || hpVar.d().equals("5") || hpVar.d().equals("6")) {
            return false;
        }
        return a((Inner_3dMap_location) hpVar);
    }

    private static boolean a(Inner_3dMap_location inner_3dMap_location) {
        double longitude = inner_3dMap_location.getLongitude();
        double latitude = inner_3dMap_location.getLatitude();
        return !(longitude == 0.0d && latitude == 0.0d) && longitude <= 180.0d && latitude <= 90.0d && longitude >= -180.0d && latitude >= -90.0d;
    }
}
