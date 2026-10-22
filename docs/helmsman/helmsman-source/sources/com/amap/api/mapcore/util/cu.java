package com.amap.api.mapcore.util;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.opengl.GLES20;
import com.amap.api.maps.model.TileOverlayOptions;
import com.amap.api.maps.model.TileProvider;
import com.autonavi.ae.gmap.GLMapState;
import com.autonavi.amap.mapcore.FPoint;
import com.autonavi.amap.mapcore.IPoint;
import com.autonavi.amap.mapcore.interfaces.ITileOverlay;
import java.lang.ref.WeakReference;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: compiled from: TileOverlayDelegateImp.java */
/* JADX INFO: loaded from: classes.dex */
public class cu implements cm {
    private static int h;
    cw.e a;
    private u b;
    private TileProvider c;
    private Float d;
    private boolean e;
    private boolean f;
    private k g;
    private int i;
    private int j;
    private int k;
    private dy l;
    private List<a> m;
    private boolean n;
    private b o;
    private String p;
    private FloatBuffer q;

    private static String a(String str) {
        h++;
        return str + h;
    }

    public cu(TileOverlayOptions tileOverlayOptions, u uVar) {
        this.f = false;
        this.i = 256;
        this.j = 256;
        this.k = -1;
        this.m = new ArrayList();
        this.n = false;
        this.o = null;
        this.p = null;
        this.q = null;
        this.b = uVar;
        TileProvider tileProvider = tileOverlayOptions.getTileProvider();
        this.c = tileProvider;
        this.i = tileProvider.getTileWidth();
        this.j = this.c.getTileHeight();
        this.q = dw.a(new float[]{0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f});
        this.d = Float.valueOf(tileOverlayOptions.getZIndex());
        this.e = tileOverlayOptions.isVisible();
        this.p = getId();
        this.g = this.b.a();
        this.k = Integer.parseInt(this.p.substring(11));
        try {
            dx.a aVar = new dx.a(this.b.e(), this.p);
            aVar.a(tileOverlayOptions.getMemoryCacheEnabled());
            aVar.b(tileOverlayOptions.getDiskCacheEnabled());
            aVar.a(tileOverlayOptions.getMemCacheSize());
            aVar.a(tileOverlayOptions.getDiskCacheSize());
            String diskCacheDir = tileOverlayOptions.getDiskCacheDir();
            if (diskCacheDir != null && !diskCacheDir.equals("")) {
                aVar.a(diskCacheDir);
            }
            dy dyVar = new dy(this.b.e(), this.i, this.j);
            this.l = dyVar;
            dyVar.a(this.c);
            this.l.a(aVar);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public cu(TileOverlayOptions tileOverlayOptions, u uVar, boolean z) {
        this(tileOverlayOptions, uVar);
        this.f = z;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.ITileOverlay
    public void remove() {
        this.b.b(this);
        this.g.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.ITileOverlay
    public void destroy(boolean z) {
        b bVar = this.o;
        if (bVar != null && bVar.a() == dd.d.RUNNING) {
            this.o.a(true);
        }
        synchronized (this.m) {
            int size = this.m.size();
            for (int i = 0; i < size; i++) {
                this.m.get(i).b();
            }
            this.m.clear();
        }
        dy dyVar = this.l;
        if (dyVar != null) {
            dyVar.g();
            this.l.a(true);
            this.l.a((TileProvider) null);
        }
        if (z) {
            remove();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.ITileOverlay
    public void clearTileCache() {
        dy dyVar = this.l;
        if (dyVar != null) {
            dyVar.f();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.ITileOverlay
    public String getId() {
        if (this.p == null) {
            this.p = a("TileOverlay");
        }
        return this.p;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.ITileOverlay
    public void setZIndex(float f) {
        this.d = Float.valueOf(f);
        this.b.d();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.ITileOverlay
    public float getZIndex() {
        return this.d.floatValue();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.ITileOverlay
    public void setVisible(boolean z) {
        this.e = z;
        this.g.setRunLowFrame(false);
        if (z) {
            a(true);
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.ITileOverlay
    public boolean isVisible() {
        return this.e;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.ITileOverlay
    public boolean equalsRemote(ITileOverlay iTileOverlay) {
        return equals(iTileOverlay) || iTileOverlay.getId().equals(getId());
    }

    @Override // com.autonavi.amap.mapcore.interfaces.ITileOverlay
    public int hashCodeRemote() {
        return super.hashCode();
    }

    private boolean a(a aVar) {
        GLMapState gLMapStateB = this.g.b();
        float f = aVar.c;
        int i = this.i;
        int i2 = this.j;
        int i3 = aVar.e.x;
        int i4 = 1 << (20 - ((int) f));
        int i5 = i2 * i4;
        int i6 = aVar.e.y + i5;
        FPoint fPointObtain = FPoint.obtain();
        gLMapStateB.geo2Map(i3, i6, fPointObtain);
        FPoint fPointObtain2 = FPoint.obtain();
        int i7 = (i4 * i) + i3;
        gLMapStateB.geo2Map(i7, i6, fPointObtain2);
        FPoint fPointObtain3 = FPoint.obtain();
        int i8 = i6 - i5;
        gLMapStateB.geo2Map(i7, i8, fPointObtain3);
        FPoint fPointObtain4 = FPoint.obtain();
        gLMapStateB.geo2Map(i3, i8, fPointObtain4);
        float[] fArr = {fPointObtain.x, fPointObtain.y, 0.0f, fPointObtain2.x, fPointObtain2.y, 0.0f, fPointObtain3.x, fPointObtain3.y, 0.0f, fPointObtain4.x, fPointObtain4.y, 0.0f};
        if (aVar.h == null) {
            aVar.h = dw.a(fArr);
        } else {
            aVar.h = dw.a(fArr, aVar.h);
        }
        fPointObtain.recycle();
        fPointObtain4.recycle();
        fPointObtain2.recycle();
        fPointObtain3.recycle();
        return true;
    }

    @Override // com.amap.api.mapcore.util.cm
    public void a() {
        List<a> list = this.m;
        if (list != null) {
            synchronized (list) {
                if (this.m.size() == 0) {
                    return;
                }
                int size = this.m.size();
                for (int i = 0; i < size; i++) {
                    a aVar = this.m.get(i);
                    if (!aVar.g) {
                        try {
                            IPoint iPoint = aVar.e;
                            if (aVar.i != null && !aVar.i.isRecycled() && iPoint != null) {
                                aVar.f = dw.a(aVar.i);
                                if (aVar.f != 0) {
                                    aVar.g = true;
                                }
                                aVar.i = null;
                            }
                        } catch (Throwable th) {
                            fn.b(th, "TileOverlayDelegateImp", "drawTiles");
                        }
                    }
                    if (aVar.g) {
                        a(aVar);
                        a(aVar.f, aVar.h, this.q);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:34:0x01a9  */
    public static ArrayList<a> b(k kVar, int i, int i2, int i3, int i4, u uVar, dy dyVar) {
        int i5;
        int i6;
        boolean z;
        IPoint iPoint;
        boolean z2;
        int i7;
        GLMapState gLMapStateB = kVar.b();
        Rect rect = kVar.getMapConfig().getGeoRectangle().getRect();
        IPoint iPointObtain = IPoint.obtain();
        IPoint iPointObtain2 = IPoint.obtain();
        iPointObtain.x = rect.left;
        iPointObtain.y = rect.top;
        int iMin = Math.min(Integer.MAX_VALUE, iPointObtain.x);
        int iMax = Math.max(0, iPointObtain.x);
        int iMin2 = Math.min(Integer.MAX_VALUE, iPointObtain.y);
        int iMax2 = Math.max(0, iPointObtain.y);
        iPointObtain.x = rect.right;
        iPointObtain.y = rect.top;
        int iMin3 = Math.min(iMin, iPointObtain.x);
        int iMax3 = Math.max(iMax, iPointObtain.x);
        int iMin4 = Math.min(iMin2, iPointObtain.y);
        int iMax4 = Math.max(iMax2, iPointObtain.y);
        iPointObtain.x = rect.left;
        iPointObtain.y = rect.bottom;
        int iMin5 = Math.min(iMin3, iPointObtain.x);
        int iMax5 = Math.max(iMax3, iPointObtain.x);
        int iMin6 = Math.min(iMin4, iPointObtain.y);
        int iMax6 = Math.max(iMax4, iPointObtain.y);
        iPointObtain.x = rect.right;
        iPointObtain.y = rect.bottom;
        int iMin7 = Math.min(iMin5, iPointObtain.x);
        int iMax7 = Math.max(iMax5, iPointObtain.x);
        int iMin8 = Math.min(iMin6, iPointObtain.y);
        int iMax8 = Math.max(iMax6, iPointObtain.y);
        int i8 = 20 - i;
        int i9 = 1 << i8;
        int i10 = iMin7 - (i9 * i2);
        gLMapStateB.getGeoCenter(iPointObtain2);
        int i11 = (iPointObtain2.x >> i8) / i2;
        int i12 = (iPointObtain2.y >> i8) / i3;
        int i13 = iMin8 - (i9 * i3);
        int i14 = iMax8;
        a aVar = new a(i11, i12, i, i4, kVar, uVar, dyVar);
        aVar.e = IPoint.obtain((i11 << i8) * i2, (i12 << i8) * i3);
        iPointObtain.recycle();
        iPointObtain2.recycle();
        ArrayList<a> arrayList = new ArrayList<>();
        arrayList.add(aVar);
        int i15 = 1;
        while (true) {
            int i16 = i11 - i15;
            int i17 = i16;
            boolean z3 = false;
            while (true) {
                i5 = i11 + i15;
                if (i17 > i5) {
                    break;
                }
                int i18 = i12 + i15;
                int i19 = (i17 << i8) * i2;
                IPoint iPoint2 = new IPoint(i19, (i18 << i8) * i3);
                if (iPoint2.x >= iMax7 || iPoint2.x <= i10) {
                    i6 = i14;
                } else {
                    int i20 = i14;
                    if (iPoint2.y < i20) {
                        int i21 = i13;
                        if (iPoint2.y > i21) {
                            if (!z3) {
                                z3 = true;
                            }
                            int i22 = i17;
                            i11 = i11;
                            i16 = i16;
                            i6 = i20;
                            i17 = i17;
                            i19 = i19;
                            i13 = i21;
                            a aVar2 = new a(i22, i18, i, i4, kVar, uVar, dyVar);
                            aVar2.e = iPoint2;
                            arrayList.add(aVar2);
                            z = z3;
                        } else {
                            i16 = i16;
                            i17 = i17;
                            i13 = i21;
                            i6 = i20;
                            i19 = i19;
                            z = z3;
                        }
                    } else {
                        i6 = i20;
                    }
                    int i23 = i12 - i15;
                    iPoint = new IPoint(i19, (i23 << i8) * i3);
                    if (iPoint.x < iMax7 || iPoint.x <= i10 || iPoint.y >= i6) {
                        z2 = z;
                        i7 = i13;
                    } else {
                        int i24 = i13;
                        if (iPoint.y > i24) {
                            if (!z) {
                                z = true;
                            }
                            boolean z4 = z;
                            i7 = i24;
                            a aVar3 = new a(i17, i23, i, i4, kVar, uVar, dyVar);
                            aVar3.e = iPoint;
                            arrayList.add(aVar3);
                            z2 = z4;
                        } else {
                            z2 = z;
                            i7 = i24;
                        }
                    }
                    i17++;
                    i14 = i6;
                    i16 = i16;
                    int i25 = i7;
                    z3 = z2;
                    i11 = i11;
                    i13 = i25;
                }
                z = z3;
                int i26 = i12 - i15;
                iPoint = new IPoint(i19, (i26 << i8) * i3);
                if (iPoint.x < iMax7) {
                    z2 = z;
                    i7 = i13;
                } else {
                    z2 = z;
                    i7 = i13;
                }
                i17++;
                i14 = i6;
                i16 = i16;
                int i27 = i7;
                z3 = z2;
                i11 = i11;
                i13 = i27;
            }
            int i28 = i16;
            int i29 = i14;
            int i30 = i13;
            int i31 = i11;
            boolean z5 = z3;
            int i32 = (i12 + i15) - 1;
            while (i32 > i12 - i15) {
                int i33 = (i32 << i8) * i3;
                IPoint iPoint3 = new IPoint((i5 << i8) * i2, i33);
                if (iPoint3.x < iMax7 && iPoint3.x > i10 && iPoint3.y < i29 && iPoint3.y > i30) {
                    if (!z5) {
                        z5 = true;
                    }
                    a aVar4 = new a(i5, i32, i, i4, kVar, uVar, dyVar);
                    aVar4.e = iPoint3;
                    arrayList.add(aVar4);
                    z5 = z5;
                }
                IPoint iPoint4 = new IPoint((i28 << i8) * i2, i33);
                if (iPoint4.x < iMax7 && iPoint4.x > i10 && iPoint4.y < i29 && iPoint4.y > i30) {
                    if (!z5) {
                        z5 = true;
                    }
                    a aVar5 = new a(i28, i32, i, i4, kVar, uVar, dyVar);
                    aVar5.e = iPoint4;
                    arrayList.add(aVar5);
                }
                i32--;
                i15 = i15;
                i5 = i5;
            }
            int i34 = i15;
            if (!z5) {
                return arrayList;
            }
            i15 = i34 + 1;
            i14 = i29;
            i11 = i31;
            i13 = i30;
        }
    }

    public void b() {
        List<a> list = this.m;
        if (list != null) {
            synchronized (list) {
                this.m.clear();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean b(k kVar, List<a> list, int i, boolean z, List<a> list2, boolean z2, u uVar, dy dyVar) {
        int size;
        if (list == null || list2 == null) {
            return false;
        }
        synchronized (list2) {
            Iterator<a> it = list2.iterator();
            while (true) {
                boolean z3 = true;
                if (!it.hasNext()) {
                    break;
                }
                a next = it.next();
                Iterator<a> it2 = list.iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        z3 = false;
                        break;
                    }
                    a next2 = it2.next();
                    if (next.equals(next2) && next.g) {
                        next2.g = next.g;
                        next2.f = next.f;
                        break;
                    }
                }
                if (!z3) {
                    next.b();
                }
            }
            list2.clear();
        }
        if (i > ((int) kVar.getMaxZoomLevel()) || i < ((int) kVar.getMinZoomLevel()) || (size = list.size()) <= 0) {
            return false;
        }
        for (int i2 = 0; i2 < size; i2++) {
            a aVar = list.get(i2);
            if (aVar != null && (!z2 || (aVar.c >= 10 && !dr.a(aVar.a, aVar.b, aVar.c)))) {
                list2.add(aVar);
                if (!aVar.g && dyVar != null) {
                    dyVar.a(z, aVar);
                }
            }
        }
        return true;
    }

    @Override // com.amap.api.mapcore.util.cm
    public void a(boolean z) {
        if (this.n) {
            return;
        }
        b bVar = this.o;
        if (bVar != null && bVar.a() == dd.d.RUNNING) {
            this.o.a(true);
        }
        b bVar2 = new b(z, this.g, this.i, this.j, this.k, this.m, this.f, this.b, this.l);
        this.o = bVar2;
        bVar2.c((Object[]) new Void[0]);
    }

    @Override // com.amap.api.mapcore.util.cm
    public void b(boolean z) {
        if (this.n != z) {
            this.n = z;
            dy dyVar = this.l;
            if (dyVar != null) {
                dyVar.a(z);
            }
        }
    }

    /* JADX INFO: compiled from: TileOverlayDelegateImp.java */
    private static class b extends dd<Void, Void, List<a>> {
        private int d;
        private boolean e;
        private int f;
        private int g;
        private int h;
        private WeakReference<k> i;
        private List<a> j;
        private boolean k;
        private WeakReference<u> l;
        private WeakReference<dy> m;

        public b(boolean z, k kVar, int i, int i2, int i3, List<a> list, boolean z2, u uVar, dy dyVar) {
            this.f = 256;
            this.g = 256;
            this.h = 0;
            this.e = z;
            this.i = new WeakReference<>(kVar);
            this.f = i;
            this.g = i2;
            this.h = i3;
            this.j = list;
            this.k = z2;
            this.l = new WeakReference<>(uVar);
            this.m = new WeakReference<>(dyVar);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.amap.api.mapcore.util.dd
        public List<a> a(Void... voidArr) {
            try {
                k kVar = this.i.get();
                if (kVar == null) {
                    return null;
                }
                int mapWidth = kVar.getMapWidth();
                int mapHeight = kVar.getMapHeight();
                int iG = (int) kVar.g();
                this.d = iG;
                if (mapWidth > 0 && mapHeight > 0) {
                    return cu.b(kVar, iG, this.f, this.g, this.h, this.l.get(), this.m.get());
                }
                return null;
            } catch (Throwable th) {
                th.printStackTrace();
                return null;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.amap.api.mapcore.util.dd
        public void a(List<a> list) {
            if (list == null) {
                return;
            }
            try {
                if (list.size() <= 0) {
                    return;
                }
                cu.b(this.i.get(), list, this.d, this.e, this.j, this.k, this.l.get(), this.m.get());
                list.clear();
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    /* JADX INFO: compiled from: TileOverlayDelegateImp.java */
    public static class a implements Cloneable {
        public int a;
        public int b;
        public int c;
        public int d;
        public IPoint e;
        public int f;
        public boolean g;
        public FloatBuffer h;
        public Bitmap i;
        public ea.a j;
        public int k;
        private final int l;
        private k m;
        private u n;
        private dy o;

        public a(int i, int i2, int i3, int i4, k kVar, u uVar, dy dyVar) {
            this.f = 0;
            this.g = false;
            this.h = null;
            this.i = null;
            this.j = null;
            this.k = 0;
            this.l = 3;
            this.a = i;
            this.b = i2;
            this.c = i3;
            this.d = i4;
            this.m = kVar;
            this.n = uVar;
            this.o = dyVar;
        }

        public a(a aVar) {
            this.f = 0;
            this.g = false;
            this.h = null;
            this.i = null;
            this.j = null;
            this.k = 0;
            this.l = 3;
            this.a = aVar.a;
            this.b = aVar.b;
            this.c = aVar.c;
            this.d = aVar.d;
            this.e = aVar.e;
            this.h = aVar.h;
            this.k = 0;
            this.n = aVar.n;
            this.m = aVar.m;
            this.o = aVar.o;
        }

        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public a clone() {
            try {
                a aVar = (a) super.clone();
                aVar.a = this.a;
                aVar.b = this.b;
                aVar.c = this.c;
                aVar.d = this.d;
                aVar.e = (IPoint) this.e.clone();
                aVar.h = this.h.asReadOnlyBuffer();
                this.k = 0;
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return new a(this);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof a)) {
                return false;
            }
            a aVar = (a) obj;
            return this.a == aVar.a && this.b == aVar.b && this.c == aVar.c && this.d == aVar.d;
        }

        public int hashCode() {
            return (this.a * 7) + (this.b * 11) + (this.c * 13) + this.d;
        }

        public String toString() {
            return this.a + "-" + this.b + "-" + this.c + "-" + this.d;
        }

        /* JADX WARN: Code duplicated, block: B:16:0x0031 A[Catch: all -> 0x0041, TryCatch #0 {, blocks: (B:5:0x0005, B:11:0x0018, B:13:0x0026, B:15:0x002d, B:16:0x0031, B:18:0x0035, B:20:0x003c, B:8:0x000c), top: B:26:0x0005, inners: #1 }] */
        /* JADX WARN: Code duplicated, block: B:18:0x0035 A[Catch: all -> 0x0041, TryCatch #0 {, blocks: (B:5:0x0005, B:11:0x0018, B:13:0x0026, B:15:0x002d, B:16:0x0031, B:18:0x0035, B:20:0x003c, B:8:0x000c), top: B:26:0x0005, inners: #1 }] */
        /* JADX WARN: Code duplicated, block: B:20:0x003c A[Catch: all -> 0x0041, TRY_LEAVE, TryCatch #0 {, blocks: (B:5:0x0005, B:11:0x0018, B:13:0x0026, B:15:0x002d, B:16:0x0031, B:18:0x0035, B:20:0x003c, B:8:0x000c), top: B:26:0x0005, inners: #1 }] */
        public synchronized void a(Bitmap bitmap) {
            int i;
            dy dyVar;
            if (bitmap != null) {
                if (!bitmap.isRecycled()) {
                    try {
                        this.j = null;
                        this.i = bitmap;
                        this.m.setRunLowFrame(false);
                    } catch (Throwable th) {
                        fn.b(th, "TileOverlayDelegateImp", "setBitmap");
                        th.printStackTrace();
                        int i2 = this.k;
                        if (i2 < 3) {
                            this.k = i2 + 1;
                            dy dyVar2 = this.o;
                            if (dyVar2 != null) {
                                dyVar2.a(true, this);
                            }
                        }
                    }
                } else {
                    i = this.k;
                    if (i < 3) {
                        this.k = i + 1;
                        dyVar = this.o;
                        if (dyVar != null) {
                            dyVar.a(true, this);
                        }
                    }
                }
            } else {
                i = this.k;
                if (i < 3) {
                    this.k = i + 1;
                    dyVar = this.o;
                    if (dyVar != null) {
                        dyVar.a(true, this);
                    }
                }
            }
            throw th;
        }

        public void b() {
            try {
                ea.a(this);
                if (this.g) {
                    this.n.a(this.f);
                }
                this.g = false;
                this.f = 0;
                Bitmap bitmap = this.i;
                if (bitmap != null && !bitmap.isRecycled()) {
                    this.i.recycle();
                }
                this.i = null;
                FloatBuffer floatBuffer = this.h;
                if (floatBuffer != null) {
                    floatBuffer.clear();
                }
                this.h = null;
                this.j = null;
                this.k = 0;
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    private void c() {
        u uVar = this.b;
        if (uVar == null || uVar.a() == null) {
            return;
        }
        this.a = (cw.e) this.b.a().s(0);
    }

    private void a(int i, FloatBuffer floatBuffer, FloatBuffer floatBuffer2) {
        if (floatBuffer == null || floatBuffer2 == null || i == 0) {
            return;
        }
        cw.e eVar = this.a;
        if (eVar == null || eVar.c()) {
            c();
        }
        this.a.a();
        GLES20.glEnable(3042);
        GLES20.glBlendFunc(1, 771);
        GLES20.glBlendColor(1.0f, 1.0f, 1.0f, 1.0f);
        GLES20.glBindTexture(3553, i);
        GLES20.glEnableVertexAttribArray(this.a.b);
        GLES20.glVertexAttribPointer(this.a.b, 3, 5126, false, 12, (Buffer) floatBuffer);
        GLES20.glEnableVertexAttribArray(this.a.c);
        GLES20.glVertexAttribPointer(this.a.c, 2, 5126, false, 8, (Buffer) floatBuffer2);
        GLES20.glUniformMatrix4fv(this.a.a, 1, false, this.b.g(), 0);
        GLES20.glDrawArrays(6, 0, 4);
        GLES20.glDisableVertexAttribArray(this.a.b);
        GLES20.glDisableVertexAttribArray(this.a.c);
        GLES20.glBindTexture(3553, 0);
        GLES20.glUseProgram(0);
        GLES20.glDisable(3042);
    }
}
