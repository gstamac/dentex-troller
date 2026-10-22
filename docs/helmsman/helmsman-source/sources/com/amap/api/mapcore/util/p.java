package com.amap.api.mapcore.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.opengl.GLES20;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import android.view.MotionEvent;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Text;
import com.amap.api.maps.model.TextOptions;
import com.autonavi.amap.mapcore.IPoint;
import com.autonavi.amap.mapcore.MapConfig;
import com.autonavi.amap.mapcore.interfaces.IMarker;
import java.io.Serializable;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/* JADX INFO: compiled from: MapOverlayImageView.java */
/* JADX INFO: loaded from: classes.dex */
public class p {
    k a;
    public cw.c c;
    private IPoint k;
    private cf l;
    private cf m;
    private FloatBuffer p;
    private List<ci> f = new ArrayList(500);
    private List<r> g = new ArrayList();
    private List<ci> h = new ArrayList();
    private a i = new a();
    private boolean j = true;
    private Handler q = new Handler(Looper.getMainLooper());
    private int[] r = new int[1];
    float[] b = new float[180000];
    int d = 0;
    int e = 0;
    private Runnable s = new Runnable() { // from class: com.amap.api.mapcore.util.p.2
        @Override // java.lang.Runnable
        public void run() {
            synchronized (p.this.f) {
                p.this.j();
            }
        }
    };
    private ec n = new ec(512, 1024);
    private Cdo o = new Cdo();

    public p(Context context, k kVar) {
        this.a = kVar;
    }

    public Marker a(MarkerOptions markerOptions) throws RemoteException {
        Marker marker;
        if (markerOptions == null) {
            return null;
        }
        cn cnVar = new cn(markerOptions, this);
        synchronized (this.f) {
            d(cnVar);
            marker = new Marker(cnVar);
        }
        return marker;
    }

    public ArrayList<Marker> a(ArrayList<MarkerOptions> arrayList, boolean z) throws RemoteException {
        MarkerOptions markerOptions;
        if (arrayList == null || arrayList.size() == 0) {
            return null;
        }
        ArrayList<Marker> arrayList2 = new ArrayList<>();
        try {
            if (arrayList.size() == 1 && (markerOptions = arrayList.get(0)) != null) {
                arrayList2.add(a(markerOptions));
                if (z && markerOptions.getPosition() != null) {
                    this.a.a(z.a(markerOptions.getPosition(), 18.0f));
                }
            } else {
                final LatLngBounds.Builder builder = LatLngBounds.builder();
                for (int i = 0; i < arrayList.size(); i++) {
                    MarkerOptions markerOptions2 = arrayList.get(i);
                    if (arrayList.get(i) != null) {
                        arrayList2.add(a(markerOptions2));
                        if (markerOptions2.getPosition() != null) {
                            builder.include(markerOptions2.getPosition());
                        }
                    }
                }
                if (z && arrayList2.size() > 0) {
                    this.a.getMainHandler().postDelayed(new Runnable() { // from class: com.amap.api.mapcore.util.p.1
                        @Override // java.lang.Runnable
                        public void run() {
                            try {
                                p.this.a.a(z.a(builder.build(), 50));
                            } catch (Throwable unused) {
                            }
                        }
                    }, 50L);
                }
            }
            return arrayList2;
        } catch (Throwable th) {
            fn.b(th, "AMapDelegateImpGLSurfaceView", "addMarkers");
            th.printStackTrace();
            return arrayList2;
        }
    }

    public Text a(TextOptions textOptions) throws RemoteException {
        Text text;
        if (textOptions == null) {
            return null;
        }
        synchronized (this.f) {
            ct ctVar = new ct(textOptions, this);
            d(ctVar);
            text = new Text(ctVar);
        }
        return text;
    }

    private void d(ci ciVar) {
        try {
            this.f.add(ciVar);
            f();
        } catch (Throwable th) {
            fn.b(th, "MapOverlayImageView", "addMarker");
        }
    }

    public boolean a(ci ciVar) {
        boolean zRemove;
        synchronized (this.f) {
            try {
                cf cfVar = this.m;
                if (cfVar != null && cfVar.getId().equals(ciVar.getId())) {
                    this.m = null;
                }
                b(ciVar);
            } catch (Throwable th) {
                th.printStackTrace();
            }
            zRemove = this.f.remove(ciVar);
        }
        return zRemove;
    }

