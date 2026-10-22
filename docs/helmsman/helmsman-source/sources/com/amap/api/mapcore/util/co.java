package com.amap.api.mapcore.util;

import android.animation.Animator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.location.Location;
import android.os.RemoteException;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.autonavi.ae.gmap.GLMapState;
import com.autonavi.amap.mapcore.IPoint;

/* JADX INFO: compiled from: MyLocationOverlay.java */
/* JADX INFO: loaded from: classes.dex */
public class co {
    ValueAnimator b;
    private k e;
    private Marker f;
    private Circle g;
    private LatLng i;
    private double j;
    private Context k;
    private t l;
    private MyLocationStyle h = new MyLocationStyle();
    private int m = 4;
    private boolean n = false;
    private final String o = "location_map_gps_locked.png";
    private final String p = "location_map_gps_3d.png";
    private BitmapDescriptor q = null;
    private boolean r = false;
    private boolean s = false;
    private boolean t = false;
    private boolean u = false;
    private boolean v = false;
    a a = null;
    Animator.AnimatorListener c = new Animator.AnimatorListener() { // from class: com.amap.api.mapcore.util.co.1
        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            co.this.i();
        }
    };
    ValueAnimator.AnimatorUpdateListener d = new ValueAnimator.AnimatorUpdateListener() { // from class: com.amap.api.mapcore.util.co.2
        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            try {
                if (co.this.g != null) {
                    LatLng latLng = (LatLng) valueAnimator.getAnimatedValue();
                    co.this.g.setCenter(latLng);
                    co.this.f.setPosition(latLng);
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    };

    public co(k kVar, Context context) {
        this.k = context.getApplicationContext();
        this.e = kVar;
        this.l = new t(this.k, kVar);
        a(4, true);
    }

    public void a(MyLocationStyle myLocationStyle) {
        try {
            this.h = myLocationStyle;
            a(myLocationStyle.isMyLocationShowing());
            if (!this.h.isMyLocationShowing()) {
                this.l.a(false);
                this.m = this.h.getMyLocationType();
                return;
            }
            j();
            Marker marker = this.f;
            if (marker == null && this.g == null) {
                return;
            }
            this.l.a(marker);
            a(this.h.getMyLocationType());
        } catch (Throwable th) {
            fn.b(th, "MyLocationOverlay", "setMyLocationStyle");
            th.printStackTrace();
        }
    }

    public MyLocationStyle a() {
        return this.h;
    }

    public void a(int i) {
        a(i, false);
    }

    private void a(int i, boolean z) {
        this.m = i;
        this.n = false;
        this.s = false;
        this.r = false;
        this.u = false;
        this.v = false;
        if (i == 1) {
            this.r = true;
            this.s = true;
            this.t = true;
        } else if (i == 2) {
            this.r = true;
            this.t = true;
        } else if (i == 3) {
            this.r = true;
            this.v = true;
        } else if (i == 4) {
            this.r = true;
            this.u = true;
        } else if (i == 5) {
            this.u = true;
        } else if (i == 7) {
            this.v = true;
        }
        if (this.u || this.v) {
            if (this.v) {
                this.l.a(true);
                if (!z) {
                    try {
                        this.e.a(z.a(17.0f));
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }
                b(45.0f);
            } else {
                this.l.a(false);
            }
            this.l.a();
            Marker marker = this.f;
            if (marker != null) {
                marker.setFlat(true);
                return;
            }
            return;
        }
        Marker marker2 = this.f;
        if (marker2 != null) {
            marker2.setFlat(false);
        }
        h();
        g();
        f();
    }

    private void f() {
        this.l.b();
    }

    private void g() {
        b(0.0f);
    }

    private void h() {
        c(0.0f);
    }

    private void b(float f) {
        k kVar = this.e;
        if (kVar == null) {
            return;
        }
        try {
            kVar.a(z.c(f));
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private void c(float f) {
        k kVar = this.e;
        if (kVar == null) {
            return;
        }
        try {
            kVar.a(z.d(f));
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void a(Location location) {
        if (location == null) {
            return;
        }
        a(this.h.isMyLocationShowing());
        if (this.h.isMyLocationShowing()) {
            this.i = new LatLng(location.getLatitude(), location.getLongitude());
            this.j = location.getAccuracy();
            if (this.f == null && this.g == null) {
                j();
            }
            Circle circle = this.g;
            if (circle != null) {
                try {
                    double d = this.j;
                    if (d != -1.0d) {
                        circle.setRadius(d);
                    }
                } catch (Throwable th) {
                    fn.b(th, "MyLocationOverlay", "setCentAndRadius");
                    th.printStackTrace();
                }
            }
            d(location.getBearing());
            if (!this.i.equals(this.f.getPosition())) {
                a(this.i);
            } else {
                i();
            }
        }
    }

    private void d(float f) {
        if (this.t) {
            float f2 = f % 360.0f;
            if (f2 > 180.0f) {
                f2 -= 360.0f;
            } else if (f2 < -180.0f) {
                f2 += 360.0f;
            }
            Marker marker = this.f;
            if (marker != null) {
                marker.setRotateAngle(-f2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void i() {
        if (this.i != null && this.r) {
            if (this.s && this.n) {
                return;
            }
            this.n = true;
            try {
                IPoint iPointObtain = IPoint.obtain();
                GLMapState.lonlat2Geo(this.i.longitude, this.i.latitude, iPointObtain);
                this.e.b(z.a(iPointObtain));
            } catch (Throwable th) {
                fn.b(th, "MyLocationOverlay", "moveMapToLocation");
                th.printStackTrace();
            }
        }
    }

    private void j() {
        MyLocationStyle myLocationStyle = this.h;
        if (myLocationStyle == null) {
            MyLocationStyle myLocationStyle2 = new MyLocationStyle();
            this.h = myLocationStyle2;
            myLocationStyle2.myLocationIcon(BitmapDescriptorFactory.fromAsset("location_map_gps_locked.png"));
            l();
            return;
        }
        if (myLocationStyle.getMyLocationIcon() == null || this.h.getMyLocationIcon().getBitmap() == null) {
            this.h.myLocationIcon(BitmapDescriptorFactory.fromAsset("location_map_gps_locked.png"));
        }
        l();
    }

    public void b() throws RemoteException {
        k();
        if (this.l != null) {
            f();
            this.l = null;
        }
    }

    private void k() {
        Circle circle = this.g;
        if (circle != null) {
            try {
                this.e.a(circle.getId());
            } catch (Throwable th) {
                fn.b(th, "MyLocationOverlay", "locationIconRemove");
                th.printStackTrace();
            }
            this.g = null;
        }
        Marker marker = this.f;
        if (marker != null) {
            marker.remove();
            this.f = null;
            this.l.a((Marker) null);
        }
    }

    public void a(boolean z) {
        Circle circle = this.g;
        if (circle != null && circle.isVisible() != z) {
            this.g.setVisible(z);
        }
        Marker marker = this.f;
        if (marker == null || marker.isVisible() == z) {
            return;
        }
        this.f.setVisible(z);
    }

    private void l() {
        try {
            if (this.g == null) {
                this.g = this.e.addCircle(new CircleOptions().zIndex(1.0f));
            }
            Circle circle = this.g;
            if (circle != null) {
                if (circle.getStrokeWidth() != this.h.getStrokeWidth()) {
                    this.g.setStrokeWidth(this.h.getStrokeWidth());
                }
                if (this.g.getFillColor() != this.h.getRadiusFillColor()) {
                    this.g.setFillColor(this.h.getRadiusFillColor());
                }
                if (this.g.getStrokeColor() != this.h.getStrokeColor()) {
                    this.g.setStrokeColor(this.h.getStrokeColor());
                }
                LatLng latLng = this.i;
                if (latLng != null) {
                    this.g.setCenter(latLng);
                }
                this.g.setRadius(this.j);
                this.g.setVisible(true);
            }
            if (this.f == null) {
                this.f = this.e.addMarker(new MarkerOptions().visible(false));
            }
            Marker marker = this.f;
            if (marker != null) {
                if (marker.getAnchorU() != this.h.getAnchorU() || this.f.getAnchorV() != this.h.getAnchorV()) {
                    this.f.setAnchor(this.h.getAnchorU(), this.h.getAnchorV());
                }
                if (this.f.getIcons() == null || this.f.getIcons().size() == 0) {
                    this.f.setIcon(this.h.getMyLocationIcon());
                } else if (this.h.getMyLocationIcon() != null && !this.f.getIcons().get(0).equals(this.h.getMyLocationIcon())) {
                    this.f.setIcon(this.h.getMyLocationIcon());
                }
                LatLng latLng2 = this.i;
                if (latLng2 != null) {
                    this.f.setPosition(latLng2);
                    this.f.setVisible(true);
                }
            }
            i();
            this.l.a(this.f);
        } catch (Throwable th) {
            fn.b(th, "MyLocationOverlay", "myLocStyle");
            th.printStackTrace();
        }
    }

    public void a(float f) {
        Marker marker = this.f;
        if (marker != null) {
            marker.setRotateAngle(f);
        }
    }

    public String c() {
        Marker marker = this.f;
        if (marker != null) {
            return marker.getId();
        }
        return null;
    }

    public String d() throws RemoteException {
        Circle circle = this.g;
        if (circle != null) {
            return circle.getId();
        }
        return null;
    }

    public void e() {
        this.g = null;
        this.f = null;
    }

    private void a(LatLng latLng) {
        LatLng position = this.f.getPosition();
        if (position == null) {
            position = new LatLng(0.0d, 0.0d);
        }
        if (this.a == null) {
            this.a = new a();
        }
        ValueAnimator valueAnimator = this.b;
        if (valueAnimator == null) {
            ValueAnimator valueAnimatorOfObject = ValueAnimator.ofObject(new a(), position, latLng);
            this.b = valueAnimatorOfObject;
            valueAnimatorOfObject.addListener(this.c);
            this.b.addUpdateListener(this.d);
        } else {
            valueAnimator.setObjectValues(position, latLng);
            this.b.setEvaluator(this.a);
        }
        if (position.latitude == 0.0d && position.longitude == 0.0d) {
            this.b.setDuration(1L);
        } else {
            this.b.setDuration(1000L);
        }
        this.b.start();
    }

    /* JADX INFO: compiled from: MyLocationOverlay.java */
    public class a implements TypeEvaluator {
        public a() {
        }

        @Override // android.animation.TypeEvaluator
        public Object evaluate(float f, Object obj, Object obj2) {
            LatLng latLng = (LatLng) obj;
            LatLng latLng2 = (LatLng) obj2;
            double d = f;
            return new LatLng(latLng.latitude + ((latLng2.latitude - latLng.latitude) * d), latLng.longitude + (d * (latLng2.longitude - latLng.longitude)));
        }
    }
}
