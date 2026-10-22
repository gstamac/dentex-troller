package com.amap.api.mapcore.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.RemoteException;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.Marker;
import com.autonavi.ae.gmap.listener.MapWidgetListener;
import com.autonavi.amap.mapcore.FPoint;
import com.autonavi.amap.mapcore.IPoint;

/* JADX INFO: compiled from: MapOverlayViewGroup.java */
/* JADX INFO: loaded from: classes.dex */
public class ej extends ViewGroup implements ad {
    ae a;
    int b;
    int c;
    private k d;
    private Context e;
    private el f;
    private ei g;
    private eg h;
    private ek i;
    private ef j;
    private eh k;
    private em l;
    private View m;
    private cf n;
    private Drawable o;
    private boolean p;
    private View q;
    private boolean r;

    public ej(Context context, k kVar) {
        super(context);
        this.o = null;
        this.p = true;
        this.b = 0;
        this.c = 0;
        try {
            this.d = kVar;
            this.e = context;
            setBackgroundColor(-1);
            a(context);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private void a(Context context) {
        this.f = new el(context, this.d);
        this.i = new ek(context, this.d);
        this.j = new ef(context);
        this.k = new eh(context);
        this.l = new em(context, this.d);
        this.g = new ei(context, this.d);
        this.h = new eg(context, this.d);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-1, -1);
        int i = 0;
        if (this.d.l() != null) {
            addView(this.d.l(), 0, layoutParams);
            i = 1;
        }
        addView(this.j, i, layoutParams);
        addView(this.f, layoutParams);
        addView(this.i, layoutParams);
        addView(this.k, new ViewGroup.LayoutParams(-2, -2));
        addView(this.l, new a(-2, -2, new FPoint(0.0f, 0.0f), 0, 0, 83));
        addView(this.g, new a(-2, -2, FPoint.obtain(0.0f, 0.0f), 0, 0, 83));
        addView(this.h, new a(-2, -2, FPoint.obtain(0.0f, 0.0f), 0, 0, 51));
        this.h.setVisibility(8);
        this.d.a(new MapWidgetListener() { // from class: com.amap.api.mapcore.util.ej.1
            @Override // com.autonavi.ae.gmap.listener.MapWidgetListener
            public void setFrontViewVisibility(boolean z) {
            }

            @Override // com.autonavi.ae.gmap.listener.MapWidgetListener
            public void invalidateScaleView() {
                if (ej.this.i == null) {
                    return;
                }
                ej.this.i.post(new Runnable() { // from class: com.amap.api.mapcore.util.ej.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        ej.this.i.b();
                    }
                });
            }

            @Override // com.autonavi.ae.gmap.listener.MapWidgetListener
            public void invalidateCompassView() {
                if (ej.this.h == null) {
                    return;
                }
                ej.this.h.post(new Runnable() { // from class: com.amap.api.mapcore.util.ej.1.2
                    @Override // java.lang.Runnable
                    public void run() {
                        ej.this.h.b();
                    }
                });
            }

            @Override // com.autonavi.ae.gmap.listener.MapWidgetListener
            public void invalidateZoomController(final float f) {
                if (ej.this.l == null) {
                    return;
                }
                ej.this.l.post(new Runnable() { // from class: com.amap.api.mapcore.util.ej.1.3
                    @Override // java.lang.Runnable
                    public void run() {
                        ej.this.l.a(f);
                    }
                });
            }
        });
        try {
            if (this.d.h().isMyLocationButtonEnabled()) {
                return;
            }
            this.g.setVisibility(8);
        } catch (Throwable th) {
            fn.b(th, "AMapDelegateImpGLSurfaceView", "locationView gone");
            th.printStackTrace();
        }
    }

    public void a(boolean z) {
        if (this.k != null && z && this.d.m()) {
            this.k.a(true);
        }
    }

    public void b(boolean z) {
        em emVar = this.l;
        if (emVar == null) {
            return;
        }
        emVar.a(z);
    }

    public void c(boolean z) {
        ei eiVar = this.g;
        if (eiVar == null) {
            return;
        }
        if (z) {
            eiVar.setVisibility(0);
        } else {
            eiVar.setVisibility(8);
        }
    }

