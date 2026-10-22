package com.amap.api.mapcore.util;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.RemoteException;
import android.util.Log;
import android.view.animation.AnimationUtils;
import com.amap.api.maps.AMapException;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.animation.Animation;
import com.autonavi.ae.gmap.GLMapState;
import com.autonavi.amap.mapcore.DPoint;
import com.autonavi.amap.mapcore.FPoint;
import com.autonavi.amap.mapcore.IPoint;
import com.autonavi.amap.mapcore.animation.GLAnimation;
import com.autonavi.amap.mapcore.animation.GLAnimationSet;
import com.autonavi.amap.mapcore.animation.GLTransformation;
import com.autonavi.amap.mapcore.animation.GLTranslateAnimation;
import com.autonavi.amap.mapcore.interfaces.IAnimation;
import com.autonavi.amap.mapcore.interfaces.IMarkerAction;
import com.autonavi.amap.mapcore.interfaces.IOverlayImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/* JADX INFO: compiled from: MarkerDelegateImp.java */
/* JADX INFO: loaded from: classes.dex */
public class cn implements cf, IAnimation, IMarkerAction {
    private static int f;
    private float K;
    private float L;
    private r O;
    private String R;
    private LatLng S;
    private LatLng T;
    private String U;
    private String V;
    float[] a;
    private p aa;
    private Object ab;
    private int ak;
    private int al;
    float[] b;
    GLAnimation e;
    private int q;
    private int r;
    private MarkerOptions y;
    private boolean g = false;
    private boolean h = false;
    private boolean i = false;
    private float j = 0.0f;
    private float k = 0.0f;
    private boolean l = false;
    private int m = 0;
    private int n = 0;
    private int o = 0;
    private int p = 0;
    private FPoint s = FPoint.obtain();
    private float[] t = {-1.0f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, -1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f};
    private float u = 0.0f;
    private float v = 1.0f;
    private float w = 1.0f;
    private float x = 1.0f;
    private boolean z = false;
    private boolean A = true;
    private int B = 5;
    private boolean C = true;
    private boolean D = true;
    private boolean E = false;
    private boolean F = false;
    private boolean G = false;
    private boolean H = true;
    private FPoint I = FPoint.obtain();
    private Point J = new Point();
    private int M = 0;
    private int N = 0;
    private r[] P = null;
    Rect c = new Rect(0, 0, 0, 0);
    private boolean Q = false;
    private float W = 0.5f;
    private float X = 1.0f;
    private boolean Y = false;
    private boolean Z = true;
    private boolean ac = false;
    private List<BitmapDescriptor> ad = new CopyOnWriteArrayList();
    private boolean ae = false;
    private boolean af = false;
    int d = 9;
    private boolean ag = true;
    private int ah = 0;
    private int ai = 20;
    private boolean aj = false;
    private long am = 0;
    private float an = Float.MAX_VALUE;
    private float ao = Float.MIN_VALUE;
    private float ap = Float.MIN_VALUE;
    private float aq = Float.MAX_VALUE;

    @Override // com.amap.api.mapcore.util.ci, com.autonavi.amap.mapcore.interfaces.IMarker
    public IMarkerAction getIMarkerAction() {
        return this;
    }

