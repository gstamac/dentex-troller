package com.amap.api.maps;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;
import com.amap.api.mapcore.util.ey;
import com.amap.api.mapcore.util.g;
import com.amap.api.mapcore.util.o;
import com.autonavi.ae.gmap.GLMapEngine;

/* JADX INFO: loaded from: classes.dex */
public final class MapsInitializer {
    private static boolean a = true;
    public static String sdcardDir = "";

    public static String getVersion() {
        return "5.4.0";
    }

    public static void initialize(Context context) throws RemoteException {
        if (context != null) {
            o.a = context.getApplicationContext();
        } else {
            Log.w("MapsInitializer", "the context is null");
        }
    }

    public static void setNetWorkEnable(boolean z) {
        a = z;
    }

    public static boolean getNetWorkEnable() {
        return a;
    }

    public static void setApiKey(String str) {
        if (str == null || str.trim().length() <= 0) {
            return;
        }
        ey.a(str);
    }

    public static void loadWorldGridMap(boolean z) {
        g.c = !z ? 1 : 0;
    }

    public static void setBuildingHeight(int i) {
        GLMapEngine.BUILDINGHEIGHT = i;
    }
}
