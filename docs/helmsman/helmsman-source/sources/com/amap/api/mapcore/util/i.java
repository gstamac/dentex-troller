package com.amap.api.mapcore.util;

import android.opengl.GLES20;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import com.amap.api.maps.model.ArcOptions;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.GroundOverlayOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.NavigateArrowOptions;
import com.amap.api.maps.model.PolygonOptions;
import com.amap.api.maps.model.PolylineOptions;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/* JADX INFO: compiled from: GLOverlayLayer.java */
/* JADX INFO: loaded from: classes.dex */
public class i {
    k a;
    private int c = 0;
    private List<ch> d = new Vector(500);
    private List<Integer> e = new Vector();
    private Handler f = new Handler(Looper.getMainLooper());
    private Runnable g = new Runnable() { // from class: com.amap.api.mapcore.util.i.1
        @Override // java.lang.Runnable
        public synchronized void run() {
            try {
                synchronized (i.this) {
                    if (i.this.d != null && i.this.d.size() > 0) {
                        Collections.sort(i.this.d, i.this.b);
                    }
                }
            } catch (Throwable th) {
                fn.b(th, "MapOverlayImageView", "changeOverlayIndex");
            }
        }
    };
    a b = new a();

    public synchronized String a(String str) {
        this.c++;
        return str + this.c;
    }

    /* JADX INFO: compiled from: GLOverlayLayer.java */
    static class a implements Serializable, Comparator<Object> {
        a() {
        }

        @Override // java.util.Comparator
        public int compare(Object obj, Object obj2) {
            ch chVar = (ch) obj;
            ch chVar2 = (ch) obj2;
            if (chVar == null || chVar2 == null) {
                return 0;
            }
            try {
                if (chVar.getZIndex() > chVar2.getZIndex()) {
                    return 1;
                }
                return chVar.getZIndex() < chVar2.getZIndex() ? -1 : 0;
            } catch (Throwable th) {
                fn.b(th, "GLOverlayLayer", "compare");
                th.printStackTrace();
                return 0;
            }
        }
    }

    public i(k kVar) {
        this.a = kVar;
    }

    public synchronized void b(String str) {
        ch next;
        if (str != null) {
            try {
                if (str.trim().length() == 0) {
                    this.d.clear();
                    a();
                } else {
                    Iterator<ch> it = this.d.iterator();
                    do {
                        if (!it.hasNext()) {
                            next = null;
                            break;
                        }
                        next = it.next();
                    } while (!str.equals(next.getId()));
                    this.d.clear();
                    if (next != null) {
                        this.d.add(next);
                    }
                }
            } catch (Throwable th) {
                fn.b(th, "GLOverlayLayer", "clear");
                th.printStackTrace();
                Log.d("amapApi", "GLOverlayLayer clear erro" + th.getMessage());
            }
        } else {
            this.d.clear();
            a();
        }
    }

    public synchronized void a() {
        this.c = 0;
    }

    public synchronized void b() {
        try {
            Iterator<ch> it = this.d.iterator();
            while (it.hasNext()) {
                it.next().destroy();
            }
            b(null);
        } catch (Throwable th) {
            fn.b(th, "GLOverlayLayer", "destory");
            th.printStackTrace();
            Log.d("amapApi", "GLOverlayLayer destory erro" + th.getMessage());
        }
    }

    synchronized ch c(String str) throws RemoteException {
        for (ch chVar : this.d) {
            if (chVar != null && chVar.getId().equals(str)) {
                return chVar;
            }
        }
        return null;
    }

    public synchronized ck a(PolylineOptions polylineOptions) throws RemoteException {
        if (polylineOptions == null) {
            return null;
        }
        cr crVar = new cr(this, polylineOptions);
        a(crVar);
        return crVar;
    }

    public synchronized cg a(NavigateArrowOptions navigateArrowOptions) throws RemoteException {
        if (navigateArrowOptions == null) {
            return null;
        }
        cp cpVar = new cp(this.a);
        cpVar.setTopColor(navigateArrowOptions.getTopColor());
        cpVar.setPoints(navigateArrowOptions.getPoints());
        cpVar.setVisible(navigateArrowOptions.isVisible());
        cpVar.setWidth(navigateArrowOptions.getWidth());
        cpVar.setZIndex(navigateArrowOptions.getZIndex());
        a(cpVar);
        return cpVar;
    }

