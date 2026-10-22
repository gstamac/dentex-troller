package com.amap.api.mapcore.util;

import android.opengl.GLES20;
import android.os.RemoteException;
import android.util.Log;
import androidx.core.view.ViewCompat;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.BaseHoleOptions;
import com.amap.api.maps.model.CircleHoleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.PolygonHoleOptions;
import com.autonavi.ae.gmap.GLMapState;
import com.autonavi.amap.mapcore.FPoint;
import com.autonavi.amap.mapcore.IPoint;
import com.autonavi.amap.mapcore.MapConfig;
import com.autonavi.amap.mapcore.interfaces.IOverlay;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: compiled from: CircleDelegateImp.java */
/* JADX INFO: loaded from: classes.dex */
public class bz implements cd {
    private static float u = 4.0075016E7f;
    private static int v = 256;
    private static int w = 20;
    private static double x = 1.0E10d;
    private String h;
    private k i;
    private FloatBuffer j;
    private List<BaseHoleOptions> o;
    private int p;
    private int q;
    private FloatBuffer r;
    private FloatBuffer s;
    private cw.d t;
    private LatLng a = null;
    private double b = 0.0d;
    private float c = 10.0f;
    private int d = ViewCompat.MEASURED_STATE_MASK;
    private int e = 0;
    private float f = 0.0f;
    private boolean g = true;
    private int k = 0;
    private boolean l = false;
    private IPoint m = IPoint.obtain();
    private FPoint n = FPoint.obtain();