    public void a(cf cfVar) {
        try {
            if (this.m != null) {
                if (cfVar != null && cfVar.getId().equals(this.m.getId())) {
                    return;
                } else {
                    this.m.b(false);
                }
            }
            if (this.f.contains(cfVar)) {
                cfVar.b(true);
                this.m = cfVar;
            }
        } catch (Throwable th) {
            fn.b(th, "MapOverlayImageView", "set2Top");
        }
    }

    public void b(cf cfVar) {
        if (this.k == null) {
            this.k = IPoint.obtain();
        }
        Rect rectH = cfVar.h();
        this.k = IPoint.obtain(rectH.left + (rectH.width() / 2), rectH.top);
        this.l = cfVar;
        try {
            this.a.a(cfVar);
        } catch (Throwable th) {
            fn.b(th, "MapOverlayImageView", "showInfoWindow");
            th.printStackTrace();
        }
    }

    public void b(ci ciVar) {
        try {
            if (ciVar.isInfoWindowShown()) {
                this.a.i();
                this.l = null;
            } else {
                cf cfVar = this.l;
                if (cfVar != null && cfVar.getId() == ciVar.getId()) {
                    this.l = null;
                }
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void j() {
        try {
            Collections.sort(this.f, this.i);
        } catch (Throwable th) {
            fn.b(th, "MapOverlayImageView", "changeOverlayIndex");
        }
    }

    public void a() {
        try {
            float mapPerPixelUnitLength = this.a.getMapConfig().getMapPerPixelUnitLength();
            synchronized (this.f) {
                h();
                if (this.f.size() == 0) {
                    return;
                }
                this.h.clear();
                int size = this.f.size();
                for (int i = 0; i < size; i++) {
                    ci ciVar = this.f.get(i);
                    if (ciVar.isVisible() && !ciVar.l()) {
                        this.j = ciVar.i();
                        if (ciVar.j() || ciVar.isInfoWindowShown()) {
                            try {
                                this.h.add(ciVar);
                            } catch (Throwable th) {
                                th.printStackTrace();
                            }
                        }
                    }
                }
                cf cfVar = this.m;
                if (cfVar != null && cfVar.isVisible()) {
                    this.h.add(this.m);
                }
                if (this.h.size() > 0) {
                    int size2 = this.h.size();
                    int i2 = 0;
                    int iK = 0;
                    int i3 = 0;
                    int i4 = 0;
                    for (int i5 = 0; i5 < size2; i5++) {
                        ci ciVar2 = this.h.get(i5);
                        synchronized (ciVar2) {
                            ciVar2.a(this.a);
                            if (i5 == 0) {
                                iK = ciVar2.k();
                            } else {
                                int iK2 = ciVar2.k();
                                if (iK2 != iK) {
                                    a(iK, i2, i3, i4);
                                    i2 = 0;
                                    i3 = 0;
                                    i4 = 0;
                                    iK = iK2;
                                }
                            }
                            ciVar2.a(this.a, this.b, i4, mapPerPixelUnitLength);
                            int iK3 = ciVar2.k();
                            if (iK3 != iK) {
                                a(iK, i2, i3, i4);
                                i2 = 0;
                                i3 = i4;
                                iK = iK3;
                                i4 = 0;
                            }
                            i4 += 36;
                            i2++;
                            if (i2 == 5000) {
                                a(iK, i2, i3, i4);
                                i2 = 0;
                                i3 = 0;
                                i4 = 0;
                            }
                        }
                    }
                    if (i2 > 0) {
                        a(iK, i2, i3, i4);
                    }
                }
            }
        } catch (Throwable th2) {
            th2.printStackTrace();
        }
    }

    private void a(int i, int i2, int i3, int i4) {
        if (i2 == 0 || i == 0) {
            return;
        }
        FloatBuffer floatBufferC = this.o.c(i2 * 36);
        this.p = floatBufferC;
        floatBufferC.put(this.b, i3, i4);
        this.p.flip();
        a(i2);
        a(i, i4, i2, this.p, this.a.getMapConfig());
        this.o.a();
    }

    private void a(int i) {
        if (i > 5000) {
            i = 5000;
        }
        if (this.d == 0) {
            int[] iArr = new int[2];
            GLES20.glGenBuffers(2, iArr, 0);
            this.d = iArr[0];
            this.e = iArr[1];
            ShortBuffer shortBufferB = this.o.b(30000);
            short[] sArr = new short[30000];
            for (int i2 = 0; i2 < 5000; i2++) {
                int i3 = i2 * 6;
                int i4 = i2 * 4;
                short s = (short) (i4 + 0);
                sArr[i3 + 0] = s;
                sArr[i3 + 1] = (short) (i4 + 1);
                short s2 = (short) (i4 + 2);
                sArr[i3 + 2] = s2;
                sArr[i3 + 3] = s;
                sArr[i3 + 4] = s2;
                sArr[i3 + 5] = (short) (i4 + 3);
            }
            shortBufferB.put(sArr);
            shortBufferB.flip();
            GLES20.glBindBuffer(34963, this.e);
            GLES20.glBufferData(34963, 60000, shortBufferB, 35044);
        }
        GLES20.glBindBuffer(34962, this.d);
        GLES20.glBufferData(34962, i * 36 * 4, this.p, 35044);
    }

    private void k() {
        k kVar;
        if (this.c != null || (kVar = this.a) == null) {
            return;
        }
        this.c = (cw.c) kVar.s(1);
    }

    private void a(int i, int i2, int i3, FloatBuffer floatBuffer, MapConfig mapConfig) {
        if (i == 0 || floatBuffer == null || i3 == 0) {
            return;
        }
        cw.c cVar = this.c;
        if (cVar == null || cVar.c()) {
            k();
        }
        this.c.a();
        GLES20.glUniform1f(this.c.h, mapConfig.getS_r());
        GLES20.glEnableVertexAttribArray(this.c.b);
        GLES20.glBindBuffer(34962, this.d);
        GLES20.glVertexAttribPointer(this.c.b, 4, 5126, false, 36, 0);
        GLES20.glEnable(3042);
        GLES20.glBlendFunc(1, 771);
        GLES20.glBindTexture(3553, i);
        GLES20.glEnableVertexAttribArray(this.c.c);
        GLES20.glBindBuffer(34962, this.d);
        GLES20.glVertexAttribPointer(this.c.c, 2, 5126, false, 36, 16);
        GLES20.glEnableVertexAttribArray(this.c.g);
        GLES20.glBindBuffer(34962, this.d);
        GLES20.glVertexAttribPointer(this.c.g, 3, 5126, false, 36, 24);
        GLES20.glUniformMatrix4fv(this.c.a, 1, false, b(), 0);
        GLES20.glBindBuffer(34963, this.e);
        GLES20.glDrawElements(4, i3 * 6, 5123, 0);
        GLES20.glBindBuffer(34962, 0);
        GLES20.glBindBuffer(34963, 0);
        GLES20.glBindTexture(3553, 0);
        GLES20.glDisableVertexAttribArray(this.c.b);
        GLES20.glDisableVertexAttribArray(this.c.c);
        GLES20.glDisable(3042);
        GLES20.glUseProgram(0);
    }

    public synchronized boolean a(Bitmap bitmap, r rVar) {
        ec.c cVarA = this.n.a(bitmap.getWidth(), bitmap.getHeight(), rVar.j());
        if (cVarA == null) {
            return false;
        }
        rVar.b(cVarA.a / this.n.a());
        rVar.a(cVarA.b / this.n.b());
        rVar.c((cVarA.a + cVarA.c) / this.n.a());
        rVar.d((cVarA.b + cVarA.d) / this.n.b());
        return true;
    }

    public float[] b() {
        k kVar = this.a;
        return kVar != null ? kVar.v() : new float[16];
    }

    public k c() {
        return this.a;
    }

    public cf d() {
        return this.l;
    }

    public cf a(MotionEvent motionEvent) {
        synchronized (this.f) {
            for (int size = this.f.size() - 1; size >= 0; size--) {
                ci ciVar = this.f.get(size);
                if ((ciVar instanceof cn) && dw.a(ciVar.h(), (int) motionEvent.getX(), (int) motionEvent.getY())) {
                    cf cfVar = (cf) ciVar;
                    this.l = cfVar;
                    return cfVar;
                }
            }
            return null;
        }
    }

    public boolean b(MotionEvent motionEvent) throws RemoteException {
        boolean zA;
        Rect rectH;
        synchronized (this.f) {
            for (int size = this.f.size() - 1; size >= 0; size--) {
                ci ciVar = this.f.get(size);
                if ((ciVar instanceof cn) && ciVar.isVisible() && ((cn) ciVar).isClickable() && (zA = dw.a((rectH = ciVar.h()), (int) motionEvent.getX(), (int) motionEvent.getY()))) {
                    this.k = IPoint.obtain(rectH.left + (rectH.width() / 2), rectH.top);
                    this.l = (cf) ciVar;
                }
            }
            zA = false;
        }
        return zA;
    }

    public List<Marker> e() {
        ArrayList arrayList;
        synchronized (this.f) {
            arrayList = new ArrayList();
            try {
                for (ci ciVar : this.f) {
                    if ((ciVar instanceof cn) && ciVar.j()) {
                        arrayList.add(new Marker((IMarker) ciVar));
                    }
                }
            } catch (Throwable th) {
                fn.b(th, "MapOverlayImageView", "getMapScreenMarkers");
                th.printStackTrace();
            }
        }
        return arrayList;
    }

    public void f() {
        this.q.removeCallbacks(this.s);
        this.q.postDelayed(this.s, 10L);
    }

    public boolean c(ci ciVar) {
        boolean zContains;
        synchronized (this.f) {
            zContains = this.f.contains(ciVar);
        }
        return zContains;
    }

    protected int g() {
        int size;
        synchronized (this.f) {
            size = this.f.size();
        }
        return size;
    }

    public void a(String str) {
        boolean z;
        if (str != null) {
            try {
                z = str.trim().length() == 0;
            } catch (Throwable th) {
                fn.b(th, "MapOverlayImageView", "clear");
                th.printStackTrace();
                return;
            }
        }
        ci ciVar = null;
        this.l = null;
        this.k = null;
        this.m = null;
        synchronized (this.f) {
            this.h.clear();
            if (z) {
                this.f.clear();
            } else {
                int size = this.f.size();
                for (int i = 0; i < size; i++) {
                    ci ciVar2 = this.f.get(i);
                    if (str.equals(ciVar2.getId())) {
                        ciVar = ciVar2;
                    } else {
                        ciVar2.remove();
                    }
                }
                this.f.clear();
                if (ciVar != null) {
                    this.f.add(ciVar);
                }
            }
        }
    }

    public void a(r rVar) {
        synchronized (this.g) {
            if (rVar != null) {
                this.g.add(rVar);
            }
        }
    }

    public void h() {
        synchronized (this.g) {
            int iE = this.a.e();
            for (int i = 0; i < this.g.size(); i++) {
                r rVar = this.g.get(i);
                if (rVar != null) {
                    rVar.h();
                    if (rVar.i() == 0) {
                        if (rVar.f() == iE) {
                            this.n.a(rVar.j());
                        } else {
                            this.r[0] = rVar.f();
                            GLES20.glDeleteTextures(1, this.r, 0);
                        }
                    }
                }
            }
        }
    }

    public void i() {
        try {
            for (ci ciVar : this.f) {
                if (ciVar != null) {
                    ciVar.destroy(false);
                }
            }
            a((String) null);
        } catch (Throwable th) {
            fn.b(th, "MapOverlayImageView", "destroy");
            th.printStackTrace();
            Log.d("amapApi", "MapOverlayImageView clear erro" + th.getMessage());
        }
    }

    /* JADX INFO: compiled from: MapOverlayImageView.java */
    static class a implements Serializable, Comparator<Object> {
        a() {
        }

        @Override // java.util.Comparator
        public int compare(Object obj, Object obj2) {
            ci ciVar = (ci) obj;
            ci ciVar2 = (ci) obj2;
            if (ciVar == null || ciVar2 == null) {
                return 0;
            }
            try {
                if (ciVar.getZIndex() > ciVar2.getZIndex()) {
                    return 1;
                }
                return ciVar.getZIndex() < ciVar2.getZIndex() ? -1 : 0;
            } catch (Throwable th) {
                fn.b(th, "MapOverlayImageView", "compare");
                th.printStackTrace();
                return 0;
            }
        }
    }
}
