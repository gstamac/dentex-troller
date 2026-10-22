package com.amap.api.mapcore.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.opengl.GLES20;
import android.os.RemoteException;
import android.util.Log;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.TextOptions;
import com.autonavi.ae.gmap.GLMapState;
import com.autonavi.amap.mapcore.FPoint;
import com.autonavi.amap.mapcore.IPoint;
import com.autonavi.amap.mapcore.Rectangle;
import com.autonavi.amap.mapcore.interfaces.IMarkerAction;
import com.autonavi.amap.mapcore.interfaces.IOverlayImage;

/* JADX INFO: compiled from: TextDelegateImp.java */
/* JADX INFO: loaded from: classes.dex */
public class ct implements cl {
    private static int b;
    private int A;
    private int B;
    private int h;
    private Bitmap i;
    private int j;
    private int k;
    private String l;
    private LatLng m;
    private boolean p;
    private p q;
    private Object r;
    private String s;
    private int t;
    private int u;
    private int v;
    private Typeface w;
    private float x;
    private float c = 0.0f;
    private float d = 0.0f;
    private int e = 4;
    private int f = 32;
    private FPoint g = FPoint.obtain();
    private float n = 0.5f;
    private float o = 1.0f;
    private Rect y = new Rect();
    private Paint z = new Paint();
    private boolean C = false;
    private boolean D = false;
    private boolean E = false;
    int a = 9;
    private float[] F = {-1.0f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, -1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f};

    @Override // com.amap.api.mapcore.util.ci, com.autonavi.amap.mapcore.interfaces.IMarker
    public IMarkerAction getIMarkerAction() {
        return null;
    }

    @Override // com.amap.api.mapcore.util.ci
    public Rect h() {
        return null;
    }

    @Override // com.amap.api.mapcore.util.ci
    public boolean i() {
        return true;
    }

