package com.amap.api.mapcore.util;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.RemoteException;
import android.util.Log;
import androidx.core.view.ViewCompat;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.autonavi.amap.mapcore.AMapNativeRenderer;
import com.autonavi.amap.mapcore.DPoint;
import com.autonavi.amap.mapcore.IPoint;
import com.autonavi.amap.mapcore.MapConfig;
import com.autonavi.amap.mapcore.Rectangle;
import com.autonavi.amap.mapcore.interfaces.IOverlay;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/* JADX INFO: compiled from: NavigateArrowDelegateImp.java */
/* JADX INFO: loaded from: classes.dex */
public class cp implements cg {
    float a;
    float b;
    float c;
    float d;
    float e;
    float f;
    float g;
    float h;
    float[] j;
    private k l;
    private String r;
    private float m = 10.0f;
    private int n = ViewCompat.MEASURED_STATE_MASK;
    private int o = ViewCompat.MEASURED_STATE_MASK;
    private float p = 0.0f;
    private boolean q = true;
    private List<IPoint> s = new Vector();
    private int t = 0;
    private boolean u = false;
    private Object v = new Object();
    private LatLngBounds w = null;
    Rect i = null;
    int k = 0;

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public boolean isAboveMaskLayer() {
        return false;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void setAboveMaskLayer(boolean z) {
    }

    public cp(k kVar) {
        this.l = kVar;
        try {
            this.r = getId();
        } catch (RemoteException e) {
            fn.b(e, "NavigateArrowDelegateImp", "create");
            e.printStackTrace();
        }
    }

    void a(List<LatLng> list) throws RemoteException {
        synchronized (this.v) {
            this.s.clear();
            if (this.i == null) {
                this.i = new Rect();
            }
            dw.a(this.i);
            if (list != null) {
                Object obj = null;
                for (LatLng latLng : list) {
                    if (latLng != null && !latLng.equals(obj)) {
                        IPoint iPointObtain = IPoint.obtain();
                        this.l.a(latLng.latitude, latLng.longitude, iPointObtain);
                        this.s.add(iPointObtain);
                        dw.b(this.i, iPointObtain.x, iPointObtain.y);
                        obj = latLng;
                    }
                }
            }
            this.t = 0;
            this.i.sort();
        }
        this.l.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void remove() throws RemoteException {
        this.l.a(getId());
        this.l.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public String getId() throws RemoteException {
        if (this.r == null) {
            this.r = this.l.c("NavigateArrow");
        }
        return this.r;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.INavigateArrow
    public void setPoints(List<LatLng> list) throws RemoteException {
        a(list);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.INavigateArrow
    public List<LatLng> getPoints() throws RemoteException {
        return b();
    }

    private List<LatLng> b() throws RemoteException {
        ArrayList arrayList;
        if (this.s == null) {
            return null;
        }
        synchronized (this.v) {
            arrayList = new ArrayList();
            for (IPoint iPoint : this.s) {
                if (iPoint != null) {
                    DPoint dPointObtain = DPoint.obtain();
                    this.l.a(iPoint.x, iPoint.y, dPointObtain);
                    arrayList.add(new LatLng(dPointObtain.y, dPointObtain.x));
                    dPointObtain.recycle();
                }
            }
        }
        return arrayList;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.INavigateArrow
    public void setWidth(float f) throws RemoteException {
        this.m = f;
        this.l.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.INavigateArrow
    public float getWidth() throws RemoteException {
        return this.m;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.INavigateArrow
    public void setTopColor(int i) throws RemoteException {
        this.n = i;
        this.a = Color.alpha(i) / 255.0f;
        this.b = Color.red(i) / 255.0f;
        this.c = Color.green(i) / 255.0f;
        this.d = Color.blue(i) / 255.0f;
        this.l.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.INavigateArrow
    public int getTopColor() throws RemoteException {
        return this.n;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.INavigateArrow
    public void setSideColor(int i) throws RemoteException {
        this.o = i;
        this.e = Color.alpha(i) / 255.0f;
        this.f = Color.red(i) / 255.0f;
        this.g = Color.green(i) / 255.0f;
        this.h = Color.blue(i) / 255.0f;
        this.l.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.INavigateArrow
    public int getSideColor() throws RemoteException {
        return this.o;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void setZIndex(float f) throws RemoteException {
        this.p = f;
        this.l.f();
        this.l.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public float getZIndex() throws RemoteException {
        return this.p;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void setVisible(boolean z) throws RemoteException {
        this.q = z;
        this.l.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public boolean isVisible() throws RemoteException {
        return this.q;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public boolean equalsRemote(IOverlay iOverlay) throws RemoteException {
        return equals(iOverlay) || iOverlay.getId().equals(getId());
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public int hashCodeRemote() throws RemoteException {
        return super.hashCode();
    }

    @Override // com.amap.api.mapcore.util.ch
    public boolean a() {
        Rectangle geoRectangle;
        return (this.i == null || (geoRectangle = this.l.getMapConfig().getGeoRectangle()) == null || !geoRectangle.isOverlap(this.i)) ? false : true;
    }

    public boolean a(MapConfig mapConfig) throws RemoteException {
        synchronized (this.v) {
            int s_x = mapConfig.getS_x();
            int s_y = mapConfig.getS_y();
            int i = 0;
            this.u = false;
            int size = this.s.size();
            float[] fArr = this.j;
            if (fArr == null || fArr.length < size * 3) {
                this.j = new float[size * 3];
            }
            this.k = size * 3;
            for (IPoint iPoint : this.s) {
                int i2 = i * 3;
                this.j[i2] = iPoint.x - s_x;
                this.j[i2 + 1] = iPoint.y - s_y;
                this.j[i2 + 2] = 0.0f;
                i++;
            }
            this.t = this.s.size();
        }
        return true;
    }

    @Override // com.amap.api.mapcore.util.ch
    public void c() throws RemoteException {
        List<IPoint> list = this.s;
        if (list == null || list.size() == 0 || this.m <= 0.0f) {
            return;
        }
        a(this.l.getMapConfig());
        if (this.j != null && this.t > 0) {
            AMapNativeRenderer.nativeDrawLineByTextureID(this.j, this.k, this.l.b().getMapLenWithWin((int) this.m), this.l.c(), this.b, this.c, this.d, this.a, 0.0f, false, true, true, this.l.v());
        }
        this.u = true;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void destroy() {
        try {
            if (this.j != null) {
                this.j = null;
            }
        } catch (Throwable th) {
            fn.b(th, "NavigateArrowDelegateImp", "destroy");
            th.printStackTrace();
            Log.d("destroy erro", "NavigateArrowDelegateImp destroy");
        }
    }

    @Override // com.amap.api.mapcore.util.ch
    public boolean d() {
        return this.u;
    }
}
