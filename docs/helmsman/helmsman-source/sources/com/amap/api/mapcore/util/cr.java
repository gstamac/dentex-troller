package com.amap.api.mapcore.util;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.opengl.GLES20;
import android.os.RemoteException;
import android.util.Log;
import androidx.core.view.ViewCompat;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.PolylineOptions;
import com.autonavi.amap.mapcore.AMapNativeRenderer;
import com.autonavi.amap.mapcore.FPoint;
import com.autonavi.amap.mapcore.FPoint3;
import com.autonavi.amap.mapcore.FPointBounds;
import com.autonavi.amap.mapcore.IPoint;
import com.autonavi.amap.mapcore.MapConfig;
import com.autonavi.amap.mapcore.Rectangle;
import com.autonavi.amap.mapcore.interfaces.IOverlay;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: compiled from: PolylineDelegateImp.java */
/* JADX INFO: loaded from: classes.dex */
public class cr implements ck {
    private float K;
    private float L;
    private float M;
    private float N;
    private float[] Q;
    private int[] R;
    private int[] S;
    private PolylineOptions X;
    private cw.d aa;
    private i f;
    private String g;
    private FloatBuffer q;
    private List<IPoint> h = new ArrayList();
    private List<FPoint> i = new ArrayList();
    private List<LatLng> j = new ArrayList();
    private List<BitmapDescriptor> k = new ArrayList();
    private List<r> l = new ArrayList();
    private List<Integer> m = new ArrayList();
    private List<Integer> n = new ArrayList();
    private List<Integer> o = new ArrayList();
    private List<Integer> p = new ArrayList();
    private BitmapDescriptor r = null;
    private LatLngBounds s = null;
    private Object t = new Object();
    private boolean u = true;
    private boolean v = true;
    private boolean w = false;
    private boolean x = false;
    private boolean y = false;
    private boolean z = true;
    private boolean A = false;
    private boolean B = false;
    private boolean C = true;
    private int D = 0;
    private int E = 0;
    private int F = ViewCompat.MEASURED_STATE_MASK;
    private int G = 0;
    private float H = 10.0f;
    private float I = 0.0f;
    private float J = 0.0f;
    private float O = 1.0f;
    private float P = 0.0f;
    private double T = 5.0d;
    private boolean U = false;
    private final int V = 2;
    private FPointBounds W = null;
    Rect a = null;
    private int Y = 0;
    private int Z = 2;
    int b = 0;
    int c = 0;
    ArrayList<FPoint> d = new ArrayList<>();
    long e = 0;

