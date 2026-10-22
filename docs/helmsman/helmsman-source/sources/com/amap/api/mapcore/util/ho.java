package com.amap.api.mapcore.util;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.autonavi.amap.mapcore.Inner_3dMap_locationListener;
import com.autonavi.amap.mapcore.Inner_3dMap_locationOption;

/* JADX INFO: compiled from: AMapClientUtils.java */
/* JADX INFO: loaded from: classes.dex */
public final class ho {

    /* JADX INFO: renamed from: com.amap.api.mapcore.util.ho$1, reason: invalid class name */
    /* JADX INFO: compiled from: AMapClientUtils.java */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] a;

        static {
            int[] iArr = new int[Inner_3dMap_locationOption.Inner_3dMap_Enum_LocationMode.values().length];
            a = iArr;
            try {
                iArr[Inner_3dMap_locationOption.Inner_3dMap_Enum_LocationMode.Battery_Saving.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                a[Inner_3dMap_locationOption.Inner_3dMap_Enum_LocationMode.Device_Sensors.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                a[Inner_3dMap_locationOption.Inner_3dMap_Enum_LocationMode.Hight_Accuracy.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    public static AMapLocationClientOption a() {
        return new AMapLocationClientOption();
    }

    public static void a(AMapLocationClientOption aMapLocationClientOption, Inner_3dMap_locationOption inner_3dMap_locationOption) {
        AMapLocationClientOption.AMapLocationMode aMapLocationMode;
        try {
            aMapLocationClientOption.setInterval(inner_3dMap_locationOption.getInterval());
            AMapLocationClientOption.AMapLocationMode aMapLocationMode2 = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy;
            int i = AnonymousClass1.a[inner_3dMap_locationOption.getLocationMode().ordinal()];
            if (i != 1) {
                aMapLocationMode = i != 2 ? AMapLocationClientOption.AMapLocationMode.Hight_Accuracy : AMapLocationClientOption.AMapLocationMode.Device_Sensors;
            } else {
                aMapLocationMode = AMapLocationClientOption.AMapLocationMode.Battery_Saving;
            }
            aMapLocationClientOption.setLocationMode(aMapLocationMode);
            aMapLocationClientOption.setOnceLocation(inner_3dMap_locationOption.isOnceLocation());
            aMapLocationClientOption.setNeedAddress(inner_3dMap_locationOption.isNeedAddress());
        } catch (Throwable unused) {
        }
    }

    public static void a(Object obj, Inner_3dMap_locationListener inner_3dMap_locationListener) {
        hq hqVar = new hq();
        hqVar.a(inner_3dMap_locationListener);
        ((AMapLocationClient) obj).setLocationListener(hqVar);
    }
}