    public synchronized cj a(PolygonOptions polygonOptions) throws RemoteException {
        if (polygonOptions == null) {
            return null;
        }
        cq cqVar = new cq(this.a);
        cqVar.setFillColor(polygonOptions.getFillColor());
        cqVar.setPoints(polygonOptions.getPoints());
        cqVar.setHoleOptions(polygonOptions.getHoleOptions());
        cqVar.setVisible(polygonOptions.isVisible());
        cqVar.setStrokeWidth(polygonOptions.getStrokeWidth());
        cqVar.setZIndex(polygonOptions.getZIndex());
        cqVar.setStrokeColor(polygonOptions.getStrokeColor());
        a(cqVar);
        return cqVar;
    }

    public synchronized cd a(CircleOptions circleOptions) throws RemoteException {
        if (circleOptions == null) {
            return null;
        }
        bz bzVar = new bz(this.a);
        bzVar.setFillColor(circleOptions.getFillColor());
        bzVar.setCenter(circleOptions.getCenter());
        bzVar.setVisible(circleOptions.isVisible());
        bzVar.setHoleOptions(circleOptions.getHoleOptions());
        bzVar.setStrokeWidth(circleOptions.getStrokeWidth());
        bzVar.setZIndex(circleOptions.getZIndex());
        bzVar.setStrokeColor(circleOptions.getStrokeColor());
        bzVar.setRadius(circleOptions.getRadius());
        a(bzVar);
        return bzVar;
    }

    public synchronized cc a(ArcOptions arcOptions) throws RemoteException {
        if (arcOptions == null) {
            return null;
        }
        by byVar = new by(this.a);
        byVar.setStrokeColor(arcOptions.getStrokeColor());
        byVar.a(arcOptions.getStart());
        byVar.b(arcOptions.getPassed());
        byVar.c(arcOptions.getEnd());
        byVar.setVisible(arcOptions.isVisible());
        byVar.setStrokeWidth(arcOptions.getStrokeWidth());
        byVar.setZIndex(arcOptions.getZIndex());
        a(byVar);
        return byVar;
    }

    public synchronized ce a(GroundOverlayOptions groundOverlayOptions) throws RemoteException {
        if (groundOverlayOptions == null) {
            return null;
        }
        cb cbVar = new cb(this.a);
        cbVar.a(groundOverlayOptions.getAnchorU(), groundOverlayOptions.getAnchorV());
        cbVar.setDimensions(groundOverlayOptions.getWidth(), groundOverlayOptions.getHeight());
        cbVar.setImage(groundOverlayOptions.getImage());
        cbVar.setPosition(groundOverlayOptions.getLocation());
        cbVar.setPositionFromBounds(groundOverlayOptions.getBounds());
        cbVar.setBearing(groundOverlayOptions.getBearing());
        cbVar.setTransparency(groundOverlayOptions.getTransparency());
        cbVar.setVisible(groundOverlayOptions.isVisible());
        cbVar.setZIndex(groundOverlayOptions.getZIndex());
        a(cbVar);
        return cbVar;
    }

    private void a(ch chVar) throws RemoteException {
        this.d.add(chVar);
        c();
    }

    public synchronized boolean d(String str) throws RemoteException {
        ch chVarC = c(str);
        if (chVarC == null) {
            return false;
        }
        return this.d.remove(chVarC);
    }

    public synchronized void c() {
        this.f.removeCallbacks(this.g);
        this.f.postDelayed(this.g, 10L);
    }

    public synchronized void a(boolean z, int i) {
        try {
            Iterator<Integer> it = this.e.iterator();
            while (it.hasNext()) {
                GLES20.glDeleteTextures(1, new int[]{it.next().intValue()}, 0);
            }
            this.e.clear();
            int size = this.d.size();
            for (ch chVar : this.d) {
                if (chVar.isVisible()) {
                    if (size > 20) {
                        if (chVar.a()) {
                            if (z) {
                                if (chVar.getZIndex() <= i) {
                                    chVar.c();
                                }
                            } else if (chVar.getZIndex() > i) {
                                chVar.c();
                            }
                        }
                    } else if (z) {
                        if (chVar.getZIndex() <= i) {
                            chVar.c();
                        }
                    } else if (chVar.getZIndex() > i) {
                        chVar.c();
                    }
                }
            }
        } catch (Throwable th) {
            fn.b(th, "GLOverlayLayer", "draw");
            th.printStackTrace();
        }
    }

    public void a(Integer num) {
        if (num.intValue() != 0) {
            this.e.add(num);
        }
    }

    public synchronized ch a(LatLng latLng) {
        for (ch chVar : this.d) {
            if (chVar != null && chVar.d() && (chVar instanceof ck) && ((ck) chVar).a(latLng)) {
                return chVar;
            }
        }
        return null;
    }

    public k d() {
        return this.a;
    }

    public float[] e() {
        k kVar = this.a;
        return kVar != null ? kVar.v() : new float[16];
    }
}
