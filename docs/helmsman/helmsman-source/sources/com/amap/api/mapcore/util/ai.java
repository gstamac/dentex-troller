package com.amap.api.mapcore.util;

import android.graphics.Rect;
import android.opengl.Matrix;
import android.os.RemoteException;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.MultiPointItem;
import com.amap.api.maps.model.MultiPointOverlayOptions;
import com.autonavi.amap.mapcore.IPoint;
import com.autonavi.amap.mapcore.MapConfig;
import com.autonavi.amap.mapcore.MapProjection;
import com.autonavi.amap.mapcore.interfaces.IMultiPointOverlay;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* JADX INFO: compiled from: MultiPointOverlayDelegate.java */
/* JADX INFO: loaded from: classes.dex */
public class ai implements IMultiPointOverlay {
    private static int E;
    private String B;
    List<MultiPointItem> h;
    IPoint m;
    aj n;
    BitmapDescriptor a = BitmapDescriptorFactory.defaultMarker();
    BitmapDescriptor b = null;
    float c = 0.0f;
    float d = 0.0f;
    float e = 0.0f;
    float f = 0.5f;
    float g = 0.5f;
    ak i = null;
    ah j = null;
    ah k = new ah(0, 1, 0, 1);
    List<MultiPointItem> l = new ArrayList();
    private float[] C = {-0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 0.0f, -0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f};
    private boolean D = true;
    List<ag> o = new ArrayList();
    private ExecutorService F = null;
    private List<String> G = new ArrayList();
    private int H = 3;
    private float[] I = new float[ag.a * this.H];
    float[] p = new float[16];
    float[] q = new float[4];
    float[] r = new float[4];
    Rect s = new Rect();
    ah t = null;
    ah u = null;
    int v = 0;
    int w = 0;
    float[] x = new float[12];
    String y = "precision highp float;\nattribute vec3 aVertex;//顶点数组,三维坐标\nuniform mat4 aMVPMatrix;//mvp矩阵\nvoid main(){\n  gl_Position = aMVPMatrix * vec4(aVertex, 1.0);\n}";
    String z = "//有颜色 没有纹理\nprecision highp float;\nvoid main(){\n  gl_FragColor = vec4(0,0,1,1.0);\n}";
    int A = -1;

    public ai(MultiPointOverlayOptions multiPointOverlayOptions, aj ajVar) {
        this.n = ajVar;
        a(multiPointOverlayOptions);
        ag agVar = new ag(a(), this);
        agVar.a(ajVar.a());
        agVar.a(this.b);
        this.o.add(agVar);
    }

    private float[] a() {
        float[] fArr = this.C;
        if (fArr == null) {
            return null;
        }
        float[] fArr2 = (float[]) fArr.clone();
        float f = this.f - 0.5f;
        float f2 = this.g - 0.5f;
        fArr2[0] = fArr2[0] + f;
        fArr2[1] = fArr2[1] - f2;
        fArr2[6] = fArr2[6] + f;
        fArr2[7] = fArr2[7] - f2;
        fArr2[12] = fArr2[12] + f;
        fArr2[13] = fArr2[13] - f2;
        fArr2[18] = fArr2[18] + f;
        fArr2[19] = fArr2[19] - f2;
        return fArr2;
    }

    private static String a(String str) {
        E++;
        return str + E;
    }

