package com.amap.api.mapcore.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.RemoteException;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.animation.Animation;
import com.autonavi.ae.gmap.GLMapState;
import com.autonavi.amap.mapcore.FPoint;
import com.autonavi.amap.mapcore.IPoint;
import com.autonavi.amap.mapcore.animation.GLAnimation;
import com.autonavi.amap.mapcore.animation.GLTransformation;
import com.autonavi.amap.mapcore.interfaces.IInfoWindowManager;
import com.autonavi.amap.mapcore.interfaces.IMarkerAction;
import com.autonavi.amap.mapcore.interfaces.IOverlay;
import java.nio.Buffer;
import java.nio.FloatBuffer;

/* JADX INFO: compiled from: PopupOverlay.java */
/* JADX INFO: loaded from: classes.dex */
public class cs implements ad, ch, IInfoWindowManager {
    private int C;
    private GLAnimation J;
    private GLAnimation K;
    k a;
    a c;
    ae h;
    private Context i;
    private cf j;
    private FPoint p;
    private FloatBuffer t;
    private boolean w;
    private Bitmap x;
    private Bitmap y;
    private boolean k = false;
    private int l = 0;
    private int m = 0;
    private int n = 0;
    private int o = 0;
    private FloatBuffer q = null;
    private boolean s = true;
    private float u = 0.5f;
    private float v = 1.0f;
    private Rect z = new Rect();
    private float A = 0.0f;
    private float B = 0.0f;
    private boolean D = true;
    private Bitmap E = null;
    private Bitmap F = null;
    private Bitmap G = null;
    private Bitmap H = null;
    private boolean I = false;
    float[] b = new float[12];
    float[] d = {1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f};
    long e = 0;
    long f = Long.MAX_VALUE;
    long g = 100;
    private boolean L = false;
    private boolean M = true;
    private String r = getId();

    private void s() {
    }

    @Override // com.amap.api.mapcore.util.ch
    public boolean a() {
        return false;
    }

    @Override // com.amap.api.mapcore.util.ch
    public void c() throws RemoteException {
    }