    public void d(boolean z) {
        eg egVar = this.h;
        if (egVar == null) {
            return;
        }
        egVar.a(z);
    }

    public void e(boolean z) {
        ek ekVar = this.i;
        if (ekVar == null) {
            return;
        }
        ekVar.a(z);
    }

    public void f(boolean z) {
        el elVar = this.f;
        if (elVar == null) {
            return;
        }
        elVar.setVisibility(z ? 0 : 8);
    }

    public void a(float f) {
        em emVar = this.l;
        if (emVar != null) {
            emVar.a(f);
        }
    }

    public void a(int i) {
        em emVar = this.l;
        if (emVar != null) {
            emVar.a(i);
        }
    }

    public void b(int i) {
        el elVar = this.f;
        if (elVar != null) {
            elVar.a(i);
            this.f.postInvalidate();
            l();
        }
    }

    private void l() {
        ek ekVar = this.i;
        if (ekVar == null || ekVar.getVisibility() != 0) {
            return;
        }
        this.i.postInvalidate();
    }

    public void c(int i) {
        el elVar = this.f;
        if (elVar != null) {
            elVar.b(i);
            l();
        }
    }

    public void d(int i) {
        el elVar = this.f;
        if (elVar != null) {
            elVar.c(i);
            l();
        }
    }

    public float e(int i) {
        if (this.f == null) {
            return 0.0f;
        }
        l();
        return this.f.d(i);
    }

    public void a(int i, float f) {
        el elVar = this.f;
        if (elVar != null) {
            elVar.a(i, f);
            l();
        }
    }

    @Override // com.amap.api.mapcore.util.ad
    public void a(ae aeVar) {
        this.a = aeVar;
    }

    public Point c() {
        el elVar = this.f;
        if (elVar == null) {
            return null;
        }
        return elVar.c();
    }

    public ef d() {
        return this.j;
    }

    public eh e() {
        return this.k;
    }

    public ei f() {
        return this.g;
    }

    public eg g() {
        return this.h;
    }

    public el h() {
        return this.f;
    }

    public void a(CameraPosition cameraPosition) {
        if (g.c == 1 || !this.d.h().isLogoEnable()) {
            return;
        }
        if (cameraPosition.zoom >= 10.0f && !dr.a(cameraPosition.target.latitude, cameraPosition.target.longitude)) {
            this.f.setVisibility(8);
        } else if (this.d.n() == -1) {
            this.f.setVisibility(0);
        }
    }

    public void i() {
        em emVar = this.l;
        if (emVar != null) {
            emVar.a();
        }
        ek ekVar = this.i;
        if (ekVar != null) {
            ekVar.a();
        }
        el elVar = this.f;
        if (elVar != null) {
            elVar.a();
        }
        ei eiVar = this.g;
        if (eiVar != null) {
            eiVar.a();
        }
        eg egVar = this.h;
        if (egVar != null) {
            egVar.a();
        }
        eh ehVar = this.k;
        if (ehVar != null) {
            ehVar.b();
        }
    }

    /* JADX INFO: compiled from: MapOverlayViewGroup.java */
    public static class a extends ViewGroup.LayoutParams {
        public FPoint a;
        public int b;
        public int c;
        public int d;

