package com.amap.api.mapcore.util;

import android.graphics.Color;
import android.graphics.Rect;
import android.opengl.GLES20;
import android.os.RemoteException;
import com.amap.api.maps.model.BaseHoleOptions;
import com.amap.api.maps.model.CircleHoleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.PolygonHoleOptions;
import com.autonavi.ae.gmap.GLMapState;
import com.autonavi.amap.mapcore.FPoint;
import com.autonavi.amap.mapcore.FPointBounds;
import com.autonavi.amap.mapcore.IPoint;
import com.autonavi.amap.mapcore.MapConfig;
import com.autonavi.amap.mapcore.Rectangle;
import com.autonavi.amap.mapcore.interfaces.IOverlay;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

/* JADX INFO: compiled from: PolygonDelegateImp.java */
/* JADX INFO: loaded from: classes.dex */
public class cq implements cj {
    private static int A = 20;
    private static double F = 1.0E10d;
    private static float y = 4.0075016E7f;
    private static int z = 256;
    private int B;
    private int C;
    private FloatBuffer D;
    private FloatBuffer E;
    private k c;
    private String f;
    private float g;
    private int h;
    private int i;
    private List<LatLng> j;
    private FloatBuffer n;
    private FloatBuffer o;
    private cw.d x;
    private float d = 0.0f;
    private boolean e = true;
    private List<IPoint> k = new Vector();
    private List<BaseHoleOptions> l = new Vector();
    private List<FPoint> m = new ArrayList();
    private int p = 0;
    private int q = 0;
    private LatLngBounds r = null;
    private boolean s = false;
    private float t = 0.0f;
    private List<IPoint> u = new ArrayList();
    private Object v = new Object();
    FPointBounds a = null;
    Rect b = null;
    private float w = 0.0f;

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public boolean isAboveMaskLayer() {
        return false;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void setAboveMaskLayer(boolean z2) {
    }

    public cq(k kVar) {
        this.c = kVar;
        try {
            this.f = getId();
        } catch (RemoteException e) {
            fn.b(e, "PolygonDelegateImp", "create");
            e.printStackTrace();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void remove() throws RemoteException {
        this.c.a(getId());
        this.c.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public String getId() throws RemoteException {
        if (this.f == null) {
            this.f = this.c.c("Polygon");
        }
        return this.f;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolygon
    public void setPoints(List<LatLng> list) throws RemoteException {
        synchronized (this.v) {
            this.j = list;
            a(list);
            this.c.setRunLowFrame(false);
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolygon
    public List<LatLng> getPoints() throws RemoteException {
        return this.j;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void setZIndex(float f) throws RemoteException {
        this.d = f;
        this.c.f();
        this.c.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public float getZIndex() throws RemoteException {
        return this.d;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void setVisible(boolean z2) throws RemoteException {
        this.e = z2;
        this.c.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public boolean isVisible() throws RemoteException {
        return this.e;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public boolean equalsRemote(IOverlay iOverlay) throws RemoteException {
        return equals(iOverlay) || iOverlay.getId().equals(getId());
    }

    void a(List<LatLng> list) throws RemoteException {
        LatLngBounds.Builder builder = LatLngBounds.builder();
        if (this.b == null) {
            this.b = new Rect();
        }
        dw.a(this.b);
        this.k.clear();
        if (list != null) {
            Object obj = null;
            for (LatLng latLng : list) {
                if (!latLng.equals(obj)) {
                    IPoint iPointObtain = IPoint.obtain();
                    this.c.a(latLng.latitude, latLng.longitude, iPointObtain);
                    this.k.add(iPointObtain);
                    dw.b(this.b, iPointObtain.x, iPointObtain.y);
                    builder.include(latLng);
                    obj = latLng;
                }
            }
            int size = this.k.size();
            if (size > 1) {
                IPoint iPoint = this.k.get(0);
                int i = size - 1;
                IPoint iPoint2 = this.k.get(i);
                if (iPoint.x == iPoint2.x && iPoint.y == iPoint2.y) {
                    this.k.remove(i);
                }
            }
        }
        this.b.sort();
        this.r = builder.build();
        FloatBuffer floatBuffer = this.n;
        if (floatBuffer != null) {
            floatBuffer.clear();
        }
        FloatBuffer floatBuffer2 = this.o;
        if (floatBuffer2 != null) {
            floatBuffer2.clear();
        }
        List<IPoint> list2 = this.k;
        if (dw.a(list2, 0, list2.size())) {
            Collections.reverse(this.k);
        }
        this.p = 0;
        this.q = 0;
        this.c.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public int hashCodeRemote() throws RemoteException {
        return super.hashCode();
    }

    @Override // com.amap.api.mapcore.util.ch
    public boolean a() {
        return this.c.getMapConfig().getGeoRectangle().isOverlap(this.b);
    }

    private void b() {
        k kVar = this.c;
        if (kVar != null) {
            this.x = (cw.d) kVar.s(3);
        }
    }

    @Override // com.amap.api.mapcore.util.ch
    public void c() throws RemoteException {
        List<IPoint> list = this.k;
        if (list == null || list.size() == 0) {
            return;
        }
        MapConfig mapConfig = this.c.getMapConfig();
        Rectangle geoRectangle = mapConfig.getGeoRectangle();
        IPoint[] clipRect = geoRectangle.getClipRect();
        List<IPoint> listA = this.k;
        if (a(geoRectangle)) {
            synchronized (this.v) {
                listA = dw.a(clipRect, this.k, true);
            }
        }
        e();
        if (listA.size() > 2) {
            b(listA, mapConfig.getS_x(), mapConfig.getS_y());
            if (this.n != null && this.o != null && this.p > 0 && this.q > 0) {
                cw.d dVar = this.x;
                if (dVar == null || dVar.c()) {
                    b();
                }
                di.a(this.x, this.h, this.i, this.n, this.g, this.o, this.p, this.q, this.c.v());
            }
        }
        this.s = true;
    }

    private void e() throws RemoteException {
        MapConfig mapConfig = this.c.getMapConfig();
        List<BaseHoleOptions> list = this.l;
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
        for (int i = 0; i < this.l.size(); i++) {
            BaseHoleOptions baseHoleOptions = this.l.get(i);
            boolean z2 = baseHoleOptions instanceof PolygonHoleOptions;
            if (z2) {
                a(b(((PolygonHoleOptions) baseHoleOptions).getPoints()), mapConfig.getS_x(), mapConfig.getS_y());
            } else if (baseHoleOptions instanceof CircleHoleOptions) {
                this.c.f();
                a((CircleHoleOptions) baseHoleOptions);
            }
            if (this.D != null && this.B > 0) {
                cw.d dVar = this.x;
                if (dVar == null || dVar.c()) {
                    b();
                }
                if (z2) {
                    di.a(this.x, -1, this.i, this.D, this.g, this.E, this.B, this.C, this.c.v());
                } else if (baseHoleOptions instanceof CircleHoleOptions) {
                    di.a(this.x, Color.argb(200, 80, 1, 1), Color.argb(200, 1, 1, 1), this.D, 10.0f, this.B, this.c.v(), 0, 0);
                }
            }
        }
        GLES20.glColorMask(true, true, true, true);
        GLES20.glStencilFunc(517, 1, 255);
        GLES20.glStencilMask(0);
    }

    private float a(double d) {
        return (float) ((Math.cos((d * 3.141592653589793d) / 180.0d) * ((double) y)) / ((double) (z << A)));
    }

    private double b(double d) {
        return 1.0d / ((double) a(d));
    }

    public void a(CircleHoleOptions circleHoleOptions) throws RemoteException {
        if (circleHoleOptions.getCenter() != null) {
            IPoint iPointObtain = IPoint.obtain();
            FPoint fPointObtain = FPoint.obtain();
            GLMapState.lonlat2Geo(circleHoleOptions.getCenter().longitude, circleHoleOptions.getCenter().latitude, iPointObtain);
            float[] fArr = new float[1086];
            double dB = b(circleHoleOptions.getCenter().latitude) * circleHoleOptions.getRadius();
            GLMapState gLMapStateB = this.c.b();
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
                fPointObtain.x = i2 - this.c.getMapConfig().getS_x();
                fPointObtain.y = i3 - this.c.getMapConfig().getS_y();
                i++;
                int i4 = i * 3;
                fArr[i4] = fPointObtain.x;
                fArr[i4 + 1] = fPointObtain.y;
                fArr[i4 + 2] = 0.0f;
            }
            this.B = 362;
            this.D = dw.a(fArr);
            iPointObtain.recycle();
            fPointObtain.recycle();
        }
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
            if (F == 1.0E10d) {
                F = 1.0E8d;
            } else {
                F = 1.0E10d;
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
        this.B = size;
        this.C = iPointArrA.length;
        this.D = dw.a(fArr);
        this.E = dw.a(fArr2);
    }

    private boolean a(Rectangle rectangle) {
        this.w = this.c.g();
        f();
        if (this.w > 10.0f && rectangle != null) {
            try {
                return !rectangle.contains(this.b);
            } catch (Throwable unused) {
            }
        }
        return false;
    }

    private void b(List<IPoint> list, int i, int i2) throws RemoteException {
        int i3;
        f();
        ArrayList<IPoint> arrayList = new ArrayList();
        int size = list.size();
        if (size < 2) {
            return;
        }
        IPoint iPoint = list.get(0);
        arrayList.add(iPoint);
        int i4 = 1;
        while (true) {
            i3 = size - 1;
            if (i4 >= i3) {
                break;
            }
            IPoint iPoint2 = list.get(i4);
            if (a(iPoint, iPoint2)) {
                arrayList.add(iPoint2);
                iPoint = iPoint2;
            }
            i4++;
        }
        arrayList.add(list.get(i3));
        float[] fArr = new float[arrayList.size() * 3];
        int size2 = arrayList.size();
        IPoint[] iPointArr = new IPoint[size2];
        int i5 = 0;
        for (IPoint iPoint3 : arrayList) {
            int i6 = i5 * 3;
            fArr[i6] = iPoint3.x - i;
            fArr[i6 + 1] = iPoint3.y - i2;
            fArr[i6 + 2] = 0.0f;
            iPointArr[i5] = iPoint3;
            i5++;
        }
        IPoint[] iPointArrA = a(iPointArr);
        if (iPointArrA.length == 0) {
            if (F == 1.0E10d) {
                F = 1.0E8d;
            } else {
                F = 1.0E10d;
            }
            iPointArrA = a(iPointArr);
        }
        float[] fArr2 = new float[iPointArrA.length * 3];
        int i7 = 0;
        for (IPoint iPoint4 : iPointArrA) {
            int i8 = i7 * 3;
            fArr2[i8] = iPoint4.x - i;
            fArr2[i8 + 1] = iPoint4.y - i2;
            fArr2[i8 + 2] = 0.0f;
            i7++;
        }
        this.p = size2;
        this.q = iPointArrA.length;
        this.n = dw.a(fArr);
        this.o = dw.a(fArr2);
    }

    private List<IPoint> b(List<LatLng> list) throws RemoteException {
        ArrayList arrayList = new ArrayList();
        if (list != null) {
            Object obj = null;
            for (LatLng latLng : list) {
                if (!latLng.equals(obj)) {
                    IPoint iPointObtain = IPoint.obtain();
                    this.c.a(latLng.latitude, latLng.longitude, iPointObtain);
                    arrayList.add(iPointObtain);
                    dw.b(this.b, iPointObtain.x, iPointObtain.y);
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

    private boolean a(IPoint iPoint, IPoint iPoint2) {
        return ((float) (iPoint2.x - iPoint.x)) >= this.t || ((float) (iPoint2.x - iPoint.x)) <= (-this.t) || ((float) (iPoint2.y - iPoint.y)) >= this.t || ((float) (iPoint2.y - iPoint.y)) <= (-this.t);
    }

    private void f() {
        float fG = this.c.g();
        if (this.k.size() > 5000 && fG <= 12.0f) {
            float f = (this.g / 2.0f) + (fG / 2.0f);
            if (f > 200.0f) {
                f = 200.0f;
            }
            this.t = this.c.b().getMapLenWithWin((int) f);
            return;
        }
        this.t = this.c.b().getMapLenWithWin(10);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolygon
    public void setStrokeWidth(float f) throws RemoteException {
        this.g = f;
        this.c.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolygon
    public float getStrokeWidth() throws RemoteException {
        return this.g;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolygon
    public void setFillColor(int i) throws RemoteException {
        this.h = i;
        this.c.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolygon
    public int getFillColor() throws RemoteException {
        return this.h;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolygon
    public void setStrokeColor(int i) throws RemoteException {
        this.i = i;
        this.c.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolygon
    public int getStrokeColor() throws RemoteException {
        return this.i;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolygon
    public void setHoleOptions(List<BaseHoleOptions> list) {
        this.l = list;
        this.c.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolygon
    public List<BaseHoleOptions> getHoleOptions() {
        return this.l;
    }

    static IPoint[] a(IPoint[] iPointArr) {
        int length = iPointArr.length;
        double[] dArr = new double[length * 2];
        for (int i = 0; i < length; i++) {
            int i2 = i * 2;
            dArr[i2] = ((double) iPointArr[i].x) * F;
            dArr[i2 + 1] = ((double) iPointArr[i].y) * F;
        }
        du duVarA = new dh().a(dArr);
        int i3 = duVarA.b;
        IPoint[] iPointArr2 = new IPoint[i3];
        for (int i4 = 0; i4 < i3; i4++) {
            IPoint iPoint = new IPoint();
            iPointArr2[i4] = iPoint;
            iPoint.x = (int) (dArr[duVarA.a(i4) * 2] / F);
            iPointArr2[i4].y = (int) (dArr[(duVarA.a(i4) * 2) + 1] / F);
        }
        return iPointArr2;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void destroy() {
        try {
            FloatBuffer floatBuffer = this.n;
            if (floatBuffer != null) {
                floatBuffer.clear();
                this.n = null;
            }
            if (this.o != null) {
                this.o = null;
            }
            FloatBuffer floatBuffer2 = this.D;
            if (floatBuffer2 != null) {
                floatBuffer2.clear();
                this.D = null;
            }
            FloatBuffer floatBuffer3 = this.E;
            if (floatBuffer3 != null) {
                floatBuffer3.clear();
                this.E = null;
            }
        } catch (Throwable th) {
            fn.b(th, "PolygonDelegateImp", "destroy");
            th.printStackTrace();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolygon
    public boolean contains(LatLng latLng) throws RemoteException {
        try {
            return dw.a(latLng, getPoints());
        } catch (Throwable th) {
            fn.b(th, "PolygonDelegateImp", "contains");
            th.printStackTrace();
            return false;
        }
    }

    @Override // com.amap.api.mapcore.util.ch
    public boolean d() {
        return this.s;
    }
}