    public IAnimation q() {
        return this;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarkerAction
    public void setRotateAngleNotUpdate(float f2) {
    }

    private static String a(String str) {
        f++;
        return str + f;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public void setRotateAngle(float f2) {
        this.y.rotateAngle(f2);
        this.k = f2;
        this.j = (((-f2) % 360.0f) + 360.0f) % 360.0f;
        this.i = true;
        r();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public void destroy(boolean z) {
        try {
            this.Q = true;
            if (z) {
                remove();
            }
            int i = 0;
            if (this.aa != null) {
                int i2 = 0;
                while (true) {
                    r[] rVarArr = this.P;
                    if (rVarArr == null || i2 >= rVarArr.length) {
                        break;
                    }
                    r rVar = rVarArr[i2];
                    if (rVar != null) {
                        this.aa.a(rVar);
                        this.aa.c().b(rVar.j());
                    }
                    i2++;
                }
            }
            while (true) {
                List<BitmapDescriptor> list = this.ad;
                if (list == null || i >= list.size()) {
                    break;
                }
                this.ad.get(i).recycle();
                i++;
            }
            this.S = null;
            this.ab = null;
            this.P = null;
        } catch (Throwable th) {
            fn.b(th, "MarkerDelegateImp", "destroy");
            th.printStackTrace();
            Log.d("destroy erro", "MarkerDelegateImp destroy");
        }
    }

    synchronized void m() {
        List<BitmapDescriptor> list = this.ad;
        if (list != null) {
            list.clear();
        }
    }

    public synchronized void a(ArrayList<BitmapDescriptor> arrayList) {
        m();
        if (arrayList != null) {
            for (BitmapDescriptor bitmapDescriptor : arrayList) {
                if (bitmapDescriptor != null) {
                    this.ad.add(bitmapDescriptor);
                }
            }
        }
        if (this.ad.size() > 0) {
            this.M = this.ad.get(0).getWidth();
            this.N = this.ad.get(0).getHeight();
        } else {
            this.ad.add(BitmapDescriptorFactory.defaultMarker());
            this.M = this.ad.get(0).getWidth();
            this.N = this.ad.get(0).getHeight();
        }
    }

    public cn(MarkerOptions markerOptions, p pVar) {
        this.aa = pVar;
        setMarkerOptions(markerOptions);
    }

    public int n() {
        try {
            return this.M;
        } catch (Throwable unused) {
            return 0;
        }
    }

    public int o() {
        try {
            return this.N;
        } catch (Throwable unused) {
            return 0;
        }
    }

    @Override // com.amap.api.mapcore.util.ci
    public Rect h() {
        if (this.t == null) {
            this.c.set(0, 0, 0, 0);
            return this.c;
        }
        try {
            GLMapState gLMapStateB = this.aa.c().b();
            int iN = n();
            int iO = o();
            IPoint iPointObtain = IPoint.obtain();
            gLMapStateB.map2Win(this.s.x, this.s.y, iPointObtain);
            Matrix.setIdentityM(this.a, 0);
            Matrix.rotateM(this.a, 0, -this.j, 0.0f, 0.0f, 1.0f);
            if (this.l) {
                Matrix.rotateM(this.a, 0, this.aa.c().getMapConfig().getS_c(), 1.0f, 0.0f, 0.0f);
                Matrix.rotateM(this.a, 0, this.aa.c().getMapConfig().getS_r(), 0.0f, 0.0f, 1.0f);
            }
            float[] fArr = new float[4];
            float[] fArr2 = this.b;
            float f2 = -iN;
            fArr2[0] = this.W * f2;
            float f3 = iO;
            fArr2[1] = this.X * f3;
            fArr2[2] = 0.0f;
            fArr2[3] = 1.0f;
            Matrix.multiplyMV(fArr, 0, this.a, 0, fArr2, 0);
            this.c.set((int) (iPointObtain.x + fArr[0]), (int) (iPointObtain.y - fArr[1]), (int) (iPointObtain.x + fArr[0]), (int) (iPointObtain.y - fArr[1]));
            float[] fArr3 = this.b;
            float f4 = iN;
            fArr3[0] = (1.0f - this.W) * f4;
            fArr3[1] = f3 * this.X;
            fArr3[2] = 0.0f;
            fArr3[3] = 1.0f;
            Matrix.multiplyMV(fArr, 0, this.a, 0, fArr3, 0);
            this.c.union((int) (iPointObtain.x + fArr[0]), (int) (iPointObtain.y - fArr[1]));
            float[] fArr4 = this.b;
            fArr4[0] = f4 * (1.0f - this.W);
            float f5 = -iO;
            fArr4[1] = (1.0f - this.X) * f5;
            fArr4[2] = 0.0f;
            fArr4[3] = 1.0f;
            Matrix.multiplyMV(fArr, 0, this.a, 0, fArr4, 0);
            this.c.union((int) (iPointObtain.x + fArr[0]), (int) (iPointObtain.y - fArr[1]));
            float[] fArr5 = this.b;
            fArr5[0] = f2 * this.W;
            fArr5[1] = f5 * (1.0f - this.X);
            fArr5[2] = 0.0f;
            fArr5[3] = 1.0f;
            Matrix.multiplyMV(fArr, 0, this.a, 0, fArr5, 0);
            this.c.union((int) (iPointObtain.x + fArr[0]), (int) (iPointObtain.y - fArr[1]));
            this.o = this.c.centerX() - iPointObtain.x;
            this.p = this.c.top - iPointObtain.y;
            iPointObtain.recycle();
            return this.c;
        } catch (Throwable th) {
            fn.b(th, "MarkerDelegateImp", "getRect");
            th.printStackTrace();
            return new Rect(0, 0, 0, 0);
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public boolean remove() {
        r();
        this.Z = false;
        p pVar = this.aa;
        if (pVar != null) {
            return pVar.a((ci) this);
        }
        return false;
    }

    private void r() {
        if (this.aa.c() != null) {
            this.aa.c().setRunLowFrame(false);
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public LatLng getPosition() {
        if (this.aj && this.s != null) {
            DPoint dPointObtain = DPoint.obtain();
            IPoint iPointObtain = IPoint.obtain();
            p();
            this.aa.c().a(this.s.x, this.s.y, iPointObtain);
            GLMapState.geo2LonLat(iPointObtain.x, iPointObtain.y, dPointObtain);
            LatLng latLng = new LatLng(dPointObtain.y, dPointObtain.x);
            iPointObtain.recycle();
            dPointObtain.recycle();
            return latLng;
        }
        return this.S;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public String getId() {
        if (this.R == null) {
            this.R = a("Marker");
        }
        return this.R;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public void setPosition(LatLng latLng) {
        if (latLng == null) {
            fn.b(new AMapException("非法坐标值 latlng is null"), "setPosition", "Marker");
            return;
        }
        this.S = latLng;
        IPoint iPointObtain = IPoint.obtain();
        if (this.ae) {
            try {
                double[] dArrA = hn.a(latLng.longitude, latLng.latitude);
                this.T = new LatLng(dArrA[1], dArrA[0]);
                GLMapState.lonlat2Geo(dArrA[0], dArrA[1], iPointObtain);
            } catch (Throwable unused) {
                this.T = latLng;
            }
        } else {
            GLMapState.lonlat2Geo(latLng.longitude, latLng.latitude, iPointObtain);
        }
        this.q = iPointObtain.x;
        this.r = iPointObtain.y;
        this.aj = false;
        p();
        r();
        this.i = true;
        iPointObtain.recycle();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarker, com.autonavi.amap.mapcore.interfaces.IMarkerAction
    public void setTitle(String str) {
        this.U = str;
        r();
        this.y.title(str);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarker
    public String getTitle() {
        return this.U;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarker, com.autonavi.amap.mapcore.interfaces.IMarkerAction
    public void setSnippet(String str) {
        this.V = str;
        r();
        this.y.snippet(str);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarker
    public String getSnippet() {
        return this.V;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarker
    public void setDraggable(boolean z) {
        this.Y = z;
        this.y.draggable(z);
        r();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarker
    public synchronized void setIcons(ArrayList<BitmapDescriptor> arrayList) {
        if (arrayList != null) {
            try {
                if (this.ad != null) {
                    a(arrayList);
                    this.af = false;
                    this.g = false;
                    this.E = false;
                    r();
                    this.i = true;
                }
            } catch (Throwable th) {
                fn.b(th, "MarkerDelegateImp", "setIcons");
                th.printStackTrace();
            }
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarker
    public synchronized ArrayList<BitmapDescriptor> getIcons() {
        List<BitmapDescriptor> list = this.ad;
        if (list == null || list.size() <= 0) {
            return null;
        }
        ArrayList<BitmapDescriptor> arrayList = new ArrayList<>();
        Iterator<BitmapDescriptor> it = this.ad.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next());
        }
        return arrayList;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarker
    public void setIcon(BitmapDescriptor bitmapDescriptor) {
        if (bitmapDescriptor != null) {
            try {
                if (this.ad == null) {
                    return;
                }
                synchronized (this) {
                    this.ad.clear();
                    this.ad.add(bitmapDescriptor);
                    this.E = false;
                    this.af = false;
                    this.g = false;
                    r();
                    this.i = true;
                    this.M = bitmapDescriptor.getWidth();
                    this.N = bitmapDescriptor.getHeight();
                }
            } catch (Throwable th) {
                fn.b(th, "MarkerDelegateImp", "setIcon");
                th.printStackTrace();
            }
        }
    }

    private void s() {
        try {
            this.t[(this.d * 0) + 4] = this.O.b();
            this.t[(this.d * 0) + 5] = this.O.d();
            this.t[(this.d * 1) + 4] = this.O.c();
            this.t[(1 * this.d) + 5] = this.O.d();
            this.t[(this.d * 2) + 4] = this.O.c();
            this.t[(2 * this.d) + 5] = this.O.a();
            this.t[(this.d * 3) + 4] = this.O.b();
            this.t[(3 * this.d) + 5] = this.O.a();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarker
    public boolean isDraggable() {
        return this.Y;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarker
    public boolean isRemoved() {
        try {
            return !this.aa.c(this);
        } catch (Throwable th) {
            th.printStackTrace();
            return false;
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarker
    public void showInfoWindow() {
        if (this.Z && isInfoWindowEnable()) {
            this.aa.b((cf) this);
            r();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarker
    public void hideInfoWindow() {
        if (isInfoWindowShown()) {
            this.aa.b((ci) this);
            r();
            this.h = false;
        }
        this.i = false;
    }

    @Override // com.amap.api.mapcore.util.cf
    public void a(boolean z) {
        this.h = z;
        this.i = true;
    }

    @Override // com.amap.api.mapcore.util.ci, com.autonavi.amap.mapcore.interfaces.IMarker
    public boolean isInfoWindowShown() {
        return this.h;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public void setVisible(boolean z) {
        if (this.Z == z) {
            return;
        }
        this.y.visible(z);
        this.Z = z;
        if (!z) {
            this.G = false;
            if (isInfoWindowShown()) {
                this.aa.b((ci) this);
            }
        }
        r();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public boolean isVisible() {
        return this.Z;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public void setAnchor(float f2, float f3) {
        if (this.W == f2 && this.X == f3) {
            return;
        }
        this.y.anchor(f2, f3);
        this.W = f2;
        this.X = f3;
        this.i = true;
        r();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public float getAnchorU() {
        return this.W;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public float getAnchorV() {
        return this.X;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public boolean equalsRemote(IOverlayImage iOverlayImage) throws RemoteException {
        return equals(iOverlayImage) || iOverlayImage.getId().equals(getId());
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public int hashCodeRemote() {
        return super.hashCode();
    }

    public boolean p() {
        try {
            p pVar = this.aa;
            if (pVar != null && pVar.c() != null && this.aa.c().b() != null) {
                if (this.s == null) {
                    this.s = FPoint.obtain();
                }
                if (this.aj) {
                    this.aa.c().b(this.ak, this.al, this.s);
                } else {
                    this.aa.c().a(this.r, this.q, this.s);
                }
                return true;
            }
            return false;
        } catch (Throwable th) {
            th.printStackTrace();
            return false;
        }
    }

    private void a(k kVar, float f2) throws RemoteException {
        float fN = ((int) (this.v * n())) * f2;
        float fO = ((int) (this.w * o())) * f2;
        float f3 = this.s.x;
        float f4 = this.s.y;
        float s_c = kVar.getMapConfig().getS_c();
        float s_r = this.j;
        if (this.l) {
            s_r -= kVar.getMapConfig().getS_r();
            s_c = 0.0f;
        }
        float f5 = this.x;
        float f6 = f5 >= 0.0f ? f5 : 0.0f;
        if (f6 > 1.0f) {
            f6 = 1.0f;
        }
        float[] fArr = this.t;
        int i = this.d;
        float f7 = this.W;
        fArr[(i * 0) + 0] = f3 - (fN * f7);
        float f8 = this.X;
        fArr[(i * 0) + 1] = f4 + ((1.0f - f8) * fO);
        fArr[(i * 0) + 2] = f3;
        fArr[(i * 0) + 3] = f4;
        fArr[(i * 0) + 6] = s_r;
        fArr[(i * 0) + 7] = s_c;
        fArr[(i * 0) + 8] = f6;
        fArr[(1 * i) + 0] = f3 + ((1.0f - f7) * fN);
        fArr[(1 * i) + 1] = f4 + ((1.0f - f8) * fO);
        fArr[(1 * i) + 2] = f3;
        fArr[(1 * i) + 3] = f4;
        fArr[(1 * i) + 6] = s_r;
        fArr[(1 * i) + 7] = s_c;
        fArr[(1 * i) + 8] = f6;
        fArr[(2 * i) + 0] = ((1.0f - f7) * fN) + f3;
        fArr[(2 * i) + 1] = f4 - (fO * f8);
        fArr[(2 * i) + 2] = f3;
        fArr[(2 * i) + 3] = f4;
        fArr[(2 * i) + 6] = s_r;
        fArr[(2 * i) + 7] = s_c;
        fArr[(2 * i) + 8] = f6;
        fArr[(3 * i) + 0] = f3 - (fN * f7);
        fArr[(3 * i) + 1] = f4 - (fO * f8);
        fArr[(3 * i) + 2] = f3;
        fArr[(3 * i) + 3] = f4;
        fArr[(3 * i) + 6] = s_r;
        fArr[(3 * i) + 7] = s_c;
        fArr[(3 * i) + 8] = f6;
        List<BitmapDescriptor> list = this.ad;
        if (list == null || list.size() <= 0) {
            return;
        }
        this.ah++;
        if (this.ah >= this.ai * this.ad.size()) {
            this.ah = 0;
        }
        if (this.ai == 0) {
            this.ai = 1;
        }
        this.O = this.P[this.ah / this.ai];
        if (this.ag) {
            return;
        }
        r();
    }

    @Override // com.amap.api.mapcore.util.ci
    public void a(k kVar, float[] fArr, int i, float f2) {
        if (this.Q) {
            return;
        }
        if ((this.S != null || this.aj) && this.ad != null) {
            this.s.x = this.q - kVar.getMapConfig().getS_x();
            this.s.y = this.r - kVar.getMapConfig().getS_y();
            try {
                if (!this.g) {
                    this.am = System.currentTimeMillis();
                    this.g = true;
                }
                if (this.aj && this.D) {
                    kVar.b(this.ak, this.al, this.s);
                }
                t();
                a(kVar, f2);
                if (!this.E || !this.ag) {
                    s();
                    this.E = true;
                }
                a(fArr, i);
                if (this.i && isInfoWindowShown()) {
                    this.aa.c().j();
                    if (System.currentTimeMillis() - this.am > 1000) {
                        this.i = false;
                    }
                }
            } catch (Throwable th) {
                fn.b(th, "MarkerDelegateImp", "drawMarker");
            }
        }
    }

    private void a(float[] fArr, int i) {
        r[] rVarArr = this.P;
        if (rVarArr == null || rVarArr.length <= 0) {
            return;
        }
        float[] fArr2 = this.t;
        System.arraycopy(fArr2, 0, fArr, i, fArr2.length);
    }

    @Override // com.amap.api.mapcore.util.ci
    public void a(k kVar) {
        Bitmap bitmap;
        if (this.af) {
            return;
        }
        synchronized (this) {
            try {
                List<BitmapDescriptor> list = this.ad;
                if (list != null) {
                    this.P = new r[list.size()];
                    int i = 0;
                    for (BitmapDescriptor bitmapDescriptor : this.ad) {
                        r rVarA = kVar.a(bitmapDescriptor);
                        int iF = rVarA != null ? rVarA.f() : 0;
                        if (rVarA == null) {
                            rVarA = new r(bitmapDescriptor, iF);
                        }
                        if (iF == 0 && (bitmap = bitmapDescriptor.getBitmap()) != null && !bitmap.isRecycled()) {
                            this.M = bitmap.getWidth();
                            this.N = bitmap.getHeight();
                            int iE = this.aa.c().e();
                            if (iE == 0) {
                                int iU = u();
                                rVarA.a(iU);
                                kVar.a(rVarA);
                                dw.b(iU, bitmap, false);
                            } else {
                                if (this.aa.a(bitmap, rVarA)) {
                                    dw.a(iE, bitmap, (int) (rVarA.b() * 512.0f), (int) (rVarA.a() * 1024.0f));
                                    rVarA.a(iE);
                                } else {
                                    int iU2 = u();
                                    dw.b(iU2, bitmap, false);
                                    rVarA.a(iU2);
                                }
                                kVar.a(rVarA);
                            }
                        }
                        rVarA.g();
                        this.P[i] = rVarA;
                        i++;
                    }
                    if (this.ad.size() == 1) {
                        this.ag = true;
                    } else {
                        this.ag = false;
                    }
                    this.E = false;
                    this.af = true;
                }
                p();
            } catch (Throwable th) {
                fn.b(th, "MarkerDelegateImp", "loadtexture");
            }
        }
    }

    private void t() {
        GLAnimation gLAnimation;
        if (!this.H && (gLAnimation = this.e) != null && !gLAnimation.hasEnded()) {
            r();
            GLTransformation gLTransformation = new GLTransformation();
            this.e.getTransformation(AnimationUtils.currentAnimationTimeMillis(), gLTransformation);
            if (!Double.isNaN(gLTransformation.scale_x) && !Double.isNaN(gLTransformation.scale_y)) {
                this.v = (float) gLTransformation.scale_x;
                this.w = (float) gLTransformation.scale_y;
            }
            if (!Double.isNaN(gLTransformation.rotate)) {
                setRotateAngle((float) gLTransformation.rotate);
            }
            if (!Double.isNaN(gLTransformation.x) && !Double.isNaN(gLTransformation.y)) {
                double d = gLTransformation.x;
                double d2 = gLTransformation.y;
                if (this.aj) {
                    FPoint fPointObtain = FPoint.obtain();
                    this.aa.c().b().win2Map((int) d, (int) d2, fPointObtain);
                    IPoint iPointObtain = IPoint.obtain();
                    this.aa.c().b().map2Geo(fPointObtain.x, fPointObtain.y, iPointObtain);
                    a(iPointObtain.x, iPointObtain.y);
                    fPointObtain.recycle();
                    iPointObtain.recycle();
                    this.aj = true;
                } else {
                    a((int) d, (int) d2);
                }
            }
            if (!Double.isNaN(gLTransformation.alpha)) {
                this.x = (float) gLTransformation.alpha;
            }
            this.i = true;
            return;
        }
        this.v = 1.0f;
        this.w = 1.0f;
        this.H = true;
    }

    private int u() {
        int[] iArr = {0};
        GLES20.glGenTextures(1, iArr, 0);
        return iArr[0];
    }

    @Override // com.amap.api.mapcore.util.ci
    public boolean i() {
        return this.ag;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarker
    public void setPeriod(int i) {
        if (i <= 1) {
            this.ai = 1;
        } else {
            this.ai = i;
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public void setObject(Object obj) {
        this.ab = obj;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public Object getObject() {
        return this.ab;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarker
    public void setPerspective(boolean z) {
        this.ac = z;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarker
    public boolean isPerspective() {
        return this.ac;
    }

    @Override // com.amap.api.mapcore.util.ci
    public int k() {
        try {
            List<BitmapDescriptor> list = this.ad;
            if (list != null && list.size() > 0) {
                return this.O.f();
            }
            return 0;
        } catch (Throwable unused) {
            return 0;
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarker
    public int getPeriod() {
        return this.ai;
    }

    @Override // com.amap.api.mapcore.util.cf
    public LatLng b() {
        if (!this.aj) {
            return this.ae ? this.T : this.S;
        }
        this.aa.c().b().win2Map(this.ak, this.al, this.s);
        DPoint dPointObtain = DPoint.obtain();
        this.aa.c().b(this.ak, this.al, dPointObtain);
        LatLng latLng = new LatLng(dPointObtain.y, dPointObtain.y);
        dPointObtain.recycle();
        return latLng;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarker
    public void set2Top() {
        this.aa.a((cf) this);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarker
    public void setFlat(boolean z) throws RemoteException {
        this.l = z;
        r();
        this.y.setFlat(z);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarker
    public boolean isFlat() {
        return this.l;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public float getRotateAngle() {
        r();
        return this.k;
    }

    @Override // com.amap.api.mapcore.util.cf
    public int c() {
        return this.m;
    }

    @Override // com.amap.api.mapcore.util.cf
    public int d() {
        return this.n;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarker
    public void setPositionByPixels(int i, int i2) {
        this.ak = i;
        this.al = i2;
        this.aj = true;
        p();
        r();
        this.i = true;
    }

    @Override // com.amap.api.mapcore.util.cf
    public int e() {
        return this.o;
    }

    @Override // com.amap.api.mapcore.util.cf
    public int f() {
        return this.p;
    }

    @Override // com.amap.api.mapcore.util.cf
    public FPoint a() {
        return this.s;
    }

    @Override // com.amap.api.mapcore.util.cf
    public boolean g() {
        return this.aj;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public void setZIndex(float f2) {
        this.u = f2;
        this.y.zIndex(f2);
        this.aa.f();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public float getZIndex() {
        return this.u;
    }

    @Override // com.amap.api.mapcore.util.ci
    public boolean j() {
        if (this.aj) {
            return true;
        }
        if (this.s == null) {
            return false;
        }
        if (!this.H) {
            return true;
        }
        this.J.x = this.q;
        this.J.y = this.r;
        if (this.aa.c().getMapConfig().getGeoRectangle().contains(this.q, this.r)) {
            return true;
        }
        v();
        this.I.x = this.s.x;
        this.I.y = this.s.y;
        FPoint[] mapRect = this.aa.c().getMapConfig().getMapRect();
        a(mapRect);
        return dw.a(this.I, mapRect);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarker
    public void setGeoPoint(IPoint iPoint) {
        this.aj = false;
        a(iPoint.x, iPoint.y);
    }

    private void a(int i, int i2) {
        this.q = i;
        this.r = i2;
        DPoint dPointObtain = DPoint.obtain();
        GLMapState.geo2LonLat(this.q, this.r, dPointObtain);
        this.S = new LatLng(dPointObtain.y, dPointObtain.x, false);
        this.aa.c().b().geo2Map(this.q, this.r, this.s);
        dPointObtain.recycle();
        r();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarker
    public IPoint getGeoPoint() {
        IPoint iPointObtain = IPoint.obtain();
        if (this.aj) {
            this.aa.c().a(this.ak, this.al, iPointObtain);
            return iPointObtain;
        }
        iPointObtain.set(this.q, this.r);
        return iPointObtain;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarker
    public void setAnimation(Animation animation) {
        IAnimation iAnimationQ = q();
        if (iAnimationQ != null) {
            iAnimationQ.setAnimation(animation == null ? null : animation.glAnimation);
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAnimation
    public void setAnimation(GLAnimation gLAnimation) {
        if (gLAnimation == null) {
            return;
        }
        this.e = gLAnimation;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarker, com.autonavi.amap.mapcore.interfaces.IAnimation
    public boolean startAnimation() {
        GLAnimation gLAnimation = this.e;
        if (gLAnimation != null) {
            if (gLAnimation instanceof GLAnimationSet) {
                GLAnimationSet gLAnimationSet = (GLAnimationSet) gLAnimation;
                for (GLAnimation gLAnimation2 : gLAnimationSet.getAnimations()) {
                    a(gLAnimation2);
                    gLAnimation2.setDuration(gLAnimationSet.getDuration());
                }
            } else {
                a(gLAnimation);
            }
            this.H = false;
            this.e.start();
            r();
        }
        return false;
    }

    private void a(GLAnimation gLAnimation) {
        if (gLAnimation instanceof GLTranslateAnimation) {
            if (this.aj) {
                LatLng position = getPosition();
                this.S = position;
                setPosition(position);
                this.aj = true;
            }
            if (this.aj) {
                GLTranslateAnimation gLTranslateAnimation = (GLTranslateAnimation) gLAnimation;
                gLTranslateAnimation.mFromXDelta = this.ak;
                gLTranslateAnimation.mFromYDelta = this.al;
                IPoint iPointObtain = IPoint.obtain();
                this.aa.c().b(gLTranslateAnimation.mToYDelta, gLTranslateAnimation.mToXDelta, iPointObtain);
                gLTranslateAnimation.mToXDelta = iPointObtain.x;
                gLTranslateAnimation.mToYDelta = iPointObtain.y;
                iPointObtain.recycle();
                return;
            }
            GLTranslateAnimation gLTranslateAnimation2 = (GLTranslateAnimation) gLAnimation;
            gLTranslateAnimation2.mFromXDelta = this.q;
            gLTranslateAnimation2.mFromYDelta = this.r;
            IPoint iPointObtain2 = IPoint.obtain();
            GLMapState.lonlat2Geo(gLTranslateAnimation2.mToXDelta, gLTranslateAnimation2.mToYDelta, iPointObtain2);
            gLTranslateAnimation2.mToXDelta = iPointObtain2.x;
            gLTranslateAnimation2.mToYDelta = iPointObtain2.y;
            iPointObtain2.recycle();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarker, com.autonavi.amap.mapcore.interfaces.IAnimation
    public void setAnimationListener(Animation.AnimationListener animationListener) {
        GLAnimation gLAnimation = this.e;
        if (gLAnimation != null) {
            gLAnimation.setAnimationListener(animationListener);
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarkerAction
    public float getAlpha() {
        return this.x;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarkerAction
    public void setAlpha(float f2) {
        this.x = f2;
        this.y.alpha(f2);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarkerAction
    public int getDisplayLevel() {
        return this.B;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarkerAction
    public MarkerOptions getOptions() {
        return this.y;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarkerAction
    public void setMarkerOptions(MarkerOptions markerOptions) {
        if (markerOptions == null) {
            return;
        }
        this.y = markerOptions;
        this.S = markerOptions.getPosition();
        IPoint iPointObtain = IPoint.obtain();
        this.ae = this.y.isGps();
        if (this.y.getPosition() != null) {
            if (this.ae) {
                try {
                    double[] dArrA = hn.a(this.y.getPosition().longitude, this.y.getPosition().latitude);
                    this.T = new LatLng(dArrA[1], dArrA[0]);
                    GLMapState.lonlat2Geo(dArrA[0], dArrA[1], iPointObtain);
                } catch (Throwable th) {
                    fn.b(th, "MarkerDelegateImp", "create");
                    this.T = this.y.getPosition();
                }
            } else {
                GLMapState.lonlat2Geo(this.S.longitude, this.S.latitude, iPointObtain);
            }
        }
        this.q = iPointObtain.x;
        this.r = iPointObtain.y;
        this.W = this.y.getAnchorU();
        this.X = this.y.getAnchorV();
        this.m = this.y.getInfoWindowOffsetX();
        this.n = this.y.getInfoWindowOffsetY();
        this.ai = this.y.getPeriod();
        this.u = this.y.getZIndex();
        this.F = this.y.isBelowMaskLayer();
        p();
        a(this.y.getIcons());
        this.Z = this.y.isVisible();
        this.V = this.y.getSnippet();
        this.U = this.y.getTitle();
        this.Y = this.y.isDraggable();
        this.R = getId();
        this.ac = this.y.isPerspective();
        this.l = this.y.isFlat();
        this.F = this.y.isBelowMaskLayer();
        this.x = this.y.getAlpha();
        setRotateAngle(this.y.getRotateAngle());
        this.B = this.y.getDisplayLevel();
        this.z = this.y.isInfoWindowAutoOverturn();
        this.A = this.y.isInfoWindowEnable();
        this.a = new float[16];
        this.b = new float[4];
        iPointObtain.recycle();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarkerAction
    public boolean isClickable() {
        return this.C;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarkerAction
    public boolean isInfoWindowAutoOverturn() {
        return this.z;
    }

    @Override // com.amap.api.mapcore.util.cf, com.autonavi.amap.mapcore.interfaces.IMarkerAction
    public boolean isInfoWindowEnable() {
        return this.A;
    }

    @Override // com.amap.api.mapcore.util.ci
    public void b(boolean z) {
        this.G = z;
    }

    @Override // com.amap.api.mapcore.util.ci
    public boolean l() {
        return this.G;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarkerAction
    public void setInfoWindowEnable(boolean z) {
        this.A = z;
        if (!z) {
            hideInfoWindow();
        }
        this.y.infoWindowEnable(z);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarkerAction
    public void setAutoOverturnInfoWindow(boolean z) {
        this.z = z;
        this.y.autoOverturnInfoWindow(z);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarkerAction
    public void setClickable(boolean z) {
        this.C = z;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarkerAction
    public void setDisplayLevel(int i) {
        this.B = i;
        this.y.displayLevel(i);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarkerAction
    public void setFixingPointEnable(boolean z) {
        this.D = z;
        if (!z) {
            boolean z2 = this.aj;
            LatLng position = getPosition();
            this.S = position;
            setPosition(position);
            if (z2) {
                this.aj = true;
                return;
            }
            return;
        }
        if (!this.aj || this.S == null) {
            return;
        }
        IPoint iPointObtain = IPoint.obtain();
        this.aa.c().b().map2Win(this.s.x, this.s.y, iPointObtain);
        this.ak = iPointObtain.x;
        this.al = iPointObtain.y;
        iPointObtain.recycle();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarkerAction
    public void setPositionNotUpdate(LatLng latLng) {
        setPosition(latLng);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarker
    public void setBelowMaskLayer(boolean z) {
        this.F = z;
    }

    private void a(FPoint[] fPointArr) {
        if (fPointArr == null) {
            return;
        }
        v();
        if (this.K <= 0.0f || this.L <= 0.0f || fPointArr.length != 4) {
            return;
        }
        float fMin = Math.min(fPointArr[0].x, fPointArr[1].x);
        this.an = fMin;
        float fMin2 = Math.min(fMin, fPointArr[2].x);
        this.an = fMin2;
        this.an = Math.min(fMin2, fPointArr[3].x);
        float fMax = Math.max(fPointArr[0].x, fPointArr[1].x);
        this.ao = fMax;
        float fMax2 = Math.max(fMax, fPointArr[2].x);
        this.ao = fMax2;
        this.ao = Math.max(fMax2, fPointArr[3].x);
        float fMin3 = Math.min(fPointArr[0].y, fPointArr[1].y);
        this.aq = fMin3;
        float fMin4 = Math.min(fMin3, fPointArr[2].y);
        this.aq = fMin4;
        this.aq = Math.min(fMin4, fPointArr[3].y);
        float fMax3 = Math.max(fPointArr[0].y, fPointArr[1].y);
        this.ap = fMax3;
        float fMax4 = Math.max(fMax3, fPointArr[2].y);
        this.ap = fMax4;
        this.ap = Math.max(fMax4, fPointArr[3].y);
        if (this.s.x < (this.an + this.ao) / 2.0f) {
            this.I.x = this.s.x + (this.K / 2.0f);
        } else {
            this.I.x = this.s.x - (this.K / 2.0f);
        }
        if (this.s.y < (this.aq + this.ap) / 2.0f) {
            this.I.y = this.s.y;
        } else {
            this.I.y = this.s.y - this.L;
        }
    }

    private void v() {
        if (this.aa.c() == null || this.aa.c().getMapConfig() == null) {
            return;
        }
        this.K = this.aa.c().getMapConfig().getMapPerPixelUnitLength() * n();
        this.L = this.aa.c().getMapConfig().getMapPerPixelUnitLength() * o();
    }
}
