package com.amap.api.mapcore.util;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.trace.LBSTraceBase;
import com.amap.api.trace.LBSTraceClient;
import com.amap.api.trace.TraceListener;
import com.amap.api.trace.TraceLocation;
import com.amap.api.trace.TraceStatusListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* JADX INFO: compiled from: TraceManager.java */
/* JADX INFO: loaded from: classes.dex */
public class et implements LocationSource.OnLocationChangedListener, LBSTraceBase {
    private Context a;
    private CoordinateConverter b;
    private TraceStatusListener g;
    private af h;
    private long e = 2000;
    private int f = 5;
    private List<TraceLocation> i = new ArrayList();
    private int j = 0;
    private int k = 0;
    private long l = 0;
    private ExecutorService c = Executors.newFixedThreadPool(1);
    private c m = new c(Looper.getMainLooper());
    private ExecutorService d = Executors.newFixedThreadPool((Runtime.getRuntime().availableProcessors() * 2) + 3);

    public et(Context context) {
        this.a = context.getApplicationContext();
        this.b = new CoordinateConverter(this.a);
    }

    @Override // com.amap.api.trace.LBSTraceBase
    public void queryProcessedTrace(int i, List<TraceLocation> list, int i2, TraceListener traceListener) {
        try {
            this.c.execute(new a(i, list, i2, traceListener));
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    @Override // com.amap.api.trace.LBSTraceBase
    public void setLocationInterval(long j) {
        this.e = j;
    }

    @Override // com.amap.api.trace.LBSTraceBase
    public void setTraceStatusInterval(int i) {
        this.f = Math.max(i, 2);
    }

    @Override // com.amap.api.trace.LBSTraceBase
    public void startTrace(TraceStatusListener traceStatusListener) {
        if (this.a == null) {
            Log.w("LBSTraceClient", "Context need to be initialized");
            return;
        }
        this.l = System.currentTimeMillis();
        this.g = traceStatusListener;
        if (this.h == null) {
            af afVar = new af(this.a);
            this.h = afVar;
            afVar.a(this.e);
            this.h.activate(this);
        }
    }

    @Override // com.amap.api.maps.LocationSource.OnLocationChangedListener
    public void onLocationChanged(Location location) {
        TraceStatusListener traceStatusListener;
        if (this.g != null) {
            try {
                long jCurrentTimeMillis = System.currentTimeMillis();
                if (jCurrentTimeMillis - this.l >= 30000 && (traceStatusListener = this.g) != null) {
                    traceStatusListener.onTraceStatus(null, null, LBSTraceClient.LOCATE_TIMEOUT_ERROR);
                    this.l = jCurrentTimeMillis;
                }
                Bundle extras = location.getExtras();
                int i = extras.getInt(MyLocationStyle.ERROR_CODE);
                if (i != 0) {
                    Log.w("LBSTraceClient", "Locate failed [errorCode:\"" + i + "\"  errorInfo:" + extras.getString(MyLocationStyle.ERROR_INFO) + "\"]");
                    return;
                }
                synchronized (this.i) {
                    this.i.add(new TraceLocation(location.getLatitude(), location.getLongitude(), location.getSpeed(), location.getBearing(), location.getTime()));
                    int i2 = this.j + 1;
                    this.j = i2;
                    if (i2 == this.f) {
                        int i3 = this.k + i2;
                        this.k = i3;
                        a(i3);
                        this.l = System.currentTimeMillis();
                        this.j = 0;
                    }
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    private void a(int i) {
        ArrayList arrayList = new ArrayList(this.i.subList(0, i));
        queryProcessedTrace(i, arrayList, 1, new b(arrayList));
    }

    @Override // com.amap.api.trace.LBSTraceBase
    public void stopTrace() {
        a();
        synchronized (this.i) {
            if (this.i.size() > this.k) {
                int size = ((this.i.size() - this.k) / this.f) + 1;
                for (int i = 0; i < size; i++) {
                    if (i == size - 1) {
                        a(this.i.size());
                    } else {
                        int i2 = this.k + this.f;
                        this.k = i2;
                        a(i2);
                    }
                }
            }
        }
    }

    private void a() {
        af afVar = this.h;
        if (afVar != null) {
            afVar.deactivate();
            this.h = null;
        }
    }

    @Override // com.amap.api.trace.LBSTraceBase
    public void destroy() {
        try {
            a();
            ExecutorService executorService = this.c;
            if (executorService != null && !executorService.isShutdown()) {
                this.c.shutdownNow();
                this.c = null;
            }
            ExecutorService executorService2 = this.d;
            if (executorService2 != null && !executorService2.isShutdown()) {
                this.d.shutdownNow();
                this.d = null;
            }
            synchronized (this.i) {
                List<TraceLocation> list = this.i;
                if (list != null) {
                    list.clear();
                    this.i = null;
                }
            }
            this.g = null;
        } catch (Throwable th) {
            th.printStackTrace();
        }
        this.a = null;
        this.b = null;
    }

    /* JADX INFO: compiled from: TraceManager.java */
    class b implements TraceListener {
        private final List<TraceLocation> b;

        @Override // com.amap.api.trace.TraceListener
        public void onTraceProcessing(int i, int i2, List<LatLng> list) {
        }

        public b(List<TraceLocation> list) {
            this.b = list;
        }

        @Override // com.amap.api.trace.TraceListener
        public void onRequestFailed(int i, String str) {
            et.this.g.onTraceStatus(this.b, null, str);
        }

        @Override // com.amap.api.trace.TraceListener
        public void onFinished(int i, List<LatLng> list, int i2, int i3) {
            et.this.g.onTraceStatus(this.b, list, LBSTraceClient.TRACE_SUCCESS);
        }
    }

    /* JADX INFO: compiled from: TraceManager.java */
    class a implements Runnable {
        private int c;
        private int d;
        private List<TraceLocation> e;
        private TraceListener g;
        private List<TraceLocation> b = new ArrayList();
        private String f = dq.a();

        public a(int i, List<TraceLocation> list, int i2, TraceListener traceListener) {
            this.c = i2;
            this.d = i;
            this.e = list;
            this.g = traceListener;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                et.this.m.a(this.g);
                int iA = a();
                List<TraceLocation> list = this.e;
                if (list != null && list.size() >= 2) {
                    Iterator<TraceLocation> it = this.e.iterator();
                    while (it.hasNext()) {
                        TraceLocation traceLocationCopy = it.next().copy();
                        if (traceLocationCopy != null && traceLocationCopy.getLatitude() > 0.0d && traceLocationCopy.getLongitude() > 0.0d) {
                            this.b.add(traceLocationCopy);
                        }
                    }
                    int size = (this.b.size() - 2) / 500;
                    eu.a().a(this.f, this.d, size, iA);
                    int i = 0;
                    int size2 = 500;
                    while (i <= size) {
                        if (i == size) {
                            size2 = this.b.size();
                        }
                        int i2 = size2;
                        ArrayList arrayList = new ArrayList();
                        for (int i3 = 0; i3 < i2; i3++) {
                            TraceLocation traceLocationRemove = this.b.remove(0);
                            if (traceLocationRemove != null) {
                                int i4 = this.c;
                                if (i4 != 1) {
                                    if (i4 == 3) {
                                        et.this.b.from(CoordinateConverter.CoordType.BAIDU);
                                    } else if (i4 == 2) {
                                        et.this.b.from(CoordinateConverter.CoordType.GPS);
                                    }
                                    et.this.b.coord(new LatLng(traceLocationRemove.getLatitude(), traceLocationRemove.getLongitude()));
                                    LatLng latLngConvert = et.this.b.convert();
                                    if (latLngConvert != null) {
                                        traceLocationRemove.setLatitude(latLngConvert.latitude);
                                        traceLocationRemove.setLongitude(latLngConvert.longitude);
                                    }
                                }
                                arrayList.add(traceLocationRemove);
                            }
                        }
                        if (arrayList.size() >= 2 && arrayList.size() <= 500) {
                            et.this.d.execute(new es(et.this.a, et.this.m, arrayList, this.c, this.f, this.d, i));
                            i++;
                            try {
                                Thread.sleep(50L);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        size2 = i2;
                    }
                    return;
                }
                eu.a().a(et.this.m, this.d, LBSTraceClient.MIN_GRASP_POINT_ERROR);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }

        private int a() {
            List<TraceLocation> list = this.e;
            int iA = 0;
            if (list != null && list.size() != 0) {
                ArrayList arrayList = new ArrayList();
                for (TraceLocation traceLocation : this.e) {
                    if (traceLocation != null) {
                        if (traceLocation.getSpeed() < 0.01d) {
                            arrayList.add(traceLocation);
                        } else {
                            iA += a(arrayList);
                            arrayList.clear();
                        }
                    }
                }
            }
            return iA;
        }

        private int a(List<TraceLocation> list) {
            int size = list.size();
            if (size <= 1) {
                return 0;
            }
            TraceLocation traceLocation = list.get(0);
            TraceLocation traceLocation2 = list.get(size - 1);
            if (traceLocation == null || traceLocation2 == null || traceLocation == null || traceLocation2 == null) {
                return 0;
            }
            return (int) ((traceLocation2.getTime() - traceLocation.getTime()) / 1000);
        }
    }

    /* JADX INFO: compiled from: TraceManager.java */
    static class c extends Handler {
        private TraceListener a;

        public c(Looper looper) {
            super(looper);
        }

        public void a(TraceListener traceListener) {
            this.a = traceListener;
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            Bundle data;
            try {
                if (this.a == null || (data = message.getData()) == null) {
                    return;
                }
                int i = data.getInt("lineID");
                switch (message.what) {
                    case 100:
                        this.a.onTraceProcessing(i, message.arg1, (List) message.obj);
                        break;
                    case 101:
                        this.a.onFinished(i, (List) message.obj, message.arg1, message.arg2);
                        break;
                    case 102:
                        this.a.onRequestFailed(i, (String) message.obj);
                        break;
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }
}