    private void a(MultiPointOverlayOptions multiPointOverlayOptions) {
        if (multiPointOverlayOptions != null) {
            if (multiPointOverlayOptions.getIcon() != null) {
                this.b = multiPointOverlayOptions.getIcon();
            } else {
                this.b = this.a;
            }
            this.f = multiPointOverlayOptions.getAnchorU();
            this.g = multiPointOverlayOptions.getAnchorV();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMultiPointOverlay
    public void addItems(List<MultiPointItem> list) {
        ak akVar;
        ah ahVarB;
        try {
            synchronized (this) {
                if (this.h == null) {
                    this.h = new ArrayList();
                }
                this.h.clear();
                this.h.addAll(list);
                int size = this.h.size();
                for (int i = 0; i < size; i++) {
                    List<MultiPointItem> list2 = this.h;
                    if (list2 == null) {
                        return;
                    }
                    MultiPointItem multiPointItem = list2.get(i);
                    if (multiPointItem != null && multiPointItem.getLatLng() != null && multiPointItem.getIPoint() == null) {
                        IPoint iPoint = new IPoint();
                        MapProjection.lonlat2Geo(multiPointItem.getLatLng().longitude, multiPointItem.getLatLng().latitude, iPoint);
                        multiPointItem.setIPoint(iPoint);
                    }
                }
                if (this.i == null && (ahVarB = b()) != null) {
                    this.i = new ak(ahVarB);
                }
                int size2 = this.h.size();
                for (int i2 = 0; i2 < size2; i2++) {
                    List<MultiPointItem> list3 = this.h;
                    if (list3 == null) {
                        return;
                    }
                    MultiPointItem multiPointItem2 = list3.get(i2);
                    if (multiPointItem2 != null && multiPointItem2.getIPoint() != null && (akVar = this.i) != null) {
                        akVar.a(multiPointItem2);
                    }
                }
                d();
            }
        } catch (Throwable th) {
            fn.b(th, "MultiPointOverlayDelegate", "addItems");
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMultiPointOverlay
    public void addItem(MultiPointItem multiPointItem) {
        synchronized (this) {
        }
        d();
    }

    private ah b() {
        List<MultiPointItem> list = this.h;
        if (list == null || list.size() == 0) {
            return null;
        }
        Iterator<MultiPointItem> it = this.h.iterator();
        MultiPointItem next = it.next();
        int i = next.getIPoint().x;
        int i2 = next.getIPoint().x;
        int i3 = next.getIPoint().y;
        int i4 = next.getIPoint().y;
        while (it.hasNext()) {
            MultiPointItem next2 = it.next();
            int i5 = next2.getIPoint().x;
            int i6 = next2.getIPoint().y;
            if (i5 < i) {
                i = i5;
            }
            if (i5 > i2) {
                i2 = i5;
            }
            if (i6 < i3) {
                i3 = i6;
            }
            if (i6 > i4) {
                i4 = i6;
            }
        }
        return new ah(i, i2, i3, i4);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMultiPointOverlay
    public void draw(MapConfig mapConfig, float[] fArr, float[] fArr2) {
        int i;
        try {
            if (this.D) {
                c();
                if (this.o.size() >= 1 && this.i != null) {
                    float s_r = mapConfig.getS_r();
                    float s_c = mapConfig.getS_c();
                    if (mapConfig.getChangeRatio() != 1.0d || this.l.size() == 0) {
                        synchronized (this.l) {
                            a(mapConfig);
                            this.l.clear();
                            float mapPerPixelUnitLength = mapConfig.getMapPerPixelUnitLength();
                            this.c = mapPerPixelUnitLength;
                            this.d = mapPerPixelUnitLength * this.b.getWidth();
                            float height = this.c * this.b.getHeight();
                            this.e = height;
                            float f = this.d;
                            a(f, height, s_r, s_c);
                            this.i.a(this.j, this.l, f * height * 16.0f);
                        }
                    }
                    if (this.m == null) {
                        this.m = new IPoint();
                    }
                    IPoint iPoint = this.m;
                    if (iPoint != null && mapConfig != null) {
                        iPoint.x = mapConfig.getS_x();
                        this.m.y = mapConfig.getS_y();
                    }
                    ag agVar = this.o.get(0);
                    synchronized (this.l) {
                        Iterator<MultiPointItem> it = this.l.iterator();
                        loop0: while (true) {
                            i = 0;
                            while (true) {
                                if (!it.hasNext()) {
                                    break loop0;
                                }
                                IPoint iPoint2 = it.next().getIPoint();
                                if (iPoint2 != null) {
                                    int i2 = iPoint2.x - this.m.x;
                                    int i3 = iPoint2.y - this.m.y;
                                    if (agVar != null && agVar.b()) {
                                        float[] fArr3 = this.I;
                                        int i4 = this.H;
                                        fArr3[(i * i4) + 0] = i2;
                                        fArr3[(i * i4) + 1] = i3;
                                        fArr3[(i4 * i) + 2] = 0.0f;
                                        i++;
                                        if (i >= ag.a) {
                                            break;
                                        }
                                    }
                                }
                            }
                            agVar.a(fArr, fArr2, this.I, this.d, this.e, s_r, s_c, i);
                        }
                    }
                    if (i > 0) {
                        agVar.a(fArr, fArr2, this.I, this.d, this.e, s_r, s_c, i);
                    }
                }
            }
        } catch (Throwable th) {
            fn.b(th, "MultiPointOverlayDelegate", "draw");
        }
    }

    private void c() {
        if (this.F == null) {
            this.F = Executors.newSingleThreadExecutor();
        }
        for (final ag agVar : this.o) {
            if (agVar != null && !agVar.b()) {
                final String str = agVar.hashCode() + "";
                if (!this.G.contains(str)) {
                    this.G.add(str);
                    this.F.execute(new Runnable() { // from class: com.amap.api.mapcore.util.ai.1
                        @Override // java.lang.Runnable
                        public void run() {
                            if (agVar.b()) {
                                return;
                            }
                            agVar.a();
                            ai.this.G.remove(str);
                        }
                    });
                }
            }
        }
    }

    private void a(MapConfig mapConfig) {
        if (mapConfig != null) {
            Rect rect = mapConfig.getGeoRectangle().getRect();
            ah ahVar = this.j;
            if (ahVar == null) {
                this.j = new ah(rect.left, rect.right, rect.top, rect.bottom);
            } else {
                ahVar.a(rect.left, rect.right, rect.top, rect.bottom);
            }
        }
    }

    private void a(float f, float f2, float f3, float f4) {
        if (this.k == null) {
            this.k = new ah(0, 1, 0, 1);
        }
        this.s.set(0, 0, 0, 0);
        IPoint iPoint = new IPoint();
        float f5 = this.f;
        float f6 = this.g;
        Matrix.setIdentityM(this.p, 0);
        Matrix.rotateM(this.p, 0, -f3, 0.0f, 0.0f, 1.0f);
        float[] fArr = this.r;
        fArr[0] = 0.0f;
        fArr[1] = 0.0f;
        fArr[2] = 0.0f;
        fArr[3] = 0.0f;
        float[] fArr2 = this.q;
        float f7 = (-f) * f5;
        fArr2[0] = f7;
        float f8 = f2 * f6;
        fArr2[1] = f8;
        fArr2[2] = 0.0f;
        fArr2[3] = 1.0f;
        Matrix.multiplyMV(fArr, 0, this.p, 0, fArr2, 0);
        this.s.set((int) (iPoint.x + this.r[0]), (int) (iPoint.y - this.r[1]), (int) (iPoint.x + this.r[0]), (int) (iPoint.y - this.r[1]));
        float[] fArr3 = this.q;
        float f9 = f * (1.0f - f5);
        fArr3[0] = f9;
        fArr3[1] = f8;
        fArr3[2] = 0.0f;
        fArr3[3] = 1.0f;
        Matrix.multiplyMV(this.r, 0, this.p, 0, fArr3, 0);
        this.s.union((int) (iPoint.x + this.r[0]), (int) (iPoint.y - this.r[1]));
        float[] fArr4 = this.q;
        fArr4[0] = f9;
        float f10 = (-f2) * (1.0f - f6);
        fArr4[1] = f10;
        fArr4[2] = 0.0f;
        fArr4[3] = 1.0f;
        Matrix.multiplyMV(this.r, 0, this.p, 0, fArr4, 0);
        this.s.union((int) (iPoint.x + this.r[0]), (int) (iPoint.y - this.r[1]));
        float[] fArr5 = this.q;
        fArr5[0] = f7;
        fArr5[1] = f10;
        fArr5[2] = 0.0f;
        fArr5[3] = 1.0f;
        Matrix.multiplyMV(this.r, 0, this.p, 0, fArr5, 0);
        this.s.union((int) (iPoint.x + this.r[0]), (int) (iPoint.y - this.r[1]));
        this.k.a(this.s.left, this.s.right, this.s.top, this.s.bottom);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMultiPointOverlay
    public MultiPointItem onClick(IPoint iPoint) {
        if (!this.D || this.i == null) {
            return null;
        }
        if (this.t == null) {
            this.t = new ah(0, 1, 0, 1);
        }
        int i = (int) (this.c * 8.0f);
        this.t.a(iPoint.x - i, iPoint.x + i, iPoint.y - i, iPoint.y + i);
        ak akVar = this.i;
        if (akVar != null && akVar.b() != null && !this.i.b().a(this.t)) {
            return null;
        }
        synchronized (this.l) {
            for (int size = this.l.size() - 1; size >= 0; size--) {
                MultiPointItem multiPointItem = this.l.get(size);
                IPoint iPoint2 = multiPointItem.getIPoint();
                if (iPoint2 != null) {
                    if (this.k == null) {
                        return null;
                    }
                    if (this.u == null) {
                        this.u = new ah(0, 1, 0, 1);
                    }
                    this.u.a(iPoint2.x + this.k.a, iPoint2.x + this.k.c, iPoint2.y + this.k.b, iPoint2.y + this.k.d);
                    if (this.u.b(this.t)) {
                        return multiPointItem;
                    }
                }
            }
            return null;
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMultiPointOverlay
    public void setAnchor(float f, float f2) {
        this.f = f;
        this.g = f2;
        d();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMultiPointOverlay
    public String getId() throws RemoteException {
        if (this.B == null) {
            this.B = a("MultiPointOverlay");
        }
        return this.B;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMultiPointOverlay
    public void remove(boolean z) {
        aj ajVar;
        this.D = false;
        this.v = 0;
        this.w = 0;
        BitmapDescriptor bitmapDescriptor = this.a;
        if (bitmapDescriptor != null) {
            bitmapDescriptor.recycle();
        }
        List<MultiPointItem> list = this.h;
        if (list != null) {
            list.clear();
            this.h = null;
        }
        ak akVar = this.i;
        if (akVar != null) {
            akVar.a();
            this.i = null;
        }
        List<MultiPointItem> list2 = this.l;
        if (list2 != null) {
            list2.clear();
        }
        ExecutorService executorService = this.F;
        if (executorService != null) {
            executorService.shutdownNow();
            this.F = null;
        }
        List<String> list3 = this.G;
        if (list3 != null) {
            list3.clear();
        }
        List<ag> list4 = this.o;
        if (list4 != null) {
            for (ag agVar : list4) {
                if (agVar != null) {
                    agVar.c();
                }
            }
            this.o.clear();
        }
        if (z && (ajVar = this.n) != null) {
            ajVar.a(this);
        }
        this.n.d();
        this.n = null;
        this.C = null;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMultiPointOverlay
    public void setVisible(boolean z) {
        if (this.D != z) {
            d();
        }
        this.D = z;
    }

    private void d() {
        aj ajVar = this.n;
        if (ajVar != null) {
            ajVar.d();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMultiPointOverlay
    public void destroy(boolean z) {
        remove(z);
        BitmapDescriptor bitmapDescriptor = this.b;
        if (bitmapDescriptor != null) {
            bitmapDescriptor.recycle();
        }
    }
}
