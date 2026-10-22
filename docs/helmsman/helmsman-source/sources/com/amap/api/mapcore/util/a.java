package com.amap.api.mapcore.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CustomRenderer;
import com.amap.api.maps.InfoWindowAnimationManager;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.Projection;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.AMapCameraInfo;
import com.amap.api.maps.model.AMapGestureListener;
import com.amap.api.maps.model.Arc;
import com.amap.api.maps.model.ArcOptions;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.CrossOverlay;
import com.amap.api.maps.model.CrossOverlayOptions;
import com.amap.api.maps.model.GroundOverlay;
import com.amap.api.maps.model.GroundOverlayOptions;
import com.amap.api.maps.model.IndoorBuildingInfo;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MultiPointOverlay;
import com.amap.api.maps.model.MultiPointOverlayOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.MyTrafficStyle;
import com.amap.api.maps.model.NavigateArrow;
import com.amap.api.maps.model.NavigateArrowOptions;
import com.amap.api.maps.model.Poi;
import com.amap.api.maps.model.Polygon;
import com.amap.api.maps.model.PolygonOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.model.RouteOverlay;
import com.amap.api.maps.model.Text;
import com.amap.api.maps.model.TextOptions;
import com.amap.api.maps.model.TileOverlay;
import com.amap.api.maps.model.TileOverlayOptions;
import com.amap.api.maps.model.animation.Animation;
import com.autonavi.ae.gmap.GLMapEngine;
import com.autonavi.ae.gmap.GLMapRender;
import com.autonavi.ae.gmap.GLMapState;
import com.autonavi.ae.gmap.callback.GLMapCoreCallback;
import com.autonavi.ae.gmap.gesture.EAMapPlatformGestureInfo;
import com.autonavi.ae.gmap.glinterface.MapLabelItem;
import com.autonavi.ae.gmap.gloverlay.BaseMapOverlay;
import com.autonavi.ae.gmap.gloverlay.BaseRouteOverlay;
import com.autonavi.ae.gmap.gloverlay.CrossVectorOverlay;
import com.autonavi.ae.gmap.gloverlay.GLOverlayBundle;
import com.autonavi.ae.gmap.gloverlay.GLTextureProperty;
import com.autonavi.ae.gmap.listener.MapSurfaceListener;
import com.autonavi.ae.gmap.listener.MapWidgetListener;
import com.autonavi.ae.gmap.maploader.NetworkState;
import com.autonavi.ae.gmap.maploader.VMapDataCache;
import com.autonavi.ae.gmap.style.MapTilsCacheAndResManager;
import com.autonavi.ae.gmap.style.StyleItem;
import com.autonavi.ae.gmap.utils.GLConvertUtil;
import com.autonavi.ae.gmap.utils.GLMapStaticValue;
import com.autonavi.amap.mapcore.AEUtil;
import com.autonavi.amap.mapcore.AMapNativeRenderer;
import com.autonavi.amap.mapcore.CameraUpdateMessage;
import com.autonavi.amap.mapcore.DPoint;
import com.autonavi.amap.mapcore.FPoint;
import com.autonavi.amap.mapcore.FileUtil;
import com.autonavi.amap.mapcore.IPoint;
import com.autonavi.amap.mapcore.MapConfig;
import com.autonavi.amap.mapcore.Rectangle;
import com.autonavi.amap.mapcore.VirtualEarthProjection;
import com.autonavi.amap.mapcore.animation.GLAlphaAnimation;
import com.autonavi.amap.mapcore.interfaces.IAMapListener;
import com.autonavi.amap.mapcore.interfaces.IMarkerAction;
import com.autonavi.amap.mapcore.interfaces.IMultiPointOverlay;
import com.autonavi.amap.mapcore.message.GestureMapMessage;
import com.qiniu.android.common.Constants;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/* JADX INFO: compiled from: AMapDelegateImp.java */
/* JADX INFO: loaded from: classes.dex */
public class a implements k, GLMapCoreCallback, IAMapListener {
    private static boolean aB = false;
    private AMap.OnMarkerClickListener H;
    private AMap.OnPolylineClickListener I;
    private AMap.OnMarkerDragListener J;
    private AMap.OnMapLoadedListener K;
    private AMap.OnCameraChangeListener L;
    private AMap.OnMapClickListener M;
    private AMap.OnMapTouchListener N;
    private AMap.OnPOIClickListener O;
    private AMap.OnMapLongClickListener P;
    private AMap.OnInfoWindowClickListener Q;
    private AMap.OnIndoorBuildingActiveListener R;
    private AMap.OnMyLocationChangeListener S;
    private e T;
    private AMapGestureListener X;
    private cs Y;
    private UiSettings Z;
    ae a;
    private Thread aA;
    private final q aE;
    private GLMapRender aJ;
    private boolean aN;
    private m aa;
    private final v ab;
    private final l ad;
    private int af;
    private MapSurfaceListener ai;
    private MapWidgetListener aj;
    private co an;
    private LocationSource ao;
    private int ba;
    private int bb;
    private b bc;
    ej c;
    final u d;
    protected final p e;
    final i f;
    protected ac h;
    CustomRenderer i;
    protected Context k;
    protected GLMapEngine l;
    h m;
    public int n;
    public int o;
    cw p;
    aj q;
    private int C = -1;
    private int D = -1;
    private int E = 0;
    private Bitmap F = null;
    private Bitmap G = null;
    private AMap.onMapPrintScreenListener U = null;
    private AMap.OnMapScreenShotListener V = null;
    private Object W = new Object();
    protected boolean b = false;
    private boolean ac = false;
    private boolean ae = true;
    private boolean ag = false;
    private int ah = 0;
    private boolean ak = false;
    protected MapConfig g = new MapConfig(true);
    private boolean al = false;
    private boolean am = false;
    private boolean ap = false;
    private Marker aq = null;
    private cf ar = null;
    private boolean as = false;
    private boolean at = false;
    private boolean au = false;
    private boolean av = false;
    private boolean aw = false;
    private Rect ax = new Rect();
    private int ay = 1;
    private MyTrafficStyle az = null;
    private boolean aC = false;
    private int aD = 0;
    private int aF = -1;
    int j = -1;
    private AtomicInteger aG = new AtomicInteger();
    private ConcurrentMap<Integer, AMap.CancelableCallback> aH = new ConcurrentHashMap();
    private List<r> aI = new ArrayList();
    private boolean aK = false;
    private float aL = 0.0f;
    private float aM = 1.0f;
    private boolean aO = true;
    private boolean aP = false;
    private boolean aQ = false;
    private int aR = 0;
    private GL10 aS = null;
    private volatile boolean aT = false;
    private volatile boolean aU = false;
    private boolean aV = true;
    private boolean aW = false;
    private boolean aX = false;
    private Lock aY = new ReentrantLock();
    private int aZ = 0;
    protected final Handler r = new Handler(Looper.getMainLooper()) { // from class: com.amap.api.mapcore.util.a.1
        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message == null || a.this.ag) {
                return;
            }
            try {
                a.this.p();
                int i = message.what;
                if (i == 2) {
                    StringBuilder sb = new StringBuilder("Key验证失败：[");
                    if (message.obj != null) {
                        sb.append(message.obj);
                    } else {
                        sb.append(ex.b);
                    }
                    sb.append("]");
                    Log.w("amapsdk", sb.toString());
                } else {
                    boolean z = true;
                    switch (i) {
                        case 10:
                            CameraPosition cameraPosition = (CameraPosition) message.obj;
                            if (cameraPosition != null && a.this.L != null) {
                                a.this.L.onCameraChange(cameraPosition);
                            }
                            break;
                        case 11:
                            try {
                                CameraPosition cameraPosition2 = a.this.getCameraPosition();
                                if (cameraPosition2 != null && a.this.c != null) {
                                    a.this.c.a(cameraPosition2);
                                }
                                if (a.this.aQ) {
                                    a.this.aQ = false;
                                    if (a.this.d != null) {
                                        a.this.d.b(false);
                                    }
                                    a.this.g(true);
                                }
                                if (a.this.av) {
                                    a.this.j();
                                    a.this.av = false;
                                }
                                a.this.a(true, cameraPosition2);
                                synchronized (a.this.W) {
                                    try {
                                        a.this.aG.get();
                                        int animateionsCount = a.this.l != null ? a.this.l.getAnimateionsCount() : -1;
                                        if (a.this.aG.get() > 0 && animateionsCount == 0) {
                                            try {
                                                AMap.CancelableCallback cancelableCallback = (AMap.CancelableCallback) a.this.aH.remove(Integer.valueOf(a.this.aG.decrementAndGet()));
                                                if (cancelableCallback != null) {
                                                    cancelableCallback.onFinish();
                                                }
                                            } catch (Throwable th) {
                                                th.printStackTrace();
                                            }
                                        }
                                    } catch (Throwable th2) {
                                        throw th2;
                                    }
                                }
                            } catch (Throwable th3) {
                                fn.b(th3, "AMapDelegateImp", "CameraUpdateFinish");
                            }
                            break;
                        case 12:
                            if (a.this.c != null) {
                                a.this.c.a(a.this.g());
                            }
                            break;
                        case 13:
                            if (a.this.c != null) {
                                eg egVarG = a.this.c.g();
                                if (egVarG != null) {
                                    egVarG.b();
                                    break;
                                }
                            }
                            break;
                        case 14:
                            try {
                                if (a.this.N != null) {
                                    a.this.N.onTouch((MotionEvent) message.obj);
                                }
                            } catch (Throwable th4) {
                                fn.b(th4, "AMapDelegateImp", "onTouchHandler");
                                th4.printStackTrace();
                            }
                            break;
                        case 15:
                            Bitmap bitmap = (Bitmap) message.obj;
                            int i2 = message.arg1;
                            if (bitmap == null || a.this.c == null) {
                                if (a.this.U != null) {
                                    a.this.U.onMapPrint(null);
                                }
                                if (a.this.V != null) {
                                    a.this.V.onMapScreenShot(null);
                                    a.this.V.onMapScreenShot(null, i2);
                                }
                            } else {
                                Canvas canvas = new Canvas(bitmap);
                                el elVarH = a.this.c.h();
                                if (elVarH != null) {
                                    elVarH.onDraw(canvas);
                                }
                                a.this.c.a(canvas);
                                if (a.this.U != null) {
                                    a.this.U.onMapPrint(new BitmapDrawable(a.this.k.getResources(), bitmap));
                                }
                                if (a.this.V != null) {
                                    a.this.V.onMapScreenShot(bitmap);
                                    a.this.V.onMapScreenShot(bitmap, i2);
                                }
                            }
                            a.this.U = null;
                            a.this.V = null;
                            break;
                        case 16:
                            if (a.this.K != null) {
                                try {
                                    a.this.K.onMapLoaded();
                                } catch (Throwable th5) {
                                    fn.b(th5, "AMapDelegateImp", "onMapLoaded");
                                    th5.printStackTrace();
                                }
                            }
                            break;
                        case 17:
                            if (a.this.l.isInMapAnimation(1) && a.this.d != null) {
                                a.this.d.b(false);
                            }
                            if (a.this.d != null) {
                                u uVar = a.this.d;
                                if (message.arg1 == 0) {
                                    z = false;
                                }
                                uVar.a(z);
                            }
                            break;
                        case 18:
                            if (a.this.a != null) {
                                a.this.a.d();
                            }
                            break;
                        case 19:
                            if (a.this.M != null) {
                                DPoint dPointObtain = DPoint.obtain();
                                a.this.b(message.arg1, message.arg2, dPointObtain);
                                try {
                                    a.this.M.onMapClick(new LatLng(dPointObtain.y, dPointObtain.x));
                                    dPointObtain.recycle();
                                } catch (Throwable th6) {
                                    fn.b(th6, "AMapDelegateImp", "OnMapClickListener.onMapClick");
                                    th6.printStackTrace();
                                }
                            }
                            break;
                        case 20:
                            try {
                                a.this.O.onPOIClick((Poi) message.obj);
                            } catch (Throwable th7) {
                                fn.b(th7, "AMapDelegateImp", "OnPOIClickListener.onPOIClick");
                                th7.printStackTrace();
                            }
                            break;
                    }
                    return;
                }
                a.this.p();
            } catch (Throwable th8) {
                fn.b(th8, "AMapDelegateImp", "handleMessage");
                th8.printStackTrace();
            }
        }
    };
    private AbstractRunnableC0012a bd = new AbstractRunnableC0012a() { // from class: com.amap.api.mapcore.util.a.11
        @Override // com.amap.api.mapcore.util.a.AbstractRunnableC0012a, java.lang.Runnable
        public void run() {
            super.run();
            try {
                a.this.setTrafficEnabled(this.c);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    };
    private AbstractRunnableC0012a be = new AbstractRunnableC0012a() { // from class: com.amap.api.mapcore.util.a.17
        @Override // com.amap.api.mapcore.util.a.AbstractRunnableC0012a, java.lang.Runnable
        public void run() {
            super.run();
            a.this.a(this.g, this.c);
        }
    };
    private AbstractRunnableC0012a bf = new AbstractRunnableC0012a() { // from class: com.amap.api.mapcore.util.a.25
        @Override // com.amap.api.mapcore.util.a.AbstractRunnableC0012a, java.lang.Runnable
        public void run() {
            super.run();
            try {
                a aVar = a.this;
                aVar.setCenterToPixel(aVar.ba, a.this.bb);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    };
    private AbstractRunnableC0012a bg = new AbstractRunnableC0012a() { // from class: com.amap.api.mapcore.util.a.34
        @Override // com.amap.api.mapcore.util.a.AbstractRunnableC0012a, java.lang.Runnable
        public void run() {
            super.run();
            a.this.b(this.g, this.d, this.e, this.f);
        }
    };
    private AbstractRunnableC0012a bh = new AbstractRunnableC0012a() { // from class: com.amap.api.mapcore.util.a.39
        @Override // com.amap.api.mapcore.util.a.AbstractRunnableC0012a, java.lang.Runnable
        public void run() {
            super.run();
            a.this.setMapCustomEnable(this.c);
        }
    };
    private AbstractRunnableC0012a bi = new AbstractRunnableC0012a() { // from class: com.amap.api.mapcore.util.a.40
        @Override // com.amap.api.mapcore.util.a.AbstractRunnableC0012a, java.lang.Runnable
        public void run() {
            super.run();
            a.this.c(this.g, this.c);
        }
    };
    private AbstractRunnableC0012a bj = new AbstractRunnableC0012a() { // from class: com.amap.api.mapcore.util.a.41
        @Override // com.amap.api.mapcore.util.a.AbstractRunnableC0012a, java.lang.Runnable
        public void run() {
            super.run();
            a.this.d(this.g, this.c);
        }
    };
    private AbstractRunnableC0012a bk = new AbstractRunnableC0012a() { // from class: com.amap.api.mapcore.util.a.42
        @Override // com.amap.api.mapcore.util.a.AbstractRunnableC0012a, java.lang.Runnable
        public void run() {
            super.run();
            a.this.e(this.g, this.c);
        }
    };
    private AbstractRunnableC0012a bl = new AbstractRunnableC0012a() { // from class: com.amap.api.mapcore.util.a.2
        @Override // com.amap.api.mapcore.util.a.AbstractRunnableC0012a, java.lang.Runnable
        public void run() {
            super.run();
            a.this.f(this.g, this.c);
        }
    };
    private AbstractRunnableC0012a bm = new AbstractRunnableC0012a() { // from class: com.amap.api.mapcore.util.a.3
        @Override // com.amap.api.mapcore.util.a.AbstractRunnableC0012a, java.lang.Runnable
        public void run() {
            super.run();
            try {
                a.this.setIndoorEnabled(this.c);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    };
    private Runnable bn = new Runnable() { // from class: com.amap.api.mapcore.util.a.4
        @Override // java.lang.Runnable
        public void run() {
            el elVarH;
            if (a.this.c == null || (elVarH = a.this.c.h()) == null) {
                return;
            }
            elVarH.d();
        }
    };
    private AbstractRunnableC0012a bo = new AbstractRunnableC0012a() { // from class: com.amap.api.mapcore.util.a.5
        @Override // com.amap.api.mapcore.util.a.AbstractRunnableC0012a, java.lang.Runnable
        public void run() {
            super.run();
            a.this.g(this.g, this.c);
        }
    };
    private EAMapPlatformGestureInfo bp = new EAMapPlatformGestureInfo();
    Point s = new Point();
    Rect t = new Rect();
    protected String u = null;
    private ac bq = null;
    float[] v = new float[16];
    float[] w = new float[16];
    float[] x = new float[16];
    private IPoint[] br = null;
    float[] y = new float[12];
    String z = "precision highp float;\nattribute vec3 aVertex;//顶点数组,三维坐标\nuniform mat4 aMVPMatrix;//mvp矩阵\nvoid main(){\n  gl_Position = aMVPMatrix * vec4(aVertex, 1.0);\n}";
    String A = "//有颜色 没有纹理\nprecision highp float;\nvoid main(){\n  gl_FragColor = vec4(1.0,0,0,1.0);\n}";
    int B = -1;

    @Override // com.amap.api.mapcore.util.k
    public int a(IMarkerAction iMarkerAction, Rect rect) {
        return 0;
    }

    @Override // com.autonavi.ae.gmap.callback.GLMapCoreCallback
    public void clearException(int i) {
    }

    @Override // com.amap.api.mapcore.util.k
    public boolean d(int i) {
        return false;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public String getSatelliteImageApprovalNumber() {
        return "GS（2017）1898";
    }

    @Override // com.autonavi.ae.gmap.callback.GLMapCoreCallback
    public void onException(int i, int i2) {
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void reloadMap() {
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setZOrderOnTop(boolean z) throws RemoteException {
    }

    @Override // com.amap.api.mapcore.util.k
    public void a(MapWidgetListener mapWidgetListener) {
        this.aj = mapWidgetListener;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setVisibilityEx(int i) {
        l lVar = this.ad;
        if (lVar != null) {
            try {
                lVar.setVisibility(i);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void onActivityPause() {
        this.ak = true;
        p(this.af);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void onActivityResume() {
        this.ak = false;
        int engineIDWithType = this.af;
        if (engineIDWithType == 0) {
            engineIDWithType = this.l.getEngineIDWithType(0);
        }
        q(engineIDWithType);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void queueEvent(Runnable runnable) {
        this.ad.queueEvent(runnable);
    }

    /* JADX INFO: renamed from: com.amap.api.mapcore.util.a$a, reason: collision with other inner class name */
    /* JADX INFO: compiled from: AMapDelegateImp.java */
    private static abstract class AbstractRunnableC0012a implements Runnable {
        boolean b;
        boolean c;
        int d;
        int e;
        int f;
        int g;

        private AbstractRunnableC0012a() {
            this.b = false;
            this.c = false;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.b = false;
        }
    }

    public a(l lVar, Context context, AttributeSet attributeSet) throws Throwable {
        this.Y = null;
        this.aN = false;
        this.k = context;
        AEUtil.init(context);
        g.b = ew.c(context);
        dc.a(this.k);
        this.m = new h(this);
        this.l = new GLMapEngine(this.k, this);
        GLMapRender gLMapRender = new GLMapRender(this);
        this.aJ = gLMapRender;
        this.ad = lVar;
        lVar.setRenderer(gLMapRender);
        this.af = a(0, new Rect(0, 0, this.n, this.o), this.n, this.o);
        this.ab = new v(this);
        ej ejVar = new ej(this.k, this);
        this.c = ejVar;
        ejVar.e().a(new c());
        this.bc = new b();
        this.f = new i(this);
        this.d = new u(this.k, this);
        this.e = new p(this.k, this);
        this.l.setMapCoreListener(this);
        lVar.setRenderMode(0);
        this.aN = false;
        this.aJ.setRenderFps(15.0f);
        this.l.setMapListener(this);
        this.aa = new s(this);
        this.T = new e(this);
        this.Y = new cs(this, context);
        ae aeVar = new ae(this.k);
        this.a = aeVar;
        aeVar.a(this.c);
        this.a.b(this.Y);
        this.aE = new q();
        this.aA = new f(this.k, this);
        this.ao = new af(this.k);
        this.q = new aj(this);
        this.p = new cw();
    }

    @Override // com.amap.api.mapcore.util.k
    public GLMapEngine a() {
        return this.l;
    }

    @Override // com.amap.api.mapcore.util.k
    public void a(int i, int i2) {
        if (this.aZ == 0 || i2 != 5) {
            this.aZ = i2;
        }
    }

    public void a(final int i, final boolean z) {
        if (c(i, 2) == z) {
            return;
        }
        if (this.aT && this.aU && this.b) {
            queueEvent(new Runnable() { // from class: com.amap.api.mapcore.util.a.6
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        a.this.b(i, 2, z);
                        a.this.l.setParamater(i, GLMapStaticValue.AM_PARAMETERNAME_SETTRAFFICTEXTURE, z ? 0 : 1, 0, 0, 0);
                        a.this.resetRenderTimeLong();
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }
            });
            return;
        }
        this.be.c = z;
        this.be.b = true;
        this.be.g = i;
    }

    @Override // com.amap.api.mapcore.util.k
    public float a(int i) {
        if (this.aT) {
            return this.l.getMapZoomer(i);
        }
        return 0.0f;
    }

    public float b(int i) {
        if (this.aT) {
            return this.l.getMapZoomer(i);
        }
        if (this.g != null) {
            return getMapConfig().getS_z();
        }
        return 0.0f;
    }

    public boolean a(int i, int i2, int i3) {
        CameraUpdateMessage cameraUpdateMessageA;
        if (!this.aT || ((int) b(i)) >= this.l.getMaxZoomLevel(i)) {
            return false;
        }
        try {
            if (!this.al && !this.ab.isZoomInByScreenCenter()) {
                this.s.x = i2;
                this.s.y = i3;
                cameraUpdateMessageA = z.a(1.0f, this.s);
            } else {
                cameraUpdateMessageA = z.a(1.0f, (Point) null);
            }
            b(cameraUpdateMessageA);
        } catch (Throwable th) {
            fn.b(th, "AMapDelegateImp", "onDoubleTap");
            th.printStackTrace();
        }
        p();
        return true;
    }

    @Override // com.amap.api.mapcore.util.k
    public void c(int i) {
        if (this.aT && ((int) b(i)) > this.l.getMinZoomLevel(i)) {
            try {
                b(z.b());
            } catch (Throwable th) {
                fn.b(th, "AMapDelegateImp", "onDoubleTap");
                th.printStackTrace();
            }
            p();
        }
    }

    @Override // com.amap.api.mapcore.util.k
    public boolean e(int i) {
        return c(i, 6);
    }

    private void t(final int i) {
        if (this.aT) {
            this.m.a();
            this.aK = true;
            this.aP = true;
            try {
                stopAnimation();
            } catch (RemoteException unused) {
            }
            queueEvent(new Runnable() { // from class: com.amap.api.mapcore.util.a.7
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        a.this.l.clearAllMessages(i);
                        a.this.l.setParamater(i, 4001, 1, 0, 0, 0);
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }
            });
        }
    }

    private void u(final int i) {
        Marker marker;
        this.aK = true;
        this.aP = false;
        if (this.at) {
            this.at = false;
        }
        if (this.as) {
            this.as = false;
        }
        if (this.au) {
            this.au = false;
        }
        this.ap = false;
        AMap.OnMarkerDragListener onMarkerDragListener = this.J;
        if (onMarkerDragListener != null && (marker = this.aq) != null) {
            try {
                onMarkerDragListener.onMarkerDragEnd(marker);
            } catch (Throwable th) {
                fn.b(th, "AMapDelegateImp", "OnMarkerDragListener.onMarkerDragEnd");
                th.printStackTrace();
            }
            this.aq = null;
        }
        queueEvent(new Runnable() { // from class: com.amap.api.mapcore.util.a.8
            @Override // java.lang.Runnable
            public void run() {
                try {
                    a.this.l.setParamater(i, 4001, 0, 0, 0, 0);
                } catch (Throwable th2) {
                    th2.printStackTrace();
                }
            }
        });
        this.ad.postDelayed(new Runnable() { // from class: com.amap.api.mapcore.util.a.9
            @Override // java.lang.Runnable
            public void run() {
                a.this.aR = 1;
            }
        }, 300L);
    }

    private void a(MotionEvent motionEvent) throws RemoteException {
        if (!this.ap || this.aq == null || this.ar == null) {
            return;
        }
        int x = (int) motionEvent.getX();
        int y = (int) (motionEvent.getY() - 60.0f);
        LatLng latLngB = this.ar.b();
        LatLng position = this.ar.getPosition();
        DPoint dPointObtain = DPoint.obtain();
        b(x, y, dPointObtain);
        LatLng latLng = new LatLng((position.latitude + dPointObtain.y) - latLngB.latitude, (position.longitude + dPointObtain.x) - latLngB.longitude);
        dPointObtain.recycle();
        this.aq.setPosition(latLng);
        AMap.OnMarkerDragListener onMarkerDragListener = this.J;
        if (onMarkerDragListener != null) {
            onMarkerDragListener.onMarkerDrag(this.aq);
        }
    }

    @Override // com.amap.api.mapcore.util.k
    public GLMapState b() {
        GLMapEngine gLMapEngine = this.l;
        if (gLMapEngine != null) {
            return gLMapEngine.getMapState(1);
        }
        return null;
    }

    @Override // com.amap.api.mapcore.util.k
    public int c() {
        return this.C;
    }

    @Override // com.amap.api.mapcore.util.k
    public int d() {
        return this.D;
    }

    @Override // com.amap.api.mapcore.util.k
    public int e() {
        return this.E;
    }

    @Override // com.amap.api.mapcore.util.k
    public void a(Location location) throws RemoteException {
        if (location == null) {
            return;
        }
        try {
            if (this.ae && this.ao != null) {
                co coVar = this.an;
                if (coVar == null && coVar == null) {
                    this.an = new co(this, this.k);
                }
                if (location.getLongitude() != 0.0d && location.getLatitude() != 0.0d) {
                    this.an.a(location);
                }
                AMap.OnMyLocationChangeListener onMyLocationChangeListener = this.S;
                if (onMyLocationChangeListener != null) {
                    onMyLocationChangeListener.onMyLocationChange(location);
                }
                p();
                return;
            }
            co coVar2 = this.an;
            if (coVar2 != null) {
                coVar2.b();
            }
            this.an = null;
        } catch (Throwable th) {
            fn.b(th, "AMapDelegateImp", "showMyLocationOverlay");
            th.printStackTrace();
        }
    }

    @Override // com.amap.api.mapcore.util.k
    public boolean a(String str) throws RemoteException {
        p();
        return this.f.d(str);
    }

    @Override // com.amap.api.mapcore.util.k
    public void f() {
        this.f.c();
    }

    @Override // com.amap.api.mapcore.util.k
    public void a(double d2, double d3, IPoint iPoint) {
        GLMapState.lonlat2Geo(d3, d2, iPoint);
    }

    @Override // com.amap.api.mapcore.util.k
    public void a(int i, int i2, FPoint fPoint) {
        this.l.p20ToMapPoint(1, i2, i, fPoint);
    }

    @Override // com.amap.api.mapcore.util.k
    public void a(int i, int i2, DPoint dPoint) {
        GLMapState.geo2LonLat(i, i2, dPoint);
    }

    @Override // com.amap.api.mapcore.util.k
    public float g() {
        return b(this.af);
    }

    @Override // com.amap.api.mapcore.util.k
    public n h() {
        return this.ab;
    }

    @Override // com.amap.api.mapcore.util.k
    public void a(cf cfVar) throws RemoteException {
        ae aeVar;
        if (cfVar == null || (aeVar = this.a) == null) {
            return;
        }
        try {
            aeVar.a(cfVar);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override // com.amap.api.mapcore.util.k
    public void i() {
        ae aeVar = this.a;
        if (aeVar != null) {
            aeVar.f();
        }
    }

    @Override // com.amap.api.mapcore.util.k
    public void a(double d2, double d3, FPoint fPoint) {
        if (!this.aT || this.l == null) {
            return;
        }
        Point pointLatLongToPixels = VirtualEarthProjection.LatLongToPixels(d2, d3, 20);
        this.l.p20ToMapPoint(1, pointLatLongToPixels.x, pointLatLongToPixels.y, fPoint);
    }

    @Override // com.amap.api.mapcore.util.k
    public void b(int i, int i2, DPoint dPoint) {
        if (!this.aT || this.l == null) {
            return;
        }
        Point point = new Point();
        this.l.screenToP20Point(1, i, i2, point);
        DPoint dPointPixelsToLatLong = VirtualEarthProjection.PixelsToLatLong(point.x, point.y, 20);
        dPoint.x = dPointPixelsToLatLong.x;
        dPoint.y = dPointPixelsToLatLong.y;
        dPointPixelsToLatLong.recycle();
    }

    protected void a(GLMapState gLMapState, int i, int i2, DPoint dPoint) {
        if (!this.aT || this.l == null) {
            return;
        }
        Point point = new Point();
        gLMapState.screenToP20Point(i, i2, point);
        DPoint dPointPixelsToLatLong = VirtualEarthProjection.PixelsToLatLong(point.x, point.y, 20);
        dPoint.x = dPointPixelsToLatLong.x;
        dPoint.y = dPointPixelsToLatLong.y;
        dPointPixelsToLatLong.recycle();
    }

    @Override // com.amap.api.mapcore.util.k
    public void b(double d2, double d3, IPoint iPoint) {
        if (!this.aT || this.l == null) {
            return;
        }
        try {
            Point pointLatLongToPixels = VirtualEarthProjection.LatLongToPixels(d2, d3, 20);
            FPoint fPointObtain = FPoint.obtain();
            this.l.p20ToScreenPoint(1, pointLatLongToPixels.x, pointLatLongToPixels.y, fPointObtain);
            if (fPointObtain.x == -10000.0f && fPointObtain.y == -10000.0f) {
                GLMapState newMapState = this.l.getNewMapState(1);
                newMapState.setCameraDegree(0.0f);
                newMapState.recalculate();
                newMapState.p20ToScreenPoint(pointLatLongToPixels.x, pointLatLongToPixels.y, fPointObtain);
                newMapState.recycle();
            }
            iPoint.x = (int) fPointObtain.x;
            iPoint.y = (int) fPointObtain.y;
            fPointObtain.recycle();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    @Override // com.amap.api.mapcore.util.k
    public void a(int i, int i2, IPoint iPoint) {
        GLMapEngine gLMapEngine;
        if (!this.aT || (gLMapEngine = this.l) == null) {
            return;
        }
        gLMapEngine.screenToP20Point(1, i, i2, iPoint);
    }

    public void a(int i, int i2, PointF pointF) {
        GLMapEngine gLMapEngine;
        if (!this.aT || (gLMapEngine = this.l) == null) {
            return;
        }
        gLMapEngine.p20ToScreenPoint(1, i, i2, pointF);
    }

    @Override // com.amap.api.mapcore.util.k
    public void j() {
        if (this.aT) {
            this.r.sendEmptyMessage(18);
        }
    }

    @Override // com.amap.api.mapcore.util.k
    public void a(boolean z) {
        ej ejVar;
        if (this.ag || (ejVar = this.c) == null) {
            return;
        }
        ejVar.b(z);
    }

    @Override // com.amap.api.mapcore.util.k
    public void b(boolean z) {
        ej ejVar;
        if (this.ag || (ejVar = this.c) == null) {
            return;
        }
        ejVar.a(z);
    }

    @Override // com.amap.api.mapcore.util.k
    public void c(boolean z) {
        ej ejVar;
        if (this.ag || (ejVar = this.c) == null) {
            return;
        }
        ejVar.c(z);
    }

    @Override // com.amap.api.mapcore.util.k
    public void d(boolean z) {
        ej ejVar;
        if (this.ag || (ejVar = this.c) == null) {
            return;
        }
        ejVar.d(z);
    }

    @Override // com.amap.api.mapcore.util.k
    public void e(boolean z) {
        ej ejVar;
        if (this.ag || (ejVar = this.c) == null) {
            return;
        }
        ejVar.e(z);
    }

    @Override // com.amap.api.mapcore.util.k
    public void f(int i) {
        ej ejVar;
        if (this.ag || (ejVar = this.c) == null) {
            return;
        }
        ejVar.a(i);
    }

    @Override // com.amap.api.mapcore.util.k
    public LatLngBounds a(LatLng latLng, float f, float f2, float f3) {
        int mapWidth = getMapWidth();
        int mapHeight = getMapHeight();
        if (mapWidth <= 0 || mapHeight <= 0 || this.ag) {
            return null;
        }
        float fA = dw.a(this.g, f);
        GLMapState gLMapState = new GLMapState(1, this.l.getMapEnginePtr());
        if (latLng != null) {
            IPoint iPointObtain = IPoint.obtain();
            GLMapState.lonlat2Geo(latLng.longitude, latLng.latitude, iPointObtain);
            gLMapState.setCameraDegree(0.0f);
            gLMapState.setMapAngle(0.0f);
            gLMapState.setMapGeoCenter(iPointObtain.x, iPointObtain.y);
            gLMapState.setMapZoomer(fA);
            gLMapState.recalculate();
            iPointObtain.recycle();
        }
        DPoint dPointObtain = DPoint.obtain();
        a(gLMapState, 0, 0, dPointObtain);
        LatLng latLng2 = new LatLng(dPointObtain.y, dPointObtain.x, false);
        a(gLMapState, mapWidth, mapHeight, dPointObtain);
        LatLng latLng3 = new LatLng(dPointObtain.y, dPointObtain.x, false);
        dPointObtain.recycle();
        gLMapState.recycle();
        return LatLngBounds.builder().include(latLng3).include(latLng2).build();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.ak || !this.aT || !this.aO) {
            return false;
        }
        this.bp.mGestureState = 3;
        this.bp.mGestureType = 8;
        this.bp.mLocation = new float[]{motionEvent.getX(), motionEvent.getY()};
        int iA = a(this.bp);
        q();
        int action = motionEvent.getAction() & 255;
        if (action == 0) {
            r();
            t(iA);
        } else if (action == 1) {
            u(iA);
        }
        if (motionEvent.getAction() == 2 && this.ap) {
            try {
                a(motionEvent);
            } catch (Throwable th) {
                fn.b(th, "AMapDelegateImp", "onDragMarker");
                th.printStackTrace();
            }
            return true;
        }
        if (this.aK) {
            try {
                this.m.a(motionEvent);
            } catch (Throwable th2) {
                th2.printStackTrace();
            }
        }
        if (this.N != null) {
            this.r.removeMessages(14);
            Message messageObtainMessage = this.r.obtainMessage();
            messageObtainMessage.what = 14;
            messageObtainMessage.obj = MotionEvent.obtain(motionEvent);
            messageObtainMessage.sendToTarget();
        }
        return true;
    }

    @Override // com.amap.api.mapcore.util.k
    public void b(int i, int i2, PointF pointF) {
        GLMapEngine gLMapEngine;
        if (!this.aT || this.ak || (gLMapEngine = this.l) == null) {
            return;
        }
        gLMapEngine.screenToMapGPoint(1, i, i2, pointF);
    }

    @Override // com.amap.api.mapcore.util.k
    public void a(float f, float f2, IPoint iPoint) {
        GLMapEngine gLMapEngine;
        if (!this.aT || this.ak || (gLMapEngine = this.l) == null) {
            return;
        }
        gLMapEngine.mapToP20Point(1, f, f2, iPoint);
    }

    @Override // com.amap.api.mapcore.util.k
    public float g(int i) {
        GLMapEngine gLMapEngine;
        if (!this.aT || this.ak || (gLMapEngine = this.l) == null) {
            return 0.0f;
        }
        return gLMapEngine.getMapState(1).getGLUnitWithWin(i);
    }

    public CameraPosition f(boolean z) {
        LatLng latLngX;
        try {
            if (this.aT && !this.ak && this.l != null) {
                if (z) {
                    DPoint dPointObtain = DPoint.obtain();
                    b(this.g.getAnchorX(), this.g.getAnchorY(), dPointObtain);
                    latLngX = new LatLng(dPointObtain.y, dPointObtain.x, false);
                    dPointObtain.recycle();
                } else {
                    latLngX = x();
                }
                return CameraPosition.builder().target(latLngX).bearing(this.l.getMapAngle(1)).tilt(this.l.getCameraDegree(1)).zoom(this.l.getMapZoomer(1)).build();
            }
            DPoint dPointObtain2 = DPoint.obtain();
            a(this.g.getS_x(), this.g.getS_y(), dPointObtain2);
            LatLng latLng = new LatLng(dPointObtain2.y, dPointObtain2.x);
            dPointObtain2.recycle();
            return CameraPosition.builder().target(latLng).bearing(this.g.getS_r()).tilt(this.g.getS_c()).zoom(this.g.getS_z()).build();
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    private LatLng x() {
        GLMapEngine gLMapEngine;
        GLMapState mapState;
        if (this.aT && (gLMapEngine = this.l) != null && (mapState = gLMapEngine.getMapState(1)) != null) {
            Point mapGeoCenter = mapState.getMapGeoCenter();
            DPoint dPointPixelsToLatLong = VirtualEarthProjection.PixelsToLatLong(mapGeoCenter.x, mapGeoCenter.y, 20);
            LatLng latLng = new LatLng(dPointPixelsToLatLong.y, dPointPixelsToLatLong.x, false);
            dPointPixelsToLatLong.recycle();
            return latLng;
        }
        DPoint dPointPixelsToLatLong2 = VirtualEarthProjection.PixelsToLatLong(this.g.getS_x(), this.g.getS_y(), 20);
        LatLng latLng2 = new LatLng(dPointPixelsToLatLong2.y, dPointPixelsToLatLong2.x, false);
        dPointPixelsToLatLong2.recycle();
        return latLng2;
    }

    @Override // com.amap.api.mapcore.util.k
    public Point k() {
        ej ejVar = this.c;
        if (ejVar != null) {
            return ejVar.c();
        }
        return new Point();
    }

    @Override // com.amap.api.mapcore.util.k
    public View l() {
        Object obj = this.ad;
        if (obj instanceof View) {
            return (View) obj;
        }
        return null;
    }

    @Override // com.amap.api.mapcore.util.k
    public boolean m() {
        ac acVar;
        GLMapEngine gLMapEngine = this.l;
        if (gLMapEngine == null || gLMapEngine.getMapZoomer(1) < 17.0f || (acVar = this.h) == null || acVar.g == null) {
            return false;
        }
        FPoint fPointObtain = FPoint.obtain();
        a(this.h.g.x, this.h.g.y, (PointF) fPointObtain);
        return this.ax.contains((int) fPointObtain.x, (int) fPointObtain.y);
    }

    private synchronized void y() {
        synchronized (this.aI) {
            int size = this.aI.size();
            for (int i = 0; i < size; i++) {
                this.aI.get(i).e().recycle();
            }
            this.aI.clear();
        }
    }

    @Override // com.amap.api.mapcore.util.k
    public void a(r rVar) {
        if (rVar == null || rVar.f() == 0) {
            return;
        }
        synchronized (this.aI) {
            this.aI.add(rVar);
        }
    }

    @Override // com.amap.api.mapcore.util.k
    public void b(String str) {
        synchronized (this.aI) {
            int size = this.aI.size();
            int i = 0;
            while (true) {
                if (i >= size) {
                    i = -1;
                    break;
                } else if (this.aI.get(i).j().equals(str)) {
                    break;
                } else {
                    i++;
                }
            }
            if (i >= 0) {
                this.aI.remove(i);
            }
        }
    }

    @Override // com.amap.api.mapcore.util.k
    public r a(BitmapDescriptor bitmapDescriptor) {
        if (bitmapDescriptor == null || bitmapDescriptor.getBitmap() == null || bitmapDescriptor.getBitmap().isRecycled()) {
            return null;
        }
        synchronized (this.aI) {
            for (int i = 0; i < this.aI.size(); i++) {
                r rVar = this.aI.get(i);
                if (rVar.e().equals(bitmapDescriptor)) {
                    return rVar;
                }
            }
            return null;
        }
    }

    @Override // com.amap.api.mapcore.util.k
    public void h(int i) {
        ej ejVar = this.c;
        if (ejVar != null) {
            ejVar.b(i);
        }
    }

    @Override // com.amap.api.mapcore.util.k
    public void i(int i) {
        ej ejVar = this.c;
        if (ejVar != null) {
            ejVar.c(i);
        }
    }

    @Override // com.amap.api.mapcore.util.k
    public void j(int i) {
        ej ejVar = this.c;
        if (ejVar != null) {
            ejVar.d(i);
        }
    }

    @Override // com.amap.api.mapcore.util.k
    public float k(int i) {
        ej ejVar = this.c;
        if (ejVar != null) {
            return ejVar.e(i);
        }
        return 0.0f;
    }

    @Override // com.amap.api.mapcore.util.k
    public void a(int i, float f) {
        ej ejVar = this.c;
        if (ejVar != null) {
            ejVar.a(i, f);
        }
    }

    @Override // com.amap.api.mapcore.util.k
    public int n() {
        return this.aF;
    }

    public void a(Runnable runnable) {
        l lVar = this.ad;
        if (lVar != null) {
            lVar.post(runnable);
        }
    }

    @Override // com.amap.api.mapcore.util.k
    public void a(int i, MotionEvent motionEvent) {
        try {
            this.as = false;
            l(i);
            cf cfVarA = this.e.a(motionEvent);
            this.ar = cfVarA;
            if (cfVarA != null && cfVarA.isDraggable()) {
                Marker marker = new Marker(this.ar);
                this.aq = marker;
                LatLng position = marker.getPosition();
                LatLng latLngB = this.ar.b();
                IPoint iPointObtain = IPoint.obtain();
                b(latLngB.latitude, latLngB.longitude, iPointObtain);
                iPointObtain.y -= 60;
                DPoint dPointObtain = DPoint.obtain();
                b(iPointObtain.x, iPointObtain.y, dPointObtain);
                this.aq.setPosition(new LatLng((position.latitude + dPointObtain.y) - latLngB.latitude, (position.longitude + dPointObtain.x) - latLngB.longitude));
                this.e.a(this.ar);
                AMap.OnMarkerDragListener onMarkerDragListener = this.J;
                if (onMarkerDragListener != null) {
                    onMarkerDragListener.onMarkerDragStart(this.aq);
                }
                this.ap = true;
                iPointObtain.recycle();
                dPointObtain.recycle();
            } else if (this.P != null) {
                DPoint dPointObtain2 = DPoint.obtain();
                b((int) motionEvent.getX(), (int) motionEvent.getY(), dPointObtain2);
                this.P.onMapLongClick(new LatLng(dPointObtain2.y, dPointObtain2.x));
                this.at = true;
                dPointObtain2.recycle();
            }
            this.aJ.resetTickCount(30);
        } catch (Throwable th) {
            fn.b(th, "AMapDelegateImp", "onLongPress");
            th.printStackTrace();
        }
    }

    @Override // com.amap.api.mapcore.util.k
    public boolean b(int i, MotionEvent motionEvent) {
        if (!this.aT) {
            return false;
        }
        a(i, (int) motionEvent.getX(), (int) motionEvent.getY());
        return false;
    }

    @Override // com.amap.api.mapcore.util.k
    public boolean c(int i, MotionEvent motionEvent) {
        if (!this.aT) {
            return false;
        }
        try {
            if (g(motionEvent) || e(motionEvent) || f(motionEvent) || d(motionEvent)) {
                return true;
            }
            b(motionEvent);
            return true;
        } catch (Throwable th) {
            fn.b(th, "AMapDelegateImp", "onSingleTapUp");
            th.printStackTrace();
            return true;
        }
    }

    private void b(final MotionEvent motionEvent) {
        queueEvent(new Runnable() { // from class: com.amap.api.mapcore.util.a.10
            @Override // java.lang.Runnable
            public void run() {
                try {
                    Message messageObtain = Message.obtain();
                    Poi poiB = a.this.b((int) motionEvent.getX(), (int) motionEvent.getY(), 25);
                    if (a.this.O == null || poiB == null) {
                        a.this.c(motionEvent);
                    } else {
                        messageObtain.what = 20;
                        messageObtain.obj = poiB;
                        a.this.r.sendMessage(messageObtain);
                    }
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c(final MotionEvent motionEvent) {
        this.r.post(new Runnable() { // from class: com.amap.api.mapcore.util.a.12
            @Override // java.lang.Runnable
            public void run() {
                Message messageObtain = Message.obtain();
                messageObtain.what = 19;
                messageObtain.arg1 = (int) motionEvent.getX();
                messageObtain.arg2 = (int) motionEvent.getY();
                a.this.r.sendMessage(messageObtain);
            }
        });
    }

    private boolean d(MotionEvent motionEvent) {
        if (this.I != null) {
            DPoint dPointObtain = DPoint.obtain();
            b((int) motionEvent.getX(), (int) motionEvent.getY(), dPointObtain);
            LatLng latLng = new LatLng(dPointObtain.y, dPointObtain.x);
            dPointObtain.recycle();
            ch chVarA = this.f.a(latLng);
            if (chVarA != null) {
                this.I.onPolylineClick(new Polyline((ck) chVarA));
            }
        }
        return false;
    }

    private boolean e(MotionEvent motionEvent) throws RemoteException {
        LatLng latLngB;
        if (!this.e.b(motionEvent)) {
            return false;
        }
        cf cfVarD = this.e.d();
        if (cfVarD == null) {
            return true;
        }
        try {
            Marker marker = new Marker(cfVarD);
            AMap.OnMarkerClickListener onMarkerClickListener = this.H;
            if (onMarkerClickListener != null && (onMarkerClickListener.onMarkerClick(marker) || this.e.g() <= 0)) {
                this.e.a(cfVarD);
                return true;
            }
            a(cfVarD);
            if (!cfVarD.g() && (latLngB = cfVarD.b()) != null) {
                IPoint iPointObtain = IPoint.obtain();
                a(latLngB.latitude, latLngB.longitude, iPointObtain);
                a(z.a(iPointObtain));
            }
            this.e.a(cfVarD);
            return true;
        } catch (Throwable th) {
            fn.b(th, "AMapDelegateImp", "onMarkerTap");
            th.printStackTrace();
            return false;
        }
    }

    private boolean f(MotionEvent motionEvent) {
        if (this.q == null) {
            return false;
        }
        IPoint iPointObtain = IPoint.obtain();
        if (this.l != null) {
            a((int) motionEvent.getX(), (int) motionEvent.getY(), iPointObtain);
        }
        boolean zA = this.q.a(iPointObtain);
        iPointObtain.recycle();
        return zA;
    }

    private boolean g(MotionEvent motionEvent) throws RemoteException {
        ae aeVar = this.a;
        if (aeVar == null || !aeVar.a(motionEvent)) {
            return false;
        }
        if (this.Q != null) {
            cf cfVarD = this.e.d();
            if (!cfVarD.isVisible()) {
                return true;
            }
            this.Q.onInfoWindowClick(new Marker(cfVarD));
        }
        return true;
    }

    private void h(int i, boolean z) {
        this.l.setParamater(i, GLMapStaticValue.AM_PARAMETERNAME_SETISSTIMAP, z ? 1 : 0, 0, 0, 0);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void drawFrame(GL10 gl10) {
        if (this.ag) {
            return;
        }
        a(1, gl10);
        GLES20.glClearColor(0.9453125f, 0.93359f, 0.9101f, 1.0f);
        GLES20.glClear(16640);
        this.l.drawFrame();
        this.l.pushRendererState();
        a(gl10);
        A();
        if (!this.aW) {
            this.aW = true;
            a(new Runnable() { // from class: com.amap.api.mapcore.util.a.13
                @Override // java.lang.Runnable
                public void run() {
                    if (a.this.ai != null) {
                        a.this.ai.onDrawFrameFirst(1);
                    }
                }
            });
        }
        CustomRenderer customRenderer = this.i;
        if (customRenderer != null) {
            customRenderer.onDrawFrame(gl10);
        }
        this.l.popRendererState();
    }

    private void a(int i, GL10 gl10) {
        int i2 = this.j;
        if (i2 != -1) {
            this.aJ.setRenderFps(i2);
            p();
            return;
        }
        if (this.l.isInMapAction(i) || this.aP) {
            this.aJ.setRenderFps(40.0f);
            return;
        }
        if (this.l.isInMapAnimation(i)) {
            this.aJ.setRenderFps(30.0f);
            this.aJ.resetTickCount(15);
        } else if (this.aN) {
            this.aJ.setRenderFps(10.0f);
        } else {
            this.aJ.setRenderFps(15.0f);
        }
    }

    private void a(GL10 gl10) {
        if (this.aw) {
            boolean zCanStopMapRender = this.l.canStopMapRender(1);
            Message messageObtainMessage = this.r.obtainMessage(15, dw.a(0, 0, getMapWidth(), getMapHeight()));
            messageObtainMessage.arg1 = zCanStopMapRender ? 1 : 0;
            messageObtainMessage.sendToTarget();
            this.aw = false;
        }
    }

    public void o() {
        if (this.g.getMapRect() == null || this.aC) {
            z();
            this.aC = false;
        }
        GLMapState mapState = this.l.getMapState(1);
        if (mapState != null) {
            Point mapGeoCenter = mapState.getMapGeoCenter();
            this.g.setS_x(mapGeoCenter.x);
            this.g.setS_y(mapGeoCenter.y);
            this.g.setS_z(mapState.getMapZoomer());
            this.g.setS_c(mapState.getCameraDegree());
            this.g.setS_r(mapState.getMapAngle());
            if (this.g.isMapStateChange()) {
                DPoint dPointPixelsToLatLong = VirtualEarthProjection.PixelsToLatLong(mapGeoCenter.x, mapGeoCenter.y, 20);
                CameraPosition cameraPosition = new CameraPosition(new LatLng(dPointPixelsToLatLong.y, dPointPixelsToLatLong.x, false), this.g.getS_z(), this.g.getS_c(), this.g.getS_r());
                dPointPixelsToLatLong.recycle();
                Message messageObtainMessage = this.r.obtainMessage();
                messageObtainMessage.what = 10;
                messageObtainMessage.obj = cameraPosition;
                this.r.sendMessage(messageObtainMessage);
                this.aQ = true;
                j();
                z();
                try {
                    if (this.ab.isZoomControlsEnabled() && this.g.isNeedUpdateZoomControllerState()) {
                        this.aj.invalidateZoomController(this.g.getS_z());
                    }
                    if (this.g.getChangeGridRatio() != 1.0d) {
                        g(true);
                    }
                    if (this.ab.isCompassEnabled() && (this.g.isTiltChanged() || this.g.isBearingChanged())) {
                        this.aj.invalidateCompassView();
                    }
                    if (this.ab.isScaleControlsEnabled()) {
                        this.aj.invalidateScaleView();
                        return;
                    }
                    return;
                } catch (Throwable th) {
                    th.printStackTrace();
                    return;
                }
            }
            if (!this.aP && this.l.getAnimateionsCount() == 0 && this.l.getStateMessageCount() == 0) {
                onChangeFinish();
            }
        }
    }

    private void z() {
        try {
            this.g.setMapRect(dw.a((k) this, true));
            GLMapState mapState = this.l.getMapState(1);
            if (mapState != null) {
                mapState.getPixel20Bound(this.t);
                this.g.getGeoRectangle().updateRect(this.t, this.g.getS_x(), this.g.getS_y());
                this.g.setMapPerPixelUnitLength(mapState.getGLUnitWithWin(1));
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private void A() {
        final ef efVarD = this.c.d();
        if (efVarD == null || efVarD.getVisibility() == 8) {
            return;
        }
        if (!this.am) {
            this.r.sendEmptyMessage(16);
            this.am = true;
            g(true);
        }
        this.r.post(new Runnable() { // from class: com.amap.api.mapcore.util.a.14
            @Override // java.lang.Runnable
            public void run() {
                if (a.this.ak) {
                    return;
                }
                try {
                    if (a.this.h != null) {
                        a aVar = a.this;
                        aVar.setIndoorBuildingInfo(aVar.h);
                    }
                } catch (Throwable th) {
                    th.printStackTrace();
                }
                efVarD.a(false);
            }
        });
    }

    void g(boolean z) {
        this.r.obtainMessage(17, z ? 1 : 0, 0).sendToTarget();
    }

    public void l(final int i) {
        queueEvent(new Runnable() { // from class: com.amap.api.mapcore.util.a.15
            @Override // java.lang.Runnable
            public void run() {
                if (!a.this.aT || a.this.l == null) {
                    return;
                }
                a.this.l.setParamater(i, GLMapStaticValue.AM_PARAMETERNAME_CLEAN_SELECTED_SUBWAY, 0, 0, 0, 0);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Poi b(int i, int i2, int i3) {
        if (!this.aT) {
            return null;
        }
        try {
            ArrayList<MapLabelItem> arrayListA = a(1, i, i2, i3);
            MapLabelItem mapLabelItem = (arrayListA == null || arrayListA.size() <= 0) ? null : arrayListA.get(0);
            if (mapLabelItem != null) {
                DPoint dPointPixelsToLatLong = VirtualEarthProjection.PixelsToLatLong(mapLabelItem.pixel20X, mapLabelItem.pixel20Y, 20);
                Poi poi = new Poi(mapLabelItem.name, new LatLng(dPointPixelsToLatLong.y, dPointPixelsToLatLong.x, false), mapLabelItem.poiid);
                dPointPixelsToLatLong.recycle();
                return poi;
            }
        } catch (Throwable unused) {
        }
        return null;
    }

    public ArrayList<MapLabelItem> a(int i, int i2, int i3, int i4) {
        if (!this.aT) {
            return null;
        }
        ArrayList<MapLabelItem> arrayList = new ArrayList<>();
        byte[] labelBuffer = this.l.getLabelBuffer(i, i2, i3, i4);
        if (labelBuffer == null) {
            return null;
        }
        int i5 = GLConvertUtil.getInt(labelBuffer, 0) >= 1 ? 1 : 0;
        int i6 = 0;
        int i7 = 4;
        while (i6 < i5) {
            MapLabelItem mapLabelItem = new MapLabelItem();
            int i8 = GLConvertUtil.getInt(labelBuffer, i7);
            int i9 = i7 + 4;
            int i10 = GLConvertUtil.getInt(labelBuffer, i9);
            int i11 = i9 + 4;
            mapLabelItem.x = i8;
            mapLabelItem.y = this.ad.getHeight() - i10;
            mapLabelItem.pixel20X = GLConvertUtil.getInt(labelBuffer, i11);
            int i12 = i11 + 4;
            mapLabelItem.pixel20Y = GLConvertUtil.getInt(labelBuffer, i12);
            int i13 = i12 + 4;
            mapLabelItem.type = GLConvertUtil.getInt(labelBuffer, i13);
            int i14 = i13 + 4;
            mapLabelItem.mSublayerId = GLConvertUtil.getInt(labelBuffer, i14);
            int i15 = i14 + 4;
            mapLabelItem.mIsFouces = labelBuffer[i15] != 0;
            int i16 = i15 + 1;
            if (labelBuffer[i16] == 0) {
                mapLabelItem.poiid = null;
            } else {
                String str = "";
                for (int i17 = 0; i17 < 20; i17++) {
                    int i18 = i17 + i16;
                    if (labelBuffer[i18] == 0) {
                        break;
                    }
                    str = str + ((char) labelBuffer[i18]);
                }
                mapLabelItem.poiid = str;
            }
            int i19 = i16 + 20;
            int i20 = i19 + 1;
            byte b2 = labelBuffer[i19];
            StringBuffer stringBuffer = new StringBuffer();
            for (int i21 = 0; i21 < b2; i21++) {
                stringBuffer.append((char) GLConvertUtil.getShort(labelBuffer, i20));
                i20 += 2;
            }
            mapLabelItem.name = stringBuffer.toString();
            arrayList.add(mapLabelItem);
            i6++;
            i7 = i20;
        }
        return arrayList;
    }

    @Override // com.amap.api.mapcore.util.k
    public float m(int i) {
        GLMapEngine gLMapEngine;
        if (!this.aT || (gLMapEngine = this.l) == null) {
            return 0.0f;
        }
        return gLMapEngine.getMapAngle(i);
    }

    @Override // com.amap.api.mapcore.util.k
    public Point n(int i) {
        GLMapEngine gLMapEngine;
        if (!this.aT || (gLMapEngine = this.l) == null) {
            return null;
        }
        return gLMapEngine.getMapCenter(i);
    }

    @Override // com.amap.api.mapcore.util.k
    public float o(int i) {
        GLMapEngine gLMapEngine;
        if (!this.aT || (gLMapEngine = this.l) == null) {
            return 0.0f;
        }
        return gLMapEngine.getCameraDegree(i);
    }

    @Override // com.amap.api.mapcore.util.k
    public void a(int i, GestureMapMessage gestureMapMessage) {
        if (!this.aT || this.l == null) {
            return;
        }
        try {
            gestureMapMessage.isUseAnchor = this.al;
            gestureMapMessage.anchorX = this.g.getAnchorX();
            gestureMapMessage.anchorY = this.g.getAnchorY();
            this.l.addGestureMessage(i, gestureMapMessage, this.ab.isGestureScaleByMapCenter(), this.g.getAnchorX(), this.g.getAnchorY());
        } catch (RemoteException unused) {
        }
    }

    public void p(int i) {
        GLMapRender gLMapRender = this.aJ;
        if (gLMapRender != null) {
            gLMapRender.renderPause();
        }
        r(i);
        NetworkState.getInstance().setNetWorkChangeListener(null);
    }

    public void q(int i) {
        r(i);
        GLMapRender gLMapRender = this.aJ;
        if (gLMapRender != null) {
            gLMapRender.renderResume();
        }
        NetworkState.getInstance().setNetWorkChangeListener(this);
    }

    @Override // com.autonavi.ae.gmap.callback.GLMapCoreCallback
    public void resetRenderTimeLong() {
        GLMapRender gLMapRender = this.aJ;
        if (gLMapRender != null) {
            gLMapRender.resetTickCount(6);
        }
    }

    @Override // com.autonavi.ae.gmap.callback.GLMapCoreCallback
    public void resetRenderTimeLongLong() {
        GLMapRender gLMapRender = this.aJ;
        if (gLMapRender != null) {
            gLMapRender.resetTickCount(30);
        }
    }

    public void p() {
        GLMapRender gLMapRender = this.aJ;
        if (gLMapRender != null) {
            gLMapRender.resetTickCount(2);
        }
    }

    public void q() {
        GLMapRender gLMapRender = this.aJ;
        if (gLMapRender != null) {
            gLMapRender.resetTickCount(2);
        }
    }

    @Override // com.autonavi.ae.gmap.callback.GLMapCoreCallback
    public void resetRenderTime(boolean z) {
        GLMapRender gLMapRender = this.aJ;
        if (gLMapRender != null) {
            if (z) {
                gLMapRender.resetTickCount(10);
            } else {
                gLMapRender.resetTickCount(2);
            }
        }
    }

    public void r() {
        GLMapRender gLMapRender;
        if (!this.aT || (gLMapRender = this.aJ) == null || gLMapRender.isRenderPause()) {
            return;
        }
        requestRender();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void requestRender() {
        GLMapRender gLMapRender = this.aJ;
        if (gLMapRender == null || gLMapRender.isRenderPause()) {
            return;
        }
        this.ad.requestRender();
    }

    public void b(int i, boolean z) {
        if (z) {
            this.l.setParamater(i, 1024, 1, 0, 0, 0);
        } else {
            this.l.setParamater(i, 1024, 0, 0, 0, 0);
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public int getRenderMode() {
        return this.ad.getRenderMode();
    }

    private void B() {
        if (aB) {
            return;
        }
        try {
            this.aA.setName("AuthThread");
            this.aA.start();
            aB = true;
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    @Override // com.amap.api.mapcore.util.k
    public float s() {
        return this.aM;
    }

    public synchronized void b(int i, int i2, int i3, int i4) {
        a(i, i2, i3, i4, false, false, (StyleItem[]) null);
    }

    public synchronized void a(final int i, final int i2, final int i3, final int i4, final boolean z, final boolean z2, final StyleItem[] styleItemArr) {
        this.l.setParamater(i, GLMapStaticValue.AM_PARAMETERNAME_RESTORED_MAPMODESTATE, i2, i3, i4, 0);
        if (!this.aT) {
            this.l.setParamater(i, GLMapStaticValue.AM_PARAMETERNAME_MAPMODESTATE, i2, i3, i4, 0);
        } else if (this.aU && this.aT && this.b) {
            queueEvent(new Runnable() { // from class: com.amap.api.mapcore.util.a.16
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        if (!z) {
                            int[] mapModeState = a.this.l.getMapModeState(i, false);
                            if (mapModeState == null) {
                                return;
                            }
                            if (mapModeState[0] == i2 && mapModeState[1] == i3 && mapModeState[2] == i4) {
                                return;
                            }
                        }
                        a.this.l.SetMapModeAndStyle(i, i2, i3, i4, z, z2, styleItemArr);
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }
            });
        } else {
            this.bg.g = i;
            this.bg.d = i2;
            this.bg.e = i3;
            this.bg.f = i4;
            this.bg.b = true;
        }
    }

    public void r(final int i) {
        queueEvent(new Runnable() { // from class: com.amap.api.mapcore.util.a.18
            @Override // java.lang.Runnable
            public void run() {
                try {
                    a.this.l.clearAllMessages(i);
                    a.this.l.clearAnimations(i, true);
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        });
    }

    @Override // com.autonavi.ae.gmap.callback.GLMapCoreCallback
    public void postQueueEvent(Runnable runnable) {
        queueEvent(runnable);
    }

    @Override // com.autonavi.ae.gmap.callback.GLMapCoreCallback
    public void postOnUIThread(Runnable runnable) {
        a(runnable);
    }

    public void c(final int i, final boolean z) {
        b(i, 18, z);
        if (this.aT && this.aU) {
            p();
            queueEvent(new Runnable() { // from class: com.amap.api.mapcore.util.a.19
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        if (z) {
                            a.this.l.setParamater(i, 1021, 1, 0, 0, 0);
                            a.this.l.setParamater(i, GLMapStaticValue.AM_PARAMETERNAME_PROCESS_3DOBJECT, 1, 0, 0, 0);
                        } else {
                            a.this.l.setParamater(i, 1021, 0, 0, 0, 0);
                            a.this.l.setParamater(i, GLMapStaticValue.AM_PARAMETERNAME_PROCESS_3DOBJECT, 0, 0, 0, 0);
                        }
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }
            });
        } else {
            this.bi.c = z;
            this.bi.b = true;
            this.bi.g = i;
        }
    }

    public void d(final int i, final boolean z) {
        if (this.aT && this.aU) {
            p();
            queueEvent(new Runnable() { // from class: com.amap.api.mapcore.util.a.20
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        if (z) {
                            a.this.l.setParamater(i, GLMapStaticValue.AM_PARAMETERNAME_SHOW_BUILDING_NORMAL, 1, 0, 0, 0);
                        } else {
                            a.this.l.setParamater(i, GLMapStaticValue.AM_PARAMETERNAME_SHOW_BUILDING_NORMAL, 0, 0, 0, 0);
                        }
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }
            });
        } else {
            this.bj.c = z;
            this.bj.b = true;
            this.bj.g = i;
        }
    }

    public void e(final int i, final boolean z) {
        if (this.aT && this.aU) {
            p();
            queueEvent(new Runnable() { // from class: com.amap.api.mapcore.util.a.21
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        if (z) {
                            a.this.l.setParamater(i, GLMapStaticValue.AM_PARAMETERNAME_SHOW_BUILDING_MARK, 1, 0, 0, 0);
                        } else {
                            a.this.l.setParamater(i, GLMapStaticValue.AM_PARAMETERNAME_SHOW_BUILDING_MARK, 0, 0, 0, 0);
                        }
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }
            });
        } else {
            this.bk.c = z;
            this.bk.b = true;
            this.bk.g = i;
        }
    }

    public void f(final int i, final boolean z) {
        if (this.aT && this.aU) {
            p();
            queueEvent(new Runnable() { // from class: com.amap.api.mapcore.util.a.22
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        if (z) {
                            a.this.l.setParamater(i, GLMapStaticValue.AM_PARAMETERNAME_SHOW_BUILDING_POI, 1, 0, 0, 0);
                        } else {
                            a.this.l.setParamater(i, GLMapStaticValue.AM_PARAMETERNAME_SHOW_BUILDING_POI, 0, 0, 0, 0);
                        }
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }
            });
        } else {
            this.bl.c = z;
            this.bl.b = true;
            this.bl.g = i;
        }
    }

    public void g(final int i, final boolean z) {
        b(i, 12, z);
        if (this.aT && this.aU) {
            p();
            queueEvent(new Runnable() { // from class: com.amap.api.mapcore.util.a.23
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        if (z) {
                            a.this.l.setParamater(i, GLMapStaticValue.AM_PARAMETERNAME_SHOW_BUILDING_TEXTURE, 1, 0, 0, 0);
                        } else {
                            a.this.l.setParamater(i, GLMapStaticValue.AM_PARAMETERNAME_SHOW_BUILDING_TEXTURE, 0, 0, 0, 0);
                        }
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }
            });
        } else {
            this.bo.c = z;
            this.bo.b = true;
            this.bo.g = i;
        }
    }

    public synchronized void a(final int i, GL10 gl10, EGLConfig eGLConfig) {
        this.aS = gl10;
        this.aU = false;
        this.n = this.ad.getWidth();
        this.o = this.ad.getHeight();
        this.aX = false;
        this.l.initConnectionManager();
        v(i);
        a(new Runnable() { // from class: com.amap.api.mapcore.util.a.24
            @Override // java.lang.Runnable
            public void run() {
                if (a.this.ai != null) {
                    a.this.ai.onSurfaceCreated(i);
                }
            }
        });
        this.aT = true;
        try {
            this.u = gl10.glGetString(7937);
        } catch (Exception unused) {
        }
        GLMapState mapState = this.l.getMapState(1);
        if (mapState != null && mapState.getNativeInstance() != 0) {
            mapState.setMapGeoCenter(this.g.getS_x(), this.g.getS_y());
            mapState.setMapAngle(this.g.getS_r());
            mapState.setMapZoomer(this.g.getS_z());
            mapState.setCameraDegree(this.g.getS_c());
            this.l.setMapState(1, mapState, false);
        }
        E();
        B();
        CustomRenderer customRenderer = this.i;
        if (customRenderer != null) {
            customRenderer.onSurfaceCreated(gl10, eGLConfig);
        }
        t();
    }

    protected void t() {
        AMapNativeRenderer.nativeDrawLineInit();
    }

    /* JADX WARN: Code duplicated, block: B:33:0x0094  */
    /* JADX WARN: Code duplicated, block: B:36:0x00b2  */
    /* JADX WARN: Code duplicated, block: B:37:0x00c1  */
    /* JADX WARN: Code duplicated, block: B:40:0x00d6  */
    /* JADX WARN: Code duplicated, block: B:41:0x00eb  */
    /* JADX WARN: Code duplicated, block: B:44:0x0124  */
    /* JADX WARN: Code duplicated, block: B:45:0x0132  */
    public void a(int i, GL10 gl10, int i2, int i3) {
        int i4;
        int i5;
        float f;
        MapConfig mapConfig;
        this.aX = false;
        if (!this.aT) {
            a(i, gl10, (EGLConfig) null);
        }
        this.n = i2;
        this.o = i3;
        this.ax = new Rect(0, 0, i2, i3);
        if (!this.aU) {
            int i6 = this.k.getResources().getDisplayMetrics().densityDpi;
            float f2 = this.k.getResources().getDisplayMetrics().density;
            if (i6 <= 120) {
                i5 = 100;
            } else {
                if (i6 <= 160) {
                    i5 = Math.max(i2, i3) > 480 ? 100 : 120;
                } else if (i6 <= 240) {
                    i5 = Math.min(i2, i3) >= 1000 ? 60 : 70;
                    i4 = 2;
                } else if (i6 > 320 && i6 > 480) {
                    i4 = 4;
                    i5 = 40;
                } else {
                    i4 = 3;
                    i5 = 50;
                }
                int i7 = (int) (f2 * 100.0f);
                this.l.setParamater(i, GLMapStaticValue.AM_PARAMETERNAME_MAP_VALUE, i5, i7, i7, i4);
                f = i5 / 100.0f;
                this.aM = f;
                mapConfig = this.g;
                if (mapConfig != null) {
                    mapConfig.setMapZoomScale(f);
                    this.g.setMapWidth(i2);
                    this.g.setMapHeight(i3);
                }
                this.l.setParamater(i, 1001, 1, 0, 0, 0);
                if (this.aN) {
                    this.l.setParamater(i, GLMapStaticValue.AM_PARAMETERNAME_FORCE_NAVI_LABEL, 1, 15, 0, 0);
                } else {
                    this.l.setParamater(i, GLMapStaticValue.AM_PARAMETERNAME_FORCE_NAVI_LABEL, 0, 0, 0, 0);
                }
                if (c(i, 18)) {
                    this.l.setParamater(i, 1021, 1, 0, 0, 0);
                    this.l.setParamater(i, GLMapStaticValue.AM_PARAMETERNAME_PROCESS_3DOBJECT, 1, 0, 0, 0);
                } else {
                    this.l.setParamater(i, 1021, 0, 0, 0, 0);
                    this.l.setParamater(i, GLMapStaticValue.AM_PARAMETERNAME_PROCESS_3DOBJECT, 0, 0, 0, 0);
                }
                this.l.setParamater(i, GLMapStaticValue.AM_PARAMETERNAME_PROCESS_ROADARROW, 1, 0, 0, 0);
                this.l.setParamater(1, GLMapStaticValue.AM_PARAMETERNAME_PROCESS_INDOOR, 0, 0, 0, 0);
                if (c(i, 16)) {
                    this.l.setParamater(i, GLMapStaticValue.AM_PARAMETERNAME_TRAFFIC, 1, 0, 0, 0);
                } else {
                    this.l.setParamater(i, GLMapStaticValue.AM_PARAMETERNAME_TRAFFIC, 0, 0, 0, 0);
                }
                this.l.setParamater(i, GLMapStaticValue.AM_PARAMETERNAME_SCENIC_WIDGET_ICON_CONTER, 17, 0, 0, 0);
                h(i, false);
            }
            i4 = 1;
            int i8 = (int) (f2 * 100.0f);
            this.l.setParamater(i, GLMapStaticValue.AM_PARAMETERNAME_MAP_VALUE, i5, i8, i8, i4);
            f = i5 / 100.0f;
            this.aM = f;
            mapConfig = this.g;
            if (mapConfig != null) {
                mapConfig.setMapZoomScale(f);
                this.g.setMapWidth(i2);
                this.g.setMapHeight(i3);
            }
            this.l.setParamater(i, 1001, 1, 0, 0, 0);
            if (this.aN) {
                this.l.setParamater(i, GLMapStaticValue.AM_PARAMETERNAME_FORCE_NAVI_LABEL, 1, 15, 0, 0);
            } else {
                this.l.setParamater(i, GLMapStaticValue.AM_PARAMETERNAME_FORCE_NAVI_LABEL, 0, 0, 0, 0);
            }
            if (c(i, 18)) {
                this.l.setParamater(i, 1021, 1, 0, 0, 0);
                this.l.setParamater(i, GLMapStaticValue.AM_PARAMETERNAME_PROCESS_3DOBJECT, 1, 0, 0, 0);
            } else {
                this.l.setParamater(i, 1021, 0, 0, 0, 0);
                this.l.setParamater(i, GLMapStaticValue.AM_PARAMETERNAME_PROCESS_3DOBJECT, 0, 0, 0, 0);
            }
            this.l.setParamater(i, GLMapStaticValue.AM_PARAMETERNAME_PROCESS_ROADARROW, 1, 0, 0, 0);
            this.l.setParamater(1, GLMapStaticValue.AM_PARAMETERNAME_PROCESS_INDOOR, 0, 0, 0, 0);
            if (c(i, 16)) {
                this.l.setParamater(i, GLMapStaticValue.AM_PARAMETERNAME_TRAFFIC, 1, 0, 0, 0);
            } else {
                this.l.setParamater(i, GLMapStaticValue.AM_PARAMETERNAME_TRAFFIC, 0, 0, 0, 0);
            }
            this.l.setParamater(i, GLMapStaticValue.AM_PARAMETERNAME_SCENIC_WIDGET_ICON_CONTER, 17, 0, 0, 0);
            h(i, false);
        }
        this.l.changeSurface(i, 0, 0, i2, i3, i2, i3);
        synchronized (this) {
            this.aU = true;
        }
        int i9 = this.ba;
        if (i9 == 0 && this.bb == 0) {
            this.g.setAnchorX(i2 >> 1);
            this.g.setAnchorY(i3 >> 1);
        } else {
            this.g.setAnchorX(Math.max(1, Math.min(i9, i2 - 1)));
            this.g.setAnchorY(Math.max(1, Math.min(this.bb, i3 - 1)));
        }
        this.l.setParamater(i, 1100, this.g.getAnchorX(), this.g.getAnchorY(), 0, 0);
        this.b = true;
        if (this.bi.b) {
            this.bi.run();
        }
        if (this.bg.b) {
            this.bg.run();
        }
        if (this.be.b) {
            this.be.run();
        }
        if (this.bh.b) {
            this.bh.run();
        }
        if (this.bd.b) {
            this.bd.run();
        }
        if (this.bj.b) {
            this.bj.run();
        }
        if (this.bk.b) {
            this.bk.run();
        }
        if (this.bl.b) {
            this.bl.run();
        }
        if (this.bo.b) {
            this.bo.run();
        }
        if (this.bm.b) {
            this.bm.run();
        }
        if (this.bf.b) {
            this.bf.run();
        }
        CustomRenderer customRenderer = this.i;
        if (customRenderer != null) {
            customRenderer.onSurfaceChanged(gl10, i2, i3);
        }
        Handler handler = this.r;
        if (handler != null) {
            handler.post(this.bn);
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void destorySurface(int i) {
        this.aY.lock();
        try {
            if (this.aT) {
                this.l.destorySurface();
                VMapDataCache.getInstance().reset();
                MapSurfaceListener mapSurfaceListener = this.ai;
                if (mapSurfaceListener != null) {
                    mapSurfaceListener.onSurfaceDestroy(i);
                }
            }
            this.aT = false;
            this.aS = null;
            this.aU = false;
            this.aX = false;
        } catch (Throwable unused) {
        }
        this.aY.unlock();
    }

    @Override // com.amap.api.mapcore.util.k
    public Context u() {
        return this.k;
    }

    private void v(int i) {
        int i2;
        int i3;
        int i4;
        int[] mapModeState = this.l.getMapModeState(i, false);
        if (mapModeState != null) {
            int i5 = mapModeState[0];
            int i6 = mapModeState[1];
            i4 = mapModeState[2];
            i2 = i5;
            i3 = i6;
        } else {
            i2 = 0;
            i3 = 0;
            i4 = 0;
        }
        this.l.loadStartResource(i, i2, i3, i4, c(i, 2), this.n, this.o);
    }

    public int a(int i, Rect rect, int i2, int i3) {
        GLMapEngine gLMapEngine = this.l;
        if (gLMapEngine == null || i < 0 || rect == null) {
            return 0;
        }
        int engineIDWithType = gLMapEngine.getEngineIDWithType(i);
        if (!this.l.isEngineCreated(engineIDWithType)) {
            this.l.createEngineWithFrame(engineIDWithType, rect, i2, i3);
            this.l.setOvelayBundle(engineIDWithType, new GLOverlayBundle<>(engineIDWithType, this));
            if (engineIDWithType == 2) {
                a(engineIDWithType, -1, false);
                v(engineIDWithType);
            }
            this.l.setMapAngle(engineIDWithType, this.g.getS_r());
            this.l.setMapZoom(engineIDWithType, this.g.getS_z());
            this.l.setMapCenter(engineIDWithType, this.g.getS_x(), this.g.getS_y());
            this.l.setCameraDegree(engineIDWithType, this.g.getS_c());
        } else {
            a(engineIDWithType, rect.left, rect.top, rect.width(), rect.height(), i2, i3);
        }
        return engineIDWithType;
    }

    public void a(final int i, final int i2, final boolean z) {
        queueEvent(new Runnable() { // from class: com.amap.api.mapcore.util.a.26
            /* JADX WARN: Type inference fix 'apply assigned field type' failed
            java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
            	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
            	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
            	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
             */
            @Override // java.lang.Runnable
            public void run() {
                if (a.this.l != null) {
                    a.this.l.setParamater(i, GLMapStaticValue.AM_PARAMETERNAME_SHOW_CONTENT, i2, z ? 1 : 0, 0, 0);
                }
            }
        });
    }

    @Override // com.amap.api.mapcore.util.k
    public int a(EAMapPlatformGestureInfo eAMapPlatformGestureInfo) {
        GLMapEngine gLMapEngine = this.l;
        if (gLMapEngine != null) {
            return gLMapEngine.getEngineIDWithGestureInfo(eAMapPlatformGestureInfo);
        }
        return 1;
    }

    public void a(final int i, final int i2, final int i3, final int i4, final int i5, final int i6, final int i7) {
        queueEvent(new Runnable() { // from class: com.amap.api.mapcore.util.a.27
            @Override // java.lang.Runnable
            public void run() {
                if (a.this.l != null) {
                    a.this.l.setServiceViewRect(i, i2, i3, i4, i5, i6, i7);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(int i, int i2, boolean z) {
        GLMapEngine gLMapEngine = this.l;
        if (gLMapEngine != null) {
            gLMapEngine.setSrvViewStateBoolValue(i, i2, z);
        }
    }

    private boolean c(int i, int i2) {
        GLMapEngine gLMapEngine = this.l;
        if (gLMapEngine != null) {
            return gLMapEngine.getSrvViewStateBoolValue(i, i2);
        }
        return false;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public CameraPosition getCameraPosition() throws RemoteException {
        return f(this.al);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public float getMaxZoomLevel() {
        MapConfig mapConfig = this.g;
        if (mapConfig != null) {
            return mapConfig.getMaxZoomLevel();
        }
        return 19.0f;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public float getMinZoomLevel() {
        MapConfig mapConfig = this.g;
        if (mapConfig != null) {
            return mapConfig.getMinZoomLevel();
        }
        return 3.0f;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void moveCamera(CameraUpdate cameraUpdate) throws RemoteException {
        if (cameraUpdate == null) {
            return;
        }
        a(cameraUpdate.getCameraUpdateFactoryDelegate());
    }

    @Override // com.amap.api.mapcore.util.k
    public void a(CameraUpdateMessage cameraUpdateMessage) throws RemoteException {
        GLMapEngine gLMapEngine = this.l;
        if (gLMapEngine == null || this.ag) {
            return;
        }
        if (this.ak && gLMapEngine.getStateMessageCount() > 0) {
            CameraUpdateMessage cameraUpdateMessageC = z.c();
            cameraUpdateMessageC.nowType = CameraUpdateMessage.Type.changeGeoCenterZoomTiltBearing;
            cameraUpdateMessageC.geoPoint = new Point(this.g.getS_x(), this.g.getS_y());
            cameraUpdateMessageC.zoom = this.g.getS_z();
            cameraUpdateMessageC.bearing = this.g.getS_r();
            cameraUpdateMessageC.tilt = this.g.getS_c();
            this.l.addMessage(cameraUpdateMessage);
            while (this.l.getStateMessageCount() > 0) {
                if (this.l.getStateMessage() != null) {
                    cameraUpdateMessage.mergeCameraUpdateDelegate(cameraUpdateMessageC);
                }
            }
            cameraUpdateMessage = cameraUpdateMessageC;
        }
        p();
        this.l.clearAnimations(1, false);
        cameraUpdateMessage.isChangeFinished = true;
        c(cameraUpdateMessage);
        this.l.addMessage(cameraUpdateMessage);
    }

    private void c(CameraUpdateMessage cameraUpdateMessage) {
        cameraUpdateMessage.isUseAnchor = this.al;
        if (this.al) {
            cameraUpdateMessage.anchorX = this.g.getAnchorX();
            cameraUpdateMessage.anchorY = this.g.getAnchorY();
        }
        if (cameraUpdateMessage.width == 0) {
            cameraUpdateMessage.width = getMapWidth();
        }
        if (cameraUpdateMessage.height == 0) {
            cameraUpdateMessage.height = getMapHeight();
        }
        cameraUpdateMessage.mapConfig = this.g;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void animateCamera(CameraUpdate cameraUpdate) throws RemoteException {
        b(cameraUpdate.getCameraUpdateFactoryDelegate());
    }

    @Override // com.amap.api.mapcore.util.k
    public void b(CameraUpdateMessage cameraUpdateMessage) throws RemoteException {
        a(cameraUpdateMessage, 250L, (AMap.CancelableCallback) null);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void animateCameraWithCallback(CameraUpdate cameraUpdate, AMap.CancelableCallback cancelableCallback) throws RemoteException {
        animateCameraWithDurationAndCallback(cameraUpdate, 250L, cancelableCallback);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void animateCameraWithDurationAndCallback(CameraUpdate cameraUpdate, long j, AMap.CancelableCallback cancelableCallback) {
        a(cameraUpdate.getCameraUpdateFactoryDelegate(), j, cancelableCallback);
    }

    /* JADX WARN: Code duplicated, block: B:45:0x0050 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    public void a(CameraUpdateMessage cameraUpdateMessage, long j, final AMap.CancelableCallback cancelableCallback) {
        if (cameraUpdateMessage == null || this.ag) {
            return;
        }
        if (this.ak || getMapHeight() == 0 || getMapWidth() == 0) {
            try {
                a(cameraUpdateMessage);
                if (cancelableCallback != null) {
                    cancelableCallback.onFinish();
                    return;
                }
                return;
            } catch (Throwable th) {
                th.printStackTrace();
                return;
            }
        }
        synchronized (this.W) {
            if (this.l.isInMapAnimation(1) && this.aG.get() > 0) {
                try {
                    AMap.CancelableCallback cancelableCallbackRemove = this.aH.remove(Integer.valueOf(this.aG.decrementAndGet()));
                    if (cancelableCallbackRemove != null) {
                        cancelableCallbackRemove.onCancel();
                        p();
                        c(cameraUpdateMessage);
                        this.l.clearAnimations(1, false);
                        cameraUpdateMessage.generateMapAnimation(this.l, (int) j);
                        queueEvent(new Runnable() { // from class: com.amap.api.mapcore.util.a.28
                            @Override // java.lang.Runnable
                            public void run() {
                                int iIncrementAndGet = a.this.aG.incrementAndGet();
                                if (cancelableCallback != null) {
                                    a.this.aH.put(Integer.valueOf(iIncrementAndGet - 1), cancelableCallback);
                                }
                            }
                        });
                    } else {
                        p();
                        c(cameraUpdateMessage);
                        this.l.clearAnimations(1, false);
                        cameraUpdateMessage.generateMapAnimation(this.l, (int) j);
                        queueEvent(new Runnable() { // from class: com.amap.api.mapcore.util.a.28
                            @Override // java.lang.Runnable
                            public void run() {
                                int iIncrementAndGet = a.this.aG.incrementAndGet();
                                if (cancelableCallback != null) {
                                    a.this.aH.put(Integer.valueOf(iIncrementAndGet - 1), cancelableCallback);
                                }
                            }
                        });
                    }
                } catch (Throwable th2) {
                    fn.b(th2, "AMapDelegateImp", "CancelableCallback.onCancel");
                    th2.printStackTrace();
                }
            } else {
                try {
                    p();
                    c(cameraUpdateMessage);
                    this.l.clearAnimations(1, false);
                    cameraUpdateMessage.generateMapAnimation(this.l, (int) j);
                    queueEvent(new Runnable() { // from class: com.amap.api.mapcore.util.a.28
                        @Override // java.lang.Runnable
                        public void run() {
                            int iIncrementAndGet = a.this.aG.incrementAndGet();
                            if (cancelableCallback != null) {
                                a.this.aH.put(Integer.valueOf(iIncrementAndGet - 1), cancelableCallback);
                            }
                        }
                    });
                } catch (Throwable th3) {
                    th3.printStackTrace();
                }
            }
            throw th;
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void stopAnimation() throws RemoteException {
        GLMapEngine gLMapEngine = this.l;
        if (gLMapEngine != null && gLMapEngine.getAnimateionsCount() > 0) {
            a(true, (CameraPosition) null);
            this.l.clearAnimations(1, false);
            synchronized (this.W) {
                try {
                    if (this.aG.get() > 0) {
                        AMap.CancelableCallback cancelableCallbackRemove = this.aH.remove(Integer.valueOf(this.aG.decrementAndGet()));
                        if (cancelableCallbackRemove != null) {
                            cancelableCallbackRemove.onCancel();
                        }
                    }
                } catch (Throwable th) {
                    fn.b(th, "AMapDelegateImp", "CancelableCallback.onCancel");
                    th.printStackTrace();
                }
            }
        }
        p();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public Polyline addPolyline(PolylineOptions polylineOptions) throws RemoteException {
        p();
        ck ckVarA = this.f.a(polylineOptions);
        if (ckVarA != null) {
            return new Polyline(ckVarA);
        }
        return null;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public NavigateArrow addNavigateArrow(NavigateArrowOptions navigateArrowOptions) throws RemoteException {
        p();
        cg cgVarA = this.f.a(navigateArrowOptions);
        if (cgVarA != null) {
            return new NavigateArrow(cgVarA);
        }
        return null;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public Polygon addPolygon(PolygonOptions polygonOptions) throws RemoteException {
        p();
        cj cjVarA = this.f.a(polygonOptions);
        if (cjVarA != null) {
            return new Polygon(cjVarA);
        }
        return null;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public Circle addCircle(CircleOptions circleOptions) throws RemoteException {
        p();
        cd cdVarA = this.f.a(circleOptions);
        if (cdVarA != null) {
            return new Circle(cdVarA);
        }
        return null;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public Arc addArc(ArcOptions arcOptions) throws RemoteException {
        p();
        cc ccVarA = this.f.a(arcOptions);
        if (ccVarA != null) {
            return new Arc(ccVarA);
        }
        return null;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public GroundOverlay addGroundOverlay(GroundOverlayOptions groundOverlayOptions) throws RemoteException {
        p();
        ce ceVarA = this.f.a(groundOverlayOptions);
        if (ceVarA != null) {
            return new GroundOverlay(ceVarA);
        }
        return null;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public MultiPointOverlay addMultiPointOverlay(MultiPointOverlayOptions multiPointOverlayOptions) throws RemoteException {
        p();
        IMultiPointOverlay iMultiPointOverlayA = this.q.a(multiPointOverlayOptions);
        if (iMultiPointOverlayA != null) {
            return new MultiPointOverlay(iMultiPointOverlayA);
        }
        return null;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public Marker addMarker(MarkerOptions markerOptions) throws RemoteException {
        p();
        return this.e.a(markerOptions);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public Text addText(TextOptions textOptions) throws RemoteException {
        p();
        return this.e.a(textOptions);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public ArrayList<Marker> addMarkers(ArrayList<MarkerOptions> arrayList, boolean z) throws RemoteException {
        p();
        return this.e.a(arrayList, z);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public TileOverlay addTileOverlay(TileOverlayOptions tileOverlayOptions) throws RemoteException {
        return this.d.a(tileOverlayOptions);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void clear() throws RemoteException {
        try {
            clear(false);
        } catch (Throwable th) {
            fn.b(th, "AMapDelegateImp", "clear");
            th.printStackTrace();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void clear(boolean z) throws RemoteException {
        String strC;
        String strD;
        try {
            i();
            co coVar = this.an;
            if (coVar == null) {
                strC = null;
                strD = null;
            } else if (z) {
                strC = coVar.c();
                strD = this.an.d();
            } else {
                coVar.e();
                strC = null;
                strD = null;
            }
            this.f.b(strD);
            this.d.c();
            this.e.a(strC);
            ej ejVar = this.c;
            if (ejVar != null) {
                ejVar.k();
            }
            aj ajVar = this.q;
            if (ajVar != null) {
                ajVar.c();
            }
            p();
        } catch (Throwable th) {
            fn.b(th, "AMapDelegateImp", "clear");
            th.printStackTrace();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public int getMapType() throws RemoteException {
        return this.ay;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setMapType(int i) throws RemoteException {
        int i2;
        int i3;
        int i4;
        this.ay = i;
        if (i == 1) {
            i3 = 0;
            i2 = 0;
            i4 = i2;
        } else if (i == 2) {
            i3 = 1;
            i2 = 0;
            i4 = i2;
        } else {
            int i5 = 4;
            if (i == 3) {
                i2 = 1;
                i3 = 0;
            } else {
                if (i != 4) {
                    i5 = 5;
                    if (i == 5) {
                        i2 = 0;
                        i3 = 2;
                    } else {
                        try {
                            this.ay = 1;
                            i3 = 0;
                            i2 = 0;
                            i4 = i2;
                        } catch (Throwable th) {
                            fn.b(th, "AMapDelegateImp", "setMaptype");
                            th.printStackTrace();
                            return;
                        }
                    }
                    fn.b(th, "AMapDelegateImp", "setMaptype");
                    th.printStackTrace();
                    return;
                }
                i3 = 0;
                i2 = 0;
            }
            i4 = i5;
        }
        this.g.setMapStyleMode(i3);
        this.g.setMapStyleTime(i2);
        this.g.setMapStyleState(i4);
        if (this.g.isCustomStyleEnable()) {
            a(1, i3, i2, i4, true, false, (StyleItem[]) null);
            this.g.setCustomStyleEnable(false);
            this.ab.setLogoEnable(true);
        } else {
            b(1, i3, i2, i4);
        }
        p();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public boolean isTrafficEnabled() throws RemoteException {
        return this.g.isTrafficEnabled();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setTrafficEnabled(final boolean z) throws RemoteException {
        if (this.aT && !this.ag) {
            queueEvent(new Runnable() { // from class: com.amap.api.mapcore.util.a.29
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        if (a.this.g.isTrafficEnabled() != z) {
                            a.this.g.setTrafficEnabled(z);
                            a.this.aJ.setTrafficMode(z);
                            int i = z ? 1 : 0;
                            a.this.l.setParamater(1, 2203, 8, 10, 0, 0);
                            if (a.this.az != null) {
                                try {
                                    a aVar = a.this;
                                    aVar.setMyTrafficStyle(aVar.az);
                                } catch (Throwable th) {
                                    th.printStackTrace();
                                }
                            }
                            a.this.l.setParamater(1, GLMapStaticValue.AM_PARAMETERNAME_TRAFFIC, i, 0, 0, 0);
                            a.this.p();
                        }
                    } catch (Throwable th2) {
                        th2.printStackTrace();
                    }
                }
            });
            return;
        }
        this.bd.c = z;
        this.bd.b = true;
        this.bd.g = 1;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public boolean isIndoorEnabled() throws RemoteException {
        return this.g.isIndoorEnable();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setIndoorEnabled(final boolean z) throws RemoteException {
        if (this.aT && !this.ag) {
            this.g.setIndoorEnable(z);
            p();
            if (z) {
                this.l.setParamater(1, GLMapStaticValue.AM_PARAMETERNAME_PROCESS_INDOOR, 1, 0, 0, 0);
            } else {
                this.l.setParamater(1, GLMapStaticValue.AM_PARAMETERNAME_PROCESS_INDOOR, 0, 0, 0, 0);
                MapConfig mapConfig = this.g;
                mapConfig.maxZoomLevel = mapConfig.isSetLimitZoomLevel() ? this.g.getMaxZoomLevel() : 19.0f;
                if (this.ab.isZoomControlsEnabled()) {
                    this.aj.invalidateZoomController(this.g.getS_z());
                }
            }
            if (this.ab.isIndoorSwitchEnabled()) {
                this.r.post(new Runnable() { // from class: com.amap.api.mapcore.util.a.30
                    @Override // java.lang.Runnable
                    public void run() {
                        if (z) {
                            a.this.b(true);
                        } else {
                            if (a.this.c == null || a.this.c.e() == null) {
                                return;
                            }
                            a.this.c.e().a(false);
                        }
                    }
                });
                return;
            }
            return;
        }
        this.bm.c = z;
        this.bm.b = true;
        this.bm.g = 1;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void set3DBuildingEnabled(boolean z) throws RemoteException {
        p(1);
        d(1, z);
        q(1);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public boolean isMyLocationEnabled() throws RemoteException {
        return this.ae;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setMyLocationEnabled(boolean z) throws RemoteException {
        if (this.ag) {
            return;
        }
        try {
            ej ejVar = this.c;
            if (ejVar != null) {
                ei eiVarF = ejVar.f();
                LocationSource locationSource = this.ao;
                if (locationSource == null) {
                    eiVarF.a(false);
                } else if (z) {
                    locationSource.activate(this.T);
                    eiVarF.a(true);
                    if (this.an == null) {
                        this.an = new co(this, this.k);
                    }
                } else {
                    co coVar = this.an;
                    if (coVar != null) {
                        coVar.b();
                        this.an = null;
                    }
                    this.ao.deactivate();
                }
            }
            if (!z) {
                this.ab.setMyLocationButtonEnabled(z);
            }
            this.ae = z;
            p();
        } catch (Throwable th) {
            fn.b(th, "AMapDelegateImp", "setMyLocationEnabled");
            th.printStackTrace();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setLoadOfflineData(final boolean z) throws RemoteException {
        queueEvent(new Runnable() { // from class: com.amap.api.mapcore.util.a.31
            /* JADX WARN: Type inference fix 'apply assigned field type' failed
            java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
            	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
            	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
            	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
             */
            @Override // java.lang.Runnable
            public void run() {
                if (a.this.l != null) {
                    a.this.l.setParamater(1, GLMapStaticValue.AM_PARAMETERNAME_ON_OFF_DBLITE, z ? 1 : 0, 0, 0, 0);
                }
            }
        });
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setMyLocationStyle(MyLocationStyle myLocationStyle) throws RemoteException {
        if (this.ag) {
            return;
        }
        if (this.an == null) {
            this.an = new co(this, this.k);
        }
        if (this.an != null) {
            if (myLocationStyle.getInterval() < 1000) {
                myLocationStyle.interval(1000L);
            }
            LocationSource locationSource = this.ao;
            if (locationSource != null && (locationSource instanceof af)) {
                ((af) locationSource).a(myLocationStyle.getInterval());
                ((af) this.ao).a(myLocationStyle.getMyLocationType());
            }
            this.an.a(myLocationStyle);
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setMyLocationType(int i) throws RemoteException {
        co coVar = this.an;
        if (coVar == null || coVar.a() == null) {
            return;
        }
        this.an.a().myLocationType(i);
        setMyLocationStyle(this.an.a());
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public List<Marker> getMapScreenMarkers() throws RemoteException {
        if (!dw.b(getMapWidth(), getMapHeight())) {
            return new ArrayList();
        }
        return this.e.e();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setMapTextEnable(boolean z) throws RemoteException {
        p(1);
        b(1, z);
        q(1);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setMyTrafficStyle(MyTrafficStyle myTrafficStyle) throws RemoteException {
        if (this.ag) {
            return;
        }
        this.az = myTrafficStyle;
        if (!this.aT || myTrafficStyle == null) {
            return;
        }
        this.l.setParamater(1, 2201, 1, 1, 1, 1);
        this.l.setParamater(1, 2202, myTrafficStyle.getSmoothColor(), myTrafficStyle.getSlowColor(), myTrafficStyle.getCongestedColor(), myTrafficStyle.getSeriousCongestedColor());
        this.l.setParamater(1, 2203, (int) (myTrafficStyle.getRatio() * 10.0f), 10, 0, 0);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public Location getMyLocation() throws RemoteException {
        if (this.ao != null) {
            return this.T.a;
        }
        return null;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setLocationSource(LocationSource locationSource) throws RemoteException {
        try {
            if (this.ag) {
                return;
            }
            LocationSource locationSource2 = this.ao;
            if (locationSource2 != null && (locationSource2 instanceof af)) {
                locationSource2.deactivate();
            }
            this.ao = locationSource;
            if (locationSource != null) {
                this.c.f().a(true);
            } else {
                this.c.f().a(false);
            }
        } catch (Throwable th) {
            fn.b(th, "AMapDelegateImp", "setLocationSource");
            th.printStackTrace();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setMyLocationRotateAngle(float f) throws RemoteException {
        co coVar = this.an;
        if (coVar != null) {
            coVar.a(f);
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public UiSettings getAMapUiSettings() throws RemoteException {
        if (this.Z == null) {
            this.Z = new UiSettings(this.ab);
        }
        return this.Z;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public Projection getAMapProjection() throws RemoteException {
        return new Projection(this.aa);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setOnMapClickListener(AMap.OnMapClickListener onMapClickListener) throws RemoteException {
        this.M = onMapClickListener;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setOnMapTouchListener(AMap.OnMapTouchListener onMapTouchListener) throws RemoteException {
        this.N = onMapTouchListener;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setOnPOIClickListener(AMap.OnPOIClickListener onPOIClickListener) throws RemoteException {
        this.O = onPOIClickListener;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setOnMapLongClickListener(AMap.OnMapLongClickListener onMapLongClickListener) throws RemoteException {
        this.P = onMapLongClickListener;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setOnMarkerClickListener(AMap.OnMarkerClickListener onMarkerClickListener) throws RemoteException {
        this.H = onMarkerClickListener;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setOnPolylineClickListener(AMap.OnPolylineClickListener onPolylineClickListener) throws RemoteException {
        this.I = onPolylineClickListener;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setOnMarkerDragListener(AMap.OnMarkerDragListener onMarkerDragListener) throws RemoteException {
        this.J = onMarkerDragListener;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setOnMaploadedListener(AMap.OnMapLoadedListener onMapLoadedListener) throws RemoteException {
        this.K = onMapLoadedListener;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setOnCameraChangeListener(AMap.OnCameraChangeListener onCameraChangeListener) throws RemoteException {
        this.L = onCameraChangeListener;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setOnInfoWindowClickListener(AMap.OnInfoWindowClickListener onInfoWindowClickListener) throws RemoteException {
        this.Q = onInfoWindowClickListener;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setOnIndoorBuildingActiveListener(AMap.OnIndoorBuildingActiveListener onIndoorBuildingActiveListener) throws RemoteException {
        this.R = onIndoorBuildingActiveListener;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setOnMyLocationChangeListener(AMap.OnMyLocationChangeListener onMyLocationChangeListener) {
        this.S = onMyLocationChangeListener;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setInfoWindowAdapter(AMap.InfoWindowAdapter infoWindowAdapter) throws RemoteException {
        ae aeVar;
        if (this.ag || (aeVar = this.a) == null) {
            return;
        }
        aeVar.a(infoWindowAdapter);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setOnMultiPointClickListener(AMap.OnMultiPointClickListener onMultiPointClickListener) {
        aj ajVar = this.q;
        if (ajVar != null) {
            ajVar.a(onMultiPointClickListener);
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public String getMapContentApprovalNumber() {
        MapConfig mapConfig = this.g;
        if (mapConfig == null || mapConfig.isCustomStyleEnable()) {
            return null;
        }
        return "GS（2016）1474 | GS（2017）2550";
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void getMapPrintScreen(AMap.onMapPrintScreenListener onmapprintscreenlistener) {
        this.U = onmapprintscreenlistener;
        this.aw = true;
        p();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void getMapScreenShot(AMap.OnMapScreenShotListener onMapScreenShotListener) {
        this.V = onMapScreenShotListener;
        this.aw = true;
        p();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public float getScalePerPixel() throws RemoteException {
        try {
            return ((float) ((((Math.cos((getCameraPosition().target.latitude * 3.141592653589793d) / 180.0d) * 2.0d) * 3.141592653589793d) * 6378137.0d) / (Math.pow(2.0d, g()) * 256.0d))) * s();
        } catch (Throwable th) {
            fn.b(th, "AMapDelegateImp", "getScalePerPixel");
            th.printStackTrace();
            return 0.0f;
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setRunLowFrame(boolean z) {
        if (z) {
            return;
        }
        r();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void removecache() throws RemoteException {
        removecache(null);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void removecache(AMap.OnCacheRemoveListener onCacheRemoveListener) throws RemoteException {
        GLMapEngine gLMapEngine;
        if (this.r == null || (gLMapEngine = this.l) == null) {
            return;
        }
        try {
            gLMapEngine.setParamater(1, GLMapStaticValue.AM_PARAMETERNAME_ON_OFF_DBLITE, 0, 0, 0, 0);
            d dVar = new d(this.k, onCacheRemoveListener);
            this.r.removeCallbacks(dVar);
            this.r.post(dVar);
        } catch (Throwable th) {
            fn.b(th, "AMapDelegateImp", "removecache");
            th.printStackTrace();
        }
    }

    /* JADX INFO: compiled from: AMapDelegateImp.java */
    private class d implements Runnable {
        private Context b;
        private AMap.OnCacheRemoveListener c;

        public d(Context context, AMap.OnCacheRemoveListener onCacheRemoveListener) {
            this.b = context;
            this.c = onCacheRemoveListener;
        }

        @Override // java.lang.Runnable
        public void run() {
            boolean z;
            Throwable th;
            boolean z2 = false;
            try {
                try {
                    Context applicationContext = this.b.getApplicationContext();
                    String strB = dw.b(applicationContext);
                    String strA = dw.a(applicationContext);
                    z = FileUtil.deleteFile(new File(strB));
                    if (z) {
                        try {
                            if (FileUtil.deleteFile(new File(strA))) {
                                z2 = true;
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            try {
                                fn.b(th, "AMapDelegateImp", "RemoveCacheRunnable");
                                if (a.this.l != null) {
                                    a.this.l.setParamater(1, GLMapStaticValue.AM_PARAMETERNAME_ON_OFF_DBLITE, 1, 0, 0, 0);
                                }
                                AMap.OnCacheRemoveListener onCacheRemoveListener = this.c;
                                if (onCacheRemoveListener != null) {
                                    onCacheRemoveListener.onRemoveCacheFinish(false);
                                    return;
                                }
                                return;
                            } catch (Throwable th3) {
                                try {
                                    if (a.this.l != null) {
                                        a.this.l.setParamater(1, GLMapStaticValue.AM_PARAMETERNAME_ON_OFF_DBLITE, 1, 0, 0, 0);
                                    }
                                    AMap.OnCacheRemoveListener onCacheRemoveListener2 = this.c;
                                    if (onCacheRemoveListener2 != null) {
                                        onCacheRemoveListener2.onRemoveCacheFinish(z);
                                    }
                                } catch (Throwable th4) {
                                    th4.printStackTrace();
                                }
                                throw th3;
                            }
                        }
                    }
                    if (a.this.l != null) {
                        a.this.l.setParamater(1, GLMapStaticValue.AM_PARAMETERNAME_ON_OFF_DBLITE, 1, 0, 0, 0);
                    }
                    AMap.OnCacheRemoveListener onCacheRemoveListener3 = this.c;
                    if (onCacheRemoveListener3 != null) {
                        onCacheRemoveListener3.onRemoveCacheFinish(z2);
                    }
                } catch (Throwable th5) {
                    th5.printStackTrace();
                }
            } catch (Throwable th6) {
                z = true;
                th = th6;
            }
        }

        public boolean equals(Object obj) {
            return obj instanceof d;
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setCustomRenderer(CustomRenderer customRenderer) throws RemoteException {
        this.i = customRenderer;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setCenterToPixel(int i, int i2) throws RemoteException {
        this.al = true;
        this.ba = i;
        this.bb = i2;
        if (this.aU && this.aT) {
            if (this.g.getAnchorX() == this.ba && this.g.getAnchorY() == this.bb) {
                return;
            }
            this.g.setAnchorX(this.ba);
            this.g.setAnchorY(this.bb);
            queueEvent(new Runnable() { // from class: com.amap.api.mapcore.util.a.32
                @Override // java.lang.Runnable
                public void run() {
                    a.this.g.setAnchorX(Math.max(0, Math.min(a.this.ba, a.this.n)));
                    a.this.g.setAnchorY(Math.max(0, Math.min(a.this.bb, a.this.o)));
                    a.this.l.setParamater(1, 1100, a.this.g.getAnchorX(), a.this.g.getAnchorY(), 0, 0);
                    a.this.aC = true;
                }
            });
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setMapTextZIndex(int i) throws RemoteException {
        this.aD = i;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public int getMapTextZIndex() throws RemoteException {
        return this.aD;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setRenderFps(int i) {
        try {
            this.j = Math.max(10, Math.min(i, 40));
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setIndoorBuildingInfo(IndoorBuildingInfo indoorBuildingInfo) throws RemoteException {
        if (this.ag || indoorBuildingInfo == null || indoorBuildingInfo.activeFloorName == null || indoorBuildingInfo.poiid == null) {
            return;
        }
        this.h = (ac) indoorBuildingInfo;
        p();
        queueEvent(new Runnable() { // from class: com.amap.api.mapcore.util.a.33
            @Override // java.lang.Runnable
            public void run() {
                if (a.this.l != null) {
                    a.this.l.setIndoorBuildingToBeActive(1, a.this.h.activeFloorName, a.this.h.activeFloorIndex, a.this.h.poiid);
                }
            }
        });
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setAMapGestureListener(AMapGestureListener aMapGestureListener) {
        h hVar = this.m;
        if (hVar != null) {
            this.X = aMapGestureListener;
            hVar.a(aMapGestureListener);
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public float getZoomToSpanLevel(LatLng latLng, LatLng latLng2) {
        MapConfig mapConfig = getMapConfig();
        if (latLng != null && latLng2 != null && this.aT && !this.ag) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(latLng);
            builder.include(latLng2);
            GLMapState gLMapState = new GLMapState(1, this.l.getMapEnginePtr());
            Pair<Float, IPoint> pairA = dw.a(mapConfig, 0, 0, 0, 0, builder.build(), getMapWidth(), getMapHeight());
            gLMapState.recycle();
            if (pairA != null) {
                return ((Float) pairA.first).floatValue();
            }
            return gLMapState.getMapZoomer();
        }
        return mapConfig.getS_z();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public Pair<Float, LatLng> calculateZoomToSpanLevel(int i, int i2, int i3, int i4, LatLng latLng, LatLng latLng2) {
        if (latLng != null && latLng2 != null && i == i2 && i2 == i3 && i3 == i4 && latLng.latitude == latLng2.latitude && latLng.longitude == latLng2.longitude) {
            return new Pair<>(Float.valueOf(getMaxZoomLevel()), latLng);
        }
        MapConfig mapConfig = getMapConfig();
        if (latLng != null && latLng2 != null && this.aT && !this.ag) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(latLng);
            builder.include(latLng2);
            GLMapState gLMapState = new GLMapState(1, this.l.getMapEnginePtr());
            Pair<Float, IPoint> pairA = dw.a(mapConfig, i, i2, i3, i4, builder.build(), getMapWidth(), getMapHeight());
            gLMapState.recycle();
            if (pairA == null) {
                return null;
            }
            DPoint dPointObtain = DPoint.obtain();
            GLMapState.geo2LonLat(((IPoint) pairA.second).x, ((IPoint) pairA.second).y, dPointObtain);
            Pair<Float, LatLng> pair = new Pair<>(pairA.first, new LatLng(dPointObtain.y, dPointObtain.x));
            dPointObtain.recycle();
            return pair;
        }
        DPoint dPointObtain2 = DPoint.obtain();
        GLMapState.geo2LonLat(mapConfig.getS_x(), mapConfig.getS_y(), dPointObtain2);
        Pair<Float, LatLng> pair2 = new Pair<>(Float.valueOf(mapConfig.getS_z()), new LatLng(dPointObtain2.y, dPointObtain2.x));
        dPointObtain2.recycle();
        return pair2;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public InfoWindowAnimationManager getInfoWindowAnimationManager() {
        return new InfoWindowAnimationManager(this.Y);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setMaskLayerParams(int i, int i2, int i3, int i4, final int i5, long j) {
        GLAlphaAnimation gLAlphaAnimation;
        try {
            if (this.aE != null) {
                float f = i4 / 255.0f;
                if (i5 == -1) {
                    gLAlphaAnimation = new GLAlphaAnimation(f, 0.0f);
                    gLAlphaAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: com.amap.api.mapcore.util.a.35
                        @Override // com.amap.api.maps.model.animation.Animation.AnimationListener
                        public void onAnimationStart() {
                        }

                        @Override // com.amap.api.maps.model.animation.Animation.AnimationListener
                        public void onAnimationEnd() {
                            a.this.r.post(new Runnable() { // from class: com.amap.api.mapcore.util.a.35.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    a.this.aF = i5;
                                    if (a.this.c != null) {
                                        a.this.c.h().setVisibility(0);
                                    }
                                }
                            });
                        }
                    });
                } else {
                    this.aF = i5;
                    gLAlphaAnimation = new GLAlphaAnimation(0.0f, f);
                    if (f > 0.2d) {
                        ej ejVar = this.c;
                        if (ejVar != null) {
                            ejVar.h().setVisibility(4);
                        }
                    } else {
                        ej ejVar2 = this.c;
                        if (ejVar2 != null) {
                            ejVar2.h().setVisibility(0);
                        }
                    }
                }
                gLAlphaAnimation.setInterpolator(new LinearInterpolator());
                gLAlphaAnimation.setDuration(j);
                this.aE.a(i, i2, i3, i4);
                this.aE.a(gLAlphaAnimation);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setMaxZoomLevel(float f) {
        this.g.setMaxZoomLevel(f);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setMinZoomLevel(float f) {
        this.g.setMinZoomLevel(f);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void resetMinMaxZoomPreference() {
        this.g.resetMinMaxZoomPreference();
        try {
            if (this.ab.isZoomControlsEnabled() && this.g.isNeedUpdateZoomControllerState()) {
                this.aj.invalidateZoomController(this.g.getS_z());
            }
        } catch (RemoteException unused) {
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setMapStatusLimits(LatLngBounds latLngBounds) {
        try {
            this.g.setLimitLatLngBounds(latLngBounds);
            C();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private boolean a(LatLngBounds latLngBounds) {
        return (latLngBounds == null || latLngBounds.northeast == null || latLngBounds.southwest == null) ? false : true;
    }

    private void C() {
        try {
            LatLngBounds limitLatLngBounds = this.g.getLimitLatLngBounds();
            if (this.l != null && a(limitLatLngBounds)) {
                GLMapState gLMapState = new GLMapState(1, this.l.getMapEnginePtr());
                IPoint iPointObtain = IPoint.obtain();
                GLMapState.lonlat2Geo(limitLatLngBounds.northeast.longitude, limitLatLngBounds.northeast.latitude, iPointObtain);
                IPoint iPointObtain2 = IPoint.obtain();
                GLMapState.lonlat2Geo(limitLatLngBounds.southwest.longitude, limitLatLngBounds.southwest.latitude, iPointObtain2);
                this.g.setLimitIPoints(new IPoint[]{iPointObtain, iPointObtain2});
                gLMapState.recycle();
                return;
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        this.g.setLimitIPoints(null);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public Handler getMainHandler() {
        return this.r;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void onChangeFinish() {
        p();
        Message messageObtainMessage = this.r.obtainMessage();
        messageObtainMessage.what = 11;
        this.r.sendMessage(messageObtainMessage);
    }

    protected void a(boolean z, CameraPosition cameraPosition) {
        MapConfig mapConfig = this.g;
        if (mapConfig == null || mapConfig.getChangedCounter() == 0) {
            return;
        }
        try {
            this.g.resetChangedCounter();
            AMapGestureListener aMapGestureListener = this.X;
            if (aMapGestureListener != null) {
                aMapGestureListener.onMapStable();
            }
            if (this.L != null && this.ad.isEnabled()) {
                if (cameraPosition == null) {
                    try {
                        cameraPosition = getCameraPosition();
                    } catch (RemoteException e) {
                        fn.b(e, "AMapDelegateImp", "cameraChangeFinish");
                        e.printStackTrace();
                    }
                }
                this.L.onCameraChangeFinish(cameraPosition);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setZoomScaleParam(float f) {
        this.aM = f;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void onFling() {
        u uVar = this.d;
        if (uVar != null) {
            uVar.b(true);
        }
        this.av = true;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public int getMapWidth() {
        return this.n;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public int getMapHeight() {
        return this.o;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public float getCameraAngle() {
        return o(this.af);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public boolean isMaploaded() {
        return this.am;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public MapConfig getMapConfig() {
        return this.g;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public View getView() throws RemoteException {
        return this.c;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void onIndoorBuildingActivity(int i, byte[] bArr) {
        ac acVar;
        if (bArr != null) {
            try {
                acVar = new ac();
                byte b2 = bArr[0];
                acVar.a = new String(bArr, 1, b2, Constants.UTF_8);
                int i2 = 1 + b2;
                int i3 = i2 + 1;
                byte b3 = bArr[i2];
                acVar.b = new String(bArr, i3, b3, Constants.UTF_8);
                int i4 = i3 + b3;
                int i5 = i4 + 1;
                byte b4 = bArr[i4];
                acVar.activeFloorName = new String(bArr, i5, b4, Constants.UTF_8);
                int i6 = i5 + b4;
                acVar.activeFloorIndex = GLConvertUtil.getInt(bArr, i6);
                int i7 = i6 + 4;
                int i8 = i7 + 1;
                byte b5 = bArr[i7];
                acVar.poiid = new String(bArr, i8, b5, Constants.UTF_8);
                int i9 = i8 + b5;
                int i10 = i9 + 1;
                byte b6 = bArr[i9];
                acVar.h = new String(bArr, i10, b6, Constants.UTF_8);
                int i11 = i10 + b6;
                acVar.c = GLConvertUtil.getInt(bArr, i11);
                int i12 = i11 + 4;
                acVar.floor_indexs = new int[acVar.c];
                acVar.floor_names = new String[acVar.c];
                acVar.d = new String[acVar.c];
                for (int i13 = 0; i13 < acVar.c; i13++) {
                    acVar.floor_indexs[i13] = GLConvertUtil.getInt(bArr, i12);
                    int i14 = i12 + 4;
                    int i15 = i14 + 1;
                    byte b7 = bArr[i14];
                    if (b7 > 0) {
                        acVar.floor_names[i13] = new String(bArr, i15, b7, Constants.UTF_8);
                        i15 += b7;
                    }
                    i12 = i15 + 1;
                    byte b8 = bArr[i15];
                    if (b8 > 0) {
                        acVar.d[i13] = new String(bArr, i12, b8, Constants.UTF_8);
                        i12 += b8;
                    }
                }
                acVar.e = GLConvertUtil.getInt(bArr, i12);
                int i16 = i12 + 4;
                if (acVar.e > 0) {
                    acVar.f = new int[acVar.e];
                    for (int i17 = 0; i17 < acVar.e; i17++) {
                        acVar.f[i17] = GLConvertUtil.getInt(bArr, i16);
                        i16 += 4;
                    }
                }
            } catch (Throwable th) {
                th.printStackTrace();
                return;
            }
        } else {
            acVar = null;
        }
        this.bq = acVar;
        a(new Runnable() { // from class: com.amap.api.mapcore.util.a.36
            @Override // java.lang.Runnable
            public void run() {
                if (a.this.bc != null) {
                    a.this.bc.a(a.this.bq);
                }
            }
        });
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public AMapCameraInfo getCamerInfo() {
        try {
            GLMapState gLMapStateB = b();
            float mapAngle = gLMapStateB.getMapAngle();
            float f = this.n / this.o;
            float fov = gLMapStateB.getFov();
            int[] iArr = new int[3];
            gLMapStateB.getCameraPosition(iArr);
            return new AMapCameraInfo(fov, f, mapAngle, iArr[0], iArr[1], iArr[2]);
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public MapTilsCacheAndResManager getMapCacheMgr() {
        GLMapEngine gLMapEngine;
        if (!this.aT || (gLMapEngine = this.l) == null) {
            return null;
        }
        return gLMapEngine.getMapCacheMgr();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void destroy() {
        this.ag = true;
        try {
            aj ajVar = this.q;
            if (ajVar != null) {
                ajVar.b();
            }
            LocationSource locationSource = this.ao;
            if (locationSource != null) {
                locationSource.deactivate();
            }
            this.ao = null;
            this.bc = null;
            GLMapRender gLMapRender = this.aJ;
            if (gLMapRender != null) {
                gLMapRender.renderPause();
            }
            Bitmap bitmap = this.G;
            if (bitmap != null) {
                bitmap.recycle();
                this.G = null;
            }
            Bitmap bitmap2 = this.F;
            if (bitmap2 != null) {
                bitmap2.recycle();
                this.F = null;
            }
            h hVar = this.m;
            if (hVar != null) {
                hVar.a((AMapGestureListener) null);
                this.m.b();
                this.m = null;
            }
            i iVar = this.f;
            if (iVar != null) {
                iVar.b();
            }
            p pVar = this.e;
            if (pVar != null) {
                pVar.i();
            }
            u uVar = this.d;
            if (uVar != null) {
                uVar.f();
            }
            y();
            Thread thread = this.aA;
            if (thread != null) {
                thread.interrupt();
                this.aA = null;
            }
            GLMapEngine gLMapEngine = this.l;
            if (gLMapEngine != null) {
                gLMapEngine.setMapListener(null);
                queueEvent(new Runnable() { // from class: com.amap.api.mapcore.util.a.37
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            GLES20.glDeleteTextures(3, new int[]{a.this.C, a.this.D, a.this.E}, 0);
                            a.this.p.a();
                            a.this.p = null;
                            a.this.l.destorySurface();
                            a.this.l = null;
                        } catch (Throwable th) {
                            th.printStackTrace();
                        }
                    }
                });
                Thread.sleep(200L);
            }
            ae aeVar = this.a;
            if (aeVar != null) {
                aeVar.b();
            }
            ej ejVar = this.c;
            if (ejVar != null) {
                ejVar.j();
                this.c = null;
            }
            co coVar = this.an;
            if (coVar != null) {
                coVar.b();
                this.an = null;
            }
            this.ao = null;
            D();
            this.az = null;
            fn.b();
        } catch (Throwable th) {
            fn.b(th, "AMapDelegateImp", "destroy");
            th.printStackTrace();
        }
    }

    private void D() {
        this.S = null;
        this.H = null;
        this.I = null;
        this.J = null;
        this.K = null;
        this.L = null;
        this.M = null;
        this.N = null;
        this.O = null;
        this.P = null;
        this.Q = null;
        this.R = null;
        this.T = null;
        this.U = null;
        this.V = null;
    }

    /* JADX INFO: compiled from: AMapDelegateImp.java */
    private class c implements eh.a {
        private c() {
        }

        @Override // com.amap.api.mapcore.util.eh.a
        public void a(int i) {
            if (a.this.h != null) {
                a.this.h.activeFloorIndex = a.this.h.floor_indexs[i];
                a.this.h.activeFloorName = a.this.h.floor_names[i];
                try {
                    a aVar = a.this;
                    aVar.setIndoorBuildingInfo(aVar.h);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* JADX INFO: compiled from: AMapDelegateImp.java */
    class b {
        b() {
        }

        public void a(ac acVar) {
            if (a.this.g.isIndoorEnable()) {
                final eh ehVarE = a.this.c.e();
                if (acVar == null) {
                    try {
                        if (a.this.R != null) {
                            a.this.R.OnIndoorBuilding(acVar);
                        }
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                    if (a.this.h != null) {
                        a.this.h.g = null;
                    }
                    if (ehVarE.d()) {
                        a.this.r.post(new Runnable() { // from class: com.amap.api.mapcore.util.a.b.1
                            @Override // java.lang.Runnable
                            public void run() {
                                ehVarE.a(false);
                            }
                        });
                    }
                    a.this.g.maxZoomLevel = a.this.g.isSetLimitZoomLevel() ? a.this.g.getMaxZoomLevel() : 19.0f;
                    try {
                        if (a.this.ab.isZoomControlsEnabled()) {
                            a.this.aj.invalidateZoomController(a.this.g.getS_z());
                            return;
                        }
                        return;
                    } catch (Throwable th2) {
                        th2.printStackTrace();
                    }
                }
                if (a.this.h != null && a.this.h.poiid.equals(acVar.poiid) && ehVarE.d()) {
                    return;
                }
                if (a.this.h == null || !a.this.h.poiid.equals(acVar.poiid) || a.this.h.g == null) {
                    a.this.h = acVar;
                    if (a.this.l != null) {
                        a.this.h.g = a.this.l.getMapCenter(1);
                    }
                }
                try {
                    if (a.this.R != null) {
                        a.this.R.OnIndoorBuilding(acVar);
                    }
                    a.this.g.maxZoomLevel = a.this.g.isSetLimitZoomLevel() ? a.this.g.getMaxZoomLevel() : 20.0f;
                    if (a.this.ab.isZoomControlsEnabled()) {
                        a.this.aj.invalidateZoomController(a.this.g.getS_z());
                    }
                    if (!a.this.ab.isIndoorSwitchEnabled()) {
                        if (a.this.ab.isIndoorSwitchEnabled() || !ehVarE.d()) {
                            return;
                        }
                        a.this.ab.setIndoorSwitchEnabled(false);
                        return;
                    }
                    if (!ehVarE.d()) {
                        a.this.ab.setIndoorSwitchEnabled(true);
                    }
                    a.this.r.post(new Runnable() { // from class: com.amap.api.mapcore.util.a.b.2
                        @Override // java.lang.Runnable
                        public void run() {
                            try {
                                ehVarE.a(a.this.h.floor_names);
                                ehVarE.a(a.this.h.activeFloorName);
                                if (ehVarE.d()) {
                                    return;
                                }
                                ehVarE.a(true);
                            } catch (Throwable th3) {
                                th3.printStackTrace();
                            }
                        }
                    });
                } catch (Throwable th3) {
                    th3.printStackTrace();
                }
            }
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMapListener
    public void beforeDrawLabel(int i, GLMapState gLMapState) {
        o();
        GLMapEngine gLMapEngine = this.l;
        if (gLMapEngine != null) {
            gLMapEngine.pushRendererState();
        }
        this.f.a(true, this.aD);
        GLMapEngine gLMapEngine2 = this.l;
        if (gLMapEngine2 != null) {
            gLMapEngine2.popRendererState();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMapListener
    public void afterDrawLabel(int i, GLMapState gLMapState) {
        o();
        GLMapEngine gLMapEngine = this.l;
        if (gLMapEngine != null) {
            gLMapEngine.pushRendererState();
        }
        this.d.b();
        this.f.a(false, this.aD);
        aj ajVar = this.q;
        if (ajVar != null) {
            ajVar.a(this.g, getViewMatrix(), getProjectionMatrix());
        }
        p pVar = this.e;
        if (pVar != null) {
            pVar.a();
        }
        cs csVar = this.Y;
        if (csVar != null) {
            csVar.b(getMapWidth(), getMapHeight());
        }
        GLMapEngine gLMapEngine2 = this.l;
        if (gLMapEngine2 != null) {
            gLMapEngine2.popRendererState();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMapListener
    public void afterDrawFrame(final int i, GLMapState gLMapState) {
        if (this.aX) {
            return;
        }
        this.aX = true;
        a(new Runnable() { // from class: com.amap.api.mapcore.util.a.38
            @Override // java.lang.Runnable
            public void run() {
                if (a.this.ai != null) {
                    a.this.ai.onSurfaceChanged(i, a.this.n, a.this.o);
                }
            }
        });
    }

    private void E() {
        Bitmap bitmap = this.F;
        if (bitmap == null || bitmap.isRecycled()) {
            this.F = dw.a(this.k, "amap_sdk_lineTexture.png");
        }
        Bitmap bitmap2 = this.G;
        if (bitmap2 == null || bitmap2.isRecycled()) {
            this.G = dw.a(this.k, "amap_sdk_lineDashTexture.png");
        }
        this.C = dw.a(this.F);
        this.D = dw.a(this.G, true);
        this.E = dw.a(512, 1024);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public long createGLOverlay(int i) {
        GLMapEngine gLMapEngine = this.l;
        if (gLMapEngine != null) {
            return gLMapEngine.createOverlay(1, i);
        }
        return 0L;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public long getGlOverlayMgrPtr() {
        GLMapEngine gLMapEngine = this.l;
        if (gLMapEngine != null) {
            return gLMapEngine.getGlOverlayMgrPtr(1);
        }
        return 0L;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public CrossOverlay addCrossVector(CrossOverlayOptions crossOverlayOptions) {
        if (crossOverlayOptions == null || crossOverlayOptions.getRes() == null) {
            return null;
        }
        CrossVectorOverlay crossVectorOverlay = new CrossVectorOverlay(1, u(), this);
        if (crossOverlayOptions != null) {
            crossVectorOverlay.setAttribute(crossOverlayOptions.getAttribute());
        }
        GLMapEngine gLMapEngine = this.l;
        if (gLMapEngine != null) {
            gLMapEngine.getOvelayBundle(1).addOverlay(crossVectorOverlay);
            crossVectorOverlay.resumeMarker(crossOverlayOptions.getRes());
        }
        return new CrossOverlay(crossOverlayOptions, crossVectorOverlay);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public RouteOverlay addNaviRouteOverlay() {
        if (!this.aT || this.l == null) {
            return null;
        }
        return new RouteOverlay(new BaseRouteOverlay(1, u(), this));
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void addOverlayTexture(int i, GLTextureProperty gLTextureProperty) {
        GLMapEngine gLMapEngine = this.l;
        if (gLMapEngine != null) {
            gLMapEngine.addOverlayTexture(i, gLTextureProperty);
            this.l.getOvelayBundle(1).addOverlayTextureItem(gLTextureProperty.mId, gLTextureProperty.mAnchor, gLTextureProperty.mXRatio, gLTextureProperty.mYRatio, gLTextureProperty.mBitmap.getWidth(), gLTextureProperty.mBitmap.getHeight());
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setCustomMapStylePath(String str) {
        if (TextUtils.isEmpty(str) || str.equals(this.g.getCustomStylePath())) {
            return;
        }
        this.g.setCustomStylePath(str);
        this.ac = true;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setCustomTextureResourcePath(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.g.setCustomTextureResourcePath(str);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public MyLocationStyle getMyLocationStyle() throws RemoteException {
        co coVar = this.an;
        if (coVar != null) {
            return coVar.a();
        }
        return null;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setMapCustomEnable(boolean z) {
        if (this.aT && !this.ag) {
            if (TextUtils.isEmpty(this.g.getCustomStylePath())) {
                return;
            }
            try {
                if (this.ac || (this.g.isCustomStyleEnable() ^ z)) {
                    this.g.setCustomStyleEnable(z);
                    this.ab.setLogoEnable(!z);
                    if (z) {
                        g(1, true);
                        da daVar = new da(this.k);
                        StyleItem[] styleItemArrA = daVar.a(this.g.getCustomStylePath());
                        this.g.setCustomBackgroundColor(daVar.a());
                        if (styleItemArrA != null && styleItemArrA.length > 0) {
                            a(1, 0, 0, 0, true, true, styleItemArrA);
                            dv.a(this.k, true);
                        } else {
                            dv.a(this.k, false);
                        }
                    } else {
                        g(1, false);
                        a(1, this.g.getMapStyleMode(), this.g.getMapStyleTime(), this.g.getMapStyleState(), true, false, (StyleItem[]) null);
                    }
                }
                this.ac = false;
                return;
            } catch (Throwable th) {
                th.printStackTrace();
                return;
            }
        }
        this.bh.b = true;
        this.bh.c = z;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void removeGLOverlay(BaseMapOverlay baseMapOverlay) {
        GLMapEngine gLMapEngine = this.l;
        if (gLMapEngine != null) {
            gLMapEngine.getOvelayBundle(1).removeOverlay(baseMapOverlay);
        }
    }

    @Override // com.amap.api.mapcore.util.k
    public float[] v() {
        float[] fArr;
        GLMapEngine gLMapEngine = this.l;
        if (gLMapEngine != null) {
            GLMapState mapState = gLMapEngine.getMapState(1);
            mapState.getViewMatrix(this.w);
            mapState.getProjectionMatrix(this.x);
            float[] fArr2 = this.w;
            if (fArr2 == null || (fArr = this.x) == null) {
                return this.v;
            }
            Matrix.multiplyMM(this.v, 0, fArr, 0, fArr2, 0);
        }
        return this.v;
    }

    @Override // com.amap.api.mapcore.util.k
    public String c(String str) {
        i iVar = this.f;
        if (iVar != null) {
            return iVar.a(str);
        }
        return null;
    }

    @Override // com.amap.api.mapcore.util.k
    public void h(boolean z) {
        if (this.ag) {
            return;
        }
        this.c.f(z);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public float[] getViewMatrix() {
        GLMapEngine gLMapEngine;
        GLMapState mapState;
        if (this.aT && this.aU && (gLMapEngine = this.l) != null && (mapState = gLMapEngine.getMapState(1)) != null) {
            mapState.getViewMatrix(this.w);
        }
        return this.w;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public float[] getProjectionMatrix() {
        GLMapEngine gLMapEngine;
        GLMapState mapState;
        if (this.aT && this.aU && (gLMapEngine = this.l) != null && (mapState = gLMapEngine.getMapState(1)) != null) {
            mapState.getProjectionMatrix(this.x);
        }
        return this.x;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void changeSurface(GL10 gl10, int i, int i2) {
        try {
            a(1, gl10, i, i2);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void createSurface(GL10 gl10, EGLConfig eGLConfig) {
        try {
            a(1, gl10, eGLConfig);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void renderSurface(GL10 gl10) {
        drawFrame(gl10);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public boolean canStopMapRender() {
        GLMapEngine gLMapEngine = this.l;
        if (gLMapEngine != null) {
            gLMapEngine.canStopMapRender(1);
        }
        return true;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void getLatLngRect(DPoint[] dPointArr) {
        try {
            Rectangle geoRectangle = this.g.getGeoRectangle();
            if (geoRectangle != null) {
                IPoint[] clipRect = geoRectangle.getClipRect();
                for (int i = 0; i < 4; i++) {
                    GLMapState.geo2LonLat(clipRect[i].x, clipRect[i].y, dPointArr[i]);
                }
            }
        } catch (Throwable unused) {
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void checkMapState(GLMapState gLMapState) {
        MapConfig mapConfig = this.g;
        if (mapConfig == null || this.ag) {
            return;
        }
        LatLngBounds limitLatLngBounds = mapConfig.getLimitLatLngBounds();
        try {
            if (limitLatLngBounds != null) {
                IPoint[] limitIPoints = this.g.getLimitIPoints();
                if (limitIPoints == null) {
                    IPoint iPointObtain = IPoint.obtain();
                    GLMapState.lonlat2Geo(limitLatLngBounds.northeast.longitude, limitLatLngBounds.northeast.latitude, iPointObtain);
                    IPoint iPointObtain2 = IPoint.obtain();
                    GLMapState.lonlat2Geo(limitLatLngBounds.southwest.longitude, limitLatLngBounds.southwest.latitude, iPointObtain2);
                    IPoint[] iPointArr = {iPointObtain, iPointObtain2};
                    this.g.setLimitIPoints(iPointArr);
                    limitIPoints = iPointArr;
                }
                float fB = dw.b(this.g, limitIPoints[0].x, limitIPoints[0].y, limitIPoints[1].x, limitIPoints[1].y, getMapWidth(), getMapHeight());
                float mapZoomer = gLMapState.getMapZoomer();
                if (this.g.isSetLimitZoomLevel()) {
                    float maxZoomLevel = this.g.getMaxZoomLevel();
                    float minZoomLevel = this.g.getMinZoomLevel();
                    if (fB < maxZoomLevel && fB > minZoomLevel) {
                        fB = Math.max(fB, Math.min(mapZoomer, maxZoomLevel));
                    }
                } else if (fB <= 0.0f || mapZoomer >= fB) {
                    fB = mapZoomer;
                }
                gLMapState.setMapZoomer(fB);
                IPoint iPointObtain3 = IPoint.obtain();
                gLMapState.getGeoCenter(iPointObtain3);
                int i = iPointObtain3.x;
                int i2 = iPointObtain3.y;
                int[] iArrA = dw.a(limitIPoints[0].x, limitIPoints[0].y, limitIPoints[1].x, limitIPoints[1].y, this.g, gLMapState, i, i2);
                if (iArrA != null && iArrA.length == 2) {
                    i = iArrA[0];
                    i2 = iArrA[1];
                }
                gLMapState.setMapGeoCenter(i, i2);
                iPointObtain3.recycle();
                return;
            }
            if (this.g.isSetLimitZoomLevel()) {
                gLMapState.setMapZoomer(Math.max(this.g.getMinZoomLevel(), Math.min(gLMapState.getMapZoomer(), this.g.getMaxZoomLevel())));
            }
        } catch (Throwable unused) {
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setRenderMode(int i) {
        l lVar = this.ad;
        if (lVar != null) {
            this.ah = i;
            lVar.setRenderMode(i);
        }
    }

    @Override // com.amap.api.mapcore.util.k
    public cv s(int i) {
        cw cwVar = this.p;
        if (cwVar == null) {
            return null;
        }
        return cwVar.a(i);
    }

    @Override // com.amap.api.mapcore.util.k
    public cw w() {
        return this.p;
    }

    @Override // com.amap.api.mapcore.util.k
    public void b(int i, int i2) {
        MapConfig mapConfig = this.g;
        if (mapConfig != null) {
            this.n = i;
            this.o = i2;
            mapConfig.setMapWidth(i);
            this.g.setMapHeight(i2);
        }
    }
}