        public a(int i, int i2, FPoint fPoint, int i3, int i4, int i5) {
            super(i, i2);
            this.a = fPoint;
            this.b = i3;
            this.c = i4;
            this.d = i5;
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        try {
            int childCount = getChildCount();
            for (int i5 = 0; i5 < childCount; i5++) {
                View childAt = getChildAt(i5);
                if (childAt != null) {
                    if (childAt.getLayoutParams() instanceof a) {
                        a(childAt, (a) childAt.getLayoutParams());
                    } else {
                        a(childAt, childAt.getLayoutParams());
                    }
                }
            }
            this.f.d();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private void a(View view, ViewGroup.LayoutParams layoutParams) {
        int[] iArr = new int[2];
        a(view, layoutParams.width, layoutParams.height, iArr);
        if (view instanceof eh) {
            a(view, iArr[0], iArr[1], 20, (this.d.k().y - 80) - iArr[1], 51);
        } else {
            a(view, iArr[0], iArr[1], 0, 0, 51);
        }
    }

    private void a(View view, a aVar) {
        int[] iArr = new int[2];
        a(view, aVar.width, aVar.height, iArr);
        if (view instanceof em) {
            a(view, iArr[0], iArr[1], getWidth() - iArr[0], getHeight(), aVar.d);
            return;
        }
        if (view instanceof ei) {
            a(view, iArr[0], iArr[1], getWidth() - iArr[0], iArr[1], aVar.d);
            return;
        }
        if (view instanceof eg) {
            a(view, iArr[0], iArr[1], 0, 0, aVar.d);
            return;
        }
        if (aVar.a != null) {
            IPoint iPointObtain = IPoint.obtain();
            this.d.b().map2Win(aVar.a.x, aVar.a.y, iPointObtain);
            iPointObtain.x += aVar.b;
            iPointObtain.y += aVar.c;
            a(view, iArr[0], iArr[1], iPointObtain.x, iPointObtain.y, aVar.d);
            iPointObtain.recycle();
        }
    }

    @Override // com.amap.api.mapcore.util.ad
    public void a(cf cfVar) {
        if (cfVar == null) {
            return;
        }
        try {
            ae aeVar = this.a;
            if (!(aeVar != null && aeVar.a() && cfVar.getTitle() == null && cfVar.getSnippet() == null) && cfVar.isInfoWindowEnable()) {
                cf cfVar2 = this.n;
                if (cfVar2 != null && !cfVar2.getId().equals(cfVar.getId())) {
                    a_();
                }
                if (this.a != null) {
                    this.n = cfVar;
                    cfVar.a(true);
                    this.r = true;
                }
            }
        } catch (Throwable unused) {
        }
    }

    private View b(cf cfVar) throws RemoteException {
        View viewA;
        Throwable th;
        Marker marker = new Marker(cfVar);
        try {
            if (this.o == null) {
                this.o = dp.a(this.e, "infowindow_bg.9.png");
            }
        } catch (Throwable th2) {
            fn.b(th2, "MapOverlayViewGroup", "showInfoWindow decodeDrawableFromAsset");
            th2.printStackTrace();
        }
        View viewA2 = null;
        try {
            if (this.r) {
                viewA = this.a.a(marker);
                if (viewA == null) {
                    try {
                        viewA = this.a.b(marker);
                    } catch (Throwable th3) {
                        th = th3;
                    }
                }
                this.q = viewA;
                this.r = false;
            } else {
                viewA = this.q;
            }
            if (viewA != null) {
                viewA2 = viewA;
            } else {
                if (!this.a.a()) {
                    return null;
                }
                viewA2 = this.a.a(marker);
            }
            if (viewA2.getBackground() != null) {
                return viewA2;
            }
            viewA2.setBackground(this.o);
            return viewA2;
        } catch (Throwable th4) {
            viewA = viewA2;
            th = th4;
        }
        fn.b(th, "MapOverlayViewGroup", "getInfoWindow or getInfoContents");
        th.printStackTrace();
        return viewA;
    }

    @Override // com.amap.api.mapcore.util.ad
    public void b() {
        try {
            cf cfVar = this.n;
            if (cfVar != null && cfVar.j()) {
                if (this.p) {
                    int iE = this.n.e() + this.n.c();
                    int iF = this.n.f() + this.n.d() + 2;
                    View viewB = b(this.n);
                    if (viewB == null) {
                        return;
                    }
                    a(viewB, iE, iF);
                    View view = this.m;
                    if (view != null) {
                        a aVar = (a) view.getLayoutParams();
                        if (aVar != null) {
                            aVar.a = this.n.a();
                            aVar.b = iE;
                            aVar.c = iF;
                        }
                        onLayout(false, 0, 0, 0, 0);
                        this.b = iE;
                        this.c = iF;
                        if (this.a.a()) {
                            this.a.a(this.n.getTitle(), this.n.getSnippet());
                        }
                        if (this.m.getVisibility() == 8) {
                            this.m.setVisibility(0);
                            return;
                        }
                        return;
                    }
                    return;
                }
                return;
            }
            View view2 = this.m;
            if (view2 == null || view2.getVisibility() != 0) {
                return;
            }
            this.m.setVisibility(8);
        } catch (Throwable th) {
            fn.b(th, "MapOverlayViewGroup", "redrawInfoWindow");
            th.printStackTrace();
        }
    }

    private void a(View view, int i, int i2) throws RemoteException {
        int i3;
        int i4;
        if (view == null) {
            return;
        }
        View view2 = this.m;
        if (view2 != null) {
            if (view == view2) {
                return;
            }
            view2.clearFocus();
            removeView(this.m);
        }
        this.m = view;
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        this.m.setDrawingCacheEnabled(true);
        this.m.setDrawingCacheQuality(0);
        this.n.h();
        if (layoutParams != null) {
            int i5 = layoutParams.width;
            i4 = layoutParams.height;
            i3 = i5;
        } else {
            i3 = -2;
            i4 = -2;
        }
        addView(this.m, new a(i3, i4, this.n.a(), i, i2, 81));
    }

    @Override // com.amap.api.mapcore.util.ad
    public void a_() {
        k kVar = this.d;
        if (kVar == null || kVar.getMainHandler() == null) {
            return;
        }
        this.d.getMainHandler().post(new Runnable() { // from class: com.amap.api.mapcore.util.ej.2
            @Override // java.lang.Runnable
            public void run() {
                if (ej.this.m != null) {
                    ej.this.m.clearFocus();
                    ej ejVar = ej.this;
                    ejVar.removeView(ejVar.m);
                    dw.a(ej.this.m.getBackground());
                    dw.a(ej.this.o);
                    ej.this.m = null;
                }
            }
        });
        cf cfVar = this.n;
        if (cfVar != null) {
            cfVar.a(false);
        }
        this.n = null;
        this.b = 0;
        this.c = 0;
    }

    private void a(View view, int i, int i2, int[] iArr) {
        View view2;
        if ((view instanceof ListView) && (view2 = (View) view.getParent()) != null) {
            iArr[0] = view2.getWidth();
            iArr[1] = view2.getHeight();
        }
        if (i <= 0 || i2 <= 0) {
            view.measure(0, 0);
        }
        if (i == -2) {
            iArr[0] = view.getMeasuredWidth();
        } else if (i == -1) {
            iArr[0] = getMeasuredWidth();
        } else {
            iArr[0] = i;
        }
        if (i2 == -2) {
            iArr[1] = view.getMeasuredHeight();
        } else if (i2 == -1) {
            iArr[1] = getMeasuredHeight();
        } else {
            iArr[1] = i2;
        }
    }

    private void a(View view, int i, int i2, int i3, int i4, int i5) {
        int i6;
        int i7 = i5 & 7;
        int i8 = i5 & 112;
        if (i7 == 5) {
            i3 -= i;
        } else if (i7 == 1) {
            i3 -= i / 2;
        }
        if (i8 == 80) {
            i4 -= i2;
        } else {
            if (i8 == 17) {
                i6 = i2 / 2;
            } else if (i8 == 16) {
                i4 /= 2;
                i6 = i2 / 2;
            }
            i4 -= i6;
        }
        view.layout(i3, i4, i3 + i, i4 + i2);
        if (view instanceof l) {
            this.d.b(i, i2);
        }
    }

    public void j() {
        a_();
        dw.a(this.o);
        i();
        removeAllViews();
        this.q = null;
    }

    @Override // com.amap.api.mapcore.util.ad
    public boolean a(MotionEvent motionEvent) {
        return (this.m == null || this.n == null || !dw.a(new Rect(this.m.getLeft(), this.m.getTop(), this.m.getRight(), this.m.getBottom()), (int) motionEvent.getX(), (int) motionEvent.getY())) ? false : true;
    }

    public void a(Canvas canvas) {
        Bitmap drawingCache;
        View view = this.m;
        if (view == null || this.n == null || (drawingCache = view.getDrawingCache(true)) == null) {
            return;
        }
        canvas.drawBitmap(drawingCache, this.m.getLeft(), this.m.getTop(), new Paint());
    }

    public void k() {
        this.b = 0;
        this.c = 0;
    }
}