    public void a(boolean z) {
        this.C = z;
        this.f.d().setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolyline
    public void setGeodesic(boolean z) throws RemoteException {
        this.w = z;
        this.f.d().setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolyline
    public boolean isGeodesic() {
        return this.w;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolyline
    public void setDottedLine(boolean z) {
        int i = this.D;
        if (i == 2 || i == 0) {
            this.x = z;
            if (z && this.v) {
                this.D = 2;
            }
            this.f.d().setRunLowFrame(false);
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolyline
    public boolean isDottedLine() {
        return this.x;
    }

    public cr(i iVar, PolylineOptions polylineOptions) {
        this.f = iVar;
        setOptions(polylineOptions);
        try {
            this.g = getId();
        } catch (RemoteException e) {
            fn.b(e, "PolylineDelegateImp", "create");
            e.printStackTrace();
        }
    }

    void a(List<LatLng> list) throws RemoteException {
        ArrayList arrayList = new ArrayList();
        LatLngBounds.Builder builder = LatLngBounds.builder();
        if (list != null) {
            LatLng latLng = null;
            for (LatLng latLng2 : list) {
                if (!this.w) {
                    IPoint iPointObtain = IPoint.obtain();
                    this.f.d().a(latLng2.latitude, latLng2.longitude, iPointObtain);
                    arrayList.add(iPointObtain);
                    builder.include(latLng2);
                } else if (latLng != null) {
                    if (Math.abs(latLng2.longitude - latLng.longitude) < 0.01d) {
                        IPoint iPointObtain2 = IPoint.obtain();
                        this.f.d().a(latLng.latitude, latLng.longitude, iPointObtain2);
                        arrayList.add(iPointObtain2);
                        builder.include(latLng);
                        IPoint iPointObtain3 = IPoint.obtain();
                        this.f.d().a(latLng2.latitude, latLng2.longitude, iPointObtain3);
                        arrayList.add(iPointObtain3);
                        builder.include(latLng2);
                    } else {
                        a(latLng, latLng2, arrayList, builder);
                    }
                }
                latLng = latLng2;
            }
        }
        this.h = arrayList;
        this.G = 0;
        if (arrayList.size() > 0) {
            this.s = builder.build();
        }
        if (this.a == null) {
            this.a = new Rect();
        }
        dw.a(this.a);
        for (IPoint iPoint : this.h) {
            dw.b(this.a, iPoint.x, iPoint.y);
        }
        this.a.sort();
        this.f.d().setRunLowFrame(false);
    }

    IPoint a(IPoint iPoint, IPoint iPoint2, IPoint iPoint3, double d, int i) {
        IPoint iPointObtain = IPoint.obtain();
        double d2 = iPoint2.x - iPoint.x;
        double d3 = iPoint2.y - iPoint.y;
        iPointObtain.y = (int) (((((double) i) * d) / Math.sqrt(((d3 * d3) / (d2 * d2)) + 1.0d)) + ((double) iPoint3.y));
        iPointObtain.x = (int) (((((double) (iPoint3.y - iPointObtain.y)) * d3) / d2) + ((double) iPoint3.x));
        return iPointObtain;
    }

    void a(List<IPoint> list, List<IPoint> list2, double d) {
        if (list.size() != 3) {
            return;
        }
        int i = 0;
        int i2 = 0;
        while (i2 <= 10) {
            float f = i2;
            float f2 = f / 10.0f;
            IPoint iPointObtain = IPoint.obtain();
            double d2 = 1.0d - ((double) f2);
            double d3 = d2 * d2;
            double d4 = ((double) (2.0f * f2)) * d2;
            float f3 = f2 * f2;
            double d5 = (((double) list.get(i).x) * d3) + (((double) list.get(1).x) * d4 * d) + ((double) (list.get(2).x * f3));
            double d6 = (((double) list.get(i).y) * d3) + (((double) list.get(1).y) * d4 * d) + ((double) (list.get(2).y * f3));
            double d7 = d3 + (d4 * d) + ((double) f3);
            iPointObtain.x = (int) (d5 / d7);
            iPointObtain.y = (int) (d6 / d7);
            list2.add(iPointObtain);
            i2 = (int) (1.0f + f);
            i = 0;
        }
    }

    void a(LatLng latLng, LatLng latLng2, List<IPoint> list, LatLngBounds.Builder builder) {
        double dAbs = (Math.abs(latLng.longitude - latLng2.longitude) * 3.141592653589793d) / 180.0d;
        LatLng latLng3 = new LatLng((latLng2.latitude + latLng.latitude) / 2.0d, (latLng2.longitude + latLng.longitude) / 2.0d, false);
        builder.include(latLng).include(latLng3).include(latLng2);
        int i = latLng3.latitude > 0.0d ? -1 : 1;
        IPoint iPointObtain = IPoint.obtain();
        this.f.d().a(latLng.latitude, latLng.longitude, iPointObtain);
        IPoint iPointObtain2 = IPoint.obtain();
        this.f.d().a(latLng2.latitude, latLng2.longitude, iPointObtain2);
        IPoint iPointObtain3 = IPoint.obtain();
        this.f.d().a(latLng3.latitude, latLng3.longitude, iPointObtain3);
        double d = dAbs * 0.5d;
        double dCos = Math.cos(d);
        IPoint iPointA = a(iPointObtain, iPointObtain2, iPointObtain3, Math.hypot(iPointObtain.x - iPointObtain2.x, iPointObtain.y - iPointObtain2.y) * 0.5d * Math.tan(d), i);
        ArrayList arrayList = new ArrayList();
        arrayList.add(iPointObtain);
        arrayList.add(iPointA);
        arrayList.add(iPointObtain2);
        a(arrayList, list, dCos);
        iPointObtain.recycle();
        iPointA.recycle();
        iPointObtain2.recycle();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void remove() throws RemoteException {
        this.f.d(getId());
        setVisible(false);
        this.f.d().setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public String getId() throws RemoteException {
        if (this.g == null) {
            this.g = this.f.a("Polyline");
        }
        return this.g;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolyline
    public void setPoints(List<LatLng> list) throws RemoteException {
        try {
            this.j = list;
            synchronized (this.t) {
                a(list);
            }
            this.z = true;
            this.f.d().setRunLowFrame(false);
            this.X.setPoints(list);
        } catch (Throwable th) {
            fn.b(th, "PolylineDelegateImp", "setPoints");
            this.h.clear();
            th.printStackTrace();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolyline
    public List<LatLng> getPoints() throws RemoteException {
        return this.j;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolyline
    public void setWidth(float f) throws RemoteException {
        this.H = f;
        this.f.d().setRunLowFrame(false);
        this.X.width(f);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolyline
    public float getWidth() throws RemoteException {
        return this.H;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolyline
    public void setColor(int i) {
        int i2 = this.D;
        if (i2 == 0 || i2 == 2) {
            this.F = i;
            this.K = Color.alpha(i) / 255.0f;
            this.L = Color.red(i) / 255.0f;
            this.M = Color.green(i) / 255.0f;
            this.N = Color.blue(i) / 255.0f;
            if (this.v) {
                if (this.x) {
                    this.D = 2;
                } else {
                    this.D = 0;
                }
            }
            this.f.d().setRunLowFrame(false);
        }
        this.X.color(i);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolyline
    public int getColor() throws RemoteException {
        return this.F;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void setZIndex(float f) throws RemoteException {
        this.I = f;
        this.f.c();
        this.f.d().setRunLowFrame(false);
        PolylineOptions polylineOptions = this.X;
        if (polylineOptions != null) {
            polylineOptions.zIndex(f);
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public float getZIndex() throws RemoteException {
        return this.I;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void setVisible(boolean z) throws RemoteException {
        this.u = z;
        this.f.d().setRunLowFrame(false);
        PolylineOptions polylineOptions = this.X;
        if (polylineOptions != null) {
            polylineOptions.visible(z);
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public boolean isVisible() throws RemoteException {
        return this.u;
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
        Rectangle geoRectangle = this.f.d().getMapConfig().getGeoRectangle();
        Rect rect = this.a;
        return rect == null || geoRectangle == null || geoRectangle.isOverlap(rect);
    }

    public boolean b() throws RemoteException {
        synchronized (this.t) {
            FPointBounds.Builder builder = new FPointBounds.Builder();
            this.i.clear();
            int i = 0;
            this.B = false;
            float[] fArr = new float[this.h.size() * 3];
            this.Q = fArr;
            this.b = fArr.length;
            for (IPoint iPoint : this.h) {
                FPoint3 fPoint3 = new FPoint3();
                this.f.d().a(iPoint.y, iPoint.x, (FPoint) fPoint3);
                int i2 = i * 3;
                this.Q[i2] = fPoint3.x;
                this.Q[i2 + 1] = fPoint3.y;
                this.Q[i2 + 2] = 0.0f;
                List<Integer> list = this.m;
                if (list != null && list.size() > i) {
                    fPoint3.setColorIndex(this.m.get(i).intValue());
                } else {
                    List<Integer> list2 = this.n;
                    if (list2 != null && list2.size() > i) {
                        fPoint3.setColorIndex(this.n.get(i).intValue());
                    }
                }
                this.i.add(fPoint3);
                builder.include(fPoint3);
                i++;
            }
            this.W = builder.build();
        }
        if (!this.C) {
            this.q = dw.a(this.Q);
        }
        this.G = this.h.size();
        f();
        return true;
    }

    private void f() {
        if (this.G > 5000) {
            float f = this.J;
            if (f <= 12.0f) {
                float f2 = (this.H / 2.0f) + (f / 2.0f);
                if (f2 > 200.0f) {
                    f2 = 200.0f;
                }
                this.P = this.f.d().b().getMapLenWithWin((int) f2);
                return;
            }
        }
        this.P = this.f.d().b().getMapLenWithWin(10);
    }

    private void e(List<FPoint> list) throws RemoteException {
        int i;
        this.d.clear();
        int size = list.size();
        if (size < 2) {
            return;
        }
        int i2 = 0;
        FPoint fPoint = list.get(0);
        this.d.add(fPoint);
        int i3 = 1;
        while (true) {
            i = size - 1;
            if (i3 >= i) {
                break;
            }
            FPoint fPoint2 = list.get(i3);
            if (i3 == 1 || a(fPoint, fPoint2)) {
                this.d.add(fPoint2);
                fPoint = fPoint2;
            } else {
                ArrayList<FPoint> arrayList = this.d;
                arrayList.remove(arrayList.size() - 1);
                this.d.add(fPoint2);
            }
            i3++;
        }
        this.d.add(list.get(i));
        int size2 = this.d.size() * 3;
        this.b = size2;
        float[] fArr = this.Q;
        if (fArr == null || fArr.length < size2) {
            this.Q = new float[size2];
        }
        int i4 = this.D;
        if (i4 == 5 || i4 == 3) {
            int[] iArr = new int[this.d.size()];
            ArrayList arrayList2 = new ArrayList();
            int i5 = 0;
            int i6 = 0;
            for (int i7 = 0; i7 < size2 / 3; i7++) {
                FPoint3 fPoint3 = (FPoint3) this.d.get(i7);
                int i8 = i7 * 3;
                this.Q[i8] = fPoint3.x;
                this.Q[i8 + 1] = fPoint3.y;
                this.Q[i8 + 2] = 0.0f;
                int i9 = fPoint3.colorIndex;
                if (i7 == 0) {
                    arrayList2.add(Integer.valueOf(i9));
                    i5 = i9;
                } else {
                    if (i9 != i5) {
                        if (i9 != -1) {
                            i5 = i9;
                        }
                        arrayList2.add(Integer.valueOf(i5));
                    }
                }
                iArr[i6] = i7;
                i6++;
            }
            int[] iArr2 = new int[arrayList2.size()];
            this.R = iArr2;
            System.arraycopy(iArr, 0, iArr2, 0, iArr2.length);
            this.o = arrayList2;
            this.p = arrayList2;
            return;
        }
        for (FPoint fPoint4 : this.d) {
            int i10 = i2 * 3;
            this.Q[i10] = fPoint4.x;
            this.Q[i10 + 1] = fPoint4.y;
            this.Q[i10 + 2] = 0.0f;
            i2++;
        }
    }

    private boolean a(FPoint fPoint, FPoint fPoint2) {
        return Math.abs(fPoint2.x - fPoint.x) >= this.P || Math.abs(fPoint2.y - fPoint.y) >= this.P;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolyline
    public void setCustomTexture(BitmapDescriptor bitmapDescriptor) {
        long jNanoTime = System.nanoTime();
        if (jNanoTime - this.e < 16) {
            return;
        }
        this.e = jNanoTime;
        if (bitmapDescriptor == null) {
            return;
        }
        synchronized (this) {
            this.v = false;
            this.y = false;
            this.D = 1;
            this.r = bitmapDescriptor;
            this.f.d().setRunLowFrame(false);
            PolylineOptions polylineOptions = this.X;
            if (polylineOptions != null) {
                polylineOptions.setCustomTexture(bitmapDescriptor);
            }
        }
    }

    private void g() {
        k kVarD;
        i iVar = this.f;
        if (iVar == null || (kVarD = iVar.d()) == null) {
            return;
        }
        this.aa = (cw.d) kVarD.s(3);
    }

    @Override // com.amap.api.mapcore.util.ch
    public void c() throws RemoteException {
        List<IPoint> list = this.h;
        if (list == null || list.size() == 0 || this.H <= 0.0f || this.f.d() == null) {
            return;
        }
        synchronized (this.t) {
            int s_x = this.f.d().getMapConfig().getS_x();
            int s_y = this.f.d().getMapConfig().getS_y();
            if (this.i.size() == this.h.size()) {
                int i = 0;
                for (IPoint iPoint : this.h) {
                    this.i.get(i).x = iPoint.x - s_x;
                    this.i.get(i).y = iPoint.y - s_y;
                    i++;
                }
            } else {
                this.i.clear();
                int i2 = 0;
                for (IPoint iPoint2 : this.h) {
                    FPoint3 fPoint3 = new FPoint3();
                    List<Integer> list2 = this.m;
                    if (list2 != null && list2.size() > i2) {
                        fPoint3.setColorIndex(this.m.get(i2).intValue());
                    }
                    fPoint3.x = iPoint2.x - s_x;
                    fPoint3.y = iPoint2.y - s_y;
                    this.i.add(fPoint3);
                    i2++;
                }
            }
        }
        if (this.z) {
            b();
            this.z = false;
        }
        float[] fArr = this.Q;
        if (fArr != null && this.G > 0) {
            if (this.C) {
                a(this.f.d().getMapConfig());
            } else {
                if (this.q == null) {
                    this.q = dw.a(fArr);
                }
                cw.d dVar = this.aa;
                if (dVar == null || dVar.c()) {
                    g();
                }
                di.a(this.aa, this.F, this.q, this.H, this.G, this.f.e());
            }
        }
        this.B = true;
    }

    private void a(MapConfig mapConfig) {
        float mapLenWithWin = this.f.d().b().getMapLenWithWin((int) this.H);
        int i = this.D;
        if (i == 0) {
            f(mapLenWithWin, mapConfig);
            return;
        }
        if (i == 1) {
            d(mapLenWithWin, mapConfig);
            return;
        }
        if (i == 2) {
            e(mapLenWithWin, mapConfig);
            return;
        }
        if (i == 3) {
            c(mapLenWithWin, mapConfig);
        } else if (i == 4) {
            b(mapLenWithWin, mapConfig);
        } else {
            if (i != 5) {
                return;
            }
            a(mapLenWithWin, mapConfig);
        }
    }

    private void a(float f, MapConfig mapConfig) {
        List<FPoint> listB;
        if (!this.y) {
            try {
                List<BitmapDescriptor> list = this.k;
                if (list != null) {
                    this.S = new int[list.size()];
                    Iterator<BitmapDescriptor> it = this.k.iterator();
                    int i = 0;
                    while (it.hasNext()) {
                        this.S[i] = a(true, it.next());
                        i++;
                    }
                    this.y = true;
                }
            } catch (Throwable th) {
                fn.b(th, "MarkerDelegateImp", "loadtexture");
                return;
            }
        }
        FPoint[] clipMapRect = mapConfig.getGeoRectangle().getClipMapRect();
        try {
            List<FPoint> list2 = this.i;
            if (a(clipMapRect)) {
                synchronized (this.t) {
                    listB = dw.b(clipMapRect, this.i, false);
                }
                list2 = listB;
            }
            if (list2.size() >= 2) {
                e(list2);
                int size = this.o.size();
                int[] iArr = new int[size];
                for (int i2 = 0; i2 < size; i2++) {
                    int iIntValue = this.o.get(i2).intValue();
                    if (iIntValue < 0) {
                        iIntValue = 0;
                    }
                    iArr[i2] = this.S[iIntValue];
                }
                int[] iArr2 = this.R;
                if (true && (iArr2 != null)) {
                    AMapNativeRenderer.nativeDrawLineByMultiTextureID(this.Q, this.b, f, iArr, size, iArr2, iArr2.length, 1.0f - this.O, this.f.e());
                }
            }
        } catch (Throwable th2) {
            th2.printStackTrace();
        }
    }

    private int a(boolean z, BitmapDescriptor bitmapDescriptor) {
        r rVar;
        if (z) {
            rVar = this.f.d().a(bitmapDescriptor);
            if (rVar != null) {
                return rVar.f();
            }
        } else {
            rVar = null;
        }
        int iH = 0;
        if (rVar == null) {
            rVar = new r(bitmapDescriptor, 0);
        }
        Bitmap bitmap = bitmapDescriptor.getBitmap();
        if (bitmap != null && !bitmap.isRecycled()) {
            iH = h();
            if (z) {
                this.f.d().a(rVar);
            }
            this.l.add(rVar);
            dw.b(iH, bitmap, true);
        }
        return iH;
    }

    private void b(float f, MapConfig mapConfig) {
        int[] iArr = new int[this.n.size()];
        for (int i = 0; i < this.n.size(); i++) {
            iArr[i] = this.n.get(i).intValue();
        }
        try {
            b();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        float[] fArr = this.Q;
        int length = fArr.length;
        int size = this.n.size();
        int[] iArr2 = this.R;
        AMapNativeRenderer.nativeDrawGradientColorLine(fArr, length, f, iArr, size, iArr2, iArr2.length, this.f.d().c(), this.f.e());
    }

    private void c(float f, MapConfig mapConfig) {
        List<FPoint> listB;
        int[] iArr = new int[this.n.size()];
        for (int i = 0; i < this.n.size(); i++) {
            iArr[i] = this.n.get(i).intValue();
        }
        FPoint[] clipMapRect = mapConfig.getGeoRectangle().getClipMapRect();
        try {
            List<FPoint> list = this.i;
            if (a(clipMapRect)) {
                synchronized (this.t) {
                    listB = dw.b(clipMapRect, this.i, false);
                }
                list = listB;
            }
            if (list.size() >= 2) {
                e(list);
                int size = this.p.size();
                int[] iArr2 = new int[size];
                for (int i2 = 0; i2 < size; i2++) {
                    iArr2[i2] = this.p.get(i2).intValue();
                }
                if (true && (this.R != null)) {
                    float[] fArr = this.Q;
                    int i3 = this.b;
                    int iC = this.f.d().c();
                    int[] iArr3 = this.R;
                    AMapNativeRenderer.nativeDrawLineByMultiColor(fArr, i3, f, iC, iArr2, size, iArr3, iArr3.length, this.f.e());
                }
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private void d(float f, MapConfig mapConfig) {
        List<FPoint> listA;
        float[] fArr;
        if (!this.y) {
            synchronized (this) {
                try {
                    if (this.r != null) {
                        this.E = a(true, this.r);
                        this.y = true;
                    }
                } catch (Throwable th) {
                    fn.b(th, "MarkerDelegateImp", "loadtexture");
                    return;
                }
            }
        }
        try {
            if (mapConfig.getChangeRatio() == 1.0d && (fArr = this.Q) != null) {
                int i = this.Y + 1;
                this.Y = i;
                if (i > this.Z) {
                    AMapNativeRenderer.nativeDrawLineByTextureID(fArr, this.b, f, this.E, this.L, this.M, this.N, this.K, 1.0f - this.O, false, false, false, this.f.e());
                    return;
                }
            }
            this.Y = 0;
            FPoint[] clipMapRect = mapConfig.getGeoRectangle().getClipMapRect();
            List<FPoint> list = this.i;
            if (a(clipMapRect)) {
                synchronized (this.t) {
                    listA = dw.a(clipMapRect, this.i, false);
                }
                list = listA;
            }
            if (list.size() >= 2) {
                e(list);
                AMapNativeRenderer.nativeDrawLineByTextureID(this.Q, this.b, f, this.E, this.L, this.M, this.N, this.K, 1.0f - this.O, false, false, false, this.f.e());
            }
        } catch (Throwable unused) {
        }
    }

    private void e(float f, MapConfig mapConfig) {
        float[] fArr;
        if (!this.y) {
            synchronized (this) {
                try {
                    if (this.r != null) {
                        this.E = a(true, this.r);
                        this.y = true;
                    }
                } catch (Throwable th) {
                    fn.b(th, "MarkerDelegateImp", "loadtexture");
                    return;
                }
            }
        }
        try {
            List<FPoint> listA = this.i;
            if (this.f.d() == null) {
                return;
            }
            if (mapConfig.getChangeRatio() == 1.0d && (fArr = this.Q) != null) {
                int i = this.Y + 1;
                this.Y = i;
                if (i > this.Z) {
                    AMapNativeRenderer.nativeDrawLineByTextureID(fArr, this.b, f, this.f.d().d(), this.L, this.M, this.N, this.K, 0.0f, true, true, false, this.f.e());
                    return;
                }
            }
            this.Y = 0;
            FPoint[] clipMapRect = mapConfig.getGeoRectangle().getClipMapRect();
            if (a(clipMapRect)) {
                synchronized (this.t) {
                    listA = dw.a(clipMapRect, this.i, false);
                }
            }
            if (listA.size() >= 2) {
                e(listA);
                AMapNativeRenderer.nativeDrawLineByTextureID(this.Q, this.b, f, this.f.d().d(), this.L, this.M, this.N, this.K, 0.0f, true, true, false, this.f.e());
            }
        } catch (Throwable unused) {
        }
    }

    private void f(float f, MapConfig mapConfig) {
        float[] fArr;
        try {
            List<FPoint> listA = this.i;
            if (this.f.d() == null) {
                return;
            }
            if (mapConfig.getChangeRatio() == 1.0d && (fArr = this.Q) != null) {
                int i = this.Y + 1;
                this.Y = i;
                if (i > this.Z) {
                    AMapNativeRenderer.nativeDrawLineByTextureID(fArr, this.b, f, this.f.d().c(), this.L, this.M, this.N, this.K, 0.0f, false, true, false, this.f.e());
                    return;
                }
            }
            this.Y = 0;
            FPoint[] clipMapRect = mapConfig.getGeoRectangle().getClipMapRect();
            if (a(clipMapRect)) {
                synchronized (this.t) {
                    listA = dw.a(clipMapRect, this.i, false);
                }
            }
            if (listA.size() >= 2) {
                e(listA);
                AMapNativeRenderer.nativeDrawLineByTextureID(this.Q, this.b, f, this.f.d().c(), this.L, this.M, this.N, this.K, 0.0f, false, true, false, this.f.e());
            }
        } catch (Throwable unused) {
        }
    }

    private int h() {
        int[] iArr = {0};
        GLES20.glGenTextures(1, iArr, 0);
        return iArr[0];
    }

    private boolean a(FPoint[] fPointArr) {
        this.J = this.f.d().g();
        f();
        if (this.J <= 10.0f) {
            return false;
        }
        try {
            if (this.f.d() != null) {
                return (dw.a(this.W.northeast, fPointArr) && dw.a(this.W.southwest, fPointArr)) ? false : true;
            }
        } catch (Throwable unused) {
        }
        return false;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void destroy() {
        try {
            remove();
            List<r> list = this.l;
            if (list != null && list.size() > 0) {
                for (int i = 0; i < this.l.size(); i++) {
                    r rVar = this.l.get(i);
                    if (rVar != null) {
                        this.f.a(Integer.valueOf(rVar.f()));
                        this.f.d().b(rVar.j());
                    }
                }
            }
            if (this.Q != null) {
                this.Q = null;
            }
            FloatBuffer floatBuffer = this.q;
            if (floatBuffer != null) {
                floatBuffer.clear();
                this.q = null;
            }
            List<BitmapDescriptor> list2 = this.k;
            if (list2 != null && list2.size() > 0) {
                Iterator<BitmapDescriptor> it = this.k.iterator();
                while (it.hasNext()) {
                    it.next().recycle();
                }
            }
            BitmapDescriptor bitmapDescriptor = this.r;
            if (bitmapDescriptor != null) {
                bitmapDescriptor.recycle();
            }
            List<Integer> list3 = this.n;
            if (list3 != null) {
                list3.clear();
                this.n = null;
            }
            List<Integer> list4 = this.m;
            if (list4 != null) {
                list4.clear();
                this.m = null;
            }
            List<LatLng> list5 = this.j;
            if (list5 != null) {
                list5.clear();
                this.j = null;
            }
            this.X = null;
        } catch (Throwable th) {
            fn.b(th, "PolylineDelegateImp", "destroy");
            th.printStackTrace();
            Log.d("destroy erro", "PolylineDelegateImp destroy");
        }
    }

    @Override // com.amap.api.mapcore.util.ch
    public boolean d() {
        return this.B;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolyline
    public LatLng getNearestLatLng(LatLng latLng) {
        List<LatLng> list;
        if (latLng != null && (list = this.j) != null && list.size() != 0) {
            float fCalculateLineDistance = 0.0f;
            int i = 0;
            for (int i2 = 0; i2 < this.j.size(); i2++) {
                try {
                    if (i2 == 0) {
                        fCalculateLineDistance = AMapUtils.calculateLineDistance(latLng, this.j.get(i2));
                    } else {
                        float fCalculateLineDistance2 = AMapUtils.calculateLineDistance(latLng, this.j.get(i2));
                        if (fCalculateLineDistance > fCalculateLineDistance2) {
                            i = i2;
                            fCalculateLineDistance = fCalculateLineDistance2;
                        }
                    }
                } catch (Throwable th) {
                    fn.b(th, "PolylineDelegateImp", "getNearestLatLng");
                    th.printStackTrace();
                }
            }
            return this.j.get(i);
        }
        return null;
    }

    @Override // com.amap.api.mapcore.util.ck
    public boolean a(LatLng latLng) {
        float[] fArr = this.Q;
        int length = fArr.length;
        System.arraycopy(fArr, 0, new float[length], 0, fArr.length);
        if (length / 3 < 2) {
            return false;
        }
        try {
            ArrayList<FPoint> arrayListI = i();
            if (arrayListI != null && arrayListI.size() >= 1) {
                double mapLenWithWin = this.f.d().b().getMapLenWithWin(((int) this.H) / 4);
                double mapLenWithWin2 = this.f.d().b().getMapLenWithWin((int) this.T);
                FPoint fPointB = b(latLng);
                FPoint fPoint = null;
                int i = 0;
                while (i < arrayListI.size() - 1) {
                    if (i == 0) {
                        fPoint = arrayListI.get(i);
                    }
                    i++;
                    FPoint fPoint2 = arrayListI.get(i);
                    if ((mapLenWithWin2 + mapLenWithWin) - a(fPointB, fPoint, fPoint2) >= 0.0d) {
                        arrayListI.clear();
                        return true;
                    }
                    fPoint = fPoint2;
                }
                arrayListI.clear();
            }
        } catch (Exception unused) {
        }
        return false;
    }

    private ArrayList<FPoint> i() {
        ArrayList<FPoint> arrayList = new ArrayList<>();
        int i = 0;
        while (true) {
            float[] fArr = this.Q;
            if (i >= fArr.length) {
                return arrayList;
            }
            float f = fArr[i];
            int i2 = i + 1;
            arrayList.add(FPoint.obtain(f, fArr[i2]));
            i = i2 + 1 + 1;
        }
    }

    private double a(FPoint fPoint, FPoint fPoint2, FPoint fPoint3) {
        return a(fPoint.x, fPoint.y, fPoint2.x, fPoint2.y, fPoint3.x, fPoint3.y);
    }

    private FPoint b(LatLng latLng) {
        IPoint iPointObtain = IPoint.obtain();
        this.f.d().a(latLng.latitude, latLng.longitude, iPointObtain);
        FPoint fPointObtain = FPoint.obtain();
        this.f.d().a(iPointObtain.y, iPointObtain.x, fPointObtain);
        iPointObtain.recycle();
        return fPointObtain;
    }

    private double a(double d, double d2, double d3, double d4, double d5, double d6) {
        double d7 = d5 - d3;
        double d8 = d - d3;
        double d9 = d6 - d4;
        double d10 = d2 - d4;
        double d11 = (d7 * d8) + (d9 * d10);
        if (d11 <= 0.0d) {
            return Math.sqrt((d8 * d8) + (d10 * d10));
        }
        double d12 = (d7 * d7) + (d9 * d9);
        if (d11 >= d12) {
            double d13 = d - d5;
            double d14 = d2 - d6;
            return Math.sqrt((d13 * d13) + (d14 * d14));
        }
        double d15 = d11 / d12;
        double d16 = d - (d3 + (d7 * d15));
        double d17 = (d4 + (d9 * d15)) - d2;
        return Math.sqrt((d16 * d16) + (d17 * d17));
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolyline
    public void setTransparency(float f) {
        this.O = (float) Math.min(1.0d, Math.max(0.0d, f));
        this.f.d().setRunLowFrame(false);
    }

    public void b(List<BitmapDescriptor> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        if (list.size() > 1) {
            this.v = false;
            this.D = 5;
            this.k = list;
            this.f.d().setRunLowFrame(false);
            return;
        }
        setCustomTexture(list.get(0));
    }

    public void c(List<Integer> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        this.m = list;
        this.o = f(list);
    }

    public void d(List<Integer> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        this.n = list;
        if (list.size() > 1) {
            this.v = false;
            this.p = f(list);
            this.D = 3;
            this.f.d().setRunLowFrame(false);
            return;
        }
        setColor(list.get(0).intValue());
    }

    public void b(boolean z) {
        List<Integer> list;
        if (!z || (list = this.n) == null || list.size() <= 1) {
            return;
        }
        this.A = z;
        this.D = 4;
        this.f.d().setRunLowFrame(false);
    }

    private List<Integer> f(List<Integer> list) {
        int[] iArr = new int[list.size()];
        ArrayList arrayList = new ArrayList();
        int i = 0;
        int i2 = 0;
        for (int i3 = 0; i3 < list.size(); i3++) {
            int iIntValue = list.get(i3).intValue();
            if (i3 == 0 || iIntValue != i) {
                arrayList.add(Integer.valueOf(iIntValue));
                iArr[i2] = i3;
                i2++;
                i = iIntValue;
            }
        }
        int[] iArr2 = new int[arrayList.size()];
        this.R = iArr2;
        System.arraycopy(iArr, 0, iArr2, 0, iArr2.length);
        return arrayList;
    }

    public void e() {
        this.y = false;
        this.E = 0;
        int[] iArr = this.S;
        if (iArr != null) {
            Arrays.fill(iArr, 0);
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void setAboveMaskLayer(boolean z) {
        this.U = z;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public boolean isAboveMaskLayer() {
        return this.U;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolyline
    public void setOptions(PolylineOptions polylineOptions) {
        if (polylineOptions == null) {
            return;
        }
        this.X = polylineOptions;
        try {
            setColor(polylineOptions.getColor());
            setGeodesic(polylineOptions.isGeodesic());
            setDottedLine(polylineOptions.isDottedLine());
            setAboveMaskLayer(polylineOptions.isAboveMaskLayer());
            setVisible(polylineOptions.isVisible());
            setWidth(polylineOptions.getWidth());
            setZIndex(polylineOptions.getZIndex());
            a(polylineOptions.isUseTexture());
            setTransparency(polylineOptions.getTransparency());
            if (polylineOptions.getColorValues() != null) {
                d(polylineOptions.getColorValues());
                b(polylineOptions.isUseGradient());
            }
            if (polylineOptions.getCustomTexture() != null) {
                setCustomTexture(polylineOptions.getCustomTexture());
                e();
            }
            if (polylineOptions.getCustomTextureList() != null) {
                b(polylineOptions.getCustomTextureList());
                c(polylineOptions.getCustomTextureIndex());
                e();
            }
            setPoints(polylineOptions.getPoints());
        } catch (RemoteException e) {
            fn.b(e, "PolylineDelegateImp", "setOptions");
            e.printStackTrace();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolyline
    public PolylineOptions getOptions() {
        return this.X;
    }
}
