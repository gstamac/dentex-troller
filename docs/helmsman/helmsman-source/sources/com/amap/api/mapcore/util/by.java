package com.amap.api.mapcore.util;

import android.graphics.Color;
import android.os.RemoteException;
import android.util.Log;
import androidx.core.view.ViewCompat;
import com.amap.api.maps.model.LatLng;
import com.autonavi.ae.gmap.GLMapState;
import com.autonavi.amap.mapcore.AMapNativeRenderer;
import com.autonavi.amap.mapcore.DPoint;
import com.autonavi.amap.mapcore.FPoint;
import com.autonavi.amap.mapcore.IPoint;
import com.autonavi.amap.mapcore.interfaces.IOverlay;

/* JADX INFO: compiled from: ArcDelegateImp.java */
/* JADX INFO: loaded from: classes.dex */
public class by implements cc {
    float a;
    float b;
    float c;
    float d;
    private LatLng e;
    private LatLng f;
    private LatLng g;
    private String l;
    private k m;
    private float[] n;
    private float h = 10.0f;
    private int i = ViewCompat.MEASURED_STATE_MASK;
    private float j = 0.0f;
    private boolean k = true;
    private int o = 0;
    private boolean p = false;
    private double q = 0.0d;
    private double r = 0.0d;
    private double s = 0.0d;

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

