package com.amap.api.trace;

import android.content.Context;
import com.amap.api.mapcore.util.dw;
import com.amap.api.mapcore.util.et;
import com.amap.api.mapcore.util.gi;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class LBSTraceClient {
    public static final String LOCATE_TIMEOUT_ERROR = "定位超时";
    public static final String MIN_GRASP_POINT_ERROR = "轨迹点太少或距离太近,轨迹纠偏失败";
    public static final String TRACE_SUCCESS = "纠偏成功";
    public static final int TYPE_AMAP = 1;
    public static final int TYPE_BAIDU = 3;
    public static final int TYPE_GPS = 2;
    private static LBSTraceBase a;
    private static LBSTraceClient b;

    public LBSTraceClient(Context context) {
        a(context);
    }

    private LBSTraceClient() {
    }

    public static LBSTraceClient getInstance(Context context) {
        if (b == null) {
            synchronized (LBSTraceClient.class) {
                if (b == null) {
                    a(context);
                    b = new LBSTraceClient();
                }
            }
        }
        return b;
    }

    private static void a(Context context) {
        if (context != null) {
            try {
                a = (LBSTraceBase) gi.a(context.getApplicationContext(), dw.e(), "com.amap.api.wrapper.LBSTraceClientWrapper", et.class, new Class[]{Context.class}, new Object[]{context.getApplicationContext()});
            } catch (Throwable unused) {
                a = new et(context.getApplicationContext());
            }
        }
    }

    public void queryProcessedTrace(int i, List<TraceLocation> list, int i2, TraceListener traceListener) {
        LBSTraceBase lBSTraceBase = a;
        if (lBSTraceBase != null) {
            lBSTraceBase.queryProcessedTrace(i, list, i2, traceListener);
        }
    }

    public void startTrace(TraceStatusListener traceStatusListener) {
        LBSTraceBase lBSTraceBase = a;
        if (lBSTraceBase != null) {
            lBSTraceBase.startTrace(traceStatusListener);
        }
    }

    public void stopTrace() {
        LBSTraceBase lBSTraceBase = a;
        if (lBSTraceBase != null) {
            lBSTraceBase.stopTrace();
        }
    }

    public void destroy() {
        LBSTraceBase lBSTraceBase = a;
        if (lBSTraceBase != null) {
            lBSTraceBase.destroy();
            a();
        }
    }

    private static void a() {
        a = null;
        b = null;
    }
}
