package com.amap.api.mapcore.util;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.GestureDetector;
import android.view.MotionEvent;
import com.amap.api.maps.model.AMapGestureListener;
import com.autonavi.ae.gmap.gesture.EAMapPlatformGestureInfo;
import com.autonavi.ae.gmap.gesture.HoverGestureDetector;
import com.autonavi.ae.gmap.gesture.MoveGestureDetector;
import com.autonavi.ae.gmap.gesture.ScaleRotateGestureDetector;
import com.autonavi.ae.gmap.gesture.ZoomOutGestureDetector;
import com.autonavi.amap.mapcore.message.HoverGestureMapMessage;
import com.autonavi.amap.mapcore.message.MoveGestureMapMessage;
import com.autonavi.amap.mapcore.message.RotateGestureMapMessage;
import com.autonavi.amap.mapcore.message.ScaleGestureMapMessage;

/* JADX INFO: compiled from: GLMapGestureDetector.java */
/* JADX INFO: loaded from: classes.dex */
public class h {
    k a;
    Context b;
    GestureDetector c;
    public AMapGestureListener d;
    private ScaleRotateGestureDetector e;
    private MoveGestureDetector f;
    private HoverGestureDetector g;
    private ZoomOutGestureDetector h;
    private boolean i = false;
    private int j = 0;
    private int k = 0;
    private int l = 0;
    private int m = 0;
    private int n = 0;
    private boolean o = false;
    private boolean p = false;
    private boolean q = true;
    private Handler r = new Handler(Looper.getMainLooper());

    static /* synthetic */ int g(h hVar) {
        int i = hVar.k;
        hVar.k = i + 1;
        return i;
    }

    static /* synthetic */ int h(h hVar) {
        int i = hVar.l;
        hVar.l = i + 1;
        return i;
    }

    static /* synthetic */ int l(h hVar) {
        int i = hVar.j;
        hVar.j = i + 1;
        return i;
    }

    static /* synthetic */ int m(h hVar) {
        int i = hVar.m;
        hVar.m = i + 1;
        return i;
    }

    public void a(AMapGestureListener aMapGestureListener) {
        this.d = aMapGestureListener;
    }

    public void a() {
        this.j = 0;
        this.l = 0;
        this.k = 0;
        this.m = 0;
        this.n = 0;
    }

    public h(k kVar) {
        this.b = kVar.u();
        this.a = kVar;
        a aVar = new a();
        GestureDetector gestureDetector = new GestureDetector(this.b, aVar, this.r);
        this.c = gestureDetector;
        gestureDetector.setOnDoubleTapListener(aVar);
        this.e = new ScaleRotateGestureDetector(this.b, new d());
        this.f = new MoveGestureDetector(this.b, new c());
        this.g = new HoverGestureDetector(this.b, new b());
        this.h = new ZoomOutGestureDetector(this.b, new e());
    }

    public boolean a(MotionEvent motionEvent) {
        if (this.n < motionEvent.getPointerCount()) {
            this.n = motionEvent.getPointerCount();
        }
        if ((motionEvent.getAction() & 255) == 0) {
            this.p = false;
            this.q = false;
        }
        if (motionEvent.getAction() == 6 && motionEvent.getPointerCount() > 0) {
            this.p = true;
        }
        if (this.o && this.n >= 2) {
            this.o = false;
        }
        try {
            if (this.d != null) {
                if (motionEvent.getAction() == 0) {
                    this.d.onDown(motionEvent.getX(), motionEvent.getY());
                } else if (motionEvent.getAction() == 1) {
                    this.d.onUp(motionEvent.getX(), motionEvent.getY());
                }
            }
            this.c.onTouchEvent(motionEvent);
            boolean zOnTouchEvent = this.g.onTouchEvent(motionEvent);
            if (this.i && this.m > 0) {
                return zOnTouchEvent;
            }
            this.h.onTouchEvent(motionEvent);
            if (this.o) {
                return zOnTouchEvent;
            }
            this.e.onTouchEvent(motionEvent);
            return this.f.onTouchEvent(motionEvent);
        } catch (Throwable th) {
            th.printStackTrace();
            return false;
        }
    }