    public by(k kVar) {
        this.m = kVar;
        try {
            this.l = getId();
        } catch (RemoteException e) {
            fn.b(e, "ArcDelegateImp", "create");
            e.printStackTrace();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void remove() throws RemoteException {
        this.m.a(getId());
        this.m.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public String getId() throws RemoteException {
        if (this.l == null) {
            this.l = this.m.c("Arc");
        }
        return this.l;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void setZIndex(float f) throws RemoteException {
        this.j = f;
        this.m.f();
        this.m.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public float getZIndex() throws RemoteException {
        return this.j;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void setVisible(boolean z) throws RemoteException {
        this.k = z;
        this.m.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public boolean isVisible() throws RemoteException {
        return this.k;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public boolean equalsRemote(IOverlay iOverlay) throws RemoteException {
        return equals(iOverlay) || iOverlay.getId().equals(getId());
    }

    public boolean b() throws RemoteException {
        int i;
        FPoint[] fPointArr;
        if (this.e == null || this.f == null || this.g == null || !this.k) {
            return false;
        }
        try {
            this.p = false;
            GLMapState gLMapStateB = this.m.b();
            if (!e()) {
                this.n = new float[9];
                FPoint fPointObtain = FPoint.obtain();
                this.m.a(this.e.latitude, this.e.longitude, fPointObtain);
                FPoint fPointObtain2 = FPoint.obtain();
                this.m.a(this.f.latitude, this.f.longitude, fPointObtain2);
                FPoint fPointObtain3 = FPoint.obtain();
                this.m.a(this.g.latitude, this.g.longitude, fPointObtain3);
                FPoint[] fPointArr2 = {fPointObtain, fPointObtain2, fPointObtain3};
                for (int i2 = 0; i2 < 3; i2++) {
                    int i3 = i2 * 3;
                    this.n[i3] = fPointArr2[i2].x;
                    this.n[i3 + 1] = fPointArr2[i2].y;
                    this.n[i3 + 2] = 0.0f;
                }
                this.o = 3;
                return true;
            }
            DPoint dPointF = f();
            int iAbs = (int) ((Math.abs(this.s - this.r) * 180.0d) / 3.141592653589793d);
            double d = (this.s - this.r) / ((double) iAbs);
            int i4 = iAbs + 1;
            FPoint[] fPointArr3 = new FPoint[i4];
            this.n = new float[i4 * 3];
            int i5 = 0;
            while (i5 <= iAbs) {
                if (i5 == iAbs) {
                    FPoint fPointObtain4 = FPoint.obtain();
                    this.m.a(this.g.latitude, this.g.longitude, fPointObtain4);
                    fPointArr3[i5] = fPointObtain4;
                    i = i5;
                    fPointArr = fPointArr3;
                } else {
                    i = i5;
                    fPointArr = fPointArr3;
                    fPointArr[i] = a(gLMapStateB, (((double) i5) * d) + this.r, dPointF.x, dPointF.y);
                }
                FPoint fPointA = a(gLMapStateB, (((double) i) * d) + this.r, dPointF.x, dPointF.y);
                fPointArr[i] = fPointA;
                int i6 = i * 3;
                this.n[i6] = fPointA.x;
                this.n[i6 + 1] = fPointArr[i].y;
                this.n[i6 + 2] = 0.0f;
                i5 = i + 1;
                i4 = i4;
                fPointArr3 = fPointArr;
            }
            dPointF.recycle();
            this.o = i4;
            return true;
        } catch (Throwable th) {
            fn.b(th, "ArcDelegateImp", "calMapFPoint");
            th.printStackTrace();
            return false;
        }
    }

    private FPoint a(GLMapState gLMapState, double d, double d2, double d3) {
        double dCos = Math.cos(d) * this.q;
        int i = (int) (d3 + ((-Math.sin(d)) * this.q));
        FPoint fPointObtain = FPoint.obtain();
        gLMapState.geo2Map((int) (d2 + dCos), i, fPointObtain);
        return fPointObtain;
    }

    private boolean e() {
        return Math.abs(((this.e.latitude - this.f.latitude) * (this.f.longitude - this.g.longitude)) - ((this.e.longitude - this.f.longitude) * (this.f.latitude - this.g.latitude))) >= 1.0E-6d;
    }

    private DPoint f() {
        IPoint iPointObtain = IPoint.obtain();
        this.m.a(this.e.latitude, this.e.longitude, iPointObtain);
        IPoint iPointObtain2 = IPoint.obtain();
        this.m.a(this.f.latitude, this.f.longitude, iPointObtain2);
        IPoint iPointObtain3 = IPoint.obtain();
        this.m.a(this.g.latitude, this.g.longitude, iPointObtain3);
        double d = iPointObtain.x;
        double d2 = iPointObtain.y;
        double d3 = iPointObtain2.x;
        double d4 = iPointObtain2.y;
        double d5 = iPointObtain3.x;
        double d6 = iPointObtain3.y;
        double d7 = d6 - d2;
        double d8 = d4 * d4;
        double d9 = d2 * d2;
        double d10 = d3 * d3;
        double d11 = d * d;
        double d12 = d4 - d2;
        double d13 = d6 * d6;
        double d14 = d5 * d5;
        double d15 = d3 - d;
        double d16 = d5 - d;
        double d17 = (((((d8 - d9) + d10) - d11) * d7) + ((((d9 - d13) + d11) - d14) * d12)) / (((d15 * 2.0d) * d7) - ((d16 * 2.0d) * d12));
        double d18 = (((((d10 - d11) + d8) - d9) * d16) + ((((d11 - d14) + d9) - d13) * d15)) / (((d12 * 2.0d) * d16) - ((d7 * 2.0d) * d15));
        double d19 = d - d17;
        double d20 = d2 - d18;
        this.q = Math.sqrt((d19 * d19) + (d20 * d20));
        this.r = a(d17, d18, d, d2);
        double dA = a(d17, d18, d3, d4);
        double dA2 = a(d17, d18, d5, d6);
        this.s = dA2;
        double d21 = this.r;
        if (d21 < dA2) {
            if (dA <= d21 || dA >= dA2) {
                this.s = dA2 - 6.283185307179586d;
            }
        } else if (dA <= dA2 || dA >= d21) {
            this.s = dA2 + 6.283185307179586d;
        }
        iPointObtain.recycle();
        iPointObtain2.recycle();
        iPointObtain3.recycle();
        return DPoint.obtain(d17, d18);
    }

    private double a(double d, double d2, double d3, double d4) {
        double dSignum = (d2 - d4) / this.q;
        if (Math.abs(dSignum) > 1.0d) {
            dSignum = Math.signum(dSignum);
        }
        double dAsin = Math.asin(dSignum);
        if (dAsin >= 0.0d) {
            return d3 < d ? 3.141592653589793d - Math.abs(dAsin) : dAsin;
        }
        return d3 < d ? 3.141592653589793d - dAsin : dAsin + 6.283185307179586d;
    }

    @Override // com.amap.api.mapcore.util.ch
    public void c() throws RemoteException {
        if (this.e == null || this.f == null || this.g == null || !this.k) {
            return;
        }
        b();
        if (this.n != null && this.o > 0) {
            float mapLenWithWin = this.m.b().getMapLenWithWin((int) this.h);
            this.m.b().getMapLenWithWin(1);
            float[] fArr = this.n;
            AMapNativeRenderer.nativeDrawLineByTextureID(fArr, fArr.length, mapLenWithWin, this.m.c(), this.b, this.c, this.d, this.a, 0.0f, false, true, false, this.m.v());
        }
        this.p = true;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IArc
    public void setStrokeWidth(float f) throws RemoteException {
        this.h = f;
        this.m.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IArc
    public float getStrokeWidth() throws RemoteException {
        return this.h;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IArc
    public void setStrokeColor(int i) throws RemoteException {
        this.i = i;
        this.a = Color.alpha(i) / 255.0f;
        this.b = Color.red(i) / 255.0f;
        this.c = Color.green(i) / 255.0f;
        this.d = Color.blue(i) / 255.0f;
        this.m.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IArc
    public int getStrokeColor() throws RemoteException {
        return this.i;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void destroy() {
        try {
            this.e = null;
            this.f = null;
            this.g = null;
        } catch (Throwable th) {
            fn.b(th, "ArcDelegateImp", "destroy");
            th.printStackTrace();
            Log.d("destroy erro", "ArcDelegateImp destroy");
        }
    }

    @Override // com.amap.api.mapcore.util.ch
    public boolean d() {
        return this.p;
    }

    public void a(LatLng latLng) {
        this.e = latLng;
    }

    public void b(LatLng latLng) {
        this.f = latLng;
    }

    public void c(LatLng latLng) {
        this.g = latLng;
    }
}