    @Override // com.amap.api.mapcore.util.ch
    public boolean d() {
        return false;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public float getZIndex() {
        return 0.0f;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public boolean isAboveMaskLayer() {
        return false;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void remove() throws RemoteException {
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void setAboveMaskLayer(boolean z) {
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IInfoWindowManager
    public void setInfoWindowAnimation(Animation animation, Animation.AnimationListener animationListener) {
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IInfoWindowManager
    public void setInfoWindowBackColor(int i) {
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IInfoWindowManager
    public void setInfoWindowBackEnable(boolean z) {
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IInfoWindowManager
    public void setInfoWindowBackScale(float f, float f2) {
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IInfoWindowManager
    public void setInfoWindowMovingAnimation(Animation animation) {
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void setZIndex(float f) {
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IInfoWindowManager
    public void startAnimation() {
    }

    public boolean e() {
        return this.D;
    }

    public void a(boolean z) {
        this.D = z;
    }

    public void a(Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled()) {
            return;
        }
        c(this.E);
        this.E = bitmap;
    }

    private synchronized void c(Bitmap bitmap) {
        if (bitmap != null) {
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
    }

    private void d(Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled()) {
            return;
        }
        c(this.F);
        this.F = bitmap;
    }

    private void e(Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled()) {
            return;
        }
        c(this.G);
        this.G = bitmap;
    }

    private void f(Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled()) {
            return;
        }
        c(this.H);
        this.H = bitmap;
    }

    private Bitmap l() {
        return this.E;
    }

    private Bitmap m() {
        return this.G;
    }

    public cs(k kVar, Context context) {
        this.i = context;
        this.a = kVar;
    }

    public int f() {
        try {
            return this.x.getWidth();
        } catch (Throwable unused) {
            return 0;
        }
    }

    public int g() {
        try {
            return this.x.getHeight();
        } catch (Throwable unused) {
            return 0;
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public String getId() {
        if (this.r == null) {
            this.r = "PopupOverlay";
        }
        return this.r;
    }

    public synchronized void b(Bitmap bitmap) {
        if (bitmap != null) {
            try {
                if (!bitmap.isRecycled()) {
                    Bitmap bitmap2 = this.x;
                    if (bitmap2 != null && bitmap2.hashCode() == bitmap.hashCode()) {
                        return;
                    }
                    Bitmap bitmap3 = this.x;
                    if (bitmap3 != null && ((this.E == null && this.F == null && this.G == null && this.H == null) || !g(bitmap3))) {
                        c(this.y);
                        this.y = this.x;
                    }
                    this.I = false;
                    this.x = bitmap;
                }
            } catch (Throwable unused) {
            }
        }
    }

    private boolean g(Bitmap bitmap) {
        if (this.E != null && bitmap.hashCode() == this.E.hashCode()) {
            return true;
        }
        if (this.G != null && bitmap.hashCode() == this.G.hashCode()) {
            return true;
        }
        if (this.F == null || bitmap.hashCode() != this.F.hashCode()) {
            return this.H != null && bitmap.hashCode() == this.H.hashCode();
        }
        return true;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void setVisible(boolean z) {
        if (!this.s && z) {
            this.w = true;
        }
        this.s = z;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public boolean isVisible() {
        return this.s;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public boolean equalsRemote(IOverlay iOverlay) throws RemoteException {
        return equals(iOverlay) || iOverlay.getId().equals(getId());
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public int hashCodeRemote() {
        return super.hashCode();
    }

    public boolean a(int i, int i2) {
        Bitmap bitmap;
        GLMapState gLMapStateB = this.a.b();
        if (this.p != null && gLMapStateB != null) {
            IPoint iPointObtain = IPoint.obtain();
            gLMapStateB.map2Win(this.p.x, this.p.y, iPointObtain);
            int iF = f();
            int iG = g();
            int i3 = (int) ((iPointObtain.x + this.l) - (iF * this.u));
            int i4 = (int) (iPointObtain.y + this.m + (iG * (1.0f - this.v)));
            iPointObtain.recycle();
            if (i3 - iF > i || i3 < (-iF) * 2 || i4 < (-iG) * 2 || i4 - iG > i2 || (bitmap = this.x) == null) {
                return false;
            }
            int width = bitmap.getWidth();
            int height = this.x.getHeight();
            if (this.t == null) {
                this.t = dw.a(new float[]{0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f});
            }
            int i5 = (int) (((double) (1.0f - this.A)) * 0.5d * ((double) width));
            int i6 = i3 + i5;
            float f = i6;
            this.b[0] = f;
            this.z.left = i6;
            float[] fArr = this.b;
            int i7 = i2 - i4;
            float f2 = i7;
            fArr[1] = f2;
            fArr[2] = 0.0f;
            int i8 = i3 + width;
            float f3 = i8 - i5;
            fArr[3] = f3;
            fArr[4] = f2;
            this.z.top = i4 - height;
            float[] fArr2 = this.b;
            fArr2[5] = 0.0f;
            fArr2[6] = f3;
            this.z.right = i8;
            float f4 = i7 + height;
            this.b[7] = f4;
            this.z.bottom = i4;
            float[] fArr3 = this.b;
            fArr3[8] = 0.0f;
            fArr3[9] = f;
            fArr3[10] = f4;
            fArr3[11] = 0.0f;
            FloatBuffer floatBuffer = this.q;
            if (floatBuffer == null) {
                this.q = dw.a(fArr3);
            } else {
                this.q = dw.a(fArr3, floatBuffer);
            }
            return true;
        }
        return false;
    }

    public void h() {
        this.c = new a("texture.glsl");
    }

    /* JADX INFO: compiled from: PopupOverlay.java */
    static class a extends cv {
        int a;
        int b;
        int c;

        a(String str) {
            if (a(str)) {
                this.a = c("aMVP");
                this.b = b("aVertex");
                this.c = b("aTextureCoord");
            }
        }
    }

    private void a(int i, FloatBuffer floatBuffer, FloatBuffer floatBuffer2) {
        if (floatBuffer == null || floatBuffer2 == null || i == 0) {
            return;
        }
        if (this.c == null) {
            h();
        }
        this.c.a();
        GLES20.glEnable(3042);
        GLES20.glBlendFunc(1, 771);
        GLES20.glBlendColor(1.0f, 1.0f, 1.0f, 1.0f);
        GLES20.glBindTexture(3553, i);
        GLES20.glEnableVertexAttribArray(this.c.b);
        GLES20.glVertexAttribPointer(this.c.b, 3, 5126, false, 12, (Buffer) floatBuffer);
        GLES20.glEnableVertexAttribArray(this.c.c);
        GLES20.glVertexAttribPointer(this.c.c, 2, 5126, false, 8, (Buffer) floatBuffer2);
        GLES20.glUniformMatrix4fv(this.c.a, 1, false, this.d, 0);
        GLES20.glDrawArrays(6, 0, 4);
        GLES20.glDisableVertexAttribArray(this.c.b);
        GLES20.glDisableVertexAttribArray(this.c.c);
        GLES20.glBindTexture(3553, 0);
        GLES20.glUseProgram(0);
        GLES20.glDisable(3042);
    }

    public void b(int i, int i2) {
        if (!this.s || this.p == null || this.x == null) {
            return;
        }
        i();
        this.x.isRecycled();
        if (!this.I && !this.x.isRecycled()) {
            try {
                int i3 = this.C;
                if (i3 != 0) {
                    GLES20.glDeleteTextures(1, new int[]{i3}, 0);
                } else {
                    this.C = o();
                }
                Bitmap bitmap = this.x;
                if (bitmap != null && !bitmap.isRecycled()) {
                    dw.b(this.C, this.x, false);
                    this.I = true;
                }
            } catch (Throwable th) {
                fn.b(th, "PopupOverlay", "drawMarker");
                th.printStackTrace();
                return;
            }
        }
        n();
        if (a(i, i2)) {
            Matrix.setIdentityM(this.d, 0);
            Matrix.orthoM(this.d, 0, 0.0f, i, 0.0f, i2, 1.0f, -1.0f);
            a(this.C, this.q, this.t);
            if (this.w) {
                this.w = false;
                s();
            }
        }
    }

    protected void i() {
        long jC;
        ae aeVar = this.h;
        if (aeVar != null) {
            jC = aeVar.c();
            if (jC <= 0) {
                jC = this.f;
            } else {
                long j = this.g;
                if (jC <= j) {
                    jC = j;
                }
            }
        } else {
            jC = 0;
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        long j2 = this.e;
        if (jCurrentTimeMillis - j2 > jC) {
            if (j2 != 0) {
                try {
                    a(this.j);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            this.e = jCurrentTimeMillis;
        }
    }

    private void n() {
        GLAnimation gLAnimation;
        if (!this.M && (gLAnimation = this.K) != null && !gLAnimation.hasEnded()) {
            this.L = true;
            GLTransformation gLTransformation = new GLTransformation();
            this.K.getTransformation(AnimationUtils.currentAnimationTimeMillis(), gLTransformation);
            if (Double.isNaN(gLTransformation.scale_x) || Double.isNaN(gLTransformation.scale_y)) {
                return;
            }
            this.A = (float) gLTransformation.scale_x;
            this.B = (float) gLTransformation.scale_y;
            return;
        }
        GLAnimation gLAnimation2 = this.J;
        if (gLAnimation2 != null && !gLAnimation2.hasEnded()) {
            this.M = false;
            this.L = true;
            this.l = this.n;
            this.m = this.o;
            GLTransformation gLTransformation2 = new GLTransformation();
            this.J.getTransformation(AnimationUtils.currentAnimationTimeMillis(), gLTransformation2);
            if (Double.isNaN(gLTransformation2.scale_x) || Double.isNaN(gLTransformation2.scale_y)) {
                return;
            }
            this.A = (float) gLTransformation2.scale_x;
            this.B = (float) gLTransformation2.scale_y;
            return;
        }
        this.A = 1.0f;
        this.B = 1.0f;
        this.L = false;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IInfoWindowManager
    public void setInfoWindowAppearAnimation(Animation animation) {
        GLAnimation gLAnimation = this.K;
        if (gLAnimation != null && gLAnimation.equals(animation.glAnimation)) {
            try {
                this.J = animation.glAnimation.mo46clone();
                return;
            } catch (Throwable th) {
                fn.b(th, "PopupOverlay", "setInfoWindowDisappearAnimation");
                return;
            }
        }
        this.J = animation.glAnimation;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IInfoWindowManager
    public void setInfoWindowDisappearAnimation(Animation animation) {
        GLAnimation gLAnimation = this.J;
        if (gLAnimation != null && gLAnimation.equals(animation.glAnimation)) {
            try {
                this.K = animation.glAnimation.mo46clone();
                return;
            } catch (Throwable th) {
                fn.b(th, "PopupOverlay", "setInfoWindowDisappearAnimation");
                return;
            }
        }
        this.K = animation.glAnimation;
    }

    private int o() {
        int[] iArr = {0};
        GLES20.glGenTextures(1, iArr, 0);
        return iArr[0];
    }

    public void c(int i, int i2) throws RemoteException {
        if (this.L) {
            this.n = i;
            this.o = i2;
        } else {
            this.l = i;
            this.m = i2;
            this.n = i;
            this.o = i2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(boolean z) {
        if (z) {
            b(l());
        } else {
            b(m());
        }
    }

    private void c(final boolean z) {
        GLAnimation gLAnimation = this.K;
        if (gLAnimation != null) {
            this.M = false;
            this.L = true;
            gLAnimation.startNow();
            this.K.setAnimationListener(new Animation.AnimationListener() { // from class: com.amap.api.mapcore.util.cs.1
                @Override // com.amap.api.maps.model.animation.Animation.AnimationListener
                public void onAnimationStart() {
                }

                @Override // com.amap.api.maps.model.animation.Animation.AnimationListener
                public void onAnimationEnd() {
                    if (cs.this.J != null) {
                        cs.this.L = true;
                        cs.this.J.startNow();
                        cs.this.b(z);
                    }
                }
            });
            return;
        }
        GLAnimation gLAnimation2 = this.J;
        if (gLAnimation2 != null) {
            this.L = true;
            gLAnimation2.startNow();
            b(z);
            return;
        }
        b(z);
    }

    private void p() {
        if (this.D && this.x != null) {
            c(false);
        } else {
            b(m());
        }
        a(false);
    }

    private void q() {
        if (!this.D && this.x != null) {
            c(true);
        } else {
            b(l());
        }
        a(true);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void destroy() {
        if (this.k) {
            try {
                remove();
                r();
                FloatBuffer floatBuffer = this.t;
                if (floatBuffer != null) {
                    floatBuffer.clear();
                    this.t = null;
                }
                FloatBuffer floatBuffer2 = this.q;
                if (floatBuffer2 != null) {
                    floatBuffer2.clear();
                    this.q = null;
                }
                this.p = null;
                this.C = 0;
            } catch (Throwable th) {
                fn.b(th, "PopupOverlay", "realDestroy");
                th.printStackTrace();
            }
        }
    }

    private void r() {
        Bitmap bitmap = this.x;
        if (bitmap != null && bitmap != null) {
            bitmap.recycle();
            this.x = null;
        }
        Bitmap bitmap2 = this.y;
        if (bitmap2 != null && !bitmap2.isRecycled()) {
            this.y.recycle();
            this.y = null;
        }
        Bitmap bitmap3 = this.E;
        if (bitmap3 != null && !bitmap3.isRecycled()) {
            this.E.recycle();
        }
        Bitmap bitmap4 = this.F;
        if (bitmap4 != null && !bitmap4.isRecycled()) {
            this.F.recycle();
        }
        Bitmap bitmap5 = this.G;
        if (bitmap5 != null && !bitmap5.isRecycled()) {
            this.G.recycle();
        }
        Bitmap bitmap6 = this.H;
        if (bitmap6 == null || bitmap6.isRecycled()) {
            return;
        }
        this.H.recycle();
    }

    public void a(FPoint fPoint) {
        this.p = fPoint;
    }

    public boolean j() {
        return this.L;
    }

    @Override // com.amap.api.mapcore.util.ad
    public synchronized void a(cf cfVar) throws RemoteException {
        if (cfVar == null) {
            return;
        }
        if (cfVar.isInfoWindowEnable()) {
            cf cfVar2 = this.j;
            if (cfVar2 != null && !cfVar2.getId().equals(cfVar.getId())) {
                a_();
            }
            if (this.h != null) {
                this.j = cfVar;
                cfVar.a(true);
                setVisible(true);
                k();
            }
        }
    }

    protected void k() {
        View viewB;
        try {
            Marker marker = new Marker(this.j);
            ae aeVar = this.h;
            if (aeVar != null) {
                Bitmap bitmapA = a(aeVar.a(marker));
                if (bitmapA == null && (viewB = this.h.b(marker)) != null) {
                    if (viewB.getBackground() == null) {
                        viewB.setBackground(this.h.g());
                    }
                    bitmapA = a(viewB);
                }
                a(bitmapA);
                d(a(this.h.c(marker)));
                e(a(this.h.d(marker)));
                f(a(this.h.e(marker)));
            }
        } catch (Throwable th) {
            fn.b(th, "PopupOverlay", "getInfoWindow");
            th.printStackTrace();
        }
    }

    private Bitmap a(View view) {
        if (view == null) {
            return null;
        }
        if ((view instanceof RelativeLayout) && this.i != null) {
            LinearLayout linearLayout = new LinearLayout(this.i);
            linearLayout.setOrientation(1);
            linearLayout.addView(view);
            view = linearLayout;
        }
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(0);
        return dw.a(view);
    }

    private Rect t() {
        return new Rect(this.z.left, this.z.top, this.z.right, this.z.top + v());
    }

    private Rect u() {
        return new Rect(this.z.left, this.z.top, this.z.right, this.z.top + w());
    }

    private int v() {
        Bitmap bitmap = this.E;
        if (bitmap == null || bitmap.isRecycled()) {
            return 0;
        }
        return this.E.getHeight();
    }

    private int w() {
        Bitmap bitmap = this.G;
        if (bitmap == null || bitmap.isRecycled()) {
            return 0;
        }
        return this.G.getHeight();
    }

    @Override // com.amap.api.mapcore.util.ad
    public void b() {
        try {
            cf cfVar = this.j;
            if (cfVar != null && cfVar.j()) {
                setVisible(true);
                Rect rectH = this.j.h();
                int iE = this.j.e() + this.j.c();
                int iF = this.j.f() + this.j.d() + 2;
                if (j()) {
                    if (this.x != null) {
                        return;
                    }
                    if (this.E == null && this.G == null) {
                        return;
                    }
                }
                IMarkerAction iMarkerAction = this.j.getIMarkerAction();
                if (iMarkerAction != null && !iMarkerAction.isInfoWindowEnable()) {
                    setVisible(false);
                    return;
                }
                setVisible(true);
                if (iMarkerAction != null && iMarkerAction.isInfoWindowAutoOverturn()) {
                    Rect rectT = t();
                    Rect rectU = u();
                    if (e()) {
                        rectU.offset(0, rectH.height() + rectT.height() + 2);
                    } else {
                        rectT.offset(0, -(rectH.height() + rectT.height() + 2));
                    }
                    int iA = this.a.a(iMarkerAction, rectT);
                    int iA2 = this.a.a(iMarkerAction, rectU);
                    if (iA > 0 && (iA2 == 0 || (iA2 > 0 && iA < iA2))) {
                        iF = this.j.f() + this.j.d() + 2 + rectH.height() + rectU.height();
                        p();
                    } else {
                        q();
                    }
                    a(this.j.a());
                    c(iE, iF);
                    return;
                }
                a(this.j.a());
                c(iE, iF);
                q();
                return;
            }
            setVisible(false);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    @Override // com.amap.api.mapcore.util.ad
    public boolean a(MotionEvent motionEvent) {
        return this.s && this.j != null && dw.a(this.z, (int) motionEvent.getX(), (int) motionEvent.getY());
    }

    @Override // com.amap.api.mapcore.util.ad
    public synchronized void a_() {
        setVisible(false);
        r();
    }

    @Override // com.amap.api.mapcore.util.ad
    public void a(ae aeVar) {
        this.h = aeVar;
    }
}