    public void b() {
        Handler handler = this.r;
        if (handler != null) {
            handler.removeCallbacks(null);
            this.r = null;
        }
    }

    /* JADX INFO: compiled from: GLMapGestureDetector.java */
    private class a implements GestureDetector.OnDoubleTapListener, GestureDetector.OnGestureListener {
        float a;
        long b;
        private int d;
        private EAMapPlatformGestureInfo e;

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return false;
        }

        private a() {
            this.d = 0;
            this.a = 0.0f;
            this.e = new EAMapPlatformGestureInfo();
            this.b = 0L;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onDown(MotionEvent motionEvent) {
            h.this.o = false;
            return true;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            if (h.this.d != null) {
                h.this.d.onFling(f, f2);
            }
            try {
                if (h.this.a.h().isScrollGesturesEnabled() && h.this.m <= 0 && h.this.k <= 0 && h.this.l == 0 && !h.this.q) {
                    this.e.mGestureState = 3;
                    this.e.mGestureType = 3;
                    this.e.mLocation = new float[]{motionEvent2.getX(), motionEvent2.getY()};
                    int iA = h.this.a.a(this.e);
                    h.this.a.onFling();
                    h.this.a.a().startMapSlidAnim(iA, new Point((int) motionEvent2.getX(), (int) motionEvent2.getY()), f, f2);
                }
                return true;
            } catch (Throwable th) {
                fn.b(th, "GLMapGestrureDetector", "onFling");
                th.printStackTrace();
                return true;
            }
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public void onLongPress(MotionEvent motionEvent) {
            if (h.this.n == 1) {
                this.e.mGestureState = 3;
                this.e.mGestureType = 7;
                this.e.mLocation = new float[]{motionEvent.getX(), motionEvent.getY()};
                h.this.a.a(h.this.a.a(this.e), motionEvent);
                if (h.this.d != null) {
                    h.this.d.onLongPress(motionEvent.getX(), motionEvent.getY());
                }
            }
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            if (h.this.d == null) {
                return false;
            }
            h.this.d.onScroll(f, f2);
            return false;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public void onShowPress(MotionEvent motionEvent) {
            try {
                this.e.mGestureState = 3;
                this.e.mGestureType = 7;
                this.e.mLocation = new float[]{motionEvent.getX(), motionEvent.getY()};
                h.this.a.a().clearAnimations(h.this.a.a(this.e), false);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }

        @Override // android.view.GestureDetector.OnDoubleTapListener
        public boolean onDoubleTap(MotionEvent motionEvent) {
            h.this.c.setIsLongpressEnabled(false);
            this.d = motionEvent.getPointerCount();
            if (h.this.d != null) {
                h.this.d.onDoubleTap(motionEvent.getX(), motionEvent.getY());
            }
            return false;
        }

        @Override // android.view.GestureDetector.OnDoubleTapListener
        public boolean onDoubleTapEvent(MotionEvent motionEvent) {
            if (this.d < motionEvent.getPointerCount()) {
                this.d = motionEvent.getPointerCount();
            }
            int action = motionEvent.getAction() & 255;
            if (this.d != 1) {
                return false;
            }
            try {
                if (!h.this.a.h().isZoomGesturesEnabled()) {
                    return false;
                }
            } catch (Throwable th) {
                fn.b(th, "GLMapGestrureDetector", "onDoubleTapEvent");
                th.printStackTrace();
            }
            if (action == 0) {
                this.e.mGestureState = 1;
                this.e.mGestureType = 9;
                this.e.mLocation = new float[]{motionEvent.getX(), motionEvent.getY()};
                int iA = h.this.a.a(this.e);
                this.a = motionEvent.getY();
                h.this.a.a(iA, ScaleGestureMapMessage.obtain(100, 1.0f, 0, 0));
                this.b = SystemClock.uptimeMillis();
                return true;
            }
            if (action == 2) {
                h.this.o = true;
                float y = this.a - motionEvent.getY();
                if (Math.abs(y) < 20.0f) {
                    return true;
                }
                this.e.mGestureState = 2;
                this.e.mGestureType = 9;
                this.e.mLocation = new float[]{motionEvent.getX(), motionEvent.getY()};
                int iA2 = h.this.a.a(this.e);
                float mapHeight = (4.0f * y) / h.this.a.getMapHeight();
                if (y > 0.0f) {
                    h.this.a.a(iA2, ScaleGestureMapMessage.obtain(101, mapHeight, 0, 0));
                } else {
                    h.this.a.a(iA2, ScaleGestureMapMessage.obtain(101, mapHeight, 0, 0));
                }
                this.a = motionEvent.getY();
                return true;
            }
            this.e.mGestureState = 3;
            this.e.mGestureType = 9;
            this.e.mLocation = new float[]{motionEvent.getX(), motionEvent.getY()};
            int iA3 = h.this.a.a(this.e);
            h.this.c.setIsLongpressEnabled(true);
            h.this.a.a(iA3, ScaleGestureMapMessage.obtain(102, 1.0f, 0, 0));
            if (action != 1) {
                h.this.o = false;
                return true;
            }
            h.this.a.a(iA3, 3);
            long jUptimeMillis = SystemClock.uptimeMillis() - this.b;
            if (h.this.o && jUptimeMillis >= 200) {
                h.this.o = false;
                return true;
            }
            return h.this.a.b(iA3, motionEvent);
        }

        @Override // android.view.GestureDetector.OnDoubleTapListener
        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            if (h.this.n != 1) {
                return false;
            }
            this.e.mGestureState = 3;
            this.e.mGestureType = 8;
            this.e.mLocation = new float[]{motionEvent.getX(), motionEvent.getY()};
            int iA = h.this.a.a(this.e);
            if (h.this.d != null) {
                try {
                    h.this.d.onSingleTap(motionEvent.getX(), motionEvent.getY());
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
            return h.this.a.c(iA, motionEvent);
        }
    }

    /* JADX INFO: compiled from: GLMapGestureDetector.java */
    private class d extends ScaleRotateGestureDetector.SimpleOnScaleRotateGestureListener {
        private boolean b;
        private boolean c;
        private boolean d;
        private Point e;
        private float[] f;
        private float g;
        private float[] h;
        private float i;
        private EAMapPlatformGestureInfo j;

        private d() {
            this.b = false;
            this.c = false;
            this.d = false;
            this.e = new Point();
            this.f = new float[10];
            this.g = 0.0f;
            this.h = new float[10];
            this.i = 0.0f;
            this.j = new EAMapPlatformGestureInfo();
        }

        /* JADX WARN: Code duplicated, block: B:27:0x00cb  */
        /* JADX WARN: Code duplicated, block: B:29:0x00d0 A[Catch: all -> 0x010a, TryCatch #2 {all -> 0x010a, blocks: (B:29:0x00d0, B:31:0x00f8, B:32:0x0100, B:24:0x00bf), top: B:74:0x00bf }] */
        /* JADX WARN: Code duplicated, block: B:31:0x00f8 A[Catch: all -> 0x010a, TryCatch #2 {all -> 0x010a, blocks: (B:29:0x00d0, B:31:0x00f8, B:32:0x0100, B:24:0x00bf), top: B:74:0x00bf }] */
        /* JADX WARN: Code duplicated, block: B:32:0x0100 A[Catch: all -> 0x010a, TRY_LEAVE, TryCatch #2 {all -> 0x010a, blocks: (B:29:0x00d0, B:31:0x00f8, B:32:0x0100, B:24:0x00bf), top: B:74:0x00bf }] */
        @Override // com.autonavi.ae.gmap.gesture.ScaleRotateGestureDetector.SimpleOnScaleRotateGestureListener
        public boolean onScaleRotate(ScaleRotateGestureDetector scaleRotateGestureDetector) {
            this.j.mGestureState = 2;
            this.j.mGestureType = 4;
            boolean z = false;
            boolean z2 = true;
            this.j.mLocation = new float[]{scaleRotateGestureDetector.getEvent().getX(), scaleRotateGestureDetector.getEvent().getY()};
            int iA = h.this.a.a(this.j);
            float scaleFactor = scaleRotateGestureDetector.getScaleFactor();
            float timeDelta = scaleRotateGestureDetector.getTimeDelta();
            int focusX = (int) scaleRotateGestureDetector.getFocusX();
            int focusY = (int) scaleRotateGestureDetector.getFocusY();
            float fAbs = Math.abs(focusX - this.e.x);
            float fAbs2 = Math.abs(focusY - this.e.y);
            this.e.x = focusX;
            this.e.y = focusY;
            float fLog = (float) Math.log(scaleFactor);
            if (h.this.k <= 0 && Math.abs(fLog) > 0.2d) {
                this.d = true;
            }
            try {
                if (h.this.a.h().isZoomGesturesEnabled()) {
                    if (!this.b && 0.06f < Math.abs(fLog)) {
                        this.b = true;
                    }
                    if (this.b && 0.01f < Math.abs(fLog)) {
                        if (fAbs > 2.0f || fAbs2 > 2.0f) {
                            try {
                                if (Math.abs(fLog) >= 0.02f) {
                                    if (timeDelta > 0.0f) {
                                        float f = fLog / timeDelta;
                                        this.g = f;
                                        this.f[h.this.k % 10] = Math.abs(f);
                                        h.g(h.this);
                                        h.this.a.a(iA, ScaleGestureMapMessage.obtain(101, fLog, focusX, focusY));
                                        if (fLog > 0.0f) {
                                            h.this.a.a(iA, 1);
                                        } else {
                                            h.this.a.a(iA, 2);
                                        }
                                    }
                                }
                                z = true;
                            } catch (Throwable th) {
                                th = th;
                                z = true;
                                fn.b(th, "GLMapGestrureDetector", "onScaleRotate");
                                th.printStackTrace();
                            }
                        } else {
                            if (timeDelta > 0.0f) {
                                float f2 = fLog / timeDelta;
                                this.g = f2;
                                this.f[h.this.k % 10] = Math.abs(f2);
                                h.g(h.this);
                                h.this.a.a(iA, ScaleGestureMapMessage.obtain(101, fLog, focusX, focusY));
                                if (fLog > 0.0f) {
                                    h.this.a.a(iA, 1);
                                } else {
                                    h.this.a.a(iA, 2);
                                }
                            }
                            z = true;
                        }
                    }
                }
            } catch (Throwable th2) {
                th = th2;
            }
            try {
                if (h.this.a.h().isRotateGesturesEnabled() && !h.this.a.e(iA) && !this.d) {
                    float rotationDegreesDelta = scaleRotateGestureDetector.getRotationDegreesDelta();
                    if (!this.c && Math.abs(rotationDegreesDelta) >= 4.0f) {
                        this.c = true;
                    }
                    if (this.c && 1.0f < Math.abs(rotationDegreesDelta) && ((fAbs <= 4.0f && fAbs2 <= 4.0f) || Math.abs(rotationDegreesDelta) >= 2.0f)) {
                        float f3 = rotationDegreesDelta / timeDelta;
                        this.i = f3;
                        this.h[h.this.l % 10] = Math.abs(f3);
                        h.h(h.this);
                        h.this.a.a(iA, RotateGestureMapMessage.obtain(101, rotationDegreesDelta, focusX, focusY));
                        try {
                            h.this.a.a(iA, 6);
                            return true;
                        } catch (Throwable th3) {
                            th = th3;
                            fn.b(th, "GLMapGestrureDetector", "onScaleRotate");
                            th.printStackTrace();
                            return z2;
                        }
                    }
                }
                return z;
            } catch (Throwable th4) {
                th = th4;
                z2 = z;
            }
        }

        @Override // com.autonavi.ae.gmap.gesture.ScaleRotateGestureDetector.SimpleOnScaleRotateGestureListener
        public boolean onScaleRotateBegin(ScaleRotateGestureDetector scaleRotateGestureDetector) {
            this.j.mGestureState = 1;
            this.j.mGestureType = 4;
            this.j.mLocation = new float[]{scaleRotateGestureDetector.getEvent().getX(), scaleRotateGestureDetector.getEvent().getY()};
            int iA = h.this.a.a(this.j);
            int focusX = (int) scaleRotateGestureDetector.getFocusX();
            int focusY = (int) scaleRotateGestureDetector.getFocusY();
            this.d = false;
            this.e.x = focusX;
            this.e.y = focusY;
            this.b = false;
            this.c = false;
            h.this.a.a(iA, ScaleGestureMapMessage.obtain(100, 1.0f, focusX, focusY));
            try {
                if (h.this.a.h().isRotateGesturesEnabled() && !h.this.a.e(iA)) {
                    h.this.a.a(iA, RotateGestureMapMessage.obtain(100, h.this.a.m(iA), focusX, focusY));
                }
            } catch (Throwable th) {
                fn.b(th, "GLMapGestrureDetector", "onScaleRotateBegin");
                th.printStackTrace();
            }
            return true;
        }

        @Override // com.autonavi.ae.gmap.gesture.ScaleRotateGestureDetector.SimpleOnScaleRotateGestureListener
        public void onScaleRotateEnd(ScaleRotateGestureDetector scaleRotateGestureDetector) {
            this.j.mGestureState = 3;
            this.j.mGestureType = 4;
            this.j.mLocation = new float[]{scaleRotateGestureDetector.getEvent().getX(), scaleRotateGestureDetector.getEvent().getY()};
            int iA = h.this.a.a(this.j);
            this.d = false;
            h.this.a.a(iA, ScaleGestureMapMessage.obtain(102, 1.0f, 0, 0));
            if (h.this.k > 0) {
                int i = h.this.k > 10 ? 10 : h.this.k;
                float f = 0.0f;
                for (int i2 = 0; i2 < 10; i2++) {
                    float[] fArr = this.f;
                    f += fArr[i2];
                    fArr[i2] = 0.0f;
                }
                if (0.004f <= f / i) {
                    h.this.a.a(iA);
                }
                this.g = 0.0f;
            }
            if (h.this.a.e(iA)) {
                return;
            }
            try {
                if (h.this.a.h().isRotateGesturesEnabled()) {
                    h.this.a.a(iA, RotateGestureMapMessage.obtain(102, h.this.a.m(iA), 0, 0));
                }
            } catch (Throwable th) {
                fn.b(th, "GLMapGestrureDetector", "onScaleRotateEnd");
                th.printStackTrace();
            }
            if (h.this.l > 0) {
                h.this.a.a(iA, 6);
                int i3 = h.this.l > 10 ? 10 : h.this.l;
                float f2 = 0.0f;
                for (int i4 = 0; i4 < 10; i4++) {
                    float[] fArr2 = this.h;
                    f2 += fArr2[i4];
                    fArr2[i4] = 0.0f;
                }
                float f3 = f2 / i3;
                if (0.1f <= f3) {
                    float f4 = f3 * 200.0f;
                    int iM = ((int) h.this.a.m(iA)) % 360;
                    if (f4 >= 60.0f) {
                        f4 = 60.0f;
                    }
                    if (this.i < 0.0f) {
                        f4 = -f4;
                    }
                    int i5 = ((int) (iM + f4)) % 360;
                }
            }
            this.g = 0.0f;
        }
    }

    /* JADX INFO: compiled from: GLMapGestureDetector.java */
    private class c implements MoveGestureDetector.OnMoveGestureListener {
        private final float b;
        private final float c;
        private EAMapPlatformGestureInfo d;

        private c() {
            this.b = 1.0f;
            this.c = 4.0f;
            this.d = new EAMapPlatformGestureInfo();
        }

        @Override // com.autonavi.ae.gmap.gesture.MoveGestureDetector.OnMoveGestureListener
        public boolean onMove(MoveGestureDetector moveGestureDetector) {
            if (h.this.i) {
                return true;
            }
            try {
                if (h.this.a.h().isScrollGesturesEnabled() && !h.this.p) {
                    this.d.mGestureState = 2;
                    this.d.mGestureType = 3;
                    this.d.mLocation = new float[]{moveGestureDetector.getEvent().getX(), moveGestureDetector.getEvent().getY()};
                    int iA = h.this.a.a(this.d);
                    PointF focusDelta = moveGestureDetector.getFocusDelta();
                    float f = h.this.j == 0 ? 4.0f : 1.0f;
                    if (Math.abs(focusDelta.x) <= f && Math.abs(focusDelta.y) <= f) {
                        return false;
                    }
                    if (h.this.j == 0) {
                        h.this.a.a().clearAnimations(iA, false);
                    }
                    h.this.a.a(iA, MoveGestureMapMessage.obtain(101, focusDelta.x, focusDelta.y));
                    h.l(h.this);
                }
                return true;
            } catch (Throwable th) {
                fn.b(th, "GLMapGestrureDetector", "onMove");
                th.printStackTrace();
                return true;
            }
        }

        @Override // com.autonavi.ae.gmap.gesture.MoveGestureDetector.OnMoveGestureListener
        public boolean onMoveBegin(MoveGestureDetector moveGestureDetector) {
            try {
                if (!h.this.a.h().isScrollGesturesEnabled()) {
                    return true;
                }
                this.d.mGestureState = 1;
                this.d.mGestureType = 3;
                this.d.mLocation = new float[]{moveGestureDetector.getEvent().getX(), moveGestureDetector.getEvent().getY()};
                h.this.a.a(h.this.a.a(this.d), MoveGestureMapMessage.obtain(100, 0.0f, 0.0f));
                return true;
            } catch (Throwable th) {
                fn.b(th, "GLMapGestrureDetector", "onMoveBegin");
                th.printStackTrace();
                return true;
            }
        }

        @Override // com.autonavi.ae.gmap.gesture.MoveGestureDetector.OnMoveGestureListener
        public void onMoveEnd(MoveGestureDetector moveGestureDetector) {
            try {
                if (h.this.a.h().isScrollGesturesEnabled()) {
                    this.d.mGestureState = 3;
                    this.d.mGestureType = 3;
                    this.d.mLocation = new float[]{moveGestureDetector.getEvent().getX(), moveGestureDetector.getEvent().getY()};
                    int iA = h.this.a.a(this.d);
                    if (h.this.j > 0) {
                        h.this.a.a(iA, 5);
                    }
                    h.this.a.a(iA, MoveGestureMapMessage.obtain(102, 0.0f, 0.0f));
                }
            } catch (Throwable th) {
                fn.b(th, "GLMapGestrureDetector", "onMoveEnd");
                th.printStackTrace();
            }
        }
    }

    /* JADX INFO: compiled from: GLMapGestureDetector.java */
    private class b implements HoverGestureDetector.OnHoverGestureListener {
        private EAMapPlatformGestureInfo b;

        private b() {
            this.b = new EAMapPlatformGestureInfo();
        }

        @Override // com.autonavi.ae.gmap.gesture.HoverGestureDetector.OnHoverGestureListener
        public boolean onHove(HoverGestureDetector hoverGestureDetector) {
            this.b.mGestureState = 2;
            this.b.mGestureType = 6;
            boolean z = false;
            this.b.mLocation = new float[]{hoverGestureDetector.getEvent().getX(), hoverGestureDetector.getEvent().getY()};
            try {
                if (!h.this.a.h().isTiltGesturesEnabled()) {
                    return true;
                }
                int iA = h.this.a.a(this.b);
                if (h.this.a.d(iA) || h.this.l > 3) {
                    return false;
                }
                float f = hoverGestureDetector.getFocusDelta().x;
                float f2 = hoverGestureDetector.getFocusDelta().y;
                if (!h.this.i) {
                    PointF pointerDelta = hoverGestureDetector.getPointerDelta(0);
                    PointF pointerDelta2 = hoverGestureDetector.getPointerDelta(1);
                    if ((pointerDelta.y > 10.0f && pointerDelta2.y > 10.0f) || (pointerDelta.y < -10.0f && pointerDelta2.y < -10.0f)) {
                        z = true;
                    }
                    if (z && Math.abs(f2) > 10.0f && Math.abs(f) < 10.0f) {
                        h.this.i = true;
                    }
                }
                if (h.this.i) {
                    h.this.i = true;
                    float f3 = f2 / 6.0f;
                    if (Math.abs(f3) > 1.0f) {
                        h.this.a.a(iA, HoverGestureMapMessage.obtain(101, f3));
                        h.m(h.this);
                    }
                }
                return true;
            } catch (Throwable th) {
                fn.b(th, "GLMapGestrureDetector", "onHove");
                th.printStackTrace();
                return true;
            }
        }

        @Override // com.autonavi.ae.gmap.gesture.HoverGestureDetector.OnHoverGestureListener
        public boolean onHoveBegin(HoverGestureDetector hoverGestureDetector) {
            this.b.mGestureState = 1;
            this.b.mGestureType = 6;
            this.b.mLocation = new float[]{hoverGestureDetector.getEvent().getX(), hoverGestureDetector.getEvent().getY()};
            try {
                if (!h.this.a.h().isTiltGesturesEnabled()) {
                    return true;
                }
                int iA = h.this.a.a(this.b);
                if (h.this.a.d(iA)) {
                    return false;
                }
                h.this.a.a(iA, HoverGestureMapMessage.obtain(100, h.this.a.o(iA)));
                return true;
            } catch (Throwable th) {
                fn.b(th, "GLMapGestrureDetector", "onHoveBegin");
                th.printStackTrace();
                return true;
            }
        }

        @Override // com.autonavi.ae.gmap.gesture.HoverGestureDetector.OnHoverGestureListener
        public void onHoveEnd(HoverGestureDetector hoverGestureDetector) {
            this.b.mGestureState = 3;
            this.b.mGestureType = 6;
            this.b.mLocation = new float[]{hoverGestureDetector.getEvent().getX(), hoverGestureDetector.getEvent().getY()};
            try {
                if (h.this.a.h().isTiltGesturesEnabled()) {
                    int iA = h.this.a.a(this.b);
                    if (h.this.a.d(iA)) {
                        return;
                    }
                    if (h.this.a.o(iA) >= 0.0f && h.this.m > 0) {
                        h.this.a.a(iA, 7);
                    }
                    h.this.i = false;
                    h.this.a.a(iA, HoverGestureMapMessage.obtain(102, h.this.a.o(iA)));
                }
            } catch (Throwable th) {
                fn.b(th, "GLMapGestrureDetector", "onHoveEnd");
                th.printStackTrace();
            }
        }
    }

    /* JADX INFO: compiled from: GLMapGestureDetector.java */
    private class e extends ZoomOutGestureDetector.SimpleOnZoomOutGestureListener {
        EAMapPlatformGestureInfo a;

        private e() {
            this.a = new EAMapPlatformGestureInfo();
        }

        @Override // com.autonavi.ae.gmap.gesture.ZoomOutGestureDetector.SimpleOnZoomOutGestureListener, com.autonavi.ae.gmap.gesture.ZoomOutGestureDetector.OnZoomOutGestureListener
        public void onZoomOut(ZoomOutGestureDetector zoomOutGestureDetector) {
            try {
                if (h.this.a.h().isZoomGesturesEnabled() && Math.abs(zoomOutGestureDetector.getFocusX()) <= 10.0f && Math.abs(zoomOutGestureDetector.getFocusY()) <= 10.0f && zoomOutGestureDetector.getTimeDelta() < 200) {
                    h.this.q = true;
                    this.a.mGestureState = 2;
                    this.a.mGestureType = 2;
                    this.a.mLocation = new float[]{zoomOutGestureDetector.getEvent().getX(), zoomOutGestureDetector.getEvent().getY()};
                    int iA = h.this.a.a(this.a);
                    h.this.a.a(iA, 4);
                    h.this.a.c(iA);
                }
            } catch (Throwable th) {
                fn.b(th, "GLMapGestrureDetector", "onZoomOut");
                th.printStackTrace();
            }
        }
    }
}