    @Override // com.amap.api.mapcore.util.ch
    public boolean a() {
        return true;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public int hashCodeRemote() throws RemoteException {
        return 0;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public boolean isAboveMaskLayer() {
        return false;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void setAboveMaskLayer(boolean z) {
    }

    public bz(k kVar) {
        this.i = kVar;
        try {
            this.h = getId();
        } catch (RemoteException e) {
            fn.b(e, "CircleDelegateImp", "create");
            e.printStackTrace();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void remove() throws RemoteException {
        this.i.a(getId());
        this.i.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public String getId() throws RemoteException {
        if (this.h == null) {
            this.h = this.i.c("Circle");
        }
        return this.h;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void setZIndex(float f) throws RemoteException {
        this.f = f;
        this.i.f();
        this.i.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public float getZIndex() throws RemoteException {
        return this.f;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void setVisible(boolean z) throws RemoteException {
        this.g = z;
        this.i.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public boolean isVisible() throws RemoteException {
        return this.g;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public boolean equalsRemote(IOverlay iOverlay) throws RemoteException {
        return equals(iOverlay) || iOverlay.getId().equals(getId());
    }

    public boolean b() throws RemoteException {
        int i = 0;
        this.l = false;
        LatLng latLng = this.a;
        if (latLng != null) {
            float[] fArr = new float[1086];
            double dB = b(latLng.latitude) * this.b;
            GLMapState gLMapStateB = this.i.b();
            gLMapStateB.geo2Map(this.m.x, this.m.y, this.n);
            fArr[0] = this.n.x;
            fArr[1] = this.n.y;
            fArr[2] = 0.0f;
            while (i < 361) {
                double d = (((double) i) * 3.141592653589793d) / 180.0d;
                double dSin = Math.sin(d) * dB;
                double dCos = Math.cos(d) * dB;
                int i2 = (int) (((double) this.m.x) + dSin);
                int i3 = (int) (((double) this.m.y) + dCos);
                gLMapStateB.geo2Map(i2, i3, this.n);
                this.n.x = i2 - this.i.getMapConfig().getS_x();
                this.n.y = i3 - this.i.getMapConfig().getS_y();
                i++;
                int i4 = i * 3;
                fArr[i4] = this.n.x;
                fArr[i4 + 1] = this.n.y;
                fArr[i4 + 2] = 0.0f;
            }
            this.k = 362;
            this.j = dw.a(fArr);
        }
        return true;
    }

    private void f() {
        k kVar = this.i;
        if (kVar != null) {
            this.t = (cw.d) kVar.s(3);
        }
    }

    @Override // com.amap.api.mapcore.util.ch
    public void c() throws RemoteException {
        if (this.a == null || this.b <= 0.0d || !this.g) {
            return;
        }
        b();
        g();
        if (this.j != null && this.k > 0) {
            cw.d dVar = this.t;
            if (dVar == null || dVar.c()) {
                f();
            }
            di.a(this.t, this.e, this.d, this.j, this.c, this.k, this.i.v(), 0, 0);
        }
        this.l = true;
    }

    void e() {
        this.k = 0;
        FloatBuffer floatBuffer = this.j;
        if (floatBuffer != null) {
            floatBuffer.clear();
        }
        this.i.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.ICircle
    public void setCenter(LatLng latLng) throws RemoteException {
        if (latLng != null) {
            this.a = latLng;
            GLMapState.lonlat2Geo(latLng.longitude, latLng.latitude, this.m);
            e();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.ICircle
    public LatLng getCenter() throws RemoteException {
        return this.a;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.ICircle
    public void setRadius(double d) throws RemoteException {
        this.b = d;
        e();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.ICircle
    public double getRadius() throws RemoteException {
        return this.b;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.ICircle
    public void setStrokeWidth(float f) throws RemoteException {
        this.c = f;
        this.i.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.ICircle
    public float getStrokeWidth() throws RemoteException {
        return this.c;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.ICircle
    public void setStrokeColor(int i) throws RemoteException {
        this.d = i;
        this.i.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.ICircle
    public int getStrokeColor() throws RemoteException {
        return this.d;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.ICircle
    public void setFillColor(int i) throws RemoteException {
        this.e = i;
        this.i.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.ICircle
    public int getFillColor() throws RemoteException {
        return this.e;
    }

    private float a(double d) {
        return (float) ((Math.cos((d * 3.141592653589793d) / 180.0d) * ((double) u)) / ((double) (v << w)));
    }

    private double b(double d) {
        return 1.0d / ((double) a(d));
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void destroy() {
        try {
            this.a = null;
            FloatBuffer floatBuffer = this.j;
            if (floatBuffer != null) {
                floatBuffer.clear();
                this.j = null;
            }
            FloatBuffer floatBuffer2 = this.r;
            if (floatBuffer2 != null) {
                floatBuffer2.clear();
                this.r = null;
            }
            FloatBuffer floatBuffer3 = this.s;
            if (floatBuffer3 != null) {
                floatBuffer3.clear();
                this.s = null;
            }
        } catch (Throwable th) {
            fn.b(th, "CircleDelegateImp", "destroy");
            th.printStackTrace();
            Log.d("destroy erro", "CircleDelegateImp destroy");
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.ICircle
    public boolean contains(LatLng latLng) throws RemoteException {
        return this.b >= ((double) AMapUtils.calculateLineDistance(this.a, latLng));
    }

    @Override // com.amap.api.mapcore.util.ch
    public boolean d() {
        return this.l;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.ICircle
    public void setHoleOptions(List<BaseHoleOptions> list) {
        this.o = list;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.ICircle
    public List<BaseHoleOptions> getHoleOptions() throws RemoteException {
        return this.o;
    }

    private void g() throws RemoteException {
        MapConfig mapConfig = this.i.getMapConfig();
        List<BaseHoleOptions> list = this.o;
        if (list == null || list.size() <= 0) {
            return;
        }
        GLES20.glClearStencil(0);
        GLES20.glStencilMask(255);
        GLES20.glClear(1024);
        GLES20.glFlush();
        GLES20.glEnable(2960);
        GLES20.glColorMask(false, false, false, false);
        GLES20.glStencilFunc(512, 1, 255);
        GLES20.glStencilOp(7681, 7680, 7680);
        for (int i = 0; i < this.o.size(); i++) {
            BaseHoleOptions baseHoleOptions = this.o.get(i);
            boolean z = baseHoleOptions instanceof PolygonHoleOptions;
            if (z) {
                a(a(((PolygonHoleOptions) baseHoleOptions).getPoints()), mapConfig.getS_x(), mapConfig.getS_y());
            } else if (baseHoleOptions instanceof CircleHoleOptions) {
                this.i.f();
                a((CircleHoleOptions) baseHoleOptions);
            }
            if (this.r != null && this.p > 0) {
                cw.d dVar = this.t;
                if (dVar == null || dVar.c()) {
                    f();
                }
                if (z) {
                    di.a(this.t, -1, this.e, this.r, getStrokeWidth(), this.s, this.p, this.q, this.i.v());
                } else if (baseHoleOptions instanceof CircleHoleOptions) {
                    di.a(this.t, -1, -1, this.r, 10.0f, this.p, this.i.v(), 0, 0);
                }
            }
        }
        GLES20.glColorMask(true, true, true, true);
        GLES20.glStencilFunc(517, 1, 255);
        GLES20.glStencilMask(0);
    }

    static IPoint[] a(IPoint[] iPointArr) {
        int length = iPointArr.length;
        double[] dArr = new double[length * 2];
        for (int i = 0; i < length; i++) {
            int i2 = i * 2;
            dArr[i2] = ((double) iPointArr[i].x) * x;
            dArr[i2 + 1] = ((double) iPointArr[i].y) * x;
        }
        du duVarA = new dh().a(dArr);
        int i3 = duVarA.b;
        IPoint[] iPointArr2 = new IPoint[i3];
        for (int i4 = 0; i4 < i3; i4++) {
            IPoint iPoint = new IPoint();
            iPointArr2[i4] = iPoint;
            iPoint.x = (int) (dArr[duVarA.a(i4) * 2] / x);
            iPointArr2[i4].y = (int) (dArr[(duVarA.a(i4) * 2) + 1] / x);
        }
        return iPointArr2;
    }

    private void a(List<IPoint> list, int i, int i2) throws RemoteException {
        if (list.size() < 2) {
            return;
        }
        float[] fArr = new float[list.size() * 3];
        int size = list.size();
        IPoint[] iPointArr = new IPoint[size];
        int i3 = 0;
        for (IPoint iPoint : list) {
            int i4 = i3 * 3;
            fArr[i4] = iPoint.x - i;
            fArr[i4 + 1] = iPoint.y - i2;
            fArr[i4 + 2] = 0.0f;
            iPointArr[i3] = iPoint;
            i3++;
        }
        IPoint[] iPointArrA = a(iPointArr);
        if (iPointArrA.length == 0) {
            if (x == 1.0E10d) {
                x = 1.0E8d;
            } else {
                x = 1.0E10d;
            }
            iPointArrA = a(iPointArr);
        }
        float[] fArr2 = new float[iPointArrA.length * 3];
        int i5 = 0;
        for (IPoint iPoint2 : iPointArrA) {
            int i6 = i5 * 3;
            fArr2[i6] = iPoint2.x - i;
            fArr2[i6 + 1] = iPoint2.y - i2;
            fArr2[i6 + 2] = 0.0f;
            i5++;
        }
        this.p = size;
        this.q = iPointArrA.length;
        this.r = dw.a(fArr);
        this.s = dw.a(fArr2);
    }

    private List<IPoint> a(List<LatLng> list) throws RemoteException {
        ArrayList arrayList = new ArrayList();
        if (list != null) {
            Object obj = null;
            for (LatLng latLng : list) {
                if (!latLng.equals(obj)) {
                    IPoint iPointObtain = IPoint.obtain();
                    this.i.a(latLng.latitude, latLng.longitude, iPointObtain);
                    arrayList.add(iPointObtain);
                    obj = latLng;
                }
            }
            int size = arrayList.size();
            if (size > 1) {
                IPoint iPoint = (IPoint) arrayList.get(0);
                int i = size - 1;
                IPoint iPoint2 = (IPoint) arrayList.get(i);
                if (iPoint.x == iPoint2.x && iPoint.y == iPoint2.y) {
                    arrayList.remove(i);
                }
            }
        }
        if (dw.a(arrayList, 0, arrayList.size())) {
            Collections.reverse(arrayList);
        }
        return arrayList;
    }

    public void a(CircleHoleOptions circleHoleOptions) throws RemoteException {
        if (circleHoleOptions.getCenter() != null) {
            IPoint iPointObtain = IPoint.obtain();
            FPoint fPointObtain = FPoint.obtain();
            GLMapState.lonlat2Geo(circleHoleOptions.getCenter().longitude, circleHoleOptions.getCenter().latitude, iPointObtain);
            float[] fArr = new float[1086];
            double dB = b(circleHoleOptions.getCenter().latitude) * circleHoleOptions.getRadius();
            GLMapState gLMapStateB = this.i.b();
            gLMapStateB.geo2Map(iPointObtain.x, iPointObtain.y, fPointObtain);
            int i = 0;
            fArr[0] = fPointObtain.x;
            fArr[1] = fPointObtain.y;
            fArr[2] = 0.0f;
            while (i < 361) {
                double d = (((double) i) * 3.141592653589793d) / 180.0d;
                double dSin = Math.sin(d) * dB;
                double dCos = Math.cos(d) * dB;
                int i2 = (int) (((double) iPointObtain.x) + dSin);
                int i3 = (int) (((double) iPointObtain.y) + dCos);
                gLMapStateB.geo2Map(i2, i3, fPointObtain);
                fPointObtain.x = i2 - this.i.getMapConfig().getS_x();
                fPointObtain.y = i3 - this.i.getMapConfig().getS_y();
                i++;
                int i4 = i * 3;
                fArr[i4] = fPointObtain.x;
                fArr[i4 + 1] = fPointObtain.y;
                fArr[i4 + 2] = 0.0f;
            }
            this.p = 362;
            this.r = dw.a(fArr);
            iPointObtain.recycle();
            fPointObtain.recycle();
        }
    }
}