    @Override // com.amap.api.mapcore.util.ci, com.autonavi.amap.mapcore.interfaces.IMarker
    public boolean isInfoWindowShown() {
        return false;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public void setAnchor(float f, float f2) {
    }

    private static String a(String str) {
        b++;
        return str + b;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public void setRotateAngle(float f) {
        this.d = f;
        this.c = (((-f) % 360.0f) + 360.0f) % 360.0f;
        c();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public void destroy(boolean z) {
        try {
            this.D = true;
            if (z) {
                remove();
            }
            Bitmap bitmap = this.i;
            if (bitmap != null) {
                bitmap.recycle();
                this.i = null;
            }
            this.m = null;
            this.r = null;
        } catch (Throwable th) {
            fn.b(th, "TextDelegateImp", "destroy");
            th.printStackTrace();
            Log.d("destroy erro", "TextDelegateImp destroy");
        }
    }

    public ct(TextOptions textOptions, p pVar) throws RemoteException {
        this.p = true;
        this.q = pVar;
        if (textOptions.getPosition() != null) {
            this.m = textOptions.getPosition();
        }
        setAlign(textOptions.getAlignX(), textOptions.getAlignY());
        this.p = textOptions.isVisible();
        this.s = textOptions.getText();
        this.t = textOptions.getBackgroundColor();
        this.u = textOptions.getFontColor();
        this.v = textOptions.getFontSize();
        this.r = textOptions.getObject();
        this.x = textOptions.getZIndex();
        this.w = textOptions.getTypeface();
        this.l = getId();
        setRotateAngle(textOptions.getRotate());
        b();
        a();
    }

    private void b() {
        String str = this.s;
        if (str == null || str.trim().length() <= 0) {
            return;
        }
        try {
            this.z.setTypeface(this.w);
            this.z.setSubpixelText(true);
            this.z.setAntiAlias(true);
            this.z.setStrokeWidth(5.0f);
            this.z.setStrokeCap(Paint.Cap.ROUND);
            this.z.setTextSize(this.v);
            this.z.setTextAlign(Paint.Align.CENTER);
            this.z.setColor(this.u);
            Paint.FontMetrics fontMetrics = this.z.getFontMetrics();
            int i = (int) (fontMetrics.descent - fontMetrics.ascent);
            int i2 = (int) (((i - fontMetrics.bottom) - fontMetrics.top) / 2.0f);
            Paint paint = this.z;
            String str2 = this.s;
            paint.getTextBounds(str2, 0, str2.length(), this.y);
            Bitmap bitmapCreateBitmap = Bitmap.createBitmap(this.y.width() + 6, i, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmapCreateBitmap);
            canvas.drawColor(this.t);
            canvas.drawText(this.s, this.y.centerX() + 3, i2, this.z);
            this.i = bitmapCreateBitmap;
            this.j = bitmapCreateBitmap.getWidth();
            this.k = this.i.getHeight();
        } catch (Throwable th) {
            fn.b(th, "TextDelegateImp", "initBitmap");
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public synchronized boolean remove() {
        c();
        this.p = false;
        return this.q.a(this);
    }

    private void c() {
        if (this.q.c() != null) {
            this.q.c().setRunLowFrame(false);
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public LatLng getPosition() {
        return this.m;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public String getId() {
        if (this.l == null) {
            this.l = a("Text");
        }
        return this.l;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public void setPosition(LatLng latLng) {
        this.m = latLng;
        a();
        c();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public void setVisible(boolean z) {
        if (this.p == z) {
            return;
        }
        this.p = z;
        c();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public boolean isVisible() {
        return this.p;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public void setZIndex(float f) {
        this.x = f;
        this.q.f();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public float getZIndex() {
        return this.x;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public float getAnchorU() {
        return this.n;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public float getAnchorV() {
        return this.o;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public boolean equalsRemote(IOverlayImage iOverlayImage) throws RemoteException {
        return equals(iOverlayImage) || iOverlayImage.getId().equals(getId());
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public int hashCodeRemote() {
        return super.hashCode();
    }

    public boolean a() {
        if (this.m == null) {
            return false;
        }
        IPoint iPointObtain = IPoint.obtain();
        GLMapState.lonlat2Geo(this.m.longitude, this.m.latitude, iPointObtain);
        this.A = iPointObtain.x;
        this.B = iPointObtain.y;
        this.q.c().a(this.m.latitude, this.m.longitude, this.g);
        iPointObtain.recycle();
        return true;
    }

    private void b(k kVar, float[] fArr, int i, float f) throws RemoteException {
        float f2 = this.j * f;
        float f3 = this.k * f;
        float f4 = this.g.x;
        float f5 = this.g.y;
        float s_c = kVar.getMapConfig().getS_c();
        float[] fArr2 = this.F;
        int i2 = this.a;
        float f6 = this.n;
        fArr2[(i2 * 0) + 0] = f4 - (f2 * f6);
        float f7 = this.o;
        fArr2[(i2 * 0) + 1] = ((1.0f - f7) * f3) + f5;
        fArr2[(i2 * 0) + 2] = f4;
        fArr2[(i2 * 0) + 3] = f5;
        float f8 = this.c;
        fArr2[(i2 * 0) + 6] = f8;
        fArr2[(i2 * 0) + 7] = s_c;
        fArr2[(1 * i2) + 0] = f4 + ((1.0f - f6) * f2);
        fArr2[(1 * i2) + 1] = f5 + ((1.0f - f7) * f3);
        fArr2[(1 * i2) + 2] = f4;
        fArr2[(1 * i2) + 3] = f5;
        fArr2[(1 * i2) + 6] = f8;
        fArr2[(1 * i2) + 7] = s_c;
        fArr2[(2 * i2) + 0] = ((1.0f - f6) * f2) + f4;
        fArr2[(2 * i2) + 1] = f5 - (f3 * f7);
        fArr2[(2 * i2) + 2] = f4;
        fArr2[(2 * i2) + 3] = f5;
        fArr2[(2 * i2) + 6] = f8;
        fArr2[(2 * i2) + 7] = s_c;
        fArr2[(3 * i2) + 0] = f4 - (f2 * f6);
        fArr2[(3 * i2) + 1] = f5 - (f3 * f7);
        fArr2[(3 * i2) + 2] = f4;
        fArr2[(3 * i2) + 3] = f5;
        fArr2[(3 * i2) + 6] = f8;
        fArr2[(3 * i2) + 7] = s_c;
        System.arraycopy(fArr2, 0, fArr, i, fArr2.length);
    }

    @Override // com.amap.api.mapcore.util.ci
    public void a(k kVar, float[] fArr, int i, float f) {
        if (!this.p || this.D || this.m == null || this.i == null) {
            return;
        }
        this.g.x = this.A - kVar.getMapConfig().getS_x();
        this.g.y = this.B - kVar.getMapConfig().getS_y();
        try {
            b(kVar, fArr, i, f);
        } catch (Throwable th) {
            fn.b(th, "TextDelegateImp", "drawMarker");
        }
    }

    @Override // com.amap.api.mapcore.util.ci
    public void a(k kVar) {
        if (this.E) {
            return;
        }
        try {
            Bitmap bitmap = this.i;
            if (bitmap == null || bitmap.isRecycled()) {
                return;
            }
            if (this.h == 0) {
                this.h = d();
            }
            dw.b(this.h, this.i, false);
            this.E = true;
            this.i.recycle();
        } catch (Throwable th) {
            fn.b(th, "TextDelegateImp", "loadtexture");
            th.printStackTrace();
        }
    }

    private int d() {
        int[] iArr = {0};
        GLES20.glGenTextures(1, iArr, 0);
        return iArr[0];
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public void setObject(Object obj) {
        this.r = obj;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public Object getObject() {
        return this.r;
    }

    @Override // com.amap.api.mapcore.util.ci
    public int k() {
        try {
            return this.h;
        } catch (Throwable unused) {
            return 0;
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public float getRotateAngle() {
        return this.d;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IText
    public void setText(String str) throws RemoteException {
        this.s = str;
        e();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IText
    public String getText() throws RemoteException {
        return this.s;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IText
    public void setBackgroundColor(int i) throws RemoteException {
        this.t = i;
        e();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IText
    public int getBackgroundColor() throws RemoteException {
        return this.t;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IText
    public void setFontColor(int i) throws RemoteException {
        this.u = i;
        e();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IText
    public int getFontColor() throws RemoteException {
        return this.u;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IText
    public void setFontSize(int i) throws RemoteException {
        this.v = i;
        e();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IText
    public int getFontSize() throws RemoteException {
        return this.v;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IText
    public void setTypeface(Typeface typeface) throws RemoteException {
        this.w = typeface;
        e();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IText
    public Typeface getTypeface() throws RemoteException {
        return this.w;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IText
    public void setAlign(int i, int i2) throws RemoteException {
        this.e = i;
        if (i == 1) {
            this.n = 0.0f;
        } else if (i == 2) {
            this.n = 1.0f;
        } else if (i == 4) {
            this.n = 0.5f;
        } else {
            this.n = 0.5f;
        }
        this.f = i2;
        if (i2 == 8) {
            this.o = 0.0f;
        } else if (i2 == 16) {
            this.o = 1.0f;
        } else if (i2 == 32) {
            this.o = 0.5f;
        } else {
            this.o = 0.5f;
        }
        c();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IText
    public int getAlignX() throws RemoteException {
        return this.e;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IText
    public int getAlignY() {
        return this.f;
    }

    private synchronized void e() {
        b();
        this.E = false;
        c();
    }

    @Override // com.amap.api.mapcore.util.ci
    public boolean j() {
        Rectangle geoRectangle = this.q.c().getMapConfig().getGeoRectangle();
        return geoRectangle != null && geoRectangle.contains(this.A, this.B);
    }

    @Override // com.amap.api.mapcore.util.ci
    public void b(boolean z) {
        this.C = z;
    }

    @Override // com.amap.api.mapcore.util.ci
    public boolean l() {
        return this.C;
    }
}
