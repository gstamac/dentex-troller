package com.amap.api.mapcore.util;

import android.os.RemoteException;
import com.amap.api.maps.AMap;
import com.amap.api.maps.model.MultiPointItem;
import com.amap.api.maps.model.MultiPointOverlayOptions;
import com.autonavi.amap.mapcore.IPoint;
import com.autonavi.amap.mapcore.MapConfig;
import com.autonavi.amap.mapcore.interfaces.IMultiPointOverlay;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: compiled from: MultiPointOverlayManagerLayer.java */
/* JADX INFO: loaded from: classes.dex */
public class aj {
    cw a;
    private List<IMultiPointOverlay> b = new ArrayList();
    private AMap.OnMultiPointClickListener c;
    private k d;

    public aj(k kVar) {
        this.d = kVar;
    }

    public cw a() {
        cw cwVarW = this.d.w();
        this.a = cwVarW;
        return cwVarW;
    }

    public synchronized void b() {
        this.c = null;
        try {
            synchronized (this.b) {
                Iterator<IMultiPointOverlay> it = this.b.iterator();
                while (it.hasNext()) {
                    it.next().destroy(false);
                }
                this.b.clear();
            }
        } catch (Throwable th) {
            fn.b(th, "MultiPointOverlayManagerLayer", "destory");
            th.printStackTrace();
        }
    }

    public synchronized void c() {
        try {
            synchronized (this.b) {
                this.b.clear();
            }
        } catch (Throwable th) {
            fn.b(th, "MultiPointOverlayManagerLayer", "clear");
            th.printStackTrace();
        }
    }

    public synchronized IMultiPointOverlay a(MultiPointOverlayOptions multiPointOverlayOptions) throws RemoteException {
        if (multiPointOverlayOptions == null) {
            return null;
        }
        ai aiVar = new ai(multiPointOverlayOptions, this);
        a((IMultiPointOverlay) aiVar);
        return aiVar;
    }

    private void a(IMultiPointOverlay iMultiPointOverlay) throws RemoteException {
        synchronized (this.b) {
            this.b.add(iMultiPointOverlay);
        }
    }

    public void a(MapConfig mapConfig, float[] fArr, float[] fArr2) {
        try {
            synchronized (this.b) {
                try {
                    Iterator<IMultiPointOverlay> it = this.b.iterator();
                    while (it.hasNext()) {
                        it.next().draw(mapConfig, fArr, fArr2);
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        } catch (Throwable th2) {
            fn.b(th2, "MultiPointOverlayManagerLayer", "draw");
            th2.printStackTrace();
        }
    }

    public boolean a(IPoint iPoint) {
        MultiPointItem multiPointItemOnClick;
        if (this.c == null) {
            return false;
        }
        synchronized (this.b) {
            for (IMultiPointOverlay iMultiPointOverlay : this.b) {
                if (iMultiPointOverlay != null && (multiPointItemOnClick = iMultiPointOverlay.onClick(iPoint)) != null) {
                    AMap.OnMultiPointClickListener onMultiPointClickListener = this.c;
                    return onMultiPointClickListener != null ? onMultiPointClickListener.onPointClick(multiPointItemOnClick) : false;
                }
            }
            return false;
        }
    }

    public void a(AMap.OnMultiPointClickListener onMultiPointClickListener) {
        this.c = onMultiPointClickListener;
    }

    public void d() {
        k kVar = this.d;
        if (kVar != null) {
            kVar.setRunLowFrame(false);
        }
    }

    public void a(ai aiVar) {
        this.b.remove(aiVar);
    }
}
